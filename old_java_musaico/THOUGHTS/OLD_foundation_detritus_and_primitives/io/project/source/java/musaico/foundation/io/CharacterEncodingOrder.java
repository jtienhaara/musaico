package musaico.foundation.io;

import java.io.Serializable;


import musaico.foundation.i18n.Internationalized;

import musaico.foundation.i18n.exceptions.L10n;

import musaico.foundation.i18n.message.Message;
import musaico.foundation.i18n.message.SimpleMessageBuilder;


/**
 * <p>
 * Compares Strings by character encoding value
 * (such as their ASCII numeric character values).
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
public class CharacterEncodingOrder
    extends AbstractOrder<String>
    implements Serializable
{
    /**
     * <p>
     * Creates a new CharacterEncodingOrder.
     * </p>
     */
    public CharacterEncodingOrder ()
    {
        super ( "Character encoding order" );
    }


    /**
     * @see musaico.foundation.io.Order#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    public final Comparison compareValues (
                                           String left,
                                           String right
                                           )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                // null == null.
                return Comparison.LEFT_EQUALS_RIGHT;
            }

            // null > any String.
            return Comparison.INCOMPARABLE_LEFT;
        }
        else if ( right == null )
        {
            // any String < null.
            return Comparison.INCOMPARABLE_RIGHT;
        }

        // any String < / == / > any String.
        int comparator_value = left.compareTo ( right );
        Comparison comparison =
            Comparison.fromComparatorValue ( comparator_value );

        return comparison;
    }
}
