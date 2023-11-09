package musaico.kernel_modules.objectsystems.checklist;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Filter;
import musaico.io.Path;
import musaico.io.Position;
import musaico.io.Progress;
import musaico.io.Reference;
import musaico.io.ReferenceCount;
import musaico.io.Region;
import musaico.io.RegionMethods;
import musaico.io.UnknownRegion;

import musaico.buffer.Buffer;

import musaico.buffer.filters.FieldIDFilter;

import musaico.field.Field;

import musaico.io.positions.EndPosition;
import musaico.io.positions.OffsetFromPosition;
import musaico.io.positions.StartPosition;

import musaico.io.references.SimpleSoftReference;

import musaico.io.regions.FixedLengthRegion;

import musaico.io.sizes.Length;
import musaico.io.sizes.UnknownSize;

import musaico.kernel.memory.Segment;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleOperationException;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.NoQuotas;
import musaico.kernel.objectsystem.ObjectTime;
import musaico.kernel.objectsystem.OEntry;
import musaico.kernel.objectsystem.ONode;
import musaico.kernel.objectsystem.ONodeOperationException;
import musaico.kernel.objectsystem.ONodeOperations;
import musaico.kernel.objectsystem.ONodeSecurityException;
import musaico.kernel.objectsystem.Record;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordPermission;
import musaico.kernel.objectsystem.RecordPermissions;
import musaico.kernel.objectsystem.SuperBlock;
import musaico.kernel.objectsystem.SuperBlockOperationException;

import musaico.kernel.objectsystem.records.ObjectRecord;

import musaico.kernel.objectsystems.onodes.GenericONodeOperations;

import musaico.kernel.oentries.SimpleOEntry;

import musaico.security.Credentials;
import musaico.security.NoSecurity; // !!! SECURITY
import musaico.security.Permissions;
import musaico.security.Security;

import musaico.time.AbsoluteTime;
import musaico.time.Time;


