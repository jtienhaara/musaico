package musaico.platform.order;


import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


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
 * Copyright (c) 2011, 2013 Johann Tienhaara
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
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20131006L;
    private static final String serialVersionHash =
        "0xDB99F857427F0ACBE060FE5082C9E229A06E1D54";


    /** Checks constructor and static method parameters for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( CompositeOrder.class );


    /** The base order to reverse. */
    private final Order<ORDER> order;

    /** Checks method parameter obligations and return value guarantees. */
    private final ObjectContracts objectContracts;


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
        throws ParametersMustNotBeNull.Violation
    {
        super ( "Reverse " + order );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               order );

        this.order = order;

        this.objectContracts = new ObjectContracts ( this );
    }


    /**
     * @see musaico.foundation.io.Order#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    public final Comparison compareValues (
                                           ORDER left,
                                           ORDER right
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.objectContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                     left, right );

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


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( ! super.equals ( obj ) )
        {
            return false;
        }
        else if ( ! ( obj instanceof ReverseOrder ) )
        {
            return false;
        }

        final ReverseOrder<?> that = (ReverseOrder<?>) obj;
        if ( ! this.order.equals ( that.order ) )
        {
            return false;
        }

        // Everything is matchy-matchy.
        return true;
    }
}
