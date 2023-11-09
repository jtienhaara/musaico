package musaico.foundation.wiring.components;

import java.io.Serializable;


import musaico.foundation.filter.DiscardAll;
import musaico.foundation.filter.Filter;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Carriers;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Selector;
import musaico.foundation.wiring.Pulse;
import musaico.foundation.wiring.Tags;
import musaico.foundation.wiring.Wire;


public class StandardSelector
    implements Selector, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Filter<Conductor> [] filters;
    private final Conductor wireFactory;
    private final int hashCode;

    @SafeVarargs
    @SuppressWarnings("unchecked") // Generic varargs heap pollution.
    public StandardSelector (
            Filter<Conductor> ... filters
            )
    {
        this ( null,      // wire_factory
               filters ); // filters
    }

    @SafeVarargs
    @SuppressWarnings({"rawtypes", "unchecked", "varargs"}) // Generic array Filter [],
                                                            // Cast Filter [] - Filter<Conductor> [],
                                                            // System.arraycopy ( varargs ).
    protected StandardSelector (
            Conductor wire_factory,
            Filter<Conductor> ... filters
            )
    {
        int hash_code = 0;

        if ( wire_factory == null )
        {
            this.wireFactory = new StandardWireFactory ( "wf" + super.hashCode (), // id
                                                         this );                   // selector
        }
        else
        {
            this.wireFactory = wire_factory;
            hash_code += this.wireFactory.hashCode ();
        }

        if ( filters == null
             || filters.length == 0 )
        {
            // SuppressWarnings({"rawtypes", "unchecked"}) Generic array Filter [],
            //                                             Cast Filter [] - Filter<Conductor> [].
            this.filters = (Filter<Conductor> [])
                new Filter [ 0 ];
        }
        else
        {
            // SuppressWarnings({"rawtypes", "unchecked"}) Generic array Filter [],
            //                                             Cast Filter [] - Filter<Conductor> [].
            this.filters = (Filter<Conductor> [])
                new Filter [ filters.length ];

            // SuppressWarnings("varargs") System.arraycopy ( varargs ).
            System.arraycopy ( filters, 0,
                               this.filters, 0, filters.length );
            for ( int f = 0; f < this.filters.length; f ++ )
            {
                if ( this.filters [ f ] == null )
                {
                    this.filters [ f ] = new DiscardAll<Conductor> ();
                }
                else
                {
                    hash_code += this.filters [ f ].hashCode ();
                }
            }
        }

        // We have to do a lot of hash codes when
        // looking up Selectors in Maps at runtime.
        // So it's important to pre-load the hash
        // code -- both for speed, and also to make
        // sure it never changes due to the underlying
        // objects housed in this data structure.
        this.hashCode = hash_code;
    }

    /**
     * @see musaico.foundation.wiring.Selector#ends()
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"}) // Generic array Filter [],
                                                 // Cast Filter [] - Filter<Conductor> [],
    public final Filter<Conductor> [] ends ()
    {
        // SuppressWarnings({"rawtypes", "unchecked"}) Generic array Filter [],
        //                                             Cast Filter [] - Filter<Conductor> [].
        final Filter<Conductor> [] ends = (Filter<Conductor> [])
            new Filter [ this.filters.length ];
        System.arraycopy ( this.filters, 0,
                           ends, 0, this.filters.length );
        return ends;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( this == object )
        {
            return true;
        }
        else
        {
            // We have to do a lot of equals ()
            // checks on Selectors.  And in any case
            // we don't want two Selectors that are
            // defined the same way to be considered
            // equal when looking them up in Maps.
            // So we only check for identity equality.
            return false;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.hashCode;
    }

    /**
     * @see musaico.foundation.wiring.Selector#buildWires(musaico.foundation.wiring.Conductor[])
     */
    @Override
    public final Wire [] buildWires (
            Board board,
            Conductor ... conductors
            )
    {
        if ( conductors == null
             || conductors.length == 0 )
        {
            return new Wire [ 0 ];
        }

        final Carrier conductors_carrier =
            new StandardCarrier ( Conductor.class, // data_type
                                  conductors );    // data

        final Pulse context =
            new Pulse ( Pulse.Direction.PUSH, // direction
                        board,                // board
                        Conductor.NONE,       // source_component
                        Wire.NONE,            // source_witre
                        new Tags () );        // tags

        this.wireFactory.push ( context,              // context
                                conductors_carrier ); // carriers

        final Carrier [] wires_carriers =
            this.wireFactory.pull ( context ); // context

        final Wire [] wires =
            new Carriers ( wires_carriers ) // carriers
                .toData ( Wire.class,       // data_type
                          new Wire [ 0 ] ); // default_value_per_carrier
        return wires;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "selector [" );
        boolean is_first = true;
        for ( Filter<Conductor> filter : this.filters )
        {
            if ( is_first )
            {
                is_first = false;
                sbuf.append ( " " );
            }
            else
            {
                sbuf.append ( " | " );
            }

            sbuf.append ( "" + filter );
        }

        if ( is_first )
        {
            sbuf.append ( "]" );
        }
        else
        {
            sbuf.append ( " ]" );
        }

        final String as_string = sbuf.toString ();

        return as_string;
    }
}
