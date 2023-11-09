package musaico.foundation.operation;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Operation;
import musaico.foundation.term.Term;


/**
 * <p>
 * An inductive Operation.
 * </p>
 *
 * <p>
 * Based on a components-and-ports structure, but tailored to
 * be a little bit more closely related to functions.
 * </p>
 *
 * <p>
 * The first time <code> apply ( ... ) </code> is called,
 * the stateless Pipe creates a stateful instance of itself, and
 * delegates the <code> apply ( ... ) </code> call to that instance.
 * It returns the instance to the caller, who can choose to call
 * it again, if the caller has more inputs to provide,
 * or to stop processing.
 * </p>
 *
 * </p>
 * When <code> apply ( ... ) </code> is invoked on a stateful Pipe instance,
 * it accepts the input, performs one step of its operation, and
 * if it has more processing to do, it returns itself; or if it has
 * no further processing to do, it returns No pipe.
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
 * @see musaico.foundation.operation.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.MODULE#LICENSE
 */
public interface Pipe<INPUT extends Object, OUTPUT extends Object>
    extends Operation<INPUT, Pipe<INPUT, OUTPUT>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @see musaico.foundation.term.Operation#apply(musaico.foundation.term.Term)
     *
     * @return A new stateful instance of this Pipe, if this is a stateless
     *         template Pipe; or this stateful Pipe, if there is more
     *         processing to do, and this can be applied to further inputs;
     *         or No Pipe, if this has received all the inputs it needs;
     *         or No Pipe if this Pipe has been <code> close () </code>d.
     *         Never null.
     */
    @Override
    public abstract Maybe<Pipe<INPUT, OUTPUT>> apply (
            Term<INPUT> input
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Signals an end to the input Terms that will be provided
     * to this Pipe.
     * </p>
     *
     * <p>
     * If this Pipe is a stateful instance, it can perform
     * any cleanup necessary.
     * </p>
     *
     * <p>
     * If this Pipe is stateless, this method has no effect.
     * </p>
     *
     * <p>
     * The <code> output () </code> of this Pipe will be closed, too.
     * There might be <code> sideEffects () </code> and/or
     *<code> parameters () </code> which will also be closed,
     * though it is up to the Pipe whether it closes any, some or all
     * such Pipes.
     * </p>
     *
     * <p>
     * After this call, invoking <code> apply ( ... ) </code> will have
     * no effect, and No Pipe will always be returned.
     * </p>
     */
    public void close ();


    // Every Operation must implement java.lang.Object#equals(java.lang.Object)

    // Every Operation must implement
    // musaico.foundation.term.Operation#errorType()

    // Every Operation must implement java.lang.Object#hashCode()


    /**
     * @return This Pipe's main input.  Can be this Pipe,
     *         if this is the source of the line.  Never null.
     */
    public abstract Pipe<?, INPUT> input ()
        throws ReturnNeverNull.Violation;


    // Every Operation must implement
    // musaico.foundation.term.Operation#inputType()


    /**
     * @return This Pipe's main output.  Can be this Pipe,
     *         if this is the end of the line.  Never null.
     */
    public abstract Pipe<OUTPUT, ?> output ()
        throws ReturnNeverNull.Violation;


    // Every Operation must implement
    // musaico.foundation.term.Operation#outputType()


    /**
     * <p>
     * Returns any and all Pipe(s), if any, which feed this Pipe,
     * in addition to the main <code> apply ( ... ) </code> input feed.
     * </p>
     *
     * <p>
     * Additional inputs are considered parameters.  For example,
     * a flow of numeric index Terms from another Pipe might specify
     * the elements to select from the input stream and feed
     * to the output pipe.  Or another Pipe might regulate the
     * flow through this Pipe via a blocking control Pipe.
     * And so on.
     * </p>
     *
     * @return All additional input Pipe(s) which feed this Pipe,
     *         such as control flow from other Pipes, and so on.
     *         Can be empty.  Never null.
     */
    public abstract Countable<Term<?>> parameters ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns any and all Pipe(s), if any, which this Pipe
     * feeds, in addition to the main outputs fed through this Pipe.
     * </p>
     *
     * <p>
     * Additional outputs are considered side-effects.  For example,
     * a control flow to other Pipes might cause those other Pipes
     * to change state whenever this Pipe receives an input
     * or produces an output.  Or a side-effect might accumulate
     * statistics about this Pipe, maintaining a state which can
     * be queried by interested parties.  And so on.
     * </p>
     *
     * @return All additional output Pipe(s) which this Pipe provides,
     *         such as control flow to other Pipes, and so on.
     *         Can be empty.  Never null.
     */
    public abstract Countable<Pipe<?, ?>> sideEffects ()
        throws ReturnNeverNull.Violation;


    // Every Operation must implement java.lang.Object#toString()
}
