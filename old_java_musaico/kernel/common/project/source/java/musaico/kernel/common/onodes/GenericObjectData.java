package musaico.kernel.common.onodes;

import java.io.Serializable;


import musaico.hash.Hash;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Path;
import musaico.io.Progress;

import musaico.kernel.driver.Driver;

import musaico.kernel.memory.Segment;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleOperationException;

import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordTypeIdentifier;

import musaico.kernel.objectsystem.onode.OEntry;
import musaico.kernel.objectsystem.onode.ONode;
import musaico.kernel.objectsystem.onode.ONodeIdentifier;
import musaico.kernel.objectsystem.onode.ONodeMetadata;
import musaico.kernel.objectsystem.onode.ONodeOperationException;

import musaico.kernel.objectsystem.records.FlatRecord;
import musaico.kernel.objectsystem.records.ObjectRecord;
import musaico.kernel.objectsystem.records.RelationTypeIdentifier;

import musaico.kernel.common.records.SegmentBackedRecord;

import musaico.kernel.objectsystem.superblock.SuperBlock;
import musaico.kernel.objectsystem.superblock.SuperBlockIdentifier;
import musaico.kernel.objectsystem.superblock.SuperBlockOperationException;

import musaico.kernel.oentries.SimpleOEntry;

import musaico.region.Space;

import musaico.security.Credentials;
import musaico.security.NoSecurity;
import musaico.security.Security;

import musaico.time.AbsoluteTime;
import musaico.time.Time;


