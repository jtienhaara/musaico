package musaico.foundation.contract.domains;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


import musaico.foundation.contract.ArrayDomain;


/**
 * <p>
 * The domain of all arrays and collections which do NOT contain a specific
 * subset of elements.
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
public class ExcludesElements
    implements ArrayDomain<Object /* array, Collection */>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20131007L;
    private static final String serialVersionHash =
        "0xD171CEE1E6A7C4990F072A87BCCCBA1CF3C7BD24";


    /** The elements which form a subset of every array / collection
     *  in this domain. */
    private final Serializable [] elements;


    /**
     * <p>
     * Creates a new domain of arrays/collections which do not contain
     * any of the specified elements.
     * </p>
     *
     * @param elements The elements which must be excluded from every
     *                 array/collection in this domain.  Must not be null.
     */
    public ExcludesElements (
                             Serializable ... elements
                             )
        throws IllegalArgumentException
    {
        if ( elements == null )
        {
            throw new IllegalArgumentException ( "Cannot create a ExcludesElements ( " + elements + " )" );
        }

        this.elements = new Serializable [ elements.length ];
        System.arraycopy ( elements, 0,
                           this.elements, 0, elements.length );
    }


    /**
     * @see musaico.foundation.contract.Domain#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid (
                            Object value
                            )
    {
        boolean found_null = false;
        final Set<Object> found;
        if ( value == null )
        {
            return false;
        }
        else if ( value.getClass ().isArray () )
        {
            int length = Array.getLength ( value );
            found = new HashSet<Object> ();
            for ( int a = 0; a < length; a ++ )
            {
                Object element = Array.get ( value, a );
                if ( element == null )
                {
                    found_null = true;
                }
                else
                {
                    found.add ( element );
                }
            }
        }
        else if ( value instanceof Set<?> )
        {
            // No point in checking, we're just going to create
            // a HashSet to check (below) anyway.
            found = new HashSet<Object> ( (Set<?>) value );
            found_null = found.contains ( null );
        }
        else if ( value instanceof Collection<?> )
        {
            Collection<?> collection = (Collection<?>) value;
            found = new HashSet<Object> ();
            for ( Object element : collection )
            {
                if ( element == null )
                {
                    found_null = true;
                }
                else
                {
                    found.add ( element );
                }
            }
        }
        else
        {
            // Not an array containing the specified elements,
            // not a collection with the specified elements.
            return false;
        }

        for ( Serializable element : this.elements )
        {
            if ( element == null )
            {
                if ( found_null )
                {
                    return false;
                }
            }
            else if ( found.contains ( element ) )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "{" );
        boolean is_first_element = true;
        for ( Serializable element : this.elements )
        {
            if ( is_first_element )
            {
                is_first_element = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            sbuf.append ( " " + element );
        }

        if ( ! is_first_element )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return "excludes " + sbuf.toString ();
    }
}
