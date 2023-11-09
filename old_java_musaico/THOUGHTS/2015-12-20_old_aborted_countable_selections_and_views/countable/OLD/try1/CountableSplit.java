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
    extends CountableSplitView<VALUE, VIEW_ITEM, VIEW> Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;




    /**
     * <p>
     * Splits the countable value being built at the specified indices,
     * and returns a list of Countables, each one containing
     * one split segment.
     * </p>
     *
     * <p>
     * If a split is requested at index 0L, then the left side of the split
     * at index 0 will always contain No value.  If a split is requested
     * at an index greater than <code> Integer.MAX_VALUE </code>, then
     * the split will occur AFTER the element counting backward from
     * <code> Countable.LAST </code> (resulting in No value to the right
     * if the split is at <code> Countable.LAST </code>).
     * </p>
     *
     * <p>
     * Indices less than 0L or greater than the length of the
     * countable value being built (except for <code> Countable.Last </code> )
     * will be ignored.
     * </p>
     *
     * <p>
     * For example, if the countable elements <code> { A, B, C } </code>
     * is split at indices <code> { 0, 2, LAST } </code> then the
     * four resulting Countables will be <code> {} </code> (empty),
     * <code> A, B </code>, <code> C </code> and <code> {} </code> (empty).
     * </p>
     *
     * @param split_at_indices The index or indices at which to split the
     *                         countable value being built.  Must not be null.
     *
     * @param split_index_goes_to Where to place the index around which the
     *                            split is happening: either at the start
     *                            of the subsequent list (Countable.FIRST),
     *                            at the end of the previous list
     *                            (Countable.LAST), or removed (any other
     *                            index).
     *
     * @return 0 or more split Countable values.
     *         If this Countable elements was constructed from
     *         an Error or Warning then an empty list will be returned.
     *         If the specified indices countable value is an Error or Warning
     *         then an empty list will be returned.
     *         Never null.  Never contains any null elements.
     */
    public abstract List<Countable<VALUE>> at (
                                               Countable<Long> split_at_indices,
                                               long split_index_goes_to
                                               )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;




    /**
     * <p>
     * Splits the countable value being built by the specified element(s)
     * pattern, and returns a list of Countables, each one containing
     * one split segment.
     * </p>
     *
     * <p>
     * For example, if the countable elements <code> { A, B, C } </code>
     * is split by pattern <code> { A, B } </code> then the
     * two resulting Countables will be <code> {} </code> (empty)
     * and <code> { C } </code>.
     * </p>
     *
     * @param split_by_sequence The pattern of element(s) which will
     *                          divide the elements of this
     *                          countable elements into a list of
     *                          countable values.  Must not be null.
     *
     * @return 0 or more split Countables.
     *         If this Countable elements was constructed from
     *         an Error or Warning then an empty list will be returned.
     *         If the specified sequence is an Error or Warning
     *         then an empty list will be returned.
     *         Never null.  Never contains any null elements.
     */
    public abstract List<Countable<VALUE>> by (
                                               Countable<VALUE> split_by_sequence
                                               )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    // !!!
    into ( long );
}
