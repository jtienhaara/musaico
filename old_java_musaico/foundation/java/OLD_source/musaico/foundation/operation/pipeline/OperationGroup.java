package musaico.foundation.term.multiplicities;

import java.io.Serializable;

import java.util.Collection;
import java.util.Set;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.pipeline.Group;
import musaico.foundation.pipeline.Pipeline;
import musaico.foundation.pipeline.SubPipeline;

import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * A Pipeline which can be used to group together multiple selections
 * of elements in any old order.
 * </p>
 *
 * @see musaico.foundation.pipeline.Pipeline#group()
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
public class TermGroup<VALUE extends Object, PARENT extends TermPipeline<VALUE, PARENT>>
    extends AbstractTermBranch<VALUE, TermGroup<VALUE, PARENT>, VALUE, PARENT, OperationPipeline.OperationGroup<VALUE, ?>>
    implements Group<VALUE, PARENT, TermGroup<VALUE, PARENT>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new TermGroup, with no previous grouping
     * in the chain.
     * </p>
     *
     * @param parent The parent pipeline.  Must not be null.
     *
     * @param slave The Group operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     */
    public TermGroup (
            PARENT parent,
            OperationPipeline.OperationGroup<VALUE, ?> slave
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent,       // parent
               slave,        // slave
               null );       // previous
    }


    /**
     * <p>
     * Creates a new TermGroup.
     * </p>
     *
     * @param parent The parent pipeline.  Must not be null.
     *
     * @param slave The Group operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     *
     * @param previous The previous group pipeline in this chain
     *                 (group ()...join ()...).  Can be null if this
     *                 is the first group pipeline in the chain.
     */
    public TermGroup (
            PARENT parent,
            OperationPipeline.OperationGroup<VALUE, ?> slave,
            TermGroup<VALUE, PARENT> previous
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( parent,
                slave,
                previous );
    }


    /**
     * @see musaico.foundation.pipeline.SubPipeline#duplicate(musaico.foundation.pipeline.Pipeline)
     */
    @Override
    public final TermGroup<VALUE, PARENT> duplicate (
            PARENT duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return this.duplicateUgly ( duplicate_parent );
    }

    // Yes this is atrocious.  Sorry.
    @SuppressWarnings("unchecked")
    private final <SLAVE extends OperationPipeline.OperationGroup<VALUE, SLAVE_PARENT>, SLAVE_PARENT extends OperationPipeline<VALUE, SLAVE_PARENT>>
        TermGroup<VALUE, PARENT> duplicateUgly (
            PARENT duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return new TermGroup<VALUE, PARENT> (
            duplicate_parent,
            ( (SLAVE) this.slave () ).duplicate (
                (SLAVE_PARENT)
                    duplicate_parent.operations () )
          );
    }


    /**
     * @see musaico.foundation.pipeline.Group#join()
     */
    @Override
        public final TermGroup<VALUE, PARENT> join ()
        throws ReturnNeverNull.Violation
    {
        final TermGroup<VALUE, PARENT> next =
            new TermGroup<VALUE, PARENT> (
                this.parent (),        // parent
                this.slave ().join (), // slave
                this );                // previous
        return next;
    }
}
