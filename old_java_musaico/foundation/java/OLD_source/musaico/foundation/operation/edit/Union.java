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
 * A Pipe which outputs the set union (or join) of the main input stream
 * from another stream.
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
public class Union<VALUE extends Object>
    extends AbstractSetOperation<VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new stateless Union template.
     * </p>
     *
     * @param input_pipe The upstream Pipe, from which we read Term(s).
     *                   Must not be null.
     *
     * @param downstream_type The Type of Term(s) to write downstream.
     *                        Must not be null.
     *
     * @param other The Pipe of Term(s) to join (union)
     *              from the main input stream.  Must not be null.
     */
    public Union (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type,
            Pipe<?, VALUE> other
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( input_pipe,      // input_pipe
                downstream_type, // downstream_type
                other );         // other
    }


    /**
     * <p>
     * Creates a new Union.
     * </p>
     *
     * @param input_pipe The upstream Pipe, from which we read Term(s).
     *                   Must not be null.
     *
     * @param upstream_or_null Either the Stream coming from the
     *                         input_pipe from which we read input Term(s),
     *                         or null if this is a stateless template Union.
     *                         Must not be null.
     *
     * @param downstream_type The Type of Term(s) to write downstream.
     *                        Must not be null.
     *
     * @param other The Pipe of Term(s) to join (union)
     *              from the main input stream.  Must not be null.
     */
    public Union (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type,
            Buffer<VALUE> upstream_or_null,
            Pipe<?, VALUE> other
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( input_pipe,           // input_pipe
                downstream_type,      // downstream_type
                upstream_or_null,     // upstream_or_null
                other );              // other
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#instantiate()
     */
    protected final Union<VALUE> instantiate ()
        throws ReturnNeverNull.Violation
    {
        final Buffer<VALUE> input_buffer =
            this.createInputStream ();
        return new Union<VALUE> (
            this.inputPipe (),          // input_pipe
            this.downstreamType (),     // downstream_type
            input_buffer,               // upstream
            this.other () );            // other
    }


    /**
     * @see musaico.foundation.operation.edit.AbstractSetOperation#different(musaico.foundation.term.TermBuilder, java.lang.Object)
     */
    @Override
    protected final TermViolation different (
            TermBuilder<VALUE> union_builder,
            VALUE element
            )
        throws ParametersMustNotBeNull.Violation
    {
        // Add the element to the union builder.
        union_builder.add ( element );

        // No error.
        return null;
    }


    /**
     * @see musaico.foundation.operation.edit.AbstractSetOperation#intersecting(musaico.foundation.term.TermBuilder, java.lang.Object)
     */
    @Override
    protected final TermViolation intersecting (
            TermBuilder<VALUE> union_builder,
            VALUE element
            )
        throws ParametersMustNotBeNull.Violation
    {
        // Add the element to the union builder.
        union_builder.add ( element );

        // No error.
        return null;
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
            // Nothing on the left.  Just output the right.
            for ( Term<VALUE> union : right )
            {
                downstream.write ( union );
            }

            return;
        }
        else if ( right_length == 0L )
        {
            // Nothing on the right.  Just output the left.
            for ( Term<VALUE> union : left )
            {
                downstream.write ( union );
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

        // We never filter out any elements of the left side;
        // we keep all left elements, and add the different right elements.
        final BloomFilter left_filter;
        if ( total_length < 32L )
        {
            // Don't bother with the BloomFilters.
            left_filter = null;
        }
        else if ( left_length < 16L )
        {
            // Don't bother with a left Bloom filter.
            left_filter = null;
        }
        else if ( right_length < 16L )
        {
            left_filter = this.bloomFilter ( total_length );
        }
        else
        {
            left_filter = this.bloomFilter ( total_length );
        }

        final TermBuilder<VALUE> union_builder =
            new TermBuilder<VALUE> ( this.downstreamType () );

        for ( Term<VALUE> left_term : left )
        {
            // Output all left elements.
            final boolean is_continuing =
                this.add (
                    left_term,     // set
                    left_filter,   // add_to_filter_or_null (Maybe null)
                    null,          // other_filter_or_null
                    null,          // other_terms_or_null
                    union_builder, // set_builder_or_null
                    downstream );  // downstream

            if ( ! is_continuing )
            {
                // Abort.
                return;
            }
        }

        for ( Term<VALUE> right_term : right )
        {
            // Output right elements that are not already in the union.
            final boolean is_continuing =
                this.add (
                    right_term,    // set
                    null,          // add_to_filter_or_null
                    left_filter,   // other_filter_or_null (Maybe null)
                    left,          // other_terms
                    union_builder, // set_builder_or_null
                    downstream );  // downstream

            if ( ! is_continuing )
            {
                // Abort.
                return;
            }
        }
    }
}
