package musaico.foundation.io;

import java.io.Serializable;


import musaico.foundation.i18n.Internationalized;

import musaico.foundation.i18n.exceptions.L10n;

import musaico.foundation.i18n.message.Message;
import musaico.foundation.i18n.message.SimpleMessageBuilder;

import musaico.foundation.time.Time;


/**
 * <p>
 * Compares Times.
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
public class TimeOrder
    extends AbstractOrder<Time>
    implements Serializable
{
    /**
     * <p>
     * Creates a new TimeOrder.
     * </p>
     */
    public TimeOrder ()
    {
        super ( "Time order" );
    }


    /**
     * @see musaico.foundation.io.Order#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    public final Comparison compareValues (
                                           Time left,
                                           Time right
                                           )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                // null == null.
                return Comparison.LEFT_EQUALS_RIGHT;
            }

            // null > any Time.
            return Comparison.INCOMPARABLE_LEFT;
        }
        else if ( right == null )
        {
            // any Time < null.
            return Comparison.INCOMPARABLE_RIGHT;
        }

        // any Time < / == / > any Time.
        int comparator_value = left.compareTo ( right );
        Comparison comparison =
            Comparison.fromComparatorValue ( comparator_value );

        return comparison;
    }
}
