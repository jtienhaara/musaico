package musaico.foundation.graph;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * A graph which may change over time; it may add or remove nodes,
 * and add or remove arcs between nodes.
 * </p>
 *
 * <p>
 * Every method call must be thread-safe.  However the caller must
 * use caution since any subsequent method call may not be to
 * the same graph structure.  For example, iterating through
 * the <code> nodes () </code> of a MutableGraph, and then stepping
 * through the <code> arcs ( node ) </code> of each node, may result
 * in attempting to iterate through the arcs of a node which is
 * no longer in the graph.
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
public interface MutableGraph<NODE extends Object, ARC extends Object>
    extends Graph<NODE, ARC>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return An Immutable copy of the current state of this Graph.
     *         The returned Graph will never change.  Never null.
     */
    public abstract ImmutableGraph<NODE, ARC> immutable ()
        throws ReturnNeverNull.Violation;
}
