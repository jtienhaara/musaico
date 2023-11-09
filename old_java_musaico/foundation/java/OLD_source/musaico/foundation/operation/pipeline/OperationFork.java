package musaico.foundation.term.multiplicities;

import java.io.Serializable;

import java.util.Collection;
import java.util.Set;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.pipeline.Fork;
import musaico.foundation.pipeline.Pipeline;
import musaico.foundation.pipeline.SubPipeline;

import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * A Pipeline which can be used to fork together multiple selections
 * of elements in any old order.
 * </p>
 *
 * @see musaico.foundation.pipeline.Pipeline#fork()
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
public class TermFork<VALUE extends Object, PARENT extends TermPipeline<VALUE, PARENT>>
    extends AbstractTermBranch<VALUE, TermFork<VALUE, PARENT>, VALUE, PARENT, OperationPipeline.OperationFork<VALUE, ?>>
    implements Fork<VALUE, PARENT, TermFork<VALUE, PARENT>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new TermFork, with no previous forking
     * in the chain.
     * </p>
     *
     * @param parent The parent pipeline.  Must not be null.
     *
     * @param slave The Fork operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     */
    public TermFork (
            PARENT parent,
            OperationPipeline.OperationFork<VALUE, ?> slave
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent,       // parent
               slave,        // slave
               null );       // previous
    }


    /**
     * <p>
     * Creates a new TermFork.
     * </p>
     *
     * @param parent The parent pipeline.  Must not be null.
     *
     * @param slave The Fork operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     *
     * @param previous The previous forked sub-branch in this chain
     *                 (fork ()...disjoin ()...).  Can be null if this
     *                 is the first forked sub-branch in the chain.
     */
    public TermFork (
            PARENT parent,
            OperationPipeline.OperationFork<VALUE, ?> slave,
            TermFork<VALUE, PARENT> previous
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( parent,
                slave,
                previous );
    }


    /**
     * @see musaico.foundation.pipeline.Fork#disjoin()
     */
    @Override
        public final TermFork<VALUE, PARENT> disjoin ()
        throws ReturnNeverNull.Violation
    {
        final TermFork<VALUE, PARENT> next =
            new TermFork<VALUE, PARENT> (
                this.parent (),           // parent
                this.slave ().disjoin (), // slave
                this );                   // previous
        return next;
    }


    /**
     * @see musaico.foundation.pipeline.SubPipeline#duplicate(musaico.foundation.pipeline.Pipeline)
     */
    @Override
    public final TermFork<VALUE, PARENT> duplicate (
            PARENT duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return this.duplicateUgly ( duplicate_parent );
    }

    // Yes this is atrocious.  Sorry.
    @SuppressWarnings("unchecked")
    private final <SLAVE extends OperationPipeline.OperationFork<VALUE, SLAVE_PARENT>, SLAVE_PARENT extends OperationPipeline<VALUE, SLAVE_PARENT>>
        TermFork<VALUE, PARENT> duplicateUgly (
            PARENT duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return new TermFork<VALUE, PARENT> (
            duplicate_parent,
            ( (SLAVE) this.slave () ).duplicate (
                (SLAVE_PARENT)
                    duplicate_parent.operations () )
          );
    }
}
