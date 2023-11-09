package musaico.kernel.memory;


import java.io.Serializable;


import musaico.security.Security;


/**
 * <p>
 * Provides security for a Segment of memory.
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
 * Copyright (c) 2009, 2012 Johann Tienhaara
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
public interface SegmentSecurity
    extends Security<SegmentFlag>, Serializable
{
    /**
     * <p>
     * Returns the access flags for this segment of virtual memory
     * which may be set (for example through mprotect ()).
     * </p>
     *
     * @return The flags which may be changed for this Segment.
     *         Never negative.
     */
    public abstract SegmentFlag [] flagsModifiable ();


    // Every SegmentSecurity must implement
    // the method request ( Permissions<SegmentFlag> ): Permissions<SegmentFlag>
}
