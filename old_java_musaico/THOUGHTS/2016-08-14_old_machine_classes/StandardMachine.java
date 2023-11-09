package musaico.foundation.machine;

import java.io.Serializable;


import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;
import musaico.foundation.graph.NodeMustBeInGraph;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.NotOne;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueClass;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.abnormal.Error;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;
import musaico.foundation.value.finite.ValueMustBeOne;


/**
 * <p>
 * An in-memory state machine with no bells or whistles.
 * </p>
 *
 *
 * <p>
 * In Java, every Machine must be Serializable in order
 * to play nicely over RMI.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class StandardMachine<INPUT extends Object, STATE extends Object>
    extends AbstractSequentialArcsMachine<Value<INPUT>, STATE, Contract<Value<?>, ?>>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new StandardMachine.
     * </p>
     *
     * @param name The name of this Machine.  Must not be null.
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new StandardMachine, and whose arcs can be
     *              traversed by this StandardMachine.  Sub-graphs
     *              can be pushed on top of the stack, so that this
     *              Machine ends up pointing to different graphs,
     *              but the bottom graph on the stack will always be
     *              this one.  Must not be null.
     */
    public StandardMachine (
                            String name,
                            Graph<STATE, Contract<Value<?>, ?>> graph
                            )
    {
        super ( name, graph );
    }


    /**
     * <p>
     * Creates a new StandardMachine.
     * </p>
     *
     * @param name The name of this Machine.  Must not be null.
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new StandardMachine, and whose arcs can be
     *              traversed by this StandardMachine.  Sub-graphs
     *              can be pushed on top of the stack, so that this
     *              Machine ends up pointing to different graphs,
     *              but the bottom graph on the stack will always be
     *              this one.  Must not be null.
     *
     * @param state The initial state of this new StandardMachine.
     *              Must be a node in the specified graph.
     *              Must not be null.
     */
    public StandardMachine (
                            String name,
                            Graph<STATE, Contract<Value<?>, ?>> graph,
                            STATE state
                            )
        throws ParametersMustNotBeNull.Violation,
               NodeMustBeInGraph.Violation
    {
        super ( name, graph, state );
    }


    /**
     * <p>
     * Creates a new StandardMachine.
     * </p>
     *
     * @param name The name of this Machine.  Must not be null.
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new StandardMachine, and whose arcs can be
     *              traversed by this StandardMachine.  Sub-graphs
     *              can be pushed on top of the stack, so that this
     *              Machine ends up pointing to different graphs,
     *              but the bottom graph on the stack will always be
     *              this one.  Must not be null.
     *
     * @param state The initial state of this new StandardMachine.
     *              Must be a node in the specified graph.
     *              Must not be null.
     *
     * @param graph_value_class The ValueClass describing the
     *                          Graph underpinning this machine.
     *                          Must not be null.
     */
    public StandardMachine (
                            String name,
                            Graph<STATE, Contract<Value<?>, ?>> graph,
                            STATE state,
                            ValueClass<Graph<STATE, Contract<Value<?>, ?>>> graph_value_class
                            )
        throws ParametersMustNotBeNull.Violation,
               NodeMustBeInGraph.Violation
    {
        super ( name, graph, state, graph_value_class );
    }


    /**
     * @see musaico.foundation.machine.Machine#state(java.lang.Object)
     */
    @Override
    public Machine<Value<INPUT>, STATE, Contract<Value<?>, ?>> state (
            STATE state
            )
        throws ParametersMustNotBeNull.Violation,
               NodeMustBeInGraph.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  state );

        // The state specified MUST be in the bottom level graph of
        // this machine.
        final Maybe<STATE> state_in_graph =
            this.graph ().node ( state );
        if ( state_in_graph instanceof NotOne )
        {
            final NotOne<STATE> not_state = (NotOne<STATE>) state_in_graph;

            final ValueViolation violation = not_state.valueViolation ();
            if ( violation.getCause () instanceof NodeMustBeInGraph.Violation )
            {
                throw (NodeMustBeInGraph.Violation) violation.getCause ();
            }
            else
            {
                final NodeMustBeInGraph.Violation node_is_not_in_graph =
                    new NodeMustBeInGraph<STATE> ( this.graph () )
                            .violation ( this, state,
                                         violation ); // cause

                throw node_is_not_in_graph;
            }
        }

        final StandardMachine<INPUT, STATE> new_machine =
            new StandardMachine<INPUT, STATE> (
                this.name (),
                this.graph (),
                state_in_graph.orNull () );

        return new_machine;
    }


    /**
     * @see musaico.foundation.machine.AbstractSequentialArcsMachine#transition(musaico.foundation.graph.Graph, musaico.foundation.value.finite.One, musaico.foundation.value.Value, musaico.foundation.graph.Arc)
     */
    @Override
    protected ZeroOrOne<STATE> transition (
                                           Graph<STATE, Contract<Value<?>, ?>> graph,
                                           STATE state,
                                           Value<INPUT> input,
                                           Arc<STATE, Contract<Value<?>, ?>> arc
                                           )
        throws ReturnNeverNull.Violation
    {
        final ValueClass<STATE> state_value_class = graph.nodeValueClass ();
        final Contract<Value<?>, ?> transition =
            arc.arc ();
        if ( transition.filter ( input ).isKept () )
        {
            final STATE new_state = arc.to ();
            return new One<STATE> ( state_value_class,
                                    new_state );
        }
        else
        {
            @SuppressWarnings("unchecked") // Generic array creation
            final InputsMustCauseTransition.Violation violation =
                this.inputsMustCauseTransition ().violation ( this,
                                                              null, // cause
                                                              input );
            final No<STATE> no_transition =
                new No<STATE> ( state_value_class,
                                new One<STATE> ( state_value_class,
                                                 state ), // cause
                                violation );
            return no_transition;
        }
    }
}
