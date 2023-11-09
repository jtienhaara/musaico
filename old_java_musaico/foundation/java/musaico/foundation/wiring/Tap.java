package musaico.foundation.wiring;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;


public interface Tap
    extends Wire, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public static final Tap NONE = new NoTap ();


    /**
     * <p>
     * The routing Tag helps a Tap figure out where to send
     * each pull / push request next.
     * </p>
     *
     * <p>
     * For example, if a pull request is sent to a tapped Wire,
     * the Tap adds a <code> TagLibrary.musaico.tap.routeTag () </code>
     * to the request context and forwards it to the first
     * tap Conductor.  When the tap Conductor sends the pull request
     * back to the Tap, the Tap examines the tag containing the
     * Tap.Route, replaces it in the new pull context, and forwards
     * it to the second tap Conductor.  And so on, until the last
     * tap Conductor forwards the request back to the Tap,
     * and the Tap removes the route Tag and forwards the
     * pull request to all the other ends of the tapped Wire.
     * When it receives the responses from all the tapped Wires,
     * it returns those responses to the last tap Conductor,
     * and so on, until returning the response to the tapped Wire end
     * from which the request originally came.
     * </p>
     *
     * <p>
     * (Of course a tap Conductor might choose to return an
     * empty pull response -- for example, in order to queue
     * up the pull request for later, then push the response
     * when it's ready -- in which case the Tap must
     * recognise that a Tap.Route flagged as a "pull"
     * tied to a "push" context means a batched / delayed
     * response, and must send the response backward through
     * the chain of tap Conductors to the original source
     * tapped Wire end.)
     * </p>
     */
    public static class Route
        implements Serializable
    {
        // The version of the parent module, YYYYMMDD format.
        private static final long serialVersionUID = MODULE.VERSION;

        /** No route at all.  Used, for example, as the data for a
         *  <code> TagLibrary.musaico.tap.routeTag () </code>
         *  to lookup the route Tag in the context for a push
         *  or pull request. */
        public static final Tap.Route NONE =
            new Tap.Route ( Pulse.Direction.PUSH, // direction
                            Conductor.NONE,       // source_tapped_wire_end
                            Wire.NONE );          // next_routing_target


        private final Pulse.Direction direction;
        private final Conductor sourceTappedWireEnd;
        private final Wire nextRoutingTarget;

        private final int hashCode;

        /**
         * <p>
         * Creates a new Tap.Route.
         * </p>
         *
         * @param direction Either PUSH for data being sent through
         *                  the wire Tap, or PULL for data being
         *                  requested through the wire Tap.
         *                  If a PULL Tap.Route is attached
         *                  to a PUSH context (Pulse), then one
         *                  of the tap Conductors has delayed response
         *                  to a pull request, and is now asynchronously
         *                  sending the response to the original
         *                  tapped Wire end.  Must not be null.
         *
         * @param source_tapped_wire_end The original source of
         *                               the push or pull request.
         *                               Must be an end of the
         *                               Tap's tapped Wire.
         *                               Must not be null.
         *
         * @param next_routing_target The next Wire in the chain to
         *                            route the request to: either
         *                            a Wire leading to a tap Conductor,
         *                            or, as the final step in the
         *                            route, the tapped Wire itself.
         *                            Must be either a tap Wire or
         *                            the tapped Wire in the specified
         *                            Tap.  Must not be null.
         */
        public Route (
                Pulse.Direction direction,
                Conductor source_tapped_wire_end,
                Wire next_routing_target
                )
            throws EveryParameter.MustNotBeNull.Violation
        {
            if ( direction == null
                 || source_tapped_wire_end == null
                 || next_routing_target == null )
            {
                throw EveryParameter.MustNotBeNull.CONTRACT.violation (
                    this,      // plaintiff
                    new Object [] { direction, source_tapped_wire_end, next_routing_target }
                    );
            }

            this.direction = direction;
            this.sourceTappedWireEnd = source_tapped_wire_end;
            this.nextRoutingTarget = next_routing_target;

            this.hashCode =
                this.direction.hashCode ()
                + this.sourceTappedWireEnd.hashCode ()
                + this.nextRoutingTarget.hashCode ();
        }

        public final Pulse.Direction direction ()
            throws Return.NeverNull.Violation
        {
            return this.direction;
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
            else if ( object == null )
            {
                return false;
            }
            else if ( object.getClass () != this.getClass () )
            {
                return false;
            }

            final Tap.Route that = (Tap.Route) object;
            if ( this.direction == null )
            {
                if ( that.direction != null )
                {
                    return false;
                }
            }
            else if ( that.direction == null )
            {
                return false;
            }
            else if ( ! this.direction.equals ( that.direction ) )
            {
                return false;
            }

            if ( this.sourceTappedWireEnd == null )
            {
                if ( that.sourceTappedWireEnd != null )
                {
                    return false;
                }
            }
            else if ( that.sourceTappedWireEnd == null )
            {
                return false;
            }
            else if ( ! this.sourceTappedWireEnd.equals ( that.sourceTappedWireEnd ) )
            {
                return false;
            }

            if ( this.nextRoutingTarget == null )
            {
                if ( that.nextRoutingTarget != null )
                {
                    return false;
                }
            }
            else if ( that.nextRoutingTarget == null )
            {
                return false;
            }
            else if ( ! this.nextRoutingTarget.equals ( that.nextRoutingTarget ) )
            {
                return false;
            }

            return true;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public final int hashCode ()
        {
            return this.hashCode;
        }

        public final Wire nextRoutingTarget ()
        {
            return this.nextRoutingTarget;
        }

        public final Conductor sourceTappedWireEnd ()
        {
            return this.sourceTappedWireEnd;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            final StringBuilder sbuf = new StringBuilder ();
            sbuf.append ( this.nextRoutingTarget.id () );
            if ( this.direction == Pulse.Direction.PULL )
            {
                sbuf.append ( ".pull" );
            }
            else if ( this.direction == Pulse.Direction.PUSH )
            {
                sbuf.append ( ".push" );
            }
            else
            {
                sbuf.append ( "." + this.direction );
            }

            sbuf.append ( " [ source = " );
            sbuf.append ( this.sourceTappedWireEnd.id () );
            sbuf.append ( " ]" );

            final String as_string = sbuf.toString ();
            return as_string;
        }
    }

    // Every Tap must implement:
    // @see musaico.foundation.wiring.Wire#ends()

    // Every Tap must implement:
    // @see musaico.foundation.wiring.Conductor#id()

    // Every Tap must implement:
    // @see musaico.foundation.wiring.Conductor#pull(musaico.foundation.wiring.Pulse)

    // Every Tap must implement:
    // @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Pulse, musaico.foundation.Carrier[])

    // Every Tap must implement:
    // @see musaico.foundation.wiring.Conductor#tags()

    public abstract Wire tappedWire ();

    public abstract Conductor [] tappers ();

    public abstract Wire [] taps ();
}
