package musaico.foundation.operations;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;
import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;
import musaico.foundation.term.UncheckedTermViolation;

import musaico.foundation.term.countable.No;

import musaico.foundation.term.pipeline.AbstractTerm;


/**
 * <p>
 * A Pipe inside a Context, behaving like a Term.
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
 * @see musaico.foundation.operations.MODULE#COPYRIGHT
 * @see musaico.foundation.operations.MODULE#LICENSE
 */
public class PipeInContext<VALUE extends Object>
    extends AbstractTerm<VALUE>
    implements Term<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( PipeInContext.class );


    // !!!
    private final Pipe<VALUE> pipe;

    // !!!
    private final Context context;


    /**
     * <p>
     * Creates a new PipeInContext.
     * </p>
     *
     * !!!
     */
    public PipeInContext (
            Pipe<VALUE> pipe,
            Context context
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( pipe == null // type_pipeline
                    ? null
                    : pipe.type ( context ).refine () );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               pipe, context );

        this.pipe = pipe;
        this.context = context;
    }


    /**
     * @see musaico.foundation.term.Multiplicity#countable()
    */
    @Override
    public final Countable<VALUE> countable ()
        throws ReturnNeverNull.Violation
    {
        // !!!
        return this.pipe.read ( this.context ).countable ();
    }


    /**
     * @see musaico.foundation.term.TermPipeline#duplicate()
     */
    @Override
    public final PipeInContext<VALUE> duplicate ()
        throws ReturnNeverNull.Violation
    {
        return new PipeInContext<VALUE> (
                       this.pipe,
                       this.context );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final PipeInContext<?> that = (PipeInContext<?>) object;
        if ( this.pipe == null )
        {
            if ( that.pipe != null )
            {
                return false;
            }
        }
        else if ( that.pipe == null )
        {
            return false;
        }
        else if ( ! this.pipe.equals ( that.pipe ) )
        {
            return false;
        }

        if ( this.context == null )
        {
            if ( that.context != null )
            {
                return false;
            }
        }
        else if ( that.context == null )
        {
            return false;
        }
        else if ( ! this.context.equals ( that.context ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.term.Multiplicity#filterState()
     */
    @Override
    public final FilterState filterState ()
        throws ReturnNeverNull.Violation
    {
        // !!!
        return this.pipe.read ( this.context ).filterState ();
    }



    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 31 * this.pipe.hashCode ()
            + 7 * this.getClass ().getName ().hashCode ()
            + this.context.hashCode ();
    }


    /**
     * @see musaico.foundation.term.Multiplicity#head()
     */
    @Override
    public final Maybe<VALUE> head ()
        throws ReturnNeverNull.Violation
    {
        // !!!
        return this.pipe.read ( this.context ).head ();
    }


    /**
     *@see musaico.foundation.term.Multiplicity#head(long)
     */
    @Override
    public final Countable<VALUE> head (
            long num_elements
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        if ( num_elements <= 0L
             || num_elements > (long) Integer.MAX_VALUE )
        {
            return new No<VALUE> ( this.type () );
        }

        // !!!
        return this.pipe.read ( this.context ).head ( num_elements );
    }


    /**
     * @see musaico.foundation.term.Multiplicity#indefiniteIterator(long)
     */
    @Override
    public final Iterator<VALUE> indefiniteIterator (
            long maximum_iterations
            )
        throws Parameter1.MustBeGreaterThanZero.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        // !!!
        return this.pipe.read ( this.context ).indefiniteIterator ( maximum_iterations );
    }


    /**
     *@see java.lang.Iterable#iterator()
     */
    @Override
    public final Iterator<VALUE> iterator ()
    {
        // !!!
        return this.pipe.read ( this.context ).iterator ();
    }


    /**
     * @see musaico.foundation.term.Multiplicity#orNull()
     */
    @Override
    public final VALUE orNull ()
        throws ReturnNeverNull.Violation
    {
        // !!!
        return this.pipe.read ( this.context ).orNull ();
    }


    /**
     * @see musaico.foundation.term.Multiplicity#orThrowChecked()
     */
    @Override
    public final VALUE orThrowChecked ()
        throws TermViolation,
               ReturnNeverNull.Violation
    {
        // !!!
        return this.pipe.read ( this.context ).orThrowChecked ();
    }


    /**
     * @see musaico.foundation.term.Multiplicity#orThrowUnchecked()
     */
    @Override
    public final VALUE orThrowUnchecked ()
        throws UncheckedTermViolation,
               ReturnNeverNull.Violation
    {
        // !!!
        return this.pipe.read ( this.context ).orThrowUnchecked ();
    }


    /**
     * @see java.lang.Object#toString()
     *
     * Can be overridden by derived classes.
     */
    @Override
    public String toString ()
    {
        return "" + this.pipe + " given " + this.context;
    }
}
