package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * A position within a region.
 * </p>
 *
 * <p>
 * Depending on the implementation, a Position might represent an integer
 * index, or a String field/column name, or a date/time, or a position in
 * 4-dimensional space, and so on.
 * </p>
 *
 * <p>
 * A Position is often used as a pointer into a data structure.
 * For example, the 5th element of an array, or the "first_name"
 * field of a structure, or the "last_name" column of a row from a table,
 * or 11:00pm Thursday Sept 19 in a calendar, or { 1, 0, -1, 0 } in a
 * 4-dimensional spatial dataset, and so on.
 * </p>
 *
 * <p>
 * The Position abstraction allows different data structures and
 * organizations to be accessed with the same basic type, the same way
 * the int address offset is used in C to index myriad data structures.
 * The user of the data structure need not worry what the underlying
 * organization is, while the structure itself need not be tied down
 * to an overly primitive addressing mechanism.
 * </p>
 *
 *
 * <p>
 * In Java every position must implement <code> equals () </code>
 * and <code> hashCode () </code>, in order to play well with
 * hash sets and hash maps.
 * </p>
 *
 * <p>
 * In Java every Position must be Serializable in order to
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
public interface Position
    extends SpatialElement, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** No position at all.  Typically returned by conditional results
     *  for methods which failed, such as requesting the next Position
     *  in a Region from the very last Position. */
    public static final Position NONE = new NoPosition ();


    // Every Position must implement
    // java.lang.Object#equals(java.lang.Object).

    // Every Position must implement java.lang.Object#hashCode().

    // Every Position must implement
    // musaico.foundation.region.SpatialElement#space().


    /**
     * <p>
     * Creates a new PositionExpression starting from this
     * Position within this Position's Space.
     * </p>
     *
     * <p>
     * If this Position is Position.NONE, then the result will always be
     * a failed PositionExpression.  The NONE position cannot be a
     * part of any successful PositionExpression.
     * </p>
     *
     * <p>
     * This is a helper method which has the same effect as calling
     * <code> this.space ().expr ( this ) </code>.  The result is
     * always the same.
     * </p>
     *
     * @return A new PositionExpression for this Position within its Space.
     *         Can be a failed PositionExpression if this is Position.NONE.
     *         Never null.
     */
    public abstract PositionExpression expr ()
        throws ReturnNeverNull.Violation;
}
