package musaico.foundation.domains.elements;

import java.io.Serializable;

import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
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
 * Can be overwritable or fixed values, and fixed or variable length,
 * depending on the flags () of the Elements, as well as on the
 * type of Elements.
 * </p>
 *
 * <p>
 * For example, ArrayElements can be overwritable or fixed values, but
 * are always fixed length; whereas ListElements can be either or both
 * overwritable and/or variable length.
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
    extends Filter<ELEMENT>, Iterable<ELEMENT>, Serializable
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
     * @return These Elements represented as an array.
     *         If container () is an array and the Elements are overwritable,
     *         then the internally stored array is returned.
     *         The caller must take locking / synchronization
     *         precautions before modifying the array of overwritable
     *         elements.  If container () is not
     *         an array, or if these Elements are flagged as fixed values,
     *         then a new array will be created
     *         to represent these Elements.  Can contain null elements.
     *         Never null.
     *
     * @throws ElementsLengthException If these Elements are too long
     *                                 to be represented as an array.
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
     *               (the first element), then the element returned is
     *               offset from the end.
     *
     * @return An array containing either the one element at
     *         the specified offset, or no elements, if the specified
     *         offset is not within these Elements.  Never null.
     *         Can contain one null element, if these Elements contain
     *         a null element at the specified offset.
     */
    public abstract ELEMENT [] at (
            long offset
            );


    /**
     * @return The elements in the container, represented
     *         as a Collection.  If the container ()
     *         is a Collection and these Elements are overwritable
     *         and variable length, then the internally stored Collection
     *         is returned.  The caller must take locking / synchronization
     *         precautions before modifying the Collection of overwritable,
     *         variable-length Elements.  If the container () is not
     *         a Collection, or if these Elements are fixed values and/or
     *         fixed length, then a new Collection will be created
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
     * or a stack implementation might not allow overwritable ElementFlags
     * In such cases, an ElementsFlagsException is thrown.
     * </p>
     *
     * @param flags The flags for the new Elements, such as
     *              ElementFlags.IMMUTABLE,
     *              or ElementFlags.MUTABLE, and so on.
     *              If changing these Elements' flags, then invoking
     *              <code> flags ().makeOverwritable () </code> or similar
     *              methods is the only relatively safe way to create
     *              the ElementFlags for the new Elements; otherwise the new
     *              Elements might be missing information they need
     *              from a derived ElementFlags implementation.
     *              Must not be null.
     *
     * @throws NullPointerException If the specified flags are null.
     *
     * @throws ElementsFlagsException If the specified flags
     *                                are incompatible with these
     *                                Elements implementations.
     *                                For example, an array implementation
     *                                might not allow variable length
     *                                flags; or a stack implementation
     *                                might not allow overwritable flags;
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


    // Every Elements must implement
    // java.lang.Object#equals(java.lang.Object)


    /**
     * <p>
     * Filters out these Elements in place if they are overwritable,
     * or creates duplicate, filtered Elements, if these are fixed values.
     * </p>
     *
     * @param filters 0 or more element Filter(s) used to determine whether
     *                each element will be KEPT or DISCARDED in the
     *                filtered Elements.  Must not be null.
     *                Must not contain any null elements.
     *
     * @return The (in-place or new) filtered Elements.  Never null.
     *
     * @throws NullPointerException If the specified filters are null,
     *                              or any of its contents are null.
     */
    @SuppressWarnings("unchecked") // Heap pollution generic varargs.
    public abstract Elements<ELEMENT> filter (
            Filter<ELEMENT> ... filters
            )
        throws NullPointerException;


    // Every Elements must implement
    // musaico.foundation.filter.Filter#filter(java.lang.Object)
    // where the result of filter ( ELEMENT grain )
    // is KEPT if these Elements has ( grain ),
    // or DISCARDED if these Elements do not has ( grain ).


    /**
     * <p>
     * Returns all offsets (if any) within these Elements at which
     * the specified element is found.
     * </p>
     *
     * <p>
     * For example, in the Elements <code> { 1, 2, 2, 3, 3, 3 } </code>,
     * calling <code> find ( 2 ) </code> will return the offsets
     * <code> { 1L, 2L } </code>, or calling <code> find ( 3 ) </code>
     * will return <code> { 3L, 4L, 5L } </code>, or calling
     * <code> find ( 4 ) </code> will return <code> {} </code>.
     *
     * <p>
     * Generally the find () method works quickest on sorted Elements.
     * </p>
     *
     * @param element The element to find in these Elements.
     *                Can be null.
     *
     * @return All offsets into these Elements at which the specified
     *         element can be found.  Can be empty.  Never null.
     *
     * @throws ElementsLengthException If searching through these Elements
     *                                 for the specified element is
     *                                 going to take an infinite amount
     *                                 of time.
     */
    public abstract Elements<Long> find (
            ELEMENT element
            )
        throws ElementsLengthException;


    /**
     * @return The ElementFlags which govern how these Elements behave
     *         (overwritability, length variability, and so on).
     *         Never null.
     *
     * @see musaico.foundation.domains.elements.Elements#Flags
     */
    public abstract ElementFlags flags ();


    /**
     * <p>
     * Creates new Elements of the same container type
     * as these Elements, containing the elements of the specified array.
     * </p>
     *
     * <p>
     * For example, if these are
     * ArrayElements <code> { 1, 2, 3 } </code>,
     * and the specified array is <code> { "a", "b", "c", "d" } </code>,
     * then to create ArrayElements<code> { "a", "b", "c", "d" } </code>,
     * call <code> this.from ( String.class, (flags), array ) </code>.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param flags The flags for the new Elements to create,
     *              such as <code> ElementFlags#IMMUTABLE </code>.
     *              Must not be null.
     *
     * @param array The array of elements to include in the output.
     *              Can contain null elements.  Must not be null.
     *
     * @return The new Elements of the same container type as these Elements,
     *         but containing the elements of the specified array.
     *         Never null.
     *
     * @throws NullPointerException If the specified flags or element class
     *                              or array is/are null.
     *
     * @throws ElementsLengthException If that contains too many elements
     *                                 to store in Elements of this
     *                                 container type.  For example,
     *                                 array containers cannot have
     *                                 more than
     *                                 <copde> Integer.MAX_VALUE </code>
     *                                 elements.
     *
     * @throws ElementsFlagsException If the specified flags
     *                                are incompatible with
     *                                this type of Elements.
     *                                For example, an array container
     *                                cannot be
     *                                <code> ElementFlags.VARIABLE_LENGTH </code>.
     *
     * @throws ConcurrentModificationException If a change in length
     *                                         or content is detected
     *                                         in the specified Elements
     *                                         during processing.  If the
     *                                         Elements are shared across
     *                                         Threads then they must be
     *                                         locked before calling
     *                                         this method.
     */
    @SuppressWarnings("unchecked") // Generic varargs heap pollution.
    public abstract <THAT_ELEMENT extends Object>
        Elements<THAT_ELEMENT> from (
            Class<THAT_ELEMENT> element_class,
            ElementFlags flags,
            THAT_ELEMENT ... array
            )
        throws NullPointerException,
               ElementsLengthException,
               ElementsFlagsException,
               ConcurrentModificationException;


    /**
     * <p>
     * Creates new Elements, usually of the same container type
     * as these Elements, containing the specified elements.
     * </p>
     *
     * <p>
     * For example, if these are
     * ArrayElements <code> { 1, 2, 3 } </code>,
     * and the specified other Elements are
     * ListElements <code> { "a", "b", "c", "d" } </code>, then to create
     * ArrayElements<code> { "a", "b", "c", "d" } </code>, call
     * <code> this.from ( (flags), other_elements ) </code>.
     * </p>
     *
     * <p>
     * Sometimes, a different type of Elements must be returned.
     * For example, calling <code> from ( ..., String elements ) </code>
     * on Elements with an <code> IndexGenerator </code> container
     * cannot result in new Elements with the same container type
     * (IndexGenerator), so <code> ArrayElements&lt;String&gt; </code>
     * are returned instead.
     * </p>
     *
     * @param flags The flags for the new Elements to create,
     *              such as <code> ElementFlags#IMMUTABLE </code>.
     *              Must not be null.
     *
     * @param that The Elements to include in the output.  Must not be null.
     *
     * @return The new Elements of the same container type as these Elements,
     *         but containing the specified elements.  Never null.
     *
     * @throws NullPointerException If the specified elements or flags
     *                              is/are null.
     *
     * @throws ElementsLengthException If that contains too many elements
     *                                 to store in Elements of this
     *                                 container type.  For example,
     *                                 array containers cannot have
     *                                 more than
     *                                 <copde> Integer.MAX_VALUE </code>
     *                                 elements.
     *
     * @throws ElementsFlagsException If the specified flags
     *                                are incompatible with
     *                                this type of Elements.
     *                                For example, an array container
     *                                cannot be
     *                                <code> ElementFlags.VARIABLE_LENGTH </code>.
     *
     * @throws ConcurrentModificationException If a change in length
     *                                         or content is detected
     *                                         in the specified Elements
     *                                         during processing.  If the
     *                                         Elements are shared across
     *                                         Threads then they must be
     *                                         locked before calling
     *                                         this method.
     */
    @SuppressWarnings("unchecked") // Generic varargs heap pollution.
    public abstract <THAT_ELEMENT extends Object>
        Elements<THAT_ELEMENT> from (
            ElementFlags flags,
            Elements<THAT_ELEMENT> that
            )
        throws NullPointerException,
               ElementsLengthException,
               ElementsFlagsException,
               ConcurrentModificationException;


    /**
     * <p>
     * Returns true if the specified element can be found in these Elements.
     * </p>
     *
     * @param element The element in question.  Can be null.
     *
     * @return True if these Elements include the specified element,
     *         false if the specified element is nowhere to be found.
     */
    public abstract boolean has (
            ELEMENT element
            );


    // Every Elements must implement
    // java.lang.Object#hashCode()


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
    public abstract boolean hasOffset (
            long offset
            );


    /**
     * <p>
     * Adds the specified element(s) to these Elements,
     * at the specified offset, either in-place, or returning new Elements,
     * depending on these Elements' <code> flags () </code>.
     * </p>
     *
     * <p>
     * For example, to create new fixed length Elements
     * from existing fixed length Elements, one might call
     * <code> insert ( Elements.AFTER_LAST, a, b, c, d ) </code>.
     * producing new Elements.  Or if these Elements are
     * are variable length, then the existing Elements will be modified
     * in place to append the elements a, b, c and d.
     * </p>
     *
     * @param offset The offset at which to insert the specified elements.
     *               If the offset is greater than or equal to
     *               <code> 0L </code> (the first element)
     *               and less than or equal to
     *               <code> length () - 1L </code> (the last element),
     *               then the elements will be inserted at
     *               the specified offset.
     *               If greater than or equal to <code> Elements.LAST </code>
     *               (the last element) and less than or equal to
     *               <code> Elements.BACKWARD + length () - 1L </code>
     *               (the first element), then the elements will be
     *               inserted at the specified offset from the end
     *               of these Elements.
     *               If <code> Elements.AFTER_LAST </code> then the
     *               elements are appended to the end of these Elements.
     *
     * @param elements The array of elements to insert at the specified
     *                 offset.  Can contain null values.  Must not be null.
     *
     * @return Either these Elements, if variable length, or new Elements
     *         containing the specified values, if these Elements
     *         are fixed length.  Never null.
     *
     * @throws NullPointerException If the specified elements are null.
     *
     * @throws IndexOutOfBoundsException If the specified offset does not
     *                                   point to a valid location in
     *                                   these Elements and it is not
     *                                   Elements.AFTER_LAST.
     *                                   If Elements.NONE (-1L) is specfieid,
     *                                   then this exception is always
     *                                   thrown.
     *
     * @throws ElementsLengthException If these Elements would swell
     *                                 to a size greater than the maximum
     *                                 length supported by these Elements.
     */
    @SuppressWarnings("unchecked") // Generic varargs heap pollution.
    public abstract Elements<ELEMENT> insert (
            long offset,
            ELEMENT ... elements
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ElementsLengthException;


    /**
     * <p>
     * Adds the specified Elements to these Elements,
     * at the specified offset, either in-place, or returning new Elements,
     * depending on these Elements' <code> flags () </code>.
     * </p>
     *
     * <p>
     * For example, to create new fixed length Elements
     * from existing fixed length Elements, one might call
     * <code> insert ( Elements.AFTER_LAST, those_elements ) </code>.
     * producing new Elements.  Or if these Elements
     * are variable length, then the existing Elements will be modified
     * in place to append the contents of those_elements.
     * </p>
     *
     * @param offset The offset at which to insert the specified elements.
     *               If the offset is greater than or equal to
     *               <code> 0L </code> (the first element)
     *               and less than or equal to
     *               <code> length () - 1L </code> (the last element),
     *               then the elements will be inserted at
     *               the specified offset.
     *               If greater than or equal to <code> Elements.LAST </code>
     *               (the last element) and less than or equal to
     *               <code> Elements.BACKWARD + length () - 1L </code>
     *               (the first element), then the elements will be
     *               inserted at the specified offset from the end
     *               of these Elements.
     *               If <code> Elements.AFTER_LAST </code> then the
     *               elements are appended to the end of these Elements.
     *
     * @param elements The Elements to insert at the specified offset.
     *                 Can contain null values.  Must not be null.
     *
     * @return Either these Elements, if variable length, or new Elements
     *         containing the specified values, if these Elements
     *         are fixed length.  Never null.
     *
     * @throws NullPointerException If the specified elements are null.
     *
     * @throws IndexOutOfBoundsException If the specified offset does not
     *                                   point to a valid location in
     *                                   these Elements and it is not
     *                                   Elements.AFTER_LAST.
     *                                   If Elements.NONE (-1L) is specfieid,
     *                                   then this exception is always
     *                                   thrown.
     *
     * @throws ElementsLengthException If these Elements would swell
     *                                 to a size greater than the maximum
     *                                 length supported by these Elements.
     *
     * @throws ConcurrentModificationException If a change in length
     *                                         or content is detected
     *                                         in the specified Elements
     *                                         during processing.  If the
     *                                         Elements are shared across
     *                                         Threads then they must be
     *                                         locked before calling
     *                                         this method.
     */
    public abstract Elements<ELEMENT> insertAll (
            long offset,
            Elements<ELEMENT> elements
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ElementsLengthException,
               ConcurrentModificationException;


    /**
     * @return True if these Elements can (given enough memory) fit into
     *         an array or Collection (length () &lt; Integer.MAX_VALUE);
     *         false if these Elements are too long to fit into
     *         an array or Collection.
     */
    public abstract boolean isArrayLength ();


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


    // Every Elements must implement
    // java.lang.Iterable#iterator()


    /**
     * <p>
     * Retrieves the Elements at the specified offsets at the time of the
     * calland removes all the remaining elements
     * from these Elements, either in-place, or returning new Elements,
     * depending on these Elements' <code> flags () </code>.
     * </p>
     *
     * @param offsets All offsets of elements to retrieve.
     *                If an offset is greater than or equal to
     *                <code> 0L </code> (the first element)
     *                and less than or equal to
     *                <code> length () - 1L </code> (the last element),
     *                then the element at the specified offset will
     *                be included in the return elements.
     *                If greater than or equal to <code> Elements.LAST </code>
     *                (the last element) and less than or equal to
     *                <code> Elements.BACKWARD + length () - 1L </code>
     *                (the first element), then the return value
     *                will include the element at the specified offset
     *                from the end of these Elements.  Must not be null.
     *
     * @return The range of Elements covering the specified offsets,
     *         or empty Elements if either offset is NONE or is out of bounds.
     *         Never null.
     *
     * @throws NullPointerException If the specified offsets are null.
     *
     * @throws IndexOutOfBoundsException If <code> Elements.NONE </code> (-1L)
     *                                   or <code> Elements.AFTER_LAST </code>
     *                                   is specified in the offsets
     *                                   to retrieve, or if any other offset
     *                                   beyond the last or before the first
     *                                   element is specified.
     *
     * @throws ConcurrentModificationException If a change in length
     *                                         or content is detected
     *                                         in the specified Elements
     *                                         during processing.  If the
     *                                         Elements are shared across
     *                                         Threads then they must be
     *                                         locked before calling
     *                                         this method.
     */
    public abstract Elements<ELEMENT> keep (
            long ... offsets
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ConcurrentModificationException;


    /**
     * <p>
     * Retrieves the Elements at the specified offsets at the time of the
     * call and removes all the remaining elements
     * from these Elements, either in-place, or returning new Elements,
     * depending on these Elements' <code> flags () </code>.
     * </p>
     *
     * @param offsets All offsets of elements to retrieve.
     *                If an offset is greater than or equal to
     *                <code> 0L </code> (the first element)
     *                and less than or equal to
     *                <code> length () - 1L </code> (the last element),
     *                then the element at the specified offset will
     *                be included in the return elements.
     *                If greater than or equal to <code> Elements.LAST </code>
     *                (the last element) and less than or equal to
     *                <code> Elements.BACKWARD + length () - 1L </code>
     *                (the first element), then the return value
     *                will include the element at the specified offset
     *                from the end of these Elements.  Must not be null.
     *
     * @return The range of Elements covering the specified offsets,
     *         or empty Elements if either offset is NONE or is out of bounds.
     *         Never null.
     *
     * @throws NullPointerException If the specified offsets are null,
     *                              or if any offset is null.
     *
     * @throws IndexOutOfBoundsException If <code> Elements.NONE </code> (-1L)
     *                                   or <code> Elements.AFTER_LAST </code>
     *                                   is specified in the offsets
     *                                   to retrieve, or if any other offset
     *                                   beyond the last or before the first
     *                                   element is specified.
     *
     * @throws ConcurrentModificationException If a change in length
     *                                         or content is detected
     *                                         in the specified Elements
     *                                         during processing.  If the
     *                                         Elements are shared across
     *                                         Threads then they must be
     *                                         locked before calling
     *                                         this method.
     */
    public abstract Elements<ELEMENT> keepAll (
            Elements<Long> offsets
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ConcurrentModificationException;


    /**
     * <p>
     * Retrieves the Elements from the specified start offset to the
     * specified end offset at the time of the call
     * (possibly in reverse order, if the start offset is greater
     * than the end offset), and removes all the remaining elements
     * from these Elements, either in-place, or returning new Elements,
     * depending on these Elements' <code> flags () </code>.
     * </p>
     *
     * @param start The offset at which to begin gathering
     *               the range of elements.  If the offset is greater than
     *               or equal to <code> 0L </code> (the first element)
     *               and less than or equal to
     *               <code> length () - 1L </code> (the last element),
     *               then the element at the specified offset will
     *               be included in the return elements.
     *               If greater than or equal to <code> Elements.LAST </code>
     *               (the last element) and less than or equal to
     *               <code> Elements.BACKWARD + length () - 1L </code>
     *               (the first element), then the return value
     *               will include the element at the specified offset
     *               from the end of these Elements.  Can be
     *               greater than the end offset, in order to gather
     *               elements in reverse order.
     *
     * @param end The offset at which to stop gathering
     *            the range of elements.  If the offset is greater than
     *            or equal to <code> 0L </code> (the first element)
     *            and less than or equal to
     *            <code> length () - 1L </code> (the last element),
     *            then the element at the specified offset will
     *            be included in the return elements.
     *            If greater than or equal to <code> Elements.LAST </code>
     *            (the last element) and less than or equal to
     *            <code> Elements.BACKWARD + length () - 1L </code>
     *            (the first element), then the return value
     *            will include the element at the specified offset
     *            from the end of these Elements.  Can be
     *            greater than the end offset, in order to gather
     *            elements in reverse order.
     *
     * @return The range of Elements covering the (start, end) range,
     *         or empty Elements if either offset is NONE or is out of bounds.
     *         Never null.
     *
     * @throws IndexOutOfBoundsException If <code> Elements.NONE </code> (-1L)
     *                                   or <code> Elements.AFTER_LAST </code>
     *                                   is specified in the offsets
     *                                   to retrieve, or if any other offset
     *                                   beyond the last or before the first
     *                                   element is specified.
     */
    public abstract Elements<ELEMENT> keepRange (
            long start,
            long end
            )
        throws NullPointerException,
               IndexOutOfBoundsException;


    /**
     * @return The number of elements in the container.
     *         Always greater than or equal to 0L.
     */
    public abstract long length ();


    /**
     * @return The elements in the container, represented
     *         as a List.  If the container ()
     *         is a List and these Elements are overwritable
     *         and variable length, then the internally stored List
     *         is returned.  The caller must take locking / synchronization
     *         precautions before modifying the List of overwritable,
     *         variable-length Elements.  If the container () is not
     *         a List, or if these Elements are fixed value or fixed length,
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
     * @return The sort order(s) of these Elements, if any.
     *         If these Elements are unsorted, then the result
     *         is empty.  If these Elements have been sorted
     *         first by <code> comparator1 </code> then by
     *         <code> comparator2 </code> and finally by
     *         <code> comparator3 </code>, then the result will be
     *         <code> { comparator1, comparator2, comparator3 } </code>.
     *         Any 2 elements considered equal by comparator3
     *         are sorted into order by comparator2, and any 2 elements
     *         considered equal by comparator2 are sorted into order
     *         by comparator1.  Note that this would be written as
     *         "sorted by comparator3, then comparator2, then comparator1",
     *         but coded as
     *         <code> { comparator1, comparator2, comparator3 } </code>
     *         Never null.
     */
    public abstract Elements<Comparator<ELEMENT>> order ();


    /**
     * <p>
     * Replaces the values contained in these Elements, starting at
     * the specified offset, with the specified elements,
     * either in-place, or returning new Elements,
     * depending on these Elements' <code> flags () </code>.
     * </p>
     *
     * <p>
     * For example, to create new fixed value Elements
     * from existing fixed value Elements, one might call
     * <code> overwrite ( 0L, a, b, c, d ) </code>.
     * producing new Elements.  Or if these Elements are
     * are overwritable, then the existing Elements will be modified
     * in place to replace the first element values with a, b, c and d.
     * </p>
     *
     * @param offset The offset at which to overwrite the specified elements.
     *               If the offset is greater than or equal to
     *               <code> 0L </code> (the first element)
     *               and less than or equal to
     *               <code> length () - 1L </code> (the last element),
     *               then the values starting at the specified offset
     *               will be overwritten.  
     *               If greater than or equal to <code> Elements.LAST </code>
     *               (the last element) and less than or equal to
     *               <code> Elements.BACKWARD + length () - 1L </code>
     *               (the first element), then the values starting at
     *               the specified offset from the end of these Elements
     *               will be overwritten.
     *               Element values are always overwritten from start
     *               to end, incrementing the offset for each value
     *               to replace.
     *
     * @param elements The array of elements to overwrite at the specified
     *                 offset.  Can contain null values.  Must not be null.
     *
     * @return Either these Elements, if variable length, or new Elements
     *         containing the specified values, if these Elements
     *         are fixed length.  Never null.
     *
     * @throws NullPointerException If the specified elements are null.
     *
     * @throws IndexOutOfBoundsException If the specified offset does not
     *                                   point to a valid location in
     *                                   these Elements
     *                                   If Elements.NONE (-1L) is specfieid,
     *                                   then this exception is always
     *                                   thrown.
     */
    @SuppressWarnings("unchecked") // Generic varargs heap pollution.
    public abstract Elements<ELEMENT> overwrite (
            long offset,
            ELEMENT ... elements
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ElementsLengthException;


    /**
     * <p>
     * Replaces the values contained in these Elements, starting at
     * the specified offset, with the specified elements,
     * either in-place, or returning new Elements,
     * depending on these Elements' <code> flags () </code>.
     * </p>
     *
     * <p>
     * For example, to create new fixed value Elements
     * from existing fixed value Elements, one might call
     * <code> overwrite ( 0L, those_elements ) </code>.
     * producing new Elements.  Or if these Elements
     * are overwritable, then the existing element values will be modified
     * in place with the contents of those_elements.
     * </p>
     *
     * @param offset The offset at which to overwrite the specified elements.
     *               If the offset is greater than or equal to
     *               <code> 0L </code> (the first element)
     *               and less than or equal to
     *               <code> length () - 1L </code> (the last element),
     *               then the values starting at the specified offset
     *               will be overwritten.  
     *               If greater than or equal to <code> Elements.LAST </code>
     *               (the last element) and less than or equal to
     *               <code> Elements.BACKWARD + length () - 1L </code>
     *               (the first element), then the values starting at
     *               the specified offset from the end of these Elements
     *               will be overwritten.
     *               Element values are always overwritten from start
     *               to end, incrementing the offset for each value
     *               to replace.
     *
     * @param elements The Elements to overwrite at the specified offset.
     *                 Can contain null values.  Must not be null.
     *
     * @return Either these Elements, if variable length, or new Elements
     *         containing the specified values, if these Elements
     *         are fixed length.  Never null.
     *
     * @throws NullPointerException If the specified elements are null.
     *
     * @throws IndexOutOfBoundsException If the specified offset does not
     *                                   point to a valid location in
     *                                   these Elements.
     *                                   If Elements.NONE (-1L) is specfieid,
     *                                   then this exception is always
     *                                   thrown.
     *
     * @throws ConcurrentModificationException If a change in length
     *                                         or content is detected
     *                                         in the specified Elements
     *                                         during processing.  If the
     *                                         Elements are shared across
     *                                         Threads then they must be
     *                                         locked before calling
     *                                         this method.
     */
    public abstract Elements<ELEMENT> overwriteAll (
            long offset,
            Elements<ELEMENT> elements
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ElementsLengthException,
               ConcurrentModificationException;


    /**
     * <p>
     * Removes the element(s) at the specified offset(s)
     * from these Elements, either in-place, or returning new Elements,
     * depending on these Elements' <code> flags () </code>.
     * </p>
     *
     * <p>
     * For example, to create new fixed value and/or fixed length Elements
     * from existing fixed value and/or fixed length Elements, one might call
     * <code> remove ( 0L, 1L, 2L, Elements.LAST ) </code>,
     * producing new Elements.  Or if these Elements are
     * are overwritable and variable length, then the existing Elements
     * will be modified in place to remove the elements at offsets
     * 0L, 1L, 2L and Elements.LAST.
     * </p>
     *
     * @param offsets The offsets of elements which will be removed.
     *                If an offset is greater than or equal to
     *                <code> 0L </code> (the first element)
     *                and less than or equal to
     *                <code> length () - 1L </code> (the last element),
     *                then the element at the specified offset
     *                will be removed.
     *                If greater than or equal to <code> Elements.LAST </code>
     *                (the last element) and less than or equal to
     *                <code> Elements.BACKWARD + length () - 1L </code>
     *                (the first element), then the element at
     *                the specified offset from the end of these Elements
     *                will be removed.
     *                The offsets must be in ascending order.  There must
     *                not be any duplicate offsets.  Must not be null.
     *                Must not contain any null elements.
     *
     * @return Either these Elements, if overwritable and variable length,
     *         or new Elements, containing these Elements except for
     *         the removed elements, if these Elements are fixed value
     *         and/or fixed length.  Never null.
     *
     * @throws NullPointerException If the specified offsets are null.
     *
     * @throws IndexOutOfBoundsException If any of the specified offsets
     *                                   does not point to a valid location
     *                                   in these Elements.
     *                                   If Elements.NONE (-1L) is specfieid,
     *                                   then this exception is always
     *                                   thrown.
     */
    public abstract Elements<ELEMENT> remove (
            long ... offsets
            )
        throws NullPointerException,
               IndexOutOfBoundsException;


    /**
     * <p>
     * Removes the element(s) at the specified offset(s)
     * from these Elements, either in-place, or returning new Elements,
     * depending on these Elements' <code> flags () </code>.
     * </p>
     *
     * @param offsets The offsets of elements which will be removed.
     *                If an offset is greater than or equal to
     *                <code> 0L </code> (the first element)
     *                and less than or equal to
     *                <code> length () - 1L </code> (the last element),
     *                then the element at the specified offset
     *                will be removed.
     *                If greater than or equal to <code> Elements.LAST </code>
     *                (the last element) and less than or equal to
     *                <code> Elements.BACKWARD + length () - 1L </code>
     *                (the first element), then the element at
     *                the specified offset from the end of these Elements
     *                will be removed.
     *                Element values are always overwritten from start
     *                to end, incrementing the offset for each value
     *                to replace.  Must not be null.
     *                Must not contain any null elements.
     *
     * @return Either these Elements, if overwritable and variable length,
     *         or new Elements, containing these Elements except for
     *         the removed elements, if these Elements are fixed value
     *         and/or fixed length.  Never null.
     *
     * @throws NullPointerException If the specified offsets are null,
     *                              or if any one specified offset is null.
     *
     * @throws IndexOutOfBoundsException If any of the specified offsets
     *                                   does not point to a valid location
     *                                   in these Elements.
     *                                   If Elements.NONE (-1L) is specfieid,
     *                                   then this exception is always
     *                                   thrown.
     *
     * @throws ConcurrentModificationException If a change in length
     *                                         or content is detected
     *                                         in the specified Elements
     *                                         during processing.  If the
     *                                         Elements are shared across
     *                                         Threads then they must be
     *                                         locked before calling
     *                                         this method.
     */
    public abstract Elements<ELEMENT> removeAll (
            Elements<Long> offsets
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ConcurrentModificationException;


    /**
     * <p>
     * Removes the range of element(s) starting at the specified offset
     * and ending at the specified offset
     * from these Elements, either in-place, or returning new Elements,
     * depending on these Elements' <code> flags () </code>.
     * </p>
     *
     * @param start The first element to remove.
     *              If an offset is greater than or equal to
     *              <code> 0L </code> (the first element)
     *              and less than or equal to
     *              <code> length () - 1L </code> (the last element),
     *              then the element at the specified offset
     *              will be removed.
     *              If greater than or equal to <code> Elements.LAST </code>
     *              (the last element) and less than or equal to
     *              <code> Elements.BACKWARD + length () - 1L </code>
     *              (the first element), then the element at
     *              the specified offset from the end of these Elements
     *              will be removed.  If the start and end offsets
     *              are reversed, so that start &gt; end, then effectively
     *              the same procedure as calling
     *              <code> removeRange ( end, start ) </code> is performed.
     *
     * @return Either these Elements, if overwritable and variable length,
     *         or new Elements, containing these Elements except for
     *         the removed elements, if these Elements are fixed value
     *         and/or fixed length.  Never null.
     *
     * @throws IndexOutOfBoundsException If either of the specified offsets
     *                                   does not point to a valid location
     *                                   in these Elements.
     *                                   If Elements.NONE (-1L) is specfieid,
     *                                   then this exception is always
     *                                   thrown.
     */
    public abstract Elements<ELEMENT> removeRange (
            long start,
            long end
            )
        throws IndexOutOfBoundsException;


    /**
     * @return The elements in the container, represented
     *         as an ordered Set.  If the container ()
     *         is a LinkedHashSet and these Elements are overwritable
     *         and variable length, then the internally stored  is returned.
     *         The caller must take locking / synchronization
     *         precautions before modifying the LinkedHashSet of overwritable,
     *         variable-length Elements.  If the container () is not
     *         a LinkedHashSet, or if these Elements are fixed value
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
     * Sorts these Elements in place if they are overwritable, or creates
     * a duplicate, sorted Elements, if these are fixed value.
     * </p>
     *
     * @param comparator The element Comparator used to arrange
     *                   the elements in order.  Must not be null.
     *
     * @return The (in-place or new) sorted Elements.  Never null.
     *
     * @throws NullPointerException If the specified comparator is null.
     *
     * @throws ElementsLengthException If these Elements have already been
     *                                 sorted so many times that tacking
     *                                 on yet another order to the
     *                                 sequence of order comparators
     *                                 would cause overflow.
     *
     * @throws IllegalStateException If these Elements are corrupt,
     *                               because they are overwritable but
     *                               their <code> order () </code>
     *                               Elements are not variable length;
     *                               or because they are not overwritable,
     *                               but the <code> order () </code>
     *                               Elements are variable length.
     *                               Every time overwritable Elements
     *                               are sorted, an order comparator
     *                               is appended.  Every time non-overwritable
     *                               Elements are sorted, new Elements
     *                               are created with new order comparator
     *                               Elements.  If these Elements are not
     *                               properly synchronized with their orders
     *                               then this corruption exception will
     *                               be thrown.
     */
    public abstract Elements<ELEMENT> sort (
            Comparator<ELEMENT> comparator
            )
        throws NullPointerException,
               ElementsLengthException,
               IllegalStateException;


    /**
     * <p>
     * Returns the specified ElementFlags or a variant that is supported
     * by Elements of this class, modified as necessary to turn on/off
     * the required / unsupported flags.
     * </p>
     *
     * <p>
     * For example, ListElements support flags with variable length,
     * but ArrayElements do not.  So ArrayElements would return a
     * copy of the specified flags with variable length set to false.
     * </p>
     *
     * @param flags The ElementFlags to return or modify.  Must not be null.
     *
     * @return Either the specified ElementFlags, if Elements of this
     *         class support all of them as-is, or a modified copy,
     *         if required flags are missing and/or unsupported flags
     *         are present in the input flags.  Never null.
     */
    public abstract ElementFlags supportedFlags (
            ElementFlags flags
            )
        throws NullPointerException;


    // Every Elements must implement
    // java.lang.Object#toString()
}
