package musaico.foundation.term.pipeline;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.pipeline.Pipeline;
import musaico.foundation.pipeline.When;

import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * A Pipeline and Subsidiary which executes a chain of Operations
 * for each input Term which is KEPT by some specific filter.
 * </p>
 *
 * <p>
 * For example, a When pipeline might be created by calling
 * <code> Pipeline.when ( term_has_more_than_1_element ) </code>,
 * where the <code> term_has_more_than_1_element </code> Filter matches
 * only OneOrMore terms that do not contain exactly 1 element.
 * </p>
 *
 * <p>
 * Each When pipeline is part of a chain of one or more
 * Whens, each of which is applied to an input Term
 * only if the previous When(s) were not, and only if
 * the condition is met.
 * </p>
 *
 *
 * <p>
 * In Java every Pipeline must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
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
 * @see musaico.foundation.term.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.term.pipeline.MODULE#LICENSE
 */
public class TermWhen<VALUE extends Object, PARENT extends TermPipeline<VALUE, PARENT>>
    extends AbstractTermBranch<VALUE, TermWhen<VALUE, PARENT>, VALUE, PARENT, OperationPipeline.OperationWhen<VALUE, ?>>
    implements When<VALUE, PARENT, TermPipeline.TermSink<VALUE>, TermWhen<VALUE, PARENT>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new TermWhen.
     * </p>
     *
     * @param parent The parent pipeline.  Must not be null.
     *
     * @param slave The When operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     */
    public TermWhen (
            PARENT parent,
            OperationPipeline.OperationWhen<VALUE, ?> slave
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent,          // parent
               slave,           // slave
               null );          // previous
    }


    /**
     * <p>
     * Creates a new TermWhen, with no previous
     * condition in the chain.
     * </p>
     *
     * @param parent The parent pipeline.  Must not be null.
     *
     * @param slave The When operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     *
     * @param previous The previous conditional pipeline in this chain
     *                 (when ()...otherwise ()...).  Can be null if this
     *                 is the first conditional pipeline in the chain.
     */
    public TermWhen (
            PARENT parent,
            OperationPipeline.OperationWhen<VALUE, ?> slave,
            TermWhen<VALUE, PARENT> previous
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( parent,
                slave,
                previous );
    }


    /**
     * @see musaico.foundation.pipeline.Decision#and(musaico.foundation.filter.Filter[])
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"}) // Generic array creation,
        // generic array heap pollution.
    public final TermWhen<VALUE, PARENT> and (
            Filter<TermPipeline.TermSink<VALUE>> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        this.slave ().and ( filters );

        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Decision#condition()
     */
    @Override
    public final Filter<TermPipeline.TermSink<VALUE>> condition ()
        throws ReturnNeverNull.Violation
    {
        return this.slave ().condition ();
    }


    /**
     * @see musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Pipeline)
     */
    @Override
    public final TermWhen<VALUE, PARENT> duplicate (
            PARENT duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return this.duplicateUgly ( duplicate_parent );
    }

    // Yes this is atrocious.  Sorry.
    @SuppressWarnings("unchecked")
    private final <SLAVE extends OperationPipeline.OperationWhen<VALUE, SLAVE_PARENT>, SLAVE_PARENT extends OperationPipeline<VALUE, SLAVE_PARENT>>
        TermWhen<VALUE, PARENT> duplicateUgly (
            PARENT duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return new TermWhen<VALUE, PARENT> (
            duplicate_parent,
            ( (SLAVE) this.slave () ).duplicate (
                (SLAVE_PARENT)
                    duplicate_parent.operations () )
          );
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
            TermPipeline.TermSink<VALUE> grain
            )
    {
        if ( grain == null )
        {
            return FilterState.DISCARDED;
        }

        final FilterState state = this.slave ()
            .filter ( grain );

        return state;
    }


    /**
     * @see musaico.foundation.pipeline.Decision#infinite()
     */
    @Override
    public final TermWhen<VALUE, PARENT> infinite ()
        throws ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().infinite ();

        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Decision#length(long)
     */
    @Override
    public final TermWhen<VALUE, PARENT> length (
            long exact_length
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        // Throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation:
        this.slave ().length ( exact_length );

        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Decision#length(long,long)
     */
    @Override
    public final TermWhen<VALUE, PARENT> length (
            long minimum,
            long maximum
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter2.MustBeGreaterThanOrEqualTo.Violation,
               ReturnNeverNull.Violation
    {
        // Throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
        // Parameter2.MustBeGreaterThanOrEqualToZero.Violation:
        this.slave ().length ( minimum, maximum );

        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Decision#multiple()
     */
    @Override
    public final TermWhen<VALUE, PARENT> multiple ()
        throws ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().multiple ();

        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Decision#or(musaico.foundation.filter.Filter[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
        public final TermWhen<VALUE, PARENT> or (
            Filter<TermPipeline.TermSink<VALUE>> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        this.slave ().or ( filters );

        return this;
    }


    /**
     * @see musaico.foundation.pipeline.When#otherwise()
     */
    @Override
    public final TermWhen<VALUE, PARENT> otherwise ()
        throws ReturnNeverNull.Violation
    {
        final TermWhen<VALUE, PARENT> next =
            new TermWhen<VALUE, PARENT> (
                this.parent (),             // parent
                this.slave ().otherwise (), // slave
                this );                     // previous
        return next;
    }


    /**
     * @see musaico.foundation.pipeline.When#otherwise(musaico.foundation.filter.Filter)
     */
    @Override
    public final TermWhen<VALUE, PARENT> otherwise (
            Filter<TermPipeline.TermSink<VALUE>> condition
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  condition );

        final TermWhen<VALUE, PARENT> next =
            new TermWhen<VALUE, PARENT> (
                this.parent (),                        // parent
                this.slave ().otherwise ( condition ), // slave
                this );                                // previous
        return next;
    }


    /**
     * @see musaico.foundation.pipeline.Decision#single()
     */
    @Override
    public final TermWhen<VALUE, PARENT> single ()
        throws ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().single ();

        return this;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "if ( " + this.slave ().condition () + " )"
            + " then " + super.toString ();
    }


    /**
     * @see musaico.foundation.pipeline.Decision#unique()
     */
    @Override
    public final TermWhen<VALUE, PARENT> unique ()
        throws ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().unique ();

        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Decision#xor(musaico.foundation.filter.Filter[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
        public final TermWhen<VALUE, PARENT> xor (
            Filter<TermPipeline.TermSink<VALUE>> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        this.slave ().xor ( filters );

        return this;
    }
}
