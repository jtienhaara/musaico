package musaico.foundation.table;

import java.io.Serializable;


/**
 * <p>
 * !!!
 * </p>
 *
 *
 * <p>
 * In Java every Table must be Serializable in order
 * to play nicely over RMI.  Of course, be warned that
 * a Table serialized and transmitted over RMI is
 * a new Table, taken from a snapshot of the original.
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
public final interface Table
    extends View, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // Every Table must implement:
    // @see musaico.foundation.table.View#columns()

    public abstract Container<Index> indices ();

    // Every Table must implement:
    // @see musaico.foundation.table.View#rows()
}
