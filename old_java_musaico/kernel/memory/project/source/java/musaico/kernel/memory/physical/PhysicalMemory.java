package musaico.kernel.memory.physical;


import musaico.kernel.memory.Memory;


/**
 * <p>
 * A memory manager for raw physical memory.  Buffers
 * allocated by this Memory are actual in-memory Buffers,
 * not virtual memory and so on.
 * </p>
 *
 * <p>
 * Typically there is one main physical memory manager
 * per kernel, though there may be other proxy physical
 * memory managers which provide security in addition to
 * proxied allocation and freeing through the main PhysicalMemory.
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
 * Copyright (c) 2010 Johann Tienhaara
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
public interface PhysicalMemory
    extends Memory
{
    // Currently there is nothing special about PhysicalMemory
    // except the interface name.  (Could change in future!)
}
