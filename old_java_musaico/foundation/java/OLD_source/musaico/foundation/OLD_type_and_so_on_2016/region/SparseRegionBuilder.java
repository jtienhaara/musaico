package musaico.foundation.region;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * Creates sparse regions.
 * </p>
 *
 * <p>
 * The SparseRegionBuilder is not thread-safe.
 * If you need to build an SparseRegion from
 * multiple threads concurrently, implement the necessary
 * locking yourself, or you'll get hit by concurrent
 * modification exceptions.
 * </p>
 *
 *
 * <p>
 * In Java every SparseRegionBuilder must be Serializable
 * in order to play nicely over RMI.
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
public class SparseRegionBuilder
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( SparseRegionBuilder.class );


    /** The space for which this builder creates sparse regions. */
    private final Space space;

    /** The sub-Regions added so far. */
    private final List<Region> subRegions =
        new ArrayList<Region> ();

    /** Checks parameters and return values for us. */
    private final ObjectContracts objectContracts;


    /**
     * <p>
     * Creates a new SparseRegionBuilder for the specified space.
     * </p>
     *
     * @param space The space for which sparse regions will be built.
     *              Must not be null.
     */
    public SparseRegionBuilder (
                                Space space
                                )
    {
        this.space = space;

        this.objectContracts = new ObjectContracts ( this );
    }


    /**
     * <p>
     * Builds the SparseRegion that has been assembled in this builder.
     * <p>
     *
     * <p>
     * For example, if integer array Regions {0-3}, {16-32} and {42-69}
     * have been concatenated to an empty SparseRegionBuilder, then
     * this method will return the SparseRegion for array positions
     * { {0-3}, {16-32}, {42-69}}.
     * </p>
     *
     * <p>
     * If there were any errors while concatenating sub-regions,
     * then this method will always return a FailedRegionExpression
     * with the obgliation which was violated.
     * </p>
     *
     * @return A RegionExpression representing the newly created
     *         SparseRegion representing the sub-regions
     *         which were concatenated to this sparse region builder,
     *         or a FailedRegionExpression if there were any errors
     *         along the way.  Never null.
     */
    public RegionExpression build ()
    {
        final Region [] sub_regions = this.subRegions ();
        if ( sub_regions.length == 0 )
        {
            final RegionViolation violation =
                new RegionViolation ( RegionObligation.REGION_MUST_NOT_BE_EMPTY,
                                      this,
                                      this );
            final SparseRegion sparse_region =
                new NoSparseRegion ( violation );
            return sparse_region.expr ();
        }

        final SparseRegion sparse_region;
        try
        {
            sparse_region = new StandardSparseRegion ( space, sub_regions );
        }
        catch ( RegionViolation violation )
        {
            return new FailedRegionExpression ( violation );
        }

        return sparse_region.expr ();
    }


    /**
     * <p>
     * Appends the specified sub-Region to the end of this
     * SparseRegionBuilder.
     * </p>
     *
     * <p>
     * For example, if this SparseRegionBuilder is builing
     * integer array index sub-regions { 0 - 10 } and { 42 - 99 },
     * and if the region { 214 - 255 } is concatenated to the
     * end, then the end result will be a SparseRegionBuilder
     * ready to build a sparse region with sub-regions
     * { 0 - 10 }, { 42 - 99 } and { 214 - 255 }.
     * </p>
     *
     * @param sub_region The sub-Region to concatenate to the end
     *                   of this SparseRegionBuilder.  Must belong
     *                   to the same Space as this SparseRegionBuilder.
     *                   Must contain only Positions which come after
     *                   the end Position in this SparseRegionBuilder.
     *                   Must not be null.
     *
     * @return This SparseRegionBuilder.  Never null.
     */
    public SparseRegionBuilder concatenate (
                                            Region sub_region
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.objectContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                     sub_region );

        this.subRegions.add ( sub_region );

        return this;
    }


    /**
     * @return The sub-regions concatenated to this builder so far.
     *         Not thread-safe.
     *         Never null.  Never contains any null elements.
     */
    public Region [] subRegions ()
    {
        Region [] template = new Region [ 0 ];
        Region [] sub_regions = this.subRegions.toArray ( template );

        return sub_regions;
    }
}
