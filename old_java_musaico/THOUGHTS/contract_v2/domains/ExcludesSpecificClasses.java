package musaico.foundation.contract.domains;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.Collection;


import musaico.foundation.contract.ArrayDomain;


/**
 * <p>
 * The domain of all arrays and collections which do not contain any
 * instances of one or more specific classes.
 * </p>
 *
 * <p>
 * For example, the domain of all arrays and collections of
 * Numbers which do not include any BigDecimals.
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
public class ExcludesSpecificClasses
    implements ArrayDomain<Object /* array, Collection */>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130928L;
    private static final String serialVersionHash =
        "0x8DD0E29F900C09985171487FFE476E54F9CB0635";


    /** The classes which are not allowed among the elements of
     *  values of this domain. */
    private final Class<?> [] excludedClasses;


    /**
     * <p>
     * Creates a new ExcludesSpecificClasses for the specified
     * classes.
     * </p>
     *
     * <p>
     * Only arrays and Collections which do NOT contain any instances
     * of the specified classes will be members of this domain.
     * </p>
     *
     * @param excluded_classes The classes to exclude from this domain.
     *                         Must not be null.  Must not contain any
     *                         null elements.
     */
    public ExcludesSpecificClasses (
                                    Class<?> ... excluded_classes
                                    )
    {
        boolean is_valid = true;
        if ( excluded_classes == null
             || excluded_classes.length == 0 )
        {
            is_valid = false;
        }
        else
        {
            for ( Class<?> excluded_class : excluded_classes )
            {
                if ( excluded_class == null )
                {
                    is_valid = false;
                    break;
                }
            }

        }

        if ( ! is_valid )
        {
            throw new IllegalArgumentException ( "Cannot create an ExcludesSpecificClasses from classes { " + classesToString ( excluded_classes ) + " }" );
        }

        this.excludedClasses = new Class<?> [ excluded_classes.length ];
        System.arraycopy ( excluded_classes, 0,
                           this.excludedClasses, 0, excluded_classes.length );
    }


    /**
     * <p>
     * Makes a nice human-readable String out of an (possibly null)
     * array of classes.
     * </p>
     */
    private String classesToString (
                                    Class<?> [] excluded_classes
                                    )
    {
        if ( excluded_classes == null )
        {
            return "";
        }

        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first_class = true;
        for ( Class<?> excluded_class : excluded_classes )
        {
            if ( is_first_class )
            {
                is_first_class = false;
            }
            else
            {
                sbuf.append ( ", " );
            }

            if ( excluded_class == null )
            {
                sbuf.append ( "null" );
            }
            else
            {
                sbuf.append ( "" + excluded_class.getSimpleName () );
            }
        }

        return sbuf.toString ();
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
                final Object element = Array.get ( value, a );
                for ( Class<?> excluded_class : this.excludedClasses )
                {
                    if ( excluded_class.isInstance ( element ) )
                    {
                        return false;
                    }
                }
            }

            return true;
        }
        else if ( value instanceof Collection<?> )
        {
            Collection<?> collection = (Collection<?>) value;
            for ( Object element : collection )
            {
                for ( Class<?> excluded_class : this.excludedClasses )
                {
                    if ( excluded_class.isInstance ( element ) )
                    {
                        return false;
                    }
                }
            }

            return true;
        }
        else
        {
            // Not an array, not a collection.
            // Treat as a single object.
            for ( Class<?> excluded_class : this.excludedClasses )
            {
                if ( excluded_class.isInstance ( value ) )
                {
                    return false;
                }
            }

            return true;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "exclude specific classes { "
            + classesToString ( this.excludedClasses )
            + " }";
    }
}
