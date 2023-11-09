package musaico.foundation.term.operations;

import java.io.Serializable;


import musaico.foundation.term.Operation;


/**
 * <p>
 * An Operation which is guaranteed to produce the same result
 * every time it is applied to a specific idempotent term.
 * </p>
 *
 * <p>
 * By contrast, any Operation which changes state over time or with
 * subsequent applications, or any Operation which cannot guarantee
 * for any other reason that it will produce consistent results,
 * is not an IdempotentOperation.
 * </p>
 *
 * @see musaico.foundation.term.operations.operations.UnidempotentOperation
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
public interface IdempotentOperation<INPUT extends Object, OUTPUT extends Object>
    extends Operation<INPUT, OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
