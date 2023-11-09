package musaico.foundation.filter.comparability;

import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Stores a "left" object and a "right" object, comparing the two
 * to detect equality (<code> equals ( Object ) </code>).
 * </p>
 *
 *
 * <p>
 * In Java every Comparator must be Serializable in order to play nicely
 * across RMI.  Be warned, however, that depending on the "left"
 * and "right" values carried by this container, its children might
 * not be Serializable.
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
public class LeftAndRight<LEFT extends Object, RIGHT extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The left object to check for equality with the right object.
    private final LEFT left;

    // The right object.
    private final RIGHT right;

    // 0 if left equals right,
    // -1 or less if left < right,
    // 1 or more if left > right.
    private final int comparison;


    /**
     * <p>
     * Creates a new LeftAndRight with the specified "left" and "right"
     * objects.
     * </p>
     *
     * @param left The left object.  Can be null.
     *
     * @param right The right object.  Can be null.
     */
    public LeftAndRight (
            LEFT left,
            RIGHT right
            )
    {
        this.left = left;
        this.right = right;

        this.comparison = LeftAndRight.compare ( left, right );
    }


    /**
     * <p>
     * Compares the specified "left" and "right" objects.
     * </p>
     *
     * @param left The left object.  Can be null.
     *
     * @param right The right object.  Can be null.
     *
     * @return 0 if the "left" object equals the "right" object,
     *         -1 or less if the "left" object is less than the "right" object,
     *         1 or more if the "left" object is greater than
     *         the "right" object.
     */
    @SuppressWarnings("unchecked") // Casts inside try...catch blocks.
    public static final <COMPARE_LEFT extends Object, COMPARE_RIGHT extends Object>
        int compare (
            COMPARE_LEFT left,
            COMPARE_RIGHT right
            )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                final int comparison = 0;
                return comparison;
            }
            else
            {
                final int comparison = Integer.MAX_VALUE;
                return comparison;
            }
        }
        else if ( right == null )
        {
            final int comparison = Integer.MIN_VALUE + 1;
            return comparison;
        }
        else if ( ( left instanceof Number )
                  && ( right instanceof Number ) )
        {
            final Number left_num = (Number) left;
            final Number right_num = (Number) right;

            final int comparison =
                NumberComparator.COMPARATOR.compare ( left_num, right_num );
            return comparison;
        }
        else if ( ( left instanceof String )
                  && ( right instanceof String ) )
        {
            final String left_num = (String) left;
            final String right_num = (String) right;

            final int comparison =
                StringComparator.COMPARATOR.compare ( left_num, right_num );
            return comparison;
        }
        else if ( left.getClass ().isArray ()
                  && right.getClass ().isArray () )
        {
            final Object [] left_array = (Object []) left;
            final Object [] right_array = (Object []) right;

            final int comparison =
                ArrayComparator.COMPARATOR.compare ( left_array,
                                                     right_array );
            return comparison;
        }
        else if ( ( left instanceof Iterable )
                  && ( right instanceof Iterable ) )
        {
            final Iterable<?> left_iterable = (Iterable<?>) left;
            final Iterable<?> right_iterable = (Iterable<?>) right;

            final int comparison =
                IterableComparator.COMPARATOR.compare ( left_iterable,
                                                        right_iterable );
            return comparison;
        }

        // Neither left nor right is null.
        // Left and right are not both Numbers.
        // Left and right are not both arrays.
        // Left and right are not both Iterables.
        // Let's try a few dfferent ways of comparing, see what happens.
        if ( left instanceof Comparable )
        {
            try
            {
                final Comparable<COMPARE_LEFT> left_comparable =
                    (Comparable<COMPARE_LEFT>) left;
                final COMPARE_LEFT right_comparable =
                    (COMPARE_LEFT) right;
                final int comparison =
                    left_comparable.compareTo ( right_comparable );
                return comparison;
            }
            catch ( Exception e )
            {
                // Carry on below.
            }
        }

        if ( right instanceof Comparable )
        {
            try
            {
                final Comparable<COMPARE_RIGHT> right_comparable =
                    (Comparable<COMPARE_RIGHT>) right;
                final COMPARE_RIGHT left_comparable =
                    (COMPARE_RIGHT) left;
                final int comparison =
                    0 - right_comparable.compareTo ( left_comparable );
                return comparison;
            }
            catch ( Exception e )
            {
                // Carry on below.
            }
        }

        boolean is_equal = false;
        try
        {
            is_equal = left.equals ( right );
        }
        catch ( Exception e )
        {
            is_equal = false;
        }

        if ( is_equal )
        {
            final int comparison = 0;
            return comparison;
        }

        // Try our best to at least keep a consistent
        // comparison number.
        int difference =
            left.getClass ().getName ().compareTo (
                right.getClass ().getName () );
        if ( difference == 0 )
        {
            // Ugly.
            difference = left.hashCode () - right.hashCode ();
            if ( difference == 0 )
            {
                // Even uglier.
                difference = Integer.MAX_VALUE;
            }
        }

        final int comparison = difference;
        return comparison;
    }


    /**
     * @return 0 if the "left" object equals the "right" object,
     *         -1 or less if the "left" object is less than the "right" object,
     *         1 or more if the "left" object is greater than
     *         the "right" object.
     */
    public final int comparison ()
    {
        return this.comparison;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
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
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final LeftAndRight<?, ?> that = (LeftAndRight<?, ?>) object;

        if ( this.left == null )
        {
            if ( that.left != null )
            {
                return false;
            }
        }
        else if ( that.left == null )
        {
            return false;
        }
        else if ( ! this.left.equals ( that.left ) )
        {
            return false;
        }

        if ( this.right == null )
        {
            if ( that.right != null )
            {
                return false;
            }
        }
        else if ( that.right == null )
        {
            return false;
        }
        else if ( ! this.right.equals ( that.right ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return
            31 * ( this.left == null
                       ? Integer.MIN_VALUE
                       : this.left.hashCode () )
            + 17 * ( this.right == null
                       ? Integer.MIN_VALUE
                       : this.right.hashCode () );
    }


    /**
     * @return True if the "left" object equals the "right" object.
     *         False if the left and right objects differ.
     */
    public final boolean isEqual ()
    {
        if ( this.comparison == 0 )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @return The left object, which will be compared to the right
     *         object with <code> left.equals ( Object ) </code>.
     *         WARNING: Can be null.
     */
    public final LEFT left ()
    {
        return this.left;
    }


    /**
     * @return The right object.  WARNING: Can be null.
     */
    public final RIGHT right ()
    {
        return this.right;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () ) + ": "
            + "left = " + this.left
            + ", "
            + "right = " + this.right;
    }
}
