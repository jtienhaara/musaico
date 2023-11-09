package musaico.foundation.term.blocking;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.capability.Capability;
import musaico.foundation.capability.StandardCapability;

import musaico.foundation.capability.operating.OperatingState;

import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.Clock;

import musaico.foundation.term.NonBlocking;
import musaico.foundation.term.NotOne;
import musaico.foundation.term.Operation;
import musaico.foundation.term.Type;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.expression.Expression;

import musaico.foundation.term.finite.No;
import musaico.foundation.term.finite.One;


/**
 * <p>
 * The output of some asynchronous process or other.
 * </p>
 *
 *
 * <p>
 * In Java, every Result must be Serializable in order to play nicely
 * over RMI.  WARNINGS: First of all, in an RMI environment, a Result
 * can only be meaningfully serialized if it is merely a client to
 * some UnicastRemoteObject.  Second, the final and partial
 * or intermediate values of a Result need not be Serializable.
 * If a Result containing a non-Serializable value is passed
 * to or from a method over an RMI remote request, it will
 * generate a RemoteException.
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
 * @see musaico.foundation.term.blocking.MODULE#COPYRIGHT
 * @see musaico.foundation.term.blocking.MODULE#LICENSE
 */
public interface Result<VALUE extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** The Capability of setting the final value for an
     *  asynchronous Result.  Typically the Result implementtion
     *  hides the interface to implement this Capability, since
     *  the Result interface is for passive subscribers, not
     *  for the publishers of result values. */
    public static final Capability SET_FINAL_RESULT =
        new StandardCapability ( "set_final_result" );

    /** The Capability of setting an intermediate or partial value for an
     *  asynchronous Result.  Typically the Result implementtion
     *  hides the interface to implement this Capability, since
     *  the Result interface is for passive subscribers, not
     *  for the publishers of result values. */
    public static final Capability UPDATE_PARTIAL_RESULT =
        new StandardCapability ( "update_partial_result" );


    /**
     * <p>
     * Creates a new Expression which will be evaluated automatically
     * at some point in future, whenever this Result is finished.
     * </p>
     *
     * <p>
     * If the final result value has already been asynchronously set, then
     * the specified callback Operation(s) will be evaluated immediately,
     * and the output returned.
     * </p>
     *
     * <p>
     * If the first Operation is a ProgressiveOperation, then any time
     * this Result's partial / intermediate value is updated,
     * the operation's <code> progress () </code> method
     * will be invoked.
     * </p>
     *
     * @see musaico.foundation.term.operations.ProgressiveOperation#progress(musaico.foundation.term.NonBlocking)
     *
     * <p>
     * For details on the final Terms which can be asynchronusly
     * set, see <code> await () </code>:
     * </p>
     *
     * @see musaico.foundation.term.blocking.Result#await(musaico.foundation.term.blocking.Blocking, java.math.BigDecimal)
     *
     * @param parent_blocking_term The Blocking term which is not complete
     *                             until this Result's final value is set.
     *                             Must not be null.
     *
     * @param callback The Operation to evaluate whenever this
     *                  Result is finished.  Must not be null.
     *
     * @return Either an Expression to be evaluated in future, when the
     *         final value is available, or the output generated
     *         by passing the final value itself through the specified
     *         Operations, if the final value has already been set.
     *         Never null.
     */
    public abstract <OUTPUT extends Object>
        Term<OUTPUT> async (
                Blocking<VALUE> parent_blocking_term,
                Operation<VALUE, OUTPUT> callback
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;


    /**
     * <p>
     * Blocks the caller until either the asynchronous result value is
     * retrieved and returned, or until the asynchronous result times
     * out, whichever happens first.
     * </p>
     *
     * <p>
     * The timeout occurs after <code> min ( this.maxTimeoutInSeconds (),
     * timeut_in_seconds ) </code>.  So even if the parameter timeout
     * is specified as, say, 2 minutes, this Result's
     * maximum timeout might be only, say, 10 seconds.  In such a case,
     * the timeout would occur after 10 seconds.
     * </p>
     *
     * <p>
     * If this blocking call times out, then a Timeout failed result
     * is returned.
     * </p>
     *
     * <p>
     * If this Result's <code> stop () </code> method is invoked
     * successfully, then the final value is set to a Cancelled result,
     * and returned.
     * </p>
     *
     * <p>
     * If the current Java Thread is interrupted, then a Cancelled
     * result is returned, but the final result is NOT set.
     * </p>
     *
     * <p>
     * Even if this asynchronous result's final value is received
     * without timing out or being cancelled, the result could
     * still be anything: a successful Just result, or an
     * Error, or only a PartialResult, and so on.
     * </p>
     *
     * @param blocking_term The Blocking Term which is waiting on this
     *                      asynchronous result's final value.
     *                      Must not be null.
     *
     * @param timeout_in_seconds The maximum amount of time the caller
     *                           is willing to wait for the blocking
     *                           call, in seconds.  BigDecimal.ZERO
     *                           can be passed to not block at all,
     *                           just immediately return the final value
     *                           or a Timeout result if incomplete.
     *                           Must not be null.  Must be greater than
     *                           or equal to BigDecimal.ZERO.
     */
    public abstract NonBlocking<VALUE> await (
            Blocking<VALUE> blocking_term,
            BigDecimal timeout_in_seconds
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation;


    /**
     * @return The Clock used by this asynchronous Result to measure start
     *         time and end time since 0 UN*X time.  Never null.
     */
    public abstract Clock clock ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The amount of time passed, in seconds, from the start ()
     *         of processing until the final value was received or
     *         the Result timed out or was Cancelled (including via stop ()),
     *         if processing has concluded for any reason;
     *         or the amount of time from the start () of processing
     *         until now, according to the <code> clock () </code>,
     *         if processing has started but not stopped;
     *         or BigDecimal.ZERO if processing has not even
     *         started yet.  Can also be BigDecimal.ZERO even if
     *         processing has started and/or stopped.
     *         Always greater than or equal to BigDecimal.ZERO.
     *         Never null.
     */
    public abstract BigDecimal elapsedTimeInSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * @return True if this Result has finished blocking
     *         and has its final value; false if it is still blocking.
     */
    public abstract boolean hasFinalTerm ();


    /**
     * @return True if a partial result is available, even if the
     *         final result is not; false if no partial value has been
     *         set.
     */
    public abstract boolean hasPartialTerm ();


    /**
     * @return The maximum amount of time, in seconds, that this
     *         Result will take, before giving up await ().
     *         If this Result is already complete, then BigDecimal.ZERO
     *         will be returned.  Otherwise the default maximum time.
     *         This default maximum time is specified by the creator of
     *         the Result to be the maximum amount of time
     *         the process will take to return a result, regardless
     *         of whether the await () caller is willing to block
     *         for more or less time.  For instance, an Result
     *         waiting on a read from a disk might guarantee a
     *         result within 10 seconds, whereas an Result
     *         waiting on a result from the network might guarantee
     *         a result within 2 minutes.  The caller to the await ()
     *         method might be willing to wait 30 seconds, but in the
     *         first case the overall maximum 10 seconds will be the
     *         limiting factor before a Timeout result is returned,
     *         and in the second case the final Term is not
     *         guaranteed to be set after the 30 second timeout,
     *         though after 30 seconds the caller will be given
     *         a Timeout result anyway.  Never null.
     *         Always greater than or equal to BigDecimal.ZERO seconds.
     */
    public abstract BigDecimal maxTimeoutInSeconds ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The current operating state of this Result, such as
     *         OperatingState.STARTED, OperatingState.PAUSED,
     *         OperatingState.STOPPED, or an ErrorState, and so on.
     *         Before this Result's work has started, its state
     *         is always OperatingState.NONE.  Never null.
     */
    public abstract OperatingState operatingState ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The final value, if it is available.  Or a partial
     *         value, if the asynchronous worker has provided one.
     *         Otherwise No value.  Never null.
     */
    public abstract NonBlocking<VALUE> partialTerm ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Asks that the publisher of this asynchronous Result pause
     * its work toward the final value.
     * </p>
     *
     * @return The new OperatingState of this Result, which could be
     *         an ErrorState to indicate that it could not be paused.
     *         Never null.
     */
    public abstract OperatingState pause ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Asks that the publisher of this asynchronous Result resume
     * its paused work toward the final value.
     * </p>
     *
     * @return The new OperatingState of this Result, which could be
     *         an ErrorState to indicate that it could not be resumed.
     *         Never null.
     */
    public abstract OperatingState resume ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Asks that the publisher of this asynchronous Result start
     * its work toward the final value.
     * </p>
     *
     * @return The new OperatingState of this Result, which could be
     *         an ErrorState to indicate that it could not be paused.
     *         Never null.
     */
    public abstract OperatingState start ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Asks that the publisher of this asynchronous Result stop
     * its work toward the final value.
     * </p>
     *
     * <p>
     * When this method is successful, the final value of this
     * Result is set to a Cancelled term.
     * </p>
     *
     * @return The new OperatingState of this Result, which could be
     *         an ErrorState to indicate that it could not be paused.
     *         Never null.
     */
    public abstract OperatingState stop ()
        throws ReturnNeverNull.Violation;


    // Every Result must implement toString ().


    /**
     * @return The type of this asynchronous result.
     *         Never null.
     */
    public abstract Type<VALUE> type ();
}
