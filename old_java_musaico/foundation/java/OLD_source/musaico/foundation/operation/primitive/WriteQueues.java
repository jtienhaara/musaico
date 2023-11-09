package musaico.foundation.operation.primitive;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.operation.Stream;

import musaico.foundation.term.Term;


/**
 * <p>
 * A lookup of Terms queued up to be written downstream to various
 * output Streams.
 * </p>
 *
 * <p>
 * Terms can be queued up for eventual output by calling
 * <code> enqueue ( output_stream, term ) </code>.
 * </p>
 *
 * <p>
 * Depending on the configuration of the WriteQueues, each Term
 * will be written to its destination immediately (<code> IMMEDIATE </code>)
 * or whenever the <code> writeAll () </code> method is invoked
 * (<code> QUEUED </code>).
 * </p>
 *
 *
 * <p>
 * In Java every WriteQueues must be Serializable in order to play
 * nicely over RMI.
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
 * @see musaico.foundation.operation.primitive.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.primitive.MODULE#LICENSE
 */
public class WriteQueues<OUTPUT extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * The type of queues determines whether the Terms are written
     * downstream as soon as they are queued up, or later, when
     * <code> writeAll () </code> is invoked, and so on.
     * </p>
     */
    public static enum Type
    {
        /** Do not queue up any Terms, simply output them as soon
         *  as they are enqueued. */
        IMMEDIATE,

        /** Queue up Terms for eventual output
         *  via <code> writeAll () </code>. */
        QUEUED;
    }


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( WriteQueues.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // Lock critical sections on this token:
    private final Serializable lock = new String ( "lock" );

    // The type of queues, which determines whether each Term is
    // written downstrem IMMEDIATEly, or QUEUED up for later
    // delivery with writeAll ().
    private final WriteQueues.Type type;

    // The output Streams, in predictable order.
    private final Countable<Stream<OUTPUT>> outputStreams;
    !!! Countable should probably BE an ArrayObject.

    // MUTABLE:
    // The lookup of queues by output streams.  Initially empty.
    // *** Synchronize on this.lock in order to access. ***
    private final Map<Stream<OUTPUT>, List<Term<OUTPUT>>> queues;


    /**
     * <p>
     * Creates a new WriteQueues.
     * </p>
     *
     * @param type The type of queues to create: write out each term
     *             <code> IMMEDIATE </code>ly, or keep terms
     *             <code> QUEUED </code> until <code> writeAll () </code>
     *             is invoked, and so on.  Must not be null.
     *
     * @param output_streams The 0 or more Streams to which Terms
     *                       will be written.  Can be empty.
     *                       Must not be null.
     */
    public WriteQueues (
            WriteQueues.Type type,
            Countable<Stream<OUTPUT>> output_streams
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type, output_streams );

        this.type = type;
        this.outputStreams = output_streams;

        this.queues = new HashMap<Stream<OUTPUT>, List<Term<OUTPUT>>> ();

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Removes all Terms from the queue for the specified Stream.
     * </p>
     *
     * @param output_stream The Stream whose queued up Term(s) (if any)
     *                      will be emptied.  Must be one of the
     *                      <code> outputStreams () </code> handled
     *                      by these WriteQueues.  Must not be null.
     *
     * @return These WriteQueues.  Never null.
     */
    public final WriteQueues<OUTPUT> clear (
            Stream<OUTPUT> output_stream
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustBeElementOf.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               output_stream );

        final Parameter1.MustBeElementOf 
        if ( this.queues.remove ( output_stream ) == null )
        {
            throw new Parameter1.MustBeElementOf.Violation ( 
    clear ( Stream<OUTPUT> ); -> synchronize on lock

    clearAll (); -> synchronize on lock

    Term<OUTPUT> dequeue ( Stream<OUTPUT> ); -> synchronize on lock

    enqueue ( Stream<OUTPUT>, Term<OUTPUT> ); -> synchronize on lock

    length (); -> synchronize on lock

    length ( Stream<OUTPUT> ); -> synchronize on lock

    outputStreams ();

    type ();

    write ( Stream<OUTPUT> ); -> just call dequeue () one at a time, so multiple threads can use this class at once.  e.g. for forking operations.

    writeAll (); -> just call dequeue () one at a time, so multiple threads can use this class at once.  e.g. for forking operations.
}
