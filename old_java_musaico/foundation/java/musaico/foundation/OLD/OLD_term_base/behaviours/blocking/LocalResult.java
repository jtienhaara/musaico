package musaico.foundation.term.blocking;

import java.io.Serializable;

import java.util.LinkedHashSet;

import java.math.BigDecimal;


import musaico.foundation.capability.operating.ErrorState;
import musaico.foundation.capability.operating.OperatingState;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.Parameter5;
import musaico.foundation.contract.obligations.Parameter6;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.Clock;
import musaico.foundation.domains.Seconds;

import musaico.foundation.term.ElementalOperation;
import musaico.foundation.term.NonBlocking;
import musaico.foundation.term.Operation;
import musaico.foundation.term.Type;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.contracts.TermMustBeNonBlocking;

import musaico.foundation.term.expression.Expression;

import musaico.foundation.term.finite.No;
import musaico.foundation.term.finite.One;

import musaico.foundation.term.operations.BlockingOperation;
import musaico.foundation.term.operations.CumulativeOperation;
import musaico.foundation.term.operations.ProgressiveOperation;


/**
 * <p>
 * An implementation of the basic aspects of an asynchronous Result.
 * </p>
 *
 * <p>
 * A LocalResult can be used in a local, non-RMI environment, as a simple
 * interface to provide background Results to Blocking terms.
 * </p>
 *
 * <pre>
 *     // Thread 1:
 *     final Result<String> result = new LocalResult<String> ( ... );
 *     ...spawn thread 2 to calculate the result's final value...
 *     final Blocking<String> blocking = new Blocking<String> ( result );
 *     final NonBlocking<String> eventual_value =
 *         blocking.await ( new BigDecimal ( "3600" ) ); // Wait up to an hour.
 * </pre>
 *
 * <pre>
 *     // Thread 2:
 *     final NonBlocking<String> partial_value1 =
 *         new One<String> ( ..., "Hello" );
 *     result.setPartialTerm ( partial_value1 );
 *     ...
 *     final NonBlocking<String> partial_value2 =
 *         new Many<String> ( ..., "Hello", "world" );
 *     result.setPartialTerm ( partial_value2 );
 *     ...
 *     final NonBlocking<String> final_value =
 *         new Many<String> ( ..., "Hello", "brave", "new", "world!" );
 *     result.setFinalTerm ( final_value );
 * </pre>
 *
 *
 * <p>
 * In Java, every Result must be Serializable in order to play nicely
 * over RMI.  WARNING: The final and partial or intermediate terms
 * of a Result need not be Serializable.  If a Result containing a
 * non-Serializable term is passed to or from a method over an RMI
 * remote request, it will generate a RemoteException.
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
public class LocalResult<VALUE extends Object>
    implements Result<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( LocalResult.class );

    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // Lock critical sections on this token.
    private final Serializable lock = new String ();

    // The Type of term shared by all the Blocking results.
    private final Type<VALUE> termType;

    // The Clock used to determine the time since UN*X 0, used
    // for calculating timeouts and total elapsed time and so on.
    private final Clock clock;

    // How much time, at most, this LocalResult will
    // wait for a final value to be set.
    private final BigDecimal maxTimeoutInSeconds;


    // MUTABLE:
    // Once this result is start () ed, the start time since UN*X time 0
    // is recorded, so that we may later record the elapsed time.
    private BigDecimal startTimeInSeconds = null;

    // MUTABLE:
    // Once the final value has been set, the amount of elapsed
    // time, waiting for the final value, is recorded.
    private BigDecimal elapsedTimeInSeconds = null;

    // MUTABLE:
    // The current operating state of this Result, such as
    // OperatingState.STARTED, OperatingState.PAUSED,
    // OperatingState.STOPPED, or an ErrorState, and so on.
    // Before this Result's work has started, its state
    // is always OperatingState.NONE.
    private OperatingState operatingState = OperatingState.NONE;

    // MUTABLE:
    // The time, in seconds, at which this LocalResult
    // started its work, according to the Clock.  Null until start () ed.
    private BigDecimal startTime;

    // MUTABLE:
    // The asynchronous Expressions awaiting this Result's final value.
    // Can change contents over time.
    private final LinkedHashSet<Expression<VALUE, ?>> expressions =
        new LinkedHashSet<Expression<VALUE, ?>> ();

    // MUTABLE:
    // The final Term, after all asynchronous processing is complete.
    // Always null until set, then the Partial value is nulled out.
    private NonBlocking<VALUE> finalTerm = null;

    // MUTABLE:
    // A partial Term.  One or more of the Blocking terms
    // might have finished and been AND'ed or OR'ed together already,
    // even though not all blocking terms have finished.
    // Always null until the first time a partial value update
    // is recevied; then non-null until the final result is set,
    // after which it is always null.
    private NonBlocking<VALUE> partialTerm = null;

    // MUTABLE:
    // All threads which are currently await ( ... )ing the final value.
    // Changes content over time.
    // Not used internally, but can be accessed by derived classes,
    // if need be.
    private final LinkedHashSet<Thread> awaitingThreads =
        new LinkedHashSet<Thread> ();


    /**
     * <p>
     * Creates a new LocalResult, which will block
     * for some maximum amount of time before failing with a Timeout.
     * </p>
     *
     * <p>
     * Once the result has been set (or the result has timed out), any and
     * the result is stored so that it can be retrieved instantly
     * at any time (for example by asynchronous Expressions).
     * </p>
     *
     * @param term_type The Type common to all the Blocking terms.
     *                  Must not be null.
     *
     * @param clock The Clock used to measure start time and elapsed time.
     *              Also used to calculate the remainder of await ( ... ) time
     *              after the input is ready (if any at all), and await the
     *              remainder of the time on the output, in case it is also
     *              a Blocking or similar Incomplete result.
     *              Must not be null.
     *
     * @param max_timeout_in_seconds The absolute maximum amount of
     *                               time to block () for the asynchronous
     *                               result before giving up.
     *                               Even if the block() caller
     *                               specifies a longer timeout,
     *                               this is the absolute maximum
     *                               blocking time.  Must be greater
     *                               than BigDecimal.ZERO.
     */
    public LocalResult (
            Type<VALUE> term_type,
            Clock clock,
            BigDecimal max_timeout_in_seconds
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustBeGreaterThanZero.Violation
    {
        this ( term_type,
               clock,
               max_timeout_in_seconds,
               new No<VALUE> ( term_type ) ); // initial_partial_value
    }


    /**
     * <p>
     * Creates a new LocalResult, which will block
     * for some maximum amount of time before failing with a Timeout.
     * </p>
     *
     * <p>
     * Once the result has been set (or the result has timed out), any and
     * all Operation(s) are notified, and the result is stored so
     * that it can be retrieved again instantly.
     * </p>
     *
     * @param term_type The Type common to all the Blocking terms.
     *                  Must not be null.
     *
     * @param clock The Clock used to measure start time and elapsed time.
     *              Also used to calculate the remainder of await ( ... ) time
     *              after the input is ready (if any at all), and await the
     *              remainder of the time on the output, in case it is also
     *              a Blocking or similar Incomplete result.
     *              Must not be null.
     *
     * @param max_timeout_in_seconds The absolute maximum amount of
     *                               time to block () for the asynchronous
     *                               result before giving up.
     *                               Even if the block() caller
     *                               specifies a longer timeout,
     *                               this is the absolute maximum
     *                               blocking time.  Must be greater
     *                               than BigDecimal.ZERO.
     *
     * @param initial_partial_value An initial value for this Result,
     *                              before any partial values have
     *                              been generated.  Must not be null.
     */
    public LocalResult (
            Type<VALUE> term_type,
            Clock clock,
            BigDecimal max_timeout_in_seconds,
            NonBlocking<VALUE> initial_partial_value
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustBeGreaterThanZero.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               term_type, clock, max_timeout_in_seconds );
        classContracts.check ( Parameter3.MustBeGreaterThanZero.CONTRACT,
                               max_timeout_in_seconds );

        this.termType = term_type;
        this.clock = clock;
        this.startTime = null;
        this.maxTimeoutInSeconds = max_timeout_in_seconds;

        this.partialTerm = initial_partial_value;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.term.blocking.Result#async(musaico.foundation.term.blocking.Blocking, musaico.foundation.term.Operation)
     */
    @Override
    public final <OUTPUT extends Object>
        Term<OUTPUT> async (
                Blocking<VALUE> parent_blocking_term,
                Operation<VALUE, OUTPUT> callback
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               parent_blocking_term, callback );

        final Term<VALUE> final_value;
        final Expression<VALUE, OUTPUT> expression;
        synchronized ( this.lock )
        {
            final_value = this.finalTerm;
            if ( final_value == null )
            {
                // Still waiting.
                expression =
                    new Expression<VALUE, OUTPUT> (
                        parent_blocking_term, // input
                        callback.outputType (),
                        this.clock,
                        callback );
                this.expressions.add ( expression );
            }
            else
            {
                expression = null;
            }
        }

        if ( final_value == null )
        {
            return expression;
        }
        else if ( callback instanceof BlockingOperation )
        {
            return expression;
        }

        // The final value has already been set.
        // Just invoke the callback right away.
        final Term<OUTPUT> output = callback.apply ( final_value );
        return output;
    }


    /**
     * @see musaico.foundation.term.blocking.Result#await(musaico.foundation.term.blocking.Blocking, java.math.BigDecimal)
     */
    @Override
    public final NonBlocking<VALUE> await (
            Blocking<VALUE> blocking_term,
            BigDecimal timeout_in_seconds
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               blocking_term, timeout_in_seconds );
        this.contracts.check ( Parameter2.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               timeout_in_seconds );

        // Blocks until timeout or final result:
        NonBlocking<VALUE> final_value = null;

        final BigDecimal min_timeout_in_seconds;
        if ( timeout_in_seconds.compareTo ( this.maxTimeoutInSeconds ) <= 0 )
        {
            min_timeout_in_seconds = timeout_in_seconds;
        }
        else
        {
            min_timeout_in_seconds = this.maxTimeoutInSeconds;
        }

        final long timeout_in_milliseconds =
            min_timeout_in_seconds.multiply ( Seconds.TO_MILLISECONDS )
                                  .longValue (); // Deliberately truncate.

        BlockingMustComplete.ElapsedTime<VALUE> elapsed_time_before_failure =
            null;
        synchronized ( this.lock )
        {
            if ( this.finalTerm != null )
            {
                final_value = this.finalTerm;
                elapsed_time_before_failure = null;
            }
            else if ( timeout_in_seconds.compareTo ( BigDecimal.ZERO ) == 0 )
            {
                // Don't wait.  Generate a Timeout return value.
                final_value = null;

                elapsed_time_before_failure =
                    new BlockingMustComplete.ElapsedTime<VALUE> (
                        this, // blocking_request
                        BigDecimal.ZERO ); // elapsed_time_in_seconds
            }
            else
            {
                final BigDecimal start_time_in_seconds =
                    this.clock.currentTimeInSeconds ();
                final Thread thread = Thread.currentThread ();
                this.awaitingThreads.add ( thread );
                try
                {
                    this.lock.wait ( timeout_in_milliseconds );

                    final BigDecimal cancelled_time_in_seconds =
                        this.clock.currentTimeInSeconds ();
                    final BigDecimal elapsed_time_in_seconds =
                        cancelled_time_in_seconds.subtract ( start_time_in_seconds );
                    elapsed_time_before_failure =
                        new BlockingMustComplete.ElapsedTime<VALUE> (
                            this, // blocking_request
                            elapsed_time_in_seconds );
                }
                catch ( InterruptedException e )
                {
                    // Aborted waiting on this Thread only (other
                    // callers might still eventually get the final value).
                    final BigDecimal cancelled_time_in_seconds =
                        this.clock.currentTimeInSeconds ();
                    final BigDecimal elapsed_time_in_seconds =
                        cancelled_time_in_seconds.subtract ( start_time_in_seconds );
                    elapsed_time_before_failure =
                        new BlockingMustComplete.CancelledTime<VALUE> (
                            this,
                            elapsed_time_in_seconds );
                }

                this.awaitingThreads.remove ( thread );
                final_value = this.finalTerm;
            }
        }

        if ( final_value != null )
        {
            return final_value;
        }

        final BlockingMustComplete blocking_must_complete =
            new BlockingMustComplete ( timeout_in_seconds );
        final TermViolation violation =
            new TermViolation (
                blocking_must_complete.violation (
                    this,
                    elapsed_time_before_failure ) );

        if ( elapsed_time_before_failure instanceof BlockingMustComplete.CancelledTime )
        {
            // Cancelled by calling Thread.interrupt () on this await ()ing
            // thread.  Only the caller of <code> await () </code> receives
            // this Cancelled result; other callers might still receive the
            // eventual final value.
            return new Cancelled<VALUE> (
                this.termType, // type
                blocking_term, // cause
                violation,
                this.partialTerm );
        }
        else
        {
            // Timeout.
            // Only the caller of <code> await () </code> receives
            // this Timeout result; other callers might still receive the
            // eventual final value.
            return new Timeout<VALUE> (
                this.termType, // type
                blocking_term, // cause
                violation,
                this.partialTerm );
        }
    }


    /**
     * @return All Threads which are currently await ( ... )ing
     *         the final value from this Result.  Never null.
     *         Never contains any null elements.
     */
    protected final LinkedHashSet<Thread> awaitingThreads ()
    {
        final LinkedHashSet<Thread> awaiting_threads;
        synchronized ( this.lock )
        {
            awaiting_threads =
                new LinkedHashSet<Thread> ( this.awaitingThreads );
        }

        return awaiting_threads;
    }


    /**
     * @see musaico.foundation.term.blocking.Result#clock()
     */
    @Override
    public final Clock clock ()
        throws ReturnNeverNull.Violation
    {
        return this.clock;
    }


    /**
     * @see musaico.foundation.term.blocking.Result#elapsedTimeInSeconds()
     */
    @Override
    public final BigDecimal elapsedTimeInSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        final BigDecimal start_time;
        synchronized ( this.lock )
        {
            if ( this.elapsedTimeInSeconds != null )
            {
                return this.elapsedTimeInSeconds;
            }

            if ( this.startTimeInSeconds == null )
            {
                // Processing has not yet started.
                return BigDecimal.ZERO;
            }

            start_time = this.startTimeInSeconds;
        }

        // Processing has started but is still underway.
        final BigDecimal current_time =
            this.clock.currentTimeInSeconds ();
        final BigDecimal elapsed_time_so_far =
            current_time.subtract ( start_time );

        return elapsed_time_so_far;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * Can be overridden.
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final LocalResult<?> that =
            (LocalResult<?>) object;
         if ( this.termType == null )
        {
            if ( that.termType != null )
            {
                return false;
            }
        }
        else if ( that.termType == null )
        {
            return false;
        }
        else if ( ! this.termType.equals ( that.termType ) )
        {
            return false;
        }

        return true;
    }


    /**
     * <p>
     * Responds to a request to change the operating state, such as from
     * STARTED to STOPPED, or PAUSED to STARTED, and so on.
     * </p>
     *
     * @param old_state The old operating state.  Must not be null.
     *
     * @param new_state The requested new operating state.  Must not be null.
     *
     * @return The actual new OperatingState, which might be the requested
     *         new_state, or it might be the old_state still, or it might
     *         be a different state altogether, possibly including
     *         an ErrorState.  Never null.
     */
    protected OperatingState handleStateChange (
            OperatingState old_state,
            OperatingState new_state
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Does absolutely nothing by default.
        // Can be overridden.
        return new_state;
    }
    

    /**
     * @see musaico.foundation.term.blocking.Result#hasFinalTerm()
     */
    @Override
    public final boolean hasFinalTerm ()
    {
        if ( this.finalTerm != null )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.term.blocking.Result#hasPartialTerm()
     */
    @Override
    public final boolean hasPartialTerm ()
    {
        if ( this.finalTerm != null
             || this.partialTerm != null )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.term.blocking.Result#maxTimeoutInSeconds()
     */
    @Override
    public final BigDecimal maxTimeoutInSeconds ()
        throws ReturnNeverNull.Violation
    {
        return this.maxTimeoutInSeconds;
    }


    /**
     * @see musaico.foundation.term.blocking.Result#operatingState()
     */
    @Override
    public final OperatingState operatingState ()
        throws ReturnNeverNull.Violation
    {
        return this.operatingState;
    }


    /**
     * @see musaico.foundation.term.blocking.Result#partialTerm()
     */
    @Override
    public final NonBlocking<VALUE> partialTerm ()
        throws ReturnNeverNull.Violation
    {
        synchronized ( this.lock )
        {
            if ( this.finalTerm != null )
            {
                return this.finalTerm;
            }
            else if ( this.partialTerm != null )
            {
                return this.partialTerm;
            }
        }

        // No partial value set.  Return a default No value.
        return new No<VALUE> ( this.termType );
    }


    /**
     * @see musaico.foundation.term.blocking.Result#pause()
     */
    @Override
    public final OperatingState pause ()
        throws ReturnNeverNull.Violation
    {
        final OperatingState old_state;
        synchronized ( this.lock )
        {
            old_state = this.operatingState;
            this.operatingState = OperatingState.PAUSING;
        }

        final OperatingState new_state =
            this.handleStateChange ( old_state,
                                     OperatingState.PAUSED );

        synchronized ( this.lock )
        {
            this.operatingState = new_state;
        }

        return new_state;
    }


    /**
     * @see musaico.foundation.term.blocking.Result#resume()
     */
    @Override
    public final OperatingState resume ()
        throws ReturnNeverNull.Violation
    {
        final OperatingState old_state;
        synchronized ( this.lock )
        {
            old_state = this.operatingState;
            this.operatingState = OperatingState.RESUMING;
        }

        final OperatingState new_state =
            this.handleStateChange ( old_state,
                                     OperatingState.STARTED );

        synchronized ( this.lock )
        {
            this.operatingState = new_state;
        }

        return new_state;
    }


    protected final NonBlocking<VALUE> setFinalTerm (
            NonBlocking<VALUE> final_value
            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               final_value );

        synchronized ( this.lock )
        {
            if ( this.finalTerm != null )
            {
                // The result was set previously.  Don't do anything now.
                return this.finalTerm;
            }

            this.finalTerm = final_value;
            this.partialTerm = null;
        }

        // Now stop.
        this.stop ();

        return final_value;
    }


    @SuppressWarnings("unchecked") // Cast ProgOp<?,?>-ProgOp<Object,Object>.
    protected final NonBlocking<VALUE> setPartialTerm (
            NonBlocking<VALUE> partial_value
            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               partial_value );

        final LinkedHashSet<Expression<VALUE, ?>> expressions;
        synchronized ( this.lock )
        {
            if ( this.finalTerm != null )
            {
                // The result was set previously.  Don't do anything now.
                return this.finalTerm;
            }

            this.partialTerm = partial_value;

            // We will progress () all asynchronous Expressions below.
            expressions =
                new LinkedHashSet<Expression<VALUE, ?>> ( this.expressions );
            this.expressions.clear ();
        }

        // Now give each Expression some progress:
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
                                           partial_value );
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

        return partial_value;
    }


    /**
     * @see musaico.foundation.term.blocking.Result#start()
     */
    @Override
    public final OperatingState start ()
        throws ReturnNeverNull.Violation
    {
        final OperatingState old_state;
        synchronized ( this.lock )
        {
            old_state = this.operatingState;
            this.operatingState = OperatingState.STARTING;

            if ( this.startTime == null )
            {
                this.startTime = this.clock.currentTimeInSeconds ();
            }
        }

        final OperatingState new_state =
            this.handleStateChange ( old_state,
                                     OperatingState.STARTED );

        synchronized ( this.lock )
        {
            this.operatingState = new_state;
        }

        return new_state;
    }


    /**
     * @see musaico.foundation.term.blocking.Result#stop()
     */
    @Override
    public final OperatingState stop ()
        throws ReturnNeverNull.Violation
    {
        final OperatingState old_state;
        synchronized ( this.lock )
        {
            old_state = this.operatingState;
            this.operatingState = OperatingState.STOPPING;
        }

        final OperatingState new_state =
            this.handleStateChange ( old_state,
                                     OperatingState.STOPPED );

        final BigDecimal stop_time = this.clock.currentTimeInSeconds ();

        final LinkedHashSet<Expression<VALUE, ?>> expressions;
        synchronized ( this.lock )
        {
            this.operatingState = new_state;

            if ( this.finalTerm == null )
            {
                // This Result was cancelled by stopping it.
                // Set the elapsed time.
                if ( this.elapsedTimeInSeconds == null )
                {
                    if ( this.startTimeInSeconds == null )
                    {
                        this.elapsedTimeInSeconds = BigDecimal.ZERO;
                    }
                    else
                    {
                        this.elapsedTimeInSeconds =
                            stop_time.subtract ( this.startTimeInSeconds );
                    }
                }

                final BlockingMustComplete.ElapsedTime<VALUE> elapsed_time_before_failure =
                    new BlockingMustComplete.CancelledTime<VALUE> (
                        this, // blocking_request
                        this.elapsedTimeInSeconds );

                final BlockingMustComplete blocking_must_complete =
                    new BlockingMustComplete ( this.maxTimeoutInSeconds () );

                final TermViolation violation =
                    new TermViolation (
                        blocking_must_complete.violation (
                            this,
                            elapsed_time_before_failure ) );

                this.finalTerm =
                    new Cancelled<VALUE> (
                        this.termType, // type
                        null, // No single cause for this Result being stopped.
                        violation,
                        this.partialTerm );
                this.partialTerm = null;
            }

            // Wake up any synchronously blocking await () calls.
            this.lock.notifyAll ();

            // We will finish all asynchronous Expressions below.
            expressions =
                new LinkedHashSet<Expression<VALUE, ?>> ( this.expressions );
            this.expressions.clear ();
        }

        for ( Expression<VALUE, ?> expression : expressions )
        {
            // Force the operation to be applied to this Result's
            // final value, then set the final value of the expression
            // to the output of the operation.
            expression.await ( BigDecimal.ZERO );
        }

        return new_state;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();

        final NonBlocking<VALUE> final_value;
        final NonBlocking<VALUE> partial_value;
        final OperatingState operating_state;
        synchronized ( this.lock )
        {
            final_value = this.finalTerm;
            partial_value = this.partialTerm;
            operating_state = this.operatingState;
        }

        sbuf.append ( ClassName.of ( this.getClass () ) );
        if ( final_value == null )
        {
            sbuf.append ( " (" + operating_state + ")" );
        }
        sbuf.append ( ":" );

        sbuf.append ( "\n{" );

        sbuf.append ( "\n    type = "
                      + this.termType );

        if ( final_value != null )
        {
            sbuf.append ( "\n    finalTerm = " + final_value );
        }
        else if ( partial_value != null )
        {
            sbuf.append ( "\n    maxTimeoutInSeconds = "
                          + this.maxTimeoutInSeconds );
            sbuf.append ( "\n    partialTerm = " + partial_value );
        }
        sbuf.append ( "\n}" );

        return sbuf.toString ();
    }


    /**
     * @see musaico.foundation.term.blocking.Result#type()
     */
    @Override
    public final Type<VALUE> type ()
    {
        return this.termType;
    }
}