/**
 * <p>
 * Operates on checklist ONodes.
 * </p>
 *
 * <p>
 * Each checklist is an hierarchical object containing
 * 0 or more child checklists as well as 0 or more steps.
 * Each step is a flat record and essentially describes
 * something that can be checkmarked in the checklist.
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
 *        with a mutex lock. </li>
 *   <li> Any and all permissions have been checked on the credentials
 *        for the oepration. </li>
 * </ul>
 *
 * @see musaico.kernel.objectsystems.onodes.VirtualONodeOperations
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
public class ChecklistOperations
    extends GenericONodeOperations
    implements ObjectRecord, Serializable
{
    /** The Region inside an ONode in which metadata about the
     *  checklist is stored. */
    public static final Region REGION_METADATA =
        new FixedLengthRegion ( new SimpleSoftReference<String> ( "metadata" ),
                                new Length ( 64L ),
                                new StartPosition (),
                                new OffsetFromPosition ( 63L ) );

    /** The Region inside an ONode in the actual checklist steps are stored. */
    public static final Region REGION_CHECKLIST =
        new FixedLengthRegion ( new SimpleSoftReference<String> ( "checklist" ),
                                new UnknownSize (),
                                new OffsetFromPosition ( 64L ),
                                new EndPosition () );


    /** The module which created these ONode operations and gives
     *  us access to kernel functionality. */
    private final Module module;


    /**
     * <p>
     * Creates a new ChecklistOperations inside the specified
     * Module.
     * </p>
     *
     * @param module The kernel module from which this
     *               ChecklistOperations was created.  Must not
     *               be null.  The module gives this ChecklistOperations
     *               access to the kernel.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid.
     */
    public ChecklistOperations (
                                Module module
                                )
    {
        if ( module == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a ChecklistOperations with module [%module%]",
                                                     "module", module );
        }

        this.module = module;

        this.module.traceEnter ( "ChecklistOperations()" );
        this.module.traceExit ( "ChecklistOperations()" );
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#createFlatRecordONode(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,java.lang.String,musaico.kernel.objectsystem.Permissions)
     */
    @Override
    public OEntry createFlatRecordONode (
                                         OEntry entry,
                                         Credentials credentials,
                                         Progress progress,
                                         String name,
                                         Permissions<RecordFlag> mode
                                         )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.createFlatRecordONode()" );

        final Reference super_block_ref = entry.superBlockRef ();
        final SuperBlock super_block;
        try
        {
            super_block = this.module.kernelObject ( SuperBlock.class,
                                                     super_block_ref );
        }
        catch ( ModuleOperationException e )
        {
            throw new ONodeOperationException ( "Access denied: Module exception while retrieving SuperBlock reference [%super_block_ref%]",
                                                "super_block_ref", super_block_ref,
                                                "cause", e );
        }

        ONode step = super_block.allocate ( this.module.credentials (),
                                            progress,
                                            mode );
        // !!! Weird why do we use OEntryFactory elsewhere but not here?
        // !!! (Linux is messy...  lookup_hash() ultimately calls d_alloc()
        // !!! to create a dentry.  We need a more elegant solution than
        // !!! that spaghetti code.  Postponed for now.)
        OEntry step_entry = new SimpleOEntry ( this.module,
                                               entry,
                                               name,
                                               super_block_ref,
                                               step.id (),
                                               true ); // is_mounted

        this.module.traceExit ( "ChecklistOperations.createFlatRecordONode()" );
        return step_entry;
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#createObjectONode(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,java.lang.String,musaico.kernel.objectsystem.Permissions)
     */
    @Override
    public OEntry createObjectONode (
                                     OEntry entry,
                                     Credentials credentials,
                                     Progress progress,
                                     String name,
                                     Permissions<RecordFlag> mode
                                     )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.createObjectONode()" );

        final Reference super_block_ref = entry.superBlockRef ();
        final SuperBlock super_block;
        try
        {
            super_block = this.module.kernelObject ( SuperBlock.class,
                                                     super_block_ref );
        }
        catch ( ModuleOperationException e )
        {
            throw new ONodeOperationException ( "Access denied: Module exception while retrieving SuperBlock reference [%super_block_ref%]",
                                                "super_block_ref", super_block_ref,
                                                "cause", e );
        }

        ONode checklist = super_block.allocate ( this.module.credentials (),
                                                 progress,
                                                 mode );
        // !!! Weird why do we use OEntryFactory elsewhere but not here?
        // !!! (Linux is messy...  lookup_hash() ultimately calls d_alloc()
        // !!! to create a dentry.  We need a more elegant solution than
        // !!! that spaghetti code.  Postponed for now.)
        OEntry checklist_entry = new SimpleOEntry ( this.module,
                                                    entry,
                                                    name,
                                                    super_block_ref,
                                                    checklist.id (),
                                                    true ); // is_mounted

        this.module.traceExit ( "ChecklistOperations.createObjectONode()" );
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#getMetadata(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,java.lang.String)
     */
    @Override
    public Field getMetadata (
                              OEntry entry,
                              Credentials credentials,
                              Progress progress,
                              String attribute_name
                              )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.getMetadata()" );

        // !!! Ext 2 doesn't use a buffer...  maybe we shouldn't either???


        // !!! Stupid linear search for now.
        ONode onode = this.module.kernelObject ( ONode.class,
                                                 entry.onodeRef () );
        Permissions<RecordFlag> permissions =
            new RecordPermissions ( cursor_owner,
                                    onode.id (),
                                    RecordPermission.READ,
                                    RecordFlag.RANDOM_ACCESS );
        Filter find_metadata = new FieldIDFilter ( !!!!!!!;
!!!
        get data in region ChecklistOperations.REGION_METADATA;
        !!!;

        // This ONode has no metadata.
        this.module.traceExit ( "ChecklistOperations.getMetadata()" );
        return null;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#ioctl(musaico.kernel.objectsystem.Cursor,musaico.kernel.objectsystem.ONode,musaico.io.Progress,musaico.io.Reference,musaico.buffer.Buffer)
     */
    @Override
    public Serializable ioctl (
                               Cursor cursor,
                               ONode onode,
                               Progress progress,
                               Reference ioctl_command,
                               Buffer ioctl_args
                               )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.ioctl()" );
        this.module.traceFail ( "ChecklistOperations.ioctl()" );

        throw new RecordOperationException ( "Ioctl not supported by ONode [%onode%] ops [%ops%] while trying to execute command [%ioctl_command%] with arguments [%ioctl_args%]",
                                             "onode", "?",
                                             "ops", this,
                                             "ioctl_command", ioctl_command,
                                             "ioctl_args", ioctl_args );
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#link(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,musaico.kernel.objectsystem.ONode)
     */
    @Override
    public long link (
                      OEntry entry,
                      Credentials credentials,
                      Progress progress,
                      ONode target_onode
                      )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.link()" );

        // Don't do anything.
        // Just return the existing referenc count.
        final long num_references_to_onode =
            target_onode.referencedByHardLinks ().count ()
            + target_onode.referencedByReaders ().count ()
            + target_onode.referencedByWriters ().count ();

        this.module.traceExit ( "ChecklistOperations.link()" );

        return num_references_to_onode;
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#listMetadata(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,musaico.buffer.Buffer)
     */
    @Override
    public Region listMetadata (
                                OEntry entry,
                                Credentials credentials,
                                Progress progress,
                                Buffer attributes
                                )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.listMetadata()" );

        // No metadata.
        Region actual_buffer_region =
            new FixedLengthRegion ( attributes.region ().id (),
                                    new Length ( 0L ) );

        this.module.traceExit ( "ChecklistOperations.listMetadata()" );

        return actual_buffer_region;
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#lookup(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,musaico.io.Path)
     */
    @Override
    public OEntry lookup (
                          OEntry entry,
                          Credentials credentials,
                          Progress progress,
                          Path path
                          )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.lookup()" );

        this.module.traceFail ( "ChecklistOperations.lookup()" );
        throw new ONodeOperationException ( "Lookup not supported by ONode [%onode%] ops [%ops%] at [%oentry%] while looking up path [%path%]",
                                            "onode", "?",
                                            "ops", this,
                                            "oentry", entry,
                                            "path", path );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#mmap(musaico.kernel.objectsystem.Cursor,musaico.io.Progress,musaico.kernel.memory.Segment)
     */
    @Override
    public void mmap (
                      Cursor cursor,
                      Progress progress,
                      Segment segment
                      )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.mmap()" );

        this.module.traceFail ( "ChecklistOperations.mmap()" );
        throw new RecordOperationException ( "MMap is not supported by ONode [%onode%] ops [%ops%] at [%oentry%]",
                                             "onode", "?",
                                             "ops", this,
                                             "oentry", cursor.entry () );
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#move(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,musaico.kernel.objectsystem.OEntry)
     */
    @Override
    public void move (
                      OEntry old_entry,
                      Credentials credentials,
                      Progress progress,
                      OEntry new_entry
                      )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.move()" );

        this.module.traceFail ( "ChecklistOperations.move()" );
        throw new ONodeOperationException ( "Move not supported by ONode [%onode%] ops [%ops%] while trying to move from old OEntry [%old_oentry%] to new OEntry [%new_oentry%]",
                                            "onode", "?",
                                            "ops", this,
                                            "old_oentry", old_entry,
                                            "new_oentry", new_entry );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#open(musaico.kernel.objectsystem.Cursor,musaico.kernel.objectsystem.ONode,musaico.io.Progress)
     */
    @Override
    public void open (
                      Cursor cursor,
                      ONode onode,
                      Progress progress
                      )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.open()" );

        // Nothing to do.

        this.module.traceExit ( "ChecklistOperations.open()" );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#poll(musaico.kernel.objectsystem.Cursor,musaico.io.Progress)
     */
    @Override
    public void poll (
                      Cursor cursor,
                      Progress progress
                      )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.poll()" );

        this.module.traceFail ( "ChecklistOperations.poll()" );
        throw new RecordOperationException ( "Poll not supported by ONode [%onode%] ops [%ops%] at [%oentry%] with cursor [%cursor%]",
                                             "onode", "?",
                                             "ops", this,
                                             "oentry", cursor.entry (),
                                             "cursor", cursor );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#read(musaico.kernel.objectsystem.Cursor,musaico.io.Progress,musaico.buffer.Buffer,musaico.io.Region)
     */
    @Override
    public Region read (
                        Cursor cursor,
                        Progress progress,
                        Buffer read_fields,
                        Region read_into_region
                        )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.read()" );
        this.module.traceFail ( "ChecklistOperations.read()" );

        throw new RecordOperationException ( "Read not supported by ONode [%onode%] ops [%ops%] at [%oentry%] while trying to read into buffer [%buffer%] region [%buffer_region%] cursor [%cursor%]",
                                             "onode", "?",
                                             "ops", this,
                                             "oentry", cursor.entry (),
                                             "buffer", read_fields,
                                             "buffer_region", read_into_region,
                                             "cursor", cursor );
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#readRelation(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress)
     */
    @Override
    public OEntry readRelation (
                                OEntry entry,
                                Credentials credentials,
                                Progress progress
                                )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.readRelation()" );

        this.module.traceFail ( "ChecklistOperations.readRelation()" );
        throw new ONodeOperationException ( "ReadRelation not supported by ONode [%onode%] ops [%ops%] at [%oentry%]",
                                            "onode", "?",
                                            "ops", this,
                                            "oentry", entry );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#region(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public Region region (
                          Cursor cursor
                          )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.region()" );

        final ONode onode;
        try
        {
            onode = this.module.kernelObject ( ONode.class,
                                               cursor.entry ().onodeRef () );
        }
        catch ( ModuleOperationException e )
        {
            throw new ONodeOperationException ( "Access denied: Module exception while retrieving ONode reference [%onode_ref%] with security [%security%]",
                                                "onode_ref", cursor.entry ().onodeRef (),
                                                "security", "?",
                                                "cause", e );
        }

        final Region onode_region = onode.region ();

        this.module.traceExit ( "ChecklistOperations.region()" );

        return onode_region;
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#removeMetadata(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,java.lang.String)
     */
    @Override
    public Field removeMetadata (
                                 OEntry entry,
                                 Credentials credentials,
                                 Progress progress,
                                 String attribute_name
                                 )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.removeMetadata()" );

        // This ONode has no metadata.
        this.module.traceFail ( "ChecklistOperations.removeMetadata()" );
        throw new ONodeOperationException ( "RemoveMetadata not supported by onode [%onode%] ops [%ops%] at [%oentry%] while trying to remove [%attribute_name%]",
                                            "onode", "?",
                                            "ops", this,
                                            "oentry", entry,
                                            "attribute_name", attribute_name );
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#removeObject(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,musaico.kernel.objectsystem.OEntry)
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
        this.module.traceEnter ( "ChecklistOperations.removeObject()" );

        this.module.traceFail ( "ChecklistOperations.removeObject()" );
        throw new ONodeOperationException ( "RemoveObject not supported by parent ONode [%parent_onode%] at [%oentry%] while trying to remove child OEntry [%child_oentry%]",
                                            "parent_onode", "?",
                                            "oentry", entry,
                                            "child_oentry", child_object_entry );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#seek(musaico.kernel.objectsystem.Cursor,musaico.io.Progress,musaico.io.Position)
     */
    @Override
    public Position seek (
                          Cursor cursor,
                          Progress progress,
                          Position position
                          )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.seek()" );

        final ONode onode;
        try
        {
            onode = this.module.kernelObject ( ONode.class,
                                               cursor.entry ().onodeRef () );
        }
        catch ( ModuleOperationException e )
        {
            throw new ONodeOperationException ( "Access denied: Module exception while retrieving ONode reference [%onode_ref%] with security [%security%]",
                                                "onode_ref", cursor.entry ().onodeRef (),
                                                "security", "?",
                                                "cause", e );
        }

        final Region onode_region = onode.region ();
        final Position actual_position = onode_region.step ( position, 0L );

        // Let the caller execute cursor.position ( actual_position ).

        this.module.traceExit ( "ChecklistOperations.seek()" );

        return actual_position;
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#setMetadata(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,musaico.field.Field)
     */
    @Override
    public Field setMetadata (
                              OEntry entry,
                              Credentials credentials,
                              Progress progress,
                              Field attribute_field
                              )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.setMetadata()" );

        // This ONode has no metadata.
        this.module.traceFail ( "ChecklistOperations.setMetadata()" );
        throw new ONodeOperationException ( "SetMetadata not supported by ONode [%onode%] ops [%ops%] at [%oentry%] while attempting to set [%attribute_field%]",
                                            "onode", "?",
                                            "ops", this,
                                            "oentry", entry,
                                            "attribute_field", attribute_field );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#sync(musaico.kernel.objectsystem.Cursor,musaico.io.Progress,boolean)
     */
    @Override
    public void sync (
                      Cursor cursor,
                      Progress progress,
                      boolean is_metadata_only
                      )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.sync()" );

        this.module.traceFail ( "ChecklistOperations.sync()" );
        throw new RecordOperationException ( "Sync not supported by ONode [%onode%] ops [%ops%] at [%oentry%] metadata only? [%is_metadata_only%]",
                                             "onode", "?",
                                             "ops", this,
                                             "oentry", cursor.entry (),
                                             "is_metadata_only", is_metadata_only );
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#time(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,musaico.kernel.objectsystem.ObjectTime,musaico.time.Time)
     */
    @Override
    public void time (
                      OEntry entry,
                      Credentials credentials,
                      Progress progress,
                      ObjectTime time_type,
                      Time time
                      )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.time()" );

        // Just set it as metadata.
        String field_name = GenericMetadata.nameForObjectTime ( time_type );
        Field time_field = this.module.types ().create ( field_name, time );
        this.setMetadata ( entry, credentials, progress, time_field );

        this.module.traceExit ( "ChecklistOperations.time()" );
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#truncate(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,musaico.io.Region)
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
        this.module.traceEnter ( "ChecklistOperations.truncate()" );

        // This ONode does not allow truncating.
        this.module.traceFail ( "ChecklistOperations.truncate()" );
        throw new ONodeOperationException ( "Truncate not supported by ONode [%onode%] ops [%ops%] at [%oentry%] while truncating to region [%truncate_everything_outside_of_region%]",
                                            "onode", "?",
                                            "ops", this,
                                            "oentry", entry,
                                            "truncate_everything_outside_of_region", truncate_everything_outside_of_region );
    }


    /**
     * @see musaico.kernel.objectsystem.ONodeOperations#unlink(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,musaico.kernel.objectsystem.ONode)
     */
    @Override
    public long unlink (
                        OEntry entry,
                        Credentials credentials,
                        Progress progress,
                        ONode target_onode
                        )
        throws ONodeOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.unlink()" );

        // Don't do anything.
        // Just return the existing referenc count.
        final long num_references_to_onode =
            target_onode.referencedByHardLinks ().count ()
            + target_onode.referencedByReaders ().count ()
            + target_onode.referencedByWriters ().count ();

        this.module.traceExit ( "ChecklistOperations.unlink()" );

        return num_references_to_onode;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#write(musaico.kernel.objectsystem.Cursor,musaico.io.Progress,musaico.buffer.Buffer,musaico.io.Region)
     */
    @Override
    public Region write (
                         Cursor cursor,
                         Progress progress,
                         Buffer write_fields,
                         Region write_from_region
                         )
        throws RecordOperationException
    {
        this.module.traceEnter ( "ChecklistOperations.write()" );

        this.module.traceFail ( "ChecklistOperations.write()" );
        throw new RecordOperationException ( "Write not supported by ONode [%onode%] ops [%ops%] at [%oentry%] while trying to write from buffer [%buffer%] region [%buffer_region%] cursor [%cursor%]",
                                             "onode", "?",
                                             "ops", this,
                                             "oentry", cursor.entry (),
                                             "buffer", write_fields,
                                             "buffer_region", write_from_region,
                                             "cursor", cursor );
    }
}
