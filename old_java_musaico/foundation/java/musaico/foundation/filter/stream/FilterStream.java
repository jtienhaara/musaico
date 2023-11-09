package musaico.foundation.filter.stream;

import java.io.Serializable;


import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Reads elements and their FilterStates as an ElementsFilter is
 * stepping through, keeping or discarding one element at a time.
 * </p>
 *
 * <p>
 * A FilterStream can be used to capture the kept and/or discarded
 * elements in order to build a new (filtered) container, or as a
 * search operator to find the first element matching an element
 * filter, and so on.
 * </p>
 *
 *
 * <p>
 * In Java, every FilterStream must be Serializable in order to play
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
 * @see musaico.foundation.filter.stream.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.stream.MODULE#LICENSE
 */
public interface FilterStream<ELEMENT extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * An intermediate FilterState, neither KEPT nor DISCARDED,
     * but just CONTINUE filtering the elements of the container.
     * </p>
     */
    public static final FilterState CONTINUE =
        new FilterState ( "CONTINUE", false );


    /**
     * <p>
     * A FilterState indicating that all elements of a container
     * have been filtered individually, and now it is time to
     * decide whether the whole container should be KEPT
     * or DISCARDED.
     * </p>
     */
    public static final FilterState END =
        new FilterState ( "END", false );


    /**
     * <p>
     * Accepts an element that has been kept or discarded
     * by an ElementsFilter.
     * </p>
     *
     * @param element The element that has been either
     *                kept or discarded.  Can be null, since null
     *                elements can appear in containers.
     *
     * @param element_filter_state Whether the element was
     *                              KEPT or DISCARDED or some other
     *                              FilterState.  Must not be null.
     *
     * @return Either a new FilterState for the container as a whole
     *         to pass to filterEnd (), such as FilterState.KEPT
     *         or FilterState.DISCARDED, or ElementsFilter.CONTINUE
     *         to continue filtering elements, or ElementsFilter.END
     *         to abort filtering elements and immediately invoke
     *         <code> filterEnd () </code> on this stream with the
     *         filter state returned by <code> filterStart () </code>.
     *         If null then the caller must treat the result as
     *         FilterState.DISCARDED for the whole container.
     *         (Do not return null!)
     *
     * @throws NullPointerException If the specified
     *                              element_filter_state is null.
     */
    public abstract FilterState filtered (
            ELEMENT element,
            FilterState element_filter_state
            )
        throws NullPointerException;


    /**
     * <p>
     * Returns the FilterState for the whole container,
     * given the specified most recent element FilterState.
     * </p>
     *
     * @param container_filter_state The FilterState before ending.
     *                               Either the FilterState returned
     *                               by <code> filterStart () </code>,
     *                               or the most recent FilterState
     *                               returned by <code> filtered () </code>
     *                               that was neither ElementsFilter.CONTINUE
     *                               nor ElementsFilter.END.
     *                               Must not be null.
     *
     * @return The final FilterState of the whole container
     *         whose elements were filtered.
     *         If null or ElementsFilter.CONTINUE
     *         or ElementsFilter.END then the caller must treat
     *         the result as FilterState.DISCARDED for the whole
     *         container.  (Do not return null!)
     *
     * @throws NullPointerException If the specified
     *                              container_filter_state is null.
     */
    public abstract FilterState filterEnd (
            FilterState container_filter_state
            )
        throws NullPointerException;


    /**
     * <p>
     * Returns the initial FilterState for the whole container.
     * </p>
     *
     * @return The initial FilterState of the whole container
     *         whose elements will be filtered.
     *         If no elements are in the container, then this
     *         will be the final filter state for the container.
     *         If null or ElementsFilter.CONTINUE
     *         or ElementsFilter.END then the caller must treat
     *         the result as FilterState.DISCARDED for the whole
     *         container.  (Do not return null!)
     */
    public abstract FilterState filterStart ();
}
