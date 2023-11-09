package musaico.foundation.operation;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.term.Term;
import musaico.foundation.term.Type;

import musaico.foundation.term.countable.No;


/**
 * <p>
 * Provides boilerplate code for Streams.
 * </p>
 *
 *
 * <p>
 * In Java, every Stream must implement equals (), hashCode ()
 * and toString().
 * </p>
 *
 * <p>
 * In Java every Stream must be Serializable in order to
 * play nicely across RMI.  However users of Streams should be cautious,
 * because Terms which might be stored inside, although Serializable
 * themselves, can contain non-Serializable elements.
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
public class NoStream<VALUE>
    implements Stream<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( NoStream.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // The upstream Pipe from which this Stream requests inputs.
    // Never changes, since we never read from it.
    private final Pipe<?, VALUE> inputPipe;

    // No more Terms are available to be read from this Stream,
    // so it is time for the downstream Pipe to close this stream.
    private final No<Term<VALUE>> eof;


    /**
     * <p>
     * Creates a new NoStream.
     * </p>
     *
     * @param input_pipe The upstream Pipe to read from.
     *                   Must not be null.
     */
    public NoStream (
            Pipe<?, VALUE> input_pipe
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input_pipe );

        this.inputPipe = input_pipe;

        final Type<VALUE> type = this.inputPipe.downstreamType ();
        final Type<Term<VALUE>> term_type =
            type.enclose ( Term.class,
                           (Term<VALUE>)
                           new No<VALUE> ( type ) )
            .buildType ();
        this.eof = new No<Term<VALUE>> ( term_type );

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.operation.Stream#close()
     */
    public final void close ()
    {
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * Can be overridden.
     */
    @Override
    public boolean equals (
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
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final NoStream<?> that = (NoStream<?>) object;

        // Immutable input pipes:
        if ( this.inputPipe == null )
        {
            if ( that.inputPipe != null )
            {
                return false;
            }
        }
        else if ( that.inputPipe == null )
        {
            return false;
        }
        else if ( ! this.inputPipe.equals ( that.inputPipe ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.operation.Stream#firstReadTimeSeconds ()
     */
    @Override
    public final BigDecimal firstReadTimeSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return BigDecimal.ZERO;
    }


    /**
     * @see musaico.foundation.operation.Stream#firstWriteTimeSeconds ()
     */
    @Override
    public final BigDecimal firstWriteTimeSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return BigDecimal.ZERO;
    }


    /**
     * @see java.lang.Object#hashCode()
     *
     * Can be overridden.
     */
    @Override
    public int hashCode ()
    {
        return 17 * this.getClass ().getName ().hashCode ()
            + ( this.inputPipe == null
                    ? 0 // constructor time.
                    : this.inputPipe.hashCode () );
    }


    /**
     * @see musaico.foundation.operation.Stream#lastReadTimeSeconds ()
     */
    @Override
    public final BigDecimal lastReadTimeSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return BigDecimal.ZERO;
    }


    /**
     * @see musaico.foundation.operation.Stream#lastWriteTimeSeconds ()
     */
    @Override
    public final BigDecimal lastWriteTimeSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return BigDecimal.ZERO;
    }


    /**
     * @see musaico.foundation.operation.Stream#length()
     */
    @Override
    public final long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return 0L;
    }


    /**
     * @see musaico.foundation.operation.Stream#nextElement(musaico.foundation.operation.Context)
     */
    @Override
    public final No<VALUE> nextElement (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        return new No<VALUE> ( this.inputPipe.downstreamType () );
    }


    /**
     * @see musaico.foundation.operation.Stream#nextIndex()
     */
    @Override
    public final long nextIndex ()
        throws Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        return -1L;
    }


    /**
     * @see musaico.foundation.operation.Stream#numTerms()
     */
    @Override
    public final long numTerms ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return 0L;
    }


    /**
     * @see musaico.foundation.operation.Stream#read(musaico.foundation.operation.Context)
     */
    @Override
    public final No<Term<VALUE>> read (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        return this.eof;
    }


    /**
     * @see musaico.foundation.operation.Stream#type()
     */
    @Override
    public final Stream.State state ()
        throws ReturnNeverNull.Violation
    {
        return Stream.State.CLOSED;
    }


    /**
     * @see java.lang.Object#toString()
     *
     * Can be overridden.
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.foundation.operation.Stream#type()
     */
    @Override
    public final Type<VALUE> type ()
        throws ReturnNeverNull.Violation
    {
        return this.inputPipe.downstreamType ();
    }


    /**
     * @see musaico.foundation.operation.Stream#write(musaico.foundation.term.Term)
     */
    @Override
    public final Stream.State write (
            Term<VALUE> term
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return Stream.State.CLOSED;
    }
}
