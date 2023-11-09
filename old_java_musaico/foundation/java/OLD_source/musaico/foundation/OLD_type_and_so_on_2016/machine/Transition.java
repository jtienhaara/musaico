package musaico.foundation.machine;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.graph.Arc;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.ValueClass;


/**
 * <p>
 * A command which will transition states in a Machine, attempting
 * the state transition by traversing a specific Arc in the
 * Machine's Graph.
 * </p>
 *
 *
 * <p>
 * In Java, every Transition must be Serializable in order to
 * play nicely over RMI.
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
 * @see musaico.foundation.machine.MODULE#COPYRIGHT
 * @see musaico.foundation.machine.MODULE#LICENSE
 */
public interface Transition<INPUT extends Object, STATE extends Object, ARC extends Object>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Attempts to execute this transition, including any and all
     * filters and operations contained in the Arc being traversed.
     * </p>
     *
     * <p>
     * It is crucial that every Transition is performed atomically,
     * so that, for example, multiple Transitions attempted concurrently
     * do not result in a bad state of the Machine and/or the inputs.
     * Typically a Machine will hold a Serializable lock object, and
     * pass it into the constructor of a Transition.  When the Transition
     * is <code> execute () </code>, it performs all work inside a
     * critical section synchronized on the Machine's lock.
     * </p>
     *
     * @return One new state of the Machine, on success;
     *         or No state or an Error state and so on, on failure;
     *         or possibly a Blocking state transition; and so on.
     *         Never null.
     */
    public abstract Maybe<STATE> execute ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The Machine which will potentially change states
     *         when this Transition is <code> execute </code>d.
     *         Never null.
     */
    public abstract Machine<INPUT, STATE, ARC> machine ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The state from which this Transition occurs.
     *         If the Machine being transitioned is not in the same
     *         state when <code> execute () </code> is called, then
     *         this Transition will fail.  Never null.
     */
    public abstract STATE startState ();


    /**
     * @return The ValueClass of this Transition, such as a
     *         <code> ValueClass<Transition<String, ParseState, LexicalArc>> </code>,
     *         or a <code> ValueClass<Transition<Number, Number, Operation>> </code>,
     *         or a <code> ValueClass<Transition<UIEvent, WidgetState, Trigger>> </code>,
     *         and so on.  Never null.
     */
    public abstract ValueClass<Transition<INPUT, STATE, ARC>> transitionValueClass ();
}
