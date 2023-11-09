package musaico.platform.order;


import java.io.Serializable;


import musaico.foundation.i18n.Internationalized;

import musaico.foundation.i18n.exceptions.L10n;

import musaico.foundation.i18n.message.Message;
import musaico.foundation.i18n.message.SimpleMessageBuilder;


/**
 * <p>
 * Compares Paths, each level of the path being compared
 * by some other Order (such as Order.DICTIONARY).
 * </p>
 *
 * <p>
 * The namespace of each path is ignored.
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
public class PathOrder
    extends AbstractOrder<Path>
    implements Serializable
{
    /** The default Path order, comparing each level of two Paths
     *  by Dictionary order. */
    public static final Order<Path> DEFAULT =
        new PathOrder ( Order.DICTIONARY );


    /** The String order for comparing the same level of two Paths. */
    private final Order<String> levelOrder;


    /**
     * <p>
     * Creates a PathOrder where each level of the paths
     * is compared by the specified Order.
     * </p>
     *
     * @param level_order The String Order to use when comparing
     *                       each level of two Paths, such as
     *                       Order.DICTIONARY.  Must not be null.
     */
    public PathOrder (
                      Order<String> level_order
                      )
    {
        super ( "Path order" );

        this.levelOrder = level_order;
    }


    /**
     * @see musaico.foundation.io.Order#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    public final Comparison compareValues (
                                           Path left,
                                           Path right
                                           )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                // null == null.
                return Comparison.LEFT_EQUALS_RIGHT;
            }

            // null > any Path.
            return Comparison.INCOMPARABLE_LEFT;
        }
        else if ( right == null )
        {
            // any Path < null.
            return Comparison.INCOMPARABLE_RIGHT;
        }

        String [] left_levels = left.names ();
        String [] right_levels = right.names ();

        for ( int l = 0; l < left_levels.length; l ++ )
        {
            if ( l >= right_levels.length )
            {
                // /a/b/c > /a/b.
                return Comparison.LEFT_GREATER_THAN_RIGHT;
            }

            final Comparison comparison =
                this.levelOrder.compareValues ( left_levels [ l ],
                                                right_levels [ l ] );
            if ( ! comparison.equals ( Comparison.LEFT_EQUALS_RIGHT ) )
            {
                // .../X/... < / == / > .../Y/....
                return comparison;
            }
        }

        if ( left_levels.length < right_levels.length )
        {
            // /a/b < /a/b/c.
            return Comparison.LEFT_LESS_THAN_RIGHT;
        }

        // /a/b/c == /a/b/c.
        return Comparison.LEFT_EQUALS_RIGHT;
    }
}
