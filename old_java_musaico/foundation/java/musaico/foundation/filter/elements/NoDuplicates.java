package musaico.foundation.filter.elements;

import java.io.Serializable;

import java.util.Collection;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


import musaico.foundation.structure.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;
import musaico.foundation.filter.KeepAll;

import musaico.foundation.filter.container.AbstractContainerFilter;

import musaico.foundation.filter.number.GreaterThanOrEqualToZero;
import musaico.foundation.filter.number.LessThanOrEqualToZero;

import musaico.foundation.filter.stream.FilterPipe;
import musaico.foundation.filter.stream.FilterStream;


/**
 * <p>
 * Discards any container that contains two or more instances of the
 * same element.
 * </p>
 *
 * <p>
 * In Java, <code> hashCode () </code> and <code> equals ( ... ) </code>
 * are used to determine duplicates.
 * </p>
 *
 * <p>
 * A Set, by definition, will always be KEPT by this filter.
 * Whereas a List with, say, two instances of the String
 * "duplicate" will be DISCARDED.
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
public class NoDuplicates<ELEMENT extends Object>
    extends AbstractElementsFilter<ELEMENT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


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
     * @see musaico.foundation.filter.elements.ElementsFilter#filterArray(java.lang.Object[], musaico.foundation.filter.elements.FilterStream)
     */
    @Override
    public final FilterState filterArray (
            ELEMENT [] container,
            FilterStream<ELEMENT> filter_stream
            )
    {
        if ( container == null )
        {
            return FilterState.DISCARDED;
        }

        try
        {
            FilterState container_filter_state = FilterState.KEPT;
            boolean is_container_short_circuited = false;

            final FilterPipe<ELEMENT> external_filter_pipe;
            FilterState external_filter_state;
            if ( filter_stream == null )
            {
                external_filter_pipe = null;
                external_filter_state = null;
            }
            else
            {
                external_filter_pipe = 
                    new FilterPipe<ELEMENT> ( filter_stream );
                external_filter_state =
                    external_filter_pipe.filterStart ();
            }

            boolean contains_null = false;
            final Set<ELEMENT> unique_elements = new HashSet<ELEMENT> ();
            for ( ELEMENT element : container )
            {
                final FilterState element_filter_state;
                if ( element == null )
                {
                    if ( contains_null )
                    {
                        element_filter_state = FilterState.DISCARDED;
                        container_filter_state = FilterState.DISCARDED;
                        is_container_short_circuited = true;
                    }
                    else
                    {
                        element_filter_state = FilterState.KEPT;
                    }
                }
                else if ( unique_elements.contains ( element ) )
                {
                    element_filter_state = FilterState.DISCARDED;
                    container_filter_state = FilterState.DISCARDED;
                    is_container_short_circuited = true;
                }
                else
                {
                    element_filter_state = FilterState.KEPT;
                }

                if ( external_filter_pipe == null )
                {
                    // No external filter stream.
                    if ( is_container_short_circuited )
                    {
                        break;
                    }
                }
                else
                {
                    // Is the external filter stream short circuited?
                    external_filter_state =
                        external_filter_pipe.filtered ( element,
                                                        element_filter_state );
                    if ( is_container_short_circuited
                         && external_filter_pipe.isShortCircuited () )
                    {
                        break;
                    }
                }
                // else CONTINUE.
            }

            if ( external_filter_pipe != null )
            {
                external_filter_pipe.filterEnd ( external_filter_state );
            }

            return container_filter_state;
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
            FilterStream<ELEMENT> filter_stream
            )
    {
        if ( container == null )
        {
            return FilterState.DISCARDED;
        }

        try
        {
            FilterState container_filter_state = FilterState.KEPT;
            boolean is_container_short_circuited = false;

            final FilterPipe<ELEMENT> external_filter_pipe;
            FilterState external_filter_state;
            if ( filter_stream == null )
            {
                external_filter_pipe = null;
                external_filter_state = null;
            }
            else
            {
                external_filter_pipe = 
                    new FilterPipe<ELEMENT> ( filter_stream );
                external_filter_state =
                    external_filter_pipe.filterStart ();
            }

            boolean contains_null = false;
            final Set<ELEMENT> unique_elements = new HashSet<ELEMENT> ();
            for ( ELEMENT element : container )
            {
                final FilterState element_filter_state;
                if ( element == null )
                {
                    if ( contains_null )
                    {
                        element_filter_state = FilterState.DISCARDED;
                        container_filter_state = FilterState.DISCARDED;
                        is_container_short_circuited = true;
                    }
                    else
                    {
                        element_filter_state = FilterState.KEPT;
                    }
                }
                else if ( unique_elements.contains ( element ) )
                {
                    element_filter_state = FilterState.DISCARDED;
                    container_filter_state = FilterState.DISCARDED;
                    is_container_short_circuited = true;
                }
                else
                {
                    element_filter_state = FilterState.KEPT;
                }

                if ( external_filter_pipe == null )
                {
                    // No external filter stream.
                    if ( is_container_short_circuited )
                    {
                        break;
                    }
                }
                else
                {
                    // Is the external filter stream short circuited?
                    external_filter_state =
                        external_filter_pipe.filtered ( element,
                                                        element_filter_state );
                    if ( is_container_short_circuited
                         && external_filter_pipe.isShortCircuited () )
                    {
                        break;
                    }
                }
                // else CONTINUE.
            }

            if ( external_filter_pipe != null )
            {
                external_filter_pipe.filterEnd ( external_filter_state );
            }

            return container_filter_state;
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
            FilterStream<ELEMENT> filter_stream
            )
    {
        if ( container == null )
        {
            return FilterState.DISCARDED;
        }

        try
        {
            FilterState container_filter_state = FilterState.KEPT;
            boolean is_container_short_circuited = false;

            final FilterPipe<ELEMENT> external_filter_pipe;
            FilterState external_filter_state;
            if ( filter_stream == null )
            {
                external_filter_pipe = null;
                external_filter_state = null;
            }
            else
            {
                external_filter_pipe = 
                    new FilterPipe<ELEMENT> ( filter_stream );
                external_filter_state =
                    external_filter_pipe.filterStart ();
            }

            boolean contains_null = false;
            final Set<ELEMENT> unique_elements = new HashSet<ELEMENT> ();
            for ( ELEMENT element : container )
            {
                final FilterState element_filter_state;
                if ( element == null )
                {
                    if ( contains_null )
                    {
                        element_filter_state = FilterState.DISCARDED;
                        container_filter_state = FilterState.DISCARDED;
                        is_container_short_circuited = true;
                    }
                    else
                    {
                        element_filter_state = FilterState.KEPT;
                    }
                }
                else if ( unique_elements.contains ( element ) )
                {
                    element_filter_state = FilterState.DISCARDED;
                    container_filter_state = FilterState.DISCARDED;
                    is_container_short_circuited = true;
                }
                else
                {
                    element_filter_state = FilterState.KEPT;
                }

                if ( external_filter_pipe == null )
                {
                    // No external filter stream.
                    if ( is_container_short_circuited )
                    {
                        break;
                    }
                }
                else
                {
                    // Is the external filter stream short circuited?
                    external_filter_state =
                        external_filter_pipe.filtered ( element,
                                                        element_filter_state );
                    if ( is_container_short_circuited
                         && external_filter_pipe.isShortCircuited () )
                    {
                        break;
                    }
                }
                // else CONTINUE.
            }

            if ( external_filter_pipe != null )
            {
                external_filter_pipe.filterEnd ( external_filter_state );
            }

            return container_filter_state;
        }
        catch ( Exception e )
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see musaico.foundation.filter.elements.ElementsFilter#filterSingleton(java.lang.Object, musaico.foundation.filter.elements.FilterStream)
     */
    @Override
    public final FilterState filterSingleton (
            ELEMENT container,
            FilterStream<ELEMENT> filter_stream
            )
    {
        try
        {
            if ( filter_stream != null )
            {
                final ELEMENT element = container;
                FilterState external_filter_state =
                    filter_stream.filterStart ();
                external_filter_state =
                        filter_stream.filtered ( element,
                                                 FilterState.KEPT );
                filter_stream.filterEnd ( external_filter_state );
            }

            return FilterState.KEPT;
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
        return ClassName.of ( this.getClass () ).hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () );
    }
}
