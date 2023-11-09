package musaico.kernel;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;

import musaico.security.Credentials;
import musaico.security.SimplePermissions;


/**
 * <p>
 * Permissions (requested by / granted to) a specific kernel module to
 * perform operations on a specific kernel object or namespace (such
 * as an ONode or a Driver or a SuperBlock or KernelNamespaces.SEGMENTS,
 * and so on).
 * </p>
 *
 * <p>
 * For example, to request direct access to a Driver:
 * </p>
 *
 * <pre>
 *     Module my_module = ...;
 *     DriverIdentifier driver_id = ...;
 *     KernelNamespace namespace =
 *         my_module.namespace ( KernelNamespace.DRIVERS );
 *     Driver driver =
 *         namespace.object ( driver_id ).retrieve ( my_module.credentials,
 *                                                   Driver.class );
 * </pre>
 *
 * <p>
 * Under the hood, the <code> retrieve () </code> method checks:
 * </p>
 *
 * <pre>
 *     KernelPermissions requested_permissions =
 *         new KernelPermissions ( ...credentials...,
 *                                 ...identifier of the object to retrieve...,
 *                                 KernelPermission.RETRIEVE_OBJECT );
 *     KernelPermissions granted_permissions =
 *         record.security ().request ( requested_permissions );
 *
 *     if ( ! granted_permissions.isAllowed ( requested_permissions ) )
 *     {
 *         ...throw security exception...
 *     }
 * </p>
 *
 *
 * <p>
 * In Java, every Permissions must be Serializable in order to play
 * nicely over RMI.
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
public class KernelPermissions
    extends SimplePermissions<KernelPermission>
    implements Serializable
{
    /**
     * <p>
     * Creates a new KernelPermissions (requested or granted,
     * depending on who is creating the permissions!) with the
     * specified credentials, target kernel object or namespace, and
     * permission flags.
     * </p>
     *
     * @param credentials The Credentials of the kernel module
     *                    requesting permission to access the
     *                    kernel object in some way.  Users should
     *                    never access kernel objects.
     *                    Must not be null.
     *
     * @param target The identifier on which permissions are being
     *               requested/granted to perform certain operations.
     *               The target can be a kernel object or a namespace
     *               such as KernelNamespaces.OBJECT_SYSTEMS_LAYER.
     *               Must not be null.
     *
     * @param permissions The kernel permissions (such as
     *                    KernelPermission.DELETE_OBJECT) to place in
     *                    the request.  Must not be
     *                    null.  Must not contain any null elements.
     *                    Can be zero-length.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid.
     */
    public KernelPermissions (
                              Credentials credentials,
                              Identifier target,
                              KernelPermission... permissions
                              )
        throws I18nIllegalArgumentException
    {
        super ( credentials, target, permissions );
    }
}
