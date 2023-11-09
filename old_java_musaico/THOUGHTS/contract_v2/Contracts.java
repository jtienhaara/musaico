package musaico.foundation.contract;

import java.io.Serializable;


/**
 * <p>
 * System-wide contract rules singleton: when to inspect a contract, when
 * to enforce it.
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
 * Copyright (c) 2012, 2013 Johann Tienhaara
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
public class Contracts
{
    /**
     * <p>
     * Returns true if all of the specified data belong to the
     * specified Domain.
     * </p>
     *
     * <p>
     * Returns true if either:
     * </p>
     *
     * <ul>
     *   <li>
     *     The specified data is a single object (not an
     *     array), and <code> domain.isValid ( data ) </code>
     *     returns true; or
     *   </li>
     *   <li>
     *     The specified data is either an array or an Iterable,
     *     and the specified domain is an ArrayDomain, and
     *     <code> domain.isValid ( data ) </code> returns true; or
     *   </li>
     *   <li>
     *     The specified data is either an array or an Iterable,
     *     and for every element <code> element </code> of the array or
     *     Iterable, <code> domain.isValid ( element ) </code> returns true.
     *   </li>
     * </ul>
     *
     * <p>
     * Otherwise the specified data does not belong entirely to the
     * specified Domain, so false is returned.
     * </p>
     *
     * @param data The data which is to be checked against
     *             the specified domain.  Can be null.
     *             Can contain null elements.
     *
     * @param domain The domain to check the specified data against.
     *               Must not be null.
     *
     * @return True if the specified data is entirely within the specified
     *         Domain; false if the whole or any part does not belong
     *         to the Domain.
     */
    public <DOMAIN_OBJECT extends Object>
        boolean isInDomain (
                            Object data,
                            Domain<DOMAIN_OBJECT> domain
                            )
    {
        // If array, test every element of the array.
        // Special case: if the Domain is an ArrayDomain, then pass
        // the entire array of data as one object.
        boolean is_in_domain = true;
        if ( data.getClass ().isArray ()
             && ! ( domain instanceof ArrayDomain ) )
        {
            // Array of data to check, one at a time.
            int length = Array.getLength ( data );
            for ( int e = 0; e < length; e ++ )
            {
                final DOMAIN_OBJECT element;
                try
                {
                    element = (DOMAIN_OBJECT) Array.get ( data, e );
                }
                catch ( ClassCastException cce )
                {
                    is_in_domain = false;
                    break;
                }

                if ( ! domain.isValid ( element ) )
                {
                    is_in_domain = false;
                    break;
                }
            }
        }
        else if ( data instanceof Iterable
                  && ! ( domain instanceof ArrayDomain ) )
        {
            // Iterable of data to check, one at a time.
            final Iterable<?> iterable = (Iterable<?>) data;
            for ( Object object : iterable )
            {
                final DOMAIN_OBJECT element;
                try
                {
                    element = (DOMAIN_OBJECT) object;
                }
                catch ( ClassCastException cce )
                {
                    is_in_domain = false;
                    break;
                }

                if ( ! domain.isValid ( element ) )
                {
                    is_in_domain = false;
                    break;
                }
            }
        }
        else
        {
            // One single inspectable to check (not an array/iterable, or the
            // whole array/iterable is treated as one inspectable object by
            // an ArrayDomain).
            if ( ! domain.isValid ( inspectable_data ) )
            {
                is_in_domain = false;
            }
        }

        return is_in_domain;
    }


    /**
     * <p>
     * Makes an array of objects serializable.
     * </p>
     *
     * <p>
     * The default implementation casts each non-Serializable object to
     * a String, and leaves each Serializable object alone.
     * </p>
     *
     * @param objects The objects to make Serializable.
     *                Must not be null.  Can contain null elements.
     *
     * @return The serializable version of the array.  Never null.
     */
    public static Serializable [] makeArrayOfSerializables (
                                                            Object [] objects
                                                            )
    {
        Serializable [] serializables =
            new Serializable [ objects.length ];
        for ( int s = 0; s < serializables.length; s ++ )
        {
            serializables [ s ] =
                Contracts.makeSerializable ( objects [ s ] );
        }

        return serializables;
    }


    /**
     * <p>
     * Makes an object serializable.
     * </p>
     *
     * <p>
     * The default implementation casts a non-Serializable object to
     * a String, and leaves a Serializable object alone.
     * </p>
     *
     * @param object The object to make Serializable.
     *               Can be null.
     *
     * @return The serializable version of the array.  Never null.
     */
    public static Serializable makeSerializable (
                                                 Object object
                                                 )
    {
        final Serializable serializable;
        if ( object == null )
        {
            serializable = "NULL";
        }
        else if ( object instanceof Object [] )
        {
            Object [] sub_array = (Object []) object;
            serializable = makeArrayOfSerializables ( sub_array );
        }
        else if ( object instanceof Serializable )
        {
            serializable = (Serializable) object;
        }
        else
        {
            serializable = "" + object;
        }

        return serializable;
    }
}
