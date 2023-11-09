package musaico.foundation.operation;

import java.io.Serializable;

import java.util.Iterator;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Finite;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.contracts.TermMustMeetAllContracts;
import musaico.foundation.term.contracts.TermMustMeetAtLeastOneContract;
import musaico.foundation.term.contracts.ValueMustBeCountable;

import musaico.foundation.term.countable.Many;
import musaico.foundation.term.countable.No;
import musaico.foundation.term.countable.One;

import musaico.foundation.term.infinite.Cyclical;
import musaico.foundation.term.infinite.CyclicalCycleMustMeet;
import musaico.foundation.term.infinite.CyclicalHeaderMustMeet;
import musaico.foundation.term.infinite.TermMustBeCyclical;

import musaico.foundation.term.multiplicities.OneOrMore;


/**
 * <p>
 * A Pipe from a single Term.
 * </p>
 *
 *
 * <p>
 * In Java, every Pipe must implement equals (), hashCode ()
 * and toString().
 * </p>
 *
 * <p>
 * In Java every Pipe must be Serializable in order to
 * play nicely across RMI.  However in general it is recommended
 * that only stateless Pipes be passed around over RMI, and even
 * then, do so with caution.  The Terms which feed inputs
 * to Pipes, although Serializable themselves,
 * can contain non-Serializable elements.
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
 * @see musaico.foundation.operation.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.MODULE#LICENSE
 */
public class TermPipe<VALUE extends Object>
    implements Pipe<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( TermPipe.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // The Term to send downstream.
    private final Term<VALUE> term;


    /**
     * <p>
     * Creates a new TermPipe.
     * </p>
     *
     * @param term The Term to send downstream.  Must not be null.
     */
    public TermPipe (
            Term<VALUE> term
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               term );

        this.term = term;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.operation.Pipe#downstreamReadType()
     */
    @Override
    public final Type<Term<VALUE>> downstreamReadType ()
        throws ReturnNeverNull.Violation
    {
        final Type<Term<VALUE>> downstream_read_type =
            this.term.type ().enclose (
                Term.class,
                (Term<VALUE>) new No<VALUE> ( this.term.type () ) )
            .buildType ();
        return downstream_read_type;
    }


    /**
     * @see musaico.foundation.operation.Pipe#downstreamType()
     */
    @Override
    public Type<VALUE> downstreamType ()
        throws ReturnNeverNull.Violation
    {
        return this.term.type ();
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

        final TermPipe<?> that = (TermPipe<?>) object;

        if ( this.term == null )
        {
            if ( that.term != null )
            {
                return false;
            }
        }
        else if ( that.term == null )
        {
            return false;
        }
        else if ( ! this.term.equals ( that.term ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 37 * this.getClass ().getName ().hashCode ()
            + 23 * ( this.term == null
                         ? 0
                         : this.term.hashCode () );
    }


    /**
     * @see musaico.foundation.operation.Pipe#inputPipes()
     */
    @Override
    @SuppressWarnings("unchecked") // Check VALUE type = VALUE type then cast
        // Pipe<VALUE, VALUE> to Pipe<VALUE, VALUE>.
    public final Countable<Pipe<?, VALUE>> inputPipes ()
        throws ReturnNeverNull.Violation
    {
        final NoPipe<VALUE, VALUE> no_pipe =
            new NoPipe<VALUE, VALUE> ( this.term.type (),
                                       this.term.type () );

        final Type<Pipe<?, VALUE>> input_pipes_type =
            (Type<Pipe<?, VALUE>>) ( (Type<?>)
                this.term.type ().enclose ( Pipe.class, // container_class
                                            no_pipe )   // none
                .buildType ()
            );
        final No<Pipe<?, VALUE>> input_pipes =
            new No<Pipe<?, VALUE>> ( input_pipes_type );
        return input_pipes;
    }


    /**
     * @see musaico.foundation.operation.Pipe#maximumOutputPipes()
     */
    @Override
    public final long maximumOutputPipes ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return Long.MAX_VALUE;
    }


    /**
     * @see musaico.foundation.operation.Pipe#minimumOutputPipes()
     */
    @Override
    public final long minimumOutputPipes ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return 1L;
    }


    /**
     * @see musaico.foundation.operation.Pipe#step(musaico.foundation.operation.Context)
     */
    @Override
    public final boolean step (
            Context context
            )
        throws ParametersMustNotBeNull.Violation
    {
        // Write the same Term out to every Stream (if more than one),
        // then we're done.
        // Throws ParametersMustNotBeNull.Violation:
        AbstractPipe.write ( context.outputs ( this ), // downstreams
                             this.term ); // terms

        return false;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();

        sbuf.append ( ClassName.of ( this.getClass () ) );
        sbuf.append ( ": " + this.term );

        return sbuf.toString ();
    }


    /**
     * @see musaico.foundation.operation.Pipe#upstreamType()
     */
    @Override
    public final Type<VALUE> upstreamType ()
        throws ReturnNeverNull.Violation
    {
        return this.term.type ();
    }
}
