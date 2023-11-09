package musaico.kernel.memory;


import java.io.Serializable;


import musaico.io.Path;


/**
 * <p>
 * Permissions for accessing memory (read and/or write and/or execute).
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
public class SegmentPermission
    extends SegmentFlag
    implements Serializable
{
    /** Segment.flags: This segment can be resized. */
    public static final SegmentPermission RESIZE          =
        new SegmentPermission ( new Path ( "/permissions/memory/resize" ) );

    /** Segment.flags: This segment is currently readable. */
    public static final SegmentPermission READ             =
        new SegmentPermission ( new Path ( "/permissions/memory/read" ) );

    /** Segment.flags: This segment is currently writable. */
    public static final SegmentPermission WRITE            =
        new SegmentPermission ( new Path ( "/permissions/memory/write" ) );

    /** Segment.flags: This segment is currently executable. */
    public static final SegmentPermission EXEC             =
        new SegmentPermission ( new Path ( "/permissions/memory/exec" ) );


    /**
     * <p>
     * Creates a new SegmentPermission with the specified path.
     * </p>
     *
     * @param path The unique path identifier for this SegmentPermission.
     *             Must not be null.
     */
    protected SegmentPermission (
                                 Path path
                                 )
    {
        super ( path );
    }
}
