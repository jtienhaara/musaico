package musaico.foundation.graph;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Value;
import musaico.foundation.value.ValueClass;
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
 * <p>
 * Graph nodes are often called vertices, states, points, and so on.
 * Graph arcs are often called edges.
 * </p>
 *
 * <p>
 * <b> WARNING: </b> Unless a particular Graph is an ImmutableGraph,
 * its nodes and arcs may change over time.
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
public interface Graph<NODE extends Object, ARC extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Returns the specified One Arc only if it exists in this Graph;
     * No Arc otherwise.
     * </p>
     *
     * @param arc The Arc which may or may not be in this Graph.
     *            Must not be null.
     *
     * @return The specified One Arc, if it is in this Graph;
     *         or No Arc, if it is not an Arc in this Graph.
     *         Never null.
     */
    public abstract ZeroOrOne<Arc<NODE, ARC>> arc (
                                                   Arc<NODE, ARC> arc
                                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The class of arcs in this Graph.  Never null.
     */
    public abstract ValueClass<ARC> arcValueClass ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns all arcs to and from the nodes in this Graph.
     * </p>
     *
     * @return All arcs in this Graph.  Can be No arcs.  Never null.
     */
    public abstract Value<Arc<NODE, ARC>> arcs ()
        throws ReturnNeverNull.Violation;


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
     *         of this Graph.  Can be No arcs.  Never null.
     */
    public abstract Value<Arc<NODE, ARC>> arcs (
                                                NODE node
                                                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The entry node, or source of this Graph; the starting point
     *         for traversing the arcs and nodes in this graph.  If
     *         this Graph is not oriented, then the entry node may
     *         connect directly to all non-exit nodes in the graph.
     *         Never null.
     */
    public abstract NODE entry ()
        throws ReturnNeverNull.Violation;


    /**
     * Every Graph must override:
     * @see java.lang.Object#equals(java.lang.Object)
     */


    /**
     * @return The exit node, or sink, from this Graph; the terminal node.
     *         If this Graph is not oriented, then all non-entry nodes
     *         in the graph may connect directly to the exit node.
     *         Never null.
     */
    public abstract NODE exit ()
        throws ReturnNeverNull.Violation;


    /**
     * Every Graph must override:
     * @see java.lang.Object#hashCode()
     */


    /**
     * @return An unchangeable copy of the current state of this Graph.
     *         The returned Graph will never change.
     *         If this Graph is already immutable, then it will be
     *         returned as-is.  Never null.
     */
    public abstract ImmutableGraph<NODE, ARC> immutable ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the specified One node only if it exists in this Graph;
     * No node otherwise.
     * </p>
     *
     * @param node The node which may or may not be in this Graph.
     *             Must not be null.
     *
     * @return The specified One node, if it is in this Graph;
     *         or No node, if it is not in this Graph.
     *         Never null.
     */
    public abstract ZeroOrOne<NODE> node (
                                          NODE node
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The class of nodes in this Graph.  Never null.
     */
    public abstract ValueClass<NODE> nodeValueClass ()
        throws ReturnNeverNull.Violation;


    /**
     * @return All of the nodes in this Graph.  Never null.
     */
    public abstract Value<NODE> nodes ()
        throws ReturnNeverNull.Violation;


    /**
     * @return Any and all sub-graphs which are included directly in this
     *         Graph.  Their nodes and arcs are not included in the
     *         outputs from <code> nodes () </code>
     *         and <code> arcs () </code> since stack logic must be
     *         implemented to push down into / pop out from each
     *         sub-graph.  Sub-graphs can help conserve memory, and
     *         are also the only foundational way to include
     *         mutable graphs which change over time.  Never null.
     */
    public abstract Value<SubGraph<NODE, ARC>> subGraphs ();


    /**
     * Every Graph must override toString () with a short description:
     * @see java.lang.toString()
     */


    /**
     * @return A detailed description of this Graph, in human-readable
     *         form.  Can contain newlines and so on.  Never null.
     */
    public abstract String toStringDetails ();
}
