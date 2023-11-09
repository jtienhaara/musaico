package musaico.foundation.filter.elements;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.structure.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;
import musaico.foundation.filter.KeepAll;

import musaico.foundation.filter.stream.FilterStream;
import musaico.foundation.filter.stream.StreamKeepAll;


/**
 * <p>
 * Filters each container by applying a numeric filter to all its
 * indices.
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add new filters to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
 *
 *
 * <p>
 * In Java every Filter must be Serializable in order to play nicely
 * across RMI.
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
public class AllIndicesFilter<ELEMENT extends Object>
    extends AbstractElementsFilter<ELEMENT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The filter that works on each individual index of
    // each container.
    private final Filter<Long> indexQualifier;

    // The filter that works on each indexed element of each
    // container.
    private final Filter<ELEMENT> elementQualifier;

    // Determines whether the whole container is kept or discarded
    // based on the stream of FilterStates from the individual
    // indexed elements.  If either an index or its element
    // is discarded, then that whole indexed element is treated
    // as discarded.  Otherwise the indexed element is treated
    // as kept.  The quantifier itself works on indices (Longs).
    private final FilterStream<Long> quantifier;


    /**
     * <p>
     * Creates a new AllIndicesFilter.
     * </p>
     *
     * @param index_qualifier The filter that works on each individual
     *                        index of each container.  If null, then
     *                        a KeepAll filter is used instead to match
     *                        every index.  (Do not pass null!)
     *                        @see musaico.foundation.filter.KeepAll
     *
     * @param element_qualifier The filter that works on each element
     *                          corresponding to each index of each
     *                          container.  If null, then
     *                          a KeepAll filter is used instead to match
     *                          every index.  (Do not pass null!)
     *                          @see musaico.foundation.filter.KeepAll
     *
     * @param quantifier Determines whether the whole container
     *                   is kept or discarded based on the stream
     *                   of FilterStates from the individual
     *                   indexed elements.  For example, a
     *                   KeptAllElements can be passed to only
     *                   keep containers whose indexed elements
     *                   are all kept by both qualifiers;
     *                   or KeptSomeElements can be passed
     *                   to ensure at least one indexed element
     *                   of each container is kept;
     *                   or KeptNoElements will ensure that all
     *                   indexed elements of each container
     *                   are discarded in order for the whole
     *                   container to be kept.  And so on.
     *                   If null then StreamKeepAll will keep
     *                   every container.  (Do not pass null!)
     */
    public AllIndicesFilter (
            Filter<Long> index_qualifier,
            Filter<ELEMENT> element_qualifier,
            FilterStream<Long> quantifier
            )
    {
        if ( index_qualifier == null )
        {
            this.indexQualifier = new KeepAll<Long> ();
        }
        else
        {
            this.indexQualifier = index_qualifier;
        }

        if ( element_qualifier == null )
        {
            this.elementQualifier = new KeepAll<ELEMENT> ();
        }
        else
        {
            this.elementQualifier = element_qualifier;
        }

        if ( quantifier == null )
        {
            this.quantifier = new StreamKeepAll<Long> ();
        }
        else
        {
            this.quantifier = quantifier;
        }
    }


    /**
     * @return The filter on individual elements.  Never null.
     */
    public final Filter<ELEMENT> elementQualifier ()
    {
        return this.elementQualifier;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
            Object object
            )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final AllIndicesFilter<?> that = (AllIndicesFilter<?>) object;
        if ( this.indexQualifier ==  null )
        {
            if ( that.indexQualifier != null )
            {
                return false;
            }
        }
        else if ( that.indexQualifier == null )
        {
            return false;
        }
        else if ( ! this.indexQualifier.equals ( that.indexQualifier ) )
        {
            return false;
        }

        if ( this.elementQualifier ==  null )
        {
            if ( that.elementQualifier != null )
            {
                return false;
            }
        }
        else if ( that.elementQualifier == null )
        {
            return false;
        }
        else if ( ! this.elementQualifier.equals ( that.elementQualifier ) )
        {
            return false;
        }

        if ( this.quantifier ==  null )
        {
            if ( that.quantifier != null )
            {
                return false;
            }
        }
        else if ( that.quantifier == null )
        {
            return false;
        }
        else if ( ! this.quantifier.equals ( that.quantifier ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.filter.elements.ElementsFilter#filterArray(java.lang.Object[], musaico.foundation.filter.elements.FilterStream)
     */
    @Override
    public final FilterState filterArray (
            ELEMENT [] container,
            FilterStream<ELEMENT> external_filter_stream
            )
    {
        if ( container == null )
        {
            return FilterState.DISCARDED;
        }

        try
        {
            FilterState container_filter_state = this.quantifier.filterStart ();

            if ( external_filter_stream != null )
            {
                external_filter_stream.filterStart ();
            }

            long index = 0L;
            for ( ELEMENT element : container )
            {
                final FilterState index_filter_state =
                    this.filterOneIndex ( index,
                                          element,
                                          external_filter_stream );

                if ( index_filter_state == null )
                {
                    return FilterState.DISCARDED;
                }
                else if ( index_filter_state == FilterStream.END )
                {
                    break;
                }
                else if ( index_filter_state != FilterStream.CONTINUE )
                {
                    container_filter_state = index_filter_state;
                    break;
                }
                // else just CONTINUE.

                index ++;
            }

            final FilterState end_container_filter_state =
                this.quantifier.filterEnd ( container_filter_state );

            if ( external_filter_stream != null )
            {
                external_filter_stream.filterEnd ( container_filter_state );
            }

            return end_container_filter_state;
        }
        catch ( Exception e )
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see musaico.foundation.filter.elements.ElementsFilter#filterCollection(java.util.Collection, musaico.foundation.filter.elements.FilterStream)
     */
    @Override
    public final FilterState filterCollection (
            Collection<ELEMENT> container,
            FilterStream<ELEMENT> external_filter_stream
            )
    {
        if ( container == null )
        {
            return FilterState.DISCARDED;
        }

        try
        {
            FilterState container_filter_state = this.quantifier.filterStart ();

            if ( external_filter_stream != null )
            {
                external_filter_stream.filterStart ();
            }

            long index = 0L;
            for ( ELEMENT element : container )
            {
                final FilterState index_filter_state =
                    this.filterOneIndex ( index,
                                          element,
                                          external_filter_stream );

                if ( index_filter_state == null )
                {
                    return FilterState.DISCARDED;
                }
                else if ( index_filter_state == FilterStream.END )
                {
                    break;
                }
                else if ( index_filter_state != FilterStream.CONTINUE )
                {
                    container_filter_state = index_filter_state;
                    break;
                }
                // else just CONTINUE.

                index ++;
            }

            final FilterState end_container_filter_state =
                this.quantifier.filterEnd ( container_filter_state );

            if ( external_filter_stream != null )
            {
                external_filter_stream.filterEnd ( container_filter_state );
            }

            return end_container_filter_state;
        }
        catch ( Exception e )
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see musaico.foundation.filter.elements.ElementsFilter#filterIterable(java.lang.Iterable, musaico.foundation.filter.elements.FilterStream)
     */
    @Override
    public final FilterState filterIterable (
            Iterable<ELEMENT> container,
            FilterStream<ELEMENT> external_filter_stream
            )
    {
        if ( container == null )
        {
            return FilterState.DISCARDED;
        }

        try
        {
            FilterState container_filter_state = this.quantifier.filterStart ();

            if ( external_filter_stream != null )
            {
                external_filter_stream.filterStart ();
            }

            long index = 0L;
            for ( ELEMENT element : container )
            {
                final FilterState index_filter_state =
                    this.filterOneIndex ( index,
                                          element,
                                          external_filter_stream );

                if ( index_filter_state == null )
                {
                    return FilterState.DISCARDED;
                }
                else if ( index_filter_state == FilterStream.END )
                {
                    break;
                }
                else if ( index_filter_state != FilterStream.CONTINUE )
                {
                    container_filter_state = index_filter_state;
                    break;
                }
                // else just CONTINUE.

                index ++;
            }

            final FilterState end_container_filter_state =
                this.quantifier.filterEnd ( container_filter_state );

            if ( external_filter_stream != null )
            {
                external_filter_stream.filterEnd ( container_filter_state );
            }

            return end_container_filter_state;
        }
        catch ( Exception e )
        {
            return FilterState.DISCARDED;
        }
    }


    /*
     * <p>
     * Filters one index of a container.
     * </p>
     *
     * @param index The index of the one element to filter.
     *              Must be greater than or equal to 0L.
     *
     * @param element The one element to filter.  Can be null.
     *
     * @param external_filter_stream An optional FilterStream to process the
     *                               element and the FilterState it generates,
     *                               or null if no filter streaming is desired.
     *
     * @return The FilterState of the container: either 
     *         <code> FilterStream.CONTINUE </code>,
     *         or a short-circuited, final filter state such as
     *         <code> FilterState.KEPT </code>
     *         or <code> FilterState.DISCARDED </code>.
     *         Never null.
     *
     * @throws Exception Can throw any old exception.  Catch all
     *                   Exceptions, and deal with each one sensibly.
     */
    private final FilterState filterOneIndex (
            long index,
            ELEMENT element,
            FilterStream<ELEMENT> external_filter_stream
            )
        throws Exception
    {
        if ( index < 0L )
        {
            return FilterState.DISCARDED;
        }

        final FilterState index_filter_state =
            this.indexQualifier.filter ( index );

        if ( index_filter_state == null )
        {
            return FilterState.DISCARDED;
        }

        final FilterState indexed_element_filter_state;
        if ( index_filter_state.isDiscarded () )
        {
            indexed_element_filter_state = index_filter_state;
        }
        else
        {
            final FilterState element_filter_state =
                this.elementQualifier.filter ( element );

            if ( element_filter_state == null )
            {
                return FilterState.DISCARDED;
            }

            indexed_element_filter_state = element_filter_state;
        }

        // The quantifier works on the index...
        final FilterState container_filter_state =
            this.quantifier.filtered ( index, indexed_element_filter_state );

        if ( external_filter_stream != null )
        {
            // ...But the external filter stream works on the element.
            external_filter_stream.filtered ( element, indexed_element_filter_state );
        }

        return container_filter_state;
    }


    /**
     * @see musaico.foundation.filter.elements.ElementsFilter#filterSingleton(java.lang.Object, musaico.foundation.filter.elements.FilterStream)
     */
    @Override
    public final FilterState filterSingleton (
            ELEMENT container,
            FilterStream<ELEMENT> external_filter_stream
            )
    {
        try
        {
            FilterState container_filter_state = this.quantifier.filterStart ();

            if ( external_filter_stream != null )
            {
                external_filter_stream.filterStart ();
            }

            final ELEMENT element = container;
            final FilterState index_filter_state =
                this.filterOneIndex ( 0L, // index
                                      element,
                                      external_filter_stream );

            if ( index_filter_state == null )
            {
                return FilterState.DISCARDED;
            }
            else if ( index_filter_state != FilterStream.END
                      && index_filter_state != FilterStream.CONTINUE )
            {
                container_filter_state = index_filter_state;
            }

            final FilterState end_container_filter_state =
                this.quantifier.filterEnd ( container_filter_state );

            if ( external_filter_stream != null )
            {
                external_filter_stream.filterEnd ( container_filter_state );
            }

            return end_container_filter_state;
        }
        catch ( Exception e )
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return ClassName.of ( this.getClass () ).hashCode ()
            * this.indexQualifier.hashCode ()
            * this.elementQualifier.hashCode ()
            + this.quantifier.hashCode ();
    }


    /**
     * @return The filter on individual indices.  Never null.
     */
    public final Filter<Long> indexQualifier ()
    {
        return this.indexQualifier;
    }


    /**
     * @return The FilterStream for this container filter.
     *         Determines whether the whole container is kept or discarded
     *         based on the stream of FilterStates from the individual
     *         indexed elements.  Never null.
     */
    public final FilterStream<Long> quantifier ()
    {
        return this.quantifier;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " [ " + this.indexQualifier
            + ", " + this.elementQualifier
            + ", " + this.quantifier + " ]";
    }
}
