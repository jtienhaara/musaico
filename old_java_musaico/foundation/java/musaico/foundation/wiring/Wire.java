package musaico.foundation.wiring;

import java.io.Serializable;


public interface Wire
    extends Conductor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public static final Wire NONE = new NoWire ();


    public abstract Conductor [] ends ();

    // Every Wire must implement:
    // @see musaico.foundation.wiring.Conductor#id()

    // Every Wire must implement:
    // @see musaico.foundation.wiring.Conductor#pull(musaico.foundation.wiring.Pulse)

    // Every Wire must implement:
    // @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Pulse, musaico.foundation.Carrier[])

    // Every Wire must implement:
    // @see musaico.foundation.wiring.Conductor#tags()
}
