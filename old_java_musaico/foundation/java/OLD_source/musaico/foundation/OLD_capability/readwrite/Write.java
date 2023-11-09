package musaico.foundation.capability.administrative;

import java.io.Serializable;


import musaico.foundation.capability.StandardCapability;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;


/**
 * <p>
 * !!!
 * </p>
 *
 *
 * <p>
 * In Java, every Control must be Serializable in order to play nicely
 * over RMI.  WARNING: Parameters such as Operations sent over RMI Controls
 * (requesters, subscribers, and so on) generally must be UnicastRemoteObjects
 * in order to properly pass messages from one machine to the other, rather
 * than simply serializing each requester or subscriber and replying locally
 * on the controlled machine.
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
 * @see musaico.foundation.capability.administrative.MODULE#COPYRIGHT
 * @see musaico.foundation.capability.administrative.MODULE#LICENSE
 */
public class !!!
    extends StandardCapability
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( !!!.class );


    // !!!
    private final !!!;


    /** Set the value, such as the final result of an asynchronous task,
     *  and so on.
     * @param value The new value.  Must not be null.
     */
    public static final <VALUE extends Object>
                Set<VALUE> set (
                    Value<VALUE> value
                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new Set<VALUE> ( value );
    }

    /** Set the value, perhaps to a partial result, according to
     *  progress made.
     * @param value The new value, typically partial.  Must not be null.
     */
    public static final <VALUE extends Object>
                Progress<VALUE> progress (
                    Value<VALUE> value
                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new Progress<VALUE> ( value );
    }

    /** Wait at most the specified number of nanoseconds for
     *  a publication, such as the final result of some operation.
     * @param max_timeout_in_nanoseconds The maximum amount of time,
     *                                   in nanoseconds, to wait for
     *                                   a publication before giving up.
     *                                   Must be greater than
     *                                   or equal to 0L nanoseconds. */
    public static final Await await (
            long max_timeout_in_nanoseconds
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        return new Await ( max_timeout_in_nanoseconds );
    }

    /** Performs the specified callback asynchronously,
     *  whenever a publication is ready, such as the final result
     *  of some operation.
     * @param subscriber The Operation to execute whenever a publication
     *                   is ready.  The Operation takes the
     *                   publication as its input (possibly a failure,
     *                   such as a Timeout), and returns a Control
     *                   to the publisher (such as NONE or an unsubscribe).
     *                   Must not be null.
     * @param progress_subscriber The optional subscriber to progress
     *                            reports, for tracking asynchronous tasks
     *                            and so on.  Can be the same as the main
     *                            subscriber.  Can be null, for no interest
     *                            in progress reports. */
    public static final <VALUE extends Object>
            Subscribe<VALUE> subscribe (
                Operation<VALUE, Control> subscriber,
                Operation<VALUE, Control> progress_subscriber
                )
        throws Parameter1.MustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new Subscribe<VALUE> ( subscriber );
    }

    /** Removes the specified subscriber, so that they will no longer
     *  receive publications.  Some publishers may shut down when
     *  they have no asynchronous subscribers and subscriber is
     *  synchronously <code> await () </code>ing a publication.
     * @param subscriber The Operation which will no longer be executed
     *                   whenever a publication is ready.
     *                   Must not be null.
     * @param progress_subscriber The optional subscriber to progress
     *                            reports.  Can be the same as the main
     *                            subscriber.  Can be null, for no interest
     *                            in progress reports. */
    public static final <VALUE extends Object>
            Unsubscribe<VALUE> unsubscribe (
                Operation<VALUE, Control> subscriber,
                Operation<VALUE, Control> progress_subscriber
                )
        throws Parameter1.MustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new Unsubscribe<VALUE> ( subscriber );
    }

    /** Request access to a specific type of Control(s), the interface(s)
     *  to which will be returned if granted, or an error if not.
     * @param control_type The Type of Control requested, such as
     *                     read control or write control or operational
     *                     control or administrative control and so on.
     *                     Must not be null.
     * @param requester The requester which will either be granted
     *                  or denied the Control.  The input Control
     *                  value might be the One Control Operation
     *                  requested, or it might be an Error access denied.
     *                  The output from invoking the requester can be
     *                  any Control, such as NONE.  Must not be null. */
    public static final Request request (
                Type<? extends Control> control_type,
                Operation<Operation<Control, VALUE>, Control> requester
                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new Request ( control_type,
                             requester );
    }

    /** Request access to a specific type of Control(s) to be revoked
     *  to the specified requester.
     * @param control_type The Type of Control to revoke, such as
     *                     read control or write control or operational
     *                     control or administrative control and so on.
     *                     Must not be null.
     * @param requester The requester whose Control access will be revoked.
     *                  Must not be null. */
    public static final Revoke revoke (
                Type<? extends Control> control_type,
                Operation<Operation<Control, VALUE>, Control> requester
                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new Revoke ( control_type,
                            requester );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
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

        final Control that = (Control) object;

        if ( ! super.equals ( that ) )
        {
            return false;
        }

        if ( this.!!! == null )
        {
            if ( that.!!! != null )
            {
                return false;
            }
        }
        else if ( that.!!! == null )
        {
            return false;
        }
        else if ( ! this.!!!.equals ( that.!!! ) )
        {
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 23
            * ( this.!!! == null
                ? 0
                : this.!!!.hashCode () );
    }


    /**
     * @return The !!! of this !!!.  Never null.
     */
    public final !!! !!! ()
        throws ReturnNeverNull.Violation
    {
        return this.!!!;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return super.toString ()
            + " ( "
            + !!!
            + " )";
    }
}
