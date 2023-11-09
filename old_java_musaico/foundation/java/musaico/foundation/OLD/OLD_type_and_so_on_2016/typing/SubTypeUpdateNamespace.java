package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * Finalizes the parent Namespace, so that when the sub-Type is built,
 * it will be placed into the correct Namespace.
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
public class SubTypeUpdateNamespace
    extends StandardOperation1<SubTypeWorkBench<?>, SubTypeWorkBench<?>>
    implements OperationBody1<SubTypeWorkBench<?>, SubTypeWorkBench<?>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Initializes the sub-TypeBuilder for us, setting the name, namespace,
    // and so on.
    private final SubTypeRename rename =
        new SubTypeRename ( "sub_type_rename" );


    /**
     * <p>
     * Creates a new SubTypeUpdateNamespace with the specified name, to be used
     * for a unique SubType operation identifier in SymbolTables.
     * </p>
     *
     * @param name The name for a unique identifier of this SubType operation.
     *             Every SubType operation must have a unique ID within
     *             each SymbolTable.  Must not be null.
     */
    public SubTypeUpdateNamespace (
                                   String name
                                   )
        throws ParametersMustNotBeNull.Violation
    {
        super ( name,
                SubTypeWorkBench.TYPE,
                SubTypeWorkBench.TYPE,
                null ); // body = this.
    }


    /**
     * @see musaico.foundation.typing.OperationBody1#evaluateBody(musaico.value.Value)
     */
    @Override
    public Value<SubTypeWorkBench<?>> evaluateBody (
            Value<SubTypeWorkBench<?>> in
            )
    {
        for ( SubTypeWorkBench<?> workbench : in )
        {
            this.subType ( workbench );
        }

        return in;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public SubTypeUpdateNamespace rename (
                                          String name
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        return new SubTypeUpdateNamespace ( name );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public SubTypeUpdateNamespace retype (
                                          String name,
                                          OperationType<? extends Operation<SubTypeWorkBench<?>>, SubTypeWorkBench<?>> type
                                          )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new SubTypeUpdateNamespace ( name );
    }


    /**
     * <p>
     * Prepares one (of possibly many) TypeBuilder(s) for creating
     * the sub-type.
     * </p>
     */
    @SuppressWarnings("unchecked") // Symbol - Type<VALUE>.
    protected <VALUE extends Object>
        void subType (
                      SubTypeWorkBench<VALUE> workbench
                      )
        throws ReturnNeverNull.Violation
    {
        // Copy all the parent Type's Symbols.
        final Type<VALUE> parent_type = workbench.parentType ();
        final TypeBuilder<VALUE> type_builder = workbench.typeBuilder ();
        type_builder.symbolTable ().addAll ( parent_type );

        // Re-initialize the sub-TypeBuilder name,
        // temporary namespace, and so on,
        this.rename.subType ( workbench );

        final Type<VALUE> root_type = (Type<VALUE>)
            parent_type.symbol ( TypeIDs.ROOT_TYPE ).orDefault ( parent_type );

        final Namespace parent_namespace =
            type_builder.namespace ();
        type_builder.setSymbol ( NamespaceID.PARENT, parent_namespace );
    }
}
