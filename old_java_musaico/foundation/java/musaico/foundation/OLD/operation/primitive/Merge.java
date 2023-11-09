package musaico.foundation.operation.primitive;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Map;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.operation.Context;
import musaico.foundation.operation.Pipe;
import musaico.foundation.operation.Stream;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;

import musaico.foundation.term.countable.No;


/**
 * <p>
 * A Pipe with 0 or more input streams combined into one output.
 * </p>
 *
 * <p>
 * The output stream comprises 1) all of the Terms from the first input
 * stream (in the same order); 2) all of the Terms from the second
 * input stream; and so on... N) all of the Terms from the Nth
 * input stream.  The first Term of the second input stream is never read in
 * until all of the Terms from the first input stream have been exhausted
 * and it has been closed.
 * </p>
 *
 * <p>
 * If a Merge pipe is constructed with 0 input pipes then it will
 * output one single No-valued Term before closing the output stream
 * (though this behaviour can be overridden by derived classes, by
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
public class Merge<VALUE extends Object>
    extends AbstractRoutingPipe<VALUE, Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new pipe that will Merge 0 or more inputs into 1 output.
     * </p>
     *
     * @param input_pipes The upstream Pipe(s), whose outputs we read,
     *                    merge, and write downstream.  Must not be null.
     *
     * @param stream_type The Type of Term(s) to merge.  Must not be null.
     */
    public <PIPE_IN extends Object>
        Merge (
            Countable<Pipe<PIPE_IN, VALUE>> input_pipes,
            Type<VALUE> stream_type
            )
        throws ParametersMustNotBeNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        super ( input_pipes,
                stream_type );
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
            Object null1,
            Object null2,
            Object null3
            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  context, input_streams, output_streams,
                                  output_map, input_stream, maybe_input_term );

        final Term<VALUE> input_term_or_null =
            maybe_input_term.orNull ();

        if ( input_term_or_null == null )
        {
            // Nothing to merge.
            return;
        }

        // Output the term we just read in to all output streams:
        for ( Stream<VALUE> output_stream : output_streams )
        {
            if ( output_stream.state () != Stream.State.OPEN )
            {
                continue;
            }

            output_map.put ( output_stream, input_term_or_null );
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
            Object null1
            )
        throws ParametersMustNotBeNull.Violation
    {
        return null;
    }
}
