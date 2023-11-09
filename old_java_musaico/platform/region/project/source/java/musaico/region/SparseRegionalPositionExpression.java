package musaico.region;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Comparison;
import musaico.io.Order;


/**
 * <p>
 * Manipulates positions and creates sizes and
 * regions from them.
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
public class SparseRegionalPositionExpression
    extends RegionalPositionExpression
    implements Serializable
{
    /** The position from which the expression started. */
    private final Position position;

    /** The region in which to perform the operations.
     *  Can be a Space.all () region (everything). */
    private final SparseRegion sparseRegion;


    /**
     * <p>
     * Creates a new PositionExpression, taking the
     * specified term as the start of the expression.
     * </p>
     *
     * @param position The position from which to start
     *                 the expression.  Must not be null.
     *
     * @param sparse_region The sparse region over which to perform
     *                      calculations for this expression.
     *                      Must not be null.  Must have the
     *                      same Space as the specified Position.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      specified are invalid
     *                                      (see above).
     */
    public SparseRegionalPositionExpression (
                                             Position position,
                                             SparseRegion sparse_region
                                             )
        throws I18nIllegalArgumentException
    {
        // Throws exception if invalid position or region.
        super ( position, sparse_region );

        this.position = position;
        this.sparseRegion = sparse_region;
    }


    /**
     * @see musaico.region.PositionExpression#next()
     */
    @Override
    public Position next ()
    {
        Position next_position =
            this.position.space ().expr ( this.position ).next ();
        Search search =
            this.sparseRegion.search ( new SpecificPosition ( this.position ) );
        long sub_region_index = search.findSubRegionIndex ();
        if ( sub_region_index >= 0L )
        {
            return next_position;
        }

        // A negative number means the specified position would be
        // in a sub-region inserted at ( -1 - index ).
        // What we want instead is the starting position of the
        // sub-region that's already there.
        sub_region_index = -1L - sub_region_index;
        if ( sub_region_index >= this.sparseRegion.numRegions () )
        {
            return this.position.space ().outOfBounds ();
        }

        Region sub_region = this.sparseRegion.region ( sub_region_index );
        return sub_region.start ();
    }


    /**
     * @see musaico.region.PositionExpression#previous()
     */
    @Override
    public Position previous ()
    {
        Position previous_position =
            this.position.space ().expr ( this.position ).previous ();
        Search search =
            this.sparseRegion.search ( new SpecificPosition ( this.position ) );
        long sub_region_index = search.findSubRegionIndex ();
        if ( sub_region_index >= 0L )
        {
            return previous_position;
        }

        // A negative number means the specified position would be
        // in a sub-region inserted at ( -1 - index ).
        // What we want instead is the ending position of the
        // PREVIOUS sub-region that's already there.
        sub_region_index = -1L - sub_region_index;
        sub_region_index --;
        if ( sub_region_index < 0L )
        {
            return this.position.space ().outOfBounds ();
        }

        Region sub_region = this.sparseRegion.region ( sub_region_index );
        return sub_region.end ();
    }
}
