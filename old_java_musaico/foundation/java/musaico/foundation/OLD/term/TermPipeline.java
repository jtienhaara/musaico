package musaico.foundation.term;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.pipeline.Edit;
import musaico.foundation.pipeline.Parent;
import musaico.foundation.pipeline.Pipeline;
import musaico.foundation.pipeline.Sink;


/**
 * <p>
 * A Pipeline expression, which already has an input term
 * (as opposed to an OperationPipeline, which must be applied
 * to an input term).
 * </p>
 *
 *
 * <p>
 * In Java every Parent must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Parent must be Serializable in order to
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
public interface TermPipeline<VALUE extends Object, PIPELINE extends TermPipeline<VALUE, PIPELINE>>
    extends Pipeline<VALUE, PIPELINE>, TermParent<VALUE, PIPELINE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** A TermPipeline which can build the final output Term
     *  by applying the sequence of operations to the input. */
    public static interface TermSink<ELEMENT extends Object>
        extends Sink<ELEMENT, TermPipeline.TermSink<ELEMENT>, Term<ELEMENT>, TermPipeline.TermSink<ELEMENT>, OperationPipeline.OperationSink<ELEMENT>>, TermPipeline<ELEMENT, TermPipeline.TermSink<ELEMENT>>, Serializable
    {
        /**
         * <p>
         * Returns a Transform of this pipeline, which can be used to
         * convert each input Term to another Type, or to an array or List,
         * and so on.
         * </p>
         *
         * <p>
         * Synonym for <code> this.type ().cast ( this.output () ) <code>.
         * </p>
         *
         * @return A new Transform.  Never null.
         */
        public abstract Transform<ELEMENT, ELEMENT> cast ()
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;


        /**
         * @see musaico.foundation.pipeline.Sink#duplicate()
         */
        @Override
        public abstract TermPipeline.TermSink<ELEMENT> duplicate ()
            throws ReturnNeverNull.Violation;


        // Every Pipeline must implement
        // musaico.foundation.pipeline.Sink#fork()


        /**
         * <p>
         * Returns a Transform from this Term to its indices
         * (0L, 1L, 2L, and so on).
         * </p>
         *
         * <p>
         * If this Term is Empty, then the Transform will produce an empty
         * Term of indices.
         * </p>
         *
         * <p>
         * If this Term is Countable, then the Transform will produce Countable
         * indices of the same length at the time the Transform's output is
         * requested.
         * </p>
         *
         * <p>
         * If the Term is Infinite, then the Transform will produce Infinite
         * indices.
         * </p>
         *
         * <p>
         * And so on.
         * </p>
         *
         * @return A mapping of this Term to its indices (0L, 1L, 2L,
         *         and so on).  Never null.
         */
        public abstract Transform<ELEMENT, Long> indices ()
            throws ReturnNeverNull.Violation;


        // Every Pipeline must implement
        // musaico.foundation.pipeline.Sink#join()


        /**
         * @see musaico.foundation.term.TermParent#operations()
         */
        @Override
        public abstract OperationPipeline.OperationSink<ELEMENT> operations ()
            throws ReturnNeverNull.Violation;


        // Every Sink must implement
        // musaico.foundation.pipeline.Sink#output()
        // which outputs a Term of Type <code> this.type () </code>.
        // The type () method may return a different Type than what
        // was input to this TermPipeline, since selections and edits
        // and filtering and ordering could lead to Terms which do not
        // satisfy the constraints of the input Type.  Therefore, in
        // order to guarantee that the output Term is a valid Term
        // of its own Type, the type () method might be forced
        // to create a new Type with fewer constraints, so that
        // the output Term meets all its constraints.


        /**
         * <p>
         * Creates a new Transform from this pipeline
         * to the specified symbol(s).
         * </p>
         *
         * @see musaico.foundation.term.Type#symbols()
         *
         * <p>
         * If no symbols are specified, then all symbols of the input Term 's
         * <code> type () </code> are extracted.
         * </p>
         *
         * <p>
         * Note that if a symbol from Type <code> t1 </code> is requested
         * from a TermPipeline whose Type is <code> t2 </code>,
         * then Type <code> t2 </code> might support all, none, or some
         * of the specified symbols.
         * </p>
         *
         * @param symbols The symbols to extract, or all symbols
         *                of the input Term, if none are specified.
         *                Can be empty.  Must not be null.
         *
         * @return The newly created symbols Transform.  Never null.
         */
        @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
        public abstract <SYMBOL extends Object>
            Transform<ELEMENT, SYMBOL> symbols (
                Operation<ELEMENT, SYMBOL> ... symbols
                )
            throws ParametersMustNotBeNull.Violation,
                   Parameter1.MustContainNoNulls.Violation,
                   ReturnNeverNull.Violation;


        /**
         * <p>
         * Creates a new Transform which will apply the specified Operation
         * to each Term output by this Pipeline.
         * </p>
         *
         * <p>
         * For example, an Operation which takes as input
         * a Term&lt;String&gt; and outputs the number of elements
         * containing the word "yes", Term&lt;Long&gt;, might be
         * used as follows:
         * </p>
         *
         * <pre>
         *     final Operation<String, Long> count_yesses = ...;
         *     final Term<String> strings = ...;
         *     final Term<Long> num_yesses =
         *         strings.transform ( count_yesses )
         *                .output ();
         * </pre>
         *
         * <p>
         * If the Type of this TermPipeline and the input Type
         * of the specified Operation are totally incompatible,
         * then the resulting Transform will always output a failed Term,
         * such as an Error.
         * </p>
         *
         * @param operation An Operation that will transform each Term
         *                  that is output by this  pipeline.
         *                  Must not be null.
         *
         * @return A new Transform.  Never null.
         */
        public abstract <OUTPUT extends Object>
            Transform<ELEMENT, OUTPUT> transform (
                Operation<ELEMENT, OUTPUT> operation
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;
    }


    /** A middle TermPipeline which cannot build output. */
    public static interface TermTrunk<ELEMENT extends Object>
        extends TermPipeline<ELEMENT, TermPipeline.TermTrunk<ELEMENT>>, Serializable
    {
        /**
         * @see musaico.foundation.term.TermParent#operations()
         */
        @Override
        public abstract OperationPipeline.OperationTrunk<ELEMENT> operations ()
            throws ReturnNeverNull.Violation;
    }




    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#check()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#contains(musaico.foundation.pipeline.Pipeline))


    /**
     * @see musaico.foundation.pipeline.Pipeline#edit()
     */
    @Override
    public abstract Edit<VALUE, PIPELINE, TermPipeline.TermSink<VALUE>> edit ()
        throws ReturnNeverNull.Violation;


    // Every Parent must implement java.lang.Object#equals(java.lang.Object)

    // Every Parent must implement java.lang.Object#hashCode()


    /**
     * @return The Term or TermPipeline which was input to this TermPipeline
     *         (or into its parent or an ancestor, if this is a Subsidiary).
     *         Might be of a different Type and class than the current
     *         TermPipeline, if, for example, a Transform appears somwhere
     *         in the pipeline.  If this TermPipeline is itself a Term,
     *         then it returns itself.  Never null.
     */
    public abstract TermPipeline.TermSink<?> input ()
        throws ReturnNeverNull.Violation;


    /**
     * @see musaico.foundation.term.TermParent#operations()
     */
    @Override
    public abstract OperationPipeline<VALUE, ?> operations ()
        throws ReturnNeverNull.Violation;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#orderBy(musaico.foundation.order.Order[])


    /**
     * <p>
     * Creates a new TermPipeline which will apply the specified Operation
     * to each Term output by this Pipeline.
     * </p>
     *
     * <p>
     * For example, an object <code> db </code> with method
     * <code> readInt () </code> which returns a
     * <code> Term&lt;Integer&gt; </code> might be used with
     * the following code:
     * </p>
     *
     * <pre>
     *     Operation<Integer, Integer> verify_minimum_age_or_prompt_for_age = ...;
     *     db.readInt ( "age" )
     *       .pipe ( verify_minimum_age_or_prompt_for_age );
     * </pre>
     *
     * <p>
     * Or an object <code> db </code> with method
     * <code> readString () </code> which returns a
     * <code> Term&lt;String&gt; </code> might be used with
     * the following code to guarantee success only occurs if the
     * single return value is actually in some specific String
     * format:
     * </p>
     *
     * <pre>
     *     Operation<String, String> check_format_last_name_comma_first_name = ...;
     *     String name =
     *         db.readName ()
     *           .pipe ( check_format_last_name_comma_first_name )
     *           .output ()
     *           .orNull ();
     *     if ( name == null ) ... failure code ...
     *     else ... success code, name guaranteed to be in expected format ...
     * </pre>
     *
     * <p>
     * Or an object <code> db </code> with method
     * <code> readName () </code> which returns a
     * <code> Term&lt;String&gt; </code> might be used with
     * the following code to re-try on failure:
     * </p>
     *
     * <pre>
     *     Operation<String, String> reconnect_to_db_and_try_again = ...;
     *     String name =
     *         db.readName ()
     *           .pipe ( reconnect_to_db_and_try_again )
     *           .orNull ();
     *     if ( name == null ) ... failure code ...
     *     else ... success code ...
     * </pre>
     *
     * <p>
     * And so on.
     * </p>
     *
     * <p>
     * If the Type of this TermPipeline and the input Type
     * of the specified Operation are totally incompatible
     * in spite of the fact that they share the same element class
     * (for example because this pipeline always outputs Terms of
     * 10 or fewer elements, while the specified Operation requires
     * Terms of more than 10 elements), then the resulting pipeline
     * will always output a failed Term, such as an Error.
     * </p>
     *
     * @param operation Operation to add to the pipeline.
     *                  Must not be null.
     *
     * @return This Pipeline.  Never null.
     */
    public abstract PIPELINE pipe (
            Operation<VALUE, VALUE> operation
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds a new child OperationPipeline to this Pipeline, if the specified
     * OperationPipeline's Type(s) is (are) compatible,
     * or creates a new Pipeline, with the output of this pipeline
     * being processed by the specified OperationPipeline to produce
     * the input to the new Pipeline.
     * </p>
     *
     * <p>
     * If the Type of this TermPipeline and the input Type
     * of the specified OperationPipeline are totally incompatible,
     * then the resulting pipeline will always output a failed Term,
     * such as an Error.
     * </p>
     *
     * @param pipeline The OperationPipeline to add to the pipeline.
     *                 Must not be null.
     *
     * @return This Pipeline.  Never null.
     */
    public abstract PIPELINE pipe (
            OperationPipeline.OperationSink<VALUE> pipeline
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#select()

    // Every Parent must implement
    // musaico.foundation.pipeline.Parent#thisPipeline()

    // Every Parent must implement java.lang.Object#toString()


    /**
     * <p>
     * Returns the Type of Terms which will emerge from the other end
     * of this pipeline, or the Type of this Term, if this is a Term.
     * </p>
     *
     * <p>
     * For example, a <code> Term&lt;Number&gt; </code> might belong to
     * <code> { NumberType } </code>.
     * </p>
     *
     * <p>
     * Or a <code> Term&lt;String&gt; </code>
     * might belong to <code> { StringType }</code>.
     * </p>
     *
     * <p>
     * Or a <code> Term&lt;String&gt; </code>
     * might belong to multiple more restrictive types
     * <code> { JustTermType, NonEmptyStringType, IdentifierStringType </code>.
     * </p>
     *
     * <p>
     * And so on.
     * </p>
     *
     * <p>
     * A pipeline must return a Type which willl treat as valid
     * the output from this stage of pipeline processing.  This Type
     * could be a different Type than what was input to this pipeline,
     * since selections and edits and filtering and ordering
     * could lead to Terms which do not satisfy the constraints
     * of the input Type.  Therefore, in order to guarantee
     * that the output Term is a valid Term of its own Type,
     * this method might be forced to create a new Type with
     * fewer constraints, so that the output Term meets all its constraints.
     * </p>
     *
     * @return The Type of this Term, or the Type of Terms which can be
     *         output from this stage of pipeline processing.  Never null.
     */
    public abstract Type<VALUE> type ()
        throws ReturnNeverNull.Violation;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#when(musaico.foundation.filter.Filter)

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#where(musaico.foundation.filter.Filter[])
}
