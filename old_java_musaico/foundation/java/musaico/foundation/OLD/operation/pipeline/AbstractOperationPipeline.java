package musaico.foundation.term.multiplicities;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;
import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.order.Order;

import musaico.foundation.pipeline.Edit;
import musaico.foundation.pipeline.Fork;
import musaico.foundation.pipeline.Group;
import musaico.foundation.pipeline.OrderBy;
import musaico.foundation.pipeline.Pipeline;
import musaico.foundation.pipeline.Select;
import musaico.foundation.pipeline.When;
import musaico.foundation.pipeline.Where;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Operation;
import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;
import musaico.foundation.term.Transform;
import musaico.foundation.term.Type;


/**
 * <p>
 * A simple Pipeline which depends entirely on a slave OperationPipeline
 * to add Operations to the pipeline.
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
public abstract class AbstractTermPipeline<VALUE extends Object, PIPELINE extends TermPipeline<VALUE, PIPELINE>, SLAVE extends OperationPipeline<VALUE, ?>>
    implements TermPipeline<VALUE, PIPELINE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( AbstractTermPipeline.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The Operation Pipeline doing all the work for us.
    // Could be an OperationPipeline, or could be a SubPipeline.
    private final SLAVE slave;


    /**
     * <p>
     * Creates a new AbstractTermPipeline.
     * </p>
     *
     * @param slave The Operation Pipeline which will do all the work.
     *              Must not be null.
     *
     * @throws ClassCastException If this AbstractTermPipeline
     *                            is not an instance of PIPELINE
     *                            (maybe vanilla TermPipeline, or
     *                            Edit, Where, OrderBy,
     *                            and so on).  In this case, the
     *                            implementer has violated the structure
     *                            of AbstractTermPipeline.
     *                            Each AbstractTermPipeline must
     *                            be an instance of its own PIPELINE
     *                            type parameter.
     */
    @SuppressWarnings("unchecked") // It is up to the implementer to ensure
        // this is an instance of PIPELINE.
    public AbstractTermPipeline (
            SLAVE slave
            )
        throws ParametersMustNotBeNull.Violation,
               ClassCastException
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               slave );

        this.slave = slave;

        this.contracts = new Advocate ( (PIPELINE) this );
    }


    /**
     * @return The Advocate which enforces contractual obligations
     *         and guarantees for this pipeline, such as parameter
     *         requirements for methods, or return value guarantess.
     *         Never null.
     */
    protected final Advocate contracts ()
    {
        return this.contracts;
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#check()
     */
    @Override
    public final PIPELINE check ()
        throws ReturnNeverNull.Violation
    {
        this.slave.check ();
        return this.thisPipeline ();
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#contains(musaico.foundation.pipeline.Pipeline)
     */
    @Override
    public final boolean contains (
            Pipeline<?, ?> that
            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that == this )
        {
            return true;
        }

        final TermPipeline.TermTap<?> this_input_source =
            this.inputSource ();

        final OperationPipeline<?, ?> operation_pipeline;
        if ( that instanceof OperationPipeline )
        {
            operation_pipeline = (OperationPipeline<?, ?>) that;
        }
        else if ( that instanceof TermPipeline )
        {
            final TermPipeline<?, ?> term_pipeline =
                (TermPipeline<?, ?>) that;
            if ( term_pipeline.equals ( this_input_source ) )
            {
                return true;
            }

            final TermPipeline.TermTap<?> that_input_source =
                term_pipeline.inputSource ();
            if ( that_input_source == this )
            {
                return false;
            }
            else if ( that_input_source == that )
            {
                return false;
            }
            else if ( ! that_input_source.equals ( this_input_source ) )
            {
                if ( ! this_input_source.contains ( that_input_source ) )
                {
                    return false;
                }
            }

            operation_pipeline = term_pipeline.operations ();
        }
        else
        {
            return false;
        }

        final boolean contains_operations =
            this.slave.contains ( operation_pipeline );
        if ( contains_operations )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#edit()
     */
    @Override
    public final Edit<VALUE, PIPELINE, Term<VALUE>> edit ()
        throws ReturnNeverNull.Violation
    {
        return new TermEdit<VALUE, PIPELINE> ( this.thisPipeline (), // parent
                                               this.slave.edit () ); // slave
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

        final AbstractTermPipeline<?, ?, ?> that =
            (AbstractTermPipeline<?, ?, ?>) object;

        final TermPipeline.TermTap<VALUE> this_input = this.inputSource ();
        final TermPipeline.TermTap<?> that_input     = that.inputSource ();
        if ( this_input == null )
        {
            if ( that_input != null )
            {
                return false;
            }
        }
        else if ( that_input == null )
        {
            return false;
        }
        else if ( ! this_input.equals ( that_input ) )
        {
            return false;
        }

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

        // It doesn't matter if this pipeline has a parent (as in
        // a TermWhen or a TermFork or a TermGroup),
        // this sub-pipeline is equal to that one.

        return true;
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#fork()
     */
    @Override
    public final TermFork<VALUE, PIPELINE> fork ()
        throws ReturnNeverNull.Violation
    {
        final TermFork<VALUE, PIPELINE> fork =
            new TermFork<VALUE, PIPELINE> (
                this.thisPipeline (),  // parent
                this.slave.fork (),   // slave
                null );                // previous

        return fork;
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#group()
     */
    @Override
    public final TermGroup<VALUE, PIPELINE> group ()
        throws ReturnNeverNull.Violation
    {
        final TermGroup<VALUE, PIPELINE> group =
            new TermGroup<VALUE, PIPELINE> (
                this.thisPipeline (),  // parent
                this.slave.group (),   // slave
                null );                // previous

        return group;
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

        final int input_source_hash_code;
        final TermPipeline.TermTap<VALUE> input_source = this.inputSource ();
        if ( input_source == null )
        {
            input_source_hash_code = 0;
        }
        else
        {
            input_source_hash_code = input_source.hashCode ();
        }

        return 37 * this.slave.hashCode ()
            + input_source_hash_code;
    }
 

    /**
     * @see musaico.foundation.term.TermPipeline#inputSource()
     *
     * Never a Transform, so the type of elements returned is always
     * VALUE not ?.
     */
    @Override
    public abstract TermPipeline.TermTap<VALUE> inputSource ()
        throws ReturnNeverNull.Violation;


    /**
     * @see musaico.foundation.term.TermPipeline#operations()
     */
    @Override
    public final SLAVE operations ()
        throws ReturnNeverNull.Violation
    {
        return this.slave;
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#orderBy(musaico.foundation.order.Order[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public final OrderBy<VALUE, PIPELINE> orderBy (
            Order<VALUE> ... orders
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        return new TermOrderBy<VALUE, PIPELINE> (
                       this.thisPipeline (),            // parent
                       this.slave.orderBy ( orders ) ); // slave
    }


    /**
     * @see musaico.foundation.term.TermPipeline#pipe(musaico.foundation.term.Operation)
     */
    @Override
    public final PIPELINE pipe (
            Operation<VALUE, VALUE> operation
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave.pipe ( operation );
        return this.thisPipeline ();
    }


    /**
     * @see musaico.foundation.term.TermPipeline#pipe(musaico.foundation.term.OperationPipeline)
     */
    @Override
    public final PIPELINE pipe (
            OperationPipeline.OperationTap<VALUE> pipeline
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().pipe ( pipeline );
        return this.thisPipeline ();
    }


    /**
     * @return The pipeline doing all the work for this
     *         AbstractTermPipeline.  Never null.
     */
    protected SLAVE slave ()
        throws ReturnNeverNull.Violation
    {
        return this.slave;
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#select()
     */
    @Override
    public final Select<VALUE, PIPELINE> select ()
        throws ReturnNeverNull.Violation
    {
        return new TermSelect<VALUE, PIPELINE> (
                       this.thisPipeline (),   // parent
                       this.slave.select () ); // slave
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#thisPipeline()
     */
    @Override
    @SuppressWarnings("unchecked") // We checked that this is an instance
        // of PIPELINE during the constructor.
    public final PIPELINE thisPipeline ()
        throws ReturnNeverNull.Violation
    {
        return (PIPELINE) this;
    }


    /**
     * @see java.lang.Object#toString()
     *
     * Can be overridden (by TermWhen, and so on).
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " { " + this.slave + " }";
    }


    /**
     * @see musaico.foundation.term.TermPipeline#type()
     */
    @Override
    public final Type<VALUE> type ()
        throws ReturnNeverNull.Violation
    {
        return this.inputSource ().type ();
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#when(musaico.foundation.filter.Filter)
     */
    @Override
    public final TermWhen<VALUE, PIPELINE> when (
            Filter<?> condition
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               condition );

        final TermWhen<VALUE, PIPELINE> conditional =
            new TermWhen<VALUE, PIPELINE> (
                this.thisPipeline (),          // parent
                this.slave.when ( condition ), // slave
                null );                        // previous

        return conditional;
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#where(musaico.foundation.filter.Filter[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public final Where<VALUE, PIPELINE> where (
            Filter<VALUE> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        return new TermWhere<VALUE, PIPELINE> (
            this.thisPipeline (),           // parent
            this.slave.where ( filters ) ); // slave
    }
}
