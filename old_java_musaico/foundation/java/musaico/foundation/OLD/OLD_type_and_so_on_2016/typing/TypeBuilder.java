package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.No;
import musaico.foundation.value.NoneConstant;
import musaico.foundation.value.NoneGenerator;
import musaico.foundation.value.NoneGuesser;
import musaico.foundation.value.One;
import musaico.foundation.value.Unjust;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * Builds up a Type for a specific Kind, one Symbol in the SymbolTable
 * at a time.  Can only create a One (one element) unless any of the
 * Symbols added is invalid (such as a non unique symbol), in which
 * case No type is built.
 * </p>
 *
 * <p>
 * The <code> build () </code> method will fail if the Type being
 * built does not abide by the constraints made by its Kind.  In this
 * case, No value will be returned, and the constraint violation
 * will be encapsulated in the No result.
 * </p>
 *
 * <p>
 * This TypeBuilder is NOT thread-safe.  Do not use it from multiple
 * threads.
 * </p>
 *
 *
 * <p>
 * In Java every TypeBuilder must be Serializable in order to play
 * nicely over RMI.  However be aware that the objects stored inside
 * a TypeBuilder need not be Serializable.  If non-Serializable
 * objects are used in an RMI environment, be prepared for trouble.
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
public class TypeBuilder<VALUE extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( TypeBuilder.class );


    /** The name of every Type-under-construction which has not yet
     *  been name()d. */
    public static final String UNNAMED_TYPE = "unnamed_type";

    /** No TypeBuilder at all. */
    public static final TypeBuilder<Object> NONE =
        new TypeBuilder<Object> ( Kind.ROOT,
                                  Object.class,
                                  new SymbolTable () );


    // Checks method obligations and guarantees.
    private final ObjectContracts contracts;

    // The Kind of Type being built.
    private final Kind kind;

    // The value class of the Type being built, such as String.class
    // or MyObject.class.
    private final Class<VALUE> valueClass;

    // The SymbolTable to which the caller is going to want to add
    //  Symbols (Operations, Constraints, Tags, and so on).
    private final SymbolTable symbolTable;


    // MUTABLE fields:

    // The current violation caused by the fields set so far for
    // the type being built.  Null initially, can change each time
    // one of the fields is set.
    private TypingViolation violation = null;

    // A permanent violation which prevents this TypeBuilder from building
    // any new Types forever.  Only ever set by the user of a TypeBuilder,
    // never set within the TypeBuilder class itself.
    private TypingViolation permanentViolation = null;

    // The raw type name of the Type being built, such as "string" or "MyType".
    // MUTABLE.  Changes whenever rawTypeName ( ... ) is called.
    private String rawTypeName = null;

    // The tag names which decorate the type id being built, such as
    // "positive,odd".
    // MUTABLE.  Changes whenever tagNames ( ... ) is called.
    private String tagNames = null;

    // Metadata for the Type being created.  By default a StandardMetadata,
    // but the caller can override the default by setting metadata ( ... )
    // any time before the Type is built.
    private Metadata metadata = null;

    // The parent Namespace in which the Type will eventually be
    // registered, such as a "primitives" Namespace or Namespace.ROOT.
    // MUTABLE.  Changes whenever namespace ( ... ) is called.
    private Namespace namespace = null;

    // The none value generator of the Type being built,
    // such as a NoneConstant with a specific value, or a NoneGuesser
    // which will be resolved at build () time.
    // MUTABLE.  Changes whenever none ( ... ) is called.
    private NoneGenerator<Object> noneGenerator = null;

    // The Visibility of the TypeID for the Type to be created.
    // Usually Visibility.PUBLIC, unless there is some reason
    // to change it by calling visibility ( ... ).
    // MUTABLE.
    private Visibility visibility = Visibility.PUBLIC;


    /**
     * <p>
     * Creates a new TypeBuilder for the specified Kind of Types.
     * </p>
     *
     * @param kind The Kind of Type being built.  Must not be null.
     *
     * @param value_class The class of values represented by
     *                    the Type being built.  Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for the
     *                     Type being built.  The caller may continue
     *                     to add to the SymbolTable after build () ing
     *                     the Type, but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     */
    public TypeBuilder (
                        Kind kind,
                        Class<VALUE> value_class,
                        SymbolTable symbol_table
                        )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               kind, value_class, symbol_table );

        this.kind = kind;
        this.valueClass = value_class;
        this.symbolTable = symbol_table;

        this.rawTypeName = this.defaultName ();
        this.tagNames = "";
        this.metadata = this.defaultMetadata ();
        this.namespace = this.defaultNamespace ();

        // Add the parent Namespace to the new Type's SymbolTable.
        this.setSymbol ( NamespaceID.PARENT, this.namespace );

        this.noneGenerator = this.defaultNoneGenerator ();

        // Check to see wheher the current default type name has already
        // been registered in the default namespace, whether the
        // none value is an instance of the specified value class,
        // and so on.
        this.setViolationState ( "constructor 1", this );

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * <p>
     * Creates a new TypeBuilder, pre-populated with the specified
     * settings.
     * </p>
     *
     * @param kind The Kind of Type being built.  Must not be null.
     *
     * @param raw_type_name The name of the Type being built, WITHOUT any
     *                      tag decorations.  Must not be null.
     *
     * @param namespace The parent namespace in which the Type being
     *                  built will be registered.  Must not be null.
     *
     * @param none_generator The generator which will create the
     *                       none value for the Type being built.
     *                       Must not be null.
     *
     * @param value_class The class of values represented by
     *                    the Type being built.  Must not be null.
     *
     * @param symbol_table The SymbolTable of operations, constraints
     *                     and so on for the Type being built.
     *                     Must not be null.
     */
    public TypeBuilder (
                        Kind kind,
                        String raw_type_name,
                        Namespace namespace,
                        NoneGenerator<Object> none_generator,
                        Class<VALUE> value_class,
                        SymbolTable symbol_table
                        )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               kind, raw_type_name, namespace, none_generator,
                               value_class, symbol_table );

        this.kind = kind;
        this.valueClass = value_class;
        this.symbolTable = symbol_table;

        this.rawTypeName = raw_type_name;
        this.tagNames = "";
        this.metadata = this.defaultMetadata ();
        this.namespace = namespace;

        // Add the parent Namespace to the new Type's SymbolTable.
        this.setSymbol ( NamespaceID.PARENT, this.namespace );

        this.noneGenerator = none_generator;

        // Check to see wheher the current default type name has already
        // been registered in the default namespace, whether the
        // none value is an instance of the specified value class,
        // and so on.
        this.setViolationState ( "constructor 2", this );

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * <p>
     * Adds the specified Symbol to the SymbolTable of the Type being built.
     * </p>
     *
     * <p>
     * If a Symbol with the same SymbolID already exists in this
     * SymbolTable, then a violation will be thrown.
     * </p>
     *
     * @param symbol The Symbol to add to the SymbolTable of the Type
     *               being built.  Must not be null.
     *
     * @return This TypeBuilder.  Never null.
     *
     * @throws SymbolMustBeUnique.Violation If a Symbol with the same id
     *                                      already exists in this table.
     */
    public TypeBuilder<VALUE> addSymbol (
                                         Symbol symbol
                                         )
        throws ParametersMustNotBeNull.Violation,
               SymbolMustBeUnique.Violation
    {
        this.symbolTable.add ( symbol );

        this.setViolationState ( "addSymbol", symbol );

        return this;
    }


    /**
     * <p>
     * Adds a newly created Type to its parent Namespace, or fails
     * if the parent Namespace already has a Type with the same ID.
     * </p>
     */
    protected void addTypeToNamespace (
                                       Type<VALUE> type,
                                       Namespace parent_namespace
                                       )
        throws SymbolMustBeUnique.Violation
    {
        if ( parent_namespace instanceof RootNamespace )
        {
            // Add the new Type to its parent namespace.
            // If an exception occurs throw it up the stack.
            final RootNamespace parent_root_namespace =
                (RootNamespace) parent_namespace;
            System.err.println ("!!! add type '"+type.id()+"' to namespace '"+parent_namespace.id ()+"'");
            parent_root_namespace.add ( type );
        }
    }


    /**
     * <p>
     * Builds a Type of the specific valueClass.
     * </p>
     *
     * @return One Type on successful building, or No Type if any
     *         of the state of this builder is invalid.
     *         Never null.
     */
    public final ZeroOrOne<Type<VALUE>> build ()
    {
        if ( this.violation != null
             || this.permanentViolation != null )
        {
            return this.noType ();
        }

        final Object none;
        try
        {
            none = this.noneGenerator.none ();
        }
        catch ( Exception e )
        {
            final TypingViolation violation =
                TypeMustBeValid.CONTRACT.violation ( this,
                                                     this,
                                                     e );
            this.violation = violation;
            this.violation.initCause ( e );
            return this.noType ();
        }


        // Create the Type.
        final Type<VALUE> type = this.createType ();


        // Give it a default Read operation, if necessary.
        final Read<VALUE> read = new Read<VALUE> ( type );
        if ( ! this.symbolTable.containsSymbol ( read.id () ) )
        {
            this.symbolTable.set ( read.id (), read );
        }

        final One<VALUE> check_none = new One<VALUE> ( type.valueClass (),
                                                       type.none () );
        try
        {
            type.checkValue ( check_none );
        }
        catch ( TypingViolation violation )
        {
            this.violation = violation;
            return this.noType ();
        }

        final One<Type<VALUE>> maybe_type =
            TypeBuilder.createOneType ( type );
        try
        {
            this.kind.checkValue ( maybe_type );
        }
        catch ( TypingViolation violation )
        {
            this.violation = violation;
            return this.noType ();
        }

        try
        {
            this.addTypeToNamespace ( type, this.namespace () );
        }
        catch ( TypingViolation violation )
        {
            this.violation = violation;
            return this.noType ();
        }

        return maybe_type;
    }


    /**
     * <p>
     * Returns true if the SymbolTable of the Type being created
     * has a Symbol by the specified identifier, false if not.
     * </p>
     *
     * @see musaico.foundation.typing.SymbolTable#containsSymbol(musaico.foundation.typing.SymbolID)
     */
    public boolean containsSymbol (
                                   SymbolID<?> id
                                   )
        throws ParametersMustNotBeNull.Violation
    {
        return this.symbolTable.containsSymbol ( id );
    }


    /**
     * <p>
     * Copies name, namespace and Symbols from the specified Type into
     * the new Type being built.
     * </p>
     *
     * @param type The Type to copy name, namespace and Symbols from.
     *             Must not be null.
     *
     * @return This TypeBuilder.  Never null.
     */
    public TypeBuilder<VALUE> copy (
                                    Type<?> type
                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type );

        this.rawTypeName = type.id ().rawTypeName ();
        this.tagNames = type.id ().tagNames ();

        this.metadata = type.metadata ().renew ();

        this.namespace =
            type.symbol ( NamespaceID.PARENT )
                .orDefault ( Namespace.NONE);

        // Add the parent Namespace to the new Type's SymbolTable.
        this.setSymbol ( NamespaceID.PARENT, this.namespace );

        this.noneGenerator = new NoneConstant<Object> ( type.none () );

        for ( SymbolType symbol_type : type.symbolTypes () )
        {
            for ( SymbolID<? extends Symbol> symbol_id :
                      type.symbolIDs ( symbol_type.asType () ) )
            {
                final Symbol symbol = type.symbol ( symbol_id ).orNone ();

                this.symbolTable.setWithCast ( symbol_id, symbol );
            }
        }

        this.setViolationState ( "copy", type );

        return this;
    }


    /**
     * <p>
     * Helper utility method.  Returns a No&lt;Type&lt;VALUE&gt;&gt;
     * with the specified Type, without any generics warnings
     * or compiler errors.
     * </p>
     *
     * @param no_type The NoType to wrap as No value.  Must not be null.
     *
     * @return No Type.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Class<Type> to Class<Type<VALUE>>.
    public static final <TYPE_VALUE extends Object>
        No<Type<TYPE_VALUE>> createNoType (
                                           NoType<TYPE_VALUE> no_type
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               no_type );

        final Type<TYPE_VALUE> no_type_as_type =
            (Type<TYPE_VALUE>) no_type; // Prevent inconvertible cast.
        return new No<Type<TYPE_VALUE>> ( (Class<Type<TYPE_VALUE>>) no_type_as_type.getClass (),
                                          no_type.violation () );
    }


    /**
     * <p>
     * Helper utility method for UnknownType only.
     * Returns a No&lt;Type&lt;VALUE&gt;&gt;
     * with the specified Type, without any generics warnings
     * or compiler errors.
     * </p>
     *
     * @param unknown_type The UnknownType to wrap as No value.
     *                     Must not be null.
     *
     * @return No Type.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Class<Type> to Class<Type<VALUE>>.
    public static final No<Type<Object>> createNoUnknownType (
                                                              UnknownType unknown_type
                                                              )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               unknown_type );

        final Type<Object> unknown_type_as_type =
            (Type<Object>) unknown_type; // Prevent inconvertible cast.
        return new No<Type<Object>> ( (Class<Type<Object>>) unknown_type_as_type.getClass (),
                                          unknown_type.violation () );
    }


    /**
     * <p>
     * Helper utility method.  Returns a One&lt;Type&lt;VALUE&gt;&gt;
     * with the specified Type, without any generics warnings
     * or compiler errors.
     * </p>
     *
     * @param one_type The Type to wrap as One value.  Must not be null.
     *
     * @return One Type.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Class<Type> to Class<Type<VALUE>>.
    public static final <TYPE_VALUE extends Object>
        One<Type<TYPE_VALUE>> createOneType (
                                             Type<TYPE_VALUE> one_type
                                             )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               one_type );

        return new One<Type<TYPE_VALUE>> ( (Class<Type<TYPE_VALUE>>) one_type.getClass (),
                                           one_type );
    }


    /**
     * <p>
     * Instantiates a Type with the name and SymbolTable and so on.
     * </p>
     *
     * <p>
     * This method will only ever be invoked during build () if and
     * only if the current violation state is null.  That is, if
     * the none value is of the wrong class, or if the Type's
     * name or valueClass has already been registered in the
     * parent namespace, and so on, then this method will never be invoked.
     * </p>
     *
     * @see musaico.foundation.typing.TypeMustBeValid
     *
     * @return The newly created Type with the name, SymbolTable and
     *         so on from this builder.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast none to VALUE.
    protected Type<VALUE> createType ()
            throws ReturnNeverNull.Violation
    {
        final Type<VALUE> type =
            new StandardType<VALUE> (
                                     this.namespace (), // parent_namespace
                                     this.kind (), // kind
                                     this.rawTypeName (), // raw_type_name
                                     this.tagNames (), // tag_names
                                     this.valueClass (), // value_class
                                     (VALUE) this.none (), // none
                                     this.symbolTable (), // symbol_table
                                     this.metadata () ); // metadata

        return type;
    }


    /**
     * @return The default Metadata.
     *         Can be overridden by derived TypeBuilder implementations.
     *         Never null.
     */
    protected Metadata defaultMetadata ()
    {
        return new StandardMetadata ();
    }

    /**
     * @return The initial name of this Type, such as "unnamed_type".
     *         Can be overridden by derived TypeBuilder implementations.
     *         Never null.
     */
    protected String defaultName ()
    {
        return TypeBuilder.UNNAMED_TYPE;
    }

    /**
     * @return The initial parent namespace into which this Type will
     *         be registered, such as Namespace.ROOT.
     *         Can be overridden by derived TypeBuilder implementations.
     *         Never null.
     */
    protected RootNamespace defaultNamespace ()
    {
        return Namespace.ROOT;
    }

    /**
     * @return The initial none value generator for this Type,
     *         such as a NoneConstant or a NoneGuesser.
     *         Can be overridden by derived TypeBuilder implementations.
     *         Never null.
     */
    protected NoneGenerator<Object> defaultNoneGenerator ()
    {
        return new NoneConstant<Object> ( Type.NONE.none () );
    }


    /**
     * <p>
     * Deactivates this TypeBuilder, because of the specified
     * TypingViolation, so that no Type can be built.
     * </p>
     *
     * <p>
     * If this TypeBuilder has previously been deactivated then
     * a call to this method will have no effect.
     * </p>
     *
     * @param violation The TypingViolation which prevents this TypeBuilder
     *                  from creating a new Type.  Must not be null.
     *
     * @return This TypeBuilder.  Never null.
     */
    public final TypeBuilder<VALUE> disable (
                                             TypingViolation violation
                                             )
    {
        this.permanentViolation = violation;

        this.setViolationState ( "disable", violation );

        return this;
    }


    /**
     * @return The Kind of Types built by this TypeBuilder.
     *         Never null.
     */
    public final Kind kind ()
    {
        return this.kind;
    }


    /**
     * @return The raw Type name of the Type being built.  Never null.
     */
    public String rawTypeName ()
        throws ReturnNeverNull.Violation
    {
        return this.rawTypeName;
    }


    /**
     * <p>
     * Sets the raw Type name of the Type being built.
     * </p>
     *
     * @param raw_type_name The raw Type name of the Type being built,
     *                      EXCLUDING tag decorations and so on
     *                      (such as "[encrypted]").  Must not be null.
     *
     * @return This TypeBuilder.  Never null.
     */
    public TypeBuilder<VALUE> rawTypeName (
                                           String raw_type_name
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               raw_type_name );

        this.rawTypeName = raw_type_name;

        this.setViolationState ( "rawTypeName", raw_type_name );

        return this;
    }


    /**
     * @return The Metadata for the Type being built.  Never null.
     */
    public Metadata metadata ()
        throws ReturnNeverNull.Violation
    {
        return this.metadata;
    }


    /**
     * <p>
     * Sets the Metadata for the Type being built, overriding the
     * default Metadata for this builder (and losing any metadata
     * that had already been set, if the caller had set any).
     * </p>
     *
     * @param metadata The Metadata for the Type being built.
     *                 Must not be null.
     *
     * @return This TypeBuilder.  Never null.
     */
    public TypeBuilder<VALUE> metadata (
                                        Metadata metadata
                                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               metadata );

        this.metadata = metadata;

        this.setViolationState ( "metadata", metadata );

        return this;
    }


    /**
     * @return The parent namespace in which the Type being built will
     *         be registered.  Never null.
     */
    public Namespace namespace ()
        throws ReturnNeverNull.Violation
    {
        return this.namespace;
    }


    /**
     * <p>
     * Sets the parent namespace in which the Type being built
     * will be registered.
     * </p>
     *
     * @param namespace The parent namespace to which the Type being built
     *                  will be added.  Must not be null.
     *
     * @return This TypeBuilder.  Never null.
     */
    public TypeBuilder<VALUE> namespace (
                                         Namespace namespace
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               namespace );

        this.namespace = namespace;

        // Add the parent Namespace to the new Type's SymbolTable.
        this.setSymbol ( NamespaceID.PARENT, this.namespace );

        this.setViolationState ( "namespace", namespace );

        return this;
    }


    /**
     * <p>
     * Sets the default sub-typer, which creates a duplicate of the Type,
     * but with some user-specified set of Tags added on the end.
     * </p>
     *
     * @return This TypeBuilder.  Never null.
     */
    @SuppressWarnings("rawtypes") // Raw Operation [] forced by compiler.
    public TypeBuilder<VALUE> defaultSubTyping ()
    {
        final SubTypeSubstituteParentType substitute_parent_type =
            new SubTypeSubstituteParentType ( "sub_type_substitute_parent_type" );
        this.symbolTable.set ( substitute_parent_type.id (),
                               substitute_parent_type );

        final SubTypeTagsMutate tags_mutate =
            new SubTypeTagsMutate ( "sub_type_tags_mutate" );
        this.symbolTable.set ( tags_mutate.id (),
                               tags_mutate );

        final SubTypeUpdateNamespace update_namespace =
            new SubTypeUpdateNamespace ( "sub_type_update_namespace" );
        this.symbolTable.set ( update_namespace.id (),
                               update_namespace );

        this.setViolationState ( "defaultSubTyping",
                                 new Operation []
                                 {
                                     substitute_parent_type,
                                     tags_mutate,
                                     update_namespace
                                 } );

        return this;
    }


    /**
     * @return The none value for the Type being built.
     *         If no none value has been set, but the valueClass has
     *         been set, then Type.NONE.none () is returned by default.
     *         Note that this might mean a conflict between the none
     *         value and the valueClass of the Type being built, leading
     *         to a violation of TypeMustBeValid.CONTRACT.
     *         Never null.
     */
    public Object none ()
        throws ReturnNeverNull.Violation
    {
        try
        {
            return this.noneGenerator.none ();
        }
        catch ( Exception e )
        {
            return Type.NONE.none ();
        }
    }


    /**
     * <p>
     * Sets the none value of the Type being built.
     * </p>
     *
     * @param none The "no value" value to fall back on when Value users
     *             of the Type being built do not want to handle exceptions.
     *             For example the method
     *             <code> Value<NewType> readFromDB () </code> might be
     *             called with <code> readFromDB ().orNone () <code>,
     *             resulting in either one successful value, or the none
     *             value specified here.  Must be an instance of the
     *             valueClass of the Type being built.  Must not be null.
     *
     * @return This TypeBuilder.  Never null.
     */
    public TypeBuilder<VALUE> none (
                                    Object none
                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               none );

        this.noneGenerator = new NoneConstant<Object> ( none );

        this.setViolationState ( "none", none );

        return this;
    }


    /**
     * @return The "no value" generator which will be used to
     *         create a constant when the Type is built, or whenever
     *         the current violation state is checked or
     *         whenever TypeBuilder.none () is invoked.
     *         Never null.
     */
    public NoneGenerator<Object> noneGenerator ()
        throws ReturnNeverNull.Violation
    {
        return this.noneGenerator;
    }


    /**
     * <p>
     * Sets the none generator of the Type being built.
     * </p>
     *
     * @param none The "no value" generator which will be used to
     *             create a constant when the Type is built, or whenever
     *             the current violation state is checked (including
     *             during the invocation of this method), or
     *             whenever TypeBuilder.none () is invoked.
     *             Must not be null.
     *
     * @see musaico.foundation.typing.TypeBuilder#none(java.lang.Object)
     *
     * @return This TypeBuilder.  Never null.
     */
    public TypeBuilder<VALUE> noneGenerator (
                                             NoneGenerator<Object> none_generator
                                             )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               none_generator );

        this.noneGenerator = none_generator;

        this.setViolationState ( "noneGenerator", none_generator );

        return this;
    }


    /**
     * @return A new No Type caused by the most recent TypingViolation,
     *         or by the permanent TypingViolation if this TypeBuilder
     *         was disabled.  Never null.
     */
    protected No<Type<VALUE>> noType ()
    {
        if ( this.permanentViolation != null )
        {
            return this.noType ( this.permanentViolation );
        }
        else
        {
            return this.noType ( this.violation );
        }
    }


    /**
     * <p>
     * Creates No Type from the specified TypingViolation.
     * </p>
     *
     * @param violation The violation of some typing contract which
     *                  led to the failure to create a Type.
     *                  Must not be null.
     *
     * @return A new No Type caused by the specified TypingViolation.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Cast this.noneGen to NoneGen<VALUE>.
    public No<Type<VALUE>> noType (
                                   TypingViolation violation
                                   )
    {
        final NoneGenerator<VALUE> none_generator;
        if ( this.valueClass.isInstance ( this.noneGenerator.none () ) )
        {
            none_generator = (NoneGenerator<VALUE>) this.noneGenerator;
        }
        else
        {
            none_generator = new NoneGuesser<VALUE> ( this.valueClass );
        }

        final NoType<VALUE> no_type =
            new NoType<VALUE> ( this.namespace,
                                this.rawTypeName,
                                this.valueClass,
                                violation,
                                none_generator,
                                this.metadata );
        return TypeBuilder.createNoType ( no_type );
    }


    /**
     * @return All the contents of the SymbolTable of the type being
     *         created, as one big String.  Never null.
     *
     * @see musaico.foundation.typing.SymbolTable#printSymbolTable()
     */
    public String printSymbolTable ()
    {
        return this.symbolTable.printSymbolTable ();
    }


    /**
     * <p>
     * Sets the specified Symbol in the SymbolTable of the Type
     * being built, possibly adding it, or possibly overwriting
     * the existing Symbol under the specified ID.
     * </p>
     *
     * <p>
     * The SymbolID need not match the Symbol's actual ID.  This is used,
     * for example, to store the parent Namespace of a Namespace under
     * "#parent", or the root un-Tagged Type of a Type under "#root",
     * and so on.
     * </p>
     *
     * @param symbol_id The unique identifier under which the Symbol
     *                  will be stored in the SymbolTable of the
     *                  Type being built, such as the
     *                  Symbol's actual id, or
     *                  <code> NamespaceID.PARENT </code>, or
     *                  <code> TypeIDs.ROOT_TYPE </code>, and so on.
     *                  Must not be null.
     *
     * @param symbol The Symbol to add or overwrite in the SymbolTable
     *               of the Type being built.  Must not be null.
     *
     * @return The SymbolTable of the Type being built.  Never null.
     */
    public <SYMBOL extends Symbol>
        TypeBuilder<VALUE> setSymbol (
                                      SymbolID<SYMBOL> symbol_id,
                                      SYMBOL symbol
                                      )
        throws ParametersMustNotBeNull.Violation
    {
        this.symbolTable.set ( symbol_id, symbol );

        this.setViolationState ( "setSymbol",
                                 "" + symbol_id + " = " + symbol );

        return this;
    }


    /**
     * <p>
     * Checks whether the Type being built is in a good state.
     * If it is ready to be built, the current violation state pf
     * this TypeBuilder is set to null.  If it is not yet valid,
     * then the violation is set accordingly.
     * </p>
     *
     * @return True if the Type is ready to be built, false if it
     *         is invalid.
     */
    protected boolean setViolationState (
                                         String update_type,
                                         Object updated_data
                                         )
    {
        if ( this.permanentViolation != null )
        {
            this.violation = permanentViolation;
            return false;
        }
        else if ( TypeMustBeValid.CONTRACT.filter ( this ).isKept () )
        {
            // Type is ready to be built.
            this.violation = null;
            return true;
        }
        else
        {
            // Type is invalid, cannot build yet.
            // If there was already a violation, leave it as is.
            // If the violation was previously null, set it now.
            if ( this.violation == null )
            {
                final String plaintiff =
                    "" + this
                    + "\n"
                    + update_type
                    + " ( "
                    + updated_data
                    + " )";
                this.violation =
                    TypeMustBeValid.CONTRACT.violation ( plaintiff,
                                                         this );
            }

            return false;
        }
    }


    /**
     * <p>
     * Looks up the specified Symbol in the SymbolTable of the Type
     * being built.
     * </p>
     *
     * @see musaico.foundation.typing.SymbolTable#symbol(musaico.foundation.typing.SymbolID)
     */
    public <SYMBOL extends Symbol>
        ZeroOrOne<SYMBOL> symbol (
                                  SymbolID<SYMBOL> id
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.symbolTable.symbol ( id );
    }


    /**
     * <p>
     * Returns all SymbolIDs of a specific Symbol Type from
     * the SymbolTable of the Type being created.
     * For example, to retrieve all Tag IDs, call
     * <code> symbolIDs ( Tag.TYPE ) </code>.
     * </p>
     *
     * @see musaico.foundation.typing.SymbolTable#symbolIDs(musaico.foundation.typing.Type)
     */
    public <SYMBOL extends Symbol>
        Value<SymbolID<SYMBOL>> symbolIDs (
                                           Type<SYMBOL> symbol_type
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return this.symbolTable.symbolIDs ( symbol_type );
    }


    /**
     * <p>
     * Returns all Symbols of a specific Symbol Type from
     * the SymbolTable of the Type being built.
     * For example, to retrieve all Tags, call
     * <code> symbols ( Tag.TYPE ) </code>.
     * </p>
     *
     * @see musaico.foundation.typing.SymbolTable#symbols(musaico.foundation.typing.Type)
     */
    public <SYMBOL extends Symbol>
        Value<SYMBOL> symbols (
                               Type<SYMBOL> symbol_type
                               )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return this.symbolTable.symbols ( symbol_type );
    }


    /**
     * @return The SymbolTable for the Type being built.
     *         Be careful, do not hand out the SymbolTable, and
     *         do not modify it once the Type being built is in use.
     *         Never null.
     */
    public final SymbolTable symbolTable ()
    {
        return this.symbolTable;
    }


    /**
     * <p>
     * Returns all Types of Symbols stored
     * in the SymbolTable of the Type being built.
     * </p>
     *
     * @see musaico.foundation.typing.SymbolTable#symbolTypes()
     */
    public Value<SymbolType> symbolTypes ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return this.symbolTable.symbolTypes ();
    }


    /**
     * @return The Tag names which decorate the raw type name, if any.
     *         For example, a "number" type might have tags
     *         "positive,odd" to create TypeID
     *         "number[positive,odd]".  Or a "string" type might
     *         have tags "lowercase,length(1,40)" to create
     *         TypeID "string[lowercase,length(1,40)]".
     *         Never null.
     */
    public String tagNames ()
        throws ReturnNeverNull.Violation
    {
        return this.tagNames;
    }


    /**
     * <p>
     * Sets the tag names of the Type being built.
     * </p>
     *
     * @param tag_names The tag names of the Type being built,
     *                  Must not be null.
     *
     * @return This TypeBuilder.  Never null.
     */
    public TypeBuilder<VALUE> tagNames (
                                        String tag_names
                                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               tag_names );

        this.tagNames = tag_names;

        this.setViolationState ( "tagNames", tag_names );

        return this;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "TypeBuilder "
            + this.hashCode ()
            + " kind '" + this.kind.id ().name () + "'"
            + " id '" + TypeID.name ( this.rawTypeName (),
                                      this.tagNames () )
            + "'"
            + " value class = " + ClassName.of ( this.valueClass )
            + " metadata = " + this.metadata
            + " namespace = " + this.namespace.id ().name ()
            + " none = '" + this.none () + "'";
    }


    /**
     * @return The class of values of the Type being built, such as
     *         String.class or Integer.class and so on.  Never null.
     */
    public Class<VALUE> valueClass ()
        throws ReturnNeverNull.Violation
    {
        return this.valueClass;
    }


    /**
     * @return Either the One TypingViolation for the current state of
     *         the Type being built, or No TypingViolation at all.
     *         For example, if the current none value is not an
     *         instance of the current valueClass, then a violation
     *         will be returned.  Never null.
     *
     * @see musaico.foundation.typing.TypeMustBeValid
     */
    public final Value<TypingViolation> violation ()
    {
        if ( this.permanentViolation != null )
        {
            return new One<TypingViolation> ( TypingViolation.class,
                                              this.permanentViolation );
        }
        else if ( this.violation != null )
        {
            return new One<TypingViolation> ( TypingViolation.class,
                                              this.violation );
        }
        else
        {
            return new No<TypingViolation> ( TypingViolation.class,
                                             TypingViolation.NONE );
        }
    }


    /**
     * @return The Visibility of the TypeID for the Type to be created,
     *         such as Visibility.PUBLIC.  Never null.
     */
    public final Visibility visibility ()
    {
        return this.visibility;
    }


    /**
     * @param visibility The new Visibility of the TypeID for the Type
     *                   to be created, such as Visibility.PUBLIC
     *                   or Visibility.PRIVATE.  Must not be null.
     */
    public final TypeBuilder<VALUE> visibility (
                                                Visibility visibility
                                                )
    {
        this.visibility = visibility;

        return this;
    }
}
