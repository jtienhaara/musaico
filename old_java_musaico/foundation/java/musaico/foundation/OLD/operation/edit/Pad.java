package musaico.foundation.operation.edit;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.operation.Pipe;
import musaico.foundation.operation.Stream;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.contracts.ValueMustBeJust;

import musaico.foundation.term.infinite.Cyclical;


/**
 * <p>
 * Inserts padding elements to bulk up each input Term to a
 * specific length.
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
public class Pad<VALUE extends Object>
    extends Insert<VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The Pipe of Long indices specifying where
    // to insert the elements.
    private final Pipe<?, Long> targetLength;

    // The Stream of indices reading from the insertAt Pipe, if this
    // is an Insert instance; or null, if this is a template.
    private final Stream<Long> targetLengthStream;

    // MUTABLE:
    // ONLY ACCESS FROM WITHIN insert () from within step ()
    // in order to guarantee synchronized access.
    // The length to which the input Term(s) will be padded.
    // Null until step ( ... ) calls out insert ( ... ) method.
    private Long padToLength = null;

    // MUTABLE:
    // ONLY ACCESS FROM WITHIN insert () from within step ()
    // in order to guarantee synchronized access.
    // The remaining input Term(s) that will come after we insert
    // the padding.
    private List<Term<VALUE>> remainingInputs = null;


    /**
     * <p>
     * Creates a new stateless Pad template.
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
     *
     * @param target_length The Pipe of Long target length specifying
     *                      how many elements the output should contain,
     *                      including padding.  Must not be null.
     */
    public Pad (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type,
            Pipe<?, Long> insert_at,
            Pipe<?, VALUE> inserts,
            Pipe<?, Long> target_length
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( input_pipe,      // input_pipe
               downstream_type, // downstream_type
               null,            // upstream_or_null
               insert_at,       // insert_at
               inserts,         // inserts
               target_length ); // target_length
    }


    /**
     * <p>
     * Creates a new Pad.
     * </p>
     *
     * @param input_pipe The upstream Pipe, from which we read Term(s).
     *                   Must not be null.
     *
     * @param upstream_or_null Either the Stream coming from the
     *                         input_pipe from which we read input Term(s),
     *                         or null if this is a stateless template Pad.
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
     *
     * @param target_length The Pipe of Long target length specifying
     *                      how many elements the output should contain,
     *                      including padding.  Must not be null.
     */
    public Pad (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type,
            Stream<VALUE> upstream_or_null,
            Pipe<?, Long> insert_at,
            Pipe<?, VALUE> inserts,
            Pipe<?, Long> target_length
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( input_pipe,           // input_pipe
                downstream_type,      // downstream_type
                upstream_or_null,     // upstream_or_null
                insert_at,            // insert_at
                inserts );            // inserts

        this.targetLength = target_length;

        if ( this.state () == Pipe.State.STATELESS )
        {
            // Template.
            this.targetLengthStream = null;
        }
        else
        {
            // Instance.
            // Create a stream for each parameter.
            this.targetLengthStream = // Any old stream.
                this.createParameterStream ( this.targetLength );
        }
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#instantiate()
     */
    protected final Pad<VALUE> instantiate ()
        throws ReturnNeverNull.Violation
    {
        final Stream<VALUE> input_stream =
            this.createInputStream ();
        return new Pad<VALUE> (
            this.inputPipe (),          // input_pipe
            this.downstreamType (),     // downstream_type
            input_stream,               // upstream
            this.insertAt (),           // insert_at
            this.inserts (),            // inserts
            this.targetLength );        // target_length
    }


    /**
     * @see musaico.foundation.operation.edit.Insert#insert(musaico.foundation.operation.Stream, musaico.foundation.operation.Stream, musaico.foundation.operation.Stream)
     */
    @Override
    protected final boolean insert (
            Stream<VALUE> upstream,
            Stream<VALUE> downstream,
            Stream<VALUE> inserts_buffer
            )
        throws ParametersMustNotBeNull.Violation
    {
        if ( this.padToLength == null )
        {
            final Term<Long> maybe_pad_to_length =
                this.targetLengthStream.read ()
                                       .orNull ();
            if ( maybe_pad_to_length != null )
            {
                this.padToLength = maybe_pad_to_length.orNull ();
            }

            if ( this.padToLength == null )
            {
                final TermViolation violation =
                    ValueMustBeJust.CONTRACT.violation (
                        this,                  // plaintiff
                        maybe_pad_to_length ); // evidence; Could be null.
                final Error<VALUE> error =
                    new Error<VALUE> ( this.downstreamType (),
                                       violation );
                downstream.write ( error );

                // We can't insert any padding, because there's
                // no target length.
                // Finished.
                this.close (); // Seal off this Pipe instance.
                return false;
            }
        }

        if ( upstream.inputPipeState () == Pipe.State.OPEN )
        {
            final Term<VALUE> input =
                upstream.read ()
                        .orNull ();
            if ( input != null )
            {
                // Read in all the rest of the input, so we can
                // figure out the length of the input before
                // we start padding it.
                if ( this.remainingInputs == null )
                {
                    this.remainingInputs = new ArrayList<Term<VALUE>> ();
                }

                this.remainingInputs.add ( input );

                return true;
            }

            final long length_so_far = upstream.length ();
            if ( length_so_far < 0L
                 || length_so_far >= this.padToLength.longValue () )
            {
                // Either there was a problem reading the input(s)
                // (length_so_far < 0L), or we have
                // no space left for any padding.
                // Finish outputting the input terms, then we're done.
                for ( Term<VALUE> queued_input : this.remainingInputs )
                {
                    downstream.write ( queued_input );
                }

                this.close (); // Seal off this Pipe instance.
                return false;
            }
        }

        // Now pad the output.
        final Term<VALUE> padding =
            inserts_buffer.read ().orNull ();
        if ( padding == null )
        {
            // Finish outputting the input terms, then we're done.
            for ( Term<VALUE> queued_input : this.remainingInputs )
            {
                downstream.write ( queued_input );
            }

            this.close (); // Seal off this Pipe instance.
            return false;
        }

        // Still inserting.
        final long length_so_far = upstream.length ();

        // figure out how long we need to make this input.
        final long pad_to_length = this.padToLength.longValue ();
        long padding_length = pad_to_length - length_so_far;

        final Countable<VALUE> finite;
        final Countable<VALUE> cycle;
        if ( padding instanceof Countable )
        {
            finite = (Countable<VALUE>) padding;
            cycle = null;
        }
        else if ( padding instanceof Cyclical )
        {
            final Cyclical<VALUE> cyclical = (Cyclical<VALUE>) padding;
            finite = cyclical.header ();
            cycle = cyclical.cycle ();
        }
        else
        {
            // We can't pad using a Term that we don't understand
            // (Acyclical, Error, and so on).
            final TermViolation violation =
                Stream.TERM_MUST_BE_COUNTABLE_OR_CYCLICAL.violation (
                    this,      // plaintiff
                    padding ); // evidence
            final Error<VALUE> error =
                new Error<VALUE> (
                    this.downstreamType (),
                    violation );
            downstream.write ( error );

            // Error.  Finished.
            this.close (); // Seal off this Pipe instance.
            return false;
        }

        final long finite_length = finite.length ();
        if ( finite_length >= padding_length )
        {
            // Reduce the size of the padding, if necessary.
            final Countable<VALUE> sub_padding;
            if ( finite_length == padding_length )
            {
                sub_padding = finite;
            }
            else
            {
                sub_padding =
                    finite.range ( 0L, padding_length - 1L );
            }

            // Write out enough padding to hit the target length.
            Pipe.State downstream_state =
                downstream.write ( sub_padding );
            if ( downstream_state != Pipe.State.OPEN )
            {
                // Downstream is closed, so we are finished.
                this.close (); // Seal off this Pipe instance.
                return false;
            }

            // Finish outputting the input terms, then we're done.
            for ( Term<VALUE> queued_input : this.remainingInputs )
            {
                downstream_state =
                    downstream.write ( queued_input );

                if ( downstream_state == Pipe.State.OPEN )
                {
                    // Continue inserting.
                    return true;
                }
                else
                {
                    // Downstream is closed, so we are finished.
                    this.close (); // Seal off this Pipe instance.
                    return false;
                }
            }

            this.close (); // Seal off this Pipe instance.
            return false;
        }

        // The finite component of the current padding term is
        // shorter than what we need, so output it then keep looking
        // for more padding.
        Pipe.State downstream_state =
            downstream.write ( finite );
        if ( downstream_state != Pipe.State.OPEN )
        {
            // Downstream is closed, so we are finished.
            this.close (); // Seal off this Pipe instance.
            return false;
        }

        padding_length -= finite_length;

        if ( cycle == null )
        {
            // We'll carry on padding next time step () calls us.
            return true;
        }

        // All the padding we need is here, since this is an infinite
        // cycle.
        final long cycle_length = cycle.length ();
        while ( padding_length > 0L )
        {
            // Reduce the size of the padding, if necessary.
            final Countable<VALUE> sub_padding;
            if ( cycle_length <= padding_length )
            {
                sub_padding = cycle;
            }
            else
            {
                sub_padding =
                    cycle.range ( 0L, padding_length - 1L );
            }

            // Write out enough padding to hit the target length.
            downstream_state =
                downstream.write ( sub_padding );

            if ( downstream_state == Pipe.State.OPEN )
            {
                // Continue inserting.
                return true;
            }
            else
            {
                // Downstream is closed, so we are finished.
                this.close (); // Seal off this Pipe instance.
                return false;
            }
        } // Keep writing out more padding from the cycle, if necessary.

        // Finish outputting the input terms, then we're done.
        for ( Term<VALUE> queued_input : this.remainingInputs )
        {
            downstream_state =
                downstream.write ( queued_input );
            if ( downstream_state == Pipe.State.OPEN )
            {
                // Continue inserting.
                return true;
            }
            else
            {
                // Downstream is closed, so we are finished.
                this.close (); // Seal off this Pipe instance.
                return false;
            }
        }

        this.close (); // Seal off this Pipe instance.
        return false;
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#parameters(java.util.List)
     *
     * Can be overridden by derived Pipes that add parameters or
     * provide fixed constants to Pad as its parameters.
     */
    @Override
    protected List<Pipe<?, ?>> parameters (
            List<Pipe<?, ?>> parameters_list
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        parameters_list = super.parameters ( parameters_list );
        parameters_list.add ( this.targetLength );
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
        parameter_streams_list =
            super.parameterStreams ( parameter_streams_list );
        if ( this.targetLengthStream != null )
        {
            parameter_streams_list.add ( this.targetLengthStream );
        }

        return parameter_streams_list;
    }
}
