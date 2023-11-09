package musaico.foundation.term.pipeline;

import java.io.Serializable;

import java.util.Collection;
import java.util.Set;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.pipeline.Join;
import musaico.foundation.pipeline.Pipeline;
import musaico.foundation.pipeline.Sink;

import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * A Pipeline which can be used to join together multiple selections
 * of elements in any old order.
 * </p>
 *
 * @see musaico.foundation.pipeline.Sink#join(musaico.foundation.pipeline.Sink[])
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
public class TermJoin<VALUE extends Object, PARENT extends TermPipeline<VALUE, PARENT> & Sink<VALUE, PARENT, Term<VALUE>, TermPipeline.TermSink<VALUE>, OperationPipeline.OperationSink<VALUE>>>
    extends AbstractTermBranch<VALUE, TermJoin<VALUE, PARENT>, VALUE, PARENT, OperationPipeline.OperationJoin<VALUE>>
    implements Join<VALUE, PARENT, Term<VALUE>, TermPipeline.TermSink<VALUE>, OperationPipeline.OperationSink<VALUE>, TermJoin<VALUE, PARENT>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new TermJoin, with no previous joins
     * in the chain.
     * </p>
     *
     * @param parent The parent pipeline.  Must not be null.
     *
     * @param slave The Join operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     */
    public TermJoin (
            PARENT parent,
            OperationPipeline.OperationJoin<VALUE> slave
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent,       // parent
               slave,        // slave
               null );       // previous
    }


    /**
     * <p>
     * Creates a new TermJoin.
     * </p>
     *
     * @param parent The parent pipeline.  Must not be null.
     *
     * @param slave The Join operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     *
     * @param previous The previous join pipeline in this chain
     *                 (join ( ... )...join ( ... )...).  Can be null if this
     *                 is the first join pipeline in the chain.
     */
    public TermJoin (
            PARENT parent,
            OperationPipeline.OperationJoin<VALUE> slave,
            TermJoin<VALUE, PARENT> previous
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( parent,
                slave,
                previous );
    }


    /**
     * @see musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Pipeline)
     */
    @Override
    public final TermJoin<VALUE, PARENT> duplicate (
            PARENT duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return this.duplicateUgly ( duplicate_parent );
    }

    // Yes this is atrocious.  Sorry.
    @SuppressWarnings("unchecked")
    private final <SLAVE extends OperationPipeline.OperationJoin<VALUE>, SLAVE_PARENT extends OperationPipeline.OperationSink<VALUE>>
        TermJoin<VALUE, PARENT> duplicateUgly (
            PARENT duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return new TermJoin<VALUE, PARENT> (
            duplicate_parent,
            ( (SLAVE) this.slave () ).duplicate (
                (SLAVE_PARENT)
                    duplicate_parent.operations () )
          );
    }


    /**
     * @see musaico.foundation.pipeline.Join#join(musaico.foundation.pipeline.Sink[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public final TermJoin<VALUE, PARENT> join (
            TermPipeline.TermSink<VALUE> ... inputs
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        //        Parameter1.MustContainNoNulls.Violation:
        final TermJoin<VALUE, PARENT> next =
            new TermJoin<VALUE, PARENT> (
                this.parent (),                // parent
                this.slave ().join ( inputs ), // slave
                this );                        // previous
        return next;
    }
}
