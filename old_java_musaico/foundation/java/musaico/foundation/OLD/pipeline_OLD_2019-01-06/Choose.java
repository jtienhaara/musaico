package musaico.foundation.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * An object which can build a new Pipeline by choosing from 1 or more
 * branching choice(s).
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
 * @see musaico.foundation.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.pipeline.MODULE#LICENSE
 */
public interface Choose<CONSTRUCTS extends Construct<CONSTRUCTS>>
    extends Construct<CONSTRUCTS>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Continues constructing the Pipeline, choosing the next step
     * in the path according to the specified choice.
     * </p>
     *
     * @param choice The choice which determines the next stage
     *               of construction.  Must not be null.
     *
     * @return Either the next stage of Constructors or Choose branches,
     *         or the final, constructed Pipeline.  Never null.
     */
    public abstract <CHOSEN_CONSTRUCT extends CONSTRUCTS, CHOICE extends Choice<CONSTRUCTS, CHOSEN_CONSTRUCT>>
        CHOSEN_CONSTRUCT choose (
            CHOICE choice
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
