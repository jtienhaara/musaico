package musaico.kernel.task;

import java.io.Serializable;


import musaico.io.Comparison;
import musaico.io.AbstractOrder;


/**
 * <p>
 * Compares Priorities.
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
public class PriorityOrder
    extends AbstractOrder<Priority>
    implements Serializable
{
    /** The standard priority order. */
    public static final PriorityOrder STANDARD =
        new PriorityOrder ();


    /**
     * <p>
     * Creates a new PriorityOrder.
     * </p>
     */
    public PriorityOrder ()
    {
        this ( "Priority order" );
    }


    /**
     * <p>
     * Creates a new PriorityOrder with the specified
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
    protected PriorityOrder (
                             String internationalized_description_text
                             )
    {
        super ( internationalized_description_text );
    }


    /**
     * @see musaico.io.Order#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    public final Comparison compareValues (
                                           Priority left,
                                           Priority right
                                           )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                // null == null.
                return Comparison.LEFT_EQUALS_RIGHT;
            }

            // null > any Priority.
            return Comparison.INCOMPARABLE_LEFT;
        }
        else if ( right == null )
        {
            // any Priority < null.
            return Comparison.INCOMPARABLE_RIGHT;
        }

        // any Priority < / == / > any Priority.
        Long left_id = left.id ();
        Long right_id = right.id ();

        // Lower id = higher priority.
        int comparator_value = 0 - left_id.compareTo ( right_id );
        Comparison comparison =
            Comparison.fromComparatorValue ( comparator_value );

        return comparison;
    }
}
