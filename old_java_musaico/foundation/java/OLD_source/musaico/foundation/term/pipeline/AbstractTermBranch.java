package musaico.foundation.term.pipeline;

import java.io.Serializable;

import java.util.Collection;
import java.util.Set;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.pipeline.Branch;
import musaico.foundation.pipeline.Pipeline;
import musaico.foundation.pipeline.Subsidiary;
import musaico.foundation.pipeline.SubsidiaryMustNotBeEnded;

import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * Boilerplate Branch methods for Terms, relying pretty much entirely
 * on an OperationPipeline "slave" to do all the heavy lifting.
 * </p>
 *
 *
 * <p>
 * In Java every Pipeline must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Pipeline must be Serializable in order to
 * play nicely across RMI.  However users of the Pipeline
 * must be careful, since the values and expected data stored inside
 * (if any) might not be Serializable.
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
public abstract class AbstractTermBranch<VALUE extends Object, BRANCH extends AbstractTermBranch<VALUE, BRANCH, PARENT_VALUE, PARENT, SLAVE>, PARENT_VALUE extends Object, PARENT extends TermPipeline<PARENT_VALUE, PARENT>, SLAVE extends OperationPipeline<VALUE, ?> & Branch<VALUE, ?, PARENT_VALUE, ?>>
    extends AbstractTermPipeline<VALUE, BRANCH, SLAVE>
    implements Branch<VALUE, BRANCH, PARENT_VALUE, PARENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( AbstractTermBranch.class );


    // Lock critical sections on this token:
    private final Serializable lock = new String ( "lock" );

    // The parent pipeline.
    private final PARENT parent;

    // The previous branch segment.  Can be null.
    private final BRANCH previous;

    // MUTABLE
    // Has end () been called?
    private boolean isEnded = false;


    /**
     * <p>
     * Creates a new AbstractTermBranch, with no previous segments
     * in the branch.
     * </p>
     *
     * @param parent The parent pipeline.  Must not be null.
     *
     * @param slave The operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     */
    public AbstractTermBranch (
            PARENT parent,
            SLAVE slave
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent,       // parent
               slave,        // slave
               null );       // previous
    }


    /**
     * <p>
     * Creates a new AbstractTermBranch.
     * </p>
     *
     * @param parent The parent pipeline.  Must not be null.
     *
     * @param slave The operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     *
     * @param previous The previous segment in this Branch.
     *                 Can be null if this is the first segment
     *                 in the branch.
     */
    public AbstractTermBranch (
            PARENT parent,
            SLAVE slave,
            BRANCH previous
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( slave );         // slave

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               parent, slave );
        // previous CAN be null.

        this.parent = parent;
        this.previous = previous;
    }


    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Pipeline)


    /**
     * @see musaico.foundation.pipeline.Subsidiary#end()
     */
    @Override
    public final PARENT end ()
        throws SubsidiaryMustNotBeEnded.Violation,
               ReturnNeverNull.Violation
    {
        if ( this.isParentEnded () )
        {
            throw SubsidiaryMustNotBeEnded.CONTRACT.violation (
                      this,   // plaintiff
                      this ); // evidence
        }

        synchronized ( this.lock )
        {
            if ( this.isEnded )
            {
                throw SubsidiaryMustNotBeEnded.CONTRACT.violation (
                          this,   // plaintiff
                          this ); // evidence
            }

            this.slave ().end ();
            return this.parent.thisPipeline ();
        }
    }


    /**
     * @see musaico.foundation.term.TermPipeline#input()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast parent.input () <?>-<VALUE>.
    public final TermPipeline.TermSink<VALUE> input ()
        throws ReturnNeverNull.Violation
    {
        // Never a Transform parent, so the type of elements returned
        // is always VALUE, not ?.
        return (TermPipeline.TermSink<VALUE>) this.parent.input ();
    }


    /**
     * @see musaico.foundation.pipeline.Select#isEnded()
     */
    @Override
    public final boolean isEnded ()
    {
        synchronized ( this.lock )
        {
            if ( this.isEnded )
            {
                return true;
            }
        }

        if ( this.isParentEnded () )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @return True if this branch's parent Subsidiary has been
     *         <code> end () </code>ed; false if it has not yet
     *         been ended, or if it is not even a SubPipelin3e.
     */
    public final boolean isParentEnded ()
    {
        if ( this.parent instanceof Subsidiary )
        {
            final Subsidiary<?, ?, ?, ?> parent_sub_pipeline =
                (Subsidiary<?, ?, ?, ?>) this.parent;
            if ( parent_sub_pipeline.isEnded () )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.pipeline.Subsidiary#parent()
     */
    @Override
    public final PARENT parent ()
        throws ReturnNeverNull.Violation
    {
        return this.parent.thisPipeline ();
    }


    /**
     * @see musaico.foundation.pipeline.Branch#previousOrNull()
     */
    @Override
    public final BRANCH previousOrNull ()
        throws ReturnNeverNull.Violation
    {
        return this.previous;
    }
}
