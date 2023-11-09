package musaico.foundation.pipeline;

import java.io.Serializable;

import java.util.Collection;
import java.util.Set;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * A Pipeline which can be used to fork multiple sub-branches
 * for parallel and/or disjunctive processing.
 * </p>
 *
 * <p>
 * Fork and Join are parallel and sequential branches, respectively;
 * and/or disjunctive and conjunctive branches, respectively,
 * depending on the context.
 * </p>
 *
 * <p>
 * For examples, see the Pipeline <code> fork ( ... ) <code>
 * and <code> join ( ... ) </code> methods:
 * </p>
 *
 * @see musaico.foundation.pipeline.Sink#fork(musaico.foundation.pipeline.Source[])
 * @see musaico.foundation.pipeline.Sink#join(musaico.foundation.pipeline.Sink[])
 *
 * <p>
 * Pipelines are not necessarily specific to Types, Terms
 * and Operations (@see musaico.foundation.term), but since that is
 * what they were designed for, the examples here all related to those
 * classes of objects.
 * </p>
 *
 * @see musaico.foundation.pipeline.Sink#fork(musaico.foundation.pipeline.Source[])
 *
 *
 * <p>
 * In Java every Pipeline must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Pipeline must be Serializable in order to
 * play nicely across RMI.  However users of the Pipeline
 * must be careful, since the values and expected data stored inside
 * (if any) might not be Serializable.
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
 * @see musaico.foundation.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.pipeline.MODULE#LICENSE
 */
public interface Fork<STREAM extends Object, PARENT extends Sink<STREAM, PARENT, OUTPUT, JOIN_SOURCE, FORK_TARGET>, OUTPUT extends Object, JOIN_SOURCE extends Sink<STREAM, ?, ?, JOIN_SOURCE, FORK_TARGET>, FORK_TARGET extends Source<JOIN_SOURCE, STREAM, ?>, FORK extends Fork<STREAM, PARENT, OUTPUT, JOIN_SOURCE, FORK_TARGET, FORK>>
    extends Branch<STREAM, FORK, STREAM, PARENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#check()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#copy(musaico.foundation.pipeline.Pipeline)


    /**
     * @return A new Fork, which will fork together elements to come
     *         after this Fork.  Never null.
     */
    @SuppressWarnings("unchecked") // Generic varargs possible heap pollution.
    public abstract FORK fork (
            FORK_TARGET ... outputs
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;


    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Parent)

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#edit()

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#end()

    // Every Pipeline must implement java.lang.Object#equals(java.lang.Object)

    // Every Pipeline must implement java.lang.Object#hashCode()

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#isEnded()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#orderBy(musaico.foundation.order.Order[])

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#parent()

    // Every Branch must implement
    // musaico.foundation.pipeline.Branch#previousOrNull()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#select()

    // Every Pipeline must implement java.lang.Object#toString()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#when(musaico.foundation.filter.Filter)

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#where(musaico.foundation.filter.Filter[])
}
