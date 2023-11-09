package musaico.foundation.graph;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * A graph of nodes, connected by arcs.
 * </p>
 *
 * <p>
 * A Graph can be used as the basis for a state machine, or to map
 * out relationships between objects, and so on.
 * </p>
 *
 *
 * <p>
 * In Java every Graph must be Serializable in order to
 * play nicely across RMI.  However users of the Graph
 * must be careful, since the nodes and arc inside might not
 * be Serializable -- leading to exceptions during serialization
 * of the parent Graph.
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
 * @see musaico.foundation.graph.MODULE#COPYRIGHT
 * @see musaico.foundation.graph.MODULE#LICENSE
 */
public interface Graph<NODE extends Object, ARC extends Object, PASS extends Object, STATE extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Returns the arc(s) leading out of the specified node.
     * </p>
     *
     * @param node The node out of which all arc(s) will be returned.
     *             Must not be null.
     *
     * @return The arcs which leave the specified node, in the order
     *         of evaluation they are stepped through during a traversal
     *         of this Graph.  Never null.
     */
    public abstract Value<ARC> arcs (
                                     NODE node
                                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Enters this Graph, setting up the initial state, and possibly
     * performing extra logic.
     * </p>
     *
     * @return The new state, after entering this Graph.  Can be
     *         No state, with an Error <code> cause () </code>
     *         (or a Blocking cause and so on); but is expected
     *         to be One state, the beginning state in this graph.
     */
    public abstract ZeroOrOne<STATE> enter ()
        throws ReturnNeverNull.Violation;


    /**
     * Every Graph must override:
     * @see java.lang.Object#equals(java.lang.Object)
     */


    /**
     * <p>
     * Leaves this Graph, possibly performing extra logic along the way.
     * </p>
     *
     * @param state The state to exit from.  Must not be null.
     *
     * @return The final state.  Can be No state, with
     *         an Error <code> cause () </code>
     *         (or a Blocking cause and so on); but is expected
     *         to be One state, the terminal state in this graph.
     */
    public abstract ZeroOrOne<STATE> exit (
                                           ZeroOrOne<STATE> state
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * Every Graph must override:
     * @see java.lang.Object#hashCode()
     */


    /**
     * @return All of the nodes in this Graph.  Never null.
     */
    public abstract Value<NODE> nodes ()
        throws ReturnNeverNull.Violation;


    /**
     * Every Graph must override:
     * @see java.lang.toString()
     */


    /**
     * <p>
     * Traverses the arc(s) identified by the specified pass(es),
     * then performing the logic attached to the arc at each step
     * to determine whether or not the traversal may proceed.
     * </p>
     *
     * <p>
     * The result is the sequence of states which were arrived at
     * successfully by traverser during the sequence of arc traversals:
     * </p>
     *
     * <ol>
     *   <li> No states, if no passes were specified. </li>
     *   <li> One state, if exactly one pass was specified, and it led
     *        to a successful arc traversal. </li>
     *   <li> Many states, if more than one passes were specified,
     *        and all of them led to successful arc traversals. </li>
     *   <li> Error state, if the very first pass did not lead
     *        to a successful arc traversal.
     *   <li> Partial states, if one or more of the passes led
     *        to successful arc traversal(s), but the next one did not,
     *        resulting in an Error state <code> cause () </code>
     *        of the Partial result. </li>
     *   <li> Various other possibilities, Blocking state changes,
     *        and so on.  In such cases, the traverser may choose
     *        to take special action, such as awaiting completion
     *        of each Blocking traversal. </li>
     * </ol>
     *
     * @param initial_state The starting state.  Traversal of the
     *                      arc(s) begins from the node specified by
     *                      this initial state.  Must not be null.
     *
     * @param passes The sequence of pass(es) to traverse arc(s)
     *               in this Graph.  Must not be null.  Must not contain
     *               any null elements.
     *
     * @return The sequence of state changes induced by the specified
     *         traversal sequence.  Never null.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution (passes).
    public abstract Maybe<STATE> traverse (
                                           ZeroOrOne<STATE> initial_state,
                                           PASS ... passes
                                           )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;
}
