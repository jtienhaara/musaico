package musaico.foundation.state;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.EqualTo;
import musaico.foundation.domains.StringRepresentation;

import musaico.foundation.filter.Domain;
import musaico.foundation.filter.composite.Or;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;

import musaico.foundation.value.NotOne;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;


/**
 * <p>
 * A Transition which expects some particular domain of values, consuming
 * every value from a specific tape (such as the output tape)
 * that matches its expectation, and inducing a transition
 * to the next state.
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
public class Write
    extends AbstractTransition
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( Write.class );

    // The index of the machine to write to.
    private static final int INDEX_WRITE = 0;

    // The index of the Tape.DEBUG machine.
    private static final int INDEX_DEBUG = 1;


    // The Transition for the Arc to write to the tape machine.
    private final Transition transitionToWrite;

    // The destination state for the Arc to write to the tape machine.
    // If the destination state does not already exist, it will
    // be added.  The tape machine's state will be set to this.
    private final Value<?> stateToWrite;


    /**
     * <p>
     * Creates a new Write transition.
     * </p>
     *
     * @param tape The Tape to write to.  For example, Tape.OUTPUT.
     *             Must not be null.
     *
     * @param transition_to_write The Transition for the Arc
     *                            to write to the tape machine.
     *                            Must not be null.
     *
     * @param state_to_write The destination state for the Arc
     *                       to write to the tape machine.
     *                       If the destination state does
     *                       not already exist, it will be added.
     *                       The tape machine's state will be
     *                       set to this.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution DOMAIN... vararg.
    public <DOMAIN extends Object> Write (
            Tape tape,
            Transition transition_to_write,
            Value<?> state_to_write
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( tape,
                Tape.DEBUG );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               tape, transition_to_write, state_to_write );

        this.transitionToWrite = transition_to_write;
        this.stateToWrite = state_to_write;
    }


    /**
     * @see java.lang.AbstractTransition#toString()
     */
    @Override
    public String toString ()
    {
        final String maybe_transition_to_write;
        if ( this.transitionToWrite == Transition.AUTOMATIC )
        {
            // Don't bother mentioning the transition that will be written.
            maybe_transition_to_write = "";
        }
        else
        {
            maybe_transition_to_write = ", " + this.transitionToWrite;
        }

        final String state_to_write;
        if ( this.stateToWrite instanceof One )
        {
            state_to_write = StringRepresentation.of (
                                 this.stateToWrite.orNone (),
                                 0 ); // max_length
        }
        else
        {
            state_to_write = "" + this.stateToWrite;
        }

        return ClassName.of ( this.getClass () )
            + "(" + this.tapes ().orNull () // Only one Tape.
            + maybe_transition_to_write
            + ", " + state_to_write + ")";
    }


    /**
     * @see musaico.foundation.tape.Transition#transition(musaico.foundation.tape.TapeMachine...)
     */
    @Override
    public final boolean transition (
                                     TapeMachine ... tape_machines
                                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  (Object) tape_machines );
        this.contracts ().check ( Parameter1.MustContainNoNulls.CONTRACT,
                                  tape_machines );

        // We have exactly one Tape, so one tape machine.
        final TapeMachine output = tape_machines [ INDEX_WRITE ];

        if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
        {
            this.debug ( tape_machines [ INDEX_DEBUG ],
                         new Date (),
                         "  " + this + " Begin (" + output + ")" );
        }

        final Arc<Value<?>, Transition> new_arc =
            Write.write ( output,
                          this.transitionToWrite,
                          this.stateToWrite,
                          tape_machines [ INDEX_DEBUG ] );
        if ( new_arc == null )
        {
            // Couldn't write to the output tape machine.
            if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
            {
                this.debug ( tape_machines [ INDEX_DEBUG ],
                             new Date (),
                             "  " + this + " Fail" );
            }

            return false;
        }

        // Set the output tape machine's state to the node just written.
        output.forceState ( new_arc.to () );

        if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
        {
            this.debug ( tape_machines [ INDEX_DEBUG ],
                         new Date (),
                         "  " + this + " Success" );
        }

        return true;
    }


    /**
     * <p>
     * Writes the specified transition and new state to the specified
     * output tape machine.
     * </p>
     *
     * <p>
     * No checking of parmeters or return values is done.
     * </p>
     */
    protected static final Arc<Value<?>, Transition> write (
            TapeMachine output,
            Transition transition_to_write,
            Value<?> state_to_write,
            TapeMachine debug
            )
    {
        final Graph<Value<?>, Transition> graph = output.graph ();
        if ( ! ( graph instanceof StateGraphBuilder ) )
        {
            // Can't output to a non-writable graph.
            if ( debug != TapeMachine.NONE )
            {
                AbstractTransition.debugMessage ( debug,
                                                  new Date (),
                                                  "  Write.write ()"
                                                  + " Cannot write to"
                                                  + "  non-StateGraphBuilder "
                                                  + graph.getClass ()
                                                  + " " + graph.entry () );
            }

            return null;
        }

        final StateGraphBuilder output_builder = (StateGraphBuilder) graph;

        final Value<?> output_state = output.state ();

        final Arc<Value<?>, Transition> new_arc =
            new Arc<Value<?>, Transition> ( output_state,
                                            transition_to_write,
                                            state_to_write );

        if ( debug != TapeMachine.NONE )
        {
            AbstractTransition.debugMessage ( debug,
                                              new Date (),
                                              "  Write.write ()"
                                              + " New arc: " + new_arc );
        }

        try
        {
            output_builder.from ( output_state )
                .add ( new_arc );
        }
        catch ( StateGraphBuilder.MustFinishBuildingTransition.Violation violation )
        {
            // Can't do it, the output machine is in a bad state.
            if ( debug != TapeMachine.NONE )
            {
                final StringWriter sw = new StringWriter ();
                final PrintWriter pw = new PrintWriter ( sw );
                violation.printStackTrace ( pw );

                AbstractTransition.debugMessage ( debug,
                                                  new Date (),
                                                  "  Write.write () Fail:"
                                                  + "\n" + sw.toString () );
            }

            return null;
        }

        if ( debug != TapeMachine.NONE )
        {
            AbstractTransition.debugMessage ( debug,
                                              new Date (),
                                              "  Write.write () Success" );
        }

        return new_arc;
    }
}
