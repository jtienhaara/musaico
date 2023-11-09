package musaico.foundation.table;

import java.io.Serializable;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


import musaico.foundation.filter.container.ContainerFilter;

import musaico.foundation.filter.elements.ElementsFilter;


/**
 * <p>
 * !!!
 * </p>
 *
 *
 * <p>
 * In Java every Table must be Serializable in order
 * to play nicely over RMI.  Of course, be warned that
 * a Table serialized and transmitted over RMI is
 * a new Table, taken from a snapshot of the original.
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
 * For copyright and licensing information, please refer to:
 * </p>
 *
 * @see musaico.foundation.table.MODULE#COPYRIGHT
 * @see musaico.foundation.table.MODULE#LICENSE
 */
public final interface Container<ELEMENT extends Object>
    implements Iterable<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    public abstract Container<ELEMENT> filter (
            ContainerFilter<ELEMENT> container_filter
            );

    public abstract Container<ELEMENT> filterElements (
            Filter<ELEMENT> element_filter
            );

    public abstract Container<ELEMENT> filterElements (
            Filter<ELEMENT> element_filter,
            FilterStream<ELEMENT> filter_stream
            );

    // Every Container must implement:
    // @see java.lang.Iterable#iterator()

    public abstract long length ();

    // !!! Maybe pass exception handlers to the methods that are
    // !!! expected to return containers too small to contain
    // !!! what this interface can actually hold -- so that
    // !!! if this interface is for a container with
    // !!! Integer.MAX_VALUE + 1 elements, we invoke the
    // !!! exception handler.
    public abstract ELEMENT [] toArray ();

    public abstract Collection<ELEMENT> toCollection ();

    public abstract List<ELEMENT> toList ();

    public abstract LinkedHashSet<ELEMENT> toOrderedSet ();

    public abstract Set<ELEMENT> toSet ();
}
