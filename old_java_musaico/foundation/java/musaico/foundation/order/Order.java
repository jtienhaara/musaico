package musaico.foundation.order;


import java.io.Serializable;

import java.util.Comparator;
import java.util.Date;
import java.util.List;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.EveryParameter;


/**
 * <p>
 * A specific order for comparing objects of a specific type,
 * such as a dictionary order or a character encoding (ASCII etc)
 * order for comparing Strings.
 * </p>
 *
 *
 * <p>
 * In Java, every Order can be used as a Java Comparator.
 * </p>
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
public interface Order<ORDERED extends Object>
    extends Comparator<ORDERED>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every Order must implement Comparator.compare ().


    /**
     * <p>
     * Compares the two order values, and returns the
     * comparison result (Comparison.LEFT_LESS_THAN_RIGHT,
     * Comparison,LEFT_EQUALS_RIGHT, and so on).
     * </p>
     *
     * @param left The left ORDERED to compare.  Must not be null.
     *
     * @param right The right ORDERED to compare.  Must not be null.
     *
     * @return The comparison of left ORDERED to right ORDERED values.
     *         Never null.
     */
    public abstract Comparison compareValues (
                                              ORDERED left,
                                              ORDERED right
                                              )
        throws EveryParameter.MustNotBeNull.Violation,
               Return.NeverNull.Violation;


    /**
     * @return The Order that is reversed from this Order, such as
     *         a new ReverseOrder.  Never null.
     */
    public abstract Order<ORDERED> reverseOrder ()
        throws Return.NeverNull.Violation;


    /**
     * <p>
     * Creates a copy of the specified array, sorts it according
     * to this Order, and returns the new sorted array.
     * </p>
     *
     * @param array The array of items to sort.  Must not be null.
     *              Must not contain any null elements.
     *
     * @return A new copy of the array which has been sorted according
     *         to this Order.  Never null.
     */
    public abstract ORDERED [] sort (
                                     ORDERED [] array
                                     )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Return.NeverNull.Violation;


    /**
     * <p>
     * Creates a copy of the specified Iterable, sorts it according
     * to this Order, and returns the new sorted List.
     * </p>
     *
     * @param iterable The items to sort.
     *                 Must not be null. Must not contain
     *                 any null elements.
     *
     * @return A new List containing all the elements of the
     *         input Iterable, which has been sorted according
     *         to this Order.  Never null.
     */
    public abstract List<ORDERED> sort (
                                        Iterable<ORDERED> iterable
                                        )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Return.NeverNull.Violation;
}
