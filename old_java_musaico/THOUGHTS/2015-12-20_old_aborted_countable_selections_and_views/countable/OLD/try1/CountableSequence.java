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
public interface CountableSequence<VALUE extends Object, VIEW_ITEM extends Object, VIEW extends CountableView<VALUE, VIEW_ITEM, VIEW>>
    extends CountableMutableView<VALUE, VIEW_ITEM, VIEW> Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;




    /** Contract which is violated whenever insert ( ... ) receives
     *  Abnormal input. */
    public static class Insert
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for insert ( ... ) operations. */
        public static final Insert CONTRACT =
            new Insert ();
    }

    /** Contract which is violated whenever insert ( ... ) does not
     *  produce any results. */
    public static class MustHaveInserted
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for insert ( ... ) operations. */
        public static final MustHaveInserted CONTRACT =
            new MustHaveInserted ();
    }

    /**
     * <p>
     * Adds the specified countable value(s) to (each of) the specified
     * location(s) in the value being built.
     * </p>
     *
     * <p>
     * For example, if the value being built is <code> 1, 2, 3, 4, 5 </code>
     * and the sub-value <code> A, B </code> is inserted at
     * index <code> { 2 } </code> then the resulting value being built
     * will be <code> { 1, 2, A, B, 3, 4, 5 } </code>.
     * </p>
     *
     * <p>
     * Or if the same sub-value is inserted into the same value being
     * built at indices <code> { 0, 1, 2, 3, 4, 5 } </code> then
     * the resulting value being built will be
     * <code> { A, B, 1, A, B, 2, A, B, 3, A, B, 4, A, B, 5, A, B } <code>.
     * </p>
     *
     * @param sub_value The element(s) to insert into the value being
     *                  built.  Must not be null.
     *
     * @param insert_at_indices The location(s) in the value being built
     *                          at which to insert the specified sub-value.
     *                          If an index is less than or equal to 0L
     *                          or greater than or equal to the length
     *                          of the countable value being built
     *                          then it is ignored.  If an index is greater
     *                          than <code> Integer.MAX_VALUE </code>
     *                          then the sub-value is appended AFTER the
     *                          element counted backward from
     *                          <code> Countable.LAST </code>.
     *                          If an index appears more
     *                          than once then the sub-values will be
     *                          inserted multiple times at the same
     *                          location.  Must not be null.
     *
     * @return This countable elements, with the sub-value(s) inserted
     *         at the specified index/indices.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If either of the specified values is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public abstract CountableElements<VALUE> insert (
                                                     Countable<VALUE> sub_value,
                                                     Countable<Long> insert_at_indices
                                                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;




    /** Contract which is violated whenever pad ( ... ) receives
     *  Abnormal input. */
    public static class Pad
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for pad ( ... ) operations. */
        public static final Pad CONTRACT =
            new Pad ();
    }

    /** Contract which is violated whenever pad ( ... ) does not
     *  produce any results. */
    public static class MustHavePadding
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for pad ( ... ) operations. */
        public static final MustHavePadding CONTRACT =
            new MustHavePadding ();
    }

    /**
     * <p>
     * Inserts padding elements to make the value being built
     * a specific length.
     * </p>
     *
     * <p>
     * For example, if the countable elements <code> { 1, 2, 3 } </code>
     * is padded with element <code> { 0 } </code>
     * at index <code> { 0 } </code> to reach target length <code> 6 </code>,
     * the result will be <code> { 0, 0, 0, 1, 2, 3 } </code>.
     * </p>
     *
     * <p>
     * If there are multiple padding elements then each will be added
     * in sequence, followed by a repeat of the sequence, and so on,
     * until one of the elements is added to reach the final target length.
     * For example, starting from countable elements
     * <code> { 1, 2, 3 } </code>, padding the start index <code> { 0 } </code>
     * with <code> { A, B, C } </code> to reach target length <code> 7 </code>
     * would result in <code> { A, B, C, A, 1, 2, 3 } </code>.
     * </p>
     *
     * <p>
     * If the padding is to be inserted at multiple locations, then
     * one sequence of padding is inserted at the first index, then
     * one sequence at the seond index, and so on, until one sequence
     * has been inserted at each of the specified indices; then another
     * sequence is inserted at each index; and so on, until one of the
     * values inserted at one of the elements causes the countable elements
     * to reach the target length.  For example, with countable
     * countable elements <code> { 1, 2, 3 } </code>, padding both the
     * start and the end indices <code> { 0, 3 } </code> with
     * padding <code> { A, B, C } </code> to reach target length
     * <code> 11 </code> would result in
     * <code> { A, B, C, A, B, 1, 2, 3, A, B, C } </code>.
     * </p>
     *
     * @param padding The element(s) to insert at specific indices
     *                in this countable elements.  Must not be null.
     *
     * @param pad_at_indices Where to insert the padding in the
     *                       value being built.  Indices less than 0L
     *                       or greater than or equal to the length
     *                       of the countable value being built are
     *                       ignored.  An index greater than
     *                       <code> Integer.MAX_VALUE </code>
     *                       causes the the padding to be appended
     *                       AFTER the index counting backward from
     *                       <code> Countable.LAST </code>.
     *                       Must not be null.
     *
     * @param target_length How many elements this countable elements
     *                      should have after the padding has
     *                      been inserted.  If less than or equal to
     *                      the length of the value being built
     *                      (for example, if the target length is 0 or
     *                      or -1 or length - 1) then no padding
     *                      will be added.
     *
     * @return This CountableElements, with the specified padding added.
     *         However if this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If either of the specified values is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public abstract CountableElements<VALUE> pad (
                                                  Countable<VALUE> padding,
                                                  Countable<Long> pad_at_indices,
                                                  long target_length
                                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever range ( ... ) receives
     *  Abnormal input. */
    public static class Range
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for range ( ... ) operations. */
        public static final Range CONTRACT =
            new Range ();
    }

    /** Contract which is violated whenever range ( ... ) does not
     *  produce any results. */
    public static class MustBeInRange
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for range ( ... ) operations. */
        public static final MustBeInRange CONTRACT =
            new MustBeInRange ();
    }

    /**
     * <p>
     * Returns all elements in the given range(s).
     * </p>
     *
     * <p>
     * Each range is a pair of indices.
     * </p>
     *
     * <p>
     * For example, a Countable elements with elements
     * { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, when the ranges
     * { 0, 2, 7, 9 } (0-2, 7-9) are requested, would result in:
     * { 1, 2, 3, 8, 9, 10 }.
     * </p>
     *
     * <p>
     * If the indices range is empty, then the result is also empty.
     * </p>
     *
     * <p>
     * If the last range has only one index (i.e. the length of the
     * indices parameter % 2 is 1) then the final range is treated
     * as ( last_specified_index .. last_index_in_elements ).
     * </p>
     *
     * @param range_pairs The range(s) of elements to return.
     *                    Must not be null.
     *
     * @return This Countable elements, containing only
     *         the elements in the specified index ranges.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public abstract CountableElements<VALUE> range (
                                                    Countable<Long> range_pairs
                                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;




    /** Contract which is violated whenever repeat ( ... ) does not
     *  produce any results. */
    public static class MustBeRepeated
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for repeat ( ... ) operations. */
        public static final MustBeRepeated CONTRACT =
            new MustBeRepeated ();
    }

    /**
     * <p>
     * Creates a new Countable elements which contains the same
     * element(s) as this value, repeated the specified number
     * of times.
     * </p>
     *
     * <p>
     * Specifying 1 repeat leaves the elements as-is.
     * </p>
     *
     * <p>
     * For example, if this is a CountableElements<Integer> whose
     * elements are <code> { A, B } </code>, then calling
     * <code> repeat ( 3 ) </code> would result in a new Countable
     * with elements <code> { A, B, A, B, A, B } </code>.
     * </p>
     *
     * @param repetitions The number of times to repeat the pattern
     *                    of elements.  If the number is less than
     *                    or equal to 1L then it is ignored and no
     *                    repetitions are performed.
     *
     * @return This countable elements, with its elements
     *         repeated the specified number of times.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public abstract CountableElements<VALUE> repeat (
                                                     long repetitions
                                                     )
        throws ReturnNeverNull.Violation;
}
