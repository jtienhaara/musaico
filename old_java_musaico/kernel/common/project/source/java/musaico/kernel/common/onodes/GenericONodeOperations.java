package musaico.kernel.common.onodes;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.buffer.Buffer;
import musaico.buffer.BufferException;
import musaico.buffer.BufferTools;
import musaico.buffer.BufferType;

import musaico.buffer.search.SpecificFieldID;

import musaico.field.Attribute;
import musaico.field.Field;
import musaico.field.FieldTypingEnvironment;

import musaico.hash.Hash;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Path;
import musaico.io.Progress;
import musaico.io.Reference;
import musaico.io.ReferenceCount;
import musaico.io.Sequence;

import musaico.kernel.common.oentries.SimpleOEntry;

import musaico.kernel.driver.Driver;

import musaico.kernel.memory.Segment;

import musaico.kernel.memory.paging.SwapState;

import musaico.kernel.memory.virtual.VirtualBuffer;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleOperationException;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.PollRequest;
import musaico.kernel.objectsystem.PollResponse;
import musaico.kernel.objectsystem.Record;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordPermission;
import musaico.kernel.objectsystem.RecordPermissions;
import musaico.kernel.objectsystem.RecordTypeIdentifier;

import musaico.kernel.objectsystem.onode.OEntry;
import musaico.kernel.objectsystem.onode.ONode;
import musaico.kernel.objectsystem.onode.ONodeIdentifier;
import musaico.kernel.objectsystem.onode.ONodeMetadata;
import musaico.kernel.objectsystem.onode.ONodeOperationException;
import musaico.kernel.objectsystem.onode.ONodeSecurityException;

import musaico.kernel.objectsystem.records.FlatRecord;
import musaico.kernel.objectsystem.records.ObjectRecord;
import musaico.kernel.objectsystem.records.RelationTypeIdentifier;

import musaico.kernel.objectsystem.superblock.SuperBlock;
import musaico.kernel.objectsystem.superblock.SuperBlockIdentifier;
import musaico.kernel.objectsystem.superblock.SuperBlockOperationException;

import musaico.region.Criterion;
import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Space;

import musaico.security.Credentials;
import musaico.security.NoSecurity; // !!! TEMPORARY HACK
import musaico.security.Permissions;
import musaico.security.Security;

import musaico.time.AbsoluteTime;
import musaico.time.Time;



