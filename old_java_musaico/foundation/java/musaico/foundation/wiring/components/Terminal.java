package musaico.foundation.wiring.components;

import java.io.Serializable;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Carriers;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Pulse;
import musaico.foundation.wiring.Tags;
import musaico.foundation.wiring.Wire;


public class Terminal
    implements Conductor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    public static final String NO_ID = "NO_ID";


    // Immutable:
    private final String id;
    private final Tags tags = new Tags ();

    private final int hashCode;

    public Terminal (
            String id
            )
    {
        if ( id == null )
        {
            this.id = Terminal.NO_ID;
        }
        else
        {
            this.id = id;
        }

        this.hashCode = this.toString ().hashCode ();
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

        final Wire sink_wire = context.sourceWire ();

        final Carriers pulled = new Carriers ();
        for ( Wire wire : context.board ().wiresFrom ( this ) )
        {
            if ( wire == sink_wire )
            {
                continue;
            }

            final Pulse next_context =
                context.next ( this,   // source_conductor
                               wire ); // source_wire

            final Carrier [] more_pulled = wire.pull ( next_context );
            pulled.add ( more_pulled );
        }

        final Carrier [] data = pulled.toArray ();
        return data;
    }

    /**
     * @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Pulse, musaico.foundation.Carrier[])
     */
    @Override
    public final void push ( Pulse context, Carrier ... data )
    {
        if ( context == null
             || data == null
             || data.length == 0 )
        {
            return;
        }

        final Wire source_wire = context.sourceWire ();

        for ( Wire wire : context.board ().wiresFrom ( this ) )
        {
            if ( wire == source_wire )
            {
                continue;
            }

            final Pulse next_context =
                context.next ( this,   // source_conductor
                               wire ); // source_wire

            wire.push ( next_context, data );
        }
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
        return "terminal [ " + this.id () + " ]";
    }
}
