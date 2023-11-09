package musaico.foundation.value;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.order.Order;


/**
 * <p>
 * A CountableView with methods to operate on the order of
 * countable elements.
 * </p>
 *
 * <p>
 * The CountableOrder is mutable.  It potentially changes
 * with every operation.
 * </p>
 *
 *
 * <p>
 * In Java every CountableView must be Serializable in order to
 * play nicely across RMI.  However users of the CountableView
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public interface CountableOrder<VALUE extends Object, VIEW_ITEM extends Object, VIEW extends CountableView<VALUE, VIEW_ITEM, VIEW>>
    extends CountableMutableView<VALUE, VIEW_ITEM, VIEW> Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Contract which is violated whenever reverse ( ... ) does not
     *  produce any results. */
    public static class MustBeReversed
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for reverse ( ... ) operations. */
        public static final MustBeReversed CONTRACT =
            new MustBeReversed ();
    }

    /**
     * <p>
     * Reverses the order of the element(s) of this Countable elements.
     * </p>
     *
     * @return This countable elements, with its elements
     *         in reverse order.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public abstract CountableOrder<VALUE, VIEW_ITEM, VIEW> reverse ()
        throws ReturnNeverNull.Violation;




    /** Contract which is violated whenever shuffle ( ... ) receives
     *  Abnormal input. */
    public static class Shuffle
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for shuffle ( ... ) operations. */
        public static final Shuffle CONTRACT =
            new Shuffle ();
    }

    /** Contract which is violated whenever shuffle ( ... ) does not
     *  produce any results. */
    public static class MustBeShuffled
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for shuffle ( ... ) operations. */
        public static final MustBeShuffled CONTRACT =
            new MustBeShuffled ();
    }

    /**
     * <p>
     * Re-orders the element(s) of this Countable elements using
     * the specified rank generator to assign a rank to each indexed
     * element of this Countable elements.
     * </p>
     *
     * @param ranker Generates the rank for each index'ed element of
     *               this Countable elements.  For example, a
     *               PseudoRandom number generator could be used.
     *               The lower the ranking number generated, the earlier
     *               in the sequence the corresponding indexed element
     *               will be placed.  Ties will result in the tied
     *               indexed elements maintaining their existing relative
     *               ordering.  If the ranker runs out of ranks, then
     *               subsequent indexed elements are placed at the end.
     *               Must not be null.
     *
     * @return This Countable elements, re-ordered so that the
     *         each indexed element is ordered according to the rank
     *         generated for it by the specified ranker.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public abstract <RANK extends Comparable<? super RANK>>
        CountableOrder<VALUE, VIEW_ITEM, VIEW> shuffle (
                                                        Value<RANK> ranker
                                                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;




    /** Contract which is violated whenever sort ( ... ) does not
     *  produce any results. */
    public static class MustBeSorted
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for sort ( ... ) operations. */
        public static final MustBeSorted CONTRACT =
            new MustBeSorted ();
    }

    /**
     * <p>
     * Sorts the element(s) of this Countable elements according to the
     * specified Order.
     * </p>
     *
     * @param order The Order in which to sort the element(s) of this
     *              Countable elements.  Must not be null.
     *
     * @return This countable elements, with its elements sorted
     *         according to the specified order.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public abstract CountableOrder<VALUE, VIEW_ITEM, VIEW> sort (
                                                                 Order<VALUE> order
                                                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
