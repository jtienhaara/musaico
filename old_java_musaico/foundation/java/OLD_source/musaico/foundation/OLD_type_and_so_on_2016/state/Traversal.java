package musaico.foundation.state;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Countable;
import musaico.foundation.value.ValueClass;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.classes.StandardValueClass;


/**
 * <p>
 * A Transition is an arc from one State to another.
 * </p>
 *
 * <p>
 * A Transition takes zero or more tape machines, each one a TapeMachine
 * which corresponds to a specific Tape (input, output, and so on),
 * and determines whether or not the specified tape machines induce a
 * state change.
 * </p>
 *
 * <p>
 * Often a Transition will read from the Tape.INPUT machine and/or write
 * to the Tape.OUTPUT machine.  Other tape machines may be required,
 * as well.
 * </p>
 *
 *
 * <p>
 * In Java every Transition must be Serializable in order to
 * play nicely across RMI.
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
public interface Transition
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Transition automagically across the arc. */
    public static final AutomaticTransition AUTOMATIC =
        new AutomaticTransition ();

    /** Never transition across the arc. */
    public static final ImpossibleTransition IMPOSSIBLE =
        new ImpossibleTransition ();


    /** ValueClass of Transitions, defining Value&lt;Transition&gt;
     *  contract, none Transition (IMPOSSIBLE), and so on. */
    public static final ValueClass<Transition> VALUE_CLASS =
        new StandardValueClass<Transition> ( // arc_value_class
            Transition.class,          // element_class
            Transition.IMPOSSIBLE ); // none


    /**
     * <p>
     * Returns true if the specified tape might be used by this
     * Transition (that is, it is included in the Tapes
     * returned by the <code> tapes () </code> method, and must
     * therefore be passed, in order, to the <code> transition () </code>
     * method).
     * </p>
     *
     * @param tape The tape which might or might not be used by
     *             this Transition.  For example, Tape.INPUT
     *             or Tape.OUTPUT, and so on.  Must not be null.
     *
     * @return True if this Transition might read, write, and/or
     *         induce state changes on the TapeMachine corresponding
     *         to the specified tape; false if it ignores
     *         the specified tape.
     */
    public abstract boolean isTapeRequired (
                                            Tape tape
                                            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * @return The tape(s) used by this Transition, if any, during
     *         calls to the <code> transition ( ... ) </code> method.
     *         For example, Tape.INPUT or Tape.OUTPUT,
     *         or both, or some other tape(s).  And so on.
     *         For each Tape returned, a corresponding TapeMachine must be
     *         passed to the <code> transition ( ... ) </code>
     *         method, in the same order as the Tapes they
     *         correspond to.  The Tapes for a given Transition
     *         never change; they are Immutable.  Never null.
     */
    public abstract Countable<Tape> tapes ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Attempts to transition States, induced by the specified tapes.
     * </p>
     *
     * @param tape_machines The TapeMachines corresponding to the
     *                      <code> tapes () </code> required as inputs into
     *                      this Transition, in order.  This arc might
     *                      induce transitions in the tape TapeMachines,
     *                      in order to get the next input value and so on,
     *                      and may also build up and TapeMachines with
     *                      StateGraphBuilders for graphs, for example to
     *                      add output.  Must not be null.  Must not contain
     *                      any null elements.
     *
     * @return True if the transition was successful, false if not.
     */
    public abstract boolean transition (
                                        TapeMachine... tape_machines
                                        )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;
}
