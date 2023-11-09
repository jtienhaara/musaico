package musaico.foundation.io;

import java.io.Serializable;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;



/**
 * <p>
 * A Range of ordered objects.
 * </p>
 *
 * <p>
 * For example, the following creates a range of increasing integers
 * from 11 to 32 (inclusive):
 * </p>
 *
 * <pre>
 *     Range<Integer> integer_range =
 *         new Range<Integer> ( 11,
 *                              32,
 *                              Order.NUMERIC );
 * </pre>
 *
 * <p>
 * The Range does not know how to step through the elements in
 * between the start and end of the range.  It is only used
 * to test whether a given object is within the bounds of the range.
 * </p>
 *
 * <p>
 * For example, using the integer range above, the following
 * will test whether 10, 11, 18, 32, 33 and 42 are in range:
 * </p>
 *
 * <pre>
 *     boolean is_in_range;
 *     is_in_range = integer_range.contains ( 10 ); // false
 *     is_in_range = integer_range.contains ( 11 ); // true
 *     is_in_range = integer_range.contains ( 18 ); // true
 *     is_in_range = integer_range.contains ( 32 ); // true
 *     is_in_range = integer_range.contains ( 33 ); // false
 *     is_in_range = integer_range.contains ( 42 ); // false
 * </p>
 *
 * <p>
 * In Java, every Range is Serializable in order to play nicely
 * over RMI.
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
public class Range<POINT extends Serializable>
    implements Serializable
{
    /** The lower bound of the range. */
    private final POINT lower;

    /** The upper bound of the range. */
    private final POINT upper;

    /** The order by which to compare the endpoints to
     *  values which might be in the middle of this range. */
    private final Order<POINT> order;


    /**
     * <p>
     * Creates a new Range from the specified lower bound to
     * the specified upper bound, using the specified Order
     * to compare values to the range.
     * </p>
     *
     * @param lower The lower bound of the new Range.  Must not
     *              be null.  Must be less than or equal to
     *              upper acccording to the specified order.
     *
     * @param upper The upper bound of the new Range.  Must not
     *              be null.  Must be greater than or equal to
     *              lower acccording to the specified order.
     *
     * @param order The order by which to compare values,
     *              such as Order.DICTIONARY or Order.NUMERIC
     *              and so on.
     *
     * @throws I18nIllegalArgumentException If any parameter is
     *                                      invalid (see above).
     */
    public Range (
                  POINT lower,
                  POINT upper,
                  Order<POINT> order
                  )
    {
        boolean is_valid_parameter_set = true;
        if ( lower == null
             || upper == null
             || order == null )
        {
            is_valid_parameter_set = false;
        }

        Comparison left_comparison = order.compareValues ( lower, upper );
        Comparison right_comparison = order.compareValues ( upper, lower );

        if ( left_comparison.equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            if ( ! right_comparison.equals ( Comparison.LEFT_GREATER_THAN_RIGHT ) )
            {
                // If left < right then can't have right <= left.
                is_valid_parameter_set = false;
            }
        }
        else if ( left_comparison.equals ( Comparison.LEFT_EQUALS_RIGHT ) )
        {
            if ( ! right_comparison.equals ( Comparison.LEFT_EQUALS_RIGHT ) )
            {
                // If left == right then can't have right != left.
                is_valid_parameter_set = false;
            }
        }
        else
        {
            // Can't have right < left.
            is_valid_parameter_set = false;
        }

        if ( ! is_valid_parameter_set )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a Range from [%lower%] to [%upper%] with order [%order%]",
                                                     "lower", lower,
                                                     "upper", upper,
                                                     "order", order );
        }

        this.lower = lower;
        this.upper = upper;
        this.order = order;
    }


    /**
     * <p>
     * Returns true if the specified value is within range.
     * </p>
     *
     * @param value The value to compare to this range.
     *              Must not be null.
     *
     * @return True if the specified value is greater than or equal
     *         to the lower bound of this range, according to the
     *         order, and also less than or equal to the upper bound
     *         of this range, according to the order.  False if the
     *         specified value is not in range.
     */
    public boolean contains (
                             POINT value
                             )
    {
        if ( value == null )
        {
            return false;
        }

        Comparison lower_comparison =
            this.order ().compareValues ( value, this.lower () );
        if ( ! lower_comparison.equals ( Comparison.LEFT_EQUALS_RIGHT )
             && ! lower_comparison.equals ( Comparison.LEFT_GREATER_THAN_RIGHT ) )
        {
            return false;
        }

        Comparison upper_comparison =
            this.order ().compareValues ( value, this.upper () );
        if ( ! upper_comparison.equals ( Comparison.LEFT_EQUALS_RIGHT )
             && ! upper_comparison.equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            return false;
        }

        return true;
    }


    /**
     * <p>
     * Returns the lower bound of this range.
     * </p>
     *
     * @return This range's lower bound.  Never null.
     */
    public POINT lower ()
    {
        return this.lower;
    }


    /**
     * <p>
     * Returns the order defining how points are compared
     * to the endpoints of this range.
     * </p>
     *
     * @return This range's order.  Never null.
     */
    public Order<POINT> order ()
    {
        return this.order;
    }


    /**
     * <p>
     * Returns the upper bound of this range.
     * </p>
     *
     * @return This range's upper bound.  Never null.
     */
    public POINT upper ()
    {
        return this.upper;
    }
}
