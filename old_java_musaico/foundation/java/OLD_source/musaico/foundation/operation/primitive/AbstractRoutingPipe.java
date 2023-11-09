package musaico.foundation.operation.primitive;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Map;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.operation.AbstractPipe;
import musaico.foundation.operation.Context;
import musaico.foundation.operation.Pipe;
import musaico.foundation.operation.Stream;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Just;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;

import musaico.foundation.term.countable.No;


/**
 * <p>
 * A Pipe with zero or more input streams which get sliced, diced and routed
 * to zero or more output streams.
 * </p>
 *
 * <p>
 * For example, a Pipe which splits its input stream into multiple output
 * streams, or a Pipe which merges its inputs streams into one output stream,
 * and so on.
 * </p>
 *
 * <p>
 * If there are insufficient elements to write at least one element
 * downstream to any of the output stream(s), then by default
 * the AbstractRoutingPipe will write out one single No-valued Term
 * to each such output.
 * (This behaviour can be overridden by derived classes, by
 * overriding the <code> postfixEmptyTermOrNull () </code> method).
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
public abstract class AbstractRoutingPipe<VALUE extends Object, STATE extends Object>
    extends AbstractPipe<VALUE, VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new AbstractRoutingPipe.
     * </p>
     *
     * @param input_pipes The 0 or more upstream Pipe(s), whose outputs
     *                    we read, route, and write to the downstream
     *                    Pipe(s).  Must not be null.
     *
     * @param downstream_type The Type of Term(s) to write downstream.
     *                        Must not be null.
     */
    public <PIPE_IN extends Object>
        AbstractRoutingPipe (
            Countable<Pipe<PIPE_IN, VALUE>> input_pipes,
            Type<VALUE> downstream_type
            )
        throws ParametersMustNotBeNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        super ( input_pipes,
                downstream_type );
    }


    // Every AbstractRoutingPipe must implement
    // musaico.foundation.operation.Pipe#maximumOutputPipes()

    // Every AbstractRoutingPipe must implement
    // musaico.foundation.operation.Pipe#minimumOutputPipes()


    /**
     * <p>
     * Sets the Term to postfix (if any) to each output stream,
     * if there are no main Terms to output to the given output stream.
     * </p>
     *
     * <p>
     * Can be overridden by classes derived from AbstractRoutingPipe.
     * </p>
     *
     * @param context The Context in which output is being generated.
     *                Must not be null.
     *
     * @param input_streams The input Stream(s) for this AbstractRoutingPipe
     *                      in the current context.  Must not be null.
     *
     * @param output_streams The output Streams for this AbstractRoutingPipe
     *                       in the current context, to which the
     *                       empty postfix Term(s) (if any) mapped by
     *                       this method will be written by the calling method.
     *                       Must not be null.
     *
     * @param output_map The map of (output stream) --&gt; (term to output),
     *                   for each of the specified output streams.
     *                   Each output stream will have either no mapped term,
     *                   and <code> output_map.get ( output_stream ) </code>
     *                   will return null, or it will have 1 mapped term,
     *                   returned by
     *                   <code> output_map.get ( output_stream ) </code>.
     *                   Must not be null.  Must be empty.
     *
     * @return The term to append to each empty output,
     *         or null if there is no empty postfix.
     */
    protected void postfixEmptyTermOrNull (
            Context context,
            Countable<Stream<VALUE>> input_streams,
            Countable<Stream<VALUE>> output_streams,
            Map<Stream<VALUE>, Term<VALUE>> output_map
            )
        throws ParametersMustNotBeNull.Violation
    {
        final No<VALUE> empty =
            new No<VALUE> ( this.downstreamType () );
        for ( Stream<VALUE> output_stream : output_streams )
        {
            output_map.put ( output_stream, empty );
        }
    }


    /**
     * <p>
     * Sets the map of Term(s) to postfix (if any) to each output stream,
     * after all other output terms are written downstream.
     * </p>
     *
     * <p>
     * Can be overridden by classes derived from AbstractRoutingPipe.
     * </p>
     *
     * @param context The Context in which output is being generated.
     *                Must not be null.
     *
     * @param input_streams The input Streams for this AbstractRoutingPipe
     *                      in the current context.  Must not be null.
     *
     * @param output_streams The output Streams for this AbstractRoutingPipe
     *                       in the current context, to which the postfix
     *                       Term (if any) mapped by this method
     *                       will be written by the calling method.
     *                       Must not be null.
     *
     * @param output_map The map of (output stream) --&gt; (term to output),
     *                   for each of the specified output streams.
     *                   Each output stream will have either no mapped term,
     *                   and <code> output_map.get ( output_stream ) </code>
     *                   will return null, or it will have 1 mapped term,
     *                   returned by
     *                   <code> output_map.get ( output_stream ) </code>.
     *                   Must not be null.  Must be empty.
     */
    protected void postfixTermOrNull (
            Context context,
            Countable<Stream<VALUE>> input_streams,
            Countable<Stream<VALUE>> output_streams,
            Map<Stream<VALUE>, Term<VALUE>> output_map
            )
        throws ParametersMustNotBeNull.Violation
    {
        // No appendix.
    }


    /**
     * <p>
     * Sets the map of Term(s) to prefix (if any) to the output streams,
     * before any other output terms are written downstream.
     * </p>
     *
     * <p>
     * Can be overridden by classes derived from AbstractRoutingPipe.
     * </p>
     *
     * @param context The Context in which output is being generated.
     *                Must not be null.
     *
     * @param input_streams The input Streams for this AbstractRoutingPipe
     *                      in the current context.  Must not be null.
     *
     * @param output_streams The output Streams for this AbstractRoutingPipe
     *                       in the current context, to which the prefix
     *                       Term (if any) mapped by this method
     *                       will be written by the calling method.
     *                       Must not be null.
     *
     * @param output_map The map of (output stream) --&gt; (term to output),
     *                   for each of the specified output streams.
     *                   Each output stream will have either no mapped term,
     *                   and <code> output_map.get ( output_stream ) </code>
     *                   will return null, or it will have 1 mapped term,
     *                   returned by
     *                   <code> output_map.get ( output_stream ) </code>.
     *                   Must not be null.  Must be empty.
     */
    protected void prefixTermOrNull (
            Context context,
            Countable<Stream<VALUE>> input_streams,
            Countable<Stream<VALUE>> output_streams,
            Map<Stream<VALUE>, Term<VALUE>> output_map
            )
        throws ParametersMustNotBeNull.Violation
    {
        // No prefix term.
    }


    /**
     * <p>
     * Sets the map of Term(s) to write downstream (if any) to the
     * output streams, given the specified maybe-input-term.
     * </p>
     *
     * @param context The Context in which output is being generated.
     *                Must not be null.
     *
     * @param input_streams The input Streams for this AbstractRoutingPipe
     *                      in the current context.  Must not be null.
     *
     * @param output_streams The output Streams for this AbstractRoutingPipe
     *                       in the current context, to which the output
     *                       Term(s) (if any) mapped by this method
     *                       will be written by the calling method.
     *                       Must not be null.
     *
     * @param output_map The map of (output stream) --&gt; (term to output),
     *                   for each of the specified output streams.
     *                   Each output stream will have either no mapped term,
     *                   and <code> output_map.get ( output_stream ) </code>
     *                   will return null, or it will have 1 mapped term,
     *                   returned by
     *                   <code> output_map.get ( output_stream ) </code>.
     *                   Must not be null.  Must be empty.
     *
     * @param input_stream The upstream source of the maybe-input-term,
     *                     from which a read was just attempted.
     *                     Must not be null.
     *
     * @param maybe_input_term The 0 or 1 Term(s) read from the input
     *                         stream(s) by the <code> step ( ... ) </code>
     *                         method.  Must not be null.
     *
     * @param initial_state_or_null The state before the current
     *                              <code> step ( ... ) </code> invocation
     *                              began, or null if this routing
     *                              pipe does not carry state.
     *
     * @param state_before_read_or_null The state before the attempt to
     *                                  read from the input stream,
     *                                  or null if this routing
     *                                  pipe does not carry state.
     *
     * @param state_after_read_or_null The state after the attempt to
     *                                 read from the input stream,
     *                                 or null if this routing
     *                                 pipe does not carry state.
     */
    protected abstract void process (
            Context context,
            Countable<Stream<VALUE>> input_streams,
            Countable<Stream<VALUE>> output_streams,
            Map<Stream<VALUE>, Term<VALUE>> output_map,
            Stream<VALUE> input_stream,
            Maybe<Term<VALUE>> maybe_input_term,
            STATE initial_state_or_null,
            STATE state_before_read_or_null,
            STATE state_after_read_or_null
            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Returns the current state of this routing pipe, given the input
     * state, at any given point during a call to <code> step ( ... ) </code>.
     * </p>
     *
     * <p>
     * The <code> step ( ... ) </code> method calls this method to set up
     * initial state before stepping, then calls it again before and after
     * each attempted read from the input stream(s) as the step proceeds.
     * </p>
     *
     * <p>
     * A routing pipe can use this state in an way it likes, or just ignore
     * it altogether.  For example, a "split-at-index-X" routing pipe
     * might capture the length of elements read in before the next input
     * Term is read in, then again after it is read in, in order to
     * determine whether the Term needs to be split across output streams.
     * </p>
     *
     * @param context The Context in which output is being generated.
     *                Must not be null.
     *
     * @param input_streams The input Streams for this AbstractRoutingPipe
     *                      in the current context.  Must not be null.
     *
     * @param output_streams The output Streams for this AbstractRoutingPipe
     *                       in the current context, to which the output
     *                       Term(s) (if any) mapped by this method
     *                       will be written by the calling method.
     *                       Must not be null.
     *
     * @param input_state_or_null The state of this routing pipe
     *                            the previous time
     *                            <code> state ( ... ) </code> was invoked,
     *                            or null if this is the first invocation
     *                            during the current step.
     *                            Can be null.
     *
     * @return The current state of this routing pipe, if any,
     *         or null if this routing pipe does not keep any state
     *         during a <code> step () </code>.  Can be null.
     */
    protected abstract STATE stepStateOrNull (
            Context context,
            Countable<Stream<VALUE>> input_streams,
            Countable<Stream<VALUE>> output_streams,
            STATE input_state_or_null
            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * @see musaico.foundation.operation.Pipe#step()
     */
    @Override
    public final boolean step (
            Context context
            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  context );

        final Countable<Stream<VALUE>> input_streams =
            context.inputs ( this );

        final Countable<Stream<VALUE>> output_streams =
            context.outputs ( this );

        final Map<Stream<VALUE>, Term<VALUE>> output_map =
            new HashMap<Stream<VALUE>, Term<VALUE>> ();

        final STATE initial_state_or_null =
            this.stepStateOrNull ( context,
                                   input_streams,
                                   output_streams,
                                   null );

        boolean is_first_input_stream = true;
        boolean have_any_terms_been_output = false;
        STATE state_or_null = initial_state_or_null;
        int index_num = 0;
        for ( Stream<VALUE> input_stream : input_streams )
        {
            // Output the prefix term (if any):
            if ( is_first_input_stream )
            {
                final BigDecimal first_read_time =
                    input_stream.firstReadTimeSeconds ();
                if ( first_read_time.compareTo ( BigDecimal.ZERO ) > 0 )
                {
                    output_map.clear ();
                    this.prefixTermOrNull ( context,
                                            input_streams,
                                            output_streams,
                                            output_map );

                    for ( Stream<VALUE> output_stream : output_streams )
                    {
                        if ( output_stream.state () != Stream.State.OPEN )
                        {
                            continue;
                        }

                        final Term<VALUE> prefix_term_or_null =
                            output_map.get ( output_stream );
                        if ( prefix_term_or_null != null )
                        {
                            output_stream.write ( prefix_term_or_null );
                        }

                        output_stream.close ();
                    }
                }

                is_first_input_stream = false;
            }

            // Read from the input stream, if it's still open:
            if ( input_stream.state () != Stream.State.OPEN )
            {
                continue;
            }

            final STATE state_before_read_or_null =
                this.stepStateOrNull ( context,
                                       input_streams,
                                       output_streams,
                                       state_or_null );
            state_or_null = state_before_read_or_null;

            final Maybe<Term<VALUE>> maybe_input_term =
                input_stream.read ( context );

            final STATE state_after_read_or_null =
                this.stepStateOrNull ( context,
                                       input_streams,
                                       output_streams,
                                       state_or_null );
            state_or_null = state_after_read_or_null;

            // Process the (maybe) term we just read in:
            output_map.clear ();
            this.process ( context,
                           input_streams,
                           output_streams,
                           output_map,
                           input_stream,
                           maybe_input_term,
                           initial_state_or_null,
                           state_before_read_or_null,
                           state_after_read_or_null );

            for ( Stream<VALUE> output_stream : output_streams )
            {
                if ( output_stream.state () != Stream.State.OPEN )
                {
                    continue;
                }

                final Term<VALUE> output_term_or_null =
                    output_map.get ( output_stream );
                if ( output_term_or_null != null )
                {
                    output_stream.write ( output_term_or_null );
                    have_any_terms_been_output = true;
                }

                output_stream.close ();
            }

            // If we have not yet output any terms,
            // and this input stream is closed,
            // then carry on with the next input stream.
            if ( ! have_any_terms_been_output
                 && input_stream.state () != Stream.State.OPEN )
            {
                continue;
            }

            // Whether or not we got an input Term from this Stream,
            // the Stream is still open, so we will have to continue
            // reading from it next step, before carrying on to the
            // next input stream (if any).
            return true;
        }

        // We are done reading from all the input streams,
        // they are all closed.
        // Output the postfix term(s) (if any) and close each output stream.
        output_map.clear ();
        if ( have_any_terms_been_output )
        {
            this.postfixTermOrNull ( context,
                                     input_streams,
                                     output_streams,
                                     output_map );
        }
        else
        {
            this.postfixEmptyTermOrNull ( context,
                                          input_streams,
                                          output_streams,
                                          output_map );
        }

        for ( Stream<VALUE> output_stream : output_streams )
        {
            if ( output_stream.state () != Stream.State.OPEN )
            {
                continue;
            }

            final Term<VALUE> postfix_term_or_null =
                output_map.get ( output_stream );
            if ( postfix_term_or_null != null )
            {
                output_stream.write ( postfix_term_or_null );
            }

            output_stream.close ();
        }

        // Nothing more to split.
        return false;
    }
}
