package musaico.foundation.machine;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;
import musaico.foundation.graph.NodeMustBeInGraph;

import musaico.foundation.value.Countable;
import musaico.foundation.value.ValueClass;


/**
 * <p>
 * A state machine which can traverse the arcs of a Graph,
 * maintaining some kind of state aong the way.
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
public interface Machine<INPUT extends Object, STATE extends Object, ARC extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The Graph of nodes and arcs which describes the
     *         states and transitions available to this Machine.
     *         Note that a MutableMachine might have a graph that
     *         change over time (such as a StackMachine); and all
     *         Machines can have MutableGraphs which change their
     *         internals over time.  Never null.
     */
    public abstract Graph<STATE, ARC> graph ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The ValueClass of Graph underlying this machine, such as
     *         a StandardValueClass&lt;Graph&lt;STATE, ARC&gt;&gt;, or some
     *         more specific ValueClass, such as ValueClass&lt;LoveMachine&gt;.
     *         Never changes (unlike the graph, which can change over time).
     *         Never null.
     */
    public abstract ValueClass<Graph<STATE, ARC>> graphValueClass ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The ValueClass of the inputs which cause transitions for
     *         this state machine, such as a
     *         <code> StandardValueClass<String> <code> for a machine
     *         triggered by text inputs, and so on.  Never null.
     */
    public abstract ValueClass<INPUT> inputValueClass ()
        throws ReturnNeverNull.Violation;


    /**
     * @return True if this Machine is currently in the "entry" state
     *         of its base Graph
     *         (<code> this.state () == this.graph ().entry () </code>);
     *         false if not.
     */
    public abstract boolean isAtEntry ();


    /**
     * @return True if this Machine is currently in the "exit" state
     *         of its base Graph
     *         (<code> this.state () == this.graph ().exit () </code>);
     *         false if not.
     */
    public abstract boolean isAtExit ();


    /**
     * @return The current One state of this Machine.
     *         Always a state in the <code> graph () </code>
     *         Graph, which can change over time if this is a
     *         MutableMachine (for example, a StackMachine which
     *         has Machines with different graphs being pushed
     *         onto and popped off the stack over time).
     *         Never null.
     */
    public abstract STATE state ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new Machine starting in the specified state.
     * </p>
     *
     * @param state The initial state for the new machine.
     *              Must not be null.
     *
     * @return A new Machine, starting in the specified state.
     *         If this is an ImmutableMachine then the Graph of
     *         the new machine will be the <code> graph () </code>
     *         of this machine, and the specified state must belong
     *         to that Graph.  If this is a MutableMachine, then
     *         the Graph of the new machine depends on the implementation.
     *         For example, the Graph of a new machine copied from
     *         a StackMachine is the bottom-most graph of the stack
     *         of this machine (not the current / top graph).
     *         Never null.
     */
    public abstract Machine<INPUT, STATE, ARC> state (
                                                      STATE state
                                                      )
        throws ParametersMustNotBeNull.Violation,
               NodeMustBeInGraph.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a possible Transition out of this machine's current state, into
     * a new state, given the specified input(s).
     * </p>
     *
     * <p>
     * The Transition(s) returned, if any, must be executed by the caller
     * in order to actually apply and cause this machine's state to change.
     * </p>
     *
     * <p>
     * If multiple Transitions are returned, then it is up to the caller
     * to decide whether to attempt each one in sequence, or all at
     * once, and so on.  When attempting the returned Transitions
     * concurrently, the first one that induces a state change will
     * cause all the others of the family to stop processing and fail,
     * rolling back any intermediate results.
     * </p>
     *
     * <p>
     * A successfully executed Transition can always be rolled back,
     * in order to backtrack, or abort a parent Transition, and so on.
     * </p>
     *
     * <p>
     * This method will return no Transitions if the specified
     * input(s) cannot possibly cause any Transitions out of the
     * Machine's current state.
     * </p>
     *
     * @param inputs Zero or more trigger(s) for the transition(s).
     *               Zero triggers will typically not trigger any
     *               state transition at all, though some Machines
     *               may allow this across some arcs.  Some Machines
     *               may also require more than one input to Transition
     *               out of some states.
     *               Must not be null.  Must not contain any null elements.
     *
     * @return The zero or more possible Transitions out of this Machine's
     *         current state.  Each individual Transition can be accessed
     *         by iterating through the result, or for convenience
     *         the overall Transitions result can step through executing
     *         individual Transitions until one succeeds, simply by calling
     *         <code> Transitions.execute () </code>.  Never null.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution INPUT... inputs.
    public abstract Transitions<INPUT, STATE, ARC> transition (
            INPUT... inputs
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;
}
