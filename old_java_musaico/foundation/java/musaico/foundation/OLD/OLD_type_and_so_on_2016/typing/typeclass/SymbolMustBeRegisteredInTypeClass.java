package musaico.foundation.typing.typeclass;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;

import musaico.foundation.typing.AbstractRegistration;
import musaico.foundation.typing.SymbolMustBeRegistered;
import musaico.foundation.typing.TypingViolation;


/**
 * <p>
 * A specific SymbolID must be required by a TypeClass.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class SymbolMustBeRegisteredInTypeClass
    extends SymbolMustBeRegistered
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;

    public static class Violation
        extends TypingViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            SymbolMustBeRegisteredInTypeClass.serialVersionUID;

        /**
         * <p>
         * Creates a new SymbolMustBeRegisteredInTypeClass.Violation
         * for some typing contract that was breached.
         * </p>
         */
        public Violation (
                          Contract<?, ?> contract,
                          Serializable plaintiff,
                          Object value
                          )
        {
            super ( contract, plaintiff, value );
        }

        /**
         * <p>
         * Creates a new Violation for some typing contract
         * that was breached.
         * </p>
         */
        public Violation (
                          Contract<?, ?> contract,
                          Serializable plaintiff,
                          Object value,
                          Throwable cause
                          )
        {
            super ( contract,
                    plaintiff,
                    value,
                    cause );
        }
    }


    public static final SymbolMustBeRegisteredInTypeClass CONTRACT =
        new SymbolMustBeRegisteredInTypeClass ();

    /**
     * <p>
     * Convenience method.  Creates a violation then sets the root
     * cause of the newly created violation.
     * </p>
     *
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public final SymbolMustBeRegisteredInTypeClass.Violation violation (
            Object plaintiff,
            AbstractRegistration registration,
            Throwable cause
            )
    {
        final SymbolMustBeRegisteredInTypeClass.Violation violation =
            this.violation ( plaintiff,
                             registration );

        violation.initCause ( cause );

        return violation;
    }

    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public final SymbolMustBeRegisteredInTypeClass.Violation violation (
            Object plaintiff,
            AbstractRegistration registration
            )
    {
        return new SymbolMustBeRegisteredInTypeClass.Violation (
                       this,
                       Contracts.makeSerializable ( plaintiff ),
                       Contracts.makeSerializable ( registration ) );
    }
}
