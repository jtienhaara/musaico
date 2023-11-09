package musaico.foundation.typing;

import java.io.Serializable;

import musaico.foundation.value.Value;


/**
 * <p>
 * The body of an abstract Operation with 3 inputs.
 * </p>
 *
 *
 * <p>
 * In Java every OperationBody3 must be Serializable in order to play
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
public interface OperationBody3<INPUT1 extends Object, INPUT2 extends Object, INPUT3 extends Object, OUTPUT extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Implements the Operation logic after the input Constraints
     * have been enforced, and before the output Constraints have
     * been enforced.
     * </p>
     *
     * <p>
     * No need to do any constraint checking here, the caller
     * must do that before invoking this method.
     * </p>
     *
     * @see musaico.foundation.typing.StandardOperation3#evaluate(musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value)
     */
    public abstract Value<OUTPUT> evaluateBody (
                                                Value<INPUT1> input1,
                                                Value<INPUT2> input2,
                                                Value<INPUT3> input3
                                                );
}
