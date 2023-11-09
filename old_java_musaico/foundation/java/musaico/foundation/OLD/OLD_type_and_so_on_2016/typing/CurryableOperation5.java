package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * A curryable Operation which takes 5 inputs.
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
public interface CurryableOperation5<INPUT1 extends Object, INPUT2 extends Object, INPUT3 extends Object, INPUT4 extends Object, INPUT5 extends Object, OUTPUT extends Object>
    extends Operation5<INPUT1, INPUT2, INPUT3, INPUT4, INPUT5, OUTPUT>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Creates and returns a curried Operation which takes 4 inputs.
     * </p>
     *
     * @param input1 Input parameter # 1 to operate on.  Must not be null.
     *
     * @return One newly created, curried Operation4, or No operation
     *         if the specified input(s) are not valid by the input Type(s).
     *         Evaluating the returned Operation with the remaining
     *         input(s) actually evaluates this Operation5 with all inputs.
     *         Never null.
     *
     * @see musaico.foundation.typing.Operation5#evaluate(musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value)
     */
    public abstract ZeroOrOne<Operation4<INPUT2, INPUT3, INPUT4, INPUT5, OUTPUT>> evaluate (
                        Value<INPUT1> input1
                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates and returns a curried Operation which takes 3 inputs.
     * </p>
     *
     * @param input1 Input parameter # 1 to operate on.  Must not be null.
     *
     * @param input2 Input parameter # 2 to operate on.  Must not be null.
     *
     * @return One newly created, curried Operation3, or No operation
     *         if the specified input(s) are not valid by the input Type(s).
     *         Evaluating the returned Operation with the remaining
     *         input(s) actually evaluates this Operation5 with all inputs.
     *         Never null.
     *
     * @see musaico.foundation.typing.Operation5#evaluate(musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value)
     */
    public abstract ZeroOrOne<Operation3<INPUT3, INPUT4, INPUT5, OUTPUT>> evaluate (
                        Value<INPUT1> input1,
                        Value<INPUT2> input2
                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates and returns a curried Operation which takes 2 inputs.
     * </p>
     *
     * @param input1 Input parameter # 1 to operate on.  Must not be null.
     *
     * @param input2 Input parameter # 2 to operate on.  Must not be null.
     *
     * @param input2 Input parameter # 3 to operate on.  Must not be null.
     *
     * @return One newly created, curried Operation2, or No operation
     *         if the specified input(s) are not valid by the input Type(s).
     *         Evaluating the returned Operation with the remaining
     *         input(s) actually evaluates this Operation5 with all inputs.
     *         Never null.
     *
     * @see musaico.foundation.typing.Operation5#evaluate(musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value)
     */
    public abstract ZeroOrOne<Operation2<INPUT4, INPUT5, OUTPUT>> evaluate (
                        Value<INPUT1> input1,
                        Value<INPUT2> input2,
                        Value<INPUT3> input3
                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates and returns a curried Operation which takes 1 input.
     * </p>
     *
     * @param input1 Input parameter # 1 to operate on.  Must not be null.
     *
     * @param input2 Input parameter # 2 to operate on.  Must not be null.
     *
     * @param input3 Input parameter # 3 to operate on.  Must not be null.
     *
     * @param input4 Input parameter # 4 to operate on.  Must not be null.
     *
     * @return One newly created, curried Operation1, or No operation
     *         if the specified input(s) are not valid by the input Type(s).
     *         Evaluating the returned Operation with the remaining
     *         input(s) actually evaluates this Operation5 with all inputs.
     *         Never null.
     *
     * @see musaico.foundation.typing.Operation5#evaluate(musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value, musaico.foundation.value.Value)
     */
    public abstract ZeroOrOne<Operation1<INPUT5, OUTPUT>> evaluate (
                        Value<INPUT1> input1,
                        Value<INPUT2> input2,
                        Value<INPUT3> input3,
                        Value<INPUT4> input4
                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
