package musaico.foundation.wiring.components;

import java.io.Serializable;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.structure.StringRepresentation;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Carriers;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.NoCarrier;
import musaico.foundation.wiring.Pulse;
import musaico.foundation.wiring.Tags;
import musaico.foundation.wiring.Wire;


public abstract class AbstractInOut
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
    private Carrier [] carriers = Carrier.NO_CARRIERS;

    @SuppressWarnings("varargs") // heap pollution generic varargs.
    @SafeVarargs // Possible heap pollution DATA ...
    public <DATA extends Object> AbstractInOut (
            String id,
            Class<DATA> data_type,
            DATA ... data
            )
    {
        this ( id, // id
               ( data_type == null || data == null )
                   ? null
                   : new StandardCarrier ( data_type,
                                           data ) );
    }

    public AbstractInOut (
            String id,
            Carrier ... carriers
            )
    {
        if ( id == null )
        {
            this.id = "NO_ID";
        }
        else
        {
            this.id = id;
        }

        if ( carriers != null
             && carriers.length != 0 )
        {
            this.in ( carriers );
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
     * @return The current in sequence, or <code> Carrier.NO_CARRIERS </cpde>
     *         if there is no current in sequence.  Never null.
     *         Never contains any null elements.
     */
    protected final Carrier [] in ()
    {
        final Carrier [] carriers;
        synchronized ( this.lock )
        {
            carriers = new Carrier [ this.carriers.length ];
            System.arraycopy ( this.carriers, 0,
                               carriers, 0, this.carriers.length );
        }

        return carriers;
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
     * <p>
     * Appends the specified data Carriers to the end of the in sequence.
     * </p>
     *
     * @return The new in sequence, or <code> Carrier.NO_CARRIERS </cpde>
     *         if there is no in sequence.  Never null.
     *         Never contains any null elements.
     */
    protected final Carrier [] in (
            Carrier ... carriers
            )
    {
        final Carrier [] new_carriers;
        synchronized ( this.lock )
        {
            new_carriers =
                new Carriers ( this.carriers )
                .add ( carriers )
                .toArray ();
            this.carriers = new_carriers;
        }

        return new_carriers;
    }

    protected abstract Carrier [] out (
            Carrier [] in_carriers
            );

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

        final Carrier [] out_carriers;
        synchronized ( this.lock )
        {
            final Carriers carriers =
                new Carriers ( this.carriers );
            final Carrier [] in_carriers =
                carriers.toArray ();
            out_carriers = this.out ( in_carriers );
            carriers.remove ( out_carriers );
        }

        return out_carriers;
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

        this.in ( data );
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
