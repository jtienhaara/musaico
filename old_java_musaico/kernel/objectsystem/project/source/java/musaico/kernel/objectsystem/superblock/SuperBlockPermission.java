package musaico.kernel.objectsystem.superblock;

import java.io.Serializable;


import musaico.io.Path;


/**
 * <p>
 * Permission to access a SuperBlock in some way, such
 * as to create or delete ONodes from the super block.
 * </p>
 *
 *
 * <p>
 * In Java, every SuperBlockFlag must be Serializable in order
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
public class SuperBlockPermission
    extends SuperBlockFlag
    implements Serializable
{
    /** Permission to allocate an ONODE. */
    public static final SuperBlockPermission CREATE_ONODE =
        new SuperBlockPermission ( new Path ( "/permissions/super_block/create_onode" ) );

    /** Permission to free and/or delete an ONODE. */
    public static final SuperBlockPermission FREE_ONODE =
        new SuperBlockPermission ( new Path ( "/permissions/super_block/free_onode" ) );

    /** Permission to lock an entire super block. */
    public static final SuperBlockPermission LOCK_MUTEX =
        new SuperBlockPermission ( new Path ( "/permissions/super_block/lock_mutex" ) );

    /** Permission to retrieve all the ONodes from the SuperBlock. */
    public static final SuperBlockPermission RETRIEVE_ONODES =
        new SuperBlockPermission ( new Path ( "/permissions/super_block/get_onodes" ) );

    /** Permission to modify an ONODE (dirty() and so on). */
    public static final SuperBlockPermission MODIFY_ONODE =
        new SuperBlockPermission ( new Path ( "/permissions/super_block/modify_onode" ) );

    /** Permission to read the quotas from the SuperBlock. */
    public static final SuperBlockPermission READ_QUOTAS =
        new SuperBlockPermission ( new Path ( "/permissions/super_block/read_quotas" ) );

    /** Permission to read metadata from a SuperBlock (mount
     *  options, stat, and so on). */
    public static final SuperBlockPermission READ_METADATA =
        new SuperBlockPermission ( new Path ( "/permissions/super_block/read_metadata" ) );

    /** Permission to re-mount the SuperBlock. */
    public static final SuperBlockPermission REMOUNT =
        new SuperBlockPermission ( new Path ( "/permissions/super_block/remount" ) );

    /** Permission to sync the whole SuperBlock (freeze and write out). */
    public static final SuperBlockPermission SYNC =
        new SuperBlockPermission ( new Path ( "/permissions/super_block/sync" ) );

    /** Permission to write ONodes (including the SuperBlock itself)
     *  out to the backing driver. */
    public static final SuperBlockPermission WRITE_ONODE =
        new SuperBlockPermission ( new Path ( "/permissions/super_block/write_onode" ) );

    /** Permission to write quotas to the SuperBlock. */
    public static final SuperBlockPermission WRITE_QUOTAS =
        new SuperBlockPermission ( new Path ( "/permissions/super_block/write_quotas" ) );


    /**
     * <p>
     * Creates a new SuperBlockPermission with the specified path.
     * </p>
     *
     * @param path The unique path identifier for this SuperBlockPermission.
     *             Must not be null.
     */
    protected SuperBlockPermission (
                                    Path path
                                    )
    {
        super ( path );
    }
}
