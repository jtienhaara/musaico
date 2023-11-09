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
 * A flat ONode, with only fields, no child ONodes, which does the
 * simplest things possible without taking advantage of object
 * system-specific knowledge.
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
public class GenericFlatONode
    extends AbstractONode
    implements Serializable
{
    /** The Module which created this ONode, typically a Module
     *  providing some sort of object system. */
    private final Module module;


    /**
     * <p>
     * Creates a new GenericFlatONode with the specified parameters.
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
    public GenericFlatONode (
                             Module module,
                             SuperBlock super_block,
                             ONodeIdentifier id,
                             Security<RecordFlag> security,
                             Record data,
                             ONodeMetadata metadata
                             )
    {
        super ( module,
                super_block,
                id,
                security,
                data,
                metadata );

        this.module = module;
    }


    /**
     * @see musaico.kernel.common.onodes.AbstractONode#addSelfToObjectData(musico.kernel.objectsystem.onode.ONode)
     */
    protected void addSelfToObjectData (
                                        ONode parent_onode
                                        )
        throws ONodeOperationException
    {
        throw new ONodeOperationException ( "!!! NOT YET IMPLEMENTED" );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#lookup(musaico.security.Credentials,musaico.kernel.objectsystem.OEntry,musaico.io.Path)
     */
    @Override
    public OEntry lookup (
                          Credentials credentials,
                          OEntry entry,
                          Path path
                          )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "GenericFlatONode.lookup()" );

        this.module.traceFail ( "GenericFlatONode.lookup()" );
        throw new ONodeOperationException ( "Lookup not supported by ONode [%onode%] ops [%ops%] at [%entry%] while looking up path [%path%]",
                                            "onode", entry.onodeRef (),
                                            "ops", this,
                                            "entry", entry,
                                            "path", path );
    }


    /**
     * @see musaico.kernel.common.onodes.AbstractONode#removeSelfFromObjectData(musico.kernel.objectsystem.onode.ONode)
     */
    protected void removeSelfFromObjectData (
                                             ONode parent_onode
                                             )
        throws ONodeOperationException
    {
        throw new ONodeOperationException ( "!!! NOT YET IMPLEMENTED" );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "FlatONode_" + this.id ();
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#truncate(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,musaico.io.Region)
     */
    @Override
    public void truncate (
                          Credentials credentials,
                          OEntry entry,
                          Region truncate_everything_outside_of_region
                          )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "GenericFlatONode.truncate()" );

        // This ONode does not allow truncating.
        this.module.traceFail ( "GenericFlatONode.truncate()" );
        throw new ONodeOperationException ( "Truncate not supported by ONode [%onode%] ops [%ops%] at [%entry%] while truncating to region [%truncate_everything_outside_of_region%]",
                                            "onode", entry.onodeRef (),
                                            "ops", this,
                                            "entry", entry,
                                            "truncate_everything_outside_of_region", truncate_everything_outside_of_region );
    }
}
