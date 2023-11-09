package musaico.kernel;

import java.io.Serializable;


import musaico.io.Path;

import musaico.io.references.SimpleSoftReference;


/**
 * <p>
 * Permissions for accessing Kernels, such as reading and/or
 * writing kernel objects.
 * </p>
 *
 *
 * <p>
 * In Java, every KernelPermission must be Serializable in order
 * to play nicely across RMI.
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
public class KernelPermission
    extends SimpleSoftReference<Path>
    implements Serializable
{
    /** KernelNamespace Permission: Create a new kernel object in the
     *                              namespace. */
    public static final KernelPermission CREATE_OBJECT =
        new KernelPermission ( new Path ( "/permissions/kernel/create_object" ) );

    /** KernelNamespace Permission: Delete a kernel object from the
     *                              namespace. */
    public static final KernelPermission DELETE_OBJECT =
        new KernelPermission ( new Path ( "/permissions/kernel/delete_object" ) );

    /** KernelObject Permission: Retrieve the actual object. */
    public static final KernelPermission RETRIEVE_OBJECT =
        new KernelPermission ( new Path ( "/permissions/kernel/retrieve_object" ) );

    /** KernelObject Permission: Replace the actual object with a new one. */
    public static final KernelPermission UPDATE_OBJECT =
        new KernelPermission ( new Path ( "/permissions/kernel/update_object" ) );


    /**
     * <p>
     * Creates a new KernelPermission with the specified path.
     * </p>
     *
     * @param path The unique path identifier for this KernelPermission.
     *             Must not be null.
     */
    protected KernelPermission (
                                Path path
                                )
    {
        super ( path );
    }
}
