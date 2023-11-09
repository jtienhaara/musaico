package musaico.foundation.wiring.components;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.filter.Filter;

import musaico.foundation.structure.StringRepresentation;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Carriers;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Pulse;
import musaico.foundation.wiring.Selector;
import musaico.foundation.wiring.Tags;
import musaico.foundation.wiring.Wire;


public class StandardWireFactory
    implements Conductor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private static final int INFINITE_LOOP_PROTECTOR = 4096;


    // Immutable (though can have mutable content):
    private final String id;
    private final Selector selector ;
    private final Queue conductorsQueue =
        new Queue ( "conductors_queue" );
    private final Tags tags = new Tags ();

    public StandardWireFactory (
            String id,
            Selector selector
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

        if ( selector == null )
        {
            this.selector = Selector.NONE;
        }
        else
        {
            this.selector = selector;
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
            return new Carrier [ 0 ];
        }

        final Pulse queue_context =
            context.next ( this,        // source_conductor
                           Wire.NONE ); // source_wire

        final Carrier [] one_set_of_conductors =
            this.conductorsQueue.pull ( queue_context );

        if ( one_set_of_conductors == null
             || one_set_of_conductors.length == 0 )
        {
            return new Carrier [ 0 ];
        }

        final Conductor [] conductors =
            new Carriers ( one_set_of_conductors )
            .toData ( Conductor.class,
                      new Conductor [ 0 ] );

        if ( conductors == null
             || conductors.length == 0 )
        {
            return new Carrier [ 0 ];
        }

        final Wire [] wires =
            this.wire ( conductors );

        if ( wires == null
             || wires.length == 0 )
        {
            return new Carrier [ 0 ];
        }

        final Carrier one_set_of_wires =
            new StandardCarrier ( Wire.class,
                                  wires );

        return new Carrier [] { one_set_of_wires };
    }

    /**
     * @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Pulse, musaico.foundation.Carrier[])
     */
    @Override
    public final void push ( Pulse context, Carrier ... data )
    {
        if ( context == null
             || data == null )
        {
            // Do nothing.
            return;
        }

        final Pulse queue_context =
            context.next ( this,        // source_conductor
                           Wire.NONE ); // source_wire

        this.conductorsQueue.push ( queue_context, data );
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
        return "wire_factory [ " + this.id () + " ]";
    }

    // Can be overridden.
    protected Wire [] wire (
            Conductor ... conductors
            )
    {
        final Filter<Conductor> end_filters [] =
            this.selector.ends ();
        if ( end_filters == null
             || end_filters.length == 0 )
        {
            return new Wire [ 0 ];
        }

        final List<Conductor> ends_list = new ArrayList<Conductor> ();
        final List<Wire> tapped_wires_list = new ArrayList<Wire> ();
        int num_selections = 0;
        for ( int ef = 0; ef < end_filters.length; ef ++ )
        {
            final Filter<Conductor> filter;
            if ( end_filters [ ef ] == null )
            {
                continue;
            }

            final int num_selections_before = num_selections;
            for ( Conductor conductor : conductors )
            {
                if ( conductor == null )
                {
                    continue;
                }

                if ( ! end_filters [ ef ].filter ( conductor ).isKept () )
                {
                    continue;
                }

                if ( conductor instanceof Wire )
                {
                    tapped_wires_list.add ( (Wire) conductor );
                }
                else
                {
                    ends_list.add ( conductor );
                }

                if ( num_selections == num_selections_before )
                {
                    num_selections ++;
                }
            }
        }

        if ( num_selections < end_filters.length )
        {
            return new Wire [ 0 ];
        }

        if ( tapped_wires_list.size () > 0 )
        {
            return this.tap ( ends_list, tapped_wires_list );
        }

        final Conductor [] template = new Conductor [ ends_list.size () ];
        final Conductor [] ends = ends_list.toArray ( template );

        final Wire wire = new StandardWire ( ends );

        return new Wire [] { wire };
    }

    private final Wire [] tap (
            List<Conductor> ends_list,
            List<Wire> tapped_wires_list
            )
    {
        final int num_tappers = ends_list.size ();
        final int num_tapped_wires = tapped_wires_list.size ();
        if ( num_tappers == 0
             || num_tapped_wires == 0 )
        {
            return new Wire [ 0 ];
        }

        final Wire [] taps = new Wire [ num_tappers * num_tapped_wires ];
        int t = 0;
        for ( Conductor tapper : ends_list )
        {
            for ( Wire tapped_wire : tapped_wires_list )
            {
                taps [ t ] = new StandardTap ( tapped_wire, tapper );
                t ++;
            }
        }

        return taps;
    }
}
