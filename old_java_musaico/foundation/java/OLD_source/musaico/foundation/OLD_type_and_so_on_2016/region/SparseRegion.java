package musaico.foundation.region;

import java.io.Serializable;

import java.util.Iterator;


/**
 * <p>
 * A Region with holes in it.
 * </p>
 *
 * <p>
 * A SparseRegion is often the result of some kind of filtering
 * of another Region.  For example, if a Region covers all of the
 * integer array indices { 0, 1, ..., 10 }, then a filter might
 * return a SparseRegion matching only a few of the Positions, such
 * as { 2, 5, 6, 9 }.  The SparseRegion is implemented as a number\
 * of contiguous sub-Regions, such as { { 2 }, { 5, 6 }, { 9 } }.
 * This allows the user of a complex data structure to filter it
 * and access the filtered data live, without duplicating the
 * data.
 * </p>
 *
 *
 * <p>
 * In Java every Region must implement hashCode () and equals () in
 * order to play nicely with HashMaps.
 * </p>
 *
 * <p>
 * in Java every Region must be Serializable in order to
 * play nicely over RMI.
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
 * @see musaico.foundation.region.MODULE#COPYRIGHT
 * @see musaico.foundation.region.MODULE#LICENSE
 */
public interface SparseRegion
    extends Region, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The number of contiguous sub-Regions contained in this
     *         SparseRegion.  Always 0 or greater.
     */
    public abstract long numRegions ();


    /**
     * <p>
     * Returns the index'th contiguous sub-Region.
     * </p>
     *
     * <p>
     * For example, if this is an integer array index SparseRegion
     * containing sub-Regions { 2 }, { 5, 6 } and { 9 }, then
     * calling <code> region ( 1 ) </code> will return the
     * { 5, 6 } sub-Region.
     * </p>
     *
     * @param index The index of the sub-Region to return, such as
     *              0, 1, 2, ...  Must be 0 or greater.  Must be
     *              less than the number of sub-Regions in this
     *              SparseRegion.
     *
     * @return A RegionExpression for the specified sub-Region,
     *         or a failed RegionExpression if the specified index
     *         is negative or greater than or equal to the number
     *         of sub-Regions in this Sparse Region.  Never null.
     */
    public abstract RegionExpression region (
                                             long index
                                             );
}
