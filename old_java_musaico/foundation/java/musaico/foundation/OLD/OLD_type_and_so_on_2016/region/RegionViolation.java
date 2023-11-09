package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.contract.DomainObligation;
import musaico.foundation.contract.DomainObligationCheckedViolation;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A violation of some obligation required of users of a Region,
 * such as "position is out of bounds" or "start position is
 * after end position" and so on.
 * </p>
 *
 *
 * <p>
 * In Java, every ContractViolation must be Serializable in order to play
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
public class RegionViolation
    extends DomainObligationCheckedViolation
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( RegionViolation.class );


    /**
     * <p>
     * Creates a RegionViolation for the specified breached
     * obligation.
     * </p>
     *
     * @param obligation The DomainObligation which was breached,
     *                   inducing this Violation.  Must not be null.
     *
     * @param plaintiff The object whose contract was breached,
     *                              or a Serializable representation
     *                              of that object (such as a String).
     *                              Must not be null.
     *
     * @param inspectable_data The data which breached the contract, or
     *                         Serializable representations of that data
     *                         (such as Strings) if the data is not all
     *                         Serializable.  Cast to a String(s)
     *                         ("" + null) if a null value(s) breached
     *                         the contract.  Must not be null.
     *                         Must not contain any null elements.
     */
    public RegionViolation (
                            DomainObligation<?, ?> obligation,
                            Serializable plaintiff,
                            Serializable inspectable_data
                            )
    {
        super ( obligation,
                plaintiff,
                inspectable_data );
    }
}
