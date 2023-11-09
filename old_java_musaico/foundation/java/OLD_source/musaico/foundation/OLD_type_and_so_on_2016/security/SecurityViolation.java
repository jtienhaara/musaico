package musaico.foundation.security;

import java.io.Serializable;


import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Violation;


/**
 * <p>
 * An error thrown when an operation is not permitted in a particular
 * security context.
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
public class SecurityViolation
    extends CheckedViolation
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a SecurityViolation for the specified breached security
     * contract.
     * </p>
     *
     * @param security_contract The security contract which was breached,
     *                          by someone who was not permitted to perform
     *                          some operation attempting to perform it
     *                          anyway -- and failing to do so, inducing
     *                          this violation of the security policy.
     *                          Must not be null.
     *
     * @param plaintiff The object whose contract was breached,
     *                  or a Serializable representation
     *                  of that object (such as a String).
     *                  Must not be null.
     *
     * @param permission The data which breached the contract, or a
     *                   Serializable representation of that data
     *                   (such as a String) if the data is not
     *                   Serializable.  Must not be null.
     */
    public SecurityViolation (
                              SecurityContract security_contract,
                              Serializable plaintiff,
                              Serializable permission
                              )
    {
        super ( security_contract,
                plaintiff,
                permission );
    }


    /**
     * <p>
     * Creates a SecurityViolation caused by a violation of some other
     * non-security Contract.
     * </p>
     *
     * @param violation The contract violation which induced this
     *                  SecurityViolation.  Must not be null.
     */
    public SecurityViolation (
                              Violation violation
                              )
    {
        super ( violation.contract (),
                violation.plaintiff (),
                violation.evidence () );

        if ( violation instanceof Throwable )
        {
            this.initCause ( (Throwable) violation );
        }
    }
}
