package musaico.foundation.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * An object which can build a new Pipeline from an input.
 * </p>
 *
 *
 * <p>
 * In Java every Source must be Serializable in order to
 * play nicely across RMI.  However users of the Source
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
 * </p>
 *
 * <p>
 * In Java every Source must implement equals (), hashCode ()
 * and toString ().
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
public interface Source<INPUT extends Object, STREAM extends Object, PIPELINE extends Pipeline<STREAM, PIPELINE>>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return A new Source with exactly the same state
     *         as this one.  If this Source is also a Pipeline,
     *         then all of its Subsidiary children (if it as any)
     *         that have already been <code> end () </code>ed
     *         will also be duplicated.  If this Source is also a Subsidiary
     *         then no parent or ancestor Pipelines are copied,
     *         only this Subsidiary and all of its Subsidiary
     *         children (if it as any) that have already been
     *         <code> end () </code>ed.  Never null.
     */
    public abstract Source<INPUT, STREAM, PIPELINE> duplicate ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new Pipeline, which can be built into a sequence of
     * operations and applied to the specifed input.
     * </p>
     *
     * @param input The input into the new Pipeline.  Must not be null.
     *
     * @return The new Pipeline.  Never null.
     */
    public abstract PIPELINE from (
            INPUT input
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
