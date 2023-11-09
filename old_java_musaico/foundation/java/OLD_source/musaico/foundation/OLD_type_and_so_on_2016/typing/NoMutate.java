package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.ZeroOrOne;
import musaico.foundation.value.Value;


/**
 * <p>
 * The default do-nothing mutate Operation, which always passes out whatever
 * Symbol was passed in.
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
public class NoMutate
    extends StandardOperation1<Mutation, Mutation>
    implements OperationBody1<Mutation, Mutation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new NoMutate with the specified SymbolID name,
     * which will mutate every Symbol passed in by passing the
     * exact same Symbol out.
     * </p>
     *
     * @param name The SymbolID name which uniquely identifies
     *             this mutate Operation within a SymbolTable.
     *             Must not be null.
     */
    public NoMutate (
                     String name
                     )
        throws ParametersMustNotBeNull.Violation
    {
        super ( name,
                Mutation.TYPE,
                Mutation.TYPE,
                null ); // body = this.
    }


    /**
     * @see musaico.foundation.typing.OperationBody1#evaluateBody(musaico.foundation.value.Value)
     */
    @Override
    public final Value<Mutation> evaluateBody (
                                               Value<Mutation> unmutated
                                               )
        throws ReturnNeverNull.Violation
    {
        // NoMutate does nothing.
        return unmutated;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public NoMutate rename (
                            String name
                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new NoMutate ( name );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public NoMutate retype (
                            String name,
                            OperationType<? extends Operation<Mutation>, Mutation> type
                            )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new NoMutate ( name );
    }
}
