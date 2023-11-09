package musaico.foundation.filter.time;

import java.io.Serializable;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Keeps all objects which do change during an operation.
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add useful new filters to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
 *
 * <p>
 * In Java every Filter must be generic in order to
 * enable composability.
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
 * @see musaico.foundation.filter.time.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.time.MODULE#LICENSE
 */
public class Changing<GRAIN extends Object>
    implements Filter<BeforeAndAfter<GRAIN>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Singleton Changing Object filter, for when
     *  generics are unnecessary. */
    public static final Changing<Object> FILTER =
        new Changing<Object> ();


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
            BeforeAndAfter<GRAIN> before_and_after
            )
    {
        if ( before_and_after == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( before_and_after.isUnchanged () )
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
        return "changing";
    }
}
