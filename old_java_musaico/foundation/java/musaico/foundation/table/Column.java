package musaico.foundation.table;

import java.io.Serializable;


/**
 * <p>
 * !!!
 * </p>
 *
 *
 * <p>
 * In Java every Column must be Serializable in order
 * to play nicely over RMI.
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
public final interface Column<ROW extends Row, VALUE extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    public abstract Class<VALUE> columnClass ();

    /**
     * <p>
     * Alias for <code> value ( row ) </code>.
     * </p>
     *
     * @see musaico.foundation.table.Column#value(musaico.foundation.table.Row)
     */
    public abstract VALUE from (
            ROW row
            );

    /**
     * <p>
     * Alias for <code> value ( row, as_class ) </code>.
     * </p>
     *
     * @see musaico.foundation.table.Column#value(musaico.foundation.table.Row, java.lang.Class)
     */
    public abstract AS_VALUE from (
            ROW row,
            Class<AS_VALUE> as_class
            );

    public abstract String name ();

    public abstract Filter<VALUE> type ();

    public abstract VALUE value (
            ROW row
            );

    public abstract AS_VALUE value (
            ROW row,
            Class<AS_VALUE> as_class
            );
}
