package musaico.foundation.io;


/**
 * <p>
 * The result of some comparison between objects.
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
public enum Comparison
{
    /** The left operand is less than the right operand
     *  (this &lt; that). */
    LEFT_LESS_THAN_RIGHT ( -1 ),

    /** The left and right operands are equal
     *  (this == that). */
    LEFT_EQUALS_RIGHT ( 0 ),

    /** The left operand is greater than the right operand
     *  (this &gt; that). */
    LEFT_GREATER_THAN_RIGHT ( 1 ),

    /** The left and right operands are not even comparable
     *  (this != that).  As far as sorting goes, the left
     *  operand (this) is considered incomparable, so it will
     *  be sorted to the end, AFTER right. */
    INCOMPARABLE_LEFT ( Integer.MAX_VALUE ),

    /** The left and right operands are not even comparable
     *  (this != that).  As far as sorting goes, the right
     *  operand (that) is considered incomparable, so it will
     *  be sorted to the end, AFTER left. */
    INCOMPARABLE_RIGHT ( Integer.MIN_VALUE );


    /** The java Comparator / Comparable equivalent value. */
    private final int comparatorValue;


    /**
     * <p>
     * Creates a new Comparison with the specified Java comparator value.
     * </p>
     *
     * @param comparator_value The value to convert this to / from
     *                         for a Java Comparetor.compare () equivalent.
     *                         Can be any number.
     */
    private Comparison (
                        int comparator_value
                        )
    {
        this.comparatorValue = comparator_value;
    }


    /**
     * @return The Comparable.compareTo () or Comparator.compare ()
     *         equivalent of this Comparison, or a kludge if nothing
     *         is quite equivalent.
     */
    public int comparatorValue ()
    {
        return this.comparatorValue;
    }


    /**
     * <p>
     * Returns true if this Comparison is any of the ones specified.
     * </p>
     *
     * @param comparisons The values to match this Comparison against.
     *                    Must not be null.  Must not contain any null
     *                    elements.
     */
    public boolean isIn (
                         Comparison ... comparisons
                         )
    {
        for ( Comparison that : comparisons )
        {
            if ( this.equals ( that ) )
            {
                return true;
            }
        }

        return false;
    }




    /**
     * <p>
     * Given the specified result from Comparable.compareTo () or
     * Comparator.compare (), returns the best matching Comparison.
     * </p>
     *
     * @param comparator_value The Java compareTo () or compare ()
     *                         result.  Can be any number.
     */
    public static Comparison fromComparatorValue (
                                                  int comparator_value
                                                  )
    {
        if ( comparator_value == Integer.MAX_VALUE )
        {
            return INCOMPARABLE_LEFT;
        }
        else if ( comparator_value == Integer.MIN_VALUE )
        {
            return INCOMPARABLE_RIGHT;
        }
        else if ( comparator_value < 0 )
        {
            return LEFT_LESS_THAN_RIGHT;
        }
        else if ( comparator_value > 0 )
        {
            return LEFT_GREATER_THAN_RIGHT;
        }
        else
        {
            return LEFT_EQUALS_RIGHT;
        }
    }
}
