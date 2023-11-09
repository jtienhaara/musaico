package musaico.foundation.value.blocking;

import java.io.Serializable;

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
public class AsynchronousResult<VALUE extends Object>
    implements Result<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Enforces static parameter obligations and so on for us. */
    private static final Advocate classContracts =
        new Advocate ( AsynchronousResult.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // Lock critical sections on this token.
    private final Serializable lock = new String ();

    // The type of the asynchronous conditional Value.
    private final Type<VALUE> type;

    // The absolute maximum amount of time to wait for the asynchronous
    // result, even if it is shorter than the length of time the
    // caller is willing to block() for.
    private final long maxTimeoutInNanoseconds;

    // In case we ever need to look up our original source via rmi (),
    // we keep track of a remotely accessible version of ourselves.
    // Can be null.
    private final Result<VALUE> rmiSource;

    // Expression(s) which depend on this AsynchronousResult
    // in order to pipe the final result through their Operations.
    // Each Expression will be kicked asynchronously,
    // if/when the result comes in, or when it times out or is cancelled,
    // and so on.
    // MUTABLE, since the contents can change over time.
    private final List<Expression<VALUE, ?>> expressions =
        new ArrayList<Expression<VALUE, ?>> ();

    // The final Value, after all asynchronous processing is complete.
    // MUTABLE.
    private NonBlocking<VALUE> finalResult = null;

    // A partial Value.  The asynchronous worker may choose to update
    // this periodically while waiting for the final result, so that
    // anyone who is interested in partial results can get them without
    // waiting for the whole asynchronous process.
    // MUTABLE.
    private NonBlocking<VALUE> partialResult = null;


    /**
     * <p>
     * Creates a new AsynchronousResult, which will block for some maximum
     * amount of time before failing with a Timeout.
     * </p>
     *
     * <p>
     * Once the result has been set (or the result has timed out), any and
     * all waiting expression(s) are notified, and the result is stored so
     * that it can be retrieved again instantly.
     * </p>
     *
     * @param type The Type of the conditional Value.
     *             Must not be null.
     *
     * @param max_timeout_in_nanoseconds The absolute maximum amount of
     *                                   time to await () for the asynchronous
     *                                   result before giving up.
     *                                   Even if the block() caller
     *                                   specifies a longer timeout,
     *                                   this is the absolute maximum
     *                                   blocking time.  Must be greater
     *                                   than 0L.
     */
    public AsynchronousResult (
                               Type<VALUE> type,
                               long max_timeout_in_nanoseconds
                               )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanZero.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type );
        classContracts.check ( Parameter2.MustBeGreaterThanZero.CONTRACT,
                               max_timeout_in_nanoseconds );

        this.type = type;
        this.maxTimeoutInNanoseconds = max_timeout_in_nanoseconds;

        this.contracts = new Advocate ( this );

        final RMIResult.Server rmi_server = RMIResult.server ();
        if ( this.rmiServer != null )
        {
            this.rmiSource = rmi_server.makeRemotable ( this );
        }
        else
        {
            this.rmiSource = null;
        }
    }


    /**
     * <p>
     * Adds the specified Expression to those which will operate on
     * this AsynchronousResult when it is eventually set.
     * </p>
     *
     * <p>
     * If the result has already been asynchronously set, then
     * the specified Expression's Operation will apply the result
     * immediately, in this thread.
     * </p>
     *
     * @param expression The Expression whose Operation
     *                   will operate on this asynchronous result when
     *                   the time comes.  Must not be null.
     */
    public <OUTPUT extends Object>
        void async (
                    Expression<VALUE, OUTPUT> expression
                    )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               expression );

        synchronized ( this.lock )
        {
            if ( this.finalResult != null )
            {
                // Do not add the expression to the list of expressions;
                // just kick the Expression immediately.
                // The Expression will request the final result
                // when we call its completed () method.
                expression.completed ();
                return;
            }

            // Later on, we'll kick the expressions individually, in order.
            this.expressions.add ( expression );
        }
    }


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
     * is specified as, say, 2 minutes, this AsynchronousResult's
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
    public final NonBlocking<VALUE> await (
                                           Blocking<VALUE> blocking_value,
                                           long timeout_in_nanoseconds
                                           )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               blocking_value );
        this.contracts.check ( Parameter2.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               timeout_in_nanoseconds );

        final long min_timeout_in_nanoseconds;
        if ( timeout_in_nanoseconds <= this.maxTimeoutInNanoseconds )
        {
            min_timeout_in_nanoseconds = timeout_in_nanoseconds;
        }
        else
        {
            min_timeout_in_nanoseconds = this.maxTimeoutInNanoseconds;
        }

        final long timeout_in_milliseconds =
            min_timeout_in_nanoseconds / 1000000000L;
        synchronized ( this.lock )
        {
            if ( this.finalResult == null )
            {
                final long start_time_in_milliseconds =
                    System.currentTimeMillis ();
                try
                {
                    this.lock.wait ( timeout_in_milliseconds );
                }
                catch ( InterruptedException e )
                {
                    // Return a Cancelled value to the caller,
                    // but do not abort this AsynchronousResult
                    // or set the final result.  cancel () should be
                    // used to abort this AsynchronousResult
                    // and set the final result to Cancelled.
                    final BlockingMustComplete.CancelledTime<VALUE> cancelled_time =
                        new BlockingMustComplete.CancelledTime<VALUE> (
                            blocking_value,
                            min_timeout_in_nanoseconds );
                    final ValueViolation value_violation =
                        BlockingMustComplete.violation (
                            min_timeout_in_nanoseconds,
                            cancelled_time );
                    final Cancelled<VALUE> cancelled =
                        new Cancelled<VALUE> ( this.type,
                                               blocking_value, // cause
                                               value_violation,
                                               this.partialResult );
                    return cancelled;
                }

                if ( this.finalResult == null )
                {
                    final BlockingMustComplete.ElapsedTime<VALUE> elapsed_time =
                        new BlockingMustComplete.ElapsedTime<VALUE> (
                            blocking_value,
                            min_timeout_in_nanoseconds );
                    final ValueViolation value_violation =
                        BlockingMustComplete.violation (
                            min_timeout_in_nanoseconds,
                            elapsed_time );
                    final Timeout<VALUE> timeout =
                        new Timeout<VALUE> ( this.type,
                                             blocking_value, // cause
                                             value_violation,
                                             this.partialResult );
                    this.setFinalResult ( timeout );
                }
            }

            return this.finalResult;
        }
    }


    /**
     * @return True if a partial result is available, even if the
     *         final result is not; false if no partial value has been
     *         set.
     */
    public boolean hasPartialResult ()
    {
        synchronized ( this.lock )
        {
            if ( this.partialResult != null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }


    /**
     * @return True if this AsynchronousResult has finished blocking
     *         and has a result; false if it is still blocking.
     */
    public boolean isFinishedBlocking ()
    {
        synchronized ( this.lock )
        {
            if ( this.finalResult != null )
            {
                // Finished blocking.
                return true;
            }
            else
            {
                // Not yet.
                return false;
            }
        }
    }


    /**
     * @return The maximum amount of time, in nanoseconds, that this
     *         AsynchronousResult will take, before giving up await ().
     *         If this AsynchronousResult is already complete, then
     *         0L will be returned.  Otherwise the default maximum time.
     *         This default maximum time is specified by the creator of
     *         the AsynchronousResult to be the maximum amount of time
     *         the process will take to return a result, regardless
     *         of whether the await () caller is willing to block
     *         for more or less time.  For instance, an AsynchronousResult
     *         waiting on a read from a disk might guarantee a
     *         result within 10 seconds, whereas an AsynchronousResult
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
    public final long maxTimeoutInNanoseconds ()
    {
        final boolean isComplete;
        synchronized ( this.lock )
        {
            if ( this.finalResult == null )
            {
                isComplete = false;
            }
            else
            {
                isComplete = true;
            }
        }

        if ( isComplete )
        {
            // Already done.
            return 0L;
        }
        else
        {
            // Default maximum blocking time, specified by the
            // creator of this AsynchronousResult.
            return this.maxTimeoutInNanoseconds;
        }
    }


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
    public NonBlocking<VALUE> partialResult (
                                             ValueViolation violation
                                             )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               violation );

        synchronized ( this.lock )
        {
            if ( this.finalResult != null )
            {
                return this.finalResult;
            }
            else if ( this.partialResult != null )
            {
                return this.partialResult;
            }
        }

        final No<VALUE> no_partial_value =
            new No<VALUE> ( this.type,
                            violation );
        return no_partial_value;
    }


    /**
     * <p>
     * Removes the specified Expression from those which will operate on
     * this AsynchronousResult when it is eventually set.
     * </p>
     *
     * @param expression The Expression which will no longer operate on this
     *                   asynchronous result when the time comes.
     *                   Must not be null.
     */
    public <OUTPUT extends Object>
        void removeExpression (
                               Expression<VALUE, OUTPUT> expression
                               )
    {
        synchronized ( this.lock )
        {
            this.expressions.remove ( expression );
        }
    }


    /**
     * @see musaico.foundation.value.blocking.Result#rmi()
     */
    @Override
    public Result<VALUE> rmi ()
        throws RemoteException
    {
        if ( this.rmiSource == null )
        {
            // Source RMI server was never initialized, so we have no
            // way to communicate with the source JVM, or to know
            // whether this is the original source object.
            throw new RemoteException (
                ClassName.of ( this.getClass () )
                + ".rmi (): Cannot return the original source of "
                + this
                + " because RMIResult.server () was not available"
                + " at construction time." );
        }

        return this.rmiSource;
    }


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
    public final NonBlocking<VALUE> setFinalResult (
                                                    NonBlocking<VALUE> final_result
                                                    )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               final_result );

        final List<Expression<VALUE, ?>> expressions;
        synchronized ( this.lock )
        {
            if ( this.finalResult != null )
            {
                // The result was set previously.  Don't do anything now.
                return this.finalResult;
            }

            this.finalResult = final_result;
            this.partialResult = null;

            this.lock.notifyAll ();

            expressions =
                new ArrayList<Expression<VALUE, ?>> ( this.expressions );

            // Remove the asynchronous expressions, so that they
            // don't tie up memory forever etc.
            this.expressions.clear ();
        }

        // Now notify listeners:
        for ( Expression<VALUE, ?> expression : expressions )
        {
            // The Expression will request the final result
            // when we call its complete () method.
            expression.completed ();
        }

        return final_result;
    }


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
     * waiting on this AsynchronousResult.
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
    @SuppressWarnings("unchecked") // Cast ProgOp<?,?>-ProgOp<Object,Object>.
    public final NonBlocking<VALUE> setPartialResult (
                                                      NonBlocking<VALUE> partial_result
                                                      )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               partial_result );

        final List<Expression<VALUE, ?>> expressions;
        synchronized ( this.lock )
        {
            if ( this.finalResult != null )
            {
                // The result was set previously.  Don't do anything now.
                return this.finalResult;
            }

            this.partialResult = partial_result;

            expressions =
                new ArrayList<Expression<VALUE, ?>> ( this.expressions );
        }

        // Now notify listeners:
        for ( Expression<VALUE, ?> expression : expressions )
        {
            final Operation<?, ?> [] operations = expression.operations ();
            if ( operations.length >= 1
                 && operations [ 0 ] instanceof ProgressiveOperation )
            {
                final ProgressiveOperation<Object, Object> progressive =
                    (ProgressiveOperation<Object, Object>) operations [ 0 ];
                try
                {
                    progressive.progress ( (NonBlocking<Object>)
                                           partial_result );
                }
                catch ( Exception e )
                {
                    // Badly formed ProgressiveOperation.
                    // Spit out stderr, but there
                    // is nothing anybody can do about it at this end.
                    // Blowing up the asynchronous result provider will not
                    // help anyone figure out why one particular
                    // ProgressiveOperation couldn't finish its job.
                    // This is ugly, but necessary.
                    System.err.println ( "AsynchronousResult "
                                         + this + " error operating on"
                                         + " partial result"
                                         + " caused by "
                                         + expression + ":" );
                    e.printStackTrace ();
                }
            }
        }

        return partial_result;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();

        final NonBlocking<VALUE> final_result;
        final NonBlocking<VALUE> partial_result;
        final List<Expression<VALUE, ?>> expressions;
        synchronized ( this.lock )
        {
            final_result = this.finalResult;
            partial_result = this.partialResult;
            expressions =
                new ArrayList<Expression<VALUE, ?>> ( this.expressions );
        }

        if ( final_result == null )
        {
            sbuf.append ( "Unfinished " );
        }

        sbuf.append ( ClassName.of ( this.getClass () ) );
        sbuf.append ( ":" );

        sbuf.append ( "\n{" );

        sbuf.append ( "\n    type = "
                      + this.type );
        sbuf.append ( "\n    maxTimeoutInNanoseconds = "
                      + this.maxTimeoutInNanoseconds );
        sbuf.append ( "\n    expressions = "
                      + expressions );
        if ( final_result != null )
        {
            sbuf.append ( "\n    finalResult = " + final_result );
        }
        else if ( partial_result != null )
        {
            sbuf.append ( "\n    partialResult = " + partial_result );
        }
        sbuf.append ( "\n}" );

        return sbuf.toString ();
    }


    /**
     * @return The type of this asynchronous result.
     *         Never null.
     */
    public Type<VALUE> type ()
    {
        return this.type;
    }
}
