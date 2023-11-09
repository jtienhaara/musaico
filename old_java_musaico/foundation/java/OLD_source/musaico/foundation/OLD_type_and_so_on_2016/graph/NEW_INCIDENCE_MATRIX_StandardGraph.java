package musaico.foundation.graph;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Countable;
import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.abnormal.Error;

import musaico.foundation.value.builder.ValueBuilder;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;


/**
 * <p>
 * A tabular implementation of a graph.
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
public class StandardGraph<NODE extends Object, ARC extends Object>
    implements CountableGraph<NODE, ARC>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( StandardGraph.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // The class of nodes in this graph.
    private final Class<NODE> nodeClass;

    // The class of arcs in this graph.
    private final Class<ARC> arcClass;

    // All nodes in this graph.
    private final NODE [] nodes;

    // Lookup of index # by node, including the entry node and exit node.
    // The entry node is ALWAYS node # 0.
    // The exit node is ALWAYS node # (nodes.length - 1).
    private final LinkedHashMap<NODE, Integer> nodeNumsByNodes;

    // All arcs in this graph.
    private final ARC [] arcs;

    // Lookup of index # by arc.
    private final LinkedHashMap<ARC, Integer> arcNumsByArcs;

    // Incidence matrix N x A arcs out of / into each node.
    // incidenceMatrix [ n ] [ a ] = -1 if arc # a leads into node # n.
    // incidenceMatrix [ n ] [ a ] =  0 if arc # a is not connected to node# n.
    // incidenceMatrix [ n ] [ a ] =  1 if arc # a leads out of node # n.
    // incidenceMatrix [ n ] [ a ] =  2 if arc # a connects node # n to itself.
    // The entry node is ALWAYS node # 0.
    // The exit node is ALWAYS node # (nodes.length - 1).
    private final long [] [] incidenceMatrix;

    // The entry node into this graph.
    private final One<NODE> entry;

    // The exit node from this graph.
    private final One<NODE> exit;

    // Pre-calculated hash code.  Set during the constructor.
    private final int hashCode;


    /**
     * <p>
     * Creates a new StandardGraph.
     * </p>
     *
     * @param node_class The class of nodes in this graph.  For example,
     *                   if every node is a String representing the name
     *                   of a city on a map, then the node class
     *                   would be String.class.  Must not be null.
     *
     * @param arc_class The class of arcs in this graph.  For example,
     *                  if every arc is a BigDecimal representing the
     *                  distance along some path between two cities on
     *                  a map, then the arc class would be BigDecimal.class.
     *                  Must not be null.
     *
     * @param entry_node The starting node in this graph.  If one or
     *                   more arcs are passed in, then the entry node
     *                   must have at least one arc leading out from it,
     *                   and all other nodes in the graph must be reachable
     *                   from the entry node.  Must not be null.
     *
     * @param exit_node The terminal node in this graph, to which all
     *                  valid paths lead.  If one or more arcs are
     *                  passed in, then the exit node must have at least
     *                  one arc leading in to it, and all other nodes
     *                  in the graph must have paths to the exit node.
     *                  Must not be null.
     *
     * @param arcs The 0 or more ordered arcs in this graph,
     *             each one connecting a pair of nodes.  For example,
     *             if this graph represents the names of cities on a map
     *             and the distances between them, then each arc passed in
     *             will describe a pair of city names and a distance.
     *             For some applications, the order of arcs is important.
     *             Must not be null.  Must not contain any null values.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution Arc<N,A>...arcs.
    public StandardGraph (
                          Class<NODE> node_class,
                          Class<ARC> arc_class,
                          NODE entry_node,
                          NODE exit_node,
                          Arc<NODE, ARC> ... arcs
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               NodesMustBeConnected.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               node_class, arc_class,
                               entry_node, exit_node, arcs );
        classContracts.check ( Parameter3.MustContainNoNulls.CONTRACT,
                               arcs );

        this.nodeClass = node_class;
        this.arcClass = arc_class;

        int hash_code = 0;

        this.nodeNumsByNodes = new LinkedHashMap<NODE, Integer> ();
        this.arcNumsByArcs = new LinkedHashMap<ARC, Integer> ();

        this.nodeNumsByNodes.put ( entry_node, 0 );

        for ( Arc<NODE, ARC> arc_placeholder : arcs )
        {
            final NODE from = arc_placeholder.from ();
            if ( ! this.nodeNumsByNodes.containsKey ( from )
                 && ! exit_node.equals ( from ) )
            {
                final int node_num = this.nodeNumsByNodes.size ();
                this.nodeNumsByNodes.put ( from, node_num );
            }

            final NODE to = arc_placeholder.to ();
            if ( ! this.nodeNumsByNodes.containsKey ( to )
                 && ! exit_node.equals ( to ) )
            {
                final int node_num = this.nodeNumsByNodes.size ();
                this.nodeNumsByNodes.put ( to, node_num );
            }

            hash_code += arc.hashCode ();
        }

        final int exit_node_num = this.nodeNumsByNodes.size ();
        this.nodeNumsByNodes.put ( exit_node, exit_node_num );

        this.entry = new One<NODE> ( this.nodeClass,
                                     entry_node );
        this.exit = new One<NODE> ( this.nodeClass,
                                    exit_node );

        this.nodes = (NODE [])
            Array.newInstance ( this.nodeClass, this.nodes.size () );
        this.arcs = (ARC [])
            Array.newInstance ( this.arcClass, this.arcs.size () );
        this.incidenceMatrix = new long [ this.nodes.length ] [];
        for ( Arc<ARC> arc_placeholder : arcs )
        {
            final ARC arc = arc_placeholder.arc ();
            final NODE from = arc_placeholder.from ();
            final NODE to = arc_placeholder.to ();

            final int arc_num = this.arcs.get ( arc );

            this.arcs [ arc_num ] = arc;

            final int from_num = this.nodeNumsByNodes.get ( from );
            if ( this.nodes [ from_num ] == null )
            {
                this.nodes [ from_num ] = from;
                this.incidenceMatrix [ from_num ] =
                    new long [ this.arcs.length ];
                for ( int a = 0; a < this.arcs.length; a ++ )
                {
                    this.incidenceMatrix [ node_num ] [ a ] = 0L;
                }
            }

            this.incidenceMatrix [ from_num ] [ arc_num ] ++;

            final int to_num = this.nodeNumsByNodes.get ( to );
            if ( this.nodes [ to_num ] == null )
            {
                this.nodes [ to_num ] = to;
                this.incidenceMatrix [ to_num ] =
                    new long [ this.arcs.length ];
                for ( int a = 0; a < this.arcs.length; a ++ )
                {
                    this.incidenceMatrix [ node_num ] [ a ] = 0L;
                }
            }

            if ( this.incidenceMatrix [ to_num ] [ arc_num ] > 0L )
            {
                this.incidenceMatrix [ to_num ] [ arc_num ] = 2L;
            }
            else
            {
                this.incidenceMatrix [ to_num ] [ arc_num ] = -1L;
            }
        }


        this.hashCode = hash_code;

        this.contracts = new Advocate ( this );

        // Make sure all nodes in the graph are reachable from
        // the entry node and can reach the exit node, and there
        // are no missing nodes (such as entry and exit) and
        // no nodes from outside the graph.
        if ( ! NodesMustBeConnected.CONTRACT.filter ( this ).isKept () )
        {
            throw NodesMustBeConnected.CONTRACT.violation ( this,
                                                            this );
        }
    }


    /**
     * @see musaico.foundation.graph.Graph#arc(musaico.foundation.graph.Arc)
     */
    @Override
    public final ZeroOrOne<Arc<NODE, ARC>> arc (
                                                Arc<NODE, ARC> arc
                                                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               arc );

        final NODE node = arc.from ();
        final ARC arc = arc.arc ();
        final Integer node_num = this.nodeNumsFromNodes.get ( node );
        !!!;
        if ( node_num != null )
        {
            
            for ( Arc<NODE, ARC> maybe_arc : arcs )
            {
                if ( maybe_arc.equals ( arc ) )
                {
                    final One<Arc<NODE, ARC>> one_arc =
                        new One<Arc<NODE, ARC>> (
                            new SillyArc<Arc<NODE, ARC>> ().getArcClass (),
                            maybe_arc );
                    return one_arc;
                }
            }
        }

        // Either the "from" node in the Arc doesn't even exist
        // in this Graph (arcs == null), or the "from" node exists
        // but the specific Arc is not in this Graph.
        final ArcMustBeInGraph.Violation violation =
            new ArcMustBeInGraph<NODE, ARC> ( this )
                .violation ( this,
                             arc );
        final No<Arc<NODE, ARC>> no_arc =
            new No<Arc<NODE, ARC>> (
                new SillyArc<Arc<NODE, ARC>> ().getArcClass (),
                violation );
        return no_arc;
    }


    /**
     * @see musaico.foundation.graph.Graph#arcClass()
     */
    @Override
    public final Class<ARC> arcClass ()
        throws ReturnNeverNull.Violation
    {
        return this.arcClass;
    }


    /**
     * @see musaico.foundation.graph.Graph#arcs()
     */
    @Override
    public final Countable<Arc<NODE, ARC>> arcs ()
        throws ReturnNeverNull.Violation
    {
        return this.arcs;
    }


    /**
     * @see musaico.foundation.graph.Graph#arcs(java.lang.Object)
     */
    @Override
    public final Value<Arc<NODE, ARC>> arcs (
                                             NODE node
                                             )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               node );

        final Countable<Arc<NODE, ARC>> arcs_from_node =
            this.arcsFromNode.get ( node );

        if ( arcs_from_node == null )
        {
            final NodeMustBeInGraph.Violation violation =
                new NodeMustBeInGraph<NODE> ( this ).violation ( this,
                                                                 node );
            final Class<Arc<NODE, ARC>> arc_class =
                new SillyArc<Arc<NODE, ARC>> ().getArcClass ();
            final Error<Arc<NODE, ARC>> error =
                new Error<Arc<NODE, ARC>> ( arc_class,
                                            violation );

            return error;
        }

        return arcs_from_node;
    }


    // Ugly hack for @*%# generics.
    private static class SillyArc<ARC_CLASS> // ARC_CLASS = Arc<?, ?>
    {
        @SuppressWarnings("unchecked")
        public Class<ARC_CLASS> getArcClass ()
        {
            return (Class<ARC_CLASS>) Arc.class;
        }
    }


    /**
     * @return This StandardGraph's contracts checker, for use by\
     *         derived classes to ensure method parameter obligations
     *         and return value guarantees and so on are met.  Never null.
     */
    protected final Advocate contracts ()
        throws ReturnNeverNull.Violation
    {
        return this.contracts;
    }


    /**
     * @see musaico.foundation.graph.Graph#entry()
     */
    @Override
    public final One<NODE> entry ()
        throws ReturnNeverNull.Violation
    {
        return this.entry;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final StandardGraph<?, ?> that = (StandardGraph<?, ?>) object;
        if ( this.hashCode != that.hashCode )
        {
            return false;
        }

        if ( this.nodeClass == null )
        {
            if ( that.nodeClass == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.nodeClass == null )
        {
            return false;
        }
        else if ( ! this.nodeClass.equals ( that.nodeClass ) )
        {
            return false;
        }

        if ( this.arcClass == null )
        {
            if ( that.arcClass == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.arcClass == null )
        {
            return false;
        }
        else if ( ! this.arcClass.equals ( that.arcClass ) )
        {
            return false;
        }

        if ( this.entry == null )
        {
            if ( that.entry == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.entry == null )
        {
            return false;
        }
        else if ( ! this.entry.equals ( that.entry ) )
        {
            return false;
        }

        if ( this.exit == null )
        {
            if ( that.exit == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.exit == null )
        {
            return false;
        }
        else if ( ! this.exit.equals ( that.exit ) )
        {
            return false;
        }

        final Iterator<NODE> this_nodes = this.nodes.iterator ();
        final Iterator<?> that_nodes = that.nodes.iterator ();
        while ( this_nodes.hasNext ()
                && that_nodes.hasNext () )
        {
            final NODE this_node = this_nodes.next ();
            final Object that_node = that_nodes.next ();

            if ( this_node == null )
            {
                if ( that_node != null )
                {
                    return false;
                }
            }
            else if ( that_node == null )
            {
                return false;
            }
            else if ( ! this_node.equals ( that_node ) )
            {
                return false;
            }
        }

        if ( this_nodes.hasNext ()
             || that_nodes.hasNext () )
        {
            return false;
        }

        final Iterator<Arc<NODE, ARC>> this_arcs = this.arcs.iterator ();
        final Iterator<?> that_arcs = that.arcs.iterator ();
        while ( this_arcs.hasNext ()
                && that_arcs.hasNext () )
        {
            final Arc<NODE, ARC> this_arc = this_arcs.next ();
            final Object that_arc = that_arcs.next ();

            if ( this_arc == null )
            {
                if ( that_arc != null )
                {
                    return false;
                }
            }
            else if ( that_arc == null )
            {
                return false;
            }
            else if ( ! this_arc.equals ( that_arc ) )
            {
                return false;
            }
        }

        if ( this_arcs.hasNext ()
             || that_arcs.hasNext () )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.graph.Graph#exit()
     * @return The exit node from this Graph, the terminal node.
     *         Never null.
     */
    @Override
    public final One<NODE> exit ()
        throws ReturnNeverNull.Violation
    {
        return this.exit;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.hashCode;
    }


    /**
     * @see musaico.foundation.graph.Graph#node(java.lang.Object)
     */
    @Override
    public final ZeroOrOne<NODE> node (
                                       NODE node
                                       )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               node );

        for ( NODE maybe_node : this.nodes )
        {
            if ( maybe_node.equals ( node ) )
            {
                final One<NODE> one_node =
                    new One<NODE> (
                        this.nodeClass,
                        maybe_node );
                return one_node;
             }
        }

        // The node doesn't exist in this Graph.
        final NodeMustBeInGraph.Violation violation =
            new NodeMustBeInGraph<NODE> ( this )
                .violation ( this,
                             node );
        final One<NODE> cause =
            new One<NODE> ( this.nodeClass,
                            node );
        final No<NODE> no_node =
            new No<NODE> (
                this.nodeClass,
                cause,
                violation );
        return no_node;
    }


    /**
     * @see musaico.foundation.graph.Graph#nodeClass()
     */
    @Override
    public final Class<NODE> nodeClass ()
        throws ReturnNeverNull.Violation
    {
        return this.nodeClass;
    }


    /**
     * @see musaico.foundation.graph.Graph#nodes()
     */
    @Override
    public final Value<NODE> nodes ()
        throws ReturnNeverNull.Violation
    {
        return this.nodes;
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "StandardGraph#" + this.hashCode + " :\n" );
        for ( Arc<NODE, ARC> arc : this.arcs )
        {
            sbuf.append ( "    " + arc + "\n" );
        }

        return sbuf.toString ();
    }
}
