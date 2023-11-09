package musaico.foundation.capability;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;


/**
 * <p>
 * No capability at all.
 * </p>
 *
 * @see musaico.foundation.capability.Capability.NONE
 *
 *
 * <p>
 * In Java, every Capability must be Serializable in order to play nicely
 * over RMI.  WARNING: Callback parameters sent over RMI Capabilities
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
public class NoCapability
    implements Capability, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( NoCapability.class );


    // The name of this NoCapability.
    private final String name;


    /**
     * <p>
     * Creates the Capability.NONE NoCapability.
     * </p>
     *
     * @param name The name of this NoCapability, such as "none".
     *             Must not be null.
     *
     * Protected.  Use Capability.NONE instead.
     */
    protected NoCapability (
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

        final NoCapability that = (NoCapability) object;

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
     * @see musaico.foundation.capability.Capability#name()
     */
    @Override
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
        final String maybe_name;
        if ( this == Capability.NONE )
        {
            maybe_name = "";
        }
        else
        {
            maybe_name = " [ " + this.name + " ]";
        }

        return ClassName.of ( this.getClass () )
            + maybe_name;
    }
}
