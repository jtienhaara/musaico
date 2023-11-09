package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * An Operation which takes 3 inputs.
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
public interface Operation3<INPUT1 extends Object, INPUT2 extends Object, INPUT3 extends Object, OUTPUT extends Object>
    extends Operation<OUTPUT>, OperationInputs3<INPUT1, INPUT2, INPUT3>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Evaluates this operation on the input(s), and returns
     * either a Just or No output.
     * </p>
     *
     * <p>
     * This method performs compile-time checking.  Whenever possible
     * it is preferable to use this method over the runtime-checking
     * method Operation.evaluate(List).
     * </p>
     *
     * @param input1 Input parameter # 1 to operate on.  Must not be null.
     *
     * @param input2 Input parameter # 2 to operate on.  Must not be null.
     *
     * @param input3 Input parameter # 3 to operate on.  Must not be null.
     *
     * @return The Value output.  If the operation is successful,
     *         then a Just result will be returned.
     *         Otherwise an Unjust result will be returned,
     *         and the caller can decide whether to call
     *         <code> orThrowChecked () <code>,
     *         <code> orDefault ( ... ) </code> and so on.
     *         Never null.
     *
     * @see musaico.foundation.typing.Operation#evaluate(java.util.List)
     */
    public abstract Value<OUTPUT> evaluate (
                                            Value<INPUT1> input1,
                                            Value<INPUT2> input2,
                                            Value<INPUT3> input3
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
