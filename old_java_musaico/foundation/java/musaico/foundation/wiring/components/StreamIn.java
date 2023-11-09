package musaico.foundation.wiring.components;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Serializable;
import java.io.Reader;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.structure.StringRepresentation;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Pulse;
import musaico.foundation.wiring.Tags;
import musaico.foundation.wiring.Wire;


public class StreamIn
    implements Conductor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    public static final String NO_ID = "NO_ID";


    // Immutable:
    private final String id;
    private final Reader in;

    private final Tags tags = new Tags ();


    public StreamIn (
            String id,
            InputStream in
            )
    {
        this ( id,
               in == null
                   ? null
                   : new InputStreamReader ( in ) );
    }

    public StreamIn (
            String id,
            String in_filename
            )
        throws IOException
    {
        this ( id,
               in_filename == null
                   ? null
                   : new FileReader ( new File ( in_filename ) ) );
    }

    public StreamIn (
            String id,
            File in_file
            )
        throws IOException
    {
        this ( id,
               in_file == null
                   ? null
                   : new FileReader ( in_file ) );
    }

    public StreamIn (
            String id,
            Reader in
            )
    {
        if ( id == null )
        {
            this.id = StreamIn.NO_ID;
        }
        else
        {
            this.id = id;
        }

        if ( in == null )
        {
            this.in = new InputStreamReader ( System.in );
        }
        else
        {
            this.in = in;
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

        final StringBuilder sbuf = new StringBuilder ();
        final char [] cbuf = new char [ 4096 ];
        int num_chars_read;
        try
        {
            while ( ( num_chars_read = in.read ( cbuf ) ) > 0 )
            {
                sbuf.append ( cbuf, 0, num_chars_read );
            }
        }
        catch ( IOException e )
        {
            sbuf.append ( "\n" );
            final StringWriter sw = new StringWriter ();
            final PrintWriter pw = new PrintWriter ( sw );
            e.printStackTrace ( pw );
            sbuf.append ( sw.toString () );
        }

        final String in_string = sbuf.toString ();
        final Carrier in_carrier =
            new StandardCarrier ( String.class, in_string );

        return new Carrier [] { in_carrier };
    }

    /**
     * @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Pulse, musaico.foundation.Carrier[])
     */
    @Override
    public final void push ( Pulse context, Carrier ... carriers )
    {
        // Do nothing.
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
        return "stream_in [ " + this.id () + " ]";
    }
}
