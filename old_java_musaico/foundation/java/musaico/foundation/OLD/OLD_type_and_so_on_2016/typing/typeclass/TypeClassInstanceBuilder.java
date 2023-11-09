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
 * Creates TypeClassInstances of a TypeClass, one or more instances per Type.
 * </p>
 *
 *
 * <p>
 * In Java every TypeClass must be Serializable in order to play nicely
 * over RMI.  However be warned that instances of any given TypeClassInstance
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
public class TypeClassInstanceBuilder<TYPE_CLASS extends TypeClass, INSTANCE extends TypeClassInstance, VALUE extends Object>
    implements Filter<INSTANCE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( TypeClassInstanceBuilder.class );


    // Checks method obligations and guarantees.
    private final ObjectContracts contracts;

    // The TypeClass to instantiate.
    private final TYPE_CLASS typeClass;

    // The Type which wants to instantiate the TypeClass.
    private final Type<VALUE> type;

    // The Value<TypeClassInstance> builder.
    private final ValueBuilder<INSTANCE> valueBuilder;


    /**
     * <p>
     * Creates a new TypeClassInstanceBuilder, to create an instance
     * of the specified TypeClass, instantiated by the specified Type.
     * </p>
     *
     * @param type_class The TypeClass to be instantiated.
     *                   Must not be null.
     *
     * @param type The Type which will instantiate the TypeClass.
     *             Must not be null.
     *
     * @param instance_class The class of TypeClassInstance to be built,
     *                       Such as TypeClassInstance.class or some
     *                       derived class.  Must not be null.
     */
    public TypeClassInstanceBuilder (
                                     TYPE_CLASS type_class,
                                     Type<VALUE> type,
                                     Class<INSTANCE> instance_class
                                     )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type_class, type, instance_class );

        this.typeClass = type_class;
        this.type = type;

        final INSTANCE none =
            this.createNone ();
        this.valueBuilder =
            new ValueBuilder<INSTANCE> ( instance_class,
                                         none );

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * <p>
     * Creates a new TypeClassInstance for the Type,
     * with the specified <code> (required SymbolID --&gt; instance ) </code>
     * mapping.
     * </p>
     *
     * <p>
     * This create method can be overridden by derived TypeClassInstanceBuilder
     * classes, in order to provide more specialized
     * TypeClassInstances.  For example, a derived TypeClass "vehicle"
     * might provide methods <code> accelerate () </code> and
     * <code> brake () </code> which invoke the underlying Operations
     * instantiated by the TypeClassInstance.
     * </p>
     *
     * @param child_type_class_instances The <code> child TypeClass --&gt; child TypeClassInstance </code>
     *                                   mapping for the new TypeClassInstance.
     *                                   Must not be null.
     *                                   Must not contain any null
     *                                   keys or values.
     *
     * @param symbols_mapping The <code> (required SymbolID --&gt; Symbol instance ) </code>
     *                        mapping for the new TypeClassInstance.
     *                        Must not be null.  Must not contain any null
     *                        keys or values.
     *
     * @return The new TypeClassInstance for the Type and
     *         the TypeClass.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast TypeClassInstance - INSTANCE.
    protected void addInstanceToBuilder (
                                         Map<TypeClass, TypeClassInstance> child_type_class_instances,
                                         Map<SymbolID<?>, Symbol> symbols_mapping
                                         )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               Parameter3.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               child_type_class_instances,
                               symbols_mapping );
        this.contracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               child_type_class_instances.keySet () );
        this.contracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               child_type_class_instances.values () );
        this.contracts.check ( Parameter3.MustContainNoNulls.CONTRACT,
                               symbols_mapping.keySet () );
        this.contracts.check ( Parameter3.MustContainNoNulls.CONTRACT,
                               symbols_mapping.values () );

        try
        {
            final INSTANCE instance = (INSTANCE)
                new TypeClassInstance ( this.typeClass,
                                        this.type,
                                        child_type_class_instances,
                                        symbols_mapping,
                                        new SymbolTable () );
            if ( this.filter ( instance ).isKept () )
            {
                this.valueBuilder.add ( instance );
            }
        }
        catch ( TypingViolation violation )
        {
            // Do not add the instance to the builder, since it
            // for some reason does not meet criteria.
        }
    }


    /**
     * <p>
     * Steps through the symbols provided by the specified symbols source,
     * and adds to / creates new permutations for every symbol which
     * instantiates a required SymbolID for the TypeClass.
     * </p>
     *
     * @param required_symbol_ids_by_name The Lookup of SymbolID(s)
     *                                    by String name, which is used
     *                                    to check to see if each actual
     *                                    Symbol's id matches a required name,
     *                                    even though it does not necessarily
     *                                    match the whole required SymbolID,
     *                                    because it might have specific
     *                                    instance Type(s) to substitute
     *                                    any UnknownType(s) in the required
     *                                    SymbolID.  For example an
     *                                    OperationID for an Operation
     *                                    <code>indexOf(Unknown):Integer</code>
     *                                    might be instantiated by an
     *                                    Operation
     *                                    <code>indexOf(String):Integer</code>,
     *                                    or by an Operation
     *                                    <code>indexOf(Char):Integer</code>,
     *                                    and so on; all un-equal
     *                                    OperationIDs, yet instantiating
     *                                    the required SymbolID.
     *                                    Must not be null.
     *                                    Must not contain any
     *                                    null keys or values.
     *
     * @param permutations All of the instantiation permutations so far.
     *                     Must not be null.  Must not contain any
     *                     null keys or values.
     *
     * @param symbols_source The Namespace from which actual Symbols
     *                       will be matched to required SymbolIDs.
     *                       For example, a Type instantiating the
     *                       TypeClass, or even the TypeClass itself,
     *                       which can provide default instantiations
     *                       of the required SymbolIDs.  Must not be null.
     */
    protected final void addSymbolPermutations (
            Map<String, List<SymbolID<?>>> required_symbol_ids_by_name,
            List<Map<SymbolID<?>, Symbol>> permutations,
            Namespace symbols_source
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  required_symbol_ids_by_name,
                                  permutations,
                                  symbols_source );
        this.contracts ().check ( Parameter1.MustContainNoNulls.CONTRACT,
                                  required_symbol_ids_by_name.keySet () );
        this.contracts ().check ( Parameter1.MustContainNoNulls.CONTRACT,
                                  required_symbol_ids_by_name.values () );
        this.contracts ().check ( Parameter2.MustContainNoNulls.CONTRACT,
                                  symbols_source );

        for ( SymbolType actual_symbol_type : symbols_source.symbolTypes () )
        {
            // Only look at non-private Symbols.
            for ( Symbol actual_symbol
                      : symbols_source.symbols ( actual_symbol_type.asType () ) )
            {
                final SymbolID<?> actual_symbol_id = actual_symbol.id ();
                final String actual_name = actual_symbol_id.name ();

                final List<SymbolID<?>> required_symbol_ids_list =
                    required_symbol_ids_by_name.get ( actual_name );
                if ( required_symbol_ids_list == null )
                {
                    // We don't care about this SymbolID.
                    continue;
                }

                this.addRequiredSymbolPermutations ( actual_symbol,
                                                     required_symbol_ids_list,
                                                     permutations );
            }
        }
    }


    /**
     * <p>
     * Given an actual Symbol which might instantiate one or more SymbolID(s)
     * required by the TypeClass, add the first instantiation (if any)
     * to each existing permutation, then duplicate all permutations for
     * each subsequent instantiation (if any).
     * </p>
     *
     * <p>
     * For example:
     * </p>
     *
     * <pre>
     *     addRequiredSymbolPermutations ( Operation3 "foo",
     *         { OperationID "foo", ConstantID "foo" },
     *         { permutation1, permutation2 } );
     * </pre>
     *
     * <p>
     * For actual symbol Operation3 "foo" which might instantiate
     * either a required OperationID of the same name, or a required
     * Constant of the same name, with the specified permutations
     * of required-SymbolIDs-to-actual-Symbols mappings.
     * </p>
     *
     * <p>
     * This might result in the instantiation
     * <code> OperationID "foo" = Operation3 "foo" </code> being added
     * to both permutation1 and permutation2.
     * </p>
     *
     * @param actual_symbol The actual Symbol which might instantiate
     *                      0 or more required SymbolIDs.  Must not be null.
     *
     * @param required_symbol_ids_list The list of required SymbolIDs which
     *                                 the specified Symbol might or
     *                                 might not instantiate.
     *                                 Must not be null.
     *                                 Must not contain any null elements.
     *
     * @param permutations All of the instantiation permutations so far.
     *                     Must not be null.  Must not contain any
     *                     null keys or values.
     *
     * @return The required SymbolID(s) which are instantiated by the
     *         specified actual_symbol, if any.  Can be empty.
     *         Never null.  Never contains any null elements.
     */
    protected final List<SymbolID<?>> addRequiredSymbolPermutations (
                                                                     Symbol actual_symbol,
                                                                     List<SymbolID<?>> required_symbol_ids_list,
                                                                     List<Map<SymbolID<?>, Symbol>> permutations
                                                                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               Parameter3.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  actual_symbol,
                                  required_symbol_ids_list,
                                  permutations );
        this.contracts ().check ( Parameter2.MustContainNoNulls.CONTRACT,
                                  required_symbol_ids_list );
        this.contracts ().check ( Parameter3.MustContainNoNulls.CONTRACT,
                                  permutations );

        final List<SymbolID<?>> instantiated_required_symbol_ids =
            new ArrayList<SymbolID<?>> ();

        for ( SymbolID<?> required_symbol_id : required_symbol_ids_list )
        {
            if ( ! this.typeClass.isInstantiatedBy ( required_symbol_id,
                                                     actual_symbol ) )
            {
                continue;
            }

            // If multiple required SymbolIDs are instantiated by
            // this actual Symbol then we will
            // duplicate the permutations and overwrite the mapping
            // in the duplicated half.
            // If not we'll just overwrite the mapping in the existing
            // permutations.
            final List<Map<SymbolID<?>, Symbol>> new_permutations =
                new ArrayList<Map<SymbolID<?>, Symbol>> ();
            for ( Map<SymbolID<?>, Symbol> permutation : permutations )
            {
                if ( ! permutation.containsKey ( required_symbol_id ) )
                {
                    permutation.put ( required_symbol_id,
                                      actual_symbol );
                }
                else
                {
                    final Map<SymbolID<?>, Symbol> new_permutation =
                        new HashMap<SymbolID<?>, Symbol> ( permutation );
                    new_permutation.put ( required_symbol_id,
                                          actual_symbol );
                    new_permutations.add ( new_permutation );
                }
            }

            permutations.addAll ( new_permutations );

            instantiated_required_symbol_ids.add ( required_symbol_id );
        }

        return instantiated_required_symbol_ids;
    }


    /**
     * <p>
     * Creates one or more TypeClassInstance(s) of the TypeClass
     * for the Type.
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
     * @return One or more newly created TypeClassInstance(s), providing
     *         the tie between the Type and the TypeClass
     *         (multiple TypeClassInstances only if there are multiple
     *         ways of resolving the required SymbolIDs);
     *         or No TypeClassInstance if the Type does not meet
     *         the requirements of the TypeClass.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Retypable<?> - Retypable<...>.
    protected final Value<INSTANCE> build ()
        throws ReturnNeverNull.Violation
    {
        // If the TypeClass has child TypeClasses, then the
        // TypeClassInstances and Types children of the Type can be
        // used to replace UnknownTypes, or even the toplevel Type
        // itself can instantiate the child TypeClasses and then replace
        // the UnknownTypes.
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 1");
        final Map<TypeClass, Value<TypeClassInstance>>
            child_type_class_instances =
            new HashMap<TypeClass, Value<TypeClassInstance>> ();
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 2");
        if ( ! this.typeClass.findCreateChildInstances (
                       this.type,
                       child_type_class_instances )
             .isKept () )
        {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 3");
            // Type does not provide instances of the child TypeClasses.
            // We cannot use it to instantiate the TypeClass.
            final List<TypeClass> missing_child_instances =
                new ArrayList<TypeClass> ();
            for ( TypeClass child_type_class : child_type_class_instances.keySet () )
            {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 4");
                final Value<TypeClassInstance> v_child_instance =
                    child_type_class_instances.get ( child_type_class );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 5");
                if ( v_child_instance == null
                     || ! v_child_instance.hasValue () )
                {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 6");
                    missing_child_instances.add ( child_type_class );
                }
            }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 7");
            final TypeMustContainChildTypeClassInstance.Violation violation =
                new TypeMustContainChildTypeClassInstance ( this.typeClass,
                                                            this.type )
                .violation ( this.typeClass,
                             missing_child_instances );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 8");
            return this.valueBuilder.buildViolation ( violation );
        }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 9");
        // Accumulate all possible permutations of the child
        // TypeClassInstances.
        final List<Map<TypeClass, TypeClassInstance>>
            child_instance_permutations =
            new ArrayList<Map<TypeClass, TypeClassInstance>> ();
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 10");
        // There is always one permutation, even if there are
        // no child TypeClasses to instantiate.
        child_instance_permutations.add (
            new HashMap<TypeClass, TypeClassInstance> () );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 11");
        for ( TypeClass child_type_class
                  : child_type_class_instances.keySet () )
        {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 12");
            final Value<TypeClassInstance> child_instances =
                child_type_class_instances.get ( child_type_class );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 13");
            int ci = 0;
            for ( TypeClassInstance child_instance : child_instances )
            {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 14");
                if ( ci == 0 )
                {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 15");
                    // First TypeClassInstance of the current TypeClass.
                    for ( Map<TypeClass, TypeClassInstance> permutation
                              : child_instance_permutations )
                    {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 16");
                        permutation.put ( child_type_class, child_instance );
                    }
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 17");
                }
                else
                {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 18");
                    // 2nd, 3rd, 4th, ... TypeClassInstance of the
                    // current TypeClass.
                    final List<Map<TypeClass, TypeClassInstance>>
                        copy_of_permutations =
                        new ArrayList<Map<TypeClass, TypeClassInstance>> (
                            child_instance_permutations );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 19");
                    for ( Map<TypeClass, TypeClassInstance> permutation
                              : copy_of_permutations )
                    {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 20");
                        final Map<TypeClass, TypeClassInstance>
                            new_permutation =
                            new HashMap<TypeClass, TypeClassInstance> (
                                permutation );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 21");
                        new_permutation.put ( child_type_class,
                                              child_instance );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 22");
                        child_instance_permutations.add ( new_permutation );
                    }
                }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 23");
                ci ++;
            }
        }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 24");
        // Prune any duplicate permutations.
        for ( int p1 = 1; p1 < child_instance_permutations.size (); p1 ++ )
        {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 25");
            final Map<TypeClass, TypeClassInstance> permutation1 =
                child_instance_permutations.get ( p1 );

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 26");
            for ( int p2 = 0; p2 < p1; p2 ++ )
            {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 27");
                final Map<TypeClass, TypeClassInstance> permutation2 =
                    child_instance_permutations.get ( p2 );

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 28");
                boolean is_p1_duplicate_of_p2 = true;
                for ( TypeClass child_type_class
                          : child_type_class_instances.keySet () )
                {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 29");
                    final TypeClassInstance child1 =
                        permutation1.get ( child_type_class );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 30");
                    final TypeClassInstance child2 =
                        permutation2.get ( child_type_class );

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 31");
                    if ( ! child1.equals ( child2 ) )
                    {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 32");
                        is_p1_duplicate_of_p2 = false;
                        break;
                    }
                }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 33");
                if ( ! is_p1_duplicate_of_p2 )
                {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 34");
                    continue;
                }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 35");
                child_instance_permutations.remove ( p1 );
                p1 --;
                break;
            }
        }


        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 36");
        // Now begin working on the Symbols required by the TypeClass.
        final List<SymbolID<?>> required_symbol_ids =
            new ArrayList<SymbolID<?>> ();
        for ( SymbolID<?> required_symbol_id
                  : this.typeClass.requiredSymbolIDs () )
        {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 37");
            required_symbol_ids.add ( required_symbol_id );
        }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 38");
        final Map<String, List<SymbolID<?>>> required_symbol_ids_by_name =
            new HashMap<String, List<SymbolID<?>>> ();
        for ( SymbolID<?> required_symbol_id : required_symbol_ids )
        {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 39");
            final String required_name = required_symbol_id.name ();
            List<SymbolID<?>> required_symbol_ids_list =
                required_symbol_ids_by_name.get ( required_name );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 40");
            if ( required_symbol_ids_list == null )
            {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 41");
                required_symbol_ids_list = new ArrayList<SymbolID<?>> ();
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 42");
                required_symbol_ids_by_name.put ( required_name,
                                                  required_symbol_ids_list );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 43");
            }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 44");
            required_symbol_ids_list.add ( required_symbol_id );
        }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 45");
        final List<Map<SymbolID<?>, Symbol>> symbol_permutations =
            new ArrayList<Map<SymbolID<?>, Symbol>> ();

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 46");
        // Start the first permutation.
        final Map<SymbolID<?>, Symbol> first_permutation =
            new HashMap<SymbolID<?>, Symbol> ();
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 47");
        symbol_permutations.add ( first_permutation );

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 48");
        // First add the Symbols that are already provided by the TypeClass.
        this.addSymbolPermutations ( required_symbol_ids_by_name,
                                     symbol_permutations,
                                     this.typeClass );

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 49");
        // Now add the Symbols that are provided by the Type.
        this.addSymbolPermutations ( required_symbol_ids_by_name,
                                     symbol_permutations,
                                     this.type );

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 50");
        List<Map<SymbolID<?>, Symbol>> complete_symbol_permutations =
            new ArrayList<Map<SymbolID<?>, Symbol>> ();
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 51");
        final List<SymbolID<?>> missing_required_symbol_ids =
            new ArrayList<SymbolID<?>> ( required_symbol_ids );
        for ( Map<SymbolID<?>, Symbol> permutation : symbol_permutations )
        {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 52");
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 53");
            final List<SymbolID<?>> missing_for_this_permutation =
                new ArrayList<SymbolID<?>> ( required_symbol_ids );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 54");
            for ( SymbolID<?> required_symbol_id : required_symbol_ids )
            {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 55");
                Symbol actual_symbol =
                    permutation.get ( required_symbol_id );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 56");
                if ( actual_symbol == null )
                {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 57");
                    // Maybe the TypeClass has a default instantiation.
                    actual_symbol =
                        this.typeClass.symbol ( required_symbol_id ).orNull ();
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 58");
                    if ( actual_symbol == null )
                    {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 59");
                        // No explicit instantiation, and no default.
                        // This permutation can't work.
                        break;
                    }
                }
                else if ( ! ( actual_symbol instanceof Retypable ) )
                {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 60");
                    // Use this Symbol.
                    // If it, for some reason, contains UnknownTypes
                    // then too bad, it will lead to a runtime error
                    // when someone tries to use the TypeClassInstance.
                    missing_for_this_permutation.remove ( required_symbol_id );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 61");
                    continue;
                }


        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 62");
                final Retypable<Symbol, Type<? extends Symbol>>
                    actual_retypable_symbol =
                    (Retypable<Symbol, Type<? extends Symbol>>)
                    actual_symbol;

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 63");
                try
                {
                    System.err.println("!!! TYPE CLASS INSTANCE BUILDER 64 " + this.typeClass.getClass ().getName () + " " + this.typeClass);
                    actual_symbol =
                        this.typeClass.substituteUnknownTypes (
                                actual_retypable_symbol,
                                this.type );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 65");
                }
                catch ( TypesMustHaveSameValueClasses.Violation violation )
                {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 66");
                    // Oh dear.  How did this happen?!?
                    // The Symbol whose Type contains UnknownTypes
                    // did not like the Type we tried to substtute
                    // for the UnknownTypes.  Give up on this permutation.
                    break;
                }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 67");
                // If we get this far then we know the current SymbolID
                // is NOT missing from this permutation.
                missing_for_this_permutation.remove ( required_symbol_id );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 68");
            }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 69");
            if ( missing_for_this_permutation.size ()
                 < missing_required_symbol_ids.size () )
            {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 70");
                missing_required_symbol_ids.clear ();
                missing_required_symbol_ids.addAll ( missing_for_this_permutation );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 71");
            }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 72");
            if ( missing_for_this_permutation.size () == 0 )
            {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 73");
                complete_symbol_permutations.add ( permutation );
            }
        }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 74");
        // No complete symbol permutations?
        if ( missing_required_symbol_ids.size () > 0 )
        {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 75");
            final TypeMustInstantiateTypeClass contract =
                new TypeMustInstantiateTypeClass ( this.typeClass,
                                                   this.type );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 76");
            final TypingViolation violation =
                contract.violation ( this.typeClass,
                                     missing_required_symbol_ids );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 77");
            return this.valueBuilder.buildViolation ( violation );
        }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 78");
        // Prune any duplicate Symbol permutations.
        for ( int p1 = 1; p1 < complete_symbol_permutations.size (); p1 ++ )
        {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 79");
            final Map<SymbolID<?>, Symbol> permutation1 =
                complete_symbol_permutations.get ( p1 );

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 80");
            for ( int p2 = 0; p2 < p1; p2 ++ )
            {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 81");
                final Map<SymbolID<?>, Symbol> permutation2 =
                    complete_symbol_permutations.get ( p2 );

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 82");
                boolean is_p1_duplicate_of_p2 = true;
                for ( SymbolID<?> required_symbol_id : required_symbol_ids )
                {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 83");
                    final Symbol actual_symbol1 =
                        permutation1.get ( required_symbol_id );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 84");
                    final Symbol actual_symbol2 =
                        permutation2.get ( required_symbol_id );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 85");

                    if ( ! actual_symbol1.equals ( actual_symbol2 ) )
                    {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 86");
                        is_p1_duplicate_of_p2 = false;
                        break;
                    }
                }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 87");
                if ( ! is_p1_duplicate_of_p2 )
                {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 88");
                    continue;
                }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 89");
                complete_symbol_permutations.remove ( p1 );
                p1 --;
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 90");
                break;
            }
        }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 91");
        // Now create the TypeClassInstance(s).
        for ( Map<TypeClass, TypeClassInstance> child_instances_permutation
                  : child_instance_permutations )
        {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 92");
            for ( Map<SymbolID<?>, Symbol> symbols_permutation
                      : complete_symbol_permutations )
            {
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 93");
                this.addInstanceToBuilder ( child_instances_permutation,
                                            symbols_permutation );
        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 94");
            }
        }

        System.err.println("!!! TYPE CLASS INSTANCE BUILDER 95");
        return this.valueBuilder.build ();
    }


    /**
     * @return The ObjectContracts for this TypeClassInstanceBuilder.
     *         Checks method obligations and guarantees.  Never null.
     */
    protected final ObjectContracts contracts ()
    {
        return this.contracts;
    }


    /**
     * <p>
     * Creates a none TypeClassInstance which always fails to return
     * any required symbols or evaluate any operations from the TypeClass.
     * </p>
     *
     * <p>
     * This create method can be overridden by derived TypeClassInstanceBuilder
     * classes, in order to provide more specialized "none"
     * TypeClassInstances.  For example, a derived TypeClass "vehicle"
     * might provide methods <code> accelerate () </code> and
     * <code> brake () </code> which invoke the underlying Operations
     * instantiated by the TypeClassInstance (in the case of none,
     * typically AlwaysFail).
     * </p>
     *
     * @return The none TypeClassInstance for the Type and
     *         TypeClass.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast TypeClassInstance - INSTANCE.
    protected INSTANCE createNone ()
        throws ReturnNeverNull.Violation
    {
        return (INSTANCE)
            new NoTypeClassInstance ( this.typeClass,
                                      this.type );
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     *
     * <p>
     * Filters out TypeClassInstances that are not viable for this
     * builder or the TypeClass.  For example, if there are constraints
     * on TypeClassInstances for the TypeClass, they can be enforced
     * here.  As each TypeClassInstance permutation is created, it
     * is filtered then, if kept, added to the final output
     * TypeClassInstance permutations.
     * </p>
     *
     * <p>
     * The default implementation keeps every TypeClassInstance.
     * This method can be overridden by derived TypeClassInstanceBuilders
     * to enforce stricter requirements.
     * </p>
     */
    @Override
    public FilterState filter (
                               INSTANCE instance
                               )
    {
        return FilterState.KEPT;
    }


    /**
     * @return The Type which is to instantiate the TypeClass.
     *         Never null.
     */
    public final Type<VALUE> type ()
    {
        return this.type;
    }


    /**
     * @return The TypeClass which is to be instantiated.
     *         Never null.
     */
    public final TYPE_CLASS typeClass ()
    {
        return this.typeClass;
    }


    /**
     * @return The Value&lt;TypeClassInstance&gt; builder.
     *         Never null.
     */
    protected final ValueBuilder<INSTANCE> valueBuilder ()
    {
        return this.valueBuilder;
    }
}
