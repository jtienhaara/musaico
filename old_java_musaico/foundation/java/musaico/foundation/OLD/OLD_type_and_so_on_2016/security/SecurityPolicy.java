package musaico.foundation.security;

import java.io.Serializable;


import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.value.Value;


/**
 * <p>
 * A single rule for when to grant permission, or a whole hierarchical
 * security policy.
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
public interface SecurityPolicy
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** No security policy at all.  Anything goes.
     *  Use with care! */
    public static final NoSecurityPolicy NONE = new NoSecurityPolicy ();


    /**
     * <p>
     * Given the specified requested permission, returns the
     * conditionally granted permission (either No value or One value,
     * or possibly a Partial value if only some of the requested
     * Permission's capabilities were not granted but others were).
     * </p>
     *
     * <p>
     * If the specified request is fully allowed, a
     * <code> Successful&lt;Permission&gt; </code>
     * is returned with the fully granted Permission.
     * </p>
     *
     * <p>
     * If some subset of the requested Permission are allowed,
     * then a <code> Partial&lt;Permission&gt; </code>
     * is returned with the granted Permission subset.
     * By default this is treated as a failure, but the caller
     * can invoke <code> orPartialSuccess () </code> on the
     * result to retrieve the partially granted Permission.
     * </p>
     *
     * <p>
     * If the Permission is not granted, then a
     * </code> Failed&lt;Permission&gt; </code> is returned,
     * and the caller must either deal with the security violation,
     * or default to <code> Permission.NONE </code> or null and so on.
     * </p>
     *
     * @param requested_permission The Permission set being requested.
     *                             Must not be null.
     *
     * @return The conditional granted Permission (No or One value,
     *         or possibly a Partial result containing only the
     *         subset of Permission's capabilities which were granted).
     *         Never null.
     */
    public abstract Value<Permission> request (
            Permission requested_permission
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
