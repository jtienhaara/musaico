package musaico.foundation.capability.permission;

import java.io.Serializable;


import musaico.foundation.capability.Capability;
import musaico.foundation.capability.StandardCapability;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;


/**
 * <p>
 * The Capability of administering permissions, such as requesting
 * permission, granting permission
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
public abstract class AbstractPermissionCapability
    extends StandardCapability
    implements PermissionCapability, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( AbstractPermissionCapability.class );


    // The Capability requested / granted / revoked / and so on.
    // Can be a CompositeCapability.
    private final Capability permission;


    /**
     * <p>
     * Creates a new AbstractPermissionCapability.
     * </p>
     *
     * @param permission The Capability which has been requested or
     *                   granted or revoked.  Can be a CompositeCapability.
     *                   Must not be null.
     */
    public AbstractPermissionCapability (
            Capability permission
            )
        throws EveryParameter.MustNotBeNull.Violation
    {
        super ();

        classContracts.check ( EveryParameter.MustNotBeNull.CONTRACT,
                               permission );

        this.permission = permission;
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
        else if ( ! super.equals ( object ) )
        {
            return false;
        }

        final AbstractPermissionCapability that =
            (AbstractPermissionCapability) object;

        if ( this.permission == null )
        {
            if ( that.permission != null )
            {
                return false;
            }
        }
        else if ( that.permission == null )
        {
            return false;
        }
        else if ( ! this.permission.equals ( that.permission ) )
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
        return super.hashCode ()
            + 23
            * ( this.permission == null
                ? 0
                : this.permission.hashCode () );
    }


    /**
     * @see musaico.foundation.capability.permission.PermissionCapability#permission()
     */
    @Override
    public final Capability permission ()
        throws Return.NeverNull.Violation
    {
        return this.permission;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return super.toString ()
            + " ( "
            + this.permission
            + " )";
    }
}
