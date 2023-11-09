package musaico.foundation.wiring.components;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Carriers;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.NoCarrier;
import musaico.foundation.wiring.Pulse;
import musaico.foundation.wiring.Tags;
import musaico.foundation.wiring.Wire;


public class Trace
    implements Conductor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // Immutable:
    private final String id;
    private final StreamOut out;
    private final Tags tags = new Tags ();


    public Trace (
            String id,
            OutputStream out
            )
    {
        this ( id,
               new StreamOut ( "trace_out", out ) );
    }

    public Trace (
            String id,
            PrintWriter out
            )
    {
        this ( id,
               new StreamOut ( "trace_out", out ) );
    }

    public Trace (
            String id,
            StreamOut out
            )
    {
        if ( id == null )
        {
            this.id = "no_id";
        }
        else
        {
            this.id = id;
        }

        if ( out == null )
        {
            this.out = new StreamOut ( "trace_out", System.out );
        }
        else
        {
            this.out = out;
        }
    }

    private final <ELEMENT extends Object> String arrayToString (
            ELEMENT [] array
            )
    {
        final StringBuilder sbuf = new StringBuilder ();

        sbuf.append ( "[" );
        boolean is_first = true;
        for ( ELEMENT element : array )
        {
            final String base_string = "" + element;
            final String no_funny_characters = base_string
                .replaceAll ( "\\n", "\\\\n" )
                .replaceAll ( "\\r", "\\\\r" )
                .replaceAll ( "'", "\"" )
                .replaceAll ( "[^ \\ta-zA-Z0-9`~!@#$%^&*()-_=+\\\\|\\[\\]}{'\";:/?\\.>,<]", "?" );
            final String element_string = "'" + no_funny_characters + "'";

            if ( is_first )
            {
                is_first = false;
                sbuf.append ( " " );
            }
            else
            {
                sbuf.append ( ", " );
            }

            sbuf.append ( element_string );
        }

        if ( is_first )
        {
            sbuf.append ( "]" );
        }
        else
        {
            sbuf.append ( " ]" );
        }

        final String array_string = sbuf.toString ();

        return array_string;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == this )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.out.hashCode ();
    }

    /**
     * @see musaico.foundation.wiring.Conductor#id()
     */
    @Override
    public final String id ()
        throws Return.NeverNull.Violation
    {
        return this.id;
    }

    /**
     * @see musaico.foundation.wiring.Conductor#pull(musaico.foundation.wiring.Pulse)
     */
    @Override
    public final Carrier [] pull ( Pulse context )
    {
        final Wire [] source_wires;
        if ( context == null )
        {
            source_wires = new Wire [ 0 ];
        }
        else
        {
            final Wire [] maybe_source_wires = context.board ().wiresFrom ( this );
            if ( maybe_source_wires == null )
            {
                source_wires = new Wire [ 0 ];
            }
            else
            {
                source_wires = maybe_source_wires;
            }
        }

        final Wire sink_wire = context.sourceWire ();
        final Conductor sink_conductor = context.sourceConductor ();
        final Pulse out_context =
            context.next ( this,        // source_conductor
                           Wire.NONE ); // source_wire

        // Pre trace output:
        final String pre_trace =
              "  trace.pre: {\n"
            + "    direction:      'pull'\n"
            + "    board:          '" + context.board () + "'\n"
            + "    sink_wire:      '" + sink_wire + "'\n"
            + "    sink_conductor: '" + sink_conductor + "'\n"
            + "    source_wires:   " + this.arrayToString ( source_wires ) + "\n"
            + "    tags:           " + context.tags () + "\n"
            + "  }\n";
        final Carrier pre_carrier = new StandardCarrier ( String.class, pre_trace );
        this.out.push ( out_context, pre_carrier );

        final Carriers pulled = new Carriers ();
        for ( Wire wire : source_wires )
        {
            if ( wire == null
                 || wire == sink_wire )
            {
                continue;
            }

            final Pulse next_context =
                context.next ( this,   // source_conductor
                               wire ); // source_wire

            final Carrier [] more_pulled = wire.pull ( next_context );

            // Mid trace output:
            final String mid_trace =
                  "  trace.mid: {\n"
                + "    direction:      'pull'\n"
                + "    board:          '" + context.board () + "'\n"
                + "    sink_wire:      '" + sink_wire + "'\n"
                + "    sink_conductor: '" + sink_conductor + "'\n"
                + "    source_wire:    '" + wire + "'\n"
                + "    tags:           " + next_context.tags () + "\n"
                + "    pulled:         " + this.arrayToString ( more_pulled ) + "\n"
                + "  }\n";
            final Carrier mid_carrier = new StandardCarrier ( String.class, mid_trace );
            this.out.push ( out_context, mid_carrier );

            pulled.add ( more_pulled );
        }

        final Carrier [] pulled_carriers = pulled.toArray ();

        // Post trace output:
        final String post_trace =
              "  trace.post: {\n"
            + "    direction:      'pull'\n"
            + "    board:          '" + context.board () + "'\n"
            + "    sink_wire:      '" + sink_wire + "'\n"
            + "    sink_conductor: '" + sink_conductor + "'\n"
            + "    pulled:         " + this.arrayToString ( pulled_carriers ) + "\n"
            + "  }\n";
        final Carrier post_carrier = new StandardCarrier ( String.class, post_trace );
        this.out.push ( out_context, post_carrier );

        return pulled_carriers;
    }

    /**
     * @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Pulse, musaico.foundation.Carrier[])
     */
    @Override
    public final void push ( Pulse context, Carrier ... carriers )
    {
            System.out.println ( "!!! Trace 1" );
            System.out.flush (); // !!!
        final Wire [] sink_wires;
        if ( context == null )
        {
            sink_wires = new Wire [ 0 ];
        }
        else
        {
            final Wire [] maybe_sink_wires = context.board ().wiresFrom ( this );
            if ( maybe_sink_wires == null )
            {
                sink_wires = new Wire [ 0 ];
            }
            else
            {
                sink_wires = maybe_sink_wires;
            }
        }

        final Wire source_wire = context.sourceWire ();
        final Conductor source_conductor = context.sourceConductor ();
        final Pulse out_context =
            context.next ( this,        // source_conductor
                           Wire.NONE ); // source_wire

        // Pre trace output:
        final String pre_trace =
              "  trace.pre: {\n"
            + "    direction:        'push'\n"
            + "    board:            '" + context.board () + "'\n"
            + "    source_wire:      '" + source_wire + "'\n"
            + "    source_conductor: '" + source_conductor + "'\n"
            + "    tags:             " + context.tags () + "\n"
            + "    data:             " + this.arrayToString ( carriers ) + "\n"
            + "    sink_wires:       " + this.arrayToString ( sink_wires ) + "\n"
            + "  }\n";
        final Carrier pre_carrier = new StandardCarrier ( String.class, pre_trace );
        this.out.push ( out_context, pre_carrier );

        System.out.println ( "!!! Trace 1.5 sink wires = " + java.util.Arrays.toString ( sink_wires ) );
            System.out.flush (); // !!!
        for ( Wire wire : sink_wires )
        {
            if ( wire == source_wire )
            {
                continue;
            }

            final Pulse next_context =
                context.next ( this,   // source_conductor
                               wire ); // source_wire

            System.out.println ( "!!! Trace pushing to wire: " + wire );
            System.out.flush (); // !!!
            wire.push ( next_context, carriers );
            System.out.println ( "!!! Trace pushed  to wire: " + wire );
            System.out.flush (); // !!!

            // Mid trace output:
            final String mid_trace =
                  "  trace.mid: {\n"
                + "    direction:        'push'\n"
                + "    board:            '" + context.board () + "'\n"
                + "    source_wire:      '" + source_wire + "'\n"
                + "    source_conductor: '" + source_conductor + "'\n"
                + "    tags:             " + next_context.tags () + "\n"
                + "    data:             " + this.arrayToString ( carriers ) + "\n"
                + "    sink_wire:        '" + wire + "'\n"
                + "  }\n";
            final Carrier mid_carrier = new StandardCarrier ( String.class, mid_trace );
            this.out.push ( out_context, mid_carrier );
        }

        // Post trace output:
        final String post_trace =
              "  trace.post: {\n"
            + "    direction:        'push'\n"
            + "    board:            '" + context.board () + "'\n"
            + "    source_wire:      '" + source_wire + "'\n"
            + "    source_conductor: '" + source_conductor + "'\n"
            + "    data:             " + this.arrayToString ( carriers ) + "\n"
            + "    sink_wires:       " + this.arrayToString ( sink_wires ) + "\n"
            + "  }\n";
        final Carrier post_carrier = new StandardCarrier ( String.class, post_trace );
        this.out.push ( out_context, post_carrier );
            System.out.println ( "!!! Trace 2" );
            System.out.flush (); // !!!
    }

    /**
     * @see musaico.foundation.wiring.Conductor#tags()
     */
    @Override
    public final Tags tags ()
        throws Return.NeverNull.Violation
    {
        return this.tags;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "trace [ " + this.id + " ]";
    }
}
