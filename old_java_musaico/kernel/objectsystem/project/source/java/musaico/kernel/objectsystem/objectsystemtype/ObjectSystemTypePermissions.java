package musaico.kernel.objectsystem.objectsystemtype;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;

import musaico.security.Credentials;
import musaico.security.SimplePermissions;


/**
 * <p>
 * Permissions (requested by / granted to) a specific requester to
 * perform operations on an ObjectSystemType.
 * </p>
 *
 * <p>
 * For example, to request mounting a SuperBlock permissions on an
 * ObjectSystemType:
 * </p>
 *
 * <pre>
 *     ObjectSystemType object_system_type = ...;
 *     ObjectSystemTypePermissions requested_permissions =
 *         new ObjectSystemTypePermissions ( ...credentials...,
 *                               object_system_type.id (),
 *                               ObjectSystemTypePermission.MOUNT );
 *     ObjectSystemTypePermissions granted_permissions =
 *         object_system_type.security ().request ( requested_permissions );
 * </p>
 *
 * <p>
 * Module provides typically pass <code> my_module.credentials () </code>
 * as their credentials, whereas user-space requests involve
 * a user reference.
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
public class ObjectSystemTypePermissions
    extends SimplePermissions<ObjectSystemTypeFlag>
    implements Serializable
{
    /**
     * <p>
     * Creates a new ObjectSystemTypePermissions (requested or granted,
     * depending on who is creating the permissions!) with the
     * specified credentials, target ObjectSystemType, and
     * object system type permissions flags.
     * </p>
     *
     * @param credentials Either a reference to a KernelCredentials,
     *                    or the KernelCredentials itself.
     *                    Be very careful about passing around
     *                    KernelCredentials!  Must not be null.
     *
     * @param target The ObjectSystemType on which permissions are being
     *               requested/granted to perform certain operations.
     *               The target can be an ONode identifier, a Driver
     *               identifier, a Cursor identifier, and so on.
     *               Must not be null.
     *
     * @param permissions The object system type permissions (such as
     *                    ObjectSystemTypePermission.MOUNT) and also any
     *                    advisory flags to place in the request.
     *                    Must not be null.  Must not contain any
     *                    null elements.  Can be zero-length.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid.
     */
    public ObjectSystemTypePermissions (
                                        Credentials credentials,
                                        Reference target,
                                        ObjectSystemTypeFlag... permissions
                                        )
        throws I18nIllegalArgumentException
    {
        super ( credentials, target, permissions );
    }
}
