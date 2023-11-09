package musaico.foundation.graph;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;
import musaico.foundation.term.Maybe;

import musaico.foundation.term.builder.TermBuilder;

import musaico.foundation.term.countable.No;
import musaico.foundation.term.countable.One;


/**
 * <p>
 * A straightforward implementation of a graph.
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
    implements CountableGraph<NODE, ARC>, ImmutableGraph<NODE, ARC>, Serializable
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

    // The Type of NODEs in this graph.
    private final Type<NODE> nodeType;

    // The Type of ARCs in this graph.
    private final Type<ARC> arcType;

    // All nodes.
    private final Countable<NODE> nodes;

    // All arcs.
    private final Countable<Arc<NODE, ARC>> arcs;

    // All SubGraph arcs.
    private final Countable<SubGraph<NODE, ARC>> subGraphs;

    // Lookup of arcs by "from" node.
    private final LinkedHashMap<NODE, Countable<Arc<NODE, ARC>>> arcsFromNode;

    // The entry node into this graph.
    private final NODE entry;

    // The exit node from this graph.
    private final NODE exit;

    // The Type of Arc<NODE, ARC>s in this graph.
    private final Type<Arc<NODE, ARC>> arcStructType;

    // The Type of SubGraph<NODE, ARC>s in this graph.
    private final Type<SubGraph<NODE, ARC>> subGraphStructType;

    // Pre-calculated hash code.  Set during the constructor.
    private final int hashCode;


    /**
     * <p>
     * Creates a new StandardGraph.
     * </p>
     *
     * @param node_type The Type of nodes in this graph.  For example,
     *                  if every node is a String representing the name
     *                  of a city on a map, then the node Type
     *                  would be a <code> Type&lt;String&gt; </code>.
     *                  Must not be null.
     *
     * @param arc_type The Type of arcs in this graph.  For example,
     *                  if every arc is a BigDecimal representing the
     *                  distance along some path between two cities on
     *                  a map, then the arc Type would be a
     *                  <code> Type&lt;BigDecimal&gt; </code>.
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
                          Type<NODE> node_type,
                          Type<ARC> arc_type,
                          NODE entry_node,
                          NODE exit_node,
                          Arc<NODE, ARC> ... arcs
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               NodesMustBeConnected.Violation,
               ArcMustNotLeadToEntryNode.Violation,
               ArcMustNotLeadFromExitNode.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               node_type, arc_type,
                               entry_node, exit_node, arcs );
        classContracts.check ( Parameter3.MustContainNoNulls.CONTRACT,
                               arcs );

        this.nodeType = node_type;
        this.arcType = arc_type;

        int hash_code = 0;

        final TermBuilder<NODE> all_nodes =
            new TermBuilder<NODE> ( this.nodeType );
        final Set<NODE> used_nodes =
            new HashSet<NODE> ();

        final Arc<NODE, ARC> none_arc =
            new StandardArc<NODE, ARC> ( this.nodeType.none (),
                                         this.arcType.none (),
                                         this.nodeType.none () );
        this.arcStructType =
            this.arcType.enclose ( Arc.class, // container_class
                                   none_arc ) // none
                        .buildType ();

        final SubGraph<NODE, ARC> none_sub_graph =
            new SubGraph<NODE, ARC> ( this,
                                      this.arcType.none () );
        this.subGraphStructType =
            this.arcType.enclose ( SubGraph.class,  // container_class
                                   none_sub_graph ) // none
                        .buildType ();

        final TermBuilder<Arc<NODE, ARC>> all_arcs =
            new TermBuilder<Arc<NODE, ARC>> ( this.arcStructType );
        final LinkedHashMap<NODE, TermBuilder<Arc<NODE, ARC>>> arcs_from_node =
            new LinkedHashMap<NODE, TermBuilder<Arc<NODE, ARC>>> ();


        final TermBuilder<SubGraph<NODE, ARC>> all_sub_graphs =
            new TermBuilder<SubGraph<NODE, ARC>> ( this.subGraphStructType );


        for ( Arc<NODE, ARC> arc : arcs )
        {
            final NODE from = arc.from ();
            if ( ! used_nodes.contains ( from ) )
            {
                all_nodes.add ( from );
                used_nodes.add ( from );
            }

            final NODE to = arc.to ();
            if ( ! used_nodes.contains ( to ) )
            {
                all_nodes.add ( to );
                used_nodes.add ( to );
            }

            all_arcs.add ( arc );

            TermBuilder<Arc<NODE, ARC>> arcs_from_current_node =
                arcs_from_node.get ( from );
            if ( arcs_from_current_node == null )
            {
                arcs_from_current_node =
                    new TermBuilder<Arc<NODE, ARC>> ( this.arcStructType );
                arcs_from_node.put ( from, arcs_from_current_node );
            }

            arcs_from_current_node.add ( arc );

            if ( arc instanceof SubGraph )
            {
                final SubGraph<NODE, ARC> sub_graph =
                    (SubGraph<NODE, ARC>) arc;
                all_sub_graphs.add ( sub_graph );
            }

            hash_code += arc.hashCode ();
        }

        this.nodes = all_nodes.build ();
        this.arcs = all_arcs.build ();
        this.subGraphs = all_sub_graphs.build ();
        this.arcsFromNode =
            new LinkedHashMap<NODE, Countable<Arc<NODE, ARC>>> ();
        for ( NODE node : this.nodes )
        {
            TermBuilder<Arc<NODE, ARC>> arcs_from_current_node =
                arcs_from_node.get ( node );

            if ( arcs_from_current_node == null )
            {
                // This node is only ever a "to" node in the arcs,
                // so it has no arcs leading out of it.
                arcs_from_current_node =
                    new TermBuilder<Arc<NODE, ARC>> ( this.arcStructType );
            }

            final Countable<Arc<NODE, ARC>> v_arcs =
                arcs_from_current_node.build ();

            this.arcsFromNode.put ( node, v_arcs );
        }

        this.entry = entry_node;
        this.exit = exit_node;

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

        // Arcs must not lead back into the entry node nor out of
        // the exit node UNLESS the entry and exit node are
        // one and the same.  When the entry nodes is the exit node,
        // we allow a circular graph.
        if ( ! this.entry.equals ( this.exit ) )
        {
            final ArcMustNotLeadToEntryNode<NODE, ARC> must_not_lead_to_entry =
                new ArcMustNotLeadToEntryNode<NODE, ARC> ( this );
            final ArcMustNotLeadFromExitNode<NODE, ARC> must_not_lead_from_exit =
                new ArcMustNotLeadFromExitNode<NODE, ARC> ( this );

            for ( Arc<NODE, ARC> arc : this.arcs )
            {
                if ( ! must_not_lead_to_entry.filter ( arc )
                           .isKept () )
                {
                    throw must_not_lead_to_entry.violation ( this,
                                                             arc );
                }
                else if ( ! must_not_lead_from_exit.filter ( arc )
                                .isKept () )
                {
                    throw must_not_lead_from_exit.violation ( this,
                                                              arc );
                }
            }
        }
    }


    /**
     * <p>
     * Creates a new StandardGraph.
     * </p>
     *
     * @param node_type The Type of nodes in this graph.  For example,
     *                  if every node is a String representing the name
     *                  of a city on a map, then the node Type
     *                  would be a <code> Type&lt;String&gt; </code>.
     *                  Must not be null.
     *
     * @param arc_type The Type of arcs in this graph.  For example,
     *                  if every arc is a BigDecimal representing the
     *                  distance along some path between two cities on
     *                  a map, then the arc Type would be a
     *                  <code> Type&lt;BigDecimal&gt; </code>.
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
    public StandardGraph (
                          Type<NODE> node_type,
                          Type<ARC> arc_type,
                          NODE entry_node,
                          NODE exit_node,
                          Iterable<Arc<NODE, ARC>> arcs
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               NodesMustBeConnected.Violation,
               ArcMustNotLeadToEntryNode.Violation,
               ArcMustNotLeadFromExitNode.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               node_type, arc_type,
                               entry_node, exit_node, arcs );
        classContracts.check ( Parameter3.MustContainNoNulls.CONTRACT,
                               arcs );

        this.nodeType = node_type;
        this.arcType = arc_type;

        int hash_code = 0;

        final TermBuilder<NODE> all_nodes =
            new TermBuilder<NODE> ( this.nodeType );
        final Set<NODE> used_nodes =
            new HashSet<NODE> ();

        final Arc<NODE, ARC> none_arc =
            new StandardArc<NODE, ARC> ( this.nodeType.none (),
                                         this.arcType.none (),
                                         this.nodeType.none () );
        this.arcStructType =
            this.arcType.enclose ( Arc.class, // container_class
                                   none_arc ) // none
                        .buildType ();

        final SubGraph<NODE, ARC> none_sub_graph =
            new SubGraph<NODE, ARC> ( this,
                                      this.arcType.none () );
        this.subGraphStructType =
            this.arcType.enclose ( SubGraph.class,  // container_class
                                   none_sub_graph ) // none
                        .buildType ();

        final TermBuilder<Arc<NODE, ARC>> all_arcs =
            new TermBuilder<Arc<NODE, ARC>> ( this.arcStructType );
        final LinkedHashMap<NODE, TermBuilder<Arc<NODE, ARC>>> arcs_from_node =
            new LinkedHashMap<NODE, TermBuilder<Arc<NODE, ARC>>> ();


        final TermBuilder<SubGraph<NODE, ARC>> all_sub_graphs =
            new TermBuilder<SubGraph<NODE, ARC>> ( this.subGraphStructType );


        for ( Arc<NODE, ARC> arc : arcs )
        {
            final NODE from = arc.from ();
            if ( ! used_nodes.contains ( from ) )
            {
                all_nodes.add ( from );
                used_nodes.add ( from );
            }

            final NODE to = arc.to ();
            if ( ! used_nodes.contains ( to ) )
            {
                all_nodes.add ( to );
                used_nodes.add ( to );
            }

            all_arcs.add ( arc );

            TermBuilder<Arc<NODE, ARC>> arcs_from_current_node =
                arcs_from_node.get ( from );
            if ( arcs_from_current_node == null )
            {
                arcs_from_current_node =
                    new TermBuilder<Arc<NODE, ARC>> ( this.arcStructType );
                arcs_from_node.put ( from, arcs_from_current_node );
            }

            arcs_from_current_node.add ( arc );

            if ( arc instanceof SubGraph )
            {
                final SubGraph<NODE, ARC> sub_graph =
                    (SubGraph<NODE, ARC>) arc;
                all_sub_graphs.add ( sub_graph );
            }

            hash_code += arc.hashCode ();
        }

        this.nodes = all_nodes.build ();
        this.arcs = all_arcs.build ();
        this.subGraphs = all_sub_graphs.build ();
        this.arcsFromNode =
            new LinkedHashMap<NODE, Countable<Arc<NODE, ARC>>> ();
        for ( NODE node : this.nodes )
        {
            TermBuilder<Arc<NODE, ARC>> arcs_from_current_node =
                arcs_from_node.get ( node );

            if ( arcs_from_current_node == null )
            {
                // This node is only ever a "to" node in the arcs,
                // so it has no arcs leading out of it.
                arcs_from_current_node =
                    new TermBuilder<Arc<NODE, ARC>> ( this.arcStructType );
            }

            final Countable<Arc<NODE, ARC>> v_arcs =
                arcs_from_current_node.build ();

            this.arcsFromNode.put ( node, v_arcs );
        }

        this.entry = entry_node;
        this.exit = exit_node;

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

        // Arcs must not lead back into the entry node nor out of
        // the exit node UNLESS the entry and exit node are
        // one and the same.  When the entry nodes is the exit node,
        // we allow a circular graph.
        if ( ! this.entry.equals ( this.exit ) )
        {
            final ArcMustNotLeadToEntryNode<NODE, ARC> must_not_lead_to_entry =
                new ArcMustNotLeadToEntryNode<NODE, ARC> ( this );
            final ArcMustNotLeadFromExitNode<NODE, ARC> must_not_lead_from_exit =
                new ArcMustNotLeadFromExitNode<NODE, ARC> ( this );

            for ( Arc<NODE, ARC> arc : this.arcs )
            {
                if ( ! must_not_lead_to_entry.filter ( arc )
                           .isKept () )
                {
                    throw must_not_lead_to_entry.violation ( this,
                                                             arc );
                }
                else if ( ! must_not_lead_from_exit.filter ( arc )
                                .isKept () )
                {
                    throw must_not_lead_from_exit.violation ( this,
                                                              arc );
                }
            }
        }
    }


    /**
     * @see musaico.foundation.graph.Graph#arc(musaico.foundation.graph.Arc)
     */
    @Override
    public final Maybe<Arc<NODE, ARC>> arc (
            Arc<NODE, ARC> arc
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               arc );

        final NODE node = arc.from ();
        final Countable<Arc<NODE, ARC>> arcs =
            this.arcsFromNode.get ( node );
        if ( arcs != null )
        {
            for ( Arc<NODE, ARC> maybe_arc : arcs )
            {
                if ( maybe_arc.equals ( arc ) )
                {
                    final One<Arc<NODE, ARC>> one_arc =
                        new One<Arc<NODE, ARC>> ( this.arcStructType,
                                                  maybe_arc );
                    return one_arc;
                }
            }
        }

        // Either the "from" node in the Arc doesn't even exist
        // in this Graph (arcs == null), or the "from" node exists
        // but the specific Arc is not in this Graph.
        final No<Arc<NODE, ARC>> no_arc =
            new No<Arc<NODE, ARC>> ( this.arcStructType );
        return no_arc;
    }


    /**
     * @see musaico.foundation.graph.Graph#arcType()
     */
    @Override
    public final Type<ARC> arcType ()
        throws ReturnNeverNull.Violation
    {
        return this.arcType;
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#arcs()
     */
    @Override
    public final Countable<Arc<NODE, ARC>> arcs ()
        throws ReturnNeverNull.Violation
    {
        return this.arcs;
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#arcs(java.lang.Object)
     */
    @Override
    public final Countable<Arc<NODE, ARC>> arcs (
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
            final No<Arc<NODE, ARC>> no_such_arc =
                new No<Arc<NODE, ARC>> ( this.arcStructType );

            return no_such_arc;
        }

        return arcs_from_node;
    }


    /**
     * @return This StandardGraph's contracts checker, for use by
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
    public final NODE entry ()
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

        if ( this.nodeType == null )
        {
            if ( that.nodeType == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.nodeType == null )
        {
            return false;
        }
        else if ( ! this.nodeType.elementClass ().equals (
                        that.nodeType.elementClass () ) )
        {
            return false;
        }

        if ( this.arcType == null )
        {
            if ( that.arcType == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.arcType == null )
        {
            return false;
        }
        else if ( ! this.arcType.elementClass ().equals (
                        that.arcType.elementClass () ) )
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
     */
    @Override
    public final NODE exit ()
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
     * @see musaico.foundation.graph.Graph#immutable()
     */
    @Override
    public StandardGraph<NODE, ARC> immutable ()
        throws ReturnNeverNull.Violation
    {
        return this;
    }


    /**
     * @see musaico.foundation.graph.Graph#node(java.lang.Object)
     */
    @Override
    public final Maybe<NODE> node (
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
                        this.nodeType,
                        maybe_node );
                return one_node;
             }
        }

        // The node doesn't exist in this Graph.
        final No<NODE> no_node =
            new No<NODE> ( this.nodeType );
        return no_node;
    }


    /**
     * @see musaico.foundation.graph.Graph#nodeType()
     */
    @Override
    public final Type<NODE> nodeType ()
        throws ReturnNeverNull.Violation
    {
        return this.nodeType;
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#nodes()
     */
    @Override
    public final Countable<NODE> nodes ()
        throws ReturnNeverNull.Violation
    {
        return this.nodes;
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#numArcs()
     */
    public final long numArcs ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return this.arcs.length ();
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#numNodes()
     */
    public final long numNodes ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return this.nodes.length ();
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#numSubGraphs()
     */
    public final long numSubGraphs ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return this.subGraphs.length ();
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#subGraphs()
     */
    public final Countable<SubGraph<NODE, ARC>> subGraphs ()
    {
        return this.subGraphs;
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public String toString ()
    {
        return "StandardGraph#" + this.hashCode;
    }


    /**
     * @see musaico.foundation.graph.Graph#toStringDetails()
     */
    @Override
    public String toStringDetails ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "StandardGraph#" + this.hashCode + " :\n" );
        for ( Arc<NODE, ARC> arc : this.arcs )
        {
            final String arc_string =
                ( "    " + arc )
                .replaceAll ( "\n", "\n    " )
                .replaceAll ( "[\\s]+$", "" );

            sbuf.append ( arc_string + "\n" );
        }

        return sbuf.toString ();
    }
}
