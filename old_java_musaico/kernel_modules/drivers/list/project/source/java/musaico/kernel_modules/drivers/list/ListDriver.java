package musaico.kernel_modules.drivers.list;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import musaico.buffer.Buffer;
import musaico.buffer.BufferException;
import musaico.buffer.BufferTools;

import musaico.field.Attribute;
import musaico.field.Field;
import musaico.field.FieldTypingEnvironment;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Filter;
import musaico.io.FilterState;
import musaico.io.Order;
import musaico.io.Path;
import musaico.io.Reference;
import musaico.io.ReferenceCount;
import musaico.io.SimpleReferenceCount;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.common.oentries.SimpleOEntry;

import musaico.kernel.common.swappers.block.BlockSwapState;
import musaico.kernel.common.swappers.block.BufferBlockSwapper;
import musaico.kernel.common.swappers.block.BufferBlockSwapSystem;
import musaico.kernel.common.swappers.block.SimpleBlockPage;

import musaico.kernel.driver.BlockDriver;
import musaico.kernel.driver.Driver;
import musaico.kernel.driver.DriverIdentifier;
import musaico.kernel.driver.DriverException;
import musaico.kernel.driver.EjectableDriver;
import musaico.kernel.driver.SchedulableDriver;
import musaico.kernel.driver.SpecialDriver;
import musaico.kernel.driver.SuspendableDriver;
import musaico.kernel.driver.TransactionalDriver;
import musaico.kernel.driver.TransactionException;

import musaico.kernel.memory.Memory;
import musaico.kernel.memory.MemoryIdentifier;
import musaico.kernel.memory.MemoryPermission;
import musaico.kernel.memory.MemoryRequestListener;
import musaico.kernel.memory.Segment;
import musaico.kernel.memory.SegmentFactory;

import musaico.kernel.memory.paging.KernelPaging;
import musaico.kernel.memory.paging.Page;
import musaico.kernel.memory.paging.SimpleSwapConfiguration;
import musaico.kernel.memory.paging.SwapConfiguration;
import musaico.kernel.memory.paging.Swapper;
import musaico.kernel.memory.paging.SwapState;
import musaico.kernel.memory.paging.SwapSystem;

import musaico.kernel.memory.paging.kernelpagings.SimpleKernelPaging;

import musaico.kernel.memory.paging.buffer.BufferPage;
import musaico.kernel.memory.paging.buffer.BufferSwapState;
import musaico.kernel.memory.paging.buffer.SimpleBufferPage;

import musaico.kernel.memory.physical.PhysicalMemory;
import musaico.kernel.memory.physical.SimplePhysicalMemory;

import musaico.kernel.memory.security.NoSegmentSecurity;

import musaico.kernel.memory.segments.DumbMemoryRequestListener;
import musaico.kernel.memory.segments.SimpleSegmentFactory;

import musaico.kernel.memory.virtual.VirtualMemory;
import musaico.kernel.memory.virtual.SimpleVirtualMemory;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleException;
import musaico.kernel.module.FakeModule;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.PollRequest;
import musaico.kernel.objectsystem.PollResponse;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordPermission;
import musaico.kernel.objectsystem.RecordPermissions;

import musaico.kernel.objectsystem.cursors.SimpleCursor;

import musaico.kernel.objectsystem.onode.OEntry;
import musaico.kernel.objectsystem.onode.ONode;

import musaico.kernel.task.Scheduler;

import musaico.kernel.types.SimpleKernelTypingEnvironment;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Size;
import musaico.region.Space;
import musaico.region.SparseRegionBuilder;

import musaico.region.array.ArrayPosition;
import musaico.region.array.ArraySpace;

import musaico.security.Credentials;
import musaico.security.NoSecurity;
import musaico.security.Permissions;
import musaico.security.Security;

import musaico.state.Node;


/**
 * <p>
 * A driver which provides access to an in-memory
 * list of Fields.
 * </p>
 *
 *
 * <br> </br>
 * <br> </br>
 *
 * <hr> </hr>
 *
 * <br> </br>
 * <br> </br>
 *
 *
 * <pre>
 * Copyright (c) 2011, 2012 Johann Tienhaara
 * All rights reserved.
 *
 * This file is part of Musaico.
 *
 * Musaico is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Musaico is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Musaico.  If not, see &lt;http://www.gnu.org/licenses/&gt;.
 * </pre>
 */
