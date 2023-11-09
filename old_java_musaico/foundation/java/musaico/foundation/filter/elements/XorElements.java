package musaico.foundation.filter.elements;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.container.XorContainers;


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
 * @see musaico.foundation.filter.elements.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.elements.MODULE#LICENSE
 */
public class XorElements<ELEMENT extends Object>
    extends XorContainers<Object>
    implements ElementsFilter<ELEMENT>, Serializable
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
    public XorElements (
            Iterable<? extends ElementsFilter<ELEMENT>> filters
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
    public XorElements (
            ElementsFilter<ELEMENT> ... filters
            )
        throws NullPointerException
    {
        // Throws NullPointerException:
        super ( filters );
    }


    !!!;


    /**
     * @see musaico.foundation.filter.elements.ElementsFilter#discards(java.lang.Object, java.util.Collection)
     */
    @Override
    public final Collection<ELEMENT> discards (
            Object container,
            Collection<ELEMENT> discards_or_null
            )
    {
        final Collection<ELEMENT> discards;
        if ( discards_or_null == null )
        {
            discards = new ArrayList<ELEMENT> ();
        }
        else
        {
            discards = discards_or_null;
        }

        final FilterStream<ELEMENT> collector =
            new FilteredCollection<ELEMENT> (
                null,       // kept_elements_or_null
                discards ); // discarded_elements_or_null

        this.filter ( container, collector );

        return discards;
    }


    /**
     * @see musaico.foundation.filter.elements.ElementsFilter#filter(java.lang.Object, musaico.foundation.filter.elements.FilterStream)
     */
    @Override
    @SuppressWarnings("unchecked") // Various try...cast...catch.
    public final FilterState filter (
            Object container,
            FilterStream<ELEMENT> filter_stream
            )
    {
        if ( container == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( container.getClass ().isArray () )
        {
            try
            {
                final ELEMENT [] array = (ELEMENT []) container;
                return this.filterArray ( array,
                                          filter_stream );
            }
            catch ( ClassCastException e )
            {
                // Fall through to singleton, below.  Maybe...
            }
        }
        else if ( container instanceof Collection )
        {
            try
            {
                Collection<ELEMENT> collection = (Collection<ELEMENT>) container;
                return this.filterCollection ( collection,
                                               filter_stream );
            }
            catch ( ClassCastException e )
            {
                // Fall through to singleton, below.  Maybe...
            }
        }
        else if ( container instanceof Iterable )
        {
            try
            {
                final Iterable<ELEMENT> iterable = (Iterable<ELEMENT>) container;
                return this.filterIterable ( iterable,
                                             filter_stream );
            }
            catch ( ClassCastException e )
            {
                // Fall through to singleton, below.  Maybe...
            }
        }

        try
        {
            final ELEMENT singleton = (ELEMENT) container;
            return this.filterSingleton ( singleton,
                                          filter_stream );
        }
        catch ( ClassCastException e )
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterArray(java.lang.Object)
     */
    @Override
    public final FilterState filterArray (
            ELEMENT [] container
            )
    {
        return this.filterArray ( container, // container
                                  null );    // filter_stream
    }


    // Every ElementsFilter must implement
    // @see musaico.foundation.filter.elements.ElementsFilter#filterArray(java.lang.Object[], musaico.foundation.filter.elements.FilterStream)


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterCollection(java.util.Collection)
     */
    @Override
    public final FilterState filterCollection (
            Collection<ELEMENT> container
            )
    {
        return this.filterCollection ( container, // container
                                       null );    // filter_stream
    }


    // Every ElementsFilter must implement
    // @see musaico.foundation.filter.elements.ElementsFilter#filterCollection(java.util.Collection, musaico.foundation.filter.elements.FilterStream)


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterIterable(java.lang.Iterable)
     */
    @Override
    public final FilterState filterIterable (
            Iterable<ELEMENT> container
            )
    {
        return this.filterIterable ( container, // container
                                     null );    // filter_stream
    }


    // Every ElementsFilter must implement
    // @see musaico.foundation.filter.elements.ElementsFilter#filterIterable(java.lang.Iterable, musaico.foundation.filter.elements.FilterStream)


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterSingleton(java.lang.Object)
     */
    @Override
    public final FilterState filterSingleton (
            ELEMENT container
            )
    {
        return this.filterSingleton ( container, // container
                                      null );    // filter_stream
    }


    // Every ElementsFilter must implement
    // @see musaico.foundation.filter.elements.ElementsFilter#filterSingleton(java.lang.Object, musaico.foundation.filter.elements.FilterStream)


    /**
     * @see musaico.foundation.filter.elements.ElementsFilter#keepers(java.lang.Object, java.util.Collection)
     */
    @Override
    public final Collection<ELEMENT> keepers (
            Object container,
            Collection<ELEMENT> keepers_or_null
            )
    {
        final Collection<ELEMENT> keepers;
        if ( keepers_or_null == null )
        {
            keepers = new ArrayList<ELEMENT> ();
        }
        else
        {
            keepers = keepers_or_null;
        }

        final FilterStream<ELEMENT> collector =
            new FilteredCollection<ELEMENT> (
                keepers, // kept_elements_or_null
                null );  // discarded_elements_or_null

        this.filter ( container, collector );

        return keepers;
    }


    !!!;


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterArray(java.lang.Object[])
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Filter<E> to ContainerFilter<E>
    public final FilterState filterArray (
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
    public final FilterState filterCollection (
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
    public final FilterState filterIterable (
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
    public final FilterState filterSingleton (
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
