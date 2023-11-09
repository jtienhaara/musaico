package musaico.kernel.common.onodes;

import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Path;
import musaico.io.Sequence;

import musaico.kernel.memory.Segment;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleOperationException;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.PollRequest;
import musaico.kernel.objectsystem.PollResponse;
import musaico.kernel.objectsystem.Record;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordLock;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordPermission;
import musaico.kernel.objectsystem.RecordPermissions;
import musaico.kernel.objectsystem.RecordSecurityException;

import musaico.kernel.objectsystem.onode.FillOEntryCallback;
import musaico.kernel.objectsystem.onode.OEntry;
import musaico.kernel.objectsystem.onode.OEntryFactory;
import musaico.kernel.objectsystem.onode.ONode;
import musaico.kernel.objectsystem.onode.ONodeLock;
import musaico.kernel.objectsystem.onode.ONodeMetadata;
import musaico.kernel.objectsystem.onode.ONodeOperationException;

import musaico.kernel.objectsystem.superblock.SuperBlock;
import musaico.kernel.objectsystem.superblock.SuperBlockIdentifier;
import musaico.kernel.objectsystem.superblock.SuperBlockOperationException;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Space;

import musaico.security.Credentials;
import musaico.security.Permissions;
import musaico.security.Security;

import musaico.time.AbsoluteTime;
import musaico.time.Time;


