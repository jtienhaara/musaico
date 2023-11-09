package musaico.foundation.machine;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.Arc;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.ValueClass;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.classes.StandardValueClass;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;


/**
 * <p>
 * A Transition over a specific Arc from one state to the next, for
 * a particular Machine that is currently in the starting state of the Arc.
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
public class ArcTransition<INPUT extends Object, STATE extends Object, ARC extends Object>
    implements Transition<INPUT, STATE, ARC>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( ArcTransition.class );


    // The Machine being transitioned.
    private final AbstractMachine<INPUT, STATE, ARC> machine;

    // The Machine's lock, which is locked for the duration
    // of every attempt to execute () this Transition.
    private final Serializable lock;

    // The arc describing the start and end states of this Transition.
    private final Arc<STATE, ARC> arc;

    // The input(s) which might induce this Transition.
    private final INPUT [] inputs;


    /**
     * <p>
     * Creates a new ArcTransition.
     * </p>
     *
     * @param machine The AbstractMachine which will be transitioned.
     *                Must not be null.
     *
     * @param lock The Machine's lock, which is locked for the duration
     *             of every attempt to <code> execute () </code>
     *             this Transition.  Must not be null.
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
    public ArcTransition (
                          AbstractMachine<INPUT, STATE, ARC> machine,
                          Serializable lock,
                          Arc<STATE, ARC> arc,
                          INPUT ... inputs
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter4.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               machine, lock, arc, inputs );
        classContracts.check ( Parameter4.MustContainNoNulls.CONTRACT,
                               inputs );

        this.machine = machine;
        this.lock = lock;
        this.arc = arc;
        this.inputs = inputs;
    }


    /**
     * @see musaico.foundation.machine.Transition#execute()
     */
    @Override
    public final Maybe<STATE> execute ()
        throws ReturnNeverNull.Violation
    {
        final Maybe<STATE> new_state;
        synchronized ( this.lock )
        {
            final STATE machine_state = this.machine.state ();
            final STATE required_state = this.arc.from ();

            if ( ! machine_state.equals ( required_state ) )
            {
                // No longer in the right state for this Transition.
                // Fail.
                new_state =
                    new No<STATE> ( this.machine.graph ().nodeValueClass () );
            }
            else
            {
                // In the right starting state.
                // By default traverse the arc; but derived classes
                // can override executeArc () to perform extra checks
                // and side-effects during the traversal.
                new_state = this.executeArc ();

                if ( new_state.hasValue () )
                {
                    this.machine.forceState ( new_state.orThrowUnchecked () );
                }
            }
        }

        return new_state;
    }


    /**
     * <p>
     * Execute any checks and side-effects in order to traverse a specific
     * Arc in the Machine's Graph, while transitioning from one state
     * to the next.
     * </p>
     *
     * <p>
     * By default this method simply returns the end state of the Arc,
     * with no additional checks or side-effects.  Derived classes may
     * override this method to alter the default traversal behaviour.
     * </p>
     *
     * @return One new state of the Machine, on success;
     *         or No state or an Error state and so on, on failure;
     *         or possibly a Blocking state transition; and so on.
     *         Never null.
     */
    protected ZeroOrOne<STATE> executeArc ()
    {
        final ZeroOrOne<STATE> new_state =
            new One<STATE> ( this.machine.graph ().nodeValueClass (),
                             this.arc.to () );

        return new_state;
    }


    /**
     * @return The Machine's lock, which is locked for the duration
     *         of every attempt to <code> execute () </code>
     *         this Transition.  Never null.
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
        return this.arc.from ();
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
