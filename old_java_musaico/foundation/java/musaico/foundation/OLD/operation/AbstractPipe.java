package musaico.foundation.operation;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Finite;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;

import musaico.foundation.term.builder.TermBuilder;

import musaico.foundation.term.countable.No;
import musaico.foundation.term.countable.One;

import musaico.foundation.term.multiplicities.Infinite;


/**
 * <p>
 * Boilerplate code for Pipes.
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
public abstract class AbstractPipe<FROM extends Object, TO extends Object>
    implements Pipe<FROM, TO>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( AbstractPipe.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // Synchronize critical sections on this token:
    private final Serializable lock = new String ( "lock" );

    // The upstream Pipe(s), from which we read Term(s).
    private final Countable<Pipe<?, FROM>> inputPipes;

    // The Type of Term(s) to write downstream.
    private final Type<TO> downstreamType;


    /**
     * <p>
     * Creates a new AbstractPipe.
     * </p>
     *
     * @param input_pipes The upstream Pipe(s), from which we read Term(s).
     *                    Can be NoPipe, if this is the start of a line
     *                    (providing constant Term(s), or reading from
     *                    the outside world, and so on).  Must not be null.
     *
     * @param downstream_type The Type of Term(s) to write downstream.
     *                        Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast Count<PI, F> - Count<?, F>.
    public <PIPE_IN extends Object>
        AbstractPipe (
            Countable<Pipe<PIPE_IN, FROM>> input_pipes,
            Type<TO> downstream_type
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input_pipes, downstream_type );

        this.inputPipes = (Countable<Pipe<?, FROM>>)
            ( (Countable<?>) input_pipes ) ;
        this.downstreamType = downstream_type;

        this.contracts = new Advocate ( this );
    }


    /**
     * @return This Pipe's Advocate, which enforces parameter obligations
     *         and return value guarantees and so on.  Never null.
     */
    protected final Advocate contracts ()
        throws ReturnNeverNull.Violation
    {
        return this.contracts;
    }


    /**
     * @see musaico.foundation.operation.Pipe#downstreamReadType()
     */
    @Override
    public final Type<Term<TO>> downstreamReadType ()
        throws ReturnNeverNull.Violation
    {
        return this.downstreamType.enclose (
                Term.class,                           // container_class
                (Term<TO>)
                new No<TO> ( this.downstreamType ) )  // none
            .buildType ();
    }


    /**
     * @see musaico.foundation.operation.Pipe#downstreamType()
     */
    @Override
    public Type<TO> downstreamType ()
        throws ReturnNeverNull.Violation
    {
        return this.downstreamType;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * Can be overridden.
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

        final AbstractPipe<?, ?> that = (AbstractPipe<?, ?>) object;

        if ( this.inputPipes == null )
        {
            if ( that.inputPipes != null )
            {
                return false;
            }
        }
        else if ( that.inputPipes == null )
        {
            return false;
        }
        else if ( ! this.inputPipes.equals ( that.inputPipes ) )
        {
            return false;
        }

        if ( this.downstreamType == null )
        {
            if ( that.downstreamType != null )
            {
                return false;
            }
        }
        else if ( that.downstreamType == null )
        {
            return false;
        }
        else if ( ! this.downstreamType.equals ( that.downstreamType ) )
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
        return this.getClass ().getName ().hashCode ();
    }


    // Every Pipe must implement
    // musaico.foundation.operation.Pipe#step(musaico.foundation.operation.Context)


    /**
     * @see musaico.foundation.operation.Pipe#inputPipes()
     */
    @Override
    public final Countable<Pipe<?, FROM>> inputPipes ()
        throws ReturnNeverNull.Violation
    {
        return this.inputPipes;
    }


    /**
     * @see java.lang.Object#toString()
     *
     * Can be overridden
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.foundation.operation.Pipe#upstreamType()
     */
    @Override
    public final Type<FROM> upstreamType ()
        throws ReturnNeverNull.Violation
    {
        return this.inputPipes.at ( 0L ).orNone ().downstreamType ();
    }


    /**
     * <p>
     * Writes the specified Term(s) to the specified downstream Stream(s).
     * </p>
     *
     * @param downstreams The Stream(s) to which the Term(s) will be written.
     *                    Each one will receive the same output.
     *                    Must not be null.
     *
     * @param terms The one or more Term(s) to write downstream.
     *              Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings("varargs") // Possible heap pollution generic varargs.
    @SafeVarargs // Possible heap pollution generic varargs.
    protected static final <OUT extends Object>
        Stream.State write (
            Term<Stream<OUT>> downstreams,
            Term<OUT> ... terms
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               Parameter2.Length.MustBeGreaterThanZero.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               downstreams, terms );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               terms );
        classContracts.check ( Parameter2.Length.MustBeGreaterThanZero.CONTRACT,
                               terms );

        final Iterator<Stream<OUT>> downstream_iterator;
        if ( downstreams instanceof Finite )
        {
            downstream_iterator = downstreams.iterator ();
        }
        else if ( downstreams instanceof Infinite )
        {
            final Infinite<Stream<OUT>> infinite =
                (Infinite<Stream<OUT>>) downstreams;
            downstream_iterator = infinite.infiniteIterator ();
        }
        else
        {
            downstream_iterator =
                downstreams.indefiniteIterator ( Long.MAX_VALUE );
        }

        Stream.State overall_state = Stream.State.CLOSED;
        while ( downstream_iterator.hasNext () )
        {
            final Stream<OUT> stream = downstream_iterator.next ();

            Stream.State state = Stream.State.OPEN;
            for ( Term<OUT> term : terms )
            {
                state = stream.write ( term );
                if ( state != Stream.State.OPEN )
                {
                    break;
                }
            }

            if ( state == Stream.State.OPEN )
            {
                overall_state = Stream.State.OPEN;
            }
        }

        return overall_state;
    }
}
