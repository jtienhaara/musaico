package musaico.foundation.graph;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;


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
     * Returns all arcs to and from the nodes in this Graph.
     * </p>
     *
     * @return All arcs in this Graph.  Can be empty.
     *         Never null.  Never contains any null elements.
     */
    public abstract ARC [] arcs ()
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Returns the arc(s) leading out of the specified node.
     * </p>
     *
     * @param node The node out of which all arc(s) will be returned.
     *             Must not be null.
     *
     * @return The arcs that leave the specified node, in the order
     *         of evaluation they are stepped through during a traversal
     *         of this Graph.  Can be 0 arcs.
     *         Never null.  Never contains any null elements.
     */
    public abstract ARC [] arcsFrom (
            NODE node
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Returns the node(s) from which the specified arc leads.
     * Typically (but not necessarily) one source node per arc.
     * </p>
     *
     * @param arc The arc whose source node will be returned.
     *            Must not be null.
     *
     * @return The source node(s) for the specified arc.
     *         Can be empty or more than 1, although these
     *         are atypical cases.  Usually an arc has
     *         exactly 1 source node, and exactly 1 target node.
     *         Never null.  Never contains any null elements.
     */
    public abstract NODE [] arcSources (
            ARC arc
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Returns the node(s) to which the specified arc leads.
     * Typically (but not necessarily) one target node per arc.
     * </p>
     *
     * @param arc The arc whose target node will be returned.
     *            Must not be null.
     *
     * @return The target node(s) for the specified arc.
     *         Can be empty or more than 1, although these
     *         are atypical cases.  Usually an arc has
     *         exactly 1 source node, and exactly 1 target node.
     *         Never null.  Never contains any null elements.
     */
    public abstract NODE [] arcTargets (
            ARC arc
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * @return The entry node, or source of this Graph; the starting point
     *         for traversing the arcs and nodes in this graph.
     *         Can be arbitrary, if this Graph is not oriented.
     *         Never null.
     */
    public abstract NODE entry ()
        throws Return.NeverNull.Violation;


    /**
     * Every Graph must override:
     * @see java.lang.Object#equals(java.lang.Object)
     */


    /**
     * @return The exit node, or sink, from this Graph; the terminal node.
     *         Can be arbitrary, if this Graph is not oriented.
     *         Never null.
     */
    public abstract NODE exit ()
        throws Return.NeverNull.Violation;


    /**
     * Every Graph must override:
     * @see java.lang.Object#hashCode()
     */


    /**
     * @return All of the nodes in this Graph.  Never null.
     *         Never contains any null elements.
     */
    public abstract NODE [] nodes ()
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


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
