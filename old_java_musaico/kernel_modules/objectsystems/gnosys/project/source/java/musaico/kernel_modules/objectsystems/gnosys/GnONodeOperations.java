package musaico.kernel_modules.objectsystems.gnosys;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.buffer.Buffer;

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
 * Operates on generic ONodes.
 * </p>
 *
 * <p>
 * Each generic ONode is just a sequence of Fields and/or
 * child ONodes.  No more typing information is known about
 * the objects in a gnosys object system.
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
public abstract class GnONodeOperations
    extends GenericONodeOperations
    implements Serializable
{
    /** The module which created these ONode operations and gives
     *  us access to kernel functionality. */
    private final Module module;


    /**
     * <p>
     * Creates a new GnONodeOperations inside the specified
     * Module.
     * </p>
     *
     * @param module The kernel module from which this
     *               GnONodeOperations was created.  Must not
     *               be null.  The module gives this GnONodeOperations
     *               access to the kernel.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid.
     */
    public GnONodeOperations (
                              Module module
                              )
    {
        super ( module );

        if ( module == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a GnONodeOperations with module [%module%]",
                                                     "module", module );
        }

        this.module = module;

        this.module.traceEnter ( "GnONodeOperations()" );
        this.module.traceExit ( "GnONodeOperations()" );
    }


    /**
     * <p>
     * Returns the GnONode pointed to by the specified entry.
     * </p>
     *
     * <p>
     * Package-private.
     * </p>
     */
    GnONode gnonode (
                     OEntry entry
                     )
        throws RecordOperationException
    {
        // Get the ONode pointed to by the specified
        // OEntry.
        final GnONode gnonode;
        try
        {
            gnonode = (GnONode)
                this.module.getKernelObject ( entry.onodeRef () );
        }
        catch ( ClassCastException e )
        {
            throw new RecordOperationException ( "Exception while retrieving ONode reference [%onode_ref%]",
                                                 "onode_ref", entry.onodeRef (),
                                                 "cause", e );
        }
        catch ( ModuleOperationException e )
        {
            throw new RecordOperationException ( "Exception while retrieving ONode reference [%onode_ref%]",
                                                 "onode_ref", entry.onodeRef (),
                                                 "cause", e );
        }

        return gnonode;
    }


    /**
     * <p>
     * Returns the Module that gives these operations access
     * to the kernel and other kernel modules.
     * </p>
     *
     * <p>
     * Package-private.
     * </p>
     */
    Module module ()
    {
        return this.module;
    }
}
