package musaico.foundation.wiring.tag;

import java.io.Serializable;

import java.util.UUID;


import musaico.foundation.wiring.Tag;


/**
 * <p>
 * A unique identifier for a tagged object.
 * </p>
 *
 * <p>
 * Not to be confused with a Name tag, which need not be unique.
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
public class ID
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
    public static final String NAME = Tag.ID_TAG_NAME;

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
     * Creates a new ID.
     * </p>
     *
     * @param uuid The unique identifier.  If null, then a new
     *             type 4 (random) UUID is generated.
     */
    public ID (
            UUID uuid
            )
    {
        super ( ID.NAME,           // name
                ID.VERSION,        // version
                uuid == null       // value:
                    ? UUID.randomUUID ()
                    : uuid,
                Tag.FLAG_SHORT );  // flags
    }
}
