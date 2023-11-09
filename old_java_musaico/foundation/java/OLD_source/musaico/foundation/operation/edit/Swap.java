package musaico.foundation.operation.edit;

import java.io.Serializable;

import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.operation.AbstractPipe;
import musaico.foundation.operation.Buffer;
import musaico.foundation.operation.Pipe;
import musaico.foundation.operation.Split;
import musaico.foundation.operation.Stream;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.countable.No;

import musaico.foundation.term.infinite.Cyclical;

import musaico.foundation.term.multiplicities.OneOrMore;


/**
 * <p>
 * Inserts element(s) at specific index(ices) into the incoming
 * Term(s).
 * </p>
 *
 *
 * <p>
 * The first time <code> apply ( ... ) </code> is called,
 * the stateless Pipe creates a stateful instance of itself, and
 * delegates the <code> apply ( ... ) </code> call to that instance.
 * The instance then performs its iterative or recursive step (see below).
 * This Pipe then returns the instance to the caller, who can choose to call
 * it again, if the caller wants the Pipe to write more Term(s)
 * to its stream(s).
 * </p>
 *
 * </p>
 * When <code> apply ( ... ) </code> is invoked on a stateful Pipe instance,
 * it performs one step of its iterative or recursive operation, and
 * <code> write ( ... ) </code>s 0 or 1 Term(s) to the provided Stream(s).
 * If it has more processing to do (that is, if it might be able to
 * write more Term(s) downstream), it returns One&lt;Pipe&gt;: itself.
 * If it has no further processing to do, it returns No&lt;Pipe&gt;.
 * </p> 
 *
 *
 * <p>
 * In Java, every Operation must implement equals (), hashCode ()
 * and toString().
 * </p>
 *
 * <p>
 * In Java every Operation must be Serializable in order to
 * play nicely across RMI.  However in general it is recommended
 * that only stateless Pipes be passed around over RMI, and even
 * then, do so with caution.  The Terms which feed parameters
 * and inputs to Pipes, although Serializable themselves,
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
 * @see musaico.foundation.operation.edit.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.edit.MODULE#LICENSE
 */
