package musaico.kernel_modules.objectsystems.gnosys;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import musaico.buffer.Buffer;
import musaico.buffer.BufferException;
import musaico.buffer.BufferTools;

import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.I18nRuntimeException;

import musaico.io.Identifier;
import musaico.io.Progress;
import musaico.io.ReferenceCount;
import musaico.io.Sequence;
import musaico.io.SimpleReferenceCount;

import musaico.kernel.KernelObjectException;

import musaico.kernel.memory.Segment;
import musaico.kernel.memory.SegmentFactory;
import musaico.kernel.memory.SegmentIdentifier;

import musaico.kernel.memory.requests.ReadFieldRequest;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleOperationException;

import musaico.kernel.objectsystem.CursorIdentifier;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordTypeIdentifier;

import musaico.kernel.objectsystem.objectsystemtype.ObjectSystemTypeIdentifier;

import musaico.kernel.objectsystem.onode.OEntry;
import musaico.kernel.objectsystem.onode.ONode;
import musaico.kernel.objectsystem.onode.ONodeIdentifier;
import musaico.kernel.objectsystem.onode.ONodeMetadata;
import musaico.kernel.objectsystem.onode.ONodeOperationException;

import musaico.kernel.objectsystem.quota.Quotas;

import musaico.kernel.objectsystem.records.FlatRecord;
import musaico.kernel.objectsystem.records.ObjectRecord;

import musaico.kernel.objectsystem.superblock.SuperBlockIdentifier;
import musaico.kernel.objectsystem.superblock.SuperBlock;
import musaico.kernel.objectsystem.superblock.SuperBlockFlag;
import musaico.kernel.objectsystem.superblock.SuperBlockLock;
import musaico.kernel.objectsystem.superblock.SuperBlockOperationException;
import musaico.kernel.objectsystem.superblock.SuperBlockPermission;
import musaico.kernel.objectsystem.superblock.SuperBlockPermissions;

import musaico.kernel.objectsystems.onodes.SimpleONode;

import musaico.mutex.Mutex;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Size;
import musaico.region.Space;

import musaico.region.array.ArraySpace;

import musaico.security.Credentials;
import musaico.security.NoSecurity;
import musaico.security.Permissions;
import musaico.security.Security;

import musaico.time.RelativeTime;


