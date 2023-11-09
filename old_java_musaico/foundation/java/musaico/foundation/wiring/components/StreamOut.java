package musaico.foundation.wiring.components;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.structure.StringRepresentation;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Pulse;
import musaico.foundation.wiring.Tags;
import musaico.foundation.wiring.Wire;


public class StreamOut
    implements Conductor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    public static final String NO_ID = "NO_ID";


    // Immutable:
    private final String id;
    private final PrintWriter out;

    private final Tags tags = new Tags ();


    public StreamOut (
            String id,
            OutputStream out
            )
    {
        this ( id,
               out == null
                   ? null
                   : new PrintWriter ( new OutputStreamWriter ( out ) ) );
    }

    public StreamOut (
            String id,
            String out_filename
            )
        throws IOException
    {
        this ( id,
               out_filename == null
                   ? null
                   : new PrintWriter ( new FileWriter ( new File ( out_filename ) ) ) );
    }

    public StreamOut (
            String id,
            File out_file
            )
        throws IOException
    {
        this ( id,
               out_file == null
                   ? null
                   : new PrintWriter ( new FileWriter ( out_file ) ) );
    }

    public StreamOut (
            String id,
            PrintWriter out
            )
    {
        if ( id == null )
        {
            this.id = StreamOut.NO_ID;
        }
        else
        {
            this.id = id;
        }

        if ( out == null )
        {
            this.out = new PrintWriter ( new OutputStreamWriter ( System.out ) );
        }
        else
        {
            this.out = out;
        }
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
        return super.hashCode ();
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
        if ( context == null )
        {
            // Do nothing.
            return new Carrier [ 0 ];
        }

        final Wire sink_wire = context.sourceWire ();
        final Conductor sink_conductor = context.sourceConductor ();
        final Pulse next_context =
            context.next ( this,        // source_conductor
                           Wire.NONE ); // source_wire

        final Object [] empty = new Object [ 0 ];
        for ( Wire wire : context.board ().wiresFrom ( this ) )
        {
            if ( wire == sink_wire
                 || wire == sink_conductor )
            {
                continue;
            }

            final Carrier [] carriers = wire.pull ( next_context );
            if ( carriers == null
                 || carriers.length == 0 )
            {
                continue;
            }

            for ( Carrier carrier : carriers )
            {
                final Object [] data =
                    carrier.elements ( Object.class, // data_class
                                       empty );      // default_value
                if ( data == null ) // Theoretically never happens.
                {
                    continue;
                }

                for ( Object datum : data )
                {
                    final String as_string;
                    if ( datum instanceof String )
                    {
                        as_string = (String) datum;
                    }
                    else
                    {
                        as_string =
                            StringRepresentation.of ( datum, // object
                                                      0 );   // max_length
                    }

                    this.out.print ( as_string );
                }
            }
        }

        this.out.flush ();

        return new Carrier [ 0 ];
    }

    /**
     * @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Pulse, musaico.foundation.Carrier[])
     */
    @Override
    public final void push ( Pulse context, Carrier ... carriers )
    {
        if ( context == null
             || carriers == null )
        {
            // Do nothing.
            return;
        }

        final Object [] empty = new Object [ 0 ];
        for ( Carrier carrier : carriers )
        {
            final Object [] data =
                carrier.elements ( Object.class, // data_class
                                   empty );      // default_value
            if ( data == null ) // Theoretically never happens.
            {
                continue;
            }

            for ( Object datum : data )
            {
                final String as_string;
                if ( datum instanceof String )
                {
                    as_string = (String) datum;
                }
                else
                {
                    as_string =
                        StringRepresentation.of ( datum, // object
                                                  0 );   // max_length
                }

                this.out.print ( as_string );
            }
        }

        this.out.flush ();
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
        return "stream_out [ " + this.id () + " ]";
    }
}
