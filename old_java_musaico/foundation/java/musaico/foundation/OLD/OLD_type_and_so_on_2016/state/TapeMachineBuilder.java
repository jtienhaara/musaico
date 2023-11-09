package musaico.foundation.state;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.ImmutableGraph;

import musaico.foundation.value.Value;


/**
 * <p>
 * A TapeMachine with a mutable Graph.
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
public class TapeMachineBuilder
    extends TapeMachine
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new TapeMachineBuilder with an empty initial
     * StateGraphBuilder.
     * </p>
     *
     * @param name The name of the state-being-built, which will also
     *             be used as the value for the entry node in the graph.
     *             Must not be null.
     *
     * @param is_automatically_transition_input Should we automagically
     *                                          transition the
     *                                          input tape machine to
     *                                          its exit state whenever
     *                                          possible?  This makes things
     *                                          easier for end users,
     *                                          but can cause problems
     *                                          with transitions such
     *                                          as Copy, which use the
     *                                          last (non-exit) state
     *                                          of a tape machine.
     *
     * @param is_debuggable True if the TapeMachine should record
     *                      debugging information whenever a debug
     *                      machine is passed to the transition ()
     *                      method; false if debug information should
     *                      be suppressed no matter what.  Typically
     *                      only input/output and debug TapeMachines have this
     *                      flag set to false; most TapeMachines
     *                      should set it to true to enable debugging when
     *                      the caller asks for it.
     */
    public TapeMachineBuilder (
                               String name,
                               boolean is_automatically_transition_input,
                               boolean is_debuggable
                               )
        throws ParametersMustNotBeNull.Violation
    {
        this ( new StateGraphBuilder (
                                      name
                                      ),
               is_automatically_transition_input,
               is_debuggable );
    }


    /**
     * <p>
     * Creates a new TapeMachineBuilder.
     * </p>
     *
     * @param state_graph_builder The mutable graph whose nodes
     *                            are the possible states of
     *                            this new TapeMachineBuilder,
     *                            and whose arcs can be traversed
     *                            by this TapeMachineBuilder.
     *                            Must not be null.
     *
     * @param is_automatically_transition_input Should we automagically
     *                                          transition the
     *                                          input tape machine to
     *                                          its exit state whenever
     *                                          possible?  This makes things
     *                                          easier for end users,
     *                                          but can cause problems
     *                                          with transitions such
     *                                          as Copy, which use the
     *                                          last (non-exit) state
     *                                          of a tape machine.
     *
     * @param is_debuggable True if the TapeMachine should record
     *                      debugging information whenever a debug
     *                      machine is passed to the transition ()
     *                      method; false if debug information should
     *                      be suppressed no matter what.  Typically
     *                      only input/output and debug TapeMachines have this
     *                      flag set to false; most TapeMachines
     *                      should set it to true to enable debugging when
     *                      the caller asks for it.
     */
    public TapeMachineBuilder (
                               StateGraphBuilder state_graph_builder,
                               boolean is_automatically_transition_input,
                               boolean is_debuggable
                               )
        throws ParametersMustNotBeNull.Violation
    {
        super ( state_graph_builder,
                is_automatically_transition_input );
    }


    /**
     * <p>
     * Builds an immutable TapeMachine from the current state and the
     * underlying StateGraphBuilder's states and Transitions, including
     * the StateGraphBuilder's automatically generated exit arcs.
     * </p>
     *
     * @see musaico.foundation.tape.StateGraphBuilder#build()
     *
     * @return A new, immutable TapeMachine, with an ImmutableGraph
     *         and the current state.  Never null.
     */
    public TapeMachine build ()
        throws ReturnNeverNull.Violation
    {
        final ImmutableGraph<Value<?>, Transition> graph;
        final Value<?> state;
        synchronized ( this.lock () )
        {
            graph = this.graphBuilder ()
                        .build ();

            state = this.state ();
        }

        final TapeMachine immutable_tape_machine =
            new TapeMachine ( graph,
                              state,
                              this.isAutomaticallyTransitionInput (),
                              this.isDebuggable () );

        return immutable_tape_machine;
    }


    /**
     * @return The StateGraphBuilder underlying this machine builder.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Cast Graph<V,T> to StateGraphBuilder.
    public final StateGraphBuilder graphBuilder ()
        throws ReturnNeverNull.Violation
    {
        final StateGraphBuilder builder = (StateGraphBuilder) this.graph ();
        return builder;
    }
}
