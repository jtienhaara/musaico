package musaico.foundation.machine;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.Arc;

import musaico.foundation.value.Countable;
import musaico.foundation.value.Immutable;
import musaico.foundation.value.Maybe;
import musaico.foundation.value.ValueClass;

import musaico.foundation.value.classes.StandardValueClass;

import musaico.foundation.value.contracts.ValueMustBeImmutable;

import musaico.foundation.value.finite.CountableWrapper;
import musaico.foundation.value.finite.No;


/**
 * <p>
 * Zero or more Transitions, which can be attempted sequentially
 * (in order, until one succeeds) by calling <code> execute () </code>.
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
public class Transitions<INPUT extends Object, STATE extends Object, ARC extends Object>
    extends CountableWrapper<Transition<INPUT, STATE, ARC>>
    implements Immutable<Transition<INPUT, STATE, ARC>>, Transition<INPUT, STATE, ARC>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Transitions.class );


    // The Machine's lock, which we close while attempting to
    // execute () transitions.
    private final Serializable lock;

    // The Machine being transitioned.
    private final AbstractMachine<INPUT, STATE, ARC> machine;

    // The state of the Machine before any transition(s) have been executed.
    private final STATE startState;

    // The Transition(s) (if any) which are included in this bundle.
    private final Countable<Transition<INPUT, STATE, ARC>> transitions;


    /**
     * <p>
     * Creates a new Transitions bundle.
     * </p>
     *
     * @param machine The AbstractMachine which will be transitioned.
     *                Must not be null.
     *
     * @param lock The Machine's lock, which is locked for the duration
     *             of every attempt to <code> execute () </code>
     *             these Transitions.  Must not be null.
     *
     * @param start_state The state of the Machine when this bundle of
     *                    Transitions was created.  No Transition from this
     *                    bundle can be executed if the Machine is no longer
     *                    in this state.  Must be Immutable.
     *                    Must not be null.
     *
     * @param transitions Zero or more Transition(s).  Must not be null.
     */
    public Transitions (
                        AbstractMachine<INPUT, STATE, ARC> machine,
                        Serializable lock,
                        STATE start_state,
                        Countable<Transition<INPUT, STATE, ARC>> transitions
                        )
        throws ParametersMustNotBeNull.Violation,
               ValueMustBeImmutable.Violation
    {
        super ( transitions );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               machine, lock, start_state, transitions );
        classContracts.check ( ValueMustBeImmutable.CONTRACT,
                               transitions );

        this.machine = machine;
        this.lock = lock;
        this.startState = start_state;
        this.transitions = transitions;
    }


    /**
     * @see musaico.foundation.machine.Transition#execute()
     */
    @Override
    public Maybe<STATE> execute ()
        throws ReturnNeverNull.Violation
    {
        Maybe<STATE> new_state =
            new No<STATE> ( this.machine.graph ().nodeValueClass () );

        synchronized ( this.lock )
        {
            for ( Transition<INPUT, STATE, ARC> transition : this )
            {
                new_state = transition.execute ();
                if ( new_state.hasValue () )
                {
                    // Success.
                    this.machine.forceState ( new_state.orThrowUnchecked () );
                    return new_state;
                }
            }
        }

        // Failure.
        return new_state;
    }


    /**
     * @return The Machine's lock, which is locked for the duration
     *         of every attempt to <code> execute () </code>
     *         these Transitions.  Never null.
     */
    protected final Serializable lock ()
    {
        return this.lock;
    }


    /**
     * @see musaico.foundation.machine.Transition#machine()
     */
    @Override
    public final AbstractMachine<INPUT, STATE, ARC> machine ()
        throws ReturnNeverNull.Violation
    {
        return this.machine;
    }


    /**
     * @see musaico.foundation.machine.Transition#startState()
     */
    @Override
    public final STATE startState ()
        throws ReturnNeverNull.Violation
    {
        return this.startState;
    }


    /**
     * @see musaico.foundation.machine.Transition#transitionValueClass()
     */
    @Override
    public ValueClass<Transition<INPUT, STATE, ARC>> transitionValueClass ()
        throws ReturnNeverNull.Violation
    {
        return new StandardValueClass<Transition<INPUT, STATE, ARC>> (
            Transition.class,
            new NoTransition<INPUT, STATE, ARC> (
                this.machine,                                        // machine
                this.machine.graph ().nodeValueClass ().none () ) ); // state
    }
}
