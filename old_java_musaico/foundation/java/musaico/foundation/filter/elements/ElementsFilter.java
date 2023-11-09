package musaico.foundation.filter.elements;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.container.ContainerFilter;

import musaico.foundation.filter.stream.FilterStream;


/**
 * <p>
 * A Filter keeps or discards an entire container based on a filter
 * applied to all or some elements.
 * </p>
 *
 * <p>
 * For example, an ElementsFilter that keeps only positive ints
 * might organize an array of ints <code> { -3, 3, -2, 2, -1, 1, 0 } </code>
 * into a kept bucket of ints <code> 3, 2, 1 </code> and a discarded
 * bucket of ints <code> -3, -2, -1, 0 </code>.
 * </p>
 *
 * <p>
 * Or an ElementsFilter that keeps only Strings containing the digits
 * <code> '0' </code> to <code> '9' </code> might organize the
 * String <code> "A 42 duck!" </code> into kept String
 * <code> "42" </code> and discarded String <code> "A  duck!" </code>;
 * or it might organize the same String into kept chars
 * <code> '4', '2' </code> and discarded chars <code> 'A', ' ', ' ',
 * 'd', 'u', 'c', 'k', '!' </code>; and so on.
 * </p>
 *
 * <p>
 * The Filter(s) that operate on each individual element can be
 * retrieved by calling <code> filters () </code>.  For example,
 * an ElementsFilter that checks each element to make sure it is
 * not null might return <code> { NotNullFilter } </code>.
 * </p>
 *
 *
 * <p>
 * In Java, every Filter must be Serializable in order to play
 * nicely over RMI.
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
 * @see musaico.foundation.filter.elements.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.elements.MODULE#LICENSE
 */
