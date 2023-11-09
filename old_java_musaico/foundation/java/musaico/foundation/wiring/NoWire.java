package musaico.foundation.wiring;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;


public class NoWire
    implements Wire, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // Protected constructor.
    // Use Wire.NONE instead.
    protected NoWire ()
    {
    }

    /**
     * @see musaico.foundation.wiring.Wire#ends()
     */
    @Override
    public final Conductor [] ends ()
    {
        return new Conductor [ 0 ];
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
        return 0;
    }

    /**
     * @see musaico.foundation.wiring.Conductor#id()
     */
    @Override
    public final String id ()
    {
        return "wire_none";
    }

    /**
     * @see musaico.foundation.wiring.Conductor#pull(musaico.foundation.wiring.Pulse)
     */
    @Override
    public final Carrier [] pull ( Pulse context )
    {
        // Do nothing.
        return new Carrier [ 0 ];
    }

    /**
     * @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Pulse, musaico.foundation.Carrier[])
     */
    @Override
    public final void push ( Pulse context, Carrier ... carriers )
    {
        // Do nothing.
    }

    /**
     * @see musaico.foundation.wiring.Conductor#tags()
     */
    @Override
    public final Tags tags ()
        throws Return.NeverNull.Violation
    {
        return Tags.NONE;
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
