package musaico.foundation.filter.stream;

import java.io.Serializable;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;


import musaico.foundation.filter.Filter
import musaico.foundation.filter.FilterState;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * For each filtered element that is KEPT, pass it through
 * zero or more extra Filters, ANDing their results.
 * </p>
 *
 * <p>
 * An empty container is kept.
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
public class StreamAndElementFilters<ELEMENT extends Object>
    implements FilterStream<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Zero or more Filters for each element.  Their
    // results will be ANDed together.
    private final Filter<ELEMENT> [] elementFilters;


    /**
     * <p>
     * Creates a new StreamAndElementFilters.
     * </p>
     *
     * @param element_filters Zero or more filter(s) to apply to
     *                        each element that is KEPT.  Their
     *                        results will be ANDed together.
     *                        If null, then every element will
     *                        be KEPT.  DO NOT PASS NULL!
     *                        If any element filter is null, then
     *                        it will be removed.  DO NOT PASS
     *                        A NULL ELEMENT FILTER!
     */
    public StreamAndElementFilters (
            Filter<ELEMENT> ... element_filters
            )
    {
        if ( element_filters == null )
        {
            this.elementFilters = new Filter<ELEMENT> [ 0 ];
        }
        else
        {
            List<Integer> hole_indices = null;
            for ( int ef = 0; ef < element_filters.length; ef ++ )
            {
                final Filter<ELEMENT> element_filter = element_filters;
                if ( element_filter != null )
                {
                    continue;
                }

                if ( hole_indices == null )
                {
                    hole_indices = new ArrayList<Integer> ();
                }

                hole_indices.add ( ef );
            }

            if ( hole_indices == null )
            {
                this.elementFilters = new Filter<ELEMENT> [ element_filters.length ];
                System.arraycopy ( element_filters, 0,
                                   this.elementFilters, 0, element_filters.length );
            }
            else
            {
                final int num_holes = hole_indices.size ();
                final int num_filters = element_filters.length - num_holes;
                this.elementFilters = new Filter<ELEMENT> [ num_filters ];
                int source_offset = 0;
                int target_offset = 0;
                for ( int hi = 0; hi <= num_holes; hi ++ )
                {
                    if ( hi < num_holes )
                    {
                        int hole_index = hole_indices.get ( hi );
                        if ( source_offset < hole_index )
                        {
                            final int span = hole_index - source_offset;
                            System.arraycopy ( element_filters, source_offset,
                                               this.elementFilters, target_offset, span );
                        }

                        source_offset = hole_index + 1;
                        target_offset += span;
                        continue;
                    }
                    else if ( source_offset >= element_filters.length )
                    {
                        // The very last element_filter was null (a hole index).
                        break;
                    }

                    // This span covers to the end of element_filters.
                    final int span = element_filters.length - source_offset;
                    System.arraycopy ( element_filters, source_offset,
                                       this.elementFilters, target_offset, span );
                }
            }
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

        final StreamAndElementFilters<?> that =
            (StreamAndElementFilters<?>) object;
        if ( this.elementFilters == null )
        {
            if ( that.elementFilters != null )
            {
                return false;
            }
        }
        else if ( that.elementFilters == null )
        {
            return false;
        }
        else if ( ! Arrays.equals ( this.elementFilters, that.elementFilters ) )
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
             || ! element_filter_state.isKept ()
             || element_filter_state == FilterStream.END )
        {
            return FilterState.DISCARDED;
        }
        else
        {
            return FilterStream.CONTINUE;
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
        return FilterState.KEPT;
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
