package musaico.kernel.objectsystem.onode;

import java.io.Serializable;


import musaico.io.Path;

import musaico.kernel.objectsystem.RecordFlag;


/**
 * <p>
 * Permission to access an ONode in some way, such
 * as to lock its mutex for exclusive access.
 * </p>
 *
 *
 * <p>
 * In Java, every RecordFlag must be Serializable in order
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
public class ONodePermission
    extends RecordFlag
    implements Serializable
{
    /** Permission to lock an ONode's mutex. */
    public static final ONodePermission LOCK_MUTEX =
        new ONodePermission ( new Path ( "/permissions/record/onode/lock_mutex" ) );


    /**
     * <p>
     * Creates a new ONodePermission with the specified path.
     * </p>
     *
     * @param path The unique path identifier for this ONodePermission.
     *             Must not be null.
     */
    protected ONodePermission (
                               Path path
                               )
    {
        super ( path );
    }
}
