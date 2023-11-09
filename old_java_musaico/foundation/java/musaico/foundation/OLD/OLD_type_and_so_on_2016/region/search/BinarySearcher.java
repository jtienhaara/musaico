package musaico.foundation.region.search;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.condition.Successful;

import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.OrderedFilter;
import musaico.foundation.order.Comparison;

import musaico.foundation.region.DomainRegionMatchingCriteria;
import musaico.foundation.region.FailedPositionExpression;
import musaico.foundation.region.FailedRegionExpression;
import musaico.foundation.region.Position;
import musaico.foundation.region.PositionExpression;
import musaico.foundation.region.Region;
import musaico.foundation.region.RegionExpression;
import musaico.foundation.region.RegionObligation;
import musaico.foundation.region.RegionViolation;
import musaico.foundation.region.Searcher;
import musaico.foundation.region.Size;
import musaico.foundation.region.Space;


/**
 * <p>
 * Performs a binary search for one specific Position in
 * a Region.
 * </p>
 *
 *
 * <p>
 * In Java, every Search must be Serializable in order to play
 * nicely over RMI.
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
 * @see musaico.foundation.region.search.MODULE#COPYRIGHT
 * @see musaico.foundation.region.search.MODULE#LICENSE
 */
public class BinarySearcher
    implements Searcher, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** 0.5 in BigDecimal format. */
    private static final BigDecimal HALF = new BigDecimal ( "0.5" );

    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( BinarySearcher.class );


    /** The region expression in which to search. */
    private final RegionExpression regionExpression;

    /** The criteria to search by. */
    private final OrderedFilter<Position> criterion;


    /**
     * <p>
     * Creates a new BinarySearcher with the specified criteria, to
     * search through the specified region expression.
     * </p>
     *
     * @param region_expression The RegionExpression to search through.
     *                          Must not be null.
     *
     * @param criteria The Filter criteria to search for,
     *                 such as a specific position, or the position at
     *                 which some datum is stored, and so on.
     *                 Must contain at least one Filter.
     *                 Must not be null.  Must not contain any null elements.
     */
    public BinarySearcher (
                           RegionExpression region_expression,
                           OrderedFilter<Position> criterion
                           )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region_expression, criterion );

        this.regionExpression = region_expression;
        this.criterion = criterion;
    }


    /**
     * @see musaico.foundation.region.Searcher#criteria()
     */
    @Override
    @SuppressWarnings({"rawtypes", // Generic array creation.
                "unchecked"}) // Implied generic array cast.
    public Filter<Position> [] criteria ()
    {
        return new Filter [] { this.criterion };
    }


    /**
     * @see musaico.foundation.region.Searcher#find()
     */
    @Override
    public PositionExpression find ()
    {
        final Region region = this.regionExpression.orNone ();

        Position low = region.start ();
        Position high = region.end ();
        Space space = region.space ();
        Position found = Position.NONE;
        while ( space.order ().compareValues ( low, high ).isOneOf ( Comparison.LEFT_LESS_THAN_RIGHT,
                                                                     Comparison.LEFT_EQUALS_RIGHT ) )
        {
            Size half_size = space.expr ( high )
                .subtract ( low )
                .multiply ( HALF ).orNone ();
            Position mid = space.expr ( low ).add ( half_size ).orNone ();
            if ( ! region.contains ( mid ) )
            {
                // The midpoint fell in the hole of a sparse
                // region.  Step it up by 1 to get out of
                // the hole.
                mid = region.expr ( mid ).next ().orNone ();
            }

            Comparison comparison = this.criterion.compare ( mid );
            if ( comparison == Comparison.LEFT_GREATER_THAN_RIGHT )
            {
                // mid > criterion.
                // Search the 1st 1/2 of remaining positions.
                high = region.expr ( mid ).previous ().orNone ();
            }
            else if ( comparison == Comparison.LEFT_LESS_THAN_RIGHT )
            {
                // mid < criterion.
                // Search the latter 1/2 of remaining positions.
                low = region.expr ( mid ).next ().orNone ();
            }
            else if ( comparison == Comparison.LEFT_EQUALS_RIGHT )
            {
                // mid == criterion.
                found = mid;
                break;
            }
            else
            {
                // Incomparable.  Not much more we can do!
                found = Position.NONE;
                break;
            }
        }

        // Either we found the position, or it's still set
        // to Position.NONE
        if ( found == Position.NONE )
        {
            final RegionObligation<Region> must_match_criteria =
                new RegionObligation<Region> ( new DomainRegionMatchingCriteria ( this.criteria () ) );
            final RegionViolation violation =
                new RegionViolation ( must_match_criteria, this, region );

            return new FailedPositionExpression ( violation );
        }
        else
        {
            return found.expr ();
        }
    }


    /**
     * @see musaico.foundation.region.Searcher#findAll()
     */
    @Override
    public RegionExpression findAll ()
    {
        final Region region = this.regionExpression.orNone ();

        // Binary search can't be used to find more than one position.
        PositionExpression middle_position_expression = this.find ();

        if ( ! ( middle_position_expression instanceof Successful ) )
        {
            final RegionObligation<Region> must_match_criteria =
                new RegionObligation<Region> ( new DomainRegionMatchingCriteria ( this.criteria () ) );
            final RegionViolation violation =
                new RegionViolation ( must_match_criteria, this, region );

            return new FailedRegionExpression ( violation );
        }

        final Position middle = middle_position_expression.orNone ();

        // Search backward from the middle Position to find the first
        // Position matching the criterion.
        Position first = middle;
        PositionExpression previous = region.expr ( middle ).previous ();
        while ( previous instanceof Successful
                && this.criterion.filter ( previous.orNone () ) == Filter.State.KEPT )
        {
            first = previous.orNone ();
            previous = previous.previous ();
        }

        // Search forward from the middle Position to find the last
        // Position matching the criterion.
        Position last = middle;
        PositionExpression next = region.expr ( middle ).next ();
        while ( next instanceof Successful
                && this.criterion.filter ( next.orNone () ) == Filter.State.KEPT )
        {
            last = next.orNone ();
            next = next.next ();
        }

        Space space = middle.space ();

        return space.region ( first, last );
    }


    /**
     * @see musaico.foundation.region.Searcher#regionSearchArea()
     */
    @Override
    public RegionExpression regionSearchArea ()
    {
        return this.regionExpression;
    }
}
