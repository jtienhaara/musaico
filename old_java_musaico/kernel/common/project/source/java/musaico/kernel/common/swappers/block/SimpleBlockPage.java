package musaico.kernel.common.swappers.block;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.ReferenceCount;
import musaico.io.SimpleReferenceCount;

import musaico.kernel.driver.BlockDriver;

import musaico.kernel.memory.MemoryException;

import musaico.kernel.memory.paging.KernelPaging;
import musaico.kernel.memory.paging.SwapState;

import musaico.region.Region;


/**
 * <p>
 * A straight-forward implementation of the BlockPage
 * page facade over a block driver.
 * </p>
 *
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
 * Copyright (c) 2010, 2012 Johann Tienhaara
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
public class SimpleBlockPage
    implements BlockPage, Serializable
{
    /** The KernelPaging manager for this Page, including LRU, clean
     *  and dirty lists, and so on.  Typically shared across all
     *  pages in all paged areas in the kernel. */
    private final KernelPaging kernelPaging;

    /** The count of references to this Page. */
    private final ReferenceCount referenceCount =
        new SimpleReferenceCount ();

    /** The swap state of this page (such as
     *  SwapStates.SWAPPED_OUT_TO_PERSISTENT_STORAGE). */
    private final SwapState swapState;

    /** The block driver. */
    private final BlockDriver blockDriver;

    /** The Region of data in the BlockDriver to read from / write to. */
    private final Region region;


    /**
     * <p>
     * Creates a new SimpleBlockPage with the specified BlockDriver
     * and SwapState.
     * </p>
     *
     * @param kernel_paging The KernelPaging manager for this Page, providing
     *                      the least-recently-used (LRU) list, clean
     *                      and dirty page lists, and so on.
     *                      Must not be null.  Typically only one
     *                      KernelPaging exists across the entire
     *                      kernel (though this need not be the case).
     *
     * @param swap_state The swap state of this page, such as
     *                   <code> SWAPPED_OUT_TO_PERISTENT_STORAGE </code>.
     *                   Must not be null.
     *
     * @param block_driver The block driver backing this page.
     *                     Must not be null.
     *
     * @param region The region of the block driver data that this
     *               page covers (for example, fields 32-63).
     *               Must not be null.
     */
    public SimpleBlockPage (
                            KernelPaging kernel_paging,
                            SwapState swap_state,
                            BlockDriver block_driver,
                            Region region
                            )
    {
        if ( kernel_paging == null
             || swap_state == null
             || block_driver == null
             || region == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimplePage with KernelPaging [%kernel_paging%] swap state [%swap_state%] BlockDriver [%block_driver%] region [%region%]",
                                                     "kernel_paging", kernel_paging,
                                                     "swap_state", swap_state,
                                                     "block_driver", block_driver,
                                                     "region", region );
        }

        this.kernelPaging = kernel_paging;
        this.swapState = swap_state;
        this.blockDriver = block_driver;
        this.region = region;
    }


    /**
     * @see musaico.swappers.pages.BlockPage#blockDriver()
     */
    public BlockDriver blockDriver ()
    {
        return this.blockDriver;
    }


    /**
     * @see musaico.kernel.memory.paging.Page#free()
     */
    public final void free ()
        throws MemoryException
    {
        this.kernelPaging ().remove ( this );
    }


    /**
     * @see musaico.kernel.memory.paging.Page#kernelPaging()
     */
    public final KernelPaging kernelPaging ()
    {
        return this.kernelPaging;
    }


    /**
     * @see musaico.kernel.memory.paging.Page#referenceCount()
     */
    public final ReferenceCount referenceCount ()
    {
        return this.referenceCount;
    }


    /**
     * @see musaico.kernel.memory.paging.Page#region()
     */
    public final Region region ()
    {
        return this.region;
    }


    /**
     * @see musaico.kernel.memory.paging.Page#swapState()
     */
    public final SwapState swapState ()
    {
        return this.swapState;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "BlockPage(" + this.blockDriver.id () + ")"
            + " region " + this.region ();
    }
}
