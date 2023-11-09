package musaico.kernel.common.onodes;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;


import musaico.buffer.Buffer;
import musaico.buffer.NullBuffer;

import musaico.field.Attribute;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Path;

import musaico.kernel.common.oentries.SimpleOEntry;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleOperationException;

import musaico.kernel.objectsystem.Record;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordPermissions;

import musaico.kernel.objectsystem.onode.OEntry;
import musaico.kernel.objectsystem.onode.ONode;
import musaico.kernel.objectsystem.onode.ONodeOperationException;
import musaico.kernel.objectsystem.onode.ONodeIdentifier;
import musaico.kernel.objectsystem.onode.ONodeMetadata;
import musaico.kernel.objectsystem.onode.ONodePermission;
import musaico.kernel.objectsystem.onode.ONodeReferences;
import musaico.kernel.objectsystem.onode.ONodeSecurityException;
import musaico.kernel.objectsystem.onode.SimpleONodeReferences;

import musaico.kernel.objectsystem.onodes.ObjectONode;

import musaico.kernel.objectsystem.superblock.SuperBlock;
import musaico.kernel.objectsystem.superblock.SuperBlockIdentifier;

import musaico.mutex.Mutex;

import musaico.region.Region;

import musaico.security.Credentials;
import musaico.security.Permissions;
import musaico.security.Security;

import musaico.state.Machine;

import musaico.time.Time;


