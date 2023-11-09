package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * Provides the Type of input # 3 to an Operation with 3 or more inputs.
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
public interface OperationInputs3<INPUT1 extends Object, INPUT2 extends Object, INPUT3 extends Object>
    extends OperationInputs2<INPUT1, INPUT2>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The Type of input parameter # 3 to this Operation.
     *         Never null.
     */
    public abstract Type<INPUT3> input3Type ()
        throws ReturnNeverNull.Violation;
}
