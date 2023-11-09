package musaico.foundation.value.blocking;

import java.io.Serializable;

import java.remote.RemoteException;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.NonBlocking;
import musaico.foundation.value.NotOne;
import musaico.foundation.value.Operation;
import musaico.foundation.value.ProgressiveOperation;
import musaico.foundation.value.Type;
import musaico.foundation.value.ValueViolation;

import musaico.foundation.value.abnormal.Error;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;

import musaico.foundation.value.incomplete.Expression;


/**
 * <p>
 * An asynchronous, delayed result which wakes up the parent Blocked
 * object after waiting, successfully or unsuccessfully, for the
 * asynchronous conditional Value.
 * </p>
 *
 *
 * <p>
 * In Java, every Result must be Serializable in order to play nicely
 * over RMI.  Anyone who wants to access a Result remotely should use
 * its <code> rmi () </code> Result wrapper.  A Result itself
 * could be simply a Serializable object which has been serialized
 * and no longer points to anything meaningful.  By accessing its
 * <code> rmi () </code> Result wrapper instead, the serialized object
 * will be discarded, and the actual remote Result used instead.
 * Note that the remote RMI system must have called
 * <code> RMIResult.serverInitialize () </code> at startup, otherwise
 * the <code> rmi () </code> method of each Result from that server
 * will fail with a RemoteException.
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
 * @see musaico.foundation.value.blocking.MODULE#COPYRIGHT
 * @see musaico.foundation.value.blocking.MODULE#LICENSE
 */
public interface Result<VALUE extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Subscribes the specified Expression to this Result, so that its
     * <code> completed () </code> method will be kicked when this Result
     * is finished.
     * </p>
     *
     * <p>
     * If the result has already been asynchronously set, then
     * the specified Expression's <code> completed () </code> method
     * will be invoked immediately, in this thread.
     * </p>
     *
     * @see musaico.foundation.value.incomplete.CompletionPossible#completed()
     *
     * @param expression The Expression whose <code> completed () </code>
     *                   method will be invoked when this Result has a
     *                   final answer.  Must not be null.
     */
    public abstract <OUTPUT extends Object>
        void async (
                    Expression<VALUE, OUTPUT> expression
                    )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Blocks the caller until either the asynchronous result is
     * retrieved and returned, or until the asynchronous result times
     * out, whichever happens first.
     * </p>
     *
     * <p>
     * The timeout occurs after <code> min ( this.maxTimeoutInNanoseconds,
     * timeut_in_nanoseconds ) </code>.  So even if the parameter timeout
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
     * If the current Thread is interrupted, then a Cancelled
     * result is returned, but the final result is NOT set.
     * </p>
     *
     * <p>
     * If the asynchronous result is received before timing out, then
     * the result could be anything -- a Successful result, a
     * FailedResult, a PartialResult, and so on.
     * </p>
     *
     * @param blocking_value The Blocking Value which is waiting on this
     *                       asynchronous result.  Must not be null.
     *
     * @param timeout_in_nanoseconds The maximum amount of time the caller
     *                               is willing to wait for the blocking
     *                               call, in nanoseconds.  0L can be passed
     *                               to not block at all, just immediately
     *                               return the final result or No result
     *                               if incomplete.  Must be greater
     *                               than or equal to 0L.
     */
    public abstract NonBlocking<VALUE> await (
                Blocking<VALUE> blocking_value,
                long timeout_in_nanoseconds
                )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation;


    /**
     * @return True if a partial result is available, even if the
     *         final result is not; false if no partial value has been
     *         set.
     */
    public abstract boolean hasPartialResult ();


    /**
     * @return True if this Result has finished blocking
     *         and has a result; false if it is still blocking.
     */
    public abstract boolean isFinishedBlocking ();


    /**
     * @return The maximum amount of time, in nanoseconds, that this
     *         Result will take, before giving up await ().
     *         If this Result is already complete, then
     *         0L will be returned.  Otherwise the default maximum time.
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
     *         limiting factor before a failed result is returned,
     *         and in the second case the Blocking Value is not
     *         guaranteed to be complete by the implementer after
     *         the 30 second timeout, but after 30 seconds the caller
     *         will be given a failed result anyway.
     *         Always greater than 0L nanoseconds.
     */
    public final abstract maxTimeoutInNanoseconds ();


    /**
     * <p>
     * If either the final result or a partial result is available,
     * returns that value.  Otherwise returns No value.
     * </p>
     *
     * @param violation The ValueViolation to use if no partial result
     *                  is available.  Must not be null.
     *
     * @return The final result, if it is available.  Or a partial
     *         result, if the asynchronous worker has provided one.
     *         Otherwise No value.  Never null.
     */
    public abstract NonBlocking<VALUE> partialResult (
            ValueViolation violation
            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Removes the specified Expression from those which will operate on
     * this Result when it is eventually set.
     * </p>
     *
     * @param expression The Expression which will no longer operate on this
     *                   asynchronous result when the time comes.
     *                   Must not be null.
     */
    public abstract <OUTPUT extends Object>
        void removeExpression (
                               Expression<VALUE, OUTPUT> expression
                               );


    /**
     * @return A wrapper for the original Result captured in this object,
     *         which might have been on a different system, but been
     *         serialized into this for sending across the wire.  The
     *         <code> rmi () </code> method always returns a Result
     *         on the source system, such as a UnicastRemoteObject.
     *         Even if this Result is the original object, an RMI-safe
     *         wrapper must be returned.
     *         Never null.
     *
     * @see musaico.foundation.value.blocking.RMIResult
     *
     * @throws RemoteException If the remote system has
     *                         not been initialized, or if
     *                         there is any other problem
     *                         accessing the remote Result.
     */
    public abstract Result<VALUE> rmi ()
        throws RemoteException;


    /**
     * <p>
     * Sets the final asynchronous result.
     * </p>
     *
     * <p>
     * The first time this method is called, the result is stored,
     * any expressions are notified, and the specified final
     * result is returned.
     * </p>
     *
     * <p>
     * Upon any subsequent call, nothing happens, and the original
     * final result is returned.
     * </p>
     *
     * @param final_resut The final asynchronous result.  Must not be null.
     *
     * @return The final asynchronous result.  The specified final result
     *         is returned unless the final result has already been
     *         set previously.  Never null.
     */
    public abstract NonBlocking<VALUE> setFinalResult (
            NonBlocking<VALUE> final_result
            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Sets the current partial asynchronous result.
     * </p>
     *
     * <p>
     * The partial result can be updated zero or more times before the
     * final result is set.
     * </p>
     *
     * <p>
     * Each time a partial result is set,
     * <code> progress () </code> is invoked on each expression
     * waiting on this Result.
     * </p>
     *
     * <p>
     * If the final result is already set, then no processing occurs, and
     * the final result is returned.
     * </p>
     *
     * <p>
     * THIS IS A BLOCKING CALL.  The Expressions could conceivably
     * block indefinitely.  Use with caution!
     * </p>
     *
     * @param partial_resut The current partial asynchronous result.
     *                      Must not be null.
     *
     * @return The partial asynchronous result, unless the final result
     *         has already been set, in which case it is returned.
     *         Never null.
     */
    public abstract NonBlocking<VALUE> setPartialResult (
            NonBlocking<VALUE> partial_result
            )
        throws ParametersMustNotBeNull.Violation;


    // Every Result must implement toString ().


    /**
     * @return The type of this asynchronous result.
     *         Never null.
     */
    public abstract Type<VALUE> type ();
}
