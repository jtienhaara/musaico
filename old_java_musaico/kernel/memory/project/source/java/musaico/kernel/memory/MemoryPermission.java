package musaico.kernel.memory;


import java.io.Serializable;


import musaico.io.references.SimpleSoftReference;

import musaico.io.Path;


/**
 * <p>
 * Permissions for accessing memory (read and/or write and/or execute).
 * </p>
 *
 *
 * <p>
 * In Java, every MemoryFlag must be Serializable in order to play nicely
 * over RMI.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public class MemoryPermission
    extends SimpleSoftReference<Path>
    implements Serializable
{
    /** Memory permission: allocate Buffer space. */
    public static final MemoryPermission ALLOCATE         =
        new MemoryPermission ( new Path ( "/permissions/memory/allocate" ) );

    /** Memory permission: free Buffer space. */
    public static final MemoryPermission FREE             =
        new MemoryPermission ( new Path ( "/permissions/memory/free" ) );


    /**
     * <p>
     * Creates a new MemoryPermission with the specified path.
     * </p>
     *
     * @param path The unique path identifier for this MemoryPermission.
     *             Must not be null.
     */
    protected MemoryPermission (
                                Path path
                                )
    {
        super ( path );
    }


    /**
     * @see musaico.io.Reference#toString()
     */
    public String toString ()
    {
        return "" + this.id ();
    }
}
