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

import musaico.foundation.value.Countable;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueClass;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.builder.ValueBuilder;

import musaico.foundation.value.classes.StandardValueClass;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;


/**
 * <p>
 * No Graph at all.  No nodes, no arcs, and so on.
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
public class NoGraph<NODE extends Object, ARC extends Object>
    implements CountableGraph<NODE, ARC>, ImmutableGraph<NODE, ARC>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( NoGraph.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // The class of nodes in this graph.
    private final ValueClass<NODE> nodeValueClass;

    // The class of arcs in this graph.
    private final ValueClass<ARC> arcValueClass;

    // Returned every time an Arc<NODE, ARC> is requested.
    private final Arc<NODE, ARC> noneArc;

    // Returned every time a SubGraph<NODE, ARC> is requested.
    private final SubGraph<NODE, ARC> noneSubGraph;


    /**
     * <p>
     * Creates a new NoGraph.
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
     */
    public NoGraph (
                    ValueClass<NODE> node_class,
                    ValueClass<ARC> arc_class
                    )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               node_class, arc_class );

        this.nodeValueClass = node_class;
        this.arcValueClass = arc_class;

        this.noneArc =
            new Arc<NODE, ARC> ( this.nodeValueClass.none (),
                                 this.arcValueClass.none (),
                                 this.nodeValueClass.none () );

        this.noneSubGraph =
            new SubGraph<NODE, ARC> ( this,
                                      this.arcValueClass.none () );

        this.contracts = new Advocate ( this );
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

        final ArcMustBeInGraph.Violation violation =
            new ArcMustBeInGraph<NODE, ARC> ( this )
                .violation ( this,
                             arc );
        final No<Arc<NODE, ARC>> no_arc =
            new No<Arc<NODE, ARC>> (
                new StandardValueClass<Arc<NODE, ARC>> (
                    Arc.class,      // element_class
                    this.noneArc ), // none
                violation );
        return no_arc;
    }


    /**
     * @see musaico.foundation.graph.Graph#arcValueClass()
     */
    @Override
    public final ValueClass<ARC> arcValueClass ()
        throws ReturnNeverNull.Violation
    {
        return this.arcValueClass;
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#arcs()
     */
    @Override
    public final Countable<Arc<NODE, ARC>> arcs ()
        throws ReturnNeverNull.Violation
    {
        final No<Arc<NODE, ARC>> no_arcs =
            new No<Arc<NODE, ARC>> (
                new StandardValueClass<Arc<NODE, ARC>> (
                    Arc.class,         // element_class
                    this.noneArc ) );  // none
        return no_arcs;
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

        final NodeMustBeInGraph.Violation violation =
        new NodeMustBeInGraph<NODE> ( this ).violation ( this,
                                                         node );
        final ValueClass<Arc<NODE, ARC>> arc_class =
                new StandardValueClass<Arc<NODE, ARC>> (
                    Arc.class,      // element_class
                    this.noneArc ); // none
        final No<Arc<NODE, ARC>> no_such_arcs =
        new No<Arc<NODE, ARC>> ( arc_class,
                                 violation );

        return no_such_arcs;
    }


    /**
     * @see musaico.foundation.graph.Graph#entry()
     */
    @Override
    public final NODE entry ()
        throws ReturnNeverNull.Violation
    {
        return this.nodeValueClass.none ();
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

        final NoGraph<?, ?> that = (NoGraph<?, ?>) object;

        if ( this.nodeValueClass == null )
        {
            if ( that.nodeValueClass == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.nodeValueClass == null )
        {
            return false;
        }
        else if ( ! this.nodeValueClass.elementClass ().equals (
                        that.nodeValueClass.elementClass () ) )
        {
            return false;
        }

        if ( this.arcValueClass == null )
        {
            if ( that.arcValueClass == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.arcValueClass == null )
        {
            return false;
        }
        else if ( ! this.arcValueClass.elementClass ().equals (
                        that.arcValueClass.elementClass () ) )
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
        return this.nodeValueClass.none ();
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 0;
    }


    /**
     * @see musaico.foundation.graph.Graph#immutable()
     */
    @Override
    public NoGraph<NODE, ARC> immutable ()
        throws ReturnNeverNull.Violation
    {
        return this;
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

        // The node doesn't exist in this Graph.
        final NodeMustBeInGraph.Violation violation =
            new NodeMustBeInGraph<NODE> ( this )
                .violation ( this,
                             node );
        final One<NODE> cause =
            new One<NODE> ( this.nodeValueClass,
                            node );
        final No<NODE> no_node =
            new No<NODE> (
                this.nodeValueClass,
                cause,
                violation );
        return no_node;
    }


    /**
     * @see musaico.foundation.graph.Graph#nodeValueClass()
     */
    @Override
    public final ValueClass<NODE> nodeValueClass ()
        throws ReturnNeverNull.Violation
    {
        return this.nodeValueClass;
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#nodes()
     */
    @Override
    public final Countable<NODE> nodes ()
        throws ReturnNeverNull.Violation
    {
        final No<NODE> no_nodes =
            new No<NODE> ( this.nodeValueClass );
        return no_nodes;
    }


    /**
     * @return No Arc at all.  Never null.
     */
    public final Arc<NODE, ARC> noneArc ()
    {
        return this.noneArc;
    }


    /**
     * @return No SubGraph at all.  Never null.
     */
    public final SubGraph<NODE, ARC> noneSubGraph ()
    {
        return this.noneSubGraph;
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#numArcs()
     */
    public final long numArcs ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return 0L;
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#numNodes()
     */
    public final long numNodes ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return 0L;
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#numSubGraphs()
     */
    public final long numSubGraphs ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return 0L;
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#subGraphs()
     */
    public final Countable<SubGraph<NODE, ARC>> subGraphs ()
    {
        final No<SubGraph<NODE, ARC>> no_sub_graphs =
            new No<SubGraph<NODE, ARC>> (
                new StandardValueClass<SubGraph<NODE, ARC>> (
                    SubGraph.class,        // element_class
                    this.noneSubGraph ) ); // none
        return no_sub_graphs;
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public String toString ()
    {
        return "NoGraph [ "
            + this.nodeValueClass
            + ", "
            + this.arcValueClass
            + " ]";
    }


    /**
     * @see musaico.foundation.graph.Graph#toStringDetails()
     */
    @Override
    public final String toStringDetails ()
    {
        return this.toString ();
    }
}
