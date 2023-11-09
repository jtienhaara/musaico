package musaico.foundation.type;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.InstanceOfClass;
import musaico.foundation.domains.StringRepresentation;

import musaico.foundation.domains.array.ArrayObject;

import musaico.foundation.domains.string.NotEmptyString;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;

import musaico.foundation.machine.Machine;

import musaico.foundation.state.Entry;
import musaico.foundation.state.Read;
import musaico.foundation.state.StateGraphBuilder;
import musaico.foundation.state.Tape;
import musaico.foundation.state.TapeMachine;
import musaico.foundation.state.Transition;

import musaico.foundation.value.Countable;
import musaico.foundation.value.Just;
import musaico.foundation.value.Maybe;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueClass;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.builder.ValueBuilder;

import musaico.foundation.value.classes.StandardValueClass;

import musaico.foundation.value.contracts.ElementsMustBeInstancesOfClass;

import musaico.foundation.value.finite.One;


/**
 * <p>
 * Tests type building.
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
public class TestTypes
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;



    public void testKind ()
        throws Exception
    {
        System.out.println ( "" );
        System.out.println ( "Testing kinds:" );

        final Graph<Value<?>, Transition> kind =
            new TypeBuilder<Graph<Value<?>, Transition>> (
                TapeMachine.GRAPH_VALUE_CLASS, // value_class
                "kind",                        // name
                TypeBuilder.ROOT_NAMESPACE )   // parent_namespace
            .on ( new ElementsMustBeInstancesOfClass ( Graph.class ) )
                .toExit ()
            .build ();

        System.out.println ( "Kind:" );
        System.out.println ( "" + kind );

        final Graph<Value<?>, Transition> test_type =
            new TypeBuilder<String> (
                StandardValueClass.STRING,   // value_class
                "test_type",                 // name
                TypeBuilder.ROOT_NAMESPACE ) // parent_namespace
            .on ( new ElementsMustBeInstancesOfClass ( String.class ) )
            .build ();

        final Value<?> input =
            new One<Graph<Value<?>, Transition>> (
                TapeMachine.GRAPH_VALUE_CLASS,
                test_type );
        final StringBuilder debug = new StringBuilder ();

        final Value<?> [] output;
        try
        {
            output =
                new TapeMachine ( kind )
                    .evaluate ( debug, input );
        }
        catch ( Exception e )
        {
            System.out.println ( "DEBUG TRACE:" );
            System.out.println ( debug.toString () );
            throw e;
        }

        System.out.println ( "Test type should be a kind:" );
        if ( output.length != 2 ) // !!! should be 1.  testing out debug for now.
        {
            System.out.println ( "DEBUG TRACE:" );
            System.out.println ( debug.toString () );
            throw new IllegalStateException (
                "Expected test type to be returned as a kind,"
                + " but return value was: "
                + new ArrayObject<Value<?>> (
                      TapeMachine.STATE_VALUE_CLASS.elementClass (),
                      output ) );
        }

        System.out.println ( "SUCCESS." );
        System.out.println ( "" );
    }


    private static TapeMachine createDebugMachine ()
    {
        final TapeMachine debug =
            new TapeMachine (
                             new StateGraphBuilder ( "debug" )
                                 .fromEntry ()
                                     .to ( "-" )
                                 .from ( "-" )
                                     .copy ( Tape.INPUT, Tape.THIS )
                                     .to ( "-" )
                                 .from ( "-" ) );
        return debug;
    }


    public static void main (
                             String [] args
                             )
        throws Exception
    {
        TapeMachine debug = null;
        try
        {
            final ValueClass<String> string_value_class =
                StandardValueClass.STRING;

            final Graph<Value<?>, Transition> text =
                new TypeBuilder<String> (
                    string_value_class,          // value_class
                    "text",                      // name
                    TypeBuilder.ROOT_NAMESPACE ) // parent_namespace
                .build ();

            System.out.println ( "Text type:" );
            System.out.println ( "" + text );

            final One<String> text_example =
                new One<String> ( string_value_class,
                                  "Hello, world!" );
            final ValueClass<Object> not_string_value_class =
                StandardValueClass.OBJECT;
            final Countable<Object> not_text_example =
                new ValueBuilder<Object> ( not_string_value_class )
                    .add ( "This first element is a String" )
                    .add ( 42 )
                    .build ();

            System.out.println ( "Value should be text: " + text_example );
            debug = createDebugMachine ();
            new TapeMachine ( text )
                .transition (
                    new TapeMachine (
                        new StateGraphBuilder ( "text_example" )
                            .to ( text_example )
                        .build () ),
                    new TapeMachine ( new StateGraphBuilder ( "output" ) ),
                    debug )
                .orThrowUnchecked ();
            debug = null;
            System.out.println ( "SUCCESS." );
            System.out.println ( "" );

            System.out.println ( "Value should NOT be text: "
                                 + StringRepresentation.of ( not_text_example,
                                                             0 ) );
            debug = createDebugMachine ();
            if ( new TapeMachine ( text )
                     .transition (
                        new TapeMachine (
                            new StateGraphBuilder ( "not_text_example" )
                                .to ( not_text_example )
                            .build () ),
                        new TapeMachine ( new StateGraphBuilder ( "output" ) ),
                        debug )
                     .orNull () != null )
            {
                throw new IllegalStateException ( "Should not have been"
                                              + " treated as text, but was!" );
            }
            final Entry expected_node =
                new Entry ( ""
                            + new Read (
                                  Tape.INPUT,
                                  string_value_class.valueContract () )
                            + " Fail" );
            System.out.println ( "Making sure the following node is"
                                 + " included in the debug graph: "
                                 + expected_node );
            boolean is_expected_node_found = false;
            debug.graph ().node ( expected_node ).orThrowUnchecked ();
            debug = null;
            System.out.println ( "SUCCESS." );
            System.out.println ( "" );
        }
        catch ( Exception e )
        {
            if ( debug != null )
            {
                System.out.println ( "DEBUG TRACE:" );
                System.out.println ( debug.graph ().toStringDetails () );
            }

            throw e;
        }


        System.out.println ( "" );
        System.out.println ( "Making sure type 'text' is accessible from root namespace." );

        new TestTypes ().testKind ();
    }
}
