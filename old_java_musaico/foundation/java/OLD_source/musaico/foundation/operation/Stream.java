package musaico.foundation.operation;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;

import musaico.foundation.term.contracts.TermMustMeetAtLeastOneContract;
import musaico.foundation.term.contracts.ValueMustBeCountable;

import musaico.foundation.term.infinite.TermMustBeCyclical;


/**
 * <p>
 * A flow of Terms from an upstream Pipe (or Pipes).
 * </p>
 *
 * <p>
 * The upstream Pipe(s) <code> write ( ... ) </code> Term(s) to this
 * Stream, and/or the downstream Pipe(s) <code> read () </code>
 * Term(s) from this Stream.
 * </p>
 *
 * <p>
 * Often the Buffer Stream is used, allowing a downstream Pipe
 * to pull Term(s) from the upstream Pipe and later retrieve them from
 * the buffer.
 * </p>
 *
 * <p>
 * Other streams can be used, such as a blocking input Stream (which
 * blocks on <code> read () </code> until input is available)
 * or a blocking output Stream (which blocks in <code> write ( ... ) </code>
 * until a downstream Pipe is ready for the output) or a read-only
 * Stream (which always returns <code> Pipe.CLOSED </code>
 * on <code> write ( ... ) </code> but provides Term(s)
 * to <code> read () </code>) or a write-only Stream (which always
 * returns <code> No&lt;Term&gt; on <code> read () </code> but allows
 * the upstream Pipe to <code> write ( ... ) </code> Term(s))
 * or a client-server Stream (which provides 2-way traffic between
 * the "upstream" client and the "downstream" server) and so on.
 * </p>
 *
 * <p>
 * Read-only and write-only Pipes can be used, for example, to communicate
 * with external processes or across a network, and so on, to/from
 * Pipes which are specialized by programming language or system
 * environment and so on.  For example, A Java program might use
 * a write-only Pipe to tell a C program what to display (since Java
 * graphics libraries, despite at least 3 major versions - AWT, Swing,
 * JavaFX -- are still atrociously bad).  Or a JavaScript program might
 * use a read-only Pipe to receive asynchronous events from a Java servlet;
 * and so on.
 * </p>
 *
 *
 * <p>
 * In Java, every Stream must implement equals (), hashCode ()
 * and toString().
 * </p>
 *
 * <p>
 * In Java every Stream must be Serializable in order to
 * play nicely across RMI.  However users of Streams should be cautious,
 * because Terms which might be stored inside, although Serializable
 * themselves, can contain non-Serializable elements.
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
public interface Stream<VALUE extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * The state of a Stream's source: OPEN if it is still capable of reading
     * Term(s) from its input Pipe, CLOSED if the Stream cannot
     * possibly read any more Terms.
     * </p>
     */
    public static enum State
    {
        OPEN,
        CLOSED;
    }




    /** Contract for Pipes that require either Countable
     *  or Cyclical (infinite) Terms to be read from their upstreams.
     *  To meet this contract, each Term must be either:
     *  1) Countable; or
     *  2) Cyclical. */
    @SuppressWarnings("unchecked") // Generic array creation varargs.
        final TermMustMeetAtLeastOneContract TERM_MUST_BE_COUNTABLE_OR_CYCLICAL =
        new TermMustMeetAtLeastOneContract (
                ValueMustBeCountable.CONTRACT,
                TermMustBeCyclical.CONTRACT
                );


    /**
     * <p>
     * Closes this Stream so that it will no longer receive any
     * Terms written to it from the upstream Pipe.
     * </p>
     *
     * <p>
     * After closing, a buffered Stream might still allow the downstream
     * Pipe to finish reading the buffered Term(s).
     * </p>
     *
     * <p>
     * The upstream Pipe feeding this Stream is NOT closed, since it
     * might still be in use, providing other Streams to other downstream
     * recipients.
     * </p>
     */
    public abstract void close ();


    // Every Stream must implement java.lang.Object#equals(java.lang.Object)


    /**
     * @return The time in seconds UTC (since midnight January 1st 1970 GMT)
     *         at which this Stream first received a read request,
     *         such as <code> read () </code>
     *         or <code> nextElement () </code>.  Even if the request
     *         was unsuccessful (no Terms were written downstream), this
     *         timestamp always represents the time of that first attempt.
     *         If no read attempts have been made, then BigDecimal.ZERO
     *         is returned.  Never null.  Always greater than or equal to
     *         BigDecimal.ZERO.
     */
    public abstract BigDecimal firstReadTimeSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * @return The time in seconds UTC (since midnight January 1st 1970 GMT)
     *         at which this Stream first received a write request,
     *         If no write attempts have been made, then BigDecimal.ZERO
     *         is returned.  Never null.  Always greater than or equal to
     *         BigDecimal.ZERO.
     */
    public abstract BigDecimal firstWriteTimeSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation;


    // Every Stream must implement java.lang.Object#hashCode()


    /**
     * @return The time in seconds UTC (since midnight January 1st 1970 GMT)
     *         at which this Stream most recently received a read request,
     *         such as <code> read () </code>
     *         or <code> nextElement () </code>.  Even if the request
     *         was unsuccessful (no Terms were written downstream), this
     *         timestamp always represents the time of that last attempt.
     *         If no read attempts have been made, then BigDecimal.ZERO
     *         is returned.  Never null.  Always greater than or equal to
     *         BigDecimal.ZERO.
     */
    public abstract BigDecimal lastReadTimeSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * @return The time in seconds UTC (since midnight January 1st 1970 GMT)
     *         at which this Stream most recently received a write request,
     *         If no write attempts have been made, then BigDecimal.ZERO
     *         is returned.  Never null.  Always greater than or equal to
     *         BigDecimal.ZERO.
     */
    public abstract BigDecimal lastWriteTimeSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * @return The number of elements read in so far with read (),
     *         or Long.MAX_VALUE if more than Long.MAX_VALUE - 1L
     *         have been read in (such as for an infinite Term).
     *         Always 0L or greater.
     */
    public abstract long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * <p>
     * Reads the next One element from this Stream, if possible.
     * </p>
     *
     * @param context The Context in which the stream data will be read
     *                from the Pipe.  This Stream must be part of the
     *                specified Context's graph of Pipes and Streams
     *                (though no contract enforcement is done, to
     *                minimize overhead).  Must not be null.
     *
     * @return The next One element from this Stream, if any;
     *         or No element if this Stream cannot provide any elements.
     *         Never null.
     */
    public abstract Maybe<VALUE> nextElement (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The index of the next element, if any, or -1L if
     *         the last call to nextElement () returned an empty result.
     *         Always -1L or greater.
     */
    public abstract long nextIndex ()
        throws Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation;


    /**
     * @return The number of Terms that have been read in from this
     *         Stream so far.  Always 0L or greater.
     */
    public abstract long numTerms ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * <p>
     * Reads the next Term from this Stream, if any; or performs
     * one iterative or recursive step on the input Pipe to generate
     * the next Term, then returns it; or returns No Term at all, if there are
     * no Terms available to be read.
     * </p>
     *
     * @param context The Context in which the stream data will be read
     *                from the Pipe.  This Stream must be part of the
     *                specified Context's graph of Pipes and Streams
     *                (though no contract enforcement is done, to
     *                minimize overhead).  Must not be null.
     *
     * @return The One next Term, if possible; or No next Term, if this
     *         Stream cannot return any more Terms right now.  Never null.
     */
    public abstract Maybe<Term<VALUE>> read (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The Stream.State of this Stream: OPEN or CLOSED.
     *         Never null.
     */
    public abstract Stream.State state ()
        throws ReturnNeverNull.Violation;


    // Every Stream must implement java.lang.Object#toString()


    /**
     * @return The Type of Terms flowing through this Stream.
     *         Never null.
     *
     * @see musaico.foundation.term.TermPipeline#type()
     */
    public abstract Type<VALUE> type ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Writes the specified Term to this Stream, so that
     * it will flow downstream.
     * </p>
     *
     * @param term The Term to write to this Stream.  Must not be null.
     *
     * @return Stream.State.OPEN if the downstream Pipe is still open
     *         to receiving Terms through this Stream's
     *         <code> write ( ... ) </code>, Stream.State.CLOSED if the
     *         downstream Pipe cannot read any more Terms, and is
     *         finished its processing.  Never null.
     */
    public abstract Stream.State write (
            Term<VALUE> term
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
