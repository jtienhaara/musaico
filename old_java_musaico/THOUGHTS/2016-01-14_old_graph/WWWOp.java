package musaico.foundation.graph;

import java.io.Serializable;

import java.util.LinkedHashMap;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
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
!!!
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
public class Op
    implements Graph<String, StandardArc<String, Value<?>>, Value<?>, Value<?>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Op.class );


    // Ugly hack for @*%# generics.
    private static class SillyValue<VALUE_CLASS> // VALUE_CLASS = Value<...>
    {
        @SuppressWarnings("unchecked")
        public Class<VALUE_CLASS> getValueClass ()
        {
            return (Class<VALUE_CLASS>) Value.class;
        }
    }


    // Checks method obligations and guarantees.
    private final ObjectContracts contracts;

    // The class of arcs used by this Graph.
    private final Class<StandardArc<String, Value<?>>> arcClass;

    // The lookup of all arcs out of all nodes in this graph.
    // When an attempt is made to traverse the arc, the pass is
    // either kept by the arc's filter (traversal is successful)
    // or discarded (traversal failed).
    private final LinkedHashMap<String, StandardArc<String, Value<?>> []> arcs;

    // The entry node into this graph.
    private final One<String> entryNode;

    // The exit node from this graph.
    private final One<String> exitNode;

    // The hash code for this graph, calculated at constructor time,
    // and returned every time someone wants our hashCode ().
    private final int hashCode;


    /**
     * <p>
     * Creates a new Op.
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
    public Op (
               Filter<Value<?>> ... parameter_types
               )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               parameter_types );

        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               arcs );

        this.arcClass =
            new SillyValue<Value<?>> ().getValueClass ();

        this.arcs =
            new LinkedHashMap<String, StandardArc<String, Value<?>> []> ( arcs );

        String entry_node = "no_parameters";
        String exit_node = entry_node;
        int hash_code = 0;

        String node = entry_node;
        for ( Filter<Value<?>> parameter_type : parameter_types )
        {
            final StandardArc<String, Value<
        }

        this.entryNode = new One<String> ( String.class,
                                           entry_node );
        this.exitNode = new One<String> ( String.class,
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
    public Value<StandardArc<String, Value<?>>> arcs (
                                                      String node
                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               node );

        final ValueBuilder<StandardArc<String, Value<?>>> builder =
            new ValueBuilder<StandardArc<String, Value<?>>> ( this.arcClass );

        final StandardArc<String, Value<?>> [] arcs = this.arcs.get ( node );
        if ( arcs == null )
        {
            final NodeMustBeInGraph.Violation violation =
                new NodeMustBeInGraph<String> ( this ).violation ( this,
                                                                 node );
            final Error<StandardArc<String, Value<?>>> error =
                new Error<StandardArc<String, Value<?>>> ( this.arcClass,
                                                           violation );

            builder.cause ( error );

            return builder.buildPartial ();
        }

        builder.addAll ( arcs );

        final Value<StandardArc<String, Value<?>>> arcs_from_node =
            builder.build ();

        return arcs_from_node;
    }


    /**
     * @return This Op's contracts checker, for ensuring
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
    public ZeroOrOne<String> enter ()
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

        final Op<?, ?> that = (Op<?, ?>) object;
        if ( this.hashCode!= that.hashCode )
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
    public ZeroOrOne<String> exit (
                                   ZeroOrOne<String> state
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
    public Value<String> nodes ()
        throws ReturnNeverNull.Violation
    {
        final ValueBuilder<String> builder =
            new ValueBuilder<String> ( String.class );
        builder.addAll ( this.arcs.keySet () );
        return builder.build ();
    }


    /**
     * @see musaico.foundation.graph.Graph#traverse(musaico.foundation.value.ZeroOrOne, java.lang.Object)
     */
    @SuppressWarnings("unchecked") // Possible heap pollution (passes).
    public Maybe<Value<?>> traverse (
                                     ZeroOrOne<Value<?>> initial_state,
                                     Value<?> ... passes
                                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               initial_state, passes );

        this.contracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               passes );

        final ValueBuilder<Value<?>> builder =
            new ValueBuilder<Value<?>> ( this.valueClass );

        String node = initial_state.orNull ();
        for ( Value<?> pass : passes )
        {
            final StandardArc<String, Value<?>> [] arcs;
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
                    new NodeMustBeInGraph<String> ( this ).violation ( this,
                                                                       node );
                final Error<Value<?>> error =
                    new Error<Value<?>> ( this.valueClass,
                                          violation );

                builder.cause ( error );

                return builder.buildPartial ();
            }

            String new_node = null;
            for ( StandardArc<String, Value<?>> arc : arcs )
            {
                final Filter<Value<?>> filter = arc.filter ();
                if ( filter.filter ( pass ).isKept () )
                {
                    new_node = arc.destination ();
                    break;
                }
            }

            if ( new_node == null )
            {
                final One<String> last_node = new One<String> ( String.class,
                                                                node );
                final ArcsMustBeTraversable.Violation violation =
                    new ArcsMustBeTraversable<String, StandardArc<String, Value<?>>, Value<?>, Value<?>> (
                        this, // graph
                        last_state )
                    .violation ( this, pass );

                final Error<String> error =
                    new Error<String> ( String.class,
                                      violation );

                builder.cause ( error );

                return builder.buildPartial ();
            }

            builder.add ( new_node );

            node = new_node;
        }

        final Maybe<String> traversal = builder.buildZeroOrOne ();

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
