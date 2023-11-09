package musaico.foundation.contract.domains;

import java.io.Serializable;

import java.util.Comparator;


/**
 * <p>
 * A Comparator which simply uses the natural order of Comparable
 * objects to make comparisons.
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
 * Copyright (c) 2013 Johann Tienhaara
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
public class NaturalOrderComparator<NATURALLY_ORDERED extends Comparable<NATURALLY_ORDERED>>
    implements Comparator<NATURALLY_ORDERED>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130929L;
    private static final String serialVersionHash =
        "0xF7F6F4F4A3AB1D4D895F61904AF3054129BCA71F";


    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
        public int compare (
                            NATURALLY_ORDERED left,
                            NATURALLY_ORDERED right
                            )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                // null == null
                return 0;
            }
            else
            {
                // null > any object.
                return Integer.MAX_VALUE;
            }
        }
        else if ( right == null )
        {
            // any object < null.
            return Integer.MIN_VALUE + 1;
        }

        return left.compareTo ( right );
    }
}
