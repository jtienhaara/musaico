package musaico.foundation.term;

import java.io.Serializable;


import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.pipeline.Branch;
import musaico.foundation.pipeline.Edit;
import musaico.foundation.pipeline.Sink;


/**
 * <p>
 * A TermPipeline of Types, which can be used to build a single Type.
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
public interface TypePipeline<VALUE extends Object, PIPELINE extends TypePipeline<VALUE, PIPELINE>>
    extends TermPipeline<Type<VALUE>, PIPELINE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** A TypePipeline which can build the final output Type
     *  by applying the sequence of operations to the input and
     *  merging together the term of Type(s). */
    public static interface TypeSink<ELEMENT extends Object>
        extends Sink<Type<ELEMENT>, TypePipeline.TypeSink<ELEMENT>, Term<Type<ELEMENT>>, TermPipeline.TermSink<Type<ELEMENT>>, OperationPipeline.OperationSink<Type<ELEMENT>>>, TypePipeline<ELEMENT, TypePipeline.TypeSink<ELEMENT>>, Serializable
    {
        /**
         * <p>
         * Builds a new Type from this TypePipeline Sink.
         * </p>
         *
         * @return A newly constructed Type, built up from
         *         from the Term of Type(s) output by this
         *         TypePipeline Sink.  Never null.
         */
        public abstract Type<ELEMENT> buildType ();


        /**
         * @see musaico.foundation.pipeline.Sink#duplicate()
         */
        @Override
        public abstract TypePipeline.TypeSink<ELEMENT> duplicate ()
            throws ReturnNeverNull.Violation;


        /**
         * <p>
         * Creates a Transform from this TypePipeline Sink into one with
         * another (derived) class of elements.
         * </p>
         *
         * <p>
         * For example, the following might be used to take a Type
         * over general elements and re-base it over Number elements:
         * </p>
         *
         * <pre>
         *     final Type<Object> term_with_1_to_3_elements = ...;
         *     final Type<Number> number_with_1_to_3_elements =
         *         term_with_1_to_3_elements.refine ()
         *                                  .elementClass ( Number.class )
         *                                  .buildType ();
         * </p>
         *
         * @param to_element_class The class of Elements to transform
         *                         this TypePipeline Sink into.
         *                         Must not be null.
         *
         * @return A Transform from this TypePipeline Sink into a new one
         *         with the specified element class as its basis.
         *         Never null.
         */
        public abstract <TO extends ELEMENT>
            TypePipeline.TypeSink<TO> elementClass (
                Class<TO> to_element_class
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;


        // Every Pipeline must implement
        // musaico.foundation.pipeline.Sink#fork()


        /**
         * <p>
         * Creates a new TypePipeline to build a Type with elements
         * of any old class, <code> TypePipeline&lt;Object&gt; </code>.
         * </p>
         *
         * <p>
         * In the process, none of the existing term constraints will be
         * changed, but the element class will be changed
         * to <code> Object.class </code>, and all element constraints
         * will be removed.
         * </p>
         *
         * @return The generalized TypePipeline.  Never null.
         */
        public abstract TypePipeline.TypeSink<Object> generalize ()
            throws ReturnNeverNull.Violation;



        // Every Pipeline must implement
        // musaico.foundation.pipeline.Sink#join()

        // Every TermPipeline.TermSink must implement
        // musaico.foundation.term.TermPipeline.TermSink#operations()

        // Every Sink must implement
        // musaico.foundation.pipeline.Sink#output()


        /**
         * <p>
         * Creates a new TypePipeline to build a Type with elements of the
         * specified class, <code> TypePipeline&lt;SPECIFIC&gt; </code>.
         * </p>
         *
         * <p>
         * In the process, none of the existing constraints will be changed,
         * but the element class will be changed to the specified new
         * element class, and an element constraint will be added.
         * </p>
         *
         * @param specific_element_class The new, specific class of elements
         *                               for the new TypePipeline.
         *                               Must not be null.
         *
         * @return The new, specific TypePipeline.  Never null.
         */
        public abstract <SPECIFIC extends ELEMENT>
            TypePipeline.TypeSink<SPECIFIC> specify (
                Class<SPECIFIC> specific_element_class
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;
    }


    /** A middle TypePipeline which cannot build output. */
    public static interface TypeTrunk<ELEMENT extends Object>
        extends TypePipeline<ELEMENT, TypePipeline.TypeTrunk<ELEMENT>>, Serializable
    {
        // Every TermPipeline.TermTrunk must implement
        // musaico.foundation.term.TermPipeline.TermTrunk#operations()
    }




    /**
     * <p>
     * Adds an Operation to this pipeline which will ensure that the
     * Type that is output by this pipeline considers all of
     * the specified element(s) to be valid.
     * </p>
     *
     * <p>
     * The resulting Type could simply be the input Type, if the input
     * Type accepts the specified element(s) as valid for its Terms.
     * </p>
     *
     * <p>
     * However the resulting Type might be weakened, removing
     * element constraints from the input Type which would otherwise
     * disallow Terms with the specified element(s).  For example,
     * a Type which requires non-empty String elements might be
     * weakened to allow empty String elements.
     * </p>
     *
     * @param elements The element(s) to allow.  Must not be null.
     *                 Must not contain any null elements.
     *
     * @return This pipeline, possibly with weakening refinement operation(s)
     *         added.  Never null.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution varargs.
    public abstract PIPELINE allowElements (
            VALUE ... elements
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an Operation to this pipeline which will ensure that the
     * Type that is output by this pipeline considers the specified
     * class of Terms to be valid.
     * </p>
     *
     * <p>
     * The resulting Type could simply be the input Type, if the input
     * Type accepts Terms of the specified class as instances of it.
     * </p>
     *
     * <p>
     * However the resulting Type might be weakened, removing constraints
     * from the input Type which would otherwise disallow Terms
     * of the specified class.  For example, a Type which requires
     * either a Multiplicity greater than or equal to 1 element(s),
     * or an Error (but never No elements), might be weakened to allow
     * No element Terms.
     * </p>
     *
     * @param term_class The class of Terms to allow.  Must not be null.
     *
     * @return This pipeline, possibly with weakening refinement operation(s)
     *         added.  Never null.
     */
    public abstract PIPELINE allowTerms (
            Class<?> term_class
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return A new sub-pipeline which will work with the cast operations
     *         of the input Type, and return to this TypePipeline
     *         when end () is invoked.  Never null.
     */
    public abstract <SUB extends Branch<Operation<VALUE, ?>, SUB, Type<VALUE>, PIPELINE>>
        SUB casts ()
        throws ReturnNeverNull.Violation;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#check()


    /**
     * @return A new sub-pipeline which will work with
     *         the Term Contract constraints of the input Type,
     *         and return to this TypePipeline when end () is invoked.
     *         Never null.
     */
    public abstract <SUB extends Branch<Contract<Term<?>, ? extends TermViolation>, SUB, Type<VALUE>, PIPELINE>>
        SUB constraints ()
        throws ReturnNeverNull.Violation;


    // Every TermPipeline must implement
    // musaico.foundation.term.TermPipeline#edit


    /**
     * @return A new sub-pipeline which will work with the element Contract
     *         constraints of the input Type, and return to this TypePipeline
     *         when end () is invoked.  Never null.
     */
    public abstract <SUB extends Branch<Contract<VALUE, ?>, SUB, Type<VALUE>, PIPELINE>>
        SUB elementConstraints ()
        throws ReturnNeverNull.Violation;


    // Every Parent must implement java.lang.Object#equals(java.lang.Object)

    // Every Parent must implement java.lang.Object#hashCode()

    // Every TermPipeline must implement
    // musaico.foundation.term.TermPipeline#input()


    /**
     * <p>
     * Adds an operation to the pipeline which will intersect the specified
     * Type with that being built (Type1 AND Type2).
     * </p>
     *
     * @see musaico.foundation.pipeline.Edit#intersection(java.lang.Object)
     *
     * @param that The TypePipeline to intersect with the current Type
     *             being built.  Must not be null.
     *
     * @return This TypePipeline.  Never null.
     */
    public abstract PIPELINE intersection (
            Type<VALUE> that
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to this pipeline to set the "none" value
     * of the output Type to the specified value.
     * </p>
     *
     * @param none The none value for the output Type.  Must not be null.
     *
     * @return This TypePipeline, Never null.
     */
    public abstract PIPELINE none (
            VALUE none
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every TermPipeline must implement
    // musaico.foundation.term.TermPipeline#operations()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#orderBy(musaico.foundation.order.Order[])

    // Every TermPipeline must implement
    // musaico.foundation.term.TermPipeline#pipe(musaico.foundation.term.Operation)

    // Every TermPipeline must implement
    // musaico.foundation.term.TermPipeline#pipe(musaico.foundation.term.OperationPipeline)

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#select()


    /**
     * @return A new sub-pipeline which will work with the static symbols
     *         of the input Type, such as Type parameters,
     *         and return to this TypePipeline when end () is invoked.
     *         Each symbol is an Operation which will be turned into
     *         a Transform by the Type when its <code> staticSymbols () </code>
     *         method is called.  Never null.
     *
     * @see musaico.foundation.term.Type#staticSymbols()
     */
    public abstract <SUB extends Branch<Operation<Type<VALUE>, ?>, SUB, Type<VALUE>, PIPELINE>>
        SUB staticSymbols ()
        throws ReturnNeverNull.Violation;


    /**
     * @return A new sub-pipeline which will work with the symbols
     *         of Terms of the input Type, each one an Operation to extract
     *         a specific symbol from the Term it is applied to,
     *         and return to this TypePipeline when end () is invoked.
     *         Never null.
     *
     * @see musaico.foundation.term.Type#symbols()
     */
    public abstract <SUB extends Branch<Operation<VALUE, ?>, SUB, Type<VALUE>, PIPELINE>>
        SUB symbols ()
        throws ReturnNeverNull.Violation;


    // Every TermPipeline must implement
    // musaico.foundation.term.TermPipeline#symbols(musacico.foundation.term.Transform[])

    // Every Parent must implement
    // musaico.foundation.pipeline.Parent#thisPipeline()

    // Every Parent must implement java.lang.Object#toString()

    // Every TermPipeline must implement
    // musaico.foundation.term.TermPipeline#type()


    /**
     * @return The Type constructor for the Type being built.
     *         By default, this TypePipeline; however the Type constructor
     *         can be overridden.  Never null.
     *
     * @see musaico.foundation.term.Type#typeConstructor()
     *
     * @see musaico.foundation.term.TypePipeline#typeConstructor(musaico.foundation.term.TermPipeline.TermSink)
     */
    public abstract TermPipeline.TermSink<Type<VALUE>> typeConstructor ();


    /**
     * <p>
     * Sets the Type constructor for the Type being built.
     * </p>
     *
     * <p>
     * By default, the toplevel TypePipeline of which this is a branch
     * will be used as the Type constructor.  However the default
     * can be overridden.  Once overridden, the Type constructor
     * will not change, even if additional operations are piped in.
     * However, the Type constructor can be retrieved and added to
     * directly, after it has been overridden, by retrieving
     * the <code> typeConstructor () </code> of this pipeline.
     * </p>
     *
     * @see musaico.foundation.term.Type#typeConstructor()
     *
     * @param type_constructor The overridden Type constructor for the Type
     *                         being built.  Must not be null.
     *
     * @return This TypePipeline.  Never null.
     */
    public abstract PIPELINE typeConstructor (
            TermPipeline.TermSink<Type<VALUE>> type_constructor
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Adds an operation to the pipeline which will join the specified
     * Type with that being built (Type1 OR Type2).
     * </p>
     *
     * @see musaico.foundation.pipeline.Edit#union(java.lang.Object)
     *
     * @param that The TypePipeline to union with the current Type
     *             being built.  Must not be null.
     *
     * @return This TypePipeline.  Never null.
     */
    public abstract PIPELINE union (
            Type<VALUE> that
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#when(musaico.foundation.filter.Filter)

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#where(musaico.foundation.filter.Filter[])
}