/**
 * <p>
 * The data for a flat record ONode (no children, only field data).
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
public class GenericObjectData
    extends SegmentBackedRecord
    implements ObjectRecord, Serializable
{
    /** The kernel module which loaded this data area and gives
     *  controlled access to the kernel. */
    private final Module module;


    /**
     * <p>
     * Creates a new data area for a flat record ONode, backed
     * by the specified segment.
     * </p>
     *
     * @param module The module which loaded this data area.
     *               Must not be null.
     *
     * @param segment The segment which will provide data for the ONode.
     *                Must not be null.
     *
     * @param security The security for this ONode data,
     *                 which dictates who is (and who is not) allowed
     *                 to open, close, read or write this data area.
     *                 Must not be null.
     */
    public GenericObjectData (
                              Module module,
                              Segment segment,
                              Security<RecordFlag> security
                              )
    {
        super ( segment, security );

        this.module = module;
    }


    /**
     * <p>
     * Adds a child ONode to the parent object ONode.
     * </p>
     *
     * <p>
     * This method throws an exception by default.  Object system
     * implementors should override it to allow createFlatRecordONode(),
     * createObjectONode(), createRelationONode() and createDriverONode()
     * to function.
     * </p>
     *
     * <p>
     * No checking of arguments need be made, assuming only
     * the GenericONodeOperations methods (createFlatRecordONode()
     * and so on) call this method.
     * </p>
     *
     * @param parent_entry The object entry (basically its filename within the
     *                     tree of object data, like a dentry in *nix).
     *                     Must not be null.
     *
     * @param credentials Who is asking for the operation, such as
     *                    a user Credentials or a Module's Credentials.
     *                    Must not be null.
     *
     * @param parent_onode The actual object ONode whose child is
     *                     being added.  Must not be null.
     *
     * @param child_entry The child OEntry being added.  Must not be null.
     *
     * @param child_onode The child flat record / object /
     *                    relation / driver ONode being added to
     *                    the parent.  Must not be null.
     *
     * @throws ONodeOperationException If anything goes horribly wrong,
     *                                 or if the specified child is
     *                                 not allowed in the parent for
     *                                 some reason.
     */
    protected void addChildONode (
                                  OEntry parent_entry,
                                  Credentials credentials,
                                  ONode parent_onode,
                                  OEntry child_entry,
                                  ONode child_onode
                                  )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "GenericONodeOperations.addChildONode()" );
        this.module.traceFail ( "GenericONodeOperations.addChildONode()" );

        throw new ONodeOperationException ( "ONode [%onode%] ops [%ops%] at [%entry%] does not support creating flat records, objects, relations or other types of ONodes",
                                            "onode", parent_onode,
                                            "ops", this,
                                            "entry", parent_entry );
    }


    /**
     * <p>
     * Wraps the call to addChildONode () and does rollback if
     * addChildONode () fails.
     * </p>
     *
     * @return The child ONode's OEntry.  Never null.
     */
    private OEntry addChildONodeOrRollback (
                                            OEntry parent_entry,
                                            Credentials credentials,
                                            Progress progress,
                                            ONode parent_onode,
                                            String child_name,
                                            ONode child_onode,
                                            SuperBlock super_block
                                            )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "GenericONodeOperations.addChildONode()" );

        // Create the child OEntry.
        Path child_path = parent_entry.path ().append ( child_name );
        OEntry child_entry = new SimpleOEntry ( this.module,
                                                parent_entry,
                                                child_name,
                                                super_block.id (),
                                                child_onode.id (),
                                                parent_entry.isMounted () );

        try
        {
            this.addChildONode ( parent_entry,
                                 credentials,
                                 parent_onode,
                                 child_entry,
                                 child_onode );

            // Success.  Register the child ONode in the
            // kernel.
            this.module.createKernelObject ( child_onode.id (),
                                             child_onode );
        }
        catch ( ONodeOperationException e )
        {
            // Roll back.
            this.module.traceFail ( "GenericONodeOperations.addChildONode()" );
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
        catch ( ModuleOperationException e )
        {
            // We can't do anything at this point to roll back,
            // because the object system has been successfully
            // updated.  !!! what to do...
            this.module.traceFail ( "GenericONodeOperations.addChildONode()" );
            throw new ONodeOperationException ( e );
        }

        this.module.traceExit ( "GenericONodeOperations.addChildONode()" );

        return child_entry;
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
     * <p>
     * Initializes the metadata for an ONode, typically one
     * that has just been create()d.
     * </p>
     *
     * <p>
     * If the ONode is already in the OEntry tree, then make sure
     * to lock the ONode down <i> before </i> calling this method.
     * </p>
     *
     * <p>
     * Protected access method, so that sub-classes of GenericONodeOperations
     * can override the <code> create () </code> method but still
     * rely on this method to do some of the work.
     * </p>
     *
     * @param metadata The ONode Metadata to fill in.
     *                 Is presumably un-initialized.  Must not be null.
     *
     * @param onode_type The type of ONode whose metadata is to be
     *                   filled in, such as <code> ObjectRecord.TYPE_ID </code>
     *                   or <code> FlatRecord.TYPE_ID </code> and so
     *                   on.  Must not be null.
     *
     * @param onode_name The name of the ONode.  For example, if a new
     *                   ONode has been created at "/foo/bar", then "bar"
     *                   will be the name of the ONode.  Must not be null.
     *
     * @param onode_mode The security mode for the ONode, such as
     *                   UNIX-style "rwxrwxr--" or some other form of
     *                   ONode security altogether.  Must not be null.
     *
     * @param super_block The SuperBlock to which the ONode belongs.
     *                    Used to determine certain metadata (such as
     *                    the object system type underlying the ONode).
     *                    Must not be null.
     *
     * @param credentials The user or module populating the metadata
     *                    for this ONode.  (Typically the creator of
     *                    a new ONode is also the Credentials asking
     *                    for its metadata to be populated.)
     *                    Must not be null.
     *
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @param now The time at which this ONode was accessed and,
     *            if it is brand new, created and modified.
     *            Must not be null.
     *
     * @param onode_specific_parameters If this ONode was just created
     *                                  (and it is not yet populated
     *                                  with data), then these parameters
     *                                  specify the details for
     *                                  relations, driver ONodes, and
     *                                  so on.  Must not be null.
     *
     * @throws ONodeOperationException If the metadata for the specified
     *                                 ONode cannot be populated
     *                                 for some abstruse reason.
     */
    protected void populateMetadata (
                                     ONodeMetadata metadata,
                                     RecordTypeIdentifier onode_type,
                                     String onode_name,
                                     Security<RecordFlag> onode_mode,
                                     SuperBlock super_block,
                                     Credentials credentials,
                                     Progress progress,
                                     Time now,
                                     Object... onode_specific_parameters
                                     )
        throws ONodeOperationException
    {
        Space space = super_block.space ();
        try
        {
            metadata.writeValue ( credentials, metadata.metaHash (),
                                  Hash.NONE );
            metadata.writeValue ( credentials, metadata.metaObjectSystemType (),
                                  super_block.objectSystemTypeRef () );
            metadata.writeValue ( credentials, metadata.metaRecordType (),
                                  onode_type );
            metadata.writeValue ( credentials, metadata.metaRegion (),
                                  space.empty () );
            metadata.writeValue ( credentials, metadata.metaSizeFieldsFree (),
                                  space.none () );
            metadata.writeValue ( credentials, metadata.metaSizeFieldsUsed (),
                                  space.none () );
            metadata.writeValue ( credentials, metadata.metaSizeRecordsFree (),
                                  space.none () );
            metadata.writeValue ( credentials, metadata.metaSizeRecordsUsed (),
                                  space.none () );
            metadata.writeValue ( credentials, metadata.metaSpace (),
                                  space );
            metadata.writeValue ( credentials, metadata.metaTimeAccessed (),
                                  now );
            metadata.writeValue ( credentials, metadata.metaTimeDeleted (),
                                  Time.NEVER );
            metadata.writeValue ( credentials, metadata.metaTimeDirtied (),
                                  Time.NEVER );

            Time time_created =
                metadata.readValue ( credentials, metadata.metaTimeCreated () );
            if ( time_created.equals ( Time.NEVER ) )
            {
                // New ONode.
                metadata.writeValue ( credentials, metadata.metaTimeCreated (),
                                      now );
                metadata.writeValue ( credentials, metadata.metaTimeModified (),
                                      now );
            }
        }
        catch ( I18nIllegalArgumentException e )
        {
            throw new ONodeOperationException ( "Failed to populateMetadata [%metadata%] for ONode named [%onode_name%]",
                                                "metadata", metadata,
                                                "onode_name", onode_name,
                                                "cause", e );
        }
        catch ( RecordOperationException e )
        {
            throw new ONodeOperationException ( e );
        }
    }


    /**
     * <p>
     * Initializes the data for an ONode, typically one
     * that has just been create()d.
     * </p>
     *
     * <p>
     * If the ONode is already in the OEntry tree, then make sure
     * to lock the ONode down <i> before </i> calling this method.
     * </p>
     *
     * <p>
     * Protected access method, so that sub-classes of GenericONodeOperations
     * can override the <code> create () </code> method but still
     * rely on this method to do some of the work.
     * </p>
     *
     * @param onode The ONode to fill in.
     *              Is presumably un-initialized.  Must not be null.
     *
     * @param onode_type The type of ONode whose data is to be
     *                   filled in, such as <code> ObjectRecord.TYPE_ID </code>
     *                   or <code> FlatRecord.TYPE_ID </code> and so
     *                   on.  Must not be null.
     *
     * @param onode_name The name of the ONode.  For example, if a new
     *                   ONode has been created at "/foo/bar", then "bar"
     *                   will be the name of the ONode.  Must not be null.
     *
     * @param onode_mode The security mode for the ONode, such as
     *                   UNIX-style "rwxrwxr--" or some other form of
     *                   ONode security altogether.  Must not be null.
     *
     * @param super_block The SuperBlock to which the ONode belongs.
     *                    Used to determine certain data (such as
     *                    the Space defining the region covered by
     *                    the ONode).  Must not be null.
     *
     * @param credentials The user or module populating this ONode.
     *                    (Typically the creator of
     *                    a new ONode is also the Credentials asking
     *                    for its data to be populated.)
     *                    Must not be null.
     *
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @param onode_specific_parameters If this ONode was just created
     *                                  (and it is not yet populated
     *                                  with data), then these parameters
     *                                  specify the details for
     *                                  relations, driver ONodes, and
     *                                  so on.  Must not be null.
     *
     * @throws ONodeOperationException If the data for the specified
     *                                 ONode cannot be populated
     *                                 for some abstruse reason.
     */
    protected void populateONode (
                                  ONode onode,
                                  RecordTypeIdentifier onode_type,
                                  String onode_name,
                                  Security<RecordFlag> onode_mode,
                                  SuperBlock super_block,
                                  Credentials credentials,
                                  Progress progress,
                                  Object... onode_specific_parameters
                                  )
        throws ONodeOperationException
    {
        throw new ONodeOperationException ( "!!! NOT IMPLEMENTED" );
    }


    /**
     * <p>
     * If the specified ONode is a Relation then resolves it
     * and returns the resolved ONode; otherwise returns the
     * ONode as-is.
     * </p>
     *
     * @param onode The ONode which might be a Relation to resolve.
     *              Must not be null.
     *
     * @param credentials Who is asking to resolve the relation,
     *                    such as the user who instigated a read
     *                    of the object system.  Must not be null.
     *
     * @param progress The progress tracker, to record how close
     *                 to completion this operation is.  Must not
     *                 be null.
     *
     * @return Either the resolved Relation ONode, or if the onode
     *         parameter is not a Relation then the ONode as-is.
     *         Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     *
     * @throws ONodeOperationException If the ONode is a Relation but
     *                                 it cannot be resolved.
     */
    protected ONode resolveRelation (
                                     ONode onode,
                                     Credentials credentials,
                                     Progress progress
                                     )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        ONodeMetadata metadata = onode.metadata ();
        final RecordTypeIdentifier record_type;
        try
        {
            record_type =
                metadata.readValue ( credentials, metadata.metaRecordType () );
        }
        catch ( RecordOperationException e )
        {
            throw new ONodeOperationException ( e );
        }

        if ( ! ( record_type instanceof RelationTypeIdentifier ) )
        {
            return onode;
        }

        // !!! NOT IMPLEMENTED YET: resolve the relation.
        throw new ONodeOperationException ( "!!! RESOLVE RELATION NOT IMPLEMENTED YET" );
    }
}
