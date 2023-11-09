package musaico.foundation.table;

import java.io.Serializable;


/**
 * <p>
 * !!!
 * </p>
 *
 *
 * <p>
 * In Java every View must be Serializable in order
 * to play nicely over RMI.  Of course, be warned that
 * a View serialized and transmitted over RMI is
 * a new View, taken from a snapshot of the original.
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
 * For copyright and licensing information, please refer to:
 * </p>
 *
 * @see musaico.foundation.table.MODULE#COPYRIGHT
 * @see musaico.foundation.table.MODULE#LICENSE
 */
public final interface View
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    public abstract Container<Column<?>> columns ();

    public abstract Container<Row> rows ();
}
