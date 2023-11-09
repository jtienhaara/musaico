package musaico.foundation.filter.equality;

import java.io.Serializable;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.comparability.LeftAndRight;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Keeps all objects which are equal to a specific object.
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
public class EqualTo<GRAIN extends Object>
    implements Filter<GRAIN>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The object to which everything kept by this filter is equal. */
    private final GRAIN object;


    /**
     * <p>
     * Creates a new EqualTo the specified object, a filter
     * that discards all possible objects except the specified one.
     * </p>
     *
     * @param object The object to which everything kept by this filter
     *               is equal.  CAN be null.
     */
    public EqualTo (
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

        final EqualTo<?> that = (EqualTo<?>) obj;
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
            GRAIN grain
            )
    {
        if ( this.object == null )
        {
            if ( grain == null )
            {
                return FilterState.KEPT;
            }
            else
            {
                return FilterState.DISCARDED;
            }
        }
        else if ( grain == null )
        {
            return FilterState.DISCARDED;
        }

        final LeftAndRight<GRAIN, GRAIN> left_and_right =
            new LeftAndRight<GRAIN, GRAIN> ( this.object, grain );
        if ( left_and_right.isEqual () )
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
        int hash_code = 17 * ClassName.of ( this.getClass () ).hashCode ();
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
        return "equal to " + this.object;
    }


    /**
     * @return The value to which each input must be equal.
     *         CAN be null.
     */
    public GRAIN valueOrNull ()
    {
        return this.object;
    }
}
