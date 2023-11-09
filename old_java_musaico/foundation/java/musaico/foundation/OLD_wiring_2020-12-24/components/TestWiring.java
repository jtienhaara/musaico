package musaico.foundation.wiring.components;

import java.io.OutputStream;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.FixedSelector;
import musaico.foundation.wiring.Schematic;
import musaico.foundation.wiring.SchematicPrinter;
import musaico.foundation.wiring.Selector;
import musaico.foundation.wiring.SimpleCarrier;
import musaico.foundation.wiring.StandardSchematic;

import musaico.foundation.wiring.tag.Name;
import musaico.foundation.wiring.tag.TerminalType;

public class TestWiring
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Board createOutBoard (
            Terminal in,
            OutputStream output_stream
            )
    {
        final Schematic schematic =
            new StandardSchematic ();

        final Selector in_selector = new FixedSelector ( in );
        in_selector.tag ( new Name ( "out.in" ) );
        final StreamOut out = new StreamOut ( in_selector, output_stream );
        out.tag ( new Name ( "out" ) );
        schematic.add (
            in,
            out
            );
        final Board board = schematic.build ();

        return board;
    }

    private final Carrier createText (
            String text
            )
    {
        final Carrier text_carrier =
            new SimpleCarrier ( String.class,
                                new String [] { text } ); // No tags for now.
        return text_carrier;
    }

    private final Carrier createHelloWorld ()
    {
        return createText ( "Hello, world!\n" );
    }


    private final void testHelloWorld ()
    {
        final Terminal in = new Terminal ();
        final Board board = createOutBoard ( in, System.out );

        final Carrier hello_world = createHelloWorld ();

        board.on ();
        board.start ();
        in.push ( board, Selector.EXTERNAL, hello_world );
        board.stop ();
        board.off ();
    }

    private final void testSpeed1 (
            int num_iterations
            )
    {
        final Terminal in = new Terminal ();
        final Board board = createOutBoard ( in, System.err );

        final String hello_world_text = "Hello, world!";
        final Carrier hello_world = createText ( hello_world_text + "\n" );

        // Warmup:
        System.err.println ( "Warmup:" );

        board.on ();
        board.start ();
        in.push ( board, Selector.EXTERNAL, hello_world );
        board.stop ();
        board.off ();

        System.err.println ( hello_world_text );
        System.err.flush ();

        System.err.println ( "Done warmup" );
        System.err.flush ();

        System.out.println ( "Starting speed test (" + num_iterations + " iterations)..." );
        System.out.flush ();

        // Go!
        final long wiring_ns_start = System.nanoTime ();
        board.on ();
        board.start ();
        for ( int iteration = 0; iteration < num_iterations; iteration ++ )
        {
            in.push ( board, Selector.EXTERNAL, hello_world );
        }
        board.stop ();
        board.off ();
        final long wiring_ns_end = System.nanoTime ();

        final long print_ns_start = System.nanoTime ();
        for ( int iteration = 0; iteration < num_iterations; iteration ++ )
        {
            System.err.println ( hello_world_text );
        }
        System.err.flush ();
        final long print_ns_end = System.nanoTime ();

        System.out.println ( "Finished speed test (" + num_iterations + " iterations)" );
        System.out.flush ();

        final long wiring_ns_time = wiring_ns_end - wiring_ns_start;
        final long wiring_s_time = wiring_ns_time / 1000000000L;

        final long print_ns_time = print_ns_end - print_ns_start;
        final long print_s_time = print_ns_time / 1000000000L;

        System.out.println ( "Wiring: " + wiring_ns_time + " ns (" + wiring_s_time + " s)" );
        System.out.println ( "Print:  " + print_ns_time + " ns (" + print_s_time + " s)" );
    }


    private final void testDaughterboard ()
    {
        System.out.println ( "=================" );
        System.out.println ( "testDaughterboard" );

        final Terminal daughter_in = new Terminal ();
        daughter_in.tag (
            new Name ( "in" ),
            new TerminalType ( TerminalType.InternalOrExternal.EXTERNAL )
            );
        final Board daughterboard =
            createOutBoard ( daughter_in, System.out );

        final Carrier hello_world = createHelloWorld ();

        final Schematic mother = new StandardSchematic ();
        final Terminal mother_in = new Terminal ();
        mother_in.tag (
            new Name ( "in" ),
            new TerminalType ( TerminalType.InternalOrExternal.INTERNAL )
            );
        mother.add ( mother_in );
        final Board motherboard = mother.build ();

        final Daughterboard cable = new Daughterboard ( motherboard, daughterboard );
        cable.tag ( new Name ( "cable" ) );

        daughterboard.reconcile ();
        motherboard.reconcile ();

        System.out.println ( "!!! Daughterboard:" ); new SchematicPrinter ().print ( daughterboard );
        System.out.println ( "!!! Motherboard:" ); new SchematicPrinter ().print ( motherboard );

        motherboard.on ();
        motherboard.start ();
        System.out.println ( "!!! TestWiring about to push..." );
        mother_in.push ( motherboard, Selector.EXTERNAL, hello_world );
        System.out.println ( "!!! TestWiring done pushing" );
        motherboard.stop ();
        motherboard.off ();
        System.out.println ( "-----------------" );
    }


    public static void main (
            String [] args
            )
    {
        final TestWiring wiring = new TestWiring ();

        wiring.testHelloWorld ();

        wiring.testDaughterboard ();

        wiring.testSpeed1 ( 1 );
        wiring.testSpeed1 ( 10 );
        wiring.testSpeed1 ( 100 );
        wiring.testSpeed1 ( 1000 );
        wiring.testSpeed1 ( 10000 );
        wiring.testSpeed1 ( 100000 );
        wiring.testSpeed1 ( 1000000 );
    }
}
