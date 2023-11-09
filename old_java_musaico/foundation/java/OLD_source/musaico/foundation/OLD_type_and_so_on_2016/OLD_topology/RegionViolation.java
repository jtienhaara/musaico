package musaico.foundation.topology;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.typing.TypingViolation;


/**
 * <p>
 * A violation of some obligation required of users of a Region,
 * such as "point is out of bounds" or "start point is
 * after end point" and so on.
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
 * @see musaico.foundation.topology.MODULE#COPYRIGHT
 * @see musaico.foundation.topology.MODULE#LICENSE
 */
public class RegionViolation
    extends TypingViolation
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new RegionViolation with the specified
     * details.
     * </p>
     *
     * @param contract The violated contract.  Must not be null.
     *
     * @param plaintiff The object whose contract was
     *                              violated.  Must not be null.
     *
     * @param inspectable_data The data which violated the contract.
     *                         Must not be null.
     */
    public RegionViolation (
                            Contract<?, ?> contract,
                            Object plaintiff,
                            Object inspectable_data
                            )
    {
        super ( contract,
                Contracts.makeSerializable ( plaintiff ),
                inspectable_data );
    }
}
