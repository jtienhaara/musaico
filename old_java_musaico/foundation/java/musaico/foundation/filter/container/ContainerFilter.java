package musaico.foundation.filter.container;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A Filter for arrays, Collections, Strings, Iterables,
 * and singletons, which keeps or discards each whole
 * array/Collection/Iterable/singleton.
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
 * @see musaico.foundation.filter.container.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.container.MODULE#LICENSE
 */
public interface ContainerFilter<ELEMENT extends Object>
    extends Filter<Object>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Filters an array.
     * </p>
     *
     * @param container The container array to keep or discard.
     *                  Can be null.
     *
     * @return The overall FilterState (as per Filter.filter ( ... )).
     *         Never null.
     *
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    public abstract FilterState filterArray (
            ELEMENT [] container
            );


    /**
     * <p>
     * Filters a Collection.
     * </p>
     *
     * @param container The container Collection to keep or discard.
     *                  Can be null.
     *
     * @return The overall FilterState (as per Filter.filter ( ... )).
     *         Never null.
     *
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    public abstract FilterState filterCollection (
            Collection<ELEMENT> container
            );


    /**
     * <p>
     * Filters an Iterable.
     * </p>
     *
     * @param container The container Iterable to keep or discard.
     *                  Can be null.
     *
     * @return The overall FilterState (as per Filter.filter ( ... )).
     *         Never null.
     *
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    public abstract FilterState filterIterable (
            Iterable<ELEMENT> container
            );


    /**
     * <p>
     * Filters a single object.
     * </p>
     *
     * @param container The single object to keep or discard.
     *                  Can be null.
     *
     * @return The overall FilterState (as per Filter.filter ( ... )).
     *         Never null.
     *
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    public abstract FilterState filterSingleton (
            ELEMENT container
            );
}
