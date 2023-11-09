package musaico.kernel.common.oentries;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.i18n.log.Level;

import musaico.io.Identifier;
import musaico.io.Path;
import musaico.io.Progress;
import musaico.io.Sequence;

import musaico.io.progresses.SimpleProgress;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleOperationException;

import musaico.kernel.objectsystem.onode.OEntry;
import musaico.kernel.objectsystem.onode.OEntryOperationException;
import musaico.kernel.objectsystem.onode.ONode;
import musaico.kernel.objectsystem.onode.ONodeIdentifier;
import musaico.kernel.objectsystem.onode.ONodeOperationException;

import musaico.kernel.objectsystem.superblock.SuperBlock;
import musaico.kernel.objectsystem.superblock.SuperBlockIdentifier;

import musaico.security.Credentials;


/**
 * <p>
 * An entry in an object hierarchy.  Like a dentry in Linux.
 * </p>
 *
 *
 * <p>
 * In Java, every OEntry must be Serializable in order to play
 * nicely over RMI, even if the object system type to which the OEntry
 * belongs is not distributable.  This is because a distributed
 *kernel may distribute OEntries to other nodes, each OEntry
 * referring back to fixed nodes on the network.
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
public class VirtualOEntry
    implements OEntry, Serializable
{
    /** Lock all critical sections on this token: */
    private final Serializable lock = new String ();

    /** The module to which this OEntry belongs.  Among other things,
     *  this is where we get our permissions to lookup our own
     *  parent and children. */
    private final Module module;

    /** The root path to the parent OEntry, possibly this if it is the
     *  root of the tree.  We only store a reference (rather than the
     *  actual OEntry) to avoid serializing a massive tree every time
     *  this OEntry is serialized. */
    private final Path parentPath;

    /** The name of this OEntry, such as "bar" for an OEntry at
     *  path "/foo/bar". */
    private final String name;

    /** The actual OEntry, whose name and path may be different
     *  from ours, but whose children we re-create as virtual
     *  OEntries when requested.  We also delegate addChild () and
     *  removeChild () to the actual OEntry. */
    private final OEntry actualEntry;


    /**
     * <p>
     * Creates a new VirtualOEntry with the specified parent entry,
     * name and actual OEntry.
     * </p>
     *
     * @param module The module to which this OEntry belongs, providing
     *               this OEntry with permissions to access the kernel.
     *               Must not be null.
     *
     * @param parent_entry The parent OEntry, of which this OEntry is
     *                     a child.  Must not be null.  If NO_SUCH_OENTRY
     *                     is passed, then the OEntry will be its own
     *                     parent.
     *
     * @param name The name of this OEntry, such as "bar" for a
     *             new OEntry "/foo/bar".  Must not be null.
     *
     * @param actual_entry The OEntry whose children we create virtual
     *                     OEntries for.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If the parameters are invalid.
     */
    public VirtualOEntry (
                          Module module,
                          OEntry parent_entry,
                          String name,
                          OEntry actual_entry
                          )
        throws I18nIllegalArgumentException
    {
        if ( module == null
             || parent_entry == null
             || name == null
             || actual_entry == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a VirtualOEntry with module [%module%] parent [%parent_entry%] name [%name%] actual OEntry [%actual_entry%]",
                                                     "module", module,
                                                     "parent_entry", parent_entry,
                                                     "name", name,
                                                     "actual_entry", actual_entry );
        }

        this.module = module;
        this.parentPath = parent_entry.path ();
        this.name = name;
        this.actualEntry = actual_entry;
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#addChild(musaico.security.Credentials,musaico.kernel.objectsystem.OEntry)
     */
    @Override
    public void addChild (
                          Credentials credentials,
                          OEntry child_entry
                          )
        throws OEntryOperationException
    {
        if ( credentials == null
             || child_entry == null
             || child_entry == SimpleOEntry.NO_SUCH_OENTRY
             || child_entry.parent () != this )
        {
            throw new I18nIllegalArgumentException ( "OEntry [%parent_entry%] cannot add child OEntry [%child_entry%] with credentials [%credentials%]",
                                                     "parent_entry", this,
                                                     "child_entry", child_entry,
                                                     "credentials", credentials );
        }

        this.module.traceEnter ( "VirtualOEntry.addChild()" );

        // !!! Haven't decided where security for OEntries should
        // !!! be handled (in the OEntry / in the Module / in the vfs).

        // !!! HMMMM WE PROBABLY NEED TO DUPLICATE THE OENTRY WITH
        // !!!! THE CORRECT PARENT, NO???

        try
        {
            this.actualEntry.addChild ( credentials, child_entry );
        }
        catch ( OEntryOperationException e )
        {
            this.module.traceFail ( "VirtualOEntry.add()" );
            throw e;
        }

        this.module.traceExit ( "VirtualOEntry.addChild()" );
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#child(java.lang.String)
     */
    @Override
    public OEntry child (
                         String child_name
                         )
        throws OEntryOperationException
    {
        this.module.traceEnter ( "VirtualOEntry.child()" );

        OEntry actual_child = this.actualEntry.child ( child_name );

        Path path = this.path ();

        Path virtual_child_path = path.append ( child_name );
        OEntry virtual_child = new VirtualOEntry ( this.module,
                                                   this,
                                                   child_name,
                                                   actual_child );

        this.module.traceExit ( "VirtualOEntry.child()" );

        return virtual_child;
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#children()
     */
    @Override
    public Sequence<OEntry> children ()
    {
        this.module.traceEnter ( "VirtualOEntry.children()" );

        Sequence<OEntry> actual_children = this.actualEntry.children ();

        Path path = this.path ();

        List<OEntry> virtual_children = new ArrayList<OEntry> ();
        for ( OEntry actual_child : actual_children )
        {
            String child_name = actual_child.name ();
            Path virtual_child_path = path.append ( child_name );
            OEntry virtual_child = new VirtualOEntry ( this.module,
                                                       this,
                                                       child_name,
                                                       actual_child );
            virtual_children.add ( virtual_child );
        }

        OEntry [] template = new OEntry [ virtual_children.size () ];
        OEntry [] children = virtual_children.toArray ( template );

        Sequence<OEntry> children_sequence =
            new Sequence<OEntry> ( SimpleOEntry.NO_SUCH_OENTRY,
                                   children );

        this.module.traceExit ( "VirtualOEntry.children()" );

        return children_sequence;
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#create(musaico.security.Credentials,java.lang.String,musaico.kernel.objectsystem.superblock.SuperBlockIdentifier,musaico.kernel.objectsystem.onode.ONode,boolean)
     */
    public OEntry create (
                          Credentials credentials,
                          String name,
                          Identifier super_block_ref,
                          ONode onode,
                          boolean is_mounted
                          )
        throws I18nIllegalArgumentException
    {
        if ( name == null
             || super_block_ref == null
             || ! ( super_block_ref instanceof SuperBlockIdentifier )
             || onode == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a VirtualOEntry with module [%module%] parent [%parent_entry%] name [%name%] super block [%super_block_ref%] onode [%onode%] is mounted [%is_mounted%]",
                                                     "module", this.module,
                                                     "parent_entry", this,
                                                     "name", name,
                                                     "super_block_ref", super_block_ref,
                                                     "onode", onode,
                                                     "is_mounted", is_mounted );
        }

        // !!! Haven't decided where security for OEntries should
        // !!! be handled (in the OEntry / in the Module / in the vfs).

        OEntry actual_child_entry =
            this.actualEntry.create ( credentials,
                                      name,
                                      super_block_ref,
                                      onode,
                                      is_mounted );

        return new VirtualOEntry ( this.module, // module
                                   this, // parent
                                   name,
                                   actual_child_entry );
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#isMounted()
     */
    @Override
    public boolean isMounted ()
    {
        return this.actualEntry.isMounted ();
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#name()
     */
    @Override
    public String name ()
    {
        return this.name;
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#onodeRef()
     */
    @Override
    public ONodeIdentifier onodeRef ()
    {
        return this.actualEntry.onodeRef ();
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#parent()
     */
    @Override
    public OEntry parent ()
    {
        this.module.traceEnter ( "VirtualOEntry.parent()" );

        // Convert the path to an OEntry.
        OEntry parent;
        try
        {
            Credentials credentials = this.module.credentials ();
            Progress progress = new SimpleProgress ();
            ONode onode = this.module.getKernelObject ( this.onodeRef () );
            parent = onode.lookup ( credentials,
                                    this,
                                    this.parentPath );
        }
        catch ( ModuleOperationException e )
        {
            this.module.logger ().log ( Level.ERROR,
                                        Locale.getDefault (),
                                        "Failed to retrieve parent OEntry from [%entry%]",
                                        "entry", this,
                                        "cause", e );
            this.module.traceFail ( "VirtualOEntry.parent()" );
            return SimpleOEntry.NO_SUCH_OENTRY;
        }
        catch ( ONodeOperationException e )
        {
            this.module.logger ().log ( Level.ERROR,
                                        Locale.getDefault (),
                                        "Failed to retrieve parent OEntry from [%entry%]",
                                        "entry", this,
                                        "cause", e );
            this.module.traceFail ( "VirtualOEntry.parent()" );
            return SimpleOEntry.NO_SUCH_OENTRY;
        }

        this.module.traceExit ( "VirtualOEntry.parent()" );

        return parent;
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#path()
     */
    @Override
    public Path path ()
    {
        return this.parentPath.append ( this.name () );
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#removeChild(musaico.security.Credentials,musaico.kernel.objectsystem.OEntry)
     */
    @Override
    public void removeChild (
                             Credentials credentials,
                             OEntry child_entry
                             )
        throws OEntryOperationException
    {
        if ( credentials == null
             || child_entry == null
             || child_entry == SimpleOEntry.NO_SUCH_OENTRY
             || child_entry.parent () != this )
        {
            throw new I18nIllegalArgumentException ( "OEntry [%parent_entry%] cannot remove child OEntry [%child_entry%] with credentials [%credentials%]",
                                                     "parent_entry", this,
                                                     "child_entry", child_entry,
                                                     "credentials", credentials );
        }

        this.module.traceEnter ( "VirtualOEntry.remove()" );

        // Let the actual OEntry check the credentials.

        // !!! HMMMM WE PROBABLY NEED TO DUPLICATE THE OENTRY WITH
        // !!!! THE CORRECT PARENT, NO???

        try
        {
            this.actualEntry.removeChild ( credentials, child_entry );
        }
        catch ( OEntryOperationException e )
        {
            this.module.traceFail ( "VirtualOEntry.remove()" );
            throw e;
        }

        this.module.traceExit ( "VirtualOEntry.remove()" );
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#superBlockRef()
     */
    @Override
    public SuperBlockIdentifier superBlockRef ()
    {
        Identifier actual_entry_super_block_ref =
            this.actualEntry.superBlockRef ();
        if ( actual_entry_super_block_ref instanceof SuperBlockIdentifier )
        {
            return (SuperBlockIdentifier) actual_entry_super_block_ref;
        }
        else
        {
            // Probably ONode.NONE.getParentNamespace(), which is
            // unfortunately not defined as a SuperBlockIdentifier.
            return SuperBlockIdentifier.NONE;
        }
    }
}
