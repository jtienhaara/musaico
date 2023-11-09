package musaico.region;

import java.io.Serializable;


import musaico.io.AbstractOrder;
import musaico.io.Comparison;
import musaico.io.Order;
import musaico.io.ReverseOrder;


/**
 * <p>
 * Provides an order for Regions of a common space,
 * comparing first by starting index, then by ending index,
 * then by size (number of indices between start and end,
 * which is lower for sparse regions than for filled ones).
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
public class RegionOrder
    extends AbstractOrder<Region>
    implements Serializable
{
    /** The default region order (by start, then by end, then
     *  by largest size first). */
    public static final RegionOrder DEFAULT =
        new RegionOrder ( "Region order" );


    /**
     * <p>
     * Creates a new RegionOrder with the specified
     * internationalized description text.
     * </p>
     *
     * <p>
     * Make sure you put internationalized_description_text
     * into your Messages.properties file, and translate it
     * for other locales!
     * </p>
     *
     * @param internationalized_description_text The international
     *                                           identifier for the
     *                                           description message
     *                                           for this order.
     *                                           This text will be
     *                                           internationalized, and
     *                                           then the Messages.properties
     *                                           files will be searched any
     *                                           time the description of
     *                                           this Order is localized
     *                                           (for example, by a client
     *                                           application).
     *                                           Must not be null.
     */
    public RegionOrder (
                        String internationalized_description_text
                        )
    {
        super ( internationalized_description_text );
    }


    /**
     * @see musaico.io.Order#compareValues(java.io.Serializable,java.io.Serializable)
     */
    public Comparison compareValues (
                                     Region left,
                                     Region right
                                     )
    {
        if ( ! left.space ().equals ( right.space () ) )
        {
            return Comparison.INCOMPARABLE_RIGHT;
        }

        Position left_start = left.start ();
        Position right_start = right.start ();
        Order<Position> position_order = left.space ().order ();
        Comparison start_comparison =
            position_order.compareValues ( left_start, right_start );
        if ( ! start_comparison.equals ( Comparison.LEFT_EQUALS_RIGHT ) )
        {
            // left.start != right.start
            return start_comparison;
        }

        // left.start = right.start.
        // Compare left.end to right.end.
        Position left_end = left.end ();
        Position right_end = right.end ();
        Comparison end_comparison =
            position_order.compareValues ( left_end, right_end );
        if ( ! end_comparison.equals ( Comparison.LEFT_EQUALS_RIGHT ) )
        {
            // left.end != right.end
            return end_comparison;
        }

        // left.start = right.start
        // left.end = right.end
        // Compare left.size to right.size.
        // Sparse regions may be smaller than full ones.
        Size left_size = left.size ();
        Size right_size = right.size ();
        Order<Size> size_order =
            new ReverseOrder<Size> ( left_size.order () );
        Comparison size_comparison =
            size_order.compareValues ( left_size,
                                       right_size );
        return size_comparison;
    }
}
