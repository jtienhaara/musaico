package musaico.foundation.term;

import java.io.Serializable;

import java.util.Collection;
import java.util.Set;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.pipeline.Edit;
import musaico.foundation.pipeline.Fork;
import musaico.foundation.pipeline.Join;
import musaico.foundation.pipeline.Pipeline;
import musaico.foundation.pipeline.Source;
import musaico.foundation.pipeline.Sink;
import musaico.foundation.pipeline.When;


/**
 * <p>
 * A Pipeline which behaves as an Operation, taking its input Term at
 * the end, once the operations have been built up.
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
public interface OperationPipeline<VALUE extends Object, PIPELINE extends OperationPipeline<VALUE, PIPELINE>>
    extends Pipeline<VALUE, PIPELINE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** A Fork OperationPipeline. */
    public static interface OperationFork<ELEMENT extends Object>
        extends Fork<ELEMENT, OperationPipeline.OperationSink<ELEMENT>, Operation<ELEMENT, ELEMENT>, TermPipeline.TermSink<ELEMENT>, OperationPipeline.OperationSink<ELEMENT>, OperationPipeline.OperationFork<ELEMENT>>, OperationPipeline<ELEMENT, OperationPipeline.OperationFork<ELEMENT>>
    {
    }


    /** A Join OperationPipeline. */
    public static interface OperationJoin<ELEMENT extends Object>
        extends Join<ELEMENT, OperationPipeline.OperationSink<ELEMENT>, Operation<ELEMENT, ELEMENT>, TermPipeline.TermSink<ELEMENT>, OperationPipeline.OperationSink<ELEMENT>, OperationPipeline.OperationJoin<ELEMENT>>, OperationPipeline<ELEMENT, OperationPipeline.OperationJoin<ELEMENT>>
    {
    }


    /** An OperationPipeline which can build the sequence of Operations
     *  into one single Operation. */
    public static interface OperationSink<ELEMENT extends Object>
        extends Source<TermPipeline.TermSink<ELEMENT>, ELEMENT, TermPipeline.TermSink<ELEMENT>>, Sink<ELEMENT, OperationPipeline.OperationSink<ELEMENT>, Operation<ELEMENT, ELEMENT>, TermPipeline.TermSink<ELEMENT>, OperationPipeline.OperationSink<ELEMENT>>, OperationPipeline<ELEMENT, OperationPipeline.OperationSink<ELEMENT>>, Serializable
    {
        /**
         * @see musaico.foundation.pipeline.Source#duplicate()
         * @see musaico.foundation.pipeline.Sink#duplicate()
         */
        @Override
        public OperationPipeline.OperationSink<ELEMENT> duplicate ()
            throws ReturnNeverNull.Violation;


        /**
         * @see musaico.foundation.pipeline.Sink#fork()
         *
         * Ensures that the Fork returned by an OperationPipeline
         * is itself an OperationPipeline.
         */
        @Override
        @SuppressWarnings("unchecked") // Generic varargs
            // possible heap pollution.
        public abstract OperationPipeline.OperationFork<ELEMENT> fork (
                OperationPipeline.OperationSink<ELEMENT> ... outputs
                )
            throws ParametersMustNotBeNull.Violation,
                   Parameter1.MustContainNoNulls.Violation,
                   ReturnNeverNull.Violation;


        // Every Source must implement
        // musaico.foundation.pipeline.Source#from(java.lang.Object)
        // with a TermPipeline.TermSink<ELEMENT> input
        // (such as a Term<ELEMENT>).


        /**
         * @see musaico.foundation.pipeline.Sink#join()
         *
         * Ensures that the Join returned by an OperationPipeline
         * is itself an OperationPipeline.
         */
        @Override
        @SuppressWarnings("unchecked") // Generic varargs
            // possible heap pollution.
        public abstract OperationPipeline.OperationJoin<ELEMENT> join (
                TermPipeline.TermSink<ELEMENT> ... inputs
                )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


        // Every Sink must implement
        // musaico.foundation.pipeline.Sink#output()


        /**
         * <p>
         * Creates a new OperationPipeline which will apply
         * the specified Operation to each input it receives.
         * </p>
         *
         * <p>
         * If the Type of this OperationPipeline and the input Type
         * of the specified Operation are totally incompatible,
         * then the resulting OperationPipeline will always output
         * a failed Term, such as an Error.
         * </p>
         *
         * @param operation An Operation that will transform each input Term.
         *                  Must not be null.
         *
         * @return A new OperationPipeline.  Never null.
         */
        public abstract <OUTPUT extends Object>
            OperationPipeline.OperationSink<OUTPUT> transform (
                Operation<ELEMENT, OUTPUT> operation
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;
    }


    /** A middle OperationPipeline which cannot build output. */
    public static interface OperationTrunk<ELEMENT extends Object>
        extends OperationPipeline<ELEMENT, OperationPipeline.OperationTrunk<ELEMENT>>, Serializable
    {
    }


    /** A When OperationPipeline. */
    public static interface OperationWhen<ELEMENT extends Object, PARENT extends OperationPipeline<ELEMENT, PARENT>>
        extends When<ELEMENT, PARENT, TermPipeline.TermSink<ELEMENT>, OperationPipeline.OperationWhen<ELEMENT, PARENT>>, OperationPipeline<ELEMENT, OperationPipeline.OperationWhen<ELEMENT, PARENT>>
    {
    }




    /**
     * <p>
     * Creates an expression Term which will process the specified
     * input lazily, on demand.
     * </p>
     *
     * @param input The input Term or TermPipeline.  Must not be null.
     *
     * @return The new expression Term.  Never null.
     */
    public abstract Term<VALUE> applyLazy (
            TermPipeline.TermSink<VALUE> input
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#check()


    /**
     * @see musaico.foundation.pipeline.Pipeline#edit()
     */
    @Override
    public abstract Edit<VALUE, PIPELINE, TermPipeline.TermSink<VALUE>> edit ()
        throws ReturnNeverNull.Violation;


    // Every Parent must implement java.lang.Object#equals(java.lang.Object)


    /**
     * <p>
     * Returns the Type of Terms output by this OperationPipeline
     * whenever there is an error, such as an invalid input Term, or even
     * an invalid parameter.
     * </p>
     *
     * <p>
     * For example, an <code> OperationPipeline&lt;Number&gt; </code>
     * might have error Type
     * <code>IntegerType.refine ().allowTerms ( Error.class ).buildTye ()</code>.
     * </p>
     *
     * <p>
     * Or an <code> OperationPipeline&lt;Date&gt; </code> might have
     * error Type
     * <code>DateType.refine ().allowTerms ( Error.class ).buildTye ()</code>.
     * </p>
     *
     * <p>
     * And so on.
     * </p>
     *
     * <p>
     * Every Term which can ever be output by this OperationPipeline
     * as an erropr valid according to the error type.  For example,
     * if this Operation outputs an Error term whenever the input
     * does not meet the input type's constraints, then the error Type
     * must consider Error Terms to be valid members of its domain.
     * </p>
     *
     * @return The error Type guaranteed by this OperationPipeline given
     *         any input Term which is invalid (does not meet
     *         any or all of the constraints of this OperationPipeline's
     *         <code> inputType () </code>).  Never null.
     */
    public abstract Type<VALUE> errorType ()
        throws ReturnNeverNull.Violation;


    // Every Parent must implement java.lang.Object#hashCode()


    /**
     * <p>
     * Returns the Type of input Terms required by this OperationPipeline.
     * </p>
     *
     * <p>
     * For example, an <code> OperationPipeline&lt;String&gt; </code>
     * might have input Type NonEmptyStringType.
     * </p>
     *
     * <p>
     * Or an <code> OperationPipeline&lt;Number&gt; </code> might have
     * input Type IntegerGreaterThanZeroType.
     * </p>
     *
     * <p>
     * And so on.
     * </p>
     *
     * <p>
     * When this OperationPipeline is applied to an input Term
     * which is not valid according to the input Type, the output type
     * is up to the pipeline implementer, but a typical OperationPipeline
     * will output an Error if the input Term is invalid.
     * </p>
     *
     * @return The input Type required by to this OperationPipeline.
     *         Never null.
     */
    public abstract Type<VALUE> inputType ()
        throws ReturnNeverNull.Violation;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#orderBy(musaico.foundation.order.Order[])


    /**
     * <p>
     * Returns the Type of Terms output by this OperationPipeline.
     * </p>
     *
     * <p>
     * For example, an <code> OperationPipeline&lt;String&gt; </code>
     * might have output Type StringContainingNoFunnyCharactersType.
     * </p>
     *
     * <p>
     * Or an <code> OperationPipeline&lt;Number&gt; </code> might have
     * output Type BigDecimalType.
     * </p>
     *
     * <p>
     * And so on.
     * </p>
     *
     * <p>
     * Every Term which can ever be output by this pipeline is valid
     * according to the output type.  For example, if this pipeline
     * outputs an Error term whenever the input does not meet the
     * input type's constraints, then the output Type must consider
     * the Error to be a valid member of its domain.  On the other hand,
     * an OperationPipeline whose output Type claims to only ever output
     * Terms of 10 or more elements can never output an Error term,
     * which has no elements.
     * </p>
     *
     * @return The output Type guaranteed by this OperationPipeline.
     *         Never null.
     */
    public abstract Type<VALUE> outputType ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds a new child Operation to this Pipeline.
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
     * @param operation The Operation to add to the pipeline.
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
     * If the output Type of this OperationPipeline and the input Type
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


    /**
     * <p>
     * Creates a new Operation which takes, as its inputs, the
     * outputs from this pipeline, and extracts the specified symbol
     * Operations, combining them into one symbol extraction Operation.
     * </p>
     *
     * <p>
     * The purpose and meaning of each symbol depends on the Type.
     * </p>
     *
     * @see musaico.foundation.term.Type#symbols()
     *
     * <p>
     * If no symbols are specified, then all symbols of each input Term 's
     * <code> type () </code> are extracted.
     * </p>
     *
     * <p>
     * Note that if a symbol from Type <code> t1 </code> is requested
     * from an OperationPipeline whose input Type is <code> t2 </code>,
     * then Type <code> t2 </code> might support all, none, or some
     * of the specified symbols.
     * </p>
     *
     * @param symbols The symbols to extract, or all symbols
     *                of each input Term, if none are specified.
     *                Can be empty.  Must not be null.
     *
     * @return The symbol extraction Operation.  Can cause a failed
     *         output from each input Term (such as an Error output),
     *         if the requested symbol(s) are not of this Type.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public abstract <SYMBOL extends Object>
        Operation<VALUE, SYMBOL> symbols (
            Operation<VALUE, SYMBOL> ... symbols
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    // Every Parent must implement
    // musaico.foundation.pipeline.Parent#thisPipeline()

    // Every Parent must implement java.lang.Object#toString()


    /**
     * @see musaico.foundation.pipeline.Pipeline#when(musaico.foundation.filter.Filter)
     *
     * Ensures that the When returned by an OperationPipeline
     * is itself an OperationPipeline.
     */
    @Override
    public abstract OperationPipeline.OperationWhen<VALUE, PIPELINE> when (
            Filter<?> condition
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#where(musaico.foundation.filter.Filter[])
}
