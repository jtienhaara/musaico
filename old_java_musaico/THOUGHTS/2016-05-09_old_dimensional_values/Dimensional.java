package musaico.foundation.value;

import java.io.Serializable;


import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * A Countable value whose elements can be indexed.
 * </p>
 *
 *
 * <p>
 * In Java every conditional Value must be Serializable in order to
 * play nicely across RMI.  However users of the conditional Value
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
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
 * @see musaico.foundation.value.MODULE#COPYRIGHT
 * @see musaico.foundation.value.MODULE#LICENSE
 */
public interface Dimensional<VALUE extends Object>
    extends Countable<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Returns the (D - N)-dimensional elements at the specified indices
     * (if any), where D is the number of dimensions of this value,
     * and N is the number of indices provided.
     * </p>
     *
     * <p>
     * For example, when calling <code> at ( 1, 2, 3 ) </code>
     * on a 3-dimensional matrix (N = 3, D = 3), a 0-dimensional
     * value will be returned (such as No value or One value).
     * Or when calling <code> at ( 1 ) </code> on the same 3-dimensional
     * matrix (N = 1, D = 3), then if the specified index is in range,
     * a 2-dimensional matrix will be returned.  And so on.
     * </p>
     *
     * <p>
     * Calling <code> at () </code> on any Dimensional value will
     * return itself.
     * </p>
     *
     * @param indices Zero or more dimensional indices, one per dimension.
     *                Must be fewer than or equal to the number of
     *                <code> dimensions () </code> of this value.
     *                Each I'th index must be greater than or equal to 0L,
     *                and less than <code> indices ( ... ) </code>
     *                called with the first (I - 1) indices.
     *                Must not be null.
     *
     * @return The specified (D - N)-dimensional elements, if any,
     *         or No elements if the specified indices are invalid.
     *         Never null.
     */
    public abstract Dimensional<VALUE> at (
                                           long... indices
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The number of dimensions in which the elements of this
     *         value reside: 0L if this Dimensional value is an
     *         infinitesimal point (there are exactly 0 or 1 accessible
     *         elements), or 1L if the elements can be accessed via
     *         <code> at ( long ) </code>,
     *         or 2L if the elements can be accessed via
     *         <code> at ( long, long ) </code>,
     *         or 3L if the elements can be accessed via
     *         <code> at ( long, long, long ) </code>,
     *         and so on.  Always 0L or greater.
     */
    public abstract long dimensions ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * <p>
     * Returns true if this Dimensional value has element(s) at the
     * specified indices, false if the specified indices are not
     * indexable in this value.
     * </p>
     *
     * <p>
     * For example, if this is a 3-dimensional matrix, and
     * <code> has ( 1, 2, 3 ) </code> is called, and if
     * index <code> { 1, 2, 3 } </code> is indexable in the matrix,
     * then true will be returned.  Or if <code> has ( 1 ) </code>
     * is called on the same 3-dimensional matrix, then true will be
     * returned.  However if <code> has ( 4, 5, 6 ) </code> is called,
     * but there are no elements at the specified position, then false
     * will be returned.  And any time the number of indices (N) exceeds
     * the number of dimensions (D) of this value, false will be returned.
     * So, for example, if <code> has ( 1, 2, 3, 4 ) </code> is called
     * on the same matrix, then false will be returned.  And so on.
     * </p>
     *
     * @param indices Zero or more indices, one per dimension.
     *                Must be fewer than or equal to the number of
     *                <code> dimensions () </code> of this value.
     *                Each I'th index must be greater than or equal to 0L,
     *                and less than <code> indices ( ... ) </code>
     *                called with the first (I - 1) indices.
     *                Must not be null.
     *
     * @return True if this Dimensional value has element(s) at the
     *         specified position; false if not.
     */
    public abstract boolean has (
                                 long... indices
                                 )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * <p>
     * Returns the number of indexable values at the specified position in
     * this Dimensional value.
     * </p>
     *
     * <p>
     * For example, if this is a 3-dimensional matrix, and
     * <code> indices ( 1, 2, 3 ) </code> is called, then 0L
     * will be returned, since the value at point <code> { 1, 2, 3 } </code>
     * is 0-dimensional.  Or if <code> indices ( 1 ) </code> is called
     * on the same 3-dimensional matrix, and if it is a valid position
     * within the matrix, then the number of 2-dimensional values
     * indexable from that position will be returned.  And so on.
     * </p>
     *
     * @param indices Zero or more indices, one per dimension.
     *                Must be fewer than or equal to the number of
     *                <code> dimensions () </code> of this value.
     *                Each I'th index must be greater than or equal to 0L,
     *                and less than <code> indices ( ... ) </code>
     *                called with the first (I - 1) indices.
     *                Must not be null.
     *
     * @return The number of indexable values at the specified position
     *         (if any).  Always greater than or equal to 0L.
     */
    public abstract long indices (
                                  long... indices
                                  )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation;
}
