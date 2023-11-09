package musaico.foundation.security;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A single capability which implies other capabilities but does not
 * add any significance beyond the implied capabilities.
 * </p>
 *
 * <p>
 * For example, a READ capability might imply ( OPEN_FILE, CLOSE_FILE,
 * READ_FILE ).
 * </p>
 *
 *
 * <p>
 * In Java every Capability must implement <code> equals ( Object ) </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Capability must be Serializable in order to play nicely
 * across RMI.
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
 * @see musaico.foundation.security.MODULE#COPYRIGHT
 * @see musaico.foundation.security.MODULE#LICENSE
 */
public class CompositeCapability
    implements Capability, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts on static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( CompositeCapability.class );

    /** The capabilities implied by this composite. */
    private final Capability [] capabilities;


    /**
     * <p>
     * Creates a new CompositeCapability with the specified implied
     * capabilities.
     * </p>
     *
     * @param capabilities The other Capabilities which are implied by
     *                     this composite.  Must not be null.  Must
     *                     not contain any null elements.
     */
    public CompositeCapability (
                                Capability ... capabilities
                                )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) capabilities );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               capabilities );

        this.capabilities = new Capability [ capabilities.length ];
        System.arraycopy ( capabilities, 0,
                           this.capabilities, 0, capabilities.length );
    }


    /**
     * <p>
     * Returns the Capabilities implied by this composite.
     * </p>
     *
     * @return All of the Capabilities which are implied by this
     *         CompositeCapability.  Never null.  Never contains any
     *         null elements.
     */
    public Capability [] capabilities ()
    {
        Capability [] capabilities =
            new Capability [ this.capabilities.length ];
        System.arraycopy ( this.capabilities, 0,
                           capabilities, 0, this.capabilities.length );

        return capabilities;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == null )
        {
            // Any CompositeCapability != null.
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            // CompositeCapabilityA != Foo or CompositeCapabilityB.
            return false;
        }

        CompositeCapability that = (CompositeCapability) object;
        if ( this.capabilities == null )
        {
            if ( that.capabilities == null )
            {
                // null == null.
                return true;
            }
            else
            {
                // null != any Capabilities.
                return false;
            }
        }
        else if ( that.capabilities == null )
        {
            // Any Capabilities != null.
            return false;
        }

        Set<Capability> this_implied_capabilities = new HashSet<Capability> ();
        for ( Capability capability : this.capabilities )
        {
            this.addCapabilityToSet ( this_implied_capabilities,
                                      capability );
        }

        Set<Capability> that_implied_capabilities = new HashSet<Capability> ();
        for ( Capability capability : that.capabilities )
        {
            this.addCapabilityToSet ( that_implied_capabilities,
                                      capability );
        }

        if ( ! this_implied_capabilities.equals ( that_implied_capabilities ) )
        {
            return false;
        }

        // Everything matched.
        return true;
    }


    /** Place all non-composite capabilities into a set for comparison. */
    private void addCapabilityToSet (
                                     Set<Capability> implied_capabilities,
                                     Capability capability
                                     )
    {
        if ( capability == null )
        {
            return;
        }
        else if ( implied_capabilities.contains ( capability ) )
        {
            return;
        }
        else if ( capability instanceof CompositeCapability )
        {
            // Don't add the composite, just the implied capabilities.
            CompositeCapability composite = (CompositeCapability) capability;
            for ( Capability child : composite.capabilities () )
            {
                this.addCapabilityToSet ( implied_capabilities, child );
            }
        }
        else
        {
            // Leaf capability (not composite).
            implied_capabilities.add ( capability );
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = 0;
        for ( Capability capability : this.capabilities )
        {
            hash_code += capability.hashCode ();
        }

        return hash_code;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "CompositeCapability {" );
        boolean is_first = true;
        for ( Capability capability : this.capabilities () )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( ", " );
            }

            sbuf.append ( "" + capability );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }
}
