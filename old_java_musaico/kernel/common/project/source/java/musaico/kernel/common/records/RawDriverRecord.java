package musaico.kernel.common.records;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Position;
import musaico.io.Progress;
import musaico.io.Reference;
import musaico.io.ReferenceCount;
import musaico.io.Region;
import musaico.io.SimpleReferenceCount;

import musaico.buffer.Buffer;

import musaico.io.positions.NoSuchPosition;

import musaico.kernel.driver.Driver;

import musaico.kernel.memory.Segment;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleOperationException;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.ObjectSystemException;
import musaico.kernel.objectsystem.ONode;
import musaico.kernel.objectsystem.Record;
import musaico.kernel.objectsystem.RecordOperationException;


/**
 * <p>
 * A record pointing to a driver, so that a driver can be
 * accessed from an ONode in an object system.
 * </p>
 *
 * <p>
 * In Linux char_dev.c only the open() operation is implemented,
 * and during it, the file operations are replaced with those
 * of the driver.  Then it calls the driver's open() method.
 * Subsequently all calls happen directly on the driver.
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
public class RawDriverRecord
    implements Record
{
    /** Lock all critical sections on this token: */
    private final Serializable lock = new String ();

    /** The number of references to this record. */
    private final ReferenceCount referenceCount =
        new SimpleReferenceCount ();

    /** The module to which this raw driver record belongs
     *  (likely the "common" module, though there
     *  is nothing stopping developers from re-using this
     *  record type in other modules as long as licensing
     *  restrictions are met). */
    private final Module module;

    /** Once this record has been open()ed, we will know
     *  which driver we point to. */
    private Driver driver;


    /**
     * <p>
     * Creates a new RawDriverRecord inside the specified Module.
     * </p>
     *
     * @param module The Module through which this record accesses
     *               kernel data.  Must not be null.
     *
     * @throws I18nIllegalArgumentException When the parameters are invalid.
     */
    public RawDriverRecord (
                            Module module
                            )
        throws I18nIllegalArgumentException
    {
        if ( module == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a RawDriverRecord with module [%module%]",
                                                     "module", module );
        }

        this.module = module;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#close(musaico.objectsystem.Cursor,musaico.objectsystem.ONode,musaico.io.Progress)
     */
    public void close (
                       Cursor cursor,
                       ONode onode,
                       Progress progress
                       )
        throws RecordOperationException
    {
        if ( this.driver == null )
        {
            throw new RecordOperationException ( "RawDriverRecord [%record%] with cursor [%cursor%] is not open",
                                                 "record", this,
                                                 "cursor", cursor );
        }

        final long reference_count = this.referenceCount.decrement ();
        if ( reference_count < 0L )
        {
            throw new RecordOperationException ( "Cannot close RawRecord [%record%]: reference count below 0 ([%reference_count%])",
                                                 "record", this,
                                                 "reference_count", reference_count );
        }

        cursor.position ( new NoSuchPosition () );

        this.driver.close ( cursor, onode, progress );

        if ( reference_count == 0L )
        {
            // No more references, close down.
            this.driver = null;
        }
    }


    /**
     * @see musaico.kernel.objectsystem.Record#fcntl(musaico.objectsystem.Cursor,musaico.io.Progress,musaico.io.Reference,musaico.buffer.Buffer)
     */
    public Serializable fcntl (
                               Cursor cursor,
                               Progress progress,
                               Reference fcntl_command,
                               Buffer fcntl_args
                               )
        throws RecordOperationException
    {
        throw new RecordOperationException ( "RawDriverRecord [%record%] does not support fcntl command [%fcntl_command%] ( [%fcntl_args%] )",
                                             "record", this,
                                             "fcntl_command", fcntl_command,
                                             "fcntl_args", fcntl_args );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#ioctl(musaico.objectsystem.Cursor,musaico.objectsystem.ONode,musaico.io.Progress,musaico.io.Reference,musaico.buffer.Buffer)
     */
    public Serializable ioctl (
                               Cursor cursor,
                               ONode onode,
                               Progress progress,
                               Reference ioctl_command,
                               Buffer ioctl_args
                               )
        throws RecordOperationException
    {
        if ( this.driver == null )
        {
            throw new RecordOperationException ( "RawDriverRecord [%record%] with cursor [%cursor%] is not open",
                                                 "record", this,
                                                 "cursor", cursor );
        }

        return this.driver.ioctl ( cursor, onode, progress,
                                   ioctl_command, ioctl_args );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#mmap(musaico.objectsystem.Cursor,musaico.io.Progress,musaico.kernel.memory.Segment)
     */
    public void mmap (
                      Cursor cursor,
                      Progress progress,
                      Segment segment
                      )
        throws RecordOperationException
    {
        if ( this.driver == null )
        {
            throw new RecordOperationException ( "RawDriverRecord [%record%] with cursor [%cursor%] is not open",
                                                 "record", this,
                                                 "cursor", cursor );
        }

        this.driver.mmap ( cursor, progress, segment );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#open(musaico.objectsystem.Cursor,musaico.objectsystem.ONode,musaico.io.Progress)
     */
    public void open (
                      Cursor cursor,
                      ONode onode,
                      Progress progress
                      )
        throws RecordOperationException
    {
        Reference driver_id = onode.driverID ();

        // Linux doesn't bother checking to see if the driver
        // pointer has previously been set, so neither do we.
        // (So far.)
        try
        {
            this.driver =
                this.module.kernelObject ( Driver.class, driver_id );
        }
        catch ( ModuleOperationException e )
        {
            throw new RecordOperationException ( "Could not open RawDriverRecord [%record%] driver identifier [%driver_id%] with cursor [%cursor%] ONode [%onode%]",
                                                 "record", this,
                                                 "driver_id", driver_id,
                                                 "cursor", cursor,
                                                 "onode", onode,
                                                 "cause", e );
        }

        if ( this.driver == null )
        {
            throw new RecordOperationException ( "Could not open RawDriverRecord [%record%] driver identifier [%driver_id%] with cursor [%cursor%] ONode [%onode%]: No such driver",
                                                 "record", this,
                                                 "driver_id", driver_id,
                                                 "cursor", cursor,
                                                 "onode", onode );
        }

        cursor.position ( this.region ( cursor ).start () );
        final long reference_count = this.referenceCount.increment ();

        this.driver.open ( cursor, onode, progress );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#poll(musaico.objectsystem.Cursor,musaico.io.Progress)
     */
    public void poll (
                      Cursor cursor,
                      Progress progress
                      )
        throws RecordOperationException
    {
        if ( this.driver == null )
        {
            throw new RecordOperationException ( "RawDriverRecord [%record%] with cursor [%cursor%] is not open",
                                                 "record", this,
                                                 "cursor", cursor );
        }

        this.driver.poll ( cursor, progress );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#read(musaico.objectsystem.Cursor,musaico.io.Progress,musaico.buffer.Buffer,musaico.io.Region)
     */
    public Region read (
                        Cursor cursor,
                        Progress progress,
                        Buffer read_fields,
                        Region read_into_region
                        )
        throws RecordOperationException
    {
        if ( this.driver == null )
        {
            throw new RecordOperationException ( "RawDriverRecord [%record%] with cursor [%cursor%] is not open",
                                                 "record", this,
                                                 "cursor", cursor );
        }

        return this.driver.read ( cursor, progress,
                                  read_fields, read_into_region );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#region(musaico.kernel.objectsystem.Cursor)
     */
    public Region region (
                          Cursor cursor
                          )
        throws RecordOperationException
    {
        if ( this.driver == null )
        {
            throw new RecordOperationException ( "RawDriverRecord [%record%] with cursor [%cursor%] is not open",
                                                 "record", this,
                                                 "cursor", cursor );
        }

        return this.driver.region ( cursor );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#seek(musaico.objectsystem.Cursor,musaico.io.Progress,musaico.io.Position)
     */
    public Position seek (
                          Cursor cursor,
                          Progress progress,
                          Position position
                          )
        throws RecordOperationException
    {
        if ( this.driver == null )
        {
            throw new RecordOperationException ( "RawDriverRecord [%record%] with cursor [%cursor%] is not open",
                                                 "record", this,
                                                 "cursor", cursor );
        }

        Position found_position =
            this.driver.seek ( cursor, progress, position );
        cursor.position ( found_position );

        return position;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#sync(musaico.objectsystem.Cursor,musaico.io.Progress,boolean)
     */
    public void sync (
                      Cursor cursor,
                      Progress progress,
                      boolean is_metadata_only
                      )
        throws RecordOperationException
    {
        if ( this.driver == null )
        {
            throw new RecordOperationException ( "RawDriverRecord [%record%] with cursor [%cursor%] is not open",
                                                 "record", this,
                                                 "cursor", cursor );
        }

        this.driver.sync ( cursor, progress, is_metadata_only );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#write(musaico.objectsystem.Cursor,musaico.io.Progress,musaico.buffer.Buffer,musaico.io.Region)
     */
    public Region write (
                         Cursor cursor,
                         Progress progress,
                         Buffer write_fields,
                         Region write_from_region
                         )
        throws RecordOperationException
    {
        if ( this.driver == null )
        {
            throw new RecordOperationException ( "RawDriverRecord [%record%] with cursor [%cursor%] is not open",
                                                 "record", this,
                                                 "cursor", cursor );
        }

        return this.driver.write ( cursor, progress,
                                   write_fields, write_from_region );
    }
}
