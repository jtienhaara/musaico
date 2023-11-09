package musaico.foundation.metadata;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.CheckedViolation;


/**
 * <p>
 * An error thrown when a requested metadatum does not exist,
 * or a metadatum cannot be set to a particular value, and so on.
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
public class MetadataViolation
    extends CheckedViolation
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a MetadataViolation for the specified breached metadata
     * contract.
     * </p>
     *
     * @param metadata_contract The metadata Contract which was breached,
     *                          for example because a non-existant
     *                          metadatum was requested, or an attempt
     *                          was made to set a metadatum to some illegal
     *                          value, and so on.
     *                          Must not be null.
     *
     * @param plaintiff The object whose contract was breached,
     *                  or a Serializable representation
     *                  of that object (such as a String).
     *                  Must not be null.
     *
     * @param metadatum_or_class The data which breached the contract, or a
     *                           Serializable representation of that data
     *                           (such as a String) if the data is not
     *                           Serializable.  Must not be null.
     */
    public MetadataViolation (
                              Contract<?, MetadataViolation> metadata_contract,
                              Serializable plaintiff,
                              Serializable metadatum_or_class
                              )
    {
        super ( metadata_contract,
                plaintiff,
                metadatum_or_class );
    }
}
