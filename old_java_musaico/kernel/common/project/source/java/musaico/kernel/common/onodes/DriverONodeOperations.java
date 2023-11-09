package musaico.kernel.common.onodes;

import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Progress;
import musaico.io.Reference;

import musaico.kernel.driver.Driver;

import musaico.kernel.module.Module;
import musaico.kernel.module.ModuleOperationException;

import musaico.kernel.objectsystem.onode.ONode;

import musaico.region.Position;
import musaico.region.Region;


/**
 * <p>
 * The ONode operations for a DriverONode, allowing a Driver to be
 * accessed through the object system (for example, opening the
 * Driver for raw reading and writing, and so on).
 * </p>
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
public class DriverONodeOperations
    extends GenericONodeOperations
    implements Serializable
{
    /** The Module which provides these ops with access
     *  to the kernel and other kernel modules. */
    private final Module module;


    /**
     * <p>
     * Creates a new DriverONodeOperations inside the specified
     * Module.
     * </p>
     *
     * @param module The kernel module from which this
     *               DriverONodeOperations was created.  Must not
     *               be null.  The module gives this DriverONodeOperations
     *               access to the kernel.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid.
     */
    public DriverONodeOperations (
                                  Module module
                                  )
    {
        super ( module );

        if ( module == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a DriverONodeOperations with module [%module%]",
                                                     "module", module );
        }

        this.module = module;

        this.module.traceEnter ( "DriverONodeOperations()" );
        this.module.traceExit ( "DriverONodeOperations()" );
    }
}
