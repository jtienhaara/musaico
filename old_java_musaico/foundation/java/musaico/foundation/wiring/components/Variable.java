package musaico.foundation.wiring.components;

import java.io.Serializable;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Carriers;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.NoCarrier;
import musaico.foundation.wiring.Pulse;
import musaico.foundation.wiring.Tags;
import musaico.foundation.wiring.Wire;


public class Variable
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
    public <DATA extends Object> Variable (
            String id,
            Class<DATA> data_type,
            DATA ... data
            )
    {
        this ( id,
               ( data_type == null || data == null )
                   ? null
                   : new StandardCarrier ( data_type,
                                           data ) );
    }

    public Variable (
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
            this.setCarriers ( carriers );
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
     * @return The current variable value, or <code> Carrier.NO_CARRIERS </cpde>
     *         if there is no current value.  Never null.
     *         Never contains any null elements.
     */
    protected final Carrier [] getCarriers ()
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
     *
     * <p>
     * Can be overridden.
     * </p>
     */
    @Override
    public Carrier [] pull ( Pulse context )
    {
        if ( context == null )
        {
            return new Carrier [ 0 ];
        }

        final Carrier [] pulled = this.getCarriers ();

        return pulled;
    }

    /**
     * @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Pulse, musaico.foundation.Carrier[])
     *
     * <p>
     * Can be overridden.
     * </p>
     */
    @Override
    public void push ( Pulse context, Carrier ... data )
    {
        if ( context == null
             || data == null )
        {
            // Do nothing.
            return;
        }

        this.setCarriers ( data );
    }

    /**
     * <p>
     * Sets the variable value to the specified data Carriers.
     * </p>
     *
     * @return The previous value, or <code> Carrier.NO_CARRIERS </cpde>
     *         if there was no previous value.  Never null.
     *         Never contains any null elements.
     */
    protected final Carrier [] setCarriers (
            Carrier ... carriers
            )
    {
        final Carrier [] previous_carriers;
        synchronized ( this.lock )
        {
            if ( this.carriers == null )
            {
                previous_carriers = Carrier.NO_CARRIERS;
            }
            else
            {
                previous_carriers = this.carriers;
            }

            if ( carriers == null )
            {
                this.carriers = Carrier.NO_CARRIERS;
            }
            else
            {
                this.carriers = new Carrier [ carriers.length ];
                System.arraycopy ( carriers, 0,
                                   this.carriers, 0, carriers.length );
                for ( int c = 0; c < this.carriers.length; c ++ )
                {
                    if ( this.carriers [ c ] == null )
                    {
                        this.carriers [ c ] = Carrier.NONE;
                    }
                }
            }
        }

        return previous_carriers;
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
