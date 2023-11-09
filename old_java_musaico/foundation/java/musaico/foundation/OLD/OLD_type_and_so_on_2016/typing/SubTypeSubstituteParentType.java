package musaico.foundation.typing;

import java.io.Serializable;

import java.util.Map;
import java.util.HashMap;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Value;


/**
 * <p>
 * Substitutes a specific Type or set of Types in a SymbolTable.
 * </p>
 *
 * <p>
 * Can be used, for example, to replace every instance of the parent
 * Type in casts and other operations with a newly created sub-Type,
 * so that the same behaviour is kept, but the Types involved are
 * refined.
 * </p>
 *
 *
 * <p>
 * In Java every Operation must be Serializable in order to
 * play nicely with RMI.
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
public class SubTypeSubstituteParentType
    extends StandardOperation1<SubTypeWorkBench<?>, SubTypeWorkBench<?>>
    implements OperationBody1<SubTypeWorkBench<?>, SubTypeWorkBench<?>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( SubTypeSubstituteParentType.class );


    // Initializes the sub-TypeBuilder for us, setting the name, namespace,
    // and so on.
    private final SubTypeRename rename =
        new SubTypeRename ( "sub_type_rename" );

    // The Type IDs to substitute (the keys) and the Types
    // to substitute them with (the values).
    private final Map<SymbolID<Type<?>>, Type<?>> typesMap;


    /**
     * <p>
     * Creates a new SubTypeSubstituteParentType to substitute the
     * specified target Type in place of the specified source Type.
     * </p>
     *
     * @param name The name for a unique identifier of this SubType operation.
     *             Every SubType operation must have a unique ID within
     *             each SymbolTable.  Must not be null.
     *
     * @param source_type The Type to be substituted.  Must not be null.
     *
     * @param target_type The Type to substitue with.  Must not be null.
     */
    public SubTypeSubstituteParentType (
                                        String name
                                        )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name,
               new HashMap<SymbolID<Type<?>>, Type<?>> () );
    }


    /**
     * <p>
     * Creates a new SubTypeSubstituteParentType to substitute the
     * specified target Type in place of the specified source Type.
     * </p>
     *
     * @param name The name for a unique identifier of this SubType operation.
     *             Every SubType operation must have a unique ID within
     *             each SymbolTable.  Must not be null.
     *
     * @param source_type The Type to be substituted.  Must not be null.
     *
     * @param target_type The Type to substitute with.  Must not be null.
     */
    public SubTypeSubstituteParentType (
                                        String name,
                                        Type<?> source_type,
                                        Type<?> target_type
                                        )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name,
               source_type == null
                   ? null
                   : source_type.id (),
               target_type );
    }


    /**
     * <p>
     * Creates a new SubTypeSubstituteParentType to substitute the
     * specified target Type in place of the specified source Type.
     * </p>
     *
     * @param name The name for a unique identifier of this SubType operation.
     *             Every SubType operation must have a unique ID within
     *             each SymbolTable.  Must not be null.
     *
     * @param source_type_id The type ID to be substituted.  Must not be null.
     *
     * @param target_type The Type to substitute with.  Must not be null.
     */
    public SubTypeSubstituteParentType (
                                        String name,
                                        SymbolID<Type<?>> source_type_id,
                                        Type<?> target_type
                                        )
        throws ParametersMustNotBeNull.Violation
    {
        super ( name,
                SubTypeWorkBench.TYPE,
                SubTypeWorkBench.TYPE,
                null ); // body = this.

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               source_type_id, target_type );

        this.typesMap = new HashMap<SymbolID<Type<?>>, Type<?>> ();

        this.typesMap.put ( source_type_id, target_type );
    }


    /**
     * <p>
     * Creates a new SubTypeSubstituteParentType with the specified
     * map of Type IDs to be substituted (keys) and Types to substitute
     * them with (values).
     * </p>
     *
     * @param name The name for a unique identifier of this SubType operation.
     *             Every SubType operation must have a unique ID within
     *             each SymbolTable.  Must not be null.
     *
     * @param types_map The map of type IDs to be substituted (keys)
     *                  to Types to substitute with (values).
     *                  Must not be null.  Must not contain
     *                  any null keys or values.
     */
    public SubTypeSubstituteParentType (
                                        String name,
                                        Map<SymbolID<Type<?>>, Type<?>> types_map
                                        )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        super ( name,
                SubTypeWorkBench.TYPE,
                SubTypeWorkBench.TYPE,
                null ); // body = this.

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               types_map );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               types_map.keySet () );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               types_map.values () );

        // Create a defensive copy so that the map does not change
        // underneath us.
        this.typesMap = new HashMap<SymbolID<Type<?>>, Type<?>> ( types_map );
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
    public SubTypeSubstituteParentType rename (
                                               String name
                                               )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        return new SubTypeSubstituteParentType ( name,
                                                 this.typesMap );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public SubTypeSubstituteParentType retype (
                                               String name,
                                               OperationType<? extends Operation<SubTypeWorkBench<?>>, SubTypeWorkBench<?>> type
                                               )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new SubTypeSubstituteParentType ( name,
                                                 this.typesMap );
    }


    /**
     * <p>
     * Prepares one (of possibly many) TypeBuilder(s) for creating
     * the sub-type, by substituting substitutable Types.
     * </p>
     */
    @SuppressWarnings("unchecked") // SymbolID<?> - SymbolID<Symbol>,
                                   // Symbol - Type<?>.
    protected void subType (
                            SubTypeWorkBench<?> workbench
                            )
        throws ReturnNeverNull.Violation
    {
        // Initialize the sub-TypeBuilder name, temporary namespace, and so on,
        this.rename.subType ( workbench );

        final Type<?> parent_type = workbench.parentType ();
        final TypeBuilder<?> type_builder = workbench.typeBuilder ();

        // Copy the parent SymbolTable.
        final SymbolTable new_symbol_table = type_builder.symbolTable ();
        new_symbol_table.addAll ( parent_type );

        // Now build the new sub-Type with the same SymbolTable as the
        // parent Type.
        final Type<?> new_sub_type = workbench.subType ().orNull ();

        // If we can't build the sub-Type, then we might as well give
        // up now.  The caller can deal with the exception when they
        // try to build the sub-Type.
        if ( new_sub_type == null )
        {
            return;
        }

        // Substitute all references to the parent Type with
        // the new sub-Type.
        this.typesMap.put ( parent_type.id (), new_sub_type );
        try
        {
            this.substituteTypesIn ( new_symbol_table );
        }
        catch ( TypesMustHaveSameValueClasses.Violation violation )
        {
            // Permanent Violation, prevent the TypeBuilder from continuing.
            type_builder.disable ( violation );
            return;
        }

        // Re-initialize the root Type, which might have been overwritten
        // while replacing the parent Type with the sub-Type.
        this.rename.subType ( workbench );
    }


    /**
     * <p>
     * Substitutes all the substitutable Types within the
     * specified SymbolTable.
     * </p>
     *
     * @param symbol_table The SymbolTable in which Types will be substituted
     *                     (as well as Operations and Terms and any other
     *                     Retypable Symbols).  Must not be null.
     *
     * @return The total number of Types and Retypable Symbols
     *         substituted in the specified SymbolTable.
     *         Can be 0, if no substitutions were made.
     *         Always greater than or equal to 0.
     */
    @SuppressWarnings("unchecked") // Cast Type<Symbol> - Type<Retypable>.
    public int substituteTypesIn (
                                  SymbolTable symbol_table
                                  )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  symbol_table );

        // Count the number of Types and/or whole Retypables we substitute.
        int num_substitutions = 0;

        for ( SymbolType symbol_type : symbol_table.symbolTypes () )
        {
            if ( symbol_type instanceof Kind )
            {
                // One or more Types in the SymbolTable might need replacing.
                final Kind kind = (Kind) symbol_type;
                for ( SymbolID<Type<?>> symbol_id
                          : symbol_table.symbolIDs ( kind ) )
                {
                    final Type<?> type =
                        symbol_table.symbol ( symbol_id ).orNone ();
                    final Type<?> substitute =
                        this.substitute ( symbol_table,
                                          symbol_id, type );

                    if ( substitute != type )
                    {
                        num_substitutions ++;
                    }
                }
            }
            else if ( Retypable.class.isAssignableFrom ( symbol_type.asType ().valueClass () ) )
            {
                // One or more Retypable Symbols in the SymbolTable
                // might need replacing.
                final Type<? extends Retypable<Symbol, Type<Symbol>>> retypable_symbol_type =
                    (Type<? extends Retypable<Symbol, Type<Symbol>>>)
                    symbol_type.asType ();
                num_substitutions +=
                    this.substituteRetypablesIn ( symbol_table,
                                                  retypable_symbol_type );
            }
        }

        return num_substitutions;
    }


    /**
     * <p>
     * Check to see whether the specified Type should be substituted;
     * and substitute it in the specified SymbolTable.
     * </p>
     *
     * @param symbol_table The SymbolTable in which to (possibly) substitute
     *                     the specified Type.  Must not be null.
     *
     * @param symbol_id The SymbolID to (possibly) substitute.
     *                  Must not be null.
     *
     * @param type The Type, keyed by the specified SymbolID, which will
     *             (possibly) be replaced.  Must not be null.
     *
     * @return The substitute Type, or the specified Type if no substitution
     *         occurred.  Never null.
     */
    protected Type<?> substitute (
                                  SymbolTable symbol_table,
                                  SymbolID<Type<?>> type_id,
                                  Type<?> type
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  symbol_table, type_id, type );

        final Type<?> substitute = this.typesMap.get ( type.id () );
        if ( substitute == null )
        {
            return type;
        }

        // Substitute the Type.
        symbol_table.set ( type_id, substitute );

        return substitute;
    }


    /**
     * <p>
     * Substitutes the specified Retypable, if necessary, with a new
     * one of the specified retypable Symbol Type.
     * </p>
     *
     * @param retypable The Retypable Symbol to be substituted.
     *                  Must not be null.
     *
     * @param substitute_type The retypable Symbol Type for the
     *                        substitute Retypable Symbol.  Must not be null.
     *
     * @return The substituted Retypable Symbol, with the specified substitute
     *         retypable Symbol Type.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Retypable<?> - RETYPABLE.
    protected <RETYPABLE extends Retypable<SYMBOL, SYMBOL_TYPE>, SYMBOL extends Symbol, SYMBOL_TYPE extends Type<? extends SYMBOL>>
        RETYPABLE substituteRetypable (
                                       RETYPABLE retypable,
                                       Type<RETYPABLE> substitute_retypable_type
                                       )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  retypable, substitute_retypable_type );

        final RETYPABLE substitute;
        try
        {
            substitute = (RETYPABLE)
                retypable.retype ( retypable.id ().name (),
                                   (SYMBOL_TYPE) substitute_retypable_type );
        }
        catch ( TypesMustHaveSameValueClasses.Violation violation )
        {
            throw violation;
        }
        catch ( ClassCastException e )
        {
            final Type<? extends Symbol> retypable_type =
                retypable.type ().asType ();
            final TypesMustHaveSameValueClasses contract =
                new TypesMustHaveSameValueClasses ( retypable_type );
            throw contract.violation ( this,
                                       substitute_retypable_type,
                                       e );
        }

        return substitute;
    }


    /**
     * <p>
     * Substitutes the specified retypable Symbol Type, if necessary,
     * with a new one, containing substituted input and output Types.
     * </p>
     *
     * @param retypable_type The retypable Symbol Type to substitute.
     *                       Must not be null.
     *
     * @return The substitute retypable Symbol Type, if its input/output Types
     *         were substituted, or the specified retypable Symbol Type, if no
     *         substitutions were necessary.  Never null.
     */
    protected <RETYPABLE extends Retypable<SYMBOL, SYMBOL_TYPE>, SYMBOL extends Symbol, SYMBOL_TYPE extends Type<? extends SYMBOL>>
        Type<RETYPABLE> substituteRetypableType (
                                                 Type<RETYPABLE> retypable_type
                                                 )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  retypable_type );

        final SymbolTable retypable_type_symbol_table = new SymbolTable ();
        // Throws TypesMustHaveSameValueClasses.Violation:
        final Type<RETYPABLE> substitute_retypable_type =
            retypable_type.rename ( retypable_type.id ().name (),
                                    retypable_type_symbol_table );

        final int num_retypable_type_substitutions =
            this.substituteTypesIn ( retypable_type_symbol_table );

        if ( num_retypable_type_substitutions == 0 )
        {
            // No need to substitute any Retypable Symbols of this Type.
            return retypable_type;
        }
        else
        {
            return substitute_retypable_type;
        }
    }




    /**
     * <p>
     * Substitutes any/all Retypable(s) of the specified retypable
     * Symbol Type in the specified SymbolTable which reference
     * substitutable Types.
     * </p>
     *
     * @param symbol_table The SymbolTable in which Retypables
     *                     will be substituted.  Must not be null.
     *
     * @param retypable_type The Type of Retypables to substitute, if the
     *                       retypable Symbol Type refers to substitutable
     *                       Types.  Must not be null.
     *
     * @return The number of Retypables which were substituted in the
     *         SymbolTable.  Always greater than or equal to 0.
     */
    @SuppressWarnings("unchecked") // Cast SymbolID<Sym>-SymbolID<RETYPABLE>.
    protected <RETYPABLE extends Retypable<SYMBOL, SYMBOL_TYPE>, SYMBOL extends Symbol, SYMBOL_TYPE extends Type<SYMBOL>>
        int substituteRetypablesIn (
                                    SymbolTable symbol_table,
                                    Type<RETYPABLE> retypable_type
                                    )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  symbol_table, retypable_type );

        final Type<RETYPABLE> substitute_retypable_type =
            this.substituteRetypableType ( retypable_type );

        if ( substitute_retypable_type == retypable_type )
        {
            // No need to substitute any Retypables of this RetypableType.
            return 0;
        }

        // We are going to substitute all Retypables of this
        // retypable Symbol Type.
        int num_substitutions = 0;
        for ( SymbolID<RETYPABLE> retypable_id
                  : symbol_table.symbolIDs ( retypable_type ) )
        {
            final RETYPABLE retypable =
                symbol_table.symbol ( retypable_id ).orNone ();
            final RETYPABLE substitute =
                this.substituteRetypable ( retypable,
                                           substitute_retypable_type );

            try
            {
                symbol_table.remove ( retypable_id );
            }
            catch ( SymbolMustBeInTable.Violation violation )
            {
                // Should be impossible!
                final ReturnNeverNull.Violation cannot_fulfill_guarantee =
                    ReturnNeverNull.CONTRACT.violation ( this,
                                                         violation );
                throw cannot_fulfill_guarantee;
            }

            symbol_table.set ( (SymbolID<RETYPABLE>) substitute.id (),
                               substitute );

            num_substitutions ++;
        }

        return num_substitutions;
    }
}
