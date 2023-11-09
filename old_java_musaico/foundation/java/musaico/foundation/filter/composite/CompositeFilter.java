package musaico.foundation.filter.composite;

import java.io.Serializable;


import musaico.foundation.filter.Filter;


/**
 * <p>
 * A filter made up of child filters, such as a logical and/or/xor/and so
 * on filter.
 * </p>
 *
 *
 * <p>
 * In Java, every Filter must be Serializable in order to play
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
 * @see musaico.foundation.filter.composite.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.composite.MODULE#LICENSE
 */
public interface CompositeFilter<GRAIN extends Object>
    extends Filter<GRAIN>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The child filters which comprise this composite.
     *         Never null.  Never contains any null elements.
     */
    public abstract Filter<GRAIN> [] filters ();
}
