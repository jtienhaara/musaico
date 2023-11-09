package musaico.kernel.memory;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalStateException;


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
 * In Java, SegmentFlags is Serializable in order to play nicely
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
public final class SegmentFlags
    implements Serializable
{
    /** Segment.flags: This segment is currently readable. */
    public static final long READ                         = 0x00000001;

    /** Segment.flags: This segment is currently writable. */
    public static final long WRITE                        = 0x00000002;

    /** Segment.flags: This segment is currently executable. */
    public static final long EXEC                         = 0x00000004;

    /** Segment.flags: This segment is currently shared between
     *                 1 or more tasks. */
    public static final long SHARED                       = 0x00000008;


    /** Segment.flags: This segment is locked in some area of memory
     *                 (cache, physical, persistent, and so on). */
    public static final long DO_NOT_SWAP                  = 0x00002000;

    /** Segment.flags: This segment is for memory-mapped I/O. */
    public static final long IO                           = 0x00004000;

    /** Segment.flags: App will access this segment sequentially. */
    public static final long ADVISE_SEQUENTIAL_READS      = 0x00008000;

    /** Segment.flags: App will randomly access this segment.
     *                 Therefore it will not benefit from clustered reads. */
    public static final long ADVISE_RANDOM_READS          = 0x00010000;


    /** Segment.flags: This segment is not to be copied on fork (). */
    public static final long DO_NOT_COPY                  = 0x00020000;

    /** Segment.flags: This segment is not expandable with remap (). */
    public static final long DO_NOT_EXPAND                = 0x00040000;


    /** Segment.flags: This segment is audited by the virtual
     *                 memory accounting system. */
    public static final long ACCOUNT                      = 0x00100000;

    /** Segment.flags: This segment must always be included in core dumps. */
    public static final long ALWAYS_DUMP                  = 0x04000000;


    /** All flags OR'ed together. */
    public static final long ALL                          = 0xFFFFFFFF;


    /**
     * <p>
     * No reason to ever instantiate this class.
     * </p>
     */
    private SegmentFlags ()
    {
        throw new I18nIllegalStateException ( "[%class%] should never be instantiated",
                                              "class", SegmentFlags.class.getName () );
    }
}
