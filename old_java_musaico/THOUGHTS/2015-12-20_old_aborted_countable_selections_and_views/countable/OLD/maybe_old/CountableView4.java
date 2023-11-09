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
    public abstract Countable<VIEW_ITEM> at (
                                             long index
                                             );


    // !!!
    public abstract Countable<VIEW_ITEM> at (
                                             long index,
                                             long length
                                             );


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
    public abstract Countable<VIEW_ITEM> first (
                                                long length
                                                );


    // !!!
    public abstract Countable<VIEW_ITEM> last ();


    // !!!
    public abstract Countable<VIEW_ITEM> last (
                                               long length
                                               );


    // !!!
    public abstract Countable<VIEW_ITEM> middle ();


    // !!!
    public abstract Countable<VIEW_ITEM> middle (
                                                 long length
                                                 );


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
