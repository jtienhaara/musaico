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
 * Captures some sequence or set or pool of element(s),
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
public interface EditSequence<STREAM extends Object, PARENT extends Parent<STREAM, PARENT>, POOL extends Object, SUB extends EditSequence<STREAM, PARENT, POOL, SUB>>
    extends Subsidiary<STREAM, SUB, STREAM, PARENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Parent)

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#end()

    // Every Subsidiary must implement java.lang.Object#equals(java.lang.Object)

    // Every Subsidiary must implement java.lang.Object#hashCode()

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#isEnded()

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#parent()


    /**
     * <p>
     * Specifies a pool of elements, such as a Term or a Collection,
     * depending on the pipeline, to use as a sequence for the Edit
     * operation at hand.
     * </p>
     *
     * @param sequence A pool of zero or more element(s) to use as a sequence
     *                 for the current Edit operation.  Must not be null.
     *                 Must not contain any null elements.
     *
     * @return The parent pipeline, such as the Edit which spawned
     *         this child EditSequence.  Never null.
     */
    @SuppressWarnings("unchecked") // Heap pollution varargs.
    public abstract PARENT sequence (
            POOL sequence
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Specifies individual elements to use as a sequence
     * for the Edit operation at hand.
     * </p>
     *
     * @param sequence Zero or more element(s) to use as a sequence
     *                 for the current Edit operation.  Must not be null.
     *                 Must not contain any null elements.
     *
     * @return The parent pipeline, such as the Edit which spawned
     *         this child EditSequence.  Never null.
     */
    @SuppressWarnings("unchecked") // Heap pollution varargs.
    public abstract PARENT sequence (
            STREAM... sequence
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    // Every Subsidiary must implement java.lang.Object#toString()
}
