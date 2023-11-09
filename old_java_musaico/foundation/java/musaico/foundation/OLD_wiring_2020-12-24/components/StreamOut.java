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


import musaico.foundation.structure.StringRepresentation;

import musaico.foundation.wiring.AbstractConductor;
import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Conductivity;
import musaico.foundation.wiring.Selector;


public class StreamOut
    extends AbstractConductor
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Selector inSelector;
    private final PrintWriter out;

    public StreamOut (
            Selector in_selector,
            OutputStream out
            )
    {
        this ( in_selector,
               new PrintWriter ( new OutputStreamWriter ( out ) ) );
    }

    public StreamOut (
            Selector in_selector,
            PrintWriter out
            )
    {
        this.inSelector = in_selector;
        this.out = out;
    }

    public final Selector inSelector (
            Board board
            )
    {
        return this.inSelector;
    }

    public final Conductor [] in (
            Board board
            )
    {
        return board.select ( this.inSelector );
    }

    /**
     * @see musaico.foundation.wiring.AbstractConductor#transition(musaico.foundation.wiring.Board, musaico.foundation.wiring.Conductivity, musaico.foundation.wiring.Conductivity)
     */
    @Override
    protected final void transition (
            Board board,
            Conductivity old_state,
            Conductivity new_state
            )
    {
        // !!! close OutpuStream, Writer, if necessary & applicable.
    }

    // @see musaico.foundation.wiring.Conductor#pull(musaico.foundation.wiring.Board, musaico.foundation.wiring.Selector)
    @Override
    public final Carrier [] pull ( Board board, Selector source )
    {
        return new Carrier [ 0 ];
    }

    // @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Board, musaico.foundation.wiring.Selector, musaico.foundation.Carrier[])
    @Override
    public final void push ( Board board, Selector source, Carrier ... carriers )
    {
        if ( board == null
             || source == null
             || source != this.inSelector
             || carriers == null
             || carriers.length == 0 )
        {
            return;
        }

        final Object [] empty = new Object [ 0 ];
        for ( Carrier carrier : carriers )
        {
            final Object [] data =
                carrier.data ( Object.class, // data_class
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
     * @see musaico.foundation.wiring.Conductor#selectors(musaico.foundation.wiring.Board)
     */
    @Override
    public final Selector [] selectors (
            Board board
            )
    {
        return new Selector []
            {
                this.inSelector
            };
    }
}
