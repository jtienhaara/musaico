package musaico.kernel.memory.paging;

import java.io.Serializable;


import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;

import musaico.region.Position;
import musaico.region.Size;
import musaico.region.Space;

import musaico.security.Credentials;


/**
 * <p>
 * One layer of a SwapSystem, describing the page size,
 * page factory, and so on for one swap state.
 * </p>
 *
 *
 * <p>
 * In Java every SwapState must be Serializable in order to
 * play nicely over RMI.
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
public interface SwapState
    extends Serializable
{
    /**
     * <p>
     * Creates a new Page starting at the specified field #.
     * </p>
     *
     * <p>
     * This method does NOT put the created page in any paged area's
     * PageTable, nor in the kernel's least-recently-used page list,
     * and so on.  This is strictly a factory-style method.
     * </p>
     *
     * <p>
     * The page(s) returned are NOT populated with data!
     * </p>
     *
     * <p>
     * The page(s) returned may be used, for example, to call
     * <code> swapper ( ... ).readIn ( out_page, ..., new_in_page, ... ) </code>
     * or
     * <code> swapper ( ... ).writeOut ( in_page, ..., new_out_page, ... ) </code>
     * and so on.
     * </p>
     *
     * <p>
     * If a request is made to create a page for a position
     * which does NOT lie on the boundaries of this SwapState,
     * or which has an invalid start position (in a different Space,
     * for example), then an illegal argument exception is thrown.
     * </p>
     *
     * <p>
     * For example, if the page size for this swap state is
     * <code> ArraySize ( 1024 ) </code> fields, and a request
     * is made to create a page at <code> ArrayPosition ( 1536 ) </code>,
     * then an exception is thrown.  Only pages of
     * <code> ArraySize ( 1024 ) </code> fields starting at
     * <code> ArrayPosition ( 0 ) </code>,
     * <code> ArrayPosition ( 1024 ) </code>,
     * <code> ArrayPosition ( 2048 ) </code> and so on would
     * be allowed for such a SwapState.
     * </p>
     *
     * @param credentials The credentials used to grant or deny
     *                    permission to allocate storage space
     *                    (physical memory, file or database
     *                    storage through a driver, and so on).
     *                    For example, the module credentials
     *                    for a module creating pages of memory,
     *                    or user credentials for user requests,
     *                    and so on.  Must not be null.
     *
     * @param start_position The start of the Page to create.
     *                       Must not be null.  Must be on the page
     *                       boundary for this swap state.
     *
     * @param swap_state_configuration The optional configuration
     *                                 Field for this swap state.
     *                                 For example, a swapped-out-to-block-
     *                                 driver swap state might expect
     *                                 the block driver to be passed in.
     *                                 Can be Field.NULL.  Must not be null.
     *
     * @return The newly created Page.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     *
     * @throws SwapException If the page cannot be created because
     *                       of some lower-level problem (out
     *                       of memory, driver error, and so on)
     *                       or the specified credentials are not
     *                       authorized to create a Page in this
     *                       SwapState.
     */
    public abstract Page createPage (
                                     Credentials credentials,
                                     Position start_position,
				     Field swap_state_configuration
                                     )
        throws I18nIllegalArgumentException,
               SwapException;


    /** Every SwapState must override equals(java.lang.Object). */

    /** Every SwapState must override hashCode(). */


    /**
     * <p>
     * Returns this SwapState's unique identifier.
     * </p>
     *
     * @return This SwapState's identifier, unique within its
     *         parent SwapSystem.  Never null.
     */
    public abstract Reference id ();


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
     * <p>
     * The SwapState only ever uses this to tell each
     * Page it creates about the KernelPaging, so that the
     * Page can tell the kernel about its clean/dirty status,
     * change its location on the kernel's page LRU, and so on.
     * </p>
     *
     * @return The paging manager which controls the Pages
     *         of memory created by this SwapState.
     *         Never null.
     */
    public abstract KernelPaging kernelPaging ();


    /**
     * <p>
     * Returns the page size, in terms of Fields, of every Page
     * at this swap state.
     * </p>
     *
     * <p>
     * The page size can vary between SwapStates in a single
     * SwapSystem, as long as every SwapState has a page size
     * that amounts to a Field count which is divisible
     * by, or divisible into, every other SwapState in the
     * same SwapSystem.
     * </p>
     *
     * <p>
     * For example, a SwapState with a page size amounting
     * to 16 fields per page can be used in the same
     * SwapSystem as a SwapState with a page size amounting
     * to 256 fields per page.  However a SwapState with
     * 13 fields per page and a SwapState with 19 fields
     * per page cannot live together in the same SwapSystem.
     * </p>
     *
     * @return This swap state's page size.  Never null.
     */
    public abstract Size pageSize ();


    /**
     * <p>
     * Returns the space of this swap state, describing the
     * positions, regions and sizes of fields within pages.
     * </p>
     *
     * <p>
     * Note that two different SwapStates in the same SwapSystem
     * might operate on completely different coordinate spaces.
     * The total number of Fields mapped between such swap states
     * will remain constant, even though the positions, regions and
     * sizes used to describe them and look up values in drivers
     * or buffers varies depending on the needs of the swap state.
     * </p>
     *
     * @return The Space to which the positions, regions and sizes
     *         of this swap state belong.  Never null.
     */
    public abstract Space space ();
}
