package musaico.kernel.memory.paging.pagetables;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Comparison;
import musaico.io.Filter;
import musaico.io.Order;
import musaico.io.Reference;
import musaico.io.ReferenceCount;

import musaico.io.references.UUIDReference;

import musaico.kernel.memory.paging.Page;
import musaico.kernel.memory.paging.PageTable;
import musaico.kernel.memory.paging.PageTableException;
import musaico.kernel.memory.paging.SwapState;
import musaico.kernel.memory.paging.SwapSystem;

import musaico.region.SpecificPosition;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Space;
import musaico.region.SparseRegion;
import musaico.region.SparseRegionBuilder;


/**
 * <p>
 * A basic implementation of the PageTable lookup of pages for
 * a paged area.
 * </p>
 *
 * <p>
 * This paged table cannot handle pages from different
 * position/region/size spaces.  For example, pages with
 * ArrayRegions and pages with TimeRegions cannot be mixed
 * together in this page table.
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
 * Copyright (c) 2010, 2011, 2012 Johann Tienhaara
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
public class SimplePageTable
    implements PageTable, Serializable
{
    /** Synchronize all critical sections on this lock: */
    private final Serializable lock = new String ();

    /** The Space describing the positions and regions in this PageTable. */
    private final Space space;

    /** The SwapSystem which tells use how and where to swap in / out. */
    private final SwapSystem swapSystem;

    /** The pages of virtual memory contained within this page table,
     *  in order of start/end positions. */
    private final List<Page> pages;

    /** The region of memory covered by this page_table,
     *  including the start posiition and end position of the memory,
     *  and how to step between the start and end points.
     *  Each "section" describes the sub-region for one Page.
     *  Mutable and changes over time. */
    private SparseRegion region;


    /**
     * <p>
     * Creates a new SimplePageTable in the specified Space.
     * </p>
     *
     * @param swap_system The swap system which this PageTable
     *                    uses to swap between pages, create
     *                    pages and so on.  Must not be null.
     *                    All of the swap states in the swap
     *                    system must be in the same space.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public SimplePageTable (
                            SwapSystem swap_system
                            )
    {
        if ( swap_system == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimplePageTable with swap system [%swap_system%]",
                                                     "swap_system", swap_system );
        }

        this.swapSystem = swap_system;

        Space space = null;
        for ( SwapState swap_state : this.swapSystem.swapStates () )
        {
            Space next_space = swap_state.space ();
            if ( space != null
                 && ! ( next_space.equals ( space ) ) )
            {
                throw new I18nIllegalArgumentException ( "Cannot create a SimplePageTable with swap system [%swap_system%]",
                                                         "swap_system", swap_system );
            }

            space = next_space;
        }

        if ( space == null )
        {
            // ??? No swap states!
            throw new I18nIllegalArgumentException ( "Cannot create a SimplePageTable with swap system [%swap_system%]",
                                                     "swap_system", swap_system );
        }

        this.space = space;

        this.pages = new ArrayList<Page> ();
        this.region = this.createRegion ();
    }


    /**
     * <p>
     * Creates a new SparseRegion describing this PageTable.
     * </p>
     *
     * <p>
     * <b> Always synchronize around calls to this method! </b>
     * </p>
     *
     * @return The newly created SparseRegion.
     *         Never null.
     */
    private SparseRegion createRegion ()
    {
        SparseRegionBuilder builder =
            this.space.sparseRegionBuilder ();
        for ( int p = 0; p < this.pages.size (); p ++ )
        {
            Page page = this.pages.get ( p );
            Region page_region = page.region ();

            builder.concatenate ( page_region );
        }

        SparseRegion page_table_region = builder.build ();

        return page_table_region;
    }


    /**
     * @see musaico.kernel.memory.paging.PageTable#cleanAll(musaico.region.Region,musaico.kernel.memory.paging.SwapState[])
     */
    @Override
    public void cleanAll (
                          Region region,
                          SwapState... swap_states
                          )
        throws I18nIllegalArgumentException,
               PageTableException
    {
        if ( swap_states == null
             || swap_states.length < 1 )
        {
            throw new I18nIllegalArgumentException ( "Page table [%page_table%] cannot clean all pages in swap states [%swap_states%]",
                                                     "page_table", this,
                                                     "swap_states", swap_states );
        }

        // Turn the swap states into a set so we can do quicker
        // lookups.
        Set<SwapState> swap_states_set = new HashSet<SwapState> ();
        for ( int ss = 0; ss < swap_states.length; ss ++ )
        {
            swap_states_set.add ( swap_states [ ss ] );
        }

        synchronized ( this.lock )
        {
            // pages () can throw a PageTableException.
            Page [] pages_in_region = this.pages ( region );
            for ( Page page : pages_in_region )
            {
                if ( swap_states_set.contains ( page.swapState () ) )
                {
                    page.kernelPaging ().clean ( page );
                }
            }
        }
    }


    /**
     * @see musaico.kernel.memory.paging.PageTable#cleanRegion(musaico.region.Region,musaico.kernel.memory.paging.SwapState[])
     */
    @Override
    public Region cleanRegion (
                               Region region,
                               SwapState... swap_states
                               )
        throws I18nIllegalArgumentException,
               PageTableException
    {
        if ( swap_states == null
             || swap_states.length < 1 )
        {
            throw new I18nIllegalArgumentException ( "Page table [%page_table%] cannot return clean region for swap states [%swap_states%]",
                                                     "page_table", this,
                                                     "swap_states", swap_states );
        }

        Filter clean_pages_only =
            new CleanDirtyPageFilter ( this,
                                       false, // clean
                                       swap_states );
        final Region clean_region;
        synchronized ( this.lock )
        {
            clean_region =
                this.space.expr ( region )
                .filter ( clean_pages_only ).region ();
        }

        return clean_region;
    }


    /**
     * @see musaico.kernel.memory.paging.PageTable#dirtyAll(musaico.region.Region,musaico.kernel.memory.paging.SwapState[])
     */
    @Override
    public void dirtyAll (
                          Region region,
                          SwapState... swap_states
                          )
        throws I18nIllegalArgumentException,
               PageTableException
    {
        if ( swap_states == null
             || swap_states.length < 1 )
        {
            throw new I18nIllegalArgumentException ( "Page table [%page_table%] cannot dirty all pages in swap states [%swap_states%]",
                                                     "page_table", this,
                                                     "swap_states", swap_states );
        }

        // Turn the swap states into a set so we can do quicker
        // lookups.
        Set<SwapState> swap_states_set = new HashSet<SwapState> ();
        for ( int ss = 0; ss < swap_states.length; ss ++ )
        {
            swap_states_set.add ( swap_states [ ss ] );
        }

        synchronized ( this.lock )
        {
            // pages () can throw a PageTableException.
            Page [] pages_in_region = this.pages ( region );
            for ( Page page : pages_in_region )
            {
                if ( swap_states_set.contains ( page.swapState () ) )
                {
                    page.kernelPaging ().dirty ( page );
                }
            }
        }
    }


    /**
     * @see musaico.kernel.memory.paging.PageTable#dirtyRegion(musaico.region.Region,musaico.kernel.memory.paging.SwapState[])
     */
    @Override
    public Region dirtyRegion (
                               Region region,
                               SwapState... swap_states
                               )
        throws I18nIllegalArgumentException,
               PageTableException
    {
        if ( swap_states == null
             || swap_states.length < 1 )
        {
            throw new I18nIllegalArgumentException ( "Page table [%page_table%] cannot return dirty region for swap states [%swap_states%]",
                                                     "page_table", this,
                                                     "swap_states", swap_states );
        }

        Filter dirty_pages_only =
            new CleanDirtyPageFilter ( this,
                                       true, // dirty
                                       swap_states );
        final Region dirty_region;
        synchronized ( this.lock )
        {
            dirty_region =
                this.space.expr ( region )
                .filter ( dirty_pages_only ).region ();
        }

        return dirty_region;
    }


    /**
     * @see musaico.kernel.memory.paging.PageTable#put(musaico.kernel.memory.paging.Page[])
     */
    @Override
    public void put (
                     Page [] pages
                     )
        throws PageTableException
    {
        if ( pages.length == 0 )
        {
            return;
        }

        synchronized ( this.lock )
        {
            // Get the set of pages to remove.
            Set<Page> page_set_to_remove = new HashSet<Page> ();
            for ( int p = 0; p < pages.length; p ++ )
            {
                Page [] overlapping_pages =
                    this.pages ( pages [ p ].region () );
                for ( int op = 0; op < overlapping_pages.length; op ++ )
                {
                    page_set_to_remove.add ( overlapping_pages [ op ] );
                }
            }

            // Convert the set of overlapping pages to be
            // removed into an array.
            Page [] pages_to_remove = new Page [ page_set_to_remove.size () ];
            Iterator<Page> removerator = page_set_to_remove.iterator ();
            int r = 0;
            while ( removerator.hasNext () )
            {
                Page page_to_remove = removerator.next ();
                pages_to_remove [ r ] = page_to_remove;
                r ++;
            }

            // Now add the new pages.
            Order<Position> order = this.space.order ();
            for ( int p = 0; p < pages.length; p ++ )
            {
                Position start = pages [ p ].region ().start ();
                Position end = pages [ p ].region ().end ();

                // Try to avoid calling search functions if we can.
                // If the new page(s) come before or after all existing
                // pages then we don't have to search for where to put
                // them.
                //
                // We are NOT re-calculating our own region here, so
                // be careful -- it will be obsolete as we start adding
                // pages!
                final int index;
                if ( this.pages.size () == 0 )
                {
                    // No contents currently, so put it at the start.
                    index = 0;
                }
                else if ( order.compareValues ( end,
                                                this.pages.get ( 0 ).region ().start () )
                          .equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
                {
                    // Before the start of all existing pages.
                    index = 0;
                }
                else if ( order.compareValues ( start,
                                                this.pages.get ( this.pages.size () - 1 ).region ().end () )
                          .equals ( Comparison.LEFT_GREATER_THAN_RIGHT ) )
                {
                    // After the end of all existing pages.
                    index = this.pages.size ();
                }
                else
                {
                    SpecificPosition find_page_start =
                        new SpecificPosition ( start );
                    index = (int) this.region ().search ( find_page_start )
                        .findSubRegionIndex ();
                }

                this.pages.add ( index, pages [ p ] );
            }

            // Remove the overlapping pages.
            this.remove ( pages_to_remove );

            // Re-create the region covered by this page
            // table, since it has likely changed (either the
            // sections are different sizes, or the whole size
            // has increased, or both).
            this.region = this.createRegion ();
        }
    }


    /**
     * @see musaico.kernel.memory.paging.PageTable#page(musaico.region.Position)
     */
    @Override
    public Page page (
                      Position field_position
                      )
        throws PageTableException
    {
        // Make sure nothing changes while we retrieve the page.
        synchronized ( this.lock )
        {
            SpecificPosition specific_position =
                new SpecificPosition ( field_position );
            long page_num = this.region ().search ( specific_position )
                .findSubRegionIndex ();

            if ( page_num < 0L
                 || page_num > this.pages.size () )
            {
                // No such page.
                throw new PageTableException ( "PageTable [%page_table%] does not contain a Page for position [%field_position%]",
                                               "page_table", this,
                                               "field_position", field_position );
            }

            int page_index = (int) page_num;
            return this.pages.get ( page_index );
        }
    }


    /**
     * @see musaico.kernel.memory.paging.PageTable#pages(musaico.region.Region)
     */
    @Override
    public Page [] pages (
                          Region overlapping_region
                          )
        throws PageTableException
    {
        List<Page> pages_list = new ArrayList<Page> ();

        // Make sure nothing changes while we retrieve the page(s).
        final Page [] pages;
        synchronized ( this.lock )
        {
            Position start = overlapping_region.start ();
            Position end = overlapping_region.end ();

            SpecificPosition find_start = new SpecificPosition ( start );
            SpecificPosition find_end = new SpecificPosition ( end );

            long start_index = this.region ().search ( find_start )
                .findSubRegionIndex ();
            long end_index = this.region ().search ( find_end )
                .findSubRegionIndex ();

            if ( end_index < 0L
                 && start_index >= 0L )
            {
                end_index = this.region ().numRegions () - 1L;
            }

            if ( start_index < 0L
                 || end_index < 0L
                 || start_index >= this.pages.size ()
                 || end_index >= this.pages.size ()
                 || start_index > end_index )
            {
                // Before the start of all existing pages or
                // after the end of all existing pages.
                return new Page [ 0 ];
            }

            pages = new Page [ (int) ( end_index - start_index + 1 ) ];
            for ( int index = (int) start_index;
                  index <= (int) end_index;
                  index ++ )
            {
                Page page = this.pages.get ( index );
                pages [ index ] = page;
            }
        }

        return pages;
    }


    /**
     * @see musaico.kernel.memory.paging.PageTable#region()
     */
    @Override
    public SparseRegion region ()
    {
        return this.region;
    }


    /**
     * @see musaico.kernel.memory.paging.PageTable#remove(musaico.kernel.memory.paging.Page[])
     */
    @Override
    public void remove (
                        Page [] pages
                        )
        throws PageTableException
    {
        synchronized ( this.lock )
        {
            for ( int p = 0; p < pages.length; p ++ )
            {
                if ( ! this.pages.contains ( pages [ p ] ) )
                {
                    throw new PageTableException ( "No such page [%page%] in page table [%page_table%]",
                                                   "page", pages [ p ],
                                                   "page_table", this );
                }
            }

            for ( int p = 0; p < pages.length; p ++ )
            {
                this.pages.remove ( pages [ p ] );
            }

            this.region = this.createRegion ();
        }
    }


    /**
     * @see musaico.kernel.memory.paging.PageTable#space()
     */
    @Override
    public Space space ()
    {
        return this.space;
    }


    /**
     * @see musaico.kernel.memory.paging.PageTable#swapSystem()
     */
    @Override
    public SwapSystem swapSystem ()
    {
        return this.swapSystem;
    }
}
