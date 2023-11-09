package musaico.foundation.capability;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;


/**
 * <p>
 * A token representing some action, to be used for declaring that
 * "I have the following Capabilities", or for requesting permission
 * to make use of a particular Capability, or as an instruction to use
 * a Capability, and so on.
 * </p>
 *
 * <p>
 * Capability is designed to allow simple token passing between a
 * capable object and its client(s), without too much regard
 * toward how the capable object implements its Capability,
 * or even which machine(s) client and capable object reside on,
 * which language(s) each is written in, and so on.
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
public interface Capability
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Do nothing. */
    public static final Capability NONE =
        new NoCapability ( "none" );


    // Every Capability must implement:
    // java.lang.Object#equals(java.lang.Object)

    // Every Capability must implement:
    // java.lang.Object#hashCode()


    /**
     * @return The name of this Capability.  Never null.
     */
    public abstract String name ()
        throws Return.NeverNull.Violation;


    // Every Capability must implement:
    // java.lang.Object#toString()
}
