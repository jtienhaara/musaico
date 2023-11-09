package musaico.kernel.common.swappers.block;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;
import musaico.io.Sequence;

import musaico.kernel.memory.paging.SwapState;
import musaico.kernel.memory.paging.SwapSystem;

import musaico.kernel.memory.paging.buffer.BufferSwapState;

import musaico.kernel.memory.paging.swap.StandardSwapSystem;


/**
 * <p>
 * 2 swap levels: swapped out to a BlockDriver, and
 * swapped in to an in-memory Buffer of Fields.
 * </p>
 *
 * <p>
 * This is the simplest swap system for most kernels to rely on.
 * Typically a kernel will use this, or a swap system built up from
 * the same 2 core swap states.
 * </p>
 *
 *
 * <p>
 * In Java every SwapSystem must be Serializable in order
 * to play nicely over RMI.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public class BufferBlockSwapSystem
    extends StandardSwapSystem
    implements Serializable
{
    /**
     * <p>
     * Creates a new BufferBlockSwapSystem relying on the specified
     * BufferBlockSwapper to swap between block storage and
     * in-memory buffer fields.
     * </p>
     *
     * <p>
     * The two SwapStates of this system (swapped out to a block
     * driver and swapped in to an in-memory Buffer of Fields) are
     * determined by the specified Swapper.
     * </p>
     *
     * <p>
     * For example, one BufferBlockSwapSystem might be created with
     * a swapper to swap between files and memory; another might
     * swap between a database server and memory; another might swap
     * between a webserver and memory; and so on.
     * </p>
     *
     * @param swapper The swapper which reads data from and writes
     *                data to a block driver, and swaps it into / out
     *                from in-memory Buffers of Fields.
     *                Must not be null.  Must swap between a
     *                BlockSwapState (out) and a BufferSwapState (in).
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public BufferBlockSwapSystem (
                                  BufferBlockSwapper swapper
                                  )
    {
        super ( swapper.outState (),
                swapper.inState (),
                swapper );

        if ( swapper == null
             || ! ( swapper.outState () instanceof BlockSwapState )
             || ! ( swapper.inState () instanceof BufferSwapState ) )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a BufferBlockSwapSystem with swapper [%swapper%]",
                                                     "swapper", swapper );
        }
    }
}
