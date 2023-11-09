package musaico.foundation.value;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A CountableView with methods to !!!.
 * </p>
 *
 * <p>
 * The Countable!!! is mutable.  It potentially changes
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
public interface Countable!!!<VALUE extends Object, VIEW_ITEM extends Object, VIEW extends CountableView<VALUE, VIEW_ITEM, VIEW>>
    extends CountableMutableView<VALUE, VIEW_ITEM, VIEW> Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;




    /** Contract which is violated whenever difference ( ... ) receives
     *  Abnormal input. */
    public static class Difference
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for difference ( ... ) operations. */
        public static final Difference CONTRACT =
            new Difference ();
    }

    /** Contract which is violated whenever difference ( ... ) does not
     *  produce any results. */
    public static class MustBeDifferent
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for difference ( ... ) operations. */
        public static final MustBeDifferent CONTRACT =
            new MustBeDifferent ();
    }

    /**
     * <p>
     * Removes the elements of the specified countable value that are
     * contained in this countable elements, and adds the elements
     * which are not contained in this countable elements.
     * </p>
     *
     * <p>
     * Multiple copies of the same element will be kept, even though
     * this is a set operation at heart.
     * </p>
     *
     * @param that The counbtable value whose element differences will
     *             be stored in this countable elements.
     *             Must not be null.
     *
     * @return This countable elements, containing only the
     *         elements that are different between the input and
     *         that specified value.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public abstract CountableElements<VALUE> difference (
                                                         Countable<VALUE> that
                                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;




    /** Contract which is violated whenever intersection ( ... ) receives
     *  Abnormal input. */
    public static class Intersection
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for intersection ( ... ) operations. */
        public static final Intersection CONTRACT =
            new Intersection ();
    }

    /** Contract which is violated whenever intersection ( ... ) does not
     *  produce any results. */
    public static class MustHaveIntersection
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for intersection ( ... ) operations. */
        public static final MustHaveIntersection CONTRACT =
            new MustHaveIntersection ();
    }

    /**
     * <p>
     * Removes the elements of the specified countable value that are
     * not contained in both this countable elements, leaving
     * behind only the intersecting or overlapping elements.
     * </p>
     *
     * <p>
     * Multiple copies of the same element will be kept, even though
     * this is a set operation at heart.
     * </p>
     *
     * @param that The counbtable value whose element intersections will
     *             be stored in this countable elements.
     *             Must not be null.
     *
     * @return This countable elements, containing only the
     *         elements that are contained in both the input and
     *         that specified value.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public abstract CountableElements<VALUE> intersection (
                                                           Countable<VALUE> that
                                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;




    /** Contract which is violated whenever union ( ... ) receives
     *  Abnormal input. */
    public static class Union
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for union ( ... ) operations. */
        public static final Union CONTRACT =
            new Union ();
    }

    /** Contract which is violated whenever union ( ... ) does not
     *  produce any results. */
    public static class MustHaveUnion
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for union ( ... ) operations. */
        public static final MustHaveUnion CONTRACT =
            new MustHaveUnion ();
    }

    /**
     * <p>
     * Adds all elements from that specified value which are not already
     * contained in this countable value being built, leaving the union
     * of this and that.
     * </p>
     *
     * <p>
     * Multiple copies of the same element will be kept, even though
     * this is a set operation at heart.
     * </p>
     *
     * @param that The counbtable value whose element unions will
     *             be stored in this countable elements.
     *             Must not be null.
     *
     * @return This countable elements, containing all the
     *         elements from the input and that specified value.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public abstract CountableElements<VALUE> union (
                                                    Countable<VALUE> that
                                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
