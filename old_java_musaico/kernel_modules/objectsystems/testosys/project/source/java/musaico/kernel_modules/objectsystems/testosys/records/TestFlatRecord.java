package musaico.kernel_modules.objectsystems.records;


import musaico.io.Progress;
import musaico.io.Reference;
import musaico.io.ReferenceCount;
import musaico.io.Region;
import musaico.io.SimpleReferenceCount;

import musaico.buffer.Buffer;

import musaico.io.positions.NoSuchPosition;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.ONode;
import musaico.kernel.objectsystem.Record;
import musaico.kernel.objectsystem.RecordOperationException;


/**
 * <p>
 * A flat record in a TestOsys object system.
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
public class TestFlatRecord
    implements FlatRecord
{
    /** Lock all critical sections on this token: */
    private final Serializable lock = new String ();

    /** The number of references to this record. */
    private final ReferenceCount referenceCount =
        new SimpleReferenceCount ();


    /**
     * <p>
     * Creates a new TestFlatRecord.
     * </p>
     */
    public TestFlatRecord ()
    {
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
        final long reference_count = this.referenceCount.decrement ();
        if ( reference_count < 0L )
        {
            throw new RecordOperationException ( "Cannot close TestFlatRecord [%record%]: reference count below 0 ([%reference_count%])",
                                                 "record", this,
                                                 "reference_count", reference_count );
        }

        cursor.setPosition ( new NoSuchPosition () );
        !!!;
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
        throw new RecordOperationException ( "TestFlatRecord [%record%] does not support fcntl command [%fcntl_command%] ( [%fcntl_args%] )",
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
        throw new RecordOperationException ( "TestFlatRecord [%record%] does not support ioctl command [%ioctl_command%] ( [%ioctl_args%] )",
                                             "record", this,
                                             "ioctl_command", ioctl_command,
                                             "ioctl_args", ioctl_args );
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
        !!!;
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
        final long reference_count = this.referenceCount.increment ();
        cursor.setPosition ( this.region ().start () );
        !!!;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#poll(musaico.objectsystem.Cursor,musaico.io.Progress,musaico.kernel.objectsystem.PollTable)
     */
    public void poll (
                      Cursor cursor,
                      Progress progress,
                      PollTable poll_table
                      )
        throws RecordOperationException
    {
        !!!;
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
        !!!;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#region()
     */
    public Region region ()
    {
        !!!;
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
        !!!;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#sync(musaico.objectsystem.Cursor,musaico.io.Progress,int)
     */
    public void sync (
                      Cursor cursor,
                      Progress progress,
                      int data_sync_flags
                      )
        throws RecordOperationException
    {
        !!!;
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
        !!!;
    }
}
