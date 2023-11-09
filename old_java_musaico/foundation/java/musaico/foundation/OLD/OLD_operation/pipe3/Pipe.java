package musaico.foundation.operation;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
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
     * Reads the full output Term from this section of Pipe.
     * </p>
     *
     * @param context The Context in which to build/return this Pipe's
     *                output Term.  The Context might contain a variable
     *                binding required by this Pipe, and possibly other
     *                state required to evaluate this Pipe.
     *                Must not be null.
     *
     * @return The Term output from this section of Pipe.
     *         The Term might be constructed from scratch by invoking
     *         this method, so it is often best to
     *         <code> read ( array, ... ) </code> instead, if possible,
     *         to request only the elements that are required downstream,
     *         rather than processing the entire Term.  In this way
     *         the pipeline can continue to build up only one final
     *         output Term, with the final Pipe in the line
     *         constructing the output Term at the end,
     *         after all processing is complete.  Never null.
     */
    public abstract Term<VALUE> read (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Reads elements from this Pipe in the specified context.
     * </p>
     *
     * @param elements The array of elements to read into.
     *                 Must not be null.  Must not contain any null elements.
     *
     * @param offset The offset into the specified array to which
     *               elements will be written.  Must be greater than
     *               or equal to 0.
     *
     * @param length The number of elements to read into the array.
     *
     * @param start The index at which to start reading elements.
     *              Can be any number.
     *              @see musaico.foundation.term.Countable#at(long)
     *
     * @param context The Context in which to read from this Pipe.
     *                The Context might contain a variable
     *                binding required by this Pipe, and possibly other
     *                state required to evaluate this Pipe.
     *                Must not be null.
     *
     * @return The number of elements read, or -1 if there are no more
     *         elements to read.
     *         If the return value is greater than 0 then all elements
     *         in the range <code> [ offset, offset + (RESULT) -1 ] </code>
     *         (inclusive) are guaranteed to be non-null. 
     *         Always -1 or greater.
     */
    public abstract int read (
            VALUE [] elements,
            int offset,
            int length,
            long start,
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter3.MustBeGreaterThanOrEqualToZero.Violation,
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


    /**
     * <p>
     * Returns the TermViolation causing this section of Pipe to
     * fail, or null if there is no error from this Pipe
     * in the given context.
     * </p>
     *
     * @param context The Context in which to determine the TermViolation.
     *                The Context might contain a variable
     *                binding required by this Pipe, and possibly other
     *                state required to evaluate this Pipe.
     *                Must not be null.
     *
     * @return The TermViolation causing this Pipe's error (if any),
     *         or null, if this Pipe does not cause an error
     *         in the given context.  CAN BE NULL.
     */
    public abstract TermViolation violation (
            Context context
            )
        throws ParametersMustNotBeNull.Violation; // Return can be null.
}
