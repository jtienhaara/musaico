package musaico.foundation.filter;

import java.io.Serializable;


/**
 * <p>
 * A Filter is used to keep or discard (filter out) objects.
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
 * @see musaico.foundation.filter.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.MODULE#LICENSE
 */
public interface Filter<GRAIN extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Returns a kept FilterState if the specified grain has
     * been kept by this Filter, or a discarded FilterState
     * if the specified grain has been filtered out.
     * </p>
     *
     * @param grain The object to filter.  Implementors
     *              of the Filter interface must be careful to deal
     *              defensively with null values.  Can be null.
     *
     * @return A kept FilterState if the object is kept;
     *         a discarded FilterState if the object is filtered out.
     */
    public abstract FilterState filter (
                                        GRAIN grain
                                        );
}
