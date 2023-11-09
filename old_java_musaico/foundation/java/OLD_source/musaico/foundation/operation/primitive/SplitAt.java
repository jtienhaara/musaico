package musaico.foundation.operation.primitive;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Map;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

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
 * A Pipe with one input stream split at the i'th
 * (and j'th and k'th...) element(s) into 0 or more output streams.
 * </p>
 *
 * <p>
 * The first output stream comprises all of the elements that appear
 * in the input stream before the i'th element; the second output
 * stream comprises elements at and after the i'th.  If another split-at
 * position is specified, j, then the second output stream ends at
 * element (j - 1), and the third element stream comprises the
 * elements starting at j.  The output stream containing the last
 * split-at element contains the rest of the elements from the input stream.
 * </p>
 *
 * <p>
 * If there are insufficient elements to write at least one element
 * downstream to any of the output stream(s), then by default
 * the SplitAt pipe will write out one single No-valued Term
 * to each such output.  For example, if the input is
 * SpliAt elements 3 and 7, but only 5 elements are received from upstream,
 * then the first output stream will receive element #s { 0, 1, 2 },
 * the second output stream will receive* element #s { 3, 4, 5 },
 * and the third output stream will receive No Term.
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
public class SplitAt<VALUE extends Object>
    extends AbstractRoutingPipe<VALUE, Long>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( SplitAt.class );


    // The index or indices at which to split elements into output pipes.
    private final long [] indices;


    /**
     * <p>
     * Creates a new pipe that will split one input channel at a specific
     * position or positions, writing the split elements to two or more
     * output channels.
     * </p>
     *
     * @param one_input_pipe The upstream Pipe, whose outputs we read,
     *                       split, and write downstream.  Must not be null.
     *
     * @param indices The index or indices at which to split the
     *                incoming elements, such as 3L to write
     *                element #s { 0L, 1L, 2L } to the first output
     *                stream and element #s { 3L, ... } to the
     *                second output stream, and so on.
     *                Must not be null.  Must not contain any
     *                negative indices.  Must be in strictly
     *                ascending order.
     */
    public <PIPE_IN extends Object>
        SplitAt (
            Just<Pipe<PIPE_IN, VALUE>> one_input_pipe,
            long... indices
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter2.MustBeGreaterThan.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        super ( one_input_pipe,
                one_input_pipe == null // downstream type: same as input type.
                    ? null
                    : one_input_pipe.orNull ().downstreamType () );

        long previous_index = -1L;
        for ( long index : indices )
        {
            classContracts.check ( Parameter2.MustBeGreaterThanOrEqualToZero.CONTRACT,
                                   index );

            if ( previous_index >= 0L )
            {
                final Parameter2.MustBeGreaterThanOrEqualTo greater_than_previous_index =
                    new Parameter2.MustBeGreaterThanOrEqualTo ( previous_index );
                classContracts.check ( greater_than_previous_index,
                                       index );
            }

            previous_index = index;
        }

        this.indices = new long [ indices.length ];
        System.arraycopy ( indices, 0,
                           this.indices, 0, indices.length );
    }


    /**
     * @see musaico.foundation.operation.Pipe#maximumOutputPipes()
     */
    @Override
    public final long maximumOutputPipes ()
        throws Return.AlwaysGreaterThan.Violation
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
        return 0L;
    }


    /**
     * <p>
     * Sets the Term to postfix (if any) to each split output stream,
     * if there are no main Terms to output to the given output stream.
     * </p>
     *
     * @param context The Context in which output is being generated.
     *                Must not be null.
     *
     * @param mapping The map of (output stream) --&gt; (term to output),
     *                for each of the specified output streams.
     *                Each output stream will have either no mapped term,
     *                and <code> mapping.get ( output_stream ) </code>
     *                will return null, or it will have 1 mapped term,
     *                returned by <code> mapping.get ( output_stream ) </code>.
     *                Must not be null.  Must be empty.
     *
     * @param input_streams The input Stream(s) for this SplitAt pipe
     *                      in the current context.  Must not be null.
     *
     * @param output_streams The output Streams for this SplitAt pipe
     *                       in the current context, to which the
     *                       empty postfix Term (if any) will be written.
     *                       Must not be null.
     *
     * @return The term to append to each empty output,
     *         or null if there is no empty postfix.
     */
    protected void postfixEmptyTermOrNull (
            Context context,
            Map<Stream<VALUE>, Term<VALUE>> mapping,
            Countable<Stream<VALUE>> input_streams,
            Countable<Stream<VALUE>> output_streams
            )
        throws ParametersMustNotBeNull.Violation
    {
        final No<VALUE> empty =
            new No<VALUE> ( this.downstreamType () );
        for ( Stream<VALUE> output_stream : output_streams )
        {
            mapping.put ( output_stream, empty );
        }
    }


    /**
     * <p>
     * Sets the Term to postfix (if any) to each split output,
     * after all other output terms are written.
     * </p>
     *
     * @param mapping The map of (output stream) --&gt; (term to output),
     *                for each of the specified output streams.
     *                Each output stream will have either no mapped term,
     *                and <code> mapping.get ( output_stream ) </code>
     *                will return null, or it will have 1 mapped term,
     *                returned by <code> mapping.get ( output_stream ) </code>.
     *                Must not be null.  Must be empty.
     *
     * @param context The Context in which output is being generated.
     *                Must not be null.
     *
     * @param input_streams The input Streams for this SplitAt pipe
     *                      in the current context.  Must not be null.
     *
     * @param output_streams The output Streams for this SplitAt pipe
     *                       in the current context, to which the postfix
     *                       Term (if any) will be written.
     *                       Must not be null.
     *
     * @return The term to append to the output being written,
     *         or null if there is no postfix.
     */
    protected void postfixTermOrNull (
            Context context,
            Map<Stream<VALUE>, Term<VALUE>> mapping,
            Countable<Stream<VALUE>> input_streams,
            Countable<Stream<VALUE>> output_streams
            )
        throws ParametersMustNotBeNull.Violation
    {
        // No appendix.
    }


    /**
     * <p>
     * Sets the Term to prefix (if any) to the split output,
     * before any other output terms are written.
     * </p>
     *
     * @param mapping The map of (output stream) --&gt; (term to output),
     *                for each of the specified output streams.
     *                Each output stream will have either no mapped term,
     *                and <code> mapping.get ( output_stream ) </code>
     *                will return null, or it will have 1 mapped term,
     *                returned by <code> mapping.get ( output_stream ) </code>.
     *                Must not be null.  Must be empty.
     *
     * @param context The Context in which output is being generated.
     *                Must not be null.
     *
     * @param input_streams The input Streams for this SplitAt pipe
     *                      in the current context.  Must not be null.
     *
     * @param output_streams The output Streams for this SplitAt pipe
     *                       in the current context, to which the prefix
     *                       Term (if any) will be written.
     *                       Must not be null.
     *
     * @return The term to prepend to the output being written,
     *         or null if there is no prefix.
     */
    protected void prefixTermOrNull (
            Context context,
            Map<Stream<VALUE>, Term<VALUE>> mapping,
            Countable<Stream<VALUE>> input_streams,
            Countable<Stream<VALUE>> output_streams
            )
        throws ParametersMustNotBeNull.Violation
    {
        // No prefix term.
    }


    /**
     * @see musaico.foundation.operation.primitive.AbstractRoutingPipe#process(musaico.foundation.operation.Context, musaico.foundation.term.Countable, musaico.foundation.term.Countable, java.util.Map, musaico.foundation.operation.Stream, musaico.foundation.term.Maybe, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    protected final void process (
            Context context,
            Countable<Stream<VALUE>> input_streams,
            Countable<Stream<VALUE>> output_streams,
            Map<Stream<VALUE>, Term<VALUE>> output_map,
            Stream<VALUE> input_stream,
            Maybe<Term<VALUE>> maybe_input_term,
            Long initial_length,
            Long length_before_read,
            Long length_after_read
            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  context, input_streams, output_streams,
                                  output_map, input_stream, maybe_input_term,
                                  initial_length, length_before_read,
                                  length_after_read );

        final Term<VALUE> input_term_or_null =
            maybe_input_term.orNull ();

        if ( input_term_or_null == null )
        {
            // Nothing to split.
            return;
        }

        // Split the term we just read in:
        final int [] array_indices;
        if ( index_num >= this.indices.length
             || ! input_term_or_null.hasValue ()
             || ! ( input_term_or_null instanceof Countable ) )
        {
            // No splits, either because we have already
            // split at all the indices, or because the input
            // term has 0 elements anyway, or because the input
            // term is not Countable and so cannot be split.
            array_indices = new int [ 0 ];
        }
        else
        {
            int num_splits = 0;
            for ( int new_index_num = index_num;
                  new_index_num < this.indices.length;
                  new_index_num ++ )
            {
                if ( this.indices [ new_index_num ] < length_after_read )
                {
                    num_splits = new_index_num - index_num + 1;
                }
                else
                {
                    break;
                }
            }

            array_indices = new int [ num_splits ];
            for ( int ni = 0; ni < array_indices.length; ni ++ )
            {
                array_indices [ ni ] = index_num + ni;
            }

            index_num = index_num + array_indices.length;
        }

        Term<VALUE> remainder = input_term_or_null;
        for ( int a = 0; a < array_indices.length; a ++ )
        {
            final int array_index = array_indices [ a ];
            final long output_stream_num = (long) array_index;
            if ( output_stream_num >= output_streams.length () )
            {
                // No more output streams, so no point splitting
                // any more.
                break;
            }

            @SuppressWarnings("unchecked") // We prevent splitting
                // entirely if the input Term is not Countable,
                // so by the time we reach this point, we know
                // we are dealing with Countable terms.
            final Countable<VALUE> term_to_split =
                (Countable<VALUE>) remainder;
            final long split_index = this.indices [ array_index ];
            final Countable<VALUE> before_split =
                term_to_split.range ( 0L, split_index - 1L );
            final long last_index = term_to_split.length () - 1L;
            remainder = term_to_split.range ( split_index,
                                              last_index );
            final Stream<VALUE> output_stream =
                        output_streams.at ( output_stream_num )
                                      // Should never happen (allegedly...):
                                      .orThrowUnchecked ();

            if ( output_stream.state () == Stream.State.OPEN )
            {
                !!!;
                output_map.put ( output_stream, before_split );
            }
        }

        // Now output the remainder from the split(s) (if any)
        // downstream:
        final long output_stream_num = (long) index_num;
        if ( output_stream_num < output_streams.length () )
        {
            final Stream<VALUE> output_stream =
                output_streams.at ( output_stream_num )
                              // Should never happen (allegedly...):
                              .orThrowUnchecked ();

            if ( output_stream.state () == Stream.State.OPEN )
            {
                !!!;
                output_map.put ( output_stream, remainder );
            }
        }
    }


    /**
     * @see musaico.foundation.operation.primitive.AbstractRoutingPipe#stepStateOrNull(musaico.foundation.operation.Context, musaico.foundation.term.Countable, musaico.foundation.term.Countable, java.lang.Object)
     */
    @Override
    protected final Object stepStateOrNull (
            Context context,
            Countable<Stream<VALUE>> input_streams,
            Countable<Stream<VALUE>> output_streams,
            Long previous_length_or_null
            )
        throws ParametersMustNotBeNull.Violation
    {
        long length = 0L;
        for ( Stream<VALUE> input_stream : input_streams )
        {
            length += input_stream.length ();
        }

        return new Long ( length );
    }
}
