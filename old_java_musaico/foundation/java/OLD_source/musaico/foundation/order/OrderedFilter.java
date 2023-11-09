package musaico.foundation.order;

import java.io.Serializable;


import musaico.foundation.filter.Filter;


/**
 * <p>
 * A Filter which can be used to give hints as to where to search next,
 * by returning a Comparison from each filtered object.
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
 * @see musaico.foundation.order.MODULE#COPYRIGHT
 * @see musaico.foundation.order.MODULE#LICENSE
 */
public interface OrderedFilter<GRAIN extends Object>
    extends Filter<GRAIN>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Filters the specified object, returning a Comparison hint
     * so that the caller can choose to search before or after
     * the specified object, accordingly.
     * </p>
     *
     * <p>
     * Useful for binary searches and so on, which rely on ordered
     * data and ordered filters.
     * </p>
     *
     * @parm grain The object to filter.  Must not be null.
     *
     * @return The Comparison for the specified object, with the object
     *         as the "left" term and this filter as the "right" term.
     *         So a return value of Comparison.LEFT_LESS_THAN_RIGHT
     *         means the searcher should search higher values.
     *         A return value of Comparison.LEFT_GREATER_THAN_RIGHT
     *         means the searcher should search lower values.
     *         Never null.
     */
    @Override
    public abstract Comparison filter (
                                       GRAIN grain
                                       );
}
