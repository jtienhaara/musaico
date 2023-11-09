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
public interface EditInsert<STREAM extends Object, PARENT extends Parent<STREAM, PARENT>, SUB extends EditInsert<STREAM, PARENT, SUB>>
    extends Subsidiary<STREAM, SUB, STREAM, PARENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Specifies a particular index into the multi-valued object at
     * which the Edit operation will insert element(s).
     * </p>
     *
     * @param index The location at which element(s) will be inserted
     *              into each input object, such as 0L to insert
     *              before the first element, or 2L before the 3rd.
     *              If the index is equal to the length of an input
     *              object, then the element(s) will be appended
     *              to that input.
     *              If the index is greater than the length
     *              of an input object, then that input
     *              generates an error.
     *              Must be greater than or equal to 0L.
     *
     * @return The parent pipeline, such as the Edit which spawned
     *         this child EditInsert.  Never null.
     */
    public abstract PARENT at (
            long index
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation;


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
     * Specifies a particular offset from the end of each input,
     * at which the Edit operation will insert element(s).
     * </p>
     *
     * @param offset The offset from the end of each input, such as 0L
     *               for before the last element, or 2L for before
     *               the 3rd last.
     *               If the index is greater than the length
     *               of an input object, then that input
     *               generates an error.
     *               Must be greater than or equal to 0L.
     *
     * @return The parent pipeline, such as the Edit which spawned
     *         this child EditInsert.  Never null.
     */
    public abstract PARENT atOffsetFromEnd (
            long offset
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Specifies a particular offset from the middle of each input,
     * at which the Edit operation will insert element(s).
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
     *               CAN be positive or negative.
     *
     * @return The parent pipeline, such as the Edit which spawned
     *         this child EditInsert.  Never null.
     */
    public abstract PARENT atOffsetFromMiddle (
            long offset
            )
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns an OrderBy pipeline, which can be used to insert
     * the element(s) at the index of each input object according
     * to the sort order of the first element to insert.
     * </p>
     *
     * <p>
     * Each input object and the element(s) to insert are assumed
     * to be sorted into the same order or a more specific order.
     * If this operation determines that either an input or the
     * element(s) to insert are out of order, then an error will
     * be generated for that input.  However no explicit check
     * for ordering is performed before the insertion is attempted.
     * </p>
     *
     * @param orders Zero or more hierarchy of Orders which will determine
     *               where to insert the element(s).
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

    // Every Subsidiary must implement java.lang.Object#toString()
}
