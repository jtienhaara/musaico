package musaico.foundation.typing.typeclass;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.typing.AbstractTag;
import musaico.foundation.typing.Namespace;
import musaico.foundation.typing.NamespaceID;
import musaico.foundation.typing.Operation;
import musaico.foundation.typing.OperationID;
import musaico.foundation.typing.StandardOperation1;
import musaico.foundation.typing.Symbol;
import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.SymbolTable;
import musaico.foundation.typing.Tag;
import musaico.foundation.typing.TagID;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypedValueBuilder;
import musaico.foundation.typing.TypeID;
import musaico.foundation.typing.TypingViolation;
import musaico.foundation.typing.Unregistered;
import musaico.foundation.typing.Visibility;

import musaico.foundation.value.No;
import musaico.foundation.value.NotOne;
import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * The instantiation of a TypeClass by a specific Type, providing
 * the Symbol corresponding to each SymbolID required by the TypeClass.
 * </p>
 *
 * <p>
 * For example, a Type instantiating a "math" TypeClass might have a
 * TypeClassInstance providing the Operations corresponding to the
 * required IDs "add", "subtract", "multiply" and "divide".
 * </p>
 *
 *
 * <p>
 * In Java every TypeClassInstance must be Serializable in order to play nicely
 * over RMI.  However be warned that any given TypeClassInstance
 * might contain non-Serializable Symbols.
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
public class TypeClassInstance
    extends AbstractTag
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The ID of the TypeClass, which can be retrieved by calling
     *  <code> instance.symbol ( TypeClassInstance.TYPE_CLASS_ID ) </code>. */
    public static final TagID TYPE_CLASS_ID = new TagID ( "#type_class",
                                                          Visibility.PRIVATE );

    /** The ID of the Type instantiating the TypeClass, retrievable with
     *  <code> instance.symbol ( TypeClassInstance.TYPE_ID ) </code>. */
    public static final TypeID TYPE_ID = new TypeID ( "#instance_type",
                                                      Visibility.PRIVATE );

    /** The prefix for storing each child TypeClassInstance in an
     *  instance's SymbolTable. */
    public static final String PREFIX_CHILD_INSTANCE = "#child_instance:";

    /** The prefix for storing each required symbol in an instance's
     *  SymbolTable. */
    public static final String PREFIX_REQUIRED_SYMBOL = "#required_symbol:";


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( TypeClassInstance.class );


    /**
     * <p>
     * Creates a new TypeClassInstance for the specified Type
     * instantiating the specified TypeClass.
     * </p>
     *
     * @param type_class The TypeClass which provides the SymbolIDs
     *                   for this TypeClassInstance.  Must not be null.
     *
     * @param instance_type The Type which instantiates the Symbols
     *                      required by the TypeClass.  Must not be null.
     *
     * @param child_type_class_instances For every child TypeClass of
     *                                   the TypeClass being instantiated,
     *                                   a TypeClassInstance must be
     *                                   provided in this lookup.
     *                                   Must not be null.  Must not
     *                                   contain any null keys or values.
     *
     * @param symbols_map The lookup of actual instantiation Symbols
     *                    by the TypeClass required SymbolIDs they instantiate.
     *                    Note that the Type of each actual Symbol might
     *                    be different from the Type of the SymbolID it
     *                    instantiates.  For example, a TypeClass might require
     *                    a ConstantID "default_value" of UnknownType "x",
     *                    instantiated by a Type by Constant "default_value"
     *                    of Type "string".  The UnknownType "x" and the Type
     *                    "string" are different Types, but the Constant
     *                    instantiates the required ConstantID by
     *                    substitution of the UnknownType.  Must not be null.
     *                    Must not contain any null keys or values.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Tag.  The caller may continue to add to the
     *                     SymbolTable after constructing this
     *                     TypeClassInstance, but is expected to cease
     *                     additions to the SymbolTable before anyone
     *                     begins using this TypeClassInstance.
     *                     Must not be null.
     *
     * @throws TypeMustContainChildTypeClassInstance.Violation If the
     *             specified child_type_class_instances Map does not
     *             contain an instantiation for any child TypeClass
     *             required by the specified parent TypeClass.
     *
     * @throws TypeMustInstantiateTypeClass.Violation If the specified
     *             symbols_map does not provide instantiation(s) of any of
     *             the SymbolIDs required by the specified TypeClass.
     *
     * @throws SymbolMustBeRegisteredInTypeClass.Violation If the specified
     *             child_type_class_instances or symbols_map Maps contain
     *             instantiations of any child TypeClass(es) / SymbolID(s)
     *             which are not actually required by the specified
     *             TypeClass.
     *
     * <p>
     * Protected.  Used only by TypeClass, and by derived implementations
     * of TypeClass and TypeClassInstance.
     * </p>
     */
    @SuppressWarnings("unchecked") // Cast SymbolID<?> - SymbolID<SYMBOL>.
    protected TypeClassInstance (
                                 TypeClass type_class,
                                 Type<?> instance_type,
                                 Map<TypeClass, TypeClassInstance> child_type_class_instances,
                                 Map<SymbolID<?>, Symbol> symbols_map,
                                 SymbolTable symbol_table
                                 )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               Parameter4.MustContainNoNulls.Violation,
               TypeMustContainChildTypeClassInstance.Violation,
               TypeMustInstantiateTypeClass.Violation,
               SymbolMustBeRegisteredInTypeClass.Violation
    {
        super ( instance_type,                  // parent_namespace
                type_class == null              // name
                    ? null
                    : type_class.id ().name (),
                symbol_table,                   // symbol_table
                type_class == null              // metadata
                    ? null
                    : type_class.metadata () );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type_class, instance_type,
                               child_type_class_instances, symbols_map );
        classContracts.check ( Parameter3.MustContainNoNulls.CONTRACT,
                               child_type_class_instances.keySet () );
        classContracts.check ( Parameter3.MustContainNoNulls.CONTRACT,
                               child_type_class_instances.values () );
        classContracts.check ( Parameter4.MustContainNoNulls.CONTRACT,
                               symbols_map.keySet () );
        classContracts.check ( Parameter4.MustContainNoNulls.CONTRACT,
                               symbols_map.values () );

        this.symbolTable ().set ( TYPE_CLASS_ID, type_class );

        this.symbolTable ().set ( TYPE_ID, instance_type );

        final Map<TypeClass, TypeClassInstance> children =
            new HashMap<TypeClass, TypeClassInstance> (
                child_type_class_instances );
        final List<TypeClass> missing_child_type_class_instances =
            new ArrayList<TypeClass> ();
        for ( TypeClass child_type_class : type_class.childTypeClasses () )
        {
            final TagID child_instance_id =
                this.childInstanceID ( child_type_class );

            final TypeClassInstance child_instance =
                children.get ( child_type_class );
            children.remove ( child_type_class );

            if ( child_instance == null )
            {
                missing_child_type_class_instances.add ( child_type_class );
                continue;
            }

            symbol_table.set ( child_instance_id, child_instance );
        }

        if ( missing_child_type_class_instances.size () > 0 )
        {
            final TypeMustContainChildTypeClassInstance.Violation violation =
                new TypeMustContainChildTypeClassInstance ( type_class,
                                                            instance_type )
                    .violation ( this,
                                 missing_child_type_class_instances );
        }

        if ( children.size () > 0 )
        {
            final SymbolMustBeRegisteredInTypeClass.Violation violation =
                SymbolMustBeRegisteredInTypeClass.CONTRACT.violation (
                    this,
                    new Unregistered ( children.keySet ().iterator ().next () ) );

            throw violation;
        }

        final Map<SymbolID<?>, Symbol> symbols =
            new HashMap<SymbolID<?>, Symbol> ( symbols_map );
        final List<SymbolID<?>> missing_required_symbol_ids =
            new ArrayList<SymbolID<?>> ();
        for ( SymbolID<?> required_symbol_id
                  : type_class.requiredSymbolIDs () )
        {
            final SymbolID<Symbol> symbol_id = (SymbolID<Symbol>)
                this.requiredSymbolID ( required_symbol_id );

            final Symbol symbol = symbols.get ( required_symbol_id );
            symbols.remove ( required_symbol_id );

            if ( symbol == null )
            {
                missing_required_symbol_ids.add ( required_symbol_id );
                continue;
            }

            symbol_table.set ( symbol_id, symbol );
        }

        if ( missing_required_symbol_ids.size () > 0 )
        {
            final TypeMustInstantiateTypeClass contract =
                new TypeMustInstantiateTypeClass ( type_class,
                                                   instance_type );
            final TypeMustInstantiateTypeClass.Violation violation =
                contract.violation ( this,
                                     missing_required_symbol_ids );

            throw violation;
        }

        if ( symbols.size () > 0 )
        {
            final SymbolMustBeRegisteredInTypeClass.Violation violation =
                SymbolMustBeRegisteredInTypeClass.CONTRACT.violation (
                    this,
                    new Unregistered ( symbols.keySet ().iterator ().next () ) );

            throw violation;
        }
    }


    /**
     * <p>
     * Creates a new NoTypeClassInstance for the specified Type,
     * not actually instantiating the specified TypeClass.
     * </p>
     *
     * <p>
     * Used to create "none" TypeClassInstances.
     * </p>
     *
     * @param type_class The TypeClass instantiated by this
     *                   "none" TypeClassInstance, such as TypeClass.NONE.
     *                   Must not be null.
     *
     * @param instance_type The Type which does not provide the Symbols for
     *                      this TypeClassInstance.  Must not be null.
     *
     * <p>
     * Protected.  Only to be used by NoTypeClassInstance and other
     * "none" TypeClassInstances.
     * </p>
     */
    protected TypeClassInstance (
                                 TypeClass type_class,
                                 Type<?> instance_type
                                 )
        throws ParametersMustNotBeNull.Violation
    {
        super ( instance_type,                  // parent_namespace
                type_class == null              // name
                    ? null
                    : type_class.id ().name (),
                new SymbolTable (),             // symbol_table
                type_class == null              // metadata
                    ? null
                    : type_class.metadata () );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type_class, instance_type );

        this.symbolTable ().set ( TYPE_CLASS_ID, type_class );

        this.symbolTable ().set ( TYPE_ID, instance_type );
    }


    /**
     * <p>
     * Creates a new NoTypeClassInstance for Type.NONE, with no TypeClass.
     * </p>
     *
     * <p>
     * Used to create the most basic of "none" TypeClassInstances.
     * </p>
     *
     * <p>
     * Protected.  Only to be used by the most basic "none"
     * TypeClassInstances.
     * </p>
     */
    protected TypeClassInstance ()
        throws ParametersMustNotBeNull.Violation
    {
        super ( Namespace.NONE,                 // parent_namespace
                "none",
                new SymbolTable (),             // symbol_table
                new StandardMetadata () );      // metadata

        this.symbolTable ().set ( TYPE_CLASS_ID, TypeClass.NONE );

        this.symbolTable ().set ( TYPE_ID, Type.NONE );
    }


    /**
     * <p>
     * Looks up the specified child TypeClassInstance of
     * this TypeClassInstance, by the child TypeClass it
     * instantiates.
     * </p>
     *
     * @param child_type_class The child TypeClass for which the
     *                         child instance will be retrieved.
     *                         Must not be null.
     *
     * @return The required One child TypeClassInstance, or
     *         No child TypeClassInstance if there is none for
     *         the specified TypeClass.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Tag - TypeClassInstance.
    public final ZeroOrOne<TypeClassInstance> child (
                                                     TypeClass child_type_class
                                                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  child_type_class );

        final TagID child_instance_id =
            this.childInstanceID ( child_type_class );

        final ZeroOrOne<Tag> v_child_tag =
            this.symbol ( child_instance_id );

        final ZeroOrOne<TypeClassInstance> v_child_instance;
        if ( v_child_tag instanceof NotOne )
        {
            NotOne<Tag> not_one = (NotOne<Tag>) v_child_tag;
            final NoTypeClassInstance none =
                new NoTypeClassInstance ( child_type_class,
                                          this.instanceType () );
            v_child_instance =
                new No<TypeClassInstance> ( TypeClassInstance.class,
                                            not_one.valueViolation () );
        }
        else
        {
            One<Tag> one = (One<Tag>) v_child_tag;
            v_child_instance =
                new One<TypeClassInstance> ( TypeClassInstance.class,
                                             (TypeClassInstance) one.orNone () );
        }

        return v_child_instance;
    }


    /**
     * <p>
     * Returns the TagID identifying the child TypeClassInstance for\
     * the specified child TypeClass.
     * </p>
     *
     * <p>
     * For example, if a parent TypeClass <code> transportation </code>
     * requires a child TypeClass <code> propulsion_source </code>, then
     * an instantiation of <code> transportation </code>, such as
     * <code> vehicle </code> or <code> self_propulsion </code>, would
     * return the ID for its child <code> propulsion_source </code>
     * instance, such as
     * <code> TagID ( "#child_instance:propulsion_source" ) </code>.
     * </p>
     *
     * <p>
     * The resulting TagID can be used to retrieve the child TypeClassInstance
     * from the parent.  For example,
     * <code> vehicle.symbol ( vehicle.childInstanceID ( propulsion_source ) ) </code>
     * might return the child TypeClassInstance <code> engine </code>, or
     * <code> self_propulsion.symbol ( self_propulsion.childInstanceID ( propulsion_source ) ) </code>
     * might return the child TypeClassInstance <code> body_part </code>,
     * and so on.
     * </p>
     *
     * <p>
     * Warning: no check is performed by this method to ensure that
     * the specified <code> child_type_class </code> is, in fact,
     * a child of the parent TypeClass instantiated by
     * this TypeClassInstance.
     * </p>
     *
     * @param child_type_class The TypeClass child of this instance's
     *                         parent TypeClass.  Must not be null.
     *
     * @return The ID of the corresponding child TypeClassInstance.
     *         Never null.
     */
    public final TagID childInstanceID (
                                        TypeClass child_type_class
                                        )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  child_type_class );

        final String child_instance_name =
            TypeClassInstance.PREFIX_CHILD_INSTANCE
            + child_type_class.id ().name ();

        final TagID child_instance_id = new TagID ( child_instance_name,
                                                    Visibility.PRIVATE );

        return child_instance_id;
    }


    /**
     * @return A Map of required child TypeClasses to child TypeClassInstances
     *         for this parent TypeClassInstance.  Used by rename ().
     *         Never null.  Never contains any null keys or values.
     */
    protected final Map<TypeClass, TypeClassInstance> childTypeClassInstances ()
    {
        final Map<TypeClass, TypeClassInstance> child_instances =
            new HashMap<TypeClass, TypeClassInstance> ();

        for ( TypeClass child_type_class
                  : this.typeClass ().childTypeClasses () )
        {
            final TypeClassInstance child_instance =
                this.child ( child_type_class ).orNull ();
            if ( child_instance != null )
            {
                child_instances.put ( child_type_class, child_instance );
            }
        }

        return child_instances;
    }


    /**
     * <p>
     * Looks up the specified Operation for this TypeClassInstance,
     * evaluates it using the specified inputs, and returns the result
     * as an Object Value.
     * </p>
     *
     * @param id The unique identifier of the Operation to look up,
     *           such as Operation "doSomething", and so on.
     *           Must be an OperationID that is required by the TypeClass.
     *           Must not be null.
     *
     * @param inputs The inputs to pass to the Operation.
     *               Must not be null.  Must not contain any null elements.
     *
     * @return The output of the requested Operation, or No output
     *         if the specified Operation does not exist or if one or
     *         more of the inputs are of the wrong Type.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Symbol - OPERATION,
                               // Cast Type<Operation<?>> - Type<OPERATION>,
                               // Several casts Value<?> - Value<Object>,
    public final <OPERATION extends Operation<? extends Object>>
        Value<Object> evaluate (
                                OperationID<OPERATION, ? extends Object> id,
                                Value<?> ... inputs
                                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  id, inputs );
        this.contracts ().check ( Parameter3.MustContainNoNulls.CONTRACT,
                                  inputs );

        final ZeroOrOne<OPERATION> v_operation = this.operation ( id );
        if ( v_operation instanceof NotOne )
        {
            final NotOne<OPERATION> failed = (NotOne<OPERATION>) v_operation;
            return (No<Object>)
                failed.orNone ().outputType ().noValue ( failed.valueViolation () );
        }

        final List<Value<Object>> inputs_list =
            new ArrayList<Value<Object>> ();
        for ( Value<?> input : inputs )
        {
            inputs_list.add ( (Value<Object>) input );
        }

        final OPERATION operation = v_operation.orNull ();
        final Value<Object> output;
        try
        {
            output = (Value<Object>)
                operation.evaluate ( inputs_list );
        }
        catch ( Exception e )
        {
            return StandardOperation1.badInput ( (Operation<Object>) operation,
                                                 -1,
                                                 e,
                                                 inputs_list );
        }

        return output;
    }


    /**
     * @return The Type which instantiates the Symbols required by
     *         the TypeClass.  Never null.
     */
    public final Type<?> instanceType ()
    {
        final Type<?> instance_type =
            this.symbolTable ().symbol ( TYPE_ID )
                .orNone ();

        return instance_type;
    }


    /**
     * <p>
     * Looks up the specified required Operation instance for
     * this TypeClassInstance.
     * </p>
     *
     * <p>
     * For example, if the TypeClass requires <code> doSomething </code>,
     * then a call to <code> instance.operation ( doSomething ) </code>
     * might return Operation <code> printHelloWorld <code>.
     * </p>
     *
     * @param required_operation_id The unique identifier of the
     *                              required Operation to look up,
     *                              such as Operation "doSomething",
     *                              and so on.  Must be an OperationID
     *                              that is required by the TypeClass.
     *                              Must not be null.
     *
     * @return The requested Operation.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Symbol - OPERATION,
                               // Cast Type<Operation<?>> - Type<OPERATION>.
    public final <OPERATION extends Operation<?>>
        ZeroOrOne<OPERATION> operation (
            OperationID<OPERATION, ?> required_operation_id
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  required_operation_id );

        final ZeroOrOne<OPERATION> v_operation =
            this.requiredSymbol ( required_operation_id );
        if ( ! v_operation.hasValue () )
        {
            final TypingViolation violation =
                SymbolMustBeRegisteredInTypeClass.CONTRACT.violation (
                    this,
                    new Unregistered ( required_operation_id ) );

            return required_operation_id.type ().noValue ( violation );
        }

        final OPERATION operation = v_operation.orNone ();

        // Note: we can check the value class of the required SymbolID's
        // Type (such as Operation1.class, Operation2.class, and so on),
        // but we cannot actually call Type.checkValue () because the
        // required SymbolID's Type might have UnknownTypes that have
        // been substituted in the actual instantiation Symbol's Type.
        // For example, a required operation ID "filter(unknown):unknown"
        // might be instantiated by "filter(string):string"
        // or "filter(int):int" and so on.
        if ( ! required_operation_id.type ().valueClass ().isInstance ( operation ) )
        {
            final TypingViolation violation =
                SymbolMustBeRegisteredInTypeClass.CONTRACT.violation (
                    this,
                    new Unregistered ( required_operation_id ) );

            return required_operation_id.type ().noValue ( violation );
        }

        final TypedValueBuilder<OPERATION> builder =
            new TypedValueBuilder<OPERATION> ( (Type<OPERATION>)
                                               operation.type () );

        builder.add ( operation );

        return builder.buildZeroOrOne ();
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public TypeClassInstance rename (
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
    @SuppressWarnings("unchecked") // Cast Tag - TypeClass.
    public TypeClassInstance rename (
                                     String name,
                                     SymbolTable symbol_table
                                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
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

        final TypeClass type_class = (TypeClass)
            symbol_table.symbol ( TYPE_CLASS_ID )
                .orDefault ( TypeClass.NONE );

        final Type<?> instance_type = symbol_table.symbol ( TYPE_ID )
                                                  .orNone ();

        try
        {
            return new TypeClassInstance ( type_class,
                                           instance_type,
                                           this.childTypeClassInstances (),
                                           this.requiredSymbols (),
                                           symbol_table );
        }
        catch ( TypingViolation violation )
        {
            final ReturnNeverNull.Violation violation2 =
                ReturnNeverNull.CONTRACT.violation ( this,
                                                     "rename ( \""
                                                     + name
                                                     + "\", symbol_table )" );
            violation2.initCause ( violation );

            throw violation2;
        }
    }


    /**
     * <p>
     * Looks up the specified Symbol for this TypeClassInstance,
     * required by the TypeClass.
     * </p>
     *
     * @param id The unique identifier of the Symbol to look up,
     *           such as Operation "doSomething", or Tag "PrivateTag",
     *           or NamespaceID.PARENT, or TypeIDs.ROOT_TYPE, and so on.
     *           Must be a SymbolID that is required by the TypeClass.
     *           Must not be null.
     *
     * @return The required Symbol.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast No<? extends Symbol> - No<Symbol>,
                               // Type<? extends Symbol> - Type<Symbol>.
    public final <SYMBOL extends Symbol>
        ZeroOrOne<SYMBOL> requiredSymbol (
                                          SymbolID<SYMBOL> required_symbol_id
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final SymbolID<SYMBOL> symbol_id =
            this.requiredSymbolID ( required_symbol_id );
        final ZeroOrOne<SYMBOL> v_symbol = this.symbol ( symbol_id );

        return v_symbol;
    }


    /**
     * @return A Map of required SymbolIDs to Symbol instantiations
     *         for this TypeClassInstance.  Used by rename ().
     *         Never null.  Never contains any null keys or values.
     */
    protected final Map<SymbolID<?>, Symbol> requiredSymbols ()
    {
        final Map<SymbolID<?>, Symbol> required_symbols =
            new HashMap<SymbolID<?>, Symbol> ();

        for ( SymbolID<?> required_symbol_id
                  : this.typeClass ().requiredSymbolIDs () )
        {
            final Symbol symbol =
                this.requiredSymbol ( required_symbol_id )
                    .orNull ();
            if ( symbol != null )
            {
                required_symbols.put ( required_symbol_id, symbol );
            }
        }

        return required_symbols;
    }


    /**
     * <p>
     * Returns the actual SymbolID identifying the Symbol instantiating
     * the specified required SymbolID from the instantiated TypeClass.
     * </p>
     *
     * <p>
     * For example, if a TypeClass <code> transportation </code>
     * requires an OperationID <code> accelerate </code>, then
     * an instantiation of <code> transportation </code>, such as
     * <code> vehicle </code> or <code> self_propulsion </code>, would
     * return the ID for its Operation instantiating
     * <code> accelerate </code>, such as
     * <code> OperationID ( "#required_symbol:accelerate" ) </code>.
     * </p>
     *
     * <p>
     * The resulting SymbolID can be used to retrieve the Symbol
     * instantiating the required SymbolID.  For example,
     * <code> vehicle.symbol ( vehicle.requiredSymbolID ( accelerate ) ) </code>
     * might return the Operation <code> accelerate ( Vehicle ) </code>.
     * </p>
     *
     * <p>
     * Warning: no check is performed by this method to ensure that
     * the specified <code> required_symbol </code> is, in fact,
     * a SymbolID required for the TypeClass instantiated by
     * this TypeClassInstance.
     * </p>
     *
     * @param required_symbol_id The SymbolID required by the TypeClass.
     *                           Must not be null.
     *
     * @return The ID of the corresponding Symbol instantiation of the
     *         required SymbolID.  Never null.
     */
    public final <SYMBOL extends Symbol>
            SymbolID<SYMBOL> requiredSymbolID (
                    SymbolID<SYMBOL> required_symbol_id
                    )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  required_symbol_id );

        final String symbol_name =
            TypeClassInstance.PREFIX_REQUIRED_SYMBOL
            + required_symbol_id.name ();

        final SymbolID<SYMBOL> symbol_id =
            required_symbol_id.rename ( symbol_name,
                                        Visibility.PRIVATE );

        return symbol_id;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "TypeClassInstance { typeClass: "
            + this.typeClass ().id ().name ()
            + ", instanceType: " + this.instanceType ().id ().name ()
            + " }";
    }


    /**
     * @return The TypeClass which provides the SymbolIDs
     *         for this TypeClassInstance.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Tag - TypeClass.
    public TypeClass typeClass ()
    {
        final TypeClass type_class = (TypeClass)
            this.symbolTable ().symbol ( TYPE_CLASS_ID )
                .orNone ();

        return type_class;
    }
}
