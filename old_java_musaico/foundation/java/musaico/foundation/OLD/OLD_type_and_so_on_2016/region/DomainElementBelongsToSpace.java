package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.contract.Domain;


/**
 * <p>
 * The domain of all SpatialElements which belong to a specific Space.
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
public class DomainElementBelongsToSpace
    implements Domain<SpatialElement>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( DomainElementBelongsToSpace.class );


    /** The space to which every SpatialElement must belong. */
    private final Space space;


    /**
     * <p>
     * Creates a new domain in which every SpatialElement belongs to the
     * specified Space.
     * </p>
     *
     * @param space The space to which every SpatialElement in this domain
     *               belongs.  Must not be null.
     */
    public DomainElementBelongsToSpace (
                                        Space space
                                        )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               space );

        this.space = space;
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

        final DomainElementBelongsToSpace that =
            (DomainElementBelongsToSpace) object;
        if ( ! this.space.equals ( that.space ) )
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
        return this.space.hashCode ();
    }


    /**
     * @see musaico.foundation.contract.Domain#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid (
                            SpatialElement spatial_element
                            )
    {
        if ( spatial_element == null )
        {
            return false;
        }

        if ( this.space.equals ( spatial_element.space () ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "spatial elements in space " + this.space;
    }
}
