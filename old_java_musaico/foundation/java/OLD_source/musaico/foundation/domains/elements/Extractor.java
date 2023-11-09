package musaico.foundation.domains.elements;

import java.io.Serializable;

import java.util.Comparator;
import java.util.Iterator;


import musaico.foundation.filter.Filter;


/**
 * <p>
 * How to return a specific type of elements container data structure,
 * such as an array or List or Set or Map and so on, or even Elements,
 * from another container: the raw container, or always a new instance,
 * and so on.
 * </p>
 *
 * <p>
 * One Extractor might try to return each raw container, when it is of
 * the correct type, so that no unnecessary memory allocation is
 * performed.  Another Extractor might always duplicate each incoming
 * container, so that it may be manipulated without affecting anybody else.
 * </p>
 *
 *
 * <p>
 * In Java, every Extractor must be Serializable in order to play nicely
 * across RMI.
 * </p>
 *
 * <p>
 * In Java, every Extractor must implement equals (), hashCode ()
 * and toString (), in order to play nicely with Maps and so that
 * debugging / troubleshooting messages aren't cryptic.
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
public interface Extractor<CONTAINER extends Object, INDEX extends Object, ELEMENT extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Returns the element at the specified index
     * from the specified container.
     * </p>
     *
     * @param container The container whose element will be returned.
     *                  Must not be null.
     *
     * @param index The index from which to return the element value.
     *              Must be a valid index in the specified container
     *              (<code> this.has ( container, index ) == true </code>).
     *              Must not be null.  Must not contain any null elements.
     *
     * @return The specified element.  Can be null.
     *
     * @throws NullPointerException If the specified container
     *                              and/or index is/are null.
     *
     * @throws IndexOutOfBoundsException If the specified index
     *                                   is less than 0L or greater than
     *                                   or equal to the length
     *                                   of the container.
     */
    public abstract ELEMENT at (
            CONTAINER container,
            INDEX index
            )
        throws NullPointerException,
               IndexOutOfBoundsException;


    /**
     * <p>
     * Creates an empty container of the specified length
     * with the specified element_class, which might or might not
     * be the same as <code> elementClass () </code>.
     * </p>
     *
     * <p>
     * Used, for example, to create a container of indices from
     * a container of elements, and so on.
     * </p>
     *
     * <p>
     * For example:
     * </p>
     *
     * <pre>
     *     final String [] string_array = new String [ 42 ];
     *     final ArrayExtractor&lt;String&gt; extractor =
     *         new ArrayExtractor&lt;String&gt; ( String.class );
     *     final Integer [] indices =
     *         extractor.create ( Integer.class,
     *                            (long) string_array.length );
     *     for ( int i = 0; i &lt; indices.length; i ++ )
     *     {
     *         indices [ i ] = i;
     *     }
     * </pre>
     *
     * @param element_class The class of elements for the new container.
     *                      Must not be null.
     *
     * @param length The length of the container to create.  Must be
     *               greater than or equal to 0L.
     *
     * @return The newly created container.  Never null.
     *
     * @throws NullPointerException If the specified container is null.
     *
     * @throws ElementsLengthException If the specified length
     *                                 is longer than the maximum
     *                                 length supported by this
     *                                 container extractor.  For example,
     *                                 if a container of length Long.MAX_VALUE
     *                                 is requested, but this
     *                                 Extractor can only handle containers
     *                                 of Integer.MAX_VALUE length, then
     *                                 an ElementsLengthException will
     *                                 be thrown.
     */
    public abstract <NEW_CONTAINER extends Object, NEW_ELEMENT extends Object>
        NEW_CONTAINER create (
            Class<NEW_ELEMENT> element_class,
            long length
            )
        throws NullPointerException,
               ElementsLengthException;


    /**
     * <p>
     * Creates an exact copy of the specified container.
     * </p>
     *
     * @param container The container to be duplicated.
     *                  Must not be null.
     *
     * @return A new container with exactly the same elements as the
     *         specified one.  Never null.
     *
     * @throws NullPointerException If the specified container is null.
     */
    public abstract CONTAINER duplicate (
            CONTAINER container
            )
        throws NullPointerException;


    /**
     * @return The Class of an individual element in a container supported
     *         by this Extractor.  For example, if this Extractor
     *         supports <code> List&lt;String&gt; </code> containers,
     *         then <code> String.class </code> might be returned.
     *         Never null.
     */
    public abstract Class<ELEMENT> elementClass ();


    // Every Extractor must implement java.lang.Object#equals(java.lang.Object)


    /**
     * <p>
     * Either extracts the specified container as-is, or creates
     * a new container with the specified range(s) of the specified
     * Elements.
     * </p>
     *
     * <p>
     * If a "raw" container is requested
     * (<code> is_new_instance = false </code>), this Extractor will simply
     * return the specified Elements' raw container if: 1) it is of
     * the same type supported by this Extractor; and 2) the full range
     * of offsets in that container is specified (either no offsets,
     * or just 0L, or <code> { 0L, length - 1 } </code>, or any other
     * sequence of offsets covering the entire range of Elements).  Note that
     * some Elements' containers host elements outside their (start, end)
     * boundaries.  For example, a RangeOfElements' container might
     * host 16 elements, while the RangeOfElements only hosts 4 of the
     * middle elements.  In this case, to ensure the exact Elements
     * are returned, and nothing more, specify at least a start offset (0L).
     * </p>
     *
     * <p>
     * When a new instance is requested, this Extractor will always
     * create and populate a new container.
     * </p>
     *
     * @param elements The Elements from which to extract, which might
     *                 or might not have a container of the same type
     *                 handled by this extractor.  Must not be null.
     *
     * @param is_new_instance True to always return a new container instance;
     *                        false to return the raw container
     *                        from the specified elements, if possible.
     *                        Even if false is specified, a new instance
     *                        will be created any time the container types
     *                        or lengths do not match.  If the raw
     *                        container is requested and no
     *                        start_end_offset_pairs
     *                        are specified, then the container returned
     *                        can be exactly what is stored, which might or
     *                        might not be of the correct length.
     *                        For example, a RangeOfElements might have
     *                        a container of length 16, even though its
     *                        range is only 4 elements in the middle
     *                        of the container.  to ensure the exact
     *                        elements are extracted, and not just the
     *                        raw container, specified is_new_instance = true
     *                        OR specify a start offset of 0L.
     *
     * @param start_end_offset_pairs 0 or more offsets into the specified
     *                               Elements.  Must not be null.
     *
     * @throws NullPointerException If the specified elements
     *                              or the array of start_end_offset_pairs
     *                              is/are null.
     *
     * @throws IndexOutOfBoundsException If any of the specified offsets
     *                                   is beyond the end of the specified
     *                                   Elements
     *                                   (<code> elements.index ( offset ) == false</code>).
     *
     * @throws ElementsLengthException If the range of offsets requested
     *                                 would lead to a container from
     *                                 is longer than the maximum
     *                                 length supported by this
     *                                 container extractor.  For example,
     *                                 if all of the Elements are requested,
     *                                 and the specified elements are of
     *                                 length Long.MAX_VALUE, but this
     *                                 Extractor can only handle containers
     *                                 of Integer.MAX_VALUE length, then
     *                                 an ElementsLengthException will
     *                                 be thrown.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public abstract CONTAINER extract (
            Elements<ELEMENT> elements,
            boolean is_new_instance,
            long ... start_end_offset_pairs
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ElementsLengthException;



    /**
     * <p>
     * Filters the specified container's elements, in place if possible,
     * or creating a new container if necessary (such as for fixed-length
     * containers).
     * </p>
     *
     * @param container The container whose elements will be filtered.
     *                  Must not be null.
     *
     * @param filter The sieve used to filter out elements.
     *               Must not be null.
     *
     * @return The (in-place or new) filtered container.  Never null.
     *
     * @throws NullPointerException If the specified container
     *                              or filter is/are null.
     */
    public abstract CONTAINER filter (
            CONTAINER container,
            Filter<ELEMENT> filter
            )
        throws NullPointerException;


    // Every Extractor must implement java.lang.Object#hashCode()


    /**
     * <p>
     * Returns true if the specified container includes the specified offset,
     * false if not.
     * </p>
     *
     * @param container The container whose element will be returned.
     *                  Must not be null.
     *
     * @param index The index from which to return the element value.
     *              Must be a valid index in the specified container
     *              (<code> this.has ( container, index ) == true </code>).
     *              Must not be null.  Must not contain any null elements.
     *
     * @return True if the the specified container does contain
     *         the specified offset, false if not.
     *
     * @throws NullPointerException If the specified container
     *                              and/or index is/are null.
     */
    public abstract boolean has (
            CONTAINER container,
            INDEX index
            )
        throws NullPointerException;


    /**
     * <p>
     * Converts the specified offset(s) into index(ices) into the specified
     * container.
     * </p>
     *
     * <p>
     * For example, if the container is an array, then the offset 0L
     * might be converted to the index (int) 0.  Or if the container
     * is an array of length 42, then the offset
     * <code> Elements.LAST + 7L </code> might be converted into the
     * index (int) 34 (the 8th last element of the array).
     * Or if the container is a <code> Map&lt;String, File&gt; </code>,
     * then the offset <code> Elements.LAST </code> might be converted
     * into the alphabetically last filename of a directory, such
     * as <code> zawinskis_law.txt </code>.  And so on.
     * </p>
     *
     * @param container The container whose indices will be returned.
     *                  Must not be null.
     *
     * @param offsets The offset(s) from the beginning or end of the container.
     *                Each can be an offset from the start (0L or greater)
     *                or an offset from the end (Elements.LAST or greater).
     *                Each can be positive or negative.
     *
     * @return The index(ices) into the specified container,
     *         Never null.  Never contains any null elements.
     *
     * @throws NullPointerException If the specified container
     *                              and/or offsets is/are null.
     *
     * @throws IndexOutOfBoundsException If the container does not have
     *                                   one or more of the specified
     *                                   offset(s), or if Elements.AFTER_LAST
     *                                   or Elements.NONE is specified.
     */
    public abstract INDEX [] indices (
            CONTAINER container,
            long ... offsets
            )
        throws NullPointerException,
               IndexOutOfBoundsException;


    /**
     * <p>
     * Returns true if the specified container has no elements.
     * </p>
     *
     * @param container The container in question.  Must not be null.
     *
     * @return True if the specified container is 0-length;
     *         false if there are one or more elements in the container.
     *
     * @throws NullPointerException If the specified container is null.
     */
    public abstract boolean isEmpty (
            CONTAINER container
            )
        throws NullPointerException;


    /**
     * <p>
     * Returns true if the specified container has exactly one element.
     * </p>
     *
     * @param container The container in question.  Must not be null.
     *
     * @return True if the specified container has exactly one element
     *         (which could conceivably be null);
     *         false if there are either 0 or more than 1 elements
     *         in the container.
     *
     * @throws NullPointerException If the specified container is null.
     */
    public abstract boolean isSingleton (
            CONTAINER container
            )
        throws NullPointerException;


    /**
     * <p>
     * Creates a new Iterator for the specified container.
     * </p>
     *
     * <p>
     * The Iterator does NOT allow elements to be <code> remove () </code>d.
     * Invoking remove on the returned Iterator will induce an
     * <code> UnsupportedOperationException </code>.
     * </p>
     *
     * <p>
     * The elements iterated over CAN contain nulls.
     * </p>
     *
     * @param container The container whose elements will be iterated over.
     *                  Must not be null.
     *
     * @return The new Iterator.  Never null.
     *
     * @throws NullPointerException If the specified container is null.
     */
    public abstract Iterator<ELEMENT> iterator (
            CONTAINER container
            )
        throws NullPointerException;


    /**
     * <p>
     * Returns the length, in elements, of the specified container.
     * </p>
     *
     * @param container The container whose length will be returned.
     *                  Must not be null.
     *
     * @return The number of elements in the specified container.
     *         Always greater than or equal to 0L.
     *
     * @throws NullPointerException If the specified container is null.
     */
    public abstract long length (
            CONTAINER container
            )
        throws NullPointerException;


    /**
     * <p>
     * Converts the specified index(ices) into offset(s) from the start
     * of the specified container.
     * </p>
     *
     * <p>
     * For example, if the container is an array, then the index (int) 0
     * might be converted to the offset 0L.  Or if the container is
     * an array of length 42, then the index 34 might be converted to
     * the offset 34L.  Or if the container is a
     * <code> Map&lt;String, File&gt; </code>, then the alphabetically
     * last filename of a directory with 21 files, such as
     * <code> zawinskis_law.txt </code>, might be converted to the offset
     * 20L (the offset to the last element).  And so on.
     * </p>
     *
     * @param container The container whose offsets will be returned.
     *                  Must not be null.
     *
     * @param indices The index(ices) of the container.
     *                Each must be a valid index in the specified container
     *                (<code> this.has ( container, index ) == true </code>).
     *                Must not be null.  Must not contain any null elements.
     *
     * @return The offsets from the start of the specified container,
     *         Each offset is always 0L or greater.  Never null.
     *
     * @throws NullPointerException If the specified container
     *                              and/or indices and/or any individual
     *                              index is/are null.
     *
     * @throws IndexOutOfBoundsException If the container does not have
     *                                   one or more of the specified
     *                                   index(ices).
     */
    @SuppressWarnings("unchecked") // Heap pollution generic varargs.
    public abstract long [] offsets (
            CONTAINER container,
            INDEX ... indices
            )
        throws NullPointerException,
               IndexOutOfBoundsException;


    /**
     * <p>
     * Sets the element at the specified index of the specified container
     * to the specified value.
     * </p>
     *
     * @param container The container whose element will be set.
     *                  Must not be null.
     *
     * @param index The index from which to return the element value.
     *              Must be a valid index in the specified container
     *              (<code> this.has ( container, index ) == true </code>).
     *              Must not be null.  Must not contain any null elements.
     *
     * @param element_or_null The value of the element to be set.
     *                        Can be null.
     *
     * @return This Extractor.  Never null.
     *
     * @throws NullPointerException If the specified container is null.
     *
     * @throws IndexOutOfBoundsException If the specified index
     *                                   is less than 0L or greater than
     *                                   or equal to the length
     *                                   of the container.
     */
    public abstract Extractor<CONTAINER, INDEX, ELEMENT> set (
            CONTAINER container,
            INDEX index,
            ELEMENT element_or_null
            )
        throws NullPointerException,
               IndexOutOfBoundsException;


    /**
     * <p>
     * Sorts the container in-place, if possible, or creates a new
     * container with the sorted elements, if necessary.
     * </p>
     *
     * @param container The container to sort.  Must not be null.
     *
     * @param order The order into which the specified container
     *              will be sorted.  Must not be null.
     *
     * @return Either the specified container, if sorting in-place
     *         is possible; or a new, sorted container, if the specified
     *         container could not be sorted in-place.  Never null.
     *
     * @throws NullPointerException If the specified container
     *                              or order is/are null.
     */
    public abstract CONTAINER sort (
            CONTAINER container,
            Comparator<ELEMENT> order
            )
        throws NullPointerException;
}
