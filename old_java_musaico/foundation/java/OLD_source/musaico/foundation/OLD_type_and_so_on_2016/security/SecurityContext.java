package musaico.foundation.security;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * The context in which a security request is made.
 * </p>
 *
 * <p>
 * For example, in a UNIX-like environment, the current user and
 * groups would be part of the subject.  In a more secure UNIX-like
 * environment, along the lines of SELinux, a number of other bits
 * of information might be required to construct the context, such
 * as the process currently running, the object the request is being
 * made of, and so on.
 * </p>
 *
 *
 * <p>
 * In Java every SecurityContext must be Serializable in order to play
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
public interface SecurityContext
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** No security context at all. */
    public static final SecurityContext NONE =
        new NoSecurityContext ();


    /**
     * <p>
     * The credentials requesting security clearance, such as a user.
     * </p>
     *
     * @return The subject of the security context.  Never null.
     */
    public abstract Credentials subject ()
        throws ReturnNeverNull.Violation;
}
