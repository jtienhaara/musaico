package musaico.foundation.wiring.components;

import java.io.Serializable;

import java.util.LinkedHashSet;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Carriers;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Pulse;
import musaico.foundation.wiring.Tags;
import musaico.foundation.wiring.Wire;


public class Cable
    implements Conductor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // Immutable:
    private final String id;
    private final Tags tags = new Tags ();

    // Mutable:
    private final Serializable lock = new String ( "lock" );
    private final LinkedHashSet<Board> ends;

    public Cable (
            Board ... ends
            )
    {
        this.ends = new LinkedHashSet<Board> ();
        if ( ends != null )
        {
            for ( Board end : ends )
            {
                if ( end == null )
                {
                    this.ends.add ( Board.NONE );
                }
                else
                {
                    this.ends.add ( end );
                }
            }
        }

        // Assemble our id (no need for locking in constructor):
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "cable [" );

        boolean is_first = true;
        for ( Board end : this.ends )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( " -" );
            }

            sbuf.append ( " " + end );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }
        sbuf.append ( "]" );

        this.id = sbuf.toString ();
    }

    /**
     * <p>
     * Connects this Cable to the specified Board.
     * </p>
     *
     * @param boards The Board(s) to connect this cable to.
     *
     * @return This Cable.  Never null.
     */
    public final Cable connect (
            Board ... boards
            )
    {
        if ( boards == null
             || boards.length == 0 )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            for ( Board board : boards )
            {
                if ( board == null )
                {
                    this.ends.add ( Board.NONE );
                }
                else
                {
                    this.ends.add ( board );
                }
            }
        }

        return this;
    }

    /**
     * <p>
     * disconnects this Cable from the specified Board.
     * </p>
     *
     * @param boards The Board(s) to disconnect this cable from.
     *
     * @return This Cable.  Never null.
     */
    public final Cable disconnect (
            Board ... boards
            )
    {
        if ( boards == null
             || boards.length == 0 )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            for ( Board board : boards )
            {
                if ( board == null )
                {
                    this.ends.remove ( Board.NONE );
                }
                else
                {
                    this.ends.remove ( board );
                }
            }
        }

        return this;
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

        final LinkedHashSet<Board> ends;
        synchronized ( this.lock )
        {
            if ( ! this.ends.contains ( context.board () ) )
            {
                return new Carrier [ 0 ];
            }

            ends = new LinkedHashSet<Board> ( this.ends );
        }

        final Carriers pulled = new Carriers ();
        for ( Board other_end : ends )
        {
            if ( other_end.equals ( context.board () ) )
            {
                continue;
            }

            final Pulse next_context =
                context.next ( other_end,   // board
                               this,        // source_conductor
                               Wire.NONE ); // source_wire

            for ( Wire wire : other_end.wiresFrom ( this ) )
            {
                if ( wire == null )
                {
                    continue;
                }

                final Carrier [] more_pulled = wire.pull ( next_context );
                pulled.add ( more_pulled );
            }
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

        final LinkedHashSet<Board> ends;
        synchronized ( this.lock )
        {
            if ( ! this.ends.contains ( context.board () ) )
            {
                return;
            }

            ends = new LinkedHashSet<Board> ( this.ends );
        }

        for ( Board other_end : ends )
        {
            if ( other_end.equals ( context.board () ) )
            {
                continue;
            }

            final Pulse next_context =
                context.next ( other_end,   // board
                               this,        // source_conductor
                               Wire.NONE ); // source_wire

            for ( Wire wire : other_end.wiresFrom ( this ) )
            {
                if ( wire == null )
                {
                    continue;
                }

                wire.push ( next_context, data );
            }
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
