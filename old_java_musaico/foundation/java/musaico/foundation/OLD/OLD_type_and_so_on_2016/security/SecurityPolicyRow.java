package musaico.foundation.security;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * One row in a SecurityPolicyTable, specifying a context pattern
 * to match, and the policy to execute for each matching security context.
 * </p>
 *
 * <p>
 * For example, one SecurityPolicyRow might grant the permission to
 * a security context with a specific subject user; whereas another
 * SecurityPolicyRow might revoke all permissions from any subject
 * not matching a specific user; and so on.
 * </p>
 *
 *
 * <p>
 * In Java every SecurityPolicyRow must be Serializable in order to
 * play nicely across RMI.
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
public class SecurityPolicyRow
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts on static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( SecurityPolicyRow.class );


    /** The pattern to match against for this row, such as
     *  "any security context where the subject is user U". */
    private final SecurityContextPattern pattern;

    /** The policy to enforce for matching security contexts,
     *  such as "grant READ capability but not WRITE or EXECUTE". */
    private final SecurityPolicy policy;


    /**
     * <p>
     * Creates a new SecurityPolicyRow which, when the specified pattern
     * is matched, enforces the specified security policy.
     * </p>
     *
     * @param pattern The security context pattern to match against.
     *                Must not be null.
     *
     * @param policy The policy to apply when the specified pattern
     *               matches a security context.  Must not be null.
     */
    public SecurityPolicyRow (
                              SecurityContextPattern pattern,
                              SecurityPolicy policy
                              )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               pattern, policy );

        this.pattern = pattern;
        this.policy = policy;
    }


    /**
     * <p>
     * Returns the security context pattern to be matched for this row.
     * </p>
     *
     * @return The pattern which matches SecurityContexts for this row.
     *         Never null.
     */
    public SecurityContextPattern pattern ()
    {
        return this.pattern;
    }


    /**
     * <p>
     * Returns the security policy to apply to SecurityContexts matching
     * this row's pattern.
     * </p>
     *
     * @return The security policy to apply to every SecurityContext
     *         that matches this row.  Never null.
     */
    public SecurityPolicy policy ()
    {
        return this.policy;
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
            // Any SecurityPolicyRow != null.
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            // SecurityPolicyRowA != Foo or SecurityPolicyRowB.
            return false;
        }

        SecurityPolicyRow that = (SecurityPolicyRow) object;
        if ( this.pattern == null )
        {
            if ( that.pattern == null )
            {
                // null == null.
                return true;
            }
            else
            {
                // null != any SecurityContextPattern.
                return false;
            }
        }
        else if ( that.pattern == null )
        {
            // Any SecurityContextPattern != null.
            return false;
        }

        if ( this.policy == null )
        {
            if ( that.policy == null )
            {
                // null == null.
                return true;
            }
            else
            {
                // null != any SecurityPolicy.
                return false;
            }
        }
        else if ( that.policy == null )
        {
            // Any SecurityPolicy != null.
            return false;
        }

        if ( ! this.pattern.equals ( that.pattern ) )
        {
            // SecurityContextPattern x != SecurityContextPattern y
            return false;
        }

        if ( ! this.policy.equals ( that.policy ) )
        {
            // SecurityPolicy x != SecurityPolicy y
            return false;
        }

        // Whole row matches.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = 0;
        hash_code += this.pattern.hashCode ();
        hash_code += this.policy.hashCode ();

        return hash_code;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "" + this.pattern () );
        sbuf.append ( " ? --> " + this.policy () );

        return sbuf.toString ();
    }
}