public class ListDriver
    implements Driver, BlockDriver, EjectableDriver, SchedulableDriver, SpecialDriver, SuspendableDriver, TransactionalDriver
{
    /** The master list driver id. */
    public static final DriverIdentifier DRIVER_ID =
        new DriverIdentifier ( "list" );


    /** Special command: sort the fields in the list
     *                   by the specified order. */
    public static final Reference SPECIAL_SORT_COMMAND =
        new SimpleSoftReference<String> ( "sort" );


    /** Synchronize all transactions and writes on this token: */
    private final Serializable lock = new String ();

    /** The number of references to this record. */
    private final ReferenceCount referenceCount =
        new SimpleReferenceCount ();

    /** The Module of which this list driver is a part.
     *  Provides logger, field typing environment, and so on. */
    private final Module module;

    /** The list. */
    private final List<Field> list;

    /** Unique identifier for this list driver. */
    private final DriverIdentifier id;

    /** The security rules for the list. */
    private final Security<RecordFlag> security;

    /** The region describing the list. */
    private Region region;


    /**
     * <p>
     * Creates a new ListDriver for an empty list.  The list can
     * grow as large as the underlying ArrayList implementation
     * allows.
     * </p>
     *
     * @param module The module which loaded this ListDriver
     *               and provides logging, field typing, and so
     *               on for this driver.  Must not be null.
     *
     * @param security The security for the new list driver.
     *                 Must not be null.
     */
    public ListDriver (
                       Module module,
                       Security<RecordFlag> security
                       )
    {
        this ( module, security, new ArrayList<Field> () );
    }


    /**
     * <p>
     * Creates a new ListDriver for the specified list.
     * </p>
     *
     * @param module The module which loaded this ListDriver
     *               and provides logging, field typing, and so
     *               on for this driver.  Must not be null.
     *
     * @param security The security for the new list driver.
     *                 Must not be null.
     *
     * @param list The list to manage with this driver.
     *             Must not be null.
     */
    public ListDriver (
                       Module module,
                       Security<RecordFlag> security,
                       List<Field> list
                       )
    {
        if ( module == null
             || security == null
             || list == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a new ListDriver with module [%module%] security [%security%] list [%list%]",
                                                     "module", module,
                                                     "security", security,
                                                     "list", list );
        }

        this.module = module;
        this.security = security;

        this.id = ListDriver.DRIVER_ID; // !!!

        this.list = list;

        this.calculateRegion ();
    }


    /**
     * <p>
     * Figures out the Region describing the underlying list.
     * </p>
     */
    protected void calculateRegion ()
    {
        long list_size = (long) this.list.size ();
        if ( list_size == 0L )
        {
            // Size is 0, but start and end points are both 0
            // (not -1 like a regular empty array space region).
            this.region = ArraySpace.STANDARD.empty ();
        }
        else
        {
            this.region =
                ArraySpace.STANDARD.region ( ArraySpace.STANDARD.position ( 0L ),
                                             ArraySpace.STANDARD.position ( (long) list_size - 1L ) );
        }
    }


    /**
     * @see musaico.kernel.driver.Driver#autoConfigure()
     */
    @Override
    public Driver autoConfigure ()
        throws DriverException
    {
        return this;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#close(musaico.objectsystem.Cursor)
     */
    @Override
    public void close (
                       Cursor cursor
                       )
        throws RecordOperationException
    {
        final long reference_count = this.referenceCount.decrement ();
    }


    /**
     * @see musaico.kernel.driver.Driver#configure(musaico.buffer.Buffer)
     */
    @Override
    public Driver configure (
                             Buffer configuration_buffer
                             )
        throws DriverException
    {
        return this;
    }


    /**
     * @see musaico.kernel.driver.EjectableDriver#eject()
     */
    @Override
    public void eject ()
        throws DriverException
    {
    }


    /**
     * @see musaico.kernel.driver.BlockDriver#flush()
     */
    @Override
    public void flush ()
        throws DriverException
    {
        // Nothing to flush.
    }


    /**
     * @see musaico.kernel.driver.Driver#id()
     */
    @Override
    public DriverIdentifier id ()
    {
        return this.id;
    }


    /**
     * @see musaico.kernel.driver.Driver#initialize()
     */
    @Override
    public void initialize ()
        throws DriverException
    {
    }


    /**
     * @see musaico.kernel.driver.EjectableDriver#insert()
     */
    @Override
    public void insert ()
        throws DriverException
    {
    }


    /**
     * @see musaico.kernel.objectsystem.Record#mmap(musaico.objectsystem.Cursor,musaico.kernel.memory.Segment)
     */
    @Override
    public void mmap (
                      Cursor cursor,
                      Segment segment
                      )
        throws RecordOperationException
    {
        // TODO !!! what to do here?
        throw new RecordOperationException ( "!!! MMAP NOT YET IMPLEMENTED" );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#open(musaico.objectsystem.Cursor)
     */
    @Override
    public void open (
                      Cursor cursor
                      )
        throws RecordOperationException
    {
        cursor.position ( this.region ( cursor ).start () );

        final long reference_count = this.referenceCount.increment ();
    }


    /**
     * @see musaico.kernel.objectsystem.Record#poll(musaico.kernel.objectsystem.Cursor,musaico.kernel.objectsystem.PollRequest,musaico.kernel.objectsystem.PollResponse)
     */
    @Override
    public void poll (
                      Cursor cursor,
                      PollRequest request,
                      PollResponse response
                      )
        throws RecordOperationException,
               I18nIllegalArgumentException
    {
        throw new RecordOperationException ( "!!! POLLING HAS NOT YET BEEN IMPLEMENTED!" );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#read(musaico.objectsystem.Cursor,musaico.buffer.Buffer,musaico.io.Region)
     */
    @Override
    public Region read (
                        Cursor cursor,
                        Buffer read_fields,
                        Region read_into_region
                        )
        throws RecordOperationException
    {
        final Position driver_start;
        int array_index;
        SparseRegionBuilder actual_buffer_regions_builder = null;
        Space space = cursor.record ().space ();
        Size one = space.one ();
        synchronized ( this.lock )
        {
            Position from_position = cursor.position ();
            long index = ( (ArrayPosition) from_position ).index ();
            if ( index < 0L
                 || index >= this.list.size () )
            {
                throw new DriverException ( "!!! BLA BLA BLA index = " + index + " vs list size = " + this.list.size () + " for position " + from_position );
            }

            array_index = (int) index;

            driver_start = this.region.start ();
            for ( Position to_position : read_into_region )
            {
                if ( array_index >= this.list.size () )
                {
                    // Stop reading, we went past the end of the list.
                    break;
                }

                Field field = this.list.get ( array_index );

                try
                {
                    read_fields.set ( to_position, field );
                }
                catch ( BufferException e )
                {
                    throw new DriverException ( "!!! BLA BLA BLA 4", e );
                }

                final Region one_position =
                    space.region ( to_position, to_position );
                if ( actual_buffer_regions_builder == null )
                {
                    // Start creating the sparse region.
                    actual_buffer_regions_builder =
                        space.sparseRegionBuilder ()
                        .concatenate ( one_position );
                }
                else
                {
                    actual_buffer_regions_builder =
                        actual_buffer_regions_builder.concatenate ( one_position );
                }

                array_index ++;
            }
        }

        final Region actual_buffer_region;
        if ( actual_buffer_regions_builder == null )
        {
            actual_buffer_region = space.empty ();
        }
        else
        {
            actual_buffer_region = actual_buffer_regions_builder.build ();
        }

        return actual_buffer_region;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#region(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public Region region (
                          Cursor cursor
                          )
        throws RecordOperationException
    {
        return this.region;
    }


    /**
     * @see musaico.kernel.driver.SuspendableDriver#resume()
     */
    @Override
    public void resume ()
        throws DriverException
    {
    }


    /**
     * @see musaico.kernel.driver.SchedulableDriver#scheduler(musaico.kernel.task.Scheduler)
     */
    @Override
    public void scheduler (
                           Scheduler scheduler
                           )
        throws DriverException
    {
        // !!! not needed for now.
    }


    /**
     * @see musaico.kernel.driver.BlockDriver#sectorReadAhead(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public Size sectorReadAhead (
                                 Cursor cursor
                                 )
        throws DriverException
    {
        // No point in reading ahead for in-memory data.
        // Just read on demand.
        return ArraySpace.STANDARD.none ();
    }


    /**
     * @see musaico.kernel.driver.BlockDriver#sectorSize(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public Size sectorSize (
                            Cursor cursor
                            )
        throws DriverException
    {
        return ArraySpace.STANDARD.size ( 4096L ); // !!!
    }


    /**
     * @see musaico.kernel.objectsystem.Record#security()
     */
    @Override
    public Security<RecordFlag> security ()
    {
        return this.security;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#seek(musaico.objectsystem.Cursor,musaico.io.Position)
     */
    @Override
    public Position seek (
                          Cursor cursor,
                          Position position
                          )
        throws RecordOperationException
    {
        Region region = this.region ( cursor );
        final Position found_position;
        if ( region.contains ( position ) )
        {
            found_position = position;
        }
        else
        {
            // !!! OR SHOULD WE JUST GROW THE LIST????
            found_position = region.space ().outOfBounds ();
        }

        cursor.position ( found_position );
        return found_position;
    }


    /**
     * @see musaico.kernel.driver.Driver#shutdown()
     */
    @Override
    public void shutdown ()
        throws DriverException
    {
        this.flush ();
    }


    /**
     * @see musaico.kernel.objectsystem.Record#space()
     */
    @Override
    public Space space ()
    {
        return this.region.space ();
    }


    /**
     * @see musaico.kernel.driver.SpecialDriver#special(musaico.kernel.objectsystem.Cursor,musaico.io.Reference,musaico.field.Field[])
     */
    @Override
    public void special (
                         Cursor cursor,
                         Reference special_command,
                         Field ... special_parameters
                         )
        throws I18nIllegalArgumentException,
               DriverException
    {
        if ( cursor == null
             || special_command == null
             || special_parameters == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot execute special command with cursor [%cursor%] special command [%special_command%] parameters [%special_parameters%]",
                                                     "cursor", cursor,
                                                     "special_command", special_command,
                                                     "special_parameters", special_parameters );
        }

        try
        {
            if ( special_command.equals ( ListDriver.SPECIAL_SORT_COMMAND )
                 && special_parameters.length == 1 )
            {
                Order<Field> order = (Order<Field>)
                    special_parameters [ 0 ].value ( Order.class );
                Collections.sort ( this.list, order );
            }
            else
            {
                throw new I18nIllegalArgumentException ( "Cannot execute special command with cursor [%cursor%] special command [%special_command%] parameters [%special_parameters%]",
                                                         "cursor", cursor,
                                                         "special_command", special_command,
                                                         "special_parameters", special_parameters );
            }
        }
        catch ( RuntimeException e )
        {
            throw new DriverException ( "Failed to execute special command for cursor [%cursor%] special command [%special_command%] parameters [%special_parameters%]",
                                        "cursor", cursor,
                                        "special_command", special_command,
                                        "special_parameters", special_parameters,
                                        "cause", e );
        }
    }


    /**
     * @see musaico.kernel.driver.SpecialDriver#specialCommands()
     */
    @Override
    public Reference [] specialCommands ()
    {
        return new Reference [] { ListDriver.SPECIAL_SORT_COMMAND };
    }


    /**
     * @see musaico.kernel.driver.SpecialDriver#specialParameters(musaico.io.Reference)
     */
    @Override
    public Attribute<?> [] specialParameters (
                                              Reference special_command
                                              )
    {
        if ( ListDriver.SPECIAL_SORT_COMMAND.equals ( special_command ) )
        {
            return new Attribute<?> []
                {
                    new Attribute<Order> ( "order",
                                           Order.class,
                                           new ArrayPosition ( 0L ) )
                };
        }
        else
        {
            throw new I18nIllegalArgumentException ( "Unknown special command [%special_command%]",
                                                     "special_command", special_command );
        }
    }


    /**
     * @see musaico.kernel.driver.Driver#state()
     */
    @Override
    public Reference state ()
    {
        return Driver.INITIALIZED;
    }


    /**
     * @see musaico.kernel.driver.SuspendableDriver#suspend()
     */
    @Override
    public void suspend ()
        throws DriverException
    {
    }


    /**
     * @see musaico.kernel.objectsystem.Record#sync(musaico.objectsystem.Cursor,boolean)
     */
    @Override
    public void sync (
                      Cursor cursor,
                      boolean is_metadata_only
                      )
        throws RecordOperationException
    {
        this.flush (); // TODO !!! ???
    }


    /**
     * @see musaico.kernel.driver.TransactionalDriver#transactionCommit(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public void transactionCommit (
                                   Cursor cursor
                                   )
        throws TransactionException
    {
    }


    /**
     * @see musaico.kernel.driver.TransactionalDriver#transactionPrepareCommit(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public void transactionPrepareCommit (
                                          Cursor cursor
                                          )
        throws TransactionException
    {
    }


    /**
     * @see musaico.kernel.driver.TransactionalDriver#transactionRollback(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public void transactionRollback (
                                     Cursor cursor
                                     )
        throws TransactionException
    {
    }


    /**
     * @see musaico.kernel.driver.TransactionalDriver#transactionStart(musaico.kernel.objectsystem.Cursor,musaico.buffer.Buffer)
     */
    @Override
    public void transactionStart (
                                  Cursor cursor,
                                  Buffer transaction_configuration
                                  )
        throws TransactionException
    {
    }


    /**
     * @see musaico.kernel.objectsystem.Record#write(musaico.objectsystem.Cursor,musaico.buffer.Buffer,musaico.io.Region)
     */
    @Override
    public Region write (
                         Cursor cursor,
                         Buffer write_fields,
                         Region write_from_region
                         )
        throws RecordOperationException
    {
        final Position driver_start;
        int array_index;
        SparseRegionBuilder actual_buffer_regions_builder = null;
        Space space = cursor.record ().space ();
        Size one = space.one ();
        synchronized ( this.lock )
        {
            Position to_position = cursor.position ();
            long index = ( (ArrayPosition) to_position ).index ();
            if ( index < 0L
                 || index > Integer.MAX_VALUE )
            {
                throw new DriverException ( "!!! BLA BLA BLA 3 index = " + index + " for position " + to_position );
            }

            array_index = (int) index;

            driver_start = this.region.start ();
            for ( Position from_position : write_from_region )
            {
                Field field = write_fields.get ( from_position );
                if ( array_index < this.list.size () )
                {
                    this.list.remove ( array_index );
                    this.list.add ( array_index, field );
                }
                else
                {
                    // Fill in gap with nulls if necessary.
                    for ( int l = list.size (); l < array_index; l ++ )
                    {
                        this.list.add ( null );
                    }

                    this.list.add ( field );
                }

                final Region one_position =
                    space.region ( to_position, to_position );
                if ( actual_buffer_regions_builder == null )
                {
                    // Start creating the sparse region.
                    actual_buffer_regions_builder =
                        space.sparseRegionBuilder ()
                        .concatenate ( one_position );
                }
                else
                {
                    actual_buffer_regions_builder =
                        actual_buffer_regions_builder.concatenate ( one_position );
                }

                array_index ++;
            }
        }

        final Region actual_buffer_region;
        if ( actual_buffer_regions_builder == null )
        {
            actual_buffer_region = space.empty ();
        }
        else
        {
            actual_buffer_region = actual_buffer_regions_builder.build ();
        }

        // Now re-calculate the list region.
        this.calculateRegion ();

        return actual_buffer_region;
    }


    /**
     * <p>
     * Outputs some details useful for debugging.
     * </p>
     */
    public String toDebugString ()
    {
        return "List driver " + this + ": " + this.list;
    }





    public static void start (
                              Module module
                              )
        throws ModuleException
    {
        System.out.println ( "Starting list driver" );

        try
        {

            // Create a list driver.
            ListDriver driver = new ListDriver ( module,
                                                 new NoSecurity<RecordFlag> () // !!!
                                                 );

            // Register it in the kernel.
            module.createKernelObject ( ListDriver.DRIVER_ID,
                                        driver );

            // Now create an entry in the /dev object system.
            // ...!!!


            Credentials me = module.credentials ();

            Region buffer_region =
                ArraySpace.STANDARD.region ( ArraySpace.STANDARD.position ( 0L ),
                                             ArraySpace.STANDARD.position ( 15L ) );

            Buffer buffer1 =
                module.physicalMemory ().allocate ( me, buffer_region );
            Position position = buffer1.region ().start ();
            FieldTypingEnvironment env = module.types ();
            buffer1.set ( position, env.create ( "firstName", "Johann" ) );
            position = buffer1.region ().expr ( position ).next ();
            buffer1.set ( position, env.create ( "lastName", "Tienhaara" ) );
            position = buffer1.region ().expr ( position ).next ();
            buffer1.set ( position, env.create ( "occupation", "Lazy layabout" ) );

            OEntry entry = SimpleOEntry.NO_SUCH_OENTRY;
            Permissions<RecordFlag> permissions =
                new RecordPermissions ( me,
                                        driver.id (),
                                        RecordPermission.READ,
                                        RecordPermission.WRITE,
                                        RecordPermission.APPEND );
            Cursor cursor = new SimpleCursor ( entry,
                                               permissions,
                                               driver );

            cursor.record ().open ( cursor );

            cursor.record ().write ( cursor, buffer1,
                                     buffer1.region () );

            Buffer buffer2 =
                module.physicalMemory ().allocate ( me, buffer_region );
            cursor.position ( driver.region ( cursor ).start () );
            cursor.record ().read ( cursor, buffer2,
                                    buffer2.region () );

            System.out.println ( "" );
            System.out.println ( driver.toDebugString () );
            System.out.println ( "" );
            System.out.println ( "Wrote it from buffer:     " + BufferTools.toString ( buffer2 ) );
            System.out.println ( "" );
            System.out.println ( "Read it into buffer:      " + BufferTools.toString ( buffer2 ) );
            System.out.println ( "" );

        }
        catch ( Throwable t )
        {
            throw new ModuleException ( "Could not start ListDriver module",
                                        "cause", t );
        }

        System.out.println ( "Done starting list driver" );
    }

    public static void stop (
                             Module module
                             )
        throws ModuleException
    {
        System.out.println ( "Stopping list driver" );

        module.deleteKernelObject ( ListDriver.DRIVER_ID );

        System.out.println ( "Done stopping list driver" );
    }


    public static void main ( String [] args )
        throws Exception
    {
        System.out.println ( "RUNNING LIST DRIVER..." );
        Module fake_module = new FakeModule ();
        Credentials me = fake_module.credentials ();
        ListDriver driver = new ListDriver ( fake_module,
                                             new NoSecurity<RecordFlag> () );
        FieldTypingEnvironment env = new SimpleKernelTypingEnvironment ();
        Region buffer_region =
            ArraySpace.STANDARD.region ( ArraySpace.STANDARD.position ( 0L ),
                                         ArraySpace.STANDARD.position ( 15L ) );
        Security<MemoryPermission> memory_security =
            new NoSecurity<MemoryPermission> (); // !!!
        PhysicalMemory physical_memory =
            new SimplePhysicalMemory ( MemoryIdentifier.PHYSICAL_FOR_KERNEL_MODULES,
                                       memory_security,
                                       env, 0L ); // unlimited alloc

        Buffer buffer1 = physical_memory.allocate ( me, buffer_region );
        Position position = buffer1.region ().start ();
        buffer1.set ( position, env.create ( "firstName", "Johann" ) );
        position = buffer1.region ().expr ( position ).next ();
        buffer1.set ( position, env.create ( "lastName", "Tienhaara" ) );
        position = buffer1.region ().expr ( position ).next ();
        buffer1.set ( position, env.create ( "occupation", "Lazy layabout" ) );
        position = buffer1.region ().expr ( position ).next ();
        position = buffer1.region ().expr ( position ).next ();
        buffer1.set ( position, env.create ( "skipped a slot", 42 ) );

        OEntry entry = SimpleOEntry.NO_SUCH_OENTRY;
        Permissions<RecordFlag> permissions =
            new RecordPermissions ( me,
                                    driver.id (),
                                    RecordPermission.READ,
                                    RecordPermission.WRITE,
                                    RecordPermission.APPEND );
        Cursor cursor = new SimpleCursor ( entry,
                                           permissions,
                                           driver );

        cursor.record ().open ( cursor );

        Region region =
            ArraySpace.STANDARD.region ( ArraySpace.STANDARD.position ( 0L ),
                                         ArraySpace.STANDARD.position ( 4L ) );

        cursor.record ().write ( cursor, buffer1, region );

        Buffer buffer2 = physical_memory.allocate ( me, buffer_region );
        cursor.position ( driver.region ( cursor ).start () );
        cursor.record ().read ( cursor, buffer2, region );

        System.out.println ( "" );
        System.out.println ( driver.toDebugString () );
        System.out.println ( "" );
        System.out.println ( "Wrote it from buffer:     " + BufferTools.toString ( buffer2 ) );
        System.out.println ( "" );
        System.out.println ( "Read it into buffer:      " + BufferTools.toString ( buffer2 ) );
        System.out.println ( "" );

        Filter<Position> my_filter = new Filter<Position> ()
            {
                public FilterState filter ( Position position )
                {
                    Space space = position.space ();
                    Size distance_from_origin = space.expr ( position )
                        .subtract ( space.origin () ).size ();
                    long index = (long)
                        space.expr ( distance_from_origin )
                        .ratio ( space.one () );

                    if ( ( index % 2L ) == 1L )
                    {
                        return FilterState.KEEP;
                    }
                    else
                    {
                        return FilterState.DISCARD;
                    }
                }
            };

        region = buffer_region.space ().expr ( buffer_region )
            .filter ( my_filter ).region ();
        System.out.println ( "Crazy filtered region: " + region );
        Buffer buffer3 = physical_memory.allocate ( me, buffer_region );
        cursor.position ( driver.region ( cursor ).start () );
        cursor.record ().read ( cursor, buffer3, region );
        System.out.println ( "Filtered it into buffer:  " + BufferTools.toString ( buffer3 ) );

        cursor.record ().close ( cursor );


        KernelPaging kernel_paging = new SimpleKernelPaging ();
        BlockSwapState swapped_out_to_block_driver =
            new BlockSwapState ( new SimpleSoftReference<String> ( "swapped_out_to_block_driver" ),
                                 kernel_paging,
                                 ArraySpace.STANDARD.size ( 16L ) );
        BufferSwapState swapped_in_to_fields =
            new BufferSwapState ( new SimpleSoftReference<String> ( "swapped_in_to_fields" ),
                                  physical_memory,
                                  kernel_paging,
                                  ArraySpace.STANDARD.size ( 32L ) );

        // Now try some swappin'.
        BlockSwapState swapped_out = swapped_out_to_block_driver;
        BufferSwapState swapped_in = swapped_in_to_fields;
        BufferBlockSwapper swapper =
            new BufferBlockSwapper ( swapped_out,
                                     swapped_in );
        SwapSystem swap_system = new BufferBlockSwapSystem ( swapper );
        Size sector_size = driver.sectorSize ( cursor );
        Position block_start = swapped_out.space ().origin ();
        Position block_end = swapped_out.space ().expr ( block_start )
            .add ( sector_size )
            .previous ();
        Region block_region =
            swapped_out.space ().region ( block_start, block_end );
        Page out_page = new SimpleBlockPage ( kernel_paging,
                                              swapped_out,
                                              driver,
                                              block_region );
        BufferPage in_page =
            new SimpleBufferPage ( me,
                                   physical_memory,
                                   kernel_paging,
                                   swapped_in,
                                   block_region );
        swapper.readIn ( me,
                         out_page, out_page.region (),
                         in_page, in_page.region () );
        System.out.println ( "Swapped into buffer:      " + BufferTools.toString ( in_page.buffer () ) );

        // Change the buffer then swap it back out.
        Position buffer_start = in_page.region ().start ();
        position = buffer_start;
        for ( long value = 0L; value < 16L; value ++ )
        {
            final Field field;
            if ( ( value % 3L ) == 0L )
            {
                field = env.create ( "number", value );
            }
            else
            {
                field = env.create ( "string", "" + value );
            }

            in_page.buffer ().set ( position, field );

            position = position.space ().expr ( position ).next ();
        }

        swapper.writeOut ( me,
                           in_page, in_page.region (),
                           out_page, out_page.region () );

        Buffer buffer4 = physical_memory.allocate ( me, buffer_region );
        cursor.record ().open ( cursor );
        cursor.record ().read ( cursor, buffer4,
                                buffer_region );
        cursor.record ().close ( cursor );
        System.out.println ( "Swapped out:              " + BufferTools.toString ( buffer4 ) );


        // Try out the whole shebang, with a proper
        // PagedArea and virtual memory and everything just
        // shy of an object system.
        Swapper [] swappers = new Swapper []
        {
            swapper
        };
        MemoryRequestListener segment_request_executor =
            new DumbMemoryRequestListener ();

	// Create the configuration settings for the swap states.
	Map<SwapState,Field> swap_state_fields =
	    new HashMap<SwapState,Field> ();
	Field block_driver_field = env.create ( "block_driver",
						Object.class,
						driver );
	swap_state_fields.put ( swapped_out_to_block_driver,
				block_driver_field );
	SwapConfiguration swap_state_configurations =
	    new SimpleSwapConfiguration ( swap_state_fields );

        SegmentFactory segment_factory =
            new SimpleSegmentFactory ( fake_module.kernelObjects (),
                                       kernel_paging,
                                       swap_system,
				       swap_state_configurations,
                                       segment_request_executor );
        VirtualMemory virtual_memory =
            new SimpleVirtualMemory ( MemoryIdentifier.VIRTUAL_MEMORY_FOR_USERS,
                                      memory_security,
                                      env,
                                      segment_factory,
                                      new NoSegmentSecurity () );
        Buffer virtual_buffer = virtual_memory.allocate ( me, buffer_region );
        String buffer_contents = BufferTools.toString ( virtual_buffer );
        System.out.println ( "Hopefully swapped in a virtual buffer: " + buffer_contents + " virtual buffer region: " + virtual_buffer.region () );



        tempDebug ( fake_module, driver, virtual_buffer, swappers );
    }


    public static void tempDebug (
                                  Module module,
                                  ListDriver driver,
                                  Buffer virtual_buffer,
                                  Swapper [] swappers
                                  )
        throws Exception
    {
        OEntry entry = SimpleOEntry.NO_SUCH_OENTRY;
        Credentials me = module.credentials ();
        Permissions<RecordFlag> permissions =
            new RecordPermissions ( me,
                                    driver.id (),
                                    RecordPermission.READ,
                                    RecordPermission.WRITE,
                                    RecordPermission.APPEND );
        Cursor cursor = new SimpleCursor ( entry,
                                           permissions,
                                           driver );

        cursor.record ().open ( cursor );
        System.out.println ( "List driver whole region: " + cursor.record ().region ( cursor ) );
        System.out.println ( "List driver sector size: " + driver.sectorSize ( cursor ) );
        cursor.record ().close ( cursor );

        System.out.println ( "Virtual buffer region: " + virtual_buffer.region () );
        for ( int s = 0; s < swappers.length; s ++ )
        {
            SwapState swapped_out = swappers [ s ].outState ();
            SwapState swapped_in = swappers [ s ].inState ();
            System.out.println ( "Swapper " + s + " out " + swapped_out + " size: " + swapped_out.pageSize () );
            System.out.println ( "Swapper " + s + " in  " + swapped_in + " size: " + swapped_in.pageSize () );
        }
    }
}
