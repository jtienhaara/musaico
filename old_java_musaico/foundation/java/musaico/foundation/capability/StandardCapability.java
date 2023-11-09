package musaico.foundation.capability;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Basic implementation of Capability, with a name but nothing more.
 * </p>
 *
 *
 * <p>
 * In Java, every Capability must implement equals (), hashCode ()
 * and toString ().  StandardCapability provides default implementations
 * of these, which can be overridden.
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
public class StandardCapability
    implements Capability, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( StandardCapability.class );


    // The name of this StandardCapability.
    private final String name;


    /**
     * <p>
     * Creates a new StandardCapability.
     * </p>
     *
     * @param name The name of the new StandardCapability.
     *             Must not be null.
     */
    public StandardCapability (
                               String name
                               )
        throws EveryParameter.MustNotBeNull.Violation
    {
        classContracts.check ( EveryParameter.MustNotBeNull.CONTRACT,
                               name );

        this.name = name;
    }


    /**
     * <p>
     * Creates a new StandardCapability.
     * </p>
     *
     * <p>
     * Protected.  For derived classes with meaningful class names,
     * such as "DoSomething" (as opposed to "StandardCapability",
     * which is as bland as contemporary music and as disconnected
     * from reality as contemporary physics).
     * </p>
     */
    protected StandardCapability ()
    {
        this.name = ClassName.of ( this.getClass () );
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

        final StandardCapability that = (StandardCapability) object;

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
        throws Return.NeverNull.Violation
    {
        return this.name;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " [ " + this.name + " ]";
    }
}
