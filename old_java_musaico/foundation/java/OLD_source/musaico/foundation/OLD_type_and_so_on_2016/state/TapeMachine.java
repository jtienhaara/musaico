package musaico.foundation.state;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;
import musaico.foundation.graph.MutableGraph;
import musaico.foundation.graph.NoGraph;
import musaico.foundation.graph.NodeMustBeInGraph;
import musaico.foundation.graph.SubGraph;

import musaico.foundation.machine.AbstractMachine;
import musaico.foundation.machine.InputsMustCauseTransition;
import musaico.foundation.machine.Machine;

import musaico.foundation.value.Countable;
import musaico.foundation.value.Just;
import musaico.foundation.value.Maybe;
import musaico.foundation.value.NotOne;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueClass;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.abnormal.Error;

import musaico.foundation.value.builder.ValueBuilder;

import musaico.foundation.value.classes.StandardValueClass;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;


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
public class TapeMachine
    extends AbstractMachine<TapeMachine, Value<?>, Transition>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** No state graph at all. */
    public static final NoGraph<Value<?>, Transition> NO_GRAPH =
        new NoGraph<Value<?>, Transition> (
            new StandardValueClass<Value<?>> (   // node_value_class
                Value.class,               // element_class
                new No<Object> (           // none
                    new StandardValueClass<Object> ( // none value
                        Object.class,         // none value element_class
                        "NO_STATE" ) ) ),     // none value none
            Transition.VALUE_CLASS ); // arc_value_class

    /** The generic class of every TapeMachine graph,
     *  ValueClass&lt;Graph&lt;Value&lt;?&gt;, Transition&gt;&gt;. */
    public static ValueClass<Graph<Value<?>, Transition>> GRAPH_VALUE_CLASS =
        new StandardValueClass<Graph<Value<?>, Transition>> (
            Graph.class, // element_class
            NO_GRAPH );  // none

    /** The generic class of every node/state in a TapeMachine,
     *  ValueClass&lt;Value&lt;?&gt;&gt;. */
    public static ValueClass<Value<?>> STATE_VALUE_CLASS =
        new StandardValueClass<Value<?>> (
            Value.class,         // element_class
            NO_GRAPH.entry () ); // none

    /** The generic class of every Arc in a TapeMachine,
     *  ValueClass&lt;Arc&lt;Value&lt;?&gt;, Transition&gt;&gt;. */
    public static ValueClass<Arc<Value<?>, Transition>> ARC_VALUE_CLASS =
        new StandardValueClass<Arc<Value<?>, Transition>> (
            Arc.class, // element_class
            NO_GRAPH.noneArc () );

    /** The generic class of every SubGraph in a TapeMachine,
     *  ValueClass&lt;SubGraph&lt;Value&lt;?&gt;, Transition&gt;&gt;. */
    public static ValueClass<SubGraph<Value<?>, Transition>> SUB_GRAPH_VALUE_CLASS =
        new StandardValueClass<SubGraph<Value<?>, Transition>> (
            SubGraph.class, // element_class
            NO_GRAPH.noneSubGraph () );


    /** No TapeMachine at all. */
    public static final TapeMachine NONE =
        new TapeMachine (
                         new StateGraphBuilder ( "none" )
                             .fromEntry ()
                             .toExit (),
                         true, // is_automatically_transition_input
                         false // is_debuggable
                         );


    // All Tape types that this TapeMachine will ever need
    // for inputs/outputs.  Note that not every Transition will need
    // all Tapes, and not every Transition will ask for the Tapes
    // in the same order.
    // The Integer mapped value is the index at which the tape's
    // corresponding state machine will appear in an array of arguments
    // to transition ( ... ).
    // Note that Tape.THIS never appears in this map, since it is
    // provided internally by the transition () method, not as a parameter
    // passed in externally.
    private final LinkedHashMap<Tape, Integer> tapes;

    // Optional Tapes.  Always come at the end, if they are provided at all.
    private final LinkedHashMap<Tape, Integer> optionalTapes;

    // Should we automagically transition the input tape machine to
    // its exit state whenever possible?  This makes things easier
    // for end users, but can cause problems with transitions such
    // as Copy, which use the last (non-exit) state of a tape machine.
    private final boolean isAutomaticallyTransitionInput;

    // Should we log debug information when asked to during transition ()?
    // Input/output TapeMachines, including the debug logger itself,
    // do not record any debug information at all.
    private final boolean isDebuggable;


    /**
     * <p>
     * Creates a new TapeMachine with a new graph containing
     * an entry node with the specified name, and an exit node.
     * </p>
     *
     * @param name The name of the state graph for the new TapeMachine,
     *             which will also be used as the value for the entry node
     *             in the graph.  Must not be null.
     */
    public TapeMachine (
                        String name
                        )
        throws ParametersMustNotBeNull.Violation,
               NodeMustBeInGraph.Violation
    {
        this ( createGraph ( name,
                             new StandardValueClass<String> ( String.class,
                                                              "" ),
                             new String [ 0 ] ),
               false );
    }


    /**
     * <p>
     * Creates a new TapeMachine with a new graph containing the
     * specified single values as states, and automatic transitions
     * between the states.
     * </p>
     *
     * @param name The name of the state graph for the new TapeMachine,
     *             which will also be used as the value for the entry node
     *             in the graph.  Must not be null.
     *
     * @param values The values to encode as states in a new TapeMachine.
     *               Must not contain any null elements.  Must not be null.
     */
    public TapeMachine (
                        String name,
                        String ... values
                        )
        throws ParametersMustNotBeNull.Violation,
               NodeMustBeInGraph.Violation
    {
        this ( createGraph ( name,
                             new StandardValueClass<String> ( String.class,
                                                              "" ),
                             values ),
               false );
    }


    /**
     * <p>
     * Creates a new TapeMachine with a new graph containing the
     * specified single values as states, and automatic transitions
     * between the states.
     * </p>
     *
     * @param name The name of the state graph for the new TapeMachine,
     *             which will also be used as the value for the entry node
     *             in the graph.  Must not be null.
     *
     * @param values The values to encode as states in a new TapeMachine.
     *               Must not contain any null elements.  Must not be null.
     */
    public TapeMachine (
                        String name,
                        Number ... values
                        )
        throws ParametersMustNotBeNull.Violation,
               NodeMustBeInGraph.Violation
    {
        this ( createGraph ( name,
                             new StandardValueClass<Number> ( Number.class,
                                                              0 ),
                             values ),
               false );
    }


    private static final <STATE extends Object>
        Graph<Value<?>, Transition> createGraph (
                                                 String name,
                                                 ValueClass<STATE> state_class,
                                                 STATE [] states
                                                 )
    {
        final StateGraphBuilder builder =
            new StateGraphBuilder ( name );
        builder.fromEntry ();
        for ( STATE state : states )
        {
            final One<STATE> one_state =
                new One<STATE> ( state_class, state );

            builder.to ( one_state );
            builder.from ( one_state );
        }
        builder.toExit ();

        final Graph<Value<?>, Transition> graph =
            builder.build ();

        return graph;
    }


    /**
     * <p>
     * Creates a new TapeMachine with a new graph containing the
     * specified values as states, and automatic transitions
     * between the states.
     * </p>
     *
     * @param name The name of the state graph for the new TapeMachine,
     *             which will also be used as the value for the entry node
     *             in the graph.  Must not be null.
     *
     * @param values The values to encode as states in a new TapeMachine.
     *               Must not contain any null elements.  Must not be null.
     */
    public TapeMachine (
                        String name,
                        Value<?> ... values
                        )
        throws ParametersMustNotBeNull.Violation,
               NodeMustBeInGraph.Violation
    {
        this ( createGraph ( name,
                             values ),
               false );
    }


    private static final Graph<Value<?>, Transition> createGraph (
            String name,
            Value<?> [] states
            )
    {
        final StateGraphBuilder builder =
            new StateGraphBuilder ( name );
        builder.fromEntry ();
        for ( Value<?> state : states )
        {
            builder.to ( state );
            builder.from ( state );
        }
        builder.toExit ();

        final Graph<Value<?>, Transition> graph =
            builder.build ();

        return graph;
    }


    /**
     * <p>
     * Creates a new TapeMachine.
     * </p>
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new TapeMachine, and whose arcs can be
     *              traversed by this TapeMachine.  Sub-graphs
     *              can be pushed on top of the stack, so that this
     *              Machine ends up pointing to different graphs,
     *              but the bottom graph on the stack will always be
     *              this one.  Must not be null.
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
     */
    public TapeMachine (
                        Graph<Value<?>, Transition> graph,
                        boolean is_automatically_transition_input
                        )
        throws ParametersMustNotBeNull.Violation
    {
        this ( graph,
               is_automatically_transition_input,
               true ); // is_debuggable
    }


    /**
     * <p>
     * Creates a new TapeMachine.
     * </p>
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new TapeMachine, and whose arcs can be
     *              traversed by this TapeMachine.  Sub-graphs
     *              can be pushed on top of the stack, so that this
     *              Machine ends up pointing to different graphs,
     *              but the bottom graph on the stack will always be
     *              this one.  Must not be null.
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
    public TapeMachine (
                        Graph<Value<?>, Transition> graph,
                        boolean is_automatically_transition_input,
                        boolean is_debuggable
                        )
        throws ParametersMustNotBeNull.Violation
    {
        this ( graph,
               graph == null
                   ? null
                   : graph.entry (),
               is_automatically_transition_input,
               is_debuggable );
    }


    /**
     * <p>
     * Creates a new TapeMachine which does NOT automagically
     * transition the input tape machine to its exit state whenever
     * possible.
     * </p>
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new TapeMachine, and whose arcs can be
     *              traversed by this TapeMachine.  Sub-graphs
     *              can be pushed on top of the stack, so that this
     *              Machine ends up pointing to different graphs,
     *              but the bottom graph on the stack will always be
     *              this one.  Must not be null.
     *
     * @see musaico.foundation.state.TapeMachine#TapeMachine(musaico.foundation.graph.Graph,boolean)
     */
    public TapeMachine (
                        Graph<Value<?>, Transition> graph
                        )
        throws ParametersMustNotBeNull.Violation
    {
        this ( graph,
               false );
    }


    /**
     * <p>
     * Creates a new TapeMachine.
     * </p>
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new TapeMachine, and whose arcs can be
     *              traversed by this TapeMachine.  Sub-graphs
     *              can be pushed on top of the stack, so that this
     *              Machine ends up pointing to different graphs,
     *              but the bottom graph on the stack will always be
     *              this one.  Must not be null.
     *
     * @param state The initial state of this new TapeMachine.
     *              Must be a node in the specified graph.
     *              Must not be null.
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
    public TapeMachine (
                        Graph<Value<?>, Transition> graph,
                        Value<?> state,
                        boolean is_automatically_transition_input,
                        boolean is_debuggable
                        )
        throws ParametersMustNotBeNull.Violation,
               NodeMustBeInGraph.Violation
    {
        super ( graph == null // name
                    ? "null graph machine"
                    : "" + graph.entry ().orNull (),
                graph,
                state );

        this.isAutomaticallyTransitionInput =
            is_automatically_transition_input;
        this.isDebuggable = is_debuggable;

        this.tapes = new LinkedHashMap<Tape, Integer> ();
        this.optionalTapes = new LinkedHashMap<Tape, Integer> ();
        if ( ! ( graph instanceof MutableGraph ) )
        {
            // Only get the Tapes required for the underlying
            // graph if the graph is not going to change by the
            // time we need them.
            this.updateTapes ();
        }
    }


    /**
     * <p>
     * Convenience method.  Accepts input(s), returns output(s),
     * and throws any violations.  Performs the boilerplate work
     * to set up an input TapeMachine, turn the output TapeMachine
     * into regular Values, and throw a violation
     * if the TapeMachine cannot transition.
     * </p>
     *
     * @param inputs Zero or more Values to turn into nodes in an
     *               input machine, connected by automatic transition arcs.
     *               Must not be null.  Must not contain any null elements.
     *
     * @return Zero or more Values, in whatever order they are returned
     *         by calling the output StateGraphBuilder's
     *         <code> nodes () </code> method.  Never null.
     *         Never contains any null elements.
     */
    public final Value<?> [] evaluate (
                                       Value<?> ... inputs
                                       )
    {
        return this.evaluate ( null, // debug
                               inputs );
    }


    /**
     * <p>
     * Convenience method.  Accepts input(s), returns output(s),
     * and throws any violations.  Performs the boilerplate work
     * to set up an input TapeMachine, turn the output TapeMachine
     * into regular Values, write debugging information to the
     * provided StringBuilder (if it is not null), and throws
     * a violation if the TapeMachine cannot transition.
     * </p>
     *
     * @param debug Optional StringBuilder to store a debugging
     *              trace of the evaluation.  Can be null.
     *
     * @param inputs Zero or more Values to turn into nodes in an
     *               input machine, connected by automatic transition arcs.
     *               Must not be null.  Must not contain any null elements.
     *
     * @return Zero or more Values, in whatever order they are returned
     *         by calling the output StateGraphBuilder's
     *         <code> nodes () </code> method.  Never null.
     *         Never contains any null elements.
     */
    public Value<?> [] evaluate (
                                 StringBuilder debug,
                                 Value<?> ... inputs
                                 )
        throws InputsMustCauseTransition.Violation
    {
        // Let ourselves fail to transition () if we require
        // tapes other than input and output.

        final TapeMachine input_machine = new TapeMachine ( "input", inputs );
        final TapeMachine output_machine =
            new TapeMachine (
                new StateGraphBuilder ( "output" ), // graph
                false, // is_automatically_transition_input
                false ); // is_debuggable

        final Maybe<Value<?>> result_state;
        final TapeMachine debug_machine;
        final TapeMachine [] parameter_machines;
        synchronized ( this.lock () )
        {
            if ( this.graph () instanceof MutableGraph )
            {
                this.updateTapes ();
            }

            final boolean is_input =
                this.tapes.containsKey ( Tape.INPUT );

            final boolean is_output =
                this.tapes.containsKey ( Tape.OUTPUT );

            final boolean is_debug_optional =
                this.isDebuggable ()
                &&
                this.optionalTapes.containsKey ( Tape.DEBUG );

            final int num_known_required_parameters =
                ( is_input ? 1 : 0 )
                + ( is_output ? 1 : 0 );

            final boolean is_debug;
            if ( debug != null
                 && is_debug_optional
                 && num_known_required_parameters == this.tapes.size () )
            {
                // Gather debug output.
                is_debug = true;
                debug_machine =
                    new TapeMachine (
                        new StateGraphBuilder ( "debug" )
                            .fromEntry ()
                                .to ( "-" )
                            .from ( "-" )
                                .copy ( Tape.INPUT, Tape.THIS )
                                .to ( "-" )
                            .from ( "-" ), // graph
                        false, // is_automatically_transition_input
                        false ); // is_debuggable
            }
            else
            {
                // Either no debug buffer was provided,
                // or a 3rd machine would be misconstrued as a different tape,
                // or DEBUG is not allowed as an optional tape.
                // In all 3 cases, we do NOT provide any debug output.
                is_debug = false;
                debug_machine = null;
            }

            // Figure out all the machines we need to pass in as parameters
            // to transition ():
            if ( is_input ) // DO provide input.
            {
                if ( is_output ) // DO gather output.
                {
                    if ( is_debug ) // DO gather debug.
                    {
                        parameter_machines = new TapeMachine [] {
                            input_machine,
                            output_machine,
                            debug_machine
                        };
                    }
                    else // Do NOT gather debug.
                    {
                        parameter_machines = new TapeMachine [] {
                            input_machine,
                            output_machine
                        };
                    }
                }
                else // Do NOT gather output.
                {
                    if ( is_debug ) // DO gather debug.
                    {
                        parameter_machines = new TapeMachine [] {
                            input_machine,
                            debug_machine
                        };
                    }
                    else // Do NOT gather debug.
                    {
                        parameter_machines = new TapeMachine [] {
                            input_machine
                        };
                    }
                }
            }
            else // Do NOT provide input.
            {
                if ( is_output ) // DO gather output.
                {
                    if ( is_debug ) // DO gather debug.
                    {
                        parameter_machines = new TapeMachine [] {
                            output_machine,
                            debug_machine
                        };
                    }
                    else // Do NOT gather debug.
                    {
                        parameter_machines = new TapeMachine [] {
                            output_machine
                        };
                    }
                }
                else // Do NOT gather output.
                {
                    if ( is_debug ) // DO gather debug.
                    {
                        parameter_machines = new TapeMachine [] {
                            debug_machine
                        };
                    }
                    else // Do NOT gather debug.
                    {
                        parameter_machines = new TapeMachine [] {
                        };
                    }
                }
            }

            // Now perform the transition(s):
            result_state = this.transition ( parameter_machines );
        }

        // Output debug info, if applicable:
        if ( debug != null
             && debug_machine != null )
        {
            // Gather up the debugging information.
            for ( Value<?> debug_node : debug_machine.graph ().nodes () )
            {
                if ( debug_node instanceof One )
                {
                    debug.append ( "" + debug_node.orNull () );
                    debug.append ( "\n" );
                }
                else
                {
                    for ( Object debug_line : debug_node )
                    {
                        debug.append ( "  " + debug_line );
                        debug.append ( "\n" );
                    }
                }
            }
        }

        // Throw violation:
        if ( result_state.orNull () == null )
        {
            final ValueViolation cause;
            if ( result_state instanceof NotOne )
            {
                final NotOne<Value<?>> failed_result =
                    (NotOne<Value<?>>) result_state;
                cause = failed_result.valueViolation ();
            }
            else
            {
                cause = null;
            }

            @SuppressWarnings("unchecked") // Generic array creation
            final InputsMustCauseTransition.Violation violation =
                this.inputsMustCauseTransition ().violation (
                    this,
                    cause,
                    parameter_machines );
            throw violation;
        }

        // Gather up the outputs.
        final List<Value<?>> output_values_list =
            new ArrayList<Value<?>> ();
        for ( Value<?> output_node : output_machine.graph ().nodes () )
        {
            if ( output_node instanceof Entry )
            {
                continue;
            }
            else if ( output_node instanceof Exit )
            {
                continue;
            }

            output_values_list.add ( output_node );
        }

        @SuppressWarnings("rawtypes") // Generic array creation
        final Value<?> [] template = (Value<?> [])
            new Value [ output_values_list.size () ];
        final Value<?> [] output_values =
            output_values_list.toArray ( template );

        return output_values;
    }


    protected void finishInput (
                                TapeMachine input
                                )
    {
        if ( input == null )
        {
            return;
        }

        // If there is an input machine, transition it
        // to the end state, if there is any arc leading
        // from its current state to its exit state.
        final Value<?> input_state = input.state ();
        final Value<Arc<Value<?>, Transition>> input_arcs =
            input.graph ().arcs ( input_state );
        final Value<?> input_end = input.graph ().exit ();
        for ( Arc<Value<?>, Transition> input_arc : input_arcs )
        {
            if ( input_arc != null
                 && input_arc.to () == input_end )
            {
                // We're done with input.
                input.forceState ( input_end );
            }
        }
    }


    /**
     * @return True if this machine should automagically transition the
     *         input tape machine to its exit state whenever
     *         possible.  This makes things easier for end users,
     *         but can cause problems with transitions such
     *         as Copy, which use the last (non-exit) state
     *         of a tape machine.
     */
    public final boolean isAutomaticallyTransitionInput ()
    {
        return this.isAutomaticallyTransitionInput;
    }


    /**
     * @return True if the TapeMachine should record
     *         debugging information whenever a debug
     *         machine is passed to the transition ()
     *         method; false if debug information should
     *         be suppressed no matter what.  Typically
     *         only input/output and debug TapeMachines have this
     *         flag set to false; most TapeMachines
     *         should set it to true to enable debugging when
     *         the caller asks for it.
     */
    public final boolean isDebuggable ()
    {
        return this.isDebuggable;
    }


    /**
     * <p>
     * Returns true if the specified tape is allowed as a parameter
     * to transitions in this TapeMachine, after all the required
     * tape parameters are provided.
     * </p>
     *
     * @param tape The tape which might or might not be used by
     *             this TapeMachine.  For example, Tape.THIS
     *             or Tape.DEBUG, and so on.  Must not be null.
     *
     * @return True if this TapeMachine might read, write, and/or
     *         induce state changes on the TapeMachine corresponding
     *         to the specified tape; false if it does not expect
     *         the specified tape.
     */
    public final boolean isTapeOptional (
                                         Tape tape
                                         )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  tape );

        synchronized ( this.lock () )
        {
            if ( this.graph () instanceof MutableGraph )
            {
                this.updateTapes ();
            }

            if ( this.optionalTapes.containsKey ( tape ) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }


    /**
     * <p>
     * Returns true if the specified tape might be used by this
     * TapeMachine (that is, it is included in the Tapes
     * returned by the <code> tapes () </code> method, and must
     * therefore be passed, in order, to the <code> transition () </code>
     * method).
     * </p>
     *
     * <p>
     * Note that <code> Tape.THIS </code> is never a required parameter
     * for any TapeMachine, even if its Transitions do require it.
     * Tape.THIS is always provided internally by the tape machine.
     * </p>
     *
     * @param tape The tape which might or might not be used by
     *             this TapeMachine.  For example, Tape.INPUT
     *             or Tape.OUTPUT, and so on.  Must not be null.
     *
     * @return True if this TapeMachine might read, write, and/or
     *         induce state changes on the TapeMachine corresponding
     *         to the specified tape; false if it does not expect
     *         the specified tape.
     */
    public final boolean isTapeRequired (
                                         Tape tape
                                         )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  tape );

        synchronized ( this.lock () )
        {
            if ( this.graph () instanceof MutableGraph )
            {
                this.updateTapes ();
            }

            if ( this.tapes.containsKey ( tape ) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }


    /**
     * @return The tape(s) which can be used by this TapeMachine,
     *         but are not required, during calls to the
     *         <code> transition ( ... ) </code> method.
     *         For example, Tape.DEBUG can be passed to capture
     *         troubleshooting information about transitions,
     *         but normally it is not provided to <code> transition () </code>.
     *         For each Tape returned, a corresponding TapeMachine may be
     *         passed to the <code> transition ( ... ) </code>
     *         method, in the same order as the Tapes they
     *         correspond to.  The optional Tapes for a given TapeMachine
     *         might change over time, if its underlying
     *         Graph is a MutableGraph.  This is because Transitions
     *         can be added to or removed from a MutableGraph
     *         over time.  Never null.
     */
    public final Countable<Tape> optionalTapes ()
        throws ReturnNeverNull.Violation
    {
        final ValueBuilder<Tape> builder;
        synchronized ( this.lock () )
        {
            if ( this.graph () instanceof MutableGraph )
            {
                this.updateTapes ();
            }

            builder = new ValueBuilder<Tape> ( Tape.VALUE_CLASS,
                                               this.optionalTapes.keySet () );
        }

        final Countable<Tape> optional_tapes = builder.build ();

        return optional_tapes;
    }


    /**
     * <p>
     * Returns all of the Arcs to be considered for the next transition
     * from the specified state, given the specified tape machines
     * as inputs/outputs.
     * </p>
     *
     * <p>
     * A TapeMachine, by default, returns all Arcs out of the
     * specified state.  However other implementations might look
     * up the best Transition based on the input.  And so on.
     * </p>
     *
     * @param state The state from which to transition.
     *              Must not be null.
     *
     * @param tape_machines The ordered tape machines for the next
     *                      transition.  Must be in order of the
     *                      <code> tapes () </code> of this TapeMachine,
     *                      with <code> optionalTapes () </code>
     *                      provided at the end, also in order.
     *                      Must not contain any null elements.
     *                      Must not be null.
     *
     * @return All Arcs to try transitioning across from the specified state.
     *         Never null.
     */
    protected Value<Arc<Value<?>, Transition>> possibleTransitionArcs (
            Value<?> state,
            TapeMachine ... tape_machines
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  state, tape_machines );

        this.contracts ().check ( Parameter2.MustContainNoNulls.CONTRACT,
                                  tape_machines );

        final Graph<Value<?>, Transition> graph = this.graph ();

        final Value<Arc<Value<?>, Transition>> arcs;
        synchronized ( this.lock () )
        {
            // Critical section just in case it's a MutableGraph
            // (whose arcs can change from one instant to the next).
            arcs = graph.arcs ( state );
        }

        return arcs;
    }


    /**
     * @see musaico.foundation.machine.Machine#state(java.lang.Object)
     */
    @Override
    public TapeMachine state (
                              Value<?> state
                              )
        throws ParametersMustNotBeNull.Violation,
               NodeMustBeInGraph.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  state );

        // The state specified MUST be in the bottom level graph of
        // this machine.
        final Maybe<Value<?>> state_in_graph =
            this.graph ().node ( state );
        if ( state_in_graph instanceof NotOne )
        {
            final NotOne<Value<?>> not_state =
                (NotOne<Value<?>>) state_in_graph;

            final ValueViolation violation = not_state.valueViolation ();
            if ( violation.getCause () instanceof NodeMustBeInGraph.Violation )
            {
                throw (NodeMustBeInGraph.Violation) violation.getCause ();
            }
            else
            {
                final NodeMustBeInGraph.Violation node_is_not_in_graph =
                    new NodeMustBeInGraph<Value<?>> ( this.graph () )
                            .violation ( this, state,
                                         violation ); // cause

                throw node_is_not_in_graph;
            }
        }

        final TapeMachine new_machine =
            new TapeMachine ( this.graph (),
                              state_in_graph.orNull (),
                              this.isAutomaticallyTransitionInput,
                              this.isDebuggable );

        return new_machine;
    }


    /**
     * @return The tape(s) used by this TapeMachine, if any, during
     *         calls to the <code> transition ( ... ) </code> method.
     *         For example, Tape.INPUT or Tape.OUTPUT,
     *         or both, or some other tape(s).  And so on.
     *         For each Tape returned, a corresponding TapeMachine must be
     *         passed to the <code> transition ( ... ) </code>
     *         method, in the same order as the Tapes they
     *         correspond to.  The Tapes for a given TapeMachine
     *         might change over time, if its underlying
     *         Graph is a MutableGraph.  This is because Transitions
     *         can be added to or removed from a MutableGraph
     *         over time.  Note that Tape.THIS is never a required
     *         Tape parameter, since this tape machine provides it
     *         internally.  Never null.
     */
    public final Countable<Tape> tapes ()
        throws ReturnNeverNull.Violation
    {
        final ValueBuilder<Tape> builder;
        synchronized ( this.lock () )
        {
            if ( this.graph () instanceof MutableGraph )
            {
                this.updateTapes ();
            }

            builder = new ValueBuilder<Tape> ( Tape.VALUE_CLASS,
                                               this.tapes.keySet () );
        }

        final Countable<Tape> tapes = builder.build ();

        return tapes;
    }


    /**
     * @see musaico.foundation.machine.Machine#transition(java.lang.Object...)
     */
    @SuppressWarnings("unchecked") // Possible heap pollution INPUT...
    public Maybe<Value<?>> transition (
                                       TapeMachine ... tape_machines
                                       )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        final Value<?> old_state;
        final Maybe<Value<?>> maybe_new_state;
        TapeMachine debug_machine = null;
        synchronized ( this.lock () )
        {
            final LinkedHashMap<Tape, Integer> required_tapes;
            final LinkedHashMap<Tape, Integer> optional_tapes;
            if ( this.graph () instanceof MutableGraph )
            {
                this.updateTapes ();

                required_tapes =
                    new LinkedHashMap<Tape, Integer> ( this.tapes );
                optional_tapes =
                    new LinkedHashMap<Tape, Integer> ( this.optionalTapes );
            }
            else
            {
                required_tapes = this.tapes;
                optional_tapes = this.optionalTapes;
            }

            int debug_index = -1;
            if ( this.isDebuggable )
            {
                debug_index = new ArrayList<Tape> ( required_tapes.keySet () )
                                  .indexOf ( Tape.DEBUG );
                if ( debug_index < 0 )
                {
                    debug_index =
                        new ArrayList<Tape> ( optional_tapes.keySet () )
                            .indexOf ( Tape.DEBUG );
                }

                if ( debug_index >= 0
                     && debug_index < tape_machines.length )
                {
                    debug_machine = tape_machines [ debug_index ];
                    System.err.println ( "!!! TAPE MACHINE " + this + " INDEX = " + debug_index + " DEBUG = " + debug_machine );

                    AbstractTransition.debugMessage (
                        debug_machine,
                        new Date (),
                        "" + this + " BEGIN transition ()" );
                }
            }

            if ( tape_machines.length < required_tapes.size () )
            {
                final List<Tape> missing_tapes =
                    new ArrayList<Tape> ();
                int tape_num = 0;
                for ( Tape tape : required_tapes.keySet () )
                {
                    if ( tape_num >= tape_machines.length )
                    {
                        missing_tapes.add ( tape );
                    }

                    tape_num ++;
                }

                final TransitionRequiresTapes.Violation violation =
                    new TransitionRequiresTapes ( missing_tapes )
                                   .violation ( this, tape_machines );
                final Error<Value<?>> no_state_change =
                    new Error<Value<?>> ( TapeMachine.STATE_VALUE_CLASS,
                                          violation );

                if ( debug_machine != null )
                {
                    AbstractTransition.debugMessage (
                        debug_machine,
                        new Date (),
                        "" + this + " END transition () FAILURE: "
                                                     + no_state_change );
                }

                return no_state_change;
            }

            final Graph<Value<?>, Transition> graph = this.graph ();
            old_state = this.state ();

            final Value<Arc<Value<?>, Transition>> possible_transition_arcs =
                this.possibleTransitionArcs ( old_state,
                                              tape_machines );

            // If we have no arcs to transition over (for example
            // because we're already at the exit state of the graph)
            // then make sure we use up the input, if possible
            // (exactly one arc leading out of its current
            // state to the "exit" state).
            if ( this.isAutomaticallyTransitionInput
                 && ! ( possible_transition_arcs instanceof Just ) )
            {
                int t = 0;
                for ( Tape transition_tape : required_tapes.keySet () )
                {
                    if ( transition_tape == Tape.INPUT )
                    {
                        final TapeMachine input = tape_machines [ t ];
                        this.finishInput ( input );
                    }

                    t ++;
                }
            }

            for ( Arc<Value<?>, Transition> arc : possible_transition_arcs )
            {
                final Transition transition = arc.arc ();

                // Figure out what order the Transition wants
                // the tape machines in.
                final Countable<Tape> transition_tapes = transition.tapes ();
                final TapeMachine [] transition_tape_machines =
                    new TapeMachine [ (int) transition_tapes.length () ];
                int input_tape_machine_index = -1;
                int t = -1;
                for ( Tape transition_tape : transition_tapes )
                {
                    t ++;

                    if ( transition_tape == Tape.THIS )
                    {
                        transition_tape_machines [ t ] = this;
                        continue;
                    }
                    else if ( ! transition_tape.isMandatory () )
                    {
                        // Optional tape.
                        final Integer tape_machine_num =
                            optional_tapes.get ( transition_tape );

                        // Set it to TapeMachine.NONE.
                        if ( tape_machine_num == null
                             || tape_machine_num.intValue () >= tape_machines.length )
                        {
                            transition_tape_machines [ t ] = TapeMachine.NONE;
                        }
                        else
                        {
                            final TapeMachine tape_machine =
                                tape_machines [ tape_machine_num.intValue () ];
                            transition_tape_machines [ t ] = tape_machine;
                        }

                        continue;
                    }

                    final Integer tape_machine_num =
                        required_tapes.get ( transition_tape );
                    if ( tape_machine_num == null
                         || tape_machine_num.intValue () >= tape_machines.length )
                    {
                        final TransitionRequiresTapes requires_tapes =
                            new TransitionRequiresTapes ( transition_tapes );
                        final TransitionRequiresTapes.Violation violation =
                            requires_tapes.violation ( this,
                                                       tape_machines );
                        final Error<Value<?>> no_new_state =
                            new Error<Value<?>> (
                                TapeMachine.STATE_VALUE_CLASS,
                                violation );

                        if ( debug_machine != null )
                        {
                            AbstractTransition.debugMessage (
                                debug_machine,
                                new Date (),
                                "" + this + " END transition () FAILURE: "
                                    + no_new_state );
                        }

                        return no_new_state;
                    }

                    final TapeMachine tape_machine =
                        tape_machines [ tape_machine_num.intValue () ];
                    transition_tape_machines [ t ] = tape_machine;

                    if ( transition_tape == Tape.INPUT )
                    {
                        input_tape_machine_index = t;
                    }
                }

                final ZeroOrOne<Value<?>> transition_new_state =
                    this.transition ( graph,
                                      old_state,
                                      transition,
                                      arc.to (),
                                      transition_tape_machines,
                                      input_tape_machine_index );

                if ( transition_new_state.orNull () != null )
                {
                    this.forceState ( transition_new_state.orNull () );

                    if ( debug_machine != null )
                    {
                        AbstractTransition.debugMessage (
                            debug_machine,
                            new Date (),
                            "" + this + " END transition () SUCCESS: "
                                + transition_new_state );
                    }

                    return transition_new_state;
                }
                // Otherwise no state change.
            }
        }

        @SuppressWarnings("unchecked") // Generic array creation
        final InputsMustCauseTransition.Violation violation =
            this.inputsMustCauseTransition ().violation ( this,
                                                          null, // cause
                                                          tape_machines );
        final Error<Value<?>> no_state_change =
            new Error<Value<?>> ( TapeMachine.STATE_VALUE_CLASS,
                                  new One<Value<?>> ( // cause
                                       TapeMachine.STATE_VALUE_CLASS,
                                       old_state ),
                                  violation );

        if ( debug_machine != null )
        {
            AbstractTransition.debugMessage (
                debug_machine,
                new Date (),
                "" + this + " END transition () FAILURE: "
                    + no_state_change );
        }

        return no_state_change;
    }


    /**
     * <p>
     * Once the tapes have been ordered, attempts to proceed
     * across a single Transition in the graph.
     * </p>
     *
     * @param graph The current Graph at the top of this machine's
     *              stack.  Must not be null.
     *
     * @param from_state The starting state in the current graph.
     *                   Must not be null.
     *
     * @param transition The arc across which the transition will
     *                   be attempted.  Must not be null.
     *
     * @param to_state The target state of the transition.
     *                 Must not be null.
     *
     * @param tape_machines The (0 or more) machines corresponding to
     *                      the specified Transition's required Tapes.
     *                      Must contain exactly the number of required
     *                      tapes, in the correct order.  Must not
     *                      contain any null elements.  Must not be null.
     *
     * @param input_tape_machine_index The index of the <code>Tape.INPUT</code>
     *                                 tape machine, if it is required
     *                                 by the specified Transition
     *                                 (0..# of required Tapes - 1);
     *                                 or -1 if it is not required.
     *
     * @return ZeroOrOne successful transitions to a new state.
     *         Either the transition worked, or it didn't.
     *         If it did, then the result will be One target state.
     *         If it didn't, then the result will be No target state.
     *         Never null.
     */
    protected ZeroOrOne<Value<?>> transition (
            Graph<Value<?>, Transition> graph,
            Value<?> from_state,
            Transition transition,
            Value<?> to_state,
            TapeMachine [] tape_machines,
            int input_tape_machine_index
            )
        throws ReturnNeverNull.Violation
    {
        // If there is an input machine, transition it
        // to the end state, if there is no more input
        // (exactly one arc leading out of its current
        // state to the "exit" state).
        final TapeMachine input;
        if ( input_tape_machine_index < 0 )
        {
            input = null;
        }
        else
        {
            input = tape_machines [ input_tape_machine_index ];

            if ( this.isAutomaticallyTransitionInput )
            {
                this.finishInput ( input );
            }
        }

        final boolean is_transitioned =
            transition.transition ( tape_machines );

        if ( is_transitioned )
        {
            // Successful transition.
            final One<Value<?>> one_transition =
                new One<Value<?>> ( TapeMachine.STATE_VALUE_CLASS,
                                    to_state );
            return one_transition;
        }
        else
        {
            // Could not transition.
            if ( this.isAutomaticallyTransitionInput
                 && input != null )
            {
                this.finishInput ( input );
            }

            @SuppressWarnings("unchecked") // Generic array creation
            final InputsMustCauseTransition.Violation violation =
                this.inputsMustCauseTransition ().violation ( this,
                                                              null, // cause
                                                              tape_machines );

            final No<Value<?>> no_transition =
                new No<Value<?>> ( TapeMachine.STATE_VALUE_CLASS,
                                   new One<Value<?>> ( // cause
                                       TapeMachine.STATE_VALUE_CLASS,
                                       from_state ),
                                   violation );
            return no_transition;
        }
    }


    /**
     * <p>
     * Re-loads the Tapes from the underlying Graph.
     * </p>
     */
    protected void updateTapes ()
    {
        synchronized ( this.lock () )
        {
            this.tapes.clear ();
            this.optionalTapes.clear ();
            for ( Arc<Value<?>, Transition> arc : this.graph ().arcs () )
            {
                final Transition transition = arc.arc ();
                for ( Tape tape : transition.tapes () )
                {
                    if ( tape == Tape.THIS )
                    {
                        // Never required as a parameter;
                        // always provided internally.
                        continue;
                    }
                    else if ( ! tape.isMandatory () )
                    {
                        // Optional Tape.
                        if ( ! this.optionalTapes.containsKey ( tape ) )
                        {
                            // We'll push all optional tapes to
                            // the end, after the last required tape
                            // index, after we're done gathering
                            // all required tapes.
                            final int index = this.optionalTapes.size ();
                            this.optionalTapes.put ( tape, index );
                        }
                    }
                    else if ( ! this.tapes.containsKey ( tape ) )
                    {
                        final int index = this.tapes.size ();
                        this.tapes.put ( tape, index );
                    }
                }
            }

            // Now push the optional tapes to the end, after the last
            // index of required tapes.
            final int option_offset = this.tapes.size ();
            for ( Tape optional_tape
                      : new ArrayList<Tape> ( this.optionalTapes.keySet () ) )
            {
                final Integer option_index =
                    this.optionalTapes.get ( optional_tape );
                final int index = option_offset + option_index.intValue ();
                this.optionalTapes.put ( optional_tape, index );
            }
        }
    }
}
