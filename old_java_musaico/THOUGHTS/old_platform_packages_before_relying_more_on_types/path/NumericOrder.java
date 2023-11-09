package musaico.foundation.io;

import java.io.Serializable;

import java.math.BigDecimal;
import java.math.BigInteger;


import musaico.foundation.i18n.Internationalized;

import musaico.foundation.i18n.exceptions.L10n;

import musaico.foundation.i18n.message.Message;
import musaico.foundation.i18n.message.SimpleMessageBuilder;


/**
 * <p>
 * Compares numbers.
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
public class NumericOrder
    extends AbstractOrder<Number>
    implements Serializable
{
    /**
     * <p>
     * Creates a new NumericOrder.
     * </p>
     */
    public NumericOrder ()
    {
        super ( "Numeric order" );
    }


    /**
     * @see musaico.foundation.io.Order#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    public final Comparison compareValues (
                                           Number left,
                                           Number right
                                           )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                // null == null.
                return Comparison.LEFT_EQUALS_RIGHT;
            }

            // null > any Number.
            return Comparison.INCOMPARABLE_LEFT;
        }
        else if ( right == null )
        {
            // any Number < null.
            return Comparison.INCOMPARABLE_RIGHT;
        }

        final int comparator_value;
        if ( ( left instanceof Float )
             || ( left instanceof Double ) )
        {
            Double left_double = new Double ( left.doubleValue () );
            Double right_double = new Double ( right.doubleValue () );
            comparator_value = left_double.compareTo ( right_double );
        }
        else if ( ( left instanceof BigDecimal )
                  && ( right instanceof BigDecimal ) )
        {
            BigDecimal left_big_decimal = (BigDecimal) left;
            BigDecimal right_big_decimal = (BigDecimal) right;
            comparator_value = left_big_decimal.compareTo ( right_big_decimal );
        }
        else if ( ( left instanceof BigInteger )
                  && ( right instanceof BigInteger ) )
        {
            BigInteger left_big_integer = (BigInteger) left;
            BigInteger right_big_integer = (BigInteger) right;
            comparator_value = left_big_integer.compareTo ( right_big_integer );
        }
        else
        {
            // Treat everything else as a long.
            Long left_long = new Long ( left.longValue () );
            Long right_long = new Long ( right.longValue () );
            comparator_value = left_long.compareTo ( right_long );
        }

        // any Number < / == / > any Number.
        Comparison comparison =
            Comparison.fromComparatorValue ( comparator_value );

        return comparison;
    }
}
