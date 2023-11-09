package musaico.region.time;

import java.io.Serializable;


import musaico.io.AbstractOrder;
import musaico.io.Comparison;
import musaico.io.Order;

import musaico.region.Size;


/**
 * <p>
 * Compares and sorts TimeSizes.
 * </p>
 *
 *
 * <p>
 * In Java, every Order must be Serializable in order to play
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
public class TimeSizeOrder
    extends AbstractOrder<Size>
    implements Serializable
{
    /** The default TimeSize order (ascending by length). */
    public static final TimeSizeOrder DEFAULT =
        new TimeSizeOrder ( "Duration order" );


    /**
     * <p>
     * Creates a new TimeSizeOrder with the specified
     * human-readable description of this order.
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
    public TimeSizeOrder (
                          String internationalized_description_text
                          )
    {
        super ( internationalized_description_text );
    }


    /**
     * @see musaico.io.Order#compareValues(java.lang.Object,java.lang.Object)
     */
    public Comparison compareValues (
                                     Size left_size,
                                     Size right_size
                                     )
    {
        if ( ! ( left_size instanceof TimeSize ) )
        {
            return Comparison.INCOMPARABLE_LEFT;
        }
        else if ( ! ( right_size instanceof TimeSize ) )
        {
            return Comparison.INCOMPARABLE_RIGHT;
        }

        TimeSize left = (TimeSize) left_size;
        TimeSize right = (TimeSize) right_size;

        return Order.TIME.compareValues ( left.duration (),
                                          right.duration () );
    }
}
