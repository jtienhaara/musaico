package musaico.foundation.contract.domains;

import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.contract.Domain;


/**
 * <p>
 * A class whose child classes include all the standard Domains
 * defined by Musaico.
 * </p>
 *
 * <p>
 * Useful for making parameter-specific obligation classes and
 * return value guarantee classes.
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
public class AllDomains
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20131007L;
    private static final String serialVersionHash =
        "0x6DF5B268FF2D47504CEB89350EEC3B059A898090";


    /**
     * <p>
     * For serial version hashing only.
     * </p>
     */
    protected AllDomains ()
    {
    }


    /** @see musaico.foundation.contract.domains.GreaterThanZero */
    public static class GreaterThanZero
        extends musaico.foundation.contract.domains.GreaterThanZero
    { private static final long serialVersionUID =
            AllDomains.serialVersionUID; }
    // For serial version hashing the whole AllDomains class:
    private final GreaterThanZero greaterThanZero =
        new GreaterThanZero ();


    /** @see musaico.foundation.contract.domains.GreaterThanOrEqualToZero */
    public static class GreaterThanOrEqualToZero
        extends musaico.foundation.contract.domains.GreaterThanOrEqualToZero
    { private static final long serialVersionUID =
            AllDomains.serialVersionUID; }
    // For serial version hashing the whole AllDomains class:
    private final GreaterThanOrEqualToZero greaterThanOrEqualToZero =
        new GreaterThanOrEqualToZero ();

    /** @see musaico.foundation.contract.domains.LessThanZero */
    public static class LessThanZero
        extends musaico.foundation.contract.domains.LessThanZero
    { private static final long serialVersionUID =
            AllDomains.serialVersionUID; }
    // For serial version hashing the whole AllDomains class:
    private final LessThanZero lessThanZero =
        new LessThanZero ();


    /** @see musaico.foundation.contract.domains.LessThanOrEqualToZero */
    public static class LessThanOrEqualToZero
        extends musaico.foundation.contract.domains.LessThanOrEqualToZero
    { private static final long serialVersionUID =
            AllDomains.serialVersionUID; }
    // For serial version hashing the whole AllDomains class:
    private final LessThanOrEqualToZero lessThanOrEqualToZero =
        new LessThanOrEqualToZero ();


    /** @see musaico.foundation.contract.domains.BoundedDomain */
    public static class BoundedDomain<BOUNDED extends Object>
        extends musaico.foundation.contract.domains.BoundedDomain<BOUNDED>
    { private static final long serialVersionUID =
            AllDomains.serialVersionUID;
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

        public BoundedDomain ( Comparator<BOUNDED> comparator,
                               BOUNDED minimum,
                               BOUNDED maximum )
            throws IllegalArgumentException
        {
            super ( comparator, minimum, maximum );
        }
    }
    // For serial version hashing the whole AllDomains class:
    private final BoundedDomain<Integer> boundedDomain =
        BoundedDomain.over ( 1, 2 );


    /** @see musaico.foundation.contract.domains.ContainsElements */
    public static class ContainsElements
        extends musaico.foundation.contract.domains.ContainsElements
    { private static final long serialVersionUID =
            AllDomains.serialVersionUID;
        public ContainsElements ( Serializable ... elements )
        {
            super ( elements );
        }
    }
    // For serial version hashing the whole AllDomains class:
    private final ContainsElements containsElements =
        new ContainsElements ( "1", 2, 3F );


    /** @see musaico.foundation.contract.domains.ExcludesElements */
    public static class ExcludesElements
        extends musaico.foundation.contract.domains.ExcludesElements
    { private static final long serialVersionUID =
            AllDomains.serialVersionUID;
        public ExcludesElements ( Serializable ... elements )
        {
            super ( elements );
        }
    }
    // For serial version hashing the whole AllDomains class:
    private final ExcludesElements excludesElements =
        new ExcludesElements ( "1", 2, 3F );


    /** @see musaico.foundation.contract.domains.ExcludesSpecificClasses */
    public static class ExcludesSpecificClasses
        extends musaico.foundation.contract.domains.ExcludesSpecificClasses
    { private static final long serialVersionUID =
            AllDomains.serialVersionUID;
        public ExcludesSpecificClasses ( Class<?> ... excluded_classes )
        {
            super ( excluded_classes );
        }
    }
    // For serial version hashing the whole AllDomains class:
    private final ExcludesSpecificClasses excludesSpecificClasses =
        new ExcludesSpecificClasses ( String.class, Number.class );


    /** @see musaico.foundation.contract.domains.NoDuplicates */
    public static class NoDuplicates
        extends musaico.foundation.contract.domains.NoDuplicates
    { private static final long serialVersionUID =
            AllDomains.serialVersionUID; }
    // For serial version hashing the whole AllDomains class:
    private final NoDuplicates noDuplicates =
        new NoDuplicates ();


    /** @see musaico.foundation.contract.domains.NoNulls */
    public static class NoNulls
        extends musaico.foundation.contract.domains.NoNulls
    { private static final long serialVersionUID =
            AllDomains.serialVersionUID; }
    // For serial version hashing the whole AllDomains class:
    private final NoNulls noNulls =
        new NoNulls ();


    /** @see musaico.foundation.contract.domains.CoDependentDomain */
    public static class CoDependentDomain
        extends musaico.foundation.contract.domains.CoDependentDomain
    { private static final long serialVersionUID =
            AllDomains.serialVersionUID;
        public CoDependentDomain ( Domain<?> ... domains )
        {
            super ( domains );
        }
    }
    // For serial version hashing the whole AllDomains class:
    private final CoDependentDomain coDependentDomain =
        new CoDependentDomain ( greaterThanZero, lessThanZero );
}
