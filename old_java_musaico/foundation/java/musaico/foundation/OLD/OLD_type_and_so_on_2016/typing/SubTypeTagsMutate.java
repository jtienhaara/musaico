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
 * Mutates a sub-Type according to the Tags being added to the parent Type.
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
public class SubTypeTagsMutate
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
     * Creates a new SubTypeTagsMutate with the specified name, to be used
     * for a unique SubType operation identifier in SymbolTables.
     * </p>
     *
     * @param name The name for a unique identifier of this SubType operation.
     *             Every SubType operation must have a unique ID within
     *             each SymbolTable.  Must not be null.
     */
    public SubTypeTagsMutate (
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
    public SubTypeTagsMutate rename (
                                     String name
                                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        return new SubTypeTagsMutate ( name );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public SubTypeTagsMutate retype (
                                     String name,
                                     OperationType<? extends Operation<SubTypeWorkBench<?>>, SubTypeWorkBench<?>> type
                                     )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new SubTypeTagsMutate ( name );
    }


    /**
     * <p>
     * Prepares one (of possibly many) TypeBuilder(s) for creating
     * the sub-type.
     * </p>
     */
    @SuppressWarnings("unchecked") // SymbolID<?> - SymbolID<Symbol>.
    protected <VALUE extends Object>
        void subType (
                      SubTypeWorkBench<VALUE> workbench
                      )
        throws ReturnNeverNull.Violation
    {
        // Initialize the sub-TypeBuilder name, temporary namespace, and so on,
        this.rename.subType ( workbench );

        final Type<VALUE> parent_type = workbench.parentType ();
        final TypeBuilder<VALUE> type_builder = workbench.typeBuilder ();
        final Symbol [] symbols_to_add = workbench.symbolsToAdd ();

        // Mutate all the Symbols before creating the new Type.
        // Each Symbol has already been mutated by the parent Type and
        // all of its Tags.  We just have to mutate further for
        // the new tags.
        for ( SymbolType symbol_type : parent_type.symbolTypes () )
        {
            for ( SymbolID<?> symbol_id : parent_type.symbolIDs ( symbol_type.asType () ) )
            {
                if ( symbol_id.visibility () == Visibility.PRIVATE )
                {
                    // Skip private SymbolIDs.
                    continue;
                }

                Symbol mutated = parent_type.symbol ( symbol_id ).orNone ();
                Mutation mutation =
                    new Mutation ( (SymbolID<Symbol>) mutated.id (),
                                   mutated );
                Value<Mutation> v_mutation =
                    new One<Mutation> ( Mutation.class,
                                        mutation );
                for ( Symbol maybe_tag : symbols_to_add )
                {
                    if ( ! ( maybe_tag instanceof Tag ) )
                    {
                        // This symbol is not a Tag, so does not get
                        // to mutate anything.
                        continue;
                    }

                    final Tag tag = (Tag) maybe_tag;
                    for ( Operation1<Mutation, Mutation> mutate
                              : tag.symbols ( Mutation.MUTATE_TYPE ) )
                    {
                        v_mutation = mutate.evaluate ( v_mutation );
                    }
                }

                if ( ! v_mutation.hasValue () )
                {
                    // The mutate operation(s) filtered out this Symbol.
                    continue;
                }

                mutation = v_mutation.orNone ();
                if ( ! mutation.symbol ().hasValue () )
                {
                    // The mutate operation(s) filtered out this Symbol.
                    continue;
                }

                mutated = mutation.symbol ().orNone ();

                try
                {
                    // Throws SymbolMustBeUnique.Violation:
                    type_builder.addSymbol ( mutated );
                }
                catch ( SymbolMustBeUnique.Violation violation )
                {
                    type_builder.disable ( violation );
                    return;
                }
            }
        }

        // Add all the symbols, including the child symbols
        // of each Tag, to the symbol table of the sub-Type being created.
        // Only the child contents of Tags get mutated, not the toplevel
        // symbols and Tags themselves.
        for ( int ns = 0; ns < symbols_to_add.length; ns ++ )
        {
            final Symbol new_symbol = symbols_to_add [ ns ];

            try
            {
                // Throws SymbolMustBeUnique.Violation:
                type_builder.addSymbol ( new_symbol );
            }
            catch ( SymbolMustBeUnique.Violation violation )
            {
                type_builder.disable ( violation );
                return;
            }

            if ( ! ( new_symbol instanceof Tag ) )
            {
                // Only Tags have their contents mutated then added
                // to the symbol table of the sub-Type being created.
                // This symbol is not a Tag, so we're done with it.
                continue;
            }

            final Tag tag = (Tag) new_symbol;

            // Now mutate then add the contents of the tag.
            for ( SymbolType symbol_type : tag.symbolTypes () )
            {
                for ( SymbolID<?> symbol_id_from_tag
                          : tag.symbolIDs ( symbol_type.asType () ) )
                {
                    if ( symbol_id_from_tag.visibility ()
                         == Visibility.PRIVATE )
                    {
                        // Skip private SymbolIDs.
                        continue;
                    }

                    Symbol mutated_symbol =
                        tag.symbol ( symbol_id_from_tag ).orNone ();

                    Mutation mutation =
                        new Mutation ( (SymbolID<Symbol>) mutated_symbol.id (),
                                       mutated_symbol );
                    Value<Mutation> v_mutation =
                        new One<Mutation> ( Mutation.class,
                                            mutation );

                    for ( int ts = ns + 1; ts < symbols_to_add.length; ts ++ )
                    {
                        final Symbol subsequent_symbol = symbols_to_add [ ts ];
                        if ( ! ( subsequent_symbol instanceof Tag ) )
                        {
                            // Only Tags get to mutate contents.
                            // This Symbol is not a Tag, so carry on.
                            continue;
                        }

                        final Tag subsequent_tag = (Tag) subsequent_symbol;
                        for ( Operation1<Mutation, Mutation> mutate
                                  : subsequent_tag.symbols ( Mutation.MUTATE_TYPE ) )
                        {
                            v_mutation = mutate.evaluate ( v_mutation );
                        }

                        if ( ! v_mutation.hasValue () )
                        {
                            // The mutate operation filtered out this Symbol.
                            break;
                        }

                        mutation = v_mutation.orNone ();

                        if ( ! mutation.symbol ().hasValue () )
                        {
                            // The mutation filtered out this Symbol.
                            break;
                        }
                    }

                    if ( ! v_mutation.hasValue () )
                    {
                        // The mutate operation filtered out this Symbol.
                        continue;
                    }

                    mutation = v_mutation.orNone ();

                    if ( ! mutation.symbol ().hasValue () )
                    {
                        // The mutation filtered out this Symbol.
                        continue;
                    }

                    mutated_symbol = mutation.symbol ().orNone ();

                    try
                    {
                        // Throws SymbolMustBeUnique.Violation:
                        type_builder.addSymbol ( mutated_symbol );
                    }
                    catch ( SymbolMustBeUnique.Violation violation )
                    {
                        type_builder.disable ( violation );
                        return;
                    }
                }
            }
        }
    }
}
