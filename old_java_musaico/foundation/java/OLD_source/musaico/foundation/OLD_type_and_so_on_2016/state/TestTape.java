package musaico.foundation.state;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.string.NotEmptyString;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;

import musaico.foundation.machine.Machine;

import musaico.foundation.value.Countable;
import musaico.foundation.value.Maybe;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueClass;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.classes.StandardValueClass;

import musaico.foundation.value.finite.One;


/**
 * <p>
 * Tests some typing state machines, to make sure TapeMachines
 * and Transitions and so on can be abused to what we need them
 * to do for type systems.
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
public class TestTape
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;



    public static void main (
                             String [] args
                             )
    {
        final ValueClass<String> string_value_class =
            new StandardValueClass<String> (
                String.class, // element_class
                "" );         // none

        final Graph<Value<?>, Transition> root_kind =
            new StateGraphBuilder ( "kind" )
            .to ( "symbol_table" )
            .from ( "symbol_table" )
                .on ( "constraint" ).to ( "constraints" )
                .on ( "constructor" ).to ( "constructors" )
            .from ( "constraints" )
                .toExit ()
            .from ( "constructors" )
                .on ( "newby" ).to ( "newby" )
                .toExit ()
            .from ( "newby" )
                .on ( NotEmptyString.DOMAIN )
                    .write ( "Hello, " )
                    .copy ( Tape.INPUT, Tape.OUTPUT )
                    .toExit ()
                .toExit ()
            .build ();

        System.out.println ( "Root Kind graph:\n"
                             + root_kind.toStringDetails () );

        final TapeMachine machine =
            new TapeMachine ( root_kind,
                              true, // is_automatically_transition_input
                              true ); // is_debuggable
        final TapeMachine input =
                new TapeMachine (
                    new StateGraphBuilder ( "input" )
                        .fromEntry ()
                            .to ( "constructor" )
                        .from ( "constructor" )
                            .toExit ()
                    .build (),
                    false, // is_automatically_transition_input
                    false ); // is_debuggable
        final TapeMachineBuilder output =
            new TapeMachineBuilder ( "output",
                                     false, // is_automatically_transition_input
                                     false ); // is_debuggable
        System.out.println ( "Required tapes = " + machine.tapes () );
        final TapeMachine [] tape_machines =
            new TapeMachine [] { input,
                                 output };
        Value<?> input_state = input.state ();
        final Value<?> start_input = input_state;
        System.out.println ( "Start input = "
                             + input_state );
        if ( input_state != input.graph ().entry () )
        {
            throw new IllegalStateException ( "Start input should be the entry node" );
        }

        Value<?> state = machine.state ();
        if ( ! "kind".equals ( state.orNull () ) )
        {
            throw new IllegalStateException ( "Start state should be One(kind)" );
        }

        // Do the real work:
        final List<String> passed_through_states =
            TestTape.feed ( machine, tape_machines );

        final boolean is_passed_through_constructors_state =
            passed_through_states.contains ( "constructors" );

        state = machine.state ();

        if ( ! is_passed_through_constructors_state )
        {
            throw new IllegalStateException ( "Should have passed through the 'constructors' state, but never did" );
        }

        if ( state != machine.graph ().exit () )
        {
            throw new IllegalStateException ( "Should have ended"
                                              +" at the exit state "
                                              + machine.graph ().exit () );
        }

        final Value<?> end_input = input.state ();
        System.out.println ( "End input = "
                             + end_input.orNull () );
        if ( end_input == start_input )
        {
            throw new IllegalStateException ( "No input was used!"
                                              + "  The Transitions should"
                                              + " have used up input(s)." );
        }
        if ( end_input != input.graph ().exit () )
        {
            throw new IllegalStateException ( "There should not be any more"
                                              + " input: " + end_input
                                              + " Should have ended with"
                                              + " input in state "
                                              + input.graph ().exit () );
        }

        System.out.println ( "SUCCESS" );


        // =========================================================
        System.out.println ( "" );
        System.out.println ( "" );
        final TapeMachine input2 =
            new TapeMachine (
                new StateGraphBuilder ( "input" )
                    .fromEntry ()
                        .to ( "constructor" )
                    .from ( "constructor" )
                        .to ( "newby" )
                    .from ( "newby" )
                        .to ( "constructor_parameter1" )
                    .from ( "constructor_parameter1" )
                        .toExit ()
                    .build (),
                false, // is_automatically_transition_input
                false ); // is_debuggable

        // Reset the machine after the last test.
        machine.forceState ( machine.graph ().entry () );

        final TapeMachineBuilder output2 =
            new TapeMachineBuilder ( "output",
                                     false, // is_automatically_transition_input
                                     false ); // is_debuggable
        final List<String> states2 =
            TestTape.feed ( machine,
                            new TapeMachine []
                            {
                                input2,
                                output2
                            } );

        final boolean is_passed_through_constructors_state2 =
            states2.contains ( "constructors" );
        final boolean is_passed_through_newby_state2 =
            states2.contains ( "newby" );

        state = machine.state ();

        if ( ! is_passed_through_constructors_state2 )
        {
            throw new IllegalStateException ( "Should have passed through the 'constructors' state, but never did" );
        }
        else if ( ! is_passed_through_newby_state2 )
        {
            throw new IllegalStateException ( "Should have passed through the 'newby' state, but never did" );
        }

        if ( state != machine.graph ().exit () )
        {
            throw new IllegalStateException ( "Should have ended"
                                              +" at the exit state "
                                              + machine.graph ().exit () );
        }

        final Value<?> end_input2 = input2.state ();
        System.out.println ( "End input = "
                             + end_input2.orNull () );
        if ( end_input2 != input2.graph ().exit () )
        {
            throw new IllegalStateException ( "There should not be any more"
                                              + " input: " + end_input2
                                              + " Should have ended with"
                                              + " input in state "
                                              + input2.graph ().exit () );
        }

        final Graph<Value<?>, Transition> output_graph2 =
            output2.build ().graph ();
        final LinkedHashSet<Value<?>> missing_output_states2 =
            new LinkedHashSet<Value<?>> ();
        missing_output_states2.add ( new One<String> ( string_value_class,
                                                       "Hello, " ) );
        missing_output_states2.add ( new One<String> ( string_value_class,
                                                       "constructor_parameter1" ) );
        for ( Value<?> output_state : output_graph2.nodes () )
        {
            missing_output_states2.remove ( output_state );
        }

        if ( missing_output_states2.size () > 0 )
        {
            throw new IllegalStateException ( "Failed to write / copy"
                                              + " output nodes: "
                                              + missing_output_states2 );
        }

        System.out.println ( "SUCCESS" );
    }


    private static List<String> feed (
                                      TapeMachine machine,
                                      TapeMachine [] tape_machines
                                      )
    {
        TapeMachine input = null;
        TapeMachine output = null;
        Value<?> input_state = null;
        int t = 0;
        for ( Tape tape : machine.tapes () )
        {
            if ( tape == Tape.INPUT )
            {
                input = tape_machines [ t ];
                input_state = input.state ();
            }
            else if ( tape == Tape.OUTPUT )
            {
                output = tape_machines [ t ];
            }

            t ++;
        }

        final List<String> state_sequence =
            new ArrayList<String> ();
        Value<?> state = machine.state ();
        state_sequence.add ( "" + state.orNull () );

        long elapsed_ns = 0L;
        long stopwatch_start = System.nanoTime ();

        System.out.print ( "" + state.orNull () );
        Maybe<Value<?>> maybe_state;
        while ( ( maybe_state =
                  machine.transition ( tape_machines ) )
                instanceof ZeroOrOne )
        {
            final long stopwatch_end = System.nanoTime ();
            final long step_ns = stopwatch_end - stopwatch_start;
            elapsed_ns += step_ns;

            state = maybe_state.orNull ();
            state_sequence.add ( "" + state.orNull () );

            final String maybe_input;
            if ( input == null )
            {
                // No input tape machine.
                maybe_input = "";
            }
            else
            {
                if ( input.state () == input_state )
                {
                    // No input was used up.
                    maybe_input = "";
                }
                else if ( input.state ().orNull () == null )
                {
                    maybe_input = "EOF";
                }
                else
                {
                    maybe_input =
                        "("
                        + input.state ().orNull ()
                        + ")";
                }
            }

            if ( state == null )
            {
                // ----> or --{input}-->
                System.out.print ( " --" + maybe_input + "--> " + state );
            }
            else if ( state.orNull () == null )
            {
                // --> or -{input}->
                System.out.print ( " --" + maybe_input + "--> EOF" );
            }
            else
            {
                // --> or -{input}->
                System.out.print ( " --" + maybe_input + "--> "
                                   + state.orNull () );
            }

            if ( input != null )
            {
                input_state = input.state ();
            }

            stopwatch_start = System.nanoTime ();
        }
        final long stopwatch_end = System.nanoTime ();
        System.out.println ( "" );


        final double elapsed_ms = ( (double) elapsed_ns ) / 1000000.0D;
        System.out.println ( "Elapsed time: " + elapsed_ms + " ms" );

        System.out.println ( "End state = " + state );

        if ( state != machine.graph ().exit () )
        {
            System.out.println ( "Last attempted transition resulted in "
                                 + maybe_state );
        }

        if ( output != null )
        {
            System.out.println ( "" );
            System.out.println ( "Output machine:\n" + output
                                 + "\n    Graph:\n" + output.graph ().toStringDetails () );
        }

        return state_sequence;
    }
}
