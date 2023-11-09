package musaico.foundation.machine;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;
import musaico.foundation.graph.NoGraph;
import musaico.foundation.graph.NodeMustBeInGraph;
import musaico.foundation.graph.NodesMustBeConnected;

import musaico.foundation.value.Countable;
import musaico.foundation.value.Just;
import musaico.foundation.value.Maybe;
import musaico.foundation.value.NotOne;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueClass;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.builder.ValueBuilder;

import musaico.foundation.value.classes.StandardValueClass;

import musaico.foundation.value.finite.No;


/**
 * <p>
 * The foundation for most state machine implementations.
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
public abstract class AbstractMachine<INPUT extends Object, STATE extends Object, ARC extends Object>
    implements Machine<INPUT, STATE, ARC>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( AbstractMachine.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // Lock critical sections on this token:
    private final Serializable lock = new String ();

    // Human-readable name of this machine.
    private final String name;

    // The "each input must cause a state transition" contract
    // for this Machine.  Generates violations whenever we cannot
    // change state given the inputs to transition ().
    private final InputsMustCauseTransition<INPUT, STATE, ARC> inputsMustCauseTransition;

    // The ValueClass for the graph underpinning this machine.
    private final ValueClass<Graph<STATE, ARC>> graphValueClass;

    // The Graph describing the states and arcs between states
    // of this Machine.
    private final Graph<STATE, ARC> graph;

    // The ValueClass for the inputs which trigger state transitions
    // in this machine.
    private final ValueClass<INPUT> inputValueClass;

    // MUTABLE:
    // The current state of this machine, which is a node in the graph.
    private STATE state;


    /**
     * <p>
     * Creates a new AbstractMachine.
     * </p>
     *
     * @param name The name of this Machine.  Must not be null.
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new AbstractMachine, and whose arcs can be
     *              traversed by this AbstractMachine.  Must not be null.
     *
     * @param input_value_class The type of inputs which induce state
     *                          transitions in this machine, such as
     *                          StandardValueClass<String> for a Machine
     *                          triggered by text inputs, and so on.
     *                          Must not be null.
     */
    public AbstractMachine (
                            String name,
                            Graph<STATE, ARC> graph,
                            ValueClass<INPUT> input_value_class
                            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name,
               graph,
               input_value_class,
               graph == null
                   ? null
                   : graph.entry () );
    }


    /**
     * <p>
     * Creates a new AbstractMachine.
     * </p>
     *
     * @param name The name of this Machine.  Must not be null.
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new AbstractMachine, and whose arcs can be
     *              traversed by this AbstractMachine.  Must not be null.
     *
     * @param input_value_class The type of inputs which induce state
     *                          transitions in this machine, such as
     *                          StandardValueClass<String> for a Machine
     *                          triggered by text inputs, and so on.
     *                          Must not be null.
     *
     * @param state The initial state of this new AbstractMachine.
     *              Must be a node in the specified graph.
     *              Must not be null.
     */
    public AbstractMachine (
                            String name,
                            Graph<STATE, ARC> graph,
                            ValueClass<INPUT> input_value_class,
                            STATE state
                            )
        throws ParametersMustNotBeNull.Violation,
               NodeMustBeInGraph.Violation
    {
        this ( name,                                       // name
               graph,                                      // graph
               input_value_class,                          // input_value_class
               state,                                      // state
               new StandardValueClass<Graph<STATE, ARC>> ( // graph_value_class
                   Graph.class,    // element_class
                   new NoGraph<STATE, ARC> (
                       graph == null
                           ? null
                           : graph.nodeValueClass (),
                       graph == null
                           ? null
                           : graph.arcValueClass () ) )
               );
    }


    /**
     * <p>
     * Creates a new AbstractMachine.
     * </p>
     *
     * @param name The name of this Machine.  Must not be null.
     *
     * @param graph The graph whose nodes are the possible states of
     *              this new AbstractMachine, and whose arcs can be
     *              traversed by this AbstractMachine.  Must not be null.
     *
     * @param input_value_class The type of inputs which induce state
     *                          transitions in this machine, such as
     *                          StandardValueClass<String> for a Machine
     *                          triggered by text inputs, and so on.
     *                          Must not be null.
     *
     * @param state The initial state of this new AbstractMachine.
     *              Must be a node in the specified graph.
     *              Must not be null.
     *
     * @param graph_value_class The ValueClass describing the
     *                          Graph underpinning this machine.
     *                          Must not be null.
     */
    public AbstractMachine (
                            String name,
                            Graph<STATE, ARC> graph,
                            ValueClass<INPUT> input_value_class,
                            STATE state,
                            ValueClass<Graph<STATE, ARC>> graph_value_class
                            )
        throws ParametersMustNotBeNull.Violation,
               NodeMustBeInGraph.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name, graph, input_value_class,
                               state, graph_value_class );

        this.name = name;

        final NodeMustBeInGraph<STATE> state_must_be_in_graph =
            new NodeMustBeInGraph<STATE> ( graph );
        classContracts.check ( state_must_be_in_graph,
                               state );

        this.graph = graph;

        this.inputValueClass = input_value_class;

        this.state = state;

        this.graphValueClass = graph_value_class;

        this.contracts = new Advocate ( this );

        this.inputsMustCauseTransition =
            new InputsMustCauseTransition<INPUT, STATE, ARC> ( this );
    }


    /**
     * @return This AbstractMachine's contracts checker, for use by\
     *         derived classes to ensure method parameter obligations
     *         and return value guarantees and so on are met.  Never null.
     */
    protected final Advocate contracts ()
        throws ReturnNeverNull.Violation
    {
        return this.contracts;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked" ) // Cast It<Graph<?, ?>> - It<Graph<?, ?>>
        // (seriously...!)
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

        final AbstractMachine<?, ?, ?> that =
            (AbstractMachine<?, ?, ?>) object;
        final STATE this_state = this.state ();
        final Object that_state = that.state ();
        if ( this_state == null )
        {
            if ( that_state == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that_state == null )
        {
            return false;
        }
        else if ( ! this_state.equals ( that_state ) )
        {
            return false;
        }

        if ( this.graph == null )
        {
            if ( that.graph == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.graph == null )
        {
            return false;
        }

        if ( ! this.graph.equals ( that.graph ) )
        {
            return false;
        }

        return true;
    }


    /**
     * <p>
     * Forces this AbstractMachine to enter the specified state,
     * without first transitioning through any arcs.
     * </p>
     *
     * @param state The new state for this AbstractMachine.
     *              Must not be null.
     *
     * @return This AbstractMachine.  Never null.
     */
    public AbstractMachine<INPUT, STATE, ARC> forceState (
            STATE state
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               state );

        synchronized ( this.lock )
        {
            this.state = state;
        }

        return this;
    }


    /**
     * @see musaico.foundation.Machine#graph()
     */
    @Override
    public final Graph<STATE, ARC> graph ()
        throws ReturnNeverNull.Violation
    {
        return this.graph;
    }


    /**
     * @see musaico.foundation.machine.Machine#graphValueClass()
     */
    @Override
    public final ValueClass<Graph<STATE, ARC>> graphValueClass ()
        throws ReturnNeverNull.Violation
    {
        return this.graphValueClass;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.graph.hashCode () * 17;
    }


    /**
     * @return The "each input must cause a state transition" contract
     *         for this Machine.  Generates violations whenever we cannot
     *         change state given the inputs to transition ().
     *         Never null.
     */
    public InputsMustCauseTransition<INPUT, STATE, ARC> inputsMustCauseTransition ()
        throws ReturnNeverNull.Violation
    {
        return this.inputsMustCauseTransition;
    }


    /**
     * @see musaico.foundation.machine.AbstractMachine#inputValueClass()
     */
    @Override
    public final ValueClass<INPUT> inputValueClass ()
        throws ReturnNeverNull.Violation
    {
        return this.inputValueClass;
    }


    /**
     * @see musaico.foundation.machine.AbstractMachine#isAtEntry()
     */
    @Override
    public final boolean isAtEntry ()
    {
        final STATE entry = this.graph ().entry ();
        if ( this.state () == entry )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.machine.AbstractMachine#isAtExit()
     */
    @Override
    public final boolean isAtExit ()
    {
        final STATE exit = this.graph ().exit ();
        if ( this.state () == exit )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @return The lock used to synchronize this AbstractMachine.
     *         To be used only by derived classes for ensuring
     *         atomicity.  Never null.
     */
    protected final Serializable lock ()
    {
        return this.lock;
    }


    /**
     * @return The human-readable name of this Machine.  Never null.
     */
    public final String name ()
    {
        return this.name;
    }


    /**
     * @see musaico.foundation.Machine#state()
     */
    @Override
    public final STATE state ()
        throws ReturnNeverNull.Violation
    {
        synchronized ( this.lock )
        {
            return this.state;
        }
    }


    // Every AbstractMachine must implement state ( STATE ).


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        final StringBuilder pushed_graph = new StringBuilder ();
        final String state_string;

        synchronized ( this.lock )
        {
            state_string = "" + this.state;
        }

        return ClassName.of ( this.getClass () )
            + " " + this.name
            + " [ state = " + state_string + " ] "
            + pushed_graph.toString ();
    }


    /**
     * @see musaico.foundation.Machine#transition(Object...)
     */
    @Override
    @SuppressWarnings("unchecked") // Possible heap pollution INPUT... inputs.
    public final Transitions<INPUT, STATE, ARC> transition (
                                    INPUT... inputs
                                    )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  inputs );
        this.contracts ().check ( Parameter1.MustContainNoNulls.CONTRACT,
                                  inputs );

        Transitions<INPUT, STATE, ARC> transitions = null;
        synchronized ( this.lock () )
        {
            final STATE state = this.state ();
            final Value<Arc<STATE, ARC>> arcs =
                this.transitionArcs ( state, inputs );

            final NoTransition<INPUT, STATE, ARC> no_transition =
                new NoTransition<INPUT, STATE, ARC> ( this, state );

            if ( arcs.hasValue () )
            {
                // One or more Transitions out of the current state.
                final ValueBuilder<Transition<INPUT, STATE, ARC>> builder =
                    new ValueBuilder<Transition<INPUT, STATE, ARC>> (
                        no_transition.transitionValueClass () );
                for ( Arc<STATE, ARC> arc : arcs )
                {
                    final Transition<INPUT, STATE, ARC> transition =
                        transitionFromArc ( arc, inputs );
                    builder.add ( transition );
                }

                final Countable<Transition<INPUT, STATE, ARC>> possible_transitions =
                    builder.build ();

                transitions =
                    new Transitions<INPUT, STATE, ARC> (
                        this,         // machine
                        this.lock (), // lock
                        state,        // start_state
                        possible_transitions ); // transitions
            }
            else
            {
                // No Transitions out of the current state
                // (Error, Warning, hopefully not Blocking but you
                // never know, and so on).
                UncheckedViolation violation = null;
                if ( arcs instanceof NotOne )
                {
                    final NotOne<Arc<STATE, ARC>> failed =
                        (NotOne<Arc<STATE, ARC>>) arcs;
                    violation = failed.valueViolation ();
                }
                else
                {
                    violation = NodesMustBeConnected.CONTRACT.violation (
                                    arcs,
                                    this.graph () );
                }

                transitions =
                    new Transitions<INPUT, STATE, ARC> (
                        this,         // machine
                        this.lock (), // lock
                        state,        // state
                        new No<Transition<INPUT, STATE, ARC>> ( // transitions
                            no_transition.transitionValueClass (), // value_class
                            violation ) );               // violation
            }
        }

        return transitions;
    }


    /**
     * <p>
     * Returns all Arcs which can be traversed, starting from the
     * specified state, given the specified inputs or triggers.
     * </p>
     *
     * <p>
     * No Arc might be returned, or One Arc, or
     * Many Arcs, and so on; or an Error or Warning, and so on.
     * </p>
     *
     * @param state The current state of this Machine.  Must not be null.
     *
     * @param inputs Zero or more trigger(s) for the transition(s).
     *               Zero triggers will typically not trigger any
     *               state transition at all, though some Machines
     *               may allow this across some arcs.  Some Machines
     *               may also require more than one input to transition
     *               out of some states.
     *               Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution INPUT... inputs.
    protected Value<Arc<STATE, ARC>> transitionArcs (
                                                     STATE state,
                                                     INPUT ... inputs
                                                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final Value<Arc<STATE, ARC>> arcs =
            this.graph ().arcs ( state );

        return arcs;
    }


    /**
     * <p>
     * Builds the Transition that would occur when this Machine receives
     * the specified input(s) / trigger(s) and traverses the specified Arc.
     * </p>
     *
     * @param arc The Arc across which the state Transition would occur.
     *            Must not be null.
     *
     * @param inputs Zero or more trigger(s) which induced the transition.
     *               Zero triggers will typically not trigger any
     *               state transition at all, though some Machines
     *               may allow this across some arcs.  Some Machines
     *               may also require more than one input to transition
     *               out of some states.
     *               Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution INPUT... inputs.
    protected Transition<INPUT, STATE, ARC> transitionFromArc (
            Arc<STATE, ARC> arc,
            INPUT ... inputs
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new ArcTransition<INPUT, STATE, ARC> (
            this,         // machine
            this.lock (), // lock
            arc,          // arc
            inputs );     // inputs
    }
}
