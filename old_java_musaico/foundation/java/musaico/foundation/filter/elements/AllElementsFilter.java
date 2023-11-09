package musaico.foundation.filter.elements;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.structure.ClassName;
import musaico.foundation.structure.StringRepresentation;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;
import musaico.foundation.filter.KeepAll;

import musaico.foundation.filter.container.AbstractContainerFilter;

import musaico.foundation.filter.number.GreaterThanOrEqualToZero;
import musaico.foundation.filter.number.LessThanOrEqualToZero;

import musaico.foundation.filter.stream.FilterPipe;
import musaico.foundation.filter.stream.FilterStream;
import musaico.foundation.filter.stream.StreamKeepAll;


/**
 * <p>
 * A bare bones ElementsFilter.
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
public class AllElementsFilter<QUALIFIER extends Filter<ELEMENT>, ELEMENT extends Object>
    extends AbstractElementsFilter<ELEMENT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The filter that works on each individual element of
    // each container.
    private final QUALIFIER qualifier;

    // Determines whether the whole container is kept or discarded
    // based on the stream of FilterStates from the individual
    // elements.
    private final FilterStream<ELEMENT> quantifier;


    /**
     * <p>
     * Creates a new AllElementsFilter.
     * </p>
     *
     * @param qualifier The filter that works on each individual
     *                  element of each container.  Must not be null.
     *
     * @param quantifier Determines whether the whole container
     *                   is kept or discarded based on the stream
     *                   of FilterStates from the individual
     *                   elements.  For example, a KeptAllElements
     *                   can be passed to only keep containers
     *                   whose elements all are kept by the
     *                   qualifier; or KeptSomeElements
     *                   can be passed to ensure at least one
     *                   element of each container is kept;
     *                   or KeptNoElements will ensure that all
     *                   elements of each container are discarded
     *                   in order for the whole container
     *                   to be kept.  And so on.  If null
     *                   then StreamKeepAll will keep every
     *                   container.  (Do not pass null!)
     *
     * @throws NullPointerException If the specified qualifier
     *                              is null.
     */
    public AllElementsFilter (
            QUALIFIER qualifier,
            FilterStream<ELEMENT> quantifier
            )
    {
        if ( qualifier == null )
        {
            throw new NullPointerException (
                "ERROR "
                + ClassName.of ( this.getClass () )
                + " cannot be constructed with qualifier = null, quantifier = "
                + StringRepresentation.of (
                      quantifier,
                      StringRepresentation.DEFAULT_OBJECT_LENGTH ) );
        }
        else
        {
            this.qualifier = qualifier;
        }

        if ( quantifier == null )
        {
            this.quantifier = new StreamKeepAll<ELEMENT> ();
        }
        else
        {
            this.quantifier = quantifier;
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

        final AllElementsFilter<?, ?> that = (AllElementsFilter<?, ?>) object;
        if ( this.qualifier ==  null )
        {
            if ( that.qualifier != null )
            {
                return false;
            }
        }
        else if ( that.qualifier == null )
        {
            return false;
        }
        else if ( ! this.qualifier.equals ( that.qualifier ) )
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
        else if ( this.qualifier == null )
        {
            return FilterState.KEPT;
        }

        try
        {
            final FilterPipe<ELEMENT> quantifier_pipe =
                new FilterPipe<ELEMENT> ( this.quantifier );
            FilterState container_filter_state =
                quantifier_pipe.filterStart ();

            final FilterPipe<ELEMENT> external_filter_pipe;
            FilterState external_filter_state;
            if ( external_filter_stream == null )
            {
                external_filter_pipe = null;
                external_filter_state = null;
            }
            else
            {
                external_filter_pipe = 
                    new FilterPipe<ELEMENT> ( external_filter_stream );
                external_filter_state = external_filter_pipe.filterStart ();
            }

            for ( ELEMENT element : container )
            {
                final FilterState element_filter_state =
                    this.qualifier.filter ( element );

                final FilterState maybe_container_filter_state =
                    quantifier_pipe.filtered ( element,
                                               element_filter_state );
                if ( maybe_container_filter_state != FilterStream.CONTINUE
                     && maybe_container_filter_state != FilterStream.END )
                {
                    container_filter_state = maybe_container_filter_state;
                }

                if ( external_filter_pipe == null )
                {
                    // No external filter stream.
                    if ( quantifier_pipe.isShortCircuited () )
                    {
                        break;
                    }
                }
                else
                {
                    // Is the external filter stream short circuited?
                    final FilterState maybe_external_filter_state =
                        external_filter_pipe.filtered ( element,
                                                        element_filter_state );
                    if ( maybe_external_filter_state != FilterStream.CONTINUE
                         && maybe_external_filter_state != FilterStream.END )
                    {
                        external_filter_state = maybe_external_filter_state;
                    }

                    if ( quantifier_pipe.isShortCircuited ()
                         && external_filter_pipe.isShortCircuited () )
                    {
                        break;
                    }
                }
                // else CONTINUE.
            }

            final FilterState container_end_filter_state =
                quantifier_pipe.filterEnd ( container_filter_state );

            if ( external_filter_pipe != null )
            {
                external_filter_pipe.filterEnd ( external_filter_state );
            }

            return container_end_filter_state;
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
        else if ( this.qualifier == null )
        {
            return FilterState.KEPT;
        }

        try
        {
            final FilterPipe<ELEMENT> quantifier_pipe =
                new FilterPipe<ELEMENT> ( this.quantifier );
            FilterState container_filter_state =
                quantifier_pipe.filterStart ();

            final FilterPipe<ELEMENT> external_filter_pipe;
            FilterState external_filter_state;
            if ( external_filter_stream == null )
            {
                external_filter_pipe = null;
                external_filter_state = null;
            }
            else
            {
                external_filter_pipe = 
                    new FilterPipe<ELEMENT> ( external_filter_stream );
                external_filter_state =
                    external_filter_pipe.filterStart ();
            }

            for ( ELEMENT element : container )
            {
                final FilterState element_filter_state =
                    this.qualifier.filter ( element );

                final FilterState maybe_container_filter_state =
                    quantifier_pipe.filtered ( element,
                                               element_filter_state );
                if ( maybe_container_filter_state != FilterStream.CONTINUE
                     && maybe_container_filter_state != FilterStream.END )
                {
                    container_filter_state = maybe_container_filter_state;
                }

                if ( external_filter_pipe == null )
                {
                    // No external filter stream.
                    if ( quantifier_pipe.isShortCircuited () )
                    {
                        break;
                    }
                }
                else
                {
                    // Is the external filter stream short circuited?
                    final FilterState maybe_external_filter_state =
                        external_filter_pipe.filtered ( element,
                                                        element_filter_state );
                    if ( maybe_external_filter_state != FilterStream.CONTINUE
                         && maybe_external_filter_state != FilterStream.END )
                    {
                        external_filter_state = maybe_external_filter_state;
                    }

                    if ( quantifier_pipe.isShortCircuited ()
                         && external_filter_pipe.isShortCircuited () )
                    {
                        break;
                    }
                }
                // else CONTINUE.
            }

            final FilterState container_end_filter_state =
                quantifier_pipe.filterEnd ( container_filter_state );

            if ( external_filter_pipe != null )
            {
                external_filter_pipe.filterEnd ( external_filter_state );
            }

            return container_end_filter_state;
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
        else if ( this.qualifier == null )
        {
            return FilterState.KEPT;
        }

        try
        {
            final FilterPipe<ELEMENT> quantifier_pipe =
                new FilterPipe<ELEMENT> ( this.quantifier );
            FilterState container_filter_state =
                quantifier_pipe.filterStart ();

            final FilterPipe<ELEMENT> external_filter_pipe;
            FilterState external_filter_state;
            if ( external_filter_stream == null )
            {
                external_filter_pipe = null;
                external_filter_state = null;
            }
            else
            {
                external_filter_pipe = 
                    new FilterPipe<ELEMENT> ( external_filter_stream );
                external_filter_state =
                    external_filter_pipe.filterStart ();
            }

            for ( ELEMENT element : container )
            {
                final FilterState element_filter_state =
                    this.qualifier.filter ( element );

                final FilterState maybe_container_filter_state =
                    quantifier_pipe.filtered ( element,
                                               element_filter_state );
                if ( maybe_container_filter_state != FilterStream.CONTINUE
                     && maybe_container_filter_state != FilterStream.END )
                {
                    container_filter_state = maybe_container_filter_state;
                }

                if ( external_filter_pipe == null )
                {
                    // No external filter stream.
                    if ( quantifier_pipe.isShortCircuited () )
                    {
                        break;
                    }
                }
                else
                {
                    // Is the external filter stream short circuited?
                    final FilterState maybe_external_filter_state =
                        external_filter_pipe.filtered ( element,
                                                        element_filter_state );
                    if ( maybe_external_filter_state != FilterStream.CONTINUE
                         && maybe_external_filter_state != FilterStream.END )
                    {
                        external_filter_state = maybe_external_filter_state;
                    }

                    if ( quantifier_pipe.isShortCircuited ()
                         && external_filter_pipe.isShortCircuited () )
                    {
                        break;
                    }
                }
                // else CONTINUE.
            }

            final FilterState container_end_filter_state =
                quantifier_pipe.filterEnd ( container_filter_state );

            if ( external_filter_pipe != null )
            {
                external_filter_pipe.filterEnd ( external_filter_state );
            }

            return container_end_filter_state;
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
            FilterStream<ELEMENT> external_filter_stream
            )
    {
        if ( this.qualifier == null )
        {
            return FilterState.KEPT;
        }

        try
        {
            final FilterPipe<ELEMENT> quantifier_pipe =
                new FilterPipe<ELEMENT> ( this.quantifier );
            FilterState container_filter_state =
                quantifier_pipe.filterStart ();

            final FilterPipe<ELEMENT> external_filter_pipe;
            FilterState external_filter_state;
            if ( external_filter_stream == null )
            {
                external_filter_pipe = null;
                external_filter_state = null;
            }
            else
            {
                external_filter_pipe = 
                    new FilterPipe<ELEMENT> ( external_filter_stream );
                external_filter_state =
                    external_filter_pipe.filterStart ();
            }

            final ELEMENT element = container;

            final FilterState element_filter_state =
                    this.qualifier.filter ( element );

            final FilterState maybe_container_filter_state =
                quantifier_pipe.filtered ( element,
                                           element_filter_state );
            if ( maybe_container_filter_state != FilterStream.CONTINUE
                 && maybe_container_filter_state != FilterStream.END )
            {
                container_filter_state = maybe_container_filter_state;
            }

            if ( external_filter_pipe != null )
            {
                final FilterState maybe_external_filter_state =
                    external_filter_pipe.filtered ( element,
                                                    element_filter_state );
                if ( maybe_external_filter_state != FilterStream.CONTINUE
                     && maybe_external_filter_state != FilterStream.END )
                {
                    external_filter_state = maybe_external_filter_state;
                }
            }

            final FilterState container_end_filter_state =
                quantifier_pipe.filterEnd ( container_filter_state );

            if ( external_filter_pipe != null )
            {
                external_filter_pipe.filterEnd ( external_filter_state );
            }

            return container_end_filter_state;
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
            * ( this.qualifier == null
                ? 0
                : this.qualifier.hashCode () )
            + this.quantifier.hashCode ();
    }


    /**
     * @return The filter on individual elements.  Never null.
     */
    public final QUALIFIER qualifier ()
    {
        return this.qualifier;
    }


    /**
     * @return The FilterStream for this container filter.
     *         Determines whether the whole container is kept or discarded
     *         based on the stream of FilterStates from the individual
     *         elements.  Never null.
     */
    public final FilterStream<ELEMENT> quantifier ()
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
            + " [ " + this.qualifier
            + ", " + this.quantifier + " ]";
    }
}
