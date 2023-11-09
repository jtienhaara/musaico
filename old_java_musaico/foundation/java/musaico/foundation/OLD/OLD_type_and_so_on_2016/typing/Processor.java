package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.AsynchronousResult;


/**
 * <p>
 * A Processor, akin to a hardware CPU, receives an instruction to
 * execute (an Expression), evaluates it, and returns the output.
 * </p>
 *
 * <p>
 * In the simplest case, a Processor immediately evaluate ()s the
 * Expression's Operation, passing in the Expression's input value.
 * </p>
 *
 *
 * <p>
 * In Java every Processor must be either Serializable or a
 * UnicastRemoteObject in order to play nicely with RMI.
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
public interface Processor
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * The toplevel Processor / CPU which delegates all Expression
     * processing to other Processors.
     */
    public static final ToplevelProcessor TOPLEVEL =
        new ToplevelProcessor ();


    /**
     * <p>
     * Evaluates the specified Expression and returns its output.
     * </p>
     *
     * <p>
     * Typically this method is invoked by the Expression, in response
     * to a call to Expression.value ().
     * </p>
     *
     * @param expression The Expression to evaluate.  Must not be null.
     *
     * @param result The AsynchronousResult which will be set by
     *               this Processor when processing is complete.
     *               Must not be null.
     */
    public abstract <INPUT extends Object, OUTPUT extends Object>
        void process (
                      Expression<OUTPUT> expression,
                      AsynchronousResult<OUTPUT> result
                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
