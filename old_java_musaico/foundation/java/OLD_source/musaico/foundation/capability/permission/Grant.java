package musaico.foundation.capability.permission;

import java.io.Serializable;


import musaico.foundation.capability.Capability;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * The Caoability of granting permission to make use of some other
 * Capability, such as granting permission to overwrite data.
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
public class Grant
    extends AbstractPermissionCapability
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new Grant.
     * </p>
     *
     * @param permission The Capability to grant.
     *                   Can be a CompositeCapability.
     *                   Must not be null.
     */
    public Grant (
            Capability permission
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( permission );
    }
}
