package musaico.foundation.security;

import java.io.Serializable;

 
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;

import musaico.foundation.value.finite.No;


/**
 * <p>
 * A table of security policies, each one applicable to a specific
 * pattern of contexts.
 * </p>
 *
 * <p>
 * For example, a SecurityPolicyTable might contain a policy for
 * the owner of a file which allows reading, writing and executing;
 * another policy for the group-owner of a file, which allows only
 * reading; and another policy for "other" users which disallows all
 * access.
 * </p>
 *
 * <p>
 * Order is important, as the first row matching a requesting security
 * context will be the applicable one.  For example, if the previous
 * example rows are ordered ( owner, group-owner, other ), then first
 * the current user will be matched against the owner pattern, and given
 * full privileges if the user matches.  However if the reverse order
 * is used, ( other, group-owner, owner ), and if the "other" pattern
 * matches every user, then even the owner user will receive limited
 * "other" permissions.
 * </p>
 *
 * <p>
 * If none of the rows in a SecurityPolicyTable match a given security
 * context, then no permission is granted to that context.
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
public class SecurityPolicyTable
    implements SecurityPolicy, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts on static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( SecurityPolicyTable.class );


    /** Checks contracts on methods for us. */
    private final ObjectContracts contracts;

    /** The rows of the table, one policy applicable to each
     *  context pattern.  The first matching row assigns the policy. */
    private final SecurityPolicyRow [] rows;

    /** The security contract which this policy table provides as the
     *  overall contract.  Each individual row's policy provides its
     *  own contract. */
    private final SecurityContract securityContract;


    /**
     * <p>
     * Creates a new SecurityPolicyTable with the specified
     * pattern/policy rows, each of which applies a specific
     * SecurityPolicy when a given SecurityContextPattern is matched.
     * The rows must be ordered such that the first matching row
     * is applied to a given SecurityContext.
     * </p>
     *
     * <p>
     * If none of the rows matches a SecurityContext then no
     * permission is granted to that context.
     * </p>
     *
     * @param rows The ordered rows of the table.  Each row matches
     *             a pattern of security contexts and, if matched,
     *             that row's security policy will be applied to the
     *             requesting security context.
     *             Must not be null.  Must not contain any null elements.
     */
    public SecurityPolicyTable (
                                SecurityPolicyRow [] rows
                                )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) rows);
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               rows );

        this.rows = new SecurityPolicyRow [ rows.length ];
        System.arraycopy ( rows, 0,
                           this.rows, 0, rows.length );

        this.securityContract = new SecurityContract ( this );

        this.contracts = new ObjectContracts ( this );
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

        SecurityContext context = requested_permission.context ();

        for ( SecurityPolicyRow row : this.rows () )
        {
            if ( row.pattern ().matches ( context ) )
            {
                SecurityPolicy policy = row.policy ();
                Value<Permission> granted_permission =
                    policy.request ( requested_permission );
                return granted_permission;
            }
        }

        SecurityViolation violation =
            new SecurityViolation ( this.securityContract,
                                    Contracts.makeSerializable ( this ),
                                    Contracts.makeSerializable ( requested_permission )
                                    );

        No<Permission> failure =
            new No<Permission> ( Permission.class,
                                 violation );

        return failure;
    }


    /**
     * <p>
     * Returns the individual rows of this security policy table, each
     * one applying a specific security policy to any context matching
     * a specific pattern.
     * </p>
     *
     * @return The rows of this table.  Never null.  Never contains any
     *         null elements.
     */
    public SecurityPolicyRow [] rows ()
    {
        SecurityPolicyRow [] rows = new SecurityPolicyRow [ this.rows.length ];
        System.arraycopy ( this.rows, 0,
                           rows, 0, this.rows.length );

        return rows;
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
            // Any SecurityPolicyTable != null.
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            // SecurityPolicyTableA != Foo or SecurityPolicyTableB.
            return false;
        }

        SecurityPolicyTable that = (SecurityPolicyTable) object;
        if ( this.rows == null )
        {
            if ( that.rows == null )
            {
                // null == null.
                return true;
            }
            else
            {
                // null != any SecurityPolicyRows.
                return false;
            }
        }
        else if ( that.rows == null )
        {
            // Any SecurityPolicyRows != null.
            return false;
        }
        else if ( this.rows.length != that.rows.length )
        {
            // #M SecurityPolicyRows != #N SecurityPolicyRows
            return false;
        }

        for ( int r = 0; r < this.rows.length; r ++ )
        {
            if ( this.rows [ r ] == null )
            {
                if ( that.rows [ r ] == null )
                {
                    // null == null
                    continue;
                }
                else
                {
                    // null != any SecurityPolicyRow
                    return false;
                }
            }
            else if ( that.rows [ r ] == null )
            {
                // Any SecurityPolicyRow != null
                return false;
            }

            if ( ! this.rows [ r ].equals ( that.rows [ r ] ) )
            {
                // SecurityPolicyRow x != SecurityPolicyRow y
                return false;
            }
        }

        // whole table matches.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = 0;
        for ( SecurityPolicyRow row : this.rows )
        {
            hash_code += row.hashCode ();
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
        sbuf.append ( "SecurityPolicyTable#" + this.hashCode () + " {" );
        sbuf.append ( "\n" );

        int r = 0;
        for ( SecurityPolicyRow row : this.rows () )
        {
            sbuf.append ( "    " + r + ": " + row );
            sbuf.append ( "\n" );

            r ++;
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }
}
