package musaico.foundation.typing;

import java.io.Serializable;


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
import musaico.foundation.value.NoneConstant;
import musaico.foundation.value.NoneGenerator;
import musaico.foundation.value.NoneGuesser;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * No Type at all.  Not very useful.
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
public class NoType<VALUE extends Object>
    implements Type<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( NoType.class );


    // Checks method obligations and guarantees, and tracks violations.
    private final TrackingContracts contracts;

    // The unique identifier for this NoType.
    private final TypeID id;

    // The value class of this NoType.  Every "none" value
    //  is an instance of this class.
    private final Class<VALUE> valueClass;

     // The violation of some contract which led to this NoType.
    private final TypingViolation violation;

    // The default "no value" generator for callers who want to fall back
    //  on some non-null, non-exception-inducing default value.
    private final NoneGenerator<VALUE> noneGenerator;

    // The SymbolTable containing any child Types, Constraints,
    // Operations, and so on for this NoType, as well
    // as the parent Namespace and so on ancestors.
    private final SymbolTable symbolTable;

    // The Metadata for this NoType.
    private final Metadata metadata;


    /**
     * <p>
     * Creates a new NoType for the specified class of objects, with
     * the specified violation cause, and the specified "none" value.
     * </p>
     *
     * @param parent_namespace The parent Namespace in which this
     *                         NoType is being created.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param type_name The name of this new NoType.  The name will be
     *                  used to create a SymbolID for this NoType.
     *                  For example, "notype".  Must not be null.
     *
     * @param value_class The class of objects represented by this NoType.
     *                    Must not be null.
     *
     * @param violation The TypingViolation which led to the creation of
     *                  this NoType.  Must not be null.
     *
     * @param none The default value to fall back on, for callers who do
     *             not want to deal with nulls or exceptions
     *             (<code> Value.orNone () </code> ).
     *             Must not be null.
     */
    public NoType (
                   Namespace parent_namespace,
                   String type_name,
                   Class<VALUE> value_class,
                   TypingViolation violation,
                   VALUE none
                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent_namespace,
               type_name,
               value_class,
               violation,
               new NoneConstant<VALUE> ( none ),
               new StandardMetadata () );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               none );
    }


    /**
     * <p>
     * Creates a new NoType for the specified class of objects, with
     * the specified violation cause, and the specified "none" value.
     * </p>
     *
     * @param parent_namespace The parent Namespace in which this
     *                         NoType is being created.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param type_name The name of this new NoType.  The name will be
     *                  used to create a SymbolID for this NoType.
     *                  For example, "notype".  Must not be null.
     *
     * @param value_class The class of objects represented by this NoType.
     *                    Must not be null.
     *
     * @param violation The TypingViolation which led to the creation of
     *                  this NoType.  Must not be null.
     *
     * @param none The default value to fall back on, for callers who do
     *             not want to deal with nulls or exceptions
     *             (<code> Value.orNone () </code> ).
     *             Must not be null.
     *
     * @param metadata The Metadata for this NoType.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public NoType (
                   Namespace parent_namespace,
                   String type_name,
                   Class<VALUE> value_class,
                   TypingViolation violation,
                   VALUE none,
                   Metadata metadata
                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent_namespace,
               type_name,
               value_class,
               violation,
               new NoneConstant<VALUE> ( none ),
               metadata,
               new SymbolTable () );
    }


    /**
     * <p>
     * Creates a new NoType for the specified class of objects, with
     * the specified violation cause, and the specified "none" value.
     * </p>
     *
     * @param parent_namespace The parent Namespace in which this
     *                         NoType is being created.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param type_name The name of this new NoType.  The name will be
     *                  used to create a SymbolID for this NoType.
     *                  For example, "notype".  Must not be null.
     *
     * @param value_class The class of objects represented by this NoType.
     *                    Must not be null.
     *
     * @param violation The TypingViolation which led to the creation of
     *                  this NoType.  Must not be null.
     *
     * @param none_generator Generates the default value to fall back on,
     *                       for callers who do not want to deal with
     *                       nulls or exceptions
     *                       (<code> Value.orNone () </code> ).
     *                       Must not be null.
     *
     * @param metadata The Metadata for this NoType.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public NoType (
                   Namespace parent_namespace,
                   String type_name,
                   Class<VALUE> value_class,
                   TypingViolation violation,
                   NoneGenerator<VALUE> none_generator,
                   Metadata metadata
                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent_namespace,
               type_name,
               value_class,
               violation,
               none_generator,
               metadata,
               new SymbolTable () );
    }


    /**
     * <p>
     * For use by <code> NoType.rename ( ... ) </code> only.
     * </p>
     */
    private NoType (
                    Namespace parent_namespace,
                    String type_name,
                    Class<VALUE> value_class,
                    TypingViolation violation,
                    NoneGenerator<VALUE> none_generator,
                    Metadata metadata,
                    SymbolTable symbol_table
                    )
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type_name, value_class, violation,
                               none_generator, metadata, symbol_table );

        this.id = new TypeID ( type_name,           // raw type name
                               "",                  // tag_names
                               Visibility.PUBLIC ); // visibility

        this.valueClass = value_class;
        this.violation = violation;
        this.noneGenerator = none_generator;
        System.err.println ("!!! no type "+this.id +" none generator = " + this.noneGenerator+" class = "+this.valueClass); new Exception("!!! this is not real exception").printStackTrace();

        this.symbolTable = symbol_table;
        this.symbolTable.set ( NamespaceID.PARENT, parent_namespace );

        this.metadata = metadata;

        this.contracts = new TrackingContracts ( this, this.metadata );
    }


    /**
     * <p>
     * Creates the most basic NoTypes of all, Type.NONE and Kind.NONE.
     * </p>
     *
     * <p>
     * Package private.  Used only by Type.NONE and Kind.NONE.
     * </p>
     */
    @SuppressWarnings("unchecked") // Casts are deliberate and pkg private.
    NoType (
            String raw_type_name
            )
    {
        this.id = new TypeID ( raw_type_name, // raw type name
                               "" );          // tag_names

        this.valueClass = (Class<VALUE>) Object.class;
        this.noneGenerator = new NoneConstant<VALUE> ( (VALUE) "" );

        this.symbolTable = new SymbolTable ();

        this.metadata = new StandardMetadata ();

        this.violation = TypingViolation.NONE;

        this.contracts =
            new TrackingContracts ( "kind notype", this.metadata );
    }


    /**
     * For Type.NONE and Kind.NONE only.
     */
    NoType<VALUE> finishConstructing ()
    {
        this.symbolTable.set ( NamespaceID.PARENT, Namespace.NONE );

        return this;
    }


    /**
     * @see musaico.foundation.typing.Type#builder()
     */
    @Override
    public NoTypedValueBuilder<VALUE> builder ()
    {
        return new NoTypedValueBuilder<VALUE> ( this, this.violation );
    }


    /**
     * @see musaico.foundation.typing.Type#checkValue(musaico.foundation.value.Value)
     */
    @Override
    public void checkValue (
                            Value<?> value
                            )
        throws ParametersMustNotBeNull.Violation,
               TypingViolation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value );

        // Any value is fine, even No value.
    }


    /**
     * @see musaico.foundation.typing.Namespace#containsSymbol()
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
    public <VIOLATION extends Throwable & Violation>
        Error<VALUE> errorValue (
                                 VIOLATION violation
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               violation );

        return new Error<VALUE> ( this.valueClass,
                                  violation );
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
            // Any Symbol != null.
            return false;
        }
        else if ( object == this )
        {
            // Any Symbol == itself.
            return true;
        }
        else if ( object.getClass () != this.getClass () )
        {
            // Any Symbol != any other class of object.
            return false;
        }

        final NoType<?> that = (NoType<?>) object;
        if ( this.id.equals ( that.id )
             && this.valueClass.equals ( that.valueClass )
             && this.noneGenerator.equals ( that.noneGenerator )
             && this.violation.equals ( that.violation )
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
        return this.id.hashCode ();
    }


    /**
     * @see musaico.foundation.typing.Symbol#id()
     *
     * Final for speed.
     */
    @Override
    public final TypeID id ()
        throws ReturnNeverNull.Violation
    {
        return this.id;
    }


    /**
     * @see musaico.foundation.typing.Type#instance(musaico.foundation.value.Value)
     */
    @Override
    public Term<VALUE> instance (
                                 Value<VALUE> value
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value );

        return new Constant<VALUE> ( this,
                                     this.noValue ( this.violation ),
                                     Metadata.NONE );
    }


    /**
     * @see musaico.foundation.typing.Type#instance(musaico.foundation.value.Value)
     */
    @Override
    @SuppressWarnings("unchecked") // Possible heap pollution / generic vararg
    public Term<VALUE> instance (
                                 VALUE ... elements
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               elements );

        return new Constant<VALUE> ( this,
                                     this.noValue ( this.violation ),
                                     Metadata.NONE );
    }


    /**
     * @see musaico.foundation.typing.Type#isInstance(musaico.foundation.value.Value)
     */
    @Override
    @SuppressWarnings("unchecked") // Deliberate cast, plus catch.
    public boolean isInstance (
                               Value<?> value
                               )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value );

        try
        {
            this.checkValue ( (Value<VALUE>) value );

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
            // Probably a derived NoType implementation.
            // The default checkValue () in this class does nothing.
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


    /**
     * @see musaico.foundation.typing.Type#none()
     */
    @Override
    public VALUE none ()
        throws ReturnNeverNull.Violation
    {
        return this.noneGenerator.none ();
    }


    /**
     * @see musaico.foundation.typing.Type#noValue()
     */
    @Override
    public final <VIOLATION extends Throwable & Violation>
        No<VALUE> noValue ()
        throws ReturnNeverNull.Violation
    {
        return new No<VALUE> ( this.valueClass (),
                               this.violation );
    }


    /**
     * @see musaico.foundation.typing.Type#noValue(java.lang.Throwable)
     */
    @Override
    public <VIOLATION extends Throwable & Violation>
        No<VALUE> noValue (
                           VIOLATION violation
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               violation );

        return new No<VALUE> ( this.valueClass,
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


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public NoType<VALUE> rename (
                                 String name
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.rename ( name, new SymbolTable () );
    }


    /**
     * @see musaico.foundation.typing.Namespace#rename(java.lang.String, musaico.foundation.typing.SymbolTable)
     */
    @Override
    public NoType<VALUE> rename (
                                 String name,
                                 SymbolTable symbol_table
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name, symbol_table );

        final Namespace parent_namespace;
        if ( symbol_table.containsSymbol ( NamespaceID.PARENT ) )
        {
            parent_namespace =
                symbol_table.symbol ( NamespaceID.PARENT ).orNone ();
        }
        else
        {
            // Default to same parent Namespace.
            parent_namespace =
                this.symbol ( NamespaceID.PARENT ).orNone ();
        }

        symbol_table.addAll ( this.symbolTable () );

        return new NoType<VALUE> ( parent_namespace,
                                   name,
                                   this.valueClass,
                                   this.violation,
                                   this.noneGenerator,
                                   this.metadata ().renew (),
                                   symbol_table );
    }


    /**
     * @see musaico.foundation.typing.Type#sub(musaico.foundation.typing.Tag[])
     */
    @Override
    public ZeroOrOne<Type<VALUE>> sub (
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

        // Can't add Tags to a NoType.
        return TypeBuilder.createNoType ( this );
    }


    /**
     * @see musaico.foundation.typing.Namespace#symbol(musaico.foundation.typing.SymbolID, musaico.foundation.typing.Symbol)
     */
    @Override
    public final <SYMBOL extends Symbol>
        ZeroOrOne<SYMBOL> symbol (
                                  SymbolID<SYMBOL> id
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
    public final <SYMBOL extends Symbol>
        Value<SymbolID<SYMBOL>> symbolIDs (
                                           Type<SYMBOL> symbol_type
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
    public final <SYMBOL extends Symbol>
        Value<SYMBOL> symbols (
                               Type<SYMBOL> symbol_type
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
     * @return This Namespace's SymbolTable, for use by the implementing class.
     *         Never null.
     */
    protected SymbolTable symbolTable ()
        throws ReturnNeverNull.Violation
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
     * @see musaico.foundation.typing.Type#from(musaico.foundation.typing.Type)
     */
    @Override
    public <TO extends Object>
        Operation1<VALUE, TO> to (
                                  Type<TO> that
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        return new NoCast<VALUE, TO> ( this, that, this.violation );
    }


    /**
     * @see java.lang.Object#toString()
     *
     * Final for speed.
     */
    @Override
    public final String toString ()
    {
        return this.id.toString ();
    }


    /**
     * @see musaico.foundation.typing.Symbol#type()
     *
     * Final for speed.
     */
    @Override
    public final Kind type ()
    {
        return this.id.type ();
    }


    /**
     * @see musaico.foundation.typing.Namespace#type(java.lang.Class)
     */
    @Override
    public <VALUE extends Object>
        Type<VALUE> typeOf (
                            Class<VALUE> value_class
                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value_class );

        // Is there a parent Namespace?  If so, forward the request to it.
        final Namespace parent_namespace =
            this.symbol ( NamespaceID.PARENT ).orNone ();
        if ( parent_namespace != this )
        {
            return parent_namespace.typeOf ( value_class );
        }

        // No parent Namespace.  Give up and return a NoType.
        final String no_such_type_name =
            "type(" + value_class.getName () + ")";
        final TypeID type_id =
            new TypeID ( no_such_type_name, // raw type name
                         "" );              // tag_names
        final Unregistered unregistered_type =
            new Unregistered ( type_id );
        final TypingViolation violation =
            SymbolMustBeRegistered.CONTRACT.violation ( this,
                                                        unregistered_type );
        final NoType<VALUE> no_type =
            new NoType<VALUE> ( this, // parent_namespace
                                no_such_type_name,
                                value_class,
                                violation,
                                new NoneGuesser<VALUE> ( value_class ),
                                Metadata.NONE );

        return no_type;
    }


    /**
     * @see musaico.foundation.typing.Type#valueClass()
     */
    @Override
    public Class<VALUE> valueClass ()
        throws ReturnNeverNull.Violation
    {
        return this.valueClass;
    }


    /**
     * @return The contract Violation which led to this NoType.  Never null.
     */
    public TypingViolation violation ()
    {
        return this.violation;
    }
}
