package musaico.foundation.filter.stream;

import java.io.Serializable;


import musaico.foundation.filter.FilterState;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Returns <code> FilterStream.END </code> when filtering, no matter its elements.
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
public class StreamEnd<ELEMENT extends Object>
    implements FilterStream<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The initial FilterState for filterStart ().
    private final FilterState startFilterState;


    /**
     * <p>
     * Creates a new StreamEnd.
     * </p>
     *
     * @param start_filter_state The FilterState to return when
     *                           <code> filterStart () </code>
     *                           is called.  If null, then
     *                           FilterState.KEPT will be used
     *                           by default.  DO NOT PASS NULL!
     */
    public StreamEnd (
            FilterState start_filter_state
            )
    {
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
        throws NullPointerException
    {
        if ( element_filter_state == null
             || element_filter_state == FilterStream.CONTINUE
             || element_filter_state == FilterStream.END )
        {
            return FilterState.DISCARDED;
        }
        else
        {
            return FilterStream.END;
        }
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
        else
        {
            return container_filter_state;
        }
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
        return ClassName.of ( this.getClass () ).hashCode ();
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
