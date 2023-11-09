package musaico.kernel.memory.virtual;


import musaico.kernel.memory.Memory;


/**
 * <p>
 * A memory manager for virtual memory.  A Buffer allocated
 * by this Memory is backed by the Segment behind the
 * VirtualMemory, so the memory is grouped in Pages and can
 * be swapped in and out.
 * </p>
 *
 * <p>
 * Typically each Segment has exactly one corresponding
 * VirtualMemory.
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
public interface VirtualMemory
    extends Memory
{
    // Currently there is nothing special about VirtualMemory
    // except the interface name.  (Could change in future!)
}
