package musaico.foundation.operation;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;


/**
 * <p>
 * The environment or stack frame and so on in which Pipe(s) are to
 * be evaluated.
 * </p>
 *
 * <p>
 * The Context contains Variable bindings for the pipeline, as well as
 * cached information such as contract violations due to invalid
 * parameters (which cannot be reset in a Context, even if the output
 * from a Pipe that was once invalid would later be valid as the
 * parameter to the violated Pipe).
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
 * @see musaico.foundation.operation.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.MODULE#LICENSE
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
     *         in this Context; or null, if the specified variable
     *         has never been bound in this Context, and so is still free.
     *         CAN BE NULL.
     */
    public abstract <VALUE extends Object>
        Pipe<VALUE> binding (
            Variable<VALUE> free_variable
            )
        throws ParametersMustNotBeNull.Violation;


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
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Returns the final Term output from the specified Pipe,
     * if it has been previously cached; or null if the Term
     * output has not yet been generated and cached in this Context.
     * </p>
     *
     * @param pipe The Pipe whose cached output Term will be returned,
     *             if any.  Must not be null.
     *
     * @return The cached output Term from the specified Pipe, if any;
     *         or null if no output Term has been cached for the
     *         specified Pipe in this Context.  CAN BE NULL.
     */
    public abstract <VALUE extends Object>
        Term<VALUE> term (
            Pipe<VALUE> pipe
            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Caches the final Term output for the specified Pipe.
     * </p>
     *
     * @param pipe The Pipe whose output Term in this Context will be cached.
     *             Must not be null.
     *
     * @param term The output Term to cache.  Must not be null.
     */
    public abstract <VALUE extends Object>
        Term<VALUE> term (
            Pipe<VALUE> pipe,
            Term<VALUE> term
            )
        throws ParametersMustNotBeNull.Violation;


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
            )
        throws ParametersMustNotBeNull.Violation;


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
            )
        throws ParametersMustNotBeNull.Violation;
}
