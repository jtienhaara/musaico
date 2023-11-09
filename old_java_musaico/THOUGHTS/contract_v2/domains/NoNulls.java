package musaico.foundation.contract.domains;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.Collection;


import musaico.foundation.contract.ArrayDomain;


/**
 * <p>
 * The domain of all arrays and collections which do not contain any null
 * elements.
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
public class NoNulls
    implements ArrayDomain<Object /* array, Collection */>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130321L;
    private static final String serialVersionHash =
        "0x2EA80D505F4888901C6A7F10AAE4DB10917B8973";


    /** The NoNulls domain singleton. */
    public static final NoNulls DOMAIN =
        new NoNulls ();


    protected NoNulls ()
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
            for ( int a = 0; a < length; a ++ )
            {
                if ( Array.get ( value, a ) == null )
                {
                    return false;
                }
            }

            return true;
        }
        else if ( value instanceof Collection<?> )
        {
            Collection<?> collection = (Collection<?>) value;
            for ( Object element : collection )
            {
                if ( element == null )
                {
                    return false;
                }
            }

            return true;
        }
        else
        {
            // Not an array with no nulls, not a collection with
            // no nulls.
            return false;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "no nulls";
    }
}
