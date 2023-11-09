package musaico.foundation.value;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * A CountableView with methods to operate on the order of view items
 * (elements / indices / and so on).
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
public interface CountableFilter<VALUE extends Object, VIEW_ITEM extends Object, VIEW extends CountableView<VALUE, VIEW_ITEM, VIEW>>
    extends CountableMutableView<VALUE, VIEW_ITEM, VIEW> Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;




    /** Contract which is violated whenever filter ( ... ) does not
     *  produce any results. */
    public static class MustMatchFilter
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for filter ( ... ) operations. */
        public static final MustMatchFilter CONTRACT =
            new MustMatchFilter ();
    }

    /**
     * <p>
     * Keeps only the view item(s) (elements / indices / and so on)
     * from this Countable view which match the specified filter,
     * removing all other view item(s).
     * </p>
     *
     * @param filter The Filter which will keep or discard view item(s)
     *               of this Countable view.
     *               Must not be null.
     *
     * @return This CountableFilter view, containing only
     *         the view item(s) that were KEPT by the specified filter.
     *         Can result in an empty countable view, if
     *         no view item(s) were kept by the filter.
     *         If this Countable view was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public abstract CountableFilter<VALUE> filter (
                                                   Filter<VALUE> filter
                                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;




    /** Contract which is violated whenever keep ( ... ) receives
     *  Abnormal input. */
    public static class Keep
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for keep ( ... ) operations. */
        public static final Keep CONTRACT =
            new Keep ();
    }

    /** Contract which is violated whenever keep ( ... ) does not
     *  produce any results. */
    public static class MustHaveKept
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for keep ( ... ) operations. */
        public static final MustHaveKept CONTRACT =
            new MustHaveKept
 ();
    }

    /**
     * <p>
     * Keeps all instance(s) of the specified view item(s) (elements /
     * indices / and so on) in this countable view, and removes
     * all other view items.
     * </p>
     *
     * <p>
     * For example, starting from the countable view items (elements
     * in this case) <code> { A, B, C, B, A } </code>, keeping
     * the view items <code> { C, A } </code> will keep the one
     * <code> C </code> and the two <code> A </code> view items,
     * removing the two <code> B </code> view items, resulting in
     * <code> { A, C, A } </code>.
     * </p>
     *
     * @param view_items_to_keep The view item(s) to keep.
     *                           If any view item is found more than once,
     *                           then all instances are kept.
     *                           Must not be null.
     *
     * @return This countable view items, with the specified view item(s)
     *         kept and all others removed.
     *         If this CountableFilter was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableFilter constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public abstract CountableFilter<VALUE> keep (
                                                 Countable<VALUE> view_items_to_keep
                                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever keepFirst ( ... ) does not
     *  produce any results. */
    public static class MustHaveKeptFirst
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for keepFirst ( ... ) operations. */
        public static final MustHaveKeptFirst CONTRACT =
            new MustHaveKeptFirst ();
    }

    /**
     * <p>
     * Removes the tail of this countable view, leaving only
     * the first N view items (elements / indices / and so on) (if any).
     * </p>
     *
     * @param num_view_items_to_keep If positive then the number of
     *                               view items to keep from the
     *                               start of this countable view.
     *                               If 0L then the value is emptied out.
     *                               If less than or equal to
     *                               <code> Countable.END </code> then
     *                               <code> keepLast ( Countable.LAST - N ) </code>
     *                               is invoked.
     *
     * @return This CountableFilter, with only the specified number
     *         of view items (if any) kept from the start.
     *         If this Countable view was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public abstract CountableFilter<VALUE> keepFirst (
                                                      long num_view_items_to_keep
                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever keepLast ( ... ) does not
     *  produce any results. */
    public static class MustHaveKeptLast
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for keepLast ( ... ) operations. */
        public static final MustHaveKeptLast CONTRACT =
            new MustHaveKeptLast ();
    }

    /**
     * <p>
     * Removes the head of this countable view, leaving only
     * the last N view items (elements / indices / and so on) (if any).
     * </p>
     *
     * @param num_view_items_to_keep If positive then the number of
     *                               view items to keep from the
     *                               end of this countable view.
     *                               If 0L then the value is emptied out.
     *                               If less than or equal to
     *                               <code> Countable.END </code> then
     *                               <code> keepFirst ( Countable.LAST - N ) </code>
     *                               is invoked.
     *
     * @return This CountableFilter, with only the specified number
     *         of elements (if any) kept from the end.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public abstract CountableFilter<VALUE> keepLast (
                                                     long num_elements_to_keep
                                                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;




    /** Contract which is violated whenever remove ( ... ) receives
     *  Abnormal input. */
    public static class Remove
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for remove ( ... ) operations. */
        public static final Remove CONTRACT =
            new Remove ();
    }

    /** Contract which is violated whenever remove ( ... ) does not
     *  produce any results. */
    public static class MustNotBeEmptyAfterRemove
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for remove ( ... ) operations. */
        public static final MustNotBeEmptyAfterRemove CONTRACT =
            new MustNotBeEmptyAfterRemove ();
    }

    /**
     * <p>
     * Removes all instance(s) of the specific element(s) from this
     * countable view.
     * </p>
     *
     * <p>
     * For example, starting from the countable elements
     * <code> { A, B, C, B, A } </code>, removing the elements
     * <code> { C, A } </code> will remove the one <code> C </code> and the
     * two <code> A </code> elements, resulting in <code> { B, B } </code>.
     * </p>
     *
     * @param elements_to_remove The element(s) to remove.
     *                           If any element is found more than once,
     *                           then all instances are removed.
     *                           Must not be null.
     *
     * @return This countable elements, with the specified element(s)
     *         removed.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableFilter constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public abstract CountableFilter<VALUE> remove (
                                                   Countable<VALUE> elements_to_remove
                                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever removeFirst ( ... ) does not
     *  produce any results. */
    public static class MustNotBeEmptyAfterRemoveFirst
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for removeFirst ( ... ) operations. */
        public static final MustNotBeEmptyAfterRemoveFirst CONTRACT =
            new MustNotBeEmptyAfterRemoveFirst ();
    }

    /**
     * <p>
     * Removes the specified number of elements from the beginning of this
     * Countable elements.
     * </p>
     *
     * @param num_elements_to_remove The number of elements to remove.
     *                               If 0L or less then nothing is done.
     *                               If greater than the number of elements
     *                               in this Countable elements then
     *                               all elements are cleared.
     *
     * @return This Countable elements.  Never null.
     */
    public abstract CountableFilter<VALUE> removeFirst (
                                                        long num_elements_to_remove
                                                        )
        throws ReturnNeverNull.Violation;


    /** Contract which is violated whenever removeLast ( ... ) does not
     *  produce any results. */
    public static class MustNotBeEmptyAfterRemoveLast
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for removeLast ( ... ) operations. */
        public static final MustNotBeEmptyAfterRemoveLast CONTRACT =
            new MustNotBeEmptyAfterRemoveLast ();
    }

    /**
     * <p>
     * Remove the specified number of elements from the end of this
     * Countable elements.
     * </p>
     *
     * @param num_elements_to_remove The number of elements to remove.
     *                               If 0L or less then nothing is done.
     *                               If greater than the number of elements
     *                               in this Countable elements then
     *                               all elements are cleared.
     *
     * @return This Countable elements.  Never null.
     *
     * <p>
     * Final for speed.
     * </p>
     */
    public abstract CountableFilter<VALUE> removeLast (
                                                       long num_elements_to_remove
                                                       )
                      throws ReturnNeverNull.Violation;
}
