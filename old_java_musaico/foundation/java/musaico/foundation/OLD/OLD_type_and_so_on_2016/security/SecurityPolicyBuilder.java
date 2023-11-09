package musaico.foundation.security;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Builds up a SecurityPolicy, typically by adding one row
 * at a time to build a SecurityPolicyTable.
 * </p>
 *
 * <p>
 * For example, to build up the following table:
 * </p>
 *
 * <pre>
 *     {
 *         User u ? --&gt; Grant READ, WRITE, EXECUTE
 *         User who is a member of group g ? --&gt; Grant READ, EXECUTE
 *         (default) --&gt; Grant READ
 *     }
 * </pre>
 *
 * <p>
 * Something along the lines of the following might suffice:
 * </p>
 *
 * <pre>
 *     SecurityPolicy policy = new SecurityPolicyBuilder ()
 *         .on ( new SubjectUserPattern ( u ),
 *               new Grant ( READ, WRITE, EXECUTE ) )
 *         .on ( new SubjectUserMemberOfGroup ( g ),
 *               new Grant ( READ, EXECUTE ) )
 *         .otherwise ( new Grant ( READ ) )
 *         .build ();
 * </pre>
 *
 *
 * <p>
 * In Java every SecurityPolicyBuilder must be Serializable in order
 * to play nicely over RMI.
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
public class SecurityPolicyBuilder
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts on methods for us. */
    private final ObjectContracts contracts;

    /** Lock critical sections on this token: */
    private final String lock = new String ();

    /** The rows of the security policy table being built up, one
     *  pattern + one policy at a time. */
    private final List<SecurityPolicyRow> rows;


    /**
     * <p>
     * Creates a new SecurityPolicyBuilder.
     * </p>
     */
    public SecurityPolicyBuilder ()
    {
        this.rows = new ArrayList<SecurityPolicyRow> ();
        this.contracts = new ObjectContracts ( this );
    }


    /**
     * <p>
     * Given a SecurityContext matching the specified pattern,
     * apply the specified SecurityPolicy (such as a Grant or a
     * Revoke and so on).
     * </p>
     *
     * @param pattern The pattern to match.  Must not be null.
     *
     * @param policy The security policy to apply when a SecurityContext
     *               matches the specified pattern.  Must not be null.
     *
     * @return This SecurityPolicyBuilder.  Never null.
     */
    public SecurityPolicyBuilder on (
                                     SecurityContextPattern pattern,
                                     SecurityPolicy policy
                                     )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               pattern, policy );

        SecurityPolicyRow row = new SecurityPolicyRow ( pattern, policy );

        synchronized ( this.lock )
        {
            this.rows.add ( row );
        }

        return this;
    }


    /**
     * <p>
     * Adds a policy which is applied to every SecurityContext
     * (no pattern).
     * </p>
     *
     * <p>
     * Typically <code> otherwise () </code> is called to set the default
     * policy, after one or more calls to <code> on () </code>.
     * </p>
     *
     * @param policy The policy to apply to all SecurityContexts
     *               reaching the new row of the policy table.
     *               Must not be null.
     *
     * @return Thus SecurityPolicyBuilder.  Never null.
     */
    public SecurityPolicyBuilder otherwise (
                                            SecurityPolicy policy
                                            )
        throws ParametersMustNotBeNull.Violation
    {
        this.on ( SecurityContextPattern.ANY, policy );

        return this;
    }


    /**
     * <p>
     * Builds a new SecurityPolicy from the current state of the
     * builder.
     * </p>
     *
     * @return A newly constructed SecurityPolicy.  Never null.
     */
    public SecurityPolicy build ()
    {
        final SecurityPolicyRow [] rows;
        synchronized ( this.lock )
        {
            SecurityPolicyRow [] template =
                new SecurityPolicyRow [ this.rows.size () ];
            rows = this.rows.toArray ( template );
        }

        SecurityPolicyTable table = new SecurityPolicyTable ( rows );

        return table;
    }
}
