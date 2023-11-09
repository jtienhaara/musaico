package musaico.foundation.metadata;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.security.SecurityPolicy;

import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.finite.No;


/**
 * <p>
 * No metadata at all, no statistics are tracked, there is no security policy,
 * and so on.
 * </p>
 *
 *
 * <p>
 * In Java every Metadata must be either Serializable or Remote
 * (such as a UnicastRemoteObject), in order to play nicely over RMI.
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
public class NoMetadata
    extends No<Metadatum>
    implements Metadata, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new NoMetadata, which does not track any statistics,
     * and can be shared by parent objects, since it does not contain
     * any parent object-specific metadata.
     * </p>
     */
    public NoMetadata ()
    {
        super ( Metadatum.class, // expected_class
                new MetadataViolation ( new RequestedMetadatumMustExist ( Serializable.class ),
                                        NoMetadata.class,   // plaintiff
                                        "no_metadatum" ) ); // evidence
    }


    /**
     * @return A new RequestedMetadatumMustExist violation for the specified
     *         class of metadatum.  Never null.
     */
    protected final MetadataViolation violation (
                                                 Class<? extends Serializable> metadatum_class
                                                 )
    {
        final MetadataViolation violation =
            new MetadataViolation ( new RequestedMetadatumMustExist ( metadatum_class ),
                                    this,
                                    this );

        return violation;
    }


    /**
     * @see musaico.foundation.metadata.Metadata#addOrGet(java.lang.Class, java.io.Serializable, musaico.foundation.metadata.Metadatum.Flag[])
     */
    @Override
    public <METADATUM extends Serializable>
        METADATUM addOrGet (
                            Class<METADATUM> metadatum_class,
                            METADATUM metadatum_if_required,
                            Metadatum.Flag ... flags
                            )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        return metadatum_if_required;
    }


    /**
     * @see musaico.foundation.metadata.Metadata#get(java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast known metadatum classes.
    public <METADATUM extends Serializable>
        ZeroOrOne<METADATUM> get (
                                  Class<METADATUM> metadatum_class
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        if ( metadatum_class == Origin.class )
        {
            return (No<METADATUM>) this.origin ();
        }
        else if ( metadatum_class == SecurityPolicy.class )
        {
            return (No<METADATUM>) this.securityPolicy ();
        }
        else if ( metadatum_class == Statistics.class )
        {
            return (No<METADATUM>) this.statistics ();
        }

        return new No<METADATUM> ( metadatum_class,
                                   this.violation ( metadatum_class ) );
    }


    /**
     * @see musaico.foundation.metadata.Metadata#has(java.lang.Class)
     */
    @Override
    public final <METADATUM extends Serializable>
        boolean has (
                     Class<METADATUM> metadatum_class
                     )
    {
        return false;
    }


    /**
     * @see musaico.foundation.metadata.Metadata#origin()
     */
    @Override
    public final No<Origin> origin ()
        throws ReturnNeverNull.Violation
    {
            return new No<Origin> ( Origin.class,
                                    this.violation ( Origin.class ) );
    }


    /**
     * @see musaico.foundation.metadata.Metadata#renew()
     */
    @Override
    public final NoMetadata renew ()
        throws ReturnNeverNull.Violation
    {
        return this;
    }


    /**
     * @see musaico.foundation.metadata.Metadata#securityPolicy()
     */
    @Override
    public final No<SecurityPolicy> securityPolicy ()
        throws ReturnNeverNull.Violation
    {
        return new No<SecurityPolicy> ( SecurityPolicy.class,
                                        this.violation ( SecurityPolicy.class ) );
    }


    /**
     * @see musaico.foundation.metadata.Metadata#statistics()
     */
    @Override
    public final No<Statistics> statistics ()
        throws ReturnNeverNull.Violation
    {
        return new No<Statistics> ( Statistics.class,
                                    this.violation ( Statistics.class ) );
    }
}
