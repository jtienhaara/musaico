package musaico.foundation.graph;

import java.io.Serializable;

import java.util.LinkedHashMap;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.abnormal.Error;

import musaico.foundation.value.builder.ValueBuilder;

import musaico.foundation.value.finite.One;


/**
 * <p>
 * A simple implementation of a graph of nodes, connected by arcs,
 * where every arc has minimal traversal logic, and the
 * state of the visitor is simply the node they are
 * currently visiting.
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
public class StandardGraph<NODE extends Object, PASS extends Object>
    implements Graph<NODE, StandardArc<NODE, PASS>, PASS, NODE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( StandardGraph.class );


    // Checks method obligations and guarantees.
    private final ObjectContracts contracts;

    // The class of NODEs used by this Graph.
    private final Class<NODE> nodeClass;

    // The class of arcs used by this Graph.
    private final Class<StandardArc<NODE, PASS>> arcClass;

    // The lookup of all arcs out of all nodes in this graph.
    // When an attempt is made to traverse the arc, the pass is
    // either kept by the arc's filter (traversal is successful)
    // or discarded (traversal failed).
    private final LinkedHashMap<NODE, StandardArc<NODE, PASS> []> arcs;

    // The entry node into this graph.
    private final ZeroOrOne<NODE> entryNode;

    // The exit node from this graph.
    private final ZeroOrOne<NODE> exitNode;

    // The hash code for this graph, calculated at constructor time,
    // and returned every time someone wants our hashCode ().
    private final int hashCode;


    /**
     * <p>
     * Creates a new StandardGraph.
     * </p>
     *
     * @param node_class The class of node objects stored in this graph.
     *                   Must not be null.
     *
     * @param arcs The lookup of zero-or-more-arcs-per-node, by node.
     *             The first node in the ordered map is the entry point
     *             into this graph.  The last node is the exit point
     *             from this graph.  Must contain at least 1 node.
     *             Must not be null.  Must not contain any
     *             null keys or values.
     */
    public StandardGraph (
                          Class<NODE> node_class,
                          LinkedHashMap<NODE, StandardArc<NODE, PASS> []> arcs
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               Parameter2.Length.MustBeGreaterThanZero.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               node_class, arcs );

        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               arcs );
        classContracts.check ( Parameter2.Length.MustBeGreaterThanZero.CONTRACT,
                               arcs );

        this.nodeClass = node_class;
        this.arcClass =
            new SillyArc<StandardArc<NODE, PASS>> ().getArcClass ();

        this.arcs =
            new LinkedHashMap<NODE, StandardArc<NODE, PASS> []> ( arcs );

        NODE entry_node = null;
        NODE exit_node = null;
        int hash_code = 0;
        for ( NODE node : this.arcs.keySet () )
        {
            if ( entry_node == null )
            {
                entry_node = null;
            }

            exit_node = node;

            hash_code += node.hashCode () * 37;
            for ( StandardArc<NODE, PASS> arc : this.arcs.get ( node ) )
            {
                hash_code += arc.hashCode ();
            }
        }

        this.entryNode = new One<NODE> ( this.nodeClass,
                                         entry_node );
        this.exitNode = new One<NODE> ( this.nodeClass,
                                        exit_node );

        this.hashCode = hash_code;

        this.contracts = new ObjectContracts ( this );
    }


    // Ugly hack for @*%# generics.
    private static class SillyArc<ARC_CLASS> // ARC_CLASS = StandardArc<?, ?>
    {
        @SuppressWarnings("unchecked")
        public Class<ARC_CLASS> getArcClass ()
        {
            return (Class<ARC_CLASS>) StandardArc.class;
        }
    }


    /**
     * @see musaico.foundation.graph.Graph#arcs(java.lang.Object)
     */
    @Override
    public Value<StandardArc<NODE, PASS>> arcs (
                                                NODE node
                                                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               node );

        final ValueBuilder<StandardArc<NODE, PASS>> builder =
            new ValueBuilder<StandardArc<NODE, PASS>> ( this.arcClass );

        final StandardArc<NODE, PASS> [] arcs = this.arcs.get ( node );
        if ( arcs == null )
        {
            final NodeMustBeInGraph.Violation violation =
                new NodeMustBeInGraph<NODE> ( this ).violation ( this,
                                                                 node );
            final Error<StandardArc<NODE, PASS>> error =
                new Error<StandardArc<NODE, PASS>> ( this.arcClass,
                                                     violation );

            builder.cause ( error );

            return builder.buildPartial ();
        }

        builder.addAll ( arcs );

        final Value<StandardArc<NODE, PASS>> arcs_from_node =
            builder.build ();

        return arcs_from_node;
    }


    /**
     * @return This StandardGraph's contracts checker, for ensuring
     *         method parameter obligations and return value guarantees
     *         and so on are met.  Never null.
     */
    protected final ObjectContracts contracts ()
        throws ReturnNeverNull.Violation
    {
        return this.contracts;
    }


    /**
     * @see musaico.foundation.graph.Graph#enter()
     */
    @Override
    public ZeroOrOne<NODE> enter ()
        throws ReturnNeverNull.Violation
    {
        return this.entryNode;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
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
        if ( this.hashCode!= that.hashCode )
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

        if ( this.entryNode == null )
        {
            if ( that.entryNode == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.entryNode == null )
        {
            return false;
        }
        else if ( ! this.entryNode.equals ( that.entryNode ) )
        {
            return false;
        }

        if ( this.exitNode == null )
        {
            if ( that.exitNode == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.exitNode == null )
        {
            return false;
        }
        else if ( ! this.exitNode.equals ( that.exitNode ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.graph.Graph#exit()
     */
    @Override
    public ZeroOrOne<NODE> exit (
                                 ZeroOrOne<NODE> state
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // We ignore the state passed in, since for all we know
        // or care,  it's just a node.
        // We simply return the exit node in this graph, without
        // changing any state data.
        return this.exitNode;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.hashCode;
    }


    /**
     * @see musaico.foundation.graph.Graph#nodes()
     */
    @Override
    public Value<NODE> nodes ()
        throws ReturnNeverNull.Violation
    {
        final ValueBuilder<NODE> builder =
            new ValueBuilder<NODE> ( this.nodeClass );
        builder.addAll ( this.arcs.keySet () );
        return builder.build ();
    }


    /**
     * @see musaico.foundation.graph.Graph#traverse(musaico.foundation.value.ZeroOrOne, java.lang.Object)
     */
    @SuppressWarnings("unchecked") // Possible heap pollution (passes).
    public Maybe<NODE> traverse (
                                 ZeroOrOne<NODE> initial_state,
                                 PASS ... passes
                                 )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               initial_state, passes );

        this.contracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               passes );

        final ValueBuilder<NODE> builder =
            new ValueBuilder<NODE> ( this.nodeClass );

        NODE node = initial_state.orNull ();
        for ( PASS pass : passes )
        {
            final StandardArc<NODE, PASS> [] arcs;
            if ( node == null )
            {
                arcs = null;
            }
            else
            {
                arcs = this.arcs.get ( node );
            }

            if ( arcs == null )
            {
                final NodeMustBeInGraph.Violation violation =
                    new NodeMustBeInGraph<NODE> ( this ).violation ( this,
                                                                     node );
                final Error<NODE> error =
                    new Error<NODE> ( this.nodeClass,
                                      violation );

                builder.cause ( error );

                return builder.buildPartial ();
            }

            NODE new_node = null;
            for ( StandardArc<NODE, PASS> arc : arcs )
            {
                final Filter<PASS> filter = arc.filter ();
                if ( filter.filter ( pass ).isKept () )
                {
                    new_node = arc.destination ();
                    break;
                }
            }

            if ( new_node == null )
            {
                final One<NODE> last_node = new One<NODE> ( this.nodeClass,
                                                            node );
                final ArcsMustBeTraversable.Violation violation =
                    new ArcsMustBeTraversable<NODE, StandardArc<NODE, PASS>, PASS, NODE> (
                        this, // graph
                        last_node )
                    .violation ( this, pass );

                final Error<NODE> error =
                    new Error<NODE> ( this.nodeClass,
                                      violation );

                builder.cause ( error );

                return builder.buildPartial ();
            }

            builder.add ( new_node );

            node = new_node;
        }

        final Maybe<NODE> traversal = builder.buildZeroOrOne ();

        return traversal;
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public String toString ()
    {
        final int num_nodes = this.arcs.keySet ().size ();
        return ClassName.of ( this.getClass () ) + " [ " + num_nodes + " nodes ]";
    }
}
