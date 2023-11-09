package musaico.foundation.wiring;

import java.io.Serializable;

import musaico.foundation.container.ImmutableContainer;

import musaico.foundation.contract.guarantees.Return;


/**
 * <p>
 * An immutable, empty Carrier of data between Conductors.
 * </p>
 *
 *
 * <p>
 * In Java, every Carrier must be Serializable in order to play
 * nicely over RMI.  However, be warned that the elements contained
 * in a Carrier might not be Serializable.
 * </p>
 *
 *
 * <br> </br>
 * <br> </br>
 *
 * <hr> </hr>
 *
 * <br> </br>
 * <br> </br>
 *
 *
 * <p>
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.foundation.container.MODULE#COPYRIGHT
 * @see musaico.foundation.container.MODULE#LICENSE
 */
public class NoCarrier
    extends ImmutableContainer<Object>
    implements Carrier, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // Protected constructor.
    // Use Carrier.NONE instead.
    protected NoCarrier ()
    {
        super ( Object.class ); // No elements.
    }

    /**
     * @see musaico.foundation.wiring.Carrier#tags()
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
        return "carrier.none";
    }
}
