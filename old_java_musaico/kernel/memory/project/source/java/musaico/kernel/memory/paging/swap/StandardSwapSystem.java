package musaico.kernel.memory.paging.swap;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Comparison;
import musaico.io.Reference;
import musaico.io.Sequence;

import musaico.kernel.memory.paging.Page;
import musaico.kernel.memory.paging.PagedArea;
import musaico.kernel.memory.paging.PageTable;
import musaico.kernel.memory.paging.SwapException;
import musaico.kernel.memory.paging.SwapOperation;
import musaico.kernel.memory.paging.Swapper;
import musaico.kernel.memory.paging.SwapState;
import musaico.kernel.memory.paging.SwapStep;
import musaico.kernel.memory.paging.SwapSystem;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Size;
import musaico.region.Space;

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
 * The StandardSwapSystem requires every swap state to be
 * related to every other swap state's number of fields per page
 * by a power of 2.
 * </p>
 *
 * <p>
 * For example, swap states of page sizes 8 fields, 16 fields and
 * 32 fields can be placed in one StandardSwapSystem, but page
 * sizes of 8, 16, 24 fields per page cannot (because 24/16 = 1.5,
 * not a power of 2).
 * </p>
 *
 * <p>
 * This rule is in order to keep boilerplate internal code relatively
 * simple.
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
public class StandardSwapSystem
    implements SwapSystem, Serializable
{
    /** How to swap from each swapped-out SwapState to the
     *  next swapped-in SwapState, or vice-versa, swapping out
     *  from each swapped-in SwapState to the previous
     *  swapped-out SwapState.  Indexed by swapped-out state.
     *  Can be empty, if there is only one swap state. */
    private final Map<SwapState,Swapper> swappers;

    /** Keep the ordered sequence of SwapStates in a separate
     *  variable for speed. */
    private final Sequence<SwapState> swapStates;

    /** If we step through this many (or more) swap states during
     *  a swap, then there is something wrong with the chain, and
     *  we have entered an infinite loop. */
    private final int infiniteLoop;

    /** How many pages to swap in/out every time a swap is required,
     *  compared to the page size of the smallest swap state.
     *  (Equal to the page size of the largest swap state.) */
    private final long relativeSwapSize;

    /** The relative number of pages for each swap state, compared
     *  to the page size of the smallest swap state.  For example,
     *  if this system has 4 swap states, with page sizes
     *  8, 16, 32 and 128 fields, respectively, then the relative
     *  number of pages for those swap states will be: 16, 8, 4, 1.
     *  This means that for every fully-swapped-in page of 128
     *  fields, it will take 16 fully-swapped-out pages to read
     *  all the fields into the one swapped-in page.  Similarly,
     *  during a swap-out operation, each partly-swapped-in page
     *  of 32 fields will require 2 partly-swapped-out pages of
     *  16 fields each. */
    private final Map<SwapState,Long> relativePageSizes =
        new HashMap<SwapState,Long> ();

    /** The swapped-in-to-fields state. */
    private final SwapState swappedInToFields;

    /** The swapped-in-to-memory swap states. */
    private final SwapState [] inSwapStates;

    /** The swapped-out swap states. */
    private final SwapState [] outSwapStates;


    /**
     * <p>
     * Creates a new StandardSwapSystem with the specified
     * Swappers.
     * </p>
     *
     * @param swapped_out The most-swapped-out SwapState in
     *                    this swap system.  Must be equal to
     *                    swappers [ 0 ].outState () if there
     *                    are 1 or more Swappers in this system.
     *                    Must not be null.
     *
     * @param swapped_in_to_fields The swapped-in-to-fields
     *                             SwapState, in which every
     *                             Page of virtual memory is
     *                             backed by a Buffer of physical
     *                             memory Fields.  Must not be null.
     *
     * @param swappers All swappers for this system, from
     *                 most-swapped-out to most-swapped in.
     *                 The swappers must provide a consistent,
     *                 unbroken chain of SwapStates.
     *                 For example, 2 Swappers might be used
     *                 to swap from "swapped-out-to-file-system"
     *                 in to "swapped-in-to-memory", and then
     *                 from "swapped-in-to-memory" in to
     *                 "swapped-in-even-more-to-fast-cache".
     *                 Must not be null.  Must not contain any
     *                 null elements.  Can be 0-length, if there
     *                 is only one swap state.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public StandardSwapSystem (
                               SwapState swapped_out,
                               SwapState swapped_in_to_fields,
                               Swapper... swappers
                               )
        throws I18nIllegalArgumentException
    {
        if ( swapped_out == null
             || swapped_in_to_fields == null
             || swappers == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a StandardSwapSystem from swapped-out state [%swapped_out%] swapped-in-to-fields state [%swapped_in_to_fields%] swappers [%swappers%]",
                                                     "swapped_out", swapped_out,
                                                     "swapped_in_to_fields", swapped_in_to_fields,
                                                     "swappers", swappers );
        }

        // Every SwapStates is always ordered from most-swapped-out
        // to most-swapped-in.
        this.swappers = new HashMap<SwapState,Swapper> ();
        SwapState [] swap_states = new SwapState [ swappers.length + 1 ];
        this.infiniteLoop = swap_states.length + 1;
        long [] relative_sizes = new long [ swap_states.length ];
        swap_states [ 0 ] = swapped_out;
        relative_sizes [ 0 ] = (long) swapped_out.space ()
            .expr ( swapped_out.pageSize () )
            .ratio ( swapped_out.space ().one () );
        long min_relative_size = relative_sizes [ 0 ];
        int num_in_swap_states;
        for ( int s = 0; s < swappers.length; s ++ )
        {
            if ( swappers [ s ] == null )
            {
                // Null swapper.
            throw new I18nIllegalArgumentException ( "Cannot create a StandardSwapSystem from swapped-out state [%swapped_out%] swapped-in-to-fields state [%swapped_in_to_fields%] swappers [%swappers%]",
                                                     "swapped_out", swapped_out,
                                                     "swapped_in_to_fields", swapped_in_to_fields,
                                                     "swappers", swappers );
            }

            SwapState swapped_out_state = swappers [ s ].outState ();
            if ( s == 0 )
            {
                // Most swapped out.
                if ( ! swap_states [ 0 ].equals ( swapped_out_state ) )
                {
                    // The first swapper does not swap in from
                    // the most-swapped-out state.
            throw new I18nIllegalArgumentException ( "Cannot create a StandardSwapSystem from swapped-out state [%swapped_out%] swapped-in-to-fields state [%swapped_in_to_fields%] swappers [%swappers%]",
                                                     "swapped_out", swapped_out,
                                                     "swapped_in_to_fields", swapped_in_to_fields,
                                                     "swappers", swappers );
                }
            }
            else if ( ! swappers [ s - 1 ].inState ().equals ( swapped_out_state ) )
            {
                // The previous swapper's in state and this swapper's
                // out state don't match.
            throw new I18nIllegalArgumentException ( "Cannot create a StandardSwapSystem from swapped-out state [%swapped_out%] swapped-in-to-fields state [%swapped_in_to_fields%] swappers [%swappers%]",
                                                     "swapped_out", swapped_out,
                                                     "swapped_in_to_fields", swapped_in_to_fields,
                                                     "swappers", swappers );
            }

            this.swappers.put ( swapped_out_state, swappers [ s ] );

            SwapState swapped_in_state = swappers [ s ].inState ();
            swap_states [ s + 1 ] = swapped_in_state;
            relative_sizes [ s + 1 ] = (long) swapped_out.space ()
                .expr ( swapped_in_state.pageSize () )
                .ratio ( swapped_in_state.space ().one () );
            if ( relative_sizes [ s + 1 ] < min_relative_size )
            {
                min_relative_size = relative_sizes [ s + 1 ];
            }
        }


        for ( Swapper swapper1 : swappers )
        {
            for ( Swapper swapper2 : swappers )
            {
                if ( swapper1 == swapper2 )
                {
                    continue;
                }

                SwapState in1 = swapper1.inState ();
                SwapState out1 = swapper1.outState ();
                SwapState in2 = swapper2.inState ();
                SwapState out2 = swapper2.outState ();
                if ( in1.equals ( in2 )
                     || out1.equals ( out2 ) )
                {
                    // Duplicate swap state.
                    throw new I18nIllegalArgumentException ( "Cannot create a StandardSwapSystem from swapped-out state [%swapped_out%] swapped-in-to-fields state [%swapped_in_to_fields%] swappers [%swappers%]",
                                                             "swapped_out", swapped_out,
                                                             "swapped_in_to_fields", swapped_in_to_fields,
                                                             "swappers", swappers );
                }
            }
        }

        this.swapStates = new Sequence<SwapState> ( new NoSwapState (),
                                                    swap_states );
        this.swappedInToFields = swapped_in_to_fields;

        SwapState found_swapped_out = null;
        SwapState found_swapped_in_to_fields = null;
        int index_swapped_in_to_fields = -1;
        int sitf = 0;
        for ( SwapState swap_state : this.swapStates )
        {
            if ( swap_state.equals ( swapped_out ) )
            {
                found_swapped_out = swap_state;
            }

            if ( swap_state.equals ( swapped_in_to_fields ) )
            {
                found_swapped_in_to_fields = swap_state;
                index_swapped_in_to_fields = sitf;
            }

            sitf ++;
        }

        if ( found_swapped_out == null
             || found_swapped_in_to_fields == null
             || index_swapped_in_to_fields < 0 )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a StandardSwapSystem from swapped-out state [%swapped_out%] swapped-in-to-fields state [%swapped_in_to_fields%] swappers [%swappers%]",
                                                     "swapped_out", swapped_out,
                                                     "swapped_in_to_fields", swapped_in_to_fields,
                                                     "swappers", swappers );
        }

        int num_swapped_in_states =
            swap_states.length - index_swapped_in_to_fields;
        int num_swapped_out_states = index_swapped_in_to_fields;
        this.inSwapStates =
            new SwapState [ num_swapped_in_states ];
        this.outSwapStates =
            new SwapState [ num_swapped_out_states ];

        System.arraycopy ( swap_states, index_swapped_in_to_fields,
                           this.inSwapStates, 0,
                           num_swapped_in_states );
        System.arraycopy ( swap_states, 0,
                           this.outSwapStates, 0,
                           num_swapped_out_states );

        // Check that the # of fields per page in each swap state
        // is related to the other page sizes by a power of 2.
        long largest_relative_page_size = 0L;
        for ( int ss = 0; ss < swap_states.length; ss ++ )
        {
            long size_ratio = relative_sizes [ ss ] / min_relative_size;
            long size_remainder = relative_sizes [ ss ] % min_relative_size;

            // Trick retrieved from the following URL 2012-01-24:
            // http://graphics.stanford.edu/~seander/bithacks.html#DetermineIfPowerOf2
            final boolean is_size_ratio_power_of_2 =
                ( size_ratio > 0L )
                && ( size_ratio & ( size_ratio - 1L ) ) == 0L;

            if ( size_remainder != 0L
                 || ! is_size_ratio_power_of_2 )
            {
                throw new I18nIllegalArgumentException ( "Cannot create a StandardSwapSystem from swapped-out state [%swapped_out%] swapped-in-to-fields state [%swapped_in_to_fields%] swappers [%swappers%]",
                                                         "swapped_out", swapped_out,
                                                         "swapped_in_to_fields", swapped_in_to_fields,
                                                         "swappers", swappers );
            }

            this.relativePageSizes.put ( swap_states [ ss ], size_ratio );

            if ( size_ratio > largest_relative_page_size )
            {
                largest_relative_page_size = size_ratio;
            }
        }

        this.relativeSwapSize = largest_relative_page_size;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapSystem#createSwapOperation(musaico.security.Credentials,musaico.kernel.memory.paging.PagedArea,musaico.region.Position,musaico.kernel.memory.paging.SwapState)
     */
    @Override
    public SwapOperation createSwapOperation (
                                              Credentials credentials,
                                              PagedArea paged_area,
                                              Position position,
                                              SwapState target_swap_state
                                              )
        throws I18nIllegalArgumentException,
               SwapException
    {
        if ( paged_area == null
             || position == null
             || target_swap_state == null
             || ! position.space ().equals ( target_swap_state.space () ) )
        {
            throw new I18nIllegalArgumentException ( "SwapSystem [%swap_system%] cannot determine swap pages for paged area [%paged_area%] position [%position%] swap state [%target_swap_state%]",
                                                     "swap_system", this,
                                                     "position", position,
                                                     "target_swap_state", target_swap_state );
        }

        PageTable page_table = paged_area.pageTable ();
        Page current_page = page_table.page ( position );

        if ( current_page == null )
        {
            throw new I18nIllegalArgumentException ( "SwapSystem [%swap_system%] cannot determine swap pages for paged area [%paged_area%] position [%position%] swap state [%target_swap_state%]",
                                                     "swap_system", this,
                                                     "position", position,
                                                     "target_swap_state", target_swap_state );
        }

        SwapState current_swap_state = current_page.swapState ();
        if ( current_swap_state.equals ( target_swap_state ) )
        {
            // No swaps to be performed!
            return new SwapOperation ( SwapOperation.Direction.IN,
                                       new SwapStep [ 0 ] );
        }

        Sequence<SwapState> swap_states = this.swapStates ();
        Comparison comparison = swap_states.compareValues ( current_swap_state,
                                                            target_swap_state );

        final boolean is_swap_in;
        final SwapOperation.Direction direction;
        SwapState from_state = current_swap_state;
        SwapState to_state;
        if ( comparison.equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            // Swap in.
            is_swap_in = true;
            direction = SwapOperation.Direction.IN;
            to_state = swap_states.after ( from_state );
        }
        else if ( comparison.equals ( Comparison.LEFT_GREATER_THAN_RIGHT ) )
        {
            // Swap out.
            is_swap_in = false;
            direction = SwapOperation.Direction.OUT;
            to_state = swap_states.before ( from_state );
        }
        else
        {
            // ! ( current_swap_state < target_swap_state )
            // && ! ( current_swap_state > target_swap_state )
            // So what is it then?  Can't figure out which way to swap.
            throw new I18nIllegalArgumentException ( "SwapSystem [%swap_system%] cannot determine swap pages for paged area [%paged_area%] position [%position%] swap state [%target_swap_state%]",
                                                     "swap_system", this,
                                                     "position", position,
                                                     "target_swap_state", target_swap_state );
        }

        final Map<SwapState,List<Page>> pages_by_swap_state =
            new HashMap<SwapState,List<Page>> ();
        int infinite_loop_protector = 0;
        Position from_position = position;
        List<SwapStep> swap_steps = new ArrayList<SwapStep> ();
        while ( ! from_state.equals ( target_swap_state )
                && infinite_loop_protector < this.infiniteLoop )
        {
	    infinite_loop_protector ++;
            long from_relative_page_size =
                this.relativePageSizes.get ( from_state );
            long from_num_pages_to_swap =
                this.relativeSwapSize / from_relative_page_size;
            Space from_space = from_state.space ();
            Size from_page_size = from_state.pageSize ();
            Size from_swap_size = from_space.expr ( from_page_size )
                .multiply ( (double) from_num_pages_to_swap ).size ();
            Size from_modulo = from_space.expr ( from_position )
                .modulo ( from_swap_size ).size ();
            Position from_start = from_space.expr ( from_position )
                .subtract ( from_modulo ).position ();
            Position from_end = from_space.expr ( from_start )
                .add ( from_swap_size )
                .subtract ( from_space.one () ).position ();
            Region from_region = from_space.region ( from_start, from_end );

            List<Page> from_pages = pages_by_swap_state.get ( from_state );
            if ( from_pages == null )
            {
                // Must be the most-swapped-[in/out] pages.
                // Get them from the page table.
                Page [] page_table_pages = page_table.pages ( from_region );
                from_pages = new ArrayList<Page> ();
                for ( Page page : page_table_pages )
                {
                    from_pages.add ( page );
                }
                pages_by_swap_state.put ( from_state, from_pages );
            }


            final Swapper swapper;
            if ( is_swap_in )
            {
                // Swapped-out state is the from state.
                swapper = this.swapper ( from_state, to_state );
            }
            else
            {
                // Swapped-out state is the to_state.
                swapper = this.swapper ( to_state, from_state );
            }

            final Position to_position;
            if ( is_swap_in )
            {
                to_position = swapper.inPosition ( from_position );
            }
            else
            {
                to_position = swapper.outPosition ( from_position );
            }


            long to_relative_page_size =
                this.relativePageSizes.get ( to_state );
            long to_num_pages_to_swap =
                this.relativeSwapSize / to_relative_page_size;
            Space to_space = to_state.space ();
            Size to_page_size = to_state.pageSize ();
            Size to_swap_size = to_space.expr ( to_page_size )
                .multiply ( (double) to_num_pages_to_swap ).size ();
            Size to_modulo = to_space.expr ( to_position )
                .modulo ( to_swap_size ).size ();
            Position to_start = to_space.expr ( to_position )
                .subtract ( to_modulo ).position ();
            Position to_end = to_space.expr ( to_start )
                .add ( to_swap_size )
                .subtract ( to_space.one () ).position ();
            Region to_region = to_space.region ( to_start, to_end );

	    // Get the configuration field (possibly Field.NULL)
	    // for the target swap state.  For example, the
	    // specific block driver for a swapped-out-to-block-driver
	    // swap state.
	    Field swap_state_configuration =
		paged_area.swapConfiguration ().forSwapState ( to_state );

            // Create the pages to swap to.
            List<Page> to_pages = new ArrayList<Page> ();
            Position to_page_start = to_region.start ();
            for ( long page_num = 0L;
                  page_num < to_num_pages_to_swap;
                  page_num ++ )
            {
                // Throws SwapException.
                Page to_page =
		    to_state.createPage ( credentials,
					  to_page_start,
					  swap_state_configuration );
                to_pages.add ( to_page );

                to_page_start = to_space.expr ( to_page_start )
                    .add ( to_page_size ).position ();
            }
            pages_by_swap_state.put ( to_state, to_pages );

            // Now create the swap steps.
            // The swapped-from and swapped-to pages might be
            // different sizes, so we might need to step through
            // regions of one or the other.  We know that they
            // are related by a power of 2, so we can safely divide
            // their relative page sizes.
            final long num_steps;
            final long page_size_ratio;
            final Size from_step_size;
            final Size to_step_size;
            if ( from_num_pages_to_swap >= to_num_pages_to_swap )
            {
                num_steps = from_num_pages_to_swap;
                page_size_ratio =
                    to_relative_page_size / from_relative_page_size;
                from_step_size = from_page_size;
                to_step_size = to_space.expr ( to_page_size )
                    .divide ( (double) page_size_ratio )
                    .size ();
            }
            else
            {
                num_steps = to_num_pages_to_swap;
                page_size_ratio =
                    from_relative_page_size / to_relative_page_size;
                from_step_size = from_space.expr ( from_page_size )
                    .divide ( (double) page_size_ratio )
                    .size ();
                to_step_size = to_page_size;
            }

            Position from_start_step = from_region.start ();
            Position to_start_step = to_region.start ();
            for ( long s = 0L; s < num_steps; s ++ )
            {
                int from_page_num = (int)
                    ( ( s * (long) from_pages.size () ) / num_steps );
                Page from_page = from_pages.get ( from_page_num );

                int to_page_num = (int)
                    ( ( s * (long) to_pages.size () ) / num_steps );
                Page to_page = to_pages.get ( to_page_num );

                Position from_end_step = from_space.expr ( from_start_step )
                    .add ( from_step_size )
                    .subtract ( from_space.one () ).position ();
                Region from_region_step =
                    from_space.region ( from_start_step, from_end_step );

                Position to_end_step = to_space.expr ( to_start_step )
                    .add ( to_step_size )
                    .subtract ( to_space.one () ).position ();
                Region to_region_step =
                    to_space.region ( to_start_step, to_end_step );

                final SwapStep swap_step;
                if ( is_swap_in )
                {
                    swap_step = new SwapStep ( swapper,
                                               direction,
                                               from_page,
                                               from_region_step,
                                               to_page,
                                               to_region_step );
                }
                else
                {
                    swap_step = new SwapStep ( swapper,
                                               direction,
                                               to_page,
                                               to_region_step,
                                               from_page,
                                               from_region_step );
                }

                swap_steps.add ( swap_step );

                from_start_step = from_space.expr ( from_start_step )
                    .add ( from_step_size ).position ();

                to_start_step = to_space.expr ( to_start_step )
                    .add ( to_step_size ).position ();
            }

            from_position = to_position;
            if ( is_swap_in )
            {
                from_state = swap_states.after ( from_state );
            }
            else
            {
                from_state = swap_states.before ( from_state );
            }
        }

        SwapStep [] template = new SwapStep [ swap_steps.size () ];
        SwapStep [] swap_steps_array = swap_steps.toArray ( template );

        SwapOperation swap_operation = new SwapOperation ( direction,
                                                           swap_steps_array );

        return swap_operation;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapSystem#inFrom(musaico.kernel.memory.paging.SwapState)
     */
    @Override
    public SwapState inFrom (
                             SwapState out_swap_state
                             )
        throws I18nIllegalArgumentException
    {
        if ( out_swap_state == null
             || ( out_swap_state instanceof NoSwapState ) )
        {
            throw new I18nIllegalArgumentException ( "StandardSwapSystem [%swap_states%] has no corresponding in swap state for out_swap_state [%swap_state%]",
                                                     "swap_states", this,
                                                     "swap_state", out_swap_state );
        }

        SwapState in_swap_state = this.swapStates ().after ( out_swap_state );
        if ( in_swap_state instanceof NoSwapState )
        {
            throw new I18nIllegalArgumentException ( "StandardSwapSystem [%swap_states%] has no corresponding in swap state for out_swap_state [%swap_state%]",
                                                     "swap_states", this,
                                                     "swap_state", out_swap_state );
        }

        return in_swap_state;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapSystem#inSwapStates()
     */
    @Override
    public SwapState [] inSwapStates ()
    {
        SwapState [] in_swap_states =
            new SwapState [ this.inSwapStates.length ];
        System.arraycopy ( this.inSwapStates, 0,
                           in_swap_states, 0, this.inSwapStates.length );

        return in_swap_states;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapSystem#isSwapInable(musaico.kernel.memory.paging.SwapState)
     */
    @Override
    public boolean isSwapInable (
                                 SwapState out_state
                                 )
    {
        if ( out_state != null
             && this.swappers.containsKey ( out_state ) )
        {
            // It has a swapper to swap in, so it is swap-in-able.
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.kernel.memory.paging.SwapSystem#isSwapOutable(musaico.kernel.memory.paging.SwapState)
     */
    @Override
    public boolean isSwapOutable (
                                  SwapState in_state
                                  )
    {
        if ( in_state != null
             && this.relativePageSizes.containsKey ( in_state )
             && ! ( this.swapStates ().before ( in_state )
                    instanceof NoSwapState ) )
        {
            // It has a SwapState before it, so it must be swap-out-able.
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.kernel.memory.paging.SwapSystem#outFrom(musaico.kernel.memory.paging.SwapState)
     */
    @Override
    public SwapState outFrom (
                              SwapState in_swap_state
                              )
        throws I18nIllegalArgumentException
    {
        if ( in_swap_state == null
             || ( in_swap_state instanceof NoSwapState ) )
        {
            throw new I18nIllegalArgumentException ( "StandardSwapSystem [%swap_states%] has no corresponding out swap state for in_swap_state [%swap_state%]",
                                                     "swap_states", this,
                                                     "swap_state", in_swap_state );
        }

        SwapState out_swap_state = this.swapStates ().before ( in_swap_state );
        if ( out_swap_state instanceof NoSwapState )
        {
            throw new I18nIllegalArgumentException ( "StandardSwapSystem [%swap_states%] has no corresponding out swap state for in_swap_state [%swap_state%]",
                                                     "swap_states", this,
                                                     "swap_state", in_swap_state );
        }

        return out_swap_state;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapSystem#outSwapStates()
     */
    @Override
    public SwapState [] outSwapStates ()
    {
        SwapState [] out_swap_states =
            new SwapState [ this.outSwapStates.length ];
        System.arraycopy ( this.outSwapStates, 0,
                           out_swap_states, 0, this.outSwapStates.length );

        return out_swap_states;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapSystem#swapper(musaico.kernel.memory.paging.SwapState,musaico.kernel.memory.paging.SwapState)
     */
    @Override
    public Swapper swapper (
                            SwapState out_swap_state,
                            SwapState in_swap_state
                            )
        throws I18nIllegalArgumentException
    {
        if ( out_swap_state == null
             || in_swap_state == null )
        {
            throw new I18nIllegalArgumentException ( "SwapSystem [%swap_system%] has no swapper for swap states out [%out_swap_state%] and in [%in_swap_state%]",
                                                     "swap_system", this,
                                                     "out_swap_state", out_swap_state,
                                                     "in_swap_state", in_swap_state );
        }

        Swapper swapper = this.swappers.get ( out_swap_state );
        if ( swapper == null )
        {
            throw new I18nIllegalArgumentException ( "SwapSystem [%swap_system%] has no swapper for swap states out [%out_swap_state%] and in [%in_swap_state%]",
                                                     "swap_system", this,
                                                     "out_swap_state", out_swap_state,
                                                     "in_swap_state", in_swap_state );
        }

        return swapper;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapState#swappedInToFields()
     */
    @Override
    public SwapState swappedInToFields ()
    {
        return this.swappedInToFields;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapSystem#swapStates()
     */
    @Override
    public Sequence<SwapState> swapStates ()
    {
        return this.swapStates;
    }
}
