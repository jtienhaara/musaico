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


public class StandardWire
    implements Wire, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // Immutable:
    private final String id;
    private final Conductor [] ends;
    private final Tags tags = new Tags ();

    private final int hashCode;

    public StandardWire (
            Conductor ... ends
            )
    {
        if ( ends == null
             || ends.length == 0 )
        {
            this.ends = new Conductor [ 0 ];
        }
        else
        {
            this.ends = new Conductor [ ends.length ];
            System.arraycopy ( ends, 0,
                               this.ends, 0, ends.length );
            for ( int e = 0; e < this.ends.length; e ++ )
            {
                if ( this.ends [ e ] == null )
                {
                    this.ends [ e ] = Conductor.NONE;
                }
            }
        }

        // Now assemble our id:
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "wire__" );
        boolean is_first = true;
        for ( Conductor end : this.ends )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( "__to__" );
            }

            sbuf.append ( "" + end.id () );
        }

        this.id = sbuf.toString ();

        this.hashCode = this.id.hashCode ();
    }

    /**
     * @see musaico.foundation.wiring.Wire#ends()
     */
    @Override
    public final Conductor [] ends ()
    {
        final Conductor [] ends = new Conductor [ this.ends.length ];
        System.arraycopy ( this.ends, 0,
                           ends, 0, this.ends.length );
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
        final Conductor sink_conductor = context.sourceConductor ();
        final Pulse next_context =
            context.next ( sink_conductor, // source_conductor
                           this );         // source_wire

        final Carriers pulled = new Carriers ();
        for ( Conductor other_end : this.ends () )
        {
            if ( other_end == sink_conductor
                 || other_end == sink_wire
                 || other_end == this )
            {
                continue;
            }

            final Carrier [] more_pulled = other_end.pull ( next_context );
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
        final Conductor source_conductor = context.sourceConductor ();
        final Pulse next_context =
            context.next ( source_conductor, // source_conductor
                           this );           // source_wire

        for ( Conductor other_end : this.ends () )
        {
            if ( other_end == source_conductor
                 || other_end == source_wire
                 || other_end == this )
            {
                continue;
            }

            other_end.push ( next_context, data );
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
        return this.id ();
    }
}
