package musaico.foundation.value.blocking;

import java.io.Serializable;

import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;

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
public class RMIResult<VALUE extends Object>
    implements Result<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static interface Server
        extends Serializable
    {
        public abstract <OBJECT extends Object>
            OBJECT makeRemotable (
                                  OBJECT not_remotely_accessible
                                  )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;
    }


    public static class StandardServer
        implements RMIResult.Server, Serializable
    {
        private static final long serialVersionUID =
            RMIResult.serialVersionUID;

        // Enforces method obligations and guarantees and so on for us.
        private final Advocate contracts;

        public StandardServer ()
        {
            this,contracts = new Advocate ( this );
        }

        @Override
        public final <OBJECT extends Object>
            OBJECT makeRemotable (
                                  OBJECT not_remotely_accessible
                                  )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   not_remotely_accessible );

            if ( not_remotely_accessible instanceof Result )
            {
                if ( not_remotely_accessible instanceof RMIResult )
                {
                    // Already remotely accessible after all.
                    return not_remotely_accessible;
                }

                final OBJECT remotely_accessible = (OBJECT)
                    new RMIResult<Object> ( (Result<Object>)
                                            not_remotely_accessible );

                return remotely_accessible;
            }
            else if ( not_remotely_accessible instanceof Expression )
            {
                if ( not_remotely_accessible instanceof RMIExpression )
                {
                    // Already remotely accessible after all.
                    return not_remotely_accessible;
                }

                !!!;
                final RMIExpression remotely_accessible =
                    new RMIExpression ( not_remotely_accessible );

                return remotely_accessible;
            }

            !!!;
        }

        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }
    }




    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( RMIResult.class );

    // Lock access to SERVER on this token:
    private static final Serializable classLock =
        new String ( RMIResult.class.getSimpleName () );

    // MUTABLE:
    // The remote RMI server, for systems which share Results across
    // JVMs.  Always null until someone calls RMIResult.initialize ().
    // Access must be synchronized on RMIResult.classLock.
    private static RMIResult.Server SERVER = null;


    // The wrapped Result, whose methods we invoke locally when a caller
    // (local or remote, over RMI) invokes our methods.
    private final Result<VALUE> result;


    /**
     * <p>
     * Creates a new RMIResult.
     * </p>
     *
     * @param result The wrapped Result, whose methods this RMIResult
     *               will invoke locally when a caller (local or remote,
     *               over RMI) invokes its methods.  For example,
     *               a standard AsynchronousResult.  Must not be null.
     */
    public RMIResult (
                      Result<VALUE> result
                      )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               result );

        this.result = result;
    }


    /**
     * @see musaico.foundation.value.blocking.Result#async(musaico.foundation.value.expression.Expression)
     */
    @Override
    public <OUTPUT extends Object>
        void async (
                    Expression<VALUE, OUTPUT> expression
                    )
        throws ParametersMustNotBeNull.Violation
    {
        return this.result.async ( expression );
    }


    /**
     * @see musaico.foundation.value.blocking.Result#await(musaico.foundation.blocking.Blocking, long)
     */
    @Override
    public final NonBlocking<VALUE> await (
                                           Blocking<VALUE> blocking_value,
                                           long timeout_in_nanoseconds
                                           )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation
    {
        return this.result.await ( blocking_value,
                                   timeout_in_nanoseconds );
    }


    /**
     * @see musaico.foundation.value.blocking.Result#hasPartialResult()
     */
    @Override
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
     * @see musaico.foundation.value.blocking.Result#isFinishedBlocking()
     */
    @Override
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
     * @see musaico.foundation.value.blocking.Result#maxTimeoutInNanoseconds()
     */
    @Override
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
            // creator of this RMIResult.
            return this.maxTimeoutInNanoseconds;
        }
    }


    /**
     * @see musaico.foundation.value.blocking.Result#partialResult(musaico.founndation.value.ValueViolation)
     */
    @Override
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
     * this RMIResult when it is eventually set.
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
        return this;
    }


    /**
     * @return The RMIResult.Server for this JVM, or  null if one has
     *         not (yet) been initialized.  Can be null.
     */
    public static final RMIResult.Server server ()
    {
        synchronized ( RMIResult.classLock )
        {
            return RMIResult.server;
        }
    }


    /**
     * <p>
     * Initializes the RMIResult.Server for this JVM, or returns the
     * already-initialized server, if one already exists.
     * </p>
     *
     * @return The new or existing RMIResult.Server.  Never null.
     *
     * @throws RemoteException If the server cannot be initialized
     *                         for any reason.
     */
    public static final RMIResult.Server serverInitialize ()
        throws RemoteException
    {
        synchronized ( RMIResult.classLock )
        {
            if ( RMIResult.server != null )
            {
                return RMIResult.server;
            }

            RMIResult.server = new RMIResult.StandardServer ();

            return RMIResult.server;
        }
    }


    /**
     * <p>
     * Initializes the RMIResult.Server for this JVM, setting it to
     * the specified server.
     * </p>
     *
     * <p>
     * This method can only be called once, and the server must not
     * have been already initialized before invocation.
     * </p>
     *
     * @param server The RMIResult.Server to use for remote Results.
     *               Must not be null.
     *
     * @return The specified RMIResult.Server.  Never null.
     *
     * @throws RemoteException If the server cannot be initialized
     *                         for any reason.
     */
    public static final RMIResult.Server serverInitialize (
            RMIResult.Server server
            )
        throws RemoteException
    {
        synchronized ( RMIResult.classLock )
        {
            if ( RMIResult.server != null
                 && RMIResult.server != server )
            {
                throw new RemoteException (
                    "RMIResult.serverInitialize ():"
                    + " RMIResult.server already exists: "
                    + RMIResult.server
                    + ".  Cannot set server to "
                    + server );
            }

            RMIResult.server = server

            return RMIResult.server;
        }
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
     * waiting on this RMIResult.
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
                    System.err.println ( "RMIResult "
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
