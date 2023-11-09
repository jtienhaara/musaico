package musaico.kernel.objectsystem.cursors;

import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Progress;
import musaico.io.Reference;

import musaico.kernel.memory.Segment;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.PollRequest;
import musaico.kernel.objectsystem.PollResponse;
import musaico.kernel.objectsystem.Record;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordSecurityException;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Size;
import musaico.region.Space;

import musaico.region.array.ArraySpace;

import musaico.security.Security;


/**
 * <p>
 * Wraps the calls to a Record so that the Cursor which is
 * providing access to that record gets updated when necessary
 * (changing position, updating progress, and so on).
 * </p>
 *
 * <p>
 * For example, whenever the record is <code> open () </code>ed,
 * the cursor is positioned at the start.
 * </p>
 *
 *
 * <p>
 * Because object systems can conceivably be distributed and using
 * RMI, every Record must be Serializable in order to play nicely
 * over RMI.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public class CursorRecordWrapper
    implements Record, Serializable
{
    /** Lock critical sections on this token: */
    private final Serializable lock = new String ();

    /** The record to whom we delegate all calls.
     *  Sometimes we do additional work, but we never override
     *  the delegate record's core operations. */
    private final Record record;

    /** Has this record been opened by the cursor and not closed?
     *  Can change over time. */
    private boolean isOpen;


    /**
     * <p>
     * Creates a new record wrapper for the specified record,
     * so that calls that are delegated to it update the
     * cursor accordingly.
     * </p>
     *
     * <p>
     * Typically each cursor creates a new wrapper for its
     * record.  It is assumed that there will generally be
     * less cursors in existance than there are records in
     * the system (ONodeData, Drivers, and so on).
     * </p>
     *
     * @param record The record to wrap.  Every call will be
     *               delegated to this record, but may also
     *               induce side-effects in the cursor passed
     *               in (such as updating the cursor's position
     *               and progress).  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public CursorRecordWrapper (
                                Record record
                                )
        throws I18nIllegalArgumentException
    {
        if ( record == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a CursorRecordWrapper with record [%record%]",
                                                     "record", record );
        }

        this.record = record;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#close(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public void close (
                       Cursor cursor
                       )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        if ( cursor == null )
        {
            throw new I18nIllegalArgumentException ( "Record [%record%] cannot perform operation [%operation_name%] for cursor [%cursor%]",
                                                     "record", this.record,
                                                     "operation_name", "close",
                                                     "cursor", cursor );
        }

        Progress progress = cursor.progress ();
        Reference progress_id = progress.push ( 2L );

        final Position out_of_bounds = this.record.space ().outOfBounds ();

        // Throw exceptions up the chain.
        try
        {
            synchronized ( this.lock )
            {
                if ( ! this.isOpen )
                {
                    throw new RecordOperationException ( "Cursor [%cursor%] record [%record%] is not open",
                                                         "cursor", cursor,
                                                         "record", this.record );
                }

                this.record.close ( cursor );

                this.isOpen = false;
            }

            progress.step ( progress_id );

            cursor.position ( out_of_bounds );

            progress.step ( progress_id );
        }
        finally
        {
            progress.pop ( progress_id );
        }
    }


    /**
     * @return True if this cursor record wrapper is open,
     *         false if it is closed.
     */
    public boolean isOpen ()
    {
        synchronized ( this.lock )
        {
            return this.isOpen;
        }
    }


    /**
     * @see musaico.kernel.objectsystem.Record#mmap(musaico.kernel.objectsystem.Cursor,musaico.kernel.memory.Segment)
     */
    @Override
    public void mmap (
                      Cursor cursor,
                      Segment segment
                      )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        if ( cursor == null )
        {
            throw new I18nIllegalArgumentException ( "Record [%record%] cannot perform operation [%operation_name%] for cursor [%cursor%]",
                                                     "record", this.record,
                                                     "operation_name", "mmap",
                                                     "cursor", cursor );
        }

        Progress progress = cursor.progress ();
        Reference progress_id = progress.push ( 1L );

        // Throw exceptions up the chain.
        try
        {
            this.record.mmap ( cursor, segment );

            progress.step ( progress_id );
        }
        finally
        {
            progress.pop ( progress_id );
        }
    }


    /**
     * @see musaico.kernel.objectsystem.Record#open(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public void open (
                      Cursor cursor
                      )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        if ( cursor == null )
        {
            throw new I18nIllegalArgumentException ( "Record [%record%] cannot perform operation [%operation_name%] for cursor [%cursor%]",
                                                     "record", this.record,
                                                     "operation_name", "open",
                                                     "cursor", cursor );
        }

        Progress progress = cursor.progress ();
        Reference progress_id = progress.push ( 2L );

        // Throw exceptions up the chain.
        try
        {
            synchronized ( this.lock )
            {
                if ( this.isOpen )
                {
                    throw new RecordOperationException ( "Cursor [%cursor%] record [%record%] is already open",
                                                         "cursor", cursor,
                                                         "record", this.record );
                }

                this.record.open ( cursor );

                this.isOpen = true;
            }

            progress.step ( progress_id );

            Region region = this.record.region ( cursor );
            Position start = region.start ();
            cursor.position ( start );

            progress.step ( progress_id );
        }
        finally
        {
            progress.pop ( progress_id );
        }
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
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        if ( cursor == null )
        {
            throw new I18nIllegalArgumentException ( "Record [%record%] cannot perform operation [%operation_name%] for cursor [%cursor%]",
                                                     "record", this.record,
                                                     "operation_name", "poll",
                                                     "cursor", cursor );
        }

        Progress progress = cursor.progress ();
        Reference progress_id = progress.push ( 1L );

        // Throw exceptions up the chain.
        try
        {
            this.record.poll ( cursor, request, response );

            progress.step ( progress_id );
        }
        finally
        {
            progress.pop ( progress_id );
        }
    }


    /**
     * @see musaico.kernel.objectsystem.Record#read(musaico.kernel.objectsystem.Cursor,musaico.buffer.Buffer,musaico.region.Region)
     */
    @Override
    public Region read (
                        Cursor cursor,
                        Buffer read_fields,
                        Region read_into_region
                        )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        if ( cursor == null )
        {
            throw new I18nIllegalArgumentException ( "Record [%record%] cannot perform operation [%operation_name%] for cursor [%cursor%]",
                                                     "record", this.record,
                                                     "operation_name", "read",
                                                     "cursor", cursor );
        }

        Progress progress = cursor.progress ();
        Reference progress_id = progress.push ( 2L );

        Position old_position = cursor.position ();

        final Region actual_region_read;

        // Throw exceptions up the chain.
        try
        {
            actual_region_read =
                this.record.read ( cursor, read_fields, read_into_region );

            progress.step ( progress_id );

            Size size_read = actual_region_read.size ();
            Space record_space = this.record.space ();
            Region record_region = this.record.region ( cursor );
            Size add_size = record_space.from ( size_read );
            Position new_position =
                record_region.expr ( old_position ).add ( add_size )
                .position ();
            cursor.position ( new_position );

            progress.step ( progress_id );
        }
        finally
        {
            progress.pop ( progress_id );
        }

        return actual_region_read;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#region(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public Region region (
                          Cursor cursor
                          )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        if ( cursor == null )
        {
            throw new I18nIllegalArgumentException ( "Record [%record%] cannot perform operation [%operation_name%] for cursor [%cursor%]",
                                                     "record", this.record,
                                                     "operation_name", "region",
                                                     "cursor", cursor );
        }

        // Don't bother keeping track of progress,
        // just throw exceptions right up the chain.
        return this.record.region ( cursor );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#security()
     */
    @Override
    public Security<RecordFlag> security ()
    {
        return this.record.security ();
    }


    /**
     * @see musaico.kernel.objectsystem.Record#seek(musaico.kernel.objectsystem.Cursor,musaico.region.Position)
     */
    @Override
    public Position seek (
                          Cursor cursor,
                          Position position
                          )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        if ( cursor == null )
        {
            throw new I18nIllegalArgumentException ( "Record [%record%] cannot perform operation [%operation_name%] for cursor [%cursor%]",
                                                     "record", this.record,
                                                     "operation_name", "seek",
                                                     "cursor", cursor );
        }

        Progress progress = cursor.progress ();
        Reference progress_id = progress.push ( 1L );

        final Position final_position;

        // Throw exceptions up the chain.
        try
        {
            final_position = this.record.seek ( cursor, position );

            progress.step ( progress_id );

            cursor.position ( final_position );
        }
        finally
        {
            progress.pop ( progress_id );
        }

        return final_position;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#space()
     */
    @Override
    public Space space ()
    {
        return this.record.space ();
    }


    /**
     * @see musaico.kernel.objectsystem.Record#sync(musaico.kernel.objectsystem.Cursor,boolean)
     */
    @Override
    public void sync (
                      Cursor cursor,
                      boolean is_metadata_only
                      )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        if ( cursor == null )
        {
            throw new I18nIllegalArgumentException ( "Record [%record%] cannot perform operation [%operation_name%] for cursor [%cursor%]",
                                                     "record", this.record,
                                                     "operation_name", "sync",
                                                     "cursor", cursor );
        }

        Progress progress = cursor.progress ();
        Reference progress_id = progress.push ( 1L );

        // Throw exceptions up the chain.
        try
        {
            this.record.sync ( cursor, is_metadata_only );

            progress.step ( progress_id );
        }
        finally
        {
            progress.pop ( progress_id );
        }
    }


    /**
     * @see musaico.kernel.objectsystem.Record#write(musaico.kernel.objectsystem.Cursor,musaico.buffer.Buffer,musaico.region.Region)
     */
    @Override
    public Region write (
                         Cursor cursor,
                         Buffer write_fields,
                         Region write_from_region
                         )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        if ( cursor == null )
        {
            throw new I18nIllegalArgumentException ( "Record [%record%] cannot perform operation [%operation_name%] for cursor [%cursor%]",
                                                     "record", this.record,
                                                     "operation_name", "write",
                                                     "cursor", cursor );
        }

        Progress progress = cursor.progress ();
        Reference progress_id = progress.push ( 2L );

        Position old_position = cursor.position ();

        final Region actual_region_written;

        // Throw exceptions up the chain.
        try
        {
            actual_region_written =
                this.record.write ( cursor, write_fields, write_from_region );

            progress.step ( progress_id );

            Size size_written = actual_region_written.size ();
            Space record_space = this.record.space ();
            Region record_region = this.record.region ( cursor );
            Size add_size = record_space.from ( size_written );
            Position new_position =
                record_region.expr ( old_position ).add ( add_size )
                .position ();
            cursor.position ( new_position );

            progress.step ( progress_id );
        }
        finally
        {
            progress.pop ( progress_id );
        }

        return actual_region_written;
    }
}
