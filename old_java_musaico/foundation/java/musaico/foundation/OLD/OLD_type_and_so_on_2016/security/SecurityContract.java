package musaico.foundation.security;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.ValueViolation;


/**
 * <p>
 * A contract specifying that a security policy must be followed
 * before subsequent processing can occur - for example, ensuring
 * read and execute permission before executing a file.
 * </p>
 *
 * <p>
 * The SecurityContract wraps the SecurityPolicy, so that for callers
 * who do not want to bother with handling the conditional results
 * of the SecurityPolicy, a simple "authorize or throw exception"
 * approach can be coded with something like:
 * </p>
 *
 * <pre>
 *     Capability READ = ...;
 *     Capability WRITE = ...;
 *     ObjectContracts my_contracts = new ObjectContracts ( this );
 *     SecurityPolicy security_policy = ...;
 *     SecurityContext context = ...;
 *
 *     Permission requested_permission =
 *         new Permission ( context, READ, WRITE );
 *     my_contracts.check ( new SecurityContract ( security_policy ),
 *                          requested_permission );
 * </pre>
 *
 * <p>
 * If the requested permission is granted to the given security
 * context by the security policy, then the code can continue.  Otherwise
 * a SecurityViolation is thrown up the stack.
 * </p>
 *
 * <p>
 * <b>
 * * * * WARNING * * *
 * </b>
 * </p>
 *
 * <p>
 * Because Arbiters are meant to turn on or off unchecked contract
 * violations (RuntimeExceptions such as ParametersMustNotBeNull.Violation),
 * it is conceivable that a contract Arbiter could be created to
 * ignore checked SecurityViolations.  So unless you know what you are doing,
 * you should call
 * <b>
 * <code> SecurityPolicy.request ( Permission ).orThrowDeclared () </code>
 * </b>
 * directly, since that call will always throw the SecurityViolation,
 * regardless of broken or malicious Arbiters.
 * </p>
 *
 * @see musaico.contract.Arbiter
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
 * nicely over RMI.
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
public class SecurityContract
    implements Contract<Permission, SecurityViolation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Manages contract checking for static methods and constructors.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( SecurityContract.class );


    // No SecurityContract.  Does absolutely nothing.
    public static final SecurityContract NONE =
        new SecurityContract ( SecurityPolicy.NONE );


    // The policy which decides which permissions to grante.
    private final SecurityPolicy policy;


    /**
     * <p>
     * Creates a new SecurityContract for the specified SecurityPolicy.
     * </p>
     *
     * @param policy The security policy which decides what Permission
     *               to grant or revoke depending on the SecurityContext.
     *               Must not be null.
     */
    public SecurityContract (
                             SecurityPolicy policy
                             )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               policy );

        this.policy = policy;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
                                     Permission permission
                                     )
    {
        return this.policy.request ( permission ).filter ();
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    public final SecurityViolation violation (
                                              Object plaintiff,
                                              Permission permission
                                              )
    {
        try
        {
            this.policy.request ( permission ).orThrowChecked ();
        }
        catch ( ValueViolation value_violation )
        {
            final Violation violation = value_violation.causeViolation ();
            if ( violation instanceof SecurityViolation )
            {
                // Return the specific SecurityViolation.
                return (SecurityViolation) violation;
            }
            else
            {
                // Caused by something else.  Wrap it in a security violation.
                return new SecurityViolation ( violation );
            }
        }

        // Return a generic SecurityViolation.
        final SecurityViolation violation =
            new SecurityViolation ( this,
                                    Contracts.makeSerializable ( plaintiff ),
                                    permission );
        return violation;
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
            // Any SecurityContract != null.
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            // SecurityContractA != Foo or SecurityContractB.
            return false;
        }

        SecurityContract that = (SecurityContract) object;
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

        return this.policy.equals ( that.policy );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.policy.hashCode ();
    }


    /**
     * <p>
     * Returns this SecurityContract's underlying SecurityPolicy.
     * </p>
     *
     * @return This contract's underlying security policy.  Never null.
     */
    public SecurityPolicy policy ()
    {
        return this.policy;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "SecurityContract " + this.policy;
    }
}
