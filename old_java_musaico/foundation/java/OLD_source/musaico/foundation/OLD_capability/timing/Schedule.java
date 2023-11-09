package musaico.foundation.capability.administrative;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.capability.Capability;
import musaico.foundation.capability.StandardCapability;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;


!!!;
/**
 * <p>
 * How frequently and how many times a capable object can perform
 * some Capability, such as pausing and resuming operating, or
 * publishing events, or reading (polling) data, and so on.
 * </p>
 *
 * <p>
 * For example, a capable object may be capable of performing
 * some Capability once every 10 seconds, or once per minute,
 * or once per year, or once at 1pm on Sunday, December, 4, and so on,
 * or with finer grained control, such as "at a certain time every day"
 * or "on a certain day of every week" or "stop after N iterations",
 * or even a Schedule which changes over time, such as "once every minute
 * for 10 minutes, then once every hour for 10 hours, then once every
 * day until the end of time", and so on.
 * </p>
 *
 *
 * <p>
 * In Java, every Capability must be Serializable in order to play nicely
 * over RMI.  WARNING: Parameters such as Operations sent over RMI Capabilities
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
public class Schedule
    implements Iterator<Capability>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Schedule.class );


    // The periods after which a processor should operate, such
    // as once per 10 seconds, or once per minute, or once per year,
    // and so on.  Measured in nanoseconds.
    private final long periodInNanoseconds;

    /** Do nothing until a Control request is received. */
    public static final OperatingMode DORMANT =
        new OperatingMode<NoPayload> ( "dormant" );

    /** Set up, run once, then tear down. */
    public static final OperatingMode ONCE =
        new OperatingMode<NoPayload> ( "once" );

    /** Set up, run forever until told to stop, then tear down. */
    public static final OperatingMode SERVICE =
        new OperatingMode<NoPayload> ( "service" );

    /** Set up, run once every periodic interval, possibly until some
     *  termination condition such as OperatingPeriodFinite or
     *  OperatingPeriodUntilTime, or possibly forever, such as
     *  OperatingPeriodForever; or until told to stop; then tear down. */
    public static final OperatingMode PERIODIC =
        new OperatingMode<Period> ( "periodic" );


    // The name of this OperatingMode.
    private final String name;


    /**
     * <p>
     * Creates a new OperatingMode.
     * </p>
     *
     * @param name The name of the new OperatingMode.  Must not be null.
     */
    public OperatingMode (
                          String name
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

        final OperatingMode that = (OperatingMode) object;

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
     * @return The name of this OperatingMode.  Never null.
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
        return ClassName.of ( OperatingMode.class )
            + " [ " + this.name + " ]";
    }
}
