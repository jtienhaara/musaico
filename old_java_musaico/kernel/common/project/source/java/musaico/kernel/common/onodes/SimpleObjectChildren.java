package musaico.kernel.common.onodes;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordTypeIdentifier;

import musaico.kernel.objectsystem.onode.OEntry;
import musaico.kernel.objectsystem.onode.ONodeOperationException;
import musaico.kernel.objectsystem.onode.ONodeSecurityException;

import musaico.kernel.objectsystem.onodes.ObjectChildren;
import musaico.kernel.objectsystem.onodes.ObjectONode;

import musaico.region.Position;
import musaico.region.Region;

import musaico.security.Credentials;
import musaico.security.Security;


/**
 * <p>
 * A basic implementation of an hierarchical object's child ONodes,
 * built upon another record (such as the SegmentBackedRecord used
 * for much ONode data).
 * </p>
 *
 * <p>
 * The SimpleObjectChildren does permit write access to its fields,
 * which are references to ONodes, so be careful who you expose
 * it to.  It is generally preferable to use the <code> addChild () </code>,
 * <code> create () </code> and <code> removeChild () </code> methods
 * to access child ONodes, since they perform hard link management.
 * </p>
 *
 *
 * <p>
 * Because object systems can conceivably be distributed, every ObjectChildren
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
public class SimpleObjectChildren
    implements ObjectChildren, Serializable
{
    /** The module which loaded this children record into memory, and which
     *  gives us access to kernel data and methods. */
    private final Module module;

    /** The ObjectONode whose children this record manages. */
    private final ObjectONode object;

    /** The underlying record which we use to read and write
     *  child ONode fields persistently. */
    private final StructuredRecord delegate;

    /** The cursor we use internally to read and write child
     *  ONodes to the underlying delegate storage area.  The
     *  position of the Cursor always reflects the next available
     *  location for adding a child ONode.  The cursor is
     *  created at constructor time but not opened until the
     *  first ObjectChildren method is called. */
    private final Cursor cursor;


    /**
     * @see musaico.kernel.objectsystem.onodes.ObjectChildren#addChild(musaico.security.Credentials,musaico.kernel.objectsystem.OEntry)
     */
    public abstract void addChild (
                                   Credentials credentials,
                                   OEntry child_entry
                                   )
        throws ONodeSecurityException,
               ONodeOperationException
    {
        this.module.traceEnter ( "SimpleObjectChildren.addChild()" );

        // TODO Check args, credentials, security

        final ONode child_onode;
        try
        {
            child_onode =
                this.module.getKernelObject ( child_entry.onodeRef () );
        }
        catch ( ModuleOperationException e )
        {
            this.module.traceFail ( "SimpleObjectChildren.addChild()" );
            throw new ONodeOperationException ( "Access denied: Module exception while retrieving ONode reference [%onode_ref%]",
                                                "onode_ref", entry.onodeRef (),
                                                "cause", e );
        }

        try
        {
            if ( ! this.cursor.isOpen () )
            {
                this.cursor.record ().open ( this.cursor );
            }

            Position position = this.cursor.position ();
            Attribute<ONodeIdentifier> child_attribute =
                new Attribute<ONodeIdentifier> ( "" + position,
                                                 ONodeIdentifier.class,
                                                 position );
            this.delegate.writeValue ( credentials,
                                       child_attribute,
                                       child_onode.id () );

            // Now link to the child ONode.
            child_onode.link ( credentials, child_entry );

            // If the child is an hierarchical object, then
            // create its ".." entry and link ".." to this object.
            if ( child_onode instanceof ObjectONode )
            {
                OEntry dot_dot =
                    child_entry.create ( credentials,
                                         "..",
                                         this.object.superBlockRef (),
                                         this.object,
                                         child_entry.isMounted () );
                child_entry.addChild ( credentials, dot_dot );
                this.object.link ( credentials, dot_dot );
            }
        }
        catch ( RecordOperationException e )
        {
            this.module.traceFail ( "SimpleObjectChildren.addChild()" );
            throw new ONodeOperationException ( e );
        }

        this.module.traceExit ( "SimpleObjectChildren.addChild()" );
    }


    /**
     * @see musaico.kernel.objectsystem.onodes.ObjectChildren#create(musaico.security.Credentials,musaico.kernel.objectsystem.onode.OEntry,musaico.kernel.objectsystem.RecordTypeIdentifier,java.lang.String,musaico.security.Security,java.lang.Object...)
     */
    public abstract OEntry create (
				   Credentials credentials,
				   OEntry entry,
                                   RecordTypeIdentifier type,
                                   String name,
                                   Security<RecordFlag> mode,
                                   Object... onode_specific_parameters
                                   )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "SimpleObjectChildren.create()" );

        final SuperBlock super_block;
        final SuperBlockIdentifier super_block_id = (SuperBlockIdentifier)
            entry.superBlockRef ();
        try
        {
            super_block =
                this.module.getKernelObject ( super_block_id );
        }
        catch ( ModuleOperationException e )
        {
            this.module.traceFail ( "SimpleObjectChildren.create()" );
            throw new ONodeOperationException ( "Access denied: Module exception while retrieving SuperBlock reference [%super_block_ref%]",
                                                "super_block_ref", super_block_id,
                                                "cause", e );
        }

        // First allocate a new ONode from the super block.
        final ONode child_onode;
        try
        {
            child_onode =
                super_block.allocate ( credentials,
                                       progress,
                                       new NoSecurity<RecordFlag> (), // !!! NEED TO FIGURE OUT WHAT TO DO HERE FOR SECURITY, USE A FACTORY?!?  CALL SOMETHING ON THE SUPER BLOCK?!?
                                       type );
        }
        catch ( SuperBlockOperationException e )
        {
            this.module.traceFail ( "SimpleObjectChildren.create()" );
            throw new ONodeOperationException ( "ONode [%onode%] SuperBlock [%super_block%] could not create child [%onode_type%]",
                                                "onode", parent_object,
                                                "super_block", super_block,
                                                "onode_type", type,
                                                "cause", e );
        }

        // Now populate the ONode content and metadata.
        Time created_time = AbsoluteTime.now ();
        try
        {
            ONodeMetadata metadata = child_onode.metadata ();
            this.populateMetadata ( metadata,
                                    type,
                                    name,
                                    mode,
                                    super_block,
                                    credentials,
                                    progress,
                                    created_time,
                                    onode_specific_parameters );
            this.populateONode ( child_onode,
                                 type,
                                 name,
                                 mode,
                                 super_block,
                                 credentials,
                                 progress,
                                 onode_specific_parameters );
        }
        catch ( ONodeOperationException e )
        {
            // Roll back.
            this.module.traceFail ( "SimpleObjectChildren.create()" );
            try
            {
                super_block.free ( credentials, progress, child_onode );
                super_block.delete ( credentials, progress, child_onode );
            }
            catch ( SuperBlockOperationException e2 )
            {
                // Couldn't free the half-baked child ONode
                // from the SuperBlock... now what?
                // !!! FOR NOW DO NOTHING...
            }

            throw e;
        }

        final OEntry child_entry;
        try
        {
            child_entry = this.addChildONodeOrRollback ( entry,
                                                         credentials,
                                                         progress,
                                                         parent_object,
                                                         name,
                                                         child_onode,
                                                         super_block );
        }
        catch ( ONodeOperationException e )
        {
            this.module.traceFail ( "SimpleObjectChildren.create()" );
            throw e;
        }

        this.module.traceExit ( "SimpleObjectChildren.create()" );

        return child_entry;
    }


    /**
     * @see musaico.kernel.objectsystem.onodes.ObjectChildren#removeChild(musaico.security.Credentials,musaico.kernel.objectsystem.onode.OEntry)
     */
    public void removeChild (
                             Credentials credentials,
                             OEntry child_entry
                             )
        throws ONodeSecurityException,
               ONodeOperationException
    {
        this.module.traceEnter ( "SimpleObjectChildren.removeChild()" );

        // TODO Check args, credentials, security

        final ONode child_onode;
        try
        {
            child_onode =
                this.module.getKernelObject ( child_entry.onodeRef () );
        }
        catch ( ModuleOperationException e )
        {
            this.module.traceFail ( "SimpleObjectChildren.removeChild()" );
            throw new ONodeOperationException ( "Access denied: Module exception while retrieving ONode reference [%onode_ref%]",
                                                "onode_ref", entry.onodeRef (),
                                                "cause", e );
        }

        try
        {
            if ( ! this.cursor.isOpen () )
            {
                this.cursor.record ().open ( this.cursor );
            }

            // We have to search for the child ONode...!!!
            !!!;
            Position position = this.cursor.position ();
            Attribute<ONodeIdentifier> child_attribute =
                new Attribute<ONodeIdentifier> ( "" + position,
                                                 ONodeIdentifier.class,
                                                 position );
            this.delegate.writeValue ( credentials,
                                       child_attribute,
                                       child_onode.id () );

            // Now un-link to the child ONode.
            child_onode.unlink ( credentials, child_entry );

            // If the child is an hierarchical object, then
            // remove its ".." entry and unlink ".." from this object.
            if ( child_onode instanceof ObjectONode )
            {
                OEntry dot_dot =
                    child_entry.removeChilds ( credentials, ".." );
                this.object.link ( credentials, dot_dot );
            }
        }
        catch ( RecordOperationException e )
        {
            this.module.traceFail ( "SimpleObjectChildren.removeChild()" );
            throw new ONodeOperationException ( e );
        }

        this.module.traceExit ( "SimpleObjectChildren.removeChild()" );
    }
}
