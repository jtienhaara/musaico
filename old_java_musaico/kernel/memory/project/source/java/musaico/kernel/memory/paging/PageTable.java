package musaico.kernel.memory.paging;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Space;
import musaico.region.SparseRegion;


/**
 * <p>
 * The table of pages for a single PagedArea.
 * </p>
 *
 * <p>
 * In Java, every PageTable must be Serializable, in order to
 * place nicely over RMI.  Note, however,
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
 * Copyright (c) 2011 Johann Tienhaara
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
public interface PageTable
    extends Serializable
{
    /**
     * <p>
     * Marks all the pages in this table in the specified
     * swap state(s) as clean.
     * </p>
     *
     * <p>
     * Each page will be marked clean by calling
     * <code>page.kernelPaging ().clean ( Page ) </code>.
     * </p>
     *
     * @param region Clean only pages in the specified swap states
     *               inside this region (possibly PageTable.region () ).
     *               Must not be null.
     *
     * @param swap_states One or more swap states to mark all
     *                    pages as clean.  For example,
     *                    <code> { SWAPPED_IN_TO_FIELDS,
     *                             SWAPPED_IN_TO_FAST_CACHE } </code>.
     *                    Must not be null.  Must not have any
     *                    null elements.  Must have at least one element.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws PageTableException If the region is out of the
     *                            bounds of this page table.
     */
    public abstract void cleanAll (
                                   Region region,
                                   SwapState... swap_states
                                   )
        throws I18nIllegalArgumentException,
               PageTableException;


    /**
     * <p>
     * Returns the Region of Pages in the specified swap
     * state(s) which are clean.
     * </p>
     *
     * <p>
     * For example, to return all Pages which are
     * <code> SWAPPED_IN_TO_FIELDS </code> or
     * <code> SWAPPED_IN_TO_FAST_CACHE </code> which are
     * currently clean:
     * </p>
     *
     * <pre>
     *     PageTable page_table = ...;
     *     Region clean_region =
     *         page_table.cleanRegion ( page_table.region (),
     *                                  SWAPPED_IN_TO_FIELDS,
     *                                  SWAPPED_IN_TO_FAST_CACHE );
     *     Page [] clean_pages = page_table.pages ( clean_region );
     * </pre>
     *
     * @param region Only consider pages in the specified swap states
     *               inside this region (possibly PageTable.region () ).
     *               Must not be null.
     *
     * @param swap_states One or more swap states.  All Pages
     *                    in any of these SwapStates which are
     *                    currently clean will be included in
     *                    the returned Region.
     *                    Must not be null.  Must not have any
     *                    null elements.  Must have at least one element.
     *
     * @return The Region of clean pages in the specified swap state(s).
     *         Never null.  Might be empty.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws PageTableException If the region is out of the
     *                            bounds of this page table.
     */
    public abstract Region cleanRegion (
                                        Region region,
                                        SwapState... swap_states
                                        )
        throws I18nIllegalArgumentException,
               PageTableException;


    /**
     * <p>
     * Marks all the pages in this table in the specified
     * swap state(s) as dirty.
     * </p>
     *
     * <p>
     * Each page will be marked dirty by calling
     * <code>page.kernelPaging ().dirty ( Page ) </code>.
     * </p>
     *
     * @param region Dirty only pages in the specified swap states
     *               inside this region (possibly PageTable.region () ).
     *               Must not be null.
     *
     * @param swap_states One or more swap states to mark all
     *                    pages as dirty.  For example,
     *                    <code> { SWAPPED_IN_TO_FIELDS,
     *                             SWAPPED_IN_TO_FAST_CACHE } </code>.
     *                    Must not be null.  Must not have any
     *                    null elements.  Must have at least one element.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws PageTableException If the region is out of the
     *                            bounds of this page table.
     */
    public abstract void dirtyAll (
                                   Region region,
                                   SwapState... swap_states
                                   )
        throws I18nIllegalArgumentException,
               PageTableException;


    /**
     * <p>
     * Returns the Region of Pages in the specified swap
     * state(s) which are dirty.
     * </p>
     *
     * <p>
     * For example, to return all Pages which are
     * <code> SWAPPED_IN_TO_FIELDS </code> or
     * <code> SWAPPED_IN_TO_FAST_CACHE </code> which are
     * currently dirty:
     * </p>
     *
     * <pre>
     *     PageTable page_table = ...;
     *     Region dirty_region =
     *         page_table.dirtyRegion ( page_table.region (),
     *                                  SWAPPED_IN_TO_FIELDS,
     *                                  SWAPPED_IN_TO_FAST_CACHE );
     *     Page [] dirty_pages = page_table.pages ( dirty_region );
     * </pre>
     *
     * @param region Only consider pages in the specified swap states
     *               inside this region (possibly PageTable.region () ).
     *               Must not be null.
     *
     * @param swap_states One or more swap states.  All Pages
     *                    in any of these SwapStates which are
     *                    currently dirty will be included in
     *                    the returned Region.
     *                    Must not be null.  Must not have any
     *                    null elements.  Must have at least one element.
     *
     * @return The Region of dirty pages in the specified swap state(s).
     *         Never null.  Might be empty.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     *
     * @throws PageTableException If the region is out of the
     *                            bounds of this page table.
     */
    public abstract Region dirtyRegion (
                                        Region region,
                                        SwapState... swap_states
                                        )
        throws I18nIllegalArgumentException,
               PageTableException;


    /**
     * <p>
     * Adds one or more Pages to this PageTable, each Page at the position
     * defined by its region.
     * </p>
     *
     * <p>
     * Typically Pages are allocated by calling:
     * </p>
     *
     * <pre>
     *     Page [] new_pages = new Page [] {
     *         my_paged_area.createPage ( SwapStates.SWAPPED_IN_TO_FIELDS,
     *                                    ...position... ),
     *         ...more pages...
     *     };
     *     my_paged_area.pageTable ().put ( new_pages );
     * </pre>
     *
     * <p>
     * This method takes care of adding the specified page(s) to the
     * end of the kernel's LRU and setting their "clean" flags.
     * </p>
     *
     * <p>
     * Any Pages overlapping the region(s) described by the specified
     * pages will be removed from the PageTable UNLESS the regions do
     * not lie exactly on their boundaries, in which case a
     * PageTableException will be thrown.  For example, if a Page with
     * region (position 17 - position 39) is put in a PageTable
     * containing 8-field-long Pages, each starting at (0, 8, 16, 24, ...),
     * then the third 8-field-long Page (16-23) will be partially
     * overwritten, causing an exception to be thrown.  Typically it is
     * sensible to only deal with Pages containing multiples of 8 Fields
     * (8-Field-long pages, 16-Field-long pages, 32-Field-long pages,
     * 64-Field-long pages, 128-Field-long pages, 256, 512, ...).
     * </p>
     *
     * @param pages The Pages to put in the PageTable.
     *              Must not be null.
     *
     * @throws PageTableException If anything goes horribly wrong.
     */
    public abstract void put (
                              Page [] pages
                              )
        throws PageTableException;


    /**
     * <p>
     * Returns the Page containing the specified Position.
     * </p>
     *
     * @param field_position The position whose Page will be returned.
     *                       Must not be null.  Must be a valid position
     *                       within this PageTable (not NoSuchPosition
     *                       or a position outside the page table's
     *                       region, and so on).
     *
     * @return The page in this PageTable containing the specified Position.
     *         Never null.
     *
     * @throws PageTableException If the specified field position is null,
     *                            NoSuchPosition, or otherwise not in
     *                            this PageTable.
     */
    public abstract Page page (
                               Position field_position
                               )
        throws PageTableException;


    /**
     * <p>
     * Returns the Page(s) intersecting the specified Region.
     * </p>
     *
     * @param region The region from which any and all intersecting
     *               Pages will b returned.  Must not be null.
     *               Must be a valid region within this PageTable
     *               (not NoSuchPosition or UnknownRegion,
     *               nor a position outside the page table's
     *               region, and so on).
     *
     * @return The page(s) in this PageTable intersecting the
     *         specified region.  Never null.
     *
     * @throws PageTableException If the specified region is null,
     *                            NoSuchRegion, or otherwise not in
     *                            this PageTable.
     */
    public abstract Page [] pages (
                                   Region region
                                   )
        throws PageTableException;


    /**
     * <p>
     * Returns the Region describing the Field bounds of this PageTable,
     * and how to step through it.
     * </p>
     *
     * <p>
     * PageTables are divided up into Pages, each of which has
     * 0 or more Fields.  Generally speaking PageTables can be
     * thought of as linear arrays of fields.
     * </p>
     *
     * <p>
     * The Region describing this PageTable has a number of
     * sub-regions.  Each sub-region is a child Region describing
     * a single Page's range of field positions.
     * </p>
     *
     * <p>
     * The Region for a PageTable can be passed around over RMI
     * without serializing the whole PageTable.  It is used by
     * Positions, for example to step through the elements of
     * the PageTable in linear order.
     * </p>
     */
    public abstract SparseRegion region ();


    /**
     * <p>
     * Removes one or more Pages from this PageTable.
     * </p>
     *
     * <p>
     * Typically Pages are freed by calling:
     * </p>
     *
     * <pre>
     *     Page [] remove_pages = ...get specific pages from my_page_table...
     *     my_page_table.remove ( remove_pages );
     *     for ( int p = 0; p < remove_pages.length; p ++ )
     *     {
     *         remove_pages [ p ].free ();
     *     }
     * </pre>
     *
     * @param pages The Pages to remove from the PageTable.
     *              Must not be null.  Can be anywhere in the page
     *              table (for example, when replacing one set of
     *              pages with another set of swapped-in/swapped-out
     *              ones).
     *
     * @throws PageTableException If the specified Pages are not
     *                            in this PageTable, or if anything
     *                            goes horribly wrong.
     */
    public abstract void remove (
                                 Page [] pages
                                 )
        throws PageTableException;


    /**
     * <p>
     * Returns the Space of positions defining Fields within the
     * pages in this PageTable.
     * </p>
     *
     * @return This PageTable's Space, such as an ArraySpace
     *         indexed by integers, or a TimeSpace indexed
     *         by times, and so on.  Never null.
     */
    public abstract Space space ();


    /**
     * <p>
     * Returns the SwapSystem used to swap Pages in and out
     * inside this PageTable.
     * </p>
     *
     * <p>
     * This SwapSystem may be shared by other PageTables.
     * </p>
     *
     * @return This PageTable's SwapSystem.  Never null.
     */
    public SwapSystem swapSystem ();
}
