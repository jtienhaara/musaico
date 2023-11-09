package musaico.foundation.term.operations;

import java.io.Serializable;


import musaico.foundation.term.Operation;


/**
 * <p>
 * An Operation which changes state over time or with
 * subsequent applications, or any Operation which cannot guarantee
 * for any other reason that it will produce consistent results.
 * </p>
 *
 * <p>
 * By contrast, any Operation which is guaranteed to produce consistent
 * results is an IdempotentOperation.
 * </p>
 *
 * @see musaico.foundation.term.operations.operations.IdempotentOperation
 *
 *
 * <p>
 * In Java, every Operation must implement equals (), hashCode ()
 * and toString().
 * </p>
 *
 * <p>
 * In Java every Operation must be Serializable in order to
 * play nicely across RMI.
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
 * @see musaico.foundation.term.operations.MODULE#COPYRIGHT
 * @see musaico.foundation.term.operations.MODULE#LICENSE
 */
public interface UnidempotentOperation<INPUT extends Object, OUTPUT extends Object>
    extends Operation<INPUT, OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
