package musaico.foundation.io;


import java.io.Serializable;


import musaico.foundation.i18n.Internationalized;

import musaico.foundation.i18n.exceptions.L10n;

import musaico.foundation.i18n.message.Message;
import musaico.foundation.i18n.message.SimpleMessageBuilder;


/**
 * <p>
 * Compares objects in reverse order.
 * </p>
 *
 * <p>
 * Whenever the wrapped order returns <code> LEFT_LESS_THAN_RIGHT </code>
 * the result is reversed to <code> LEFT_GREATER_THAN_RIGHT </code>,
 * and vice-versa.
 * </p>
 *
 * <p>
 * All other comparisons (such as <code> INCOMPARABLE_LEFT </code>)
 * are returned as-is.
 * </p>
 *
 *
 * <p>
 * In Java, every Order is Serializable in order to play nicely
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
public class ReverseOrder<ORDER extends Serializable>
    extends AbstractOrder<ORDER>
    implements Serializable
{
    /** The base order to reverse. */
    private final Order<ORDER> order;


    /**
     * <p>
     * Creates a ReverseOrder where the specified order
     * is used to compare values, and whenever left is less than
     * or greater than right, the comparison is reversed.
     * </p>
     *
     * <p>
     * For example, a numeric order might compare 3 and 5
     * with <code> Comparison.LEFT_LESS_THAN_RIGHT </code>.
     * By wrapping the same numeric comparison in a ReverseOrder,
     * the result from the same comparison will be
     * <code> Comparison.LEFT_GREATER_THAN_RIGHT </code>.
     * </p>
     *
     * @param order The forward order, which will be reversed.
     *              Must not be null.
     */
    public ReverseOrder (
                         Order<ORDER> order
                         )
    {
        super ( new SimpleMessageBuilder ()
                .text ( "Reverse [%order_description%]" )
                .parameter ( "order_description", order.description () )
                .build () );

        this.order = order;
    }


    /**
     * @see musaico.foundation.io.Order#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    public final Comparison compareValues (
                                           ORDER left,
                                           ORDER right
                                           )
    {
        Comparison base_comparison =
            this.order.compareValues ( left, right );
        switch ( base_comparison )
        {
        case LEFT_LESS_THAN_RIGHT:
            return Comparison.LEFT_GREATER_THAN_RIGHT;

        case LEFT_GREATER_THAN_RIGHT:
            return Comparison.LEFT_LESS_THAN_RIGHT;

        default:
            return base_comparison;
        }
    }
}
