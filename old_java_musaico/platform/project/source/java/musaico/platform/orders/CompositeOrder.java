package musaico.platform.order;


import java.io.Serializable;

import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Order based on two or more delegate orders, such as
 * "order by X then by Y then by Z".
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
 * Copyright (c) 2012, 2013 Johann Tienhaara
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
public class CompositeOrder<ORDER extends Object>
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


    /** The delegate orders.  Each one is used in turn to compare
     *  values, until one returns a non-equal comparison, then the
     *  result is returned. */
    private final Order<ORDER> [] orders;

    /** Checks method parameter obligations and return value guarantees. */
    private final ObjectContracts objectContracts;


    /**
     * <p>
     * Creates a new CompositeOrder with the specified delegate orders.
     * </p>
     *
     * <p>
     * For example, to create an order which compares first using
     * some order x, then by some order y, then by some order z,
     * until one of them returns a non-equal comparison:
     * </p>
     *
     * <pre>
     *     Order<Foo> x = ...;
     *     Order<Foo> y = ...;
     *     Order<Foo> z = ...;
     *     Order<Foo> composite = new CompositeOrder<Foo> ( x, y, z );
     * </pre>
     *
     * @param orders The delegate orders to compare with.
     *               Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings ( { "unchecked", // Heap pollution from generic array.
                "rawtypes" } ) // Java insists on raw array constructor.
    public CompositeOrder (
                           Order<ORDER> ... orders
                           )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        super ( createIDFrom ( orders ) );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object []) orders );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               orders );

        this.orders = new Order [ orders.length ];
        System.arraycopy ( orders, 0,
                           this.orders, 0, orders.length );

        this.objectContracts = new ObjectContracts ( this );
    }


    /**
     * <p>
     * Creates a unique ID for this composite order from the specified
     * orders.
     * </p>
     */
    private static String createIDFrom (
                                        Order<?> [] orders
                                        )
    {
        if ( orders == null )
        {
            return "{ null }";
        }

        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "{" );
        boolean is_first = true;
        for ( Order<?> order : orders )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            sbuf.append ( " " + order );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }


    /**
     * @see musaico.foundation.io.Order#compareValues(java.lang.Object,java.lang.Object)
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

        for ( Order<ORDER> order : orders )
        {
            Comparison comparison = order.compareValues ( left, right );
            if ( ! comparison.equals ( Comparison.LEFT_EQUALS_RIGHT ) )
            {
                return comparison;
            }

            // Otherwise keep comparing...
        }

        // All orders returned equal.
        return Comparison.LEFT_EQUALS_RIGHT;
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
        else if ( ! ( obj instanceof CompositeOrder ) )
        {
            return false;
        }

        final CompositeOrder<?> that = (CompositeOrder<?>) obj;
        // Paranoid safety check:
        if ( this.orders == null )
        {
            if ( that.orders == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.orders == null )
        {
            return false;
        }

        if ( this.orders.length != that.orders.length )
        {
            return false;
        }

        for ( int o = 0; o < this.orders.length; o ++ )
        {
            if ( ! this.orders [ o ].equals ( that.orders [ o ] ) )
            {
                return false;
            }
        }

        // Everything is matchy-matchy.
        return true;
    }
}
