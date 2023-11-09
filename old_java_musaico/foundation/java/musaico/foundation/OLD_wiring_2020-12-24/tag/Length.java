package musaico.foundation.wiring.tag;

import java.io.Serializable;

import musaico.foundation.wiring.Tag;


/**
 * <p>
 * A Tag specifying the number of Objects in a Carrier.
 * </p>
 *
 * <p>
 * In Java, every Tag must be Serializable in order to play
 * nicely over RMI.
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
 * @see musaico.foundation.wiring.tag.MODULE#COPYRIGHT
 * @see musaico.foundation.wiring.tag.MODULE#LICENSE
 */
public class Length
    extends Tag
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * The name, or key, for every Tag of this class.
     * </p>
     */
    public static final String NAME = "musaico.length";

    /**
     * <p>
     * The current version of the value, or data structure,
     * for each Tag of this class.  (Older versions of this
     * same Tag class might exist simultaneously in the same
     * runtime environment, with different value types.)
     * </p>
     */
    public static final int VERSION = 1;


    /**
     * <p>
     * Creates a new Length.
     * </p>
     *
     * @param length The number of objects.  If less than 0L,
     *               then 0L will be used by default.
     */
    public Length (
            long length
            )
    {
        super ( Length.NAME,    // name
                Length.VERSION, // version
                length < 0L         // value:
                    ? 0L
                    : length,
                Tag.FLAG_SHORT );   // flags
    }
}
