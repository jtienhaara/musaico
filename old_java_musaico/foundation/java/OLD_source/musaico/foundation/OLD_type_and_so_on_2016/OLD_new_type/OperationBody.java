package musaico.foundation.type;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.machine.InputsMachine;

import musaico.foundation.value.Value;


/**
 * <p>
 * The implementation of an Operation, performing all the real work
 * during invoke ( ... ).
 * </p>
 *
 * <p>
 * The OperationBody takes over once the Operation has finished
 * checking input contracts.
 * </p>
 *
 *
 * <p>
 * In Java every OperationBody must be Serializable in order to play
 * nicely with RMI.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public interface OperationBody<OUTPUT extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Implements the Operation logic after the input constraints
     * have been enforced, and before the output constraints have
     * been enforced.
     * </p>
     *
     * @see musaico.foundation.type.Operation1#invoke(musaico.foundation.value.Value...)
     */
    public abstract Value<OUTPUT> execute (
            InputsMachine<Object, Symbol, Type> inputs
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
