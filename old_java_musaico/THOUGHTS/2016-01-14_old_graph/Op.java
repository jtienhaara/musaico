package musaico.foundation.graph;

import java.io.Serializable;

import java.util.ArrayList;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Countable;
import musaico.foundation.value.Maybe;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.abnormal.Error;

import musaico.foundation.value.builder.ValueBuilder;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;

import musaico.foundation.value.normal.ValueMustNotBeEmpty;


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
public abstract class Op
    implements Graph<Integer, Contract<Iterable<?>, ? extends ValueViolation>, Value<?>, Value<?>>, Serializable
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

    // Ugly hack for @*%# generics.
    private static class SillyContract<CONTRACT_CLASS> // =Contract<...>
    {
        @SuppressWarnings("unchecked")
        public Class<CONTRACT_CLASS> getContractClass ()
        {
            return (Class<CONTRACT_CLASS>) Contract.class;
        }
    }


    // Checks method obligations and guarantees.
    private final ObjectContracts contracts;

    // The class of arcs used by this Graph.
    private final Class<Contract<Iterable<?>, ? extends ValueViolation>> arcClass =
        new SillyContract<Contract<Iterable<?>, ? extends ValueViolation>> ().getContractClass ();

    private final Class<Value<?>> valueClass =
        new SillyValue<Value<?>> ().getValueClass ();

    private final Contract<Iterable<?>, ? extends ValueViolation> [] parameterTypes;

    private final Value<?> [] valuesSoFar;

    // The entry node into this graph.
    private final One<Integer> entryNode;

    // The exit node from this graph.
    private final One<Integer> exitNode;

    // The hash code for this graph, calculated at constructor time,
    // and returned every time someone wants our hashCode ().
    private final int hashCode;


    /**
     * <p>
     * Creates a new Op.
     * </p>
     * !!!
     */
    @SuppressWarnings("unchecked")
    private Op (
                Contract<Iterable<?>, ? extends ValueViolation> [] parameter_types,
                Value<?> [] values_so_far
                )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object []) parameter_types,
                               (Object []) values_so_far );

        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               parameter_types );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               values_so_far );

        this.parameterTypes = parameter_types;

        this.valuesSoFar = values_so_far;

        this.hashCode = 42; // !!!

        this.entryNode = new One<Integer> ( Integer.class,
                                            0 );
        this.exitNode = new One<Integer> ( Integer.class,
                                           this.parameterTypes.length - 1 );

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * <p>
     * Creates a new Op.
     * </p>
     * !!!
     */
    @SuppressWarnings("unchecked")
    public Op (
               Contract<Iterable<?>, ? extends ValueViolation> ... parameter_types
               )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object []) parameter_types );

        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               parameter_types );

        this.parameterTypes = parameter_types;

        this.valuesSoFar = new Value<?> [ 0 ];

        this.hashCode = 42; // !!!

        this.entryNode = new One<Integer> ( Integer.class,
                                            0 );
        this.exitNode = new One<Integer> ( Integer.class,
                                           this.parameterTypes.length - 1 );

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
    public Value<Contract<Iterable<?>, ? extends ValueViolation>> arcs (
            Integer parameter_index
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               parameter_index );

        if ( parameter_index < 0
             || parameter_index >= this.parameterTypes.length )
        {
            final NodeMustBeInGraph.Violation violation =
                new NodeMustBeInGraph<Integer> ( this )
                    .violation ( this, parameter_index );
            return new No<Contract<Iterable<?>, ? extends ValueViolation>> (
                this.arcClass,
                violation );
        }

        // There is only one arc out of a node -- the next parameter.
        final One<Contract<Iterable<?>, ? extends ValueViolation>> parameter_type =
            new One<Contract<Iterable<?>, ? extends ValueViolation>> (
                this.arcClass,
                this.parameterTypes [ parameter_index ] );

        return parameter_type;
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
    /* !!!
    @Override
    public ZeroOrOne<Integer> enter ()
        throws ReturnNeverNull.Violation
    {
        return this.entryNode;
    }
    !!! */


    /**
     * @see musaico.foundation.graph.Graph#enter()
     */
    @Override
    public ZeroOrOne<Value<?>> enter ()
        throws ReturnNeverNull.Violation
    {
        final ValueMustNotBeEmpty.Violation violation =
            ValueMustNotBeEmpty.CONTRACT.violation ( this,
                                                     new ArrayList<Object> () );
        final Value<?> starting_state = new No<Object> ( Object.class,
                                                         violation );
        final One<Value<?>> v_starting_state =
            new One<Value<?>> ( this.valueClass,
                                starting_state );

        return v_starting_state;
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

        final Op that = (Op) object;
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
    public ZeroOrOne<Value<?>> exit (
                                     ZeroOrOne<Value<?>> state
                                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return state;
    }


    /**
     * @see musaico.foundation.graph.Graph#exit()
     */
    /* !!!
    @Override
    public ZeroOrOne<Integer> exit (
                                    ZeroOrOne<Integer> state
                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // We simply return the exit node in this graph, without
        // changing any state data.
        return this.exitNode;
    }
    !!! */


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
    public Countable<Integer> nodes ()
        throws ReturnNeverNull.Violation
    {
        final ValueBuilder<Integer> builder =
            new ValueBuilder<Integer> ( Integer.class );
        for ( int parameter_index = 0;
              parameter_index < this.parameterTypes.length;
              parameter_index ++ )
        {
            builder.add ( parameter_index );
        }
        return builder.build ();
    }


    /**
     * @see musaico.foundation.graph.Graph#traverse(musaico.foundation.value.ZeroOrOne, java.lang.Object)
     */
    @SuppressWarnings("unchecked") // Poss. heap pollution (parameter_values).
    public Maybe<Value<?>> traverse (
                                     ZeroOrOne<Value<?>> initial_state,
                                     Value<?> ... parameter_values
                                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               initial_state, parameter_values );

        this.contracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               parameter_values );

        final int parameter_index;
        if ( ! initial_state.hasValue () )
        {
            parameter_index = 0;
        }
        else
        {
            parameter_index = -1; // !!!
        }

        if ( parameter_index < 0 )
        {
            final NodeMustBeInGraph.Violation violation =
                new NodeMustBeInGraph<Integer> ( this )
                    .violation ( this, parameter_index );
            return new No<Value<?>> ( this.valueClass,
                                      violation );
        }
        else if ( ( parameter_index + parameter_values.length )
                  >= parameterTypes.length )
        {
            final NodeMustBeInGraph.Violation violation =
                new NodeMustBeInGraph<Integer> ( this )
                    .violation ( this, parameter_index
                                 + parameter_values.length );
            return new No<Value<?>> ( this.valueClass,
                                      violation );
        }

        if ( parameter_index != 0
             || parameter_values.length != this.parameterTypes.length )
        {
            throw new UnsupportedOperationException ( "NOT YET IMPLEMENTED" );
        }

        for ( int v = 0; v < parameter_values.length; v ++ )
        {
            final int p = v + parameter_index;

            if ( ! this.parameterTypes [ p ].filter ( parameter_values [ v ] )
                 .isKept () )
            {
                final ValueViolation violation =
                    this.parameterTypes [ p ].violation (
                        this,
                        parameter_values [ v ] );
                return new No<Value<?>> ( this.valueClass,
                                          violation );
            }
        }

        final Value<?> output = this.evaluate ( parameter_values );

        final One<Value<?>> state = new One<Value<?>> ( this.valueClass,
                                                        output );

        return state;
    }

    protected abstract Value<?> evaluate ( Value<?> ... parameters );


    /**
     * @see java.lang.toString()
     */
    @Override
    public String toString ()
    {
        return "op"; // !!!
    }
}
