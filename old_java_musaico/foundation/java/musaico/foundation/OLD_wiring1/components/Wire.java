package musaico.foundation.wiring;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;
import musaico.foundation.contract.obligations.Parameter2;


/**
 * <p>
 * A conductor of data through the Components in a Board.
 * </p>
 *
 *
 * <p>
 * In Java every Wire must be Serializable in order to
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
 * @see musaico.foundation.wiring.MODULE#COPYRIGHT
 * @see musaico.foundation.wiring.MODULE#LICENSE
 */
public interface Wire<SOURCE extends Object, SINK extends SOURCE>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * Every Wire must override:
     * @see java.lang.Object#equals(java.lang.Object)
     */


    /**
     * Every Wire must override:
     * @see java.lang.Object#hashCode()
     */


    /**
     * @return The index of the Component in the parent Board
     *         that pulls the data conducted across this Wire.
     *         Always greater than or equal to 0 and less than
     *         the number of Components in the parent Board
     *         (though the latter must be enforced externally).
     */
    public abstract int sink ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * @return The index of the Component in the parent Board
     *         that pushes the data conducted across this Wire.
     *         Always greater than or equal to 0 and less than
     *         the number of Components in the parent Board
     *         (though the latter must be enforced externally).
     */
    public abstract int source ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * Every Wire must override toString () with a short description:
     * @see java.lang.Object#toString()
     */


    /**
     * <p>
     * Returns the class of data conducted in this Wire.
     * </p>
     *
     * <p>
     * Only elements of this wireClass can ever be
     * pushed into this Wire or pulled from it.
     * </p>
     *
     * <p>
     * The wireClass is always the same as the
     *  <code> wireOutClass () </code> of the <code> source () </code>
     * Component in the parent Board.
     * </p>
     *
     * <p>
     * The wireClass is either the same as, or more specific than, the
     *  <code> wireInClass () </code> of the <code> sink () </code>
     * Component in the parent Board.
     * </p>
     *
     * <p>
     * For example, if the source Component is capable of pushing
     * only Integer objects, but the sink Component can pull any
     * kind of Number objects, then the class of data travelling
     * across this Wire will always be Integers.
     * </p>
     *
     * <p>
     * (The reverse is not true: if the sink Component expects
     * Integers, then no Wire can be connected to it from a source
     * that can output any kind of Numbers.  In this case, only
     * an Integer source will do.)
     * </p>
     *
     * @return The class of data transmitted across this Wire.
     *         Never null.
     */
    public abstract Class<SOURCE> wireClass ()
        throws Return.NeverNull.Violation;
}
