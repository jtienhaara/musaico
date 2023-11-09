package musaico.foundation.wiring.components;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


import musaico.foundation.structure.StringRepresentation;

import musaico.foundation.wiring.AbstractConductor;
import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Conductivity;
import musaico.foundation.wiring.Selector;


public class Function
    extends AbstractConductor
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final LinkedHashSet<Selector> inSelectors;
    private final LinkedHashSet<Selector> outSelectors;

    public Function (
            Selector [] in_selectors,
            Selector [] out_selectors
            )
    {
        this.inSelectors = new LinkedHashSet<Selector> ();
        if ( in_selectors != null
             && in_selectors.length > 0 )
        {
            for ( Selector in_selector : in_selectors )
            {
                if ( in_selector != null )
                {
                    this.inSelectors.add ( in_selector );
                }
            }
        }

        this.outSelectors = new LinkedHashSet<Selector> ();
        if ( out_selectors != null
             && out_selectors.length > 0 )
        {
            for ( Selector out_selector : out_selectors )
            {
                if ( out_selector != null )
                {
                    this.outSelectors.add ( out_selector );
                }
            }
        }
    }

    public final Selector [] inSelectors ()
    {
        final Selector [] in_selectors =
            new Selector [ this.inSelectors.size () ];
        int is = 0;
        for ( Selector in_selector : this.inSelectors )
        {
            in_selectors [ is ] = in_selector;
            is ++;
        }

        return in_selectors;
    }

    public final Conductor [] in (
            Board board
            )
    {
        final List<Conductor []> in_list =
            new ArrayList<Conductor []> ();
        int total_length = 0;
        for ( Selector in_selector : this.inSelectors )
        {
            final Conductor [] selection =
                board.select ( in_selector );
            if ( selection == null
                 || selection.length == 0 )
            {
                conttinue;
            }

            in_list.add ( selection );
            total_length += selection.length;
        }

        if ( in_list.size () == 0 )
        {
            return new Conductor [ 0 ];
        }
        else if ( in_list.size () == 1 )
        {
            return in_list.get ( 0 );
        }

        final Conductor [] in = new Conductor [ total_length ];
        int offset = 0;
        for ( Conductor [] selection : in_list )
        {
            System.arraycopy ( selection, 0,
                               in, offset, selection.length );
            offset += selection.length;
        }

        return in;
    }

    public final Selector [] outSelectors ()
    {
        final Selector [] out_selectors =
            new Selector [ this.outSelectors.size () ];
        int os = 0;
        for ( Selector out_selector : this.outSelectors )
        {
            out_selectors [ os ] = out_selector;
            os ++;
        }

        return out_selectors;
    }

    public final Conductor [] out (
            Board board
            )
    {
        final List<Conductor []> out_list =
            new ArrayList<Conductor []> ();
        int total_length = 0;
        for ( Selector out_selector : this.outSelectors )
        {
            final Conductor [] selection =
                board.select ( out_selector );
            if ( selection == null
                 || selection.length == 0 )
            {
                conttinue;
            }

            out_list.add ( selection );
            total_length += selection.length;
        }

        if ( out_list.size () == 0 )
        {
            return new Conductor [ 0 ];
        }
        else if ( out_list.size () == 1 )
        {
            return out_list.get ( 0 );
        }

        final Conductor [] out = new Conductor [ total_length ];
        int offset = 0;
        for ( Conductor [] selection : out_list )
        {
            System.arraycopy ( selection, 0,
                               out, offset, selection.length );
            offset += selection.length;
        }

        return out;
    }

    // @see musaico.foundation.wiring.Conductor#transition(musaico.foundation.wiring.Conductivity, musaico.foundation.wiring.Conductivity)
    @Override
    protected final void transition (
            Conductivity old_state,
            Conductivity new_state
            )
    {
    }

    // @see musaico.foundation.wiring.Conductor#pull(musaico.foundation.wiring.Board, musaico.foundation.wiring.Selector)
    @Override
    public final Carrier [] pull ( Board board, Selector source )
    {
        if ( board == null
             || source == null
             || ! this.outSelectors.contains ( source ) )
        {
            return new Carrier [ 0 ];
        }

        !!!;
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

    // @see musaico.foundation.wiring.Conductor#selectors()
    @Override
    public final Selector [] selectors ()
    {
        return new Selector []
            {
                this.inSelector
            };
    }
}
