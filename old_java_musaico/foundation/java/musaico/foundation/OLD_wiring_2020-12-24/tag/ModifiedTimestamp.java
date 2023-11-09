package musaico.foundation.wiring.tag;

import java.io.Serializable;

import java.util.Date;

import musaico.foundation.wiring.Tag;


/**
 * <p>
 * Timestamp describing when a Tagged object was most recently
 * modified.
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
public class ModifiedTimestamp
    extends Timestamp
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
    public static final String NAME = "musaico.timestamp.modified";

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
     * Creates a new ModifiedTimestamp Tag.
     * </p>
     *
     * @param milliseconds_since_utc_0 The absolute milliseconds since
     *                                 UTC 0 (January 1, 1970, GMT).
     */
    public ModifiedTimestamp (
            long milliseconds_since_utc_0
            )
    {
        super ( ModifiedTimestamp.NAME,      // name
                ModifiedTimestamp.VERSION,   // version
                milliseconds_since_utc_0 );  // milliseconds_since_utc_0
    }

    /**
     * <p>
     * Creates a new ModifiedTimestamp Tag.
     * </p>
     *
     * @param value The absolue time, as a Date object.  If null, then
     *              UTC 0 is used by default.
     */
    public ModifiedTimestamp (
            Date timestamp
            )
    {
        super ( ModifiedTimestamp.NAME,     // name
                ModifiedTimestamp.VERSION,  // version
                timestamp );                // timestamp
    }
}
