package musaico.foundation.metadata;

import java.io.Serializable;


/**
 * <p>
 * Tracks statistics for an object, such as the number of active
 * references to it, the number of errors and warnings its methods
 * have generated, and so on.
 * </p>
 *
 * <p>
 * Call increment () to add one to a particular statistic for the object.
 * </p>
 *
 * <p>
 * Call decrement () to subtract one from a particular statistic.
 * </p>
 *
 * <p>
 * Call count () to return the statistic for the object.
 * </p>
 *
 *
 * <p>
 * In Java, every metadatum must be Serializable in order
 * to play nicely over RMI.
 * </p>
 *
 * <p>
 * In Java, every metadatum must implement equals(Object), hashCode()
 * and toString().
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
 * @see musaico.foundation.metadata.MODULE#COPYRIGHT
 * @see musaico.foundation.metadata.MODULE#LICENSE
 */
public interface Statistics
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** No Statistics at all, a placebo. */
    public static final Statistics NONE = new NoStatistics ();


    /**
     * <p>
     * Returns the current specified statistic.
     * </p>
     */
    public abstract long count ();


    /**
     * <p>
     * Decrements the specified statistic by one.
     * </p>
     *
     * @return The new statistic.
     */
    public abstract long decrement ();


    /** Every Statistics must implement:
     *  @see java.lang.Object#equals(java.lang.Object) */

    /** Every Statistics must implement:
     *  @see java.lang.Object#hashCode() */


    /**
     * <p>
     * Increments the specified statistic by one.
     * </p>
     *
     * @return The new statistic.
     */
    public abstract long increment ();
}
