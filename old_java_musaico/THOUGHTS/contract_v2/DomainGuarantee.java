package musaico.foundation.contract;

import java.io.Serializable;

import java.lang.reflect.Array;


/**
 * <p>
 * A requirement of the consumer of the method or other service under
 * contract to use values in a specific domain, such as "the parameter
 * value must be greater than or equal to 1".
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
 * nicely over RMI.
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
public abstract class DomainGuarantee<INSPECTABLE extends Object, DOMAIN_GUARANTEE_VIOLATION extends Throwable>
    implements Guarantee<INSPECTABLE, DOMAIN_GUARANTEE_VIOLATION>, Serializable
{
    /** No domain guarantee whatsoever.  Anything goes. */
    public static final NoDomainGuarantee<Object> NONE =
        new NoDomainGuarantee<Object> ( Domain.NONE );


    /** The domain to which each and every inspectable object must belong. */
    private final Domain<INSPECTABLE> domain;


    /**
     * <p>
     * Creates a new DomainGuarantee requiring that all inspectable
     * data belong to the specified Domain.
     * </p>
     *
     * @param domain The domain to which all inspectable data must belong.
     *               Must not be null.
     */
    public DomainGuarantee (
                             Domain<INSPECTABLE> domain
                             )
    {
        if ( domain == null )
        {
            throw new IllegalArgumentException ( "new DomainGuarantee ( " + domain + " )" );
        }

        this.domain = domain;
    }


    /**
     * <p>
     * Throws the appropriate DomainGuaranteeViolation for this
     * domain guarantee, typically called only by enforce ().
     * </p>
     *
     * @param object_under_contract The object under contract, such
     *                              as an object whose method checks its
     *                              own return values, or the amount of time
     *                              it took calculating them, against
     *                              this guarantee.
     *                              Must not be null.
     *
     * @param inspectable_data The inspectable data, checked by
     *                         this contract.  Must be Serializable
     *                         all through the object tree (for
     *                         example as a result of calling
     *                         <code> makeSerializable () </code>
     *                         on the original data).  Must not be null.
     *                         Must not contain any null elements.
     *
     * @return A newly created violation detailing this contract,
     *         a serializable version of the object under contract,
     *         and serializable version(s) of the inspectable data
     *         which failed to meet this contract (such as return value
     *         from a method provided by the object under contract).
     *         Never null.
     */
    protected abstract DOMAIN_GUARANTEE_VIOLATION createViolation (
                                                                   Serializable object_under_contract,
                                                                   Serializable inspectable_data
                                                                   );


    /**
     * <p>
     * Returns the domain to which all guaranteed values must belong.
     * </p>
     *
     * @return The domain to which inspectable values must belong, such
     *         as "numbers greater than or equal to 1" or
     *         "strings that are exactly 16 characters long" and so on.
     *         Never null.
     */
    public Domain<INSPECTABLE> domain ()
    {
        return this.domain;
    }


    /**
     * @see musaico.foundation.contract.Contract#enforce(java.lang.Object, java.lang.Object[])
     */
    @Override
    @SuppressWarnings("unchecked") // Because we explicitly cast, even though we check for exceptions (!)
    public final void enforce (
                               Object object_under_contract,
                               INSPECTABLE inspectable_data
                               )
        throws DOMAIN_GUARANTEE_VIOLATION
    {
        Domain<INSPECTABLE> domain = this.domain ();

        // If array, test every element of the array.
        // Special case: if the Domain is an ArrayDomain, then pass
        // the entire array of inspectable data as one object.
        boolean is_in_domain = true;
        if ( inspectable_data.getClass ().isArray ()
             && ! ( domain instanceof ArrayDomain ) )
        {
            // Array of inspectable data to check, one at a time.
            int length = Array.getLength ( inspectable_data );
            for ( int e = 0; e < length; e ++ )
            {
                Object inspectable_object = Array.get ( inspectable_data, e );
                final INSPECTABLE inspectable;
                try
                {
                    inspectable = (INSPECTABLE) inspectable_object;
                }
                catch ( ClassCastException cce )
                {
                    is_in_domain = false;
                    break;
                }

                if ( ! domain.isValid ( inspectable ) )
                {
                    is_in_domain = false;
                    break;
                }
            }
        }
        else
        {
            // One single inspectable to check (not an array, or the
            // whole array is treated as one inspectable object by
            // an ArrayDomain).
            if ( ! domain.isValid ( inspectable_data ) )
            {
                is_in_domain = false;
            }
        }

        if ( ! is_in_domain )
        {
            final DOMAIN_GUARANTEE_VIOLATION violation =
                this.violation ( object_under_contract,
                                 inspectable_data );
            throw violation;
        }
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
    protected Serializable [] makeArrayOfSerializables (
                                                        Object [] objects
                                                        )
    {
        return Contracts.makeArrayOfSerializables ( objects );
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
    protected Serializable makeSerializable (
                                             Object object
                                             )
    {
        return Contracts.makeSerializable ( object );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( this.getClass () != obj.getClass () )
        {
            return false;
        }

        DomainGuarantee<?, ?> that = (DomainGuarantee<?, ?>) obj;
        if ( ! this.domain ().equals ( that.domain () ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.domain ().hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "guaranteed " + this.domain ();
    }


    /**
     * <p>
     * Throws the appropriate DomainGuaranteeViolation for this
     * domain guarantee, typically called only by enforce ().
     * </p>
     *
     * @param object_under_contract The object under contract, such
     *                              as an object whose method checks its
     *                              own return values, or the amount of time
     *                              it took calculating them, against
     *                              this guarantee.
     *                              Must not be null.
     *
     * @param inspectable_data The inspectable data, checked by
     *                         this contract.  Must not be null.
     *                         Must not contain any null elements.
     *
     * @return A newly created violation detailing this contract,
     *         a serializable version of the object under contract,
     *         and serializable version(s) of the inspectable data
     *         which failed to meet this contract (such as return value
     *         from a method provided by the object under contract).
     *         Never null.
     */
    public DOMAIN_GUARANTEE_VIOLATION violation (
                                                 Object object_under_contract,
                                                 INSPECTABLE inspectable_data
                                                 )
    {
        final Serializable serializable_object_under_contract =
            this.makeSerializable ( object_under_contract );
        final Serializable serializable_inspectable_data =
            this.makeSerializable ( inspectable_data );

        return this.createViolation ( serializable_object_under_contract,
                                      serializable_inspectable_data );
    }
}