/**
 * <p>
 * A basic ONode, which does the simplest things possible without
 * taking advantage of object system-specific knowledge.
 * </p>
 *
 * <p>
 * Implementors can extend AbstractONode to provide the following
 * ONode functionality at a minimum:
 * </p>
 *
 * <ul>
 *   <li> <code> lookup () </code> </li>
 *   <li> <code> truncate () </code> </li>
 * </ul>
 *
 * <p>
 * The following support methods, defined by AbstractONode itself
 * in order to implement link, unlink and move, must also be provided:
 * </p>
 *
 * <ul>
 *   <li> <code> addSelfToObjectData () </code> </li>
 *   <li> <code> removeSelfFromObjectData () </code> </li>
 * </ul>
 *
 * <p>
 * These support functions will only be called with ONodes from
 * the same object system, possibly the super block's
 * root mount location.
 * </p>
 *
 * <p>
 * Based loosely on the "simple" inode defined in Linux's
 * <code> fs/inode.c </code> file.
 * </p>
 *
 *
 * <p>
 * Because object systems can conceivably be distributed, every ONode
 * must be Serializable in order to play nicely over RMI.
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
public abstract class AbstractONode
    implements ONode, Serializable
{
    /** The Module which created this ONode, typically a Module
     *  providing some sort of object system. */
    private final Module module;

    /** To lock this ONode during an operation, get the Mutex
     *  and create a MutexLock (or an ONodeLock or a RecordLock
     *  and so on) on it and lock the Mutex. */
    private final Mutex mutex;

    /** The identifier which is unique throughout the kernel.
     *  Typically includes the unique identifier of the
     *  super block, plus an extra token to uniquely identify
     *  this ONode within the SuperBlock.  Akin to the inode
     *  number in a UNIX filesystem. */
    private final ONodeIdentifier id;

    /** The number of hard links, reading and writing Cursors
     *  and so on that are currently referring to this ONode. */
    private final ONodeReferences references =
        new SimpleONodeReferences ();

    /** Security settings for this ONode, such as
     *  "rwxrwx---" UNIX-style permissions, or perhaps
     *  something simpler or more complex.  This
     *  Security defines what the outside world can and
     *  can't do to this ONode. */
    private final Security<RecordFlag> security;

    /** The object system (super block) to which this
     *  ONode belongs. */
    private final SuperBlock superBlock;

    /** The backing data for this ONode.  Possibly a persistent
     *  store with swapping into in-memory fields, or possibly
     *  something simpler (pure physical memory) or more complex. */
    private final Record data;

    /** Allows us, and whoever we give this object to, to
     *  modify our metadata times, fields used and free,
     *  and so on. */
    private final ONodeMetadata metadata;


    /**
     * <p>
     * Creates a new AbstractONode with the specified parameters.
     * </p>
     *
     * <p>
     * This ONode does NOT fill in any metadata.  That is left
     * to the caller, and this ONode's operations methods.
     * </p>
     *
     * @param module The module which provides this ONode access
     *               to the kernel and other kernel modules.
     *               Must not be null.
     *
     * @param super_block The SuperBlock which created this ONode.
     *                    Must not be null.
     *
     * @param id The unique identifier of this ONode within the SuperBlock.
     *           Must not be null.
     *
     * @param security The security manager for this ONode.
     *                 For example, UNIX-like security.
     *                 Must not be null.
     *
     * @param data The data area (such as a segment and paged area
     *             swapping between block drivers and memory) backing
     *             this ONode.  Must not be Record.NONE or any other
     *             NoRecord.  Must not be null.
     *
     * @param metadata The metadata area for this ONode.
     *                 Can be paged and swapped, or just in
     *                 physical memory, depending on how bloated
     *                 the kernel is permitted to be.
     *                 Must not be ONodeMetadata.NONE or any other
     *                 NoONodeMetadata.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     */
    public AbstractONode (
                          Module module,
                          SuperBlock super_block,
                          ONodeIdentifier id,
                          Security<RecordFlag> security,
                          Record data,
                          ONodeMetadata metadata
                          )
    {
        if ( module == null
             || super_block == null
             || id == null
             || security == null
             || data == null
             || metadata == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create an AbstractONode with module [%module%] super_block [%super_block%] id [%id%] security [%security%] data [%data%] metadata [%metadata%]",
                                                     "module", module,
                                                     "super_block", super_block,
                                                     "id", id,
                                                     "security", security,
                                                     "data", data,
                                                     "metadata", metadata );
        }

        this.module = module;
        this.id = id;
        this.superBlock = super_block;
        this.security = security;
        this.data = data;
        this.metadata = metadata;

        this.mutex = new Mutex ( this );
    }


    /**
     * <p>
     * Adds a reference to this ONode to the specified parent ONode's
     * data.
     * </p>
     *
     * <p>
     * Once the data has been added, the caller is responsible for
     * updating the OEntry tree, reference counts and so on.
     * This method only ever updates the parent ONode's data
     * and metadata, nothing else.
     * <p>
     *
     * @param parent_onode The parent ONode to which a reference to this
     *                     child ONode will be added.  Must not be null.
     *
     * @throws ONodeOperationException If this ONode cannot be added
     *                                 to the parent due to a security
     *                                 violation, underlying driver
     *                                 problem and so on.
     */
    protected abstract void addSelfToObjectData (
                                                 ONode parent_onode
                                                 )
        throws ONodeOperationException;


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#data()
     */
    public Record data ()
    {
        return this.data;
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#id()
     */
    @Override
    public ONodeIdentifier id ()
    {
        return this.id;
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#link(musaico.security.Credentials,musaico.kernel.objectsystem.OEntry)
     */
    @Override
    public long link (
                      Credentials credentials,
                      OEntry entry
                      )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "AbstractONode.link()" );

        // Don't do anything.
        // Just return the existing reference count.
        final long num_references_to_onode =
            this.references ().count ();

        this.module.traceExit ( "AbstractrONode.link()" );

        return num_references_to_onode;
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#metadata()
     */
    @Override
    public ONodeMetadata metadata ()
    {
        return this.metadata;
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#move(musaico.security.Credentials,musaico.kernel.objectsystem.OEntry,musaico.kernel.objectsystem.OEntry)
     */
    @Override
    public void move (
                      Credentials credentials,
                      OEntry old_entry,
                      OEntry new_entry
                      )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "AbstractONode.move()" );

        if ( ! old_entry.onodeRef ().equals ( new_entry.onodeRef () ) )
        {
            throw new ONodeOperationException ( "Cannot move [%old_entry%] ONode id [%old_onode_id%] to [%new_entry%] [%new_onode_id%]",
                                                "old_entry", old_entry,
                                                "old_onode_id", old_entry.onodeRef (),
                                                "new_entry", new_entry,
                                                "new_onode_id", new_entry.onodeRef () );
        }

        OEntry old_parent_entry = old_entry.parent ();
        OEntry new_parent_entry = new_entry.parent ();

        final ONode old_parent_onode;
        final ONode new_parent_onode;
        try
        {
            // Get the old and new parent ONodes.
            old_parent_onode =
                this.module.getKernelObject ( old_parent_entry.onodeRef () );
            if ( old_entry.onodeRef ().equals ( new_entry.onodeRef () ) )
            {
                // Rename.
                new_parent_onode = old_parent_onode;
            }
            else
            {
                // Relocate.
                new_parent_onode =
                    this.module.getKernelObject ( new_parent_entry.onodeRef () );
            }

            // Make sure the old and new parent ONodes are both
            // hierarchical objects.
            if ( ! ( old_parent_onode instanceof ObjectONode ) )
            {
                throw new ONodeOperationException ( "OEntry [%entry%] ONode [%onode%] is not an hierarchical object",
                                                    "entry", old_parent_entry,
                                                    "onode", old_parent_onode );
            }
            else if ( ! ( new_parent_onode instanceof ObjectONode ) )
            {
                throw new ONodeOperationException ( "OEntry [%entry%] ONode [%onode%] is not an hierarchical object",
                                                    "entry",new_parent_entry,
                                                    "onode", new_parent_onode );
            }

            // If we're moving within the same object system,
            // we don't need to do anything special -- just
            // move the child ONode from the old parent and / or
            // old name to the new parent and / or new name.
            //
            // But if we're moving across object systems, we've
            // got more work to do.  We need to copy the data
            // over.
            if ( ! new_parent_onode.superBlockRef ().equals ( old_parent_onode.superBlockRef () ) )
            {
                throw new ONodeOperationException ( "!!!! MOVING BETWEEN OBJECT SYSTEMS NOT IMPLEMENTED QUITE YET..." );
            }

            // If the child ONode is an hierachical object, then
            // delete its ".." ONode.
            if ( this instanceof ObjectONode )
            {
                OEntry dot_dot_entry = this.lookup ( credentials,
                                                     old_entry,
                                                     new Path ( ".." ) );
                old_parent_onode.unlink ( credentials,
                                          dot_dot_entry );
            }

            // Remove this ONode from the old parent.
            this.removeSelfFromObjectData ( old_parent_onode );
            this.unlink ( credentials, old_entry );

            // Add this ONode to the new parent.
            this.addSelfToObjectData ( new_parent_onode );
            this.link ( credentials, new_entry );

            // Now if the child ONode is an hierachical object, then
            // add its ".." ONode.
            if ( this instanceof ObjectONode )
            {
                final SuperBlockIdentifier new_super_block_id =
                    (SuperBlockIdentifier) new_parent_onode.superBlockRef ();
                OEntry dot_dot_entry =
                    new SimpleOEntry ( this.module,
                                       new_entry,
                                       "..",
                                       new_super_block_id,
                                       this.id (),
                                       new_entry.isMounted () );
                new_parent_onode.link ( credentials,
                                        dot_dot_entry );
            }
        }
        catch ( ONodeOperationException e )
        {
            this.module.traceFail ( "AbstractONode.move()" );
            throw e;
        }
        catch ( ModuleOperationException e )
        {
            this.module.traceFail ( "AbstractONode.move()" );
            throw new ONodeOperationException ( e );
        }

        this.module.traceExit ( "AbstractONode.move()" );
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#mutex(musaico.security.Credentials)
     */
    @Override
    public Mutex mutex (
                        Credentials credentials
                        )
        throws ONodeSecurityException
    {
        Permissions<RecordFlag> requested_permissions =
            new RecordPermissions ( credentials,
                                    this.id (),
                                    ONodePermission.LOCK_MUTEX );

        Permissions<RecordFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new ONodeSecurityException ( "Access denied: [%credentials%] may not lock the mutex of ONode [%onode%]",
                                               "credentials", credentials,
                                               "onode", this );
        }

        return this.mutex;
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#references()
     */
    @Override
    public ONodeReferences references ()
    {
        return this.references;
    }


    /**
     * <p>
     * Removes a reference to this ONode from the specified parent ONode's
     * data.
     * </p>
     *
     * <p>
     * Once the data has been removed, the caller is responsible for
     * updating the OEntry tree, reference counts and so on.
     * This method only ever updates the parent ONode's data
     * and metadata, nothing else.
     * <p>
     *
     * @param parent_onode The parent ONode from which a reference to this
     *                     child ONode will be removed.  Must not be null.
     */
    protected abstract void removeSelfFromObjectData (
                                                      ONode parent_onode
                                                      )
        throws ONodeOperationException;


    /**
     * @see musaico.kernel.objectsystem.ONode#security()
     */
    @Override
    public Security<RecordFlag> security ()
    {
        return this.security;
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#superBlockRef()
     */
    @Override
    public SuperBlockIdentifier superBlockRef ()
    {
        return this.superBlock.id ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "ONode_" + this.id;
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#unlink(musaico.security.Credentials,musaico.kernel.objectsystem.OEntry)
     */
    @Override
    public long unlink (
                        Credentials credentials,
                        OEntry entry
                        )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "AbstractONode.unlink()" );

        // Don't do anything.
        // Just return the existing reference count.
        final long num_references_to_onode =
            this.references ().count ();

        this.module.traceExit ( "AbstractONode.unlink()" );

        return num_references_to_onode;
    }
}
