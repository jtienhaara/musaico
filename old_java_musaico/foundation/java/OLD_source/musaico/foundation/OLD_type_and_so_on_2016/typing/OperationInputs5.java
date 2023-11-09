package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * Provides the Type of input # 5 to an Operation with 5 or more inputs.
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
public interface OperationInputs5<INPUT1 extends Object, INPUT2 extends Object, INPUT3 extends Object, INPUT4 extends Object, INPUT5 extends Object>
    extends OperationInputs4<INPUT1, INPUT2, INPUT3, INPUT4>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The Type of input parameter # 5 to this Operation.
     *         Never null.
     */
    public abstract Type<INPUT5> input5Type ()
        throws ReturnNeverNull.Violation;
}
