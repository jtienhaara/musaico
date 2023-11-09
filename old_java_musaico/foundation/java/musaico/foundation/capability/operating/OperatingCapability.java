package musaico.foundation.capability.operating;

import java.io.Serializable;


import musaico.foundation.capability.Capability;

import musaico.foundation.contract.guarantees.Return;


/**
 * <p>
 * A Capability which describes or affects a client's access to
 * a particular Capability, such as requesting operating, granting
 * operating, revoking operating, and so on.
 * </p>
 *
 *
 * <p>
 * In Java, every Capability must implement equals (), hashCode ()
 * and toString ().
 * </p>
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
 * @see musaico.foundation.capability.MODULE#COPYRIGHT
 * @see musaico.foundation.capability.MODULE#LICENSE
 */
public interface OperatingCapability
    extends Capability, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** The capability to start operating. */
    public static final Start START = new Start ();

    /** The capability to pause operating. */
    public static final Pause PAUSE = new Pause ();

    /** The capability to resume operating. */
    public static final Resume RESUME = new Resume ();

    /** The capability to stop operating. */
    public static final Stop STOP = new Stop ();


    // Every Capability must implement:
    // java.lang.Object#equals(java.lang.Object)

    // Every Capability must implement:
    // java.lang.Object#hashCode()

    // Every Capability must implement:
    // musaico.foundation.capability.Capability#name()


    /**
     * @return The target state of this OperatingCapability.  For example,
     *         a "start" OperatingCapability might target
     *         <code> OperatingState.STARTED </code>.  It can be
     *         an ErrorCapability, though this would be inadvisable
     *         in most, if not all, cases.  Never null.
     */
    public abstract OperatingState targetState ()
        throws Return.NeverNull.Violation;


    // Every Capability must implement:
    // java.lang.Object#toString()
}
