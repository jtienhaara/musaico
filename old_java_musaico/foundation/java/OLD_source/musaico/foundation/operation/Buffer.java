package musaico.foundation.operation;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;

import musaico.foundation.term.countable.One;


/**
 * <p>
 * A buffered Stream of Terms.
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
public class Buffer<VALUE>
    extends AbstractStream<VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( Buffer.class );


    // Lock critical read sections on this token.
    // Warning: never lock the readLock and then induce read ()
    // or next () in another thread.  Never lock the readLock
    // inside a synchronized ( writeLock ) block.
    private final Serializable readLock = new String ( "read_lock" );

    // Lock critical write sections on this token.
    // Warning: never lock the writeLock and then induce write ( ... )
    // in another thread.  Note that calling
    // AbstractStream.readFromInputPipe () could conceivably
    // induce write ( ... ) in another thread.
    private final Serializable writeLock = new String ( "write_lock" );

    // MUTABLE contents:
    // Use writeLock to access.
    // The Terms written to this Buffer so far by the upstream Pipe.
    private final List<Term<VALUE>> queue;

    // MUTABLE:
    // Use readLock to access.
    // The index of the next Term to be read from this Buffer.
    private int nextReadIndex = 0;

    // MUTABLE:
    // Use writeLock to access.
    // The time in seconds UTC (since midnight January 1st 1970 GMT)
    // at which this Stream received its first write request,
    private BigDecimal firstWriteTimeSeconds = BigDecimal.ZERO;

    // MUTABLE:
    // Use writeLock to access.
    // The time in seconds UTC (since midnight January 1st 1970 GMT)
    // at which this Stream most recently received a write request,
    private BigDecimal lastWriteTimeSeconds = BigDecimal.ZERO;


    /**
     * <p>
     * Creates a new Buffer.
     * </p>
     *
     * @param input_pipe The upstream Pipe to read from.
     *                   Must not be null.
     */
    public Buffer (
            Pipe<?, VALUE> input_pipe
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( input_pipe );

        this.queue = new ArrayList<Term<VALUE>> ();
        this.nextReadIndex = 0;
    }


    /**
     * <p>
     * Creates a new Buffer.
     * </p>
     *
     * @param input_pipe The upstream Pipe to read from.
     *                   Must not be null.
     *
     * @param streams_to_write_to The Stream(s) to which the upstream
     *                            Pipe will write Term(s).
     *                            Must not be null.
     */
    public Buffer (
            Pipe<?, VALUE> input_pipe,
            Term<Stream<VALUE>> streams_to_write_to
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( input_pipe );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               streams_to_write_to );

        this.queue = new ArrayList<Term<VALUE>> ();
        this.nextReadIndex = 0;
    }


    /**
     * @return The number of Term(s) currently available for reading
     *         from the Buffer.  There might be more in future, after
     *         call(s) to read ().  Always greater than or equal to 0.
     *
     * WARNING: Do NOT call this method internally from with a block
     *          synchronized on the writeLock.
     *          You CAN call from within a block synchronized
     *          on the readlock.
     */
    public final int available ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        final int queue_size;
        final int next_read_index;
        synchronized ( this.readLock )
        {
            next_read_index = this.nextReadIndex;

            synchronized ( this.writeLock )
            {
                queue_size = this.queue.size ();
            }
        }

        final int available = queue_size - next_read_index - 1;
        return available;
    }


    /**
     * @see musaico.foundation.operation.Stream#firstWriteTimeSeconds ()
     */
    @Override
    public final BigDecimal firstWriteTimeSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        synchronized ( this.writeLock )
        {
            return this.firstWriteTimeSeconds;
        }
    }


    /**
     * @see musaico.foundation.operation.Stream#lastWriteTimeSeconds ()
     */
    @Override
    public final BigDecimal lastWriteTimeSeconds ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        synchronized ( this.writeLock )
        {
            return this.lastWriteTimeSeconds;
        }
    }


    // Returns the index of the next Term which will be returned by read (),
    // or -1 if the downstream Pipe will not read any more Terms.
    // Synchronizes on readLock.
    // DO NOT CALL THIS METHOD INSIDE A synchronized ( this.writeLock ) BLOCK!
    private final int nextReadIndex ()
    {
        synchronized ( this.readLock )
        {
            return this.nextReadIndex;
        }
    }


    // Returns the current # of Terms in the buffer.
    // Synchronizes on writeLock.
    private final int queueSize ()
    {
        synchronized ( this.writeLock )
        {
            return this.queue.size ();
        }
    }


    /**
     * @see musaico.foundation.operation.AbstractStream#readInternal(musaico.foundation.operation.Context)
     */
    @Override
    protected final Maybe<Term<VALUE>> readInternal (
            Context context
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  context );

        final int read_index;
        synchronized ( this.readLock )
        {
            if ( this.nextReadIndex < 0 ) // Downstream is done reading.
            {
                return this.eof ();
            }

            if ( this.nextReadIndex >= this.queueSize () )
            {
                this.readFromInputPipe ( context );
                if ( this.nextReadIndex >= this.queueSize () )
                {
                    // Upstream did not feed us any terms.
                    if ( this.state () != Stream.State.OPEN )
                    {
                        // We're done.
                        this.nextReadIndex = -1;
                    }

                    return this.eof ();
                }
            }

            read_index = this.nextReadIndex;
            this.nextReadIndex ++;
        }

        final Term<VALUE> term;
        synchronized ( this.writeLock )
        {
            term = this.queue.get ( read_index );
        }

        return new One<Term<VALUE>> ( this.termType (),
                                      term  );
    }


    /**
     * <p>
     * Resets the cursor for this Buffer to point at the start
     * of input Terms and the first element.
     * </p>
     */
    public void reset ()
    {
        synchronized ( this.readLock )
        {
            this.nextReadIndex = 0;
        }

        super.resetLengthNextIndexNumTerms ();
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
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  term );

        if ( this.state () == Stream.State.CLOSED )
        {
            return Stream.State.CLOSED;
        }

        if ( this.nextReadIndex () < 0 ) // Downstream is done reading.
        {
            this.close ();
            return Stream.State.CLOSED;
        }

        synchronized ( this.writeLock )
        {
            final BigDecimal current_time = this.timeInSeconds ();
            if ( this.firstWriteTimeSeconds == BigDecimal.ZERO )
            {
                this.firstWriteTimeSeconds = current_time;
            }
            this.lastWriteTimeSeconds = current_time;

            this.queue.add ( term );
        }

        if ( ! ( term instanceof Countable ) )
        {
            // After an Infinite term or an Error and so on,
            // no more Terms can be accepted into this Buffer.
            this.close ();
            return Stream.State.CLOSED;
        }

        return Stream.State.OPEN;
    }
}
