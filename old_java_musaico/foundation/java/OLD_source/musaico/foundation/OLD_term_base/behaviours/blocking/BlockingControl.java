package musaico.foundation.term;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Iterator;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;
import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.order.Order;


/**
 * <p>
 * Controls behaviour when a Term must block, waiting for I/O or
 * some timing event and so on.
 * </p>
 *
 *
 * <p>
 * In Java every BehaviourControl must be Serializable in order to
 * play nicely across RMI.  However users of the BehaviourControl
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
 * </p>
 *
 * <p>
 * In Java every BehaviourControl must implement equals (), hashCode ()
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public interface BlockingControl<VALUE extends Object, BUILT extends Object>
    extends BehaviourControl<VALUE, BUILT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Adds an operation to the pipeline which will process
     * each blocking input Term asynchronously with the specified Operation,
     * or immediately if it is non-blocking.
     * </p>
     *
     * <p>
     * A new Expression is created which will be evaluated automatically
     * at some point in future, whenever blocking is finished,
     * or right away, for a non-blocking input Term.
     * </p>
     *
     * <p>
     * If the Operation is a ProgressiveOperation, then any time
     * a blocking Term's partial / intermediate value is updated,
     * the operation's <code> progress () </code> method
     * will be invoked.
     * </p>
     *
     * @see musaico.foundation.term.operations.ProgressiveOperation#progress(musaico.foundation.term.Term)
     *
     * @see musaico.foundation.term.BlockingControl#await(java.math.BigDecimal)
     *
     * <p>
     * Pipeline output: Either an Expression to be evaluated in future,
     * when the final value is available, or the output generated
     * by passing the final value itself through the specified
     * Operations, if the final value has already been set.
     * </p>
     *
     * @param callback The Operation to evaluate whenever blocking
     *                 is finished.  Must not be null.
     *
     * @return This pipeline.  Never null.
     */
    public abstract Control<VALUE, BUILT> async (
            Operation<VALUE, ?> callback
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Causes a blocking Term to block up to the specified amount of time,
     * waiting for the final result.
     * </p>
     *
     * <p>
     * A result which is complete (not blocking) and single will
     * return itself immediately.
     * </p>
     *
     * <p>
     * A Blocking result, on the other hand, will block up to the
     * specified time, waiting for an asynchronous result.  If the
     * result is still unknown after blocking for that period, the
     * Blocking result might return another Blocking result, or it might
     * return an abject Timeout, depending on whether it thinks
     * the caller might want to keep waiting.
     * </p>
     *
     * <p>
     * Pipeline output: the conditional result after waiting
     * up to the specified time for the asynchronous result
     * to come in.  Could be One result, a Finite term,
     * an Infinite term, No value, a Timeout result, and so on,
     * depending on the state of the query results when the
     * blocking period is over.
     * </p>
     *
     * @param timeout_in_seconds How long (maximum) to wait before
     *                           giving up on the asynchronous result
     *                           and returning either another Blocking
     *                           result or a Timeout result.
     *                           Must be greater than or equal to
     *                           BigDecimal.ZERO seconds.
     *
     * @return This pipeline.
     *         Never null.
     */
    public abstract Control<VALUE, BUILT> await (
            BigDecimal timeout_in_seconds
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation;


    // Every BlockingControl must implement
    // musaico.foundation.term.BehaviourControl#behaviour()


    /**
     * <p>
     * Aborts blocking, causing any and all dependents waiting on
     * a result to receive a failed result, unless the Term
     * is not (any longer) blocking anyway.
     * </p>
     *
     * <p>
     * For example, a blocking Term representing some I/O operation could block
     * a thread for some period of time.  If, in the meantime, the
     * system to which the thread belongs must exit, the system
     * can simply <code> cancel () </code> the I/O, allowing the blocking Term
     * to tie up any loose ends before aborting the I/O attempt,
     * and allowing the blocking thread to clean up its own loose ends
     * after receiving the failed result.
     * </p>
     *
     * <p>
     * On the other hand, <code> cancel () </code>ling a non-blocking
     * Term has no effect at all.
     * </p>
     *
     * <p>
     * Pipeline output: the cancelled result.  Can be a failed term
     * (such as a Cancelled term) if a blocking call was actually
     * cancelled, or, for a non-blocking Term, the same, unchanged term.
     * </p>
     *
     * @param violation The exception which is thrown when each
     *                  recipient expects a single value and invokes
     *                  <code> orThrowUnchecked () </code> on the
     *                  cancelled result.  Must not be null.
     *
     * @return This pipeline.  Never null.
     */
    public abstract Control<VALUE, BUILT> cancel (
            TermViolation violation
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every BlockingControl must implement java.lang.Object#equals(java.lang.Object)


    // Every BlockingControl must implement java.lang.Object#hashCode()


    // Every BlockingControl must implement java.lang.Object#toString()
}
