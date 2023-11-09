package musaico.foundation.security;

import java.io.Serializable;

import java.util.LinkedHashSet;
import java.util.Set;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * Requested or granted permission for exercising a particular set
 * of capabilities in a particular security context (such as a specific
 * user reading and executing a specific file).
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
public class Permission
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts for static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Permission.class );


    /** No permission in no security context. */
    public static final Permission NONE =
        new Permission ( SecurityContext.NONE,
                         new Capability [ 0 ] );


    /** Enforces obligations and guarantees for us. */
    private final ObjectContracts contracts;

    /** The context in which this permission was requested and/or granted.
     *  Includes the subject (who requested the permission). */
    private final SecurityContext context;

    /** The capabilities requested/granted.  Can be empty. */
    private final Set<Capability> capabilities;


    /**
     * <p>
     * Creates a new Permission in the specified context for the specified
     * Capabilities.
     * </p>
     *
     * @param context The context in which the permission is being
     *                requested or granted.  Must not be null.
     *
     * @param capabilities The capabilities which are being requested
     *                     or granted.  Must not be null.  Must not contain
     *                     any null elements.
     */
    public Permission (
                       SecurityContext context,
                       Capability ... capabilities
                       )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context, capabilities );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               (Object []) capabilities );

        this.context = context;

        this.capabilities = new LinkedHashSet<Capability> ();
        for ( Capability capability : capabilities )
        {
            this.capabilities.add ( capability );
        }

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * <p>
     * Returns the security context (including requesting subject)
     * in which the permission was requested and/or granted.
     * </p>
     *
     * @return The security context of this permission.  Never null.
     */
    public SecurityContext context ()
        throws ReturnNeverNull.Violation
    {
        return this.context;
    }


    /**
     * <p>
     * Returns a copy of the set of capabilities included in the
     * requested / granted permission.
     * </p>
     *
     * @return The requested or granted Capabilities.  Never null.
     *         Never includes any null elements.
     */
    public Capability [] capabilities ()
    {
        Capability [] capabilities =
            new Capability [ this.capabilities.size () ];
        int c = 0;
        for ( Capability capability : this.capabilities )
        {
            capabilities [ c ] = capability;
        }

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
        if ( object == null
             || ! ( object instanceof Permission ) )
        {
            return false;
        }

        Permission that = (Permission) object;
        if ( ! this.context ().equals ( that.context () ) )
        {
            return false;
        }

        for ( Capability capability : that.capabilities () )
        {
            if ( ! this.isAllowed ( capability ) )
            {
                return false;
            }
        }

        for ( Capability capability : this.capabilities () )
        {
            if ( ! that.isAllowed ( capability ) )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = this.context.hashCode ();
        for ( Capability capability : this.capabilities )
        {
            hash_code += capability.hashCode ();
        }

        return hash_code;
    }


    /**
     * <p>
     * Returns true if the specified Permission are allowed by these
     * Permission.
     * </p>
     *
     * <p>
     * That is, returns true if the specified Permission' SecurityContext
     * is equal to these Permission' context, and the specified
     * Permission' Capabilities are equal to, or a subset of, these
     * Permission' allowed Capabilities.
     * </p>
     *
     * @param permission The Permission to check.  Must not be null.
     *
     * @return True if the specified Permission are explicitly allowed
     *         by these Permission in the same context; false if not.
     */
    public boolean isAllowed (
                              Permission permission
                              )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               permission );

        if ( this.isAllowed ( permission.context () )
             && this.isAllowed ( permission.capabilities () ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * <p>
     * Returns true if these Permission' security context is equal
     * to the specified context.
     * </p>
     *
     * @param context The SecurityContext to match against these Permission'
     *                context.  Must not be null.
     */
    public boolean isAllowed (
                              SecurityContext context
                              )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        SecurityContext my_context = this.context ();
        if ( my_context.equals ( context ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * <p>
     * Returns true if the specified Capabilities are all included in this
     * Permission, false if any is not included.
     * </p>
     *
     * @param capabilities The capabilities to check.  Can be empty, but
     *                     true is always returned for an empty array of
     *                     Capabilities.  Must not be null.
     *                     Must not contain any null values.
     *
     * @return True if all of the specified Capabilities are included
     *         in this permission, false if not.
     */
    public boolean isAllowed (
                              Capability ... capabilities
                              )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object []) capabilities );
        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               (Object []) capabilities );

        for ( Capability capability : capabilities )
        {
            if ( ! this.capabilities.contains ( capability ) )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "Permission [ " + this.context + " ] = { " );
        boolean is_first_capability = true;
        for ( Capability capability : this.capabilities )
        {
            if ( is_first_capability )
            {
                is_first_capability = false;
            }
            else
            {
                sbuf.append ( ", " );
            }

            sbuf.append ( "" + capability );
        }

        return sbuf.toString ();
    }


    /**
     * <p>
     * Returns these Permission unless the specified Capabilities
     * are not permitted, in which case a SecurityViolation is thrown.
     * </p>
     *
     * @return This Permission.  Never null.
     */
    public Permission ifNotPermittedThrowViolation (
                                                    Capability ... capabilities
                                                    )
        throws SecurityViolation,
               ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        if ( ! this.isAllowed ( capabilities ) )
        {
            throw new SecurityViolation ( SecurityContract.NONE,
                                          this,
                                          this );
        }

        return this;
    }
}
