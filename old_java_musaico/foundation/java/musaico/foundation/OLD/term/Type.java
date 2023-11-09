package musaico.foundation.term;

import java.io.Serializable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.pipeline.Pipeline;


/**
 * <p>
 * Classifies Terms by whole term and by value and by element,
 * based on: 1) Contract(s) governing each whole Term of this Type; and
 * 2) Contract(s) governing each element of each Term of this Type;
 * as well as a "none" element (like 0 or ""
 * or an empty array, and so on); and a few other useful tools.
 * </p>
 *
 * <p>
 * The "none" element is used as a sort of default whenever
 * a caller does not want to handle exceptions.  Rather
 * than calling <code> term.orThrowUnchecked () <code>
 * or <code> term.orThrowChecked () </code>, a caller can choose
 * to ask for the none element by calling <code> orNone () <code>.
 * Calling <code> orNone () <code> on LessThanOne elements
 * or on a value with more than 1 elements will result
 * in the "none" element being returned.
 * </p>
 *
 * <p>
 * Implementers of Types should choose "none" elements which
 * are sensible defaults.  For example, an application which populates
 * a data entry screen with Strings and integers, defaulting to "none"
 * values when the database contains NULLs, probably should probably
 * get back empty Strings ("") and 0 integers, so that the user of the
 * data entry screen can make sense of the values, and the application
 * logic can also -- when desirable -- treat the "none" values as
 * special values (such as "age 0 means prompt the customer for
 * age identification when they purchase tobacco products" and so on).
 * </p>
 *
 *
 * <p>
 * In Java every Pipeline must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Operation must be Serializable in order to
 * play nicely across RMI.
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public interface Type<VALUE extends Object>
    extends Filter<Object>, OperationPipeline.OperationSink<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every OperationPipeline must implement
    // musaico.foundation.term.OperationPipeline#applyLazy(musaico.foundation.term.TermPipeline#TermSink)


    /**
     * <p>
     * Allocates a new array of VALUE elements of the specified size.
     * </p>
     *
     * <p>
     * Convenience method to get around the pain of creating generic
     * arrays in Java.
     * </p>
     *
     * @param num_elements The number of elements in the array to allocate.
     *                     Must be greater than or equal to 0.
     */
    public abstract VALUE [] array (
            int num_elements
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a Transform of the specified Term, which can be used to
     * convert that Term to another Type, or to an array or List,
     * and so on.
     * </p>
     *
     * <p>
     * Calling <code> type.cast ( term ).to ( another_type ).buildType () </code>
     * is equivalent to <code> type.to ( another_type ).apply ( term ) </code>.
     * </p>
     *
     * @param input The Term or TermPipeline to cast.  Must not be null.
     *
     * @return A new Transform.  Never null.
     */
    public abstract Transform<VALUE, VALUE> cast (
            TermPipeline.TermSink<VALUE> input
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#check()


    /**
     * <p>
     * Returns the contract(s) governing each whole Term,
     * which determine whether an entire Term (not just a single element)
     * is or isn't a member of this Type.
     * </p>
     *
     * <p>
     * The Term contract includes the element contract(s) as a subset.
     * If any of this Type's element contract(s) DISCARDs an element,
     * then the whole term contract must
     * discard any term that contains that single element, as well.
     * However even if all elements are KEPT by all element contract(s), the
     * overall constraints can still reject the whole term (for example because
     * it is the wrong multiplicity).
     * </p>
     *
     * <p>
     * Note that it is not always practical to verify that a Term's
     * elements match the element contract(s).  For example, Infinite Terms
     * can generally only be checked against the Contract
     * for the overall term.
     * </p>
     *
     * @return The contract(s) governing whole Terms of this Type.
     *         Can be empty.  Never null.
     */
    public abstract Countable<Contract<Term<?>, ? extends TermViolation>> constraints ()
        throws ReturnNeverNull.Violation;


    // Every Pipeline must implement
    // musaico.foundation.term.OperationPipeline#duplicate()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#edit()


    /**
     * @return The Class of every element of every valid Term of this Type.
     *         Never null.
     */
    public abstract Class<VALUE> elementClass ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the Contract(s) governing each and every element of the value
     * of every Term of this Type.
     * </p>
     *
     * <p>
     * In order to be considered a valid Term of this Type,
     * a Term must contain only elements which are KEPT
     * by each of the element contract(s).
     * </p>
     *
     * <p>
     * (There might be additional requirements imposed by
     * this Type's <code> constraints () </code>, such as constraints on
     * the Term's multiplicity, and so on.)
     * </p>
     *
     * <p>
     * Note that it is not always practical to verify that a Term's
     * elements all meet the Type's element constraints.  For example,
     * Infinite Terms can generally only be checked
     * against the Contract for the overall term, not against
     * the constraints for the individual elements.
     * </p>
     *
     * @return The Contract(s) governing elements of valid Terms of this Type.
     *         Can be empty.  Never null.
     */
    public abstract Countable<Contract<VALUE, ?>> elementConstraints ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new TypePipeline to build a Type of containers of elements
     * of this Type.
     * </p>
     *
     * <p>
     * A container is a type of objects with one type parameter,
     * such as a generic class (<code> List&lt;VALUE&gt; </code>,
     * <code> Term&lt;VALUE&gt; </code>, and so on) or an array
     * (<code> VALUE [] </code>, or <code> VALUE [] [] </code>, and so on).
     * </p>
     *
     * <p>
     * The following examples show various container Types being created.
     * </p>
     *
     * <pre>
     *     final Type<String> string_type = ...;
     *     final Type<List<String>> list_of_strings_type =
     *         string_type.enclose ( List.class,             // container_class
     *                               new ArrayList<String> () )       // none
     *                    .buildType ();
     *     final Type<Term<String>> term_of_strings_type =
     *         string_type.enclose ( Term.class,             // container_class
     *                               new No<String> ( string_type ) ) // none
     *                    .buildType ();
     *     final Type<String []> array_of_strings_type =
     *         string_tyoe.enclose ( String [].class,        // container_class
     *                               new String [ 0 ] )               // none
     *                    .buildType ();
     * </pre>
     *
     * @param container_class The class of containers which will be the
     *                        element class of the new TypePipeline.
     *                        Must be either the class of, or an ancestor
     *                        of the class of, the specified none value.
     *                        Must not be null.
     *
     * @param none The default value returned by <code> Term.orNone () </code>.
     *             Typically an empty container, though the meaning
     *             of "none" varies widely.  Must be an instance of the
     *             specified container_class.  Must not be null.
     *
     * @return A newly created TypePipeline which can build the Type of
     *         containers of this Type.  Never null.
     */
    public abstract <CONTAINER extends Object>
        TypePipeline.TypeSink<CONTAINER> enclose (
            Class<?> container_class,
            CONTAINER none
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeInstanceOfClass.Violation,
               ReturnNeverNull.Violation;


    // Every Pipeline must implement java.lang.Object#equals(java.lang.Object)

    // Every Filter must implement
    // musaico.foundation.filter.Filter#filter(java.lang.Object)

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Sink#fork(OperationPipeline.OperationSink[])


    /**
     * <p>
     * Constructs a new Term, or an Error
     * if the specified array cannot be turned into a valid Term
     * of this pipeline's Type; then applies the operation pipeline
     * to the newly constructed Term, and returns the output.
     * </p>
     *
     * @param source The array to convert into a Term and
     *               run through this pipeline.  Must not be null.
     *
     * @return A new TermPipeline with a Term as its input, passing
     *         through this OperationPipeline.OperationSink.
     *         The Term input could be an Error or some similar
     *         failure, if no valid Term of this pipeline's input Type
     *         can be created from the specified source array.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public abstract TermPipeline.TermSink<VALUE> from (
            VALUE ... source
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Constructs a new Term, or an Error
     * if the specified Collection cannot be turned into a valid Term
     * of this pipeline's Type; then applies the operation pipeline
     * to the newly constructed Term, and returns the output.
     * </p>
     *
     * @param source The Collection to convert into a Term and
     *               run through this pipeline.  Must not be null.
     *
     * @return The output from passing into this pipeline
     *         a newly constructed Term, if possible,
     *         or passing an Error through this pipeline,
     *         if no valid Term of this pipeline's Type can be created
     *         from the specified source Collection.  Never null.
     */
    public abstract Term<VALUE> from (
            Collection<VALUE> source
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new transform Operation, or returns an Error
     * if the specified Map cannot be turned into a valid
     * transform Operation from this Type.
     * </p>
     *
     * @param source The Map to convert into a transform Operation
     *               from this Type.  Must not be null.
     *               Must not contain any null keys or values.
     *
     * @return A new transform Operation from this Type, if possible,
     *         or an Error, if no valid Term of this Type can be created
     *         from the specified source Map.  Note that the Operation's
     *         input Type might be a newly created simple Type, one which
     *         accepts only the values in the map as term elements.
     *         Never null.
     */
    public abstract <TARGET extends Object>
        Operation<VALUE, TARGET> from (
            Map<VALUE, TARGET> source
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Constructs a new Term, or an Error
     * if the specified Set cannot be turned into a valid Term
     * of this pipeline's Type; then applies the operation pipeline
     * to the newly constructed Term, and returns the output.
     * </p>
     *
     * @param source The Set to convert into a Term and
     *               run through this pipeline.  Must not be null.
     *
     * @return The output from passing into this pipeline
     *         a newly constructed Term, if possible,
     *         or passing an Error through this pipeline,
     *         if no valid Term of this pipeline's Type can be created
     *         from the specified source Set.  Never null.
     */
    public abstract Term<VALUE> from (
            Set<VALUE> source
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Source must implement
    // musaico.foundation.pipeline.Source#from(java.lang.Object)
    // with a TermPipeline.TermSink<ELEMENT> input
    // (such as when type.from ( Term<ELEMENT> ) is called,
    // or type.from ( Transform<X, ELEMENT> ), and so on).


    /**
     * <p>
     * Returns a transform Operation which will either transform
     * inputs of this Type using the specified Operation, or
     * return an Error for each input, if the specified
     * transform Operation cannot be turned into a valid one
     * from this Type.
     * </p>
     *
     * @param source The transform Operation to convert.
     *               Must not be null.  Must not contain any null
     *               keys or values.
     *
     * @return A transform Operation which accepts inputs of this Type,
     *         either the specified operation as-is, or a new one.
     *         If the specified Operation cannot accept Terms of this Type,
     *         then the transform Operation returned will always generate
     *         an Error output, no matter the input.  Never null.
     */
    public abstract <TARGET extends Object>
        Operation<VALUE, TARGET> from (
            Operation<?, TARGET> source
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Pipeline must implement java.lang.Object#hashCode()


    /**
     * <p>
     * Returns an Operation which takes an input Term and outputs
     * its indices (0L, 1L, 2L, and so on).
     * </p>
     *
     * <p>
     * If an input Term is Empty, then the Operation will produce an empty
     * Term of indices.
     * </p>
     *
     * <p>
     * If an input Term is Countable, then the Operation will produce Countable
     * indices of the same length at the time the Operation's output is
     * requested.
     * </p>
     *
     * <p>
     * If an input Term is Infinite, then the Operation will produce Infinite
     * indices.
     * </p>
     *
     * <p>
     * And so on.
     * </p>
     *
     * @return An Operation which maps each input Term to its indices
     *         (0L, 1L, 2L, and so on).  Never null.
     */
    public abstract Operation<VALUE, Long> indices ()
        throws ReturnNeverNull.Violation;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Sink#join(TermPipeline.TermSink[])


    /**
     * @return The "none" value from this Type, such as 0 or ""
     *         or an empty array, and so on.  Never null.
     */
    public abstract VALUE none ()
        throws ReturnNeverNull.Violation;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#orderBy(musaico.foundation.order.Order[])

    // Every Sink must implement
    // musaico.foundation.pipeline.Sink#output()

    // Every OperationPipeline must implement
    // musaico.foundation.term.OperationPipeline#pipe(musaico.foundation.term.Operation)

    // Every OperationPipeline must implement
    // musaico.foundation.term.OperationPipeline#pipe(musaico.foundation.term.OperationPipeline)


    /**
     * <p>
     * Creates a new TypePipeline with a Term of Type(s) as the input
     * to the pipeline.
     * </p>
     *
     * <p>
     * This can be used, for example, to create new multi-types:
     * </p>
     *
     * <pre>
     *     Type<String> text = ...;
     *
     *     Type<String> string_of_minimum_length = ...;
     *     Type<String> string_that_matches_pattern = ...;
     *     Type<Object> term_with_1_to_3_elements = ...;
     *     Type<String> string_with_1_to_3_elements =
     *         term_with_1_to_3_elements.refine ()
     *                                  .elementClass ( String.class )
     *                                  .buildType ();
     *
     *     Type<String> proper_names_type =
     *         text.refine ()
     *             .edit ()
     *                 .append ( string_of_minimum_length,
     *                           string_that_matches_pattern,
     *                           string_with_1_to_3_elements )
     *             .end ()
     *             .buildType ();
     * </pre>
     *
     * <p>
     * If this is an atomic Type, then the input to the TypePipeline
     * is a Term with one element: this Type.
     * </p>
     *
     * <p>
     * However if this is a logical or other composite Type, such as
     * <code> number &amp; less_than_100 &amp; 1_to_10_elements </code>,
     * then the Term will contain all of the component Type(s).
     * </p>
     *
     * <p>
     * The refine method returns a TypePipeline which operates
     * on this Type.  By contrast, this Type is itself an OperationPipeline
     * which accepts Terms of this Type as inputs.  To create a new Type,
     * call <code> Type.refine ().xyz ().buildType () </code>.  To create
     * a new Operation, call <code> Type.xyz ().output () </code>.
     * </p>
     *
     * @return A new TypePipeline, with this Type as the input.  Never null.
     */
    public abstract TypePipeline.TypeSink<VALUE> refine ()
        throws ReturnNeverNull.Violation;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#select()


    /**
     * <p>
     * Returns zero or more static symbol(s) defined on this Type,
     * such as constants, type parameters, and so on.
     * </p>
     *
     * <p>
     * These symbols apply are held by the Type, not its Terms.
     * </p>
     *
     * @see musaico.foundation.term.Type#symbols()
     *
     * @return This Type's static symbol(s) (if any).
     *         Can be empty.  Never null.
     */
    public abstract Transform<Type<VALUE>, ?> staticSymbols ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns zero or more symbol(s) defined on Terms of this Type.
     * </p>
     *
     * <p>
     * A symbol can represent anything the Type implementer desires,
     * such as:
     * </p>
     *
     * <ul>
     *     <li> Zero or more field (s) or component(s) of each element
     *          of a Term, such as a <code> first_name </code> field
     *          of each <code> Person </code> element, or a set of
     *          one or more <code> isbn </code> code field(s) of each
     *          <code> Book </code> element, or zero or more
     *          <code> Book </code> component(s) of each
     *          <code> Library </code> element, and so on. </li>
     *
     *     <li> Zero or more attribute(s) or metadata of each whole Term,
     *          such as its <code> length </code> attribute,
     *          or its <code> created_timestamp </code> metadata,
     *          or a set of access <code> permissions </code>, and so on. </li>
     *
     *     <li> A different structual view of each whole Term,
     *          such as the <code> history </code> of version(s)
     *          of a mutable Term, or a 2-dimensional view of the
     *          element(s) in the Term, divided into one or more
     *          <code> columns </code> or <code> rows </code>
     *          or <code> vectors </code>, and so on. </li>
     *
     *     <li> A constant defined by the Type, such as the <code> none </code>
     *          value, or the <code> minimum </code>
     *          and/or <code> maximum </code> length of each Term,
     *          or a set of <code> default_permissions </code> for each Term,
     *          or a <code> number_of_dimensions </code> for each Term,
     *          and so on. </li>
     * </ul>
     *
     * <p>
     * These symbols are held by Terms of this Type, not by the Type
     * itself.
     * </p>
     *
     * @see musaico.foundation.term.Type#staticSymbols()
     *
     * @return The symbol(s) which can be extracted from each Term
     *         of this Type, if any.  Each symbol is,
     *         in fact, an Operation to extract the symbol value(s) from
     *         each input Term.  Can be empty.  Never null.
     */
    public abstract Countable<Operation<VALUE, ?>> symbols ()
        throws ReturnNeverNull.Violation;


    // Every OperationPipeline must implement
    // musaico.foundation.term.OperationPipeline#symbols(musaico.foundation.term.Operation[])

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Parent#thisPipeline()


    /**
     * <p>
     * Returns a new transform Operation to another Type.
     * </p>
     *
     * @param to_type The new target Type.  Can be this Type, in which
     *                case a pass-through Operation is always returned.
     *                Must not be null.
     *
     * @return The transform Operation.  If none exists nor can one
     *         be created then the returned Operation will always result
     *         in an Error output, no matter the input.  Never null.
     */
    public abstract <NEW_TARGET extends Object>
        Operation<VALUE, NEW_TARGET> to (
            Type<NEW_TARGET> to_type
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Pipeline must implement java.lang.Object#toString()


    /**
     * <p>
     * Returns the Type constructor of this Type, which could be:
     * </p>
     *
     * <ul>
     *     <li> <code> One&lt;Type&gt; </code> (this one), in which case
     *          this Type is a simple Type, constructed by calling
     *          <code> new XYZType ( ... ) </code>. </li>
     *
     *     <li> An <code> Expression&lt;Type&gt; </code>.  For example,
     *          a Type constructed from a Kind (Type constructor
     *          with one or more Type parameters) would return
     *          an Expression containing one or more Transforms.
     *          A <code> Type&lt;Map&lt;String, Integer&gt;&gt; </code>,
     *          for example, might have been constructed from a
     *          <code> Kind2&lt;Class&lt;String&gt;, Class&lt;Integer&gt;, Type&gt; </code>.
     *          Then its typeConstructor would take the form of an
     *          Expression over the pipeline
     *          <code> Transform&lt;Class&lt;String&gt;, Transform&lt;Class&lt;Integer&gt;, Type&gt;&gt; </code>.
     *          There might be multiple Kinds, transforming Types
     *          along the way.  In such a case, the Kind operation(s)
     *          can be found by stepping back through the
     *          <code> inputOrigin () </code>s of the Transforms,
     *          and retrieving the <code> operation () </code>
     *          of each one. </li>
     *
     *     <li> Potentially other Terms of Types the author cannot
     *          currently see any use for. </li>
     * </ul>
     *
     * @return The Type constructor of this Type, such as
     *         <code> One&lt;Type&gt; </code> or an
     *         <code> Expression&lt;Type&gt; </code> and so on.
     *         Never null.
     */
    public abstract Term<Type<VALUE>> typeConstructor ()
        throws ReturnNeverNull.Violation;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#when(musaico.foundation.filter.Filter)

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#where(musaico.foundation.filter.Filter[])
}
