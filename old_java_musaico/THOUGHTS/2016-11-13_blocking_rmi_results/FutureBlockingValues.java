package musaico.foundation.value.blocking;

import java.io.Serializable;

import java.util.concurrent.Future;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.ValueViolation;

import musaico.foundation.value.abnormal.Error;

import musaico.foundation.value.finite.One;


/**
 * <p>
 * A set of Blocking Value(s) which can be cancelled all at once.
 * </p>
 *
 *
 * <p>
 * In Java, every FutureBlockingValues must be Serializable in order to
 * play nicely over RMI.
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
public class FutureBlockingValues
    implements Future, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( FutureBlockingValues.class );

    // Synchronize critical sections on this token:
    private final Serializable lock =
        new String ( "lock" );

    // The Blocking Values to cancel.
    private final Blocking<?> [] blockingValues;

    // MUTABLE:
    // Set to true when cancel () is called.
    private boolean isCancelled = false;

    /**
     * <p>
     * Creates a FutureBlockingValues.
     * </p>
     *
     * @param blocking_values The Blocking Values whose AsynchronousResults
     *                        will all be cancelled when this
     *                        is cancelled.  Must not be null.
     *                        Must not contain any null elements.
     *
     * @throws RemoteException If UnicastRemoteObject cannot be constructed.
     */
    public FutureBlockingValues (
                                 Blocking<?> [] blocking_values
                                 )
        throws RemoteException
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object []) blocking_values );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               (Object []) blocking_values );

        this.blockingValues = blocking_values;
    }

    /**
     * @see musaico.foundation.value.blocking.Future#cancel()
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"}) // Generic array.
    public final Maybe<Future> cancel (
                                       boolean may_interrupt_if_running
                                       )
        throws ReturnNeverNull.Violation
    {
        synchronized ( this.lock )
        {
            if ( this.isCancelled )
            {
                final One<Cancellable> ok =
                    new One<Cancellable> ( Cancellable.TYPE,
                                           this );

                return ok;
            }

            final CancellableMustNotBeCancelled.Violation violation =
                CancellableMustNotBeCancelled.CONTRACT.violation (
                    this, // plaintiff
                    this ); // evidence
            final ValueViolation cancellation =
                new ValueViolation ( violation );

            for ( Blocking<?> blocking_value : this.blockingValues )
            {
                // We don't care whether the Blocking Value
                // returns a Cancelled result.  It might have already
                // finished blocking; and even if it failed to cancel,
                // we have no way of distinguishing between a failed
                // cancellation and a more general failure to get
                // the Value it wanted to get, which occurred long
                // before we tried to cancel the operation.
                // Therefore we just cancel and ignore the outcome.
                blocking_value.cancel ( cancellation );
            }

            this.isCancelled = true;
        }

        final One<Cancellable> ok =
            new One<Cancellable> ( Cancellable.TYPE,
                                   this );

        return ok;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final CancellableBlockingValues that =
            (CancellableBlockingValues) object;

        if ( this.blockingValues == null )
        {
            if ( that.blockingValues != null )
            {
                return false;
            }
        }
        else if ( that.blockingValues == null )
        {
            return false;
        }
        else
        {
            if ( this.blockingValues.length != that.blockingValues.length )
            {
                return false;
            }

            for ( int bv = 0; bv < this.blockingValues.length; bv ++ )
            {
                final Blocking<?> this_blocking_value =
                    this.blockingValues [ bv ];
                final Blocking<?> that_blocking_value =
                    that.blockingValues [ bv ];

                if ( this_blocking_value == null )
                {
                    if ( that_blocking_value != null )
                    {
                        return false;
                    }
                }
                else if ( that_blocking_value == null )
                {
                    return false;
                }
                else
                {
                    if ( ! this_blocking_value.equals ( that_blocking_value ) )
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        if ( this.blockingValues == null )
        {
            return 0;
        }

        int hash_code = 0;
        for ( Blocking<?> blocking_value : this.blockingValues )
        {
            hash_code += blocking_value.hashCode ();
        }

        return hash_code;
    }

    /**
     * @see musaico.foundation.value.blocking.Cancellable#cancel()
     */
    @Override
    public final boolean isCancelled ()
    {
        synchronized ( this.lock )
        {
            return this.isCancelled;
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        for ( Blocking<?> blocking_value : this.blockingValues )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( ", " );
            }

            sbuf.append ( "" + blocking_value );
        }

        return ClassName.of ( this.getClass () )
            + " ( "
            + sbuf.toString ()
            + " )";
    }
}
