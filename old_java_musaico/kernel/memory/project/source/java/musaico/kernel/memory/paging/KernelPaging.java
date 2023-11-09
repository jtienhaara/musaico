package musaico.kernel.memory.paging;


import java.io.Serializable;


import musaico.io.LRU;
import musaico.io.SimpleTypedIdentifier;
import musaico.io.TypedIdentifier;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.KernelNamespaces;

import musaico.kernel.memory.paging.Page;
import musaico.kernel.memory.paging.SwapException;
import musaico.kernel.memory.paging.Swapper;


/**
 * <p>
 * Manages the least-recently used list of pages, dirty and clean
 * pages, and so on.
 * </p>
 *
 * <p>
 * Typically there is one KernelPaging for the whole kernel, though this
 * need not be the case.  Usually only the individual Pages and
 * the PagedArea need to use the KernelPaging structure.
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
public interface KernelPaging
    extends LRU<Page>, Serializable
{
    /** The Identifier by which the single KernelPaging LRU can be
     *  retrieved from the kernel / from a module
     *  (using getKernelObject()). */
    public static final TypedIdentifier<KernelPaging> KERNEL_OBJECT_ID =
        new SimpleTypedIdentifier<KernelPaging> ( KernelNamespaces.MEMORY_LAYER,
                                                  new SimpleSoftReference<String> ( "kernel_paging" ),
                                                  KernelPaging.class );


    // Every KernelPaging must implement the PageLRU methods:
    //
    //     + void recent ( Page )
    //     + Page leastRecent ()
    //     + remove ( Page ) -> must also remove page from clean, dirty lists


    /**
     * <p>
     * Places the specified Page on the clean list, so
     * that it will not be synchronized with its backing
     * store.
     * </p>
     *
     * @param clean_page The page to place on the clean list.
     *                   Must not be null.
     *
     * @return True if the page was dirty; false if it was
     *         either already clean or unknown to this KernelPaging.
     */
    public abstract boolean clean (
                                   Page clean_page
                                   );


    /**
     * <p>
     * Places the specified Page on the dirty list, so
     * that it will be synchronized with its backing
     * store at the next opportunity.
     * </p>
     *
     * @param dirty_page The page to place on the dirty list.
     *                   Must not be null.
     *
     * @return True if the page was clean; false if it was
     *         either already dirty or unknown to this KernelPaging.
     */
    public abstract boolean dirty (
                                   Page dirty_page
                                   );


    /**
     * <p>
     * Returns true if the specified Page is in the dirty list.
     * </p>
     *
     * @param page The page to query.  Must not be null.
     *
     * @return True if the specified Page is in this paging
     *         manager's dirty list; false otherwise.
     */
    public abstract boolean isDirty (
                                     Page page
                                     );
}
