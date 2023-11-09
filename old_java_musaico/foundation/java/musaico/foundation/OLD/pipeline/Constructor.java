package musaico.foundation.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * An object which can build a new Pipeline from a series of 1 or more
 * parameter(s).
 * </p>
 *
 *
 * <p>
 * In Java every Construct must be Serializable in order to
 * play nicely across RMI.  However users of the Construct
 * must be careful, since it could contain non-Serializable elements.
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
 * @see musaico.foundation.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.pipeline.MODULE#LICENSE
 */
public interface Constructor<PARAMETER extends Object, CONSTRUCTED extends Construct>
    extends Construct, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // !!! Operation derives from Constructor.

    /**
     * <p>
     * Continues constructing the Pipeline, adding the specified
     * parameter.
     * </p>
     *
     * @param parameter The parameter which determines the next stage
     *                  of construction.  Must be kept by this Constructor's
     *                  <code> contract () </code>, or else
     *                  a contract violation will be thrown.
     *                  Must not be null.
     *
     * @return Either the next stage of Constructors or branching Choose steps,
     *         or the final, constructed Pipeline.  Never null.
     *
     * @throws UncheckedViolation Throws a contract Violation if the specified
     *                            parameter is not kept by this Constructor's
     *                            <code> contract () </code>.
     */
    public abstract CONSTRUCTED apply (
            PARAMETER parameter
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               UncheckedViolation,
               ReturnNeverNull.Violation;


    /**
     * @return The Contract governing permissable value(s) for this
     *         Constructor's parameter.  Never null.
     */
    public abstract Contract<PARAMETER [], ? extends UncheckedViolation> contract ()
        throws ReturnNeverNull.Violation;
}
