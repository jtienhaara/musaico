package musaico.foundation.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;


/**
 * <p>
 * A builder of a sequence of Operations, each of which takes Objects
 * of a specific Class as input, and outputs Objects of the
 * same Class as output.
 * </p>
 *
 * <p>
 * The final output of the entire Pipeline can be of a class other than
 * the input, but it is only ever created when <code> output () </code>
 * is called on the pipeline's Sink.  Typically sub-Pipelines cannot
 * produce output (they are <code> end () </code>ed to return to a toplevel
 * pipeline).
 * </p>
 *
 * <p>
 * For example, a Pipeline might take an input of
 * <code> Term&lt;String&gt; </code> and produce an output of
 * <code> Term&lt;String&gt; </code>; or it might take an input of
 * <code> Term&lt;Number&gt; </code> and produce an output of
 * <code> Term&lt;Number&gt; </code>; and so on.
 * </p>
 *
 * <p>
 * Pipelines are not necessarily specific to Types, Terms
 * and Operations (@see musaico.foundation.term), but since that is
 * what they were designed for, the examples here all related to those
 * classes of objects.
 * </p>
 *
 * <p>
 * Pipelines are the basis for Edit, Fork, Join, OrderBy, Select,
 * When, Where, and so on operation tree builders.
 * Each one builds up a tree of operations that will manipulate,
 * order, choose ranges of, or filter the input.
 * </p>
 *
 *
 * <p>
 * In Java every Pipeline must be Serializable in order to
 * play nicely across RMI.  However users of the Pipeline
 * must be careful, since the values and expected data stored inside
 * (if any) might not be Serializable.
 * </p>
 *
 * <p>
 * In Java every Pipeline must implement equals (), hashCode ()
 * and toString ().
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
 * @see musaico.foundation.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.pipeline.MODULE#LICENSE
 */
