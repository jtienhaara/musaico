package musaico.foundation.region;

import java.io.Serializable;

import musaico.foundation.contract.Domain;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * All Regions which are different from a specific Region.
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
public class DomainRegionDifferent
    implements Domain<Region>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( DomainRegionDifferent.class );


    /** The Region to compare other Regions to. */
    private final Region region;


    /**
     * <p>
     * Creates a new DomainRegionDifferent, which matches all Regions
     * which have different Positions than the one specified.
     * </p>
     *
     * @param region The region to which other Regions will be compared.
     *               Must not be null.
     */
    public DomainRegionDifferent (
                                  Region region
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region );

        this.region = region;
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

        final DomainRegionDifferent that = (DomainRegionDifferent) object;
        if ( this.region.equals ( that.region ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.region.hashCode ();
    }


    /**
     * @see musaico.foundation.contract.Domain#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid (
                            Region region
                            )
    {
        if ( region == null )
        {
            return false;
        }

        if ( this.region.equals ( region ) )
        {
            // Not different.
            return false;
        }
        else
        {
            // Different.
            return true;
        }
    }


    /**
     * @return The Region to which other Regions are compared for difference.
     *         Never null.
     */
    public Region region ()
    {
        return this.region;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "regions different from " + this.region;
    }
}
