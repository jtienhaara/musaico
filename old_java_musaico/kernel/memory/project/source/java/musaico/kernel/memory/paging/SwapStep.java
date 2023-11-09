package musaico.kernel.memory.paging;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.region.Region;

import musaico.security.Credentials;


/**
 * <p>
 * A container for mapping a swapped-in page to a swapped-out
 * page (or vice-versa) over some region (either covering the
 * entire region of each page, or some subset thereof).
 * </p>
 *
 * <p>
 * SwapStep simplifies the geometry of swaps, allowing the
 * caller of a SwapSystem to swap in or out one page at a time.
 * The onus is on a SwapSystem to create the SwapStep required
 * to swap in or out a given position or region in virtual memory.
 * </p>
 *
 * <p>
 * A user of a SwapStep might do something like the following
 * to perform one part of a swap operation:
 * </p>
 *
 * <pre>
 *     SwapStep swap_step = ...;
 *     swap_step.swap ( credentials );
 * </pre>
 *
 * <p>
 * Thus a caller might do something like the following
 * to read in all the sectors from the backing driver
 * into memory:
 * </p>
 *
 * <pre>
 *     PagedArea paged_area = ...;
 *     Position position_to_swap_in = ...;
 *     SwapSystem swap_system = paged_area.swapSystem ();
 *     SwapState in_memory = swap_system.swappedInToFields ();
 *
 *     synchronized ( ...some atomic lock on the pages... )
 *     {
 *         SwapStep [] swaps =
 *             swap_system.swapstep ( paged_area,
 *                                    position_to_swap_in,
 *                                    in_memory );
 *
 *         Set<Page> pages = new HashSet<Page> ();
 *         for ( SwapStep swap_step : swaps )
 *         {
 *             swap_step.swap ( credentials );
 *
 *             pages.add ( swap_step.inPage () );
 *         }
 *
 *         paged_area.pageTable ().put ( pages.toArray ( new Page [ 0 ] ) );
 *     }
 * </pre>
 *
 * <p>
 * Similarly, swapping out or synchronizing dirty data might
 * be done with something like:
 * </p>
 *
 * <pre>
 *     PagedArea paged_area = ...;
 *     Position position_to_swap_out = ...;
 *     SwapSystem swap_system = paged_area.swapSystem ();
 *     SwapState swapped_out = swap_system.swapStates ().first ();
 *
 *     synchronized ( ...some atomic lock on the pages... )
 *     {
 *         SwapStep [] swaps =
 *             swap_system.swapstep ( paged_area,
 *                                    position_to_swap_out,
 *                                    swapped_out );
 *
 *         Set<Page> pages = new HashSet<Page> ();
 *         for ( SwapStep swap_step : swap_step )
 *         {
 *             swap_step.swap ( credentials );
 *
 *             pages.add ( swap_step.outPage () );
 *         }
 *
 *         paged_area.pageTable ().put ( pages.toArray ( new Page [ 0 ] ) );
 *     }
 * </pre>
 *
 * <p>
 * Each SwapStep can <code> swap () </code> only once.
 * </p>
 *
 * <p>
 * Under the hood, calling each SwapStep.swap () is the same as:
 * </p>
 *
 * <pre>
 *     SwapStep swap_step = ...;
 *     Swapper swapper = swap_step.swapper ();
 *     Page out_page = swap_step.outPage ();
 *     Region out_region = swap_step.outRegion ();
 *     Page in_page = swap_step.inPage ();
 *     Region in_region = swap_step.inRegion ();
 *
 *     if ( swap_step.isSwapIn () )
 *     {
 *         swapper.readIn ( out_page,
 *                          out_region,
 *                          in_page,
 *                          in_region );
 *     }
 *     else
 *     {
 *         swapper.writeOut ( in_page,
 *                            in_region,
 *                            out_page,
 *                            out_region );
 *     }
 * </pre>
 *
 * <p>
 * The SwapStep class is NOT Serializable.  It is meant to
 * be used locally for atomic page swaps, not shared over RMI
 * or stored persistently and so on.
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
public class SwapStep
{
    /** Lock to prevent 2 callers from swap () ing concurrently. */
    private final Object lock = new Object ();

    /** The Swapper to swap the page in or out. */
    private final Swapper swapper;

    /** Will the swap be a readIn?  If not, it will be a writeOut. */
    private final boolean isSwapIn;

    /** The swapped-out page. */
    private final Page outPage;

    /** The region of the swapped-out page to swap in from or out to. */
    private final Region outRegion;

    /** The swapped-in page. */
    private final Page inPage;

    /** The region of the swapped-in page to swap out from or in to. */
    private final Region inRegion;


    /** Whether or not this swap step is currently swap () ping.
     *  False initially, true once the swap has been started. */
    private boolean isSwapStarted = false;

    /** Whether or not this swap step has been performed yet.
     *  False initially, true once the swap has been successfully
     *  completed. */
    private boolean isSwapCompleted = false;


    /**
     * <p>
     * Creates a new SwapStep with the specified Swapper, swap
     * direction, swapped-out page, region to / from which to swap
     * inside the swapped-out page, swapped-in page and region
     * to/from which to swap inside the swapped-in page.
     * </p>
     *
     * @param swapper The swapper to perform the swap step.
     *                Must not be null.
     *
     * @param direction Either <code> SwapOperation.Direction.IN </code>
     *                  to swap in from the out page to the in page,
     *                  or <code> SwapOperation.Direction.OUT </code>
     *                  to swap out from the in page to the out page.
     *                  Must not be null.
     *
     * @param out_page The swapped-out page to swap to/from.
     *                 Must not be null.
     *
     * @param out_region The region of the swapped-out page to
     *                   swap to/from (either the whole page region
     *                   or some subset thereof).  Must not be null.
     *
     * @param in_page The swapped-in page to swap to/from.
     *                Must not be null.
     *
     * @param in_region The region of the swapped-in page to
     *                  swap to/from (either the whole page region
     *                  or some subset thereof).  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public SwapStep (
                     Swapper swapper,
                     SwapOperation.Direction direction,
                     Page out_page,
                     Region out_region,
                     Page in_page,
                     Region in_region
                     )
        throws I18nIllegalArgumentException
    {
        if ( swapper == null
             || direction == null
             || out_page == null
             || out_region == null
             || ! out_region.space ().equals ( out_page.region ().space () )
             || in_page == null
             || in_region == null
             || ! in_region.space ().equals ( in_page.region ().space () ) )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SwapStep with swapper [%swapper%] direction [%direction%] out_page [%out_page%] out_region [%out_region%] in_page [%in_page%] in_region [%in_region%]",
                                                     "swapper", swapper,
                                                     "direction", direction,
                                                     "out_page", out_page,
                                                     "out_region", out_region,
                                                     "in_page", in_page,
                                                     "in_region", in_region );
        }

        this.swapper = swapper;
        if ( direction.equals ( SwapOperation.Direction.IN ) )
        {
            this.isSwapIn = true;
        }
        else
        {
            this.isSwapIn = false;
        }

        this.outPage = out_page;
        this.outRegion = out_region;
        this.inPage = in_page;
        this.inRegion = in_region;
    }


    /**
     * <p>
     * Returns the swapped-in page for this swap step.
     * </p>
     *
     * @return The swapped-in Page.  Never null.
     */
    public Page inPage ()
    {
        return this.inPage;
    }


    /**
     * <p>
     * Returns the Region of the swapped-in page for this swap step.
     * </p>
     *
     * @return The Region of the swapped-in Page.  Never null.
     */
    public Region inRegion ()
    {
        return this.inRegion;
    }


    /**
     * <p>
     * Returns true if swap () has been successfully
     * completed, false if not.
     * </p>
     *
     * @return True if this step has already swap () ped,
     *         false if either it has not been executed or it
     *         was attempted but failed.
     */
    public boolean isSwapCompleted ()
    {
        synchronized ( this.lock )
        {
            return this.isSwapCompleted;
        }
    }


    /**
     * <p>
     * Returns true if the operation is a swap-in, false if the
     * operation is a swap-out.
     * </p>
     *
     * @return True if this step involves a swap-in, false if swap-out.
     */
    public boolean isSwapIn ()
    {
        return this.isSwapIn;
    }


    /**
     * <p>
     * Returns true if swap () has been started, false if not.
     * </p>
     *
     * @return True if this step's swap () has been invoked,
     *         false if either it has never been invoked.
     */
    public boolean isSwapStarted ()
    {
        synchronized ( this.lock )
        {
            return this.isSwapStarted;
        }
    }


    /**
     * <p>
     * Returns the swapped-out page for this swap step.
     * </p>
     *
     * @return The swapped-out Page.  Never null.
     */
    public Page outPage ()
    {
        return this.outPage;
    }


    /**
     * <p>
     * Returns the Region of the swapped-out page for this swap step.
     * </p>
     *
     * @return The Region of the swapped-out Page.  Never null.
     */
    public Region outRegion ()
    {
        return this.outRegion;
    }


    /**
     * <p>
     * Performs this swap step without locking anything.
     * </p>
     *
     * <p>
     * Generally you should lock something to make sure this
     * step, and all other swap steps being performed, are
     * executed atomically.
     * </p>
     *
     * @param credentials Who is requesting the swap.
     *                    The Credentials determine whether
     *                    this swap is authorized.
     *
     * @throws SwapException If the underlying Swapper throws
     *                       an exception during the swap.
     */
    public void swap (
                      Credentials credentials
                      )
        throws SwapException
    {
        synchronized ( this.lock )
        {
            if ( this.isSwapStarted )
            {
                final SwapOperation.Direction direction;
                if ( this.isSwapIn () )
                {
                    direction = SwapOperation.Direction.IN;
                }
                else
                {
                    direction = SwapOperation.Direction.OUT;
                }

                throw new SwapException ( "SwapStep [%swap_step%] direction [%direction%] out page [%out_page%] region [%out_region%] in page [%in_page%] [%in_region%] has already swapped",
                                          "swap_step", this,
                                          "direction", direction,
                                          "out_page", this.outPage (),
                                          "out_region", this.outRegion (),
                                          "in_page", this.inPage (),
                                          "in_region", this.inRegion () );
            }

            this.isSwapStarted = true;
        }

        if ( this.isSwapIn () )
        {
            this.swapper ().readIn ( credentials,
                                     this.outPage (),
                                     this.outRegion (),
                                     this.inPage (),
                                     this.inRegion () );
        }
        else
        {
            this.swapper ().writeOut ( credentials,
                                       this.inPage (),
                                       this.inRegion (),
                                       this.outPage (),
                                       this.outRegion () );
        }

        synchronized ( this.lock )
        {
            this.isSwapCompleted = true;
        }
    }


    /**
     * <p>
     * Returns the swapper for this swap step.
     * </p>
     *
     * @return The Swapper.  Never null.
     */
    public Swapper swapper ()
    {
        return this.swapper;
    }
}
