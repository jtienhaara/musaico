package musaico.foundation.operations;

import java.io.Serializable;


/**
 * <p>
 * An Operation which may have side effects, such as changing the state
 * of the world (files, databases, the user's screen, and so on), or
 * starts processes or threads, and so on.
 * </p>
 *
 * @see musaico.foundation.operations.OperationWithoutSideEffects
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
 * @see musaico.foundation.operations.MODULE#COPYRIGHT
 * @see musaico.foundation.operations.MODULE#LICENSE
 */
public interface OperationWithSideEffects<INPUT extends Object, OUTPUT extends Object, EFFECT extends Object>
    extends Operation<INPUT, OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
