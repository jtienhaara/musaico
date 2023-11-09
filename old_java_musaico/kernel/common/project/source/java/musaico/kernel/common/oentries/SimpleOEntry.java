package musaico.kernel.common.oentries;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


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
public class SimpleOEntry
    implements OEntry, Serializable
{
    /** No such OEntry.  Used to denote things like "no more children". */
    public static final OEntry NO_SUCH_OENTRY =
        new SimpleOEntry ();


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

    /** A reference to the SuperBlock (object system) to which
     *  this OEntry belongs. */
    private final SuperBlockIdentifier superBlockRef;

    /** A reference to the ONode which contains this OEntry's data. */
    private final ONodeIdentifier onodeRef;

    /** Is this OEntry mounted?  Or is it the root of an unmounted
     *  object system? */
    private final boolean isMounted;

    /** Names of the children of this OEntry.  We only store
     *  names in order to avoid serializing a massive tree of
     *  OEntries every time a distributed kernel or object system passes
     *  us across the network.  Currently the names are not in any
     *  order; we may want to change this to be the order of when
     *  the children were added...??? */
    private final Set<String> childrenNames =
        new HashSet<String> ();


    /**
     * <p>
     * Creates a new SimpleOEntry with the specified parent entry,
     * name, reference to the parent superblock, the ONode
     * containing this entry's data, and mounted flag.
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
     * @param super_block_ref A reference to the superblock
     *                       (object system) to which this
     *                       OEntry belongs.  Must not be null.
     *
     * @param onode The ONode containing the data for this OEntry.
     *              Must not be null.
     *
     * @param is_mounted True if this OEntry is part of a mounted
     *                   object system, false if it is the root
     *                   of an unmounted object system.
     *
     * @throws I18nIllegalArgumentException If the parameters are invalid.
     */
    public SimpleOEntry (
                         Module module,
                         OEntry parent_entry,
                         String name,
                         SuperBlockIdentifier super_block_ref,
                         ONode onode,
                         boolean is_mounted
                         )
        throws I18nIllegalArgumentException
    {
        if ( module == null
             || parent_entry == null
             || name == null
             || super_block_ref == null
             || onode == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleOEntry with module [%module%] parent [%parent_entry%] name [%name%] super block [%super_block_ref%] onode [%onode%] is mounted [%is_mounted%]",
                                                     "module", module,
                                                     "parent_entry", parent_entry,
                                                     "name", name,
                                                     "super_block_ref", super_block_ref,
                                                     "onode", onode,
                                                     "is_mounted", is_mounted );
        }

        this.module = module;

        if ( parent_entry == SimpleOEntry.NO_SUCH_OENTRY )
        {
            this.parentPath = new Path ( "/" );
        }
        else
        {
            this.parentPath = parent_entry.path ();
        }

        this.name = name;
        this.superBlockRef = super_block_ref;
        this.onodeRef = onode.id ();
        this.isMounted = is_mounted;
    }


    /**
     * <p>
     * Creates a new SimpleOEntry with the specified parent entry,
     * name, reference to the parent superblock, reference to the
     * ONode containing this entry's data, and mounted flag.
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
     * @param super_block_ref A reference to the superblock
     *                       (object system) to which this
     *                       OEntry belongs.  Must not be null.
     *
     * @param onode_ref A reference to the ONode containing the data
     *                  for this OEntry.  Must not be null.
     *
     * @param is_mounted True if this OEntry is part of a mounted
     *                   object system, false if it is the root
     *                   of an unmounted object system.
     *
     * @throws I18nIllegalArgumentException If the parameters are invalid.
     */
    public SimpleOEntry (
                         Module module,
                         OEntry parent_entry,
                         String name,
                         SuperBlockIdentifier super_block_ref,
                         ONodeIdentifier onode_ref,
                         boolean is_mounted
                         )
        throws I18nIllegalArgumentException
    {
        if ( module == null
             || parent_entry == null
             || name == null
             || super_block_ref == null
             || onode_ref == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleOEntry with module [%module%] parent [%parent_entry%] name [%name%] super block [%super_block_ref%] onode [%onode%] is mounted [%is_mounted%]",
                                                     "parent_entry", parent_entry,
                                                     "name", name,
                                                     "super_block_ref", super_block_ref,
                                                     "onode", onode_ref,
                                                     "is_mounted", is_mounted );
        }

        this.module = module;

        if ( parent_entry == SimpleOEntry.NO_SUCH_OENTRY )
        {
            this.parentPath = new Path ( "/" );
        }
        else
        {
            this.parentPath = parent_entry.path ();
        }

        this.name = name;
        this.superBlockRef = super_block_ref;
        this.onodeRef = onode_ref;
        this.isMounted = is_mounted;
    }


    /**
     * <p>
     * Creates the NO_SUCH_OENTRY.  Only one of these exists.
     * </p>
     */
    private SimpleOEntry ()
    {
        this.name = "NONE";
        this.parentPath = new Path ( this.name ); // maybe should use the array constructor to get "/NONE"...?
        this.superBlockRef = SuperBlockIdentifier.NONE;
        this.onodeRef = ONodeIdentifier.NONE;
        this.isMounted = false;
        this.module = null;
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#addChild(musaico.security.Credentials,musaico.kernel.objectsystem.OEntry)
     */
    @Override
    public void addChild (
                          Credentials credentials,
                          OEntry child_entry
                          )
        throws OEntryOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "SimpleOEntry.addChild()" );

        if ( credentials == null
             || child_entry == null
             || this == SimpleOEntry.NO_SUCH_OENTRY
             || child_entry == SimpleOEntry.NO_SUCH_OENTRY
             || child_entry.parent () != this )
        {
            this.module.traceFail ( "SimpleOEntry.addChild()" );
            throw new I18nIllegalArgumentException ( "OEntry [%parent_entry%] cannot add child OEntry [%child_entry%] with credentials [%credentials%]",
                                                     "parent_entry", this,
                                                     "child_entry", child_entry,
                                                     "credentials", credentials );
        }

        // !!! Haven't decided where security for OEntries should
        // !!! be handled (in the OEntry / in the Module / in the vfs).

        final String child_name = child_entry.name ();
        synchronized ( this.lock )
        {
            if ( this.childrenNames.contains ( child_name ) )
            {
                this.module.traceFail ( "SimpleOEntry.addChild()" );
                throw new OEntryOperationException ( "Failed to add child OEntry [%child_entry%] with duplicate name [%child_name%] to parent OEntry [%parent_entry%]",
                                                     "child_entry", child_entry,
                                                     "child_name", child_name,
                                                     "parent_entry", this );
            }

            this.childrenNames.add ( child_name );
        }

        this.module.traceExit ( "SimpleOEntry.addChild()" );
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
        this.module.traceEnter ( "SimpleOEntry.child()" );

        if ( this == SimpleOEntry.NO_SUCH_OENTRY )
        {
            this.module.traceFail ( "SimpleOEntry.child()" );
            throw new OEntryOperationException ( "Failed to retrieve child [%child_name%] of OEntry [%entry%]",
                                                 "child_name", child_name,
                                                 "entry", this );
        }

        final boolean has_child;
        synchronized ( this.lock )
        {
            has_child = this.childrenNames.contains ( child_name );
        }

        if ( ! has_child )
        {
            throw new OEntryOperationException ( "Failed to retrieve child [%child_name%] of OEntry [%entry%]",
                                                 "child_name", child_name,
                                                 "entry", this );
        }

        // Convert the name to an OEntry.
        final OEntry child;
        try
        {
            Credentials credentials = this.module.credentials ();
            Progress progress = new SimpleProgress ();
            ONode onode = this.module.getKernelObject ( this.onodeRef () );

            // Create a path relative to us, using the
            // child OEntry's name.
            Path child_path = new Path ( child_name );

            // Lookup the child OEntry.
            child = onode.lookup ( credentials,
                                   this,
                                   child_path );
        }
        catch ( ModuleOperationException e )
        {
            this.module.logger ().log ( Level.ERROR,
                                        Locale.getDefault (),
                                        "Failed to retrieve child [%child_name%] of OEntry [%entry%]",
                                        "child_name", child_name,
                                        "entry", this,
                                        "cause", e );
            this.module.traceFail ( "SimpleOEntry.child()" );
            throw new OEntryOperationException ( "Failed to retrieve child [%child_name%] of OEntry [%entry%]",
                                        "child_name", child_name,
                                        "entry", this,
                                        "cause", e );
        }
        catch ( ONodeOperationException e )
        {
            this.module.logger ().log ( Level.ERROR,
                                        Locale.getDefault (),
                                        "Failed to retrieve child [%child_name%] of OEntry [%entry%]",
                                        "entry", this,
                                        "cause", e );
            this.module.traceFail ( "SimpleOEntry.child()" );
            throw new OEntryOperationException ( "Failed to retrieve child [%child_name%] of OEntry [%entry%]",
                                        "child_name", child_name,
                                        "entry", this,
                                        "cause", e );
        }

        this.module.traceExit ( "SimpleOEntry.child()" );

        return child;
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#children()
     */
    @Override
    public Sequence<OEntry> children ()
    {
        this.module.traceEnter ( "SimpleOEntry.children()" );

        if ( this == SimpleOEntry.NO_SUCH_OENTRY )
        {
            this.module.traceFail ( "SimpleOEntry.children()" );
            return new Sequence<OEntry> ( SimpleOEntry.NO_SUCH_OENTRY,
                                          new OEntry [ 0 ] );
        }

        final String [] child_names;
        synchronized ( this.lock )
        {
            String [] template =
                new String [ this.childrenNames.size () ];
            child_names = this.childrenNames.toArray ( template );
        }

        // Convert the names to OEntries.
        OEntry [] children = new OEntry [ child_names.length ];
        try
        {
            Credentials credentials = this.module.credentials ();
            Progress progress = new SimpleProgress ();
            ONode onode = this.module.getKernelObject ( this.onodeRef () );
            for ( int c = 0; c < child_names.length; c ++ )
            {
                // Create a path relative to us, using the
                // child OEntry's name.
                Path child_path = new Path ( child_names [ c ] );

                // Lookup the child OEntry.
                children [ c ] = onode.lookup ( credentials,
                                                this,
                                                child_path );
            }
        }
        catch ( ModuleOperationException e )
        {
            this.module.logger ().log ( Level.ERROR,
                                        Locale.getDefault (),
                                        "Failed to retrieve the children of OEntry [%entry%]",
                                        "entry", this,
                                        "cause", e );
            this.module.traceFail ( "SimpleOEntry.children()" );
            return new Sequence<OEntry> ( SimpleOEntry.NO_SUCH_OENTRY,
                                          new OEntry [ 0 ] );
        }
        catch ( ONodeOperationException e )
        {
            this.module.logger ().log ( Level.ERROR,
                                        Locale.getDefault (),
                                        "Failed to retrieve the children of OEntry [%entry%]",
                                        "entry", this,
                                        "cause", e );
            this.module.traceFail ( "SimpleOEntry.children()" );
            return new Sequence<OEntry> ( SimpleOEntry.NO_SUCH_OENTRY,
                                          new OEntry [ 0 ] );
        }

        // Now turn the children into a sequence of OEntries.
        Sequence<OEntry> children_sequence =
            new Sequence<OEntry> ( SimpleOEntry.NO_SUCH_OENTRY,
                                   children );

        this.module.traceExit ( "SimpleOEntry.children()" );

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
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleOEntry with module [%module%] parent [%parent_entry%] name [%name%] super block [%super_block_ref%] onode [%onode%] is mounted [%is_mounted%]",
                                                     "module", this.module,
                                                     "parent_entry", this,
                                                     "name", name,
                                                     "super_block_ref", super_block_ref,
                                                     "onode", onode,
                                                     "is_mounted", is_mounted );
        }

        // !!! Haven't decided where security for OEntries should
        // !!! be handled (in the OEntry / in the Module / in the vfs).

        return new SimpleOEntry ( this.module, // module
                                  this, // parent
                                  name,
                                  (SuperBlockIdentifier) super_block_ref,
                                  onode,
                                  is_mounted );
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#isMounted()
     */
    @Override
    public boolean isMounted ()
    {
        return this.isMounted;
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
        return this.onodeRef;
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#parent()
     */
    @Override
    public OEntry parent ()
    {
        this.module.traceEnter ( "SimpleOEntry.parent()" );

        if ( this == SimpleOEntry.NO_SUCH_OENTRY )
        {
            this.module.traceFail ( "SimpleOEntry.parent()" );
            return SimpleOEntry.NO_SUCH_OENTRY;
        }

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
            this.module.traceFail ( "SimpleOEntry.parent()" );
            return SimpleOEntry.NO_SUCH_OENTRY;
        }
        catch ( ONodeOperationException e )
        {
            this.module.logger ().log ( Level.ERROR,
                                        Locale.getDefault (),
                                        "Failed to retrieve parent OEntry from [%entry%]",
                                        "entry", this,
                                        "cause", e );
            this.module.traceFail ( "SimpleOEntry.parent()" );
            return SimpleOEntry.NO_SUCH_OENTRY;
        }

        this.module.traceExit ( "SimpleOEntry.parent()" );

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
    {
        this.module.traceEnter ( "SimpleOEntry.remove()" );

        if ( credentials == null
             || child_entry == null
             || this == SimpleOEntry.NO_SUCH_OENTRY
             || child_entry == SimpleOEntry.NO_SUCH_OENTRY
             || child_entry.parent () != this )
        {
            this.module.traceFail ( "SimpleOEntry.remove()" );
            throw new I18nIllegalArgumentException ( "OEntry [%parent_entry%] cannot remove child OEntry [%child_entry%] with credentials [%credentials%]",
                                                     "parent_entry", this,
                                                     "child_entry", child_entry,
                                                     "credentials", credentials );
        }

        final boolean was_removed;
        synchronized ( this.lock )
        {
            was_removed =
                this.childrenNames.remove ( child_entry.name () );
        }

        if ( ! was_removed )
        {
            this.module.traceFail ( "SimpleOEntry.remove()" );
            throw new I18nIllegalArgumentException ( "OEntry [%parent_entry%] cannot remove child OEntry [%child_entry%]",
                                                     "parent_entry", this,
                                                     "child_entry", child_entry );
        }

        this.module.traceExit ( "SimpleOEntry.remove()" );
    }


    /**
     * @see musaico.kernel.objectsystem.OEntry#superBlockRef()
     */
    @Override
    public SuperBlockIdentifier superBlockRef ()
    {
        return this.superBlockRef;
    }
}
