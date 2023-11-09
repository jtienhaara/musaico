package musaico.foundation.wiring;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;


/**
 * <p>
 * One point in a wiring Board: either a discrete Component,
 * or a complete Chip, containing an internal Board of its own.
 * </p>
 *
 *
 * <p>
 * In Java every Component must be Serializable in order to
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
public interface Component
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Applies the specified Circuit to this Component, performing
     * any logic, pulling data, pushing data, and so on.
     * </p>
     *
     * <p>
     * A Component might store some data then pass the input to its
     * output.  Or it might filter out the data, not passing out
     * any output.  Or it might not require any input, generating
     * output from nothing.  And so on.
     * </p>
     *
     * <p>
     * If this Component is not able to conduct data in the given Circuit,
     * it must immediately return <code>WireState.BLOCKING</code>.
     * </p>
     *
     * @param circuit The Wires and ground of the full Circuit context
     *                in which to run this Component.  The Circuit
     *                contains this Component's wireIn () inputs,
     *                wireOut () outputs, ground (), and so on.
     *                Must not be null.
     *
     * @return The WireState of this Component, such as BLOCKED and waiting
     *         for its connections to be READY, or READY to pull more
     *         input and/or push more output, or END for unable
     *         to pull any more input or push any more output, and so on.
     *         Never null.
     */
    public abstract WireState conduct (
            Circuit circuit
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Return.NeverNull.Violation;


    /**
     * Every Component must override:
     * @see java.lang.Object#equals(java.lang.Object)
     */


    /**
     * Every Component must override:
     * @see java.lang.Object#hashCode()
     */


    /**
     * @return The name of this Component.  Not necessarily unique.
     *         (Note to implementers: the more Components with
     *         the same name in in a single Circuit, the harder it is
     *         to find the one you're looking for.  Try to give
     *         each Component a short, distinctive name that stands
     *         a chance of remaining unique.)  Never null.
     */
    public abstract String name ()
        throws Return.NeverNull.Violation;


    /**
     * Every Component must override toString () with a short description:
     * @see java.lang.Object#toString()
     */


    /**
     * @return The classes of data that can be accepted on the
     *         input wires to this Component.  Never null.
     *         Never contains any null elements.
     */
    public abstract Class<?> [] wiresIn ()
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * @return The classes of data that can be accepted on the
     *         output wires from this Component.  Never null.
     *         Mever contains any null elements.
     */
    public abstract Class<?> [] wiresOut ()
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;
}
