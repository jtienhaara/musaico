package musaico.foundation.filter.comparability;

import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Compares arrays.
 * </p>
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
public class ArrayComparator
    implements Comparator<Object []>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The singleton ArrayComparator. */
    public static final ArrayComparator COMPARATOR =
        new ArrayComparator ();




    // Creates the ArrayComparator singleton.
    // Use ArrayComparator.COMPARATOR instead.
    private ArrayComparator ()
    {
    }


    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public final int compare (
            Object [] left,
            Object [] right
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
                return Integer.MAX_VALUE;
            }
        }
        else if ( right == null )
        {
            return Integer.MIN_VALUE + 1;
        }

        if ( left.length < right.length )
        {
            final int comparison =
                Integer.MIN_VALUE + 1 + left.length;
            return comparison;
        }
        else if ( left.length > right.length )
        {
            final int comparison =
                Integer.MAX_VALUE - right.length;
            return comparison;
        }

        int first_different_index = -1;
        int difference = 0;
        for ( int a = 0; a < left.length; a ++ )
        {
            final int element_comparison =
                LeftAndRight.compare  ( left [ a ],
                                        right [ a ] );

            if ( element_comparison != 0 )
            {
                first_different_index = a;
                difference = element_comparison;
                break;
            }
        }

        final int comparison = difference;
        return comparison;
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
