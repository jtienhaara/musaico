package musaico.foundation.typing;

import java.io.Serializable;

import java.util.ArrayList;


import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;
import musaico.foundation.metadata.TrackingContracts;

import musaico.foundation.value.Error;
import musaico.foundation.value.No;
import musaico.foundation.value.NonBlocking;
import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueMustNotBeEmpty;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * Base implementation for Symbol Types, such as NamespaceType,
 * Kind, TagType, ConstraintType, OperationType,
 * and so on.
 * </p>
 *
 * <p>
 * Every AbstractSymbolType must implement:
 * </p>
 *
 * <ul>
 *   <li> <code> id () </code> </li>
 *   <li> <code> none () </code> </li>
 *   <li> <code> toString () </code> </li>
 *   <li> <code> valueClass () </code> </li>
 * </ul>
 *
 * <p>
 * As well every AbstractSymbolType implementation in
 * musaico.foundation.typing must override the
 * <code> finishConstructing () </code> method to return itself as
 * its own class, and call the method from Namespace.java to
 * add the Namespace.ROOT as parent namespace.
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
public abstract class AbstractSymbolType<SYMBOL extends Symbol>
    implements Type<SYMBOL>, SymbolType, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AbstractSymbolType.class );


    // Checks method obligations and guarantees, tracks violations.
    private final TrackingContracts contracts;

    // The raw Type name of this symbol type.
    private final String rawTypeName;

    // The SymbolTable containing any child Types, Constraints,
    // Operations, and so on for this NoType, as well
    // as the parent Namespace and so on ancestors.
    private final SymbolTable symbolTable;

    // The Metadata for this AbstractSymbolTable.
    private final Metadata metadata;


    /**
     * <p>
     * Creates a new AbstractSymbolType with the specified SymbolTable.
     * </p>
     *
     * @param raw_type_name The name of this new Type.  Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Type.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardType,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this AbstractSymbolType.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    AbstractSymbolType (
                        String raw_type_name,
                        SymbolTable symbol_table,
                        Metadata metadata
                        )
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               symbol_table, metadata );

        this.rawTypeName = raw_type_name;
        this.symbolTable = symbol_table;
        this.metadata = metadata;

        this.contracts =
            new TrackingContracts ( raw_type_name, this.metadata );
    }


    /**
     * @see musaico.foundation.typing.SymbolType#asType()
     */
    @Override
    public final Type<SYMBOL> asType ()
    {
        return this;
    }


    /**
     * @see musaico.foundation.typing.Type#builder()
     */
    @Override
    public final TypedValueBuilder<SYMBOL> builder ()
    {
        return new TypedValueBuilder<SYMBOL> ( this );
    }


    /**
     * @see musaico.foundation.typing.Type#checkValue(musaico.foundation.value.Value)
     */
    @Override
    public final void checkValue (
                                  Value<?> value
                                  )
        throws ParametersMustNotBeNull.Violation,
               TypingViolation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value );

        final Class<SYMBOL> value_class = this.valueClass ();
        if ( ! value_class.isAssignableFrom ( value.expectedClass () ) )
        {
            // Not even the right class of value(s).
            final ValueMustBeInstanceOfClass contract =
                new ValueMustBeInstanceOfClass ( value_class );
            final TypingViolation violation =
                contract.violation ( this,
                                     value );
            throw violation;
        }

        // Throws a TypingViolation if any of this Type's constraints
        // is not met:
        new ConstraintChecker ().check ( this, value );
    }


    /**
     * @see musaico.foundation.typing.Symbol#containsSymbol(musaico.foundation.typing.SymbolID)
     */
    @Override
    public final boolean containsSymbol (
                                         SymbolID<?> id
                                         )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               id );

        return this.symbolTable.containsSymbol ( id );
    }


    /**
     * @see musaico.foundation.typing.Type#errorValue(java.lang.Throwable)
     */
    @Override
    public final <VIOLATION extends Throwable & Violation>
        Error<SYMBOL> errorValue (
                                  VIOLATION violation
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               violation );

        return new Error<SYMBOL> ( this.valueClass (),
                                   violation );
    }


    /**
     * @return The ObjectContracts for this SymbolType.
     *         Checks method obligations and guarantees, tracks violations.
     *         Never null.
     */
    protected final TrackingContracts contracts ()
    {
        return this.contracts;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    @SuppressWarnings("unchecked") // Try...catch is apparently unchecked.
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == null )
        {
            // Any AbstractSymbolType != null.
            return false;
        }
        else if ( object == this )
        {
            // Any AbstractSymbolType == itself.
            return true;
        }
        else if ( ! ( object instanceof AbstractSymbolType ) )
        {
            // Any AbstractSymbolType != any other class of object.
            return false;
        }

        final AbstractSymbolType<?> that = (AbstractSymbolType<?>) object;
        final Symbol this_none = this.none ();
        final Symbol that_none = that.none ();
        if ( this.id ().name ().equals ( that.id ().name () )
             && this.valueClass ().equals ( that.valueClass () )
             && (
                 this_none == that_none
                 || this_none.id ().name ().equals ( that_none.id ().name () )
                )
             && this.symbolTable.equals ( that.symbolTable ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.Namespace#findMetadatum(java.lang.Class)
     */
    @Override
    public final <METADATUM extends Serializable>
        ZeroOrOne<METADATUM> findMetadatum (
                                            Class<METADATUM> metadatum_class
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final Metadata metadata = this.metadata ();
        if ( metadata.has ( metadatum_class ) )
        {
            return metadata.get ( metadatum_class );
        }

        final ZeroOrOne<Namespace> v_parent_namespace =
            this.symbol ( NamespaceID.PARENT );
        final Namespace parent_namespace = v_parent_namespace.orNone ();
        if ( ! v_parent_namespace.hasValue ()
             || parent_namespace == this )
        {
            // Let our Metadata generate a No<METADATUM>.
            return metadata.get ( metadatum_class );
        }

        // Search up the hierarchy of Namespaces.
        return parent_namespace.findMetadatum ( metadatum_class );
    }


    /**
     * @see java.lang.Object#hashCode()
     *
     * Final for speed.
     */
    @Override
    public final int hashCode ()
    {
        return this.getClass ().getName ().hashCode () * 31
            + this.id ().name ().hashCode ();
    }


    // Every AbstractSymbolType must implement id ().


    /**
     * @see musaico.foundation.typing.Type#instance(musaico.foundation.value.Value)
     */
    @Override
    public Term<SYMBOL> instance (
                                  Value<SYMBOL> value
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value );

        Value<SYMBOL> final_value = null;
        try
        {
            this.checkValue ( value );

            // OK value.
            final_value = value;
        }
        catch ( TypingViolation violation )
        {
            // Invalid value.
            final_value = this.errorValue ( violation );
        }

        final Term<SYMBOL> instance;
        if ( final_value instanceof NonBlocking )
        {
            // The value is non-blocking, so return a constant.
            final NonBlocking<SYMBOL> non_blocking_value =
                (NonBlocking<SYMBOL>) final_value;
            instance =
                new Constant<SYMBOL> ( this,
                                       non_blocking_value,
                                       this.metadata.renew () );
        }
        else
        {
            // The value is blocking for some reason.
            // Return a BlockingConstant to resolve the value.
            instance = new BlockingConstant<SYMBOL> ( this,
                                                      final_value,
                                                      this.metadata.renew () );
        }

        return instance;
    }


    /**
     * @see musaico.foundation.typing.Type#instance(musaico.foundation.value.Value)
     */
    @Override
    @SuppressWarnings("unchecked") // Possible heap pollution / generic vararg
    public Term<SYMBOL> instance (
                                  SYMBOL ... elements
                                  )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object []) elements );
        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               elements );

        final TypedValueBuilder<SYMBOL> builder =
            new TypedValueBuilder<SYMBOL> ( this );
        for ( SYMBOL element : elements )
        {
            builder.add ( element );
        }

        final Value<SYMBOL> value = builder.build ();

        return this.instance ( value );
    }


    /**
     * @see musaico.foundation.typing.Type#isInstance(musaico.foundation.value.Value)
     */
    @Override
    @SuppressWarnings("unchecked") // Deliberate cast, plus catch.
    public final boolean isInstance (
                                     Value<?> value
                                     )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value );

        try
        {
            this.checkValue ( (Value<SYMBOL>) value );

            // No problems detected.
            return true;
        }
        catch ( ClassCastException exception )
        {
            // Not even the right class.  Obviously not an instance!
            return false;
        }
        catch ( TypingViolation violation )
        {
            // For whatever reason, the specified value is not
            // an instance of this type.
            return false;
        }
    }


    /**
     * @see musaico.foundation.typing.Namespace#metadata()
     */
    @Override
    public final Metadata metadata ()
    {
        return this.metadata;
    }


    // Every AbstractSymbolType must implement none ().


    /**
     * @see musaico.foundation.typing.Type#noValue()
     */
    @Override
    public final <VIOLATION extends Throwable & Violation>
        No<SYMBOL> noValue ()
        throws ReturnNeverNull.Violation
    {
        final Iterable<SYMBOL> no_objects = new ArrayList<SYMBOL> ();
        final ValueMustNotBeEmpty.Violation violation =
            ValueMustNotBeEmpty.CONTRACT.violation ( this,
                                                     no_objects );
        return new No<SYMBOL> ( this.valueClass (),
                                violation );
    }


    /**
     * @see musaico.foundation.typing.Type#noValue(java.lang.Throwable)
     */
    @Override
    public final <VIOLATION extends Throwable & Violation>
        No<SYMBOL> noValue (
                            VIOLATION violation
                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               violation );

        return new No<SYMBOL> ( this.valueClass (),
                                violation );
    }


    /**
     * @see musaico.foundation.typing.Namespace#printSymbolTable()
     */
    @Override
    public final String printSymbolTable ()
        throws ReturnNeverNull.Violation
    {
        return this.symbolTable.printSymbolTable ();
    }


    // Every AbstractSymbolType must implement
    // rename ( String ).

    // Every AbstractSymbolType must implement
    // rename ( String, SymbolTable ).


    /**
     * @see musaico.foundation.typing.Type#sub(musaico.foundation.typing.Tag[])
     */
    @Override
    @SuppressWarnings("unchecked") // Force SYMBOL - Type<SYMBOL>,
        // Class<SubTypeWorkBench<?>> - Class<SubTypeWorkBench<SYMBOL>>.
    public final ZeroOrOne<Type<SYMBOL>> sub (
                                              Tag ... tags
                                              )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object[]) tags ); // Avoid varargs mess.
        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               tags );

        Type<SYMBOL> input_type = this;
        ZeroOrOne<Type<SYMBOL>> v_output_type = null;
        for ( Operation1<SubTypeWorkBench<?>, SubTypeWorkBench<?>> sub_type
            : this.symbolTable ().symbols ( SubTypeWorkBench.SUB_TYPE_TYPE ) )
        {
            final SymbolTable tagged_symbol_table = new SymbolTable ();
            final TypeBuilder<SYMBOL> type_builder =
                this.type ().typeBuilder ( this.valueClass (),
                                           tagged_symbol_table );

            final SubTypeWorkBench<SYMBOL> workbench =
                new SubTypeWorkBench<SYMBOL> ( input_type,
                                               type_builder,
                                               tags );
            final One<SubTypeWorkBench<?>> v_workbench =
                new One<SubTypeWorkBench<?>> ( SubTypeWorkBench.TYPE.valueClass (),
                                               workbench );

            // Now perform the sub-typing.
            sub_type.evaluate ( v_workbench );

            v_output_type = workbench.subType ();

            if ( ! v_output_type.hasValue () )
            {
                // A sub-typing Operation aborted.
                return v_output_type;
            }

            input_type = v_output_type.orNone ();
        }

        if ( v_output_type == null )
        {
            final SymbolTable symbol_table =
                new SymbolTable ( this.symbolTable () );
            final TypeBuilder<SYMBOL> type_builder =
                this.type ().typeBuilder ( this.valueClass (),
                                           symbol_table );
            return type_builder.noType ( TypingViolation.NONE );
        }

        return v_output_type;
    }


    /**
     * @see musaico.foundation.typing.Namespace#symbol(musaico.foundation.typing.SymbolID)
     */
    @Override
    public final <SYMBOL_FROM_TABLE extends Symbol>
        ZeroOrOne<SYMBOL_FROM_TABLE> symbol (
                                             SymbolID<SYMBOL_FROM_TABLE> id
                                             )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               id );

        return this.symbolTable.symbol ( id );
    }


    /**
     * @see musaico.foundation.typing.Namespace#symbolIDs(musaico.foundation.typing.Type)
     */
    @Override
    public final <SYMBOL_FROM_TABLE extends Symbol>
        Value<SymbolID<SYMBOL_FROM_TABLE>> symbolIDs (
                                                      Type<SYMBOL_FROM_TABLE> symbol_type
                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               symbol_type );

        return this.symbolTable.symbolIDs ( symbol_type );
    }


    /**
     * @see musaico.foundation.typing.Namespace#symbols(musaico.foundation.typing.Type)
     */
    @Override
    public final <SYMBOL_FROM_TABLE extends Symbol>
        Value<SYMBOL_FROM_TABLE> symbols (
                                          Type<SYMBOL_FROM_TABLE> symbol_type
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               symbol_type );

        return this.symbolTable.symbols ( symbol_type );
    }


    /**
     * @return This AbstractSymbolType's SymbolTable.  For use
     *         by the implementing class only.  Never null.
     */
    protected SymbolTable symbolTable ()
    {
        return this.symbolTable;
    }


    /**
     * @see musaico.foundation.typing.Namespace#symbolTypes()
     */
    @Override
    public final Value<SymbolType> symbolTypes ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return this.symbolTable.symbolTypes ();
    }


    /**
     * @see musaico.foundation.typing.Type#to(musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Force Op<?, ?> to Op<SYMBOL, TO>.
    public final <TO extends Object>
        Operation1<SYMBOL, TO> to (
                                   Type<TO> that
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        final OperationID<Operation1<SYMBOL, TO>, TO> cast_id =
            new OperationID<Operation1<SYMBOL, TO>, TO> ( "cast",
                new OperationType1<SYMBOL, TO> ( this, that ) );

        final Operation1<SYMBOL, TO> cast;
        final Operation1<SYMBOL, TO> this_cast =
            this.symbol ( cast_id ).orNone ();
        if ( ! ( this_cast instanceof NoCast ) )
        {
            // This symbol table has a cast from this to that.
            cast = this_cast;
        }
        else
        {
            // This symbol table has no caster.
            // Try that symbol able instead.
            final Operation1<SYMBOL, TO> that_cast =
                that.symbol ( cast_id ).orNone ();
            if ( that_cast instanceof NoCast )
            {
                cast = this_cast;
            }
            else
            {
                cast = that_cast;
            }
        }

        return cast;
    }


    // Every AbstractSymbolType must implement toString ().


    /**
     * @see musaico.foundation.typing.Symbol#type()
     */
    @Override
    public final Kind type ()
        throws ReturnNeverNull.Violation
    {
        return Kind.ROOT;
    }


    /**
     * @see musaico.foundation.typing.Namespace#typeOf(java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type looked up by class.
    public final <VALUE extends Object>
                          Type<VALUE> typeOf (
                                              Class<VALUE> value_class
                                              )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value_class );

        return Namespace.ROOT.typeOf ( value_class );
    }


    // Every AbstractSymbolType must implement valueClass ().
}
