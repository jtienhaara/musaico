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
 * Copyright (c) 2009, 2011 Johann Tienhaara
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
public class MemoryFlag
    extends SimpleSoftReference<Path>
    implements Serializable
{
    /** Segment.flags: This segment is currently shared between
     *                 1 or more tasks. */
    public static final MemoryFlag SHARED           =
        new MemoryFlag ( new Path ( "/permissions/memory/shared" ) );


    /** Segment.flags: This segment can have its PagedArea
     *                 backing changed (for example from swap-backed
     *                 to mmap'ed). */
    public static final MemoryFlag CHANGE_PAGED_AREA =
        new MemoryFlag ( new Path ( "/permissions/memory/change_paged_area" ) );

    /** Segment.flags: This segment is NOT locked in some area of memory
     *                 (cache, physical, persistent, and so on), so
     *                 swapping can occur. */
    public static final MemoryFlag SWAP             =
        new MemoryFlag ( new Path ( "/permissions/memory/swap" ) );

    /** Segment.flags: This segment is for memory-mapped I/O. */
    public static final MemoryFlag IO               =
        new MemoryFlag ( new Path ( "/permissions/memory/io" ) );

    /** Segment.flags: App will access this segment sequentially. */
    public static final MemoryFlag ADVISE_SEQUENTIAL_READS =
        new MemoryFlag ( new Path ( "/permissions/memory/advise_sequential_reads" ) );

    /** Segment.flags: App will randomly access this segment.
     *                 Therefore it will not benefit from clustered reads. */
    public static final MemoryFlag ADVISE_RANDOM_READS =
        new MemoryFlag ( new Path ( "/permissions/memory/advise_random_reads" ) );


    /** Segment.flags: This segment is to be copied on fork (), as normal. */
    public static final MemoryFlag COPY             =
        new MemoryFlag ( new Path ( "/permissions/memory/copy" ) );

    /** Segment.flags: This segment is expandable with remap (). */
    public static final MemoryFlag EXPAND           =
        new MemoryFlag ( new Path ( "/permissions/memory/expand" ) );


    /** Segment.flags: This segment is audited by the virtual
     *                 memory accounting system. */
    public static final MemoryFlag ACCOUNT          =
        new MemoryFlag ( new Path ( "/permissions/memory/account" ) );

    /** Segment.flags: This segment must always be included in core dumps. */
    public static final MemoryFlag ALWAYS_DUMP      =
        new MemoryFlag ( new Path ( "/permissions/memory/always_dump" ) );




    /**
     * <p>
     * Creates a new MemoryFlag with the specified path.
     * </p>
     *
     * @param path The unique path identifier for this MemoryFlag.
     *             Must not be null.
     */
    protected MemoryFlag (
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
