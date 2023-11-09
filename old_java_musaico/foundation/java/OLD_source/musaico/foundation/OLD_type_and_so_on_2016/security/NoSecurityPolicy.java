package musaico.foundation.security;

import java.io.Serializable;


import musaico.foundation.value.finite.One;


/**
 * <p>
 * No security policy at all, anything goes.  Use with caution!
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
public class NoSecurityPolicy
    implements SecurityPolicy, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new NoSecurityPolicy for laissez-faire "security".
     * </p>
     *
     * <p>
     * You should use SecurityPolicy.NONE rather than this constructor.
     * </p>
     */
    // package private
    NoSecurityPolicy ()
    {
    }


    /**
     * @see musaico.foundation.security.SecurityPolicy#request(musaico.foundation.security.Permission)
     */
    @Override
    public One<Permission> request (
            Permission requested_permission
            )
    {
        return
            new One<Permission> ( Permission.class,
                                  requested_permission );
    }
}
