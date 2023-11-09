package musaico.foundation.construct;

import java.io.Serializable;


/**
 * <p>
 * A branch in the construction of some output object, allowing the
 * caller to choose from 1 or more branching choice(s).
 * </p>
 *
 * <p>
 * Allows constructor overloading, and possibly other, unthought-of
 * or as-yet-unrecognized, uses.
 * </p>
 *
 * <p>
 * For example, a constructor of Time objects might allow the caller
 * to Choose between constructing a Time on a specific Date versus
 * constructing a Dateless Time object:
 * </p>
 *
 * <pre>
 *     final Choose<Time> time_constructor = ...;
 *     final Choice<Time, AbsoluteTime> absolute_time = ...;
 *     final Choice<Time, RelativeTime> relative_time = ...;
 *
 *     final AbsoluteTime date_and_time =
 *         time_constructor.choose ( absolute_time )
 *                         .apply ( 2019 ) // Year
 *                         .apply ( 03 )   // Month
 *                         .apply ( 18 )   // Day
 *                         .apply ( 18 )   // Hour
 *                         .apoly ( 32 )   // Minute
 *                         .build ();
 *     final RelativeTime just_time =
 *         time_constructor.choose ( relative_time )
 *                         .apply ( 18 )   // Hour
 *                         .apoly ( 32 )   // Minute
 *                         .build ();
 * </pre>
 *
 *
 * <p>
 * In Java every Construct must be Serializable in order to
 * play nicely across RMI.  However users of the Construct
 * must be careful, since it could contain non-Serializable elements.
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
 * @see musaico.foundation.construct.MODULE#COPYRIGHT
 * @see musaico.foundation.construct.MODULE#LICENSE
 */
public interface Choose<CONSTRUCTS extends Construct<OUTPUT>, OUTPUT extends Object>
    extends Construct<OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Continues constructing, choosing the next step
     * in the path according to the specified choice,
     * and returning either the final Constructed object, or
     * the next Constructor in the chain.
     * </p>
     *
     * @param choice The choice which determines the next stage
     *               of construction.  Must not be null.
     *
     * @return Either the next stage of Constructors or Choose branches,
     *         or the final, Constructed object.  Never null.
     */
    public abstract <CHOSEN_CONSTRUCT extends CONSTRUCTS, CHOICE extends Choice<CONSTRUCTS, CHOSEN_CONSTRUCT>>
        CHOSEN_CONSTRUCT choose (
            CHOICE choice
            )
        throws ConstructionException;


    !!! this is just like Constructor - totally pointless.
        ConstructionException
}