public interface ElementsFilter<ELEMENT extends Object>
    extends ContainerFilter<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Given the specified container (array, Collection, and so on),
     * returns only those elements that lead to the container
     * being DISCARDED, if any such elements are in the container.
     * </p>
     *
     * <p>
     * Thus the following code:
     * </p>
     *
     * <p>
     * Has the same effect as the following (more verbose) code:
     * </p>
     *
     * <pre>
     *     final ElementsFilter<ELEM> elements_filter = ...;
     *     final ELEM [] container = ...;
     *
     *     final Collection<ELEM> discards =
     *         elements_filter.discards ( container,
     *                                    new ArrayList<ELEMENT> () );
     * </pre>
     *
     * <pre>
     *     final ElementsFilter<ELEM> elements_filter = ...;
     *     final ELEM [] container = ...;
     *
     *     final Collection<ELEM> discards =
     *         new ArrayList<ELEM> ();
     *
     *     final FilterStream<ELEM> collector =
     *         new FilteredCollection<ELEM> (
     *             null,       // kept_elements_or_null
     *             discards ); // discarded_elements_or_null
     *
     *     final FilterState container_filter_state =
     *         elements_filter.filter ( array, collector );
     * </pre>
     *
     * <p>
     * In both cases, the <code> discards </code> Collection
     * contains the elements that cause the container to be
     * DISCARDED (if any such elements exist).
     * </p>
     *
     * @param container The container (array, Collection, and
     *                  so on) whose discard elements
     *                  will be returned.  If null, then no
     *                  elements will be returned.
     *                  DO NOT PASS NULL.
     *
     * @param discards_or_null The Collection to add discard
     *                         elements to.  If null,
     *                         then a new ArrayList will be
     *                         created and returned.
     *
     * @return A Collection of discard elements.  Can be empty.
     *         Never null.
     */
    public abstract Collection<ELEMENT> discards (
            Object container,
            Collection<ELEMENT> discards_or_null
            );


    // In Java every ContainerFilter must implement
    // musaico.foundation.filter.Filter#filter(java.lang.Object)


    /**
     * <p>
     * Organizes the elements of the specified container whole into
     * the specified kept and discarded buckets of elements.
     * </p>
     *
     * <p>
     * If the specified <code> filter_stream </code> is null, then
     * this Filter can short-circuit the filtering when it determines
     * that the container will be kept or discarded regardless of
     * any elements it has not yet inspected.  For example, a
     * NoNulls filter might return DISCARDED the first time it
     * finds a null element.
     * </p>
     *
     * </p>
     * However when filter_stream is not null, this Filter will
     * continue processing elements either until it has passed
     * all the container's elements to the specified filter_stream,
     * or until both this filter and the specified filter_stream
     * agree to short-circuit.  Until the filter_stream returns
     * a kept or discarded (short circuit) result, this Filter
     * must continue stepping through elements.
     * </p>
     *
     * @param container The whole container whose elements will be
     *                  organized into kept and discarded buckets.
     *                  Can be null.
     *
     * @param filter_stream A FilterStream that can be used to
     *                      further process each kept or discarded
     *                      element.  For example, a FilteredCollection
     *                      can be used to gather up the kept
     *                      and discarded elements into new
     *                      containers.  If null, then no further
     *                      processing will be performed.
     *                      Can be null.
     *
     * @return The overall FilterState (as per Filter.filter ( ... )).
     *         Never null.
     *
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    public abstract FilterState filter (
            Object container,
            FilterStream<ELEMENT> filter_stream
            );


    // In Java every ContainerFilter must implement
    // musaico.foundation.filter.container.ContainerFilter#filterArray(java.lang.Object[])


    /**
     * <p>
     * Filters an array and organizes the elements of the specified
     * container whole into the specified kept and discarded buckets
     * of elements.
     * </p>
     *
     * @param container The whole container whose elements will be
     *                  organized into kept and discarded buckets.
     *                  Can be null.
     *
     * @param filter_stream A FilterStream that can be used to
     *                      further process each kept or discarded
     *                      element.  For example, a FilteredCollection
     *                      can be used to gather up the kept
     *                      and discarded elements into new
     *                      containers.  If null, then no further
     *                      processing will be performed.
     *                      Can be null.
     *
     * @return The overall FilterState (as per Filter.filter ( ... )).
     *         Never null.
     *
     * @see musaico.foundation.filter.elements.ElementsFilter#filter(java.lang.Object, musaico.foundation.filter.elements.FilterStream)
     */
    public abstract FilterState filterArray (
            ELEMENT [] container,
            FilterStream<ELEMENT> filter_stream
            );


    // In Java every ContainerFilter must implement
    // musaico.foundation.filter.container.ContainerFilter#filterCollection(java.util.Collection)


    /**
     * <p>
     * Filters a Collection and organizes the elements of the specified
     * container whole into the specified kept and discarded buckets
     * of elements.
     * </p>
     *
     * @param container The whole container whose elements will be
     *                  organized into kept and discarded buckets.
     *                  Can be null.
     *
     * @param filter_stream A FilterStream that can be used to
     *                      further process each kept or discarded
     *                      element.  For example, a FilteredCollection
     *                      can be used to gather up the kept
     *                      and discarded elements into new
     *                      containers.  If null, then no further
     *                      processing will be performed.
     *                      Can be null.
     *
     * @return The overall FilterState (as per Filter.filter ( ... )).
     *         Never null.
     *
     * @see musaico.foundation.filter.elements.ElementsFilter#filter(java.lang.Object, musaico.foundation.filter.elements.FilterStream)
     */
    public abstract FilterState filterCollection (
            Collection<ELEMENT> container,
            FilterStream<ELEMENT> filter_stream
            );


    // In Java every ContainerFilter must implement
    // musaico.foundation.filter.container.ContainerFilter#filterIterable(java.lang.Iterable)


    /**
     * <p>
     * Filters an Iterable and organizes the elements of the specified
     * container whole into the specified kept and discarded buckets
     * of elements.
     * </p>
     *
     * @param container The whole container whose elements will be
     *                  organized into kept and discarded buckets.
     *                  Can be null.
     *
     * @param filter_stream A FilterStream that can be used to
     *                      further process each kept or discarded
     *                      element.  For example, a FilteredCollection
     *                      can be used to gather up the kept
     *                      and discarded elements into new
     *                      containers.  If null, then no further
     *                      processing will be performed.
     *                      Can be null.
     *
     * @return The overall FilterState (as per Filter.filter ( ... )).
     *         Never null.
     *
     * @see musaico.foundation.filter.elements.ElementsFilter#filter(java.lang.Object, musaico.foundation.filter.elements.FilterStream)
     */
    public abstract FilterState filterIterable (
            Iterable<ELEMENT> container,
            FilterStream<ELEMENT> filter_stream
            );


    // In Java every ContainerFilter must implement
    // musaico.foundation.filter.container.ContainerFilter#filterSingleton(java.lang.Object)


    /**
     * <p>
     * Filters a single object and organizes the elements of the specified
     * container whole into the specified kept and discarded buckets
     * of elements.
     * </p>
     *
     * @param container The whole container whose elements will be
     *                  organized into kept and discarded buckets.
     *                  Can be null.
     *
     * @param filter_stream A FilterStream that can be used to
     *                      further process each kept or discarded
     *                      element.  For example, a FilteredCollection
     *                      can be used to gather up the kept
     *                      and discarded elements into new
     *                      containers.  If null, then no further
     *                      processing will be performed.
     *                      Can be null.
     *
     * @return The overall FilterState (as per Filter.filter ( ... )).
     *         Never null.
     *
     * @see musaico.foundation.filter.elements.ElementsFilter#filter(java.lang.Object, musaico.foundation.filter.elements.FilterStream)
     */
    public abstract FilterState filterSingleton (
            ELEMENT container,
            FilterStream<ELEMENT> filter_stream
            );


    /**
     * <p>
     * Given the specified container (array, Collection, and so on),
     * returns only those elements that lead to the container
     * being KEPT, if any such elements are in the container.
     * </p>
     *
     * <p>
     * Thus the following code:
     * </p>
     *
     * <p>
     * Has the same effect as the following (more verbose) code:
     * </p>
     *
     * <pre>
     *     final ElementsFilter<ELEM> elements_filter = ...;
     *     final ELEM [] container = ...;
     *
     *     final Collection<ELEM> keepers =
     *         elements_filter.keepers ( container,
     *                                   new ArrayList<ELEMENT> () );
     * </pre>
     *
     * <pre>
     *     final ElementsFilter<ELEM> elements_filter = ...;
     *     final ELEM [] container = ...;
     *
     *     final Collection<ELEM> keepers =
     *         new ArrayList<ELEM> ();
     *
     *     final FilterStream<ELEM> collector =
     *         new FilteredCollection<ELEM> (
     *             keepers, // kept_elements_or_null
     *             null );  // discarded_elements_or_null
     *
     *     final FilterState container_filter_state =
     *         elements_filter.filter ( array, collector );
     * </pre>
     *
     * <p>
     * In both cases, the <code> keepers </code> Collection
     * contains the elements that cause the container to be
     * KEPT (if any such elements exist).
     * </p>
     *
     * @param container The container (array, Collection, and
     *                  so on) whose keeper elements
     *                  will be returned.  If null, then no
     *                  elements will be returned.
     *                  DO NOT PASS NULL.
     *
     * @param keepers_or_null The Collection to add keeper
     *                        elements to.  If null,
     *                        then a new ArrayList will be
     *                        created and returned.
     *
     * @return A Collection of keeper elements.  Can be empty.
     *         Never null.
     */
    public abstract Collection<ELEMENT> keepers (
            Object container,
            Collection<ELEMENT> keepers_or_null
            );
}
