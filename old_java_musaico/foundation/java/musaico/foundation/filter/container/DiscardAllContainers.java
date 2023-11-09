package musaico.foundation.filter.container;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.structure.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Discards (filters out) every container, filters out nothing that is
 * a legitimate container.
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
public class DiscardAllContainers<ELEMENT extends Object>
    extends AbstractContainerFilter<ELEMENT>
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
     * @see musaico.foundation.filter.container.ContainerFilter#filterArray(java.lang.Object[])
     */
    @Override
    public final FilterState filterArray (
            ELEMENT [] container
            )
    {
        return FilterState.DISCARDED;
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterCollection(java.util.Collection)
     */
    @Override
    public final FilterState filterCollection(
            Collection<ELEMENT> container
            )
    {
        return FilterState.DISCARDED;
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterIterable(java.lang.Iterable)
     */
    @Override
    public final FilterState filterIterable(
            Iterable<ELEMENT> container
            )
    {
        return FilterState.DISCARDED;
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterSingleton(java.lang.Object)
     */
    @Override
    public final FilterState filterSingleton(
            ELEMENT container
            )
    {
        return FilterState.DISCARDED;
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
