package musaico.foundation.order;


import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;
import musaico.foundation.contract.obligations.Parameter1;


/**
 * <p>
 * Compares objects in reverse order.
 * </p>
 *
 * <p>
 * Whenever the wrapped order returns <code> LEFT_LESS_THAN_RIGHT </code>
 * the result is reversed to <code> LEFT_GREATER_THAN_RIGHT </code>,
 * and vice-versa.
 * </p>
 *
 * <p>
 * All other comparisons (such as <code> INCOMPARABLE_LEFT </code>)
 * are returned as-is.
 * </p>
 *
 *
 * <p>
 * In Java, every Order is Serializable in order to play nicely
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
 * @see musaico.foundation.order.MODULE#COPYRIGHT
 * @see musaico.foundation.order.MODULE#LICENSE
 */
public class ReverseOrder<ORDERED extends Object>
    implements Order<ORDERED>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks constructor and static method parameters for us. */
    private static final Advocate classAdvocate =
        new Advocate ( ReverseOrder.class );


    /** The base order to reverse. */
    private final Order<ORDERED> order;

    /** Checks method parameter obligations and return value guarantees. */
    private final Advocate advocate;


    /**
     * <p>
     * Creates a ReverseOrder where the specified order
     * is used to compare values, and whenever left is less than
     * or greater than right, the comparison is reversed.
     * </p>
     *
     * <p>
     * For example, a numeric order might compare 3 and 5
     * with <code> Comparison.LEFT_LESS_THAN_RIGHT </code>.
     * By wrapping the same numeric comparison in a ReverseOrder,
     * the result from the same comparison will be
     * <code> Comparison.LEFT_GREATER_THAN_RIGHT </code>.
     * </p>
     *
     * @param order The forward order, which will be reversed.
     *              Must not be null.
     */
    public ReverseOrder (
            Order<ORDERED> order
            )
        throws EveryParameter.MustNotBeNull.Violation
    {
        this.order = order;

        this.advocate = new Advocate ( this );
    }


    /**
     * @see java.util.Comparator#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    public final int compare (
            ORDERED left,
            ORDERED right
            )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                // reverse ( null == null ).
                return 0;
            }
            else
            {
                // reverse ( null > any value ).
                return Integer.MIN_VALUE + 1;
            }
        }
        else if ( right == null )
        {
            // reverse ( any value < null ).
            return Integer.MAX_VALUE;
        }

        final Comparison comparison = this.compareValues ( left, right );
        return comparison.difference ();
    }


    /**
     * @see musaico.foundation.io.Order#compare(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    public final Comparison compareValues (
            ORDERED left,
            ORDERED right
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Return.NeverNull.Violation
    {
        this.advocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              left, right );

        Comparison base_comparison =
            this.order.compareValues ( left, right );
        if ( base_comparison.isIncomparable () )
        {
            // Still incomparable, even in reverse.
            return base_comparison;
        }

        final int difference = base_comparison.difference ();
        final int reversed_difference;
        if ( difference == Integer.MIN_VALUE )
        {
            reversed_difference = Integer.MAX_VALUE;
        }
        else
        {
            reversed_difference = 0 - difference;
        }

        final Comparison reversed_comparison =
            Comparison.fromDifference ( reversed_difference );

        return reversed_comparison;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                           Object obj
                           )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null
                  || obj.getClass () != this.getClass () )
        {
            return false;
        }

        final ReverseOrder<?> that = (ReverseOrder<?>) obj;
        if ( this.order == null )
        {
            if ( that.order != null )
            {
                return false;
            }
        }
        else if ( that.order == null )
        {
            return false;
        }
        else if ( ! this.order.equals ( that.order ) )
        {
            return false;
        }

        // Everything is identical.
        return true;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return -1 * this.order.hashCode ();
    }

    /**
     * @see musaico.foundation.order.Order#reverseOrder()
     */
    @Override
    public final Order<ORDERED> reverseOrder ()
        throws Return.NeverNull.Violation
    {
        return this.order;
    }

    /**
     * @see musaico.foundation.io.Order#sort(java.lang.Object[])
     */
    @Override
    @SuppressWarnings("unchecked") // cast sorted_array to ORDERED [].
    public final ORDERED [] sort (
            ORDERED [] array
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Return.NeverNull.Violation
    {
        this.advocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              array );
        this.advocate.check ( Parameter1.MustContainNoNulls.CONTRACT,
                              array );

        final ORDERED [] sorted_array = (ORDERED [])
            Array.newInstance ( array.getClass ().getComponentType (),
                                array.length );
        Arrays.sort ( sorted_array, this );
        return sorted_array;
    }

    /**
     * @see musaico.foundation.io.Order#sort(java.lang.Iterable)
     */
    @Override
    public final List<ORDERED> sort (
            Iterable<ORDERED> iterable
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Return.NeverNull.Violation
    {
        this.advocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              iterable );
        this.advocate.check ( Parameter1.MustContainNoNulls.CONTRACT,
                              iterable );

        final List<ORDERED> sorted_list;
        if ( iterable instanceof Collection )
        {
            final Collection<ORDERED> collection =
                (Collection<ORDERED>) iterable;
            sorted_list = new ArrayList<ORDERED> ( collection );
        }
        else
        {
            sorted_list = new ArrayList<ORDERED> ();
            for ( ORDERED element : iterable )
            {
                sorted_list.add ( element );
            }
        }

        Collections.sort ( sorted_list, this );

        return sorted_list;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "reverse ( " + this.order + " )";
    }
}
