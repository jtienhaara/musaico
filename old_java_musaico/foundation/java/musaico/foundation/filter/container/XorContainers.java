package musaico.foundation.filter.container;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.composite.Xor;


/**
 * <p>
 * Logically XORs together the filter results of one or
 * more child Filters, so that a given object is KEPT only if
 * exactly one of the child filter results is KEPT.
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
public class XorContainers<ELEMENT extends Object>
    extends Xor<Object>
    implements ContainerFilter<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new Xor filter to combine all the specified
     * child filters.
     * </p>
     *
     * <p>
     * An object is DISCARDED (filtered out) unless exactly ONE
     * of the child filters KEPT it.
     * </p>
     *
     * @param filters The child Filter(s).  If null, then no filtering
     *                will be done.  If any element is null, then that
     *                element will be removed from the filters.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public XorContainers (
            Iterable<? extends ContainerFilter<ELEMENT>> filters
            )
        throws NullPointerException
    {
        // Throws NullPointerException:
        super ( filters );
    }


    /**
     * <p>
     * Creates a new Xor filter to combine all the specified
     * child filters.
     * </p>
     *
     * <p>
     * An object is DISCARDED (filtered out) unless exactly ONE
     * of the child filters KEPT it.
     * </p>
     *
     * @param filters The child Filter(s).  If null, then no filtering
     *                will be done.  If any element is null, then that
     *                element will be removed from the filters.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public XorContainers (
            ContainerFilter<ELEMENT> ... filters
            )
        throws NullPointerException
    {
        // Throws NullPointerException:
        super ( filters );
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterArray(java.lang.Object[])
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Filter<E> to ContainerFilter<E>
    // Not final so that XorElements can override:
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

        int num_kept = 0;
        for ( Filter<Object> filter : filters )
        {
            // SuppressWarnings("unchecked"):
            final ContainerFilter<ELEMENT> container_filter =
                (ContainerFilter<ELEMENT>) filter;
            if ( container_filter.filterArray ( container ).isKept () )
            {
                num_kept ++;
                if ( num_kept > 1 )
                {
                    return FilterState.DISCARDED;
                }
            }
        }

        if ( num_kept == 1 )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterCollection(java.util.Collection)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Filter<E> to ContainerFilter<E>
    // Not final so that XorElements can override:
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

        int num_kept = 0;
        for ( Filter<Object> filter : filters )
        {
            // SuppressWarnings("unchecked"):
            final ContainerFilter<ELEMENT> container_filter =
                (ContainerFilter<ELEMENT>) filter;
            if ( container_filter.filterCollection ( container ).isKept () )
            {
                num_kept ++;
                if ( num_kept > 1 )
                {
                    return FilterState.DISCARDED;
                }
            }
        }

        if ( num_kept == 1 )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterIterable(java.lang.Iterable)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Filter<E> to ContainerFilter<E>
    // Not final so that XorElements can override:
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

        int num_kept = 0;
        for ( Filter<Object> filter : filters )
        {
            // SuppressWarnings("unchecked"):
            final ContainerFilter<ELEMENT> container_filter =
                (ContainerFilter<ELEMENT>) filter;
            if ( container_filter.filterIterable ( container ).isKept () )
            {
                num_kept ++;
                if ( num_kept > 1 )
                {
                    return FilterState.DISCARDED;
                }
            }
        }

        if ( num_kept == 1 )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterSingleton(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Filter<E> to ContainerFilter<E>
    // Not final so that XorElements can override:
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

        int num_kept = 0;
        for ( Filter<Object> filter : filters )
        {
            // SuppressWarnings("unchecked"):
            final ContainerFilter<ELEMENT> container_filter =
                (ContainerFilter<ELEMENT>) filter;
            if ( container_filter.filterSingleton ( container ).isKept () )
            {
                num_kept ++;
                if ( num_kept > 1 )
                {
                    return FilterState.DISCARDED;
                }
            }
        }

        if ( num_kept == 1 )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }
}
