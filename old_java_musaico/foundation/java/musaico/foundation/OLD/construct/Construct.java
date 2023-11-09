package musaico.foundation.construct;

import java.io.Serializable;


/**
 * <p>
 * Either a Constructor of some sort to build an Object, or a Choose
 * branching decision to overload Constructors, or the final
 * Constructed object itself.
 * </p>
 *
 *
 * <p>
 * In Java every Construct must be Serializable in order to
 * play nicely across RMI.  However users of the Construct
 * must be careful, since it could contain non-Serializable elements.
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
 * @see musaico.foundation.construct.MODULE#COPYRIGHT
 * @see musaico.foundation.construct.MODULE#LICENSE
 */
public interface Construct<OUTPUT extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
