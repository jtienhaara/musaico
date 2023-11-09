package musaico.foundation.filter.stream;

import java.io.Serializable;


import musaico.foundation.filter.FilterState;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * A FilterStream that flips each child FilterStream's output
 * container FilterState before returning it during filtering
 * of elements.
 * </p>
 *
 * <p>
 * When <code> FilterStream.CONTINUE </code> or
 * <code> FilterStream.END </code> is returned by the child
 * FilterStream, it is returned as-is (its opposite is not
 * returned).
 * </p>
 *
 * <p>
 * The container FilterStates returned by the child FilterStream
 * during <code> filterStart () </code> and
 * <code> filterEnd () </code> are always left as-is (their
 * opposites are not returned).
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
public class StreamOppositeOutput<ELEMENT>
    implements FilterStream<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Handles all the opposite element filter states.
    private final FilterStream<ELEMENT> filterStream;


    /**
     * <p>
     * Creates a new StreamOppositeOutput.
     * </p>
     *
     * @param filter_stream The child FilterStream, which will
     *                      return container FilterStates during
     *                      filtering that will be flipped
     *                      to their opposites (except in the cases
     *                      of <code> FilterStream.CONTINUE </code>
     *                      and <code> FilterStream.END </code>).
     *                      If null, then StreamKeepAll is
     *                      used to accept every container.
     *                      DO NOT PASS NULL.
     */
    public StreamOppositeOutput (
            FilterStream<ELEMENT> filter_stream
            )
    {
        if ( filter_stream == null )
        {
            this.filterStream = new StreamKeepAll<ELEMENT> ();
        }
        else
        {
            this.filterStream = filter_stream;
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

        final StreamOppositeOutput<?> that = (StreamOppositeOutput<?>) object;
        if ( this.filterStream ==  null )
        {
            if ( that.filterStream != null )
            {
                return false;
            }
        }
        else if ( that.filterStream == null )
        {
            return false;
        }
        else if ( ! this.filterStream.equals ( that.filterStream ) )
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
            return FilterState.DISCARDED;
        }

        final FilterState container_filter_state =
            this.filterStream.filtered ( element,
                                         element_filter_state );

        if ( container_filter_state == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( container_filter_state == FilterStream.CONTINUE
                  || container_filter_state == FilterStream.END )
        {
            return container_filter_state;
        }

        final FilterState opposite = container_filter_state.opposite ();
        return opposite;
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
             || container_filter_state == FilterStream.CONTINUE
             || container_filter_state == FilterStream.END )
        {
            return FilterState.DISCARDED;
        }

        return this.filterStream.filterEnd ( container_filter_state );
    }


    /**
     * @see musaico.foundation.filter.stream.FilterStream#filterStart()
     */
    @Override
    public final FilterState filterStart ()
    {
        return this.filterStream.filterStart ();
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = ClassName.of ( this.getClass () ).hashCode ();
        if ( this.filterStream != null )
        {
            hash_code += this.filterStream.hashCode ();
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
