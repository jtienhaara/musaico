package musaico.kernel.memory.paging;

import java.util.HashSet;
import java.util.Set;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.security.Credentials;


/**
 * <p>
 * An atomic swap operation on a PagedArea, swapping 0 or more
 * Pages in or out to a target SwapState.
 * </p>
 *
 * <p>
 * A SwapOperation can be executed only once.
 * </p>
 *
 * <p>
 * The SwapOperation class is NOT Serializable.  It is meant to
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
public class SwapOperation
{
    /** Which direction to swap. */
    public static enum Direction
    {
        IN,
        OUT;
    }


    /** The direction of this swap (in or out). */
    private final SwapOperation.Direction direction;

    /** The steps to perform in this swap operation.  Possibly 0-length,
     *  if the desired data is already in the target swap state. */
    private final SwapStep [] swapSteps;


    /**
     * <p>
     * Creates a new SwapOperation comprising the specified individual
     * steps.
     * </p>
     *
     * <p>
     * Each step involves swapping a region of one page in to or
     * out from a region of another page.  Because pages in different
     * swap states might be different sizes, each SwapStep might
     * involve, for example, swapping an entire swapped-out page
     * to 1/8 of the region covered by a swapped-in page.
     * Or vice-versa, one step might involve swapping 1/8 of
     * a swapped-in page out to an entire swapped-out page.  And so on.
     * This SwapOperation covers all the steps necessary to bring
     * a specific Position and its surrounding region to a specific
     * SwapState.
     * </p>
     *
     * @param direction The direction of the swap.
     *                  Either <code> SwapOperation.Direction.IN </code>
     *                  or  <code> SwapOperation.Direction.OUT </code>.
     *                  Must not be null.
     *
     * @param swap_steps The steps to perform for this SwapOperation.
     *                   Must not be null.  Must not contain any null
     *                   elements.  Can be 0-length, if the desired
     *                   data is already in its target swap state.
     *                   Must be all the same swap direction.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified are invalid
     *                                      (see above).
     */
    public SwapOperation (
                          SwapOperation.Direction direction,
                          SwapStep [] swap_steps
                          )
    {
        if ( direction == null
             || swap_steps == null
             || ! SwapOperation.isValidSwapOperation ( direction, swap_steps ) )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SwapOperation in direction [%direction%] with swap_steps [%swap_steps%]",
                                                     "direction", direction,
                                                     "swap_steps", swap_steps );
        }

        this.direction = direction;
        this.swapSteps = new SwapStep [ swap_steps.length ];
        System.arraycopy ( swap_steps, 0,
                           this.swapSteps, 0, this.swapSteps.length );
    }


    /**
     * @return True if the specified array of SwapSteps is valid for
     *         a SwapOperation, false if it contains any null
     *         elements or a step which is in the wrong direction.
     */
    private static final boolean isValidSwapOperation (
                                                       SwapOperation.Direction direction,
                                                       SwapStep [] swap_steps
                                                       )
    {
        for ( SwapStep swap_step : swap_steps )
        {
            if ( swap_step == null )
            {
                return false;
            }
            else if ( direction.equals ( SwapOperation.Direction.IN ) )
            {
                // Swap in
                if ( ! swap_step.isSwapIn () )
                {
                    return false;
                }
            }
            else
            {
                // Swap out
                if ( swap_step.isSwapIn () )
                {
                    return false;
                }
            }
        }

        return true;
    }




    /**
     * <p>
     * Returns the direction of this swap:
     * <code> SwapOperation.Direction.IN </code> for swapping in,
     * <code> SwapOperation.Direction.OUT </code> for swapping out.
     * </p>
     *
     * @return The direction of swapping (in or out).  Never null.
     *
     * Final for a little extra speed.
     */
    public final SwapOperation.Direction direction ()
    {
        return this.direction;
    }


    /**
     * <p>
     * Returns true if this is a swap-in, false if this is a swap-out.
     * </p>
     *
     * @return True if this is a swap in operation, false otherwise.
     *
     * Final for a little extra speed.
     */
    public final boolean isSwapIn ()
    {
        if ( this.direction.equals ( SwapOperation.Direction.IN ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * <p>
     * Returns the Pages being swapped from in this operation.
     * </p>
     *
     * <p>
     * During a swap-in, this returns the swapped-out pages.
     * During a swap-out, this returns the swapped-in pages.
     * </p>
     *
     * @return The pages being swapped from.  Never null.
     *         Never contains any null elements.  May be 0-length.
     */
    public Page [] sourcePages ()
    {
        final Set<Page> pages = new HashSet<Page> ();
        final boolean is_swap_in = this.isSwapIn ();
        for ( SwapStep swap_step : this.swapSteps )
        {
            final Page page;
            if ( is_swap_in )
            {
                page = swap_step.outPage ();
            }
            else
            {
                page = swap_step.inPage ();
            }

            pages.add ( page );
        }

        Page [] template = new Page [ pages.size () ];
        Page [] pages_array = pages.toArray ( template );

        return pages_array;
    }


    /**
     * <p>
     * Performs this swap operation without locking anything.
     * </p>
     *
     * <p>
     * Generally you should lock something to make sure this
     * operation is executed atomically.
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
        for ( SwapStep swap_step : this.swapSteps )
        {
            swap_step.swap ( credentials );
        }
    }


    /**
     * <p>
     * Returns the individual swap steps comprising this SwapOperation.
     * </p>
     *
     * @return This operation's individual atomic swap steps.
     *         Never null.  Never contains any null elements.
     *         May be 0-length.
     */
    public SwapStep [] swapSteps ()
    {
        SwapStep [] swap_steps = new SwapStep [ this.swapSteps.length ];
        System.arraycopy ( this.swapSteps, 0,
                           swap_steps, 0, swap_steps.length );
        return swap_steps;
    }


    /**
     * <p>
     * Returns the Pages being swapped to in this operation.
     * </p>
     *
     * <p>
     * During a swap-in, this returns the swapped-in pages.
     * During a swap-out, this returns the swapped-out pages.
     * </p>
     *
     * @return The pages being swapped to.  Never null.
     *         Never contains any null elements.  May be 0-length.
     */
    public Page [] targetPages ()
    {
        final Set<Page> pages = new HashSet<Page> ();
        final boolean is_swap_in = this.isSwapIn ();
        for ( SwapStep swap_step : this.swapSteps )
        {
            final Page page;
            if ( is_swap_in )
            {
                page = swap_step.inPage ();
            }
            else
            {
                page = swap_step.outPage ();
            }

            pages.add ( page );
        }

        Page [] template = new Page [ pages.size () ];
        Page [] pages_array = pages.toArray ( template );

        return pages_array;
    }
}
