package musaico.foundation.operations;

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
 * @see musaico.foundation.operations.MODULE#COPYRIGHT
 * @see musaico.foundation.operations.MODULE#LICENSE
 */
public interface Pipe<VALUE extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * The kind of stream a Pipe provides, or the kind of stream
     * a downstream Pipe wants to read, and so on.
     * </p>
     */
    public static enum Stream
    {
        /** A Countable Term. */
        COUNTABLE,

        /** A Cyclical infinite Term. */
        CYCLICAL,

        /** The Countable header () of a Cyclical infinite Term. */
        CYCLICAL_HEADER,

        /** The repeated Many cycle () of a Cyclical infinite Term. */
        CYCLICAL_CYCLE,

        /** A Continuous infinite Term. */
        CONTINUOUS,

        /** An infinite Term that is neither Cyclical nor Continuous. */
        INFINITE,

        /** An Expression or other Reducible Term. */
        REDUCIBLE,

        /** Some other type of Term. */
        OTHER;
    }




    /**
     * <p>
     * Converts the specified index for the specified stream.
     * </p>
     *
     * <p>
     * If the index is greater than or equal to 0L and less than
     * the <code> length ( ... ) </code> of the specified stream,
     * then it will be returned as-is.
     * </p>
     *
     * @see musaico.foundation.term.Countable#at(long)
     *
     * @param index The index to convert.  Can be any value.
     *
     * @param stream The stream for which the index will be converted.
     *               Must not be null.
     *
     * @param context The Context in which to calculate the clamped
     *                index.  The Context might contain a variable
     *                binding required by this Pipe, and possibly other
     *                state required to evaluate this Pipe.
     *                Must not be null.
     *
     * @return The clamped index, or Countable.NONE (-1L)
     *         if the input index was out of bounds, or if
     *         the specified stream is not supported.
     *         Always greater than or equal to -1L.
     */
    public abstract long clamp (
            long index,
            Pipe.Stream stream,
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation;


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
     * Returns the number of elements coming from the stream term
     * which can be read from the specified stream.
     * </p>
     *
     * <p>
     * For example, if this Pipe has a Term or an Operation
     * feeding it a <code> Pipe.Stream.CYCLICAL </code>
     * stream, then the number of elements of either the
     * <code> Pipe.Stream.CYCLICAL_HEADER </code> stream or the
     * <code> Pipe.Stream.CYCLICAL_CYCLE </code> stream
     * can be requested.
     * </p>
     *
     * @param stream The stream whose length will be returned.
     *               Must not be null.
     *
     * @param context The Context in which to determine the length.
     *                The Context might contain a variable
     *                binding required by this Pipe, and possibly other
     *                state required to evaluate this Pipe.
     *                Must not be null.
     *
     * @return The number of elements coming from the specified stream.
     *         Always 0L or greater.
     */
    public abstract long length (
            Pipe.Stream stream,
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation;


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
     *         <code> read ( array, ... ) </code> from the stream(s)
     *         instead, if possible, to request only the elements
     *         that are required downstream, rather than processing
     *         the entire Term.  In this way the pipeline can continue
     *         to build up only one final output Term, with the final
     *         Pipe in the line constructing the output Term at the end,
     *         after all processing is complete.  Never null.
     */
    public abstract Term<VALUE> read (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Reads elements from the specified stream in the specified context.
     * </p>
     *
     * <p>
     * For example, if this Pipe has a Term or an Operation
     * feeding it a <code> Pipe.Stream.CYCLICAL </code>
     * stream, then the elements of either the
     * <code> Pipe.Stream.CYCLICAL_HEADER </code> stream or the
     * <code> Pipe.Stream.CYCLICAL_CYCLE </code> stream
     * can be read.
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
     * @param first_element_index The index into the stream
     *                            at which to start reading elements.
     *                            Can be any number.
     *            @see musaico.foundation.term.Countable#at(long)
     *
     * @param stream The stream whose elements will be read.
     *               Must not be null.
     *
     * @param context The Context in which to read from the stream.
     *                The Context might contain a variable
     *                binding required by this Pipe, and possibly other
     *                state required to evaluate this Pipe.
     *                Must not be null.
     *
     * @return The number of elements read, or -1 if there are no more
     *         elements to read, or -1 if the requested
     *         <code> ( offset + length ) </code> is greater than
     *         the number of elements in this Pipe's stream
     *         starting at <code> first_element_index </code>.
     *         If the return value is greater than 0 then all elements
     *         in the range <code> [ offset, offset + length -1 ] </code>
     *         (inclusive) are guaranteed to be non-null. 
     *         Always -1 or greater.
     */
    public abstract int read (
            VALUE [] elements,
            int offset,
            int length,
            long first_element_index,
            Pipe.Stream stream,
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter3.MustBeGreaterThanOrEqualToZero.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation;


    /**
     * <p>
     * Returns the type of output stream provided by this Pipe.
     * </p>
     *
     * @param context The Context in which to determine this Pipe's
     *                output stream.  The Context might contain a variable
     *                binding required by this Pipe, and possibly other
     *                state required to evaluate this Pipe.
     *                Must not be null.
     *
     * @return The variety of stream provided by this Pipe as output,
     *         such as COUNTABLE elements, or CYCLICAL elements,
     *         or a CONTINUOUS value, and so on.  Never null.
     */
    public abstract Pipe.Stream stream (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


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
