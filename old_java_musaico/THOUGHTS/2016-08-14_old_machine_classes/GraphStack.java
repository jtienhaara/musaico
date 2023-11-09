package musaico.foundation.machine;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.Graph;
import musaico.foundation.graph.NodeMustBeInGraph;

import musaico.foundation.value.Just;
import musaico.foundation.value.Maybe;
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
     *         Never null.
     */
    public abstract Graph<STATE, ARC> graph ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The ValueClass of Graph underlying this machine, such as
     *         a ValueClass&lt;Graph&lt;STATE, ARC&gt;&gt;, or some
     *         more specific ValueClass, such as ValueClass&lt;LoveMachine&gt;.
     *         Never null.
     */
    public abstract ValueClass<Graph<STATE, ARC>> graphValueClass ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The Graph of nodes and arcs which is currently top
     *         on the stack of graphs for this Machine.
     *         Never null.
     */
    public abstract Graph<STATE, ARC> graphCurrent ()
        throws ReturnNeverNull.Violation;


    /**
     * @return All Graphs of nodes and arcs on the stack.
     *         Always at least one.  Never null.
     */
    public abstract Just<Graph<STATE, ARC>> graphStack ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The number of Graphs of nodes and arcs on the stack.
     *         Always at least one.
     */
    public abstract long graphStackDepth ()
        throws Return.AlwaysGreaterThanOrEqualToOne.Violation;


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
     *         Always a state in the <code> graphCurrent () </code>,
     *         which might or might not be the base <code> graph () </code>.
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
     * @return A new Machine, starting in the specified state,
     *         with only this graph's base graph, not the rest of its stack.
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
     * Performs a transition out of this machine's current state, into
     * a new state, given the specified input(s).
     * </p>
     *
     * @param input Zero or more trigger(s) for the transition(s).
     *              Zero triggers will typically not trigger any
     *              state transition at all, though some Machines
     *              may allow this.  One input typically triggers
     *              one transition; two inputs typically trigger
     *              a sequence of two state transitions; and so on.
     *              However some machines may require multiple inputs
     *              to traverse a single arc.
     *              Must not be null.  Must not contain any null elements.
     *
     * @return The One new state of this Machine, on success;
     *         or No state or an Error state and so on, on failure;
     *         or possibly a Blocking state transition; and so on.
     *         Note that if a sequence of states is transitioned through,
     *         only the last state is returned, but the previous states
     *         might be accessible through the chain of <code> cause </code>s,
     *         from the last state transitioned to (leaf consequence)
     *         to the initial state transitioned from (root cause),
     *         if this Machine implementation tracks the state transitions.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution INPUT... inputs.
    public abstract Maybe<STATE> transition (
                                             INPUT... inputs
                                             )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;
}
