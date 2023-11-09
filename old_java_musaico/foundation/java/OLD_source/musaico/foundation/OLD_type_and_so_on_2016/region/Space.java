package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.order.Order;


/**
 * <p>
 * The space in which a Region, Position, Size, and so on resides.
 * </p>
 *
 * <p>
 * For example, Regions, Positions and Sizes describing arrays
 * might be described with non-negative integers, whereas the Regions,
 * Positions and Sizes describing a 3-dimensional volume might
 * be described with 3-tuples doube[] positions in space.
 * </p>
 *
 *
 * <p>
 * In Java, every Space must be Serializable in order to play nicely
 * over RMI.
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
 * @see musaico.foundation.region.MODULE#COPYRIGHT
 * @see musaico.foundation.region.MODULE#LICENSE
 */
public interface Space
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** The default order for a Space which can't be bothered
     *  creating its own useful order of positions
     *  is alphabetical by toString () (ASCii, not dictionary order).
     *  This isn't very useful, so each Space should define its
     *  own Order. */
    public static final Order<Position> ORDER_BY_POSITION_STRING =
        new PositionStringOrder ();


    /** No space at all.  There are no positions in this Space, so
     *  every Region is empty. */
    public static final Space NONE = new NoSpace ();


    public abstract Region all ()
        throws ReturnNeverNull.Violation;

    public abstract Region empty ()
        throws ReturnNeverNull.Violation;

    // Every Space must implement
    // java.lang.Object#equals(java.lang.Object).

    public abstract PositionExpression expr (
                                             Position position
                                             )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;

    public abstract RegionExpression expr (
                                           Region region
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;

    public abstract SizeExpression expr (
                                         Size size
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;

    public abstract PositionExpression from (
                                             Position that_position
                                             )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;

    public abstract RegionExpression from (
                                           Region that_region
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;

    public abstract SizeExpression from (
                                         Size that_size
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;

    // Every Space must implement java.lang.Object#hashCode().

    public abstract Position max ()
        throws ReturnNeverNull.Violation;

    public abstract Position min ()
        throws ReturnNeverNull.Violation;

    public abstract Size one ()
        throws ReturnNeverNull.Violation;

    public abstract Order<Position> order ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new Space, equal to this one in every way except
     * the sort order of its Positions, which is set to the
     * specified Order.
     * </p>
     *
     * <p>
     * For example, an integer Space sorted by ascending numeric order
     * might be reversed into an integer Space sorted by descending
     * numeric order by using this method.
     * </p>
     *
     * @param order The Order of the new Space.
     *              Must be capable of operating on the Positions
     *              supported by this Space.  Must not be null.
     *
     * @return A newly created Space, identical to this one in every
     *         way except the Order, which is set to the specified
     *         parameter.  Never null.
     */
    public abstract Space order (
                                 Order<Position> order
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    public abstract Position origin ()
        throws ReturnNeverNull.Violation;

    public abstract RegionExpression region (
                                             Position start,
                                             Position end
                                             )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    public abstract SparseRegionBuilder sparseRegionBuilder ()
        throws ReturnNeverNull.Violation;
}
