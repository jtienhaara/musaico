package musaico.foundation.buffer;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.typing.Typing;


/**
 * <p>
 * A violation of some Obligation or Guarantee stipulated by a Buffer,
 * such as "position is out of bounds".
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
 * @see musaico.foundation.buffer.MODULE#COPYRIGHT
 * @see musaico.foundation.buffer.MODULE#LICENSE
 */
public class BufferViolation
    extends Typing.Violation
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( BufferViolation.class );


    /**
     * <p>
     * Creates a BufferViolation for the specified breached
     * contract.
     * </p>
     *
     * @param contract The Contract which was breached,
     *                 inducing this Violation.  Must not be null.
     *
     * @param plaintiff The object whose contract was breached,
     *                  or a Serializable representation
     *                  of that object (such as a String).
     *                  Must not be null.
     *
     * @param inspectable_data The data which breached the contract, or
     *                         Serializable representations of that data
     *                         (such as Strings) if the data is not all
     *                         Serializable.  Cast to a String(s)
     *                         ("" + null) if a null value(s) breached
     *                         the contract.  Must not be null.
     *                         Must not contain any null elements.
     */
    public BufferViolation (
                            Contract<?, ?> contract,
                            Serializable plaintiff,
                            Serializable inspectable_data
                            )
    {
        super ( contract,
                plaintiff,
                inspectable_data );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               contract,
                               plaintiff,
                               inspectable_data );
    }


    /**
     * <p>
     * Creates a BufferViolation for the specified breached
     * contract, with the specified cause.
     * </p>
     *
     * @param contract The Contract which was breached,
     *                 inducing this Violation.  Must not be null.
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
     *
     * @param cause The cause of this BufferViolation, which can be
     *              some other contract violation which led to this one,
     *              or any other type of Exception or Error and so on
     *              which caused this BufferViolation.  Must not be null.
     */
    public BufferViolation (
                            Contract<?, ?> contract,
                            Serializable plaintiff,
                            Serializable inspectable_data,
                            Throwable cause
                            )
    {
        super ( contract,
                plaintiff,
                inspectable_data,
                cause );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               contract,
                               plaintiff,
                               inspectable_data,
                               cause );
    }
}
