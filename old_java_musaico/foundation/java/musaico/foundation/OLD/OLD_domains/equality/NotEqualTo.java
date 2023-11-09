package musaico.foundation.domains.equality;

import java.io.Serializable;


import musaico.foundation.domains.comparability.LeftAndRight;

import musaico.foundation.filter.AbstractDomain;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The domain of all objects which are not equal to a specific object.
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add new domains to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
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
 * @see musaico.foundation.domains.equality.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.equality.MODULE#LICENSE
 */
public class NotEqualTo
    extends AbstractDomain<Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The object to which nothing in this domain is equal. */
    private final Object object;


    /**
     * <p>
     * Creates a new NotEqualTo the specified object, a domain
     * containing all possible objects except the specified one.
     * </p>
     *
     * @param object The object to which nothing in this domain is equal.
     *               Must not be null.
     */
    public NotEqualTo (
                       Object object
                       )
        throws NullPointerException
    {
        if ( object == null )
        {
            throw new NullPointerException ( "Cannot create a NotEqualTo null domain" );
        }

        this.object = object;
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

        final NotEqualTo that = (NotEqualTo) obj;
        if ( this.object == null )
        {
            if ( that.object != null )
            {
                return false;
            }
        }
        else if ( that.object == null )
        {
            return false;
        }
        else if ( ! this.object.equals ( that.object ) )
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
        else if ( this.object.equals ( value ) )
        {
            return FilterState.DISCARDED;
        }
        else
        {
            return FilterState.KEPT;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = 17 * this.getClass ().getName ().hashCode ();
        if ( this.object != null )
        {
            hash_code += this.object.hashCode ();
        }

        return hash_code;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "not equal to " + this.object;
    }


    /**
     * @return The value to which each input must not be equal.
     *         Never null.
     */
    public Object value ()
    {
        return this.object;
    }
}
