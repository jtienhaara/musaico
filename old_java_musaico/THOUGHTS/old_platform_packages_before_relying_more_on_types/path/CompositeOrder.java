package musaico.foundation.io;


import java.io.Serializable;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;

import musaico.foundation.i18n.message.SimpleMessageBuilder;


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
public class CompositeOrder<ORDER extends Object>
    extends AbstractOrder<ORDER>
    implements Serializable
{
    /** The delegate orders.  Each one is used in turn to compare
     *  values, until one returns a non-equal comparison, then the
     *  result is returned. */
    private final Order<ORDER> [] orders;


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
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public CompositeOrder (
                           Order<ORDER> ... orders
                           )
    {
        super ( new SimpleMessageBuilder ()
                .text ( "Composite order by: [%orders%]" )
                .parameter ( "orders", orders )
                .build () );

        boolean is_invalid_args = false;
        if ( orders == null )
        {
            is_invalid_args = true;
        }
        else
        {
            for ( Order<ORDER> order : orders )
            {
                if ( order == null )
                {
                    is_invalid_args = true;
                    break;
                }
            }
        }

        if ( is_invalid_args )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a CompositeOrder with orders [%orders%]",
                                                     "orders", orders );
        }

        this.orders = orders;
    }


    /**
     * @see musaico.foundation.io.Order#compareValues(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    public final Comparison compareValues (
                                           ORDER left,
                                           ORDER right
                                           )
        throws I18nIllegalArgumentException
    {
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
}
