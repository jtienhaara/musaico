package musaico.foundation.capability.publishing;

import java.io.Serializable;


import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * The ability to publish events to subscribers.
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
 * @see musaico.foundation.capability.publishing.MODULE#COPYRIGHT
 * @see musaico.foundation.capability.publishing.MODULE#LICENSE
 */
public class Publish
    extends AbstractPublishingCapability
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The name of every Publish capability. */
    public static final String NAME = "publish";


    !!! private final Class<?> eventClass;
    /**
     * <p>
     * Creates a new Publish capability.
     * </p>
     *
     * @param topic The topic which can be published, such as
     *              "product_change" or "OperatingState" and so on.
     *              Must not be null.
     */
    public Start (
                  String topic
                  )
    {
        super ( Publish.NAME,
                topic );
    }
}
