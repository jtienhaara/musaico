package musaico.foundation.region.search;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.condition.Successful;

import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.region.DomainRegionMatchingCriteria;
import musaico.foundation.region.DomainRegionNonEmpty;
import musaico.foundation.region.EmptyRegionExpression;
import musaico.foundation.region.FailedPositionExpression;
import musaico.foundation.region.FailedRegionExpression;
import musaico.foundation.region.NoRegion;
import musaico.foundation.region.Position;
import musaico.foundation.region.PositionExpression;
import musaico.foundation.region.Region;
import musaico.foundation.region.RegionExpression;
import musaico.foundation.region.RegionObligation;
import musaico.foundation.region.RegionViolation;
import musaico.foundation.region.Searcher;
import musaico.foundation.region.Space;
import musaico.foundation.region.SparseRegionBuilder;


/**
 * <p>
 * Performs a linear search for one or more Position(s) in
 * a RegionExpression.
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
public class LinearSearcher
    implements Searcher, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( LinearSearcher.class );


    /** The region expression in which to search. */
    private final RegionExpression regionExpression;

    /** The criteria to search by. */
    private final Filter<Position> [] criteria;


    /**
     * <p>
     * Creates a new LinearSearcher with the specified criteria, to
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
    @SuppressWarnings({"unchecked", // Heap pollution<generic varargs>
                "rawtypes"}) // Generic array creation
    public LinearSearcher (
                           RegionExpression region_expression,
                           Filter<Position> ... criteria
                           )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region_expression, criteria );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               criteria );

        this.regionExpression = region_expression;
        this.criteria = new Filter [ criteria.length ];
        System.arraycopy ( criteria, 0,
                           this.criteria, 0, criteria.length );
    }


    /**
     * @see musaico.foundation.region.Searcher#criteria()
     */
    @Override
    @SuppressWarnings({"rawtypes", // Generic array creation.
                "unchecked"}) // Implied generic array cast.
    public Filter<Position> [] criteria ()
    {
        final Filter<Position> [] criteria =
            new Filter [ this.criteria.length ];
        System.arraycopy ( this.criteria, 0,
                           criteria, 0, this.criteria.length );

        return criteria;
    }


    /**
     * @see musaico.foundation.region.Searcher#find()
     */
    @Override
    public PositionExpression find ()
    {
        final Region region = this.regionExpression.orNone ();

        for ( Position position : region )
        {
            boolean found = true;
            for ( Filter<Position> filter : this.criteria )
            {
                if ( filter.filter ( position ) != Filter.State.KEPT )
                {
                    found = false;
                    break;
                }
            }

            if ( found == true )
            {
                // All criteria matched.
                final PositionExpression expr = region.expr ( position );
                return expr;
            }
        }

        // Couldn't find any Position matching the criteria.
        final RegionObligation<Region> must_match_criteria =
            new RegionObligation<Region> ( new DomainRegionMatchingCriteria ( criteria ) );
        final RegionViolation violation =
            new RegionViolation ( must_match_criteria,
                                  this, region );

        return new FailedPositionExpression ( violation );
    }


    /**
     * @see musaico.foundation.region.Searcher#findAll()
     */
    @Override
    public RegionExpression findAll ()
    {
        final Region region = this.regionExpression.orNone ();
        final Space space = region.space ();
        List<Position[]> end_points = new ArrayList<Position[]> ();
        Position last_start = null;
        Position last_end = null;

        for ( PositionExpression current = region.expr ( region.start () );
              ( current instanceof Successful );
              current = current.next () )
        {
            final Position position = current.orNone ();
            boolean is_kept = true;
            for ( Filter<Position> criterion : this.criteria )
            {
                if ( criterion.filter ( position ) != Filter.State.KEPT )
                {
                    is_kept = false;
                    break;
                }
            }

            if ( is_kept == true )
            {
                if ( last_start == null )
                {
                    last_start = position;
                    last_end = position;
                }
                else
                {
                    last_end = position;
                }
            }
            else
            {
                if ( last_end != null )
                {
                    end_points.add ( new Position []
                        {
                            last_start,
                            last_end
                        } );
                    last_start = null;
                    last_end = null;
                }
            }
        }

        if ( last_end != null )
        {
            end_points.add ( new Position []
                {
                    last_start,
                    last_end
                } );
        }

        if ( end_points.size () == 0 )
        {
            // Everything was filtered out.
            final RegionObligation<Region> non_empty =
                new RegionObligation<Region> ( new DomainRegionNonEmpty () );
            final RegionViolation violation =
                new RegionViolation ( non_empty, this, region );
            return new EmptyRegionExpression ( new NoRegion ( violation ) );
        }
        else if ( end_points.size () == 1 )
        {
            // One contiguous region.
            Position start = end_points.get ( 0 ) [ 0 ];
            Position end = end_points.get ( 0 ) [ 1 ];

            return space.region ( start, end );
        }
        else
        {
            final SparseRegionBuilder sparse =
                space.sparseRegionBuilder ();
            for ( Position [] start_end : end_points )
            {
                final Position start = start_end [ 0 ];
                final Position end = start_end [ 1 ];
                final Region sub_region;
                try
                {
                    sub_region = space.region ( start, end )
                        .orThrowChecked ();
                }
                catch ( RegionViolation violation )
                {
                    // The Space didn't like those region end points.
                    return new FailedRegionExpression ( violation );
                }

                sparse.concatenate ( sub_region );
            }

            return sparse.build ();
        }
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
