package musaico.foundation.construct;

import java.io.Serializable;


import musaico.foundation.filter.Filter;


/**
 * <p>
 * Takes a parameter and builds either a final, Constructed object, or
 * the next Constructor in an arbitrarily long sequence to the eventual
 * Constructed object.
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
public interface Constructor<PARAMETER extends Object, CONSTRUCT extends Construct<OUTPUT>, OUTPUT extends Object>
    extends Construct<OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Builds either the final, Constructed object, or the next link in the
     * sequence of Constructors.
     * </p>
     *
     * @param parameter The parameter which determines the next stage
     *                  of construction.  Must be kept by this Constructor's
     *                  <code> contract () </code>, or else
     *                  a ConstructionException will be thrown.
     *                  Must not be null.
     *
     * @return Either the next stage of Constructors or branching Choose steps,
     *         or the final, Constructed object.  Never null.
     *
     * @throws ConstructionException If the specified parameter is not KEPT
     *                               by the <code> parameterFilter () </code>.
     */
    public abstract CONSTRUCT apply (
            PARAMETER parameter
            )
        throws ConstructionException;
    

    /**
     * @return The Contract governing permissable value(s) for this
     *         Constructor's parameter.  Never null.
     */
    public abstract Filter<PARAMETER> parameterFilter ();
}
