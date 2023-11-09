package musaico.foundation.construct;

import java.io.Serializable;


/**
 * <p>
 * An enumerated choice between possible subsequent Constructors
 * and/or Choose branches and/or final Constructed outputs,
 * determining the branch path down which construction
 * will continue.
 * </p>
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
public interface Choice<CONSTRUCTS extends Construct<OUTPUT>, CHOSEN_CONSTRUCT extends CONSTRUCTS, OUTPUT extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Creates or returns the next construct, such as another
     * Constructor or Choose step, or the final, Constructed output,
     * given this choice of paths for the specified Choose step.
     * </p>
     *
     * @param choose The Choose branching step which allowed one or more
     *               choices, of which this is the one chosen.
     *               Must not be null.
     *
     * @return The Construct from this choice at the specified step:
     *         either another Constructor or Choose step, or the final,
     *         Constructed output.  Never null.
     */
    public abstract CHOSEN_CONSTRUCT accept (
            Choose<CONSTRUCTS> choose
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
