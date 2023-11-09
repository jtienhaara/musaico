package musaico.foundation.filter.container;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.structure.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Allows another ContainerFilter to filter each String as a
 * container of Characters.
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
 * @see musaico.foundation.filter.container.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.container.MODULE#LICENSE
 */
public class StringContainerFilter
    implements ContainerFilter<Character>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Matches the String as a container of Characters.
    private final ContainerFilter<Character> containerFilter;


    /**
     * <p>
     * Creates a new container StringContainerFilter.
     * </p>
     *
     * @param container_filter Matches the String as a container
     *                         of Characters.  If null then a
     *                         KeepAllContainers.Filter will be
     *                         used by default.
     */
    public StringContainerFilter (
            ContainerFilter<Character> container_filter
            )
        throws IllegalArgumentException
    {
        if ( container_filter == null )
        {
            this.containerFilter = new KeepAllContainers<Character> ();
        }
        else
        {
            this.containerFilter = container_filter;
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

        final StringContainerFilter that = (StringContainerFilter) object;
        if ( this.containerFilter == null )
        {
            if ( that.containerFilter != null )
            {
                return false;
            }
        }
        else if ( that.containerFilter == null )
        {
            return false;
        }
        else if ( ! this.containerFilter.equals ( that.containerFilter ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
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
            return this.containerFilter.filter ( container );
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
        return this.containerFilter.filterArray ( container );
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterCollection(java.util.Collection)
     */
    @Override
    public final FilterState filterCollection (
            Collection<Character> container
            )
    {
        return this.containerFilter.filterCollection ( container );
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterIterable(java.lang.Iterable)
     */
    @Override
    public final FilterState filterIterable (
            Iterable<Character> container
            )
    {
        return this.containerFilter.filterIterable ( container );
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterSingleton(java.lang.Object)
     */
    @Override
    public final FilterState filterSingleton (
            Character container
            )
    {
        return this.containerFilter.filterSingleton ( container );
    }


    /**
     * <p>
     * Filters the specified String as a container of Characters.
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
            return this.containerFilter.filterArray ( null );
        }

        final char [] chars = container.toCharArray ();
        final Character [] characters = new Character [ chars.length ];
        for ( int c = 0; c < chars.length; c ++ )
        {
            characters [ c ] = chars [ c ];
        }

        return this.containerFilter.filterArray ( characters );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 17 * this.getClass ().getName ().hashCode ()
            + this.containerFilter.hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " "
            + this.containerFilter;
    }
}
