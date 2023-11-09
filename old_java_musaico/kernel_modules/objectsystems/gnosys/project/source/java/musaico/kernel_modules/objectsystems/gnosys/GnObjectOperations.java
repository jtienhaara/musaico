package musaico.kernel_modules.objectsystems.gnosys;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.buffer.Buffer;
import musaico.buffer.BufferException;

import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Path;
import musaico.io.Progress;
import musaico.io.Reference;
import musaico.io.ReferenceCount;

import musaico.io.markers.RecordEnd;
import musaico.io.markers.RecordStart;

import musaico.kernel.memory.Segment;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleOperationException;

import musaico.kernel.objectsystem.Cursor;
import musaico.kernel.objectsystem.ObjectSystemIdentifier;
import musaico.kernel.objectsystem.OEntry;
import musaico.kernel.objectsystem.ONode;
import musaico.kernel.objectsystem.ONodeIdentifier;
import musaico.kernel.objectsystem.ONodeOperationException;
import musaico.kernel.objectsystem.ONodeOperations;
import musaico.kernel.objectsystem.ONodeSecurityException;
import musaico.kernel.objectsystem.Record;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.SuperBlock;
import musaico.kernel.objectsystem.SuperBlockOperationException;

import musaico.kernel.objectsystem.quotas.NoQuotas;

import musaico.kernel.objectsystem.records.FlatRecord;
import musaico.kernel.objectsystem.records.ObjectRecord;

import musaico.kernel.oentries.SimpleOEntry;

import musaico.region.Position;
import musaico.region.Region;

import musaico.security.Credentials;
import musaico.security.NoSecurity; // !!! SECURITY
import musaico.security.Permissions;
import musaico.security.Security;

import musaico.time.AbsoluteTime;
import musaico.time.Time;


