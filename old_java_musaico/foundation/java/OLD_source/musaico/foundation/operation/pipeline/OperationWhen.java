package musaico.foundation.term.multiplicities;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.pipeline.Pipeline;
import musaico.foundation.pipeline.When;

import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * A Pipeline and SubPipeline which executes a chain of Operations
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
 * @see musaico.foundation.term.multiplicities.MODULE#COPYRIGHT
 * @see musaico.foundation.term.multiplicities.MODULE#LICENSE
 */
public class TermWhen<VALUE extends Object, PARENT extends TermPipeline<VALUE, PARENT>>
    extends AbstractTermBranch<VALUE, TermWhen<VALUE, PARENT>, VALUE, PARENT, OperationPipeline.OperationWhen<VALUE, ?>>
    implements When<VALUE, PARENT, TermWhen<VALUE, PARENT>>, Serializable
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
     * @see musaico.foundation.pipeline.When#condition()
     */
    @Override
    public final Filter<?> condition ()
        throws ReturnNeverNull.Violation
    {
        return this.slave ().condition ();
    }


    /**
     * @see musaico.foundation.pipeline.SubPipeline#duplicate(musaico.foundation.pipeline.Pipeline)
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
            Filter<?> condition
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
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "if ( " + this.slave ().condition () + " )"
            + " then " + super.toString ();
    }
}
