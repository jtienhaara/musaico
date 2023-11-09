package musaico.foundation.contract.domains;

import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.contract.Domain;


/**
 <p>
 * A domain of objects which must be within { minimum, maximum }
 * bounds, inclusive.
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add new domains to AllDomains.java! ***
 * </p>
 *
 *
 * <p>
 * In Java every Domain must be Serializable in order to play nicely
 * across RMI.
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
public class BoundedDomain<BOUNDED extends Object>
    implements Domain<BOUNDED>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130929L;
    private static final String serialVersionHash =
        "0x4E8EA496600801DA048EF41340F8A2B734170E4D";


    /** The comparator used to compare possible domain values to
     *  the minimum and maximum values. */
    private final Comparator<BOUNDED> comparator;

    /** The minimum value allowed for any value in this domain.
     *  All values must be greater than or equal to this minimum. */
    private final BOUNDED minimum;

    /** The maximum value allowed for any value in this domain.
     *  All values must be less than or equal to this maximum. */
    private final BOUNDED maximum;


    /**
     * <p>
     * Creates a new BoundedDomain for the specified naturally
     * ordered class of objects, with the specified minimum and
     * maximum values.
     * </p>
     *
     * @param minimum The minimum value for objects in this domain.
     *                Must not be null.
     *
     * @param maximum The maximum value for objects in this domain.
     *                Must not be null.
     *
     * @throws IllegalArgumentException If the specified minimum is
     *                                  greater than the specified
     *                                  maximum according to the
     *                                  natural order.
     */
    public static <NATURALLY_ORDERED extends Comparable<NATURALLY_ORDERED>>
        BoundedDomain<NATURALLY_ORDERED> over (
                                               NATURALLY_ORDERED minimum,
                                               NATURALLY_ORDERED maximum
                                               )
        throws IllegalArgumentException
    {
        return new BoundedDomain<NATURALLY_ORDERED> ( new NaturalOrderComparator<NATURALLY_ORDERED> (),
                                                      minimum, maximum );
    }


    /**
     * <p>
     * Creates a new BoundedDomain using the specified comparator,
     * with the specified minimum and maximum values for objects
     * in the new domain.
     * </p>
     *
     * @param comparator The comparator to use when checking bounds.
     *                   Must not be null.
     *
     * @param minimum The minimum value for objects in this domain.
     *                Must not be null.
     *
     * @param maximum The maximum value for objects in this domain.
     *                Must not be null.
     *
     * @throws IllegalArgumentException If the specified minimum is
     *                                  greater than the specified
     *                                  maximum according to the
     *                                  specified comparator, or if any
     *                                  of the parameters is null.
     */
    public BoundedDomain (
                          Comparator<BOUNDED> comparator,
                          BOUNDED minimum,
                          BOUNDED maximum
                          )
        throws IllegalArgumentException
    {
        if ( comparator == null
             || minimum == null
             || maximum == null
             || comparator.compare ( minimum, maximum ) > 0 )
        {
            throw new IllegalArgumentException ( "Cannot create a BoundedDomain with comparator "
                                                + comparator
                                                + " minimum value "
                                                + minimum
                                                + " maximum value "
                                                + maximum );
        }

        this.comparator = comparator;
        this.minimum = minimum;
        this.maximum = maximum;
    }


    /**
     * @see musaico.foundation.contract.Domain#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid (
                            BOUNDED value
                            )
    {
        if ( value == null )
        {
            return false;
        }

        if ( this.comparator.compare ( value, this.minimum ) < 0 )
        {
            // Too low, out of bounds.
            return false;
        }
        else if ( this.comparator.compare ( value, this.maximum ) > 0 )
        {
            // Too high, out of bounds.
            return false;
        }

        // In bounds.
        return true;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "between " + this.minimum + " and " + this.maximum;
    }
}
