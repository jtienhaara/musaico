package musaico.foundation.operation;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.Seconds;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;

import musaico.foundation.term.countable.No;
import musaico.foundation.term.countable.One;


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
public abstract class AbstractStream<VALUE>
    implements Stream<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( AbstractStream.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // Lock critical sections on this token:
    private final Serializable lock = new String ( "lock" );

    // The upstream Pipe from which this Stream requests inputs
    // by calling its step ( ... ) method repeatedly until either
    // the downstream sink Pipe tells us to close,
    // or the upstream source Pipe tells us that stepping is done
    // and there are no more inputs to be had.
    private final Pipe<?, VALUE> inputPipe;

    // MUTABLE:
    // The current state of this stream.  OPEN until the derived class
    // decides to change it to the CLOSED state.
    private Stream.State state = Stream.State.OPEN;

    // MUTABLE:
    // The number of elements read () in so far (0L - Long.MAX_VALUE).
    private long length = 0L;

    // MUTABLE:
    // The number of Terms read () in so far (0L - Long.MAX_VALUE).
    private long numTerms = 0L;

    // The Type of Terms flowing through this Stream.
    private final Type<VALUE> type;

    // MUTABLE:
    // Iterates through the elements of one of the Terms in the queue,
    // or null if there is not currently an Iterator for any Term in the queue.
    private Iterator<VALUE> elementsIterator = null;

    // MUTABLE:
    // The index of the nextElement() (-1L - Long.MAX_VALUE).
    private long nextIndex = 0L;

    // No more Terms are available to be read from this Stream,
    // so it is time for the downstream Pipe to close this stream.
    private final No<Term<VALUE>> eof;

    // MUTABLE:
    // The time in seconds UTC (since midnight January 1st 1970 GMT)
    // at which this Stream received its first read request,
    private BigDecimal firstReadTimeSeconds = BigDecimal.ZERO;

    // MUTABLE:
    // The time in seconds UTC (since midnight January 1st 1970 GMT)
    // at which this Stream most recently received a read request,
    private BigDecimal lastReadTimeSeconds = BigDecimal.ZERO;


    /**
     * <p>
     * Creates a new AbstractStream.
     * </p>
     *
     * @param input_pipe The upstream Pipe to read from.
     *                   Must not be null.
     */
    public AbstractStream (
            Pipe<?, VALUE> input_pipe
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input_pipe );

        this.inputPipe = input_pipe;

        this.type = this.inputPipe.downstreamType ();

        final Type<Term<VALUE>> term_type =
            input_pipe.downstreamReadType ();
        this.eof = new No<Term<VALUE>> ( term_type );

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.operation.Stream#close()
     *
     * Can be overridden.
     */
    public void close ()
    {
        synchronized ( this.lock )
        {
            this.state = Stream.State.CLOSED;
        }
    }


    /**
     * @return This Stream's Advocate, which enforces parameter obligations
     *         and return value guarantees and so on.  Never null.
     */
    protected final Advocate contracts ()
        throws ReturnNeverNull.Violation
    {
        return this.contracts;
    }


    /**
     * @return The "no more Terms to read from this Stream" term,
     *         which is returned from <code> read () </code>
     *         when the upstream Pipe no longer has any output,
     *         or when this Stream has been closed, and so on.
     *         Never null.
     */
    public final Maybe<Term<VALUE>> eof ()
        throws ReturnNeverNull.Violation
    {
        return this.eof;
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

        final AbstractStream<?> that = (AbstractStream<?>) object;

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
        synchronized ( this.lock )
        {
            return this.firstReadTimeSeconds;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     *
     * Can be overridden.
     */
    @Override
    public int hashCode ()
    {
        return this.getClass ().getName ().hashCode ();
    }


    /**
     * @see musaico.foundation.operation.Stream#lastReadTimeSeconds ()
     */
    @Override
    public final BigDecimal lastReadTimeSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        synchronized ( this.lock )
        {
            return this.lastReadTimeSeconds;
        }
    }


    /**
     * @see musaico.foundation.operation.Stream#length()
     */
    @Override
    public final long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        synchronized ( this.lock )
        {
            return this.length;
        }
    }


    /**
     * @see musaico.foundation.operation.Stream#nextElement(musaico.foundation.operation.Context)
     */
    @Override
    public final Maybe<VALUE> nextElement (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        final VALUE element;
        synchronized ( this.lock )
        {
            // The read times will be updated the first time
            // we get here, during this.read ( ... ).

            if ( this.elementsIterator == null )
            {
                int infinite_loop_protector = 0;
                Term<VALUE> next_term =
                    this.read ( context )
                        .orNull ();
                while ( next_term != null
                        && infinite_loop_protector < 1024 )
                {
                    if ( next_term.hasValue () )
                    {
                        break;
                    }

                    next_term =
                        this.read ( context )
                            .orNull ();

                    infinite_loop_protector ++;
                }

                if ( next_term == null )
                {
                    this.nextIndex = -1L;
                    return this.noElement ();
                }

                this.elementsIterator = next_term.iterator ();
            }

            element = this.elementsIterator.next ();
            this.nextIndex ++;
        }

        final One<VALUE> one_element =
            new One<VALUE> ( this.type,
                             element );
        return one_element;
    }


    /**
     * @see musaico.foundation.operation.Stream#nextIndex()
     */
    @Override
    public final long nextIndex ()
        throws Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        synchronized ( this.lock )
        {
            return this.nextIndex;
        }
    }


    /**
     * @return No element at all.  Never null.
     */
    protected final No<VALUE> noElement ()
    {
        return new No<VALUE> ( this.type );
    }


    /**
     * @see musaico.foundation.operation.Stream#numTerms()
     */
    @Override
    public final long numTerms ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        synchronized ( this.lock )
        {
            return this.numTerms;
        }
    }


    /**
     * @see musaico.foundation.operation.Stream#read(musaico.foundation.operation.Context)
     */
    @Override
    public final Maybe<Term<VALUE>> read (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        synchronized ( this.lock )
        {
            final BigDecimal current_time = this.timeInSeconds ();
            if ( this.firstReadTimeSeconds == BigDecimal.ZERO )
            {
                this.firstReadTimeSeconds = current_time;
            }
            this.lastReadTimeSeconds = current_time;

            final Maybe<Term<VALUE>> maybe_term =
                this.readInternal ( context );
            final Term<VALUE> term = maybe_term.orNull ();
            if ( term != null )
            {
                if ( this.numTerms < Long.MAX_VALUE )
                {
                    this.numTerms ++;
                }

                if ( term instanceof Countable )
                {
                    final Countable<VALUE> countable = (Countable<VALUE>) term;
                    final long add_length = countable.length ();
                    if ( add_length > ( Long.MAX_VALUE - this.length ) )
                    {
                        this.length = Long.MAX_VALUE;
                    }
                    else
                    {
                        this.length += add_length;
                    }
                }
                else if ( term.hasValue () )
                {
                    // Assume infinite.
                    this.length = Long.MAX_VALUE;
                }
                // Else the Term has no elements, such as an Error,
                // so length does not change.
            }

            return maybe_term;
        }
    }


    /**
     * <p>
     * Reads the next Term from the input Pipe, if possible.
     * </p>
     *
     * <p>
     * This method performs an iterative or recursive step on the input Pipe,
     * if possible, in order to generate the next Term.
     * The input Pipe, in turn, calls <code> write ( ... ) </code>
     * on this Stream with the next Term (if any).
     * </p>
     *
     * @param context The Context in which the stream data will be read
     *                from the Pipe.  This Stream must be part of the
     *                specified Context's graph of Pipes and Streams
     *                (though no contract enforcement is done, to
     *                minimize overhead).  Must not be null.
     */
    protected void readFromInputPipe (
            Context context
            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        synchronized ( this.lock )
        {
            if ( this.state != Stream.State.OPEN ) // Upstream is done writing.
            {
                return;
            }

            final boolean is_still_processing =
                this.inputPipe.step ( context );
            if ( ! is_still_processing )
            {
                this.close ();
            }
        }
    }


    /**
     * <p>
     * Reads the next Term from this Stream, if any; or performs
     * one step () on the input Pipe to generate the next Term,
     * then returns it; or returns No Term at all, if there are
     * no more Terms to be read.
     * </p>
     *
     * @param context The Context in which the stream data will be read
     *                from the Pipe.  This Stream must be part of the
     *                specified Context's graph of Pipes and Streams
     *                (though no contract enforcement is done, to
     *                minimize overhead).  Must not be null.
     *
     * @return The One next Term, if possible; or No next Term, if this
     *         Stream cannot return any more Terms.  Never null.
     */
    protected abstract Maybe<Term<VALUE>> readInternal (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Resets the length, numTerms and nextIndex to 0L.
     * </p>
     *
     * <p>
     * Used, for example, by Buffer.reset ().
     * </p>
     */
    protected void resetLengthNextIndexNumTerms ()
    {
        synchronized ( this.lock )
        {
            this.length = 0L;
            this.nextIndex = 0L;
            this.numTerms = 0L;
        }
    }


    /**
     * @see musaico.foundation.operation.Stream#type()
     */
    @Override
    public final Stream.State state ()
        throws ReturnNeverNull.Violation
    {
        synchronized ( this.lock )
        {
            return this.state;
        }
    }


    /**
     * @return The Type of Maybe-Term returned to downstream Pipes
     *         by this Stream's <code> read () </code>.  Never null.
     */
    public final Type<Term<VALUE>> termType ()
        throws ReturnNeverNull.Violation
    {
        return this.eof.type ();
    }


    /**
     * @return The current system time, in seconds UTC.  The default
     *         implementation returns System.currentTimeMillis (),
     *         converted to seconds.  Never null.
     */
    protected BigDecimal timeInSeconds ()
        throws ReturnNeverNull.Violation
    {
        final long milliseconds = System.currentTimeMillis ();
        final BigDecimal seconds =
            new BigDecimal ( milliseconds )
            .multiply ( Seconds.PER_MILLISECOND );

        return seconds;
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
        return this.type;
    }


    // Every Stream must implement
    // musaico.foundation.operation.Stream#write(musaico.foundation.term.Term)
}
