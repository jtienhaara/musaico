package musaico.foundation.metadata;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A contract specifying that a particular type of metadatum must exist
 * inside the metadata for a particular object.
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
 * @see musaico.foundation.metadata.MODULE#COPYRIGHT
 * @see musaico.foundation.metadata.MODULE#LICENSE
 */
public class RequestedMetadatumMustExist
    implements Contract<Metadata, MetadataViolation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Manages contract checking for static methods and constructors. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( RequestedMetadatumMustExist.class );


    /** The class of metadatum which must exist in each Metadata. */
    private final Class<?> metadatumClass;


    /**
     * <p>
     * Creates a new RequestedMetadatumMustExist contract for the
     * specified class of metadatum.
     * </p>
     *
     * @param metadatum_class The class of metadatum which must exist
     *                        inside every Metadata.  Must not be null.
     */
    public RequestedMetadatumMustExist (
                                        Class<?> metadatum_class
                                        )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               metadatum_class );

        this.metadatumClass = metadatum_class;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
                                     Metadata metadata
                                     )
    {
        for ( Serializable metadatum : metadata )
        {
            if ( this.metadatumClass.isInstance ( metadatum ) )
            {
                return FilterState.KEPT;
            }
        }

        // Requested metadata not present.
        return FilterState.DISCARDED;
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    public final MetadataViolation violation (
                                              Object plaintiff,
                                              Metadata metadata
                                              )
    {
        final MetadataViolation violation =
            new MetadataViolation ( this,
                                    Contracts.makeSerializable ( plaintiff ),
                                    metadata );
        return violation;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            // Any RequestedMetadatumMustExist != null.
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            // RequestedMetadatumMustExistA != Foo or RequestedMetadatumMustExistB.
            return false;
        }

        RequestedMetadatumMustExist that = (RequestedMetadatumMustExist) object;
        if ( this.metadatumClass == null )
        {
            if ( that.metadatumClass == null )
            {
                // null == null.
                return true;
            }
            else
            {
                // null != any class of metadatum.
                return false;
            }
        }
        else if ( that.metadatumClass == null )
        {
            // Any class of metadatum != null.
            return false;
        }

        return this.metadatumClass.equals ( that.metadatumClass );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.metadatumClass.hashCode ();
    }


    /**
     * <p>
     * Returns this RequestedMetadatumMustExist's underlying class of
     * metadatum.
     * </p>
     *
     * @return This contract's underlying class of metadatum.  Never null.
     */
    public Class<?> metadatumClass ()
    {
        return this.metadatumClass;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "RequestedMetadatumMustExist "
            + ClassName.of ( this.metadatumClass );
    }
}
