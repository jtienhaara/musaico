package musaico.kernel.memory.paging;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.region.Position;
import musaico.region.Region;

import musaico.security.Credentials;


/**
 * <p>
 * Performs swap-ins and swap-outs, writing memory out
 * to persistent storage, reading in pages from persistent
 * storage, and so on.
 * </p>
 *
 * <p>
 * Suppose the kernel's in-memory page size is 4096 fields,
 * and backing driver X has a block / sector size of 512
 * fields.  Then the swapper which swaps between in-memory
 * buffers and fields stored in the block driver will be
 * asked to create 1 "in page" for every 8 "out pages",
 * and conversely 8 "out pages" for every 1 "in page".
 * </p>
 *
 * <p>
 * Thus a caller might do something like the following
 * to read in all the sectors from the backing driver
 * into memory:
 * </p>
 *
 * <pre>
 *     SwapStep [] swaps = ...;
 *
 *     for ( SwapStep swap_step : swaps )
 *     {
 *         Swapper swapper = swap_step.swapper ();
 *         Page out_page = swap_step.outPage ();
 *         Region out_region = swap_step.outRegion ();
 *         Page in_page = swap_step.inPage ();
 *         Region in_region = swap_step.inRegion ();
 *
 *         swapper.readIn ( out_page,
 *                          out_region,
 *                          in_page,
 *                          in_region );
 *     }
 * </pre>
 *
 * <p>
 * Similarly, swapping out or synchronizing dirty data might
 * be done with something like:
 * </p>
 *
 * <pre>
 *     SwapStep [] swaps = ...;
 *
 *     for ( SwapStep swap_step : swaps )
 *     {
 *         Swapper swapper = ...;
 *         Page in_page = swap_step.inPage ();
 *         Region in_region = swap_step.inRegion ();
 *         Page out_page = swap_step.outPage ();
 *         Region out_region = swap_step.outRegion ();
 *
 *         swapper.writeOut ( in_page,
 *                            in_region,
 *                            out_page,
 *                            out_region );
 *     }
 * </pre>
 *
 *
 * <p>
 * In Java every Swapper must be Serializable in order
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
 * Copyright (c) 2009, 2010, 2011, 2012 Johann Tienhaara
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
public interface Swapper
    extends Serializable
{
    /**
     * <p>
     * Returns the "in" position corresponding to the
     * specified "out" position.
     * </p>
     *
     * <p>
     * Swappers can swap between swap states which use completely
     * different Position/Region/Size spaces, so this method
     * provides the mapping from one origin-based position to the
     * other.
     * </p>
     *
     * @param out_position The "out" position whose corresponding
     *                     "in" position will be returned.  Must
     *                     not be null.  Must be a valid Position
     *                     inside <code> outState ().space () </code>.
     *
     * @return The corresponding "in" position.  Never null.
     *         Never out of bounds for <code> inState ().space () </code>.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract Position inPosition (
                                         Position out_position
                                         )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Retuns the "in" SwapState for this Swapper.  When readIn ()
     * or "writeOut" is called, the "in" page must be a Page in
     * this state.
     * </p>
     *
     * @return The "in" SwapState for this Swapper.  Never null.
     */
    public abstract SwapState inState ();


    /**
     * <p>
     * Returns the "out" position corresponding to the
     * specified "in" position.
     * </p>
     *
     * <p>
     * Swappers can swap between swap states which use completely
     * different Position/Region/Size spaces, so this method
     * provides the mapping from one origin-based position to the
     * other.
     * </p>
     *
     * @param in_position The "in" position whose corresponding
     *                    "out" position will be returned.  Must
     *                    not be null.  Must be a valid Position
     *                    inside <code> inState ().space () </code>.
     *
     * @return The corresponding "out" position.  Never null.
     *         Never out of bounds for <code> outState ().space () </code>.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract Position outPosition (
                                          Position in_position
                                          )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Retuns the "out" SwapState for this Swapper.  When readIn ()
     * or writeOut () is called, the "out" page must be a Page in
     * this state.
     * </p>
     *
     * @return The "out" SwapState for this Swapper.  Never null.
     */
    public abstract SwapState outState ();


    /**
     * <p>
     * Given the specified "swapped-out" Page, reads in
     * the data into the "swapped-in" Page (creating or
     * overwriting buffers and other memory as necessary).
     * </p>
     *
     * <p>
     * Typically different classes of Page will be used for
     * the swapped-in and swapped-out Pages.  For
     * example, if the "swapped-out" Page is a StoredInDatabasePage,
     * then the "swapped-in" Page might be a BufferPage, and
     * this method might read in all the Fields from the database
     * into the swapped-in BufferPage.
     * </p>
     *
     * @param credentials The credentials to use for the read.
     *                    Typically a temporary copy of a kernel
     *                    Module's credentials.
     *                    Must not be null.
     *
     * @param out_page The swapped-out Page to read in.  Must not be null.
     *                 Must be an instance of a "swapped out" Page class for
     *                 this Swapper.
     *
     * @param out_region The region over which to read from the
     *                   swapped-out page.  Must not be null.
     *                   Must have the same number of Positions as
     *                   the in_region (even if the Positions are
     *                   not the same, such as an out region 0-8
     *                   and an in region 300-308).
     *
     * @param in_page The swapped-in Page to read into.  Must not be null.
     *                Must be an instance of a "swapped in" Page class for
     *                this Swapper.
     *
     * @param in_region The region over which to read from the
     *                  swapped-in page.  Must not be null.
     *                  Must have the same number of Positions as
     *                  the out_region (even if the Positions are
     *                  not the same, such as an in region 0-8
     *                  and an out region 300-308).
     *
     * @throws SwapException If the Page cannot be read in by
     *                       this Swapper.
     */
    public abstract void readIn (
                                 Credentials credentials,
                                 Page out_page,
                                 Region out_region,
                                 Page in_page,
                                 Region in_region
                                 )
        throws SwapException;


    /**
     * <p>
     * Given the specified "swapped-in" Page, writes the
     * data from the Page out to the specified "swapped-out" Page
     * (creating and/or overwriting Buffers and other data
     * structures as necessary in order to do so).
     * </p>
     *
     * <p>
     * Typically different classes of Pages will be used.  For
     * example, if the "swapped-in" Page is a BufferPage,
     * and the "swapped-out" Page is a StoredInDatabasePage,
     * then this method might write out all the Fields from
     * the BufferPage to persistent storage in the database.
     * </p>
     *
     * @param credentials The credentials to use for the write.
     *                    Typically a temporary copy of a kernel
     *                    Module's credentials.
     *                    Must not be null.
     *
     * @param in_page The swapped-in Page to write out  Must not be null.
     *                Must be an instance of a "swapped in" Page class
     *                for this Swapper.
     *
     * @param in_region The region over which to read from the
     *                  swapped-in page.  Must not be null.
     *                  Must have the same number of Positions as
     *                  the out_region (even if the Positions are
     *                  not the same, such as an in region 0-8
     *                  and an out region 300-308).
     *
     * @param out_page The swapped-out Page to write out to.  Must not be null.
     *                 Must be an instance of a "swapped out" Page class
     *                 for this Swapper.
     *
     * @param out_region The region over which to read from the
     *                   swapped-out page.  Must not be null.
     *                   Must have the same number of Positions as
     *                   the in_region (even if the Positions are
     *                   not the same, such as an out region 0-8
     *                   and an in region 300-308).
     *
     * @throws SwapException If the Page cannot be written out by
     *                       this Swapper.
     */
    public abstract void writeOut (
                                   Credentials credentials,
                                   Page in_page,
                                   Region in_region,
                                   Page out_page,
                                   Region out_region
                                   )
        throws SwapException;
}
