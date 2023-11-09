package musaico.foundation.order;


import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domain.comparators.ReverseComparator;


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
    extends ReverseComparator<ORDERED>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks constructor and static method parameters for us. */
    private static final Advocate classContracts =
        new Advocate ( ReverseOrder.class );


    /** The base order to reverse. */
    private final Order<ORDERED> order;

    /** Checks method parameter obligations and return value guarantees. */
    private final Advocate contracts;


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
        throws ParametersMustNotBeNull.Violation
    {
        super ( classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                       order ) );

        this.order = order;

        this.contracts = new Advocate ( this );
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
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
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
     * @see musaico.foundation.order.Order#reverseOrder()
     */
    @Override
    public final Order<ORDERED> reverseOrder ()
        throws ReturnNeverNull.Violation
    {
        return this.order;
    }
}
