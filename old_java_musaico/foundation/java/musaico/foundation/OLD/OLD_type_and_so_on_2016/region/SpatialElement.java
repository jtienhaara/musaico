package musaico.foundation.region;

import java.io.Serializable;


/**
 * <p>
 * A position, region, size, or other aspect which is meaningful
 * within a Space.
 * </p>
 *
 * <p>
 * For example, integer position 3 is meaningful within the space
 * of all non-negative integers.  Or a region from time 0:00:00
 * to 0:42:00 might be meaningful within a time space.  Or the
 * 4-dimensional size { 1.0, 2.0, 3.0, 0.0 } might be meaningful
 * inside a Cartesian coordinates space.  And so on.
 * </p>
 *
 * <p>
 * SpatialElements are frequently checked against domains to make
 * sure RegionObligations are met.  When an element is outside
 * the obligatory domain, a RegionViolation will result in a
 * failed operation.  For example, attempting to create an
 * ascending integer Region with start position 10 and end
 * position 2 might lead to a RegionViolation because the start
 * position comes after the end position.
 * </p>
 *
 *
 * <p>
 * In Java every SpatialElement must be Serializable in order to
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
public interface SpatialElement
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The addressing Space whence this sptial element sprang, such
     *         as an integer index space, or a String field / column
     *         name space, or a date/time space, or a 4-dimensional
     *         space and so on.  Never null.
     */
    public abstract Space space ();
}
