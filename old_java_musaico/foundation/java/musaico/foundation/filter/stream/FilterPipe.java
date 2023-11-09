package musaico.foundation.filter.stream;

import java.io.Serializable;

import java.util.Arrays;


import musaico.foundation.filter.FilterState;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * A composite FilterStream, piping through one or more component
 * FilterStreams.
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
public class FilterPipe<ELEMENT>
    implements FilterStream<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Lock critical sections on this token:
    private final Serializable lock = new String ( "lock" );

    // The component FilterStream(s).  Each component FilterStream
    // takes each element and the FilterState for that one element
    // and returns a FilterState for the whole container.
    private final FilterStream<ELEMENT> [] filterStreams;

    // MUTABLE:
    // The last filter state returned by this FilterPipe,
    // or null if it has not even been started yet.
    // Use synchronized ( this.lock ) for critical sections.
    private FilterState containerFilterStateOrNull = null;

    // MUTABLE:
    // True if this FilterPipe should not process any
    // more elements; false if it is still determining
    // the FilterState of the whole container.
    // Use synchronized ( this.lock ) for critical sections.
    private boolean isShortCircuited = false;


    /**
     * <p>
     * Creates a new FilterPipe.
     * </p>
     *
     * @param filter_streams One or more FilterStreams through
     *                       which every element will pass.
     *                       If empty, or if null is passed,
     *                       then StreamKeepAll will be the one
     *                       filter stream in the pipe.  DO NOT
     *                       PASS NULL.  If any of the
     *                       FilterStreams are null, then they
     *                       will be removed from the pipe.
     *                       DO NOT PASS NULL FilterStreams.
     */
    @SafeVarargs
    @SuppressWarnings({"rawtypes", "unchecked", "varargs"}) // Generic array creation, cast,
    // generic varargs heap pollution.
    public FilterPipe (
            FilterStream<ELEMENT> ... filter_streams
            )
    {
        if ( filter_streams == null
             || filter_streams.length == 0 )
        {
            this.filterStreams = (FilterStream<ELEMENT> [])
                new FilterStream [] { new StreamKeepAll<ELEMENT> () };
        }
        else
        {
            int num_filter_streams = 0;
            for ( FilterStream<ELEMENT> filter_stream : filter_streams )
            {
                if ( filter_stream != null )
                {
                    num_filter_streams ++;
                }
            }

            this.filterStreams = (FilterStream<ELEMENT> [])
                new FilterStream [ num_filter_streams ];
            if ( num_filter_streams == filter_streams.length )
            {
                System.arraycopy ( filter_streams, 0,
                                   this.filterStreams, 0, num_filter_streams );
            }
            else
            {
                int fs = 0;
                for ( FilterStream<ELEMENT> filter_stream : filter_streams )
                {
                    if ( filter_stream != null )
                    {
                        this.filterStreams [ fs ] = filter_stream;
                        fs ++;
                    }
                }
            }
        }
    }


    /**
     * @return The last filter state returned
     *         by the FilterPipe.  If the
     *         FilterPipe has not yet been started,
     *         then it will be started, and the
     *         resulting FilterState returned.
     *         Never null.
     */
    public final FilterState containerFilterState ()
    {
        synchronized ( this.lock )
        {
            if ( this.containerFilterStateOrNull == null )
            {
                this.filterStart ();
            }

            return this.containerFilterStateOrNull;
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
            
        final FilterPipe<?> that = (FilterPipe<?>) object;
        if ( this.filterStreams == null )
        {
            if ( that.filterStreams != null )
            {
                return false;
            }
        }
        else if ( that.filterStreams == null )
        {
            return false;
        }
        else if ( ! Arrays.equals ( this.filterStreams, that.filterStreams ) )
        {
            return false;
        }

        final boolean this_is_short_circuited;
        final FilterState this_container_filter_state_or_null;
        synchronized ( this.lock )
        {
            this_is_short_circuited = this.isShortCircuited;
            this_container_filter_state_or_null = this.containerFilterStateOrNull;
        }

        final boolean that_is_short_circuited;
        final FilterState that_container_filter_state_or_null;
        synchronized ( that.lock )
        {
            that_is_short_circuited = that.isShortCircuited;
            that_container_filter_state_or_null = that.containerFilterStateOrNull;
        }

        if ( this_is_short_circuited != that_is_short_circuited )
        {
            return false;
        }

        if ( this_container_filter_state_or_null ==  null )
        {
            if ( that_container_filter_state_or_null != null )
            {
                return false;
            }
        }
        else if ( that_container_filter_state_or_null == null )
        {
            return false;
        }
        else if ( ! this_container_filter_state_or_null.equals ( that_container_filter_state_or_null ) )
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
        if ( element_filter_state == null )
        {
            synchronized ( this.lock )
            {
                this.containerFilterStateOrNull = FilterState.DISCARDED;
                this.isShortCircuited = true;
                return this.containerFilterStateOrNull;
            }
        }

        final FilterState old_container_filter_state;
        synchronized ( this.lock )
        {
            if ( this.containerFilterStateOrNull == null )
            {
                this.filterStart (); // Fill in the null value with the start state and return it.
            }
            else if ( this.isShortCircuited )
            {
                return this.containerFilterStateOrNull;
            }

            old_container_filter_state = this.containerFilterStateOrNull;
        }

        for ( FilterStream<ELEMENT> filter_stream : this.filterStreams )
        {
            final FilterState new_container_filter_state =
                filter_stream.filtered ( element, element_filter_state );
            new_container_filter_state.hashCode (); // Throws NullPointerException if filter stream returned null.

            if ( new_container_filter_state == FilterStream.END )
            {
                // Keep the initial container filter state.
                synchronized ( this.lock )
                {
                    this.isShortCircuited = true;
                    return old_container_filter_state;
                }
            }
            else if ( new_container_filter_state == FilterStream.CONTINUE )
            {
                // Keep filtering individual elements.
                continue;
            }
            else
            {
                // Short-circuite with the specified result FilterState.
                synchronized ( this.lock )
                {
                    this.containerFilterStateOrNull = new_container_filter_state;
                    this.isShortCircuited = true;
                }

                return new_container_filter_state;
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
        container_filter_state.hashCode (); // Throws NullPointerException if parameter is null.

        for ( FilterStream<ELEMENT> filter_stream : this.filterStreams )
        {
            container_filter_state =
                filter_stream.filterEnd ( container_filter_state );
            container_filter_state.hashCode (); // Throws NullPointerException if filter stream returned null.
        }

        synchronized ( this.lock )
        {
            this.isShortCircuited = true;
            this.containerFilterStateOrNull = container_filter_state;
        }

        return container_filter_state;
    }


    /**
     * @see musaico.foundation.filter.stream.FilterStream#filterStart()
     */
    @Override
    public final FilterState filterStart ()
    {
        FilterState container_filter_state = FilterState.KEPT; // Default if no component streams.
        for ( FilterStream<ELEMENT> filter_stream : this.filterStreams )
        {
            container_filter_state = filter_stream.filterStart ();
            container_filter_state.hashCode (); // Throws NullPointerException if filter stream returned null.
        }

        synchronized ( this.lock )
        {
            this.containerFilterStateOrNull = container_filter_state;
        }

        return container_filter_state;
    }


    /**
     * @return The FilterStream(s) that receive every element
     *         and the FilterState result of filtering that
     *         element, and returns a FilterState for the
     *         container.  The filter stream(s) can return
     *         FilterStream.CONTINUE to continue
     *         filtering individual elements, or
     *         FilterStream.END to return the
     *         start state, or any other FilterState to short
     *         circuit the element filtering and
     *         return the specified result for the
     *         whole container.  Creates a defensive copy.
     *         Never null.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" }) // Generic array creation, cast.
    public FilterStream<ELEMENT> [] filterStreams ()
    {
        final FilterStream<ELEMENT> [] filter_streams =
            (FilterStream<ELEMENT> [])
            new FilterStream [ this.filterStreams.length ];
        System.arraycopy ( this.filterStreams, 0,
                           filter_streams, 0, this.filterStreams.length );
        return filter_streams;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = ClassName.of ( this.getClass () ).hashCode ()
            * Arrays.hashCode ( this.filterStreams );
        return hash_code;
    }


    /**
     * @return True if the filter stream should not
     *         process any more elements; false if it
     *         is still determining the FilterState
     *         of the whole container.
     */
    public final boolean isShortCircuited ()
    {
        synchronized ( this.lock )
        {
            return this.isShortCircuited;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass ()
                              + " ( " + Arrays.toString ( this.filterStreams ) + " )" );
    }
}
