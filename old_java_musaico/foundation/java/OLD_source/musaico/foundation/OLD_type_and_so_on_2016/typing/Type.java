package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Error;
import musaico.foundation.value.No;
import musaico.foundation.value.NoneGenerator;
import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * A Type describes a class of objects, possibly with other modifier
 * Tags attached (such as an ArrayTag or a PrivateTag and so on).
 * </p>
 *
 * <p>
 * Type forms the under-the-hood basis of a typing environment,
 * providing type casters and other operations to both simplify
 * application development and enforce stringent rules.
 * </p>
 *
 * <p>
 * For example, an Integer[] array might have a Type which enforces
 * length 3.  Then any instance of that Type is always guaranteed to
 * have length 3, providing the possibility of compile-time type safety,
 * or at least early runtime detection of mis-matches between data.
 * An array of Integers of length 2 would not be compatible with such
 * a Type.
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
public interface Type<VALUE extends Object>
    extends Namespace, NoneGenerator<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Just to make sure everything is resolved in the right order. */
    public static final Object DUPLICATE_JUNK1 =
        Kind.ROOT;

    /** Just to make sure everything is resolved in the right order. */
    public static final Object DUPLICATE_JUNK2 =
        Namespace.NONE;

    /** No Type at all in no typing environment. */
    public static final NoType<Object> NONE =
        new NoType<Object> ( "notype" ); // Put in Namespace.ROOT by Namespace.


    /**
     * <p>
     * Creates a new TypedValueBuilder to build values for instances
     * of this Type.
     * </p>
     *
     * <p>
     * The TypedValueBuilder checks the constraints made by this Type whenever
     * the caller executes its <code> build () </code> method.
     * </p>
     *
     * <p>
     * The TypedValueBuilder is NOT thread-safe.
     * </p>
     *
     * <p>
     * For example, to build One new String value:
     * </p>
     *
     * <pre>
     *     Type<String> string = ...;
     *     Value<String> value =
     *         string.builder ().add ( "John" ).build ();
     * </pre>
     *
     * <p>
     * Or to build a vector of a String type:
     * </p>
     *
     * <pre>
     *     Type<String> string = ...;
     *     Value<String> vector = string.builder ()
     *         .add ( "John" )
     *         .add ( "Q." )
     *         .add ( "Doe" )
     *         .build ();
     * </pre>
     *
     * <p>
     * And so on.
     * </p>
     *
     * @return A new TypedValueBuilder for this Type.  Never null.
     */
    public abstract TypedValueBuilder<VALUE> builder ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Checks the specified value against all constraints asserted
     * by this Type and all its Tags.
     * </p>
     *
     * @param value The value to check for compliance with this
     *              Type's constraints.  Must not be null.
     *
     * @throws TypingViolation If any value constraint is not met.
     */
    public abstract void checkValue (
                                     Value<?> value
                                     )
        throws ParametersMustNotBeNull.Violation,
               TypingViolation;


    /**
     * <p>
     * Returns an Error Value for this type, with the "none" object, and
     * the specified Violation.
     * </p>
     *
     * @see musaico.foundation.typing.Type#none()
     *
     * @param violation The Violation which led to an Error Value.
     *                  Must not be null.
     *
     * @return A newly created Error Value for this type, with the specified
     *         Violation.  Never null.
     */
    public abstract <VIOLATION extends Throwable & Violation>
        Error<VALUE> errorValue (
                                 VIOLATION violation
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @see musaico.foundation.typing.Symbol#id()
     */
    @Override
    public abstract TypeID id ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new Term instance of this type, with the specified value.
     * </p>
     *
     * <p>
     * For example, to create a new String instance:
     * </p>
     *
     * <pre>
     *     Type<String> string = ...;
     *     Term<String> name =
     *         string.instance ( new One<String> ( String.class,
     *                                             "Bill" ) );
     * </pre>
     *
     * <p>
     * This method checks the constraints of this Type, failing
     * if the specified value does not meet the requirements.
     * For example a positive number Type might disallow numbers
     * equal to or below 0.  Or a Type with a Length tag might fail the
     * value if it is not a vector of the specified length.
     * And so on.
     * </p>
     *
     * @param value The value of the new Term to create.
     *              Must not be null.
     *
     * @return The new Term instance of this
     *         Type.  If the specified value did not meet any of
     *         this Type's constraints, then a Term with
     *         No value will be returned.  Never null.
     */
    public abstract Term<VALUE> instance (
                                          Value<VALUE> value
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new Term instance of this type, with the specified value(s).
     * </p>
     *
     * <p>
     * For example, to create a new String instance:
     * </p>
     *
     * <pre>
     *     Type<String> string = ...;
     *     Term<String> name = string.instance ( "Bill" );
     * </pre>
     *
     * <p>
     * Or a many-valued String instance:
     * </p>
     *
     * <pre>
     *     Type<String> string = ...;
     *     Term<String> name = string.instance ( "Jack", "and", "Jill" );
     * </pre>
     *
     * <p>
     * This method is simply a shortcut for:
     * </p>
     *
     * <pre>
     *     Type<VALUE> type = ...;
     *     Value<VALUE> value = type.builder ().add ( ... )... .build ();
     *     Term<VALUE> instance = type.instance ( value );
     * </pre>
     *
     * <p>
     * Callers should note in particular that the builder may
     * <code> build () </code> No value if the values passed in do
     * not meet the constraints of this Type.
     * For example a positive numbers Type might disallow numbers
     * below 0.  Or a Type with a Length tag might fail the
     * value if it is not a vector of the specified length.
     * And so on.
     * </p>
     *
     * @param elements The value(s) of the new Term instance to create.
     *                 Must not be null.
     *
     * @return The new Term instance of this
     *         Type.  If the specified value(s) did not meet any of
     *         this Type's constraints, then a Term with
     *         No value will be returned.  Never null.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution / generic vararg
    public abstract Term<VALUE> instance (
                                          VALUE ... elements
                                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns true if the specified Value conforms to this Type's
     * value Class and constraints, or false if it does not.
     * </p>
     *
     * @param value The Value to check against this Type's value class
     *              and constraints.  Must not be null.
     *
     * @return True if the specified Value is an instance of this Type,
     *         false if it does not match the value class and/or constraints.
     */
    public abstract boolean isInstance (
                                        Value<?> value
                                        )
        throws ParametersMustNotBeNull.Violation;


    /**
     * @see musaico.foundation.value.NoneGenerator#none()
     *
     * <p>
     * Returns the "no value" object for this Type.
     * </p>
     *
     * <p>
     * The none value is used as a fallback, whenever the user of a Value
     * of this Type invokes <code> orNone () </code> on a failed Value
     * such as <code> No&lt;VALUE&gt; </code>
     * or <code> Error&lt;VALUE&gt; </code>.
     * </p>
     *
     * @return The "no value" value for this type.  Never null.
     */
    @Override
    public abstract VALUE none ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a No Value for this type, with the "none" object, and
     * a default violation, such as a violation of
     * the ValueMustNotBeEmpty contract.
     * </p>
     *
     * @see musaico.foundation.typing.Type#none()
     *
     * @return A newly created No Value for this type, with a
     *         default violation.  Never null.
     */
    public abstract <VIOLATION extends Throwable & Violation>
        No<VALUE> noValue ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a No Value for this type, with the "none" object, and
     * the specified Violation.
     * </p>
     *
     * @see musaico.foundation.typing.Type#none()
     *
     * @param violation The Violation which led to No Value.
     *                  Must not be null.
     *
     * @return A newly created No Value for this type, with the specified
     *         Violation.  Never null.
     */
    public abstract <VIOLATION extends Throwable & Violation>
        No<VALUE> noValue (
                           VIOLATION violation
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @see musaico.foundation.typing.Namespace#rename(java.lang.String, musaico.foundation.typing.SymbolTable)
     */
    @Override
    public abstract Type<VALUE> rename (
                                        String name,
                                        SymbolTable symbol_table
                                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new sub-type of this Type with the specified Tag
     * modifiers added, and registers it in this type's typing
     * environment.
     * <p>
     *
     * <p>
     * For example, something like the following might create
     * a Private sub-type of a String type, so that instances
     * are not readable:
     * </p>
     *
     * <pre>
     *     Type<String> string = ...;
     *     Type<String> private_string = string.sub ( new PrivateTag () )
     *                                       .orThrowChecked ();
     * </pre>
     *
     * @param tags One or more tags to add to the sub-type of this type.
     *             Must not be null.  Must not contain any null elements.
     *             Must not contain any duplicate tags.  Must not contain
     *             any tags which are already in this Type.  Each Tag
     *             must not contain any symbols which conflict with the
     *             symbols of this Type or with the symbols of the other
     *             Tags, since the SymbolTable of each Tag is copied
     *             into the SymbolTable of the new sub-type.
     *
     * @return The newly created and registered Type, or a NoType
     *         if the new type could not be created or registered for
     *         some reason (for example one of the specified Tags is
     *         already contained in this Type).  Never null.
     */
    public abstract ZeroOrOne<Type<VALUE>> sub (
                                                Tag ... tags
                                                )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /** Every Type must implement all the methods declared by
     *  Namespace. */


    /**
     * <p>
     * Returns the typecaster Operation from this Type to the specified Type,
     * if any is in this Type's or the target Type's SymbolTable;
     * or if the value class is common to this and that Types
     * (such as Type&lt;String&gt; text cast to
     * Type&lt;String&gt; text[decimal]), then an
     * Identity cast is returned; otherwise a NoCast is returned.
     * </p>
     *
     * @param that The Type to cast to.  Must not be null.
     *
     * @return The cast Operation.  Possibly a NoCast which always fails.
     *         Never null.
     */
    public abstract <TO extends Object>
        Operation1<VALUE, TO> to (
                                  Type<TO> that
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return This Type's type Kind, also returned by
     *         <code> id ().type () </code>.
     *
     * @see musaico.foundation.typing.SymbolID#type()
     */
    @Override
    public abstract Kind type ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The class of objects represented by this Type.  Every
     *         Term of this type has a Value of zero or more objects
     *         of this type's value class.  Never null.
     */
    public abstract Class<VALUE> valueClass ()
        throws ReturnNeverNull.Violation;
}
