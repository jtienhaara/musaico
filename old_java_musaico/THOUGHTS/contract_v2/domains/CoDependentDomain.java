package musaico.foundation.contract.domains;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.Collection;


import musaico.foundation.contract.Domain;
import musaico.foundation.contract.ArrayDomain;


/**
 * <p>
 * A Domain which can vary from array to array, but which must
 * remain constant throughout elements of the array.
 * </p>
 *
 * <p>
 * For example "an array of integers which is either all positive, or
 * all negative".  If the first element in the array is GreaterThanZero,
 * then all of the remaining elements must be GreaterThanZero as well, or
 * the array is not part of this domain.  Conversely if the
 * first element of the array is LessThanZero, then the remaining elements
 * must also be LessThanZero.
 * </p>
 *
 *
 * <p>
 * In Java every Domain must be Serializable in order to play nicely
 * over RMI.
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
public class CoDependentDomain
    implements Domain<Object /* array, Collection, single object */>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130929L;
    private static final String serialVersionHash =
        "0x2600A7291E75B0A22FB0238159518DF17A83B90F";


    /** The domains which are allowed for elements of arrays in this
     *  domain.  For example, GreaterThanZero and LessThanZero for
     *  arrays which are all either greater than zero or less than zero,
     *  but not mixed. */
    private final Domain<? extends Object> [] domains;


    /**
     * <p>
     * Creates a new CoDependentDomain for arrays which can be
     * made up of elements all of the same domain, from any one of
     * the domains specified.
     * </p>
     *
     * @param domains The domains allowed for array elements.
     *                Each element of an array must be a member of
     *                the same single domain in order for the array
     *                to be valid for this overall domain.  Mixing
     *                elements from <code> domain [ 0 ] </code>
     *                and elements from <code> domain [ 1 ] </code>
     *                is invalid.  Must contain at least 2 Domains.
     *                Must not be null.  Must not contain
     *                any null elements.
     *
     * @throws IllegalArgumentException If any of the domains is invalid
     *                                  (see above).
     */
    @SuppressWarnings("unchecked") // Possible heap pollution / generic varargs
    public CoDependentDomain (
                              Domain<? extends Object> ... domains
                              )
        throws IllegalArgumentException
    {
        boolean is_legal = true;
        if ( domains == null )
        {
            is_legal = false;
        }
        else if ( domains.length < 2 )
        {
            is_legal = false;
        }
        else
        {
            for ( Domain<? extends Object> domain : domains )
            {
                if ( domain == null )
                {
                    is_legal = false;
                    break;
                }
            }
        }

        if ( ! is_legal )
        {
            final StringBuilder sbuf = new StringBuilder ();
            if ( domains == null )
            {
                sbuf.append ( "null" );
            }
            else
            {
                sbuf.append ( "{" );
                boolean is_first = true;
                for ( Domain<? extends Object> domain : domains )
                {
                    if ( is_first )
                    {
                        is_first = false;
                    }
                    else
                    {
                        sbuf.append ( "," );
                    }

                    sbuf.append ( " " + domain );
                }

                if ( ! is_first )
                {
                    sbuf.append ( " " );
                }

                sbuf.append ( "}" );
            }

            throw new IllegalArgumentException ( "Cannot create a CoDependentDomain from domains " + sbuf );
        }

        this.domains = new Domain<?> [ domains.length ];
        System.arraycopy ( domains, 0,
                           this.domains, 0, domains.length );
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
            Domain<? extends Object> domain = null;
            boolean is_valid = true;
            for ( int a = 0; a < length; a ++ )
            {
                Object element = Array.get ( value, a );
                if ( domain == null )
                {
                    // First element.  Set the domain.
                    domain = this.findDomain ( element );
                    if ( domain == null )
                    {
                        // The first element is invalid.  We're done.
                        is_valid = false;
                        break;
                    }
                }
                else
                {
                    // Subsequent element.  Make sure it's in the same domain.
                    is_valid = this.isMember ( element, domain );
                    if ( ! is_valid )
                    {
                        // This element is not in the right domain.
                        break;
                    }
                }
            }

            return is_valid;
        }
        else if ( value instanceof Collection<?> )
        {
            Collection<?> collection = (Collection<?>) value;
            Domain<? extends Object> domain = null;
            boolean is_valid = true;
            for ( Object element : collection )
            {
                if ( domain == null )
                {
                    // First element.  Set the domain.
                    domain = this.findDomain ( element );
                    if ( domain == null )
                    {
                        // The first element is invalid.  We're done.
                        is_valid = false;
                        break;
                    }
                }
                else
                {
                    // Subsequent element.  Make sure it's in the same domain.
                    is_valid = this.isMember ( element, domain );
                    if ( ! is_valid )
                    {
                        // This element is not in the right domain.
                        break;
                    }
                }
            }

            return is_valid;
        }
        else
        {
            // A single element.  As long as it is a member of one
            // of the domains, it's OK.
            final Domain<? extends Object> domain = this.findDomain ( value );
            if ( domain == null )
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }


    /**
     * @return The first domain matching the specified value, or null
     *         if the value is not in any of the domains.
     */
    private Domain<? extends Object> findDomain (
                                                 Object value
                                                 )
    {
        for ( Domain<? extends Object> domain : this.domains )
        {
            if ( isMember ( value, domain ) )
            {
                return domain;
            }
        }

        // The specified value is not a member of any of the domains.
        return null;
    }


    /**
     * @return True if the specified value is a member of the specified
     *         Domain, false if not.
     */
    @SuppressWarnings("unchecked") // We do check the cast, with a catch().
    private <SUB_DOMAIN_TYPE extends Object>
        boolean isMember (
                          Object value,
                          Domain<SUB_DOMAIN_TYPE> domain
                          )
    {
        if ( value == null )
        {
            return false;
        }

        final boolean is_member;
        try
        {
            SUB_DOMAIN_TYPE sub_domain_value =
                (SUB_DOMAIN_TYPE) value;
            is_member = domain.isValid ( sub_domain_value );
        }
        catch ( ClassCastException e )
        {
            // Clearly the value is not a member of the domain, since
            // it isn't even the right class.
            return false;
        }

        return is_member;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "{" );
        boolean is_first = true;
        for ( Domain<? extends Object> domain : this.domains )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            sbuf.append ( " " + domain );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return "all elements must belong to the same domain, any one of "
            + sbuf.toString ();
    }
}
