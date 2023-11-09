package musaico.foundation.value.blocking;

import java.io.Serializable;

import java.rmi.RemoteException;

import java.lang.reflect.Constructor;

import java.util.Iterator;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.CheckedValueViolation;
import musaico.foundation.value.Countable;
import musaico.foundation.value.Idempotent;
import musaico.foundation.value.NonBlocking;
import musaico.foundation.value.ReadOnceThenIdempotent;
import musaico.foundation.value.Operation;
import musaico.foundation.value.Select;
import musaico.foundation.value.Type;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.abnormal.Error;

import musaico.foundation.value.contracts.ValueMustBeCountable;
import musaico.foundation.value.contracts.ValueMustBeNonBlocking;

import musaico.foundation.value.finite.No;

import musaico.foundation.value.incomplete.CompletionPossible;
import musaico.foundation.value.incomplete.Expression;

import musaico.foundation.value.iterators.ValueIterator;


/**
 * <p>
 * A conditional Value which might not yet have been retrieved, and
 * can be used to either block the caller until the result comes in,
 * or process the value asynchronously.
 * </p>
 *
 * <p>
 * For example, the result of an I/O operation, such as receiving
 * the response from a networking request, or reading content from
 * a file, or querying a database, and so on.
 * </p>
 *
 * <p>
 * A Blocking result is an Incomplete result.  Callers not expecting
 * a Blocking result will fail on this value.
 * </p>
 *
 * <p>
 * But callers who are willing to
 * wait for the result can call <code> await () </code>,
 * specify how long they are willing to wait, and then block until
 * the result comes in.
 * </p>
 *
 * <p>
 * Alternatively, a caller who is willing to wait can create a
 * callback (Operation) and pipe the result to it asynchronously,
 * whenever it comes in.
 * </p>
 *
 * <p>
 * This flexibility allows the
 * caller to decide how to manage concurrency, and can be used
 * to decouple information services from concurrency concerns,
 * while ensuring that callers must explicitly decide to wait
 * for a blocking result.
 * </p>
 *
 * <p>
 * Those who do not wish to fail on calls to <code> orNone () </code>
 * and so on should use the Asynchronous class instead.  Asynchronous
 * is NOT a Value, but can be used to force the caller to choose
 * between blocking and handling the result asynchronously.
 * </p>
 *
 *
 * <p>
 * In Java every conditional Value must be Serializable in order to
 * play nicely across RMI.  However users of the conditional Values
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
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
public class Blocking<VALUE extends Object>
    implements CompletionPossible<VALUE>, ReadOnceThenIdempotent<VALUE>, Thread.UncaughtExceptionHandler, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Blocking.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The type of the conditional value.
    private final Type<VALUE> type;

    // The cause of this value, such as an Unidempotent value of which
    // this is an Idempotent snapshot; or a Warning or Partial value;
    // and so on.  This value can be its own cause.
    private final Value<?> cause;

    // The violation that causes trouble for callers whenever
    // this.orXYZ () is called.
    private final ValueViolation valueViolation;

    // The worker which is executing to generate the AsynchronousResult,
    // such as a thread, or I/O operation, or some other Cancellable object.
    // If this Blocking Value is <code> cancel () </code>ed, then the worker
    // will be, too.
    private final Cancellable worker;

    // The result we block on or pipe asynchronously.
    private final AsynchronousResult<VALUE> asynchronousResult;

    // If this Blocking Value's worker is a CancellableThread,
    // and if the Thread throws an uncaught Exception while
    // it is working, then this Blocking Value fails.  However if the
    // Thread already had a Thread.UncaughtExceptionHandler, it will be
    // invoked once this Blocking Value is done handling the exception.
    private final Thread.UncaughtExceptionHandler threadExceptionHandler;

    // When this AsynchronousResult was created.  This is
    // considered to be the time at which this AsynchronousResult's
    // processing began, even though in practice the creation
    // time of the AsynchronousResult and the actual time at which
    // processing began to determine the final result could be
    // drastically different.
    private long creationTimeInMilliseconds;


    /**
     * <p>
     * Creates a new Blocking result, allowing the caller to
     * decide whether to wait for the result synchronously
     * with <code> await () <code>, or process the result
     * asynchronously with <code> pipe () <code>.
     * </p>
     *
     * <p>
     * This constructor creates a CancellableThread wrapper for the
     * specified Thread.
     * </p>
     *
     * @param worker_thread The Thread which works toward
     *                      the AsynchronousResult.
     *                      If this Blocking Value is
     *                      <code> cancel () </code>ed, then the worker
     *                      thread will be <code> interrupt () </code>ed.
     *                      Must not be null.
     *
     * @param asynchronous_result The result which will either eventually
     *                            receive a final conditional Value,
     *                            or timeout while blocking.
     *                            Be careful: if the asynchronous result
     *                            is serialized over RMI, then this
     *                            Blocking result will ALWAYS timeout
     *                            if the caller
     *                            calls <code> await () </code>, or
     *                            no results will ever be passed to
     *                            the Operation passed to the
     *                            <code> pipe () <code> method.
     *                            This is because a serialized asynchronous
     *                            result is not the same remote object
     *                            which will receive the update.  In an
     *                            RMI environment, it is best to set up
     *                            a UnicastRemoteObject version of
     *                            AsynchronousResult, in order to safely
     *                            pass the object around via RMI.
     *                            Must not be null.
     *
     * @throws RemoteException If the CancellableThread constructor fails
     *                         for some abstruse reason.
     */
    public Blocking (
                     Thread worker_thread,
                     AsynchronousResult<VALUE> asynchronous_result
                     )
        throws RemoteException,
               ParametersMustNotBeNull.Violation
    {
        this (
              // Throws RemoteException:
              new CancellableThread ( worker_thread ), // worker
              null, // cause
              asynchronous_result );
    }


    /**
     * <p>
     * Creates a new Blocking result, allowing the caller to
     * decide whether to wait for the result synchronously
     * with <code> await () <code>, or process the result
     * asynchronously with <code> pipe () <code>.
     * </p>
     *
     * <p>
     * This constructor reuses the same ValueViolation as the
     * specified asynchronous result.
     * </p>
     *
     * @param worker The Cancellable worker which is executing to generate
     *               the AsynchronousResult.  If this Blocking Value is
     *               <code> cancel () </code>ed, then the worker
     *               will be, too.  Must not be null.
     *
     * @param asynchronous_result The result which will either eventually
     *                            receive a final conditional Value,
     *                            or timeout while blocking.
     *                            Be careful: if the asynchronous result
     *                            is serialized over RMI, then this
     *                            Blocking result will ALWAYS timeout
     *                            if the caller
     *                            calls <code> await () </code>, or
     *                            no results will ever be passed to
     *                            the Operation passed to the
     *                            <code> pipe () <code> method.
     *                            This is because a serialized asynchronous
     *                            result is not the same remote object
     *                            which will receive the update.  In an
     *                            RMI environment, it is best to set up
     *                            a UnicastRemoteObject version of
     *                            AsynchronousResult, in order to safely
     *                            pass the object around via RMI.
     *                            Must not be null.
     */
    public Blocking (
                     Cancellable worker,
                     AsynchronousResult<VALUE> asynchronous_result
                     )
        throws ParametersMustNotBeNull.Violation
    {
        this ( worker,
               null, // cause
               asynchronous_result );
    }


    /**
     * <p>
     * Creates a new Blocking result, allowing the caller to
     * decide whether to wait for the result synchronously
     * with <code> await () <code>, or process the result
     * asynchronously with <code> pipe () <code>.
     * </p>
     *
     * <p>
     * This constructor reuses the same ValueViolation as the
     * specified asynchronous result.
     * </p>
     *
     * @param worker The Cancellable worker which is executing to generate
     *               the AsynchronousResult.  If this Blocking Value is
     *               <code> cancel () </code>ed, then the worker
     *               will be, too.  Must not be null.
     *
     * @param cause The optional cause of this value, such as a
     *              non-Idempotent value of which this is a snapshot,
     *              or a Warning, or a Partial value, and so on.
     *              If null, then this value is its own cause.
     *              Can be null.
     *
     * @param asynchronous_result The result which will either eventually
     *                            receive a final conditional Value,
     *                            or timeout while blocking.
     *                            Be careful: if the asynchronous result
     *                            is serialized over RMI, then this
     *                            Blocking result will ALWAYS timeout
     *                            if the caller
     *                            calls <code> await () </code>, or
     *                            no results will ever be passed to
     *                            the Operation passed to the
     *                            <code> pipe () <code> method.
     *                            This is because a serialized asynchronous
     *                            result is not the same remote object
     *                            which will receive the update.  In an
     *                            RMI environment, it is best to set up
     *                            a UnicastRemoteObject version of
     *                            AsynchronousResult, in order to safely
     *                            pass the object around via RMI.
     *                            Must not be null.
     */
    public Blocking (
                     Cancellable worker,
                     Value<?> cause,
                     AsynchronousResult<VALUE> asynchronous_result
                     )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               worker, asynchronous_result );

        this.type = asynchronous_result.type ();
        this.valueViolation =
            ValueMustBeNonBlocking.CONTRACT.violation ( this,
                                                        this );
        if ( cause == null )
        {
            this.cause = this;
        }
        else
        {
            this.cause = cause;
        }
        this.worker = worker;
        this.asynchronousResult = asynchronous_result;

        if ( this.worker instanceof CancellableThread )
        {
            final CancellableThread worker_thread =
                (CancellableThread) this.worker;
            final Thread thread = worker_thread.thread ();

            this.threadExceptionHandler =
                thread.getUncaughtExceptionHandler (); // Could be null.

            thread.setUncaughtExceptionHandler ( this );
        }
        else
        {
            this.threadExceptionHandler = null;
        }

        this.creationTimeInMilliseconds = System.currentTimeMillis ();

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new Blocking result, allowing the caller to
     * decide whether to wait for the result synchronously
     * with <code> await () <code>, or process the result
     * asynchronously with <code> pipe () <code>.
     * </p>
     *
     * <p>
     * This constructor reuses a different ValueViolation from the
     * specified asynchronous result.
     * </p>
     *
     * @param worker The Cancellable worker which is executing to generate
     *               the AsynchronousResult.  If this Blocking Value is
     *               <code> cancel () </code>ed, then the worker
     *               will be, too.  Must not be null.
     *
     * @param asynchronous_result The result which will either eventually
     *                            receive a final conditional Value,
     *                            or timeout while blocking.
     *                            Be careful: if the asynchronous result
     *                            is serialized over RMI, then this
     *                            Blocking result will ALWAYS timeout
     *                            if the caller
     *                            calls <code> await () </code>, or
     *                            no results will ever be passed to
     *                            the Operation passed to the
     *                            <code> pipe () <code> method.
     *                            This is because a serialized asynchronous
     *                            result is not the same remote object
     *                            which will receive the update.  In an
     *                            RMI environment, it is best to set up
     *                            a UnicastRemoteObject version of
     *                            AsynchronousResult, in order to safely
     *                            pass the object around via RMI.
     *                            Must not be null.
     *
     * @param value_violation The exception which caused the blocking
     *                        conditional Value.  Must not be null.
     */
    public Blocking (
                     Cancellable worker,
                     AsynchronousResult<VALUE> asynchronous_result,
                     ValueViolation value_violation
                     )
        throws ParametersMustNotBeNull.Violation
    {
        this ( worker,
               null, // cause
               asynchronous_result,
               value_violation );
    }


    /**
     * <p>
     * Creates a new Blocking result, allowing the caller to
     * decide whether to wait for the result synchronously
     * with <code> await () <code>, or process the result
     * asynchronously with <code> pipe () <code>.
     * </p>
     *
     * <p>
     * This constructor reuses a different ValueViolation from the
     * specified asynchronous result.
     * </p>
     *
     * @param worker The Cancellable worker which is executing to generate
     *               the AsynchronousResult.  If this Blocking Value is
     *               <code> cancel () </code>ed, then the worker
     *               will be, too.  Must not be null.
     *
     * @param cause The optional cause of this value, such as a
     *              non-Idempotent value of which this is a snapshot,
     *              or a Warning, or a Partial value, and so on.
     *              If null, then this value is its own cause.
     *              Can be null.
     *
     * @param asynchronous_result The result which will either eventually
     *                            receive a final conditional Value,
     *                            or timeout while blocking.
     *                            Be careful: if the asynchronous result
     *                            is serialized over RMI, then this
     *                            Blocking result will ALWAYS timeout
     *                            if the caller
     *                            calls <code> await () </code>, or
     *                            no results will ever be passed to
     *                            the Operation passed to the
     *                            <code> pipe () <code> method.
     *                            This is because a serialized asynchronous
     *                            result is not the same remote object
     *                            which will receive the update.  In an
     *                            RMI environment, it is best to set up
     *                            a UnicastRemoteObject version of
     *                            AsynchronousResult, in order to safely
     *                            pass the object around via RMI.
     *                            Must not be null.
     *
     * @param value_violation The exception which caused the blocking
     *                        conditional Value.  Must not be null.
     */
    public Blocking (
                     Cancellable worker,
                     Value<?> cause,
                     AsynchronousResult<VALUE> asynchronous_result,
                     ValueViolation value_violation
                     )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               worker, asynchronous_result, value_violation );

        this.type = asynchronous_result.type ();
        if ( cause == null )
        {
            this.cause = this;
        }
        else
        {
            this.cause = cause;
        }
        this.valueViolation = value_violation;
        this.worker = worker;
        this.asynchronousResult = asynchronous_result;

        if ( this.worker instanceof CancellableThread )
        {
            final CancellableThread worker_thread =
                (CancellableThread) this.worker;
            final Thread thread = worker_thread.thread ();

            this.threadExceptionHandler =
                thread.getUncaughtExceptionHandler (); // Could be null.

            thread.setUncaughtExceptionHandler ( this );
        }
        else
        {
            this.threadExceptionHandler = null;
        }

        this.creationTimeInMilliseconds = System.currentTimeMillis ();

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new Blocking value from the specified Blocking value(s).
     * Once all Blocking value(s) have reported in (each one either
     * generating a result or timing out), the final result will be
     * ready for this new Blocking value.
     * </p>
     *
     * <p>
     * The final result of the new Blocking Value will be
     * calcuclated by calling <code> logic.apply ( result ) </code>
     * on each asynchronous result as it is received (either Timeout
     * or final NonBlocking result).
     * For example, if four Blocking values are passed in, and their
     * results are received in the order 3, 1, 4, 2, then the
     * final result will be:
     * </p>
     *
     * <pre>
     *     logic.apply ( result#3 );
     *     logic.apply ( result#1 );
     *     logic.apply ( result#4 );
     *     logic.apply ( result#2 );
     * </pre>
     *
     * @param type The Type that is expected for
     *             the final result of this Blocking value.
     *             Must not be null.
     *
     * @param logic The logic to perform on each result as it comes in,
     *              typically <code> AndValues </code>
     *              or <code> OrValues </code>.  Must not be null.
     *              Must always output a NonBlocking value.
     *
     * @param blocking_values 0 or more Blocking values to wait upon
     *                        before this new Blocking value's result
     *                        is finalized.  Must not be null.
     *                        Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Generic varargs heap pollution.
    public Blocking (
                     Type<VALUE> type,
                     Operation<VALUE, VALUE> logic,
                     Blocking<VALUE> ... blocking_values
                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        this ( type,
               null, // cause
               logic,
               blocking_values );
    }


    /**
     * <p>
     * Creates a new Blocking value from the specified Blocking value(s).
     * Once all Blocking value(s) have reported in (each one either
     * generating a result or timing out), the final result will be
     * ready for this new Blocking value.
     * </p>
     *
     * <p>
     * The final result of the new Blocking Value will be
     * calcuclated by calling <code> logic.apply ( result ) </code>
     * on each asynchronous result as it is received (either Timeout
     * or final NonBlocking result).
     * For example, if four Blocking values are passed in, and their
     * results are received in the order 3, 1, 4, 2, then the
     * final result will be:
     * </p>
     *
     * <pre>
     *     logic.apply ( result#3 );
     *     logic.apply ( result#1 );
     *     logic.apply ( result#4 );
     *     logic.apply ( result#2 );
     * </pre>
     *
     * @param type The type that is expected for
     *             the final result of this Blocking value.
     *             Must not be null.
     *
     * @param cause The optional cause of this value, such as a
     *              non-Idempotent value of which this is a snapshot,
     *              or a Warning, or a Partial value, and so on.
     *              If null, then this value is its own cause.
     *              Can be null.
     *
     * @param logic The logic to perform on each result as it comes in,
     *              typically <code> AndValues </code>
     *              or <code> OrValues </code>.  Must not be null.
     *              Must always output a NonBlocking value.
     *
     * @param blocking_values 0 or more Blocking values to wait upon
     *                        before this new Blocking value's result
     *                        is finalized.  Must not be null.
     *                        Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Generic varargs heap pollution.
    public Blocking (
                     Type<VALUE> type,
                     Value<?> cause,
                     Operation<VALUE, VALUE> logic,
                     Blocking<VALUE> ... blocking_values
                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               logic, blocking_values );
        classContracts.check ( Parameter3.MustContainNoNulls.CONTRACT,
                               blocking_values );

        // First figure out the max timeout of the child Blocking values.
        long max_timeout_in_nanoseconds = 0L;
        ValueViolation violation = null;
        for ( Blocking<VALUE> child_blocking : blocking_values )
        {
            final AsynchronousResult<VALUE> child_async =
                child_blocking.asynchronousResult ();
            final long timeout_in_nanoseconds =
                child_async.maxTimeoutInNanoseconds ();
            if ( timeout_in_nanoseconds > max_timeout_in_nanoseconds )
            {
                max_timeout_in_nanoseconds = timeout_in_nanoseconds;
            }

            if ( violation == null )
            {
                violation = child_blocking.valueViolation ();
            }
        }

        if ( violation == null )
        {
            // Create a default timed out value violation.
            violation =
                ValueMustBeNonBlocking.CONTRACT.violation ( this,
                                                            this );
        }

        this.worker = new FutureBlockingValues ( blocking_values );
        this.asynchronousResult =
            new MultipleBlockingValuesCollector<VALUE> ( type,
                                                         max_timeout_in_nanoseconds,
                                                         logic,
                                                         blocking_values );

        this.threadExceptionHandler = null;

        this.type = type;
        if ( cause == null )
        {
            this.cause = this;
        }
        else
        {
            this.cause = cause;
        }
        this.valueViolation = violation;

        this.creationTimeInMilliseconds = System.currentTimeMillis ();

        this.contracts = new Advocate ( this );
    }


    /**
     * @return The AsynchronousResult from this Blocking value.
     *         Protected.  Usable only by other Values in this package
     *         as well as derived Blocking Values.  The AsynchronousResult
     *         is not intended to be given out publicly.  Never null.
     */
    protected AsynchronousResult<VALUE> asynchronousResult ()
    {
        return this.asynchronousResult;
    }


    /**
     * @see musaico.foundation.value.Value#await(long)
     *
     * <p>
     * Blocks until the result comes in, or until the specified
     * timeout period has elapsed, whichever comes first.
     * </p>
     */
    @Override
    public final NonBlocking<VALUE> await (
                                           long timeout_in_nanoseconds
                                           )
    {
        return this.asynchronousResult.await ( this,
                                               timeout_in_nanoseconds );
    }


    /**
     * @see musaico.foundation.value.Value#blockingMaxNanoseconds()
     */
    @Override
    public final long blockingMaxNanoseconds ()
    {
        return this.asynchronousResult.maxTimeoutInNanoseconds ();
    }


    /**
     * @see musaico.foundation.value.Value#cancel(musaico.foundation.value.ValueViolation)
     */
    @Override
    public final NonBlocking<VALUE> cancel (
                                            ValueViolation violation
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        if ( this.worker.cancel ().hasValue () )
        {
            final long end_time_in_milliseconds = System.currentTimeMillis ();
            final long duration_in_nanoseconds =
                ( end_time_in_milliseconds
                  - this.creationTimeInMilliseconds )
                * 1000000000L;
            final BlockingMustComplete.CancelledTime<VALUE> cancelled_time =
                new BlockingMustComplete.CancelledTime<VALUE> (
                    this,
                    duration_in_nanoseconds );

            final ValueViolation value_violation =
                BlockingMustComplete.violation (
                    this.asynchronousResult.maxTimeoutInNanoseconds (),
                    cancelled_time );

            final Cancelled<VALUE> cancelled =
                new Cancelled<VALUE> ( this.type,
                                       value_violation,
                                       this.asynchronousResult.partialResult (
                                           this.valueViolation ) );

            this.asynchronousResult.setFinalResult ( cancelled );
        }

        final NonBlocking<VALUE> result =
            this.asynchronousResult.block ( this, 0L );

        return result;
    }


    /**
     * @see musaico.foundation.value.Value#cause()
     */
    @Override
    public final Value<?> cause ()
        throws ReturnNeverNull.Violation
    {
        return this.cause;
    }


    /**
     * @see musaico.foundation.value.Value#causeRoot()
     */
    @Override
    public final Value<?> causeRoot ()
        throws ReturnNeverNull.Violation
    {
        if ( this.cause == this )
        {
            return this;
        }
        else
        {
            return this.cause.causeRoot ();
        }
    }


    /**
     * @see musaico.foundation.value.incomplete.CompletionPossible#completed()
     */
    @Override
    public final Value<VALUE> completed ()
        throws ReturnNeverNull.Violation
    {
        if ( this.isComplete () )
        {
            return this.asynchronousResult.partialResult (
                       this.valueViolation );
        }
        else
        {
            return this;
        }
    }


    /**
     * @see musaico.foundation.value.Value#consequence()
     */
    @Override
    public final NonBlocking<?> consequence ()
        throws ReturnNeverNull.Violation
    {
        return this.asynchronousResult.partialResult ( this.valueViolation );
    }


    /**
     * @see musaico.foundation.value.Value#consequenceLeaf()
     */
    @Override
    public final Value<?> consequenceLeaf ()
        throws ReturnNeverNull.Violation
    {
        return this.consequence ().consequenceLeaf ();
    }


    /**
     * @see musaico.foundation.value.Value#countable()
     */
    @Override
    public final Countable<VALUE> countable ()
        throws ReturnNeverNull.Violation
    {
        // We cannot block here.
        if ( this.asynchronousResult.isFinishedBlocking () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.asynchronousResult.block ( this, 0L );
            return result.countable ();
        }
        else
        {
            // Not done blocking yet.  Return No countable values.
            return new No<VALUE> ( this.type,
                                   this,
                                   ValueMustBeCountable.CONTRACT.violation (
                                          this,
                                          this,
                                          this.valueViolation ) );
        }
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object obj
                                 )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null
             || ! ( obj instanceof Blocking ) )
        {
            return false;
        }

        Blocking<?> that = (Blocking<?>) obj;

        if ( this.asynchronousResult != that.asynchronousResult )
        {
            // Must actually be the same asynchronous result we're
            // waiting for, not one that is equal().
            return false;
        }
        else if ( this.worker != that.worker )
        {
            // Must actually be the same Cancellable worker we're
            // waiting for, not one that is equal().
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see musaico.foundation.value.Value#filter()
     */
    @Override
    public final FilterState filter ()
    {
        // We cannot block here.
        if ( this.asynchronousResult.isFinishedBlocking () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.asynchronousResult.block ( this, 0L );
            return result.filter ();
        }
        else
        {
            // Not done blocking yet.  Return DISCARDED.
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see musaico.foundation.value.Value#hasValue()
     */
    @Override
    public final boolean hasValue ()
    {
        // We cannot block here.
        if ( this.asynchronousResult.isFinishedBlocking () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.asynchronousResult.block ( this, 0L );
            return result.hasValue ();
        }
        else
        {
            // Not done blocking yet.  Return false.
            return false;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        int hash_code = 0;
        hash_code += this.getClass ().hashCode ();
        hash_code += ( this.type == null ? 0 : this.type.hashCode () );

        hash_code *= 17;

        return hash_code;
    }


    /**
     * @see musaico.foundation.value.Value#head()
     */
    @Override
    public final ZeroOrOne<VALUE> head ()
        throws ReturnNeverNull.Violation
    {
        // We cannot block here.
        if ( this.asynchronousResult.isFinishedBlocking () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.asynchronousResult.block ( this, 0L );
            return result.head ();
        }
        else if ( this.asynchronousResult.hasPartialResult () )
        {
            // Didn't finish blocking, but there is at least something
            // waiting for us as a partial result.  Delegate the call.
            final NonBlocking<VALUE> partial_result =
                this.asynchronousResult.partialResult ( this.valueViolation );
            return partial_result.head ();
        }
        else
        {
            // Not done blocking yet.  Return No head.
            return new No<VALUE> ( this.type,
                                   this.valueViolation );
        }
    }


    /**
     * @see musaico.foundation.value.Value#head(long)
     */
    @Override
    public final Countable<VALUE> head (
                                        long num_elements
                                        )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               num_elements );

        // We cannot block here.
        if ( this.asynchronousResult.isFinishedBlocking () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.asynchronousResult.block ( this, 0L );
            return result.head ( num_elements );
        }
        else if ( this.asynchronousResult.hasPartialResult () )
        {
            // Didn't finish blocking, but there is at least something
            // waiting for us as a partial result.  Delegate the call.
            final NonBlocking<VALUE> partial_result =
                this.asynchronousResult.partialResult ( this.valueViolation );
            return partial_result.head ( num_elements );
        }
        else
        {
            // Not done blocking yet.  Return No head.
            return new No<VALUE> ( this.type,
                                   this.valueViolation );
        }
    }


    /**
     * @see musaico.foundation.value.Value#idempotent()
     */
    @Override
    public final Idempotent<VALUE> idempotent ()
    {
        // We cannot block here.
        final NonBlocking<VALUE> result_so_far;
        if ( this.asynchronousResult.isFinishedBlocking () )
        {
            // Finished blocking.  Delegate the call.
            result_so_far = this.asynchronousResult.block ( this, 0L );
        }
        else if ( this.asynchronousResult.hasPartialResult () )
        {
            // Didn't finish blocking, but there is at least something
            // waiting for us as a partial result.  Delegate the call.
            result_so_far = asynchronousResult.partialResult ( this.valueViolation );
        }
        else
        {
            // Not done blocking yet and no partial result.
            result_so_far = new No<VALUE> ( this.type,
                                            this.valueViolation );
        }

        final Idempotent<VALUE> idempotent = result_so_far.idempotent ();

        return idempotent;
    }


    /**
     * @see musaico.foundation.value.Value#indefiniteIterator(long)
     */
    @Override
    public final Iterator<VALUE> indefiniteIterator (
                                                     long maximum_iterations
                                                     )
        throws Parameter1.MustBeGreaterThanZero.Violation
    {
        return this.iterator ();
    }


    /**
     * @see musaico.foundation.value.incomplete.CompletionPossible#isComplete()
     *
     * @return True if this Blocking value has finished blocking
     *         and has a result; false if it is still blocking.
     *         If the result is true, then the caller can safely
     *         invoke <code> await ( 0L ) </code> and get back
     *         the final result (whether the final result be
     *         a Just value, or a Cancelled or Timeout value,
     *         and so on).
     */
    @Override
    public final boolean isComplete ()
    {
        return this.asynchronousResult.isFinishedBlocking ();
    }


    /**
     * @see musaico.foundation.value.Value#iterator()
     */
    @Override
    public final Iterator<VALUE> iterator ()
    {
        // We cannot block here.
        if ( this.asynchronousResult.isFinishedBlocking () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.asynchronousResult.block ( this, 0L );
            return result.iterator ();
        }
        else
        {
            // Not done blocking yet.  Return an empty Iterator.
            return new ValueIterator<VALUE> ();
        }
    }


    /**
     * @see musaico.foundation.value.Value#orDefault(java.lang.Object)
     */
    @Override
    public final VALUE orDefault (
                                  VALUE default_value
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               default_value );

        // We cannot block here.
        if ( this.asynchronousResult.isFinishedBlocking () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.asynchronousResult.block ( this, 0L );
            return result.orDefault ( default_value );
        }
        else
        {
            // Not done blocking yet.  Return the specified default.
            return default_value;
        }
    }


    /**
     * @see musaico.foundation.value.Value#orNone()
     */
    @Override
    public final VALUE orNone ()
    {
        // We cannot block here.
        if ( this.asynchronousResult.isFinishedBlocking () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.asynchronousResult.block ( this, 0L );
            return result.orNone ();
        }
        else
        {
            // Not done blocking yet.  Return none.
            return this.type.none ();
        }
    }


    /**
     * @see musaico.foundation.value.Value#orNull()
     */
    @Override
    public final VALUE orNull ()
    {
        // We cannot block here.
        if ( this.asynchronousResult.isFinishedBlocking () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.asynchronousResult.block ( this, 0L );
            return result.orNull ();
        }
        else
        {
            // Not done blocking yet.  Return null.
            return null;
        }
    }


    /**
     * @see musaico.foundation.value.Value#orPartial()
     */
    @Override
    public final NonBlocking<VALUE> orPartial ()
    {
        // Return the final result, if available; otherwise the current
        // partial result, if any; otherwise No value.
        return this.asynchronousResult.partialResult ( this.valueViolation )
            .orPartial ();
    }


    /**
     * @see musaico.foundation.value.Value#orThrow(java.lang.Class)
     */
    @Override
    public final <EXCEPTION extends Exception>
        VALUE orThrow (
                       Class<EXCEPTION> exception_class
                       )
        throws EXCEPTION,
               ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               exception_class );

        // We cannot block here.
        if ( this.asynchronousResult.isFinishedBlocking () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.asynchronousResult.block ( this, 0L );
            return result.orThrow ( exception_class );
        }

        // Not done blocking yet.  Throw an exception.
        Exception cause_exception = null;
        EXCEPTION exception = null;
        try
        {
            // First try single arg MyException ( String message ).
            final Constructor<EXCEPTION> constructor =
                exception_class.getConstructor ( String.class );
            exception =
                constructor.newInstance ( this.valueViolation ().getMessage () );
        }
        catch ( Exception e )
        {
            cause_exception = e;
            try
            {
                // Now try 0 args constructor.
                final Constructor<EXCEPTION> constructor =
                    exception_class.getConstructor ();
                exception = constructor.newInstance ();
            }
            catch ( Exception e2 )
            {
                exception = null;
            }
        }

        if ( exception == null )
        {
            final ReturnNeverNull.Violation violation =
                ReturnNeverNull.CONTRACT.violation ( this,
                                                     "Could not instantiate exception class " + exception_class.getName () );
            if ( cause_exception != null )
            {
                violation.initCause ( cause_exception );
            }

            throw violation;
        }

        exception.initCause ( this.valueViolation () );

        throw exception;
    }


    /**
     * @see musaico.foundation.value.Value#orThrow(java.lang.Exception)
     */
    @Override
    public final <EXCEPTION extends Exception>
        VALUE orThrow (
                       EXCEPTION exception
                       )
        throws EXCEPTION,
               ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               exception );

        // We cannot block here.
        if ( this.asynchronousResult.isFinishedBlocking () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.asynchronousResult.block ( this, 0L );
            return result.orThrow ( exception );
        }

        // Not done blocking yet.  Throw the exception.
        if ( exception.getCause () == null )
        {
            exception.initCause ( this.valueViolation () );
        }

        throw exception;
    }


    /**
     * @see musaico.foundation.value.Value#orThrowChecked()
     */
    @Override
    public final VALUE orThrowChecked ()
        throws CheckedValueViolation
    {
        // We cannot block here.
        if ( this.asynchronousResult.isFinishedBlocking () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.asynchronousResult.block ( this, 0L );
            return result.orThrowChecked ();
        }
        else
        {
            // Not done blocking yet.  Throw our value violation.
            throw new CheckedValueViolation ( this.valueViolation );
        }
    }


    /**
     * @see musaico.foundation.value.Value#orThrowUnchecked()
     */
    @Override
    public final VALUE orThrowUnchecked ()
        throws ValueViolation
    {
        // We cannot block here.
        if ( this.asynchronousResult.isFinishedBlocking () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.asynchronousResult.block ( this, 0L );
            return result.orThrowUnchecked ();
        }
        else
        {
            // Not done blocking yet.  Throw an unchecked violation.
            throw this.valueViolation;
        }
    }


    /**
     * @see musaico.foundation.value.Value#orViolation(musaico.foundation.contract.Contract,java.lang.Object, java.lang.Object)
     */
    @Override
    public final <EVIDENCE extends Object, VIOLATION extends Throwable & Violation>
        VALUE orViolation (
                           Contract<EVIDENCE, VIOLATION> contract,
                           Object plaintiff,
                           EVIDENCE evidence
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               VIOLATION
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               contract, plaintiff, evidence );

        // We cannot block here.
        if ( this.asynchronousResult.isFinishedBlocking () )
        {
            // Finished blocking.  Delegate the call.
            final NonBlocking<VALUE> result =
                this.asynchronousResult.block ( this, 0L );
            return result.orViolation ( contract, plaintiff, evidence );
        }

        // Not done blocking yet.  Throw a violation of the contract.
        final VIOLATION violation =
            contract.violation ( plaintiff,
                                 evidence );
        violation.initCause ( this.valueViolation );
        throw violation;
    }


    /**
     * @see musaico.foundation.value.Value#pipe(musaico.foundation.value.Operation)
     *
     * <p>
     * Queues up the specified Operation to handle the result
     * asynchronously, whenever it comes in.
     * </p>
     *
     * <p>
     * NOTE: If the result has already been asynchronously set, then
     * the specified Operation will handle the result
     * immediately, in this thread.
     * </p>
     *
     * @return Either a new Expression, awaiting this value's
     *         final result before applying the specified value operation;
     *         or, if this value is already finished blocking,
     *         then the processed final result right away.
     *         Never null.
     */
    @Override
    public final <OUTPUT extends Object>
        Value<OUTPUT> pipe (
                            Operation<VALUE, OUTPUT> operation
                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               operation );

        // Blocking always queues up the operation to
        // evaluate the asynchronous result later.
        final Expression<VALUE, OUTPUT> expression =
            new Expression<VALUE, OUTPUT> ( this, // input
                                            operation.outputType (),
                                            operation ); // operation
        this.asynchronousResult.async ( expression );

        return expression;
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public final String toString ()
    {
        if ( this.contracts == null ) // Constructor time only.
        {
            return ClassName.of ( this.getClass () );
        }

        StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( ClassName.of ( this.getClass () ) );
        sbuf.append ( "<" );
        sbuf.append ( this.type );
        sbuf.append ( ">" );

        return sbuf.toString ();
    }


    /**
     * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
     */
    @Override
    public void uncaughtException (
                                   Thread thread,
                                   Throwable throwable
                                   )
    {
        if ( thread == null
             || throwable == null )
        {
            return;
        }

        final long end_time_in_milliseconds = System.currentTimeMillis ();
        final long duration_in_nanoseconds =
            ( end_time_in_milliseconds
              - this.creationTimeInMilliseconds )
            * 1000000000L;
        final BlockingMustComplete.ErrorTime<VALUE> error_time =
            new BlockingMustComplete.ErrorTime<VALUE> (
                this,
                duration_in_nanoseconds );
        final BlockingMustComplete contract =
            new BlockingMustComplete (
                this.asynchronousResult.maxTimeoutInNanoseconds () );
        final BlockingMustComplete.Violation violation =
            contract.violation ( this,         // plaintiff
                                 error_time ); // evidence
        violation.initCause ( throwable );

        final Error<VALUE> error =
            new Error<VALUE> ( this.type,
                               violation );

        this.asynchronousResult.setFinalResult ( error );

        if ( this.threadExceptionHandler != null )
        {
            this.threadExceptionHandler.uncaughtException (
                thread,
                throwable );
        }
    }


    /**
     * @see musaico.foundation.value.Value#type()
     */
    @Override
    public final Type<VALUE> type ()
    {
        return this.type;
    }


    /**
     * @see musaico.foundation.value.NotOne#valueViolation()
     */
    @Override
    public final ValueViolation valueViolation ()
        throws ReturnNeverNull.Violation
    {
        this.contracts.check ( ReturnNeverNull.CONTRACT,
                               this.valueViolation );

        return this.valueViolation;
    }
}
