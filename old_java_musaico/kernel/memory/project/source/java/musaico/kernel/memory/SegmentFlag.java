package musaico.kernel.memory;


import java.io.Serializable;


import musaico.io.Path;

import musaico.io.references.SimpleSoftReference;


/**
 * <p>
 * Flags affecting how segments and pages can be manipulated.
 * </p>
 *
 * <p>
 * For example, some pages are read-only, some are shared among
 * processes, some are locked and not swappable, and so on.
 * </p>
 *
 *
 * <p>
 * In Java, every SegmentFlag must be Serializable in order to play nicely
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
 * Copyright (c) 2009, 2011, 2012 Johann Tienhaara
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
public class SegmentFlag
    extends SimpleSoftReference<Path>
    implements Serializable
{
    /** Segment.flags: This segment is currently shared between
     *                 1 or more tasks. */
    public static final SegmentFlag SHARED           =
        new SegmentFlag ( new Path ( "/permissions/memory/shared" ) );


    /** Segment.flags: This segment can be closed by the caller. */
    public static final SegmentFlag ALLOW_CLOSE =
        new SegmentFlag ( new Path ( "/permissions/memory/allow_close" ) );

    /** Segment.flags: This segment can be opened by the caller. */
    public static final SegmentFlag ALLOW_OPEN =
        new SegmentFlag ( new Path ( "/permissions/memory/allow_open" ) );

    /** Segment.flags: This segment can return its PagedArea
     *                 to the caller. */
    public static final SegmentFlag ALLOW_GET_PAGED_AREA =
        new SegmentFlag ( new Path ( "/permissions/memory/allow_get_paged_area" ) );

    /** Segment.flags: This segment can have its PagedArea
     *                 backing changed (for example from swap-backed
     *                 to mmap'ed). */
    public static final SegmentFlag ALLOW_CHANGE_PAGED_AREA =
        new SegmentFlag ( new Path ( "/permissions/memory/allow_change_paged_area" ) );

    /** Segment.flags: This segment is NOT locked in some area of memory
     *                 (cache, physical, persistent, and so on), so
     *                 swapping can occur. */
    public static final SegmentFlag ALLOW_SWAP       =
        new SegmentFlag ( new Path ( "/permissions/memory/allow_swap" ) );

    /** Segment.flags: This segment is for memory-mapped I/O. */
    public static final SegmentFlag IO_MMAP          =
        new SegmentFlag ( new Path ( "/permissions/memory/io" ) );

    /** Segment.flags: App will access this segment sequentially. */
    public static final SegmentFlag ADVISE_SEQUENTIAL_READS =
        new SegmentFlag ( new Path ( "/permissions/memory/advise_sequential_reads" ) );

    /** Segment.flags: App will randomly access this segment.
     *                 Therefore it will not benefit from clustered reads. */
    public static final SegmentFlag ADVISE_RANDOM_READS =
        new SegmentFlag ( new Path ( "/permissions/memory/advise_random_reads" ) );


    /** Segment.flags: This segment is to be copied on fork (), as normal. */
    public static final SegmentFlag COPY_ON_FORK     =
        new SegmentFlag ( new Path ( "/permissions/memory/copy" ) );

    /** Segment.flags: This segment is expandable with remap (). */
    public static final SegmentFlag ALLOW_EXPAND_VIA_REMAP =
        new SegmentFlag ( new Path ( "/permissions/memory/expand" ) );


    /** Segment.flags: This segment is audited by the virtual
     *                 memory accounting system. */
    public static final SegmentFlag ACCOUNT_AUDITING =
        new SegmentFlag ( new Path ( "/permissions/memory/account" ) );

    /** Segment.flags: This segment must always be included in core dumps. */
    public static final SegmentFlag ALWAYS_DUMP      =
        new SegmentFlag ( new Path ( "/permissions/memory/always_dump" ) );




    /**
     * <p>
     * Creates a new SegmentFlag with the specified path.
     * </p>
     *
     * @param path The unique path identifier for this SegmentFlag.
     *             Must not be null.
     */
    protected SegmentFlag (
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
