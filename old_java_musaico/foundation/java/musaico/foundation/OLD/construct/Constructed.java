package musaico.foundation.construct;

import java.io.Serializable;


/**
 * <p>
 * The housing for the final output Object produced by a (sequence of)
 * Constructor(s).
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
public interface Constructed<OUTPUT extends Object>
    extends Construct<OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The output Object that was constructed by some sequence of
     *         Constructor(s).  Never null, though there is no way to
     *         enforce non-null at runtime.
     */
    public abstract OUTPUT output ();
}
