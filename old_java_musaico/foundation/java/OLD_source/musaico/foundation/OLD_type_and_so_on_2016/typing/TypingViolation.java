package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A violation of some typing contract.
 * </p>
 *
 * <p>
 * For example, a violation caused by attempting to cast an object
 * to a type with no typecasting path.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class TypingViolation
    extends CheckedViolation
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks constructor and static method obligations. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( TypingViolation.class );


    /** No TypingViolation at all.  Not very useful. */
    public static final TypingViolation NONE =
        new TypingViolation ( Contract.NONE,
                              TypingViolation.class,
                              "TypingViolation.NONE" );


    /**
     * <p>
     * Creates a new TypingViolation for some typing contract
     * that was breached.
     * </p>
     */
    public TypingViolation (
                            Contract<?, ?> contract,
                            Serializable plaintiff,
                            Object value
                            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( contract, plaintiff, value, null );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               contract, plaintiff, value );
    }

    /**
     * <p>
     * Creates a new TypingViolation for some typing contract
     * that was breached.
     * </p>
     */
    public TypingViolation (
                            Contract<?, ?> contract,
                            Serializable plaintiff,
                            Object value,
                            Throwable cause
                            )
    {
        super ( contract,
                plaintiff,
                Contracts.makeSerializable ( value ) );

        if ( cause != null )
        {
            this.initCause ( cause );
        }
    }


    public <VIOLATION extends Throwable & Violation>
                              TypingViolation (
                                               VIOLATION cause
                                               )
        throws ParametersMustNotBeNull.Violation
    {
        this ( cause == null ? null : cause.contract (),
               cause == null ? null : cause.plaintiff (),
               cause == null ? null : cause.inspectableData (),
               cause );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               cause );
    }
}
