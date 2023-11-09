package musaico.kernel.memory.paging;


import java.io.Serializable;


import musaico.io.AbstractOrder;
import musaico.io.Comparison;

import musaico.region.Position;


/**
 * <p>
 * Compares Pages by their start positions.
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
 * Copyright (c) 2011, 2012 Johann Tienhaara
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
public class PageOrder
    extends AbstractOrder<Page>
    implements Serializable
{
    /**
     * <p>
     * Creates a new PageOrder.
     * </p>
     */
    public PageOrder ()
    {
        super ( "Page order" );
    }


    /**
     * @see musaico.io.Order#compareValues(java.lang.Object,java.lang.Object)
     */
    public Comparison compareValues (
                                     Page left,
                                     Page right
                                     )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                // null == null.
                return Comparison.LEFT_EQUALS_RIGHT;
            }
            else
            {
                // null > any Page.
                return Comparison.LEFT_GREATER_THAN_RIGHT;
            }
        }
        else if ( right == null )
        {
            // any Page < null.
            return Comparison.LEFT_LESS_THAN_RIGHT;
        }
        else if ( ! left.region ().space ().equals ( right.region ().space () ) )
        {
            // Can't compare positions from 2 completely different spaces.
            return Comparison.INCOMPARABLE_RIGHT;
        }

        Position left_start = left.region ().start ();
        Position right_start = right.region ().start ();

        return left.region ().space ().order ()
            .compareValues ( left_start, right_start );
    }
}
