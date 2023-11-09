package musaico.region;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Comparison;
import musaico.io.Filter;
import musaico.io.FilterState;
import musaico.io.Order;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.RegionExpression;
import musaico.region.Size;
import musaico.region.SparseRegion;


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
 * <pre>
 * Copyright (c) 2011 Johann Tienhaara
 * All rights reserved.
 *
 * This file is part of Musaico.
 *
 * Musaico is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Musaico is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Musaico.  If not, see &lt;http://www.gnu.org/licenses/&gt;.
 * </pre>
 */
public class StandardRegionExpression
    implements RegionExpression, Serializable
{
    /** The first Region term of this expression. */
    private final Region region;


    /**
     * <p>
     * Creates a new StandardRegionExpression with the specified starting
     * term.
     * </p>
     *
     * @param region The first Region term of the expression.
     *               Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      specified are invalid
     *                                      (see above).
     */
    public StandardRegionExpression (
                                     Region region
                                     )
    {
        if ( region == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a region expression with starting term [%region%]",
                                                     "region", region );
        }

        this.region = region;
    }


    /**
     * @see musaico.region.RegionExpression#concatenate(musaico.region.Region)
     */
    @Override
    public SparseRegionBuilder concatenate (
                                            Region that
                                            )
    {
        final Space space = this.region.space ();
        SparseRegionBuilder sparse = space.sparseRegionBuilder ();
        sparse.concatenate ( this.region );

        if ( ! space.equals ( that.space () ) )
        {
            // Return the builder with only the first term
            // concatenated.
            return sparse;
        }

        sparse.concatenate ( that );

        return sparse;
    }


    /**
     * @see musaico.region.RegionExpression#difference(musaico.region.Region)
     */
    @Override
    public RegionExpression difference (
                                        Region that
                                        )
    {
        final Space space = this.region.space ();
        if ( ! space.equals ( that.space () ) )
        {
            return space.expr ( space.empty () );
        }

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
            Region sparse_region = space
                .expr ( this.region )
                .concatenate ( that )
                .build ();
            return space.expr ( sparse_region );
        }

        if ( order.compareValues ( that_end, this_start )
             .equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            // There is a gap between the end of that region
            // and the start of this.region.  Both regions are part
            // of the overall difference, so they must be returned
            // as a sparse array.
            Region sparse_region = space
                .expr ( that )
                .concatenate ( this.region )
                .build ();
            return space.expr ( sparse_region );
        }

        // There is some overlap between the regions.
        if ( this_start.equals ( that_start )
             && this_end.equals ( that_end ) )
        {
            // The 2 regions are identical.  No difference.
            return space.expr ( space.empty () );
        }

        // There is at least 1 non-empty region of difference, possibly 2.
        final Region region1;
        final Comparison start_comparison =
            order.compareValues ( this_start, that_start );
        if ( start_comparison.equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            // The region between this.region.start and that.start
            // is part of the difference.
            Position start = this_start;
            Position end = space.expr ( that_start )
                .subtract ( space.one () ).position ();
            region1 = space.region ( start, end );
        }
        else if ( start_comparison.equals ( Comparison.LEFT_GREATER_THAN_RIGHT ) )
        {
            // The region between that.start and this.region.start
            // is part of the difference.
            Position start = that_start;
            Position end = space.expr ( this_start )
                .subtract ( space.one () ).position ();
            region1 = space.region ( start, end );
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
            Position start = space.expr ( this_end )
                .add ( space.one () ).position ();
            Position end = that_end;
            region2 = space.region ( start, end );
        }
        else if ( end_comparison.equals ( Comparison.LEFT_GREATER_THAN_RIGHT ) )
        {
            // The region between that.end and this.region.end
            // is part of the difference.
            Position start = space.expr ( that_end )
                .add ( space.one () ).position ();
            Position end = this_end;
            region2 = space.region ( start, end );
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
                Region sparse_region = space
                    .expr ( region1 )
                    .concatenate ( region2 )
                    .build ();
                return space.expr ( sparse_region );
            }
            else
            {
                return space.expr ( region1 );
            }
        }
        else
        {
            return space.expr ( region2 );
        }
    }


    /**
     * @see musaico.region.RegionExpression#filter(musaico.io.Filter)
     */
    @Override
    public RegionExpression filter (
                                    Filter<Position> filter
                                    )
    {
        final Space space = this.region.space ();
        List<Position[]> end_points = new ArrayList<Position[]> ();
        Position last_start = null;
        Position last_end = null;

        Position out_of_bounds = space.outOfBounds ();
        for ( Position position = this.region.start ();
              ! position.equals ( out_of_bounds );
              position = this.region.expr ( position ).next () )
        {
            if ( filter.filter ( position ).equals ( FilterState.KEEP ) )
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
            return space.expr ( space.empty () );
        }
        else if ( end_points.size () == 1 )
        {
            // One contiguous region.
            Position start = end_points.get ( 0 ) [ 0 ];
            Position end = end_points.get ( 0 ) [ 1 ];

            return space.expr ( space.region ( start, end ) );
        }
        else
        {
            SparseRegionBuilder sparse =
                space.sparseRegionBuilder ();
            for ( Position [] start_end : end_points )
            {
                Position start = start_end [ 0 ];
                Position end = start_end [ 1 ];
                Region sub_region = space.region ( start, end );
                sparse.concatenate ( sub_region );
            }

            Region sparse_region = sparse.build ();
            return space.expr ( sparse_region );
        }
    }


    /**
     * @see musaico.region.RegionExpression#intersection(musaico.region.Region)
     */
    @Override
    public RegionExpression intersection (
                                          Region that
                                          )
    {
        final Space space = this.region.space ();
        if ( ! space.equals ( that.space () ) )
        {
            return space.expr ( space.empty () );
        }

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
            return space.expr ( space.empty () );
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
             .isIn ( Comparison.LEFT_GREATER_THAN_RIGHT,
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
             .isIn ( Comparison.LEFT_LESS_THAN_RIGHT,
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

        return space.expr ( space.region ( start, end ) );
    }


    /**
     * @see musaico.region.RegionExpression#invert()
     */
    @Override
    public RegionExpression invert ()
    {
        final Space space = this.region.space ();
        // Just take the difference between the entire space
        // and this region.
        Position min = space.min ();
        Position max = space.max ();
        Region space_region = space.region ( min, max );

        return this.difference ( space_region );
    }


    /**
     * @see musaico.region.RegionExpression#region()
     */
    @Override
    public Region region ()
    {
        return this.region;
    }


    /**
     * @see musaico.region.RegionExpression#scale(musaico.region.Size)
     */
    @Override
    public RegionExpression scale (
                                   Size size
                                   )
    {
        final Space space = this.region.space ();
        if ( ! space.equals ( size.space () ) )
        {
            return space.expr ( space.empty () );
        }

        final Size region_size = this.region.size ();
        if ( region_size.equals ( size ) )
        {
            // 1:1 scaling.
            return this;
        }

        final double ratio =
            space.expr ( region_size ).ratio ( size );
        if ( Double.isNaN ( ratio ) )
        {
            return space.expr ( space.empty () );
        }

        final Position start = this.region.start ();
        final Position end = this.region.end ();
        final Position scaled_start;
        final Position scaled_end;
        double move_ratio = ( ratio - 1.0D ) * 0.5;
        if ( move_ratio < 0D )
        {
            // Shrink.
            Size move_size = space.expr ( region_size )
                .multiply ( 0D - move_ratio ).size ();
            scaled_start = space.expr ( start )
                .add ( move_size ).position ();
            scaled_end = space.expr ( end )
                .subtract ( move_size ).position ();
        }
        else
        {
            // Enlarge.
            Size move_size = space.expr ( region_size )
                .multiply ( move_ratio ).size ();
            scaled_start = space.expr ( start )
                .subtract ( move_size ).position ();
            scaled_end = space.expr ( end )
                .add ( move_size ).position ();
        }

        // If the region was grown too large or shrunk too small
        // for its space then return the empty region.
        Position out_of_bounds = space.outOfBounds ();
        Order<Position> order = space.order ();
        if ( scaled_start.equals ( out_of_bounds )
             || scaled_end.equals ( out_of_bounds )
             || order.compareValues ( scaled_start, scaled_end )
             .equals ( Comparison.LEFT_GREATER_THAN_RIGHT ) )
        {
            return space.expr ( space.empty () );
        }

        Region scaled_region = space.region ( scaled_start, scaled_end );
        return space.expr ( scaled_region );
    }


    /**
     * @see musaico.region.RegionExpression#split(long)
     */
    @Override
    public SparseRegion split (
                               long into
                               )
    {
        final Space space = this.region.space ();

        if ( into <= 0L )
        {
            return space.sparseRegionBuilder ().build ();
        }
        else if ( this.region.equals ( space.empty () ) )
        {
            return space.sparseRegionBuilder ().build ();
        }
        else if ( into == 1L
                  || this.region.size ().equals ( space.one () ) )
        {
            return space.sparseRegionBuilder ()
                .concatenate ( this.region )
                .build ();
        }

        Size region_size = this.region.size ();
        Size sub_size = space.expr ( region_size )
            .divide ( (double) into ).size ();
        Size subs_total = space.expr ( sub_size )
            .multiply ( (double) into ).size ();
        Size extras = space.expr ( region_size )
            .subtract ( subs_total ).size ();
        final Size none = space.none ();
        final Size one = space.one ();

        SparseRegionBuilder sparse =
            space.sparseRegionBuilder ();
        Position start = this.region.start ();
        for ( long sr = 0L; sr < into; sr ++ )
        {
            final Size this_sub_size;
            if ( ! extras.equals ( none ) )
            {
                this_sub_size = space.expr ( sub_size ).add ( one ).size ();
                extras = space.expr ( extras ).subtract ( one ).size ();
            }
            else
            {
                this_sub_size = sub_size;
            }

            Position next_start = space.expr ( start )
                .add ( this_sub_size ).position ();
            Position end = space.expr ( next_start )
                .subtract ( one ).position ();

            Region sub_region = space.region ( start, end );
            sparse.concatenate ( sub_region );

            start = next_start;
        }

        return sparse.build ();
    }


    /**
     * @see musaico.region.RegionExpression#split(musaico.region.Size)
     */
    @Override
    public SparseRegion split (
                               Size size
                               )
    {
        final Space space = this.region.space ();
        SparseRegionBuilder sparse = space.sparseRegionBuilder ();
        if ( ! space.equals ( size.space () ) )
        {
            return sparse.build ();
        }

        Size none = space.none ();
        if ( size.equals ( none ) )
        {
            return sparse.build ();
        }

        double ratio = space.expr ( this.region.size () ).ratio ( size );
        if ( ratio <= 1D )
        {
            return sparse.concatenate ( this.region ).build ();
        }

        Size one = space.one ();
        Position out_of_bounds = space.outOfBounds ();
        Position start = this.region.start ();
        while ( ! start.equals ( out_of_bounds ) )
        {
            final Position next_start = space.expr ( start )
                .add ( size ).position ();
            final Position end;
            if ( ! next_start.equals ( out_of_bounds ) )
            {
                end = space.expr ( next_start ).subtract ( one ).position ();
            }
            else
            {
                end = this.region.end ();
            }

            Region sub_region = space.region ( start, end );
            sparse.concatenate ( sub_region );

            start = next_start;
        }

        return sparse.build ();
    }


    /**
     * @see musaico.region.RegionExpression#subtract(musaico.region.Region)
     */
    @Override
    public RegionExpression subtract (
                                      Region that
                                      )
    {
        final Space space = this.region.space ();
        if ( ! space.equals ( that.space () ) )
        {
            return space.expr ( space.empty () );
        }

        // Keep all of the elements of this.region which
        // are not also in that region.  (Unlike difference we
        // do not also keep the elements of that region which
        // are not in this.region.)
        Order<Position> order = space.order ();
        Position this_start = this.region.start ();
        Position this_end = this.region.start ();
        Position that_start = that.start ();
        Position that_end = that.start ();

        Size one = space.one ();

        final Region region1;
        if ( order.compareValues ( this_start, that_start )
             .equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            if ( order.compareValues ( this_end, that_start )
                 .equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
            {
                // This entire region comes before that one.
                region1 = this.region;
            }
            else
            {
                // Only part of this region comes before that one.
                Position start = this_start;
                Position end = space.expr ( that_start )
                    .subtract ( one ).position ();
                region1 = space.region ( start, end );
            }
        }
        else
        {
            // this.region starts on or after that region.  No elements
            // from this.region come before that region.
            region1 = null;
        }

        final Region region2;
        if ( order.compareValues ( this_end, that_end )
             .equals ( Comparison.LEFT_GREATER_THAN_RIGHT ) )
        {
            if ( order.compareValues ( this_start, that_end )
                 .equals ( Comparison.LEFT_GREATER_THAN_RIGHT ) )
            {
                // This entire region comes after that one.
                region2 = this.region;
            }
            else
            {
                // Only part of this region comes after that one.
                Position start = space.expr ( that_end )
                    .add ( one ).position ();
                Position end = this_end;
                region2 = space.region ( start, end );
            }
        }
        else
        {
            // this.region ends on or before that region.  No elements
            // from this.region come after that region.
            region2 = null;
        }

        if ( region1 != null )
        {
            if ( region2 != null )
            {
                Region sparse_region = space.sparseRegionBuilder ()
                    .concatenate ( region1 )
                    .concatenate ( region2 )
                    .build ();
                return space.expr ( sparse_region );
            }
            else
            {
                return space.expr ( region1 );
            }
        }
        else
        {
            return space.expr ( region2 );
        }
    }


    /**
     * @see musaico.region.RegionExpression#union(musaico.region.Region)
     */
    @Override
    public RegionExpression union (
                                   Region that
                                   )
    {
        final Space space = this.region.space ();
        if ( ! space.equals ( that.space () ) )
        {
            return space.expr ( space.empty () );
        }

        Position this_start = this.region.start ();
        Position this_end = this.region.end ();
        Position that_start = that.start ();
        Position that_end = that.end ();

        Size one = space.one ();

        Order<Position> order = space.order ();

        if ( order.compareValues ( this_end,
                                   space.expr ( that_start )
                                   .subtract ( one ).position () )
             .equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            // this entire region, gap, that entire region.
            Region sparse_region = space.sparseRegionBuilder ()
                .concatenate ( this.region )
                .concatenate ( that )
                .build ();
            return space.expr ( sparse_region );
        }
        else if ( order.compareValues ( this_start,
                                        space.expr ( that_end )
                                        .add ( one ).position () )
                  .equals ( Comparison.LEFT_GREATER_THAN_RIGHT ) )
        {
            // that entire region, gap, this entire region.
            Region sparse_region = space.sparseRegionBuilder ()
                .concatenate ( that )
                .concatenate ( this.region )
                .build ();
            return space.expr ( sparse_region );
        }

        final Position start;
        if ( order.compareValues ( this_start, that_start )
             .isIn ( Comparison.LEFT_LESS_THAN_RIGHT,
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
             .isIn ( Comparison.LEFT_GREATER_THAN_RIGHT,
                     Comparison.LEFT_EQUALS_RIGHT ) )
        {
            end = this_end;
        }
        else
        {
            end = that_end;
        }

        return space.expr ( space.region ( start, end ) );
    }
}
