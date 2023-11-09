package musaico.foundation.machine;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.Arc;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.ValueClass;

import musaico.foundation.value.classes.StandardValueClass;

import musaico.foundation.value.finite.No;


/**
 * <p>
 * No Transition at all.
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
public class NoTransition<INPUT extends Object, STATE extends Object, ARC extends Object>
    implements Transition<INPUT, STATE, ARC>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( NoTransition.class );


    // The Machine being transitioned.
    private final Machine<INPUT, STATE, ARC> machine;

    // The state of the Machine before any transition(s) have been executed.
    private final STATE startState;


    /**
     * <p>
     * Creates a new NoTransition.
     * </p>
     *
     * @param machine The Machine which will be transitioned.
     *                Must not be null.
     *
     * @param start_state The state of the Machine when this bundle of
     *                    Transitions was created.  Must not be null.
     */
    public NoTransition (
                         Machine<INPUT, STATE, ARC> machine,
                         STATE start_state
                         )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               machine, start_state );

        this.machine = machine;
        this.startState = start_state;
    }


    /**
     * @see musaico.foundation.machine.Transition#execute()
     */
    @Override
    public final Maybe<STATE> execute ()
        throws ReturnNeverNull.Violation
    {
        Maybe<STATE> new_state =
            new No<STATE> ( this.machine.graph ().nodeValueClass () );
        return new_state;
    }


    /**
     * @see musaico.foundation.machine.Transition#machine()
     */
    @Override
    public final Machine<INPUT, STATE, ARC> machine ()
        throws ReturnNeverNull.Violation
    {
        return this.machine;
    }


    /**
     * @see musaico.foundation.machine.Transition#startState()
     */
    @Override
    public final STATE startState ()
    {
        return this.startState;
    }


    /**
     * @see musaico.foundation.machine.Transition#transitionValueClass()
     */
    @Override
    public final ValueClass<Transition<INPUT, STATE, ARC>> transitionValueClass ()
        throws ReturnNeverNull.Violation
    {
        return new StandardValueClass<Transition<INPUT, STATE, ARC>> (
            Transition.class,
            this ); // none
    }
}
