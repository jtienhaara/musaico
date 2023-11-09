package musaico.foundation.typing;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.No;
import musaico.foundation.value.Value;


/**
 * <p>
 * An Operation which asserts that each value passed in meets
 * one or more Constraints.
 * </p>
 *
 * <p>
 * If a value does not meet the requirements of any of the Constraints,
 * then a No value is passed out, with the TypingViolation describing
 * the violated Constraint.
 * </p>
 *
 * <p>
 * Typically a Term asserts that its value meets all obligations
 * and guarantees required by its Type, using a ConstraintChecker.
 * </p>
 *
 *
 * <p>
 * In Java a ConstraintChecker is NOT Serializable.
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
public class ConstraintChecker
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts on methods for us. */
    private final ObjectContracts contracts;


    /**
     * <p>
     * Creates a new ConstraintChecker.
     * </p>
     */
    public ConstraintChecker ()
    {
        this.contracts = new ObjectContracts ( this );
    }


    /**
     * <p>
     * Checks the incoming Value against the specified Constraints
     * container, and throws a TypingViolation if any of the
     * Constraints is violated.
     * </p>
     *
     * @param constraints_container The symbol table object (such as
     *                              a Type or Tag and so on) whose
     *                              Constraints will be checked against
     *                              the specified value.  Must not be null.
     *
     * @param value The Value to check against the Constraints.
     *              Must not be null.
     */
    public final <CONSTRAINED extends Object>
        void check (
                    Namespace constraints_container,
                    Value<CONSTRAINED> value
                    )
        throws TypingViolation,
               ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               constraints_container, value );

        final Value<Constraint> constraints =
            constraints_container.symbols ( Constraint.TYPE );
        for ( Constraint constraint : constraints )
        {
            if ( ! constraint.filter ( value ).isKept () )
            {
                throw constraint
                    .violation ( this,
                                 value );
            }
        }
    }
}
