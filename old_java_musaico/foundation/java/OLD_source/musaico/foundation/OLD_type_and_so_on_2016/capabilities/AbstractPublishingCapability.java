package musaico.foundation.capability.operating;

import java.io.Serializable;


import musaico.foundation.capability.StandardCapability;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Implementation of the common aspects of a PublishingCapability.
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
public abstract class AbstractPublishingCapability
    extends StandardCapability
    implements PublishingCapability, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( AbstractPublishingCapability.class );


    // The topic being published or subscribed to and so on.
    private final String topic;


    /**
     * <p>
     * Creates a new AbstractPublishingCapability.
     * </p>
     *
     * @param name The name of the new AbstractPublishingCapability.
     *             Must not be null.
     *
     * @param topic The topic of this AbstractPublishingCapability.
     *              Must not be null.
     */
    public AbstractPublishingCapability (
                                         String name,
                                         String topic
                                         )
        throws ParametersMustNotBeNull.Violation
    {
        super ( name );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               topic );

        this.topic = topic;
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

        final AbstractPublishingCapability that = (AbstractPublishingCapability) object;

        if ( ! super.equals ( that ) )
        {
            return false;
        }

        if ( this.topic == null )
        {
            if ( that.topic != null )
            {
                return false;
            }
        }
        else if ( that.topic == null )
        {
            return false;
        }
        else if ( ! this.topic.equals ( that.topic ) )
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
            * ( this.topic == null
                ? 0
                : this.topic.hashCode () );
    }


    /**
     * @see musaico.foundation.capability.operating.PublishingCapability#topic()
     */
    @Override
    public final String topic ()
        throws ReturnNeverNull.Violation
    {
        return this.topic;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return super.toString ()
            + " ( "
            + this.topic
            + " )";
    }
}
