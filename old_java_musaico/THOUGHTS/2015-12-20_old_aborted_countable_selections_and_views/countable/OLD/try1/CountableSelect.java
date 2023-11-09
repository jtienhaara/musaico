package musaico.foundation.value;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * A CountableView with methods to retrieve or inspect
 * countable view items (elements or indices and so on).
 * </p>
 *
 * <p>
 * The CountableSelect is mutable.  It potentially changes
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
public interface CountableSelect<VALUE extends Object, VIEW_ITEM extends Object, VIEW extends CountableView<VALUE, VIEW_ITEM, VIEW>>
    extends CountableImmutableView<VALUE, VIEW_ITEM, VIEW> Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;




    /** Contract which is violated whenever all ( ... ) receives
     *  Abnormal input. */
    public static class All
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for all ( ... ) operations. */
        public static final All CONTRACT =
            new All ();
    }

    /** Contract which is violated whenever all ( ... ) does not
     *  produce any results. */
    public static class MustHaveAll
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for all ( ... ) operations. */
        public static final MustHaveAll CONTRACT =
            new MustHaveAll ();
    }

    /**
     * <p>
     * Returns all view item(s) (elements or indices and so on).
     * </p>
     *
     * @return A new Countable value containing all view items
     *         (elements or indices and so on) from this CountableView.
     *         If this CountableView was constructed from
     *         an Error or Warning then a new such will be returned.
     *         Never null.
     */
    public abstract Countable<VIEW_ITEM> all ()
        throws ReturnNeverNull.Violation;




    /** Contract which is violated whenever one ( ... ) receives
     *  Abnormal input. */
    public static class One
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for one ( ... ) operations. */
        public static final One CONTRACT =
            new One ();
    }

    /** Contract which is violated whenever one ( ... ) does not
     *  produce any results. */
    public static class MustHaveOne
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for one ( ... ) operations. */
        public static final MustHaveOne CONTRACT =
            new MustHaveOne ();
    }

    /**
     * <p>
     * Returns exactly One view item (element / index / and so on)
     * if this view has one view item, or returns No view items if
     * this view has less or more than one view items.
     * </p>
     *
     * @return The One view item in this view, or No view item if this
     *         view does not have exactly one view item.
     *         Never null.
     */
    public abstract ZeroOrOne<VIEW_ITEM> one ()
        throws ReturnNeverNull.Violation;




    /**
     * <p>
     * Determines whether this countable view contains all elements of
     * the specified sub-value, in the same order, with possibly
     * other view items before and/or after the embedded sub-value.
     * </p>
     *
     * <p>
     * No value is always considered to be contained inside any
     * Countable view (including an empty countable view) of the
     * same value class.  Neither Warnings no Errors are ever
     * contained within any Countable view.  And no Multiple is ever
     * contained by any empty or single-item countable view.
     * </p>
     *
     * @param sub_value The countable value to search for.  Must not be null.
     *
     * @return True if the specified sub-value is nested anywhere inside
     *         the view items of this countable view; false if it does
     *         not appear anywhere in this countable view.
     *         Always false if this Countable view was constructed
     *         from an Error or Warning.  Always false if the specified
     *         sub-value is an Error or Warning.
     */
    public abstract boolean contains (
                                      Countable<VIEW_ITEM> sub_value
                                      )
        throws ParametersMustNotBeNull.Violation;




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
     * @return The One very first view item (element or index and so on),
     *         or No view item if this countable view is empty.
     *         Never null.
     */
    public abstract ZeroOrOne<VIEW_ITEM> first ()
        throws ReturnNeverNull.Violation;




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
     * @return The One very last view item (element or index and so on),
     *         or No view item if this countable view is empty.  Never null.
     */
    public abstract ZeroOrOne<VIEW_ITEM> last ()
        throws ReturnNeverNull.Violation;




    /** Contract which is violated whenever maximum ( ... ) receives
     *  Abnormal input. */
    public static class Maximum
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for maximum ( ... ) operations. */
        public static final Maximum CONTRACT =
            new Maximum ();
    }

    /** Contract which is violated whenever maximum ( ... ) does not
     *  produce any results. */
    public static class MustHaveMaximum
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for maximum ( ... ) operations. */
        public static final MustHaveMaximum CONTRACT =
            new MustHaveMaximum ();
    }

    /**
     * <p>
     * Returns the highest view item (element / index / and so on)
     * in this countable view according to the specified Order.
     * </p>
     *
     * <p>
     * If there is a tie, then two or more view items will be returned.
     * </p>
     *
     * @param order The sort order which is used to compare view items.
     *              Must not be null.
     *
     * @return The maximum view item (element / index / and so on)
     *         in this countable view according to the specified order.
     *         Never null.
     */
    public abstract Countable<VIEW_ITEM> maximum ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;




    /** Contract which is violated whenever median ( ... ) receives
     *  Abnormal input. */
    public static class Median
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for median ( ... ) operations. */
        public static final Median CONTRACT =
            new Median ();
    }

    /** Contract which is violated whenever median ( ... ) does not
     *  produce any results. */
    public static class MustHaveMedian
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for median ( ... ) operations. */
        public static final MustHaveMedian CONTRACT =
            new MustHaveMedian ();
    }

    /**
     * <p>
     * Returns the median view item(s) (element / index / and so on)
     * in this countable view according to the specified Order.
     * </p>
     *
     * <p>
     * If there is a tie, then two or more view items will be returned.
     * </p>
     *
     * @param order The sort order which is used to compare view items.
     *              Must not be null.
     *
     * @return The median view item (element / index / and so on)
     *         in this countable view according to the specified order.
     *         Never null.
     */
    public abstract Countable<VIEW_ITEM> median ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;




    /** Contract which is violated whenever middle ( ... ) receives
     *  Abnormal input. */
    public static class Middle
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for middle ( ... ) operations. */
        public static final Middle CONTRACT =
            new Middle ();
    }

    /** Contract which is violated whenever middle ( ... ) does not
     *  produce any results. */
    public static class MustHaveMiddle
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for middle ( ... ) operations. */
        public static final MustHaveMiddle CONTRACT =
            new MustHaveMiddle ();
    }

    /**
     * @return The view item(s) (elements or indices and so on)
     *         located in the centre of this countable view,
     *         or No view item if this countable view is empty.
     *         If this view has an odd number of view items, then
     *         One middle view item will be returned.  If this
     *         view has a non-zero even number of view items, then
     *         Multiple view items will be returned (two, to be precise).
     *         Never null.
     */
    public abstract Countable<VIEW_ITEM> middle ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;




    /** Contract which is violated whenever minimum ( ... ) receives
     *  Abnormal input. */
    public static class Minimum
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for minimum ( ... ) operations. */
        public static final Minimum CONTRACT =
            new Minimum ();
    }

    /** Contract which is violated whenever minimum ( ... ) does not
     *  produce any results. */
    public static class MustHaveMinimum
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for minimum ( ... ) operations. */
        public static final MustHaveMinimum CONTRACT =
            new MustHaveMinimum ();
    }

    /**
     * <p>
     * Returns the lowest view item (element / index / and so on)
     * in this countable view according to the specified Order.
     * </p>
     *
     * <p>
     * If there is a tie, then two or more view items will be returned.
     * </p>
     *
     * @param order The sort order which is used to compare view items.
     *              Must not be null.
     *
     * @return The minimum view item (element / index / and so on)
     *         in this countable view according to the specified order.
     *         Never null.
     */
    public abstract Countable<VIEW_ITEM> minimum ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;




    /** Contract which is violated whenever mode ( ... ) receives
     *  Abnormal input. */
    public static class Mode
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for mode ( ... ) operations. */
        public static final Mode CONTRACT =
            new Mode ();
    }

    /** Contract which is violated whenever mode ( ... ) does not
     *  produce any results. */
    public static class MustHaveMode
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for mode ( ... ) operations. */
        public static final MustHaveMode CONTRACT =
            new MustHaveMode ();
    }

    /**
     * <p>
     * Returns the mode view item (element / index / and so on)
     * in this countable view according to the specified Order.
     * </p>
     *
     * <p>
     * If there is a tie, then two or more view items will be returned.
     * </p>
     *
     * @param order The sort order which is used to compare view items.
     *              Must not be null.
     *
     * @return The mode view item (element / index / and so on)
     *         in this countable view according to the specified order.
     *         Never null.
     */
    public abstract Countable<VIEW_ITEM> mode ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;




    /** Contract which is violated whenever unique ( ... ) receives
     *  Abnormal input. */
    public static class Unique
        extends ValueMustNotBeAbnormal
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for unique ( ... ) operations. */
        public static final Unique CONTRACT =
            new Unique ();
    }

    /** Contract which is violated whenever unique ( ... ) does not
     *  produce any results. */
    public static class MustBeUnique
        extends ValueMustNotBeEmpty
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            MODULE.VERSION;

        /** The contract for unique ( ... ) operations. */
        public static final MustBeUnique CONTRACT =
            new MustBeUnique ();
    }

    /**
     * @return A new Countable value containing only unique
     *         view items (elements or indices and so on), with
     *         all duplicates removed.
     *         If this Countable view was constructed from
     *         an Error or Warning then one such is returned.
     *         Never null.
     */
    public abstract Countable<VIEW_ITEM> unique ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
