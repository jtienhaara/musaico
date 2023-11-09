package musaico.foundation.domains;

import java.io.Serializable;


import musaico.foundation.filter.AbstractDomain;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The domain of all instances of a specific class.
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add new domains to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
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
 * <p>
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.foundation.domains.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.MODULE#LICENSE
 */
public class InstanceOfClass
    extends AbstractDomain<Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The object to which nothing in this domain is equal. */
    private final Class<?> domainClass;


    /**
     * <p>
     * Creates a new InstanceOfClass folr the specified class, a domain
     * containing all possible objects of the specified class.
     * </p>
     *
     * @param domain_class The class of which every member of this
     *                     domain is an instance.  Must not be null.
     */
    public InstanceOfClass (
                            Class<?> domain_class
                            )
        throws NullPointerException
    {
        if ( domain_class == null )
        {
            throw new NullPointerException ( "Cannot create a InstanceOfClass null domain" );
        }

        this.domainClass = domain_class;
    }


    /**
     * @return The domain class.  Every object in this domain is
     *         an instance of the domain class.  Never null.
     */
    public final Class<?> domainClass ()
    {
        return this.domainClass;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null )
        {
            return false;
        }
        else if ( this.getClass () != obj.getClass () )
        {
            return false;
        }

        final InstanceOfClass that = (InstanceOfClass) obj;
        if ( this.domainClass != that.domainClass )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
                                     Object value
                                     )
    {
        if ( value == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( this.domainClass.isInstance ( value ) )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = 17 * this.getClass ().getName ().hashCode ();
        if ( this.domainClass != null )
        {
            hash_code += this.domainClass.getName ().hashCode ();
        }

        return hash_code;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "instance of " + this.domainClass.getName ();
    }
}
