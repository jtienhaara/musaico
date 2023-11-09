package musaico.foundation.filter.comparability;

import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Compares a Long to any type of Number.
 * </p>
 *
 *
 * <p>
 * In Java every Comparator must be Serializable in order to play nicely
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
 * @see musaico.foundation.filter.comparability.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.comparability.MODULE#LICENSE
 */
public class LongComparator<NUMBER extends Number>
    implements Comparator<NUMBER>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The singleton generic LongComparator, for comparing
     *  Longs to other Numbers. */
    public static final LongComparator<Number> COMPARATOR =
        new LongComparator<Number> ();

    /** The singleton specific LongComparator, for comparing
     *  Longs to Longs. */
    public static final LongComparator<Long> LONG_COMPARATOR =
        new LongComparator<Long> ();


    // Creates the LongComparator singletons.
    // Use LongComparator.COMPARATOR
    // or LongComparator.LONG_COMPARATOR instead.
    private LongComparator ()
    {
    }


    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public final int compare (
            Number left,
            Number right
            )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                return 0;
            }
            else
            {
                // null > any Number.
                return Integer.MAX_VALUE;
            }
        }
        else if ( right == null )
        {
            // Any Number < null.
            return Integer.MIN_VALUE + 1;
        }

        final long left_value = left.longValue ();
        final long right_value = right.longValue ();

        final long difference;
        if ( left_value < 0
             && right_value > 0
             && left_value < ( Long.MIN_VALUE + right_value ) )
        {
            difference = Long.MIN_VALUE;
        }
        else
        {
            difference = left_value - right_value;
        }

        final int int_difference;
        if ( difference < ( Integer.MIN_VALUE + 2 ) )
        {
            int_difference = Integer.MIN_VALUE + 2;
        }
        else if ( difference > ( Integer.MAX_VALUE - 1 ) )
        {
            int_difference = Integer.MAX_VALUE - 1;
        }
        else
        {
            int_difference = (int) difference;
        }

        return int_difference;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return ClassName.of ( this.getClass () )
            .hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () );
    }
}
