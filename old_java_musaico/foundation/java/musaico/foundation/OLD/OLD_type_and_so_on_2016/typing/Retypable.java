package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A Symbol which can be re-Typed, such as a Constant "string" which
 * can be re-Type as a Constant "string[length&gt;=1]", or an Operation
 * "(string):int" which can be re-Type as an
 * Operation "(string[length&gt;=1):int[odd,positive]", and so on.
 * </p>
 *
 * <p>
 * Re-Typing can be useful, for example, during sub-Typing, when
 * references to the parent Type are replaced by references to the
 * new sub-Type.  In this way, Cast and similar Operations are
 * transferred from the parent Type to the sub-Type.
 * </p>
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
public interface Retypable<SYMBOL extends Symbol, SYMBOL_TYPE extends Type<? extends SYMBOL>>
    extends Symbol, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Creates an exact copy of this Symbol, but with the specified
     * ID name and SymbolType.
     * </p>
     *
     * @param name The name to use for the SymbolID of the new Symbol
     *             to be created.  Must not be null.
     *
     * @param type The SymbolType fo the new Symbol.  Must have
     *             the same underlying value class(es) as this Symbol.
     *             For example, when re-typing a Constant, the new
     *             ConstantType's valueType () must have the same
     *             valueClass () as this Constant's valueType ().
     *             Or when re-typing an Operation, the new Operation's
     *             input and output Types must have the same valueClass ()es
     *             as the input and output Types in this Operation.
     *             Note that as long as the valueClass () of each
     *             corresponding Type is the same, the Type itself can
     *             be different.  For example, if this is an Operation
     *             which takes Type "string" as input and outputs
     *             Type "int" as output, then it can be renamed to take
     *             tagged Type "string[length&gt;=1]" as input and
     *             return tagged Type "int[positive,odd]" as output.
     *             Must not be null.
     *
     * @return A newly created duplicate of this Symbol,
     *         with the specified id name and SymbolType.  Never null.
     *
     * @throws TypesMustHaveSameValueClass.Violation If the specified
     *                                               Type cannot be used
     *                                               to retype this Symbol.
     */
    public abstract SYMBOL retype (
                                   String name,
                                   SYMBOL_TYPE type
                                   )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation;
}
