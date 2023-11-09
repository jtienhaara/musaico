package musaico.foundation.operations;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.TermViolation;


/**
 * <p>
 * !!!
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
 * @see musaico.foundation.!!!.MODULE#COPYRIGHT
 * @see musaico.foundation.!!!.MODULE#LICENSE
 */
public interface Context
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Returns the bound variable Pipe corresponding to the specified
     * free Variable, if it has been bound in this Context;
     * or the specified free Variable, if it has not binding in this Context.
     * </p>
     *
     * @param free_variable The variable whose binding will be returned.
     *                      Must not be null.
     *
     * @return The specified variable's binding, if it is a bound variable
     *         in this Context; or the specified free variable, if it
     *         has not been bound in this Context.  Never null.
     */
    public abstract <VALUE extends Object>
        Pipe<VALUE> binding (
            Variable<VALUE> free_variable
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Binds the specified free Variable to the specified Pipe
     * in this Context.
     * </p>
     *
     * @param free_variable The variable whose binding will be set.
     *                      It must not already be bound in this Context.
     *                      Must not be null.
     *
     * @param binding The specified variable's binding.
     *                Must not be null.
     */
    public abstract <VALUE extends Object>
        void binding (
            Variable<VALUE> free_variable,
            Pipe<VALUE> binding
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the TermViolation for the specified Pipe in this Context
     * (if any).
     * </p>
     *
     * @param pipe The Pipe might have either caused, or been affected by,
     *             a TermViolation. Must not be null.
     *
     * @return The TermViolation, if any; or null.
     *         CAN BE NULL.
     */
    public abstract TermViolation violation (
            Pipe<?> pipe
            );


    /**
     * <p>
     * Sets the TermViolation for the specified Pipe in this Context.
     * </p>
     *
     * @param pipe The Pipe which either caused, or is affected by,
     *             the error.  Must not be null.
     *
     * @param violation The TermViolation which constitutes the error.
     *                  Must not be null.
     */
    public abstract void violation (
            Pipe<?> pipe,
            TermViolation violation
            );
}
