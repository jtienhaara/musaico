package musaico.foundation.filter.container;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.composite.Not;


/**
 * <p>
 * Negates the effect of another ContainerFilter, so that every KEPT result
 * becomes DISCARDED and vice-versa.
 * </p>
 *
 *
 * <p>
 * In Java, every Filter must be Serializable in order to play
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
 * @see musaico.foundation.filter.container.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.container.MODULE#LICENSE
 */
public class NotContainers<ELEMENT extends Object>
    extends Not<Object>
    implements ContainerFilter<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new Not filter to negate the specified filter.
     * </p>
     *
     * <p>
     * An object is DISCARDED (filtered out) whenever the child
     * filter KEPTs it; and an object is KEPTed whenever the child
     * filter decides it is DISCARDED.
     * </p>
     *
     * @param filter The child Filter to negate.  If null, then
     *               no filtering at all will be done.
     */
    public NotContainers (
            ContainerFilter<ELEMENT> filter
            )
        throws NullPointerException
    {
        // Throws NullPointerException:
        super ( filter );
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterArray(java.lang.Object[])
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Filter<E> to ContainerFilter<E>
    // Not final so that NotElements can override:
    public FilterState filterArray (
            ELEMENT [] container
            )
    {
        if ( container == null )
        {
            return FilterState.DISCARDED;
        }

        final Filter<Object> [] filters = this.myFilters ();
        if ( filters.length == 0 )
        {
            return FilterState.KEPT;
        }

        for ( Filter<Object> filter : filters )
        {
            // SuppressWarnings("unchecked"):
            final ContainerFilter<ELEMENT> container_filter =
                (ContainerFilter<ELEMENT>) filter;
            if ( container_filter.filterArray ( container ).isKept () )
            {
                return FilterState.DISCARDED;
            }
        }

        return FilterState.KEPT;
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterCollection(java.util.Collection)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Filter<E> to ContainerFilter<E>
    // Not final so that NotElements can override:
    public FilterState filterCollection (
            Collection<ELEMENT> container
            )
    {
        if ( container == null )
        {
            return FilterState.DISCARDED;
        }

        final Filter<Object> [] filters = this.myFilters ();
        if ( filters.length == 0 )
        {
            return FilterState.KEPT;
        }

        for ( Filter<Object> filter : filters )
        {
            // SuppressWarnings("unchecked"):
            final ContainerFilter<ELEMENT> container_filter =
                (ContainerFilter<ELEMENT>) filter;
            if ( container_filter.filterCollection ( container ).isKept () )
            {
                return FilterState.DISCARDED;
            }
        }

        return FilterState.KEPT;
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterIterable(java.lang.Iterable)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Filter<E> to ContainerFilter<E>
    // Not final so that NotElements can override:
    public FilterState filterIterable (
            Iterable<ELEMENT> container
            )
    {
        if ( container == null )
        {
            return FilterState.DISCARDED;
        }

        final Filter<Object> [] filters = this.myFilters ();
        if ( filters.length == 0 )
        {
            return FilterState.KEPT;
        }

        for ( Filter<Object> filter : filters )
        {
            // SuppressWarnings("unchecked"):
            final ContainerFilter<ELEMENT> container_filter =
                (ContainerFilter<ELEMENT>) filter;
            if ( ! container_filter.filterIterable ( container ).isKept () )
            {
                return FilterState.DISCARDED;
            }
        }

        return FilterState.KEPT;
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterSingleton(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Filter<E> to ContainerFilter<E>
    // Not final so that NotElements can override:
    public FilterState filterSingleton (
            ELEMENT container
            )
    {
        if ( container == null )
        {
            return FilterState.DISCARDED;
        }

        final Filter<Object> [] filters = this.myFilters ();
        if ( filters.length == 0 )
        {
            return FilterState.KEPT;
        }

        for ( Filter<Object> filter : filters )
        {
            // SuppressWarnings("unchecked"):
            final ContainerFilter<ELEMENT> container_filter =
                (ContainerFilter<ELEMENT>) filter;
            if ( ! container_filter.filterSingleton ( container ).isKept () )
            {
                return FilterState.DISCARDED;
            }
        }

        return FilterState.KEPT;
    }
}
