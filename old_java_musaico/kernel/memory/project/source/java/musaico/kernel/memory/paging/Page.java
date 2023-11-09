package musaico.kernel.memory.paging;

import java.io.Serializable;


import musaico.io.ReferenceCount;

import musaico.kernel.memory.MemoryException;

import musaico.region.Position;
import musaico.region.Region;


/**
 * <p>
 * A page of virtual memory.
 * </p>
 *
 * <p>
 * A Page may reference an in-memory Buffer of Fields (or
 * possibly even an Object).  Or it may be a swapped-out
 * Page, referencing objects in persistent storage, with
 * a "page fault" mechanism to read the Page into memory.
 * And so on.
 * </p>
 *
 * @see musaico.kernel.memory.paging.pages
 *
 * <p>
 * In Java, all Pages must be Serializable.  Note, however,
 * that a Page may contain non-Serializable data.
 * (For example, a buffer Page with non-Serializable Fields.)
 * Therefore great care must be taken when passing Pages
 * back and forth across RMI.
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
public interface Page
    extends Serializable
{
    /**
     * <p>
     * Releases the resources for this Page (such as Buffer memory,
     * ONode references, and so on).
     * </p>
     *
     * <p>
     * Once called, this Page cannot be used any longer.
     * </p>
     */
    public abstract void free ()
        throws MemoryException;


    /**
     * <p>
     * Returns the paging manager which controls the LRU,
     * dirty list, and so on.
     * </p>
     *
     * <p>
     * Typically there is only one KernelPaging per kernel.
     * </p>
     *
     * @return The paging manager which controls this Page
     *         of memory.  Never null.
     */
    public abstract KernelPaging kernelPaging ();


    /**
     * <p>
     * Returns the count of objects referring to this Page.
     * </p>
     *
     * <p>
     * Note that beyond the multiple kernel objects which might be
     * used by a single Process and refer to this Page, there
     * might also be multiple Processes referring to this Page.
     * For example, read-only Pages should always be shared
     * between Processes which require read access to the same
     * objects.  Even for writable objects, a "copy-on-write"
     * mechanism might be used to minimize time and memory usage
     * until the point where a given Process does begin writing
     * to the Page.
     * </p>
     *
     * @return This Page's ReferenceCount.  Never null.
     */
    public abstract ReferenceCount referenceCount ();


    /**
     * <p>
     * Returns the Region describing this Page, including the
     * first and last Field positions, the Size of the Page,
     * and so on.
     * </p>
     *
     * @return The Region describing this Page.  Never null.
     */
    public abstract Region region ();


    /**
     * <p>
     * Returns the SwapState in which this Page resides, such
     * as <code> SwapStates.SWAPPED_IN_TO_FIELDS </code>, or
     * <code> SwapStates.SWAPPED_OUT_TO_PERSISTENT_STORAGE </code>,
     * and so on.
     * </p>
     *
     * </p>
     * The SwapState of a Page should generally never change with time
     * (unless it is some kind of virtual or proxy page, or other
     * umbrella for multiple pages, but these do not exist at the
     * time of writing).
     * </p>
     *
     * @return This page's swap state.  Never null.
     */
    public abstract SwapState swapState ();
}
