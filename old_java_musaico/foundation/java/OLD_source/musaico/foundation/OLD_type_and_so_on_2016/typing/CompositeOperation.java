package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.value.Value;


/**
 * <p>
 * An operation which contains one or more sub-operations.
 * </p>
 *
 * <p>
 * For example, a branch, or a "pipe", and so on.
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
public interface CompositeOperation<OUTPUT extends Object>
    extends Operation<OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The child Operations.  Never null.  Never contains any
     *         null elements.
     */
    public abstract Value<? extends Operation<?>> operations ();
}
