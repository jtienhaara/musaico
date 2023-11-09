package musaico.foundation.metadata;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


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
     * Returns an array including every Statistic currently stored
     * for the refernced object.
     * </p>
     *
     * @return All Stats currently stored.  Never contains any nulls.
     *         Never null.
     */
    public abstract Stat [] all ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the current specified statistic.
     * </p>
     *
     * @param statistic The particular Stat to operate on, such
     *                  as Stat.REFERENCES or Stat.ERRORS.
     *                  If the specified Stat is unknown because
     *                  it has never been incremented, then 0L will
     *                  be returned.  Must not be null.
     *
     * @return The requested statistic, if it is being tracked, or 0L
     *         if it is an unknown statistic.  Always greater than
     *         or equal to 0L.
     */
    public abstract long count (
                                Stat statistic
                                )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Decrements the specified statistic by one.
     * </p>
     *
     * @param statistic The particular Stat to operate on, such
     *                  as Stat.REFERENCES or Stat.ERRORS.
     *                  If the specified Stat is unknown because
     *                  it has never been incremented, then this
     *                  method will have no effect, and 0L will be returned.
     *                  Must not be null.
     *
     * @return The new statistic.  Always greater than or equal to 0L.
     */
    public abstract long decrement (
                                    Stat statistic
                                    )
        throws ParametersMustNotBeNull.Violation;


    /** Every Statistics must implement:
     *  @see java.lang.Object#equals(java.lang.Object) */

    /** Every Statistics must implement:
     *  @see java.lang.Object#hashCode() */


    /**
     * <p>
     * Increments the specified statistic by one.
     * </p>
     *
     * @param statistic The particular Stat to operate on, such
     *                  as Stat.REFERENCES or Stat.ERRORS.
     *                  If the specified Stat is unknown because
     *                  it has never been incremented, then it will
     *                  be stored as 1L, and 1L will be returned.
     *                  Must not be null.
     *
     * @return The new statistic.  Always 0L or greater.
     */
    public abstract long increment (
                                    Stat statistic
                                    )
        throws ParametersMustNotBeNull.Violation;
}
