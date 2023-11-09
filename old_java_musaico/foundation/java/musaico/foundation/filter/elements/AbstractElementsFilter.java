package musaico.foundation.filter.elements;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;


import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.container.AbstractContainerFilter;

import musaico.foundation.filter.stream.FilterStream;
import musaico.foundation.filter.stream.FilteredCollection;


/**
 * <p>
 * Provides default implementations for boilerplate ContainerFilter
 * and ElementsFilter methods.
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add new filters to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
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
public abstract class AbstractElementsFilter<ELEMENT extends Object>
    extends AbstractContainerFilter<ELEMENT>
    implements ElementsFilter<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


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
}
