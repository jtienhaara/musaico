package musaico.foundation.wiring;

import java.io.Serializable;


import musaico.foundation.contract.obligations.EveryParameter;


/**
 * <p>
 * Builtin Tags that are used by the Musaico wiring library.
 * </p>
 *
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
 * @see musaico.foundation.wiring.MODULE#COPYRIGHT
 * @see musaico.foundation.wiring.MODULE#LICENSE
 */
public class TagLibrary
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    public static final TagLibrary.MusaicoNamespace musaico =
        new TagLibrary.MusaicoNamespace ();


    /**
     * <p>
     * The "musaico" namespace, containing builtin Tags used by
     * the Musaico wiring library.
     * </p>
     */
    public static class MusaicoNamespace
        extends TagNamespace
        implements Serializable
    {
        private static final long serialVersionUID = TagLibrary.serialVersionUID;

        private MusaicoNamespace ()
        {
            super ( TagNamespace.ROOT, "musaico" );
        }

        public final TapNamespace tap =
            new TagLibrary.MusaicoNamespace.TapNamespace ( this );

        /**
         * <p>
         * The "musaico.tap" namespace, containing Tags used by
         * wire Taps to route push and pull requests.
         * </p>
         */
        public static class TapNamespace
            extends TagNamespace
                    implements Serializable
        {
            private static final long serialVersionUID = TagLibrary.serialVersionUID;

            private TapNamespace (
                    TagLibrary.MusaicoNamespace parent_namespace
                    )
            {
                super ( parent_namespace, "tap" );
            }

            /**
             * <p>
             * A "musaico.tap.(id)" Tag containing details about how
             * to route a push / pull request.
             * </p>
             *
             * @param tap The wire Tap whose request route is to be
             *            captured in a Tag.  Must not be null.
             *
             * @param route The Tap.Route.  Can be Tap.Route.NONE.
             *              Must not be null.
             */
            public final Tag routeTag (
                    Tap tap,
                    Tap.Route route
                    )
                throws EveryParameter.MustNotBeNull.Violation
            {
                if ( tap == null
                     || route == null )
                {
                    throw EveryParameter.MustNotBeNull.CONTRACT.violation (
                        this,      // plaintiff
                        new Object [] { tap, route }
                        );
                }

                return new Tag (
                    this,            // namespace (path = "musaico.tap")
                    tap.id (),       // id (path = "musaico.tap.(id)")
                    Tap.Route.class, // data_type
                    route            // data
                    );
            }
        };
    };

    // Never instantiate TagLibrary.  It's a static utility class.
    private TagLibrary ()
    {
    }
}
