package musaico.foundation.wiring.components;

import java.io.Serializable;

import java.util.LinkedHashMap;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Carriers;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Pulse;
import musaico.foundation.wiring.Tag;
import musaico.foundation.wiring.TagLibrary;
import musaico.foundation.wiring.Tags;
import musaico.foundation.wiring.Tap;
import musaico.foundation.wiring.Wire;


// !!! For now the tap conductors are sequential
// !!! (not parallel as in the original design).
// !!! let's see how this works out...
public class StandardTap
    implements Tap, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // Immutable:
    private final String id;
    private final Wire tappedWire;
    private final Conductor [] tappers;
    private final Tags tags = new Tags ();

    private final Wire [] taps;
    private final LinkedHashMap<Wire, Wire> routes = new LinkedHashMap<Wire, Wire> ();
    private final LinkedHashMap<Wire, Wire> reverseRoutes = new LinkedHashMap<Wire, Wire> ();

    private final Tag routeTag;

    private final int hashCode;

    public StandardTap (
            Wire tapped_wire,
            Conductor ... tappers
            )
    {
        int hash_code = 0;

        final Conductor [] wire_ends;
        if ( tapped_wire == null )
        {
            this.tappedWire = Wire.NONE;
            wire_ends = new Conductor [ 0 ];
        }
        else
        {
            this.tappedWire = tapped_wire;
            final Conductor [] maybe_wire_ends = this.tappedWire.ends ();
            if ( maybe_wire_ends == null )
            {
                wire_ends = new Conductor [ 0 ];
            }
            else
            {
                wire_ends = maybe_wire_ends;
            }
        }

        if ( tappers == null
             || tappers.length >= ( Integer.MAX_VALUE - tappers.length ) ) // ( 2 * tappers.length ) >= Integer.MAX_VALUE?  Too long.
        {
            this.tappers = new Conductor [ 0 ];
        }
        else
        {
            this.tappers = new Conductor [ tappers.length ];
            System.arraycopy ( tappers, 0,
                               this.tappers, 0, tappers.length );
            // this.tappers can still contain nulls at this point!
        }

        // Start assembling our id:
        final StringBuilder id_sbuf = new StringBuilder ();
        id_sbuf.append ( "tap_" );
        boolean is_first_tapper = true;

        for ( int t = 0; t < this.tappers.length; t ++ )
        {
            if ( this.tappers [ t ] == null )
            {
                this.tappers [ t ] = Conductor.NONE;
            }
            else
            {
                hash_code += this.tappers [ t ].hashCode ();
            }

            if ( is_first_tapper )
            {
                id_sbuf.append ( "_" );
                is_first_tapper = false;
            }
            else
            {
                id_sbuf.append ( "_" );
            }
            id_sbuf.append ( "" + this.tappers [ t ].id () );
        }

        // Now finish assembling our id:
        this.id = id_sbuf.toString ();
        hash_code += this.id.hashCode ();

        // Once we've assembled our ID, we can create the route Tag
        // used to look up Tags during push / pull requests.
        this.routeTag =
            TagLibrary.musaico.tap.routeTag ( this,             // tap
                                              Tap.Route.NONE ); // route

        // We MUST set the hashCode before we insert ourselves
        // into this.routes hashmap!
        this.hashCode = hash_code;

        // Now that we have an id, create the tap wires:
        this.taps = new Wire [ 2 * this.tappers.length ];
        for ( int t = 0; t < this.tappers.length; t ++ )
        {
            final int t_out = 2 * t;
            final int t_in = t_out + 1;

            this.taps [ t_out ] = new StandardWire ( this, this.tappers [ t ] );
            this.taps [ t_in ] = new StandardWire ( this.tappers [ t ], this );

            // When we send a request out on this tap Wire, we tag it
            // so that when the request comes back in, we forward it
            // on to the next target Wire specified by this route.
            final Wire route;
            if ( t == 0 )
            {
                this.routes.put ( this,
                                  this.taps [ t_out ] );
                this.reverseRoutes.put ( this.taps [ t_in ],
                                         this.tappedWire );
            }
            else
            {
                this.routes.put ( this.taps [ t_out - 2 ],
                                  this.taps [ t_out ] );
                this.reverseRoutes.put ( this.taps [ t_in ],
                                         this.taps [ t_in - 2 ] );
            }

            if ( t == this.tappers.length - 1 )
            {
                // No more Taps -- route the request to the tapped wire.
                this.routes.put ( this.taps [ t_out ],
                                  this.tappedWire );
                this.reverseRoutes.put ( this,
                                         this.taps [ t_in ] );
            }
        }
    }

    /**
     * @see musaico.foundation.wiring.Wire#ends()
     */
    @Override
    public final Conductor [] ends ()
    {
        return this.tappedWire.ends ();
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

    protected final Tap.Route getCreateRoute (
            Pulse context,
            Pulse.Direction direction
            )
    {
        final Tag [] maybe_routing_tag =
            context.tags ().find ( this.routeTag );
        if ( maybe_routing_tag != null
             && maybe_routing_tag.length == 1
             && maybe_routing_tag [ 0 ] != null )
        {
            final Tag routing_tag = maybe_routing_tag [ 0 ];
            final Tap.Route route =
                routing_tag.data ( Tap.Route.class,  // as_type
                                   Tap.Route.NONE ); // default_value
            return route;
        }

        final Wire source_wire = context.sourceWire ();
        if ( source_wire == null )
        {
            return Tap.Route.NONE;
        }

        final Wire target_wire;
        if ( direction == context.direction () )
        {
            target_wire = this.routes.get ( source_wire );
        }
        else
        {
            target_wire = this.reverseRoutes.get ( source_wire );
        }

        if ( target_wire == null )
        {
            return Tap.Route.NONE;
        }

        final Conductor source_conductor = context.sourceConductor ();
        final Tap.Route route =
            new Tap.Route ( direction,        // direction
                            source_conductor, // source_tapped_wire_end
                            target_wire );    // next_routing_target
        return route;
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

        // Figure out the current route.
        final Tap.Route route = this.getCreateRoute ( context,
                                                      Pulse.Direction.PULL );
        if ( route == Tap.Route.NONE )
        {
            return new Carrier [ 0 ];
        }

        final Wire target_wire = route.nextRoutingTarget ();

        final Pulse next_context =
            context.next ( route.sourceTappedWireEnd (), // source_conductor
                           target_wire );                // source_wire

        final Tags tags = next_context.tags ();
        tags.remove ( this.routeTag );

        // Figure out the next route, when the request comes back to us.
        if ( target_wire != this.tappedWire )
        {
            final Tap.Route next_route =
                this.getCreateRoute ( next_context,
                                      route.direction () );
            final Tag next_route_tag =
                TagLibrary.musaico.tap.routeTag ( this,         // tap
                                                  next_route ); // route
            tags.insert ( next_route_tag );
        }

        // Now route the request.
        final Carrier [] pulled = target_wire.pull ( next_context );

        if ( pulled == null )
        {
            return new Carrier [ 0 ];
        }

        return pulled;
    }

    /**
     * @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Pulse, musaico.foundation.Carrier[])
     */
    @Override
    public final void push ( Pulse context, Carrier ... carriers )
    {
        if ( context == null
             || carriers == null
             || carriers.length == 0 )
        {
            return;
        }

        // Figure out the current route.
        final Tap.Route route = this.getCreateRoute ( context,
                                                      Pulse.Direction.PUSH );
        if ( route == Tap.Route.NONE )
        {
            return;
        }

        final Wire target_wire = route.nextRoutingTarget ();

        final Pulse next_context =
            context.next ( route.sourceTappedWireEnd (), // source_conductor
                           target_wire );                // source_wire

        final Tags tags = next_context.tags ();
        tags.remove ( this.routeTag );

        // Figure out the next route, when the request comes back to us.
        if ( target_wire != this.tappedWire )
        {
            // Overwrite the current routing tag with the next routing tag.
            final Tap.Route next_route =
                this.getCreateRoute ( next_context,
                                      route.direction () );
            final Tag next_route_tag =
                TagLibrary.musaico.tap.routeTag ( this,         // tap
                                                  next_route ); // route
            tags.insert ( next_route_tag );
        }

        // Now route the request.
        target_wire.push ( next_context, carriers );
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
     * @see musaico.foundation.wiring.Tap#tappedWire()
     */
    @Override
    public final Wire tappedWire ()
    {
        return this.tappedWire;
    }

    /**
     * @see musaico.foundation.wiring.Tap#tappers()
     */
    @Override
    public final Conductor [] tappers ()
    {
        final Conductor [] tappers = new Conductor [ this.tappers.length ];
        System.arraycopy ( this.tappers, 0,
                           tappers, 0, this.tappers.length );
        return tappers;
    }

    /**
     * @see musaico.foundation.wiring.Tap#taps()
     */
    @Override
    public final Wire [] taps ()
    {
        final Wire [] taps = new Wire [ this.taps.length ];
        System.arraycopy ( this.taps, 0,
                           taps, 0, this.taps.length );
        return taps;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return this.id ();
    }
}
