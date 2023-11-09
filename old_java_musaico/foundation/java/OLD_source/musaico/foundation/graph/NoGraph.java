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

    // The Type of NODEs in this graph.
    private final Type<NODE> nodeType;

    // The Type of ARCs in this graph.
    private final Type<ARC> arcType;

    // The Type of Arc<NODE, ARC>s in this graph.
    private final Type<Arc<NODE, ARC>> arcStructType;

    // The Type of SubGraph<NODE, ARC>s in this graph.
    private final Type<SubGraph<NODE, ARC>> subGraphStructType;


    /**
     * <p>
     * Creates a new NoGraph.
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
     */
    public NoGraph (
                    Type<NODE> node_type,
                    Type<ARC> arc_type
                    )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               node_type, arc_type );

        this.nodeType = node_type;
        this.arcType = arc_type;

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

        this.contracts = new Advocate ( this );
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

        final No<Arc<NODE, ARC>> no_arc =
            new No<Arc<NODE, ARC>> ( this.arcStructType );
        return no_arc;
    }


    /**
     * @return The Type of Arc&lt;NODE, ARC&gt;s in this graph.  Never null.
     */
    public final Type<Arc<NODE, ARC>> arcStructType ()
    {
        return this.arcStructType;
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
        final No<Arc<NODE, ARC>> no_arcs =
            new No<Arc<NODE, ARC>> ( this.arcStructType );
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

        final No<Arc<NODE, ARC>> no_such_arcs =
            new No<Arc<NODE, ARC>> ( this.arcStructType );

        return no_such_arcs;
    }


    /**
     * @see musaico.foundation.graph.Graph#entry()
     */
    @Override
    public final NODE entry ()
        throws ReturnNeverNull.Violation
    {
        return this.nodeType.none ();
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

        return true;
    }


    /**
     * @see musaico.foundation.graph.Graph#exit()
     */
    @Override
    public final NODE exit ()
        throws ReturnNeverNull.Violation
    {
        return this.nodeType.none ();
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
    public final Maybe<NODE> node (
            NODE node
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               node );

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
        final No<NODE> no_nodes =
            new No<NODE> ( this.nodeType );
        return no_nodes;
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
     * @return The Type of SubGraphs in this graph.  Never null.
     */
    public final Type<SubGraph<NODE, ARC>> subGraphStructType ()
    {
        return this.subGraphStructType;
    }


    /**
     * @see musaico.foundation.graph.CountableGraph#subGraphs()
     */
    public final Countable<SubGraph<NODE, ARC>> subGraphs ()
    {
        final No<SubGraph<NODE, ARC>> no_sub_graphs =
            new No<SubGraph<NODE, ARC>> ( this.subGraphStructType );
        return no_sub_graphs;
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public String toString ()
    {
        return "NoGraph [ "
            + this.nodeType
            + ", "
            + this.arcType
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
