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
    extends CountableModify<VALUE, VIEW_ITEM, VIEW> Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    !!!!!!!!!!!!!!!!!!!!!!! Implement CountableModify methods,
        !!!!!!!!!!!!!!!!!!! this is just for posterity:




    /** Contract which is violated whenever replace ( ... ) receives
     *  Abnormal input. */
    public static class Replace
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for replace ( ... ) operations. */
        public static final Replace CONTRACT =
            new Replace ();
    }

    /** Contract which is violated whenever replace ( ... ) does not
     *  produce any results. */
    public static class MustHaveReplacement
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for replace ( ... ) operations. */
        public static final MustHaveReplacement CONTRACT =
            new MustHaveReplacement ();
    }

    /**
     * <p>
     * Replaces any and all instances of the specified sub-value
     * with the specified replacement sub-value.
     * </p>
     *
     * <p>
     * For example, replacing all instances of <code> { B, C, D } </code>
     * with replacement <code> { X } </code> inside the countable elements
     * <code> { A, B, C, D, A, B, C, D, E } </code> would result
     * in <code> { A, X, A, X, E } </code>.
     * </p>
     *
     * @param sub_value The elements to find and replace in this
     *                  countable elements.  Must not be null.
     *
     * @param replacement The elements to insert in place of each matching
     *                    sequence.  Can be empty.  Must not be null.
     *
     * @return This countable elements, with each and every instance
     *         of the specified sub value replaced by the specified
     *         replacement sequence.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If either of the specified values is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public abstract CountableElements<VALUE> replace (
                                                      Countable<VALUE> sub_value,
                                                      Countable<VALUE> replacement
                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever replaceIndices ( ... ) receives
     *  Abnormal input. */
    public static class ReplaceIndices
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for replaceIndices ( ... ) operations. */
        public static final ReplaceIndices CONTRACT =
            new ReplaceIndices ();
    }

    /** Contract which is violated whenever replaceIndices ( ... ) does not
     *  produce any results. */
    public static class MustHaveReplacedIndices
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for replaceIndices ( ... ) operations. */
        public static final MustHaveReplacedIndices CONTRACT =
            new MustHaveReplacedIndices ();
    }

    /**
     * <p>
     * Replaces one or more element(s) at the specified indices with the
     * specified replacement sub-value.
     * </p>
     *
     * <p>
     * For example, 1 element at each index  <code> { 0, 2, LAST } </code>
     * with replacement <code> { X, Y } </code> inside the
     * <code> { A, B, C, D, E } </code> would result
     * in <code> { X, Y, B, X, Y, D, X, Y } </code>.
     * </p>
     *
     * @param num_elements_to_replace The number of elements to replace
     *                                at each index.  If less than
     *                                or equal to 0 then this method
     *                                does nothing.
     *
     * @param indices The indices of the elements to replace in this
     *                countable elements.  Must not be null.
     *
     * @param replacement The elements to insert in place of the element
     *                    at each specified index.  Can be empty.
     *                    Must not be null.
     *
     * @return This countable elements, with the element(s) at
     *         each index  replaced by the specified replacement sequence.
     *         If num_elements_to_replace is less than or equal to 0 then
     *         this countable elements is returned as-is.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If either of the specified values is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public abstract CountableElements<VALUE> replaceIndices (
                                                             int num_elements_to_replace,
                                                             Countable<Long> indices,
                                                             Countable<VALUE> replacement
                                                             )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
