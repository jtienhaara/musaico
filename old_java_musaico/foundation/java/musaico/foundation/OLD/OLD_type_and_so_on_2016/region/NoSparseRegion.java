package musaico.foundation.region;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * No SparseRegion at all (no sub-Regions and no holes).
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
public class NoSparseRegion
    extends NoRegion
    implements SparseRegion, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( NoSparseRegion.class );


    /**
     * <p>
     * Creates a new NoSparseRegion caused by the specified violation
     * (such as "start/end position is out of bounds" or
     * "start position must be before end position" and so on).
     * </p>
     *
     * @param violation The RegionViolation which led to the
     *                  creation of this NoSparseRegion.
     *                  Must not be null.
     */
    public NoSparseRegion (
                           RegionViolation violation
                           )
    {
        super ( violation );
    }


    /**
     * @see musaico.foundation.region.SparseRegion#numRegions()
     */
    @Override
    public long numRegions ()
    {
        return 0L;
    }


    /**
     * @see musaico.foundation.region.SparseRegion#region(long)
     */
    @Override
    public RegionExpression region (
                                    long index
                                    )
    {
        return new FailedRegionExpression ( this.checkedException () );
    }
}
