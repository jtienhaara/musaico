package musaico.foundation.graph;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Value;

import musaico.foundation.value.iterators.InfiniteLoopProtector;
import musaico.foundation.value.iterators.IteratorMustBeFinite;

/**
 * <p>
 * A guarantee that every node in each graph can be reached from the
 * graph's entry node and can reach the graph's exit node.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
 * nicely over RMI.
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
public class NodesMustBeConnected
    implements Contract<Graph<?, ?>, NodesMustBeConnected.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            NodesMustBeConnected.serialVersionUID;

        /**
         * <p>
         * Creates a new NodesMustBeConnected.Violation with the specified
         * details.
         * </p>
         *
         * @param contract The violated contract.  Must not be null.
         *
         * @param plaintiff The object whose contract was
         *                  violated.  Must not be null.
         *
         * @param evidence The graph which violated the contract.
         *                 Must not be null.
         */
        public <VIOLATING_NODE extends Object>
            Violation (
                       Contract<Graph<?, ?>, NodesMustBeConnected.Violation> contract,
                       Object plaintiff,
                       Graph<VIOLATING_NODE, ?> evidence
                       )
        {
            super ( contract,
                    "The graph " + evidence
                    + " contains disconnected node(s): "
                    + NodesMustBeConnected.listDisconnectedNodes ( evidence )
                    + " and/or has no entry or exit nodes"
                    + " and/or appears to be infinitely deep.", // description
                    plaintiff,
                    evidence );
        }
    }




    /** Ensures all nodes in every graph are connected. */
    public static final NodesMustBeConnected CONTRACT =
        new NodesMustBeConnected ();


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        return "Each graph's nodes must all be connected by at least"
            + " one path from the entry node (or source), and by"
            + " at least one path to the exit node (or sink).";
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null )
        {
            return false;
        }
        else if ( obj.getClass () != this.getClass () )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               Graph<?, ?> graph
                               )
    {
        return this.filterGenericized ( graph );
    }


    /*
     * <p>
     * Does the actual filtering work, which requires NODE and
     * ARC generic parameters -- which can't be used in the
     * base filter () override for filter ( Graph<?, ?> ).
     * </p>
     */
    private final <NODE extends Object, ARC extends Object>
        FilterState filterGenericized (
                                       Graph<NODE, ARC> graph
                                       )
    {
        if ( graph == null )
        {
            return FilterState.DISCARDED;
        }

        return NodesMustBeConnected.listDisconnectedNodes ( graph,
                                                            null ); // disconnected_nodes
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return ClassName.of ( this.getClass () ).hashCode ();
    }


    /**
     * <p>
     * Filters out the graph and optionally adds the problem nodes
     * to the specified set.
     * </p>
     *
     * @param graph The graph to either KEEP or DISCARD.  Must not be null.
     *
     * @return The Set of nodes which are not connected to the entry/exit
     *         node(s) in the graph.  Can be empty.  Never null.
     *         Never contains any null elements.
     */
    public static <CHECK_NODE extends Object, CHECK_ARC extends Object>
        Set<CHECK_NODE> listDisconnectedNodes (
                Graph<CHECK_NODE, CHECK_ARC> graph
                )
    {
        final Set<CHECK_NODE> disconnected_nodes =
            new HashSet<CHECK_NODE> ();

        NodesMustBeConnected.listDisconnectedNodes (
            graph,
            disconnected_nodes );

        return disconnected_nodes;
    }


    /**
     * <p>
     * Filters out the graph and optionally adds the problem nodes
     * to the specified set.
     * </p>
     *
     * @param graph The graph to either KEEP or DISCARD.  Must not be null.
     *
     * @param disconnected_nodes Optional Set to which each problem
     *                           node from the graph will be added.
     *                           If null, then the first time a problem
     *                           node is encountered, the DISCARDED
     *                           result will be returned right away
     *                           without further checks.  But if the
     *                           disconnected_nodes set is non-null,
     *                           then all problem nodes will be added to\
     *                           it before the KEPT or DISCARDED result
     *                           is returned.  Can be null.
     *
     * @return A KEPT filter state if the specified graph is connected;
     *         a DISCARDED filter state if the specified graph is
     *         not fully connected.  Never null.
     */
    public static <CHECK_NODE extends Object, CHECK_ARC extends Object>
        FilterState listDisconnectedNodes (
                                           Graph<CHECK_NODE, CHECK_ARC> graph,
                                           Set<CHECK_NODE> disconnected_nodes
                                           )
    {
        final Value<Arc<CHECK_NODE, CHECK_ARC>> arcs = graph.arcs ();
        final CHECK_NODE entry = graph.entry ();
        final CHECK_NODE exit = graph.exit ();

        // Every Graph must have an entry node and an exit node.
        // (They can be one and the same node.)
        if ( entry == null
             || exit == null )
        {
            return FilterState.DISCARDED;
        }

        final InfiniteLoopProtector finite = new InfiniteLoopProtector ();
        final Iterator<Arc<CHECK_NODE, CHECK_ARC>> infinite_loop_cause =
            arcs.iterator ();

        final Set<CHECK_NODE> from_entry = new HashSet<CHECK_NODE> ();
        final Set<CHECK_NODE> to_exit = new HashSet<CHECK_NODE> ();
        int old_num_enterable_nodes = -1;
        int old_num_exitable_nodes = -1;

        // Step through the arcs repeatedly, until we have determined
        // that either: all nodes are reachable from the entry node
        // and can also reach the exit node; or at least one node cannot
        // be connected.
        while ( from_entry.size () > old_num_enterable_nodes
                || to_exit.size () > old_num_exitable_nodes )
        {
            final IteratorMustBeFinite.Violation infinite_loop =
                finite.step ( infinite_loop_cause );
            if ( infinite_loop != null )
            {
                return FilterState.DISCARDED;
            }

            old_num_enterable_nodes = from_entry.size ();
            old_num_exitable_nodes = to_exit.size ();

            // Step through the arcs and add any to our "connected"
            // sets that are connected to the existing nodes and
            // which have not previously been added.
            for ( Arc<CHECK_NODE, CHECK_ARC> arc : arcs )
            {
                if ( entry.equals ( arc.from () ) )
                {
                    from_entry.add ( arc.from () );
                    from_entry.add ( arc.to () );
                }
                else if ( from_entry.contains ( arc.from () ) )
                {
                    from_entry.add ( arc.to () );
                }

                if ( exit.equals ( arc.to () ) )
                {
                    to_exit.add ( arc.to () );
                    to_exit.add ( arc.from () );
                }
                else if ( to_exit.contains ( arc.to () ) )
                {
                    to_exit.add ( arc.from () );
                }
            }

        }

        if ( from_entry.size () != to_exit.size () )
        {
            // One node either is not reachable from the entry node,
            // or cannot reach the exit node.
            if ( disconnected_nodes != null )
            {
                for ( CHECK_NODE no_path_to_exit : from_entry )
                {
                    if ( ! to_exit.contains ( no_path_to_exit ) )
                    {
                        disconnected_nodes.add ( no_path_to_exit );
                    }
                }

                for ( CHECK_NODE no_path_from_entry : to_exit )
                {
                    if ( ! from_entry.contains ( no_path_from_entry ) )
                    {
                        disconnected_nodes.add ( no_path_from_entry );
                    }
                }
            }
            else
            {
                return FilterState.DISCARDED;
            }
        }

        boolean is_entry_node_included = false;
        boolean is_exit_node_included = false;
        for ( CHECK_NODE node : graph.nodes () )
        {
            if ( entry.equals ( node ) )
            {
                is_entry_node_included = true;
            }

            if ( exit.equals ( node ) )
            {
                is_exit_node_included = true;
            }

            if ( ! from_entry.contains ( node ) )
            {
                // This node was not listed as a "to" node in any arc,
                // and is not the entry node.
                if ( disconnected_nodes != null )
                {
                    disconnected_nodes.add ( node );
                }
                else
                {
                    return FilterState.DISCARDED;
                }
            }
            else if ( ! to_exit.contains ( node ) )
            {
                // This node was not listed as a "from" node in any arc,
                // and is not the exit node.
                if ( disconnected_nodes != null )
                {
                    disconnected_nodes.add ( node );
                }
                else
                {
                    return FilterState.DISCARDED;
                }
            }

            from_entry.remove ( node );
            to_exit.remove ( node );
        }

        if ( from_entry.size () > 0 )
        {
            // At least one node was listed as a "to" node in an arc,
            // but is not in the list of all nodes for the graph.
            // The graph is connected, but it might be connected
            // through a node that is not even in the graph.  So fail.
            if ( disconnected_nodes != null )
            {
                for ( CHECK_NODE node : from_entry )
                {
                    disconnected_nodes.add ( node );
                }
            }
            else
            {
                return FilterState.DISCARDED;
            }
        }
        else if ( to_exit.size () > 0 )
        {
            // At least one node was listed as a "from" node in an arc,
            // but is not in the list of all nodes for the graph.
            // The graph is connected, but it might be connected
            // through a node that is not even in the graph.  So fail.
            if ( disconnected_nodes != null )
            {
                for ( CHECK_NODE node : to_exit )
                {
                    disconnected_nodes.add ( node );
                }
            }
            else
            {
                return FilterState.DISCARDED;
            }
        }

        if ( disconnected_nodes != null
             && disconnected_nodes.size () > 0 )
        {
            return FilterState.DISCARDED;
        }

        // All nodes are reachable from the entry node, and all nodes
        // can reach the exit node.  There are no nodes missing from
        // the arcs.  There are no arcs containing nodes that are
        // outside the graph.  Pass.
        return FilterState.KEPT;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public NodesMustBeConnected.Violation violation (
                                                     Object plaintiff,
                                                     Graph<?, ?> evidence
                                                     )
    {
        return new NodesMustBeConnected.Violation ( this,
                                                    plaintiff,
                                                    evidence );
    }


    /**
     * <p>
     * Helper method.  Always passes this NodesMustBeConnected contract
     * as the first parameter to the full method, and sets the specified
     * initial cause (if any).
     * </p>
     *
     * @see musaico.foundation.graph.NodesMustBeConnected#violation(musaico.foundation.contract.Contract, java.lang.Object, java.lang.Object)
     */
    public NodesMustBeConnected.Violation violation (
                                                     Object plaintiff,
                                                     Graph<?, ?> evidence,
                                                     Throwable cause
                                                     )
    {
        final NodesMustBeConnected.Violation violation =
            this.violation ( plaintiff,
                             evidence );

        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }
}
