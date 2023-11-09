package musaico.kernel_modules.drivers.excel;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import org.apache.poi.ss.usermodel.Workbook;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Filter;
import musaico.io.FilterState;
import musaico.io.Path;
import musaico.io.Position;
import musaico.io.Progress;
import musaico.io.Reference;
import musaico.io.ReferenceCount;
import musaico.io.SimpleReferenceCount;
import musaico.io.Region;
import musaico.io.Size;
import musaico.io.UnknownRegion;

import musaico.buffer.Buffer;
import musaico.buffer.BufferException;
import musaico.buffer.BufferTools;

import musaico.field.Field;
import musaico.field.FieldTypingEnvironment;

import musaico.io.markers.RecordEnd;
import musaico.io.markers.RecordStart;

import musaico.io.positions.EndPosition;
import musaico.io.positions.NoSuchPosition;
import musaico.io.positions.OffsetFromPosition;
import musaico.io.positions.StartPosition;

import musaico.io.progresses.NoProgress;

import musaico.io.regions.FixedLengthRegion;
import musaico.io.regions.SectionedRegion;

import musaico.io.references.SimpleSoftReference;

import musaico.io.sizes.Length;

import musaico.kernel.driver.BlockDriver;
import musaico.kernel.driver.Driver;
import musaico.kernel.driver.DriverIdentifier;
import musaico.kernel.driver.DriverException;
import musaico.kernel.driver.EjectableDriver;
import musaico.kernel.driver.SchedulableDriver;
import musaico.kernel.driver.SpecialDriver;
import musaico.kernel.driver.SuspendableDriver;
import musaico.kernel.driver.TransactionalDriver;

import musaico.kernel.memory.KernelPaging;
import musaico.kernel.memory.Memory;
import musaico.kernel.memory.Page;
import musaico.kernel.memory.PageFactory;
import musaico.kernel.memory.Segment;
import musaico.kernel.memory.SegmentFactory;
import musaico.kernel.memory.SimplePageFactory;
import musaico.kernel.memory.Swapper;
import musaico.kernel.memory.SwapState;
import musaico.kernel.memory.SwapStates;

import musaico.kernel.memory.kernelpagings.SimpleKernelPaging;

import musaico.kernel.memory.pages.BufferPage;
import musaico.kernel.memory.pages.BufferPageFactory;
import musaico.kernel.memory.pages.SimpleBufferPage;

import musaico.kernel.memory.physical.PhysicalMemory;
import musaico.kernel.memory.physical.SimplePhysicalMemory;

import musaico.kernel.memory.security.NoSegmentSecurity;

import musaico.kernel.memory.segments.SimpleSegmentFactory;

import musaico.kernel.memory.virtual.VirtualMemory;
import musaico.kernel.memory.virtual.SimpleVirtualMemory;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleException;
import musaico.kernel.module.FakeModule;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.OEntry;
import musaico.kernel.objectsystem.ONode;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordPermission;
import musaico.kernel.objectsystem.RecordPermissions;

import musaico.kernel.objectsystem.cursors.SimpleCursor;

import musaico.kernel.oentries.SimpleOEntry;

import musaico.kernel.task.Scheduler;

import musaico.kernel.types.SimpleKernelTypingEnvironment;

import musaico.security.Credentials;
import musaico.security.Permissions;

import musaico.state.Node;


