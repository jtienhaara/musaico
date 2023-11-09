package musaico.foundation.domains.comparability;

import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.domains.ClassName;


/**
 * <p>
 * Compares a Float to any type of Number.
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
 * @see musaico.foundation.domains.comparability.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.comparability.MODULE#LICENSE
 */
public class FloatComparator
    implements Comparator<Number>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The singleton FloatComparator. */
    public static final FloatComparator COMPARATOR =
        new FloatComparator ();


    // Creates the FloatComparator singleton.
    // Use FloatComparator.COMPARATOR instead.
    private FloatComparator ()
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

        final float left_value = left.floatValue ();
        final float right_value = right.floatValue ();

        final float difference;
        if ( left_value < 0
             && right_value > 0
             && left_value < ( Float.MIN_VALUE + right_value ) )
        {
            difference = Float.MIN_VALUE;
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
        else
        {
            return false;
        }
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
