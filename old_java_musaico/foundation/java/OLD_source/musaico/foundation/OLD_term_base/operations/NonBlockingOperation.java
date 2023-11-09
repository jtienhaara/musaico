package musaico.foundation.term.operations;

import java.io.Serializable;


import musaico.foundation.term.Operation;


/**
 * <p>
 * An Operation which performs CPU work then returns immediately, no
 * waiting on I/O or threads or processes and so on.
 * </p>
 *
 * @see musaico.foundation.term.operations.operations.BlockingOperation
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
public interface NonBlockingOperation<INPUT extends Object, OUTPUT extends Object>
    extends Operation<INPUT, OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
