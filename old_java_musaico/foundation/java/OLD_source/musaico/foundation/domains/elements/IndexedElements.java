package musaico.foundation.domains.elements;

import java.io.Serializable;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;


import musaico.foundation.filter.Filter;


/**
 * <p>
 * Common interface for the elements of an array, Collection,
 * Iterable, single Object, Map, multi-dimensional array, and so on.
 * </p>
 *
 * <p>
 * Every data structure is treated as a flat sequence of elements.
 * </p>
 *
 * <p>
 * Can be mutable or immutable, and if mutable, can
 * be fixed in size or variable length.
 * </p>
 *
 * <p>
 * No locking is done by any low-level Elements implementation,
 * though concurrency protection can be added by wrapper Elements.
 * You must lock low-level Elements externally to access them concurrently.
 * </p>
 *
 *
 * <p>
 * In Java every Elements must implement <code> equals ( ... ) </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Elements must be Serializable in order to
 * play nicely across RMI.  However users of the Elements
 * must be careful, since the elements and any other data or metadata
 * stored inside might not be Serializable.
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
 * @see musaico.foundation.domains.elements.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.elements.MODULE#LICENSE
 */
public interface Elements<ELEMENT extends Object>
    extends Iterable<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Offsets which can be used for every sequence of Elements
     *  to address the first (start) element (if any), or to be used
     *  as starting points for counting forward frpm the start
     *  (FORWARD + 0L, FORWARD + 1L, ..., FORWARD + 999L, and so on). */
    public static final long FIRST = 0L;
    public static final long FORWARD = Elements.FIRST;
    public static final long FROM_START = Elements.FIRST;

    /** The offset AFTER the last element.  There is always No element
     *  at this offset, but it can be used, for example, to append
     *  new elements to the end. */
    public static final long AFTER_LAST = Long.MIN_VALUE;

    /** The offset of the last (end) element (if any).
     *  Alternatively, a starting point to count backward from the end
     *  (BACKWARD + 0L, BACKWARD + 1L, ..., BACKWARD + 999L, and so on). */
    public static final long LAST = Elements.AFTER_LAST + 1L;
    public static final long BACKWARD = Elements.LAST;
    public static final long FROM_END = Elements.LAST;

    /** No offset at all; element # -1L is nowhere to be found. */
    public static final long NONE = -1L;






    /**
     * @return The elements in the container, represented
     *         as an array.  If the container ()
     *         is an array and these Elements are mutable,
     *         then the internally stored array is returned.
     *         The caller must take locking / synchronization
     *         precautions before modifying the array of mutable
     *         Elements.  If the container () is not
     *         an array, or if these Elements are immutable,
     *         then a new array will be created
     *         to represent the container data structure.
     *
     * @throws ElementsLengthException If these Elements cannot be represented
     *                                 as an array, for example because there
     *                                 are too many.
     */
    public abstract ELEMENT [] array ()
        throws ElementsLengthException;


    /**
     * <p>
     * Retrieves the offset'th element.
     * </p>
     *
     * @param offset The offset of the element to retrieve.
     *               If greater than or equal to
     *               <code> 0L </code> (the first element)
     *               and less than or equal to
     *               <code> length () - 1L </code> (the last element),
     *               then the element at the specified offset is returned.
     *               If greater than or equal to <code> Elements.LAST </code>
     *               (the last element) and less than or equal to
     *               <code> Elements.BACKWARD + length () - 1L </code>
     *               (the first element), then the element is returned
     *               in reverse order from the end.
     *               If <code> Elements.NONE </code> (-1L)
     *               or <code> Elements.AFTER_LAST </code>
     *               is specified, or if any other offset beyond the
     *               last or before the first element is specified,
     *               then No element is returned.
     *
     * @return Retrieves the 1 element at the specified offset, if valid;
     *         or none () (0 length), if the offset is invalid.
     *         Can be null.
     */
    public abstract ELEMENT [] at (
            long offset
            );


    /**
     * @return The elements in the container, represented
     *         as a Collection.  If the container ()
     *         is a Collection and these Elements are mutable
     *         and unfixed length, then the internally stored Collection
     *         is returned.  The caller must take locking / synchronization
     *         precautions before modifying the Collection of mutable
     *         Elements.  If the container () is not
     *         a Collection, or if these Elements are immutable
     *         or fixed length, then a new Collection will be created
     *         to represent the container data structure.
     *         Can contain null elements.  Never null.
     *
     * @throws ElementsLengthException If these Elements cannot be represented
     *                                 as a Collection, for example
     *                                 because there are too many.
     */
    public abstract Collection<ELEMENT> collection ()
        throws ElementsLengthException;


    /**
     * @return The container of elements, such as an array, a single
     *         Object, a Collection, and so on.  Never null.
     */
    public abstract Object container ();


    /**
     * <p>
     * Creates a copy of these Elements.
     * </p>
     *
     * <p>
     * The new Elements will be created with the specified flags,
     * if possible.  Note, however, that not all Elements implementations
     * allow all combinations of flags.  For example, an array
     * implementation might not allow variable length ElementFlags;
     * or a stack implementation might not mutable ElementFlags
     * In such cases, an ElementsFlagsException is thrown.
     * </p>
     *
     * @param flags The flags for the new Elements, such as
     *              ElementFlags.IMMUTABLE_FIXED_LENGTH,
     *              or ElementFlags.MUTABLE_VARIABLE_LENGTH, and so on.
     *              If changing these Elements' flags, then invoking
     *              <code> flags ().makeMutable () </code> or similar
     *              methods is the only relatively safe way to create
     *              the ElementFlags for the new Elements; otherwise the new
     *              Elements might be missing information they need
     *              from a derived ElementFlags implementation.
     *              Must not be null.
     *
     * @throws ElementsFlagsException If the specified flags
     *                                are incompatible with these
     *                                Elements implementations.
     *                                For example, an array implementation
     *                                might not allow variable length
     *                                flags; or a stack implementation
     *                                might not allow mutable flags;
     *                                and so on.  These Elements
     *                                might also require a specific
     *                                implementation of ElementFlags,
     *                                so if the specified ElementFlags
     *                                are of the wrong class, this
     *                                exception will be thrown.
     */
    public abstract Elements<ELEMENT> duplicate (
            ElementFlags flags
            )
        throws NullPointerException,
               ElementsFlagsException;


    /**
     * @return The Class of the elements stored in this container.
     *         Never null.
     */
    public abstract Class<ELEMENT> elementClass ();


    /**
     * @return The ElementFlags which govern how these Elements behave
     *         (mutability, length variability, and so on).
     *         Never null.
     *
     * @see musaico.foundation.domains.elements.Elements#Flags
     */
    public abstract ElementFlags flags ();


    /**
     * <p>
     * Filters these Elements in place if they are mutable and variable
     * length, or creates a duplicate, filtered Elements,
     * if these are immutable and/or fixed length.
     * </p>
     *
     * @param filter The sieve used to filter out elements.
     *               Must not be null.
     *
     * @return The (in-place or new) filtered Elements.  Never null.
     *
     * @throws NullPointerException If the specified filter is null.
     */
    public abstract Elements<ELEMENT> from (
            Elements<ELEMENT> that,
            ElementFlags flags
            )
        throws NullPointerException,
               ElementsLengthException,
               ElementsFlagsException;


    /**
     * <p>
     * Returns true if these Elements include the specified offset,
     * false if not.
     * </p>
     *
     * @param offset The offset of the element in question.
     *               If greater than or equal to
     *               <code> 0L </code> (the first element)
     *               and less than or equal to
     *               <code> length () - 1L </code> (the last element),
     *               then the element at the specified offset is returned.
     *               If greater than or equal to <code> Elements.LAST </code>
     *               (the last element) and less than or equal to
     *               <code> Elements.BACKWARD + length () - 1L </code>
     *               (the first element), then the element is returned
     *               in reverse order from the end.
     *               If <code> Elements.NONE </code> (-1L)
     *               or <code> Elements.AFTER_LAST </code>
     *               is specified, or if any other offset beyond the
     *               last or before the first element is specified,
     *               then No element is returned.
     *
     * @return True if the these Elements contain the specified offset,
     *         false if not.
     */
    public abstract boolean has (
            long offset
            );


    /**
     * @return True if these are 0-length Elements;
     *         false if there are one or more Elements.
     */
    public abstract boolean isEmpty ();


    /**
     * @return True if these Elements are exactly length 1L;
     *         false if there are 0, or 2 or more, Elements.
     */
    public abstract boolean isSingleton ();


    /**
     * @return The number of elements in the container.
     *         Always greater than or equal to 0L.
     */
    public abstract long length ();


    /**
     * @return The elements in the container, represented
     *         as a List.  If the container ()
     *         is a List and these Elements are mutable and unfixed length,
     *         then the internally stored List is returned.
     *         The caller must take locking / synchronization
     *         precautions before modifying the List of mutable
     *         Elements.  If the container () is not
     *         a List, or if these Elements are immutable or fixed length,
     *         then a new List will be created
     *         to represent the container data structure.
     *
     * @throws ElementsLengthException If these Elements cannot be represented
     *                                 as a List, for example
     *                                 because there are too many.
     */
    public abstract List<ELEMENT> list ()
        throws ElementsLengthException;


    /**
     * @return An empty array of elements (0 length).  Never null.
     */
    public abstract ELEMENT [] none ();


    /**
     * @return The offsets of these Elements (0L, 1L, ..., length () - 2L,
     *         length () - 1L), represented as Elements
     *         all their own.  For example, <code> { 0L, ..., 17L } </code>
     *         for Elements of length 18L.  Never null.
     */
    public abstract Elements<Long> offsets ();


    /**
     * <p>
     * Retrieves the Elements in the specified range at the time of the
     * call (possibly in reverse order, if the start and end offsets
     * are passed in reverse) and returns the new range of Elements.
     * </p>
     *
     * @param start The first offset of the elements range
     *              to retrieve.  If greater than or equal to
     *              <code> 0L </code> (the first element)
     *              and less than or equal to
     *              <code> length () - 1L </code> (the last element),
     *              then the return value starts at the specified offset.
     *              If greater than or equal to <code> Elements.LAST </code>
     *              (the last element) and less than or equal to
     *              <code> Elements.BACKWARD + length () - 1L </code>
     *              (the first element), then the return value starts at
     *              the specified offset from the end.
     *              If <code> Elements.NONE </code> (-1L)
     *              or <code> Elements.AFTER_LAST </code>
     *              is specified, or if any other offset beyond the
     *              last or before the first element is specified,
     *              then empty Elements are returned.
     *
     * @param end The last offset of the elements range to retrieve.
     *            The same format as the start parameter, above.
     *            If a later offset than the start, then the elements
     *            are returned in their original order.  If an earlier
     *            offset than the start, then the order is reversed.
     *            If out of range, then No result is returned.
     *
     * @return The range of Elements covering the specified offsets,
     *         or empty Elements if either offset is NONE or is out of bounds.
     *         Never null.
     */
    public abstract Elements<ELEMENT> range (
            long start,
            long end
            );


    /**
     * @return The elements in the container, represented
     *         as an ordered Set.  If the container ()
     *         is a LinkedHashSet and these Elements are mutable
     *         and unfixed length, then the internally stored  is returned.
     *         The caller must take locking / synchronization
     *         precautions before modifying the LinkedHashSet of mutable
     *         Elements.  If the container () is not
     *         a LinkedHashSet, or if these Elements are immutable
     *         or fixed length, then a new LinkedHashSet will be created
     *         to represent the container data structure.
     *
     * @throws ElementsLengthException If these Elements cannot be represented
     *                                 as a LinkedHashSet, for example
     *                                 because there are too many.
     */
    public abstract LinkedHashSet<ELEMENT> set ()
        throws ElementsLengthException;


    /**
     * @return The single element, if these Elements are of exactly
     *         length 1.  Can be null.
     *
     * @throws ElementsLengthException If these Elements are not length 1.
     */
    public abstract ELEMENT singleton ()
        throws ElementsLengthException;


    /**
     * <p>
     * Sorts these Elements in place if they are mutable, or creates
     * a duplicate, sorted Elements, if these are immutable.
     * </p>
     *
     * @param comparator The element Comparator used to arrange
     *                   the elements in order.  Must not be null.
     *
     * @return The (in-place or new) sorted Elements.  Never null.
     *
     * @throws NullPointerException If the specified comparator is null.
     */
    public abstract Elements<ELEMENT> sort (
            Comparator<ELEMENT> comparator
            )
        throws NullPointerException;
}
