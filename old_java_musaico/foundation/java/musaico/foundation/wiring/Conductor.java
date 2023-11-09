package musaico.foundation.wiring;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;


public interface Conductor
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public static final Conductor NONE = new NoConductor ();


    public abstract String id ()
        throws Return.NeverNull.Violation;

    public abstract Carrier [] pull ( Pulse context );

    public abstract void push ( Pulse context, Carrier ... carriers );

    public abstract Tags tags ()
        throws Return.NeverNull.Violation;
}
