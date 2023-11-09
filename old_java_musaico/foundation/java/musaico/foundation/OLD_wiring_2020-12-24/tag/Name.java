package musaico.foundation.wiring.tag;

import java.io.Serializable;

import musaico.foundation.wiring.Tag;


/**
 * <p>
 * The name of a Tagged object (not to be confused with
 * its generated ID tag).
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
public class Name
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
    public static final String NAME = Tag.NAME_TAG_NAME;

    /**
     * <p>
     * The current version of the value, or data structure,
     * for each Tag of this class.  (Older versions of this
     * same Tag class might exist simultaneously in the same
     * runtime environment, with different value types.)
     * </p>
     */
    public static final int VERSION = 1;

    /** No name. */
    public static final String NONE = "NO-NAME";


    /**
     * <p>
     * Creates a new Name Tag.
     * </p>
     *
     * @param name The name of the Tagged object.  If null, then Name.NONE
     *             is used by default.
     */
    public Name (
            String name
            )
    {
        super ( Name.NAME,           // name
                Name.VERSION,        // version
                name == null         // value:
                    ? Name.NONE
                    : name,
                Tag.FLAG_SHORT );    // flags
    }
}
