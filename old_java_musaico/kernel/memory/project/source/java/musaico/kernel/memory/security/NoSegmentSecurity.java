package musaico.kernel.memory.security;

import java.io.Serializable;


import musaico.kernel.memory.SegmentFlag;
import musaico.kernel.memory.SegmentPermission;
import musaico.kernel.memory.SegmentSecurity;

import musaico.security.Permissions;
import musaico.security.Security;


/**
 * <p>
 * A laissez-faire SegmentSecurity.  You should really not use
 * this except for testing!
 * </p>
 *
 *
 * <p>
 * In Java, every Security implementation must implement Serializable
 * in order to play nicely across RMI.
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
public class NoSegmentSecurity
    implements SegmentSecurity, Serializable
{
    /**
     * @see musaico.kernel.memory.SegmentSecurity#flagsModifiable()
     */
    public SegmentFlag [] flagsModifiable ()
    {
        return new SegmentFlag []
            {
                SegmentFlag.SHARED,
                SegmentFlag.ALLOW_CLOSE,
                SegmentFlag.ALLOW_OPEN,
                SegmentFlag.ALLOW_GET_PAGED_AREA,
                SegmentFlag.ALLOW_CHANGE_PAGED_AREA,
                SegmentFlag.ALLOW_SWAP,
                SegmentFlag.IO_MMAP,
                SegmentFlag.ADVISE_SEQUENTIAL_READS,
                SegmentFlag.ADVISE_RANDOM_READS,
                SegmentFlag.COPY_ON_FORK,
                SegmentFlag.ALLOW_EXPAND_VIA_REMAP,
                SegmentFlag.ACCOUNT_AUDITING,
                SegmentFlag.ALWAYS_DUMP,

                SegmentPermission.READ,
                SegmentPermission.WRITE,
                SegmentPermission.EXEC
            };
    }


    /**
     * @see musaico.security.Security#request(musaico.security.Permissions)
     */
    public Permissions<SegmentFlag> request (
                                            Permissions<SegmentFlag> requested_permissions
                                            )
    {
        Permissions<SegmentFlag> granted_permissions =
            requested_permissions;
        return granted_permissions;
    }
}
