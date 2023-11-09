package musaico.foundation.wiring;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;
import musaico.foundation.contract.obligations.Parameter1;


/**
 * <p>
 * An input source for a Component to pull its data, either across a Wire
 * from another Component, or from Ground, and so on.
 * </p>
 *
 *
 * <p>
 * In Java every Ground must be Serializable in order to
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
public interface Ground<ELEMENT extends Object>
    extends WireIn<ELEMENT>, WireOut<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * Every Ground must implement:
     * @see musaico.foundation.wiring.WireIn#circuit()
     * @see musaico.foundation.wiring.WireOut#circuit()
     */

    /**
     * Every Ground must implement:
     * @see musaico.foundation.wiring.WireIn#dataClass()
     * @see musaico.foundation.wiring.WireOut#dataClass()
     */

    /**
     * Every Ground must override:
     * @see java.lang.Object#equals(java.lang.Object)
     */

    /**
     * <p>
     * Grounding this Ground has no effect.
     * </p>
     *
     * @return This Ground.  Never null.
     *
     * @see musaico.foundation.wiring.WireIn#ground()
     * @see musaico.foundation.wiring.WireOut#ground()
     */
    @Override
    public abstract Ground<ELEMENT> ground ()
        throws Return.NeverNull.Violation;


    /**
     * Every Ground must override:
     * @see java.lang.Object#hashCode()
     */

    /**
     * <p>
     * Always returns an empty array of data.
     * </p>
     *
     * @return 0 data elements.  Always empty.
     *         Never null.  Never contains any null elements.
     *
     * @see musaico.foundation.wiring.WireIn#pull()
     */
    @Override
    public abstract ELEMENT [] pull ()
        throws Return.NeverNull.Violation,
               Return.Length.AlwaysEquals.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Dumps data element(s) to this Ground.
     * </p>
     *
     * <p>
     * The default implementation discards the specified data.
     * However other Ground implementations might log it,
     * and so on.
     * </p>
     *
     * @param elements The data to push to this output Ground.
     *                 Must not be null.  Must not contain any
     *                 null elements.
     *
     * @return WireState.CLOSED.  Never null.
     *
     * @see musaico.foundation.wiring.WireOut#push(java.lang.Object[])
     */
    @Override
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public abstract WireState push (
            ELEMENT ... elements
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Return.NeverNull.Violation,
               Return.AlwaysEquals.Violation;


    /**
     * @return WireState.CLOSED.  Never null.
     *
     * @see musaico.foundation.wiring.WireIn#state()
     * @see musaico.foundation.wiring.WireOut#state()
     */
    @Override
    public abstract WireState state ()
        throws Return.NeverNull.Violation;


    /**
     * Every Ground must override toString () with a short description:
     * @see java.lang.Object#toString()
     */
}