/**
 * <p>
 * A driver which provides access to Microsoft (tm) Excel (tm)
 * spreadsheets via the Apache (tm) POI (tm) library.
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
 * Copyright (c) 2011 Johann Tienhaara
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
public class ExcelDriver
    implements Driver, BlockDriver
{
    /** The master Excel driver id. */
    public static final DriverIdentifier DRIVER_ID =
        new DriverIdentifier ( "excel" );


    /** The toplevel, master ExcelDriver.  It does not
     *  point to a spreadsheet, but all other drivers do. */
    private final ExcelDriver root;

    /** Synchronize all transactions and writes on this token: */
    private final Serializable lock = new String ();

    /** The children ExcelDrivers of the root (unconfigurable)
     *  ExcelDriver.  Every time someone configures an ExcelDriver,
     *  we create a new instance and put it in this list inside
     *  the root ExcelDriver. */
    private final List<ExcelDriver> rootChildren;

    /** The number of references to this record. */
    private final ReferenceCount referenceCount =
        new SimpleReferenceCount ();

    /** The number of times open () has been called with read
     *  mode. */
    private final ReferenceCount readers =
        new SimpleReferenceCount ();

    /** The number of times open () has been called with write
     *  mode. */
    private final ReferenceCount writers =
        new SimpleReferenceCount ();

    /** The Module of which this Excel driver is a part.
     *  Provides logger, field typing environment, and so on. */
    private final Module module;

    /** The region describing the size and layout of the
     *  Excel driver. */
    private final FixedLengthRegion region;

    /** The Excel/POI workbook (except for the root ExcelDriver,
     *  which does not point to a spreadsheet). */
    private final Workbook workbook;

    /** Unique identifier for this Excel driver. */
    private final DriverIdentifier id;


    /**
     * <p>
     * Creates a new root ExcelDriver.  When configure () is called,
     * instances of ExcelDriver are created and added to the object
     * system beneath /dev/excel.
     * </p>
     *
     * <p>
     * Multiple root ExcelDrivers can be created, but it's pointless
     * to do so.
     * </p>
     *
     * @param module The module which loaded this ExcelDriver
     *               and provides logging, field typing, and so
     *               on for this driver.  Must not be null.
     */
    public ExcelDriver (
                        Module module
                        )
    {
        if ( module == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a new ExcelDriver with module [%module%]",
                                                     "module", module );
        }

        this.root = this;
        this.module = module;
        this.rootChildren = new ArrayList<ExcelDriver> ();
        this.id = ExcelDriver.DRIVER_ID;
        this.region = new FixedLengthRegion ( this.id, 0L );
        this.workbook = null;
    }


    /**
     * <p>
     * Creates a copy of the root driver.
     * </p>
     */
    protected ExcelDriver (
                           Module module,
                           ExcelDriver root_excel_driver,
                           Workbook workbook
                           )
    {
        if ( module == null
             || root_excel_driver == null
             || workbook == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a new ExcelDriver with module [%module%] root ExcelDriver [%root_excel_driver%] workbook [%workbook%]",
                                                     "module", module,
                                                     "root_excel_driver", root_excel_driver,
                                                     "workbook", workbook );
        }

        this.root = root_excel_driver;
        this.module = module;
        this.rootChildren = null;
        this.id = new DriverIdentifier ( "excel/" + workbook );
        this.region = new FixedLengthRegion ( this.id,
                                              !!! );
        this.workbook = workbook;
    }


    /**
     * @see musaico.kernel.driver.Driver#autoConfigure()
     */
    @Override
    public Driver autoConfigure ()
        throws DriverException
    {
        throw new DriverException ( "ExcelDriver cannot be autoConfigured" );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#close(musaico.objectsystem.Cursor,musaico.objectsystem.ONode,musaico.io.Progress)
     */
    @Override
    public void close (
                       Cursor cursor,
                       ONode onode,
                       Progress progress
                       )
        throws RecordOperationException
    {
        IOException io_exception = null;
        synchronized ( this.lock )
        {
            final long reference_count = this.referenceCount.decrement ();

            if ( cursor.permissions ().isAllowed ( RecordPermission.READ ) )
            {
                final long num_readers = this.readers.decrement ();
                if ( num_readers == 0L )
                {
                    try
                    {
                        this.streamIn.close ();
                        this.streamIn = null;
                    }
                    catch ( IOException e )
                    {
                        // Throw the first IOException later.
                        if ( io_exception == null )
                        {
                            io_exception = e;
                        }
                    }
                }
            }

            if ( cursor.permissions ().isAllowed ( RecordPermission.WRITE ) )
            {
                final long num_writers = this.writers.decrement ();
                if ( num_writers == 0L )
                {
                    try
                    {
                        this.streamOut.flush ();
                        this.streamOut.close ();
                        this.streamOut = null;
                    }
                    catch ( IOException e )
                    {
                        // Throw the first IOException later.
                        if ( io_exception == null )
                        {
                            io_exception = e;
                        }
                    }
                }
            }
        }

        if ( io_exception != null )
        {
            throw new RecordOperationException ( "Could not close Excel driver [%excel_driver%] cursor [%cursor%]",
                                                 "excel_driver", this,
                                                 "cursor", cursor,
                                                 "cause", io_exception );
        }
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
        if ( this.workbook != null )
        {
            // Send it to the parent to create a new driver.
            return this.root.configure ( configuration_buffer );
        }

        // This is the root driver.  We'll create a new instance.
        // !!! Probably need to have a generic StreamDriver which FileDriver
        // !!! implements (not to mention SocketDriver etc).
        // !!! Also need to decide how to implement driver stacking.
        if ( readable ) --> InputStream input_stream = ...;
        Workbook workbook = WorkbookFactory.create ( input_stream );
        ExcelDriver new_instance = new ExcelDriver ( this.module,
                                                     this,
                                                     workbook );
        !!!;
        final String filename;
        try
        {
            filename =
                BufferTools.findAndGet ( configuration_buffer,
                                         configuration_buffer.region ().start (),
                                         "filename" )
                .value ( String.class );
        }
        catch ( Exception e ) // NullPointer, type exceptions
        {
            throw new DriverException ( "No filename specified while configuring FileDriver [%file_driver%]: configuration options [%configuration_buffer%]",
                                        "file_driver", this,
                                        "configuration_buffer", configuration_buffer );
        }

        FileDriver configured_file_driver =
            new FileDriver ( this.module,
                             this,
                             new File ( filename ) );

        synchronized ( this.lock )
        {
            this.rootChildren.add ( configured_file_driver );
        }

        return configured_file_driver;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#fcntl(musaico.objectsystem.Cursor,musaico.io.Progress,musaico.io.Reference,musaico.buffer.Buffer)
     */
    @Override
    public Serializable fcntl (
                               Cursor cursor,
                               Progress progress,
                               Reference fcntl_command,
                               Buffer fcntl_args
                               )
        throws RecordOperationException
    {
        throw new RecordOperationException ( "Driver [%driver%] does not support fcntl command [%fcntl_command%] ( [%fcntl_args%] )",
                                             "driver", this,
                                             "fcntl_command", fcntl_command,
                                             "fcntl_args", fcntl_args );
    }


    /**
     * @see musaico.kernel.driver.BlockDriver#flush(musaico.io.Progress)
     */
    @Override
    public void flush (
                       Progress progress
                       )
        throws DriverException
    {
        !!!;
        if ( this.streamOut != null )
        {
            try
            {
                this.streamOut.flush ();
            }
            catch ( IOException e )
            {
                throw new DriverException ( "Failed to flush ExcelDriver [%excel_driver%]",
                                            "excel_driver", this,
                                            "cause", e );
            }
        }
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
     * @see musaico.kernel.objectsystem.Record#ioctl(musaico.objectsystem.Cursor,musaico.objectsystem.ONode,musaico.io.Progress,musaico.io.Reference,musaico.buffer.Buffer)
     */
    @Override
    public Serializable ioctl (
                               Cursor cursor,
                               ONode onode,
                               Progress progress,
                               Reference ioctl_command,
                               Buffer ioctl_args
                               )
        throws RecordOperationException
    {
        throw new RecordOperationException ( "Driver [%driver%] does not support ioctl command [%ioctl_command%] ( [%ioctl_args%] )",
                                             "driver", this,
                                             "ioctl_command", ioctl_command,
                                             "ioctl_args", ioctl_args );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#mmap(musaico.objectsystem.Cursor,musaico.io.Progress,musaico.kernel.memory.Segment)
     */
    @Override
    public void mmap (
                      Cursor cursor,
                      Progress progress,
                      Segment segment
                      )
        throws RecordOperationException
    {
        // TODO !!! what to do here?
        throw new RecordOperationException ( "!!! MMAP NOT YET IMPLEMENTED" );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#open(musaico.objectsystem.Cursor,musaico.objectsystem.ONode,musaico.io.Progress)
     */
    @Override
    public void open (
                      Cursor cursor,
                      ONode onode,
                      Progress progress
                      )
        throws RecordOperationException
    {
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!:
        IOException io_exception = null;
        synchronized ( this.lock )
        {
            cursor.position ( this.region ( cursor ).start () );

            final long reference_count = this.referenceCount.increment ();

            if ( cursor.permissions ().isAllowed ( RecordPermission.WRITE ) )
            {
                final long num_writers = this.writers.increment ();
                if ( num_writers == 1L )
                {
                    try
                    {
                        this.file.getParentFile ().mkdirs ();
                        if( ! this.file.exists () )
                        {
                            this.file.createNewFile ();
                        }

                        if ( ! this.file.canWrite () )
                        {
                            throw new IOException ( "Cannot write file" );
                        }

                        this.streamOut = new FileOutputStream ( this.file );
                    }
                    catch ( IOException e )
                    {
                        // Throw the first IOException later.
                        if ( io_exception == null )
                        {
                            io_exception = e;
                        }
                    }
                }
            }

            if ( cursor.permissions ().isAllowed ( RecordPermission.READ ) )
            {
                final long num_readers = this.readers.increment ();
                if ( num_readers == 1L )
                {
                    try
                    {
                        this.streamIn = new FileInputStream ( this.file );
                    }
                    catch ( IOException e )
                    {
                        // Throw the first IOException later.
                        if ( io_exception == null )
                        {
                            io_exception = e;
                        }
                    }
                }
            }
        }

        if ( io_exception != null )
        {
            throw new RecordOperationException ( "Could not open file driver [%file_driver%] cursor [%cursor%]",
                                                 "file_driver", this,
                                                 "cursor", cursor,
                                                 "cause", io_exception );
        }
    }


    /**
     * @see musaico.kernel.objectsystem.Record#poll(musaico.objectsystem.Cursor,musaico.io.Progress)
     */
    @Override
    public void poll (
                      Cursor cursor,
                      Progress progress
                      )
        throws RecordOperationException
    {
        throw new RecordOperationException ( "!!! POLLING HAS NOT YET BEEN IMPLEMENTED!" );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#read(musaico.objectsystem.Cursor,musaico.io.Progress,musaico.buffer.Buffer,musaico.io.Region)
     */
    @Override
    public Region read (
                        Cursor cursor,
                        Progress progress,
                        Buffer read_fields,
                        Region read_into_region
                        )
        throws RecordOperationException
    {
        List<FixedLengthRegion> actual_buffer_regions_list =
            new ArrayList<FixedLengthRegion> ();
        Length one = new Length ( 1L );
        try
        {
            synchronized ( this.lock )
            {
                Region from_region = this.region ( cursor );
                Position from_position =
                    from_region.step ( cursor.position (), 0L );

                if ( from_position instanceof NoSuchPosition )
                {
                    throw new RecordOperationException ( "FileDriver [%file_driver%] cursor [%cursor%] no such position [%position%]",
                                                         "file_driver", this,
                                                         "cursor", cursor,
                                                         "position", from_position );
                }

                for ( Position to_position = read_into_region.start ();
                      ! ( to_position instanceof NoSuchPosition );
                      to_position = read_into_region.step ( to_position, 1L ) )
                {
                    if ( from_position instanceof NoSuchPosition )
                    {
                        // Stop reading, no more streams.
                        break;
                    }

                    final Field stream_field;
                    final long index =
                        this.region.indexOf ( from_position );
                    if ( index == 0L )
                    {
                        stream_field =
                            read_fields.environment ().create ( this.file.getPath (),
                                                                RecordStart.class,
                                                                new RecordStart ( from_region,
                                                                                  this.file.getPath () ) );
                    }
                    else if ( index == FileDriver.POSITION_IN_STREAM.offset () )
                    {
                        stream_field =
                            read_fields.environment ().create ( "in",
                                                                // !!! InputStream.class,
                                                                Object.class,
                                                                this.streamIn );
                    }
                    else if ( index == FileDriver.POSITION_OUT_STREAM.offset () )
                    {
                        stream_field =
                            read_fields.environment ().create ( "out",
                                                                // !!! OutputStream.class,
                                                                Object.class,
                                                                this.streamOut );
                    }
                    else if ( index == ( this.region.length () - 1L ) )
                    {
                        stream_field =
                            read_fields.environment ().create ( this.file.getPath (),
                                                                RecordEnd.class,
                                                                new RecordEnd ( from_region,
                                                                                this.file.getPath () ) );
                    }
                    else
                    {
                        throw new RecordOperationException ( "Unknown FileDriver [%file_driver%] position [%position%]",
                                                             "file_driver", this,
                                                             "position", from_position );
                    }

                    read_fields.set ( to_position, stream_field );

                    actual_buffer_regions_list
                        .add ( new FixedLengthRegion ( to_position,     // id
                                                       one,             // length
                                                       to_position,     // start
                                                       to_position ) ); // end

                    from_position = from_region.step ( from_position, 1L );
                }
            }
        }
        catch ( BufferException e )
        {
            throw new RecordOperationException ( e );
        }

        FixedLengthRegion [] template =
            new FixedLengthRegion [ actual_buffer_regions_list.size () ];
        FixedLengthRegion [] actual_buffer_regions =
            actual_buffer_regions_list.toArray ( template );
        Region actual_buffer_region =
            new SectionedRegion ( read_into_region.id (),
                                  actual_buffer_regions );

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
        if ( this.file == null )
        {
            // No file configured.  Root driver.
            return this.region;
        }
        else if ( cursor.permissions ().isAllowed ( RecordPermission.READ )
                  && cursor.permissions ().isAllowed ( RecordPermission.WRITE ) )
        {
            // Read AND write.
            return this.region;
        }
        else if ( cursor.permissions ().isAllowed ( RecordPermission.READ ) )
        {
            // Read only.
            Filter filter =
                new PositionExcludedFilter ( FileDriver.POSITION_OUT_STREAM );
            return this.region.filter ( filter );
        }
        else if ( cursor.permissions ().isAllowed ( RecordPermission.WRITE ) )
        {
            // Write only.
            Filter filter =
                new PositionExcludedFilter ( FileDriver.POSITION_IN_STREAM );
            return this.region.filter ( filter );
        }
        else
        {
            return new FixedLengthRegion ( this.id (),
                                           new Length ( 0L ),
                                           new NoSuchPosition (),
                                           new NoSuchPosition () );
        }
    }


    /**
     * @see musaico.kernel.driver.BlockDriver#sectorReadAhead()
     */
    @Override
    public Size sectorReadAhead ()
        throws DriverException
    {
        // No point in reading ahead for 4 fields (record start,
        // input stream, output stream, record end).
        // Just read on demand.
        return new Length ( 0L );
    }


    /**
     * @see musaico.kernel.driver.BlockDriver#sectorSearch(musaico.kernel.objectsystem.Cursor,musaico.io.Progress,musaico.io.Region,musaico.buffer.Buffer)
     */
    @Override
    public Position sectorSearch (
                                  Cursor cursor,
                                  Progress progress,
                                  Region search_region,
                                  Buffer criteria
                                  )
        throws DriverException
    {
        return new NoSuchPosition (); // !!!
    }


    /**
     * @see musaico.kernel.driver.BlockDriver#sectorRegion()
     */
    @Override
    public Region sectorRegion ()
        throws DriverException
    {
        return new FixedLengthRegion ( this.id, new Length ( 64L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#seek(musaico.objectsystem.Cursor,musaico.io.Progress,musaico.io.Position)
     */
    @Override
    public Position seek (
                          Cursor cursor,
                          Progress progress,
                          Position position
                          )
        throws RecordOperationException
    {
        Position found_position = this.region ( cursor ).step ( position, 0L );
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
        this.flush ( new NoProgress () );

        // !!! if necessary force streams to close, throw
        // !!! error since they weren't closed previously.
    }


    /**
     * @see musaico.kernel.driver.Driver#state()
     */
    @Override
    public Reference state ()
    {
        // !!!
        return Driver.INITIALIZED;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#sync(musaico.objectsystem.Cursor,musaico.io.Progress,boolean)
     */
    @Override
    public void sync (
                      Cursor cursor,
                      Progress progress,
                      boolean is_metadata_only
                      )
        throws RecordOperationException
    {
        this.flush ( progress ); // TODO !!! ???
    }


    /**
     * @see musaico.kernel.objectsystem.Record#write(musaico.objectsystem.Cursor,musaico.io.Progress,musaico.buffer.Buffer,musaico.io.Region)
     */
    @Override
    public Region write (
                         Cursor cursor,
                         Progress progress,
                         Buffer write_fields,
                         Region write_from_region
                         )
        throws RecordOperationException
    {
        throw new RecordOperationException ( "FileDriver does not support writing over the 2 input / output stream fields.  You should probably stack a StreamDriver on top of this FileDriver, then you can write byte[] blocks to the StreamDriver." );
    }




    /** Filters out the specified Position from a region. */
    public static class PositionExcludedFilter
        implements Filter, Serializable
    {
        private final Position positionToFilterOut;

        public PositionExcludedFilter (
                                       Position position_to_filter_out
                                       )
        {
            this.positionToFilterOut = position_to_filter_out;
        }

        public FilterState filter (
                                   Position position
                                   )
        {
            if ( position.equals ( this.positionToFilterOut ) )
            {
                return FilterState.DISCARD;
            }
            else
            {
                return FilterState.KEEP;
            }
        }
    }
}
