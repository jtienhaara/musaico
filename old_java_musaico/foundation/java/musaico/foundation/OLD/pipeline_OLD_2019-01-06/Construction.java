package musaico.foundation.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * The housing for the final constructed goal Object produced by
 * a Constructor, a Choose, a Pipeline, and so on.
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
public interface Construction<CONSTRUCTED extends Object>
    extends Construct<CONSTRUCTED>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The goal Object that was constructed by some sequence of
     *         Constructor(s), Choose(s), Pipeline(s), and so on.
     *         Never null.
     */
    public abstract CONSTRUCTED goal ()
        throws ReturnNeverNull.Violation;
}
