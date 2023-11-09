package musaico.foundation.wiring.components;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.Filter;

import musaico.foundation.filter.equality.EqualTo;

import musaico.foundation.filter.membership.InstanceOf;


import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Wire;


public class TapSelector
    extends StandardSelector
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Conductor tapper;

    public TapSelector (
            Conductor tapper
            )
    {
        super ( new EqualTo<Conductor> ( tapper ),
                new InstanceOf<Conductor> ( Wire.class ) );

        this.tapper = tapper;
    }
}
