package musaico.kernel.memory.paging;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;
import musaico.io.Sequence;

import musaico.region.Position;

import musaico.security.Credentials;


/**
 * <p>
 * Contains all of the Swappers and SwapStates defining
 * the way various PagedAreas in virtual memory swap in
 * and out, and where they swap into and out from.
 * </p>
 *
 * <p>
 * For example, one SwapSystem might simply provide only
 * physical memory, offering no Swappers to other swap
 * states.
 * </p>
 *
 * <p>
 * Another SwapSystem might provide two swap states,
 * "swapped out to disk" and "swapped into memory",
 * akin to an operating system:
 * </p>
 *
 * <pre>
 *     +------------------------------------------------------+
 *     |  SwapState: swapped out to the filesystem.           |
 *     +------------------------------------------------------+
 *                                 ^
 *                                 | Swapper
 *                                 v
 *     +------------------------------------------------------+
 *     |  SwapState: swapped in to physical Buffer memory.    |
 *     +------------------------------------------------------+
 * </pre>
 *
 * <p>
 * Another SwapSystem might provide multiple states of
 * swapping:
 * </p>
 *
 * <pre>
 *     +------------------------------------------------------+
 *     |  SwapState: swapped way out to a remote web server.  |
 *     +------------------------------------------------------+
 *                                 ^
 *                                 | Swapper
 *                                 v
 *     +------------------------------------------------------+
 *     |  SwapState: swapped out to a local database server.  |
 *     +------------------------------------------------------+
 *                                 ^
 *                                 | Swapper
 *                                 v
 *     +------------------------------------------------------+
 *     |  SwapState: swapped in to physical Buffer memory.    |
 *     +------------------------------------------------------+
 *                                 ^
 *                                 | Swapper
 *                                 v
 *     +------------------------------------------------------+
 *     |  SwapState: swapped way in to fast cache.            |
 *     +------------------------------------------------------+
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
public interface SwapSystem
    extends Serializable
{
    /**
     * <p>
     * Returns all of the pages that must be swapped in and/or out
     * to get the Page(s) at the specified Position into the
     * specified SwapState.
     * </p>
     *
     * <p>
     * For example, to swap in a Field at a specific position
     * in virtual memory:
     * </p>
     *
     * <pre>
     *     PagedArea paged_area = ...;
     *     SwapSystem swap_system = paged_area.swapSystem ();
     *     Position position = ...;
     *     SwapState swapped_in = swap_system.swappedInToFields ();
     *     SwapOperation swap =
     *         swap_system.createSwapOperation ( paged_area, position, swapped_in );
     *     swap.swap ();
     * </pre>
     *
     * <p>
     * The target swap state may be several swap states in or out
     * from the current swap state.
     * </p>
     *
     * @param credentials The credentials to use to create the
     *                    pages necessary for swapping.
     *                    Typically a temporary copy of a kernel
     *                    Module's credentials.  Must not be null.
     *
     * @param paged_area The paged area of virtual memory, part of
     *                   which is to be swapped to a certain state.
     *                   For example a memory-mapped object based on
     *                   fields from a database.  The paged area also
     *                   provides swap state configuration, such as
     *                   the specific block driver for a
     *                   swapped-out-to-block-driver swap state.
     *                   Must not be null.
     *
     * @param position The position to swap.  Must not be null.
     *
     * @param swap_state The swap state to transition the Page(s)
     *                   surrounding the specified Position to.
     *                   Must not be null.  Must be a SwapState
     *                   in this SwapSystem.
     *
     * @return A new SwapOperation containing 0 or more SwapSteps,
     *         each containing a pair of Page
     *         regions which must be swapped in/out in order for
     *         the specified position to end up in the specified
     *         swap state.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     *
     * @throws SwapException If the specified Credentials are not
     *                       authorized to create the pages necessary
     *                       to create a swap operation, or if some
     *                       low-level exception occurs (I/O error and
     *                       so on).
     */
    public abstract SwapOperation createSwapOperation (
                                                       Credentials credentials,
                                                       PagedArea paged_area,
                                                       Position position,
                                                       SwapState swap_state
                                                       )
        throws I18nIllegalArgumentException,
               SwapException;


    /**
     * <p>
     * Returns the next (in) SwapState from the specified (out)
     * SwapState.  Throws an exception If the specified (out)
     * state is as swapped-in as can be.
     * </p>
     *
     * <p>
     * Equivalent to calling
     * <code> swapStates ().after ( out_swap_state ) </code>.
     * </p>
     *
     * @param out_swap_state The swap state to swap in from.
     *                       Must not be null.  Must be a valid
     *                       SwapState in this list of swap states.
     *
     * @return The corresponding in swap state.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract SwapState inFrom (
                                      SwapState out_swap_state
                                      )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Returns all the SwapStates starting from swappedInToFields ()
     * through to the most-swapped-in swap state (possibly only one
     * swap state).
     * </p>
     *
     * @return All of the SwapStates which are swapped-in.
     *         Never null.  Never contains any null elements.
     *         Always at least one SwapState (swappedInToFields()).
     */
    public abstract SwapState [] inSwapStates ();


    /**
     * <p>
     * Returns true if the specified SwapState can be swapped in to
     * another, more-swapped-in SwapState.
     * </p>
     *
     * @param out_state The SwapState to check.  Must not be null.
     *
     * @return True if the specified SwapState can be swapped in,
     *         false if it is either already the most-swapped-in
     *         SwapState or not a swap state at all in this system.
     */
    public abstract boolean isSwapInable (
                                          SwapState out_state
                                          );


    /**
     * <p>
     * Returns true if the specified SwapState can be swapped out to
     * another, more-swapped-out SwapState.
     * </p>
     *
     * @param in_state The SwapState to check.  Must not be null.
     *
     * @return True if the specified SwapState can be swapped out,
     *         false if it is either already the most-swapped-out
     *         SwapState or not a swap state at all in this system.
     */
    public abstract boolean isSwapOutable (
                                           SwapState in_state
                                           );


    /**
     * <p>
     * Returns the next (out) SwapState from the specified (in)
     * SwapState.  Throws an exception If the specified (in)
     * state is as swapped-out as can be.
     * </p>
     *
     * <p>
     * Equivalent to calling
     * <code> swapStates ().before ( in_swap_state ) </code>.
     * </p>
     *
     * @param in_swap_state The swap state to swap out from.
     *                      Must not be null.  Must be a valid
     *                      SwapState in this list of swap states.
     *
     * @return The corresponding out swap state.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above for details).
     */
    public abstract SwapState outFrom (
                                       SwapState in_swap_state
                                       )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Returns all the SwapStates swapped out from swappedInToFields ()
     * (not including swappedInToFields () iself)
     * through to the most-swapped-out swap state (if any).
     * </p>
     *
     * @return All of the SwapStates which are swapped-out.
     *         Never null.  Never contains any null elements.
     *         Can be 0-length.
     */
    public abstract SwapState [] outSwapStates ();


    /**
     * <p>
     * Returns this PagedArea's swapping functionality.
     * </p>
     *
     * <p>
     * Segments which belong to this PagedArea use
     * Swappers to swap in and out their swappedPages.
     * </p>
     *
     * <p>
     * Other classes looking to induce Page swapping should
     * use the page synchronization mechanisms.
     * </p>
     *
     * <p>
     * When the kernel is asked to flush a Page(s), it will
     * swap them out using the appropriate swapper.  When free memory is
     * running scarce, it can swap out dirty pages then free up
     * and reuse clean pages to address the constraints.
     * When reading in one or more Pages, the appropriate swapper is used
     * to swap in from some external Page source(s).  And
     * so on.
     * </p>
     *
     * @param out_swap_state The "out" state of virtual memory pages,
     *                       Must not be null.
     *                       Must be a valid SwapState within this
     *                       PagedArea's swapStates ().
     *
     * @param in_swap_state The "in" state of virtual memory pages.
     *                      Must not be null.  Must be a valid
     *                      SwapState within this PagedArea's swapStates ().
     *                      Must be equal to
     *                      <code> swapStates ().after ( out_swap_state ) </code>.
     *
     * @return The Swapper used to swap in and out between the specified
     *         swap states.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract Swapper swapper (
                                     SwapState out_swap_state,
                                     SwapState in_swap_state
                                     )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Returns the SwapState which represents in-memory Buffers
     * of Fields (such as a BufferSwapState, or some completely
     * other swap state which creates pages of physical memory
     * Buffers).
     * </p>
     *
     * <p>
     * Every Page in the swapped-in-to-fields SwapState
     * MUST be a <code> BufferPage </code>.
     * </p>
     *
     * @see musaico.kernel.memory.paging.buffer.BufferPage
     *
     * @return This swap system's "in-memory buffer of fields"
     *         swap state.  Every swap system has one.
     *         Never null.
     */
    public abstract SwapState swappedInToFields ();


    /**
     * <p>
     * Returns the ordered sequence of SwapStates for this PagedArea.
     * </p>
     *
     * <p>
     * This method should be used to find
     * the various swap states supported by this SwapSystem.
     * </p>
     *
     * <p>
     * The SwapStates is always ordered from most-swapped-out
     * to most-swapped-in.
     * </p>
     *
     * @return The ordered sequence of SwapStates for this SwapSystem.
     *         Never null.
     */
    public abstract Sequence<SwapState> swapStates ();
}
