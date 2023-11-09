package musaico.foundation.filter.stream;

import java.io.Serializable;


import musaico.foundation.filter.FilterState;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * A FilterStream that flips each element FilterState before
 * sending it to a child FilterStream.
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
public class StreamOppositeInput<ELEMENT>
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
     * Creates a new StreamOppositeInput.
     * </p>
     *
     * @param filter_stream The child FilterStream, which will
     *                      accept the opposite of every element
     *                      FilterState during filtering.
     *                      If null, then StreamKeepAll is
     *                      used to accept every container.
     *                      DO NOT PASS NULL.  Doing so makes
     *                      you a bad person, and on your death
     *                      bed, you will cry out "I should
     *                      not have passed null!" but it will
     *                      be too late, you will be dead.  Euh.
     */
    public StreamOppositeInput (
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

        final StreamOppositeInput<?> that = (StreamOppositeInput<?>) object;
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

        final FilterState opposite = element_filter_state.opposite ();
        return this.filterStream.filtered ( element,
                                            opposite );
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
