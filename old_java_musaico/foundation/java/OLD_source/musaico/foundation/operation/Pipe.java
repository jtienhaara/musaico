package musaico.foundation.operation;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.pipeline.Construct;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;


/**
 * <p>
 * A (possibly iterative or recursive) primitive operation which can be
 * connected to a graph of other Pipe(s) to perform processing of Terms.
 * </p>
 *
 * <p>
 * Based on a components-and-ports structure (such as Flow-Based Programming
 * see J. Paul Morrison's website: http://www.jpaulmorrison.com/fbp,
 * or his excellent book), but tailored to be a little bit more
 * closely related to functional programming, and specifically
 * inspired by the declarative recursion in inductive types.
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
public interface Pipe<FROM extends Object, TO extends Object>
    extends Construct<Pipe<FROM, TO>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The Type of Maybe-Term that would be returned by a call to
     *         <code> read () </code> on any Stream reading from this Pipe.
     *         Never null.
     */
    public abstract Type<Term<TO>> downstreamReadType ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The Type of Terms flowing out of this Pipe whenever
     *         it writes downstream.  Never null.
     */
    public abstract Type<TO> downstreamType ()
        throws ReturnNeverNull.Violation;


    // Every Pipe must implement java.lang.Object#equals(java.lang.Object)

    // Every Pipe must implement java.lang.Object#hashCode()


    /**
     * @return This Pipe's input Pipe(s).  Can be
     *         <code> No&lt;Pipe&lt;...&gt;&gt; </code>
     *         if this is the first Pipe in a Pipeline.  Never null.
     */
    public abstract Countable<Pipe<?, FROM>> inputPipes ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The greatest number of output Pipe(s) to which this Pipe
     *         can write its outputs.  Typically 1L, but can be 0L
     *         (for example if this Pipe writes to external systems
     *         rather than continuing the pipeline), or can be
     *         more than 1L (for example if this Pipe splits its
     *         inputs into multiple output Pipes, or filters
     *         its inputs into a KEPT output Pipe and a DISCARDED
     *         output Pipe, and so on).  Always greater than
     *         or equal to <code> minimumOutputPipes () </code>.
     */
    public abstract long maximumOutputPipes ()
        throws Return.AlwaysGreaterThan.Violation;


    /**
     * @return The least number of output Pipe(s) to which this Pipe
     *         can write its outputs.  Typically 1L, but can be 0L
     *         (for example if this Pipe writes to external systems
     *         rather than continuing the pipeline), or can be
     *         more than 1L (for example if this Pipe splits its
     *         inputs into multiple output Pipes, or filters
     *         its inputs into a KEPT output Pipe and a DISCARDED
     *         output Pipe, and so on).  Always greater than
     *         or equal to 0L.
     */
    public abstract long minimumOutputPipes ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * <p>
     * Performs one iterative or recursive step, writing out
     * 0 or more Term(s) (though usually only 0 or 1) to the downstream(s)
     * in the specified Context.
     * </p>
     *
     * @param context The Context in which to step this Pipe forward
     *                one step, flowing Term(s) down the stream(s) defined
     *                in the Context.  Must not be null.
     *
     * @return True if this Pipe is still stepping and might produce
     *         more Term(s); false if this Pipe cannot produce
     *         any more Terms, and so should terminate.
     */
    public abstract boolean step (
            Context context
            )
        throws ParametersMustNotBeNull.Violation;


    // Every Pipe must implement java.lang.Object#toString()


    /**
     * @return The Type of Terms flowing into this Pipe whenever
     *         it <code> read () </code>s from <code> upstreams () </code>.
     *         Never null.
     */
    public abstract Type<FROM> upstreamType ()
        throws ReturnNeverNull.Violation;
}
