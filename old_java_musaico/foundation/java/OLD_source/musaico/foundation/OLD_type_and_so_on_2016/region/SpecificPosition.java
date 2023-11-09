package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.order.AbstractOrderedFilter;
import musaico.foundation.order.Comparison;
import musaico.foundation.order.Order;


/**
 * <p>
 * Searches for a specific position in Region, ordered by Position.
 * </p>
 *
 *
 * <p>
 * In Java every Filter must be Serializable in order
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
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.foundation.region.MODULE#COPYRIGHT
 * @see musaico.foundation.region.MODULE#LICENSE
 */
public class SpecificPosition
    extends AbstractOrderedFilter<Position>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( SpecificPosition.class );


    /** The specific position to search for. */
    private final Position specificPosition;

    /** The Order to search in.  Can be the default order of Positions
     *  in the space, or can be the specific order of a particular
     *  Region. */
    private final Order<Position> order;


    /**
     * <p>
     * Creates a new SpecificPosition to search for the specified
     * Position, ordered by the Position's default order.
     * </p>
     *
     * @param specific_position The position to search for.
     *                          Must not be null.
     */
    public SpecificPosition (
                             Position specific_position
                             )
        throws ParametersMustNotBeNull.Violation
    {
        this ( specific_position, specific_position.space ().order () );
    }


    /**
     * <p>
     * Creates a new SpecificPosition to search for the specified
     * Position, ordered by the specified order of Positions.
     * </p>
     *
     * @param specific_position The position to search for.
     *                          Must not be null.
     *
     * @param order The order of positions to search through.
     *              Must not be null.
     */
    public SpecificPosition (
                             Position specific_position,
                             Order<Position> order
                             )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               specific_position, order );

        this.specificPosition = specific_position;
        this.order = order;
    }


    /**
     * @return The order of Positions being searched.  Never null.
     */
    public Order<Position> order ()
    {
        return this.order;
    }


    /**
     * <p>
     * Returns the position being sought by this SpecificPosition
     * search criterion.
     * </p>
     *
     * @return The position being sought by this SpecificPosition
     *         search criterion.  Never null.
     */
    public Position position ()
    {
        return this.specificPosition;
    }


    /**
     * @see musaico.foundation.order.AbstractOrderedFilter#compare(java.lang.Object)
     */
    @Override
    public Comparison compare (
                               Position position
                               )
    {
        Position specific_position = this.position ();
        return this.order.compareValues ( position, specific_position );
    }
}
