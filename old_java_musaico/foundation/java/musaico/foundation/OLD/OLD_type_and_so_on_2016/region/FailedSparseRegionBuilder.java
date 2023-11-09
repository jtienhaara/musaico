package musaico.foundation.region;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Always creates a NoSparseRegion, because of some contract violation.
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
public class FailedSparseRegionBuilder
    extends SparseRegionBuilder
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( FailedSparseRegionBuilder.class );


    /** The region violation which led to this failed sparse region builder. */
    private final RegionViolation violation;


    /**
     * <p>
     * Creates a new FailedSparseRegionBuilder caused by the specified
     * contract violation.
     * </p>
     *
     * @param violation The RegionViolation which caused this
     *                  failed sparse region builder in the first
     *                  place.  Must not be null.
     */
    public FailedSparseRegionBuilder (
                                      RegionViolation violation
                                      )
    {
        super ( Space.NONE );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               violation );

        this.violation = violation;
    }


    /**
     * @see musaico.foundation.region.SparseRegionBuilder#build()
     */
    @Override
    public RegionExpression build ()
    {
        // Never do anything, this sparse region builder failed
        // a long time ago.
        return new FailedRegionExpression ( this.violation );
    }
}
