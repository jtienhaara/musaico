package musaico.foundation.wiring;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;


/**
 * <p>
 * An input source for a Component to pull its data, either across a Wire
 * from another Component, or from Ground, and so on.
 * </p>
 *
 *
 * <p>
 * In Java every WireIn must be Serializable in order to
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
public interface WireIn<ELEMENT extends Object>
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
     * Returns the class of data conducted via this WireIn.
     * </p>
     *
     * <p>
     * The dataClass could be more specific than what the Componet
     * requires for its input, depending on where the data is
     * coming from.  The dataClass is never more general than what
     * the Component expects.
     * </p>
     *
     * @return The class of data conducted through this WireIn.
     *         Never null.
     */
    public abstract Class<ELEMENT> dataClass ()
        throws Return.NeverNull.Violation;


    /**
     * Every WireIn must override:
     * @see java.lang.Object#equals(java.lang.Object)
     */


    /**
     * <p>
     * Ties this WireIn to its parent Circuit's ground, so that,
     * like /dev/null, pulling data will always return the empty array.
     * </p>
     *
     * <p>
     * Note that, unlike /dev/null, the cicuit's ground could
     * actually do things (logging and so on).  The default ground,
     * though, behaves as /dev/null.
     * </p>
     *
     * @return This WireIn.  Never null.
     */
    public abstract WireIn<ELEMENT> ground ()
        throws Return.NeverNull.Violation;


    /**
     * Every WireIn must override:
     * @see java.lang.Object#hashCode()
     */


    /**
     * <p>
     * Requests any and all data that can be conducted from this
     * WireIn.
     * </p>
     *
     * <p>
     * Depending on the implementation (usually a combination of
     * this WireIn plus the Circuit backing it), this method can
     * block.  If you don't want to be blocked, check the
     * <code> state () </code> to ensure readiness before
     * calling <code> pull () </code>.
     * </p>
     *
     * @return 0 or more data elements.  Can be empty.
     *         Never null.  Never contains any null elements.
     */
    public abstract ELEMENT [] pull ()
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * @return The current pull state of this WireIn, which could
     *         be READY to pull data, or BLOCKED if the input has
     *         to come from another WireIn that is not yet ready,
     *         or CLOSED if there is no more data to pull, and so on.
     *         Never null.
     */
    public abstract WireState state ()
        throws Return.NeverNull.Violation;


    /**
     * Every WireIn must override toString () with a short description:
     * @see java.lang.Object#toString()
     */
}
