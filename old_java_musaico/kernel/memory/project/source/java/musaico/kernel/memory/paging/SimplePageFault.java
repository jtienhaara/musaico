package musaico.kernel.memory.paging;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.I18nIllegalStateException;

import musaico.region.Position;


/**
 * <p>
 * Represents a no-nonsense page fault in the virtual memory layer.
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
public class SimplePageFault
    implements PageFault, Serializable
{
    /** Sycnrhonize all critical sections on this lock: */
    private final Serializable lock = new String ();

    /** The identifier of the Page which caused the fault. */
    private final Position pageID;

    /** The state to swap the specified page into, such as
     *  SwapStates.SWAPPED_IN_TO_FIELDS or
     * SwapStates.SWAPPED_IN_TO_FAST_CACHE. */
    private final SwapState targetSwapState;

    /** The Page which caused the fault.  Set by the fault handler. */
    private Page page;

    /** This page fault's flags (out of memory, no such page, and so on).
     *  Set by the fault handler. */
    private int flags;

    /** True once the page fault handler has set the Page to
     *  which this fault refers. */
    private boolean isPageSet;


    /**
     * <p>
     * Creates a new SimplePageFault with the specified page ID.
     * </p>
     *
     * @param page_id The reference to the Page which faulted.
     *                Must not be null.
     *
     * @param target_swap_state The state to swap the specified
     *                          Page into, such as
     *                          SwapStates.SWAPPED_IN_TO_FIELDS
     *                          or SwapStates.SWAPPED_IN_TO_FAST_CACHE,
     *                          and so on.  Must not be null.
     *
     * @see musaico.kernel.memory.paging.PageFault
     */
    public SimplePageFault (
                            Position page_id,
                            SwapState target_swap_state
                            )
    {
        if ( page_id == null
             || target_swap_state == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimplePageFault with page id [%page_id%] target swap state [%target_swap_state%]",
                                                     "page_id", page_id,
                                                     "target_swap_state", target_swap_state );
        }

        this.pageID = page_id;
        this.targetSwapState = target_swap_state;
        this.isPageSet = false;
    }


    /**
     * @see musaico.kernel.memory.paging.PageFault#flags()
     */
    @Override
    public int flags ()
    {
        return this.flags;
    }


    /**
     * @see musaico.kernel.memory.paging.PageFault#pageRef()
     */
    @Override
    public Position pageRef ()
    {
        return this.pageID;
    }


    /**
     * @see musaico.kernel.memory.paging.PageFault#page()
     */
    @Override
    public Page page ()
    {
        return this.page;
    }


    /**
     * @see musaico.kernel.memory.paging.PageFault#page(Page,int)
     */
    @Override
    public void page (
                      Page page,
                      int flags
                      )
    {
        if ( page == null
             || flags < 0 )
        {
            throw new I18nIllegalArgumentException ( "Cannot set page to [%page%] with flags [%flags%]",
                                                     "page", page,
                                                     "flags", flags );
        }

        synchronized ( this.lock )
        {
            if ( this.isPageSet == true )
            {
                throw new I18nIllegalStateException ( "Cannot set the page for a PageFault twice: old page [%old_page%] new page [%new_page%]",
                                                      "old_page", this.page,
                                                      "new_page", page );
            }

            this.page = page;
            this.flags = flags;

            this.isPageSet = true;
        }
    }


    /**
     * @see musaico.kernel.memory.paging.PageFault#swapStateTarget()
     */
    @Override
    public SwapState swapStateTarget ()
    {
        return this.targetSwapState;
    }
}
