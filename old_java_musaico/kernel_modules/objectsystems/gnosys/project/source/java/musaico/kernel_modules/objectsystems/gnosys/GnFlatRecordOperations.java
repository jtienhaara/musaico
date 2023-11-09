package musaico.kernel_modules.objectsystems.gnosys;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.buffer.Buffer;
import musaico.buffer.BufferException;
import musaico.buffer.BufferTools;

import musaico.buffer.filters.FieldIDFilter;

import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Filter;
import musaico.io.Path;
import musaico.io.Progress;
import musaico.io.Reference;
import musaico.io.ReferenceCount;

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
 * Operates on a flat record node in a gnosys object system.
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
public class GnFlatRecordOperations
    extends GnONodeOperations
    implements Serializable
{
    /**
     * <p>
     * Creates a new GnFlatRecordOperations inside the specified
     * Module.
     * </p>
     *
     * @param module The kernel module from which this
     *               GnFlatRecordOperations was created.  Must not
     *               be null.  The module gives this GnFlatRecordOperations
     *               access to the kernel.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid.
     */
    public GnFlatRecordOperations (
                                   Module module
                                   )
    {
        super ( module );
    }


    // !!! fcntl eventually...


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
        this.module ().traceEnter ( "GnFlatRecordOperations.mmap()" );

        this.module ().traceFail ( "GnFlatRecordOperations.mmap()" );
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
        this.module ().traceEnter ( "GnFlatRecordOperations.move()" );

        this.module ().traceFail ( "GnFlatRecordOperations.move()" );
        throw new ONodeOperationException ( "Move not supported by ONode [%onode%] ops [%ops%] while trying to move from old OEntry [%old_oentry%] to new OEntry [%new_oentry%]",
                                            "onode", "?",
                                            "ops", this,
                                            "old_oentry", old_entry,
                                            "new_oentry", new_entry );
    }


    // !!! poll eventually...


    // Fall back on the default GenericONodeOperations.read().


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
        this.module ().traceEnter ( "GnFlatRecordOperations.sync()" );

        this.module ().traceFail ( "GnFlatRecordOperations.sync()" );
        throw new RecordOperationException ( "Sync not supported by ONode [%onode%] ops [%ops%] at [%oentry%] metadata only? [%is_metadata_only%]",
                                             "onode", "?",
                                             "ops", this,
                                             "oentry", cursor.entry (),
                                             "is_metadata_only", is_metadata_only );
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
        this.module ().traceEnter ( "GnFlatRecordOperations.truncate()" );

        // This ONode does not allow truncating.
        this.module ().traceFail ( "GnFlatRecordOperations.truncate()" );
        throw new ONodeOperationException ( "Truncate not supported by ONode [%onode%] ops [%ops%] at [%oentry%] while truncating to region [%truncate_everything_outside_of_region%]",
                                            "onode", "?",
                                            "ops", this,
                                            "oentry", entry,
                                            "truncate_everything_outside_of_region", truncate_everything_outside_of_region );
    }


    // Fall back on the default GenericONodeOperations.write().
}
