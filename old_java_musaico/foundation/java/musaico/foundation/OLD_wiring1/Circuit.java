package musaico.foundation.wiring;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;
import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;


/**
 * <p>
 * The runtime data connections of one Component in one placement.
 * </p>
 *
 * <p>
 * Any given Component might be placed in multiple Boards,
 * and might be placed multiple times in a single Board.
 * Each placement has its own corresponding Circuit, that enables
 * data flow at runtime.
 * </p>
 *
 *
 * <p>
 * In Java every Circuit must be Serializable in order to
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
public interface Circuit
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The Component whose runtime data flow is accessed
     *         through this Circuit.  Never null.
     */
    public abstract Component component ()
        throws Return.NeverNull.Violation;


    /**
     * @return The unique identifier of this Circuit's Component,
     *         distinguishing it from other Components, as well as
     *         other placements of the same Component elsewhere
     *         in other Boards or in the same Board.  Never null.
     */
    public abstract String componentID ()
        throws Return.NeverNull.Violation;


    /**
     * Every Circuit must override:
     * @see java.lang.Object#equals(java.lang.Object)
     */


    /**
     * <p>
     * Creates or returns the existing Ground for the specified
     * data class.
     * </p>
     *
     * @param data_class The class of data that can flow on the
     *                   Ground wire.  Must not be null.
     *
     * @return The Ground wire for this Circuit.
     *         Data can be pushed to Ground to get rid of it,
     *         or data can be pulled from Ground to close
     *         a Component's input, and so on.
     *         The default Ground acts like /dev/null.
     *         However this behaviour can be overridden
     *         (for example, to log data that passes through).
     *         Never null.
     */
    public abstract <ELEMENT extends Object>
        Ground<ELEMENT> ground (
            Class<ELEMENT> data_class
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Return.NeverNull.Violation;


    /**
     * Every Circuit must override:
     * @see java.lang.Object#hashCode()
     */


    /**
     * <p>
     * Creates, or returns the existing, data WireIn(s) to the
     * specified input wire of the Component.
     * </p>
     *
     * @param wire_out The index of the Component's input wire,
     *                 indexing the Component's <code> wiresIn () </code>
     *                 classes.  Must be greater than or equal to 0
     *                 and less than the Component's
     *                 <code> wiresIn ().length </code>.
     *
     * @param data_class The class of data to be input from the WireIn(s).
     *                   Used for Java generics only.  Must equal
     *                   <code> component.wireIn () [ wire_in ] </code>.
     *                   Must not be null.
     *
     * @return 0 or more input WireIn(s) from which the Component
     *         can pull data.  Never null.  Never contains any
     *         null elements.
     */
    public abstract <ELEMENT extends Object>
        WireIn<ELEMENT> [] in (
            int wire_in,
            Class<ELEMENT> data_class
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter1.MustBeLessThan.Violation,
               Parameter2.MustEqual.Violation,
               Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Returns the number of data WireIn(s) tied to the Component's
     * specified input wire in this Circuit.
     * </p>
     *
     * @param wire_in The index of the Component's input wire,
     *                indexing the Component's <code> wiresIn () </code>
     *                classes.  Must be greater than or equal to 0
     *                and less than the Component's
     *                <code> wiresIn ().length </code>.
     *
     * @return The number of input WireIn(s) tied to the Component's specified
     *         input wire.  Always greater than or equal to 0.
     */
    public abstract int numIn ()
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter1.MustBeLessThan.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * <p>
     * Returns the number of data WireOut(s) tied to the Component's
     * specified output wire in this Circuit.
     * </p>
     *
     * @param wire_out The index of the Component's output wire,
     *                 indexing the Component's <code> wiresOut () </code>
     *                 classes.  Must be greater than or equal to 0
     *                 and less than the Component's
     *                 <code> wiresOut ().length </code>.
     *
     * @return The number of output WireOut(s) tied to the Component's specified
     *         output wire.  Always greater than or equal to 0.
     */
    public abstract int numOut (
            int wire_out
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter1.MustBeLessThan.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * <p>
     * Creates, or returns the existing, data WireOut(s) from the
     * specified output wire of the Component.
     * </p>
     *
     * @param wire_out The index of the Component's output wire,
     *                 indexing the Component's <code> wiresOut () </code>
     *                 classes.  Must be greater than or equal to 0
     *                 and less than the Component's
     *                 <code> wiresOut ().length </code>.
     *
     * @param data_class The class of data to be output to the WireOut(s).
     *                   Used for Java generics only.  Must equal
     *                   <code> component.wireOut () [ wire_out ] </code>.
     *                   Must not be null.
     *
     * @return 0 or more output WireOut(s) to which the Component
     *         can push data.  Never null.  Never contains any
     *         null elements.
     */
    public abstract <ELEMENT extends Object>
        WireOut<ELEMENT> [] out (
            int wire_out,
            Class<ELEMENT> data_class
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter1.MustBeLessThan.Violation,
               Parameter2.MustEqual.Violation,
               Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * Every Circuit must override toString () with a short description:
     * @see java.lang.Object#toString()
     */
}
