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
 * Sets up a TypeBuilder for sub-typing, setting the name of the new
 * sub-Type, the tag names, and so on.
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
public class SubTypeRename
    extends StandardOperation1<SubTypeWorkBench<?>, SubTypeWorkBench<?>>
    implements OperationBody1<SubTypeWorkBench<?>, SubTypeWorkBench<?>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new SubTypeRename with the specified name, to be used
     * for a unique SubType operation identifier in SymbolTables.
     * </p>
     *
     * @param name The name for a unique identifier of this SubType operation.
     *             Every SubType operation must have a unique ID within
     *             each SymbolTable.  Must not be null.
     */
    public SubTypeRename (
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
    public SubTypeRename rename (
                                 String name
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        return new SubTypeRename ( name );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public SubTypeRename retype (
                                 String name,
                                 OperationType<? extends Operation<SubTypeWorkBench<?>>, SubTypeWorkBench<?>> type
                                 )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new SubTypeRename ( name );
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
        final Type<VALUE> parent_type = workbench.parentType ();
        final TypeBuilder<VALUE> type_builder = workbench.typeBuilder ();
        final Symbol [] symbols_to_add = workbench.symbolsToAdd ();

        final String raw_type_name = parent_type.id ().rawTypeName ();
        type_builder.rawTypeName ( raw_type_name );

        final String parent_tag_names = parent_type.id ().tagNames ();
        final String child_tag_names = workbench.tagNames ();
        final String tag_names;
        if ( parent_tag_names.equals ( "" ) )
        {
            tag_names = child_tag_names;
        }
        else if ( child_tag_names.equals ( "" ) )
        {
            tag_names = parent_tag_names;
        }
        else if ( child_tag_names.equals ( parent_tag_names ) )
        {
            // The parent type is a sub-type that already has the full name.
            tag_names = parent_tag_names;
        }
        else
        {
            tag_names = parent_tag_names + "," + child_tag_names;
        }
        type_builder.tagNames ( tag_names );

        type_builder.none ( parent_type.none () );

        // Until the Type is ready to be finalized by SubTypeUpdateNamespace,
        // all created sub-Types will be placed into temporary parent
        // Namespaces to avoid collision with each other.
        final Namespace parent_root_namespace =
            new StandardRootNamespace ( Namespace.NONE,
                                        "temporary_namespace",
                                        new SymbolTable () );
        type_builder.namespace ( parent_root_namespace );

        final Type<VALUE> root_type = (Type<VALUE>)
            parent_type.symbol ( TypeIDs.ROOT_TYPE ).orDefault ( parent_type );
        type_builder.setSymbol ( TypeIDs.ROOT_TYPE, root_type );
    }
}
