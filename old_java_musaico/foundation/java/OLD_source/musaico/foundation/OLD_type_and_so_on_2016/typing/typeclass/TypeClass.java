package musaico.foundation.typing.typeclass;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.typing.AbstractTagWithTypeConstraint;
import musaico.foundation.typing.Kind;
import musaico.foundation.typing.Namespace;
import musaico.foundation.typing.NamespaceID;
import musaico.foundation.typing.NamespaceVisitor;
import musaico.foundation.typing.NamespaceWalker;
import musaico.foundation.typing.Retypable;
import musaico.foundation.typing.SubTypeSubstituteParentType;
import musaico.foundation.typing.Symbol;
import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.SymbolTable;
import musaico.foundation.typing.SymbolType;
import musaico.foundation.typing.Tag;
import musaico.foundation.typing.TagID;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypesMustHaveSameValueClasses;
import musaico.foundation.typing.TypingViolation;
import musaico.foundation.typing.UnknownType;

import musaico.foundation.value.No;
import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;


/**
 * <p>
 * A class of disparate Types which provide a certain set of
 * Symbols.
 * </p>
 *
 * <p>
 * For example, a TypeClass "set" might cover all Types which
 * have the Operations "difference", "intersection" and "union".
 * Then sub-typing any given Type with TypeClass "set" would either
 * result in a Type that is guaranteed to have those Operations,
 * or would fail immediately.
 * </p>
 *
 * <p>
 * TypeClass can also be used to add functionality to a (sub-)Type that
 * the Type does not already have, simply by placing the required
 * Symbols into the TypeClass's SymbolTable.  For example:
 * </p>
 *
 * <pre>
 *     final TypeClass set = ...;
 *
 *     final Type<String> string = ...;
 *     final Operation2<String, String, String> string_difference = ...;
 *     final Operation2<String, String, String> string_intersection = ...;
 *     final Operation2<String, String, String> string_union = ...;
 *
 *     final SymbolTable symbol_table = new SymbolTable ();
 *     symbol_table.add ( string_difference );
 *     symbol_table.add ( string_intersection );
 *     symbol_table.add ( string_union );
 *
 *     final TypeClass string_set =
 *         set.rename ( set.id ().name (),
 *                      symbol_table );
 *
 *     final Type<String> string_with_set_functionality =
 *         string.sub ( string_set );
 * </pre>
 *
 *
 * <p>
 * In Java every TypeClass must be Serializable in order to play nicely
 * over RMI.  However be warned that instances of any given TypeClass
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
public class TypeClass
    extends AbstractTagWithTypeConstraint
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( TypeClass.class );


    /** No TypeClass at all. */
    public static final TypeClass NONE =
        new TypeClass ( Namespace.NONE,
                        "notypeclass" );


    // SymbolIDs which must be provided by every Type in this TypeClass.
    private final SymbolID<?> [] requiredSymbolIDs;


    /**
     * <p>
     * Creates a new TypeClass with the specified name, to be used
     * for a unique Tag identifier in SymbolTables.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Tag will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param name The name for a unique identifier of this Tag.  Every Tag
     *             must have a unique ID within each SymbolTable.
     *             Must not be null.
     *
     * @param required_symbol_ids All SymbolIDs for Symbols which
     *                            must be provided by every Type in this
     *                            TypeClass.  Must not be null.
     *                            Must not contain any null elements.
     */
    public TypeClass (
                      Namespace parent_namespace,
                      String name,
                      SymbolID<?> ... required_symbol_ids
                      )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        this ( parent_namespace,
               name,
               required_symbol_ids,
               new SymbolTable () );
    }


    /**
     * <p>
     * Creates a new TypeClass with the specified name, to be used
     * for a unique Tag identifier in SymbolTables.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Tag will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param name The name for a unique identifier of this Tag.  Every Tag
     *             must have a unique ID within each SymbolTable.
     *             Must not be null.
     *
     *
     * @param required_symbol_ids All SymbolIDs for Symbols which
     *                            must be provided by every Type in this
     *                            TypeClass.  Must not be null.
     *                            Must not contain any null elements.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Tag.  The caller may continue to add to the
     *                     SymbolTable after constructing this TypeClass,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this tag.
     *                     Must not be null.
     */
    public TypeClass (
                      Namespace parent_namespace,
                      String name,
                      SymbolID<?> [] required_symbol_ids,
                      SymbolTable symbol_table
                      )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        this ( parent_namespace,
               name,
               required_symbol_ids,
               symbol_table,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new TypeClass with the specified name, to be used
     * for a unique Tag identifier in SymbolTables.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Tag will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param name The name for a unique identifier of this Tag.  Every Tag
     *             must have a unique ID within each SymbolTable.
     *             Must not be null.
     *
     *
     * @param required_symbol_ids All SymbolIDs for Symbols which
     *                            must be provided by every Type in this
     *                            TypeClass.  Must not be null.
     *                            Must not contain any null elements.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Tag.  The caller may continue to add to the
     *                     SymbolTable after constructing this TypeClass,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this tag.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this TypeClass.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public TypeClass (
                      Namespace parent_namespace,
                      String name,
                      SymbolID<?> [] required_symbol_ids,
                      SymbolTable symbol_table,
                      Metadata metadata
                      )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        super ( parent_namespace, name, symbol_table, metadata );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object []) required_symbol_ids );
        classContracts.check ( Parameter3.MustContainNoNulls.CONTRACT,
                               required_symbol_ids );

        this.requiredSymbolIDs =
            new SymbolID<?> [ required_symbol_ids.length ];
        System.arraycopy ( required_symbol_ids, 0,
                           this.requiredSymbolIDs, 0,
                           required_symbol_ids.length );
    }


    /**
     * @return All TypeClass children of this TypeClass.
     *         Never null.  Never contains any null elements.
     */
    public final TypeClass [] childTypeClasses ()
    {
        final List<TypeClass> child_type_classes_list =
            new ArrayList<TypeClass> ();
        for ( Tag maybe_child_type_class : this.symbols ( Tag.TYPE ) )
        {
            if ( ! ( maybe_child_type_class instanceof TypeClass ) )
            {
                continue;
            }

            final TypeClass child_type_class =
                (TypeClass) maybe_child_type_class;

            child_type_classes_list.add ( child_type_class );
        }

        final TypeClass [] template =
            new TypeClass [ child_type_classes_list.size () ];
        final TypeClass [] child_type_classes =
            child_type_classes_list.toArray ( template );

        return child_type_classes;
    }


    /**
     * @see musaico.foundation.typing.Namespace#equalsNamespace(musaico.foundation.typing.Namespace)
     */
    protected final boolean equalsNamespace (
                                             Tag tag
                                             )
    {
        if ( ! ( tag instanceof TypeClass ) )
        {
            return false;
        }

        final TypeClass that = (TypeClass) tag;

        if ( this.requiredSymbolIDs.length != that.requiredSymbolIDs.length )
        {
            return false;
        }

        for ( int s = 0; s < this.requiredSymbolIDs.length; s ++ )
        {
            if ( ! this.requiredSymbolIDs [ s ].equals ( that.requiredSymbolIDs [ s ] ) )
            {
                return false;
            }
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
                                     Type<?> type
                                     )
    {
        if ( type == null )
        {
            return FilterState.DISCARDED;
        }

        // Make sure the child TypeClasses can be instantiated.
        final Map<TypeClass, Value<TypeClassInstance>>
            child_type_class_instances =
                new HashMap<TypeClass, Value<TypeClassInstance>> ();
        if ( ! this.findCreateChildInstances ( type,
                                               child_type_class_instances )
                   .isKept () )
        {
            // Type does not provide instances of the child TypeClasses.
            // We cannot use it to instantiate this TypeClass.
            return FilterState.DISCARDED;
        }

        // Make sure all of the symbol IDs are present and accounted for.
        for ( SymbolID<?> symbol_id : this.requiredSymbolIDs )
        {
            if ( ! type.containsSymbol ( symbol_id ) )
            {
                return FilterState.DISCARDED;
            }
        }

        // All accounted for.
        return FilterState.KEPT;
    }


    /**
     * <p>
     * Finds child TypeClassInstance(s) in the specified Type which
     * instantiate the child TypeClass(es) of the parent TypeClass;
     * or creates a new child TypeClassInstance for each child TypeClass
     * that has no existing counterpart in the Type.
     * </p>
     *
     * <p>
     * For example, if a parent TypeClass "algebraic" has child TypeClasses
     * "arithmetical" with add(T1,T1):T1 and subtract(T1,T1):T1 operations
     * and child TypeClass "equational" with a solve(Expression,Expression):T2
     * operation, then the following Types can instantiate the parent
     * "algebraic" TypeClass:
     * </p>
     *
     * <ul>
     *   <li> Type "integer", with child TypeClassInstance
     *        "integer_arithmetic" (instantiating the child TypeClass
     *        "arithmetical") and child TypeClassInstance "integer_equation"
     *        (instantiating child TypeClass "equational").  The
     *        child TypeClassInstances might refer to any Type(s),
     *        including "integer" itself, to resolve all the required
     *        SymbolIDs. </li>
     *   <li> Any Type whose child Types can be instantiated as
     *        TypeClassInstances of the child TypeClasses "arithmetical"
     *        and "equational". </li>
     *   <li> Type "set", with child operations "add(set,set):set",
     *        "subtract(set,set):set" and "solve(Expression,Expression):set".
     *        The "set" Type itself can instantiate the parent TypeClass
     *        "algebraic" as well as both child TypeClasses "arithmetical"
     *        and "equational". </li>
     * </ul>
     *
     * @param type The Type whose child TypeClassInstance(s) will be
     *             retrieved or created.  Must not be null.
     *
     * @param child_type_class_instances The mapping of
     *                                   <code> child TypeClass --&gt; child TypeClassInstance(s) </code>
     *                                   which will be populated from the
     *                                   child TypeClass(es) of this TypeClass
     *                                   (the keys), mapped to the new or
     *                                   existing child TypeClassInstance(s)
     *                                   of the Type (the values).
     *                                   Can be empty or can be partially
     *                                   or fully populated.  Must not be null.
     *                                   Must not contain any null elements.
     *
     * @return FilterState.KEPT if all child TypeClass(es) have corresponding
     *         instantiations from the specified Type; FilterState.DISCARDED
     *         if any of the child TypeClass key(s) refer to "none"
     *         TypeClassInstance value(s).  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Class<?> - Class<TypeClassInstance>.
    protected final FilterState findCreateChildInstances (
                                                          Type<?> type,
                                                          Map<TypeClass, Value<TypeClassInstance>> child_type_class_instances
                                                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  type, child_type_class_instances );
        this.contracts ().check ( Parameter2.MustContainNoNulls.CONTRACT,
                                  child_type_class_instances.keySet () );
        this.contracts ().check ( Parameter2.MustContainNoNulls.CONTRACT,
                                  child_type_class_instances.values () );

        int num_uninstantiated_child_type_classes = 0;
        for ( TypeClass child_type_class : this.childTypeClasses () )
        {
            final TagID id = child_type_class.id ();

            final Value<TypeClassInstance> existing_instances =
                child_type_class_instances.get ( child_type_class );
            if ( existing_instances != null
                 && existing_instances.hasValue () )
            {
                // This Type has already provided one or more
                // TypeClassInstance(s) of the child TypeClass, perhaps
                // on a previous call to this method.
                continue;
            }

            // First check the type to see if it has a child TypeClassInstance
            // already.  If so, just use that.
            final Tag maybe_child_instance =
                type.symbol ( id ).orNull ();

            if ( maybe_child_instance != null
                 && ( maybe_child_instance instanceof TypeClassInstance ) )
            {
                final TypeClassInstance child_instance =
                    (TypeClassInstance) maybe_child_instance;
                final TypeClass instantiated_type_class =
                    child_instance.typeClass ();
                if ( child_type_class.equals ( instantiated_type_class ) )
                {
                    final One<TypeClassInstance> v_child_instance =
                        new One<TypeClassInstance> ( (Class<TypeClassInstance>)
                                                     child_instance.getClass (),
                                                     child_instance );
                    child_type_class_instances.put ( child_type_class,
                                                     v_child_instance );
                    continue;
                }
            }

            // We need to try instantiating this child TypeClass.
            // First let's try the child Types of the specified parent Type
            // (if any).
            // We might find multiple instances here.
            boolean is_found_at_least_one = false;
            for ( SymbolType symbol_type : type.symbolTypes () )
            {
                if ( ! ( symbol_type instanceof Kind ) )
                {
                    continue;
                }

                final Kind kind = (Kind) symbol_type;
                for ( Type<?> child_type : type.symbols ( kind ) )
                {
                    // Try to instantiate the child TypeClass with
                    // this child Type.
                    final Value<TypeClassInstance> v_child_instances =
                        child_type_class.instance ( child_type );
                    if ( v_child_instances.hasValue () )
                    {
                        final Value<TypeClassInstance> current_instances =
                            child_type_class_instances.get ( child_type_class );
                        if ( current_instances == null
                             || ! current_instances.hasValue () )
                        {
                            child_type_class_instances.put ( child_type_class,
                                                             v_child_instances );
                        }
                        else
                        {
                            final Value<TypeClassInstance> new_instances =
                                current_instances.or ( v_child_instances );
                            child_type_class_instances.put ( child_type_class,
                                                             new_instances );
                        }
                        is_found_at_least_one = true;
                        continue;
                    }
                }

                if ( is_found_at_least_one )
                {
                    continue;
                }
            }

            if ( is_found_at_least_one )
            {
                continue;
            }

            // Last ditch effort: try to use the parent type itself to\
            // instantiate the child TypeClass.
            final Value<TypeClassInstance> v_child_instances =
                child_type_class.instance ( type );
            child_type_class_instances.put ( child_type_class,
                                             v_child_instances );

            if ( ! v_child_instances.hasValue () )
            {
                num_uninstantiated_child_type_classes ++;
            }
        }

        if ( num_uninstantiated_child_type_classes == 0 )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * <p>
     * Creates one or more TypeClassInstance(s) of this TypeClass
     * from the specified Type.
     * </p>
     *
     * <p>
     * Sometimes a Type might only satisfy a TypeClass's requirements
     * in one way.  However sometimes the requirements can be satisfied
     * by different Symbols and/or child Types.  In such cases,
     * more than one TypeClassInstance will be returned, one for each
     * permutation.
     * </p>
     *
     * <p>
     * This method can be overridden by derived classes, to return
     * more specific TypeClassInstance(s).
     * </p>
     *
     * @param type The Type which will instantiate this TypeClass.
     *             Must not be null.
     *
     * @return One or more newly created TypeClassInstance(s), providing
     *         the tie between the specified Type and this TypeClass
     *         (multiple TypeClassInstances only if there are multiple
     *         ways of resolving the required SymbolIDs);
     *         or No TypeClassInstance if the specified Type does not meet
     *         the requirements of this TypeClass.  Never null.
     */
    public <VALUE extends Object>
        Value<TypeClassInstance> instance (
                                           Type<VALUE> type
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  type );

        final TypeClassInstanceBuilder<?, TypeClassInstance, VALUE> builder =
            new TypeClassInstanceBuilder<TypeClass, TypeClassInstance, VALUE> ( this, type, TypeClassInstance.class );

        return builder.build ();
    }


    /**
     * <p>
     * Returns true if the specified required SymbolID is instantiated
     * by the specified actual Symbol, or false if not.
     * </p>
     *
     * <p>
     * For example, required OperationID "foo" might be instantiated
     * by actual Symbol Operation3 "foo", but required ConstantID "foo"
     * is not instantiated by actual Symbol Operation3 "foo".
     * </p>
     *
     * @param required_symbol_id The SymbolID required by this TypeClass.
     *                           Must not be null.
     *
     * @param actual_symbol The actual Symbol which might or might not
     *                      instantiate the required SymbolID.
     *                      Must not be null.
     *
     * @return True if the actual symbol does instantiate the
     *         required SymbolID, false if it does not.
     */
    protected final boolean isInstantiatedBy (
                                              SymbolID<?> required_symbol_id,
                                              Symbol actual_symbol
                                              )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  required_symbol_id, actual_symbol );

        if ( ! required_symbol_id.name ().equals ( actual_symbol.id ().name () ) )
        {
            // Not even the same name.
            return false;
        }

        final Type<?> required_type =
            required_symbol_id.type ();
        final Type<?> actual_type =
            actual_symbol.type ().asType ();

        final Class<?> required_value_class = required_type.valueClass ();
        final Class<?> actual_value_class = actual_type.valueClass ();

        if ( required_value_class == actual_value_class )
        {
            // Actual value class is exactly what is required.
            // For example, Operation2.class value class is required, and
            // the actual value class is Operation2.class.
            return true;
        }
        else if ( required_value_class.isAssignableFrom ( actual_value_class ) )
        {
            // Actual value class is derived from the required value class,
            // so it's OK.
            // For example, Expression.class value class is required,
            // and actual value class is XYZExpression.class which
            // extends Expression.class.
            return true;
        }
        else
        {
            // The actual symbol has the right name, but wrong Type.
            // It cannot instantiate the required SymbolID.
            return false;
        }
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public final TypeClass rename (
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
    public TypeClass rename (
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

        return new TypeClass ( parent_namespace,
                               name,
                               this.requiredSymbolIDs,
                               symbol_table,
                               this.metadata ().renew () );
    }


    /**
     * <p>
     * Given a Symbol, substitute all UnknownTypes with the specified Type.
     * </p>
     *
     * <p>
     * For example, if the actual symbol is a <code> Constant </code>,
     * and its value Type is an <code> UnknownType </code>, then
     * it will be substituted by the same <code> Constant </code> but
     * with value Type <code> type </code>.  Or if the actual symbol
     * is an <code> Operation1 </code> with <code> UnknownType </code>
     * input and output Types, then it will be substituted by a new
     * <code> Operation1 </code> whose input and output Typess are set
     * to the specified <code> type </code>.
     * </p>
     *
     * @param symbol The Symbol whose UnknownTypes (if any) will be
     *               substituted.  Must not be null.
     *
     * @param type The Type to sustitute each UnknownType with.
     *             Must not be null.
     *
     * @return The Symbol with substituted Types: either the specified Symbol,
     *         if there were no UnknownTypes to substitute, or a new
     *         Symbol with the specified Type substituted for each
     *         UnknownType.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Type<S> - Retypable<S, Type<S>>.
    protected final Retypable<Symbol, Type<? extends Symbol>> substituteUnknownTypes (
        Retypable<Symbol, Type<? extends Symbol>> retypable_symbol,
        final Type<?> type
        )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        System.out.println("!!! type class substituteUnknownTypes 0" );
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               retypable_symbol, type );

        System.out.println("!!! type class substituteUnknownTypes 1" );
        final Type<? extends Symbol> symbol_type =
            retypable_symbol.type ().asType ();

        System.out.println("!!! type class substituteUnknownTypes 2" );
        final SymbolTable substitute_symbol_table =
            new SymbolTable ();
        System.out.println("!!! type class substituteUnknownTypes 3" );
        final Type<? extends Symbol> substitute_type =
            symbol_type.rename ( symbol_type.id ().name (),
                                 substitute_symbol_table );

        System.out.println("!!! type class substituteUnknownTypes 4" );
        final Map<SymbolID<Type<?>>, Type<?>> unknown_types_to_substitute =
            new HashMap<SymbolID<Type<?>>, Type<?>> ();
        System.out.println("!!! type class substituteUnknownTypes 5" );
        final NamespaceVisitor visitor = new NamespaceVisitor ()
        {
            private static final long serialVersionUID =
                TypeClass.serialVersionUID;
            /**
             * @see musaico.foundation.typing.NamespaceVisitor#visit(musaico.foundation.typing.Namespace)
             */
            public NamespaceVisitor.VisitStatus visit (
                                                       Namespace namespace
                                                       )
                throws ParametersMustNotBeNull.Violation,
                       ReturnNeverNull.Violation
            {
        System.out.println("!!! type class substituteUnknownTypes v-1" );
                if ( namespace instanceof UnknownType )
                {
                    System.out.println("!!! type class substituteUnknownTypes v-2" );
                    final UnknownType unknown_type =
                        (UnknownType) namespace;
        System.out.println("!!! type class substituteUnknownTypes v-3" );
                    unknown_types_to_substitute.put ( unknown_type.id (),
                                                      type );
        System.out.println("!!! type class substituteUnknownTypes v-4" );
                    // Don't bother descending into this UnknownType.
                    return NamespaceVisitor.VisitStatus.POP;
                }

        System.out.println("!!! type class substituteUnknownTypes v-5" );
                return NamespaceVisitor.VisitStatus.CONTINUE;
            }
        };

        System.out.println("!!! type class substituteUnknownTypes 6" );
        final NamespaceWalker walker = new NamespaceWalker ( visitor );
        System.out.println("!!! type class substituteUnknownTypes 7" );
        walker.walk ( symbol_type );

        System.out.println("!!! type class substituteUnknownTypes 8" );
        // No UnknownTypes?
        if ( unknown_types_to_substitute.size () == 0 )
        {
        System.out.println("!!! type class substituteUnknownTypes 9" );
            return retypable_symbol;
        }

        System.out.println("!!! type class substituteUnknownTypes 10" );
        // Need to substitute a new Symbol of the new Type for the
        // specified Symbol.
        final SubTypeSubstituteParentType substitute_unknowns =
            new SubTypeSubstituteParentType ( "substitute_unknowns",
                                              unknown_types_to_substitute );

        System.out.println("!!! type class substituteUnknownTypes 11" );
        // Can throw TypesMustHaveSameValueClasses.Violation:
        substitute_unknowns.substituteTypesIn ( substitute_symbol_table );

        System.out.println("!!! type class substituteUnknownTypes 12" );
        final Retypable<Symbol, Type<? extends Symbol>> substitute_symbol =
            (Retypable<Symbol, Type<? extends Symbol>>)
            retypable_symbol.retype ( retypable_symbol.id ().name (),
                                      substitute_type );

        System.out.println("!!! type class substituteUnknownTypes 13" );
        return substitute_symbol;
    }


    /**
     * <p>
     * Returns all SymbolIDs required of a Type which is a member of
     * this TypeClass.
     * </p>
     *
     * @return The SymbolIDs which must be in the SymbolTable of
     *         the specified Type in order to become a member of
     *         (tagged by) this TypeClass.  Never null.  Never contains any
     *         any null elements.
     */
    public final <VALUE extends Object>
        SymbolID<?> [] requiredSymbolIDs ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        final SymbolID<?> [] required_symbol_ids =
            new SymbolID<?> [ this.requiredSymbolIDs.length ];
        System.arraycopy ( this.requiredSymbolIDs, 0,
                           required_symbol_ids, 0,
                           this.requiredSymbolIDs.length );

        return required_symbol_ids;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        if ( this.requiredSymbolIDs == null )
        {
            // Constructor has not completed.
            return super.toString ();
        }

        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "typeclass " );
        sbuf.append ( this.id ().name () );

        // Required symbol IDs:
        sbuf.append ( " {" );
        boolean is_first = true;
        for ( SymbolID<?> required_symbol_id : this.requiredSymbolIDs )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            sbuf.append ( " " );
            sbuf.append ( "" + required_symbol_id );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        sbuf.append ( ", " );

        // Child TypeClasses:
        sbuf.append ( " {" );
        is_first = true;
        for ( TypeClass child_type_class : this.childTypeClasses () )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            sbuf.append ( " child typeclass " );
            // Just output "typeclass NAME", not all the required
            // symbols and grandchild type classes:
            sbuf.append ( "" + child_type_class.id ().name () );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }
}
