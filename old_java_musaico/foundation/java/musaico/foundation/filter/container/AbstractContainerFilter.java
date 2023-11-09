package musaico.foundation.filter.container;

import java.io.Serializable;

import java.util.Collection;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;


import musaico.foundation.structure.ClassName;

import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Boilerplate common implementations for ContainerFilters.
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
public abstract class AbstractContainerFilter<ELEMENT extends Object>
    implements ContainerFilter<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Various try...cast...catch.
    public FilterState filter (
            Object object
            )
    {
        if ( object == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( object.getClass ().isArray () )
        {
            try
            {
                final ELEMENT [] array = (ELEMENT []) object;
                return this.filterArray ( array );
            }
            catch ( ClassCastException e )
            {
                // Fall through to singleton, below.  Maybe...
            }
        }
        else if ( object instanceof Collection )
        {
            try
            {
                Collection<ELEMENT> collection = (Collection<ELEMENT>) object;
                return this.filterCollection ( collection );
            }
            catch ( ClassCastException e )
            {
                // Fall through to singleton, below.  Maybe...
            }
        }
        else if ( object instanceof Iterable )
        {
            try
            {
                final Iterable<ELEMENT> iterable = (Iterable<ELEMENT>) object;
                return this.filterIterable ( iterable );
            }
            catch ( ClassCastException e )
            {
                // Fall through to singleton, below.  Maybe...
            }
        }

        try
        {
            final ELEMENT singleton = (ELEMENT) object;
            return this.filterSingleton ( singleton );
        }
        catch ( ClassCastException e )
        {
            return FilterState.DISCARDED;
        }
    }


    // In Java every ContainerFilter must implement
    // musaico.foundation.filter.container.ContainerFilter#filterArray(java.lang.Object[])

    // In Java every ContainerFilter must implement
    // musaico.foundation.filter.container.ContainerFilter#filterCollection(java.util.Collection)

    // In Java every ContainerFilter must implement
    // musaico.foundation.filter.container.ContainerFilter#filterIterable(java.lang.Iterable)

    // In Java every ContainerFilter must implement
    // musaico.foundation.filter.container.ContainerFilter#filterSingleton(java.lang.Object)
}
