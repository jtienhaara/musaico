package musaico.foundation.metadata;

import java.io.Serializable;

import java.util.LinkedHashSet;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.security.SecurityPolicy;

import musaico.foundation.value.Countable;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * A place to store metadata about some data, such as created/modified
 * timestamps, security policy, reference count, statistics, and any
 * other structured or ad hoc data-about-data which might be useful
 * to the developer.
 * </p>
 *
 * <p>
 * Every Metadata has standard methods for retrieving standard
 * information such as access times and reference count.  To add
 * non-standard metadata, use the <code> add ( metadatum ) </code>
 * method.  Be warned that a Metadata object is, in one sense,
 * immutable.  Each metadatum is create-once-never-overwrite.
 * Only the content of each metadatum can change over time.
 * Therefore the only way to remove or replace a metadatum is
 * to create a whole new Metadata object and start again.
 * </p>
 *
 *
 * <p>
 * In Java every metadatum must implement:
 * </p>
 *
 * @see java.lang.Object#equals(java.lang.Object)
 * @see java.lang.Object#hashCode()
 * @see java.lang.Object#toString()
 *
 * <p>
 * In Java every metadatum stored by a Metadata must be
 * Serializable in order to play nicely over RMI.
 * </p>
 *
 *
 * <p>
 * In Java every Metadata must be Serializable
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
 * @see musaico.foundation.metadata.MODULE#COPYRIGHT
 * @see musaico.foundation.metadata.MODULE#LICENSE
 */
public interface Metadata
    extends Countable<Metadatum>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** No metadata at all.  Not very useful. */
    public static final Metadata NONE =
        new NoMetadata ();


    /**
     * <p>
     * Retrieves the requested metadatum if it already exists, or adds
     * the specified metadatum if one is not already stored in this
     * Metadata.
     * </p>
     *
     * @param metadatum_class The class of metadatum to add or get.
     *                        This is the unique key for the metadatum.
     *                        Must not be null.
     *
     * @param metadatum The default metadatum to store, in case this Metadata
     *                  does not already have one.  Must not be null.
     *
     * @return The requested metadatum, either the existing one, or the
     *         specified value if it was added to this Metadata.
     *         Never null.
     */
    public abstract <METADATUM extends Serializable>
        METADATUM addOrGet (
                            Class<METADATUM> metadatum_class,
                            METADATUM metadatum_if_required,
                            Metadatum.Flag ... metadatum_flags
                            )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Retrieves the requested metadatum if it already exists.
     * </p>
     *
     * @param metadatum_class The class of metadatum to get.
     *                        This is the unique key for the metadatum.
     *                        Must not be null.
     *
     * @return The Successful requested metadatum, if one already exists
     *         in this Metadata, or a Failed result, if this Metadata does
     *         not contain the requested metadatum.
     *         Never null.
     */
    public abstract <METADATUM extends Serializable>
        ZeroOrOne<METADATUM> get (
                                  Class<METADATUM> metadatum_class
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns true if the requested metadatum exists.
     * </p>
     *
     * @param metadatum_class The class of metadatum to inspect.
     *                        This is the unique key for the metadatum.
     *                        Must not be null.
     *
     * @return True if one already exists in this Metadata,
     *         false otherwise.
     */
    public abstract <METADATUM extends Serializable>
        boolean has (
                     Class<METADATUM> metadatum_class
                     )
        throws ParametersMustNotBeNull.Violation;


    /**
     * @return The Origin for the object covered by this Metadata
     *         (if any).  Never null.
     */
    public abstract ZeroOrOne<Origin> origin ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a Metadata instance for a new parent Object, either the
     * same, shared Metadata, or a newly created one.
     * </p>
     *
     * <p>
     * Some Metadata (such as NoMetadata) simply return themselves;
     * while others (such as StandardMetadata) create new instances.
     * </p>
     *
     * <p>
     * When a new Metadata instance is created, only the metadata
     * which are flagged to be carried over (such as SecurityPolicy)
     * shall be copied into the new instance.  Other metadata
     * (such as Statistics, which should be zeroed out) are not transferred
     * to the newly created instance.  One special metadatum is
     * the Origin: if an Origin exists, then a new Origin is created for the
     * newly created instance.
     * </p>
     *
     * @return The Metadata for a new Object.  Possibly the same as
     *         this Metadata, if this type of Metadata is to be shared,
     *         or possibly a brand new Metadata.  Never null.
     */
    public abstract Metadata renew ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The SecurityPolicy for the object covered by this Metadata
     *         (if any).  Never null.
     */
    public abstract ZeroOrOne<SecurityPolicy> securityPolicy ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The Statistics for the object covered by this Metadata
     *         (if any).  Never null.
     */
    public abstract ZeroOrOne<Statistics> statistics ()
        throws ReturnNeverNull.Violation;
}
