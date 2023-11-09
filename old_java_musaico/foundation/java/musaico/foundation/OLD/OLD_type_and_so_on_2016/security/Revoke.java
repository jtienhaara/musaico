package musaico.foundation.security;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

 
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;

import musaico.foundation.value.incomplete.Partial;


/**
 * <p>
 * Revokes a specific set of capabilities to any who request them,
 * granting everything else.
 * </p>
 *
 * <p>
 * Typically used as part of a permissive SecurityPolicyTable such as:
 * </p>
 *
 * <pre>
 *     SecurityPolicyTable
 *     {
 *         SecurityContextPattern.ANY ? --&gt; Grant ( READ, WRITE, EXECUTE )
 *         ...some specific pattern...  --&gt; Revoke ( WRITE ) }.
 *     }
 * </p>
 *
 *
 * <p>
 * In Java every SecurityPolicy must be Serializable in order to
 * play nicely over RMI.
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
public class Revoke
    implements SecurityPolicy, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts on static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Revoke.class );


    /** Checks contracts on methods for us. */
    private final ObjectContracts contracts;

    /** The capabilities to revoke. */
    private final Set<Capability> capabilities;

    /** The security contract for violations (whenever a capability
     *  that is requested is explicitly part of this revoke). */
    private final SecurityContract securityContract;


    /**
     * <p>
     * Creates a new Revoke to grant everything except the specified
     * Capabilities, and only the specified Capabilities.
     * </p>
     *
     * @param capabilities The capabilities to revoke whenever requested.
     *                     All other requested capabilities are granted.
     *                     Must not be null.  Must not contain any
     *                     null elements.
     */
    public Revoke (
                   Capability ... capabilities
                   )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) capabilities);
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               capabilities );

        this.capabilities = new LinkedHashSet<Capability> ();
        for ( Capability capability : capabilities )
        {
            this.addCapability ( capability );
        }

        this.securityContract = new SecurityContract ( this );

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * During the constructor only, Capabilities are added to the
     * internal set.  After the constructor no modifications are permitted.
     */
    private void addCapability (
                                Capability capability
                                )
    {
        if ( this.capabilities.contains ( capability ) )
        {
            // Already added it, infinite loop?
            return;
        }

        this.capabilities.add ( capability );

        if ( capability instanceof CompositeCapability )
        {
            CompositeCapability composite = (CompositeCapability) capability;
            for ( Capability implied : composite.capabilities () )
            {
                this.addCapability ( implied );
            }
        }
    }


    /**
     * @see musaico.foundation.security.SecurityPolicy#request(musaico.foundation.security.Permission)
     */
    @Override
    public Value<Permission> request (
            Permission requested_permission
            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               requested_permission );

        List<Capability> granted_capabilities =
            new ArrayList<Capability> ();
        boolean are_all_requested_capabilities_granted = true;
        for ( Capability requested_capability : requested_permission.capabilities () )
        {
            if ( ! this.capabilities.contains ( requested_capability ) )
            {
                granted_capabilities.add ( requested_capability );
            }
            else
            {
                are_all_requested_capabilities_granted = false;
            }
        }

        final Value<Permission> result;
        if ( are_all_requested_capabilities_granted )
        {
            // Nothing was disallowed, so success.
            result =
                new One<Permission> (
                    Permission.class,
                    requested_permission );
        }
        else if ( granted_capabilities.size () == 0 )
        {
            // Absolutely nothing was allowed, so complete failure.
            SecurityViolation violation =
                new SecurityViolation ( this.securityContract,
                                        Contracts.makeSerializable ( this ),
                                        Contracts.makeSerializable ( requested_permission )
                                        );
            result =
                new No<Permission> (
                    Permission.class,
                    violation );
        }
        else
        {
            // Partial success, at least one Capability was granted.
            // Treat as failure by default, but
            // if the caller wants to find out which capabilities they
            // were granted, they can call <code> orPartial () </code>
            // to get back One value containing the partial
            // permission.
            Capability [] template =
                new Capability [ granted_capabilities.size () ];
            Capability [] capabilities =
                granted_capabilities.toArray ( template );

            SecurityContext context = requested_permission.context ();

            Permission partial_permission =
                new Permission ( context, capabilities );

            SecurityViolation violation =
                new SecurityViolation ( this.securityContract,
                                        Contracts.makeSerializable ( this ),
                                        Contracts.makeSerializable ( requested_permission )
                                        );
            result =
                new Partial<Permission> (
                    Permission.class,
                    violation,
                    partial_permission );
        }

        return result;
    }


    /**
     * <p>
     * Returns the capabilities revoked by this policy, including all
     * implied capabilities.
     * </p>
     *
     * @return The Capabilities revokedd by this policy.  Never null.
     *         Never contains any null elements.
     */
    public Capability [] capabilities ()
    {
        Capability [] template =
            new Capability [ this.capabilities.size () ];
        Capability [] capabilities =
            this.capabilities.toArray ( template );

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
            // Any Revoke != null.
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            // RevokeA != Foo or RevokeB.
            return false;
        }

        Revoke that = (Revoke) object;
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
        else if ( this.capabilities.size () != that.capabilities.size () )
        {
            // #M Capabilities != #N Capabilities
            return false;
        }

        for ( Capability this_capability : this.capabilities )
        {
            if ( ! that.capabilities.contains ( this_capability ) )
            {
                // Capabilities including x != Capabilities excluding x
                return false;
            }
        }

        for ( Capability that_capability : that.capabilities )
        {
            if ( ! this.capabilities.contains ( that_capability ) )
            {
                // Capabilities excluding x != Capabilities including x
                return false;
            }
        }

        // all Capabilities match.
        return true;
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
        sbuf.append ( "Revoke {" );
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
