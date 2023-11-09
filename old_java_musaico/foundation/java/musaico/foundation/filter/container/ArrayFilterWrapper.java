package musaico.foundation.filter.container;

import java.io.Serializable;


import musaico.foundation.structure.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Wraps any ContainerFilter so that is can be composed into
 * hierarchies of Filters that deal with arrays.
 * </p>
 *
 * <p>
 * By default, a ContainerFilter implements
 * <code> Filter&lt;Object&gt; </code>.  In order to add such a
 * filter into a hierarchy of <code> Filter&lt;String []&gt; </code>,
 * for example, a new <code> ArrayFilterWrapper&lt;String&gt; </code>
 * can be constructed, passing to the constructor the
 * <code> ContainerFilter&lt;String&gt; </code> to be wrapped
 * as a <code> Filter&lt;String []&gt; </code>.
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
public class ArrayFilterWrapper<ELEMENT extends Object>
    implements Filter<ELEMENT []>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // The wrapped ContainerFilter, which is a Filter<Object>.
    private final ContainerFilter<ELEMENT> containerFilter;


    /**
     * <p>
     * Creates a new ArrayFilterWrapper to wrap the specified
     * ContainerFilter as a <code> Filter&lt;E[]&gt; </code>.
     * </p>
     *
     * @param container_filter The ContainerFilter to wrap.
     *                         The ContainerFilter is a
     *                         <code> Filter&lt;Object&gt;.
     *                         This class wraps it to look like a
     *                         <code> Filter&lt;E[]&gt; </code>.
     *                         If null, then a KeepAllContainers
     *                         will be wrapped by default.
     *                         DO NOT PASS NULL.
     */
    public ArrayFilterWrapper (
            ContainerFilter<ELEMENT> container_filter
            )
    {
        if ( container_filter == null )
        {
            this.containerFilter = new KeepAllContainers<ELEMENT> ();
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

        final ArrayFilterWrapper<?> that =
            (ArrayFilterWrapper<?>) object;

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
            ELEMENT [] container
            )
    {
        final FilterState filter_state =
            this.containerFilter.filterArray ( container );
        return filter_state;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return ClassName.of ( this.getClass () ).hashCode ()
            + this.containerFilter.hashCode ();
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
