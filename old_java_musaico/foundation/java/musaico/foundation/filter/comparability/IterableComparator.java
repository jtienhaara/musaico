package musaico.foundation.filter.comparability;

import java.io.Serializable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;


import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Compares Iterables.
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
public class IterableComparator
    implements Comparator<Iterable<?>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The singleton IterableComparator. */
    public static final IterableComparator COMPARATOR =
        new IterableComparator ();




    // Creates the IterableComparator singleton.
    // Use IterableComparator.COMPARATOR instead.
    private IterableComparator ()
    {
    }


    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public final int compare (
            Iterable<?> left,
            Iterable<?> right
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

        int left_size = -1;
        int right_size = -1;
        if ( ( left instanceof Collection )
             && ( right instanceof Collection ) )
        {
            final Collection<?> left_collection =
                (Collection<?>) left;
            final Collection<?> right_collection =
                (Collection<?>) right;
            left_size = left_collection.size ();
            right_size = right_collection.size ();
        }

        if ( left_size < right_size )
        {
            final int comparison =
                Integer.MIN_VALUE + 1 + left_size;
            return comparison;
        }
        else if ( left_size > right_size )
        {
            final int comparison =
                Integer.MAX_VALUE - right_size;
            return comparison;
        }

        // Compare the Iterables pairwise.
        int first_different_index = -1;
        int difference = 0;
        final Iterator<?> left_it = left.iterator ();
        final Iterator<?> right_it = right.iterator ();
        int index = 0;
        while ( left_it.hasNext () )
        {
            if ( ! right_it.hasNext () )
            {
                first_different_index = index;
                difference = Integer.MAX_VALUE;
                break;
            }

            final Object left_element = left_it.next ();
            final Object right_element = right_it.next ();

            final int element_comparison =
                LeftAndRight.compare ( left_element,
                                       right_element );
            if ( element_comparison != 0 )
            {
                first_different_index = index;
                difference = element_comparison;
                break;
            }

            index ++;
        }

        if ( right_it.hasNext () )
        {
            // Left iterable ran out of elements, right keeps going.
            if ( first_different_index < 0 )
            {
                first_different_index = index;
            }

            difference = Integer.MIN_VALUE + 1;
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
