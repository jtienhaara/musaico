package musaico.foundation.term;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.domains.array.Elements;


/**
 * <p>
 * A Term with zero or more elements that can be quickly counted,
 * and the first and last elements (if any) returned,
 * without iterating through the elements.
 * </p>
 *
 * <p>
 * Typically a Countable value is underpinned by an array or list or
 * multi-dimensional data structure such as a table or a matrix.
 * </p>
 *
 * <p>
 * Every Countable value is inherently non-blocking.  A Countable
 * may be No value, One value, or Many values, but may not, by its
 * countable nature, be Infinite.  It also may not be returned from
 * an Abnormal or Incomplete term, since it is considered a successful
 * Term.  However a Countable can be mutable, changing over time.
 * </p>
 *
 *
 * <p>
 * In Java every Multiplicity must be Serializable in order to
 * play nicely across RMI.  However users of the conditional Term
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public interface Countable<VALUE extends Object>
    extends Finite<VALUE>, Term<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Indices which can be used for every Countable term
     *  to address the first (start) element (if any), or to be used
     *  as starting points for counting forward frpm the start
     *  (FORWARD + 0L, FORWARD + 1L, ..., FORWARD + 999L, and so on). */
    public static final long FIRST = Elements.FIRST;
    public static final long FORWARD = Countable.FIRST;
    public static final long FROM_START = Countable.FIRST;

    /** The index AFTER the last one.  There is always No element
     *  at this index, but it can be used, for example, with Operations
     *  which "insert" new elements at the end. */
    public static final long AFTER_LAST = Elements.AFTER_LAST;

    /** Indices which can be used for every Countable term
     *  to address the last (end) element (if any), or to be used
     *  as a starting point to count backward from the end
     *  (BACKWARD + 0L, BACKWARD + 1L, ..., BACKWARD + 999L, and so on). */
    public static final long LAST = Elements.LAST;
    public static final long BACKWARD = Countable.LAST;
    public static final long FROM_END = Countable.LAST;

    /** No index at all; element # -1L is nowhere to be found in any term. */
    public static final long NONE = Elements.NONE;


    /**
     * <p>
     * Retrieves the element of this term at the specified index
     * at the time of the call.
     * </p>
     *
     * @param index The index of the element to retrieve.
     *              If greater than or equal to
     *              <code> 0L </code> (the first element)
     *              and less than or equal to
     *              <code> length () - 1L </code> (the last element),
     *              then the element at the specified index is returned.
     *              If greater than or equal to <code> Countable.LAST </code>
     *              (the last element) and less than or equal to
     *              <code> Countable.BACKWARD + length () - 1L </code>
     *              (the first element), then the element is returned
     *              in reverse order from the end.
     *              If <code> Countable.NONE </code> (-1L)
     *              or <code> Countable.AFTER_LAST </code>
     *              is specified, or if any other index beyond the
     *              last or before the first element is specified,
     *              then No element is returned.
     *
     * @return Retrieves the immutable element at the specified index,
     *         or No element if the specified index is NONE
     *         or is out of bounds.  Never null.
     */
    public abstract Maybe<VALUE> at (
                                     long index
                                     )
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the specified index, converted if offset from LAST/BACKWARD,
     * and clamped to the bounds of this Countable value.
     * </p>
     *
     * @param index The index to clamp.  Can be any number.
     *
     * @return The clamped index, or Countable.NONE (-1L)
     *         if the input index was out of bounds.
     *         Always greater than or equal to -1L.
     */
    public abstract long clamp (
            long index
            )
        throws Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation;


    /**
     * @return The number of elements in this Countable term.
     *         Always 0 or greater.
     */
    public abstract long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * <p>
     * Retrieves the elements in the specified range at the time of the
     * call (possibly in reverse order) and returns a new Countable
     * containing only those elements.
     * </p>
     *
     * @param start The first index of the elements range to retrieve.
     *              If greater than or equal to
     *              <code> 0L </code> (the first element)
     *              and less than or equal to
     *              <code> length () - 1L </code> (the last element),
     *              then the return value starts at the specified index.
     *              If greater than or equal to <code> Countable.LAST </code>
     *              (the last element) and less than or equal to
     *              <code> Countable.BACKWARD + length () - 1L </code>
     *              (the first element), then the return value starts at
     *              the specified offset from the end.
     *              If <code> Countable.NONE </code> (-1L)
     *              or <code> Countable.AFTER_LAST </code>
     *              is specified, or if any other index beyond the
     *              last or before the first element is specified,
     *              then No result is returned.
     *
     * @param end The last index of the elements range to retrieve.
     *            The same format as the start parameter, above.
     *            If a later index than the start, then the elements
     *            are returned in their original order.  If an earlier
     *            index than the start, then the order is reversed.
     *            If out of range, then No result is returned.
     *
     * @return The Countable range of elements covering the specified indices,
     *         or No elements if either index is NONE or is out of bounds.
     *         Always Countable and immutable.  Never null.
     */
    public abstract Countable<VALUE> range (
                                            long start,
                                            long end
                                            )
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Reads elements from this Countable Term into the specified array
     * of elements.
     * </p>
     *
     * @param elements The array of elements to read into.
     *                 Must not be null.
     *
     * @param offset The offset into the specified array to which
     *               elements will be written.  Must be greater than
     *               or equal to 0.
     *
     * @param length The number of elements to read into the array.
     *
     * @param first_element_index The index into this Countable value
     *                            at which to start reading elements.
     *                            Can be any number.
     *            @see musaico.foundation.term.Countable#at(long)
     *
     * @return The number of elements read, or -1 if there are no more
     *         elements to read, or -1 if the requested
     *         <code> ( offset + length ) </code> is greater than
     *         the number of elements in this Countable value
     *         starting at <code> first_element_index </code>.
     *         If the return value is greater than 0 then all elements
     *         in the range <code> [ offset, offset + length -1 ] </code>
     *         (inclusive) are guaranteed to be non-null. 
     *         Always -1 or greater.
     */
    public abstract int read (
            VALUE [] array,
            int offset,
            int length,
            long first_element_index
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter3.MustBeGreaterThanOrEqualToZero.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation;
}
