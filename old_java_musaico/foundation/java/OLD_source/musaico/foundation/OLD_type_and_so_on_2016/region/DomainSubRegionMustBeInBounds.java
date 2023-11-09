package musaico.foundation.region;

import java.io.Serializable;

import musaico.foundation.contract.Domain;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * All Regions which have Size equal to one position.
 * </p>
 *
 *
 * <p>
 * In Java every Domain must implement equals (), hashCode () and
 * toString ().
 * </p>
 *
 * <p>
 * In Java every Domain must be Serializable in order to play nicely
 * across RMI.
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
public class DomainSubRegionMustBeInBounds
    implements Domain<Integer>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( DomainSubRegionMustBeInBounds.class );


    /** The SparseRegion in whose bounds each sub-region index must
     *  fall. */
    private final SparseRegion sparseRegion;


    /**
     * <p>
     * Creates a new DomainSubRegionMustBeInBounds contract for the
     * specified sparse region.
     * </p>
     *
     * @param sparse_region The sparse region in whose bounds each
     *                      sub-region index must fall.
     *                      Must not be null.
     */
    public DomainSubRegionMustBeInBounds (
                                          SparseRegion sparse_region
                                          )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               sparse_region );

        this.sparseRegion = sparse_region;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final DomainSubRegionMustBeInBounds that =
            (DomainSubRegionMustBeInBounds) object;
        if ( ! this.sparseRegion ().equals ( that.sparseRegion () ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.sparseRegion.hashCode ();
    }


    /**
     * @see musaico.foundation.contract.Domain#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid (
                            Integer sub_region_index_object
                            )
    {
        if ( sub_region_index_object == null )
        {
            return false;
        }

        final int sub_region_index = sub_region_index_object.intValue ();
        if ( sub_region_index < 0
             || sub_region_index >= this.sparseRegion.numRegions () )
        {
            return false;
        }

        return true;
    }


    /**
     * @return The sparse region, whose sub-region indices this
     *         domain covers.  Never null.
     */
    public SparseRegion sparseRegion ()
    {
        return this.sparseRegion;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "sub-region indices of " + this.sparseRegion
            + " in the range 0.." + this.sparseRegion.numRegions ();
    }
}
