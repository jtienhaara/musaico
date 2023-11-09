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
public class ReferenceComparators
{
    /** ref1 is less than (comes before) ref2. */
    public static final int REF1_LESS_THAN_REF2 = -1;

    /** ref1 is equal to ref2. */
    public static final int REF1_EQUAL_TO_REF2 = 0;

    /** ref1 is greater than (comes after) ref2. */
    public static final int REF1_GREATER_THAN_REF2 = 1;


    /** Compares References by String alphabetical comparisons. */
    public static final Comparator<Reference> ALPHABETICAL =
        new ReferenceComparatorAlphabetical ();


    /** No need to create instances of this class. */
    private ReferenceComparators ()
    {
    }
}
