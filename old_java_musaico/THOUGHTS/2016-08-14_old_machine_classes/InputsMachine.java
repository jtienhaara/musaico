package musaico.foundation.machine;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;
import musaico.foundation.graph.NodeMustBeInGraph;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.NotOne;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueClass;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.abnormal.Error;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;
import musaico.foundation.value.finite.ValueMustBeOne;
import musaico.foundation.value.ValueViolation;


/**
 * <p>
 * An in-memory state machine which remembers the sequence of
 * inputs which induced the state transitions it has undergone so far.
 * </p>
 *
 * <p>
 * Each state returned by <code> transition ( Value<INPUT> ... ) </code>
 * also includes, as a cause, the state(s) which came before it.
 * For example, suppose the sequence of transitions of an InputsMachine
 * is as follows:
 * </p>
 *
 * <p>
 * parked --[ignition]--&gt; idle --[release_handbrake]--&gt; unparked
 *        --[accelerate]--&gt; driving
 * </p>
 *
 * <p>
 * The InputsMachine started in state "parked", then
 * <code> transition ( "ignition", "release_handbrake", "accelerate" ) </code>
 * was called.  The final result is state "driving", with
 * cause state "unparked", which in turn has cause state
 * "idle", which in turn has cause state "parked".
 * </p>
 *
 * <p>
 * An InputsMachine is typically only suitable as a short-lived state machine,
 * since the sequence of inputs can grow quite large, and consume
 * a great deal of memory, if left unbounded.
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
public class InputsMachine<INPUT extends Object, STATE extends Object, ARC extends Contract<Value<?>, ?>>
    extends AbstractSequentialArcsMachine<Value<INPUT>, STATE, ARC>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( InputsMachine.class );


    // MUTABLE:
    // The sequence of inputs which have induced successful transition(s).
    private final List<Value<INPUT>> inputs = new ArrayList<Value<INPUT>> ();

    // MUTABLE:
    // The sequence of inputs which have induced successful transition(s)
    // during the current call to transition ( INPUT ... ).
    private final List<Value<INPUT>> currentTransition =
        new ArrayList<Value<INPUT>> ();


    /**
     * <p>
     * Creates a new InputsMachine.
     * </p>
     *
     * @param name The name of this Machine.  Must not be null.
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new InputsMachine, and whose arcs can be
     *              traversed by this InputsMachine.  Sub-graphs
     *              can be pushed on top of the stack, so that this
     *              Machine ends up pointing to different graphs,
     *              but the bottom graph on the stack will always be
     *              this one.  Must not be null.
     */
    public InputsMachine (
                          String name,
                          Graph<STATE, ARC> graph
                          )
    {
        super ( name, graph );
    }


    /**
     * <p>
     * Creates a new InputsMachine.
     * </p>
     *
     * @param name The name of this Machine.  Must not be null.
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new InputsMachine, and whose arcs can be
     *              traversed by this InputsMachine.  Sub-graphs
     *              can be pushed on top of the stack, so that this
     *              Machine ends up pointing to different graphs,
     *              but the bottom graph on the stack will always be
     *              this one.  Must not be null.
     *
     * @param state The initial state of this new InputsMachine.
     *              Must be a node in the specified graph.
     *              Must not be null.
     *
     * @param inputs The inputs which led the new InputsMachine to
     *               its initial state.  Must not be null.
     *               Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution Value<INPUT>...
    public InputsMachine (
                          String name,
                          Graph<STATE, ARC> graph,
                          STATE state,
                          Value<INPUT> ... inputs
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               NodeMustBeInGraph.Violation
    {
        super ( name, graph, state );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) inputs );
        classContracts.check ( Parameter3.MustContainNoNulls.CONTRACT,
                               inputs );


        for ( Value<INPUT> input : inputs )
        {
            this.inputs.add ( input );
        }
    }


    /**
     * <p>
     * Creates a new InputsMachine.
     * </p>
     *
     * @param name The name of this Machine.  Must not be null.
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new InputsMachine, and whose arcs can be
     *              traversed by this InputsMachine.  Sub-graphs
     *              can be pushed on top of the stack, so that this
     *              Machine ends up pointing to different graphs,
     *              but the bottom graph on the stack will always be
     *              this one.  Must not be null.
     *
     * @param state The initial state of this new InputsMachine.
     *              If One state, then it must be a node in the
     *              specified graph.  If not One state, then this
     *              machine starts in an error state, and cannot
     *              make any transitions.  Must not be null.
     *
     * @param inputs The inputs which led the new InputsMachine to
     *               its initial state.  Must not be null.
     *               Must not contain any null elements.
     */
    public InputsMachine (
                          String name,
                          Graph<STATE, ARC> graph,
                          STATE state,
                          List<Value<INPUT>> inputs
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               NodeMustBeInGraph.Violation
    {
        super ( name, graph, state );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) inputs );
        classContracts.check ( Parameter3.MustContainNoNulls.CONTRACT,
                               inputs );

        this.inputs.addAll ( inputs );
    }


    /**
     * @return The inputs into this InputsMachine so far.  Never null.
     *         Never contains any null elements.
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // To create Value<INPUT> [].
    public final Value<INPUT> [] inputs ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        final Value<INPUT> [] inputs_array;
        synchronized ( this.lock () )
        {
            final Value<INPUT> [] template =
                new Value [ this.inputs.size () ];
            inputs_array = this.inputs.toArray ( template );
        }

        return inputs_array;
    }


    /**
     * @see musaico.foundation.machine.Machine#state(java.lang.Object)
     */
    @Override
        public InputsMachine<INPUT, STATE, ARC> state (
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

        final List<Value<INPUT>> new_inputs;
        if ( this.state ().equals ( state ) )
        {
            new_inputs = this.inputs;
        }
        else
        {
            new_inputs = new ArrayList<Value<INPUT>> ();
        }

        final InputsMachine<INPUT, STATE, ARC> new_machine =
            new InputsMachine<INPUT, STATE, ARC> (
                this.name (),
                this.graph (),
                state_in_graph.orNull (),
                new_inputs );

        return new_machine;
    }


    /**
     * @see musaico.foundation.AbstractSequentialArcsMachine#transition(Object...)
     */
    @Override
    @SuppressWarnings("unchecked") // Possible heap pollution Value<INPUT>...
    public final Maybe<STATE> transition (
                                          Value<INPUT>... inputs
                                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        synchronized ( this.lock () )
        {
            this.currentTransition.clear ();

            final Maybe<STATE> output_state =
                super.transition ( inputs );

            if ( output_state.orNull () != null )
            {
                // Add the new input to the list of inputs for the
                // current call to transition ( Value<INPUT> ... ).
                this.inputs.addAll ( this.currentTransition );
            }

            this.currentTransition.clear ();

            return output_state;
        }
    }


    /**
     * @see musaico.foundation.machine.AbstractSequentialArcsMachine#transition(musaico.foundation.graph.Graph, musaico.foundation.value.finite.One, java.lang.Object, musaico.foundation.value.Value, musaico.foundation.graph.Arc)
     */
    @Override
    protected ZeroOrOne<STATE> transition (
                                           Graph<STATE, ARC> graph,
                                           STATE state,
                                           Value<INPUT> input,
                                           Arc<STATE, ARC> arc
                                           )
        throws ReturnNeverNull.Violation
    {
        final ValueClass<STATE> state_value_class = graph.nodeValueClass ();
        final ARC transition = arc.arc ();
        final One<STATE> cause_state =
            new One<STATE> ( state_value_class,
                             state );
        if ( transition.filter ( input ).isKept () )
        {
            final STATE new_state = arc.to ();
            final One<STATE> one_transition =
                new One<STATE> ( state_value_class,
                                 cause_state, // cause
                                 new_state );
            this.currentTransition.add ( input );
            return one_transition;
        }
        else
        {
            final No<STATE> no_transition =
                new No<STATE> ( state_value_class,
                                cause_state, // cause
                                transition.violation ( transition, input ) );
            return no_transition;
        }
    }
}