/**
 * <p>
 * Wrapper for ONode data which provides security checks and
 * other object system-independent checks before and after
 * each operation (open, close, read, write and so on).
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
public class VirtualONodeData
    implements Record, Serializable
{
    /** The module which loaded this VirtualONodeData into memory. */
    private final Module module;

    /** The parent ONode of this data. */
    private final ONode onode;

    /** The real ONode data implementation, which we wrap. */
    private final Record onodeData;


    /**
     * <p>
     * Creates a new VirtualONodeData with the specified parent ONode,
     * data and settings.
     * </p>
     *
     * @param module The Module which provides access to the kernel
     *               for this ONode data.  Must not be null.
     *
     * @param onode The parent ONode of this data.  Must not be null.
     *
     * @param onode_data The actual ONode data implementation, whose
     *                   methods the virtual ONode wraps in security
     *                   checks and so on.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public VirtualONodeData (
			     Module module,
			     ONode onode,
			     Record onode_data
			     )
    {
	if ( module == null
	     || onode == null
	     || onode_data == null )
	{
	    throw new I18nIllegalArgumentException ( "Cannot create a VirtualONodeData with module [%module%] under ONode [%onode%] for data [%onode_data%]",
						     "module", module,
						     "onode", onode,
						     "onode_data", onode_data );
	}

	this.module = module;
	this.onode = onode;
	this.onodeData = onode_data;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#close(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public void close (
                       Cursor cursor
                       )
        throws RecordOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONodeData.close()" );

        if ( cursor == null )
        {
            this.module.traceFail ( "VirtualONodeData.close()" );
            throw new I18nIllegalArgumentException ( "Cannot close ONode with Cursor [%cursor%]",
                                                     "cursor", cursor );
        }

        final OEntry entry = (OEntry) cursor.entry ();
	if ( ! this.onode.id ().equals ( entry.onodeRef () ) )
	{
	    throw new RecordSecurityException ( "OEntry [%entry%] pointing to ONode [%onode_ref%] cannot operate on ONode data for [%onode_id%]",
						"entry", entry,
						"onode_ref", entry.onodeRef (),
						"onode_id", this.onode.id () );
	}

	final Credentials credentials = cursor.owner ();

        final RecordLock mutex_lock =
            new RecordLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Call the real ONode data's close().
	    try
	    {
		this.onodeData.close ( cursor );
	    }
	    catch ( RecordOperationException e )
            {
		this.module.traceFail ( "VirtualONodeData.close()" );
		throw e;
            }

            // Now decrement reference counts.
            final long readers;
            final long writers;
            final long hard_links;
            if ( cursor.isReader () )
            {
                readers = this.onode.references ()
		    .readers ( credentials ).decrement ();
            }
            else
            {
                readers = this.onode.references ()
		    .readers ( credentials ).count ();
            }

            if ( cursor.isWriter () )
            {
                writers = this.onode.references ()
		    .writers ( credentials ).decrement ();
            }
            else
            {
                writers = this.onode.references ()
		    .writers ( credentials ).count ();
            }

            // If there are no more references to the ONode,
            // either in the object system or user space Cursors,
            // then throw it out.
	    long num_references = this.onode.references ().count ();
            if ( num_references == 0L )
            {
                // This ONode is done for good.
                final SuperBlockIdentifier super_block_id =
                    (SuperBlockIdentifier) this.onode.superBlockRef ();
                final SuperBlock super_block;
                try
                {
                    super_block =
                        this.module.getKernelObject ( super_block_id );
                    super_block.delete ( this.module.credentials (),
					 cursor.progress (),
                                         this.onode );
                }
                catch ( ModuleOperationException e )
                {
                    this.module.traceFail ( "VirtualONodeData.close()" );
                    throw new ONodeOperationException ( "Failed to delete ONode [%onode%] from its super block [%super_block%] after closing cursor [%cursor%] and removing the last reference",
                                                        "onode", this.onode,
                                                        "super_block", super_block_id,
                                                        "cursor", cursor,
                                                        "cause", e );
                }
                catch ( SuperBlockOperationException e )
                {
                    this.module.traceFail ( "VirtualONodeData.close()" );
                    throw new ONodeOperationException ( "Failed to delete ONode [%onode%] from its super block [%super_block%] after closing cursor [%cursor%] and removing the last reference",
                                                        "onode", this.onode,
                                                        "super_block", super_block_id,
                                                        "cursor", cursor,
                                                        "cause", e );
                }
            }

            // close () does not modify any of the ONode's metadata times.
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONodeData.close()" );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#mmap(musaico.kernel.objectsystem.Cursor,musaico.kernel.memory.Segment)
     */
    @Override
    public void mmap (
                      Cursor cursor,
                      Segment segment
                      )
        throws RecordOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONodeData.mmap()" );

        if ( cursor == null
             || segment == null )
        {
            this.module.traceFail ( "VirtualONodeData.mmap()" );
            throw new I18nIllegalArgumentException ( "Cannot mmap ONode with Cursor [%cursor%] Segment [%segment%]",
                                                     "cursor", cursor,
                                                     "segment", segment );
        }

        final OEntry entry = (OEntry) cursor.entry ();
	if ( ! this.onode.id ().equals ( entry.onodeRef () ) )
	{
	    throw new RecordSecurityException ( "OEntry [%entry%] pointing to ONode [%onode_ref%] cannot operate on ONode data for [%onode_id%]",
						"entry", entry,
						"onode_ref", entry.onodeRef (),
						"onode_id", this.onode.id () );
	}

        final Credentials credentials = cursor.owner ();
	// !!! No permissions currently required for mmap

        final ONodeLock mutex_lock =
            new ONodeLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Touch the metadata time(s) for the ONode
            // (accessed time, modified time, and so on).
            Time accessed_time = AbsoluteTime.now ();
	    ONodeMetadata metadata = this.onode.metadata ();
            try
            {
                metadata.writeValue ( credentials,
				      metadata.metaTimeAccessed (),
				      accessed_time );
            }
            catch ( I18nIllegalArgumentException e )
            {
                this.module.traceFail ( "VirtualONodeData.mmap()" );
                throw new RecordOperationException ( "Failed to update times for entry [%entry%] onode [%onode%] to [%time%]",
                                                     "entry", entry,
                                                     "onode", this.onode,
                                                     "time", accessed_time,
                                                     "cause", e );
            }

            // !!! The GenericONodeData should be used by most
            // !!! object systems.  It will change out the paged area
            // !!! to a new MMappedPagedArea ( segment, onode )
            // !!! or something like that.  Object systems can override
            // !!! the GenericONodeData to change it out to
            // !!! something with more levels of swapping (e.g. cached
            // !!! Objects).
            try
            {
                this.onodeData.mmap ( cursor, segment );
            }
            catch ( RecordOperationException e )
            {
                this.module.traceFail ( "VirtualONodeData.mmap()" );
                throw e;
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONodeData.mmap()" );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#open(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public void open (
                      Cursor cursor
                      )
        throws RecordOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONodeData.open()" );

        if ( cursor == null )
        {
            this.module.traceFail ( "VirtualONodeData.open()" );
            throw new I18nIllegalArgumentException ( "Cannot open ONode with Cursor [%cursor%]",
                                                     "cursor", cursor );
        }

	Credentials credentials = cursor.owner ();
        final RecordLock mutex_lock =
            new RecordLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Call the ONode data's open().
	    try
            {
		this.onodeData.open ( cursor );
	    }
	    catch ( RecordOperationException e )
            {
		this.module.traceFail ( "VirtualONodeData.open()" );
		throw e;
            }

            // Now increment reference counts.
            if ( cursor.isReader () )
            {
                this.onode.references ().readers ( credentials ).increment ();
            }

            if ( cursor.isWriter () )
            {
                this.onode.references ().writers ( credentials ).increment ();
            }

            // Point the Cursor to the start of the ONode.
            cursor.position ( this.region ( cursor ).start () );

            // Touch the metadata time(s) for the ONode
            // (accessed time, modified time, and so on).
            Time accessed_time = AbsoluteTime.now ();
	    ONodeMetadata metadata = this.onode.metadata ();
            try
            {
		metadata.writeValue ( credentials,
				      metadata.metaTimeAccessed (),
				      accessed_time );
            }
            catch ( I18nIllegalArgumentException e )
            {
                this.module.traceFail ( "VirtualONodeData.open()" );
                throw new RecordOperationException ( "Failed to update times for entry [%entry%] onode [%onode%] to [%time%]",
                                                     "entry", cursor.entry (),
                                                     "onode", this.onode,
                                                     "time", accessed_time,
                                                     "cause", e );
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONodeData.open()" );
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
        this.module.traceEnter ( "VirtualONodeData.poll()" );

        if ( cursor == null
             || request == null
             || response == null )
        {
            this.module.traceFail ( "VirtualONodeData.poll()" );
            throw new I18nIllegalArgumentException ( "Cannot poll ONode with cursor [%cursor%] poll request [%poll_request%] response [%poll_response%]",
                                                     "cursor", cursor,
                                                     "poll_request", request,
                                                     "poll_response", response );
        }

        final OEntry entry = (OEntry) cursor.entry ();
	if ( ! this.onode.id ().equals ( entry.onodeRef () ) )
	{
	    throw new RecordSecurityException ( "OEntry [%entry%] pointing to ONode [%onode_ref%] cannot operate on ONode data for [%onode_id%]",
						"entry", entry,
						"onode_ref", entry.onodeRef (),
						"onode_id", this.onode.id () );
	}

        final Credentials credentials = cursor.owner ();
	// !!! No permissions currently required for poll

        final RecordLock mutex_lock =
            new RecordLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Call the ONode data's poll().
	    try
            {
		this.onodeData.poll ( cursor, request, response );
	    }
	    catch ( RecordOperationException e )
            {
		this.module.traceFail ( "VirtualONodeData.poll()" );
		throw e;
            }

            // poll () does not modify any of the ONode's metadata times.
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONodeData.poll()" );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#read(musaico.kernel.objectsystem.Cursor,musaico.buffer.Buffer,musaico.io.Region)
     */
    @Override
    public Region read (
                        Cursor cursor,
                        Buffer read_fields,
                        Region read_into_region
                        )
        throws RecordOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONodeData.read()" );

        if ( cursor == null
             || read_fields == null
             || read_into_region == null )
        {
            this.module.traceFail ( "VirtualONodeData.read()" );
            throw new I18nIllegalArgumentException ( "Cannot read ONode with Cursor [%cursor%] read buffer [%read_fields%] read buffer region [%read_into_region%]",
                                                     "cursor", cursor,
                                                     "read_fields", read_fields,
                                                     "read_into_region", read_into_region );
        }

        final OEntry entry = (OEntry) cursor.entry ();
	if ( ! this.onode.id ().equals ( entry.onodeRef () ) )
	{
	    throw new RecordSecurityException ( "OEntry [%entry%] pointing to ONode [%onode_ref%] cannot operate on ONode data for [%onode_id%]",
						"entry", entry,
						"onode_ref", entry.onodeRef (),
						"onode_id", this.onode.id () );
	}

        final Credentials credentials = cursor.owner ();
	Permissions<RecordFlag> requested_permissions =
	    new RecordPermissions ( credentials,
				    RecordPermission.READ );
	Permissions<RecordFlag> granted_permissions =
	    this.onode.security ().request ( requested_permissions );
	if ( ! granted_permissions.isAllowed ( requested_permissions ) )
	{
            this.module.traceFail ( "VirtualONodeData.read()" );
	    throw new RecordSecurityException ( "Access denied: cursor [%cursor%] cannot read data from ONode [%onode%]",
						"cursor", cursor,
						"onode", this.onode );
	}

        final Region actual_buffer_region;

	// Don't lock the ONode for reads and writes,
	// since they are likely to block.
	//
	// Call the ONode data's read().
	try
        {
	    actual_buffer_region =
		this.onodeData.read ( cursor,
				      read_fields,
				      read_into_region );
	}
	catch ( RecordOperationException e )
        {
	    this.module.traceFail ( "VirtualONodeData.read()" );
	    throw e;
	}

        final RecordLock mutex_lock =
            new RecordLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Touch the metadata time(s) for the ONode
            // (accessed time, modified time, and so on).
            Time accessed_time = AbsoluteTime.now ();
	    ONodeMetadata metadata = this.onode.metadata ();
            try
            {
		metadata.writeValue ( credentials,
				      metadata.metaTimeAccessed (),
				      accessed_time );
            }
            catch ( I18nIllegalArgumentException e )
            {
                this.module.traceFail ( "VirtualONodeData.read()" );
                throw new RecordOperationException ( "Failed to update times for entry [%entry%] onode [%onode%] to [%time%]",
                                                     "entry", entry,
                                                     "onode", this.onode,
                                                     "time", accessed_time,
                                                     "cause", e );
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONodeData.read()" );

        return actual_buffer_region;
    }


    /**
     * @see musaico.kernel.objectsystem.records.ObjectRecord#readObject(musaico.kernel.objectsystem.Cursor,musaico.kernel.objectsystem.OEntryFactory,musaico.kernel.objectsystem.FillOEntryCallback)
     */
    /* !!!
    public void readObject (
                            Cursor cursor,
                            OEntryFactory child_factory,
                            FillOEntryCallback add_child_callback
                            )
        throws I18nIllegalArgumentException,
               RecordOperationException
    {
        this.module.traceEnter ( "VirtualONodeData.readObject()" );

        if ( cursor == null
             || child_factory == null
             || add_child_callback == null )
        {
            this.module.traceFail ( "VirtualONodeData.readObject()" );
            throw new I18nIllegalArgumentException ( "Cannot readObject ONode with Cursor [%cursor%] child factory [%child_factory%] callback [%add_child_callback%]",
                                                     "cursor", cursor,
                                                     "child_factory", child_factory,
                                                     "add_child_callback", add_child_callback );
        }


        final OEntry entry = (OEntry) cursor.entry ();
	if ( ! this.onode.id ().equals ( entry.onodeRef () ) )
	{
	    throw new RecordSecurityException ( "OEntry [%entry%] pointing to ONode [%onode_ref%] cannot operate on ONode data for [%onode_id%]",
						"entry", entry,
						"onode_ref", entry.onodeRef (),
						"onode_id", this.onode.id () );
	}

        final Credentials credentials = cursor.owner ();
	// In Linux vfs_readdir only checks read, so we don't check
	// execute permission either.
	Permissions<RecordFlag> requested_permissions =
	    new RecordPermissions ( credentials,
				    RecordPermission.READ );
	Permissions<RecordFlag> granted_permissions =
	    this.onode.security ().request ( requested_permissions );
	if ( ! granted_permissions.isAllowed ( requested_permissions ) )
	{
            this.module.traceFail ( "VirtualONodeData.readObject()" );
	    throw new RecordSecurityException ( "Access denied: cursor [%cursor%] cannot read data from ONode [%onode%]",
						"cursor", cursor,
						"onode", this.onode );
	}

        final RecordLock mutex_lock =
            new RecordLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Call the ONode data's readObject().
	    if ( ! ( this.onodeData instanceof ObjectRecord ) )
            {
                this.module.traceFail ( "VirtualONodeData.readObject()" );
                throw new RecordOperationException ( "ReadObject not supported by ONode [%onode%] data [%onode_data%] at [%entry%] while trying to read object contents from cursor [%cursor%] with child factory [%child_factory%] callback [%add_child_callback%]",
                                                     "onode", this.onode,
                                                     "onode_data", this.onodeData,
                                                     "entry", entry,
                                                     "cursor", cursor,
                                                     "child_factory", child_factory,
                                                     "add_child_callback", add_child_callback );
            }

            ObjectRecord hierarchical_object = (ObjectRecord) this.onodeData;
            try
            {
                hierarchical_object.readObject ( cursor,
						 child_factory,
						 add_child_callback );
            }
            catch ( RecordOperationException e )
            {
                this.module.traceFail ( "VirtualONodeData.readObject()" );
                throw e;
            }

            // Touch the metadata time(s) for the ONode
            // (accessed time, modified time, and so on).
            Time accessed_time = AbsoluteTime.now ();
	    ONodeMetadata metadata = this.onode.metadata ();
            try
            {
		metadata.writeValue ( credentials,
				      metadata.metaTimeAccessed (),
				      accessed_time );
            }
            catch ( I18nIllegalArgumentException e )
            {
                this.module.traceFail ( "VirtualONodeData.readObject()" );
                throw new RecordOperationException ( "Failed to update times for entry [%entry%] onode [%onode%] to [%time%]",
                                                     "entry", entry,
                                                     "onode", this.onode,
                                                     "time", accessed_time,
                                                     "cause", e );
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONodeData.readObject()" );
    }
    !!! */


    /**
     * @see musaico.kernel.objectsystem.Record#region(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public Region region (
                          Cursor cursor
                          )
        throws RecordOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONodeData.region()" );

        if ( cursor == null )
        {
            this.module.traceFail ( "VirtualONodeData.region()" );
            throw new I18nIllegalArgumentException ( "Cannot return the region covered by ONode with Cursor [%cursor%]",
                                                     "cursor", cursor );
        }

        final OEntry entry = (OEntry) cursor.entry ();
	if ( ! this.onode.id ().equals ( entry.onodeRef () ) )
	{
	    throw new RecordSecurityException ( "OEntry [%entry%] pointing to ONode [%onode_ref%] cannot operate on ONode data for [%onode_id%]",
						"entry", entry,
						"onode_ref", entry.onodeRef (),
						"onode_id", this.onode.id () );
	}

        final Credentials credentials = cursor.owner ();
	// !!! No permissions currently required for getting the region.

        final Region onode_region;

        final RecordLock mutex_lock =
            new RecordLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Call the ONode data's region(), if any.
            // If the ONode supports transactions, and the Cursor
            // is currently involved in a transaction, then the
            // Region returned might not be the Region that everyone
            // else sees.
	    try
            {
		onode_region = this.onodeData.region ( cursor );
	    }
	    catch ( RecordOperationException e )
            {
		this.module.traceFail ( "VirtualONodeData.region()" );
		throw e;
            }

            // region () does not modify any of the ONode's metadata times.
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONodeData.region()" );

        return onode_region;
    }


    /**
     * @see musaico.kernel.objectsystem.records.Relation#relatives(musaico.kernel.objectsystem.Cursor)
     */
    /* !!!
    public Sequence<Path> relatives (
                                     Cursor cursor
                                     )
        throws I18nIllegalArgumentException,
               RecordOperationException
    {
        this.module.traceEnter ( "VirtualONodeData.relatives()" );

        if ( cursor == null )
        {
            this.module.traceFail ( "VirtualONodeData.relatives()" );
            throw new I18nIllegalArgumentException ( "Cannot determine relatives of relation from Cursor [%cursor%]",
                                                     "cursor", cursor );
        }


        final OEntry entry = (OEntry) cursor.entry ();
	if ( ! this.onode.id ().equals ( entry.onodeRef () ) )
	{
	    throw new RecordSecurityException ( "OEntry [%entry%] pointing to ONode [%onode_ref%] cannot operate on ONode data for [%onode_id%]",
						"entry", entry,
						"onode_ref", entry.onodeRef (),
						"onode_id", this.onode.id () );
	}

        final Credentials credentials = cursor.owner ();
	Permissions<RecordFlag> requested_permissions =
	    new RecordPermissions ( credentials,
				    RecordPermission.READ );
	Permissions<RecordFlag> granted_permissions =
	    this.onode.security ().request ( requested_permissions );
	if ( ! granted_permissions.isAllowed ( requested_permissions ) )
	{
            this.module.traceFail ( "VirtualONodeData.relatives()" );
	    throw new RecordSecurityException ( "Access denied: cursor [%cursor%] cannot retrieve relatives of ONode [%onode%]",
						"cursor", cursor,
						"onode", this.onode );
	}

        final Sequence<Path> relatives;

        final RecordLock mutex_lock =
            new RecordLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Call the ONode's relatives().
	    if ( ! ( this.onodeData instanceof Relation ) )
            {
                this.module.traceFail ( "VirtualONodeData.relatives()" );
                throw new RecordOperationException ( "Relatives not supported by ONode [%onode%] data [%onode_data%] at [%entry%] from cursor [%cursor%]",
                                                     "onode", this.onode,
                                                     "onode_data", this.onodeData,
                                                     "entry", entry,
                                                     "cursor", cursor );
            }

            final Relation relation = (Relation) this.onodeData;
            try
            {
                relatives = relation.relatives ( cursor );
            }
            catch ( RecordOperationException e )
            {
                this.module.traceFail ( "VirtualONodeData.relatives()" );
                throw e;
            }

            // Touch the metadata time(s) for the ONode
            // (accessed time, modified time, and so on).
            Time accessed_time = AbsoluteTime.now ();
	    ONodeMetadata metadata = this.onode.metadata ();
            try
            {
		metadata.writeValue ( credentials,
				      metadata.metaTimeAccessed (),
				      accessed_time );
            }
            catch ( I18nIllegalArgumentException e )
            {
                this.module.traceFail ( "VirtualONodeData.relatives()" );
                throw new RecordOperationException ( "Failed to update times for entry [%entry%] onode [%onode%] to [%time%]",
                                                     "entry", entry,
                                                     "onode", this.onode,
                                                     "time", accessed_time,
                                                     "cause", e );
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONodeData.relatives()" );

        return relatives;
    }
    !!! */


    /**
     * @see musaico.kernel.objectsystem.records.Relation#resolveRelation(musaico.kernel.objectsystem.Cursor)
     */
    /* !!!
    public OEntry resolveRelation (
                                   Cursor cursor
                                   )
        throws I18nIllegalArgumentException,
               RecordOperationException
    {
        this.module.traceEnter ( "VirtualONodeData.resolveRelation()" );

        if ( cursor == null )
        {
            this.module.traceFail ( "VirtualONodeData.resolveRelation()" );
            throw new I18nIllegalArgumentException ( "Cannot resolve the relation at Cursor [%cursor%]",
                                                     "cursor", cursor );
        }


        final OEntry entry = (OEntry) cursor.entry ();
	if ( ! this.onode.id ().equals ( entry.onodeRef () ) )
	{
	    throw new RecordSecurityException ( "OEntry [%entry%] pointing to ONode [%onode_ref%] cannot operate on ONode data for [%onode_id%]",
						"entry", entry,
						"onode_ref", entry.onodeRef (),
						"onode_id", this.onode.id () );
	}

        final Credentials credentials = cursor.owner ();
	Permissions<RecordFlag> requested_permissions =
	    new RecordPermissions ( credentials,
				    RecordPermission.EXECUTE );
	Permissions<RecordFlag> granted_permissions =
	    this.onode.security ().request ( requested_permissions );
	if ( ! granted_permissions.isAllowed ( requested_permissions ) )
	{
            this.module.traceFail ( "VirtualONodeData.resolveRelation()" );
	    throw new RecordSecurityException ( "Access denied: cursor [%cursor%] cannot resolve relation for ONode [%onode%]",
						"cursor", cursor,
						"onode", this.onode );
	}

        final OEntry resolved_relation_entry;

        final RecordLock mutex_lock =
            new RecordLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Call the ONode's resolveRelation().
	    if ( ! ( this.onodeData instanceof Relation ) )
            {
                this.module.traceFail ( "VirtualONodeData.resolveRelation()" );
                throw new RecordOperationException ( "ResolveRelation not supported by ONode [%onode%] data [%onode_data%] at [%entry%] while trying to resolve the relation at cursor [%cursor%]",
                                                     "onode", this.onode,
                                                     "onode_data", this.onodeData,
                                                     "entry", entry,
                                                     "cursor", cursor );
            }

            Relation relation = (Relation) this.onodeData;
            try
            {
                resolved_relation_entry =
                    relation.resolveRelation ( cursor );
            }
            catch ( RecordOperationException e )
            {
                this.module.traceFail ( "VirtualONodeData.resolveRelation()" );
                throw e;
            }

            // Touch the metadata time(s) for the ONode
            // (accessed time, modified time, and so on).
            Time accessed_time = AbsoluteTime.now ();
	    ONodeMetadata metadata = this.onode.metadata ();
            try
            {
		metadata.writeValue ( credentials,
				      metadata.metaTimeAccessed (),
				      accessed_time );
            }
            catch ( I18nIllegalArgumentException e )
            {
                this.module.traceFail ( "VirtualONodeData.resolveRelation()" );
                throw new RecordOperationException ( "Failed to update times for entry [%entry%] onode [%onode%] to [%time%]",
                                                     "entry", entry,
                                                     "onode", this.onode,
                                                     "time", accessed_time,
                                                     "cause", e );
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONodeData.resolveRelation()" );

        return resolved_relation_entry;
    }
    !!! */


    /**
     * @see musaico.kernel.objectsystem.Record#security()
     */
    @Override
    public Security security ()
    {
	return this.onode.security ();
    }


    /**
     * @see musaico.kernel.objectsystem.Record#seek(musaico.kernel.objectsystem.Cursor,musaico.io.Position)
     */
    @Override
    public Position seek (
                          Cursor cursor,
                          Position position
                          )
        throws RecordOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONodeData.seek()" );

        if ( cursor == null
             || position == null )
        {
            this.module.traceFail ( "VirtualONodeData.seek()" );
            throw new I18nIllegalArgumentException ( "Cannot seek with cursor [%cursor%] to position [%position%]",
                                                     "cursor", cursor,
                                                     "position", position );
        }

        final OEntry entry = (OEntry) cursor.entry ();
	if ( ! this.onode.id ().equals ( entry.onodeRef () ) )
	{
	    throw new RecordSecurityException ( "OEntry [%entry%] pointing to ONode [%onode_ref%] cannot operate on ONode data for [%onode_id%]",
						"entry", entry,
						"onode_ref", entry.onodeRef (),
						"onode_id", this.onode.id () );
	}

        final Credentials credentials = cursor.owner ();
	// !!! No permissions currently required for seeking.

        final Position actual_position;

        final RecordLock mutex_lock =
            new RecordLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Call the ONode data's seek().
	    try
            {
		actual_position = this.onodeData.seek ( cursor, position );
	    }
	    catch ( RecordOperationException e )
            {
		this.module.traceFail ( "VirtualONodeData.seek()" );
		throw e;
            }

            cursor.position ( actual_position );

            // seek () does not modify any of the ONode's metadata times.
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONodeData.seek()" );

        return actual_position;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#space()
     */
    @Override
    public Space space ()
    {
        this.module.traceEnter ( "VirtualONodeData.space()" );

	Space space = this.onodeData.space ();

        this.module.traceExit ( "VirtualONodeData.space()" );

        return space;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#sync(musaico.kernel.objectsystem.Cursor,boolean)
     */
    @Override
    public void sync (
                      Cursor cursor,
                      boolean is_metadata_only
                      )
        throws RecordOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONodeData.sync()" );

        if ( cursor == null )
        {
            this.module.traceFail ( "VirtualONodeData.sync()" );
            throw new I18nIllegalArgumentException ( "Cannot sync with cursor [%cursor%] metadata only? [%is_metadata_only%]",
                                                     "cursor", cursor,
                                                     "is_metadata_only", is_metadata_only );
        }

        final OEntry entry = (OEntry) cursor.entry ();
	if ( ! this.onode.id ().equals ( entry.onodeRef () ) )
	{
	    throw new RecordSecurityException ( "OEntry [%entry%] pointing to ONode [%onode_ref%] cannot operate on ONode data for [%onode_id%]",
						"entry", entry,
						"onode_ref", entry.onodeRef (),
						"onode_id", this.onode.id () );
	}

        final Credentials credentials = cursor.owner ();
        final ONode onode;
        // !!! No permissions currently required for sync'ing.

        final RecordLock mutex_lock =
            new RecordLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Call the ONode's sync() to do the work.
            // Some ONode datas just steps through all
            // the pages in the ONode's paged area's page
            // table, sync'ing it up with the backing swap space(s).
            // If there are multiple levels of swap spaces, this
            // operation could take a while!
            try
            {
                this.onodeData.sync ( cursor, is_metadata_only );
            }
            catch ( RecordOperationException e )
            {
                this.module.traceFail ( "VirtualONodeData.sync()" );
                throw e;
            }

            // sync () does not modify any of the ONode's metadata times.
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONodeData.sync()" );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#write(musaico.kernel.objectsystem.Cursor,musaico.buffer.Buffer,musaico.io.Region)
     */
    @Override
    public Region write (
                         Cursor cursor,
                         Buffer write_fields,
                         Region write_from_region
                         )
        throws RecordOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "VirtualONodeData.write()" );

        if ( cursor == null
             || write_fields == null
             || write_from_region == null )
        {
            this.module.traceFail ( "VirtualONodeData.write()" );
            throw new I18nIllegalArgumentException ( "Cannot write ONode with Cursor [%cursor%] write buffer [%write_fields%] write buffer region [%write_from_region%]",
                                                     "cursor", cursor,
                                                     "write_fields", write_fields,
                                                     "write_from_region", write_from_region );
        }

        final OEntry entry = (OEntry) cursor.entry ();
	if ( ! this.onode.id ().equals ( entry.onodeRef () ) )
	{
	    throw new RecordSecurityException ( "OEntry [%entry%] pointing to ONode [%onode_ref%] cannot operate on ONode data for [%onode_id%]",
						"entry", entry,
						"onode_ref", entry.onodeRef (),
						"onode_id", this.onode.id () );
	}

        final Credentials credentials = cursor.owner ();
	Permissions<RecordFlag> requested_permissions =
	    new RecordPermissions ( credentials,
				    RecordPermission.WRITE );
	Permissions<RecordFlag> granted_permissions =
	    this.onode.security ().request ( requested_permissions );
	if ( ! granted_permissions.isAllowed ( requested_permissions ) )
	{
            this.module.traceFail ( "VirtualONodeData.write()" );
	    throw new RecordSecurityException ( "Access denied: cursor [%cursor%] cannot write data to ONode [%onode%]",
						"cursor", cursor,
						"onode", this.onode );
	}

        final Region actual_buffer_region;

	// Don't lock the ONode for reads and writes,
	// since they are likely to block.
	//
	// Call the ONode's write().
	try
        {
	    actual_buffer_region =
		this.onodeData.write ( cursor,
				       write_fields,
				       write_from_region );
	}
	catch ( RecordOperationException e )
        {
	    this.module.traceFail ( "VirtualONodeData.write()" );
	    throw e;
	}

        final RecordLock mutex_lock =
            new RecordLock ( this.onode.mutex ( credentials ) );
        mutex_lock.lock ();
        try
        {
            // Touch the metadata time(s) for the ONode
            // (accessed time, modified time, and so on).
            Time accessed_time = AbsoluteTime.now ();
	    ONodeMetadata metadata = this.onode.metadata ();
            try
            {
		metadata.writeValue ( credentials,
				      metadata.metaTimeAccessed (),
				      accessed_time );
		metadata.writeValue ( credentials,
				      metadata.metaTimeModified (),
				      accessed_time );
            }
            catch ( I18nIllegalArgumentException e )
            {
                this.module.traceFail ( "VirtualONodeData.write()" );
                throw new RecordOperationException ( "Failed to update times for entry [%entry%] onode [%onode%] to [%time%]",
                                                     "entry", entry,
                                                     "onode", this.onode,
                                                     "time", accessed_time,
                                                     "cause", e );
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }

        this.module.traceExit ( "VirtualONodeData.write()" );

        return actual_buffer_region;
    }
}
