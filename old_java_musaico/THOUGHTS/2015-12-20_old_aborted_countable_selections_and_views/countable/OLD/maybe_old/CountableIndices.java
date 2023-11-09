package musaico.foundation.value;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.domains.BoundedDomain;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;


/**
 * <p>
 * A mutable offshoot from a Countable value, which can be used to filter,
 * sort, replace, and so on, and build new Countable values.
 * </p>
 *
 * <p>
 * A read-only view of a Countable value, which can be used to filter,
 * find and so on the indices of a Countable value, but will never
 * change the elements.
 * </p>
 *
 *
 * <p>
 * In Java every CountableIndices must be Serializable in order to
 * play nicely across RMI.  However users of the CountableIndices
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
 * @see musaico.foundation.value.MODULE#COPYRIGHT
 * @see musaico.foundation.value.MODULE#LICENSE
 */
public interface CountableIndices<VALUE extends Object>
    extends CountableView<VALUE, Long, CountableIndices<VALUE>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;




    /** Contract which is violated whenever clamp ( ... ) receives
     *  Abnormal input. */
    public static class Clamp
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for clamp ( ... ) operations. */
        public static final Clamp CONTRACT =
            new Clamp ();
    }

    /** Contract which is violated whenever clamp ( ... ) does not
     *  produce any results. */
    public static class MustHaveClampedIndices
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for clamp ( ... ) operations. */
        public static final MustHaveClampedIndices CONTRACT =
            new MustHaveClampedIndices ();
    }

    /**
     * <p>
     * Returns only the indices specified which fall into the range
     * of element indices in this countable indices.
     * </p>
     *
     * <p>
     * The <code> Countable.LAST </code> special index is clamped
     * as the actual index of the last element in the
     * countable value being built (if any).
     * </p>
     *
     * <p>
     * For example, if this countable indices contains 3 elements
     * (indices 0, 1 and 2) then clamping the countable indices
     * { -2, 0, 2, 3, 4, 5 } would result in the indices { 0, 2 },
     * since { -2, 3, 4, 5 } are all invalid indices for the
     * countable indices.
     * </p>
     *
     * @param indices Zero or more indices to clamp.  Must not be null.
     *
     * @return Zero or more of the specified indices which fall in the
     *         range of valid indices for this countable indices.
     *         If the specified indices are No value or an Error or
     *         a Warning then they are returned as-is.
     *         If this Countable indices was constructed from
     *         an Error or Warning then one such is returned.
     *         Never null.
     */
