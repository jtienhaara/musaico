package musaico.foundation.pipeline;

import java.io.Serializable;


/**
 * <p>
 * Either a Constructor of some sort to build a goal Object, or a Choose
 * branching decision to build a goal Object, or a Construction carrying
 * the final goal Object itself, or possibly some other type of Construct
 * used to build a goal Object (such as a Pipeline, for potentially
 * more complicated builds).
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
public interface Construct<GOAL extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
