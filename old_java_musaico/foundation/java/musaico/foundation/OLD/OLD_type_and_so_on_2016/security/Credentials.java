package musaico.foundation.security;

import java.io.Serializable;


/**
 * <p>
 * Credentials for a security request.
 * </p>
 *
 * <p>
 * For example, a user name and password, or a signature, and so on,
 * which in some way originated a request for permission to do
 * something.
 * </p>
 *
 * <p>
 * Security providers should be careful to never give Credentials
 * objects to untrusted sources, and arguably to even time limit
 * the validity of a Credentials object for trusted sources.
 * Alternatively, if the context of a security request can be
 * generated from other state (such as an HTTP request with a
 * session ID), then the Credentials should be generated that way
 * and only used internally by trusted code.  As soon as a Credentials
 * object is given to an external code source, the whole application
 * is at risk.
 * </p>
 *
 * <p>
 * Developers should also be careful to never place anything
 * confidential in a Credentials implementation, since it may
 * be shared with non-trustworthy agents.  For example, never
 * store a plaintext password in a Credentials.
 * </p>
 *
 *
 * <p>
 * In Java every Credentials must implement equals() and hashCode().
 * </p>
 *
 * <p>
 * In Java every Credentials must be Serializable in order to play
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
public interface Credentials
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** No credentials at all. */
    public static final Credentials NONE =
        new NoCredentials ();
}
