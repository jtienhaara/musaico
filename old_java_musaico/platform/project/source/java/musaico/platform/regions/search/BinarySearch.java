package musaico.region.search;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Comparison;

import musaico.region.Criterion;
import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Search;
import musaico.region.Size;
import musaico.region.Space;
import musaico.region.SparseRegion;


/**
 * <p>
 * Performs a binary search for one specific Position in
 * a Region.
 * </p>
 *
 * <p>
 * If the search criterion passed into a BinarySearch matches multiple
 * Positions in the Region then the results of the binary
 * search will be non-deterministic!  Be sure to use a valid
 * search criterion (such as a simple Position criterion to search for one
 * specific position).
 * </p>
 *
 *
 * <p>
 * In Java, every Search must be Serializable in order to play
 * nicely over RMI.
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
 * Copyright (c) 2011, 2012 Johann Tienhaara
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
public class BinarySearch
    implements Search, Serializable
{
    /** The region to search through. */
    private final Region region;

    /** The criterion to search by. */
    private final Criterion criterion;


    /**
     * <p>
     * Creates a new BinarySearch with the specified criterion.
     * </p>
     *
     * @param region The region to search through.  Must not be null.
     *
     * @param criterion The criterion to search for,
     *                  such as a specific position.
     *                  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public BinarySearch (
                         Region region,
                         Criterion criterion
                         )
        throws I18nIllegalArgumentException
    {
        if ( region == null
             || criterion == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a [%search_class%] with criterion [%criterion%]",
                                                     "search_class", this.getClass ().getSimpleName (),
                                                     "criterion", criterion );
        }

        this.region = region;
        this.criterion = criterion;
    }


    /**
     * @see musaico.region.Search#criterion()
     */
    @Override
    public Criterion criterion ()
    {
        return this.criterion;
    }


    /**
     * @see musaico.region.Search#find()
     */
    @Override
    public Position find ()
    {
        final Region region = this.region ();

        Position low = region.start ();
        Position high = region.end ();
        Space space = region.space ();
        Position found = space.outOfBounds ();
        Criterion criterion = this.criterion ();
        while ( space.order ().compareValues ( low, high ).isIn ( Comparison.LEFT_LESS_THAN_RIGHT,
                                                                  Comparison.LEFT_EQUALS_RIGHT ) )
        {
            Size half_size = space.expr ( high )
                .subtract ( low )
                .multiply ( 0.5D ).size ();
            Position mid = space.expr ( low ).add ( half_size ).position ();
            if ( ! region.contains ( mid ) )
            {
                // The midpoint fell in the hole of a sparse
                // region.  Step it up by 1 to get out of
                // the hole.
                mid = region.expr ( mid ).next ();
            }

            Comparison comparison = criterion.compare ( mid );
            if ( comparison == Comparison.LEFT_GREATER_THAN_RIGHT )
            {
                // mid > criterion.
                // Search the 1st 1/2 of remaining positions.
                high = region.expr ( mid ).previous ();
            }
            else if ( comparison == Comparison.LEFT_LESS_THAN_RIGHT )
            {
                // mid < criterion.
                // Search the latter 1/2 of remaining positions.
                low = region.expr ( mid ).next ();
            }
            else if ( comparison == Comparison.LEFT_EQUALS_RIGHT )
            {
                // mid == criterion.
                found = mid;
                break;
            }
            else
            {
                // Incomparable.  Not much more we can do!
                return space.outOfBounds ();
            }

            criterion = this.criterion ();
        }

        // Either we found the position, or it's still set
        // to outOfBounds ().
        return found;
    }


    /**
     * @see musaico.region.Search#findAll()
     */
    @Override
    public Region findAll ()
    {
        final Region region = this.region ();

        // Binary search can't be used to find more than one position.
        Position found_position = this.find ();
        Space space = found_position.space ();
        if ( found_position.equals ( space.outOfBounds () ) )
        {
            return space.empty ();
        }

        return space.region ( found_position, found_position );
    }




    /**
     * @see musaico.region.Search#findSubRegion()
     */
    @Override
    public long findSubRegionIndex ()
    {
        final Region region = this.region ();
        if ( ! ( region instanceof SparseRegion ) )
        {
            // Not a sparse region?  Then why ask for
            // subregions in the first place.
            return Long.MIN_VALUE;
        }

        final SparseRegion sparse_region = (SparseRegion) region;

        long num_regions = sparse_region.numRegions ();
        long low = 0L;
        long high = num_regions - 1L;
        while ( low <= high )
        {
            long mid = ( low + high ) / 2L;
            Region sub_region = sparse_region.region ( mid );
            Criterion criterion = this.criterion ();
            Comparison start_comparison =
                criterion.compare ( sub_region.start () );
            if ( start_comparison == Comparison.LEFT_GREATER_THAN_RIGHT )
            {
                // mid.start () > criterion.
                // Search the 1st 1/2 of remaining positions.
                high = mid - 1L;
                continue;
            }
            else if ( start_comparison == Comparison.LEFT_EQUALS_RIGHT )
            {
                // mid.start () == criterion.
                return mid;
            }
            else if ( start_comparison != Comparison.LEFT_LESS_THAN_RIGHT )
            {
                // mid.start () != criterion (incomparable etc).
                return -1L;
            }

            Comparison end_comparison =
                criterion.compare ( sub_region.end () );
            if ( end_comparison == Comparison.LEFT_LESS_THAN_RIGHT )
            {
                // mid.end () < criterion.
                // Search the latter 1/2 of remaining positions.
                low = mid + 1L;
                continue;
            }
            else if ( end_comparison == Comparison.LEFT_EQUALS_RIGHT )
            {
                // mid.end () == criterion.
                return mid;
            }
            else if ( end_comparison != Comparison.LEFT_GREATER_THAN_RIGHT )
            {
                // mid.end () != criterion (incomparable etc).
                return -1L;
            }

            // At this point we know that
            // mid.start () < criterion < mid.end ()
            // So this is the sub-region.
            return mid;
        }

        // Somewhere in between low and high.
        // Note that at this point low > high.
        // The non-existent sub-region would fit just
        // before the low'th sub-region.
        return -1L - low;
    }


    /**
     * @see musaico.region.Search#region()
     */
    @Override
    public Region region ()
    {
        return this.region;
    }
}
