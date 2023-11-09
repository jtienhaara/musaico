package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.contract.Domain;
import musaico.foundation.contract.DomainObligation;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * An obligation which must be met by the caller of Region
 * methods, such as "position must not be out of bounds",
 * or "start position must be before or at end position",
 * and so on.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
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
 * @see musaico.foundation.region.MODULE#COPYRIGHT
 * @see musaico.foundation.region.MODULE#LICENSE
 */
public class RegionObligation<INSPECTABLE extends Object>
    extends DomainObligation<INSPECTABLE, RegionViolation>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** RegionObligation: region must not be empty. */
    public static final RegionObligation<Region> REGION_MUST_NOT_BE_EMPTY =
        new RegionObligation<Region> ( new DomainRegionNonEmpty () );

    /** RegionObligation: region must be size one (one position). */
    public static final RegionObligation<Region> REGION_MUST_BE_SIZE_ONE =
        new RegionObligation<Region> ( new DomainRegionWithSizeOne () );


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( RegionObligation.class );


    /**
     * <p>
     * Creates a new RegionObligation requiring that every Position
     * or Region or Size belong to the specified Domain.
     * </p>
     *
     * @param domain The domain to which every Position/Region/Size
     *               must belong.  Must not be null.
     *
     * @throws IllegalArgumentException If the specified domain is null.
     */
    public RegionObligation (
                             Domain<INSPECTABLE> domain
                             )
        throws IllegalArgumentException
    {
        super ( domain );
    }


    /**
     * @see musaico.foundation.contract.DomainObligation#createViolation(java.io.Serializable, java.io.Serializable)
     */
    protected RegionViolation createViolation (
                                               Serializable plaintiff,
                                               Serializable inspectable_data
                                               )
    {
        return new RegionViolation ( this,
                                     plaintiff,
                                     inspectable_data );
    }
}