public class Insert<VALUE extends Object>
    extends AbstractPipe<VALUE, VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( Insert.class );


    // The Pipe of Long indices specifying where
    // to insert the elements.
    private final Pipe<?, Long> insertAt;

    // The Stream of indices reading from the insertAt Pipe, if this
    // is an Insert instance; or null, if this is a template.
    private final Stream<Long> insertAtStream;

    // The Pipe of Term(s) to insert at the specified
    // index(ices).
    private final Pipe<?, VALUE> inserts;

    // The Buffer of Term(s) to insert, if this is an Insert instance;
    // or null, if this is a template.
    private final Buffer<VALUE> insertsBuffer;

    // MUTABLE:
    // ONLY ACCESS FROM WITHIN step ()
    // in order to guarantee synchronized access.
    // The most recently read index from the insertAtStream,
    // if any; or null.
    private Long insertAtIndex = null;

    // MUTABLE:
    // ONLY ACCESS FROM WITHIN step ()
    // in order to guarantee synchronized access.
    // The next input Term, if the previous one was split into two;
    // or null.
    private Term<VALUE> nextInput = null;


    /**
     * <p>
     * Creates a new stateless Insert template.
     * </p>
     *
     * @param input_pipe The upstream Pipe, from which we read Term(s).
     *                   Must not be null.
     *
     * @param downstream_type The Type of Term(s) to write downstream.
     *                        Must not be null.
     *
     * @param insert_at The Pipe of Long indices specifying where
     *                  to insert the elements.  Must not be null.
     *
     * @param inserts The Pipe of Term(s) to insert at the specified
     *                index(ices).  Must not be null.
     */
    public Insert (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type,
            Pipe<?, Long> insert_at,
            Pipe<?, VALUE> inserts
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( input_pipe,      // input_pipe
               downstream_type, // downstream_type
               null,            // upstream_or_null
               insert_at,       // insert_at
               inserts );       // inserts
    }


    /**
     * <p>
     * Creates a new Insert.
     * </p>
     *
     * @param input_pipe The upstream Pipe, from which we read Term(s).
     *                   Must not be null.
     *
     * @param upstream_or_null Either the Stream coming from the
     *                         input_pipe from which we read input Term(s),
     *                         or null if this is a stateless template Insert.
     *                         Must not be null.
     *
     * @param downstream_type The Type of Term(s) to write downstream.
     *                        Must not be null.
     *
     * @param insert_at The Pipe of Long indices specifying where
     *                  to insert the elements.  Must not be null.
     *
     * @param inserts The Pipe of Term(s) to insert at the specified
     *                index(ices).  Must not be null.
     */
    public Insert (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type,
            Stream<VALUE> upstream_or_null,
            Pipe<?, Long> insert_at,
            Pipe<?, VALUE> inserts
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( input_pipe,           // input_pipe
                downstream_type,      // downstream_type
                upstream_or_null );   // upstream_or_null

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               insert_at, inserts );

        this.insertAt = insert_at;
        this.inserts = inserts;

        if ( this.state () == Pipe.State.STATELESS )
        {
            // Template.
            this.insertAtStream = null;
            this.insertsBuffer = null;
        }
        else
        {
            // Instance.
            // Create a stream for each parameter.
            this.insertAtStream = // Any old stream.
                this.createParameterStream ( this.insertAt );
            this.insertsBuffer = // Always a buffer.
                this.createParameterBuffer ( this.inserts );
        }
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#step(musaico.foundation.operation.Stream, musaico.foundation.operation.Stream)
     */
    @Override
    protected final boolean step (
            Stream<VALUE> upstream,
            Stream<VALUE> downstream
            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  upstream, downstream,
                                  this.insertAtStream,
                                  this.insertsBuffer );

        // Summary:
        // insert ( input, input_index,
        //          insert_at_indices, elements_to_insert )
        //     when insert_at_indices is empty
        //         then return input
        //     otherwise
        //         when head ( insert_at_indices )
        //                  lessThanOrEqualTo ( input_index )
        //             then elements_to_insert,
        //                  insert ( input, input_index,
        //                           tail ( insert_at_indices ),
        //                           elements_to_insert )
        //         otherwise
        //             range ( input,
        //                     input_index to head ( insert_at_indices ) - 1 )
        //             insert ( input, head ( insert_at_indices ),
        //                             insert_at_indices,
        //                             elements_to_insert )
        final Long insert_at_index_object;
        if ( this.insertAtIndex != null )
        {
            insert_at_index_object = this.insertAtIndex;
        }
        else
        {
            insert_at_index_object =
                this.insertAtStream.nextElement ().orNull ();
        }

        //     when insert_at_indices is empty
        //         then return input
        if ( insert_at_index_object == null )
        {
            final Term<VALUE> input;
            if ( this.nextInput != null )
            {
                input = this.nextInput;
                this.nextInput = null;
            }
            else
            {
                input = upstream.read ().orNull ();
            }

            if ( input == null )
            {
                // Nothing to insert, no more input.
                // Finished.
                this.close (); // Seal off this Pipe instance.
                return false;
            }

            final Pipe.State downstream_state =
                downstream.write ( input );
            if ( downstream_state == Pipe.State.OPEN )
            {
                // Continue writing out the rest of the input, since we
                // have nothing more to insert.
                return true;
            }
            else
            {
                // Downstream is closed, so we are finished.
                this.close (); // Seal off this Pipe instance.
                return false;
            }
        }

        final long insert_at_index = insert_at_index_object.longValue ();
        final long length_so_far = upstream.length ();

        //         when head ( insert_at_indices )
        //                  lessThanOrEqualTo ( input_index )
        //             then elements_to_insert,
        //                  insert ( input, input_index,
        //                           tail ( insert_at_indices ),
        //                           elements_to_insert )
        if ( insert_at_index <= length_so_far )
        {
            if ( this.insert ( upstream,              // upstream
                               downstream,            // downstream
                               this.insertsBuffer ) ) // inserts_buffer
            {
                // Continue inserting.
                return true;
            }

            // Finished inserting.
            this.insertsBuffer.reset ();
            this.insertAtIndex = null;

            final Pipe.State downstream_state =
                downstream.inputPipeState ();
            if ( downstream_state != Pipe.State.OPEN )
            {
                // Downstream is closed, so we are finished.
                this.close (); // Seal off this Pipe instance.
                return false;
            }

            // Finished inserting.
            // Continue after the insert, below.
        }

        //             range ( input,
        //                     input_index to head ( insert_at_indices ) - 1 )
        //             insert ( input, head ( insert_at_indices ),
        //                             insert_at_indices,
        //                             elements_to_insert )
        final long num_elements_to_write =
            insert_at_index - length_so_far;
        final Term<VALUE> input;
        if ( this.nextInput != null )
        {
            input = this.nextInput;
            this.nextInput = null;
        }
        else
        {
            input = upstream.read ().orNull ();
        }

        if ( input == null )
        {
            // The next insert at index is after the end of the input.
            // Can't insert any more.
            // !!! SHOULD THIS BE AN ERROR...????
            // Finished.
            this.close (); // Seal off this Pipe instance.
            return false;
        }

        this.nextInput =
            new Split<VALUE> ()
                    .write (
                            input,                 // input
                            num_elements_to_write, // split_at_index
                            downstream,            // first_half_or_null
                            null,                  // second_half_or_null
                            downstream );          // error_or_null
        if ( ! ( this.nextInput instanceof Countable )
             && ! ( this.nextInput instanceof Cyclical ) )
        {
            // Error.  Abort.
            this.close (); // Seal off this Pipe instance.
            return false;
        }

        // We've either inserted the entire input Term, or we've
        // inserted part of the input Term, and stored
        // the remainder for next time.
        // Now we have to carry on and insert at this position.
        // Continue.
        return true;

        /* !!!
        final Countable<VALUE> countable_input;
        final OneOrMore<VALUE> input_cycle;
        if ( input == null )
        {
            // The next insert at index is after the end of the input.
            // Can't insert any more.
            // !!! SHOULD THIS BE AN ERROR...????
            // Finished.
            return false;
        }
        else if ( input instanceof Countable )
        {
            countable_input = (Countable<VALUE>) input;
            input_cycle = null;
        }
        else if ( input instanceof Cyclical )
        {
            final Cyclical<VALUE> cyclical_input = (Cyclical<VALUE>) input;
            countable_input = cyclical_input.header ();
            input_cycle = cyclical_input.cycle ();
        }
        else
        {
            // Error, don't know what this input term is.
            // Can't insert into something we can't understand.
            // The input must be either:
            // 1) Countable; or
            // 2) Cyclical.
            final TermViolation violation =
                Stream.TERM_MUST_BE_COUNTABLE_OR_CYCLICAL.violation (
                    this, // plaintiff
                    input );
            final Error<VALUE> error =
                new Error<VALUE> ( this.downstreamType (),
                                   violation );
            downstream.write ( error );

            // Ended with an error.
            // Finished.
            return false;
        }

        if ( countable_input.length () <= num_elements_to_write )
        {
            // The next insert at index comes after the (countable part of)
            // this input.
            downstream.write ( countable_input );

            final long num_remaining_to_write =
                num_elements_to_write - countable_input.length ();

            // Is there an infinite cycle?  If so, write out its elements
            // until we hit the next insert at index.
            if ( input_cycle != null )
            {
                final long cycle_length = input_cycle.length ();
                final long full_repetitions =
                    num_remaining_to_write / cycle_length;
                final long partial_length =
                    num_remaining_to_write % cycle_length;

                for ( long c = 0L; c < full_repetitions; c++ )
                {
                    downstream.write ( input_cycle );
                }

                final Countable<VALUE> partial_header;
                if ( partial_length == 0L )
                {
                    partial_header =
                        new No<VALUE> ( this.downstreamType () );
                }
                else
                {
                    final Countable<VALUE> partial_cycle_to_write =
                        input_cycle.range ( 0L, partial_length - 1L );
                    downstream.write ( partial_cycle_to_write );

                    partial_header = input_cycle.range ( partial_length,
                                                         Countable.LAST );
                }

                this.nextInput =
                    new Cyclical<VALUE> ( this.downstreamType (),
                                          partial_header,
                                          input_cycle );
            }

            // We've inserted all or part of the input Term.
            // Now we either have to keep inserting unti we reach
            // the next insert at position, or we have to insert
            // at this position.
            // Continue.
            return true;
        }

        final Countable<VALUE> partial_countable_to_write =
            countable_input.range ( 0L, num_elements_to_write - 1L );
        downstream.write ( partial_countable_to_write );

        final Countable<VALUE> remainder =
            countable_input.range ( num_elements_to_write, Countable.LAST );
        if ( input_cycle == null )
        {
            this.nextInput = remainder;
        }
        else
        {
            this.nextInput = new Cyclical<VALUE> ( this.downstreamType (),
                                                   remainder,
                                                   input_cycle );
        }

        // We've inserted part of the input Term, and stored
        // the remainder for next time.
        // Now we have to carry on and insert at this position.
        // Continue.
        return true;
        !!! */
    }


    /**
     * <p>
     * Inserts Terms downstream.
     * </p>
     *
     * <p>
     * Invoked whenever the upstream arrives at an insert-at index.
     * Because this method is only ever invoked from
     * <code> step () </code>, all access to object variables
     * is synchronized and thread-safe.
     * </p>
     *
     * <p>
     * Can be overridden by derived classes (such as Pad).
     * </p>
     *
     * @param upstream The stream to <code> read () </code> from,
     *                 if/when needed.  Must not be null.
     *
     * @param downstream The Stream to <code> write ( ... ) </code>
     *                   a Term to, if possible.  Must not be null.
     *
     * @param inserts_buffer The Buffer of Term(s) to insert.
     *                       Must not be null.
     *
     * @return True if the insertion process is still stepping and might
     *         insert more Term(s); false if all insertions at this
     *         position are complete.
     */
    protected boolean insert (
            Stream<VALUE> upstream,
            Stream<VALUE> downstream,
            Stream<VALUE> inserts_buffer
            )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<VALUE> insertion =
            inserts_buffer.read ().orNull ();
        if ( insertion != null )
        {
            // Still inserting.
            final Pipe.State downstream_state =
                downstream.write ( insertion );
            if ( downstream_state == Pipe.State.OPEN )
            {
                // Continue inserting.
                return true;
            }
            else
            {
                // Downstream is closed, so we are finished.
                return false;
            }
        }

        // Stop inserting; continue writing out the input after the insert.
        return false;
    }


    /**
     * @return The Pipe of Long indices specifying where
     *         to insert the elements.  Never null.
     */
    public final Pipe<?, Long> insertAt ()
        throws ReturnNeverNull.Violation
    {
        return this.insertAt;
    }


    /**
     * @return The Pipe of Term(s) to insert at the specified
     *         index(ices).  Never null.
     */
    public final Pipe<?, VALUE> inserts ()
        throws ReturnNeverNull.Violation
    {
        return this.inserts;
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#instantiate()
     *
     * Can be overridden by derived classes (such as Pad).
     */
    protected Insert<VALUE> instantiate ()
        throws ReturnNeverNull.Violation
    {
        final Stream<VALUE> input_stream =
            this.createInputStream ();
        return new Insert<VALUE> (
            this.inputPipe (),          // input_pipe
            this.downstreamType (),     // downstream_type
            input_stream,               // upstream
            this.insertAt,              // insert_at
            this.inserts );             // inserts
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#parameters(java.util.List)
     *
     * Can be overridden by derived Pipes that add parameters or
     * provide fixed constants to Insert as its parameters.
     */
    @Override
    protected List<Pipe<?, ?>> parameters (
            List<Pipe<?, ?>> parameters_list
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        parameters_list.add ( this.insertAt );
        parameters_list.add ( this.inserts );
        return parameters_list;
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#parameterStreams(java.util.List)
     */
    @Override
    protected List<Stream<?>> parameterStreams (
            List<Stream<?>> parameter_streams_list
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        if ( this.insertAtStream != null
             && this.insertsBuffer != null )
        {
            parameter_streams_list.add ( this.insertAtStream );
            parameter_streams_list.add ( this.insertsBuffer );
        }

        return parameter_streams_list;
    }
}
