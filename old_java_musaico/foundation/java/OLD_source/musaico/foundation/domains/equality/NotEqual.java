package musaico.foundation.domains.equality;

import java.io.Serializable;


import musaico.foundation.domains.comparability.LeftAndRight;

import musaico.foundation.filter.AbstractDomain;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The domain of all pairs of objects which are not equal to each other.
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
public class NotEqual
    extends AbstractDomain<LeftAndRight<Object, Object>>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The NotEqual domain singleton. */
    public static final NotEqual DOMAIN =
        new NotEqual ();


    protected NotEqual ()
    {
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

        return true;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
                                     LeftAndRight<Object, Object> left_and_right
                                     )
    {
        if ( left_and_right == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( left_and_right.isEqual () )
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
        return this.getClass ().getName ().hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "not equal";
    }
}
