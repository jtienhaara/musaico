package musaico.foundation.operation;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.Parameter5;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;


/**
 * <p>
 * A source of input for an Operation, such as a parameter, the input
 * Term, a control stream from another Operation, and so on.
 * </p>
 *
 * <p>
 * Based on a components-and-ports structure, but tailored to
 * look more like standard input functionality.
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
 * @see musaico.foundation.operation.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.MODULE#LICENSE
 */
public interface Pipe<VALUE extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Closes this Pipe, and any and all of its inputs, in the
     * specified context.
     * </p>
     *
     * @param context The Context in which to close this Pipe.
     *                The Context might contain a variable
     *                binding required by this Pipe, and possibly other
     *                state required to evaluate this Pipe.
     *                Must not be null.
     */
    public abstract void close (
            Context context
            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Returns the Pipe(s) which are input(s) into this Pipe
     * in the given context.
     * </p>
     *
     * @param context The Context in which to retrieve the input pipe(s).
     *                The Context might contain a variable
     *                binding required by this Pipe, and possibly other
     *                state required to evaluate this Pipe.
     *                Must not be null.
     *
     * @return All Pipe(s) from which this Pipe must read values,
     *         such as parameters or inputs to an operation.
     *         Can be empty.  Never null.  Never contains any null elements.
     */
    public abstract Pipe<?> [] inputPipes (
            Context context
            )
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Returns any and all Pipe(s), if any, which this Pipe
     * provides as outputs, in addition to itself.
     * </p>
     *
     * @param context The Context in which to retrieve the output Pipe(s).
     *                The Context might contain a variable
     *                binding required by this Pipe, and possibly other
     *                state required to evaluate this Pipe.
     *                Must not be null.
     *
     * @return All additional output Pipe(s) which this Pipe provides,
     *         such as control flow to other Pipes, and so on.
     *         Can be empty.  Never null.  Never contains any null elements.
     */
    public abstract Pipe<?> [] outputPipes (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Reads elements from this Pipe in the specified context.
     * </p>
     *
     * @param output The Stream which will accept the output(s)
     *               from this Pipe.  Must not be null.
     *
     * @return The number of elements read into the output_finite list,
     *         or -1L if there were no more finite elements to read.
     *         Always -1L or greater.
     */
    public abstract long read (
            Stream<VALUE> output
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter4.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter5.MustBeGreaterThanOrEqualToZero.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation;


    /**
     * <p>
     * Returns the output Type of this Pipe in the given Context.
     * </p>
     *
     * @return This Pipe's output Type.  Never null.
     */
    public abstract Type<VALUE> type (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
