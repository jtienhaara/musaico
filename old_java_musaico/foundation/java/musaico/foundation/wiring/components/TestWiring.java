package musaico.foundation.wiring.components;

import java.io.OutputStream;
import java.io.Serializable;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Pulse;
import musaico.foundation.wiring.Schematic;
import musaico.foundation.wiring.Selector;
import musaico.foundation.wiring.Tags;
import musaico.foundation.wiring.Wire;

public class TestWiring
    implements Serializable
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
            new StandardSchematic ( "out_board" );

        final StreamOut out = new StreamOut ( "out", output_stream );

        schematic.addConductors (
            in,
            out
            );

        final Selector selector =
            new SpecificConductorsSelector ( in, out );

        schematic.addSelectors ( selector );

        final Board out_board = schematic.build (); // No motherboards.

        return out_board;
    }

    private final Carrier createText (
            String text
            )
    {
        final Carrier text_carrier =
            new StandardCarrier ( String.class,
                                  new String [] { text } );
        return text_carrier;
    }

    private final Carrier createHelloWorld ()
    {
        return createText ( "Hello, world!\n" );
    }


    private final void testHelloWorld ()
    {
        final Terminal in = new Terminal ( "in" );
        final Board out_board = createOutBoard ( in, System.out );

        final Carrier hello_world = createHelloWorld ();

        final Pulse context = new Pulse ( Pulse.Direction.PUSH,
                                          out_board,
                                          Conductor.NONE,
                                          Wire.NONE,
                                          new Tags () );

        in.push ( context, hello_world );
    }

    private final void testSpeed1 (
            int num_iterations
            )
    {
        final Terminal in = new Terminal ( "in" );
        final Board board = createOutBoard ( in, System.err );

        final String hello_world_text = "Hello, world!";
        final Carrier hello_world = createText ( hello_world_text + "\n" );

        // Warmup:
        System.err.println ( "Warmup:" );

        final Pulse pre_context = new Pulse ( Pulse.Direction.PUSH,
                                              board,
                                              Conductor.NONE,
                                              Wire.NONE,
                                              new Tags () );

        in.push ( pre_context, hello_world );

        System.err.println ( hello_world_text );
        System.err.flush ();

        System.err.println ( "Done warmup" );
        System.err.flush ();

        System.out.println ( "Starting speed test (" + num_iterations + " iterations)..." );
        System.out.flush ();

        // Go!
        final long wiring_ns_start = System.nanoTime ();
        for ( int iteration = 0; iteration < num_iterations; iteration ++ )
        {
            final Pulse context = new Pulse ( Pulse.Direction.PUSH,
                                              board,
                                              Conductor.NONE,
                                              Wire.NONE,
                                              new Tags () );

            in.push ( context, hello_world );
        }
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

    private final Board buildTappedBoard (
            Terminal in,
            Conductor ... tappers
            )
    {
        final Schematic schematic =
            new StandardSchematic ( "out_board" );

        final StreamOut out = new StreamOut ( "out", System.out );

        schematic.addConductors (
            in,
            out
            );
        schematic.addConductors (
            tappers
            );

        final Selector wire_selector =
            new SpecificConductorsSelector ( in, out );

        schematic.addSelectors (
            wire_selector
            );

        for ( Conductor tapper : tappers )
        {
            final Selector tap_selector =
                new TapSelector ( tapper );

            schematic.addSelectors (
                tap_selector
                );
        }

        final Board out_board = schematic.build (); // No motherboards.

        return out_board;
    }

    private final void testTapWait ()
    {
        final Terminal in = new Terminal ( "in" );

        final Wait wait_tapper = new Wait ( "wait_3_seconds",
                                            3000L ); // milliseconds.

        final Board out_board = this.buildTappedBoard ( in, wait_tapper );

        final Carrier hello_world = createHelloWorld ();

        // !!! new SchematicPrinter ().print ( out_board ); // !!!
        System.out.println ( "Starting tap test with 3 second wait..." );
        System.out.flush ();

        final Pulse context = new Pulse ( Pulse.Direction.PUSH,
                                          out_board,
                                          Conductor.NONE,
                                          Wire.NONE,
                                          new Tags () );

        in.push ( context, hello_world );

        System.out.println ( "...Finished tap test with 3 second wait." );
        System.out.flush ();
    }

    private final void testTapTrace ()
    {
        final Terminal in = new Terminal ( "in" );

        final Trace trace_tapper = new Trace ( "trace",
                                               System.out );

        final Board out_board = this.buildTappedBoard ( in, trace_tapper );

        final Carrier hello_world = createHelloWorld ();

        System.out.println ( "Starting tap test with tracing..." );
        System.out.flush ();

        final Pulse context = new Pulse ( Pulse.Direction.PUSH,
                                          out_board,
                                          Conductor.NONE,
                                          Wire.NONE,
                                          new Tags () );

        in.push ( context, hello_world );

        System.out.println ( "...Finished tap test with tracing." );
        System.out.flush ();
    }

    private final void testTapTraceAndWait ()
    {
        final Terminal in = new Terminal ( "in" );

        final Trace trace_tapper = new Trace ( "trace",
                                               System.out );
        final Wait wait_tapper = new Wait ( "wait_3_seconds",
                                            3000L ); // milliseconds.

        final Board out_board = this.buildTappedBoard ( in, trace_tapper, wait_tapper );
        // !!!  new SchematicPrinter ().print ( out_board ); // !!!

        final Carrier hello_world = createHelloWorld ();

        System.out.println ( "Starting tap test with tracing and 3 second wait..." );
        System.out.flush ();

        final Pulse context = new Pulse ( Pulse.Direction.PUSH,
                                          out_board,
                                          Conductor.NONE,
                                          Wire.NONE,
                                          new Tags () );

        in.push ( context, hello_world );

        System.out.println ( "...Finished tap test with tracing and 3 second wait." );
        System.out.flush ();
    }


    /* !!!
    private final void testDaughterboard ()
    {
        System.out.println ( "=================" );
        System.out.println ( "testDaughterboard" );

        final Terminal daughter_in = new Terminal ( "daughter_in" );
        daughter_in.tag (
            new Name ( "in" ),
            new TerminalType ( TerminalType.InternalOrExternal.EXTERNAL )
            );
        final Board daughterboard =
            createOutBoard ( daughter_in, System.out );

        final Carrier hello_world = createHelloWorld ();

        final Schematic mother = new StandardSchematic ();
        final Terminal mother_in = new Terminal ( "mother_in" );
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

        final Pulse context = new Pulse ( Pulse.Direction.PUSH,
                                          motherboard,
                                          Conductor.NONE,
                                          Wire.NONE,
new Tags ()  );

        mother_in.push ( context, hello_world );
        System.out.println ( "!!! TestWiring done pushing" );
        motherboard.stop ();
        motherboard.off ();
        System.out.println ( "-----------------" );
    }
    !!! */

    private final void testStreamIn ()
    {
        final Terminal prompt = new Terminal ( "prompt" );
        final StreamIn in = new StreamIn ( "in", System.in );
        final StreamOut out = new StreamOut ( "out", System.out );
        final Trace trace_tapper = new Trace ( "trace",
                                               System.out );

        final Schematic schematic =
            new StandardSchematic ( "in_out_board" );

        schematic.addConductors (
            prompt,
            in,
            out,
            trace_tapper
            );

        final Selector prompt_selector =
            new SpecificConductorsSelector ( prompt, out );
        final Selector in_selector =
            new SpecificConductorsSelector ( in, out );
        final Selector tap_selector =
            new TapSelector ( trace_tapper );

        schematic.addSelectors ( prompt_selector,
                                 in_selector
                                 // ,
                                 // tap_selector
                                 );

        final Board in_out_board = schematic.build (); // No motherboards.

        final Carrier prompt_carrier =
            this.createText ( "Type something, will ya?  (Ctrl-D to end.)\n" );

        final Pulse context = new Pulse ( Pulse.Direction.PUSH,
                                          in_out_board,
                                          Conductor.NONE,
                                          Wire.NONE,
                                          new Tags () );

        System.out.println ( "Starting stream in test..." );
        prompt.push ( context, prompt_carrier );
        out.pull ( context );
        System.out.println ( "Finished stream in test." );
    }


    public static void main (
            String [] args
            )
    {
        final TestWiring wiring = new TestWiring ();

        try
        {
            wiring.testHelloWorld ();

            wiring.testTapWait ();
            wiring.testTapTrace ();
            wiring.testTapTraceAndWait ();

            /* !!!
               wiring.testDaughterboard ();
               !!! */

            wiring.testSpeed1 ( 1 );
            wiring.testSpeed1 ( 10 );
            wiring.testSpeed1 ( 100 );
            wiring.testSpeed1 ( 1000 );
            wiring.testSpeed1 ( 10000 );
            wiring.testSpeed1 ( 100000 );
            wiring.testSpeed1 ( 1000000 );

            wiring.testStreamIn ();
        }
        catch ( Throwable t )
        {
            t.printStackTrace ( System.out );
        }
    }
}
