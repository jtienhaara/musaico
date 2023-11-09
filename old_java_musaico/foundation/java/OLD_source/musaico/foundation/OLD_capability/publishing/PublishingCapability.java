package musaico.foundation.capability.operating;

import java.io.Serializable;


import musaico.foundation.capability.Capability;

import musaico.foundation.contract.guarantees.ReturnNeverNull;


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
public interface PublishingCapability
    extends Capability, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every Capability must implement:
    // java.lang.Object#equals(java.lang.Object)

    // Every Capability must implement:
    // java.lang.Object#hashCode()

    // Every Capability must implement:
    // musaico.foundation.capability.Capability#name()


    /**
     * @return The topic of this PublishingCapability.  For example,
     *         an operable object might publish notification to all
     *         its subscribers whenever its operating state has changed
     *         (started to stopped, stopped to started, and so on),
     *         specifying "OperatingState" as its topic.
     *         Or a database of products might publish a notification
     *         with "product_change" as its topic, any time a product
     *         changes price or description.  And so on.
     *         There are very few restrictions on the topics which
     *         a given publisher or subscriber object may be Capable
     *         of, except that no topic may be null.
     *         Otherwise, the range of supported topics is entirely
     *         up to the publisher / subscriber.  A subscriber object
     *         which receives publications for topics it cares nothing
     *         about should just ignore those publications (or
     *         unsubscribe from them).  Never null.
     */
    public abstract String topic ()
        throws ReturnNeverNull.Violation;


    // Every Capability must implement:
    // java.lang.Object#toString()
}
