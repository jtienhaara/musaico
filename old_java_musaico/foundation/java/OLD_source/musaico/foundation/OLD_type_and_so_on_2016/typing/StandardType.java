package musaico.foundation.typing;

import java.io.Serializable;

import java.util.ArrayList;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.Error;
import musaico.foundation.value.No;
import musaico.foundation.value.NotOne;
import musaico.foundation.value.One;
import musaico.foundation.value.NonBlocking;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueMustNotBeEmpty;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * A no-bells-or-whistles Type, with the core behaviour injected to
 * the constructor.
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
public class StandardType<VALUE extends Object>
    extends AbstractNamespace<TypeID, Type<?>>
    implements Type<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( StandardType.class );


    // The base name of this type, without any tag names appended to
    //  the end.  For example the base name of "string[tag1, tag2, tag3]"
    //  is "string".  The base name of "string" is also "string".
    private final String rawTypeName;

    // The value class of this StandardType.  Every value
    //  is made up of 0 or more instances of this class.
    private final Class<VALUE> valueClass;

    // The default "no value" for callers who want to fall back
    //  on some non-null, non-exception-inducing default value
    //  whenever there is an error or an empty result from an
    //  operation and so on.
    private final VALUE none;


    /**
     * <p>
     * Creates a new StandardType for the specified class of objects, with
     * the specified "none" value.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Type will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param raw_type_name The name of this new Type.  The name will be
     *                      used to create a SymbolID for this Type.
     *                      For example, "string" or "int" or "SomeObject".
     *                      Note that if any Tags are currently in the
     *                      SymbolTable then the tag names will be appended
     *                      to the name.  For example, "string[tag1, tag2]"
     *                      or "int[tag1, tag2, tag3]" and so on.
     *                      Must not be null.
     *
     * @param value_class The class of objects represented by
     *                    this StandardType.  Must not be null.
     *
     * @param none The default value to fall back on, for callers who do
     *             not want to deal with nulls or exceptions.
     *             For example, an empty String, or 0, or SomeObject.NONE.
     *             Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Type.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardType,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     */
    public StandardType (
                         Namespace parent_namespace,
                         String raw_type_name,
                         Class<VALUE> value_class,
                         VALUE none,
                         SymbolTable symbol_table
                         )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent_namespace,
               Kind.ROOT,
               raw_type_name, value_class, none, symbol_table,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new StandardType for the specified class of objects, with
     * the specified "none" value and the specified kind
     * (Type).
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Type will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param kind The kind (Type) of this new Type.  Might be
     *             Kind.ROOT, or some other kind, such as
     *             a UMLKind and so on.  Must not be null.
     *
     * @param raw_type_name The name of this new Type.  The name will be
     *                      used to create a SymbolID for this Type.
     *                      For example, "string" or "int" or "SomeObject".
     *                      Note that if any Tags are currently in the
     *                      SymbolTable then the tag names will be appended
     *                      to the name.  For example, "string[tag1, tag2]"
     *                      or "int[tag1, tag2, tag3]" and so on.
     *                      Must not be null.
     *
     * @param value_class The class of objects represented by
     *                    this StandardType.  Must not be null.
     *
     * @param none The default value to fall back on, for callers who do
     *             not want to deal with nulls or exceptions.
     *             For example, an empty String, or 0, or SomeObject.NONE.
     *             Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Type.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardType,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this StandardType.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public StandardType (
                         Namespace parent_namespace,
                         Kind kind,
                         String raw_type_name,
                         Class<VALUE> value_class,
                         VALUE none,
                         SymbolTable symbol_table,
                         Metadata metadata
                         )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent_namespace,
               kind,
               raw_type_name,
               "", // tag_names
               value_class,
               none,
               symbol_table,
               metadata );
    }
    /**
     * <p>
     * Creates a new StandardType for the specified class of objects, with
     * the specified "none" value and the specified kind
     * (Type).
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Type will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param kind The kind (Type) of this new Type.  Might be
     *             Kind.ROOT, or some other kind, such as
     *             a UMLKind and so on.  Must not be null.
     *
     * @param raw_type_name The name of this new Type.  The name will be
     *                      used to create a SymbolID for this Type.
     *                      For example, "string" or "int" or "SomeObject".
     *                      Note that if any Tags are currently in the
     *                      SymbolTable then the tag names will be appended
     *                      to the name.  For example, "string[tag1, tag2]"
     *                      or "int[tag1, tag2, tag3]" and so on.
     *                      Must not be null.
     *
     * @param tag_names The "tag1,tag2,..." tag names to append to
     *                  the raw type name to get "Type[tag1,tag2,...]'.
     *                  Must not be null.
     *
     * @param value_class The class of objects represented by
     *                    this StandardType.  Must not be null.
     *
     * @param none The default value to fall back on, for callers who do
     *             not want to deal with nulls or exceptions.
     *             For example, an empty String, or 0, or SomeObject.NONE.
     *             Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Type.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardType,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this StandardType.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public StandardType (
                         Namespace parent_namespace,
                         Kind kind,
                         String raw_type_name,
                         String tag_names,
                         Class<VALUE> value_class,
                         VALUE none,
                         SymbolTable symbol_table,
                         Metadata metadata
                         )
        throws ParametersMustNotBeNull.Violation
    {
        super ( parent_namespace,
                new TypeID ( kind,
                             raw_type_name,
                             tag_names,
                             Visibility.PUBLIC ),
                symbol_table,
                metadata );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value_class,
                               none,
                               metadata );

        this.rawTypeName = raw_type_name;
        this.valueClass = value_class;
        this.none = none;
    }


    /**
     * @see musaico.foundation.typing.Type#builder()
     */
    @Override
    public final TypedValueBuilder<VALUE> builder ()
    {
        return new TypedValueBuilder<VALUE> ( this );
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
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  value );

        if ( ! this.valueClass.isAssignableFrom ( value.expectedClass () ) )
        {
            // Not even the right class of value(s).
            final ValueMustBeInstanceOfClass contract =
                new ValueMustBeInstanceOfClass ( this.valueClass );
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
     * @see musaico.foundation.typing.Type#errorValue(java.lang.Throwable)
     */
    @Override
    public final <VIOLATION extends Throwable & Violation>
        Error<VALUE> errorValue (
                                 VIOLATION violation
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  violation );

        return new Error<VALUE> ( this.valueClass (),
                                  violation );
    }


    /**
     * @see musaico.foundation.typing.AbstractNamespace#equalsNamespace(musaico.foundation.typing.Namespace)
     */
    @Override
    protected final boolean equalsNamespace (
                                             Type<?> other_type
                                             )
    {
        if ( other_type.getClass () != this.getClass () )
        {
            return false;
        }

        StandardType<?> that = (StandardType<?>) other_type;

        if ( ! this.valueClass.equals ( that.valueClass )
             || ! this.none.equals ( that.none ) )
        {
            // StandardType w/ fields A,B,C != StandardType w/ fields X,Y,Z.
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
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
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  value );

        Value<VALUE> final_value = null;
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

        final Term<VALUE> instance;
        if ( final_value instanceof NonBlocking )
        {
            // The value is non-blocking, so return a constant.
            final NonBlocking<VALUE> non_blocking_value =
                (NonBlocking<VALUE>) final_value;
            instance =
                new Constant<VALUE> ( this, non_blocking_value,
                                      this.metadata ().renew () );
        }
        else
        {
            // The value is blocking for some reason.
            // Return a BlockingConstant to resolve the value.
            instance = new BlockingConstant<VALUE> ( this, final_value,
                                                     this.metadata ().renew () );
        }

        return instance;
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
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  (Object []) elements );
        this.contracts ().check ( Parameter1.MustContainNoNulls.CONTRACT,
                                  elements );

        final TypedValueBuilder<VALUE> builder =
            new TypedValueBuilder<VALUE> ( this );
        for ( VALUE element : elements )
        {
            builder.add ( element );
        }

        final Value<VALUE> value = builder.build ();

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
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
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
            // For whatever reason, the specified value is not
            // an instance of this type.
            return false;
        }
    }


    /**
     * @see musaico.foundation.typing.Type#none()
     */
    @Override
    public final VALUE none ()
        throws ReturnNeverNull.Violation
    {
        return this.none;
    }


    /**
     * @see musaico.foundation.typing.Type#noValue()
     */
    @Override
    public final <VIOLATION extends Throwable & Violation>
        No<VALUE> noValue ()
        throws ReturnNeverNull.Violation
    {
        final Iterable<VALUE> no_objects = new ArrayList<VALUE> ();
        final ValueMustNotBeEmpty.Violation violation =
            ValueMustNotBeEmpty.CONTRACT.violation ( this,
                                                     no_objects );
        return new No<VALUE> ( this.valueClass (),
                               violation );
    }


    /**
     * @see musaico.foundation.typing.Type#noValue(java.lang.Throwable)
     */
    @Override
    public final <VIOLATION extends Throwable & Violation>
        No<VALUE> noValue (
                           VIOLATION violation
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  violation );

        return new No<VALUE> ( this.valueClass (),
                               violation );
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public StandardType<VALUE> rename (
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
    public StandardType<VALUE> rename (
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

        return new StandardType<VALUE> ( parent_namespace,
                                         this.type (),
                                         name,
                                         this.valueClass,
                                         this.none,
                                         symbol_table,
                                         this.metadata ().renew () );
    }


    /**
     * @see musaico.foundation.typing.Type#sub(musaico.foundation.typing.Tag[])
     */
    @Override
    @SuppressWarnings("unchecked") // cast root of this Type to Type<VALUE>,
                                   // new sub-Type<?> to Type<VALUE>,
        // Class<SubTypeWorkBench<?>> - Class<SubTypeWorkBench<VALUE>>.
    public final ZeroOrOne<Type<VALUE>> sub (
                                             Tag ... tags
                                             )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  (Object[]) tags ); // Avoid varargs mess.
        this.contracts ().check ( Parameter1.MustContainNoNulls.CONTRACT,
                                  tags );

        Type<VALUE> input_type = this;
        ZeroOrOne<Type<VALUE>> v_output_type = null;
        for ( Operation1<SubTypeWorkBench<?>, SubTypeWorkBench<?>> sub_type
            : this.symbolTable ().symbols ( SubTypeWorkBench.SUB_TYPE_TYPE ) )
        {
            final SymbolTable tagged_symbol_table = new SymbolTable ();
            final TypeBuilder<VALUE> type_builder =
                this.type ().typeBuilder ( this.valueClass (),
                                           tagged_symbol_table );

            final SubTypeWorkBench<VALUE> workbench =
                new SubTypeWorkBench<VALUE> ( input_type,
                                              type_builder,
                                              tags );
            final One<SubTypeWorkBench<?>> v_workbench =
                new One<SubTypeWorkBench<?>> ( SubTypeWorkBench.TYPE.valueClass (),
                                               workbench );

            final Value<SubTypeWorkBench<?>> v_workbench_out =
                sub_type.evaluate ( v_workbench );
            if ( v_workbench_out instanceof NotOne )
            {
                // The sub-typing operation blew up.  Not good.
                final NotOne<SubTypeWorkBench<?>> failed =
                    (NotOne<SubTypeWorkBench<?>>) v_workbench_out;
                final ValueViolation violation =
                    failed.valueViolation ();
                v_output_type =
                    new No<Type<VALUE>> ( new SillyType<Type<VALUE>> ().getTypeClass (),
                                          violation );
            }
            else
            {
                // Success.
                v_output_type = workbench.subType ();
            }

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
            final TypeBuilder<VALUE> type_builder =
                this.type ().typeBuilder ( this.valueClass (),
                                           symbol_table );
            return type_builder.noType ( TypingViolation.NONE );
        }

        // If we generated a Type, replace references to this Type
        // with references to the sub-Type, then check it against all its
        // TagWithTypeConstraint tags.
        if ( v_output_type.hasValue () )
        {
            final Type<VALUE> output_type = v_output_type.orNone ();

            for ( Tag tag : output_type.symbols ( Tag.TYPE ) )
            {
                if ( ! ( tag instanceof TagWithTypeConstraint ) )
                {
                    continue;
                }

                final TagWithTypeConstraint check_tag =
                    (TagWithTypeConstraint) tag;
                final Contract<Type<?>, TypingViolation> constraint =
                    check_tag.typeConstraint ();
                if ( ! constraint.filter ( output_type ).isKept () )
                {
                    final TypingViolation violation =
                        constraint.violation ( check_tag,     //plaintiff
                                               output_type ); //inspectable
                    final SymbolTable symbol_table =
                        new SymbolTable ( this.symbolTable () );
                    final TypeBuilder<VALUE> type_builder =
                        this.type ().typeBuilder ( this.valueClass (),
                                                   symbol_table );
                    return type_builder.noType ( violation );
                }
            }
        }

        return v_output_type;
    }


    // Ugly hack for @*%# generics.
    private static class SillyType<TYPE_CLASS> // Type<?>
    {
        @SuppressWarnings("unchecked")
        public Class<TYPE_CLASS> getTypeClass ()
        {
            return (Class<TYPE_CLASS>) Type.class;
        }
    }


    /**
     * @see musaico.foundation.typing.Type#to(musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Force Op<?, ?> to Op<VALUE, TO>,
        // force Type<TO> to Type<VALUE> when we know the value classes are ==.
    public final <TO extends Object>
        Operation1<VALUE, TO> to (
                                  Type<TO> that
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  that );

        final OperationID<Operation1<VALUE, TO>, TO> cast_id =
            new OperationID<Operation1<VALUE, TO>, TO> ( Operation.CAST,
                new OperationType1<VALUE, TO> ( this, that ) );

        final Operation1<VALUE, TO> cast;
        final Operation1<VALUE, TO> this_cast =
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
            final Operation1<VALUE, TO> that_cast =
                that.symbol ( cast_id ).orNone ();
            if ( that_cast instanceof NoCast )
            {
                // Last ditch effort: if the specified Type has the
                // same value class as this Type, then fall back
                // on the identity caster.
                if ( this.valueClass ().equals ( that.valueClass () ) )
                {
                    // Same value class.  Use Identity.
                    cast = (Operation1<VALUE, TO>)
                        new Identity<VALUE> ( this,
                                              (Type<VALUE>) that );
                }
                else
                {
                    // Nobody has a caster.
                    // Fall back on the NoCast we generated earlier.
                    cast = this_cast;
                }
            }
            else
            {
                // The "to" Type has a caster.
                cast = that_cast;
            }
        }

        return cast;
    }


    /**
     * @see musaico.foundation.typing.Type#type()
     */
    @Override
    public Kind type ()
        throws ReturnNeverNull.Violation
    {
        return this.id ().type ();
    }


    /**
     * @see musaico.foundation.typing.Type#valueClass()
     */
    @Override
    public final Class<VALUE> valueClass ()
        throws ReturnNeverNull.Violation
    {
        return this.valueClass;
    }
}
