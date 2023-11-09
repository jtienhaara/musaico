package musaico.foundation.term.multiplicities;

import java.io.Serializable;

import java.lang.reflect.Constructor;

import java.util.Iterator;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;
import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.order.Order;

import musaico.foundation.pipeline.Pipeline;
import musaico.foundation.pipeline.SubPipeline;
import musaico.foundation.pipeline.SubPipelineMustNotBeEnded;
import musaico.foundation.pipeline.Tap;

import musaico.foundation.term.Operation;
import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * A simple SubPipeline which depends entirely on the Type(s)
 * of the input Term to add Operations to the pipeline.
 * </p>
 *
 *
 * <p>
 * In Java every Pipeline must be Serializable in order to
 * play nicely across RMI.  However users of the Pipeline
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
 * </p>
 *
 * <p>
 * In Java every Pipeline must implement equals (), hashCode ()
 * and toString ().
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
public abstract class AbstractTermSubPipeline<VALUE extends Object, PARENT extends TermPipeline<VALUE, PARENT>, SUB extends SubPipeline<VALUE, PARENT, SUB>, SLAVE extends SubPipeline<VALUE, ?, ?>>
    implements SubPipeline<VALUE, PARENT, SUB>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( AbstractTermSubPipeline.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // Lock critical sections on this token:
    private final Serializable lock = new String ( "lock" );

    // The parent Pipeline.
    private final PARENT parent;

    // The SubPipeline doing all the work for us.
    private final SLAVE slave;

    // MUTABLE
    // Has end () been called?
    private boolean isEnded = false;


    /**
     * <p>
     * Creates a new AbstractTermSubPipeline.
     * </p>
     *
     * @param parent The parent of this SubPipeline.  Must not be null.
     *
     * @param slave The SubPipeline which will do all the work.
     *              Must not be null.
     *
     * @throws ClassCastException If this AbstractTermSubPipeline
     *                            is not an instance of SUB
     *                            (Edit, Where, OrderBy,
     *                            and so on).  In this case, the
     *                            implementer has violated the structure
     *                            of AbstractTermSubPipeline.
     *                            Each AbstractTermSubPipeline
     *                            must be an instance of its own
     *                            SUB type parameter.
     */
    @SuppressWarnings("unchecked") // It is up to the implementer to ensure
        // this is an instance of SUB.
    public AbstractTermSubPipeline (
            PARENT parent,
            SLAVE slave
            )
        throws ParametersMustNotBeNull.Violation,
               ClassCastException
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               parent, slave );

        this.parent = parent;
        this.slave = slave;

        this.contracts = new Advocate ( (SUB) this );
    }


    /**
     * @return The Advocate for this sub pipeline.  Never null.
     */
    protected final Advocate contracts ()
        throws ReturnNeverNull.Violation
    {
        return this.contracts;
    }



    /**
     * @see musaico.foundation.pipeline.SubPipeline#end()
     */
    @Override
    public final PARENT end ()
        throws ReturnNeverNull.Violation
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

            this.slave.end ();
            return this.parent.thisPipeline ();
        }
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final AbstractTermSubPipeline<?, ?, ?, ?> that =
            (AbstractTermSubPipeline<?, ?, ?, ?>) object;

        if ( this.slave == null )
        {
            if ( that.slave != null )
            {
                return false;
            }
        }
        else if ( that.slave == null )
        {
            return false;
        }
        else if ( ! this.slave.equals ( that.slave ) )
        {
            return false;
        }

        // It doesn't matter whether the parent pipelines are equal,
        // this sub-pipeline is equal to that one.

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        if ( this.slave == null )
        {
            return 0;
        }

        return 37 * this.slave.hashCode ();
    }


    /**
     * @see musaico.foundation.term.TermPipeline#inputSource()
     */
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
     * @return The SubPipeline doing all the work for us.  Never null.
     */
    protected SLAVE slave ()
        throws ReturnNeverNull.Violation
    {
        return this.slave;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " " + this.slave;
    }
}
