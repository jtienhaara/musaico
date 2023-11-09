package musaico.foundation.structure;

import java.io.Serializable;


/**
 * <p>
 * A data Structure, which could be as simple as a single Field,
 * or more complex, containing multiple Fields in some particular
 * organization.
 * </p>
 *
 *
 * <p>
 * In Java every Structure must implement equals (), hashCode ()
 * and toString ().
 * </p>
 *
 * <p>
 * In Java every Structure must be Serializable in order
 * to play nicely over RMI.  However users of Structures must
 * be careful: the contents of a Structure might not be Serializable,
 * in which case exceptions will be generated when trying to
 * pass Structures over RMI.
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
 * @see musaico.foundation.!!!.MODULE#COPYRIGHT
 * @see musaico.foundation.!!!.MODULE#LICENSE
 */
public interface Structure
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
