package musaico.foundation.capability.operating;

import java.io.Serializable;


import musaico.foundation.capability.StandardCapability;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * The ability to change operating state, such as by starting (changing
 * from OperatingState.NONE to OperatingState.STARTED, or from
 * OperatingState.STOPPED to OperatingState.STARTED), or stopping,
 * or pausing, or resuming, and so on.
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
 * @see musaico.foundation.capability.operating.MODULE#COPYRIGHT
 * @see musaico.foundation.capability.operating.MODULE#LICENSE
 */
public abstract class AbstractOperatingCapability
    extends StandardCapability
    implements OperatingCapability, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( AbstractOperatingCapability.class );


    // The target state of this OperatingCapability, such as
    // OperatingState.STARTED.
    private final OperatingState targetState;


    /**
     * <p>
     * Creates a new AbstractOperatingCapability.
     * </p>
     *
     * @param target_state The target state of this OperatingCapability,
     *                     such as <code> OperatingState.STARTED </code>.
     *                     Must not be null.
     */
    public AbstractOperatingCapability (
            OperatingState target_state
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ();

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               target_state );

        this.targetState = target_state;
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

        final AbstractOperatingCapability that = (AbstractOperatingCapability) object;

        if ( ! super.equals ( that ) )
        {
            return false;
        }

        if ( this.targetState == null )
        {
            if ( that.targetState != null )
            {
                return false;
            }
        }
        else if ( that.targetState == null )
        {
            return false;
        }
        else if ( ! this.targetState.equals ( that.targetState ) )
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
            + 17
            * ( this.targetState == null
                ? 0
                : this.targetState.hashCode () );
    }


    /**
     * @see musaico.foundation.capability.operating.OperatingCapability#targetState()
     */
    @Override
    public final OperatingState targetState ()
        throws ReturnNeverNull.Violation
    {
        return this.targetState;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return super.toString ()
            + " ( "
            + this.targetState
            + " )";
    }
}