public interface Pipeline<STREAM extends Object, PIPELINE extends Pipeline<STREAM, PIPELINE>>
    extends Construct<STREAM>, Parent<STREAM, PIPELINE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Ensures that each input is acceptable for
     * this Pipeline, or else the corresponding output will be a failure
     * (such as an Error Term, or No Term, and so on).
     * </p>
     *
     * @return This pipeline.  Never null.
     */
    public abstract PIPELINE check ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns True if this Pipeline contains the specified Pipeline;
     * false if not.
     * </p>
     *
     * <p>
     * If <code> this.equals ( that ) </code> then true is returned.
     * </p>
     *
     * <p>
     * If this Pipeline contains, as a subset, the operations comprising
     * that Pipeline, then true is returned.  For example, if this Pipeline
     * comprises the operations <code> { edit ().append ( x ).end ()
     * .orderBy ( order ).end ().check ().select ().first ( 5L ) </code>,
     * and that Pipeline comprises the operations <code> orderBy ( order )
     * .end ().check () </code>, then true will be returned.
     * </p>
     *
     * @param that The Pipeline which might or might not be contained
     *             in this Pipeline.  Must not be null.
     *
     * @return True if this Pipeline contains that Pipeline; false if not.
     */
    public abstract boolean contains (
            Pipeline<?, ?> that
            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Returns an Edit pipeline, which can be used to add elements to
     * and/or remove elements from each input.
     * </p>
     *
     * <p>
     * For example, to concatenate all elements of two Terms, and then
     * add a single element to the end, the following code might be used:
     * </p>
     *
     * <pre>
     *     final Term<String> middle = ... "brave", "new", ...;
     *     final Term<String> concatenation =
     *         middle.edit ()
     *                 .prepend ( "Hello," )
     *                 .append ( "world!" )
     *               .end ()
     *               .output ();
     * </pre>
     *
     * <p>
     * The output of the <code> output () </code> method would be
     * a Term&lt;String&gt; along the lines of { "Hello,", "brave",
     * "new", "world!".
     * </p>
     *
     * @return The Edit pipeline.  Possibly new, possibly this pipeline.
     *         Never null.
     */
    public abstract Edit<STREAM, PIPELINE, ? extends Object> edit ()
        throws ReturnNeverNull.Violation;


    // Every Pipeline must implement java.lang.Object#equals(java.lang.Object)

    // Every Pipeline must implement java.lang.Object#hashCode()


    /**
     * <p>
     * Returns an OrderBy pipeline, which can be used to sort
     * or re-arrange the elements in multi-valued objects such as
     * Terms.
     * </p>
     *
     * <p>
     * For example, to arrange the elements of a String Term by some
     * specific "dictionary" sort Order, then, whenever the "dictionary"
     * Order considers two terms to be the same, by the "ascii"
     * Order, and then reverse the sorted order, the following code
     * might be used:
     * </p>
     *
     * <pre>
     *     final Term<String> term = ...;
     *     final Order<String> dictionary = ...;
     *     final Order<String> ascii = ...;
     *     final Term<String>  =
     *         term.orderBy ( dictionary, ascii )
     *             .reverse ()
     *             .apply ();
     * </pre>
     *
     * <p>
     * The output of the <code> apply ( ... ) </code> method would be
     * the reverse-sorted elements of the input term.
     * </p>
     *
     * @param orders Zero or more hierarchy of Orders to sort by.
     *               If the first Order considers two elements to be the same,
     *               then the second Order is applied.  If the second Order
     *               also considers the two elements to be the same, then
     *               the third Order is applied.  And so on.  Can be empty.
     *               Must not be null.  Must not contain any null elements.
     *
     * @return A new OrderBy sub-pipeline.  Never null.
     */
    @SuppressWarnings("unchecked") // Generic varargs possible heap pollution.
    public abstract OrderBy<STREAM, PIPELINE> orderBy (
            Order<STREAM> ... orders
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a Select pipeline, which can be used to
     * retrieve subsets of multi-valued inputs such as Terms.
     * </p>
     *
     * <p>
     * For example, to select the second through fourth elements of
     * a String Term (inclusive) (if possible), the following code
     * might be used:
     * </p>
     *
     * <pre>
     *     final Term<String> term = ...;
     *     final Term<String> elements_1_to_3 =
     *         term.select ()
     *             .range ( 1L, 3L )
     *             .apply ();
     * </pre>
     *
     * <p>
     * The output of the <code> apply ( ... ) </code> method would be
     * the elements 1L, 2L and 3L (second through fourth) from the
     * specified Term, or fewer if the specified Term does not
     * have that many elements.
     * </p>
     *
     * @return A new Select sub-pipeline.  Never null.
     */
    public abstract Select<STREAM, PIPELINE> select ()
        throws ReturnNeverNull.Violation;


    // Every Parent must implement
    // musaico.foundation.pipeline.Parent#thisPipeline()


    /**
     * <p>
     * Creates a When (if) clause which will be applied to any
     * input that meets the specified Filter.
     * </p>
     *
     * <p>
     * For example, to output an Error Term whenever an input Term
     * is empty or has more than 3 elements:
     * </p>
     *
     * <pre>
     *     final Filter<Term<String>> is_empty = ...;
     *     final Filter<Term<String>> is_more_than_3_elements = ...;
     *     final Error<String> error = ...;
     *     final Operation<String, String> extract_initials = ...;
     *     final Term<String> name = ...;
     *     final Term<String> initials =
     *         name.when ( is_empty )
     *                 .edit ().replaceWith ( error ).end ()
     *             .otherwise ( is_more_than_3_elements )
     *                 .edit ().replaceWith ( error ).end ()
     *             .otherwise ()
     *                 .pipe ( extract_initials )
     *             .end ()
     *             .output ();
     * </pre>
     *
     * @param condition The Filter which must return kept for each input
     *                  in order for the operations in the When
     *                  to be applied to it.  Must not be null.
     *
     * @return A new When sub-pipeline that will add operations
     *         to apply to each input that matches the specified Filter.
     *         Never null.
     */
    public abstract When<STREAM, PIPELINE, ? extends Object, ?> when (
            Filter<?> condition
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a Where pipeline, which can be used
     * to filter individual elements from multi-valued inputs such as
     * Terms.
     * </p>
     *
     * <p>
     * For example, to filter out all elements of a Number term which
     * are not in the range 0..10, and return only one element
     * per unique Number, removing duplicates, the following code
     * might be used:
     * </p>
     *
     * <pre>
     *     final Term<Number> term = ...;
     *     final Term<Number> greater_than_or_equal_to_0 =
     *         term.where ( GreaterThanOrEqualToZero.DOMAIN )
     *             .where ( new LessThanOrEqualToNumber ( 10 ) )
     *             .unique ()
     *             .apply ();
     * </pre>
     *
     * <p>
     * The output of the <code> apply ( ... ) </code> method would be
     * some subset of the Numbers ( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ),
     * depending on the input, with no duplicates.
     * </p>
     *
     * @param filters Zero or more Filters to be applied.  Can be empty.
     *                Must not be null.  Must not contain any null elements.
     *
     * @return A new Where sub-pipeline.  (Not to be confused with the more
     *         lycanthropic variety, the were-sub-pipeline.)  Never null.
     */
    @SuppressWarnings("unchecked") // Generic varargs possible heap pollution.
    public abstract Where<STREAM, PIPELINE> where (
            Filter<STREAM> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;
}
