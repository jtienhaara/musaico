package musaico.kernel.memory.paging.kernelpagings;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.io.lrus.SimpleLRU;

import musaico.kernel.memory.paging.KernelPaging;
import musaico.kernel.memory.paging.Page;


/**
 * <p>
 * A simple paging manager, to manage the page LRU, dirty
 * and clean lists, and so on.
 * </p>
 *
 * <p>
 * Note that the class SimpleLRU does the LRU work for us
 * (by extension).
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
public class SimpleKernelPaging
    extends SimpleLRU<Page>
    implements KernelPaging, Serializable
{
    /** Synchronize critical sections on this token: */
    private final Serializable lock = new String ();

    /** Clean pages (don't need synchronizing). */
    private final List<Page> cleanPages = new ArrayList<Page> ();

    /** Dirty pages (need to be synchronized with their backing stores). */
    private final List<Page> dirtyPages = new ArrayList<Page> ();


    /**
     * @see musaico.kernel.memory.paging.KernelPaging#clean(Page)
     */
    public boolean clean (
                          Page clean_page
                          )
    {
        final boolean was_dirty;
        synchronized ( this.lock )
        {
            if ( this.dirtyPages.remove ( clean_page ) )
            {
                was_dirty = true;
            }
            else
            {
                was_dirty = false;
            }

            this.cleanPages.remove ( clean_page );
            this.cleanPages.add ( clean_page );
        }

        return was_dirty;
    }


    /**
     * @see musaico.kernel.memory.paging.KernelPaging#dirty(Page)
     */
    public boolean dirty (
                          Page dirty_page
                          )
    {
        final boolean was_clean;
        synchronized ( this.lock )
        {
            if ( this.cleanPages.remove ( dirty_page ) )
            {
                was_clean = true;
            }
            else
            {
                was_clean = false;
            }

            this.dirtyPages.remove ( dirty_page );
            this.dirtyPages.add ( dirty_page );
        }

        return was_clean;
    }


    /**
     * @see musaico.kernel.memory.paging.KernelPaging#isDirty(Page)
     */
    public boolean isDirty (
                            Page page
                            )
    {
        synchronized ( this.lock )
        {
            return this.dirtyPages.contains ( page );
        }
    }


    /**
     * @override musaico.io.lrus.SimpleLRU#remove(ITEM))
     */
    public Page remove (
                        Page page
                        )
    {
        synchronized ( this.lock )
        {
            this.cleanPages.remove ( page );
            this.dirtyPages.remove ( page );
            return super.remove ( page );
        }
    }
}
