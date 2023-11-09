package musaico.kernel.common.records;

import java.io.Serializable;


import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.TimeoutException;

import musaico.io.Reference;

import musaico.kernel.memory.MemoryException;
import musaico.kernel.memory.MemoryRequest;
import musaico.kernel.memory.Segment;

import musaico.kernel.memory.requests.ReadMemoryRequest;
import musaico.kernel.memory.requests.WriteMemoryRequest;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.PollRequest;
import musaico.kernel.objectsystem.PollResponse;
import musaico.kernel.objectsystem.Record;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordPermission;
import musaico.kernel.objectsystem.RecordPermissions;
import musaico.kernel.objectsystem.RecordSecurityException;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Space;

import musaico.security.Credentials;
import musaico.security.Permissions;
import musaico.security.Security;

import musaico.time.RelativeTime;

/**
 * <p>
 * A Record wrapper for paged segment data (used for the ONode
 * data and metadata in most object systems).
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
public class SegmentBackedRecord
    implements Record, Serializable
{
    /** The segment backing this record.  All read and write operations
     *  are essentially forwarded to this segment. */
    private final Segment segment;

    /** The security for this segment-backed record. */
    private final Security<RecordFlag> security;


    /**
     * <p>
     * Creates a new record backed by paged segment data.
     * </p>
     *
     * @param segment The segment which will provide data for this record.
     *                Must not be null.
     *
     * @param security The security for this segment-backed record,
     *                 which dictates who is (and who is not) allowed
     *                 to open, close, read or write this record.
     *                 Must not be null.
     */
    public SegmentBackedRecord (
				Segment segment,
				Security<RecordFlag> security
				)
    {
	this.segment = segment;
	this.security = security;
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
	cursor.position ( this.segment.space ().outOfBounds () );
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
	throw new I18nIllegalArgumentException ( "!!! NOT YET IMPLEMENTED" );
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
	// Check that the cursor is allowed to open this record in
	// the specified mode.
	Permissions<RecordFlag> requested_permissions =
	    cursor.permissions ();
	Permissions<RecordFlag> granted_permissions =
	    this.security ().request ( requested_permissions );
	if ( ! granted_permissions.isAllowed ( requested_permissions ) )
	{
	    throw new RecordSecurityException ( "Access denied: cannot open segment-backed record for [%segment_id%] with cursor [%cursor%] requested permissions [%requested_permissions%] granted permissions [%granted_permissions%]",
						"segment_id", this.segment.id (),
						"cursor", cursor,
						"requested_permissions", requested_permissions,
						"granted_permissions", granted_permissions );
	}

	// Set the cursor to point to the start of the segment.
	Region region = this.segment.region ();
	Position start = region.start ();

	cursor.position ( start );
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
	throw new I18nIllegalArgumentException ( "!!! NOT YET IMPLEMENTED" );
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
	// Check that the cursor is allowed to read this record.
	Permissions<RecordFlag> requested_permissions =
	    new RecordPermissions ( cursor.owner (),
				    RecordPermission.READ );
	Permissions<RecordFlag> granted_permissions =
	    this.security ().request ( requested_permissions );
	if ( ! granted_permissions.isAllowed ( requested_permissions ) )
	{
	    throw new RecordSecurityException ( "Access denied: cursor [%cursor%] cannot read segment-backed record for [%segment_id%]",
						"cursor", cursor,
						"segment_id", this.segment.id () );
	}

	ReadMemoryRequest request =
	    new ReadMemoryRequest ( cursor.owner (),
				    this.segment.id (),
				    new RelativeTime ( 1000L, 0L ), // !!! 1s
				    cursor.position (),
				    read_fields,
				    read_into_region );

	final Region actual_region_read_into;
	try
	{
	    actual_region_read_into =
		this.segment.request ( request ).waitFor ().response ();
	}
	catch ( TimeoutException e )
	{
	    throw new RecordOperationException ( e );
	}
	catch ( MemoryException e )
	{
	    throw new RecordOperationException ( e );
	}

	return actual_region_read_into;
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
	return this.segment.region ();
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
	final Position actual_new_position;
	if ( this.segment.region ().contains ( position ) )
	{
	    actual_new_position = position;
	}
	else
	{
	    actual_new_position =
		this.segment.space ().outOfBounds ();
	}

	cursor.position ( actual_new_position );
	return actual_new_position;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#space()
     */
    @Override
    public Space space ()
    {
	return this.segment.space ();
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
	throw new I18nIllegalArgumentException ( "!!! NOT YET IMPLEMENt<eD" );
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
	Position cursor_position = cursor.position ();
	Region segment_region = this.segment.region ();

	// Check that the cursor is allowed to write (and possibly
	// also append to) this record.
	final Permissions<RecordFlag> requested_permissions;
	if ( segment_region.contains ( cursor_position ) )
	{
	    requested_permissions =
		new RecordPermissions ( cursor.owner (),
					RecordPermission.WRITE );
	}
	else
	{
	    requested_permissions =
		new RecordPermissions ( cursor.owner (),
					RecordPermission.WRITE,
					RecordPermission.APPEND );
	}

	Permissions<RecordFlag> granted_permissions =
	    this.security ().request ( requested_permissions );
	if ( ! granted_permissions.isAllowed ( requested_permissions ) )
	{
	    throw new RecordSecurityException ( "Access denied: cursor [%cursor%] cannot write segment-backed record for [%segment_id%]",
						"cursor", cursor,
						"segment_id", this.segment.id () );
	}

	WriteMemoryRequest request =
	    new WriteMemoryRequest ( cursor.owner (),
				     this.segment.id (),
				     new RelativeTime ( 1000L, 0L ), // !!! 1s
				     cursor_position,
				     write_fields,
				     write_from_region );

	final Region actual_region_written_from;
	try
	{
	    actual_region_written_from =
		this.segment.request ( request ).waitFor ().response ();
	}
	catch ( TimeoutException e )
	{
	    throw new RecordOperationException ( e );
	}
	catch ( MemoryException e )
	{
	    throw new RecordOperationException ( e );
	}

	return actual_region_written_from;
    }
}
