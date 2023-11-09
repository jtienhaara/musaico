package musaico.foundation.region;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import musaico.foundation.condition.Partial;

import musaico.foundation.contract.Domain;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;
import musaico.foundation.order.Comparison;

import musaico.foundation.value.Multiple;


/**
 * <p>
 * A Region with holes in it.
 * </p>
 *
 * <p>
 * A SparseRegion is often the result of some kind of filtering
 * of another Region.  For example, if a Region covers all of the
 * integer array indices { 0, 1, ..., 10 }, then a filter might
 * return a SparseRegion matching only a few of the Positions, such
 * as { 2, 5, 6, 9 }.  The SparseRegion is implemented as a number\
 * of contiguous sub-Regions, such as { { 2 }, { 5, 6 }, { 9 } }.
 * This allows the user of a complex data structure to filter it
 * and access the filtered data live, without duplicating the
 * data.
 * </p>
 *
 *
 * <p>
 * In Java every Region must implement hashCode () and equals () in
 * order to play nicely with HashMaps.
 * </p>
 *
 * <p>
 * in Java every Region must be Serializable in order to
 * play nicely over RMI.
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
public class StandardSparseRegion
    extends Partial<Position, RegionViolation>
    implements SparseRegion, Multiple<Position, RegionViolation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( StandardSparseRegion.class );


    /** The Space to which this sparse regon and all its sub-regions belong. */
    private final Space space;

    /** The sorted Regions covered by this sparse array region.
     *  None of these is a sparse region! */
    private final Region [] subRegions;

    /** The start of the first sub-region (if any). */
    private final Position start;

    /** The end of the last sub-region (if any). */
    private final Position end;

    /** The size of this sub-region, NOT including holes. */
    private final Size size;

    /** The obligation that each requested sub-region must be between
     *  # 0 and # (num sub-regions - 1), inclusive. */
    private final RegionObligation<Integer> subRegionMustBeInBounds;


    /**
     * <p>
     * Creates a new StandardSparseRegion from the specified
     * Region(s).
     * </p>
     *
     * @param space The space to which the newly created sparse region
     *              and all its sub-regions belong.  Must not be null.
     *
     * @param sub_regions The sub-regions, possibly separated by holes,
     *                    which comprise this StandardSparseRegion.
     *                    May contain sparse regions.  May be unsorted.
     *                    This constructor takes care of flattening
     *                    the sub regions and sorting them.
     *                    Must not be null.  Must not contain any null
     *                    elements.
     */
    public StandardSparseRegion (
                                 Space space,
                                 Region [] sub_regions
                                 )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               RegionViolation
    {
        super ( Position.class,             // expected_class
                sub_regions [ 0 ].start (), // partial_value
                new RegionViolation ( RegionObligation.REGION_MUST_BE_SIZE_ONE,
                                      SparseRegion.class,
                                      sub_regions ),
                Position.NONE );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               space, sub_regions );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               sub_regions );

        final Domain<SpatialElement> domain_belong_to_space =
            new DomainElementBelongsToSpace ( space );
        final RegionObligation<SpatialElement> must_belong_to_space =
            new RegionObligation<SpatialElement> ( domain_belong_to_space );

        final List<Region> flattened_sub_regions =
            new ArrayList<Region> ();

        for ( Region sub_region : sub_regions )
        {
            classContracts.check ( must_belong_to_space,
                                   sub_region );

            if ( ! ( sub_region instanceof StandardSparseRegion ) )
            {
                // Flat Region.
                flattened_sub_regions.add ( sub_region );
            }
            else
            {
                SparseRegion sparse_sub_region =
                    (SparseRegion) sub_region;
                for ( long ssr = 0L;
                      ssr < sparse_sub_region.numRegions ();
                      ssr ++ )
                {
                    Region sub_sub_region =
                        sparse_sub_region.region ( ssr )
                        .orThrowChecked ();

                    classContracts.check ( must_belong_to_space,
                                           sub_sub_region );

                    flattened_sub_regions.add ( sub_sub_region );
                }
            }
        }

        // Sort the sub-regions and merge any that overlap.
        Collections.sort ( flattened_sub_regions, RegionOrder.DEFAULT );

        final List<Region> final_sub_regions = new ArrayList<Region> ();
        final Order<Position> order = space.order ();
        SizeExpression total_size = new ZeroSizeExpression ();
        Region last_sub_region = null;
        for ( Region sub_region : flattened_sub_regions )
        {
            if ( last_sub_region != null )
            {
                final Position start = sub_region.start ();
                final Position end = sub_region.end ();
                final Position last_start = last_sub_region.start ();
                final Position last_end = last_sub_region.end ();
                Comparison comparison =
                    order.compareValues ( start, last_end );
                if ( comparison.isOneOf ( Comparison.LEFT_LESS_THAN_RIGHT,
                                          Comparison.LEFT_EQUALS_RIGHT ) )
                {
                    // Overlapping regions.  Don't bother
                    // adding the previous one just yet.
                    comparison = order.compareValues ( last_end, end );
                    if ( comparison.isOneOf ( Comparison.LEFT_GREATER_THAN_RIGHT,
                                              Comparison.LEFT_EQUALS_RIGHT ) )
                    {
                        // Just ignore this sub-region completely,
                        // the previous one engulfs it.
                    }
                    else
                    {
                        // Create a new merged region from the last
                        // sub-region and this sub-region.
                        last_sub_region = space.region ( last_start, end )
                            .orThrowChecked ();
                    }
                }
                else
                {
                    // This sub_region and last_sub_region do not overlap.
                    // Add the last_sub_region to the finalized flattened
                    // list.
                    final_sub_regions.add ( last_sub_region );
                    total_size = total_size.add ( last_sub_region.size () );
                    last_sub_region = sub_region;
                }
            }
            else
            {
                // last_sub_region was null, nothing to merge yet.
                last_sub_region = sub_region;
            }
        }

        if ( last_sub_region != null )
        {
            final_sub_regions.add ( last_sub_region );
            total_size = total_size.add ( last_sub_region.size () );
        }

        this.space = space;

        // Now finalize the array of sub-regions.
        Region [] template =
            new Region [ final_sub_regions.size () ];
        this.subRegions = final_sub_regions.toArray ( template );

        if ( this.subRegions.length > 0 )
        {
            // At least 1 sub-region.
            this.start = this.subRegions [ 0 ].start ();
            this.end = this.subRegions [ this.subRegions.length - 1 ].end ();
            this.size = total_size.orThrowChecked ();
        }
        else
        {
            // Empty sparse region.
            this.start = Position.NONE;
            this.end = Position.NONE;
            this.size = Size.NONE;
        }

        final DomainSubRegionMustBeInBounds sub_regions_domain =
            new DomainSubRegionMustBeInBounds ( this );
        this.subRegionMustBeInBounds =
            new RegionObligation<Integer> ( sub_regions_domain );
    }


    /**
     * @see musaico.region.Region#contains(musaico.region.Position)
     */
    @Override
    public boolean contains (
                             Position position
                             )
    {
        // Binary search of the sub-regions.
        final Order<Position> order = this.space ().order ();
        int low = 0;
        int high = this.subRegions.length - 1;
        while ( low <= high )
        {
            int mid = ( low + high ) / 2;
            Region sub_region = this.subRegions [ mid ];
            Comparison start_comparison =
                order.compareValues ( position, sub_region.start () );
            if ( start_comparison == Comparison.LEFT_LESS_THAN_RIGHT )
            {
                // position < mid.start ().
                // Search the 1st 1/2 of remaining positions.
                high = mid - 1;
                continue;
            }
            else if ( start_comparison == Comparison.LEFT_EQUALS_RIGHT )
            {
                // position = mid.start ().
                return true;
            }
            else if ( start_comparison != Comparison.LEFT_LESS_THAN_RIGHT )
            {
                // position != mid.start () (incomparable etc).
                return false;
            }

            Comparison end_comparison =
                order.compareValues ( position, sub_region.end () );
            if ( end_comparison == Comparison.LEFT_GREATER_THAN_RIGHT )
            {
                // position > mid.end ().
                // Search the latter 1/2 of remaining positions.
                low = mid + 1;
                continue;
            }
            else if ( end_comparison == Comparison.LEFT_EQUALS_RIGHT )
            {
                // position = mid.end ().
                return true;
            }
            else if ( end_comparison != Comparison.LEFT_GREATER_THAN_RIGHT )
            {
                // position != mid.end () (incomparable etc).
                return false;
            }

            // At this point we know that
            // mid.start () < position < mid.end ()
            // So the position lies in this sub-region.
            return true;
        }

        // Note that at this point low > high.
        // The non-existent sub-region would fit just
        // before the low'th sub-region.
        return false;
    }


    /**
     * @see musaico.region.Region#end()
     */
    @Override
    public Position end ()
    {
        return this.end;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( ! ( obj instanceof Region ) )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }

        long num_regions = this.numRegions ();
        if ( ! ( obj instanceof StandardSparseRegion ) )
        {
            if ( num_regions > 1L )
            {
                return false;
            }

            Region that = (Region) obj;
            if ( this.start ().equals ( that.start () )
                 && this.end ().equals ( that.end () ) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        StandardSparseRegion that = (StandardSparseRegion) obj;
        if ( num_regions != that.numRegions () )
        {
            return false;
        }

        for ( long r = 0L; r < num_regions; r ++ )
        {
            Region this_sub_region = this.region ( r ).orNone ();
            Region that_sub_region = that.region ( r ).orNone ();
            if ( ! this_sub_region.equals ( that_sub_region ) )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * @see musaico.region.Region#expr()
     */
    @Override
    public RegionExpression expr ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new StandardRegionExpression ( this );
    }


    /**
     * @see musaico.region.Region#expr(musaico.region.Position)
     */
    @Override
    public PositionExpression expr (
                                    Position position
                                    )
    {
        try
        {
            return new RegionalPositionExpression ( position, this );
        }
        catch ( RegionViolation violation )
        {
            // Position does not belong to the correct Space, or somesuch.
            return new FailedPositionExpression ( violation );
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode ()
    {
        int hash_code = this.start ().hashCode ();
        hash_code += this.end ().hashCode ();
        hash_code += 72073L * this.numRegions ();
        hash_code = hash_code >> 32;
        return hash_code;
    }


    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Position> iterator ()
    {
        return new RegionalPositionIterator ( this );
    }


    /**
     * @return The number of contiguous sub-Regions contained in this
     *         SparseRegion.  Always 0 or greater.
     */
    public long numRegions ()
    {
        return (long) this.subRegions.length;
    }


    /**
     * <p>
     * Returns the index'th contiguous sub-Region.
     * </p>
     *
     * <p>
     * For example, if this is an integer array index SparseRegion
     * containing sub-Regions { 2 }, { 5, 6 } and { 9 }, then
     * calling <code> region ( 1 ) </code> will return the
     * { 5, 6 } sub-Region.
     * </p>
     *
     * @param index The index of the sub-Region to return, such as
     *              0, 1, 2, ...  Must be 0 or greater.  Must be
     *              less than the number of sub-Regions in this
     *              SparseRegion.
     *
     * @return A RegionExpression for the specified sub-Region,
     *         or a failed RegionExpression if the specified index
     *         is negative or greater than or equal to the number
     *         of sub-Regions in this Sparse Region.  Never null.
     */
    public RegionExpression region (
                                    long index
                                    )
    {
        if ( index < 0L
             || index >= (long) this.subRegions.length )
        {
            final RegionViolation violation =
                new RegionViolation ( this.subRegionMustBeInBounds,
                                      this,
                                      index );
            return new FailedRegionExpression ( violation );
        }

        final Region sub_region = this.subRegions [ (int) index ];
        return new StandardRegionExpression ( sub_region );
    }


    /**
     * @see musaico.foundation.region.Region#searcher(musaico.foundation.region.RegionExpression, musaico.foundation.region.Filter[])
     */
    @Override
    @SuppressWarnings("unchecked") // Heap pollution<generic varargs>
    public Searcher searcher (
                              RegionExpression region_expression,
                              Filter<Position> ... criteria
                              )
    {
        if ( this.subRegions.length > 0 )
        {
            return this.subRegions [ 0 ].searcher ( region_expression,
                                                    criteria );
        }
        else
        {
            final RegionObligation<Region> obligation =
                new RegionObligation<Region> ( new DomainRegionNonEmpty () );
            final RegionViolation violation =
                new RegionViolation ( obligation,
                                      this,
                                      this );
            return new FailedSearcher ( region_expression,
                                        criteria,
                                        violation );
        }
    }


    /**
     * @see musaico.region.Region#size()
     */
    @Override
    public Size size ()
    {
        return this.size;
    }


    /**
     * @see musaico.region.SpatialElement#space()
     */
    @Override
    public Space space ()
    {
        return this.space;
    }


    /**
     * @see musaico.region.Region#start()
     */
    @Override
    public Position start ()
    {
        return this.start;
    }


    /**
     * @see musaico.value.Value#toArray()
     */
    @Override
    public Position [] toArray ()
    {
        final List<Position> positions_list =
            new ArrayList<Position> ();
        for ( Position position : this )
        {
            positions_list.add ( position );
        }

        final Position [] template = new Position [ positions_list.size () ];
        final Position [] positions = positions_list.toArray ( template );

        return positions;
    }
}
