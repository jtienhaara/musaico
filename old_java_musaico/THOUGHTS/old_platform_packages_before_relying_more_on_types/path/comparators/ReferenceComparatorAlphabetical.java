package musaico.foundation.io.comparators;


import java.util.Comparator;


import musaico.foundation.io.Reference;


/**
 * <p>
 * Compares References using some particular comparison policy.
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
 * Copyright (c) 2009 Johann Tienhaara
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
public class ReferenceComparatorAlphabetical
    implements Comparator<Reference>
{
    /**
     * @see java.util.Comparator#compare( T, T )
     */
    public int compare (
                        Reference ref1,
                        Reference ref2
                        )
    {
        if ( ref1 == null
             && ref2 == null )
        {
            return 0;
        }
        else if ( ref1 == null )
        {
            // Null comes last.
            return ReferenceComparators.REF1_GREATER_THAN_REF2;
        }
        else if ( ref1 == null )
        {
            // Null comes last.
            return ReferenceComparators.REF1_LESS_THAN_REF2;
        }
        else
        {
            String str1 = "" + ref1;
            String str2 = "" + ref2;

            return str1.compareTo ( str2 );
        }
    }
}
