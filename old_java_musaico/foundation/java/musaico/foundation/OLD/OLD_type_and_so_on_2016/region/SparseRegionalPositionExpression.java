package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.order.Comparison;
import musaico.foundation.order.Order;


/**
 * <p>
 * Manipulates positions and creates sizes and
 * regions from them.
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
public class SparseRegionalPositionExpression
    extends RegionalPositionExpression
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( SparseRegionalPositionExpression.class );


    /** The position from which the expression started. */
    private final Position position;

    /** The region in which to perform the operations.
     *  Can be a Space.all () region (everything). */
    private final SparseRegion sparseRegion;

    /** The index of the sub-region within the sparse region
     *  which actually contains the position.  For example,
     *  if the Position is the end position of sub-region # 3,
     *  then next () will point to the start of sub-region # 4.
     *  And so on. */
    private final long subRegionIndex;


    /**
     * <p>
     * Creates a new PositionExpression, taking the
     * specified term as the start of the expression.
     * </p>
     *
     * @param position The position from which to start
     *                 the expression.  Must not be null.
     *
     * @param sparse_region The sparse region over which to perform
     *                      calculations for this expression.
     *                      Must not be null.  Must have the
     *                      same Space as the specified Position.
     *
     * @param sub_region_index The index of the sub-region within
     *                         the specified sparse region which
     *                         contains the specified position.
     *                         Must be greater than or equal to 0,
     *                         and less than
     *                         <code> sparse_region.numRegions () </code>.
     *                         The sub-region at the specified index
     *                         must actually contain the specified
     *                         position.
     */
    public SparseRegionalPositionExpression (
                                             Position position,
                                             SparseRegion sparse_region,
                                             long sub_region_index
                                             )
        throws ParametersMustNotBeNull.Violation,
               RegionViolation
    {
        // Throws exception if invalid position or region.
        super ( position, sparse_region );

        // !!!! contract checks;

        this.position = position;
        this.sparseRegion = sparse_region;
        this.subRegionIndex = sub_region_index;
    }


    /**
     * @see musaico.foundation.region.PositionExpression#next()
     */
    @Override
    public PositionExpression next ()
    {
        // Look in the current sub-region first.
        final Region sub_region =
            this.sparseRegion.region ( this.subRegionIndex )
            .orNone (); // We never expect problems here.
        // !!! Contract checks;
        final PositionExpression sub_next =
            sub_region.expr ( this.position ).next ();

        final RegionViolation violation1;
        try
        {
            final Position next_position =
                sub_next.orThrowChecked ();
            return new SparseRegionalPositionExpression ( next_position,
                                                          this.sparseRegion,
                                                          this.subRegionIndex );
        }
        catch ( RegionViolation v )
        {
            violation1 = v;
        }

        // Next, look in the next sub-region (if any).
        final long next_sub_region_index =
            this.subRegionIndex - 1L;
        if ( next_sub_region_index < 0L )
        {
            return new FailedPositionExpression ( violation1 );
        }

        final Region next_sub_region;
        try
        {
            next_sub_region =
                this.sparseRegion.region ( next_sub_region_index )
                .orThrowChecked ();
        }
        catch ( RegionViolation violation2 )
        {
            return new FailedPositionExpression ( violation2 );
        }

        final Position start_position =
            next_sub_region.start ();

        try
        {
            return new SparseRegionalPositionExpression ( start_position,
                                                          this.sparseRegion,
                                                          next_sub_region_index );
        }
        catch ( RegionViolation violation3 )
        {
            return new FailedPositionExpression ( violation3 );
        }
    }


    /**
     * @see musaico.foundation.region.PositionExpression#previous()
     */
    @Override
    public PositionExpression previous ()
    {
        // Look in the current sub-region first.
        final Region sub_region =
            this.sparseRegion.region ( this.subRegionIndex )
            .orNone (); // We never expect problems here.
        // !!! Contract checks;
        final PositionExpression sub_previous =
            sub_region.expr ( this.position ).previous ();

        final RegionViolation violation1;
        try
        {
            final Position previous_position =
                sub_previous.orThrowChecked ();
            return new SparseRegionalPositionExpression ( previous_position,
                                                          this.sparseRegion,
                                                          this.subRegionIndex );
        }
        catch ( RegionViolation v )
        {
            violation1 = v;
        }

        // Next, look in the previous sub-region (if any).
        final long previous_sub_region_index =
            this.subRegionIndex - 1L;
        if ( previous_sub_region_index < 0L )
        {
            return new FailedPositionExpression ( violation1 );
        }

        final Region previous_sub_region;
        try
        {
            previous_sub_region =
                this.sparseRegion.region ( previous_sub_region_index )
                .orThrowChecked ();
        }
        catch ( RegionViolation violation2 )
        {
            return new FailedPositionExpression ( violation2 );
        }

        final Position end_position =
            previous_sub_region.end ();

        try
        {
            return new SparseRegionalPositionExpression ( end_position,
                                                          this.sparseRegion,
                                                          previous_sub_region_index );
        }
        catch ( RegionViolation violation3 )
        {
            return new FailedPositionExpression ( violation3 );
        }
    }
}
