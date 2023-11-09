package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.order.NaturallyOrdered;


/**
 * <p>
 * The size of a Region, or the distance between two Positions.
 * </p>
 *
 * <p>
 * The Size class is meant to be a flexible representation of the
 * sizes of different data structures, much like a <code> size_t </code>
 * integer representation of size is used to describe many different
 * types of data structures in C.  For example, an integer Size
 * might represent the number of elements in an array; or a
 * 4-dimensional real valued Size might represent the distance
 * along X, Y, Z and W between two different points; or a Size
 * might be used to represent the number of seconds and nanoseconds
 * elapsed between two points in time; and so on.
 * </p>
 *
 * <p>
 * Every Size has a natural order, such as "size 1 is less than size 2"
 * or "a volume of 3 cubic metres is less than a volume of 10 cubic
 * metres", and so on.
 * </p>
 *
 *
 * <p>
 * In Java every Size must implement hashCode () and equals () in
 * order to play nicely with HashMaps.
 * </p>
 *
 * <p>
 * in Java every Size must be Serializable in order to
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
public interface Size
    extends SpatialElement, NaturallyOrdered<Size>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** No size at all.  An empty Region or an empty Space (with
     *  no Positions at all) has size Size.NONE.  Typically used only
     *  by the orNone () method of conditional resuilts. */
    public static final Size NONE = new NoSize ();


    // Every Size must implement
    // java.lang.Object#equals(java.lang.Object).

    // Every Size must implement java.lang.Object#hashCode().

    // Every Size must implement musaico.foundation.order.NaturallyOrdered#order().

    // Every Size must implement
    // musaico.foundation.region.SpatialElement#space().


    /**
     * <p>
     * Creates a new SizeExpression starting from this
     * Size within this Size's Space.
     * </p>
     *
     * <p>
     * If this Size is Size.NONE, then the result is a successful
     * ZeroSizeExpression.
     * </p>
     *
     * <p>
     * This is a helper method which has the same effect as calling
     * <code> this.space ().expr ( this ) </code>.  The result is
     * always the same.
     * </p>
     *
     * @return A new SizeExpression for this Size within its Space.
     *         Can be a ZeroSizeExpression if this is Size.NONE.
     *         Never null.
     */
    public abstract SizeExpression expr ()
        throws ReturnNeverNull.Violation;
}
