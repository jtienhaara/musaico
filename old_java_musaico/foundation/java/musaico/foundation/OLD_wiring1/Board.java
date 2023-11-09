package musaico.foundation.wiring;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;
import musaico.foundation.contract.obligations.Parameter1;

import musaico.foundation.contract.guarantees.Return;


/**
 * <p>
 * A network of Components that can be used as a structure, graph,
 * flowchart, and so on.
 * </p>
 *
 * <p>
 * A wiring Board can be used as the basis for a state machine,
 * or to map out relationships between objects, and so on.
 * </p>
 *
 *
 * <p>
 * In Java every Board must be Serializable in order to
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
public interface Board
    extends Iterable<Component>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Returns the requested Component from this Board, or throws
     * a range violation if the specified index is less than 0
     * or greater than or equal to this Board's number of Components.
     * </p>
     *
     * @param index The index number of the Component to return.
     *              0 for the first Component on this Board,
     *              ( <code> size () </code> - 1 ) for the last.
     *              Must be greater than or equal to 0.
     *              Must be less than <code> size () </code>.
     *
     * @return The specified Component of this Board.
     *         Must be greater than or equal to 0.
     *         Must be less than this Board's number of Components.
     *         Never null.
     */
    public abstract Component component (
            int index
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter1.MustBeLessThan.Violation;


    /**
     * Every Board must implement:
     * @see java.lang.Object#equals(java.lang.Object)
     */


    /**
     * Every Board must implement:
     * @see java.lang.Object#hashCode()
     */


    !!!!!!!!!!!!!;
    /**
     * <p>
     * Returns the index(ices) of the requested Component(s) from this Board.
     * </p>
     *
     * @param name The index(ices) of the Component(s) to return.
     *             Must not be null.
     *
     * @return The index(ices) of the Component(s) with the
     *         specified name, if any exist in this Board.
     *         Can be empty, if no such Components are members of this Board.
     *         Never null.  Each returned index is always greater
     *         than or equal to 0, and less than <code> size () </code>.
     */
    public abstract int [] indexOf (
            String name
            )
        throws Return.NeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation,
               Return.AlwaysLessThan.Violation;


    /**
     * <p>
     * Returns the requested Component(s) from this Board, by name.
     * </p>
     *
     * @param name The name of the Component(s) to return.
     *             Must not be null.
     *
     * @return The index(ices) of the Component(s) with the
     *         specified name, if any exist in this Board.
     *         Can be empty, if no such Components are members of this Board.
     *         Never null.  Each returned index is always greater
     *         than or equal to 0, and less than <code> size () </code>.
     */
    public abstract int [] indexOf (
            String name
            )
        throws Return.NeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation,
               Return.AlwaysLessThan.Violation;


    /**
     * @return The name of this Board.  Not necessarily unique.
     *         (Note to implementers: the more Boards with
     *         the same name in in a single Circuit, the harder it is
     *         to find the one you're looking for.  Try to give
     *         each Board a short, distinctive name that stands
     *         a chance of remaining unique.)  Never null.
     */
    public abstract String name ()
        throws Return.NeverNull.Violation;


    /**
     * @return The number of Components in this Board.
     *         Always 2 or greater.
     */
    public abstract int size ()
        throws Return.AlwaysGreaterThanOne.Violation;


    /**
     * @return The indices of the input / output Terminal(s)
     *         to / from this Board.  Can be empty,
     *         although a Board with no Terminals is probably
     *         not very useful unless it is the root Board.
     *         Never null.  Never contains any null elements.
     */
    public abstract int [] terminals ()
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * Every Board must implement toString () with a short description:
     * @see java.lang.Object#toString()
     */


    /**
     * @return A detailed description of this Board, in human-readable
     *         form.  Can contain newlines and so on.  Never null.
     */
    public abstract String toStringDetails ()
        throws Return.NeverNull.Violation;
}
