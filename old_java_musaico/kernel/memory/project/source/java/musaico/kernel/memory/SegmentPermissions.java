package musaico.kernel.memory;

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
 * perform operations on a Segment, or a virtual buffer built on
 * a segment of memory (reading, writing, and so on).
 * </p>
 *
 * <p>
 * For example, to request read and execute permissions on a Segment:
 * </p>
 *
 * <pre>
 *     Segment segment = ...;
 *     SegmentPermissions requested_permissions =
 *         new SegmentPermissions ( ...credentials...,
 *                                  segment.id (),
 *                                  SegmentPermission.READ,
 *                                  SegmentPermission.WRITE );
 *     SegmentPermissions granted_permissions =
 *         segment.security ().request ( requested_permissions );
 * </p>
 *
 * <p>
 * Module providers typically pass <code> my_module.credentials () </code>
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
 * Copyright (c) 2011, 2012 Johann Tienhaara
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
public class SegmentPermissions
    extends SimplePermissions<SegmentFlag>
    implements Serializable
{
    /**
     * <p>
     * Creates a new SegmentPermissions (requested or granted,
     * depending on who is creating the permissions!) with the
     * specified credentials, target memory/segment/page/and so on,
     * and memory permissions flags.
     * </p>
     *
     * @param credentials Either a reference to a KernelCredentials,
     *                    or the KernelCredentials itself.
     *                    Be very careful about passing around
     *                    KernelCredentials!  Must not be null.
     *
     * @param target The memory/segment/page and so on on which
     *               permissions are being requested/granted to
     *               perform certain operations.  The target can
     *               be a Memory identifier, a Segment identifier,
     *               a Page identifier, a PagedArea identifier,
     *               and so on.  Must not be null.
     *
     * @param permissions The memory permissions (such as
     *                    SegmentPermission.READ) and also any
     *                    advisory flags (such as
     *                    SegmentFlag.ADVISE_RANDOM_ACCESS)
     *                    to place in the request.  Must not be
     *                    null.  Must not contain any null elements.
     *                    Can be zero-length.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid.
     */
    public SegmentPermissions (
                               Credentials credentials,
                               Reference target,
                               SegmentFlag... permissions
                               )
        throws I18nIllegalArgumentException
    {
        super ( credentials, target, permissions );
    }
}
