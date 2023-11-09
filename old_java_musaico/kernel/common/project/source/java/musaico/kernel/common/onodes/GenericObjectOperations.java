package musaico.kernel.common.onodes;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Progress;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleOperationException;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordTypeIdentifier;

import musaico.kernel.objectsystem.onode.FillOEntryCallback;
import musaico.kernel.objectsystem.onode.OEntry;
import musaico.kernel.objectsystem.onode.OEntryFactory;
import musaico.kernel.objectsystem.onode.ONode;
import musaico.kernel.objectsystem.onode.ONodeMetadata;
import musaico.kernel.objectsystem.onode.ONodeOperationException;

import musaico.kernel.objectsystem.records.FlatRecord;
import musaico.kernel.objectsystem.records.ObjectRecord;

import musaico.kernel.objectsystem.superblock.SuperBlockOperationException;

import musaico.security.Credentials;
import musaico.security.Security;

import musaico.time.Time;


/**
 * <p>
 * Operations for hierarchical objects (akin to directories in a
 * file system).
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
public class GenericObjectOperations
    extends GenericONodeOperations
    implements ObjectRecord, Serializable
{
    /** The module which loaded this object into memory, and which
     *  gives us access to kernel data and methods. */
    private final Module module;


    /**
     * <p>
     * Creates a new hierarchical object operations.
     * </p>
     *
     * @param module The module which loaded this operations.
     *               Must not be null.
     */
    public GenericObjectOperations (
                                    Module module
                                    )
    {
        super ( module );

        this.module = module;
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#create(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,musaico.kernel.objectsystem.RecordTypeIdentifier,java.lang.String,musaico.security.Security,java.lang.Object...)
     */
    @Override
    public OEntry create (
                          OEntry entry,
                          Credentials credentials,
                          Progress progress,
                          RecordTypeIdentifier type,
                          String name,
                          Security<RecordFlag> mode,
                          Object... onode_specific_parameters
                          )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        this.module.traceEnter ( "GenericONodeOperations.create()" );

        final ONode unresolved_parent_object;
        try
        {
            unresolved_parent_object =
                this.module.getKernelObject ( entry.onodeRef () );
        }
        catch ( ModuleOperationException e )
        {
            this.module.traceFail ( "GenericONodeOperations.create()" );
            throw new ONodeOperationException ( "Access denied: Module exception while retrieving ONode reference [%onode_ref%]",
                                                "onode_ref", entry.onodeRef (),
                                                "cause", e );
        }

        // Resolve the relation, if it is one.
        final ONode parent_object =
            this.resolveRelation ( unresolved_parent_object,
                                   credentials, progress );

        // Make sure the parent ONode is an object node (not a flat
        // record or a relation).
        ONodeMetadata parent_metadata = parent_object.metadata ();
        final RecordTypeIdentifier parent_record_type;
        try
        {
            parent_record_type =
                parent_metadata.readValue ( credentials,
                                            parent_metadata.metaRecordType () );
        }
        catch ( RecordOperationException e )
        {
            this.module.traceFail ( "GenericONodeOperations.create()" );
            throw new ONodeOperationException ( e );
        }

        if ( ! parent_record_type.equals ( ObjectRecord.TYPE_ID ) )
        {
            this.module.traceFail ( "GenericONodeOperations.create()" );
            throw new ONodeOperationException ( "Cannot create a [%child_onode_type%] ONode under ONode [%parent_onode%] type [%parent_onode_type%]",
                                                "child_onode_type", type,
                                                "parent_onode", parent_object,
                                                "parent_onode_type", parent_record_type );
        }

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
            this.module.traceFail ( "GenericONodeOperations.create()" );
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
            this.module.traceFail ( "GenericONodeOperations.create()" );
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
            this.module.traceFail ( "GenericONodeOperations.create()" );
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
            this.module.traceFail ( "GenericONodeOperations.create()" );
            throw e;
        }

        this.module.traceExit ( "GenericONodeOperations.create()" );

        return child_entry;
    }


    protected OEntry createFlatRecordONode (
                                            OEntry entry,
                                            Credentials credentials,
                                            Progress progress,
                                            String name,
                                            Security<RecordFlag> mode
                                            )
        throws I18nIllegalArgumentException,
               ONodeOperationException
    {
        this.module.traceEnter ( "GenericONodeOperations.createFlatRecordONode()" );

        final ONode parent_object;
        try
        {
            parent_object =
                this.module.getKernelObject ( entry.onodeRef () );
        }
        catch ( ModuleOperationException e )
        {
            this.module.traceFail ( "GenericONodeOperations.createFlatRecordONode()" );
            throw new ONodeOperationException ( "Access denied: Module exception while retrieving ONode reference [%onode_ref%]",
                                                "onode_ref", entry.onodeRef (),
                                                "cause", e );
        }

        // !!! NOT IMPLEMENTED YET: resolve the relation, if it is one.

        // Make sure the ONode is an object node (not a flat
        // record or a relation).
        ONodeMetadata parent_metadata = parent_object.metadata ();
        final RecordTypeIdentifier parent_record_type;
        try
        {
            parent_record_type =
                parent_metadata.readValue ( credentials,
                                            parent_metadata.metaRecordType () );
        }
        catch ( RecordOperationException e )
        {
            this.module.traceFail ( "GenericONodeOperations.createFlatRecordONode()" );
            throw new ONodeOperationException ( e );
        }

        if ( ! parent_record_type.equals ( ObjectRecord.TYPE_ID ) )
        {
            this.module.traceFail ( "GenericONodeOperations.createFlatRecordONode()" );
            throw new ONodeOperationException ( "Cannot create a flat ONode under ONode [%onode%] type [%onode_type%]",
                                                "onode", parent_object,
                                                "onode_type", parent_record_type );
        }

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
            this.module.traceFail ( "GenericONodeOperations.createFlatRecordONode()" );
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
                                       FlatRecord.TYPE_ID );
        }
        catch ( SuperBlockOperationException e )
        {
            this.module.traceFail ( "GenericONodeOperations.createFlatRecordONode()" );
            throw new ONodeOperationException ( "ONode [%onode%] SuperBlock [%super_block%] could not create child [%onode_type%]",
                                                "onode", parent_object,
                                                "super_block", super_block,
                                                "onode_type", FlatRecord.TYPE_ID,
                                                "cause", e );
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
            this.module.traceFail ( "GenericONodeOperations.createFlatRecordONode()" );
            throw e;
        }

        this.module.traceExit ( "GenericONodeOperations.createFlatRecordONode()" );

        return child_entry;
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#createObjectONode(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,java.lang.String,musaico.security.Security)
     */
    /* !!!
    @Override
    protected OEntry createObjectONode (
                                     OEntry entry,
                                     Credentials credentials,
                                     String name,
                                     Security<RecordFlag> mode
                                     )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "GenericONodeOperations.createObjectONode()" );

        final ONode parent_object;
        try
        {
            parent_object =
                this.module.getKernelObject ( entry.onodeRef () );
        }
        catch ( ModuleOperationException e )
        {
            this.module.traceFail ( "GenericONodeOperations.createObjectONode()" );
            throw new ONodeOperationException ( "Access denied: Module exception while retrieving ONode reference [%onode_ref%]",
                                                "onode_ref", entry.onodeRef (),
                                                "cause", e );
        }

        // !!! NOT IMPLEMENTED YET: resolve the relation, if it is one.

        // Make sure the ONode is an object node (not a flat
        // record or a relation).
        MetadataBuilder parent_metadata = parent_object.metadata ();
        if ( ! parent_metadata.recordType ().equals ( ObjectRecord.TYPE_ID ) )
        {
            this.module.traceFail ( "GenericONodeOperations.createObjectONode()" );
            throw new ONodeOperationException ( "Cannot create an object ONode under ONode [%onode%] type [%onode_type%]",
                                                "onode", parent_object,
                                                "onode_type", parent_metadata.recordType () );
        }

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
            this.module.traceFail ( "GenericONodeOperations.createObjectONode()" );
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
                                       ObjectRecord.TYPE_ID );
        }
        catch ( SuperBlockOperationException e )
        {
            this.module.traceFail ( "GenericONodeOperations.createObjectONode()" );
            throw new ONodeOperationException ( "ONode [%onode%] SuperBlock [%super_block%] could not create child [%onode_type%]",
                                                "onode", parent_object,
                                                "super_block", super_block,
                                                "onode_type", ObjectRecord.TYPE_ID );
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
            this.module.traceFail ( "GenericONodeOperations.createObjectONode()" );
            throw e;
        }

        this.module.traceExit ( "GenericONodeOperations.createObjectONode()" );

        return child_entry;
    }
    !!! */


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#createRelationONode(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,java.lang.String,musaico.security.Security,musaico.kernel.objectsystem.RecordTypeIdentifier,musaico.kernel.objectsystem.ONodeIdentifier[])
     */
    /* !!!
    @Override
    protected OEntry createRelationONode (
                                       OEntry entry,
                                       Credentials credentials,
                                       String name,
                                       Security<RecordFlag> mode,
                                       RecordTypeIdentifier relation_type,
                                       ONodeIdentifier [] related_onodes
                                       )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "GenericONodeOperations.createRelationONode()" );
        final ONode parent_object;
        try
        {
            parent_object =
                this.module.getKernelObject ( entry.onodeRef () );
        }
        catch ( ModuleOperationException e )
        {
            this.module.traceFail ( "GenericONodeOperations.createRelationONode()" );
            throw new ONodeOperationException ( "Access denied: Module exception while retrieving ONode reference [%onode_ref%]",
                                                "onode_ref", entry.onodeRef (),
                                                "cause", e );
        }

        // !!! NOT IMPLEMENTED YET: resolve the relation, if it is one.

        // Make sure the ONode is an object node (not a flat
        // record or a relation).
        MetadataBuilder parent_metadata = parent_object.metadata ();
        if ( ! parent_metadata.recordType ().equals ( ObjectRecord.TYPE_ID ) )
        {
            this.module.traceFail ( "GenericONodeOperations.createObjectONode()" );
            throw new ONodeOperationException ( "Cannot create a relation ONode under ONode [%onode%] type [%onode_type%]",
                                                "onode", parent_object,
                                                "onode_type", parent_metadata.recordType () );
        }

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
            this.module.traceFail ( "GenericONodeOperations.createRelationONode()" );
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
                                       relation_type );
        }
        catch ( SuperBlockOperationException e )
        {
            this.module.traceFail ( "GenericONodeOperations.createObjectONode()" );
            throw new ONodeOperationException ( "ONode [%onode%] SuperBlock [%super_block%] could not create child [%onode_type%]",
                                                "onode", parent_object,
                                                "super_block", super_block,
                                                "onode_type", ObjectRecord.TYPE_ID,
                                                "cause", e );
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
            this.module.traceFail ( "GenericONodeOperations.createRelationONode()" );
            throw e;
        }

        this.module.traceExit ( "GenericONodeOperations.createObjectONode()" );

        return child_entry;
    }
    !!! */


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#createDriverONode(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,java.lang.String,musaico.security.Security,musaico.kernel.objectsystem.Record)
     */
    /* !!!
    @Override
    protected OEntry createDriverONode (
                                     OEntry entry,
                                     Credentials credentials,
                                     String name,
                                     Security<RecordFlag> mode,
                                     Record driver_needs_cast
                                     )
        throws I18nIllegalArgumentException,
               ONodeOperationException
    {
        this.module.traceEnter ( "GenericONodeOperations.createDriverONode()" );

        if ( ! ( driver_needs_cast instanceof Driver ) )
        {
            this.module.traceFail ( "GenericONodeOperations.createDriverONode()" );
            throw new I18nIllegalArgumentException ( "Cannot create driver ONode under [%parent_entry%] with driver [%driver%]: not a Driver",
                                                     "parent_entry", entry,
                                                     "driver", driver_needs_cast );
        }

        Driver driver = (Driver) driver_needs_cast;

        final ONode parent_object;
        try
        {
            parent_object =
                this.module.getKernelObject ( entry.onodeRef () );
        }
        catch ( ModuleOperationException e )
        {
            this.module.traceFail ( "GenericONodeOperations.createDriverONode()" );
            throw new ONodeOperationException ( "Access denied: Module exception while retrieving ONode reference [%onode_ref%]",
                                                "onode_ref", entry.onodeRef (),
                                                "cause", e );
        }

        // !!! NOT IMPLEMENTED YET: resolve the relation, if it is one.

        // Make sure the ONode is an object node (not a flat
        // record or a relation).
        MetadataBuilder parent_metadata = parent_object.metadata ();
        if ( ! parent_metadata.recordType ().equals ( ObjectRecord.TYPE_ID ) )
        {
            this.module.traceFail ( "GenericONodeOperations.createDriverONode()" );
            throw new ONodeOperationException ( "Cannot create a driver ONode under ONode [%onode%] type [%onode_type%]",
                                                "onode", parent_object,
                                                "onode_type", parent_metadata.recordType () );
        }

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
            this.module.traceFail ( "GenericONodeOperations.createDriverONode()" );
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
                                       Driver.TYPE_ID );
        }
        catch ( SuperBlockOperationException e )
        {
            this.module.traceFail ( "GenericONodeOperations.createDriverONode()" );
            throw new ONodeOperationException ( "ONode [%onode%] SuperBlock [%super_block%] could not create child [%onode_type%]",
                                                "onode", parent_object,
                                                "super_block", super_block,
                                                "onode_type", Driver.TYPE_ID,
                                                "cause", e );
        }

        // Make sure it's a DriverONode.
        if ( ! ( child_onode instanceof DriverONode ) )
        {
            this.module.traceFail ( "GenericONodeOperations.createDriverONode()" );
            throw new ONodeOperationException ( "SuperBlock [%super_block%] did not create a DriverONode for driver [%driver%] name [%name%]: [%actual_driver_onode%]",
                                                "super_block", super_block,
                                                "driver", driver,
                                                "name", name,
                                                "actual_driver_onode", child_onode );
        }

        // Tell the newly allocated DriverONode which driver
        // it points to.
        DriverONode driver_onode = (DriverONode) child_onode;
        driver_onode.driver ( driver );

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
            this.module.traceFail ( "GenericONodeOperations.createDriverONode()" );
            throw e;
        }

        this.module.traceExit ( "GenericONodeOperations.createDriverONode()" );

        return child_entry;
    }
    !!! */


    /**
     * @see musaico.kernel.objectsystem.records.ObjectRecord#readObject(musaico.kernel.objectsystem.Cursor,musaico.kernel.objectsystem.onode.OEntryFactory,musaico.kernel.objectsystem.onode.FillOEntryCallback)
     */
    public void readObject (
                            Cursor cursor,
                            OEntryFactory child_factory,
                            FillOEntryCallback add_child_callback
                            )
        throws I18nIllegalArgumentException,
               RecordOperationException
    {
        this.module.traceEnter ( "GenericONodeOperations.readObject()" );

        throw new RecordOperationException ( "!!! NOT YET IMPLEMENTED" );

        this.module.traceExit ( "GenericONodeOperations.readObject()" );
    }
}
