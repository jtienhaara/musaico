package musaico.foundation.wiring.tag;

import java.io.Serializable;

import java.util.Date;

import musaico.foundation.wiring.Tag;


/**
 * <p>
 * A Tag that stores absolute time, in milliseconds since UTC 0.
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
public class Timestamp
    extends Tag
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


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
     * Creates a new Timestamp Tag with version <code> Timestamp.VERSION </code>.
     * </p>
     *
     * @param name The name of this Tag.  If null, then Tag.NONE.name ()
     *             will be used by default, and this Tag will be made
     *             identical to Tag.NONE in every way, regardless of
     *             the other values.
     *
     * @param milliseconds_since_utc_0 The absolute milliseconds since
     *                                 UTC 0 (January 1, 1970, GMT).
     */
    public Timestamp (
            String name,
            long milliseconds_since_utc_0
            )
    {
        this ( name,                       // name
               // Javac refuses to accept Timestamp.VERSION and I can't figure out why, so reverting to:
               VERSION,            // version
               milliseconds_since_utc_0 ); // value
    }

    /**
     * <p>
     * Creates a new Timestamp Tag.
     * </p>
     *
     * @param name The name of this Tag.  If null, then Tag.NONE.name ()
     *             will be used by default, and this Tag will be made
     *             identical to Tag.NONE in every way, regardless of
     *             the other values.
     *
     * @param version The version number of this Tag.  Use Timestamp.VERSION
     *                if you don't care about the underlying structure
     *                of the value.
     *
     * @param milliseconds_since_utc_0 The absolute milliseconds since
     *                                 UTC 0 (January 1, 1970, GMT).
     */
    public Timestamp (
            String name,
            int version,
            long milliseconds_since_utc_0
            )
    {
        super ( name == null           // name:
                    ? Tag.NONE.name ()
                    : name,
                name == null           // version:
                    ? Tag.NONE.version ()
                    : version,
                name == null           // value:
                    ? Tag.NONE.value ()
                    : milliseconds_since_utc_0 );
    }

    /**
     * <p>
     * Creates a new Timestamp Tag with version <code> Timestamp.VERSION </code>.
     * </p>
     *
     * @param name The name of this Tag.  If null, then Tag.NONE.name ()
     *             will be used by default, and this Tag will be made
     *             identical to Tag.NONE in every way, regardless of
     *             the other values.
     *
     * @param timestamp The absolue time, as a Date object.
     *                  If null, then UTC 0 is used by default.
     */
    public Timestamp (
            String name,
            Date timestamp
            )
    {
        this ( name,            // name
               // Javac refuses to accept Timestamp.VERSION and I can't figure out why, so reverting to:
               VERSION, // version
               timestamp );     // value
    }

    /**
     * <p>
     * Creates a new Timestamp Tag.
     * </p>
     *
     * @param name The name of this Tag.  If null, then Tag.NONE.name ()
     *             will be used by default, and this Tag will be made
     *             identical to Tag.NONE in every way, regardless of
     *             the other values.
     *
     * @param version The version number of this Tag.  Use Timestamp.VERSION
     *                if you don't care about the underlying structure
     *                of the value.
     *
     * @param timestamp The absolue time, as a Date object.
     *                  If null, then UTC 0 is used by default.
     */
    public Timestamp (
            String name,
            int version,
            Date timestamp
            )
    {
        this ( name,              // name
               version,           // version
               timestamp == null  // value:
                   ? 0L
                   : timestamp.getTime () );
    }
}
