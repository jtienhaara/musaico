package musaico.foundation.operation.edit;

import java.io.Serializable;

import java.util.Iterator;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.BloomFilter;
import musaico.foundation.domains.ClassName;

import musaico.foundation.operation.AbstractPipe;
import musaico.foundation.operation.Buffer;
import musaico.foundation.operation.Pipe;
import musaico.foundation.operation.Stream;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Finite;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Operation;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.builder.TermBuilder;

import musaico.foundation.term.contracts.TermMustMeetAllContracts;
import musaico.foundation.term.contracts.TermMustMeetAtLeastOneContract;
import musaico.foundation.term.contracts.ValueMustBeCountable;

import musaico.foundation.term.countable.Many;
import musaico.foundation.term.countable.No;
import musaico.foundation.term.countable.One;

import musaico.foundation.term.infinite.Cyclical;
import musaico.foundation.term.infinite.CyclicalCycleMustMeet;
import musaico.foundation.term.infinite.CyclicalHeaderMustMeet;
import musaico.foundation.term.infinite.TermMustBeCyclical;

import musaico.foundation.term.multiplicities.OneOrMore;


/**
 * <p>
 * A Pipe which outputs the asymmetric set difference
 * of the main input stream from another stream (input - another),
 * leaving only the elements which are found in the input stream
 * but are not in the other stream, and replacing each removed
 * element with a specific sequence of element(s).
 * </p>
 *
 * <p>
 * Each stream must be read in its entirety before anything can be output.
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
public class Map<VALUE extends Object>
    extends AbstractSetOperation<VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( Map.class );


    // The Pipe of Term(s) to replace each element removed
    // from the input stream.
    private final Pipe<?, VALUE> replacement;

    // The Buffer of replacement Term(s), if this is a Map instance;
    // or null, if this is a template.
    private final Buffer<VALUE> replacementBuffer;


    /**
     * <p>
     * Creates a new stateless Map template.
     * </p>
     *
     * @param input_pipe The upstream Pipe, from which we read Term(s).
     *                   Must not be null.
     *
     * @param downstream_type The Type of Term(s) to write downstream.
     *                        Must not be null.
     *
     * @param other The Pipe of Term(s) to subtract
     *              from the main input stream.  Must not be null.
     *
     * @param replacement The Pipe of Term(s) to replace each element removed
     *                    from the input stream.  Must not be null.
     */
    public Map (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type,
            Pipe<?, VALUE> other,
            Pipe<?, VALUE> replacement
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( input_pipe,      // input_pipe
               downstream_type, // downstream_type
               null,            // upstream_or_null 
               other,           // other
               replacement );   // replacement
    }


    /**
     * <p>
     * Creates a new Map.
     * </p>
     *
     * @param input_pipe The upstream Pipe, from which we read Term(s).
     *                   Must not be null.
     *
     * @param upstream_or_null Either the Stream coming from the
     *                         input_pipe from which we read input Term(s),
     *                         or null if this is a stateless template
     *                         Map.  Must not be null.
     *
     * @param downstream_type The Type of Term(s) to write downstream.
     *                        Must not be null.
     *
     * @param other The Pipe of Term(s) to subtract
     *              from the main input stream.  Must not be null.
     *
     * @param replacement The Pipe of Term(s) to replace each element removed
     *                    from the input stream.  Must not be null.
     */
    public Map (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type,
            Buffer<VALUE> upstream_or_null,
            Pipe<?, VALUE> other,
            Pipe<?, VALUE> replacement
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( input_pipe,           // input_pipe
                downstream_type,      // downstream_type
                upstream_or_null,     // upstream_or_null
                other );              // other

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               replacement );

        this.replacement = replacement;

        if ( this.state () == Pipe.State.STATELESS )
        {
            // Template.
            this.replacementBuffer = null;
        }
        else
        {
            // Instance.
            // Create a stream for each parameter.
            this.replacementBuffer = // Always a buffer.
                this.createParameterBuffer ( this.replacement );
        }
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#instantiate()
     */
    protected final Map<VALUE> instantiate ()
        throws ReturnNeverNull.Violation
    {
        final Buffer<VALUE> input_buffer =
            this.createInputStream ();
        return new Map<VALUE> (
            this.inputPipe (),          // input_pipe
            this.downstreamType (),     // downstream_type
            input_buffer,               // upstream
            this.other (),              // other
            this.replacement );         // replacement
    }


    /**
     * @see musaico.foundation.operation.edit.AbstractSetOperation#different(musaico.foundation.term.TermBuilder, java.lang.Object)
     */
    @Override
    protected final TermViolation different (
            TermBuilder<VALUE> replace_builder,
            VALUE element
            )
        throws ParametersMustNotBeNull.Violation
    {
        // Add the element to the replace builder.
        replace_builder.add ( element );

        // No error.
        return null;
    }


    /**
     * @see musaico.foundation.operation.edit.AbstractSetOperation#intersecting(musaico.foundation.term.TermBuilder, java.lang.Object)
     */
    @Override
    protected final TermViolation intersecting (
            TermBuilder<VALUE> replace_builder,
            VALUE element
            )
        throws ParametersMustNotBeNull.Violation
    {
        // Add the sequence of replacement element(s)
        // to the replace builder.
        Term<VALUE> replacement_term;
        while ( ( replacement_term =
                      this.replacementBuffer.read ()
                                            .orNull () )
                != null )
        {
            if ( ! ( replacement_term instanceof Countable ) )
            {
                final ValueMustBeCountable.Violation violation =
                    ValueMustBeCountable.CONTRACT.violation (
                        this, // plaintiff
                        replacement_term );
                return violation;
            }

            final Countable<VALUE> countable_replacement =
                (Countable<VALUE>) replacement_term;
            replace_builder.addAll ( countable_replacement );
        }

        this.replacementBuffer.reset ();

        // No error.
        return null;
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
        parameters_list.add ( this.replacement );
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
        if ( this.replacementBuffer != null )
        {
            parameter_streams_list.add ( this.replacementBuffer );
        }

        return parameter_streams_list;
    }


    /**
     * @see musaico.foundation.operation.edit.AbstractSetOperation#setOperation(musaico.foundation.term.Term[], long, musaico.foundation.term.Term[], long, musaico.foundation.operation.Stream)
     */
    @Override
    protected final void setOperation (
            Term<VALUE> [] left,
            long left_length,
            Term<VALUE> [] right,
            long right_length,
            Stream<VALUE> downstream
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter4.MustBeGreaterThanOrEqualToZero.Violation
    {
        if ( left_length == 0L )
        {
            // Nothing on the left, so output nothing.
            final No<VALUE> no_output =
                new No<VALUE> ( downstream.type () );

            downstream.write ( no_output );

            return;
        }
        else if ( right_length == 0L )
        {
            // Nothing on the right.  Just output the left.
            for ( Term<VALUE> replace : left )
            {
                downstream.write ( replace );
            }

            return;
        }

        final long total_length;
        if ( ( Long.MAX_VALUE - left_length ) > right_length )
        {
            total_length = left_length + right_length;
        }
        else
        {
            total_length = Long.MAX_VALUE;
        }

        // We never filter out any elements of the right side;
        // we drop all right elements, and add the different left elements.
        final BloomFilter right_filter;
        if ( total_length < 32L )
        {
            // Don't bother with the BloomFilters.
            right_filter = null;
        }
        else if ( left_length < 16L )
        {
            right_filter = this.bloomFilter ( total_length );
        }
        else if ( right_length < 16L )
        {
            // Don't bother with a right Bloom filter.
            right_filter = null;
        }
        else
        {
            right_filter = this.bloomFilter ( total_length );
        }

        final TermBuilder<VALUE> replace_builder =
            new TermBuilder<VALUE> ( this.downstreamType () );

        for ( Term<VALUE> right_term : right )
        {
            // Build up the filter from the right side elements.
            // We will remove them all from the left side.
            final boolean is_continuing =
                this.add (
                    right_term,   // set
                    right_filter, // add_to_filter_or_null (Maybe null)
                    null,         // other_filter_or_null
                    null,         // other_terms_or_null
                    null,         // set_builder_or_null
                    downstream ); // downstream

            if ( ! is_continuing )
            {
                // Abort.
                return;
            }
        }

        for ( Term<VALUE> left_term : left )
        {
            // Output left elements that are not on the right side,
            // and replace each intersecting element
            // with the replacement sequence.
            final boolean is_continuing =
                this.add (
                    left_term,       // set
                    null,            // add_to_filter_or_null
                    right_filter,    // other_filter_or_null (Maybe null)
                    right,           // other_terms
                    replace_builder, // set_builder
                    downstream );    // downstream

            if ( ! is_continuing )
            {
                // Abort.
                return;
            }
        }
    }
}
