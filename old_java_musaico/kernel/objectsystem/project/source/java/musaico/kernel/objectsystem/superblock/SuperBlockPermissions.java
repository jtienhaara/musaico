package musaico.kernel.objectsystem.superblock;

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
 * perform operations on a SuperBlock.
 * </p>
 *
 * <p>
 * For example, to request creating ONode permissions on a SuperBlock:
 * </p>
 *
 * <pre>
 *     SuperBlock super_block = ...;
 *     SuperBlockPermissions requested_permissions =
 *         new SuperBlockPermissions ( ...credentials...,
 *                                     super_block.id (),
 *                                     SuperBlockPermission.CREATE_ONODE );
 *     SuperBlockPermissions granted_permissions =
 *         super_block.security ().request ( requested_permissions );
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
public class SuperBlockPermissions
    extends SimplePermissions<SuperBlockFlag>
    implements Serializable
{
    /**
     * <p>
     * Creates a new SuperBlockPermissions (requested or granted,
     * depending on who is creating the permissions!) with the
     * specified credentials, target SuperBlock, and super block
     * permissions flags.
     * </p>
     *
     * @param credentials Either a reference to a KernelCredentials,
     *                    or the KernelCredentials itself.
     *                    Be very careful about passing around
     *                    KernelCredentials!  Must not be null.
     *
     * @param target The SuperBlock on which permissions are being
     *               requested/granted to perform certain operations.
     *               The target can be an ONode identifier, a Driver
     *               identifier, a Cursor identifier, and so on.
     *               Must not be null.
     *
     * @param permissions The super block permissions (such as
     *                    SuperBlockPermission.CREATE_ONODE) and also any
     *                    advisory flags to place in the request.
     *                    Must not be null.  Must not contain any
     *                    null elements.  Can be zero-length.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid.
     */
    public SuperBlockPermissions (
                                  Credentials credentials,
                                  Reference target,
                                  SuperBlockFlag... permissions
                                  )
        throws I18nIllegalArgumentException
    {
        super ( credentials, target, permissions );
    }
}