/**
 * <p>
 * The SuperBlock for a generic object system (gnosys).
 * </p>
 *
 * <p>
 * This superblock creates generic ONodes without any types.
 * Every ONode is just a sequence of Fields, nothing more
 * is known about the nature or structure of the objects
 * and flat records in a generic object system.
 * </p>
 *
 *
 * <p>
 * Because object systems can conceivably be distributed, every SuperBlock
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
public class GnSuperBlock
    implements SuperBlock, Serializable
{
    /** Current revision of the gnosys object system format.
     *  Increment this number with every (minor) change. */
    public static final long REVISION = 1L;


    /** Use a MutexLock to lock on this for every critical section: */
    private final Mutex mutex;

    /** The kernel module which loaded us in, and through which
     *  we access the kernel and other modules. */
    private final Module module;

    /** A reference to the object system type which created
     *  this super block. */
    private final ObjectSystemTypeIdentifier objectSystemTypeRef;

    /** The unique id for this super block. */
    private final SuperBlockIdentifier id;

    /** The mount point for this SuperBlock. */
    private final OEntry mountPoint;

    /** Mount options for this super block.
     *  Mutable since we can remount (). */
    private Buffer mountOptions;

    /** SegmentFactory which allows us to create a Segment with
     *  individual security for each ONode in the object system. */
    private final SegmentFactory segmentFactory;

    /** The Segment (with PagedArea) which backs this
     *  super block.  The segment typically does mapping between
     *  swap spaces, swapping in buffers from backing drivers,
     *  and writing out blocks from memory to backing drivers.
     *  Each ONode in the object system covers one or more
     *  blocks from the most-swapped-out driver.   Typically
     *  multiple ONodes cover the Region of the Segment
     * as a whole. */
    private final Segment data;

    /** Tracks the metadata for this object system (free # onodes, free
     *  # fields, object system type, and so on). */
    private final ONodeMetadata metadata;

    /** Security manager for this super block.
        !!! Security not implemented yet. */
    private final Security<SuperBlockFlag> security =
        new NoSecurity<SuperBlockFlag> ();

    /** Quota policy and current usage for this object system.
     *  !!! Quotas not implemented yet. */
    private final Quotas quotas = Quotas.NONE;

    /** External objects who hold references to this super block. */
    private final ReferenceCount references;

    /** isSetup starts off false.  The setup () method must
     *  be called to read in the super block's ONode after
     *  constructing or remounting the object system. */
    private boolean isSetup = false;

    /** All ONodes in this object system.  THE FIRST ONODE
     *  IS ALWAYS THE SUPER BLOCK'S ONODE! */
    private final List<ONode> onodes = new ArrayList<ONode> ();

    /** ONodes which have been free()d but not yet delete()d. */
    private final List<ONode> onodesPendingDeletion =
        new ArrayList<ONode> ();

    /** ONodes which are empty in the paged area, or which
     *  have been deleted. */
    private final List<ONode> onodesFree =
        new ArrayList<ONode> ();

    /** Lookup of ONodes from the onodes list by id. */
    private final Map<ONodeIdentifier,ONode> onodesMap =
        new HashMap<ONodeIdentifier,ONode> ();

    /** References to the Cursors open on ONodes in this system. */
    private final List<CursorIdentifier> cursorIdentifiers =
        new ArrayList<CursorIdentifier> ();


    /**
     * <p>
     * Creates a new GnSuperBlock with the specified kernel paging.
     * </p>
     *
     * <p>
     * <b>
     * Once this super block is created, you will need to call
     * <code> GnSuperBlock.setup () </code> to finish setting
     * up the super block.
     * </b>
     * </p>
     *
     * @see musaico.kernel_modules.objectsystem.gnosys.GnSuperBlock#setup()
     *
     * @param module The module which loaded this super block.
     *               Must not be null.
     *
     * @param object_system_type_id The id of the ObjectSystemType
     *                              which created this super block.
     *                              Must not be null.
     *
     * @param id The unique identifier of this SuperBlock.
     *           Must not be null.
     *
     * @param mount_point The OEntry at which to mount this
     *                    super block.  Must not be null.  Must
     *                    not already be mounted.
     *
     * @param mount_options The options specified for mounting this
     *                      super block.  Must not be null.
     *
     * @param segment_factory The factory which allows this object
     *                        system to allocate one Segment per
     *                        ONode.  Must not be null.
     *
     * @param super_block_data The Segment / PagedArea pair which backs
     *                         this GnSuperBlock.
     *                         This SuperBlock will eventually read from
     *                         the segment, inducing swapping in, in order
     *                         to read the super block content.  The super
     *                         block content tells this super block where
     *                         to find its ONodes.  Must not be null.
     *
     * @param system_metadata The read/write metadata (# fields free,
     *                        # onodes free and so on) for this
     *                        object system.  Used to generate
     *                        Metadata snapshots when requested.
     *                        Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     */
    public GnSuperBlock (
                         Module module,
                         ObjectSystemTypeIdentifier object_system_type_id,
                         SuperBlockIdentifier id,
                         OEntry mount_point,
                         Buffer mount_options,
                         SegmentFactory segment_factory,
                         Segment super_block_data,
                         ONodeMetadata system_metadata
                         )
        throws I18nIllegalArgumentException
    {
        if ( module == null
             || object_system_type_id == null
             || id == null
             || mount_point == null
             || mount_options == null
             || segment_factory == null
             || super_block_data == null
             || system_metadata == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a GnSuperBlock in module [%module%] with object_system_type_id [%object_system_type_id%] id [%id%] mount_point [%mount_point%] mount_options [%mount_options%] segment factory [%segment_factory%] super block data [%super_block_data%] object system metadata [%system_metadata%]",
                                                     "object_system_type_id", object_system_type_id,
                                                     "id", id,
                                                     "mount_point", mount_point,
                                                     "mount_options", mount_options,
                                                     "segment_factory", segment_factory,
                                                     "super_block_data", super_block_data,
                                                     "system_metadata", system_metadata );
        }

        this.mutex = new Mutex ( this );
        this.module = module;
        this.objectSystemTypeRef = object_system_type_id;
        this.id = id;
        this.mountPoint = mount_point;
        this.mountOptions = mount_options;
        this.segmentFactory = segment_factory;
        this.data = super_block_data;
        this.metadata = system_metadata;
        this.references = new SimpleReferenceCount ();

        this.isSetup = false;
    }


    /**
     * <p>
     * Sets up this super block, reading in the root onode,
     * setting up internal tables, and so on.
     * </p>
     *
     * @param progress The progress meter which can be used by
     *                 callers to monitor how close to completion
     *                 the requested operation is.  Must not be null.
     *
     * @throws SuperBlockOperationException If this method has
     *                                      already been called.
     */
    public void setup (
                       Progress progress
                       )
        throws SuperBlockOperationException
    {
        // Lock ourselves.
        final SuperBlockLock mutex_lock =
            new SuperBlockLock ( this.mutex );
        mutex_lock.lock ();
        try
        {
            if ( this.isSetup )
            {
                throw new SuperBlockOperationException ( "Cannot setup SuperBlock [%super_block%] more than once",
                                                         "super_block", this );
            }

            long onode;
            ONodeIdentifier onode_id;
            Position position;
            long super_block_onode_size = 0L;

            // Read in the super block.
            Region super_region = this.data.region ();
            position = super_region.start ();

            ReadFieldRequest read_revision =
                new ReadFieldRequest ( this.module.credentials (),
                                       this.data.id (),
                                       new RelativeTime ( 1L, 0L ), // !!!
                                       position );
            Field revision_field =
                this.data.request ( read_revision ).waitFor ()
                    .response ();
            super_block_onode_size ++;

            if ( revision_field == null
                 || revision_field.value ( Long.class ).longValue ()
                    != GnSuperBlock.REVISION )
            {
                throw new SuperBlockOperationException ( "SuperBlock [%super_block%] does not understand revision [%super_block_revision%] (max [%internal_revision%])",
                                                         "super_block", this,
                                                         "super_block_revision", revision_field,
                                                         "internal_revision", GnSuperBlock.REVISION );
            }

            // Now read in all the references to ONodes.
            position = super_region.expr ( position ).next ();
            super_block_onode_size ++;
            long onode_num = 0L;
            Size num_object_system_fields = ArraySpace.STANDARD.none ();
            while ( super_region.contains ( position ) )
            {
                // !!!! MAYBE EACH ONODE DESCRIPTION SHOULD BE A RECORD...???
                ReadFieldRequest read_onode_region =
                    new ReadFieldRequest ( this.module.credentials (),
                                           this.data.id (),
                                           new RelativeTime ( 1L, 0L ), // !!!
                                           position );
                Field onode_region_field =
                    this.data.request ( read_onode_region ).waitFor ()
                        .response ();

                if ( ! onode_region_field.id ().equals ( "onode" ) )
                {
                    // No more ONodes.
                    break;
                }

                Region onode_region =
                    onode_region_field.value ( Region.class );

                // !!! CHECK FOR OUT OF BOUNDS
                position =
                    super_region.expr ( position ).next ();
                // !!! CHECK FIELD NAME = "onode_type"
                ReadFieldRequest read_onode_type =
                    new ReadFieldRequest ( this.module.credentials (),
                                           this.data.id (),
                                           new RelativeTime ( 1L, 0L ), // !!!
                                           position );
                RecordTypeIdentifier onode_type =
                    this.data.request ( read_onode_type ).waitFor ()
                        .response ()
                        .value ( RecordTypeIdentifier.class );

                final ONode onode;
                if ( onode_type.equals ( FlatRecord.TYPE_ID ) )
                {
                    onode_ops = new GenericFlatRecord!!! ( this.module );
                }
                else if ( onode_type.equals ( ObjectRecord.TYPE_ID ) )
                {
                    onode_ops = new GnObjectOperations ( this.module );
                }
                else
                {
                    // !!! SYMBOLIC LINKS, UNIONS, ...
                    onode_ops = new GnFlatRecordOperations ( this.module ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                }

                // One segment and paged area for each ONode
                // in the object system:
                SegmentIdentifier segment_id =
                    new SegmentIdentifier ( onode_id.name () );
                final Segment segment;
                try
                {
                    segment =
                        segment_factory.createSegment ( segment_id,
                                                        new NoSegmentSecurity (), // !!!!!!!!!!!!!!!!!!!!!!!!!! NO SECURITY
                                                        this.module.credentials () ); // !!!
                }
                catch ( MemoryException e )
                {
                    // !!! log me, tell the module we failed
                    throw new SuperBlockOperationException ( e );
                }

                ONodeMetadata onode_metadata = new GnONodeMetadata ();

                onode_num ++;
                onode_id = new ONodeIdentifier ( this.id (), onode_num );
                !!!;
                ONode onode =
                    new SimpleONode ( this.module,
                                  this, // SuperBlock
                                  onode_id,
                                  onode_ops,
                                  new NoSecurity<RecordFlag> (), //!!!!!!!!!!!!!!!!!!! no security yet
                                  onode_data,
                                  onode_metadata );

                // Metadata for the ONode:
                onode_metadata.hash ( Hash.NONE ); // !!!
                onode_metadata.objectSystemType ( this.objectSystemTypeRef );
                onode_metadata.recordType ( onode_type );
                onode_metadata.region ( onode_region );
                onode_metadata.sizeFieldsFree ( ArraySpace.STANDARD.none () ); // !!!
                onode_metadata.sizeFieldsUsed ( onode_region.size () ); // !!!
                onode_metadata.sizeRecordsFree ( ArraySpace.STANDARD.none () ); // !!!
                onode_metadata.sizeRecordsUsed ( ArraySpace.STANDARD.one () ); // !!!
                onode_metadata.space ( ArraySpace.STANDARD );
                onode_metadata.timeAccessed ( Time.NEVER ); // !!!
                onode_metadata.timeCreated ( Time.NEVER ); // !!!
                onode_metadata.timeDeleted ( Time.NEVER ); // !!!
                onode_metadata.timeDirtied ( Time.NEVER ); // !!!
                onode_metadata.timeModified ( Time.NEVER ); // !!!


                this.onodes.add ( onode );

                // Tally metadata (number of fields, number of onodes, and
                // so on).
                Size num_onode_fields =
                    ArraySpace.STANDARD.from ( onode_region.size () );
                num_object_system_fields =
                    ArraySpace.STANDARD.expr ( num_object_system_fields )
                    .add ( num_onode_fields );

                position = super_region.expr ( position )
                    .next ();
                super_block_onode_size ++;
            }

            Size num_onodes = ArraySpace.STANDARD.size ( onode_num );

            // Now the super block's ONode.
            Region super_block_onode_region =
                ArraySpace.STANDARD ()
                .region ( super_region.start (),
                          position );
            onode_id = new ONodeIdentifier ( this.id (), 0L );
            ONode super_block_onode =
                new SimpleONode ( this.module,
                                  this, // SuperBlock
                                  onode_id,
                                  new GnObjectOperations ( this.module ), // ops
                                  new NoSecurity<RecordFlag> (), //!!!!!!!!!!!!!!!!!!! no security yet
                                  this.data, // segment / data,
                                  new GnONodeMetadata () ); // metadata

            // Set up the metadata for the root onode.
	    ONodeMetadata super_block_onode_metadata =
		super_block_onode.metadata ();
            super_block_onode_metadata.hash ( Hash.NONE ); // !!!
            super_block_onode_metadata.objectSystemType ( this.objectSystemTypeRef );
            super_block_onode_metadata.recordType ( ObjectRecord.TYPE_ID );
            super_block_onode_metadata.region ( super_block_onode_region );
            super_block_onode_metadata.sizeFieldsFree ( ArraySpace.STANDARD.none () ); // !!!
            super_block_onode_metadata.sizeFieldsUsed ( num_onodes );
            super_block_onode_metadata.sizeRecordsFree ( ArraySpace.STANDARD.none () ); // !!!
            super_block_onode_metadata.sizeRecordsUsed ( ArraySpace.STANDARD.one () );
            super_block_onode_metadata.space ( ArraySpace.STANDARD );
            super_block_onode_metadata.timeAccessed ( Time.NEVER ); // !!!
            super_block_onode_metadata.timeCreated ( Time.NEVER ); // !!!
            super_block_onode_metadata.timeDeleted ( Time.NEVER ); // !!!
            super_block_onode_metadata.timeDirtied ( Time.NEVER ); // !!!
            super_block_onode_metadata.timeModified ( Time.NEVER ); // !!!


            this.onodes.add ( 0, super_block_onode );

            // Set up the metadata for the whole object system.
            this.metadata.hash ( Hash.NONE ); // !!!
            this.metadata.objectSystemType ( this.objectSystemTypeRef );
            this.metadata.recordType ( RecordTypeIdentifier.NONE );
            this.metadata.region ( ArraySpace.STANDARD.empty () ); // ??? do we need a real region for the whole object system...?
            this.metadata.sizeFieldsFree ( ArraySpace.STANDARD.none () ); // !!!
            this.metadata.sizeFieldsUsed ( num_object_system_fields );
            this.metadata.sizeRecordsFree ( ArraySpace.STANDARD.none () ); // !!!
            this.metadata.sizeRecordsUsed ( num_onodes );
            this.metadata.space ( ArraySpace.STANDARD );
            this.metadata.timeAccessed ( Time.NEVER ); // !!!
            this.metadata.timeCreated ( Time.NEVER ); // !!!
            this.metadata.timeDeleted ( Time.NEVER ); // !!!
            this.metadata.timeDirtied ( Time.NEVER ); // !!!
            this.metadata.timeModified ( Time.NEVER ); // !!!

            /* GnOsys-specific stats: */
            /* !!!
                        placeholder.environment ().create ( "stat_gnosys_super_block",
                                                            this.id () ),
                        placeholder.environment ().create ( "stat_gnosys_segment_id",
                                                            this.data.id () ),
                        placeholder.environment ().create ( "stat_gnosys_num_onodes",
                                                            this.onodes.size () ),
                        placeholder.environment ().create ( "stat_gnosys_num_onodes_pending_deletion",
                                                            this.onodesPendingDeletion.size () ),
                        placeholder.environment ().create ( "stat_gnosys_num_cursors",
                                                            this.cursorIdentifiers.size () )
                                                            !!! */

            this.isSetup = true;
        }
        finally
        {
            mutex_lock.unlock ();
        }
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#allocate(musaico.security.Credentials,musaico.io.Progress,musaico.security.Security,musaico.kernel.objectsystem.RecordTypeIdentifier)
     */
    @Override
    public ONode allocate (
                           Credentials credentials,
                           Progress progress,
                           Security<RecordFlag> onode_security,
                           RecordTypeIdentifier onode_type
                           )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException
    {
        if ( credentials == null
             || progress == null
             || onode_security == null
             || onode_type == null )
        {
            throw new I18nIllegalArgumentException ( "SuperBlock [%super_block%] cannot allocate an ONode for credentials [%credentials%] with progress [%progress%] onode_security [%onode_security%] onode_type [%onode_type%]",
                                                     "super_block", this,
                                                     "credentials", credentials,
                                                     "progress", progress,
                                                     "onode_security", onode_security,
                                                     "onode_type", onode_type );
        }

        // is the caller allowed to allocate ONodes?
        Permissions<SuperBlockFlag> requested_permissions =
            new SuperBlockPermissions ( credentials,
                                        this.id (),
                                        SuperBlockPermission.CREATE_ONODE );
        Permissions<SuperBlockFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SuperBlockOperationException ( "Access denied: [%credentials%] cannot allocate an ONode from SuperBlock [%super_block%] with security [%security%] (requested: [%requested_permissions%] granted: [%granted_permissions%])",
                                                     "credentials", credentials,
                                                     "super_block", this,
                                                     "security", security,
                                                     "requested_permissions", requested_permissions,
                                                     "granted_permissions", granted_permissions );
        }

        // OK now it's safe to do our thing.
        // Create an ONode.
        final ONode onode;
        final SuperBlockLock mutex_lock =
            new SuperBlockLock ( this.mutex );
        mutex_lock.lock ();
        try
        {
            if ( this.onodesFree.size () == 0 )
            {
                throw new SuperBlockOperationException ( "SuperBlock [%super_block%] has no free ONodes available to allocate",
                                                         "super_block", this );
            }

            onode = this.onodesFree.get ( 0 );
            this.onodesFree.remove ( 0 );
            // !!!!! WE SHOULD GIVE IT OPERATIONS...!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            // Register the new ONode in the kernel.
            try
            {
                this.module.createKernelObject ( onode.id (), onode );
            }
            catch ( ModuleOperationException e )
            {
                throw new SuperBlockOperationException ( e );
            }

            this.onodes.add ( onode );
            this.onodesMap.put ( onode.id (), onode );
        }
        finally
        {
            mutex_lock.unlock ();
        }

        return onode;
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#cursorRefs()
     */
    @Override
    public Sequence<CursorIdentifier> cursorRefs ()
    {
        final CursorIdentifier [] cursor_ids;
        // Lock ourselves.
        final SuperBlockLock mutex_lock =
            new SuperBlockLock ( this.mutex );
        try
        {
            mutex_lock.lock ();
        }
        catch ( SuperBlockOperationException e )
        {
            // Can't get a lock on ourselves...  Give up and
            // blow up real good.
            // !!! NEED A BETTER WAY TO DEAL WITH THIS...
            throw new I18nRuntimeException ( e );
        }

        try
        {
            CursorIdentifier [] template =
                new CursorIdentifier [ this.cursorIdentifiers.size () ];
            cursor_ids = this.cursorIdentifiers.toArray ( template );
        }
        finally
        {
            mutex_lock.unlock ();
        }

        Sequence<CursorIdentifier> cursor_refs =
            new ArbitraryOrderIndex ( CursorIdentifier.NONE,
                                      cursor_ids );

        return cursor_refs;
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#delete(musaico.security.Credentials,musaico.io.Progress,musaico.kernel.objectsystem.ONode)
     */
    @Override
    public void delete (
                        Credentials credentials,
                        Progress progress,
                        ONode onode
                        )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException
    {
        if ( credentials == null
             || progress == null
             || onode == null )
        {
            throw new I18nIllegalArgumentException ( "SuperBlock [%super_block%] cannot delete ONode [%onode%] for credentials [%credentials%] with progress [%progress%]",
                                                     "super_block", this,
                                                     "onode", onode,
                                                     "credentials", credentials,
                                                     "progress", progress );
        }

        // is the caller allowed to delete ONodes?
        Permissions<SuperBlockFlag> requested_permissions =
            new SuperBlockPermissions ( credentials,
                                        this.id (),
                                        SuperBlockPermission.FREE_ONODE );
        Permissions<SuperBlockFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SuperBlockOperationException ( "Access denied: [%credentials%] cannot delete an ONode from SuperBlock [%super_block%] with security [%security%] (requested: [%requested_permissions%] granted: [%granted_permissions%])",
                                                     "credentials", credentials,
                                                     "super_block", this,
                                                     "security", security,
                                                     "requested_permissions", requested_permissions,
                                                     "granted_permissions", granted_permissions );
        }

        // OK now it's safe to do our thing.
        // Lock ourselves and the ONode.
        final SuperBlockLock mutex_lock =
            new SuperBlockLock ( this.mutex,
                                 onode.mutex () );
        mutex_lock.lock ();
        try
        {
            // Check that nobody is referencing the ONode.
            long num_references = 0L;
            num_references += onode.references ().hardLinks ().count ();
            num_references += onode.references ().readers ().count ();
            num_references += onode.references ().writers ().count ();

            if ( num_references > 0L )
            {
                throw new SuperBlockOperationException ( "SuperBlock [%super_block%] cannot delete ONode [%onode%] because it is still referenced: hard links [%referenced_by_hard_links%] readers [%referenced_by_readers%] writers [%referenced_by_writers%]",
                                                         "super_block", this,
                                                         "onode", onode,
                                                         "referenced_by_hard_links", onode.references ().hardLinks (),
                                                         "referenced_by_readers", onode.references ().readers (),
                                                         "referenced_by_writers", onode.references ().writers () );
            }

            // Truncate the pages in the ONode.
            onode.truncate (); // !!! Maybe should use ops.truncate () but it requires an OEntry, which cannot be uniquely identified from the ONode side...

            // Remove the ONode from our list.
            this.onodes.remove ( onode );
            this.onodesMap.remove ( onode.id () );
        }
        finally
        {
            mutex_lock.unlock ();
        }
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#dirty(musaico.security.Credentials,musaico.io.Progress,musaico.kernel.objectsystem.ONode)
     */
    @Override
    public void dirty (
                       Credentials credentials,
                       Progress progress,
                       ONode onode
                       )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException
    {
        if ( credentials == null
             || progress == null
             || onode == null )
        {
            throw new I18nIllegalArgumentException ( "SuperBlock [%super_block%] cannot dirty ONode [%onode%] for credentials [%credentials%] with progress [%progress%]",
                                                     "super_block", this,
                                                     "onode", onode,
                                                     "credentials", credentials,
                                                     "progress", progress );
        }

        // is the caller allowed to dirty ONodes?
        Permissions<SuperBlockFlag> requested_permissions =
            new SuperBlockPermissions ( credentials,
                                        this.id (),
                                        SuperBlockPermission.MODIFY_ONODE );
        Permissions<SuperBlockFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SuperBlockOperationException ( "Access denied: [%credentials%] cannot dirty an ONode from SuperBlock [%super_block%] with security [%security%] (requested: [%requested_permissions%] granted: [%granted_permissions%])",
                                                     "credentials", credentials,
                                                     "super_block", this,
                                                     "security", security,
                                                     "requested_permissions", requested_permissions,
                                                     "granted_permissions", granted_permissions );
        }

        // OK now it's safe to do our thing.
        // Lock the ONode.
        final SuperBlockLock mutex_lock =
            new SuperBlockLock ( onode.mutex () );
        mutex_lock.lock ();
        try
        {
            onode.dirty ();
        }
        catch ( ONodeOperationException e )
        {
            throw new SuperBlockOperationException ( e );
        }
        finally
        {
            mutex_lock.unlock ();
        }
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#free(musaico.security.Credentials,musaico.io.Progress,musaico.kernel.objectsystem.ONode)
     */
    @Override
    public void free (
                      Credentials credentials,
                      Progress progress,
                      ONode onode
                      )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException
    {
        if ( credentials == null
             || progress == null
             || onode == null )
        {
            throw new I18nIllegalArgumentException ( "SuperBlock [%super_block%] cannot delete ONode [%onode%] for credentials [%credentials%] with progress [%progress%]",
                                                     "super_block", this,
                                                     "onode", onode,
                                                     "credentials", credentials,
                                                     "progress", progress );
        }

        // is the caller allowed to free ONodes?
        Permissions<SuperBlockFlag> requested_permissions =
            new SuperBlockPermissions ( credentials,
                                        this.id (),
                                        SuperBlockPermission.FREE_ONODE );
        Permissions<SuperBlockFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SuperBlockOperationException ( "Access denied: [%credentials%] cannot free an ONode from SuperBlock [%super_block%] with security [%security%] (requested: [%requested_permissions%] granted: [%granted_permissions%])",
                                                     "credentials", credentials,
                                                     "super_block", this,
                                                     "security", security,
                                                     "requested_permissions", requested_permissions,
                                                     "granted_permissions", granted_permissions );
        }

        // OK now it's safe to do our thing.
        // Lock ourselves and the ONode.
        final SuperBlockLock mutex_lock =
            new SuperBlockLock ( this.mutex,
                                 onode.mutex () );
        mutex_lock.lock ();
        try
        {
            // Check that nobody is referencing the ONode.
            long num_references = 0L;
            num_references += onode.references ().hardLinks ().count ();
            num_references += onode.references ().readers ().count ();
            num_references += onode.references ().writers ().count ();

            if ( num_references > 0L )
            {
                throw new SuperBlockOperationException ( "SuperBlock [%super_block%] cannot delete ONode [%onode%] because it is still referenced: hard links [%referenced_by_hard_links%] readers [%referenced_by_readers%] writers [%referenced_by_writers%]",
                                                         "super_block", this,
                                                         "onode", onode,
                                                         "referenced_by_hard_links", onode.references ().hardLinks (),
                                                         "referenced_by_readers", onode.references ().readers (),
                                                         "referenced_by_writers", onode.references ().writers () );
            }

            try
            {
                this.module.deleteKernelObject ( onode.id () );
            }
            catch ( ModuleOperationException e )
            {
                throw new SuperBlockOperationException ( e );
            }

            // Dirty the ONode.
            onode.dirty ();

            // Move the ONode from the main ONodes list to the
            // "pending deletion" list, so that nobody can get
            // a new hold on the ONode.
            if ( this.onodes.remove ( onode ) )
            {
                this.onodesPendingDeletion.add ( onode );
            }
            this.onodesMap.remove ( onode.id () );
        }
        catch ( ONodeOperationException e )
        {
            throw new SuperBlockOperationException ( e );
        }
        finally
        {
            mutex_lock.unlock ();
        }
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#id()
     */
    @Override
    public SuperBlockIdentifier id ()
    {
        return this.id;
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#isDirty()
     */
    @Override
    public boolean isDirty ()
    {
        // Lock ourselves.
        final SuperBlockLock mutex_lock =
            new SuperBlockLock ( this.mutex );
        try
        {
            mutex_lock.lock ();
        }
        catch ( SuperBlockOperationException e )
        {
            // Can't get a lock on ourselves...  Give up and
            // blow up real good.
            // !!! NEED A BETTER WAY TO DEAL WITH THIS...
            throw new I18nRuntimeException ( e );
        }

        try
        {
            if ( this.onodesPendingDeletion.size () > 0 )
            {
                return true;
            }

            for ( int o = 0; o < this.onodes.size (); o ++ )
            {
                ONode onode = this.onodes.get ( o );
                if ( onode.isDirty () )
                {
                    return true;
                }
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }

        // Didn't find any dirty ONodes.
        return false;
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#metadata(musaico.security.Credentials,musaico.io.Progress)
     */
    @Override
        public ONodeMetadata metadata ()
    {
        return this.metadata;
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#mutex(musaico.security.Credentials)
     */
    @Override
    public Mutex mutex (
                        Credentials credentials
                        )
    {
        // is the caller allowed access to our mutex?
        Permissions<SuperBlockFlag> requested_permissions =
            new SuperBlockPermissions ( credentials,
                                        this.id (),
                                        SuperBlockPermission.LOCK_MUTEX );
        Permissions<SuperBlockFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SuperBlockOperationException ( "Access denied: [%credentials%] cannot lock mutex of SuperBlock [%super_block%] with security [%security%] (requested: [%requested_permissions%] granted: [%granted_permissions%])",
                                                     "credentials", credentials,
                                                     "super_block", this,
                                                     "security", security,
                                                     "requested_permissions", requested_permissions,
                                                     "granted_permissions", granted_permissions );
        }

        // OK now it's safe to do our thing.
        return this.mutex;
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#objectSystemTypeRef()
     */
    @Override
    public ObjectSystemTypeIdentifier objectSystemTypeRef ()
    {
        return this.objectSystemTypeRef;
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#onodes(musaico.security.Credentials,musaico.io.Progress)
     */
    @Override
    public Sequence<ONode> onodes (
                                   Credentials credentials,
                                   Progress progress
                                   )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException
    {
        if ( credentials == null
             || progress == null )
        {
            throw new I18nIllegalArgumentException ( "SuperBlock [%super_block%] cannot retrieve ONodes for credentials [%credentials%] with progress [%progress%]",
                                                     "super_block", this,
                                                     "credentials", credentials,
                                                     "progress", progress );
        }

        // is the caller allowed to retrieve ONodes?
        Permissions<SuperBlockFlag> requested_permissions =
            new SuperBlockPermissions ( credentials,
                                        this.id (),
                                        SuperBlockPermission.RETRIEVE_ONODES );
        Permissions<SuperBlockFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SuperBlockOperationException ( "Access denied: [%credentials%] cannot retrieve ONodes from SuperBlock [%super_block%] with security [%security%] (requested: [%requested_permissions%] granted: [%granted_permissions%])",
                                                     "credentials", credentials,
                                                     "super_block", this,
                                                     "security", security,
                                                     "requested_permissions", requested_permissions,
                                                     "granted_permissions", granted_permissions );
        }

        // OK now it's safe to do our thing.
        final ONode [] onodes;
        // Lock ourselves.
        final SuperBlockLock mutex_lock =
            new SuperBlockLock ( this.mutex );
        mutex_lock.lock ();
        try
        {
            ONode [] template = new ONode [ this.onodes.size () ];
            onodes = this.onodes.toArray ( template );
        }
        finally
        {
            mutex_lock.unlock ();
        }

        // Now create an index out of the onodes.
        Sequence<ONode> index_of_onodes =
            new ArbitraryOrderIndex ( ONode.NONE,
                                      onodes );
        return index_of_onodes;
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#quotas()
     */
    @Override
    public Quotas quotas ()
    {
        return this.quotas;
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#readMountOptions(musaico.security.Credentials,musaico.io.Progress,musaico.buffer.Buffer)
     */
    @Override
    public Region readMountOptions (
                                    Credentials credentials,
                                    Progress progress,
                                    Buffer placeholder
                                    )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException
    {
        if ( credentials == null
             || progress == null
             || placeholder == null )
        {
            throw new I18nIllegalArgumentException ( "SuperBlock [%super_block%] cannot read mount options for credentials [%credentials%] with progress [%progress%] into placeholder [%placeholder%]",
                                                     "super_block", this,
                                                     "credentials", credentials,
                                                     "progress", progress,
                                                     "placeholder", placeholder );
        }

        // is the caller allowed to read mount options?
        Permissions<SuperBlockFlag> requested_permissions =
            new SuperBlockPermissions ( credentials,
                                        this.id (),
                                        SuperBlockPermission.READ_METADATA );
        Permissions<SuperBlockFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SuperBlockOperationException ( "Access denied: [%credentials%] cannot read mount options from SuperBlock [%super_block%] with security [%security%] (requested: [%requested_permissions%] granted: [%granted_permissions%])",
                                                     "credentials", credentials,
                                                     "super_block", this,
                                                     "security", security,
                                                     "requested_permissions", requested_permissions,
                                                     "granted_permissions", granted_permissions );
        }

        // OK now it's safe to do our thing.
        // Lock ourselves.
        final Region copied_region;
        final SuperBlockLock mutex_lock =
            new SuperBlockLock ( this.mutex );
        mutex_lock.lock ();
        try
        {
            copied_region =
                BufferTools.copy ( this.mountOptions,
                                   this.mountOptions.region (),
                                   placeholder,
                                   placeholder.region () );
        }
        catch ( BufferException e )
        {
            throw new SuperBlockOperationException ( e );
        }
        finally
        {
            mutex_lock.unlock ();
        }

        return copied_region;
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#references()
     */
    @Override
    public ReferenceCount references ()
    {
        return this.references;
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#remountObjectSystem(musaico.security.Credentials,musaico.io.Progress,int,String[])
     */
    @Override
    public void remountObjectSystem (
                                     Credentials credentials,
                                     Progress progress,
                                     Buffer mount_options
                                     )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException
    {
        if ( credentials == null
             || progress == null
             || mount_options == null )
        {
            throw new I18nIllegalArgumentException ( "SuperBlock [%super_block%] cannot re-mount for credentials [%credentials%] with progress [%progress%] with options [%mount_options%]",
                                                     "super_block", this,
                                                     "credentials", credentials,
                                                     "progress", progress,
                                                     "mount_options", mount_options );
        }

        // is the caller allowed to remount?
        Permissions<SuperBlockFlag> requested_permissions =
            new SuperBlockPermissions ( credentials,
                                        this.id (),
                                        SuperBlockPermission.REMOUNT );
        Permissions<SuperBlockFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SuperBlockOperationException ( "Access denied: [%credentials%] cannot remount SuperBlock [%super_block%] with security [%security%] (requested: [%requested_permissions%] granted: [%granted_permissions%])",
                                                     "credentials", credentials,
                                                     "super_block", this,
                                                     "security", security,
                                                     "requested_permissions", requested_permissions,
                                                     "granted_permissions", granted_permissions );
        }

        // OK now it's safe to do our thing.
        throw new SuperBlockOperationException ( "!!! RE-MOUNTING NOT IMPLEMENTED BY GnSuperBlock" );
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#root()
     */
    @Override
    public OEntry root ()
    {
        return this.mountPoint;
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#security()
     */
    @Override
    public Security<SuperBlockFlag> security ()
    {
        return this.security;
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#space()
     */
    @Override
    public Space space ()
    {
        return this.data.space;
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#syncObjectSystem(musaico.security.Credentials,musaico.io.Progress)
     */
    @Override
    public void syncObjectSystem (
                                  Credentials credentials,
                                  Progress progress
                                  )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException
    {
        if ( credentials == null
             || progress == null )
        {
            throw new I18nIllegalArgumentException ( "SuperBlock [%super_block%] cannot synchronize the object system for credentials [%credentials%] with progress [%progress%]",
                                                     "super_block", this,
                                                     "credentials", credentials,
                                                     "progress", progress );
        }

        // is the caller allowed to sync this super block?
        Permissions<SuperBlockFlag> requested_permissions =
            new SuperBlockPermissions ( credentials,
                                        this.id (),
                                        SuperBlockPermission.SYNC );
        Permissions<SuperBlockFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SuperBlockOperationException ( "Access denied: [%credentials%] cannot sync SuperBlock [%super_block%] with security [%security%] (requested: [%requested_permissions%] granted: [%granted_permissions%])",
                                                     "credentials", credentials,
                                                     "super_block", this,
                                                     "security", security,
                                                     "requested_permissions", requested_permissions,
                                                     "granted_permissions", granted_permissions );
        }

        // OK now it's safe to do our thing.
        throw new SuperBlockOperationException ( "!!! SYNC'ING THE OBJECT SYSTEM NOT IMPLEMENTED BY GnSuperBlock" );
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#write(musaico.security.Credentials,musaico.io.Progress,musaico.kernel.objectsystem.ONode)
     */
    @Override
    public void write (
                       Credentials credentials,
                       Progress progress,
                       ONode onode
                       )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException
    {
        if ( credentials == null
             || progress == null
             || onode == null )
        {
            throw new I18nIllegalArgumentException ( "SuperBlock [%super_block%] cannot write ONode [%onode%] for credentials [%credentials%] with progress [%progress%]",
                                                     "super_block", this,
                                                     "onode", onode,
                                                     "credentials", credentials,
                                                     "progress", progress );
        }

        // is the caller allowed to write ONodes?
        Permissions<SuperBlockFlag> requested_permissions =
            new SuperBlockPermissions ( credentials,
                                        this.id (),
                                        SuperBlockPermission.WRITE_ONODE );
        Permissions<SuperBlockFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SuperBlockOperationException ( "Access denied: [%credentials%] cannot write ONodes to SuperBlock [%super_block%] with security [%security%] (requested: [%requested_permissions%] granted: [%granted_permissions%])",
                                                     "credentials", credentials,
                                                     "super_block", this,
                                                     "security", security,
                                                     "requested_permissions", requested_permissions,
                                                     "granted_permissions", granted_permissions );
        }

        // OK now it's safe to do our thing.
        // Lock the ONode.
        final SuperBlockLock mutex_lock =
            new SuperBlockLock ( onode.mutex () );
        mutex_lock.lock ();
        try
        {
            try
            {
                onode.sync ();
            }
            catch ( ONodeOperationException e )
            {
                throw new SuperBlockOperationException ( "SuperBlock [%super_block%] failed to write ONode [%onode%]",
                                                         "super_block", this,
                                                         "onode", onode,
                                                         "cause", e );
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }
    }


    /**
     * @see musaico.kernel.objectsystem.SuperBlock#writeSuperBlock(musaico.security.Credentials,musaico.io.Progress)
     */
    @Override
    public void writeSuperBlock (
                                 Credentials credentials,
                                 Progress progress
                                 )
        throws I18nIllegalArgumentException,
               SuperBlockOperationException
    {
        if ( credentials == null
             || progress == null )
        {
            throw new I18nIllegalArgumentException ( "SuperBlock [%super_block%] cannot write out the super block for credentials [%credentials%] with progress [%progress%]",
                                                     "super_block", this,
                                                     "credentials", credentials,
                                                     "progress", progress );
        }

        // is the caller allowed to write this super block?
        Permissions<SuperBlockFlag> requested_permissions =
            new SuperBlockPermissions ( credentials,
                                        this.id (),
                                        SuperBlockPermission.WRITE_ONODE );
        Permissions<SuperBlockFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SuperBlockOperationException ( "Access denied: [%credentials%] cannot write SuperBlock [%super_block%] with security [%security%] (requested: [%requested_permissions%] granted: [%granted_permissions%])",
                                                     "credentials", credentials,
                                                     "super_block", this,
                                                     "security", security,
                                                     "requested_permissions", requested_permissions,
                                                     "granted_permissions", granted_permissions );
        }

        // OK now it's safe to do our thing.
        // Lock ourselves.
        final Region copied_region;
        ONode super_block_onode = this.onodes.get ( 0 );
        final SuperBlockLock mutex_lock =
            new SuperBlockLock ( this.mutex,
                                 super_block_onode.mutex () );
        mutex_lock.lock ();
        try
        {
            try
            {
                super_block_onode.sync ();
            }
            catch ( ONodeOperationException e )
            {
                throw new SuperBlockOperationException ( "SuperBlock [%super_block%] failed to write ONode [%onode%]",
                                                         "super_block", this,
                                                         "onode", super_block_onode,
                                                         "cause", e );
            }
        }
        finally
        {
            mutex_lock.unlock ();
        }
    }
}
