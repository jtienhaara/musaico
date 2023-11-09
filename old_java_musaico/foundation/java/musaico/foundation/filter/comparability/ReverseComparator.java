package musaico.foundation.filter.comparability;


import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Compares objects in reverse order.
 * </p>
 *
 * <p>
 * The wrapped Comparator's result is reversed (flipped sign).
 * </p>
 *
 * <p>
 * If the wrapped Comparator returns Integer.MIN_VALUE, then Integer.MAX_VALUE
 * is returned instead.
 * </p>
 *
 *
 * <p>
 * In Java, every Comparator must be Serializable in order to play nicely
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
 * <p>
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.foundation.filter.comparability.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.comparability.MODULE#LICENSE
 */
public class ReverseComparator<REVERSED extends Object>
    implements Comparator<REVERSED>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The base Comparator to reverse. */
    private final Comparator<REVERSED> comparator;


    /**
     * <p>
     * Creates a ReverseComparator where the specified Comparator
     * is used to compare values, and whenever left is less than
     * or greater than right, the comparison is reversed.
     * </p>
     *
     * <p>
     * For example, a numeric Comparator might compare 3 to 5
     * as <code> -2 </code>.  By wrapping the same numeric comparison
     * in a ReverseComparator, the result from the same comparison will be
     * (positive) <code> 2 </code>.
     * </p>
     *
     * @param comparator The forward Comparator, which will be reversed.
     *              Must not be null.
     *
     * @throws NullPointerException If the specified comparator is null.
     */
    public ReverseComparator (
            Comparator<REVERSED> comparator
            )
        throws NullPointerException
    {
        if ( comparator == null )
        {
            throw new NullPointerException ( "ERROR Cannot create "
                                             + ClassName.of ( this.getClass () )
                                             + " from comparator = "
                                             + comparator );
        }

        this.comparator = comparator;
    }


    /**
     * @return The forward Comparator which is reversed by this Comparator.
     *         Never null.
     */
    public final Comparator<REVERSED> comparator ()
    {
        return this.comparator;
    }


    /**
     * @see java.util.Comparator#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    public final int compare (
            REVERSED left,
            REVERSED right
            )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                // Null == null.
                return 0;
            }
            else
            {
                // null > any value.
                return Integer.MAX_VALUE;
            }
        }
        else if ( right == null )
        {
            // any value < null.
            return Integer.MIN_VALUE + 1;
        }

        int difference = this.comparator.compare ( left, right );
        final int reversed_difference;
        if ( difference == Integer.MIN_VALUE )
        {
            reversed_difference = Integer.MAX_VALUE;
        }
        else
        {
            reversed_difference = 0 - difference;
        }

        return reversed_difference;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Obj -> ReverseComp after getClass().
    public final boolean equals (
            Object obj
            )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }
        else if ( obj.getClass () != this.getClass () )
        {
            return false;
        }

        final ReverseComparator<REVERSED> that =
            (ReverseComparator<REVERSED>) obj;
        final Comparator<REVERSED> this_comparator = this.comparator;
        final Comparator<REVERSED> that_comparator = that.comparator ();

        if ( this_comparator == null )
        {
            if ( that_comparator != null )
            {
                return false;
            }
        }
        else if ( that_comparator == null )
        {
            return false;
        }
        else if ( ! this_comparator.equals ( that_comparator ) )
        {
            return false;
        }

        // Everything is matchy-matchy.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 31 * this.getClass ().getName ().hashCode ()
            + ( this.comparator == null
                    ? 0
                    : this.comparator.hashCode () );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " ( " + this.comparator + " )";
    }
}