/**
 * <p>
 * Provides a pretty boring set of operations for ONOdes.
 * </p>
 *
 * <p>
 * Derived flat record operations classes should override:
 * </p>
 *
 * <ul>
 *   <li> mmap </li>
 *   <li> poll </li>
 *   <li> read </li>
 *   <li> sync </li>
 *   <li> truncate </li>
 *   <li> write </li>
 * </ul>
 *
 * <p>
 * Derived hierarchical object operations classes should override:
 * </p>
 *
 * <ul>
 *   <li> addChildONode () (protected method) </li>
 *   <li> lookup </li>
 *   <li> removeChildONode () (protected method) </li>
 *   <li> removeObject </li>
 *   <li> sync </li>
 * </ul>
 *
 * <p>
 * Derived relation operations classes should override:
 * </p>
 *
 * <ul>
 *   <li> readRelation </li>
 *   <li> sync </li>
 * </ul>
 *
 * <p>
 * Derived driver ONodes should implement:
 * </p>
 *
 * <ul>
 *   <li> poll </li>
 *   <li> read </li>
 *   <li> sync </li>
 *   <li> truncate </li>
 *   <li> write </li>
 * </ul>
 *
 * <p>
 * All of the aforementioned methods are implemented to blow up
 * in SimpleONode.  So if you want to provide those
 * operations for your object system, you <b> must </b> override
 * those methods.
 * </p>
 *
 *
 * <p>
 * All the ONodeOperations here assume:
 * </p>
 *
 * <ul>
 *   <li> The caller has checked parameters for nulls and generally
 *        to ensure the ONodeOperations contract has been followed. </li>
 *   <li> The ONode(s) underlying the operation have been locked
 *        with a MutexLock. </li>
 *   <li> Any and all permissions have been checked on the credentials
 *        for the oepration. </li>
 * </ul>
 *
 * @see musaico.kernel.common.onodes.VirtualONodeOperations
 *
 *
 * <p>
 * Because object systems can conceivably be distributed, every ONodeOperations
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
public class SimpleONode
    implements ONodeOperations, Serializable
{
    /** The module which created these ONode operations and gives
     *  us access to kernel functionality. */
    private final Module module;


    /**
     * <p>
     * Creates a new SimpleONode inside the specified
     * Module.
     * </p>
     *
     * @param module The kernel module from which this
     *               SimpleONode was created.  Must not
     *               be null.  The module gives this SimpleONode
     *               access to the kernel.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid.
     */
    public SimpleONode (
                                   Module module
                                   )
    {
        if ( module == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleONode with module [%module%]",
                                                     "module", module );
        }

        this.module = module;

        this.module.traceEnter ( "SimpleONode()" );
        this.module.traceExit ( "SimpleONode()" );
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
     * the SimpleONode methods (createFlatRecordONode()
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
        this.module.traceEnter ( "SimpleONode.addChildONode()" );
        this.module.traceFail ( "SimpleONode.addChildONode()" );

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
        this.module.traceEnter ( "SimpleONode.addChildONode()" );

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
            this.module.traceFail ( "SimpleONode.addChildONode()" );
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
            this.module.traceFail ( "SimpleONode.addChildONode()" );
            throw new ONodeOperationException ( e );
        }

        this.module.traceExit ( "SimpleONode.addChildONode()" );

        return child_entry;
    }


    /**
     * @see musaico.kernel.objectsystem.onodes.ObjectONode#create(musaico.security.Credentials,musaico.kernel.objectsystem.OEntry,musaico.kernel.objectsystem.RecordTypeIdentifier,java.lang.String,musaico.security.Security,java.lang.Object...)
     */
    @Override
    public OEntry create (
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
        this.module.traceEnter ( "GenericFlatRecordOperations.create()" );

        this.module.traceFail ( "GenericFlatRecordOperations.create()" );

        throw new ONodeOperationException ( "Create not supported by ONode [%onode%] ops [%ops%] at [%entry%] while creating [%name%]",
                                            "onode", entry.onodeRef (),
                                            "ops", this,
                                            "entry", entry,
                                            "name", name );
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
        this.module.traceEnter ( "SimpleONode.lookup()" );

        this.module.traceFail ( "SimpleONode.lookup()" );
        throw new ONodeOperationException ( "Lookup not supported by ONode [%onode%] ops [%ops%] at [%entry%] while looking up path [%path%]",
                                            "onode", entry.onodeRef (),
                                            "ops", this,
                                            "entry", entry,
                                            "path", path );
    }


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
     * Protected access method, so that sub-classes of SimpleONode
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
     * Protected access method, so that sub-classes of SimpleONode
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
     * @see musaico.kernel.objectsystem.onodes.Relation#readRelation(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress)
     */
    @Override
    public OEntry readRelation (
                                OEntry entry,
                                Credentials credentials,
                                Progress progress
                                )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "SimpleONode.readRelation()" );

        this.module.traceFail ( "SimpleONode.readRelation()" );
        throw new ONodeOperationException ( "ReadRelation not supported by ONode [%onode%] ops [%ops%] at [%entry%]",
                                            "onode", entry.onodeRef (),
                                            "ops", this,
                                            "entry", entry );
    }


    /**
     * <p>
     * Removes a child ONode from the parent object ONode.
     * </p>
     *
     * <p>
     * This method throws an exception by default.  Object system
     * implementors should override it to allow move() to function.
     * </p>
     *
     * <p>
     * No checking of arguments need be made, assuming only
     * the SimpleONode methods (move() and so on)
     * call this method.
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
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @param parent_onode The actual object ONode whose child is
     *                     being removed.  Must not be null.
     *
     * @param child_entry The child OEntry being removed.  Must not be null.
     *
     * @param child_onode The child flat record / object /
     *                    relation / driver ONode being removed from
     *                    the parent.  Must not be null.
     *
     * @throws ONodeOperationException If anything goes horribly wrong,
     *                                 or if the specified child is
     *                                 not allowed to be removed from
     *                                the parent for some reason.
     */
    protected void removeChildONode (
                                     OEntry parent_entry,
                                     Credentials credentials,
                                     Progress progress,
                                     ONode parent_onode,
                                     OEntry child_entry,
                                     ONode child_onode
                                     )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "SimpleONode.removeChildONode()" );
        this.module.traceFail ( "SimpleONode.removeChildONode()" );

        throw new ONodeOperationException ( "ONode [%onode%] ops [%ops%] at [%entry%] does not support removing children",
                                            "onode", parent_onode,
                                            "ops", this,
                                            "entry", parent_entry );
    }


    /**
     * @see musaico.kernel.objectsystem.onodes.ObjectONode#removeObject(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,musaico.kernel.objectsystem.OEntry)
     */
    @Override
    public void removeObject (
                              OEntry entry,
                              Credentials credentials,
                              Progress progress,
                              OEntry child_object_entry
                              )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "SimpleONode.removeObject()" );

        this.module.traceFail ( "SimpleONode.removeObject()" );
        throw new ONodeOperationException ( "RemoveObject not supported by parent ONode [%parent_onode%] at [%entry%] while trying to remove child OEntry [%child_entry%]",
                                            "parent_onode", entry.onodeRef (),
                                            "entry", entry,
                                            "child_entry", child_object_entry );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONode#truncate(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,musaico.io.Region)
     */
    @Override
    public void truncate (
                          OEntry entry,
                          Credentials credentials,
                          Progress progress,
                          Region truncate_everything_outside_of_region
                          )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "SimpleONode.truncate()" );

        // This ONode does not allow truncating.
        this.module.traceFail ( "SimpleONode.truncate()" );
        throw new ONodeOperationException ( "Truncate not supported by ONode [%onode%] ops [%ops%] at [%entry%] while truncating to region [%truncate_everything_outside_of_region%]",
                                            "onode", entry.onodeRef (),
                                            "ops", this,
                                            "entry", entry,
                                            "truncate_everything_outside_of_region", truncate_everything_outside_of_region );
    }
}