/**
 * <p>
 * Operates on an hierarchical object node in a gnosys object system.
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
 *        for the operation. </li>
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
public class GnObjectOperations
    extends GnONodeOperations
    implements Serializable
{
    /**
     * <p>
     * Creates a new GnObjectOperations inside the specified
     * Module.
     * </p>
     *
     * @param module The kernel module from which this
     *               GnObjectOperations was created.  Must not
     *               be null.  The module gives this GnObjectOperations
     *               access to the kernel.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid.
     */
    public GnObjectOperations (
                               Module module
                               )
    {
        super ( module );
    }


    /**
     * @see musaico.kernel.objectsystems.GenericONodeOperations#addChildONode(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,musaico.kernel.objectsystem.ONode,musaico.kernel.objectsystem.OEntry,musaico.kernel.objectsystem.ONode)
     */
    @Override
    protected void addChildONode (
                                  OEntry parent_entry,
                                  Credentials credentials,
                                  Progress progress,
                                  ONode parent_onode,
                                  OEntry child_entry,
                                  ONode child_onode
                                  )
        throws ONodeOperationException
    {
        this.module ().traceEnter ( "GnObjectOperations.addChildONode()" );

        final GnONode gnonode;
        try
        {
            // !!! FOR NOW we just create a virtual buffer to
            // !!! automagically swap in the data we need from
            // !!! the start of the paged area backing this
            // !!! object system.
            gnonode = this.gnonode ( parent_entry );
            Buffer object_buffer = this.buffer ( parent_entry );

            // Each element of this onode is a Field:
            //
            //     Field.id = name of the child OEntry.
            //     Field.value = ONodeIdentifier linking to the child ONode.
            //
            // We need to find a slot to put the new child, and
            // add it there.  So we look for the RecordEnd close of
            // the object contents.
            Field record_end_marker_field = null;
            final Region gnonode_region = gnonode.region ();
            Position place_position = gnonode_region.space ().outOfBounds ();
            for ( Position position : gnonode_region )
            {
                Field field = object_buffer.get ( position );
                // No holes (null fields) allowed.
                if ( ( field.value () ) instanceof RecordStart )
                {
                    // Skip past the first Field in the object.
                    continue;
                }
                else if ( ( field.value () ) instanceof RecordEnd )
                {
                    // We're past the last child of this object.
                    record_end_marker_field = field;
                    place_position = position;
                    break;
                }
            }

            if ( ! region.contains ( place_position ) )
            {
                // No RecordEnd field?  Corruption!
                this.module ().traceFail ( "GnObjectOperations.addChildONode()" );
                throw new ONodeOperationException ( "Corruption: Cannot find RecordEnd terminator for GnONode [%onode%]",
                                                    "onode", gnonode );
            }
            else if ( ! region.contains ( region.expr ( place_position ).next () ) )
            {
                // No more space left in this ONode.
                this.module ().traceFail ( "GnObjectOperations.addChildONode()" );
                throw new ONodeOperationException ( "Object [%parent_entry%] ONode [%parent_onode%] is out of space for children while trying to add child [%child_entry%] ONode [%child_onode%]",
                                                    "parent_entry", parent_entry,
                                                    "parent_onode", gnonode,
                                                    "child_entry", child_entry,
                                                    "child_onode", child_onode );
            }

            // Create the field for the pointer to the child.
            Field child_onode_field =
                object_buffer.environment ().create ( child_entry.name (),
                                                      ONodeIdentifier.class,
                                                      child_onode.id () );

            // Now we need to move the RecordEnd marker back a slot
            // to make room for the new child ONode.
            object_buffer.set ( place_position, child_onode_field );
            place_position =
                object_buffer.region ().expr ( place_position ).previous ();
            object_buffer.set ( place_position, record_end_marker_field );
        }
        catch ( RecordOperationException e )
        {
            this.module ().traceFail ( "GnObjectOperations.addChildONode()" );
            throw new ONodeOperationException ( e );
        }
        catch ( BufferException e )
        {
            // !!! Probable corruption!!!
            this.module ().traceFail ( "GnObjectOperations.addChildONode()" );
            throw new ONodeOperationException ( e );
        }

        this.module ().traceExit ( "GnObjectOperations.addChildONode()" );
    }


    // !!! fcntl later...


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
        this.module ().traceEnter ( "GnObjectOperations.lookup()" );

        if ( path.depth () == 1
             && path.name ().equals ( "" ) )
        {
            // "/a/b/c/" or something ending in "/".
            return entry;
        }

        final GnONode gnonode;
        try
        {
            // !!! FOR NOW we just create a virtual buffer to
            // !!! automagically swap in the data we need from
            // !!! the start of the paged area backing this
            // !!! object system.
            gnonode = this.gnonode ( entry );
            Buffer object_buffer = this.buffer ( entry );

            // Each element of this onode is a Field:
            //
            //     Field.id = name of the child OEntry.
            //     Field.value = ONodeIdentifier linking to the child ONode.
            //
            // We need to lookup the toplevel name of the specified
            // Path, resolve the ONode, and possibly call lookup on
            // that ONode (if there is anything deeper in the path).
            for ( Position position : gnonode.region () )
            {
                Field field = object_buffer.get ( position );
                // No holes (null fields) allowed.
                if ( ( field.value () ) instanceof RecordStart )
                {
                    // Skip past the first Field in the object.
                    continue;
                }
                else if ( ( field.value () ) instanceof RecordEnd )
                {
                    // We're past the last child of this object.
                    break;
                }

                String child_name = field.id ();

                if ( child_name.equals ( path.name () ) )
                {
                    ONodeIdentifier child_onode_id =
                        field.value ( ONodeIdentifier.class );
                    OEntry child_entry =
                        new SimpleOEntry ( this.module (),
                                           entry,
                                           child_name,
                                           gnonode.superBlockRef (),
                                           child_onode_id,
                                           entry.isMounted () );
                    // Do we need to recurse looking up?
                    if ( path.depth () > 1 )
                    {
                        this.module ().traceExit ( "GnObjectOperations.lookup()" );
                        return this.lookup ( entry,
                                             credentials,
                                             progress,
                                             path.child () );
                    }

                    this.module ().traceExit ( "GnObjectOperations.lookup()" );
                    return child_entry;
                }
            }
        }
        catch ( RecordOperationException e )
        {
            this.module ().traceFail ( "GnObjectOperations.lookup()" );
            throw new ONodeOperationException ( e );
        }

        // If we got this far it means we didn't find the
        // specified child ONode.  So we fail the lookup.

        this.module ().traceFail ( "GnObjectOperations.lookup()" );
        throw new ONodeOperationException ( "OEntry [%entry%] ONode [%onode%] has no child [%path%]",
                                            "entry", entry,
                                            "onode", gnonode,
                                            "path", path );
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
        this.module.traceEnter ( "GenericONodeOperations.read()" );
        this.module.traceFail ( "GenericONodeOperations.read()" );

        throw new RecordOperationException ( "Read not supported by ONode [%onode%] ops [%ops%] at [%oentry%] while trying to read into buffer [%buffer%] region [%buffer_region%] cursor [%cursor%]",
                                             "onode", "?",
                                             "ops", this,
                                             "oentry", cursor.entry (),
                                             "buffer", read_fields,
                                             "buffer_region", read_into_region,
                                             "cursor", cursor );
    }


    /**
     * @see musaico.kernel.objectsystems.GenericONodeOperations#removeChildONode(musaico.kernel.objectsystem.OEntry,musaico.security.Credentials,musaico.io.Progress,musaico.kernel.objectsystem.ONode,musaico.kernel.objectsystem.OEntry,musaico.kernel.objectsystem.ONode)
     */
    @Override
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
        this.module ().traceEnter ( "GnONodeOperations.removeChildONode()" );

        final GnONode gnonode;
        try
        {
            // !!! FOR NOW we just create a virtual buffer to
            // !!! automagically swap in the data we need from
            // !!! the start of the paged area backing this
            // !!! object system.
            gnonode = this.gnonode ( parent_entry );
            Buffer object_buffer = this.buffer ( parent_entry );

            // Each element of this onode is a Field:
            //
            //     Field.id = name of the child OEntry.
            //     Field.value = ONodeIdentifier linking to the child ONode.
            //
            // We need to find a slot to put the new child, and
            // add it there.  So we look for the RecordEnd close of
            // the object contents.
            final Region gnonode_region = gnonode.region ();
            Position place_position = gnonode_region.space ().outOfBounds ();
            for ( Position position : gnonode_region )
            {
                Field field = object_buffer.get ( position );
                // No holes (null fields) allowed.
                if ( ( field.value () ) instanceof RecordStart )
                {
                    // Skip past the first Field in the object.
                    continue;
                }
                else if ( ( field.value () ) instanceof RecordEnd )
                {
                    // Couldn't find the specified child ONode.
                    this.module ().traceFail ( "GnONodeOperations.removeChildONode()" );
                    throw new ONodeOperationException ( "Object [%parent_entry%] ONode [%parent_onode%] has no child [%child_entry%] ONode [%child_onode%]",
                                                        "parent_entry", parent_entry,
                                                        "parent_onode", parent_onode,
                                                        "child_entry", child_entry,
                                                        "child_onode", child_onode );
                }
                else if ( field.id ().equals ( child_entry.name () ) )
                {
                    place_position = position;
                    break;
                }
            }

            if ( ! gnonode_region.contains ( place_position ) )
            {
                // No RecordEnd field?  Corruption!
                this.module ().traceFail ( "GnObjectOperations.addChildONode()" );
                throw new ONodeOperationException ( "Corruption: Cannot find RecordEnd terminator for GnONode [%onode%]",
                                                    "onode", gnonode );
            }

            // Remove the child and move everything back a step.
            final Region object_region = object_buffer.region ();
            for ( Position move_to = place_position;
                  object_region.contains ( move_to );
                  move_to = object_region.expr ( move_to ).next () )
            {
                Position move_from =
                    object_buffer.region ().expr ( move_to ).next ();
                Field move_field = object_buffer.get ( move_from );
                object_buffer.set ( move_to, move_field );

                if ( ( move_field.value () ) instanceof RecordEnd )
                {
                    object_buffer.set ( move_from, null );
                    break;
                }
            }
        }
        catch ( RecordOperationException e )
        {
            this.module ().traceFail ( "GnObjectOperations.removeChildONode()" );
            throw new ONodeOperationException ( e );
        }
        catch ( BufferException e )
        {
            // !!! Probable corruption!!!
            this.module ().traceFail ( "GnObjectOperations.removeChildONode()" );
            throw new ONodeOperationException ( e );
        }

        this.module ().traceExit ( "GnONodeOperations.removeChildONode()" );
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
        this.module ().traceEnter ( "GnObjectOperations.removeObject()" );

        this.module ().traceFail ( "GnObjectOperations.removeObject()" );
        throw new ONodeOperationException ( "RemoveObject not supported by parent ONode [%parent_onode%] at [%oentry%] while trying to remove child OEntry [%child_oentry%]",
                                            "parent_onode", "?",
                                            "oentry", entry,
                                            "child_oentry", child_object_entry );
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
        this.module ().traceEnter ( "GnObjectOperations.sync()" );

        this.module ().traceFail ( "GnObjectOperations.sync()" );
        throw new RecordOperationException ( "Sync not supported by ONode [%onode%] ops [%ops%] at [%oentry%] metadata only? [%is_metadata_only%]",
                                             "onode", "?",
                                             "ops", this,
                                             "oentry", cursor.entry (),
                                             "is_metadata_only", is_metadata_only );
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
        this.module.traceEnter ( "GenericONodeOperations.write()" );

        this.module.traceFail ( "GenericONodeOperations.write()" );
        throw new RecordOperationException ( "Write not supported by ONode [%onode%] ops [%ops%] at [%oentry%] while trying to write from buffer [%buffer%] region [%buffer_region%] cursor [%cursor%]",
                                             "onode", "?",
                                             "ops", this,
                                             "oentry", cursor.entry (),
                                             "buffer", write_fields,
                                             "buffer_region", write_from_region,
                                             "cursor", cursor );
    }
}
