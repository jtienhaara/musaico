package musaico.foundation.filter.stream;

import java.io.Serializable;


import musaico.foundation.filter.FilterState;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * A FilterStream that directs kept elements to one FilterStream,
 * and discarded elements to another FilterStream.
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
public class FilterTee<ELEMENT>
    implements FilterStream<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The initial FilterState for filterStart ().
    private final FilterState startFilterState;

    // Handles all the kept elements (elements that match).
    private final FilterStream<ELEMENT> keptFilterStreamOrNull;

    // Handles all the discarded elements (elements that
    // do not match).
    private final FilterStream<ELEMENT> discardedFilterStreamOrNull;


    /**
     * <p>
     * Creates a new FilterTee to send all kept elements to one
     * FilterStream, and all discarded elements to the other.
     * </p>
     *
     * @param start_filter_state The FilterState to return when
     *                           <code> filterStart () </code>
     *                           is called.  If null, then
     *                           FilterState.KEPT will be used
     *                           by default.  DO NOT PASS NULL!
     *
     * @param kept_filter_stream_or_null The FilterStream to which kept elements
     *                                   will be streamed, or null to not stream
     *                                   kept elements.  Can be null.
     *
     * @param discarded_filter_stream_or_null The FilterStream to which discarded
     *                                        elements will be streamed, or null
     *                                        to not stream discarded elements.
     *                                        Can be null.
     */
    public FilterTee (
            FilterState start_filter_state,
            FilterStream<ELEMENT> kept_filter_stream_or_null,
            FilterStream<ELEMENT> discarded_filter_stream_or_null
            )
    {
        this.keptFilterStreamOrNull = kept_filter_stream_or_null;
        this.discardedFilterStreamOrNull = discarded_filter_stream_or_null;

        if ( start_filter_state == null )
        {
            this.startFilterState = FilterState.KEPT;
        }
        else
        {
            this.startFilterState = start_filter_state;
        }
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

        final FilterTee<?> that = (FilterTee<?>) object;
        if ( this.startFilterState ==  null )
        {
            if ( that.startFilterState != null )
            {
                return false;
            }
        }
        else if ( that.startFilterState == null )
        {
            return false;
        }
        else if ( ! this.startFilterState.equals ( that.startFilterState ) )
        {
            return false;
        }

        if ( this.keptFilterStreamOrNull ==  null )
        {
            if ( that.keptFilterStreamOrNull != null )
            {
                return false;
            }
        }
        else if ( that.keptFilterStreamOrNull == null )
        {
            return false;
        }
        else if ( ! this.keptFilterStreamOrNull.equals ( that.keptFilterStreamOrNull ) )
        {
            return false;
        }

        if ( this.discardedFilterStreamOrNull ==  null )
        {
            if ( that.discardedFilterStreamOrNull != null )
            {
                return false;
            }
        }
        else if ( that.discardedFilterStreamOrNull == null )
        {
            return false;
        }
        else if ( ! this.discardedFilterStreamOrNull.equals ( that.discardedFilterStreamOrNull ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.filter.stream.FilterStream#filtered(java.lang.Object, musaico.foundation.filter.FilterState)
     */
    @Override
    public final FilterState filtered (
            ELEMENT element,
            FilterState element_filter_state
            )
    {
        if ( element_filter_state == null
             || element_filter_state == FilterStream.CONTINUE
             || element_filter_state == FilterStream.END )
        {
            // Garbage in?  Discarded out.
            return FilterState.DISCARDED;
        }
        else if ( ! element_filter_state.isKept () )
        {
            // Stream to the discarded FilterStream.
            if ( this.discardedFilterStreamOrNull != null )
            {
                return this.discardedFilterStreamOrNull.filtered ( element,
                                                                   element_filter_state );
            }
        }
        else
        {
            // Stream to the kept FilterStream.
            if ( this.keptFilterStreamOrNull != null )
            {
                return this.keptFilterStreamOrNull.filtered ( element,
                                                              element_filter_state );
            }
        }

        return FilterStream.CONTINUE;
    }


    /**
     * @see musaico.foundation.filter.stream.FilterStream#filterEnd(musaico.foundation.filter.FilterState)
     */
    @Override
    public final FilterState filterEnd (
            FilterState container_filter_state
            )
        throws NullPointerException
    {
        if ( container_filter_state == null
             || ! container_filter_state.isKept ()
             || container_filter_state == FilterStream.CONTINUE
             || container_filter_state == FilterStream.END )
        {
            // End using the discarded FilterStream.
            if ( this.discardedFilterStreamOrNull != null )
            {
                return this.discardedFilterStreamOrNull.filterEnd ( container_filter_state );
            }
        }
        else
        {
            // End using the kept FilterStream.
            if ( this.keptFilterStreamOrNull != null )
            {
                return this.keptFilterStreamOrNull.filterEnd ( container_filter_state );
            }
        }

        // If the appropriate stream is null, return the container
        // filter state that was passed in to us.
        return container_filter_state;
    }


    /**
     * @see musaico.foundation.filter.stream.FilterStream#filterStart()
     */
    @Override
    public final FilterState filterStart ()
    {
        return this.startFilterState;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = ClassName.of ( this.getClass () ).hashCode ();
        if ( this.startFilterState != null )
        {
            hash_code += this.startFilterState.hashCode ();
        }
        if ( this.keptFilterStreamOrNull != null )
        {
            hash_code += this.keptFilterStreamOrNull.hashCode ();
        }
        if ( this.discardedFilterStreamOrNull != null )
        {
            hash_code += this.discardedFilterStreamOrNull.hashCode ();
        }

        return hash_code;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () );
    }
}
