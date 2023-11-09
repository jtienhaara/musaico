package musaico.region.search;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Comparison;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Search;
import musaico.region.Criterion;
import musaico.region.Space;
import musaico.region.SparseRegion;


/**
 * <p>
 * Performs a linear search for one or more Position(s) in
 * a Region.
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
public class LinearSearch
    implements Search, Serializable
{
    /** The region to search through. */
    private final Region region;

    /** The criterion to search by. */
    private final Criterion criterion;


    /**
     * <p>
     * Creates a new LinearSearch with the specified criterion.
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
    public LinearSearch (
                         Region region,
                         Criterion criterion
                         )
        throws I18nIllegalArgumentException
    {
        if ( region == null
             || criterion == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a [%search_class%] through region [%region%] with criterion [%criterion%]",
                                                     "search_class", this.getClass ().getSimpleName (),
                                                     "region", region,
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

        for ( Position position : region )
        {
            Criterion criterion = this.criterion ();
            Comparison comparison = criterion.compare ( position );
            if ( comparison == Comparison.LEFT_EQUALS_RIGHT )
            {
                // Found it.
                return position;
            }
            else if ( comparison != Comparison.LEFT_LESS_THAN_RIGHT )
            {
                // Searched too far but didn't find it, or came
                // across an incomparable position.
                return region.space ().outOfBounds ();
            }
            // Else Comprison.LEFT_LESS_THAN_RIGHT: keep searching.
        }

        // Reached the end of the region and didn't find it.
        return region.space ().outOfBounds ();
    }


    /**
     * @see musaico.region.Search#findAll()
     */
    @Override
    public Region findAll ()
    {
        final Region region = this.region ();

        // Find the starting point of the matching region.
        Position start = this.find ();
        final Position out_of_bounds = region.space ().outOfBounds ();
        if ( start.equals ( out_of_bounds ) )
        {
            // No matching Positions.
            return region.space ().empty ();
        }

        // Now find the ending point of the matching region.
        Position end = start;
        for ( Position current = region.expr ( end ).next ();
              ! current.equals ( out_of_bounds );
              current = region.expr ( current ).next () )
        {
            Criterion criterion = this.criterion ();
            Comparison comparison = criterion.compare ( end );
            if ( comparison == Comparison.LEFT_EQUALS_RIGHT )
            {
                // Found another matching Position.
                end = current;
            }
            else
            {
                // Ran past the end of the matching region.
                break;
            }
        }

        return region.space ().region ( start, end );
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

        Space space = sparse_region.space ();
        Region empty = space.empty ();
        long num_regions = sparse_region.numRegions ();
        for ( long r = 0; r < num_regions; r ++ )
        {
            Region sub_region = sparse_region.region ( r );

            Position start = sub_region.start ();
            Criterion criterion = this.criterion ();
            Comparison start_comparison = criterion.compare ( start );
            if ( ! start_comparison.isIn ( Comparison.LEFT_LESS_THAN_RIGHT,
                                           Comparison.LEFT_EQUALS_RIGHT ) )
            {
                // Before this sub-region.
                return -1L - r;
            }

            Position end = sub_region.end ();
            Comparison end_comparison = criterion.compare ( end );
            if ( end_comparison.isIn ( Comparison.LEFT_GREATER_THAN_RIGHT,
                                       Comparison.LEFT_EQUALS_RIGHT ) )
            {
                // Found it.
                return r;
            }

            // After this sub-region, keep looking.
        }

        // Didn't find it, so it should go after the end.
        return -1L - num_regions;
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
