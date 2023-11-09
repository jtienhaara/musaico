package musaico.foundation.operation.edit;

import java.io.Serializable;

import java.util.Iterator;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.operation.AbstractPipe;
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
 * A Pipe which prepends Terms to the end of the input stream.
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
public class Prepend<VALUE extends Object>
    extends AbstractPipe<VALUE, VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( Prepend.class );


    // The Pipe of Term(s) to prepend to the end of the input stream.
    private final Pipe<?, VALUE> prepends;

    // The Stream of Term(s) to prepend, if this is a Prepend instance;
    // or null, if this is a template.
    private final Stream<VALUE> prependsStream;


    /**
     * <p>
     * Creates a new stateless Prepend template.
     * </p>
     *
     * @param input_pipe The upstream Pipe, from which we read Term(s).
     *                   Must not be null.
     *
     * @param downstream_type The Type of Term(s) to write downstream.
     *                        Must not be null.
     *
     * @param prepends The Pipe of Term(s) to prepend to the end
     *                of the input stream.  Must not be null.
     */
    public Prepend (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type,
            Pipe<?, VALUE> prepends
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( input_pipe,      // input_pipe
               downstream_type, // downstream_type
               null,            // upstream_or_null
               prepends );       // prepends
    }


    /**
     * <p>
     * Creates a new Prepend.
     * </p>
     *
     * @param input_pipe The upstream Pipe, from which we read Term(s).
     *                   Must not be null.
     *
     * @param upstream_or_null Either the Stream coming from the
     *                         input_pipe from which we read input Term(s),
     *                         or null if this is a stateless template Prepend.
     *                         Must not be null.
     *
     * @param downstream_type The Type of Term(s) to write downstream.
     *                        Must not be null.
     *
     * @param prepends The Pipe of Term(s) to prepend to the end
     *                of the input stream.  Must not be null.
     */
    public Prepend (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type,
            Stream<VALUE> upstream_or_null,
            Pipe<?, VALUE> prepends
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( input_pipe,           // input_pipe
                downstream_type,      // downstream_type
                upstream_or_null );   // upstream_or_null

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               prepends );

        this.prepends = prepends;

        if ( this.state () == Pipe.State.STATELESS )
        {
            // Template.
            this.prependsStream = null;
        }
        else
        {
            // Instance.
            // Create a stream for each parameter.
            this.prependsStream =
                this.createParameterStream ( this.prepends );
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
                                  this.prependsStream );

        if ( this.state () == Pipe.State.OPEN )
        {
            final Term<VALUE> input = upstream.read ().orNull ();
            if ( input != null )
            {
                final Pipe.State downstream_state =
                    downstream.write ( input );
                if ( downstream_state == Pipe.State.OPEN )
                {
                    // Continue writing out the rest of the input, since we
                    // have nothing more to prepend.
                    return true;
                }
                else
                {
                    // Downstream is closed, so we are finished.
                    return false;
                }
            }

            // The input stream is now done.
            // We seal ourselves off from the input pipe, and
            // carry on prepending below.
            this.close (); // Seal off this Pipe instance.
        }

        final Term<VALUE> prependix =
            this.prependsStream.read ().orNull ();
        if ( prependix == null )
        {
            // Nothing more to prepend, we're done.
            return false;
        }

        // Still prepending.
        final Pipe.State downstream_state =
            downstream.write ( prependix );
        if ( downstream_state == Pipe.State.OPEN )
        {
            // Continue prepending.
            return true;
        }
        else
        {
            // Downstream is closed, so we are finished.
            return false;
        }
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#instantiate()
     */
    protected final Prepend<VALUE> instantiate ()
        throws ReturnNeverNull.Violation
    {
        final Stream<VALUE> input_stream =
            this.createInputStream ();
        return new Prepend<VALUE> (
            this.inputPipe (),          // input_pipe
            this.downstreamType (),     // downstream_type
            input_stream,               // upstream
            this.prepends );             // prepends
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#parameters(java.util.List)
     *
     * Can be overridden by derived Pipes that add parameters or
     * provide fixed constants to Prepend as its parameters.
     */
    @Override
    protected List<Pipe<?, ?>> parameters (
            List<Pipe<?, ?>> parameters_list
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        parameters_list.add ( this.prepends );
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
        if ( this.prepends != null )
        {
            parameter_streams_list.add ( this.prependsStream );
        }

        return parameter_streams_list;
    }
}
