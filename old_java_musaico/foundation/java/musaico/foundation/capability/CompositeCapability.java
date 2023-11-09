package musaico.foundation.capability;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.EveryParameter;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Multiple Capabilities bundled as one.
 * </p>
 *
 * <p>
 * Can be used, for example, to represent a sequence of Capabilities
 * for an instruction, or a set of Capabilities for a query result,
 * and so on.
 * </p>
 *
 *
 * <p>
 * In Java, every Capability must implement equals (), hashCode ()
 * and toString ().  CompositeCapability provides default implementations
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
public class CompositeCapability
    extends StandardCapability
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( CompositeCapability.class );


    // The child Capabilities of this composite parent.
    private final Capability [] capabilities;


    /**
     * <p>
     * Creates a new CompositeCapability with a default name.
     * </p>
     *
     * @param capabilities The children of the new Capability.
     *                     Must not be null.
     *                     Must not contain any null elements.
     */
    public CompositeCapability (
                                Iterable<Capability> capabilities
                                )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        this ( createName (
                   (Capability [])
                   classContracts.check (
                       Parameter1.MustContainNoNulls.CONTRACT,
                       classContracts.check (
                           EveryParameter.MustNotBeNull.CONTRACT, // C[][] in & out
                           (Object) capabilities
                           )
                           [0] // EveryParameter.MustNotBeNull returns array
                       )
                   ),
               createArray ( capabilities ) );
    }


    /**
     * <p>
     * Creates a new CompositeCapability with a default name.
     * </p>
     *
     * @param capabilities The children of the new Capability.
     *                     Must not be null.
     *                     Must not contain any null elements.
     */
    public CompositeCapability (
                                Capability ... capabilities
                                )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        this ( createName (
                   (Capability [])
                   classContracts.check (
                       Parameter1.MustContainNoNulls.CONTRACT,
                       classContracts.check (
                           EveryParameter.MustNotBeNull.CONTRACT, // C[][] in & out
                           (Object) capabilities
                           )
                           [0] // EveryParameter.MustNotBeNull returns array
                       )
                   ),
               capabilities );
    }


    /**
     * <p>
     * Creates a new CompositeCapability.
     * </p>
     *
     * @param name The name of the new CompositeCapability.
     *             Must not be null.
     *
     * @param capabilities The children of the new Capability.
     *                     Must not be null.
     *                     Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Cast output from EveryParameter.MustNotBeNull.
    public CompositeCapability (
                                String name,
                                Iterable<Capability> capabilities
                                )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( name,
               createArray (
                   classContracts.check (
                       Parameter2.MustContainNoNulls.CONTRACT,
                       (Iterable<Capability>)
                       classContracts.check (
                           EveryParameter.MustNotBeNull.CONTRACT, // C[] in & out
                           capabilities
                           )
                           [0] // EveryParameter.MustNotBeNull returns array
                       )
                   )
               );
    }


    /**
     * <p>
     * Creates a new CompositeCapability.
     * </p>
     *
     * @param name The name of the new CompositeCapability.
     *             Must not be null.
     *
     * @param capabilities The children of the new Capability.
     *                     Must not be null.
     *                     Must not contain any null elements.
     */
    public CompositeCapability (
                                String name,
                                Capability ... capabilities
                                )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        super ( name );

        classContracts.check ( EveryParameter.MustNotBeNull.CONTRACT,
                               (Object) capabilities );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               capabilities );

        this.capabilities = new Capability [ capabilities.length ];
        System.arraycopy ( capabilities, 0,
                           this.capabilities, 0, capabilities.length );
    }


    // Creates the default name of a CompositeCapability, "{...}",
    // where the ellipsis is filled with the names of all the children.
    private static final String createName (
                                            Capability [] capabilities
                                            )
    {
        final StringBuilder child_capabilities = new StringBuilder ();
        boolean is_first = true;
        child_capabilities.append ( "{" );
        for ( Capability capability : capabilities )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                child_capabilities.append ( "," );
            }

            child_capabilities.append ( " " );
            child_capabilities.append ( capability.name () );
        }

        if ( ! is_first )
        {
            child_capabilities.append ( " " );
        }

        child_capabilities.append ( "}" );

        return child_capabilities.toString ();
    }


    // Creates the default name of a CompositeCapability, "{...}",
    // where the ellipsis is filled with the names of all the children.
    private static final String createName (
                                            Iterable<Capability> capabilities
                                            )
    {
        final StringBuilder child_capabilities = new StringBuilder ();
        boolean is_first = true;
        child_capabilities.append ( "{" );
        for ( Capability capability : capabilities )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                child_capabilities.append ( "," );
            }

            child_capabilities.append ( " " );
            child_capabilities.append ( capability.name () );
        }

        if ( ! is_first )
        {
            child_capabilities.append ( " " );
        }

        child_capabilities.append ( "}" );

        return child_capabilities.toString ();
    }


    // Convert an Iterable to an array.
    private static final Capability [] createArray (
            Iterable<Capability> capabilities_iterable
            )
    {
        final Collection<Capability> capabilities_collection;
        if ( capabilities_iterable instanceof Collection )
        {
            capabilities_collection =
                (Collection<Capability>) capabilities_iterable;
        }
        else
        {
            capabilities_collection = new ArrayList<Capability> ();

            for ( Capability capability : capabilities_iterable )
            {
                capabilities_collection.add ( capability );
            }
        }

        final Capability [] template =
            new Capability [ capabilities_collection.size () ];
        final Capability [] capabilities_array =
            capabilities_collection.toArray ( template );
        return capabilities_array;
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

        final CompositeCapability that = (CompositeCapability) object;

        if ( this.capabilities == null )
        {
            if ( that.capabilities != null )
            {
                return false;
            }
        }
        else if ( that.capabilities == null )
        {
            return false;
        }
        else if ( this.capabilities.length != that.capabilities.length )
        {
            return false;
        }

        for ( int c = 0; c < this.capabilities.length; c ++ )
        {
            final Capability this_capability = this.capabilities [ c ];
            final Capability that_capability = that.capabilities [ c ];

            if ( this_capability == null )
            {
                if ( that_capability != null )
                {
                    return false;
                }
            }
            else if ( that_capability == null )
            {
                return false;
            }
            else if ( ! this_capability.equals ( that_capability ) )
            {
                return false;
            }
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
            + 11 * this.capabilities.length;
    }


    /**
     * @return The child Capabilities of this composite.  Never null.
     *         Never contains any null elements.
     */
    public final Capability [] capabilities ()
        throws Return.NeverNull.Violation
    {
        final Capability [] capabilities =
            new Capability [ this.capabilities.length ];
        System.arraycopy ( this.capabilities, 0,
                           capabilities, 0, this.capabilities.length );

        return capabilities;
    }


    // StandardCapability provides toString ().
}