!!!!!!!!!!!!!!!!!!!!!!!!! where to put clamp?  filter?  sequence?
    public abstract Countable<Long> clamp (
                                           Countable<Long> indices
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Determines whether this countable indices contains all
     * of the specified indices.
     * </p>
     *
     * <p>
     * No indices is always considered to be contained inside any
     * Countable indices (including an empty countable indices) of the
     * same value class.  Neither Warnings no Errors are ever
     * contained within any Countable indices.  And no Multiple is ever
     * contained by any empty or single-index countable indices.
     * </p>
     *
     * @param indices The indices to search for.  Must not be null.
     *
     * @return True if the specified sub-value is nested anywhere inside
     *         the elements of this countable indices; false if it does
     *         not appear anywhere in this value.
     *         Always false if this Countable indices was constructed
     *         from an Error or Warning.  Always false if the specified
     *         sub-value is an Error or Warning.
     */
    public abstract boolean contains (
                                      Countable<Long> indices
                                      )
        throws ParametersMustNotBeNull.Violation;


    /**
     * @return A newly created copy of this CountableIndices, which
     *         can be modified independently of this one.
     *         Never null.
     */
    public abstract CountableIndices<VALUE> duplicate ()
        throws ReturnNeverNull.Violation;


    /** Contract which is violated whenever filter ( ... ) receives
     *  Abnormal input. */
    public static class FilterAll
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for filter ( ... ) operations. */
        public static final FilterAll CONTRACT =
            new FilterAll ();
    }

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
     * Returns the indices of all elements matching the specified filter.
     * </p>
     *
     * @param filter The Filter which will keep or discard the
     *               element(s) from this Countable indices.
     *               Must not be null.
     *
     * @return A Countable value containing the indices of all matching
     *         elements from this countable indices
     *         Can result in empty indices countable value, if
     *         no element(s) were kept by the filter.
     *         If this Countable indices was constructed from
     *         an Error or Warning then one such is returned.
     *         Never null.
     */
    public abstract Countable<Long> filter (
                                            Filter<VALUE> filter
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever filterFirst ( ... ) receives
     *  Abnormal input. */
    public static class FilterFirst
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for filterFirst ( ... ) operations. */
        public static final FilterFirst CONTRACT =
            new FilterFirst ();
    }

    /** Contract which is violated whenever filterFirst ( ... ) does not
     *  produce any results. */
    public static class MustMatchFirstFilter
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for filterFirst ( ... ) operations. */
        public static final MustMatchFirstFilter CONTRACT =
            new MustMatchFirstFilter ();
    }

    /**
     * <p>
     * Returns the first index of an element from this
     * countable indices which matches the specified filter.
     * </p>
     *
     * @param filter The Filter which will keep or discard the
     *               element(s) from this Countable indices.
     *               Must not be null.
     *
     * @return Either the first One index matching the filter, or No
     *         matching index.
     *         If this Countable indices was constructed from
     *         an Error or Warning then No matching index is always returned.
     *         Never null.
     */
    public abstract ZeroOrOne<Long> filterFirst (
                                                 Filter<VALUE> filter
                                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever filterLast ( ... ) receives
     *  Abnormal input. */
    public static class FilterLast
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for filterLast ( ... ) operations. */
        public static final FilterLast CONTRACT =
            new FilterLast ();
    }

    /** Contract which is violated whenever filterLast ( ... ) does not
     *  produce any results. */
    public static class MustMatchLastFilter
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for filterLast ( ... ) operations. */
        public static final MustMatchLastFilter CONTRACT =
            new MustMatchLastFilter ();
    }

    /**
     * <p>
     * Returns the last index of an element from this
     * countable indices which matches the specified filter.
     * </p>
     *
     * @param filter The Filter which will keep or discard the
     *               element(s) from this Countable indices.
     *               Must not be null.
     *
     * @return Either the last One index matching the filter, or No
     *         matching index.
     *         If this Countable indices was constructed from
     *         an Error or Warning then No matching index is always returned.
     *         Never null.
     */
    public abstract ZeroOrOne<Long> filterLast (
                                                Filter<VALUE> filter
                                                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever find ( ... ) receives
     *  Abnormal input. */
    public static class Find
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for find ( ... ) operations. */
        public static final Find CONTRACT =
            new Find ();
    }

    /** Contract which is violated whenever find ( ... ) does not
     *  produce any results. */
    public static class MustMatchFind
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for find ( ... ) operations. */
        public static final MustMatchFind CONTRACT =
            new MustMatchFind ();
    }

    /**
     * <p>
     * Returns all indices of the elements in this Countable indices
     * which begin the specified sub-region of elements.
     * </p>
     *
     * <p>
     * For example, calling <code> find ( { 1, 2 } ) </code>
     * on the repeating Countable indices
     * <code> { 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3 } </code>
     * would return the indices <code> { 1, 5, 9 } </code>, since
     * the sub-values at indices 1-2, 5-6 and 9-10 all match.
     * </p>
     *
     * <p>
     * Calling find with any Unjust parameter value (No, Error,
     * Warning and so on) always results in an Unjust return value.
     * In particular <code> find ( No value ) </code> always
     * returns No indices.
     * </p>
     *
     * @param sub_value The sub-sequence of elements to search for.
     *                  Must not be null.
     *
     * @return The indices at which sequences of the specified sub-value
     *         were found, or No indices if it was not found,
     *         or an Error or Warning
     *         if this countable indices was built from one.
     *         If the specified sub-value is an Error or Warning, then
     *         one such is returned.
     *         Never null.
     */
    public abstract Countable<Long> find (
                                          Countable<VALUE> sub_value
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever findFirst ( ... ) receives
     *  Abnormal input. */
    public static class FindFirst
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for findFirst ( ... ) operations. */
        public static final FindFirst CONTRACT =
            new FindFirst ();
    }

    /** Contract which is violated whenever findFirst ( ... ) does not
     *  produce any results. */
    public static class MustMatchFindFirst
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for findFirst ( ... ) operations. */
        public static final MustMatchFindFirst CONTRACT =
            new MustMatchFindFirst ();
    }

    /**
     * <p>
     * Returns the first index of the elements in this Countable indices
     * which begin the specified sub-region of elements.
     * </p>
     *
     * <p>
     * For example, calling <code> findFirst ( { 1, 2 } ) </code>
     * on the repeating Countable indices
     * <code> { 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3 } </code>
     * would return the index <code> { 1 } </code>, since
     * the sub-values at indices 1-2 match.
     * </p>
     *
     * <p>
     * Calling findFirst with any Unjust parameter value (No, Error,
     * Warning and so on) always results in No return value.
     * </p>
     *
     * @param sub_value The sub-sequence of elements to search for.
     *                  Must not be null.
     *
     * @return The first index at which the specified sub-value was found,
     *         or No indices if it was not found, or No index
     *         if this countable indices was built from one.
     *         If the specified sub-value is an Error or Warning, then
     *         No index is returned.
     *         Never null.
     */
    public abstract ZeroOrOne<Long> findFirst (
                                               Countable<VALUE> sub_value
                                               )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever findLast ( ... ) receives
     *  Abnormal input. */
    public static class FindLast
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for findLast ( ... ) operations. */
        public static final FindLast CONTRACT =
            new FindLast ();
    }

    /** Contract which is violated whenever findLast ( ... ) does not
     *  produce any results. */
    public static class MustMatchFindLast
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for findLast ( ... ) operations. */
        public static final MustMatchFindLast CONTRACT =
            new MustMatchFindLast ();
    }

    /**
     * <p>
     * Returns the last index of the elements in this Countable indices
     * which begin the specified sub-region of elements.
     * </p>
     *
     * <p>
     * For example, calling <code> findLast ( { 1, 2 } ) </code>
     * on the repeating Countable indices
     * <code> { 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3 } </code>
     * would return the index <code> { 9 } </code>, since
     * the sub-values at indices 9-10 match.
     * </p>
     *
     * <p>
     * Calling findLast with any Unjust parameter value (No, Error,
     * Warning and so on) always results in No return value.
     * </p>
     *
     * @param sub_value The sub-sequence of elements to search for.
     *                  Must not be null.
     *
     * @return The last index at which the specified sub-value was found,
     *         or No indices if it was not found, or No index
     *         if this countable indices was built from one.
     *         If the specified sub-value is an Error or Warning, then
     *         No index is returned.
     *         Never null.
     */
    public abstract ZeroOrOne<Long> findLast (
                                              Countable<VALUE> sub_value
                                              )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever first ( ... ) receives
     *  Abnormal input. */
    public static class First
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for first ( ... ) operations. */
        public static final First CONTRACT =
            new First ();
    }

    /** Contract which is violated whenever first ( ... ) does not
     *  produce any results. */
    public static class MustHaveFirst
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for first ( ... ) operations. */
        public static final MustHaveFirst CONTRACT =
            new MustHaveFirst ();
    }

    /**
     * @return The first index in this countable indices,
     *         or No index if this countable indices has no
     *         elements.
     *         If this Countable indices was constructed from
     *         an Error or Warning then No first index is returned.
     *         Never null.
     */
    public abstract ZeroOrOne<Long> first ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever firstAndLast ( ... ) receives
     *  Abnormal input. */
    public static class FirstAndLast
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for firstAndLast ( ... ) operations. */
        public static final FirstAndLast CONTRACT =
            new FirstAndLast ();
    }

    /** Contract which is violated whenever firstAndLast ( ... )
     *  does not produce any results. */
    public static class MustHaveFirstAndLast
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for firstAndLast ( ... ) operations. */
        public static final MustHaveFirstAndLast CONTRACT =
            new MustHaveFirstAndLast ();
    }

    /**
     * <p>
     * Returns the first then last indices of this countable indices.
     * </p>
     *
     * @return The first index then last index of this countable indices,
     *         or no indices at all if this countable indices has
     *         no elements.
     *         If this Countable indices was constructed from
     *         an Error or Warning then one such is returned.
     *         Never null.
     */
    public abstract Countable<Long> firstAndLast ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever index ( ... ) receives
     *  Abnormal input. */
    public static class Index
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for index ( ... ) operations. */
        public static final Index CONTRACT =
            new Index ();
    }

    /** Contract which is violated whenever index ( ... ) does not
     *  produce any results. */
    public static class MustHaveIndex
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for index ( ... ) operations. */
        public static final MustHaveIndex CONTRACT =
            new MustHaveIndex ();
    }

    /**
     * <p>
     * Returns the specified index from the countable value being
     * built (if any), or No index if it is not within range.
     * </p>
     *
     * @param index The index to convert.  If <code> Countable.LAST </code>
     *              then the last index of the countable value being built
     *              (if any) shall be returned.  If less than 0L or
     *              greater than or equal to the length of the
     *              countable value being built then No index
     *              shall be returned.
     *
     * @return The specified index, or No index if it is out of range.
     *         If this Countable indices was constructed from
     *         an Error or Warning then No index is returned.
     *         Never null.
     */
    public abstract ZeroOrOne<Long> index (
                                           long index
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever instancesOf ( ... )
     *  receives Abnormal input. */
    public static class InstancesOf
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for find ( ... ) operations. */
        public static final InstancesOf CONTRACT =
            new InstancesOf ();
    }

    /** Contract which is violated whenever instancesOf ( ... )
     *  does not produce any results. */
    public static class MustHaveInstancesOf
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for instancesOf ( ... ) operations. */
        public static final MustHaveInstancesOf CONTRACT =
            new MustHaveInstancesOf ();
    }

    /**
     * <p>
     * Returns all indices of the elements in this Countable indices
     * which are instances of the specified class.
     * </p>
     *
     * <p>
     * For example, calling
     * <code> instancesOf ( BigDecimal.class ) </code>
     * on the Countable indices
     * <code> { (Integer) 0, (BigDecimal) 1, (Long) 2L, (BigDecimal) 3,
     *          (Double) 4 } </code>
     * would return the indices <code> { 1, 3 } </code>, since
     * the elements at indices 0 and 3 are both BigDecimals.
     * </p>
     *
     * <p>
     * Calling instancesOf with any Unjust parameter value (No, Error,
     * Warning and so on) always results in an Unjust return value.
     * In particular <code> instancesOf ( No value ) </code> always
     * returns No indices.
     * </p>
     *
     * @param instance_of_class The class of elements to find in
     *                          this countable indices.
     *                          Must not be null.
     *
     * @return The indices at which elements of the specified class
     *         were found,
     *         or No indices if no such instance was not found,
     *         or an Error or Warning
     *         if this countable indices was built from one.
     *         Never null.
     */
    public abstract Countable<Long> instancesOf (
                                                 Class<? extends VALUE> instance_of_class
                                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever instanceOfFirst ( ... )
     *  receives Abnormal input. */
    public static class InstanceOfFirst
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for instanceOfFirst ( ... ) operations. */
        public static final InstanceOfFirst CONTRACT =
            new InstanceOfFirst ();
    }

    /** Contract which is violated whenever instanceOfFirst ( ... )
     *  does not produce any results. */
    public static class MustHaveFirstInstanceOf
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for instanceOfFirst ( ... ) operations. */
        public static final MustHaveFirstInstanceOf CONTRACT =
            new MustHaveFirstInstanceOf ();
    }

    /**
     * <p>
     * Returns the first index of the elements in this Countable indices
     * which begin the specified sub-region of elements.
     * </p>
     *
     * <p>
     * For example, calling <code> instanceOfFirst ( { 1, 2 } ) </code>
     * on the repeating Countable indices
     * <code> { 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3 } </code>
     * would return the index <code> { 1 } </code>, since
     * the sub-values at indices 1-2 match.
     * </p>
     *
     * <p>
     * Calling instanceOfFirst with any Unjust parameter value (No, Error,
     * Warning and so on) always results in No return value.
     * </p>
     *
     * @param instance_of_class The class of elements to find in
     *                          this countable indices.
     *                          Must not be null.
     *
     * @return The first index at which the specified sub-value was found,
     *         or No indices if it was not found, or No index
     *         if this countable indices was built from one.
     *         If the specified sub-value is an Error or Warning, then
     *         No index is returned.
     *         Never null.
     */
    public abstract ZeroOrOne<Long> instanceOfFirst (
                                                     Class<? extends VALUE> instance_of_class
                                                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever instanceOfLast ( ... ) receives
     *  Abnormal input. */
    public static class InstanceOfLast
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for instanceOfLast ( ... ) operations. */
        public static final InstanceOfLast CONTRACT =
            new InstanceOfLast ();
    }

    /** Contract which is violated whenever instanceOfLast ( ... ) does not
     *  produce any results. */
    public static class MustHaveLastInstanceOf
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for instanceOfLast ( ... ) operations. */
        public static final MustHaveLastInstanceOf CONTRACT =
            new MustHaveLastInstanceOf ();
    }

    /**
     * <p>
     * Returns the last index of the elements in this Countable indices
     * which begin the specified sub-region of elements.
     * </p>
     *
     * <p>
     * For example, calling <code> instanceOfLast ( { 1, 2 } ) </code>
     * on the repeating Countable indices
     * <code> { 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3 } </code>
     * would return the index <code> { 9 } </code>, since
     * the sub-values at indices 9-10 match.
     * </p>
     *
     * <p>
     * Calling instanceOfLast with any Unjust parameter value (No, Error,
     * Warning and so on) always results in No return value.
     * </p>
     *
     * @param sub_value The sub-sequence of elements to search for.
     *                  Must not be null.
     *
     * @return The last index at which the specified sub-value was found,
     *         or No indices if it was not found, or No index
     *         if this countable indices was built from one.
     *         If the specified sub-value is an Error or Warning, then
     *         No index is returned.
     *         Never null.
     */
    public abstract ZeroOrOne<Long> instanceOfLast (
                                                    Class<? extends VALUE> instance_of_class
                                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever last ( ... ) receives
     *  Abnormal input. */
    public static class Last
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for last ( ... ) operations. */
        public static final Last CONTRACT =
            new Last ();
    }

    /** Contract which is violated whenever last ( ... ) does not
     *  produce any results. */
    public static class MustHaveLast
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for last ( ... ) operations. */
        public static final MustHaveLast CONTRACT =
            new MustHaveLast ();
    }

    /**
     * @return The last index in this countable indices,
     *         or No index if this countable indices has no
     *         elements.
     *         If this Countable indices was constructed from
     *         an Error or Warning then No last index is returned.
     *         Never null.
     */
    public abstract ZeroOrOne<Long> last ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /** Contract which is violated whenever lastAndFirst ( ... ) receives
     *  Abnormal input. */
    public static class LastAndFirst
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for lastAndFirst ( ... ) operations. */
        public static final LastAndFirst CONTRACT =
            new LastAndFirst ();
    }

    /** Contract which is violated whenever lastAndFirst ( ... )
     *  does not produce any results. */
    public static class MustHaveLastAndFirst
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for lastAndFirst ( ... ) operations. */
        public static final MustHaveLastAndFirst CONTRACT =
            new MustHaveLastAndFirst ();
    }

    /**
     * <p>
     * Returns the last then first indices of this countable indices.
     * </p>
     *
     * <p>
     * This is a convenience method, shorthand for
     * <code> firstAndlast ().reverse () </code>.
     * </p>
     *
     * @return The last index then first index of this countable indices,
     *         or no indices at all if this countable indices has
     *         no elements.
     *         If this Countable indices was constructed from
     *         an Error or Warning then one such is returned.
     *         Never null.
     */
    public abstract Countable<Long> lastAndFirst ()
        throws ReturnNeverNull.Violation;
}
