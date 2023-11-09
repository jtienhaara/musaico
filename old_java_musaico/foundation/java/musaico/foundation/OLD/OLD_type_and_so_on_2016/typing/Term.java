package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;

import musaico.foundation.value.Synchronicity;
import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * A Term has a value Type and a Value.  A Term might be a constant instance
 * of some Type, or an Expression which evaluates to the value Type,
 * and so on.
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
public interface Term<VALUE extends Object>
    extends Symbol, Retypable<Term<VALUE>, AbstractTermType<? extends Term<VALUE>, VALUE>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Casts the value of this Term to a Term of the
     * specified Type.
     * </p>
     *
     * <p>
     * For example, to cast an Integer to a hexadecimal-coded String,
     * something along the lines of the following might be used:
     * </p>
     *
     * <pre>
     *     Term<Integer> int_term = ...;
     *     Type<String> hex_string_type =
     *         Namespace.ROOT.type ( String.class, StringEncoding.HEXADECIMAL )
     *            .orNone ();
     *     Term<String> hex_term =
     *         int_term.as ( hex_string_type );
     *     String hex = hex_term.value ().orDefault ( "0x00" );
     * </pre>
     *
     * @param target_type The target type to cast to, such as
     *                    a <code> Type<String> </code>.
     *                    Must not be null.
     *
     * @return A new Term, possibly a NoTerm if the typecast
     *         could not be performed.  Never null.
     */
    public abstract <CAST extends Object>
        Term<CAST> as (
                       Type<CAST> target_type
                       )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Evaluates the operation corresponding to the specified ID,
     * using this Term's value as input, and either returns the
     * successful Just output value, or returns No value.  The operation
     * is looked up in the Type of this Term.
     * </p>
     *
     * <p>
     * To block waiting for the Operation to complete, call
     * <code> sync () </code> on the result.  To handle the result
     * asynchronously, call <code> async () </code> on the result.
     * </p>
     *
     * @param operation_id The unique identifier of the Operation to evaluate
     *                     on this Term's value.  Must not be null.
     *
     * @return The output Term, typically an Expression which will
     *         evaluate the Operation identified by the specified id,
     *         using this Term's value as input.  Never null.
     */
    public abstract <OUTPUT extends Object>
        Term<OUTPUT> call (
                           OperationID<Operation1<VALUE, OUTPUT>, OUTPUT> operation_id
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Evaluates the specified operation on this Term's
     * value, and either returns the successful Just output value,
     * or returns No value.
     * </p>
     *
     * <p>
     * To block waiting for the Operation to complete, call
     * <code> sync () </code> on the result.  To handle the result
     * asynchronously, call <code> async () </code> on the result.
     * </p>
     *
     * @param operation The exact Operation to evaluate.
     *                  Must not be null.
     *
     * @return The output Term, typically an Expression which will
     *         evaluate the specified operation on this Term's value.
     *         Never null.
     */
    public abstract <OUTPUT extends Object>
        Term<OUTPUT> call (
                           Operation1<VALUE, OUTPUT> operation
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Looks up the specified class of metadatum, starting in
     * this Term's <code> metadata () </code>, then progressing
     * to the <code> type () </code>'s metadata, then upwards
     * through the metadata of the parent Namespaces.
     * </p>
     *
     * @param metadatum_class The class of metadatum to look up.
     *                        Must have a zero args "none"
     *                        constructor.  Must not be null.
     *
     * @return Either the One requested metadatum, or No metadatum
     *         if it does not exist in any of the searched Metadata.
     *         Never null.
     */
    public abstract <METADATUM extends Serializable>
        ZeroOrOne<METADATUM> findMetadatum (
                                            Class<METADATUM> metadatum_class
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Conditionally returns the metadata for this Term.
     * </p>
     *
     * <p>
     * The Type of this Term might cause a failed read of
     * this Term's metadata.  For example, in a typing environment
     * where metadata is strictly forbidden due to memory constraints,
     * a failed Metadata will be returned.
     * </p>
     *
     * @return The conditional metadata for this Term.
     *         Always either a single One&lt;Metadata&gt; or a
     *         failed No&lt;Metadata&gt;.  Never null.
     */
    public abstract ZeroOrOne<Metadata> metadata ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Evaluates the specified operations on this Term's
     * value, and either returns the successful Just output value,
     * or returns a No value.
     * </p>
     *
     * <p>
     * To block waiting for the Pipe to complete, call
     * <code> sync () </code> on the result.  To handle the result
     * asynchronously, call <code> async () </code> on the result.
     * </p>
     *
     * <p>
     * This is a convenience method which has the same effect as:
     * </p>
     *
     * <pre>
     *     call ( new Pipe<INPUT> ( operations ) );
     * </pre>
     *
     * @param operations The operations to execute in the pipe, in order.
     *             Must not be null.  Must not contain any
     *             null elements.
     *
     * @return The output Term, typically an Expression which will
     *         evaluate the specified operations pipe on this Term's value.
     *         Never null.
     */
    public abstract Term<?> pipe (
                                  Value<Operation1<?, ?>> operations
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String,musaico.foundation.typing.Type)
     */
    @Override
    public abstract Term<VALUE> retype (
                                        String name,
                                        AbstractTermType<? extends Term<VALUE>, VALUE> type
                                        )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The type of Value which this Term evaluates to,
     *         such as a string Type, or an integer Type, and so on.
     *         Never null.
     */
    public abstract Type<VALUE> valueType ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Conditionally returns the value of this Term.
     * </p>
     *
     * <p>
     * Some terms (such as Expressions) return Blocking results.
     * Call <code> sync () </code> on the return value to block on
     * the result, or <code> async () </code> to handle the result
     * asynchronously.
     * </p>
     *
     * <p>
     * The Type of this Term might cause a failed read of
     * this Term value.  For example, a Term which is
     * tagged as private might always fail to return the value.
     * </p>
     *
     * @return The conditional value of this Term.
     *         Never null.
     */
    public abstract Synchronicity<VALUE> value ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Casts the value of this Term to another type.
     * </p>
     *
     * <p>
     * To block waiting for the cast Operation to complete, call
     * <code> sync () </code> on the result.  To handle the result
     * asynchronously, call <code> async () </code> on the result.
     * </p>
     *
     * <p>
     * For example, an Integer Term might be cast to a String
     * Term with something like:
     * </p>
     *
     * <pre>
     *     Term<Integer> age = ...;
     *     String age_as_string = age.value ( String.class )
     *                               .sync ()
     *                               .orDefault ( "-1" );
     * </pre>
     *
     * <p>
     * Or a String's content might be hashed with something like:
     * </p>
     *
     * <pre>
     *     Term<String> text = ...;
     *     String hashed_text = text.value ( SHA1.class )
     *                              .sync ()
     *                              .orThrowDeclared ();
     * </pre>
     *
     * <p>
     * And so on.
     * </p>
     *
     * @param target_class The class to cast to, such as String.class
     *                     or Integer.class and so on.  Must not be null.
     *
     * @param tags Zero or more Tags modifying the behaviour of the
     *             specified Class.  Must not be null.  Must not
     *             contain any null elements.
     *
     * @return The conditionally cast value, such as a String or
     *         an Integer and so on.  Never null.
     */
    public abstract <CAST extends Object>
        Synchronicity<CAST> value (
                                   Class<CAST> target_class,
                                   Tag ... tags
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Casts the value of this Term to another type.
     * </p>
     *
     * <p>
     * To block waiting for the cast Operation to complete, call
     * <code> sync () </code> on the result.  To handle the result
     * asynchronously, call <code> async () </code> on the result.
     * </p>
     *
     * <p>
     * For example, an Integer Term might be cast to a String
     * Term with something like:
     * </p>
     *
     * <pre>
     *     Term<Integer> age = ...;
     *     Type<String> string_type = ...;
     *     String age_as_string = age.value ( string_type ).
     *                               .sync ()
     *                               .orDefault ( "-1" );
     * </pre>
     *
     * @param target_type The type to cast to, such as a Type<String>
     *                    or a Type<Integer> and so on.  Must not be null.
     *
     * @return The conditionally cast value, such as a String or
     *         an Integer and so on.  Never null.
     */
    public abstract <CAST extends Object>
        Synchronicity<CAST> value (
                                   Type<CAST> target_type
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Term must implement equals(), hashCode() and toString().
}
