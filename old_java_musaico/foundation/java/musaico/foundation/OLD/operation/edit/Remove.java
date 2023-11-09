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

import musaico.foundation.term.abnormal.Discarded;
import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.countable.No;

import musaico.foundation.term.infinite.Cyclical;

import musaico.foundation.term.multiplicities.OneOrMore;


/**
 * <p>
 * Removes specific element(s) from the incoming Term(s).
 * </p>
 *
 * <p>
 * The input Term(s) to a Remove Pipe must be divided into those
 * that are "kept" (the elements to remove), and those that are Discarded
 * (the elements which are not to be removed).  The Discarded
 * inputs are unwrapped and sent to the output stream, while the
 * removed inputs are not.
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
public class Remove<VALUE extends Object>
    extends AbstractPipe<VALUE, VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( Remove.class );


    /**
     * <p>
     * Creates a new stateless Remove template.
     * </p>
     *
     * @param input_pipe The upstream Pipe, from which we read
     *                   the element(s) to remove (kept Term(s))
     *                   and the rest (Discarded Term(s)).
     *                   Must not be null.
     *
     * @param downstream_type The Type of kept Term(s) to write downstream.
     *                        Must not be null.
     */
    public Remove (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( input_pipe,      // input_pipe
               downstream_type, // downstream_type
               null );          // upstream_or_null
    }


    /**
     * <p>
     * Creates a new Remove.
     * </p>
     *
     * @param input_pipe The upstream Pipe, from which we read
     *                   the element(s) to remove (kept Term(s))
     *                   and the rest (Discarded Term(s)).
     *                   Must not be null.
     *
     * @param upstream_or_null Either the Stream coming from the
     *                         input_pipe from which we read input Term(s),
     *                         or null if this is a stateless template Remove.
     *                         Must not be null.
     *
     * @param downstream_type The Type of kept Term(s) to write downstream.
     *                        Must not be null.
     */
    public Remove (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type,
            Stream<VALUE> upstream_or_null
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( input_pipe,           // input_pipe
                downstream_type,      // downstream_type
                upstream_or_null );   // upstream_or_null
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
                                  upstream, downstream );

        final Term<VALUE> input = upstream.read ().orNull ();
        if ( input == null )
        {
            // No more input, we're done.
            return false;
        }
        else if ( ! ( input instanceof Discarded ) )
        {
            // Remove this input Term.
            return true;
        }

        final Discarded<VALUE> not_to_remove = (Discarded<VALUE>) input;
        final Term<VALUE> input_not_removed = not_to_remove.unwrap ();
        final Pipe.State downstream_state =
            downstream.write ( input_not_removed );
        if ( downstream_state == Pipe.State.OPEN )
        {
            // Continue removing.
            return true;
        }
        else
        {
            // The downstream Pipe is closed, so we're done.
            return false;
        }
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#instantiate()
     *
     * Can be overridden by derived classes (such as Pad).
     */
    protected Remove<VALUE> instantiate ()
        throws ReturnNeverNull.Violation
    {
        final Stream<VALUE> input_stream =
            this.createInputStream ();
        return new Remove<VALUE> (
            this.inputPipe (),          // input_pipe
            this.downstreamType (),     // downstream_type
            input_stream );             // upstream
    }
}
