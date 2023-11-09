package musaico.foundation.capability.permission;

import java.io.Serializable;


import musaico.foundation.capability.Capability;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.obligations.EveryParameter;


/**
 * <p>
 * The Caoability of requesting permission to make use of some other
 * Capability, such as requesting permission to overwrite data.
 * </p>
 *
 * <p>
 * If a Request Capability is sent to a capable object as a command,
 * then the sender typically expects a Grant or a Revoke response
 * from the capable object.
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
 * @see musaico.foundation.capability.permission.MODULE#COPYRIGHT
 * @see musaico.foundation.capability.permission.MODULE#LICENSE
 */
public class Request
    extends AbstractPermissionCapability
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new Request.
     * </p>
     *
     * @param permission The Capability to request.
     *                   Can be a CompositeCapability.
     *                   Must not be null.
     */
    public Request (
            Capability permission
            )
        throws EveryParameter.MustNotBeNull.Violation
    {
        super ( permission );
    }
}
