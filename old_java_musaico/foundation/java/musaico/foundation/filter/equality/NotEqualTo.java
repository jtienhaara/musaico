package musaico.foundation.filter.equality;

import java.io.Serializable;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.comparability.LeftAndRight;


/**
 * <p>
 * Keeps all objects which are not equal to a specific object.
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add useful new Filters to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
 *
 * <p>
 * In Java every Filter must be Serializable in order to play nicely
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
 * @see musaico.foundation.filter.equality.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.equality.MODULE#LICENSE
 */
public class NotEqualTo<GRAIN extends Object>
    implements Filter<GRAIN>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The object to which nothing kept by this filter is equal.
    private final GRAIN object;


    /**
     * <p>
     * Creates a new NotEqualTo the specified object, a filter
     * that keeps all possible objects except the specified one.
     * </p>
     *
     * @param object The object to which nothing kept by this filter is equal.
     *               CAN be null.
     */
    public NotEqualTo (
            GRAIN object
            )
    {
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

        final NotEqualTo<?> that = (NotEqualTo<?>) obj;
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
            GRAIN value
            )
    {
        if ( this.object == null )
        {
            if ( value == null )
            {
                return FilterState.DISCARDED;
            }
            else
            {
                return FilterState.KEPT;
            }
        }
        else if ( value == null )
        {
            // Not a real object.  Never equal to anything.
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
     *         CAN be null.
     */
    public GRAIN valueOrNull ()
    {
        return this.object;
    }
}
