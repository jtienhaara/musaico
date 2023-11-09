package musaico.foundation.wiring.tag;

import java.io.Serializable;

import musaico.foundation.wiring.Tag;


/**
 * <p>
 * A TerminalType describes the purpose of a terminal in a
 * Schematic: to be wired internally, or to expose connectivity
 * to the outside world, to allow external components to
 * interact with the component described by a Schematic via
 * its external-facing terminal.
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
public class TerminalType
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
    public static final String NAME = "musaico.terminal-type";

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
     * Terminal type: internal- or external-facing?
     * </p>
     *
     * <p>
     * Internal terminals are used within the schematic, for
     * things like wiring one component to another.
     * </p>
     *
     * <p>
     * External terminals expose entry / exit points for
     * parent schematics to hook into child schematics.
     * </p>
     */
    public static enum InternalOrExternal
    {
        /** An internal connection, not exposed outside
         *  the schematic. */
        INTERNAL,

        /** A connection which is exposed to allow external
         *  components to interact with a schematic via
         *  an external-facing connection terminal. */
        EXTERNAL;
    }


    /**
     * <p>
     * Creates a new TerminalType tag.
     * </p>
     *
     * @param internal_or_external Where the terminal is exposed:
     *                             to the EXTERNAL world, or only
     *                             INTERNAL.  If null, then INTERNAL
     *                             is the default.
     */
    public TerminalType (
            TerminalType.InternalOrExternal internal_or_external
            )
    {
        super ( TerminalType.NAME,           // name
                TerminalType.VERSION,        // version
                internal_or_external == null // value:
                    ? TerminalType.InternalOrExternal.INTERNAL
                    : internal_or_external );
    }
}
