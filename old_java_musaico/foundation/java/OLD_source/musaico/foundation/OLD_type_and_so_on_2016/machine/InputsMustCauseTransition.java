package musaico.foundation.machine;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Maybe;

import musaico.foundation.value.finite.One;


/**
 * <p>
 * A guarantee that each input leads to the successful traversal
 * of at least one arc out of a specific state in a specific graph.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
 * nicely over RMI.
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
public class InputsMustCauseTransition<INPUT extends Object, STATE extends Object, ARC extends Object>
    implements Contract<INPUT[], InputsMustCauseTransition.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            InputsMustCauseTransition.serialVersionUID;


        /**
         * <p>
         * Creates a new InputsMustCauseTransition.Violation with the specified
         * details.
         * </p>
         *
         * @param contract The violated contract.  Must not be null.
         *
         * @param plaintiff The object whose contract was
         *                  violated.  Must not be null.
         *
         * @param evidence The input which violated the contract.
         *                 Must not be null.
         */
        public <VIOLATING_INPUT extends Object>
            Violation (
                       Contract<VIOLATING_INPUT[], InputsMustCauseTransition.Violation> contract,
                       Object plaintiff,
                       VIOLATING_INPUT [] evidence
                       )
        {
            super ( contract,
                    "The input did not induce"
                    + " a state transition.", // description
                    plaintiff,
                    evidence );
        }
    }




    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( InputsMustCauseTransition.class );


    // The state machine, in which every input must induce a state transition.
    private final Machine<INPUT, STATE, ARC> machine;

    // The initial state of the state machine.
    private final STATE initialState;


    /**
     * <p>
     * Creates a new InputsMustCauseTransition contract, ensuring that every
     * input leads to a state transition from the machine's
     * current state.
     * </p>
     *
     * @param machine The state machine, a duplicate of which will be
     *                transitioned for each input.  Must not be null.
     */
    public InputsMustCauseTransition (
            Machine<INPUT, STATE, ARC> machine
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( machine,
               machine == null
                   ? null
                   : machine.state () );
    }


    /**
     * <p>
     * Creates a new InputsMustCauseTransition contract, ensuring that every
     * input leads to a state transition from the specified state.
     * </p>
     *
     * @param machine The state machine, a duplicate of which will be
     *                transitioned for each input.  Must not be null.
     *
     * @param initial_state The state from which the machine will start
     *                      before each input.  Must not be null.
     */
    public InputsMustCauseTransition (
            Machine<INPUT, STATE, ARC> machine,
            STATE initial_state
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               machine, initial_state );

        this.machine = machine;
        this.initialState = initial_state;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        return "Each input to machine "
            + this.machine
            + " must induce a transition from state "
            + this.initialState
            + ".";
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null )
        {
            return false;
        }
        else if ( obj.getClass () != this.getClass () )
        {
            return false;
        }

        final InputsMustCauseTransition<?, ?, ?> that =
            (InputsMustCauseTransition<?, ?, ?>) obj;
        if ( this.machine == null )
        {
            if ( that.machine == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.machine == null )
        {
            return false;
        }
        else if ( ! this.machine.equals ( that.machine ) )
        {
            return false;
        }

        if ( this.initialState == null )
        {
            if ( that.initialState == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.initialState == null )
        {
            return false;
        }
        else if ( ! this.initialState.equals ( that.initialState ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array creation INPUT [].
    public FilterState filter (
                               INPUT [] inputs
                               )
    {
        if ( inputs == null )
        {
            return FilterState.DISCARDED;
        }

        final Machine<INPUT, STATE, ARC> transition_from =
            this.machine.state ( this.initialState );
        final Transitions<INPUT, STATE, ARC> transitions =
            transition_from.transition ( inputs );
        if ( ! transitions.hasValue () )
        {
            // No Transitions can possibly induce a new state.
            return FilterState.DISCARDED;
        }

        final Maybe<STATE> state_change =
            transitions.execute ();
        if ( ! state_change.hasValue () )
        {
            // No Transitions actually induced a new state.
            return FilterState.DISCARDED;
        }
        else
        {
            return FilterState.KEPT;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.machine.hashCode () * 17
            + this.initialState.hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public InputsMustCauseTransition.Violation violation (
            Object plaintiff,
            INPUT [] evidence
            )
    {
        return new InputsMustCauseTransition.Violation ( this,
                                                         plaintiff,
                                                         evidence );
    }


    /**
     * <p>
     * Helper method.  Always passes this InputsMustCauseTransition contract
     * as the first parameter to the full method, and sets the specified
     * initial cause (if any).
     * </p>
     *
     * @see musaico.foundation.machine.InputsMustCauseTransition#violation(musaico.foundation.contract.Contract, java.lang.Object...)
     */
    @SuppressWarnings("unchecked") // Possible heap pollution INPUT...
    public InputsMustCauseTransition.Violation violation (
            Object plaintiff,
            Throwable cause,
            INPUT ... evidence
            )
    {
        final InputsMustCauseTransition.Violation violation =
            this.violation ( plaintiff,
                             evidence );

        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }


    /**
     * <p>
     * Helper method.  Always passes this InputsMustCauseTransition contract
     * as the first parameter to the full method, and sets the specified
     * initial cause (if any).
     * </p>
     *
     * @see musaico.foundation.machine.InputsMustCauseTransition#violation(musaico.foundation.contract.Contract, java.lang.Object...)
     */
    public InputsMustCauseTransition.Violation violation (
            Object plaintiff,
            INPUT [] evidence,
            Throwable cause
            )
    {
        final InputsMustCauseTransition.Violation violation =
            this.violation ( plaintiff,
                             cause,
                             evidence );

        return violation;
    }
}
