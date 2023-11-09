package musaico.foundation.term.multiplicities;

import java.io.Serializable;

import java.util.Collection;
import java.util.Set;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.pipeline.Branch;
import musaico.foundation.pipeline.Pipeline;
import musaico.foundation.pipeline.SubPipeline;
import musaico.foundation.pipeline.SubPipelineMustNotBeEnded;

import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * A SubPipeline which is also a Pipeline and can have previous
 * branch segment(s).
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
 * @see musaico.foundation.term.multiplicities.MODULE#COPYRIGHT
 * @see musaico.foundation.term.multiplicities.MODULE#LICENSE
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


    // Every SubPipeline must implement
    // musaico.foundation.pipeline.SubPipeline#duplicate(musaico.foundation.pipeline.Pipeline)


    /**
     * @see musaico.foundation.pipeline.SubPipeline#end()
     */
    @Override
    public final PARENT end ()
        throws SubPipelineMustNotBeEnded.Violation,
               ReturnNeverNull.Violation
    {
        if ( this.isParentEnded () )
        {
            throw SubPipelineMustNotBeEnded.CONTRACT.violation (
                      this,   // plaintiff
                      this ); // evidence
        }

        synchronized ( this.lock )
        {
            if ( this.isEnded )
            {
                throw SubPipelineMustNotBeEnded.CONTRACT.violation (
                          this,   // plaintiff
                          this ); // evidence
            }

            this.slave ().end ();
            return this.parent.thisPipeline ();
        }
    }


    /**
     * @see musaico.foundation.term.TermPipeline#inputSource()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast parent.inputSource () <?>-<VALUE>.
    public final TermPipeline.TermTap<VALUE> inputSource ()
        throws ReturnNeverNull.Violation
    {
        // Never a Transform parent, so the type of elements returned
        // is always VALUE, not ?.
        return (TermPipeline.TermTap<VALUE>) this.parent.inputSource ();
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
     * @return True if this branch's parent SubPipeline has been
     *         <code> end () </code>ed; false if it has not yet
     *         been ended, or if it is not even a SubPipelin3e.
     */
    public final boolean isParentEnded ()
    {
        if ( this.parent instanceof SubPipeline )
        {
            final SubPipeline<?, ?, ?> parent_sub_pipeline =
                (SubPipeline<?, ?, ?>) this.parent;
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
     * @see musaico.foundation.pipeline.SubPipeline#parent()
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
