package musaico.kernel.memory.paging.pagetables;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Filter;
import musaico.io.FilterState;

import musaico.kernel.memory.paging.Page;
import musaico.kernel.memory.paging.PageTable;
import musaico.kernel.memory.paging.PageTableException;
import musaico.kernel.memory.paging.SwapState;

import musaico.region.Position;


/**
 * <p>
 * Filters out all portions of a PageTable's region which
 * do not contain clean/dirty pages in the specified swap states.
 * </p>
 *
 * <p>
 * The PageTable must be locked before using this Filter,
 * so this should only be used from within the PageTable
 * class itself.
 * </p>
 *
 *
 * <p>
 * In Java, every Filter must be Serializable in order to play
 * nicely over RMI.
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
 * Copyright (c) 2011, 2012 Johann Tienhaara
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
public class CleanDirtyPageFilter
    implements Filter<Position>, Serializable
{
    /** The page table whose clean/dirty pages will be filtered. */
    private final PageTable pageTable;

    /** Whether to keep dirty (true) or clean (false) pages. */
    private final boolean isDirty;

    /** The swap states to look at (omit pages not in these
     *  swap states). */
    private final Set<SwapState> swapStates;


    /**
     * <p>
     * Creates a new CleanDirtyPageFilter for the specified
     * PageTable.
     * </p>
     *
     * @param page_table The PageTable whose pages will be
     *                   filtered.  Must not be null.
     *                   Must be locked.
     *
     * @param dirty_flag Whether to keep dirty pages (true)
     *                   or clean pages (false).
     *
     * @param swap_states One or more swap states.  All Pages
     *                    in any of these SwapStates which are
     *                    currently (clean/dirty) will be
     *                    included in the returned Region.
     *                    Must not be null.  Must not have any
     *                    null elements.  Must have at least one element.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     */
    public CleanDirtyPageFilter (
                                 PageTable page_table,
                                 boolean dirty_flag,
                                 SwapState... swap_states
                                 )
    {
        if ( page_table == null
             || swap_states == null
             || swap_states.length < 1 )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a CleanDirtyPageFilter with page_table [%page_table%] dirty_flag [%dirty_flag%] swap_states [%swap_states%]",
                                                     "page_table", page_table,
                                                     "dirty_flag", dirty_flag,
                                                     "swap_states", swap_states );
        }

        this.pageTable = page_table;
        this.isDirty = dirty_flag;

        // Turn the swap states into a set so we can do quicker
        // lookups.
        this.swapStates = new HashSet<SwapState> ();
        for ( int ss = 0; ss < swap_states.length; ss ++ )
        {
            this.swapStates.add ( swap_states [ ss ] );
        }
    }


    /**
     * @see musaico.io.Filter#filter(musaico.region.Position)
     */
    public FilterState filter (
                               Position position
                               )
    {
        final Page page;
        try
        {
            page = this.pageTable.page ( position );
        }
        catch ( PageTableException e )
        {
            // Shouldn't happen if the page table was locked!
            // As of right now though there's not much we
            // can do but just ignore this missing page...
            return FilterState.DISCARD;
        }

        if ( ! this.swapStates.contains ( page.swapState () ) )
        {
            return FilterState.DISCARD;
        }

        if ( page.kernelPaging ().isDirty ( page ) == this.isDirty )
        {
            return FilterState.KEEP;
        }

        return FilterState.DISCARD;
    }
}
