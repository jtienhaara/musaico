package musaico.foundation.filter.elements;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;


import musaico.foundation.structure.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;
import musaico.foundation.filter.KeepAll;

import musaico.foundation.filter.stream.FilterStream;
import musaico.foundation.filter.stream.FilteredCollection;
import musaico.foundation.filter.stream.StreamKeepAll;


/**
 * <p>
 * Allows another ElementsFilter to filter each String as a
 * container of Character elements.
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
public class StringElementsFilter
    implements ElementsFilter<Character>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Matches the String as a container of Character elements.
    private final ElementsFilter<Character> elementsFilter;


    /**
     * <p>
     * Creates a new StringElementsFilter.
     * </p>
     *
     * @param elements_filter Matches the String as a container
     *                        of Characters.  If null then a
     *                        AllElementsFilter with KeepAll
     *                        as the individual element filter
     *                        will be used by default.
     */
    public StringElementsFilter (
            ElementsFilter<Character> elements_filter
            )
        throws IllegalArgumentException
    {
        if ( elements_filter == null )
        {
            final KeepAll<Character> keep_all_characters =
                new KeepAll<Character> ();
            final StreamKeepAll<Character> short_circuit_quickly =
                new StreamKeepAll<Character> ();
            this.elementsFilter = new AllElementsFilter<KeepAll<Character>, Character> (
                                      keep_all_characters,     // element_filter
                                      short_circuit_quickly ); // filter_stream
        }
        else
        {
            this.elementsFilter = elements_filter;
        }
    }


    /**
     * @see musaico.foundation.filter.elements.ElementsFilter#discards(java.lang.Object, java.util.Collection)
     */
    @Override
    public final Collection<Character> discards (
            Object container,
            Collection<Character> discards_or_null
            )
    {
        final Collection<Character> discards;
        if ( discards_or_null == null )
        {
            discards = new ArrayList<Character> ();
        }
        else
        {
            discards = discards_or_null;
        }

        final FilterStream<Character> collector =
            new FilteredCollection<Character> (
                null,       // kept_elements_or_null
                discards ); // discarded_elements_or_null

        this.filter ( container, collector );

        return discards;
    }


    /**
     * @see musaico.foundation.filter.elements.StringElementsFilter#discards(java.lang.Object, java.util.Collection)
     */
    public final char [] discardChars (
            Object container,
            Collection<Character> discards_or_null
            )
    {
        final Collection<Character> discards =
            this.discards ( container, discards_or_null );
        final char [] discard_chars =
            new char [ discards.size () ];
        int d = 0;
        for ( Character character : discards )
        {
            discard_chars [ d ] = character.charValue ();
            d ++;
        }
        return discard_chars;
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

        final StringElementsFilter that = (StringElementsFilter) object;
        if ( this.elementsFilter == null )
        {
            if ( that.elementsFilter != null )
            {
                return false;
            }
        }
        else if ( that.elementsFilter == null )
        {
            return false;
        }
        else if ( ! this.elementsFilter.equals ( that.elementsFilter ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.filter.AbstractContainerFilter#filter(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Various try...cast...catch.
    public FilterState filter (
            Object container
            )
    {
        if ( container instanceof String )
        {
            final String string = (String) container;
            return this.filterString ( string );
        }
        else
        {
            return this.elementsFilter.filter ( container );
        }
    }


    /**
     * @see musaico.foundation.filter.elements.ElementsFilter#filter(java.lang.Object, musaico.foundation.filter.elements.FilterStream)
     */
    @Override
    @SuppressWarnings("unchecked") // Various try...cast...catch.
    public final FilterState filter (
            Object container,
            FilterStream<Character> filter_stream
            )
    {
        if ( container instanceof String )
        {
            final String string = (String) container;
            return this.filterString ( string, filter_stream );
        }
        else
        {
            return this.elementsFilter.filter ( container, filter_stream );
        }
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterArray(java.lang.Object[])
     */
    @Override
    public final FilterState filterArray (
            Character [] container
            )
    {
        return this.elementsFilter.filterArray ( container );
    }


    /**
     * @see musaico.foundation.filter.elements.ElementsFilter#filterArray(java.lang.Object[], musaico.foundation.filter.elements.FilterStream)
     */
    @Override
    public final FilterState filterArray (
            Character [] container,
            FilterStream<Character> filter_stream
            )
    {
        return this.elementsFilter.filterArray (
                   container,
                   filter_stream );
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterCollection(java.util.Collection)
     */
    @Override
    public final FilterState filterCollection (
            Collection<Character> container
            )
    {
        return this.elementsFilter.filterCollection ( container );
    }


    /**
     * @see musaico.foundation.filter.elements.ElementsFilter#filterCollection(java.util.Collection, musaico.foundation.filter.elements.FilterStream)
     */
    @Override
    public final FilterState filterCollection (
            Collection<Character> container,
            FilterStream<Character> filter_stream
            )
    {
        return this.elementsFilter.filterCollection (
                   container,
                   filter_stream );
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterIterable(java.lang.Iterable)
     */
    @Override
    public final FilterState filterIterable (
            Iterable<Character> container
            )
    {
        return this.elementsFilter.filterIterable ( container );
    }


    /**
     * @see musaico.foundation.filter.elements.ElementsFilter#filterIterable(java.lang.Iterable, musaico.foundation.filter.elements.FilterStream)
     */
    @Override
    public final FilterState filterIterable (
            Iterable<Character> container,
            FilterStream<Character> filter_stream
            )
    {
        return this.elementsFilter.filterIterable (
                   container,
                   filter_stream );
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterSingleton(java.lang.Object)
     */
    @Override
    public final FilterState filterSingleton (
            Character container
            )
    {
        return this.elementsFilter.filterSingleton ( container );
    }


    /**
     * @see musaico.foundation.filter.elements.ElementsFilter#filterSingleton(java.lang.Object, musaico.foundation.filter.elements.FilterStream)
     */
    @Override
    public final FilterState filterSingleton (
            Character container,
            FilterStream<Character> filter_stream
            )
    {
        return this.elementsFilter.filterSingleton (
                   container,
                   filter_stream );
    }


    /**
     * <p>
     * Filters the specified String as a container of Character
     * elements.
     * </p>
     *
     * @param container The container String to keep or discard.
     *                  Can be null.
     *
     * @return The overall FilterState (as per Filter.filter ( ... )).
     *         Never null.
     *
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    public final FilterState filterString (
            String container
            )
    {
        if ( container == null )
        {
            return this.elementsFilter.filterArray ( null );
        }

        final char [] chars = container.toCharArray ();
        final Character [] characters = new Character [ chars.length ];
        for ( int c = 0; c < chars.length; c ++ )
        {
            characters [ c ] = chars [ c ];
        }

        return this.elementsFilter.filterArray ( characters );
    }


    /**
     * <p>
     * Filters the specified String as a container of Character
     * elements.
     * </p>
     *
     * @param container The container String to keep or discard.
     *                  Can be null.
     *
     * @param filter_stream A FilterStream that can be used to
     *                      further process each kept or discarded
     *                      Character.  For example, a FilteredCollection
     *                      can be used to gather up the kept
     *                      and discarded elements into new
     *                      containers.  If null, then no further
     *                      processing will be performed.
     *                      Can be null.
     *
     * @return The overall FilterState (as per Filter.filter ( ... )).
     *         Never null.
     *
     * @see musaico.foundation.filter.elements.ElementsFilter#filter(java.lang.Object, musaico.foundation.filter.elements.FilterStream)
     */
    public final FilterState filterString (
            String container,
            FilterStream<Character> filter_stream
            )
    {
        if ( container == null )
        {
            return this.elementsFilter.filterArray ( null, filter_stream );
        }

        final char [] chars = container.toCharArray ();
        final Character [] characters = new Character [ chars.length ];
        for ( int c = 0; c < chars.length; c ++ )
        {
            characters [ c ] = chars [ c ];
        }

        return this.elementsFilter.filterArray ( characters, filter_stream );
    }



    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 17 * this.getClass ().getName ().hashCode ()
            + this.elementsFilter.hashCode ();
    }


    /**
     * @see musaico.foundation.filter.elements.ElementsFilter#keepers(java.lang.Object, java.util.Collection)
     */
    @Override
    public final Collection<Character> keepers (
            Object container,
            Collection<Character> keepers_or_null
            )
    {
        final Collection<Character> keepers;
        if ( keepers_or_null == null )
        {
            keepers = new ArrayList<Character> ();
        }
        else
        {
            keepers = keepers_or_null;
        }

        final FilterStream<Character> collector =
            new FilteredCollection<Character> (
                keepers, // kept_elements_or_null
                null );  // discarded_elements_or_null

        this.filter ( container, collector );

        return keepers;
    }


    /**
     * @see musaico.foundation.filter.elements.StringElementsFilter#keepers(java.lang.Object, java.util.Collection)
     */
    public final char [] keeperChars (
            Object container,
            Collection<Character> keepers_or_null
            )
    {
        final Collection<Character> keepers =
            this.keepers ( container, keepers_or_null );
        final char [] keeper_chars =
            new char [ keepers.size () ];
        int k = 0;
        for ( Character character : keepers )
        {
            keeper_chars [ k ] = character.charValue ();
            k ++;
        }
        return keeper_chars;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " "
            + this.elementsFilter;
    }
}
