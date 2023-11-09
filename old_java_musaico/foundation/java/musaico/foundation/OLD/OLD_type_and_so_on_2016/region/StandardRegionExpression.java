package musaico.foundation.region;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.condition.Successful;

import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Comparison;
import musaico.foundation.order.Order;


/**
 * <p>
 * Manipulates any region, performing set operations on them and
 * so on.
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
public class StandardRegionExpression
    extends Successful<Region, RegionViolation>
    implements RegionExpression, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( StandardRegionExpression.class );

    /** Used by scale (). */
    private static final BigDecimal HALF = new BigDecimal ( "0.5" );


    /** Checks parameters to methods for us. */
    private final ObjectContracts objectContracts;

    /** The first Region term of this expression. */
    private final Region region;

    /** Every operand must belong to th same Space as this expression's
     *  underlying Region. */
    private final RegionObligation<SpatialElement> mustBelongToSameSpace;


    /**
     * <p>
     * Creates a new StandardRegionExpression with the specified starting
     * term.
     * </p>
     *
     * @param region The first Region term of the expression.
     *               Must not be null.
     */
    public StandardRegionExpression (
                                     Region region
                                     )
    {
        super ( Region.class, region );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region );

        this.region = region;

        final Space space = this.region.space ();
        this.mustBelongToSameSpace =
            new RegionObligation<SpatialElement> ( new DomainElementBelongsToSpace ( space ) );

        this.objectContracts = new ObjectContracts ( this );
    }


    /**
     * @see musaico.foundation.region.RegionExpression#concatenate(musaico.foundation.region.Region)
     */
    @Override
    public RegionExpression concatenate (
                                         Region that
                                         )
    {
        try
        {
            this.objectContracts.check ( this.mustBelongToSameSpace,
                                         that );
        }
        catch ( RegionViolation violation )
        {
            return new FailedRegionExpression ( violation );
        }

        final Space space = this.region.space ();
        SparseRegionBuilder sparse = space.sparseRegionBuilder ();
        sparse.concatenate ( this.region );
        sparse.concatenate ( that );

        return sparse.build ();
    }


    /**
     * @see musaico.foundation.region.RegionExpression#difference(musaico.foundation.region.Region)
     */
    @Override
    public RegionExpression difference (
                                        Region that
                                        )
    {
        try
        {
            this.objectContracts.check ( this.mustBelongToSameSpace,
                                         that );
        }
        catch ( RegionViolation violation )
        {
            return new FailedRegionExpression ( violation );
        }

        final Space space = this.region.space ();
        Order<Position> order = space.order ();

        Position this_start = this.region.start ();
        Position this_end = this.region.end ();
        Position that_start = that.start ();
        Position that_end = that.end ();

        if ( order.compareValues ( this_end, that_start )
             .equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            // There is a gap between the end of this.region
            // and the start of that region.  Both regions are part
            // of the overall difference, so they must be returned
            // as a sparse array.
            return this.concatenate ( that );
        }

        if ( order.compareValues ( that_end, this_start )
             .equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            // There is a gap between the end of that region
            // and the start of this.region.  Both regions are part
            // of the overall difference, so they must be returned
            // as a sparse array.
            return that.expr ().concatenate ( this.region );
        }

        // There is some overlap between the regions.
        if ( this_start.equals ( that_start )
             && this_end.equals ( that_end ) )
        {
            // The 2 regions are identical.  No difference.
            final RegionObligation<Region> non_empty =
                new RegionObligation<Region> ( new DomainRegionDifferent ( this.region ) );
            final RegionViolation violation =
                new RegionViolation ( non_empty, this.region, that );
            return new EmptyRegionExpression ( new NoRegion ( violation ) );
        }

        // There is at least 1 non-empty region of difference, possibly 2.
        final Region region1;
        final Comparison start_comparison =
            order.compareValues ( this_start, that_start );
        if ( start_comparison.equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            // The region between this.region.start and that.start
            // is part of the difference.
            final Position start = this_start;
            final Position end;
            try
            {
                end = that_start.expr ().subtract ( space.one () )
                    .orThrowChecked ();
                region1 = space.region ( start, end )
                    .orThrowChecked ();
            }
            catch ( RegionViolation violation )
            {
                // No such position before that_start.
                return new FailedRegionExpression ( violation );
            }
        }
        else if ( start_comparison.equals ( Comparison.LEFT_GREATER_THAN_RIGHT ) )
        {
            // The region between that.start and this.region.start
            // is part of the difference.
            final Position start = that_start;
            final Position end;
            try
            {
                end = this_start.expr ().subtract ( space.one () )
                    .orThrowChecked ();
                region1 = space.region ( start, end )
                    .orThrowChecked ();
            }
            catch ( RegionViolation violation )
            {
                // No such position before that_start.
                return new FailedRegionExpression ( violation );
            }
        }
        else
        {
            // this_start == that_start (or incomparable).
            region1 = null;
        }

        final Region region2;
        final Comparison end_comparison =
            order.compareValues ( this_end, that_end );
        if ( end_comparison.equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            // The region between this.region.end and that.end
            // is part of the difference.
            final Position start;
            try
            {
                start = this_end.expr ().add ( space.one () )
                    .orThrowChecked ();
                final Position end = that_end;
                region2 = space.region ( start, end )
                    .orThrowChecked ();
            }
            catch ( RegionViolation violation )
            {
                // No such position after that_end.
                return new FailedRegionExpression ( violation );
            }
        }
        else if ( end_comparison.equals ( Comparison.LEFT_GREATER_THAN_RIGHT ) )
        {
            // The region between that.end and this.region.end
            // is part of the difference.
            final Position start;
            try
            {
                start = that_end.expr ().add ( space.one () )
                    .orThrowChecked ();
                final Position end = this_end;
                region2 = space.region ( start, end )
                    .orThrowChecked ();
            }
            catch ( RegionViolation violation )
            {
                // No such position after that_end.
                return new FailedRegionExpression ( violation );
            }
        }
        else
        {
            // this_start == that_start (or incomparable).
            region2 = null;
        }

        if ( region1 != null )
        {
            if ( region2 != null )
            {
                return region1.expr ().concatenate ( region2 );
            }
            else
            {
                return region1.expr ();
            }
        }
        else if ( region2 != null )
        {
            return region2.expr ();
        }
        else
        {
            // The regions are identical, no difference.
            final RegionObligation<Region> non_empty =
                new RegionObligation<Region> ( new DomainRegionDifferent ( region1 ) );
            final RegionViolation violation =
                new RegionViolation ( non_empty, region1, region2 );
            return new EmptyRegionExpression ( new NoRegion ( violation ) );
        }
    }


    /**
     * @see musaico.foundation.region.RegionExpression#exclude(musaico.foundation.region.Region)
     */
    @Override
    public RegionExpression exclude (
                                     Region that
                                     )
    {
        try
        {
            this.objectContracts.check ( this.mustBelongToSameSpace,
                                         that );
        }
        catch ( RegionViolation violation )
        {
            return new FailedRegionExpression ( violation );
        }

        final Space space = this.region.space ();
        Order<Position> order = space.order ();

        Position this_start = this.region.start ();
        Position this_end = this.region.end ();
        Position that_start = that.start ();
        Position that_end = that.end ();

        if ( order.compareValues ( this_end, that_start )
             .equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            // There is a gap between the end of this.region
            // and the start of that region.  Just return this
            // whole Region.
            return this;
        }

        if ( order.compareValues ( that_end, this_start )
             .equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            // There is a gap between the end of that region
            // and the start of this.region.  Just return this
            // whole region.
            return this;
        }

        // There is some overlap between the regions.
        if ( this_start.equals ( that_start )
             && this_end.equals ( that_end ) )
        {
            // The 2 regions are identical.  Nothing left after exclusion.
            final RegionObligation<Region> non_empty =
                new RegionObligation<Region> ( new DomainRegionDifferent ( this.region ) );
            final RegionViolation violation =
                new RegionViolation ( non_empty, this.region, that );
            return new EmptyRegionExpression ( new NoRegion ( violation ) );
        }

        // There is at least 1 non-empty region of exclusion, possibly 2.
        final Region region1;
        final Comparison start_comparison =
            order.compareValues ( this_start, that_start );
        if ( start_comparison.equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            // The region between this.region.start and that.start
            // is part of the exclusion.
            final Position start = this_start;
            final Position end;
            try
            {
                end = that_start.expr ().subtract ( space.one () )
                    .orThrowChecked ();
                region1 = space.region ( start, end )
                    .orThrowChecked ();
            }
            catch ( RegionViolation violation )
            {
                // No such position before that_start.
                return new FailedRegionExpression ( violation );
            }
        }
        else
        {
            // that_start <= this_start <= that_end (or incomparable).
            region1 = null;
        }

        final Region region2;
        final Comparison end_comparison =
            order.compareValues ( this_end, that_end );
        if ( end_comparison.equals ( Comparison.LEFT_GREATER_THAN_RIGHT ) )
        {
            // The region between that.end and this.region.end
            // is part of the exclusion.
            final Position start;
            try
            {
                start = that_end.expr ().add ( space.one () )
                    .orThrowChecked ();
                final Position end = this_end;
                region2 = space.region ( start, end )
                    .orThrowChecked ();
            }
            catch ( RegionViolation violation )
            {
                // No such position after that_end.
                return new FailedRegionExpression ( violation );
            }
        }
        else
        {
            // that_start <= this_end <= that_end (or incomparable).
            region2 = null;
        }

        if ( region1 != null )
        {
            if ( region2 != null )
            {
                return region1.expr ().concatenate ( region2 );
            }
            else
            {
                return region1.expr ();
            }
        }
        else if ( region2 != null )
        {
            return region2.expr ();
        }
        else
        {
            final RegionObligation<Region> non_empty =
                new RegionObligation<Region> ( new DomainRegionDifferent ( region1 ) );
            final RegionViolation violation =
                new RegionViolation ( non_empty, region1, region2 );
            return new EmptyRegionExpression ( new NoRegion ( violation ) );
        }
    }


    /**
     * @see musaico.foundation.region.RegionExpression#intersection(musaico.foundation.region.Region)
     */
    @Override
    public RegionExpression intersection (
                                          Region that
                                          )
    {
        try
        {
            this.objectContracts.check ( this.mustBelongToSameSpace,
                                         that );
        }
        catch ( RegionViolation violation )
        {
            return new FailedRegionExpression ( violation );
        }

        final Space space = this.region.space ();

        Position this_start = this.region.start ();
        Position this_end = this.region.end ();
        Position that_start = that.start ();
        Position that_end = that.end ();

        Order<Position> order = space.order ();

        if ( order.compareValues ( this_end, that_start )
             .equals ( Comparison.LEFT_LESS_THAN_RIGHT )
             || order.compareValues ( this_start, that_end )
             .equals ( Comparison.LEFT_GREATER_THAN_RIGHT ) )
        {
            // No overlap.
            final RegionObligation<Region> non_empty =
                new RegionObligation<Region> ( new DomainRegionNonEmpty () );
            final RegionViolation violation =
                new RegionViolation ( non_empty, this.region, that );
            return new EmptyRegionExpression ( new NoRegion ( violation ) );
        }
        else if ( this_start.equals ( that_start )
                  && this_end.equals ( that_end ) )
        {
            // Identical regions.
            return this;
        }

        // Some overlap.
        final Position start;
        if ( order.compareValues ( this_start, that_start )
             .isOneOf ( Comparison.LEFT_GREATER_THAN_RIGHT,
                        Comparison.LEFT_EQUALS_RIGHT ) )
        {
            // this.region.start >= that.start.
            start = this_start;
        }
        else
        {
            // this.region_start < that.start.
            start = that_start;
        }

        final Position end;
        if ( order.compareValues ( this_end, that_end )
             .isOneOf ( Comparison.LEFT_LESS_THAN_RIGHT,
                        Comparison.LEFT_EQUALS_RIGHT ) )
        {
            // this.region.end <= that.end.
            end = this_end;
        }
        else
        {
            // this.region.end > that.end.
            end = that_end;
        }

        return space.region ( start, end );
    }


    /**
     * @see musaico.foundation.region.RegionExpression#invert()
     */
    @Override
    public RegionExpression invert ()
    {
        final Space space = this.region.space ();
        // Just take the difference between the entire space
        // and this region.
        final Region space_region = space.all ();
        return space_region.expr ().exclude ( this.region );
    }


    /**
     * @see musaico.foundation.region.RegionExpression#scale(musaico.foundation.region.Size)
     */
    @Override
    public RegionExpression scale (
                                   Size size
                                   )
    {
        try
        {
            this.objectContracts.check ( this.mustBelongToSameSpace,
                                         size );
        }
        catch ( RegionViolation violation )
        {
            return new FailedRegionExpression ( violation );
        }

        final Space space = this.region.space ();

        final Size region_size = this.region.size ();
        if ( region_size.equals ( size ) )
        {
            // 1:1 scaling.
            return this;
        }

        final BigDecimal ratio =
            region_size.expr ().ratio ( size );
        if ( ratio.compareTo ( BigDecimal.ZERO ) <= 0 )
        {
            final RegionObligation<Region> non_empty =
                new RegionObligation<Region> ( new DomainRegionNonEmpty () );
            final RegionViolation violation =
                new RegionViolation ( non_empty, this.region, size );
            return new EmptyRegionExpression ( new NoRegion ( violation ) );
        }

        final Position start = this.region.start ();
        final Position end = this.region.end ();
        final Position scaled_start;
        final Position scaled_end;
        BigDecimal move_ratio = ( ratio.subtract ( BigDecimal.ONE ) )
            .multiply ( HALF );
        if ( move_ratio.compareTo ( BigDecimal.ZERO ) < 0 )
        {
            // Shrink.
            try
            {
                final Size move_size = region_size.expr ()
                    .multiply ( move_ratio.negate () ).orThrowChecked ();
                scaled_start = start.expr ().add ( move_size )
                    .orThrowChecked ();
                scaled_end = end.expr ().subtract ( move_size )
                    .orThrowChecked ();
            }
            catch ( RegionViolation violation )
            {
                // Failed to scale the size or tweak the start/end positions.
                return new FailedRegionExpression ( violation );
            }
        }
        else
        {
            // Enlarge.
            try
            {
                Size move_size = region_size.expr ().multiply ( move_ratio )
                    .orThrowChecked ();
                scaled_start = start.expr ().subtract ( move_size )
                    .orThrowChecked ();
                scaled_end = end.expr ().add ( move_size )
                    .orThrowChecked ();
            }
            catch ( RegionViolation violation )
            {
                // Failed to scale the size or tweak the start/end positions.
                return new FailedRegionExpression ( violation );
            }
        }

        // If the region was grown too large or shrunk too small
        // for its space then return a failed region expression.
        Position min = space.min ();
        Position max = space.max ();
        Order<Position> order = space.order ();
        if ( ( scaled_start instanceof NoPosition )
             || ( scaled_end instanceof NoPosition )
             || order.compare ( scaled_start, scaled_end ) > 0
             || order.compare ( scaled_start, min ) < 0
             || order.compare ( scaled_end, max ) > 0 )
        {
            // Too big / too small region.
            final RegionViolation violation =
                new RegionViolation ( this.mustBelongToSameSpace,
                                      this.region,
                                      size );
            return new FailedRegionExpression ( violation );
        }

        return space.region ( scaled_start, scaled_end );
    }


    /**
     * @see musaico.foundation.region.RegionExpression#search(musaico.foundation.region.Filter[])
     */
    @Override
    @SuppressWarnings("unchecked") // Heap pollution<generic varargs>
    public Searcher search (
                            Filter<Position> ... criteria
                            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.objectContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                     (Object []) criteria );
        this.objectContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                                     (Object []) criteria );

        // This region expression always uses the default
        // search strategy for the Space to which the region
        // belongs.
        final Searcher searcher = this.region.searcher ( this, criteria );
        return searcher;
    }


    /**
     * @see musaico.foundation.region.RegionExpression#sort(musaico.foundation.order.Order)
     */
    @Override
    public RegionExpression sort (
                                  Order<Position> order
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.objectContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                     order );

        final Space unsorted_space = this.region.space ();
        final Space sorted_space = unsorted_space.order ( order );
        final Region sorted_region;
        try
        {
            sorted_region = sorted_space.from ( this.region )
                .orThrowChecked ();
        }
        catch ( RegionViolation violation )
        {
            // Cannot convert this region into the sorted Space
            // for some reason.
            return new FailedRegionExpression ( violation );
        }

        final RegionExpression region_expr =
            sorted_region.expr ();
        return region_expr;
    }


    /**
     * @see musaico.foundation.region.RegionExpression#sparseRegionOrNone()
     */
    public SparseRegion sparseRegionOrNone ()
    {
        if ( this.region instanceof SparseRegion )
        {
            return (SparseRegion) this.region;
        }

        final Space space = this.region.space ();
        final SparseRegionBuilder sparse = space.sparseRegionBuilder ();
        sparse.concatenate ( this.region );
        final Region sparse_region = sparse.build ().orNone ();
        if ( sparse_region instanceof SparseRegion )
        {
            return (SparseRegion) sparse_region;
        }

        // Should not ever happen, but just in case there's a bug...
        final RegionObligation<Region> non_empty =
            new RegionObligation<Region> ( new DomainRegionNonEmpty () );
        final RegionViolation violation =
            new RegionViolation ( non_empty, this.region, sparse_region );
        return new NoSparseRegion ( violation );
    }


    /**
     * @see musaico.foundation.region.RegionExpression#splitBy(musaico.foundation.region.Size)
     */
    @Override
    public SparseRegion splitBy (
                                 Size size
                                 )
    {
        try
        {
            this.objectContracts.check ( this.mustBelongToSameSpace,
                                         size );
        }
        catch ( RegionViolation violation )
        {
            return new NoSparseRegion ( violation );
        }

        final Space space = this.region.space ();
        final SparseRegionBuilder sparse = space.sparseRegionBuilder ();
        if ( ! space.equals ( size.space () ) )
        {
            return sparse.build ().sparseRegionOrNone ();
        }

        if ( size instanceof NoSize )
        {
            return sparse.build ().sparseRegionOrNone ();
        }

        BigDecimal ratio = this.region.size ().expr ().ratio ( size );
        if ( ratio.compareTo ( BigDecimal.ONE ) <= 0 )
        {
            return sparse.concatenate ( this.region ).build ()
                .sparseRegionOrNone ();
        }

        Size one = space.one ();
        PositionExpression start_expr =
            this.region.expr ( this.region.start () );
        while ( start_expr instanceof Successful )
        {
            final PositionExpression next_start_expr =
                start_expr.add ( size );
            Position end;
            try
            {
                end = next_start_expr.subtract ( one ).orThrowChecked ();
            }
            catch ( RegionViolation violation )
            {
                end = this.region.end ();
            }

            final Position start = start_expr.orNone (); // We know it's Succ.
            final Region sub_region;
            try
            {
                sub_region = space.region ( start, end )
                    .orThrowChecked ();
            }
            catch ( RegionViolation violation )
            {
                // The Space didn't like those region endpoints.
                return new NoSparseRegion ( violation );
            }

            sparse.concatenate ( sub_region );

            start_expr = next_start_expr;
        }

        return sparse.build ().sparseRegionOrNone ();
    }


    /**
     * @see musaico.foundation.region.RegionExpression#splitInto(long)
     */
    @Override
    public SparseRegion splitInto (
                                   long into
                                   )
    {
        final Space space = this.region.space ();

        if ( into <= 0L )
        {
            return space.sparseRegionBuilder ().build ()
                .sparseRegionOrNone ();
        }
        else if ( this.region.equals ( space.empty () ) )
        {
            return space.sparseRegionBuilder ().build ()
                .sparseRegionOrNone ();
        }
        else if ( into == 1L
                  || this.region.size ().equals ( space.one () ) )
        {
            return space.sparseRegionBuilder ()
                .concatenate ( this.region )
                .build ()
                .sparseRegionOrNone ();
        }

        final BigDecimal into_bd = new BigDecimal ( "" + into );

        Size region_size = this.region.size ();
        final Size sub_size;
        Size extras;
        try
        {
            sub_size = region_size.expr ().divide ( into_bd )
                .orThrowChecked ();
            final Size subs_total = sub_size.expr ().multiply ( into_bd )
                .orThrowChecked ();
            extras = region_size.expr ().subtract ( subs_total )
                .orThrowChecked ();
        }
        catch ( RegionViolation violation )
        {
            // Failed to calculate one of the sizes.
            return new FailedSparseRegionBuilder ( violation )
                .build ()
                .sparseRegionOrNone ();
        }

        final Size one = space.one ();

        SparseRegionBuilder sparse =
            space.sparseRegionBuilder ();
        Position start = this.region.start ();
        for ( long sr = 0L; sr < into; sr ++ )
        {
            final Position end;
            final Position next_start;
            try
            {
                final Size this_sub_size;
                if ( ! ( extras instanceof NoSize ) )
                {
                    this_sub_size = sub_size.expr ().add ( one )
                        .orThrowChecked ();
                    extras = extras.expr ().subtract ( one )
                        .orThrowChecked ();
                }
                else
                {
                    this_sub_size = sub_size;
                }

                next_start = start.expr ().add ( this_sub_size )
                    .orThrowChecked ();
                end = next_start.expr ().subtract ( one )
                    .orThrowChecked ();
            }
            catch ( RegionViolation violation )
            {
                // Failed to scale the size or tweak the start/end positions.
                return new FailedSparseRegionBuilder ( violation )
                    .build ()
                    .sparseRegionOrNone ();
            }

            final Region sub_region;
            try
            {
                sub_region = space.region ( start, end )
                    .orThrowChecked ();
            }
            catch ( RegionViolation violation )
            {
                // The Space didn't like those region endpoints.
                return new NoSparseRegion ( violation );
            }

            sparse.concatenate ( sub_region );

            start = next_start;
        }

        return sparse.build ()
            .sparseRegionOrNone ();
    }


    /**
     * @see musaico.foundation.region.RegionExpression#union(musaico.foundation.region.Region)
     */
    @Override
    public RegionExpression union (
                                   Region that
                                   )
    {
        try
        {
            this.objectContracts.check ( this.mustBelongToSameSpace,
                                         that );
        }
        catch ( RegionViolation violation )
        {
            return new FailedRegionExpression ( violation );
        }

        final Space space = this.region.space ();

        Position this_start = this.region.start ();
        Position this_end = this.region.end ();
        Position that_start = that.start ();
        Position that_end = that.end ();

        Size one = space.one ();

        Order<Position> order = space.order ();

        if ( order.compareValues ( this_end,
                                   that_start.expr ().subtract ( one )
                                   .orDefault ( that_start ) )
             .equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            // this entire region, gap, that entire region.
            return this.concatenate ( that );
        }
        else if ( order.compareValues ( this_start,
                                        that_end.expr ().add ( one )
                                        .orDefault ( that_end ) )
                  .equals ( Comparison.LEFT_GREATER_THAN_RIGHT ) )
        {
            // that entire region, gap, this entire region.
            return that.expr ().concatenate ( this.region );
        }

        final Position start;
        if ( order.compareValues ( this_start, that_start )
             .isOneOf ( Comparison.LEFT_LESS_THAN_RIGHT,
                        Comparison.LEFT_EQUALS_RIGHT ) )
        {
            start = this_start;
        }
        else
        {
            start = that_start;
        }

        final Position end;
        if ( order.compareValues ( this_end, that_end )
             .isOneOf ( Comparison.LEFT_GREATER_THAN_RIGHT,
                        Comparison.LEFT_EQUALS_RIGHT ) )
        {
            end = this_end;
        }
        else
        {
            end = that_end;
        }

        return space.region ( start, end );
    }
}
