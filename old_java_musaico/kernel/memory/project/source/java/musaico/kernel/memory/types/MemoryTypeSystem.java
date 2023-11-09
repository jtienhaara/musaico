package musaico.kernel.memory.types;


import java.io.Serializable;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import musaico.kernel.memory.Segment;

import musaico.kernel.memory.paging.Page;

import musaico.types.SimpleTypeSystem;
import musaico.types.Type;
import musaico.types.TypeException;
import musaico.types.TypeSystem;
import musaico.types.TypingEnvironment;


/**
 * <p>
 * TypeSystem of memory types, such as Page and Segment and Memory
 * and so on.
 * </p>
 *
 * <p>
 * Memory types are all cross-platform,
 * so they can be shared between C, JavaScript, Tcl, Java,
 * and so on platforms.
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
 * Copyright (c) 2009 Johann Tienhaara
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
public class MemoryTypeSystem
    extends SimpleTypeSystem
    implements Serializable
{
    /**
     * <p>
     * Creates a new MemoryTypeSystem beneath the specified
     * type system.
     * </p>
     *
     * @param parent_type_system The parent type system, of which this
     *                           is a child.  Must not be null.
     *
     * @throws TypeException If the parent type system is null
     *                       or invalid.
     */
    public MemoryTypeSystem (
                              TypeSystem parent_type_system
                              )
        throws TypeException
    {
        super ( parent_type_system );
    }






    /**
     * <p>
     * Static method creates a memory type system, and adds
     * the standard memory types, inside the specified
     * TypingEnvironment.
     * </p>
     */
    public static TypeSystem standard (
                                       TypingEnvironment environment
                                       )
        throws TypeException
    {
        // Create the memory type system.
        TypeSystem memory = new MemoryTypeSystem ( environment.root () );

        // Create the types.
        Type<Page> page_type = new PageType ();
        Type<Segment> segment_type = new SegmentType ();

        // Add the types to the typing environment.
        environment.register ( Page.class, memory, page_type );
        environment.register ( Segment.class, memory, segment_type );

        return memory;
    }
}
