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
 * Removes element(s) within specific index range(s) from the incoming
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
public class RemoveRange<VALUE extends Object>
    extends AbstractPipe<VALUE, VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( RemoveRange.class );


    // The Pipe of Long range start indices specifying where
    // to start removing the elements.
    private final Pipe<?, Long> removeStart;

    // The Stream of range start indices reading from the removeStart Pipe,
    // if this is a RemoveRange instance; or null, if this is a template.
    private final Stream<Long> removeStartStream;

    // The Pipe of Long range end indices specifying where
    // to end removing the elements.
    private final Pipe<?, Long> removeEnd;

    // The Stream of range end indices reading from the removeEnd Pipe,
    // if this is a RemoveRange instance; or null, if this is a template.
    private final Stream<Long> removeEndStream;

    // MUTABLE:
    // ONLY ACCESS FROM WITHIN step ()
    // in order to guarantee synchronized access.
    // The most recently read range start index from the removeStartStream,
    // if any; or null.
    private Long removeStartIndex = null;

    // MUTABLE:
    // ONLY ACCESS FROM WITHIN step ()
    // in order to guarantee synchronized access.
    // The most recently read range end index from the removeEndStream,
    // if any; or null.
    private Long removeEndIndex = null;

    // MUTABLE:
    // ONLY ACCESS FROM WITHIN step ()
    // in order to guarantee synchronized access.
    // The next input Term, if the previous one was split into two;
    // or null.
    private Term<VALUE> nextInput = null;


    /**
     * <p>
     * Creates a new stateless RemoveRange template.
     * </p>
     *
     * @param input_pipe The upstream Pipe, from which we read Term(s).
     *                   Must not be null.
     *
     * @param downstream_type The Type of Term(s) to write downstream.
     *                        Must not be null.
     *
     * @param remove_start The Pipe of Long range start indices
     *                     specifying where to start removing the elements.
     *                     Must not be null.
     *
     * @param remove_end The Pipe of Long range end indices
     *                   specifying where to end removing the elements.
     *                   Must not be null.
     */
    public RemoveRange (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type,
            Pipe<?, Long> remove_start,
            Pipe<?, Long> remove_end
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( input_pipe,      // input_pipe
               downstream_type, // downstream_type
               null,            // upstream_or_null
               remove_start,    // remove_start
               remove_end );    // remove_end
    }


    /**
     * <p>
     * Creates a new RemoveRange.
     * </p>
     *
     * @param input_pipe The upstream Pipe, from which we read Term(s).
     *                   Must not be null.
     *
     * @param upstream_or_null Either the Stream coming from the
     *                         input_pipe from which we read input Term(s),
     *                         or null if this is a stateless template
     *                         RemoveRange.  Must not be null.
     *
     * @param downstream_type The Type of Term(s) to write downstream.
     *                        Must not be null.
     *
     * @param remove_start The Pipe of Long range start indices
     *                     specifying where to start removing the elements.
     *                     Must not be null.
     *
     * @param remove_end The Pipe of Long range end indices
     *                   specifying where to end removing the elements.
     *                   Must not be null.
     */
    public RemoveRange (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type,
            Stream<VALUE> upstream_or_null,
            Pipe<?, Long> remove_start,
            Pipe<?, Long> remove_end
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( input_pipe,           // input_pipe
                downstream_type,      // downstream_type
                upstream_or_null );   // upstream_or_null

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               remove_start, remove_end );

        this.removeStart = remove_start;
        this.removeEnd = remove_end;

        if ( this.state () == Pipe.State.STATELESS )
        {
            // Template.
            this.removeStartStream = null;
            this.removeEndStream = null;
        }
        else
        {
            // Instance.
            // Create a stream for each parameter.
            this.removeStartStream = // Any old stream.
                this.createParameterStream ( this.removeStart );
            this.removeEndStream = // Any old stream.
                this.createParameterStream ( this.removeEnd );
        }
    }


    /**
     * <p>
     * Fills the hole left by removing elements, if need be.
     * </p>
     *
     * <p>
     * By default, the RemoveRange class does not do anything.
     * Derived classes (such as Replace) can override this behaviour.
     * </p>
     *
     * @param upstream The stream to <code> read () </code> from,
     *                 if/when needed.  Must not be null.
     *
     * @param downstream The Stream to <code> write ( ... ) </code>
     *                   a Term to, if possible.  Must not be null.
     *
     * @return The number of Term(s) filled into the hole, if any.
     *         Always 0 or greater.
     */
    protected int hole (
            Stream<VALUE> upstream,
            Stream<VALUE> downstream
            )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        // Do nothing.
        return 0;
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
                                  this.removeStartStream,
                                  this.removeEndStream );

        // Summary:
        // remove ( input, input_index,
        //          remove_start, remove_end )
        //     when remove_start is empty
        //         then return input
        //     otherwise when remove_end is empty
        //         then return input
        //     otherwise
        //         when head ( remove_start )
        //                  greaterThan ( input_index )
        //             then range ( input,
        //                          input_index to head ( remove_start ) - 1 )
        //                  remove ( input, head ( remove_end ) + 1,
        //                           tail ( remove_start ),
        //                           tail ( remove_end ) )
        //         otherwise
        //             remove ( input, head ( remove_end ) + 1,
        //                      tail ( remove_start ),
        //                      tail ( remove_end ) )
        final int max_attempts_to_output_one_term = 64;
        for ( int attempt = 0;
              attempt < max_attempts_to_output_one_term;
              attempt ++ )
        {
            //     when remove_start is empty
            //         then return input
            final Long remove_start_index_object;
            if ( this.removeStartIndex != null )
            {
                remove_start_index_object = this.removeStartIndex;
            }
            else
            {
                remove_start_index_object =
                    this.removeStartStream.nextElement ().orNull ();
            }

            //     otherwise when remove_end is empty
            //         then return input
            final Long remove_end_index_object;
            if ( this.removeEndIndex != null )
            {
                remove_end_index_object = this.removeEndIndex;
            }
            else
            {
                remove_end_index_object =
                    this.removeEndStream.nextElement ().orNull ();
            }

            if ( remove_start_index_object == null
                 || remove_end_index_object == null )
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
                    // Nothing to remove, no more input.
                    // Finished.
                    this.close (); // Seal off this Pipe instance.
                    return false;
                }

                final Pipe.State downstream_state =
                    downstream.write ( input );
                if ( downstream_state == Pipe.State.OPEN )
                {
                    // Continue writing out the rest of the input, since we
                    // have nothing more to remove.
                    return true;
                }
                else
                {
                    // Downstream is closed, so we are finished.
                    this.close (); // Seal off this Pipe instance.
                    return false;
                }
            }


            //     otherwise
            //         when head ( remove_start )
            //                  greaterThan ( input_index )
            //             then range ( input,
            //                          input_index to head ( remove_start ) - 1 )
            //                  remove ( input, head ( remove_end ) + 1,
            //                           tail ( remove_start ),
            //                           tail ( remove_end ) )
            final long remove_start_index =
                remove_start_index_object.longValue ();
            final long remove_end_index =
                remove_end_index_object.longValue ();
            final long length_so_far = upstream.length ();
            if ( length_so_far < remove_start_index )
            {
                final long num_elements_to_write =
                    remove_start_index - length_so_far;
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
                    // The next remove start index is after the end
                    // of the input.  Can't remove any more.
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
            }

            // If we got this far, it means we are still in the middle
            // of a hole (remove_start_index <= input_index)
            // or we somehow missed the hole altogetner (?error
            // invalid remove start and end indices?).
            if ( remove_end_index <= length_so_far )
            {
                continue;
            }

            //         otherwise
            //             remove ( input, head ( remove_end ) + 1,
            //                      tail ( remove_start ),
            //                      tail ( remove_end ) )
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
                // The next remove start index is after the end
                // of the input.  Can't remove any more.
                // !!! SHOULD THIS BE AN ERROR...????
                // Finished.
                this.close (); // Seal off this Pipe instance.
                return false;
            }

            final long num_elements_to_skip =
                remove_end_index - length_so_far + 1L;
            this.nextInput =
                new Split<VALUE> ()
                        .write (
                            input,                 // input
                            num_elements_to_skip,  // split_at_index
                            null,                  // first_half_or_null
                            null,                  // second_half_or_null
                            downstream );          // error_or_null
            if ( ! ( this.nextInput instanceof Countable )
                 && ! ( this.nextInput instanceof Cyclical ) )
            {
                // Error.  Abort.
                this.close (); // Seal off this Pipe instance.
                return false;
            }

            // If this is a derived class (such as Replace), fill the hole.
            final int num_filled_terms =
                this.hole ( upstream, downstream );
            if ( num_filled_terms > 0 )
            {
                // We've written something downstream.
                // No need to keep looping.
                // We'll continue performing step () as normal.
                return true;
            }

            // Keep trying to output something.
            continue;
        } // Keep trying to output something for a little while.

        // Give up if we can't avoid the holes after N attempts.
        // Let step () carry on next time.
        return true;
    }


    /**
     * @return     The Pipe of Long range end indices specifying where
     *             to end removing the elements.  Never null.
     */
    public final Pipe<?, Long> removeEnd ()
        throws ReturnNeverNull.Violation
    {
        return this.removeEnd;
    }


    /**
     * @return     The Pipe of Long range start indices specifying where
     *             to start removing the elements.  Never null.
     */
    public final Pipe<?, Long> removeStart ()
        throws ReturnNeverNull.Violation
    {
        return this.removeStart;
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#instantiate()
     *
     * Can be overridden by derived classes (such as Pad).
     */
    protected RemoveRange<VALUE> instantiate ()
        throws ReturnNeverNull.Violation
    {
        final Stream<VALUE> input_stream =
            this.createInputStream ();
        return new RemoveRange<VALUE> (
            this.inputPipe (),          // input_pipe
            this.downstreamType (),     // downstream_type
            input_stream,               // upstream
            this.removeStart,           // remove_start
            this.removeEnd );           // remove_end
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#parameters(java.util.List)
     *
     * Can be overridden by derived Pipes that add parameters or
     * provide fixed constants to RemoveRange as its parameters.
     */
    @Override
    protected List<Pipe<?, ?>> parameters (
            List<Pipe<?, ?>> parameters_list
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        parameters_list.add ( this.removeStart );
        parameters_list.add ( this.removeEnd );
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
        if ( this.removeStartStream != null
             && this.removeEndStream != null )
        {
            parameter_streams_list.add ( this.removeStartStream );
            parameter_streams_list.add ( this.removeEndStream );
        }

        return parameter_streams_list;
    }
}
