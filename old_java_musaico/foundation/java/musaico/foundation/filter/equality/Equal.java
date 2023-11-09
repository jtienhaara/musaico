package musaico.foundation.filter.equality;

import java.io.Serializable;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.comparability.LeftAndRight;


/**
 * <p>
 * Keeps all pairs of objects which are equal to each other.
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
 * @see musaico.foundation.filter.equality.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.equality.MODULE#LICENSE
 */
public class Equal<SIDE extends Object>
    implements Filter<LeftAndRight<SIDE, SIDE>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Singleton Equal filter covering plain Objects,
     *  for when generics are unnecessary. */
    public static final Equal<Object> FILTER =
        new Equal<Object> ();


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
            LeftAndRight<SIDE, SIDE> left_and_right
            )
    {
        if ( left_and_right == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( left_and_right.isEqual () )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see musaico.foundation.filter.Equal#filter(musaico.foundation.filter.comparability.LeftAndRight))
     */
    public final FilterState filter (
            SIDE left,
            SIDE right
            )
    {
        final LeftAndRight<SIDE, SIDE> left_and_right =
            new LeftAndRight<SIDE, SIDE> ( left, right );
        final FilterState filter_state = this.filter ( left_and_right );
        return filter_state;
    }


    /** Singleton Equal filter covering plain Objects,
     *  for when generics are unnecessary. */
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
        return "equal";
    }
}
