package musaico.foundation.wiring;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;
import musaico.foundation.contract.obligations.Parameter1;


/**
 * <p>
 * An output sink for a Component to push output, either across a Wire
 * to another Component, or to Ground, and so on.
 * </p>
 *
 *
 * <p>
 * In Java every WireOut must be Serializable in order to
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
public interface WireOut<ELEMENT extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The parent Circuit to which this WireIn leads.
     *         Never null.
     */
    public abstract Circuit circuit ()
        throws Return.NeverNull.Violation;


    /**
     * <p>
     * Returns the class of data conducted via this WireOut.
     * </p>
     *
     * <p>
     * The dataClass could be more specific than what the Componet
     * requires for its input, depending on where the data is
     * coming from.  The dataClass is never more general than what
     * the Component expects.
     * </p>
     *
     * @return The class of data conducted through this WireOut.
     *         Never null.
     */
    public abstract Class<ELEMENT> dataClass ()
        throws Return.NeverNull.Violation;


    /**
     * Every WireOut must override:
     * @see java.lang.Object#equals(java.lang.Object)
     */


    /**
     * <p>
     * Ties this WireOut to the specified Circuit's ground, so that,
     * like /dev/null, pushing data will always return the CLOSED
     * state.
     * </p>
     *
     * <p>
     * Note that, unlike /dev/null, the cicuit's ground could
     * actually do things (logging and so on).  The default ground,
     * though, behaves as /dev/null.
     * </p>
     *
     * @return This WireOut.  Never null.
     */
    public abstract WireOut<ELEMENT> ground ()
        throws Return.NeverNull.Violation;


    /**
     * Every WireOut must override:
     * @see java.lang.Object#hashCode()
     */


    /**
     * <p>
     * Outputs data element(s) to this WireOut.
     * </p>
     *
     * <p>
     * Depending on the implementation (usually a combination of
     * this WireOut plus the Circuit backing it), this method can
     * block.  If you don't want to be blocked, check the
     * <code> pushState () </code> to ensure readiness before
     * calling <code> push ( ... ) </code>.
     * </p>
     *
     * @param elements The data to push to this output WireOut.
     *                 Must not be null.  Must not contain any
     *                 null elements.
     *
     * @return The current push state of this WireOut, which could
     *         be READY to receive data, or BLOCKED if the output goes
     *         to a target that is not yet ready to receive data,
     *         or CLOSED if the target will do nothing with any
     *         more data.  Never null.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public abstract WireState push (
            ELEMENT ... elements
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Return.NeverNull.Violation;


    /**
     * @return The current push state of this WireOut, which could
     *         be READY to receive data, or BLOCKED if the output goes
     *         to a target that is not yet ready to receive data,
     *         or CLOSED if the target will do nothing with any
     *         more data.  Never null.
     */
    public abstract WireState state ()
        throws Return.NeverNull.Violation;


    /**
     * Every WireOut must override toString () with a short description:
     * @see java.lang.Object#toString()
     */
}
