package musaico.foundation.contract.domains;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


import musaico.foundation.contract.ArrayDomain;


/**
 * <p>
 * The domain of all arrays and collections which do not contain any
 * duplicate elements.
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
public class NoDuplicates
    implements ArrayDomain<Object /* array, Collection */>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130321L;
    private static final String serialVersionHash =
        "0x4F1565104797F1AE8F4FCD77C69E007D96BC89F4";


    /** The NoDuplicates domain singleton. */
    public static final NoDuplicates DOMAIN =
        new NoDuplicates ();


    protected NoDuplicates ()
    {
    }


    /**
     * @see musaico.foundation.contract.Domain#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid (
                            Object value
                            )
    {
        if ( value == null )
        {
            return false;
        }
        else if ( value.getClass ().isArray () )
        {
            int length = Array.getLength ( value );
            Set<Object> unique_elements = new HashSet<Object> ();
            boolean has_null = false;
            for ( int a = 0; a < length; a ++ )
            {
                Object element = Array.get ( value, a );
                if ( element == null )
                {
                    if ( has_null )
                    {
                        return false;
                    }

                    has_null = true;
                }
                else
                {
                    if ( unique_elements.contains ( element ) )
                    {
                        return false;
                    }

                    unique_elements.add ( element );
                }
            }

            return true;
        }
        else if ( value instanceof HashSet<?> )
        {
            // No point in checking, we're just going to create
            // a HashSet to check (below) anyway.
            return true;
        }
        else if ( value instanceof Collection<?> )
        {
            Collection<?> collection = (Collection<?>) value;
            Set<Object> unique_elements = new HashSet<Object> ();
            boolean has_null = false;
            for ( Object element : collection )
            {
                if ( element == null )
                {
                    if ( has_null )
                    {
                        return false;
                    }

                    has_null = true;
                }
                else
                {
                    if ( unique_elements.contains ( element ) )
                    {
                        return false;
                    }

                    unique_elements.add ( element );
                }
            }

            return true;
        }
        else
        {
            // Not an array with no duplicates, not a collection with
            // no duplicates.  If you think double negatives are fun,
            // you're not wrong!
            return false;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "no duplicate";
    }
}
