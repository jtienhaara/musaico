package musaico.foundation.typing;

import java.io.Serializable;

import java.util.List;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.Value;


/**
 * <p>
 * An Operation transforms an input value into an output value.
 * </p>
 *
 * <p>
 * An Operation must not have any side-effects (reading or writing state,
 * performing input/output operations, and so on) unless they are
 * explicitly declared via the input(s) and/or output.  For more
 * on side-effects:
 * </p>
 *
 * @see musaico.foundation.typing.sideeffects.SideEffects
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
public interface Operation<OUTPUT extends Object>
    extends Symbol, Retypable<Operation<OUTPUT>, OperationType<? extends Operation<OUTPUT>, OUTPUT>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** The raw name for the unique identifier for a no-op. */
    public static final String NO_NAME = "none";

    /** Default name for cast Operations from one Type to another. */
    public static final String CAST = "cast";

    /** No cast.  Does nothing.
     * Do not register NONE objects in any namespace. */
    public static final NoCast<Object, Object> NO_CAST =
        new NoCast<Object, Object> ( Type.NONE, Type.NONE );


    /**
     * <p>
     * Evaluates this operation on the specified List of input(s), and returns
     * either a Just or No output.
     * </p>
     *
     * <p>
     * This method performs no compile-time checking.  Whenever possible
     * it is preferable to use more specific methods, such as
     * Operation1.evaluate(input1), or Operation2.evaluate(input1, input2),
     * and so on.
     * </p>
     *
     * @param inputs The input Value(s) to operate on.  Must not be null.
     *               Must not contain any null elements.  Must contain
     *               at least one input element.
     *
     * @return The Value output.  If the operation is successful,
     *         then a Just result will be returned.
     *         Otherwise an Unjust result will be returned,
     *         and the caller can decide whether to call
     *         <code> orThrowChecked () <code>,
     *         <code> orDefault ( ... ) </code> and so on.
     *         Never null.
     */
    public Value<OUTPUT> evaluate (
                                   List<Value<Object>> inputs
                                   )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Parameter1.Length.MustBeGreaterThanZero.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The Type of output values returned by this Operation.
     *         Also returned by
     *         <code> id ().operationType ().outputType () </code>.
     *         Never null.
     */
    public abstract Type<OUTPUT> outputType ();


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String,musaico.foundation.typing.Type)
     */
    @Override
    public abstract Operation<OUTPUT> retype (
                                              String name,
                                              OperationType<? extends Operation<OUTPUT>, OUTPUT> type
                                              )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return This Operation's OperationType, also returned by
     *         <code> id ().type () </code>.
     *
     * @see musaico.foundation.typing.SymbolID#type()
     */
    @Override
    public abstract
        OperationType<? extends Operation<OUTPUT>, OUTPUT> type ()
            throws ReturnNeverNull.Violation;
}
