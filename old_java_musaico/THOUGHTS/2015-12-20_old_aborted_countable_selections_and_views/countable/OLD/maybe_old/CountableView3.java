package !!!;


/**
 * <p>
 * !!!
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
public interface CountableView<VALUE extends Object, VIEW_ITEM extends Object, VIEW extends CountableView<VALUE, VIEW_ITEM, VIEW>>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // !!!
    public abstract Countable<VIEW_ITEM> all ();


    // !!!
    public abstract Countable<VIEW_ITEM> at ();


    /**
     * <p>
     * Determines whether this countable elements contains all elements of
     * the specified sub-value, in the same order, either with or without
     * preceding and/or proceeding other elements.
     * </p>
     *
     * <p>
     * No value is always considered to be contained inside any
     * Countable elements (including an empty countable elements) of the
     * same value class.  Neither Warnings no Errors are ever
     * contained within any Countable elements.  And no Multiple is ever
     * contained by any empty or single-item countable elements.
     * </p>
     *
     * @param sub_value The countable value to search for.  Must not be null.
     *
     * @return True if the specified sub-value is nested anywhere inside
     *         the elements of this countable elements; false if it does
     *         not appear anywhere in this value.
     *         Always false if this Countable elements was constructed
     *         from an Error or Warning.  Always false if the specified
     *         sub-value is an Error or Warning.
     */
    public abstract boolean contains (
                                      Countable<VALUE> sub_value
                                      )
        throws ParametersMustNotBeNull.Violation;


    /**
     * @return A newly created copy of this CountableView, which
     *         can be modified independently of this one.
     *         Never null.
     */
    public abstract VIEW duplicate ()
        throws ReturnNeverNull.Violation;


    // !!!
    public abstract Countable<VIEW_ITEM> first ();


    // !!!
    public abstract CountableView<VALUE, VIEW_ITEM, VIEW> in (
            CountableOperation0 operation
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // !!!
    public abstract <PARAMETER1 extends Object>
        CountableView<VALUE, VIEW_ITEM, VIEW> in (
            CountableOperation1 operation,
            PARAMETER1 parameter1
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // !!!
    public abstract <PARAMETER1 extends Object, PARAMETER2 extends Object>
        CountableView<VALUE, VIEW_ITEM, VIEW> in (
            CountableOperation2 operation,
            PARAMETER1 parameter1,
            PARAMETER2 parameter2
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // !!!
    public abstract Countable<VIEW_ITEM> last ();


    // !!!
    public abstract Countable<VIEW_ITEM> middle ();


    /**
     * @return The number of items (elements / indices / and so on,
     *         depending on this class) in this Countable view.
     *         Always One long number that is 0 or greater unless this
     *         countable value view was constructed from an Error or Warning.
     *         Never null.
     *
     * @see musaico.foundation.value.Countable#length()
     */
    public abstract One<Long> length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;


    // !!!
    public abstract CountableView<VALUE, VIEW_ITEM, VIEW> redo ();


    // !!!
    public abstract CountableView<VALUE, VIEW_ITEM, VIEW> redoAll ();


    /**
     * <p>
     * Returns an array of items (elements / indices / and so on,
     * depending on this class) contructed from the specified template
     * array and the contents of this countable view.
     * </p>
     *
     * @see java.util.Collection#toArray(java.lang.Object[])
     *
     * @param template The array template used to fill or construct the
     *                 array to return.  Can contain null values.
     *                 Must not be null.
     *
     * @return The array of values from this countable view.
     *         A defensive copy is returned, so modification is fine.
     *         Never null.
     */
    public abstract VIEW_ITEM [] toArray (
                                          VIEW_ITEM [] template
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a list of items (elements / indices / and so on,
     * depending on this class) from this countable view.
     * </p>
     *
     * @return The list of elements / indices / and so on from
     *         this countable view, in the same order
     *         as they are in this countable value view.
     *         A defensive copy is returned, so modification is fine.
     *         Never null.
     */
    public abstract List<VIEW_ITEM> toList ()
        throws ReturnNeverNull.Violation;

    /**
     * <p>
     * Returns a map of items (elements / indices / and so on,
     * depending on this class) from this countable view,
     * each one initially mapped to the same default value.
     * </p>
     *
     * @param default_value The value to which each element/index/and so on
     *                      key will be mapped.  Must not be null.
     *
     * @return The mapped values from this countable view,
     *         with the key set in the same order as they are
     *         in this countable value view.  A LinkedHashMap
     *         is returned in order to ensure consistent
     *         ordering when iterating through the element/index/and so on
     *         keys.  A defensive copy is returned, so modification is fine.
     *         Never null.
     */
    public abstract <MAP_TO_VALUE extends Object>
        LinkedHashMap<VIEW_ITEM, MAP_TO_VALUE> toMap (
                                                      MAP_TO_VALUE default_value
                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;

    /**
     * <p>
     * Returns a set of items (elements / indices / and so on,
     * depending on this class) from this countable view.
     * </p>
     *
     * @return The set of values from this countable view,
     *         in the same order as they are in this countable value view.
     *         A LinkedHashSet is returned in order to ensure consistent
     *         ordering when iterating through the elements/indices/
     *         and so on.  A defensive copy is returned, so modification
     *         is fine.  Never null.
     */
    public abstract LinkedHashSet<VIEW_ITEM> toSet ()
        throws ReturnNeverNull.Violation;


    // !!!
    public abstract CountableView<VALUE, VIEW_ITEM, VIEW> undo ();


    // !!!
    public abstract CountableView<VALUE, VIEW_ITEM, VIEW> undoAll ();


    /**
     * @return A newly created Countable value containing the items
     *         (elements / indices / and so on, depending on this class)
     *         from this countable view.
     *         If this Countable view was constructed from
     *         an Error or Warning then one such is returned.
     *         Never null.
     */
    public abstract Countable<VIEW_ITEM> value ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
