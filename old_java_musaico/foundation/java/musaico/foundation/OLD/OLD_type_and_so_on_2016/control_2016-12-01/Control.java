package musaico.foundation.value.control;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.value.Operation;
import musaico.foundation.value.Type;


/**
 * <p>
 * A message to a controllable processor, instructing it to start or stop,
 * return a value, publish events, and so on.
 * </p>
 *
 * <p>
 * Control is designed to allow simple message passing between a
 * controller and a processor being controlled, without too much regard
 * toward which machine each resides on, which language each
 * is written in, and so on.
 * </p>
 *
 * <p>
 * However be warned that Controls which carry Operations
 * (subscribe, unsubscribe, request control, revoke control, and so on)
 * are inherently machine-specific, language-specific, and so on,
 * unless precautions are taken by the developer to build cross-network
 * or cross-language Operations.  For example, a developer wanting to
 * allow subscriptions over RMI would have to provide a subscriber
 * Operation which is a UnicastRemoteObject, or something similar.
 * (The processor would surely also have to be a UnicastRemoteObject
 * or something similar, but that is outside the scope
 * of the control package.)
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
 * @see musaico.foundation.value.control.MODULE#COPYRIGHT
 * @see musaico.foundation.value.control.MODULE#LICENSE
 */
public class Control<MODE extends Mode, PAYLOAD extends Serializable>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Control.class );


    /** No payload, for a Control which does not carry one. */
    public static final Serializable NO_PAYLOAD =
        new NoPayload ();


    /** Do nothing. */
    public static final Control<NoMode, Serializable> NONE =
        new Control<NoMode, Serializable> ( "none", Mode.NONE,
                                            Control.NO_PAYLOAD );

    /** Set up and begin processing. */
    !!! public static final OperatingControl START =
        new OperatingControl ( "start",  );

    /** Pause processing. */
    public static final OperatingControl PAUSE =
        new OperatingControl ( "pause" );

    /** Resume processing. */
    public static final OperatingControl RESUME =
        new OperatingControl ( "resume" );

    /** Abort processing and tear down. */
    public static final OperatingControl STOP =
        new OperatingControl ( "stop" );

    /** Get the value, such as an initial value, or a partial
     *  or final result of an asynchronous task, or an error if no
     *  value is available, and so on.  Do not wait.
     *  (See <code> await () </code> and <code> subscribe () </code>
     *  for possible alternatives.) */
    public static final ReadControl GET =
        new ReadControl ( "get" );

    /** Unset the value. */
    public static final WriteControl UNSET =
        new WriteControl ( "unset" );

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
    public static final <VALUE extends Object, CONTROL extends Object>
            Subscribe<VALUE, CONTROL> subscribe (
                Operation<VALUE, CONTROL> subscriber,
                Operation<VALUE, CONTROL> progress_subscriber
                )
        throws Parameter1.MustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new Subscribe<VALUE, CONTROL> ( subscriber );
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
    public static final <VALUE extends Object, CONTROL extends Object>
            Unsubscribe<VALUE, CONTROL> unsubscribe (
                Operation<VALUE, CONTROL> subscriber,
                Operation<VALUE, CONTROL> progress_subscriber
                )
        throws Parameter1.MustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new Unsubscribe<VALUE, CONTROL> ( subscriber );
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
    public static final <CONTROL extends Control, VALUE extends Object>
            Request<CONTROL, VALUE> request (
                Type<CONTROL> control_type,
                Operation<Operation<CONTROL, VALUE>, CONTROL> requester
                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new Request<CONTROL, VALUE> ( control_type,
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
    public static final <CONTROL extends Control, VALUE extends Object>
            Revoke<CONTROL, VALUE> revoke (
                Type<CONTROL> control_type,
                Operation<Operation<CONTROL, VALUE>, CONTROL> requester
                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new Revoke<CONTROL, VALUE> ( control_type,
                                            revoke );
    }


    // The name of this Control.
    private final String name;

    // The mode specified by this Control, such as an OperatingMode,
    // or a ReadMode, or Mode.NONE, and so on.
    private final MODE mode;


    /**
     * <p>
     * Creates a new Control.
     * </p>
     *
     * @param name The name of the new Control.  Must not be null.
     *
     * @param mode The mode specified by this Control, such as
     *             an OperatingMode, or a ReadMode, or Mode.NONE,
     *             and so on.  Must not be null.
     *
     * @param payload The body of this Control message, such as the period
     *                and/or termination settings for operating, or the
     *                field to read, and so on.  Must not be null.
     */
    public Control (
                    String name,
                    MODE mode,
                    PAYLOAD payload
                    )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        this.name = name;
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

        if ( this.name == null )
        {
            if ( that.name != null )
            {
                return false;
            }
        }
        else if ( that.name == null )
        {
            return false;
        }
        else if ( ! this.name.equals ( that.name ) )
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
        return 29
            * ( this.name == null
                ? 0
                : this.name.hashCode () );
    }


    /**
     * @return The name of this Control.  Never null.
     */
    public final String name ()
        throws ReturnNeverNull.Violation
    {
        return this.name;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( Control.class )
            + " [ " + this.name + " ]";
    }
}
