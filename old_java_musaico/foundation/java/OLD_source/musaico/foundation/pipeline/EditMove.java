package musaico.foundation.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;


/**
 * <p>
 * Captures the movement or relocation of some element(s),
 * helping to build up an Edit subsidiary pipeline.
 * </p>
 *
 *
 * <p>
 * In Java every Subsidiary must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Subsidiary must be Serializable in order to
 * play nicely across RMI.
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
 * @see musaico.foundation.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.pipeline.MODULE#LICENSE
 */
public interface EditMove<STREAM extends Object, PARENT extends Parent<STREAM, PARENT>, SUB extends EditMove<STREAM, PARENT, SUB>>
    extends Subsidiary<STREAM, SUB, STREAM, PARENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Specifies a particular offset from the original position within
     * each input, to which the Edit operation will apply.
     * </p>
     *
     * @param offset The offset from the original position of each input,
     *               such as 0L for no movement, or 2L to shift the
     *               element(s) backward by 2 elements.
     *               If (starting_index + offset) is greater than the length
     *               of an input object, or less than 0L, then that input
     *               generates an error.
     *               The insertion of elements is done
     *               AFTER the removal from the old location,
     *               so the range of elements will move forward
     *               if the offset is less than 0, and it will
     *               move backward if the offset is greater than 0L.
     *               Can be any number.
     *
     * @return The parent pipeline, such as the Edit which spawned
     *         this child EditMove.  Never null.
     */
    public abstract PARENT by (
            long offset
            )
        throws ReturnNeverNull.Violation;


    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Parent)

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#end()

    // Every Subsidiary must implement java.lang.Object#equals(java.lang.Object)

    // Every Subsidiary must implement java.lang.Object#hashCode()

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#isEnded()


    /**
     * <p>
     * Returns an OrderBy pipeline, which can be used to move
     * the element(s) of each input object according
     * to the sort order of the first element being moved.
     * </p>
     *
     * <p>
     * Outside the range of element(s) to be moved, each input object
     * is otherwise assumed to be sorted into the same order
     * or a more specific order.  If this operation determines
     * that either an input or the element(s) to move contain element(s)
     * that are out of order, then an error will
     * be generated for that input.  However no explicit check
     * for ordering is performed before the move is attempted.
     * </p>
     *
     * @param orders Zero or more hierarchy of Orders which will determine
     *               the location to which the element(s) will be moved.
     *               If the first Order considers two elements to be the same,
     *               then the second Order is applied.  If the second Order
     *               also considers the two elements to be the same, then
     *               the third Order is applied.  And so on.  Can be empty.
     *               Must not be null.  Must not contain any null elements.
     *
     * @return A new OrderBy sub-pipeline.  Never null.
     */
    @SuppressWarnings("unchecked") // Generic varargs possible heap pollution.
    public abstract OrderBy<STREAM, PARENT> orderBy (
            Order<STREAM> ... orders
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#parent()


    /**
     * <p>
     * Specifies a particular offset from the original position within
     * each input, to which the Edit operation will apply; if the moved
     * element(s) end up moving past one endof the input
     * object, then they will be rotated to the other end.
     * </p>
     *
     * @param offset The offset from the original position of each input,
     *               such as 0L for no movement, or 2L to shift the
     *               element(s) backward by 2 elements.
     *               If (starting_index + offset) is greater than the length
     *               of an input object, or less than 0L, then that the
     *               elements which wrap will be rotated to the other end.
     *               The insertion of elements is done
     *               AFTER the removal from the old location,
     *               so the range of elements will move forward
     *               if the offset is less than 0, and it will
     *               move backward if the offset is greater than 0L.
     *               Can be any number.
     *
     * @return The parent pipeline, such as the Edit which spawned
     *         this child EditMove.  Never null.
     */
    public abstract PARENT rotate (
            long offset
            )
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Specifies a particular index into the multi-valued object to
     * which the Edit operation will apply.
     * </p>
     *
     * @param index The location to which the range of elements
     *              will be moved in each input object, such as 0L
     *              to move to before the first element, or 2L
     *              to move to before the 3rd.
     *              If the index is equal to the length of an input
     *              object, then the chunk will be moved to the end
     *              of that input.
     *              If the index is greater than the length
     *              of an input object, then that input
     *              generates an error.
     *              The insertion of the moved element(s) is done
     *              AFTER the removal from the old location,
     *              so the range of elements will move forward
     *              if the specified index is less than
     *              the start index of the selection,
     *              and it will move backward if the specified
     *              index is greater than the start index
     *              of the selection.
     *              Must be greater than or equal to 0L.
     *
     * @return The parent pipeline, such as the Edit which spawned
     *         this child EditMove.  Never null.
     */
    public abstract PARENT to (
            long index
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Specifies a particular offset from the end of each input,
     * to which the Edit operation will apply.
     * </p>
     *
     * @param offset The offset from the end of each input, such as 0L
     *               to move to before the last element, or 2L to move
     *               to before the 3rd last.
     *               If the offset is equal to the length of an input
     *               object, then the chunk will be moved to the beginning
     *               of that input.
     *               If the offset is greater than the length
     *               of an input object, then that input
     *               generates an error.
     *               The insertion of the moved element(s) is done
     *               AFTER the removal from the old location,
     *               so the range of elements will move forward
     *               if (end - offset) is less than
     *               the start index of the selection,
     *               and it will move backward if (end - offset)
     *               is greater than the start index
     *               of the selection.
     *               Must be greater than or equal to 0L.
     *
     * @return The parent pipeline, such as the Edit which spawned
     *         this child EditMove.  Never null.
     */
    public abstract PARENT toOffsetFromEnd (
            long offset
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Specifies a particular offset from the middle of each input,
     * to which the Edit operation will move element(s).
     * </p>
     *
     * @param offset The offset from the middle of each input, such as 0L
     *               for before the middle element, or 2L for
     *               before the (middle + 2)'th element, or -2L for
     *               before the (middle - 2)'th element.
     *               If the offset is greater than half the length
     *               of an input object, or if the offset is less than
     *               -1L times half the length of an input object,
     *               then that input generates an error.
     *               The insertion of the moved element(s) is done
     *               AFTER the removal from the old location,
     *               so the range of elements will move forward
     *               if the ( middle + offset ) is less than
     *               the start index of the selection,
     *               and it will move backward if ( middle + offset )
     *               is greater than the start index of the selection.
     *               CAN be positive or negative.
     *
     * @return The parent pipeline, such as the Edit which spawned
     *         this child EditMove.  Never null.
     */
    public abstract PARENT toOffsetFromMiddle (
            long offset
            )
        throws ReturnNeverNull.Violation;


    // Every Subsidiary must implement java.lang.Object#toString()
}
