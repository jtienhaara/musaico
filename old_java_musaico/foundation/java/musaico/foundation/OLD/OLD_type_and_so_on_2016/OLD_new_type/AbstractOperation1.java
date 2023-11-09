package musaico.foundation.type;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.machine.InputsMachine;

import musaico.foundation.value.Value;


/**
 * <p>
 * An Operation which performs the boilerplate work, and relies
 * on the derived class to provide a simple method which takes
 * 1 input and produces 1 output.
 * </p>
 *
 *
 * <p>
 * In Java every AbstractOperation1 must be Serializable in order to play
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
public abstract class AbstractOperation1<INPUT1 extends Object, OUTPUT extends Object>
    extends Operation
    implements OperationBody<OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** !!! */
    private static Graph<Symbol, Type> createGraph (
                                                    Type input1_type
                                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input1_type);

        final 
    }


    /**
     * <p>
     * Executes this operation with the specified input(s).
     * </p>
     *
     * <p>
     * Protected.  For use only by this AbstractOperation1 itself.
     * </p>
     *
     * <p>
     * NO CHECKING IS PERFORMED ON ANY OF THE INPUT(S).
     * </p>
     *
     * <p>
     * Before calling, check for nulls and verify each input
     * against this operation's invocation <code> graph () </code>.
     * Use this method with caution.
     * </p>
     *
     * @param input1 The input # 1 to this operation.  Checks should have
     *               already been performed to ensure validity of
     *               the input before calling this method.
     *
     * @return The output from this operation.  The caller is responsible
     *         for ensuring the validity of the output.
     *
     * @throws Exception Any exception at all may be thrown by this
     *                   method.  The caller must catch all exceptions,
     *                   and decide how to wrap them (in Errors, Warnings,
     *                   No values, and so on) to pass up the chain.
     */
    protected abstract Value<OUTPUT> execute (
                                              Value<INPUT1> input1
                                              )
        throws Exception;


    /**
     * <p>
     * Implements the Operation logic after the input constraints
     * have been enforced, and before the output constraints have
     * been enforced.
     * </p>
     *
     * @see musaico.foundation.type.Operation1#invoke(musaico.foundation.value.Value...)
     */
    public abstract Value<OUTPUT> execute (
            InputsMachine<Object, Symbol, Type> inputs
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
