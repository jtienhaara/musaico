package musaico.foundation.container;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;
import musaico.foundation.contract.obligations.Parameter1;

import musaico.foundation.filter.Filter;

import musaico.foundation.filter.elements.ElementsFilter;

import musaico.foundation.iterator.ArrayIterator;

import musaico.foundation.order.Order;

import musaico.foundation.structure.StringRepresentation;


/**
 * <p>
 * A Container in a bad state.
 * </p>
 *
 *
 * <p>
 * In Java, every Container must be Serializable in order to play
 * nicely over RMI.  However, be warned that the elements contained
 * in a Container might not be Serializable.
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
 * @see musaico.foundation.container.MODULE#COPYRIGHT
 * @see musaico.foundation.container.MODULE#LICENSE
 */
public class ErrorContainer<ELEMENT extends Object>
    implements Container<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private static final Advocate classAdvocate =
        new Advocate ( ErrorContainer.class );

    private final Container<ELEMENT> sourceContainer;
    private final String reason;
    private final RuntimeException exception;


    public ErrorContainer (
            Container<ELEMENT> source_container,
            String reason,
            Exception exception
            )
        throws EveryParameter.MustNotBeNull.Violation
    {
        this ( source_container,
               reason,
               new IllegalStateException (
                   exception == null
                       ? reason
                       : exception.getMessage (),
                   exception ) );
    }

    public ErrorContainer (
            Container<ELEMENT> source_container,
            String reason,
            RuntimeException exception
            )
        throws EveryParameter.MustNotBeNull.Violation
    {
        classAdvocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              source_container, reason, exception );

        this.sourceContainer = source_container;
        this.reason = reason;
        this.exception = exception;
    }

    /**
     * @see musaico.foundation.container.Container#append(java.lang.Object[])
     */
    @Override
    @SuppressWarnings({"unchecked", "varargs"}) // Cast Object - ELEMENT [],
                                                // heap pollution generic varargs.
    @SafeVarargs
    public final Container<ELEMENT> append (
            ELEMENT ... elements
            )
        throws Return.NeverNull.Violation
    {
        return this;
    }

    /**
     * @see musaico.foundation.container.Container#clear()
     */
    @Override
    public final Container<ELEMENT> clear ()
        throws Return.NeverNull.Violation
    {
        return this;
    }

    /**
     * @see musaico.foundation.container.Container#contains(java.lang.Object[])
     */
    @Override
    @SuppressWarnings("varargs") // heap pollution generic varargs.
    @SafeVarargs
    public final boolean contains (
            ELEMENT ... needles
            )
    {
        return false;
    }

    /**
     * @see musaico.foundation.container.Container#contains(musaico.foundation.filter.Filter)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Filter<?> - Filter<Object>.
    public final boolean contains (
            Filter<?> filter
            )
    {
        return false;
    }

    /**
     * @see musaico.foundation.container.Container#duplicate()
     */
    @Override
    public final Container<ELEMENT> duplicate ()
        throws Return.NeverNull.Violation
    {
        return this;
    }

    /**
     * @see musaico.foundation.container.Container#duplicateImmutable()
     */
    @Override
    public final ErrorContainer<ELEMENT> duplicateImmutable ()
        throws Return.NeverNull.Violation
    {
        return this;
    }

    /**
     * @see musaico.foundation.container.Container#duplicateMutable()
     */
    @Override
    public final ErrorContainer<ELEMENT> duplicateMutable ()
        throws Return.NeverNull.Violation
    {
        return this;
    }

    /**
     * @see musaico.foundation.container.Container#elementType()
     */
    @Override
    public final Class<ELEMENT> elementType ()
        throws Return.NeverNull.Violation
    {
        return this.sourceContainer.elementType ();
    }

    /**
     * @see musaico.foundation.container.Container#elements()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Object - ELEMENT [],
    public final ELEMENT [] elements ()
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return (ELEMENT [])
            Array.newInstance ( this.sourceContainer.elementType (), 0 );
    }

    /**
     * @see musaico.foundation.container.Container#elements(java.lang.Class, java.lang.Object[])
     */
    @Override
    @SuppressWarnings({"unchecked", "varargs"}) // Cast Object - ELEMENT [],
                                                // heap pollution generic varargs.
    @SafeVarargs
    public final <AS extends Object> AS [] elements (
            Class<AS> as_type,
            AS ... default_value
            )
    {
        return (AS [])
            Array.newInstance ( as_type, 0 );
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals (
            Object object
            )
    {
        if ( object == this )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public final Exception exception ()
    {
        return this.exception;
    }

    /**
     * @see musaico.foundation.container.Container#filter(musaico.foundation.filter.elements.ElementsFilter)
     */
    @SuppressWarnings("unchecked") // Cast Object - ELEMENT [],
    @Override
    public final Container<ELEMENT> filter (
            ElementsFilter<ELEMENT> filter
            )
        throws Return.NeverNull.Violation
    {
        return this;
    }

    /**
     * @see musaico.foundation.container.Container#filterContainer(musaico.foundation.filter.Filter)
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"}) // Raw array without generic param new Container [],
                                                 // cast Filter<?> - Filter<Object>.
    public final Container<ELEMENT> [] filterContainer (
            Filter<?> filter
            )
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return (Container<ELEMENT> [])
            new Container [ 0 ];
    }

    /**
     * @see musaico.foundation.container.Container#filterElements()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Object - ELEMENT [],
                                   // cast Filter<?> - Filter<Object>.
    public final Container<ELEMENT> filterElements (
            Filter<?> filter
            )
        throws Return.NeverNull.Violation
    {
        return this;
    }

    /**
     * @see musaico.foundation.container.Container#find(musaico.foundation.order.Order, java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Object - ELEMENT [],
    public final Container<ELEMENT> find (
            Order<ELEMENT> order,
            ELEMENT needle
            )
        throws Return.NeverNull.Violation
    {
        return this;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.sourceContainer.hashCode ();
    }

    /**
     * @see musaico.foundation.container.Container#head(int)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Object - ELEMENT [],
    public final Container<ELEMENT> head (
            int num_elements
            )
        throws Return.NeverNull.Violation
    {
        return this;
    }

    /**
     * @see musaico.foundation.container.Container#insert(int, java.lang.Object[])
     */
    @Override
    @SuppressWarnings({"unchecked", "varargs"}) // Cast Object - ELEMENT [],
                                                // heap pollution generic varargs.
    @SafeVarargs
    public final Container<ELEMENT> insert (
            Container.InsertMode insert_mode,
            int index,
            ELEMENT ... elements
            )
        throws Return.NeverNull.Violation
    {
        return this;
    }

    /**
     * @see musaico.foundation.container.Container#insert(musaico.foundation.order.Order, java.lang.Object[])
     */
    @Override
    @SuppressWarnings({"unchecked", "varargs"}) // Cast Object - ELEMENT [],
                                                // heap pollution generic varargs.
    @SafeVarargs
    public final Container<ELEMENT> insert (
            Container.InsertMode insert_mode,
            Order<ELEMENT> order,
            ELEMENT ... elements
            )
        throws Return.NeverNull.Violation
    {
        return this;
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public final Iterator<ELEMENT> iterator ()
        throws Return.NeverNull.Violation
    {
        return new ArrayIterator<ELEMENT> ( this.elements () );
    }

    /**
     * @see musaico.foundation.container.Container#numElements()
     */
    @Override
    public final int numElements ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return 0;
    }

    /**
     * @see musaico.foundation.container.Container#order()
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"}) // Raw array without generic param new Container [],
                                                 // Cast Order [] - Order<ELEMENT> [].
    public final Order<ELEMENT> [] order ()
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return (Order<ELEMENT> []) new Order [ 0 ];
    }

    /**
     * @see musaico.foundation.container.Container#orThrow()
     */
    @Override
    public final Container<ELEMENT> orThrow ()
        throws RuntimeException,
               Return.NeverNull.Violation
    {
        throw new RuntimeException ( this.reason,
                                     this.exception );
    }

    /**
     * @see musaico.foundation.container.Container#prepend(java.lang.Object[])
     */
    @Override
    @SuppressWarnings({"unchecked", "varargs"}) // Cast Object - ELEMENT [],
                                                // heap pollution generic varargs.
    @SafeVarargs
    public final Container<ELEMENT> prepend (
            ELEMENT ... elements
            )
        throws Return.NeverNull.Violation
    {
        return this;
    }

    public final String reason ()
    {
        return this.reason;
    }

    /**
     * @see musaico.foundation.container.Container#remove(java.lang.Object[])
     */
    @Override
    @SuppressWarnings({"unchecked", "varargs"}) // Cast Object - ELEMENT [],
                                                // heap pollution generic varargs.
    @SafeVarargs
    public final Container<ELEMENT> remove (
            ELEMENT ... elements
            )
        throws Return.NeverNull.Violation
    {
        return this;
    }

    /**
     * @see musaico.foundation.container.Container#reverse()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Object - ELEMENT [],
    public final Container<ELEMENT> reverse ()
        throws Return.NeverNull.Violation
    {
        return this;
    }

    /**
     * @see musaico.foundation.container.Container#set(java.lang.Object[])
     */
    @Override
    @SuppressWarnings("varargs") // heap pollution generic varargs.
    @SafeVarargs
    public final Container<ELEMENT> set (
            ELEMENT ... elements
            )
        throws Return.NeverNull.Violation
    {
        return this;
    }

    /**
     * @see musaico.foundation.container.Container#sort(musaico.foundation.order.Order)
     */
    @Override
    public final Container<ELEMENT> sort (
            Order<ELEMENT> order
            )
        throws Return.NeverNull.Violation
    {
        return this;
    }

    public final Container<ELEMENT> sourceContainer ()
    {
        return this.sourceContainer;
    }

    /**
     * @see musaico.foundation.container.Container#tail(int)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Object - ELEMENT [],
    public final Container<ELEMENT> tail (
            int num_elements
            )
        throws Return.NeverNull.Violation
    {
        return this;
    }

    /**
     * @see java.lang.Object#toString()
     *
     * CAN be overridden.
     */
    @Override
    public String toString ()
        throws Return.NeverNull.Violation
    {
        return "error_container [ "
            + this.reason
            + " ]";
    }

    /**
     * @see musaico.foundation.container.Container#version()
     */
    @Override
    public final int version ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return this.sourceContainer.version () + 1;
    }
}
