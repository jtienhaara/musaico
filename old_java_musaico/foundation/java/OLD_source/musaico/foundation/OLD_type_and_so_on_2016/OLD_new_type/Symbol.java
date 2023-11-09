package musaico.foundation.type;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * A Symbol is any named object.
 * </p>
 *
 * <p>
 * For example, a Type is a Symbol, an Operation is a Symbol,
 * an input to an Operation is a Symbol, and so on.
 * </p>
 *
 * <p>
 * An Operation is a Symbol whose graph determines the type(s) of
 * inputs it accepts, and in what order.
 * </p>
 *
 * <p>
 * And so on.
 * </p>
 *
 *
 * <p>
 * In Java every Symbol must be Serializable in order to play
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
 * @see musaico.foundation.type.MODULE#COPYRIGHT
 * @see musaico.foundation.type.MODULE#LICENSE
 */
public interface Symbol
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The name of this Symbol.  Never null.
     */
    public abstract String name ()
        throws ReturnNeverNull.Violation;
}
