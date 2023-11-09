package musaico.foundation.state;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;

import musaico.foundation.machine.Machine;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.finite.One;


/**
 * <p>
 * Tests some typing state machines, to make sure StateMachines
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
public class TestTransducer
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
        final Graph<Value<?>, Transition> add_signature_graph =
            new StateGraphBuilder ( "add_signature" )
            .fromEntry ()
                .choose ( "input" )
                .on ( new InstanceOfClass ( Number.class ) )
                .to ( "x" )
            .from ( "x" )
                .choose ( "input" )
                .on ( new InstanceOfClass ( Number.class ) )
                .to ( "x" )
            .from ( "x" )
                .toExit ()
            .build ();
        final StateMachine add_signature =
            new StateMachine ( add_signature_graph );

        final Graph<Value<?>, Transition> add_body_graph =
            new StateGraphBuilder ( "add_body" )
            .fromEntry ()
                .on ( new Transition ()
                          {
                              !!!
                          } )
                    .toExit ()
            .build ();
        final StateMachine add = new StateMachine ( add_graph );

        final Graph<Value<?>, Transition> add_graph =
            new StateGraphBuilder ( "add" )
            .fromEntry ()
                .to ( "x" )
            .from ( "x" )
                .on ( new InstanceOfClass ( Number.class ) )
                    .to ( "x" )
                .toExit ()
            .build ();
        final StateMachine add = new StateMachine ( add_graph );

        final Graph<Value<?>, Transition> input_graph =
            new StateGraphBuilder ( "numbers_to_add" )
            .fromEntry ()
                .to ( 1 )
            .from ( 1 )
                .to ( 2 )
            .from ( 2 )
                .to ( 3 )
            .from ( 3 )
                .toExit ()
            .build ();
        final StateMachine input = new StateMachine ( input_graph );

        // Output: StateMachine that adds to a GraphBuilder
        // with every transition.
        final StateMachineBuilder output =
            new StateMachineBuilder ( "add ( x, ... )" );

        final Graph<Value<?>, Transition> program_graph =
            new StateGraphBuilder ( "program12345" )
            .fromEntry ()
                .on ( "input" )
                    .to ( input )
                .on ( "logic" )
                    .to ( add )
                .on ( "output" )
                    .to ( output )
                // !!! .on ( "world" ).to ( world ) // registers, memory, etc.
                .toExit ();
            .from ( input )
                .toEntry ()
            .from ( add )
                .toEntry ()
            // !!! .from ( output )
            // !!!     .toEntry ()
            // !!! .from ( world )
            // !!!     .toEntry ()
            .build ();
        final StateMachine program12345 = new StateMachine ( program_graph );

        final Graph<Value<?>, Transition> stack_graph =
            new StateGraphBuilder ( "stack" )
            .fromEntry ()
                .to ( program12345 )
                .toExit ()
            .from ( program12345 )
                .toExit ()
            .build ();
        final StateMachine stack = new StateMachine ( stack_graph );

        final Graph<Value<?>, Transition> processors_graph =
            new StateGraphBuilder ( "processors" )
            .fromEntry ()
                .to ( "processor1" )
            .from ( "processor1" )
                .on ( new Execute!!!InstanceOfClass ( Number.class ) ).to ( "x" )
                .toExit ()
            .build ();
        final StateMachine add = new StateMachine ( add_graph );




        System.out.println ( "Root Kind graph:\n" + root_kind );

        final StateMachine machine =
            new StateMachine ( root_kind );
        final StateMachine input =
                new StateMachine (
                    new StateGraphBuilder ( "inputgraph" )
                        .to ( "constructor" )
                        .from ( "constructor" )
                            .toExit ()
                    .build ()
                );
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

        System.out.print ( "" + state.orNull () );
        Maybe<Value<?>> maybe_state;
        boolean is_passed_through_constructors_state = false;
        while ( ( maybe_state =
                  machine.transition ( input ) )
                instanceof ZeroOrOne )
        {
            state = maybe_state.orNull ();
            final String maybe_input;
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
                    "{"
                    + input.state ().orNull ()
                    + "}";
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

                if ( "constructors".equals ( state.orNull () ) )
                {
                    is_passed_through_constructors_state = true;
                }
            }

            input_state = input.state ();
        }
        System.out.println ( "" );

        System.out.println ( "End state = " + state );

        if ( ! is_passed_through_constructors_state )
        {
            throw new IllegalStateException ( "Should have passed through the 'constructors' state, but never did" );
        }

        if ( state != root_kind.exit () )
        {
            throw new IllegalStateException ( "Should have ended"
                                              +" at the exit state "
                                              + root_kind.exit () );
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
    }
}
