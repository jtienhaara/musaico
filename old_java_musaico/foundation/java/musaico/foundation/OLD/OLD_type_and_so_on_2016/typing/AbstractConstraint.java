package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.value.Value;


/**
 * <p>
 * Boilerplate method implementations for Constraints.
 * </p>
 *
 * <p>
 * Derived Constraints only need to implement the filter which determines
 * whether a Value is KEPT (OK) or DISCARDED (constraint violation):
 * </p>
 *
 * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
 *
 *
 * <p>
 * In Java every Symbol must be Serializable in order to play
 * nicely with RMI.
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
public abstract class AbstractConstraint
    extends AbstractSymbol<ConstraintID, Constraint>
    implements Constraint, Contract<Value<?>, TypingViolation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Creates a new AbstractConstraint.
     * </p>
     *
     * @param name The name of the new Constraint, which will be used
     *             to create a ConstraintID, unique within every SymbolTable.
     *             Must not be null.
     */
    public AbstractConstraint (
                               String name
                               )
        throws ParametersMustNotBeNull.Violation
    {
        // Use our Type's metadata for tracking contract violations.
        super ( new ConstraintID ( name ) );
    }

    /**
     * <p>
     * Convenience method.  Creates a violation of this contract,
     * then attaches the specified root cause throwable.
     * </p>
     *
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object,java.lang.Object)
     */
    public final TypingViolation violation (
                                            Object plaintiff,
                                            Value<?> inspectable_data,
                                            Throwable cause
                                            )
    {
        final TypingViolation violation =
            this.violation ( plaintiff,
                             inspectable_data );

        violation.initCause ( cause );

        return violation;
    }

    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public TypingViolation violation (
                                      Object plaintiff,
                                      Value<?> inspectable_data
                                      )
    {
        return new TypingViolation ( this,
                                     Contracts.makeSerializable ( plaintiff ),
                                     Contracts.makeSerializable ( inspectable_data ) );
    }
}
