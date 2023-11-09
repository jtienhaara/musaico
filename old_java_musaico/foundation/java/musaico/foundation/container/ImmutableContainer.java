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
import musaico.foundation.contract.obligations.Parameter2;

import musaico.foundation.filter.Filter;

import musaico.foundation.filter.elements.ElementsFilter;

import musaico.foundation.iterator.ArrayIterator;

import musaico.foundation.order.Order;

import musaico.foundation.structure.StringRepresentation;


/**
 * <p>
 * An iterable, immutable Container implementation.
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
public class ImmutableContainer<ELEMENT extends Object>
    implements Container<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private static final Advocate classAdvocate =
        new Advocate ( ImmutableContainer.class );

    private final int version;
    private final Order<ELEMENT> orderOrNull;
    private final Class<ELEMENT> elementType;
    private final ELEMENT [] elements;

    private final int toStringMaxTotalLength  = 158 - "container [  ]".length (); // Fit on 2 terminal lines.
    private final int toStringMaxElementLength = 32; // Arbitrary.


    @SuppressWarnings("varargs") // heap pollution generic varargs.
    @SafeVarargs // Possible heap pollution ELEMENT ...
    public ImmutableContainer (
            Class<ELEMENT> element_type,
            ELEMENT ... elements
            )
        throws EveryParameter.MustNotBeNull.Violation
    {
        this ( 0,            // version
               null,         // order_or_null
               element_type, // element_type
               elements );   // elements
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Cast Object - ELEMENT [],
                                                // heap pollution generic varargs.
    @SafeVarargs // Possible heap pollution ELEMENT ...
    public <CONSTRUCTED_WITH extends ELEMENT> ImmutableContainer (
            Class<ELEMENT> element_type,
            Class<CONSTRUCTED_WITH> constructed_with_type,
            CONSTRUCTED_WITH ... elements
            )
        throws EveryParameter.MustNotBeNull.Violation
    {
        classAdvocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              element_type, constructed_with_type, elements );

        this.version = 0;
        this.orderOrNull = null;

        this.elementType = element_type;
        // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
        final ELEMENT [] defensive_duplicate = (ELEMENT [])
            Array.newInstance ( this.elementType,
                                elements.length );
        System.arraycopy ( elements, 0,
                           defensive_duplicate, 0, elements.length );
        this.elements = defensive_duplicate;
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Cast Object - ELEMENT [],
                                                // heap pollution generic varargs.
    @SafeVarargs // Possible heap pollution ELEMENT ...
    protected ImmutableContainer (
            int version,
            Order<ELEMENT> order_or_null,
            Class<ELEMENT> element_type,
            ELEMENT ... elements
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter1.MustBeGreaterThanOrEqualToZero.Violation
    {
        classAdvocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              element_type, elements );
        classAdvocate.check ( Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                              version );

        this.version = version;
        this.orderOrNull = order_or_null;

        this.elementType = element_type;
        // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
        final ELEMENT [] defensive_duplicate = (ELEMENT [])
            Array.newInstance ( this.elementType,
                                elements.length );
        System.arraycopy ( elements, 0,
                           defensive_duplicate, 0, elements.length );
        this.elements = defensive_duplicate;
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
        if ( elements == null )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "append ( null )",
                                                 Parameter1.MustNotBeNull.CONTRACT.violation (
                                                     this,       // plaintiff
                                                     elements ) ); // evidence_or_null
        }
        else if ( elements.length == 0 )
        {
            return this;
        }

        // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
        final ELEMENT [] new_elements = (ELEMENT [])
            Array.newInstance ( this.elementType, this.elements.length + elements.length );
        System.arraycopy ( this.elements, 0,
                           new_elements, 0, this.elements.length );
        System.arraycopy ( elements, 0,
                           new_elements, this.elements.length, elements.length );

        final Container<ELEMENT> new_container =
            new ImmutableContainer<ELEMENT> ( this.version + 1,
                                              null, // No longer sorted, if it was.
                                              this.elementType,
                                              new_elements );

        return new_container;
    }

    /**
     * @see musaico.foundation.container.Container#clear()
     */
    @Override
    public final Container<ELEMENT> clear ()
        throws Return.NeverNull.Violation
    {
        return new ImmutableContainer<ELEMENT> ( this.version + 1,
                                                 this.orderOrNull, // Possibly still sorted, since it's empty.
                                                 this.elementType );
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
        if ( needles == null )
        {
            return false;
        }
        else if ( needles.length == 0 )
        {
            return false;
        }

        final Set<ELEMENT> missing =
            new HashSet<ELEMENT> ();
        boolean is_null_missing = false;
        for ( ELEMENT needle : needles )
        {
            if ( needle == null )
            {
                is_null_missing = true;
            }
            else
            {
                missing.add ( needle );
            }
        }

        for ( ELEMENT hay : this.elements )
        {
            if ( hay == null )
            {
                is_null_missing = false;
            }
            else
            {
                missing.remove ( hay );
            }

            if ( missing.size () == 0
                 && ! is_null_missing )
            {
                return true;
            }
        }

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
        if ( filter == null )
        {
            return false;
        }

        final Filter<Object> object_filter = (Filter<Object>)
            ( (Object) filter );
        try
        {
            for ( ELEMENT element : this.elements )
            {
                if ( object_filter.filter ( element ).isKept () )
                {
                    return true;
                }
            }

            return false;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    /**
     * @see musaico.foundation.container.Container#duplicate()
     */
    @Override
    public final Container<ELEMENT> duplicate ()
        throws Return.NeverNull.Violation
    {
        return new ImmutableContainer<ELEMENT> ( this.version,
                                                 this.orderOrNull,
                                                 this.elementType,
                                                 this.elements );
    }

    /**
     * @see musaico.foundation.container.Container#duplicateImmutable()
     */
    @Override
    public final ImmutableContainer<ELEMENT> duplicateImmutable ()
        throws Return.NeverNull.Violation
    {
        return new ImmutableContainer<ELEMENT> ( this.version,
                                                 this.orderOrNull,
                                                 this.elementType,
                                                 this.elements );
    }

    /**
     * @see musaico.foundation.container.Container#duplicateMutable()
     */
    @Override
    public final MutableContainer<ELEMENT> duplicateMutable ()
        throws Return.NeverNull.Violation
    {
        return new MutableContainer<ELEMENT> ( this.version,
                                               this.orderOrNull,
                                               this.elementType,
                                               this.elements );
    }

    /**
     * @see musaico.foundation.container.Container#elementType()
     */
    @Override
    public final Class<ELEMENT> elementType ()
        throws Return.NeverNull.Violation
    {
        return this.elementType;
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
        // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
        final ELEMENT [] defensive_duplicate = (ELEMENT [])
            Array.newInstance ( this.elementType, this.elements.length );
        System.arraycopy ( this.elements, 0,
                           defensive_duplicate, 0, this.elements.length );

        return defensive_duplicate;
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
        if ( as_type == null
             || ! as_type.isAssignableFrom ( this.elementType ) )
        {
            return default_value;
        }

        if ( this.elements.length == 0 )
        {
            // No point duplicating a 0-length array.
            // SuppressWarnings("unchecked") Cast ELEMENT [] to AS []
            return (AS []) this.elements;
        }

        final AS [] defensive_duplicate = (AS [])
            Array.newInstance ( as_type, this.elements.length );

        System.arraycopy ( this.elements, 0,
                           defensive_duplicate, 0, this.elements.length );

        return defensive_duplicate;
    }

    private final String elementString (
            int e
            )
    {
        final String element_string;
        if ( e < 0
             || e >= this.elements.length )
        {
            element_string = "??? [ " + e + " ] (this.elements.length = " + this.elements.length + ")";
        }
        else
        {
            element_string = StringRepresentation.of ( this.elements [ e ],
                                                       this.toStringMaxTotalLength );
        }

        return element_string;
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
        if ( filter == null )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "filter ( null )",
                                                 Parameter1.MustNotBeNull.CONTRACT.violation (
                                                     this,       // plaintiff
                                                     elements ) ); // evidence_or_null
        }

        // Make a defensive copy on which to filter.
        final ELEMENT [] defensive_copy = this.elements ();

        final Collection<ELEMENT> keepers;
        try
        {
            keepers =
                filter.keepers ( defensive_copy, // container
                                 null );         // keepers_or_null
        }
        catch ( Exception e )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "filter ( " + filter + " ) -> filter.keepers ( " + Arrays.toString ( defensive_copy ) + ", null )",
                                                 e );
        }

        if ( keepers == null )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "filter ( " + filter + " ) -> filter.keepers ( " + Arrays.toString ( defensive_copy ) + ", null ) -> null",
                                                 Return.NeverNull.CONTRACT.violation (
                                                     this  ,       // plaintiff
                                                     elements ) ); // evidence_or_null
        }
        else if ( keepers.size () == 0 )
        {
            return new ImmutableContainer<ELEMENT> ( this.version + 1,
                                                     this.orderOrNull, // Still sorted, since it's empty.
                                                     this.elementType );
        }
        else if ( keepers.size () == defensive_copy.length )
        {
            return this;
        }

        // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
        final ELEMENT [] template = (ELEMENT [])
            Array.newInstance ( this.elementType, keepers.size () );
        final ELEMENT [] filtered_elements =
            keepers.toArray ( template );

        final Container<ELEMENT> filtered_container =
            new ImmutableContainer<ELEMENT> ( this.version + 1,
                                              this.orderOrNull, // Still sorted, we've only removed, not reordered.
                                              this.elementType,
                                              filtered_elements );

        return filtered_container;
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
        if ( filter == null )
        {
            return (Container<ELEMENT> [])
                new Container []
                {
                    new ErrorContainer<ELEMENT> ( this,
                                                  "filterContainer ( null )",
                                                 Parameter1.MustNotBeNull.CONTRACT.violation (
                                                     this,       // plaintiff
                                                     elements ) ) // evidence_or_null
                };
        }

        final Filter<Object> object_filter = (Filter<Object>)
            ( (Object) filter );
        try
        {
            if ( object_filter.filter ( this ).isKept () )
            {
                return (Container<ELEMENT> [])
                    new Container [] { this };
            }
            else
            {
                return (Container<ELEMENT> [])
                    new Container [ 0 ];
            }
        }
        catch ( Exception e )
        {
            return (Container<ELEMENT> [])
                new Container [ 0 ];
        }
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
        if ( filter == null )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "filterElements ( null )",
                                                 Parameter1.MustNotBeNull.CONTRACT.violation (
                                                     this,       // plaintiff
                                                     elements ) ); // evidence_or_null
        }

        final Filter<Object> object_filter = (Filter<Object>)
            ( (Object) filter );
        int e = -1;
        final List<Integer> discards = new ArrayList<Integer> ();
        try
        {
            for ( e = 0; e < this.elements.length; e ++ )
            {
                if ( ! object_filter.filter ( this.elements [ e ] ).isKept () )
                {
                    discards.add ( e );
                }
            }
        }
        catch ( Exception ex )
        {
            final String element_string = this.elementString ( e );

            return new ErrorContainer<ELEMENT> ( this,
                                                 "filterElements ( " + filter + " ) -> filter.filter ( " + element_string + " ).isKept ()",
                                                 ex );
        }

        if ( discards.size () == this.elements.length )
        {
            return new ImmutableContainer<ELEMENT> ( this.version + 1,
                                                     this.orderOrNull, // Still sorted, we've only filtered, not reordered.
                                                     this.elementType );
        }
        else if ( discards.size () == 0 )
        {
            return this;
        }

        // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
        final ELEMENT [] filtered_elements = (ELEMENT [])
            Array.newInstance ( this.elementType,
                                this.elements.length - discards.size () );
        int start_index = 0;
        int offset = 0;
        for ( int d = 0; d <= discards.size (); d ++ )
        {
            final int end_index;
            if ( d < discards.size () )
            {
                end_index = discards.get ( d ) - 1;
            }
            else
            {
                end_index = discards.size () - 1;
            }

            if ( end_index <= start_index )
            {
                start_index = end_index + 1;
                continue;
            }

            System.arraycopy ( this.elements, start_index,
                               filtered_elements, offset, end_index - start_index + 1 );

            offset += end_index - start_index + 1;
            start_index = end_index + 1;
        }

        final Container<ELEMENT> filtered_container =
            new ImmutableContainer<ELEMENT> ( this.version + 1,
                                              this.orderOrNull, // Still sorted, we've only filtered, not reordered.
                                              this.elementType,
                                              filtered_elements );
        return filtered_container;
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
        if ( order == null
             || needle == null )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "find ( " + order + ", " + needle + " )",
                                                 EveryParameter.MustNotBeNull.CONTRACT.violation (
                                                     this,                                // plaintiff
                                                     new Object [] { order, needle } ) ); // evidence_or_null
        }
        else if ( this.orderOrNull == null
                  || ! this.orderOrNull.equals ( order ) )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "find ( " + order + ", " + needle + " ) (this.order = " + this.orderOrNull + ")",
                                                 new Parameter1.MustEqual<Order<ELEMENT>> ( this.orderOrNull ).violation (
                                                     this,      // plaintiff
                                                     order ) ); // evidence_or_null
        }

        final int index =
            Arrays.binarySearch ( this.elements,
                                  needle,
                                  this.orderOrNull ); // Just to be safe.
        if ( index < 0 )
        {
            return new ImmutableContainer<ELEMENT> (
                           this.version + 1,
                           this.orderOrNull, // Still sorted, but empty.
                           this.elementType ); // No elements.
        }

        int first = index;
        int last = index;

        for ( int b = index - 1; b >= 0; b -- )
        {
            if ( this.orderOrNull.compare ( needle,
                                            this.elements [ b ] ) != 0 )
            {
                break;
            }

            first = b;
        }

        for ( int a = index + 1; a < this.elements.length; a ++ )
        {
            if ( this.orderOrNull.compare ( needle,
                                            this.elements [ a ] ) != 0 )
            {
                break;
            }

            last = a;
        }

        // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
        final ELEMENT [] found_elements = (ELEMENT [])
            Array.newInstance ( this.elementType, last - first + 1 );
        System.arraycopy ( this.elements, first,
                           found_elements, 0, last - first + 1 );

        final Container<ELEMENT> found_container =
            new ImmutableContainer<ELEMENT> ( 
                    this.version + 1,
                    this.orderOrNull, // Still sorted, but all the same.
                    this.elementType,
                    found_elements );

        return found_container;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return super.hashCode ();
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
        if ( num_elements < 0
             || num_elements > this.elements.length )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "head ( " + num_elements + " ) (this.elements.length = " + this.elements.length + ")",
                                                 new Parameter1.MustBeBetween<Integer> ( 1, this.elements.length - 1 ).violation (
                                                     this,             // plaintiff
                                                     num_elements ) ); // evidence_or_null
        }

        // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
        final ELEMENT [] new_elements = (ELEMENT [])
            Array.newInstance ( this.elementType, num_elements );
        System.arraycopy ( this.elements, 0,
                           new_elements, 0, num_elements );

        final Container<ELEMENT> new_container =
            new ImmutableContainer<ELEMENT> ( this.version + 1,
                                              this.orderOrNull, // Still sorted, we've only lopped off the tail (cruelty to data structures!), not reordered.
                                              this.elementType,
                                              new_elements );
        return new_container;
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
        if ( insert_mode == null
             || elements == null )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "insert ( " + insert_mode + ", " + index + ", null )",
                                                 EveryParameter.MustNotBeNull.CONTRACT.violation (
                                                     this,       // plaintiff
                                                     new Object [] { insert_mode, index, elements } ) ); // evidence_or_null
        }
        else if ( elements.length == 0 )
        {
            return this;
        }
        else if ( index >= this.elements.length
                  || index < ( 0 - this.elements.length ) )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "insert ( " + insert_mode + ", " + index + ", " + Arrays.toString ( elements ) + " ) (this.elements.length = " + this.elements.length + ")",
                                                 new Parameter1.MustBeBetween<Integer> ( 0 - this.elements.length, this.elements.length - 1 ).violation (
                                                     this,      // plaintiff
                                                     index ) ); // evidence_or_null
        }

        final int actual_index;
        if ( index >= 0 )
        {
            actual_index = index;
        }
        else
        {
            actual_index = index + this.elements.length;
        }

        final int num_extra_elements;
        if ( insert_mode == Container.InsertMode.OVERWRITE )
        {
            final int num_elements_to_end = this.elements.length - actual_index;
            if ( num_elements_to_end < elements.length )
            {
                num_extra_elements = elements.length - num_elements_to_end;
            }
            else
            {
                num_extra_elements = 0;
            }
        }
        else // Container.InsertMode.INSERT
        {
            num_extra_elements = elements.length;
        }

        // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
        final ELEMENT [] new_elements = (ELEMENT [])
            Array.newInstance ( this.elementType, this.elements.length + num_extra_elements );
        if ( actual_index > 0 )
        {
            System.arraycopy ( this.elements, 0,
                               new_elements, 0, actual_index );
        }
        System.arraycopy ( elements, 0,
                           new_elements, actual_index, elements.length );
        if ( insert_mode != Container.InsertMode.OVERWRITE
             && actual_index < this.elements.length )
        {
            System.arraycopy ( this.elements, actual_index,
                               new_elements, actual_index + elements.length, this.elements.length - actual_index );
        }

        final Container<ELEMENT> new_container =
            new ImmutableContainer<ELEMENT> ( this.version + 1,
                                              null, // No longer sorted.
                                              this.elementType,
                                              new_elements );

        return new_container;
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
        if ( insert_mode == null
             || order == null
             || elements == null )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "insert ( " + insert_mode + ", " + order + ", " + Arrays.toString ( elements ) + " ) (this.order = " + this.orderOrNull + ")",
                                                 EveryParameter.MustNotBeNull.CONTRACT.violation (
                                                     this,                                  // plaintiff
                                                     new Object [] { order, elements } ) ); // evidence_or_null
        }
        else if ( this.orderOrNull == null
                  || ! this.orderOrNull.equals ( order ) )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "insert ( " + insert_mode + ", " + order + ", " + Arrays.toString ( elements ) + " ) (this.order = " + this.orderOrNull + ")",
                                                 new Parameter2.MustEqual<Order<ELEMENT>> ( this.orderOrNull ).violation (
                                                     this,      // plaintiff
                                                     order ) ); // evidence_or_null
        }

        // First, sort the new elements.
        // (Otherwise we can't be sure that they're in order.)
        try
        {
            order.sort ( elements );
        }
        catch ( Exception ex )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "insert ( " + insert_mode + ", " + order + ", " + Arrays.toString ( elements ) + " ) -> this.orderOrNull.sort ( elements ) (Internal Order was used to avoid hijinks, rather than parameter order: " + order + ")",
                                                 ex );
        }

        if ( this.elements.length == 0 )
        {
            // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
            final ELEMENT [] new_elements = (ELEMENT [])
                Array.newInstance ( this.elementType,
                                    elements.length );
            System.arraycopy ( elements, 0,
                               new_elements, 0, elements.length );

            final Container<ELEMENT> inserted_container =
                new ImmutableContainer<ELEMENT> ( this.version + 1,
                                                  this.orderOrNull, // Inserted in the same order.
                                                  this.elementType,
                                                  new_elements );
            return inserted_container;
        }

        final ELEMENT [] new_elements;
        int e = -1;
        try
        {
            // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
            final ELEMENT [] temp_elements = (ELEMENT [])
                Array.newInstance ( this.elementType,
                                    this.elements.length + elements.length );

            int start_search = 0;
            int end_search = this.elements.length;
            int offset = 0;
            int total_num_replaced = 0;
            for ( e = 0; e < elements.length; e ++ )
            {
                int insert_index =
                    Arrays.binarySearch ( this.elements,
                                          start_search,
                                          end_search + 1,
                                          elements [ e ],
                                          this.orderOrNull ); // Just to be safe.
                final int num_replaced;
                if ( insert_index < 0 )
                {
                    insert_index = 1 - insert_index;
                    num_replaced = 0;
                }
                else
                {
                    if ( insert_mode == Container.InsertMode.OVERWRITE )
                    {
                        num_replaced = 1;
                    }
                    else
                    {
                        num_replaced = 0;
                    }
                }
                total_num_replaced += num_replaced;

                final int start_copy = start_search;
                final int end_copy = insert_index - 1;
                if ( end_copy >= start_copy )
                {
                    System.arraycopy ( this.elements, start_copy,
                                       temp_elements, offset, end_copy - start_copy + 1 );
                    offset += end_copy - start_copy + 1;
                }

                temp_elements [ insert_index ] = elements [ e ];
                offset ++;

                start_search = insert_index + 1 + num_replaced;
            }

            if ( total_num_replaced == 0 )
            {
                new_elements = temp_elements;
            }
            else
            {
                // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
                new_elements = (ELEMENT [])
                    Array.newInstance ( this.elementType,
                                        temp_elements.length - total_num_replaced );
                System.arraycopy ( temp_elements, 0,
                                   new_elements, 0, temp_elements.length - total_num_replaced );
            }
        }
        catch ( Exception ex )
        {
            final String element_string = this.elementString ( e );
            return new ErrorContainer<ELEMENT> ( this,
                                                 "insert ( " + insert_mode + ", " + order + ", " + Arrays.toString ( elements ) + " ) -> Arrays.binarySearch ( " + Arrays.toString ( this.elements ) + ", " + element_string + ", " + this.orderOrNull + " ) (Internal Order was used to avoid hijinks, rather than parameter order: " + order + ")",
                                                 ex );
        }

        final Container<ELEMENT> inserted_container =
            new ImmutableContainer<ELEMENT> ( this.version + 1,
                                              this.orderOrNull, // Inserted in the same order.
                                              this.elementType,
                                              new_elements );
        return inserted_container;
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public final Iterator<ELEMENT> iterator ()
        throws Return.NeverNull.Violation
    {
        // Iterate over a defensive copy.
        // ArrayIterator doesn't expose or do anything to
        // the elements at the time of writing, but who
        // knows if or when that might change...
        // Defense first.
        final ELEMENT [] elements = this.elements ();
        return new ArrayIterator<ELEMENT> ( elements );
    }

    /**
     * @see musaico.foundation.container.Container#numElements()
     */
    @Override
    public final int numElements ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return this.elements.length;
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
        if ( this.orderOrNull == null )
        {
            return (Order<ELEMENT> []) new Order [ 0 ];
        }
        else
        {
            return (Order<ELEMENT> []) new Order [] { this.orderOrNull };
        }
    }

    /**
     * @see musaico.foundation.container.Container#orThrow()
     */
    @Override
    public final Container<ELEMENT> orThrow ()
        throws RuntimeException,
               Return.NeverNull.Violation
    {
        return this;
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
        if ( elements == null )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "prepend ( null )",
                                                 Parameter1.MustNotBeNull.CONTRACT.violation (
                                                     this,       // plaintiff
                                                     elements ) ); // evidence_or_null
        }
        else if ( elements.length == 0 )
        {
            return this;
        }

        // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
        final ELEMENT [] new_elements = (ELEMENT [])
            Array.newInstance ( this.elementType, elements.length + this.elements.length );
        System.arraycopy ( elements, 0,
                           new_elements, 0, elements.length );
        System.arraycopy ( this.elements, 0,
                           new_elements, elements.length, this.elements.length );

        final Container<ELEMENT> new_container =
            new ImmutableContainer<ELEMENT> ( this.version + 1,
                                              null, // No longer sorted.
                                              this.elementType,
                                              new_elements );

        return new_container;
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
        if ( elements == null )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "remove ( null )",
                                                 Parameter1.MustNotBeNull.CONTRACT.violation (
                                                     this,       // plaintiff
                                                     elements ) ); // evidence_or_null
        }
        else if ( elements.length == 0 )
        {
            return this;
        }

        final Set<ELEMENT> to_remove =
            new HashSet<ELEMENT> ();
        boolean is_null_to_be_removed = false;
        if ( this.orderOrNull == null )
        {
            // Unsoerted.
            for ( ELEMENT element : elements )
            {
                if ( element == null )
                {
                    is_null_to_be_removed = true;
                }
                else
                {
                    to_remove.add ( element );
                }
            }
        }

        final List<Integer> discards = new ArrayList<Integer> ();

        boolean is_sorted = false;
        if ( this.orderOrNull != null )
        {
            // Sorted.
            is_sorted = true;
            for ( ELEMENT element : elements )
            {
                if ( element == null )
                {
                    // Uh-oh.  Can't binary search for null.
                    // Revert to unsorted.
                    discards.clear ();
                    is_sorted = false;
                    break;
                }

                final int element_index =
                    Arrays.binarySearch ( this.elements,
                                          element,
                                          this.orderOrNull );
                if ( element_index >= 0 )
                {
                    discards.add ( element_index );
                }
            }
        }

        if ( ! is_sorted )
        {
            // Unsorted.
            for ( int e = 0; e < this.elements.length; e ++ )
            {
                if ( this.elements [ e ] == null )
                {
                    if ( is_null_to_be_removed )
                    {
                        discards.add ( e );
                    }
                }
                else if ( to_remove.contains ( this.elements [ e ] ) )
                {
                    discards.add ( e );
                }
            }
        }

        if ( discards.size () == this.elements.length )
        {
            return new ImmutableContainer<ELEMENT> ( this.version + 1,
                                                     this.orderOrNull, // Still sorted, we've only removed, not reordered.
                                                     this.elementType );
        }
        else if ( discards.size () == 0 )
        {
            return this;
        }

        // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
        final ELEMENT [] post_removal_elements = (ELEMENT [])
            Array.newInstance ( this.elementType,
                                this.elements.length - discards.size () );
        int start_index = 0;
        int offset = 0;
        for ( int d = 0; d <= discards.size (); d ++ )
        {
            final int end_index;
            if ( d < discards.size () )
            {
                end_index = discards.get ( d ) - 1;
            }
            else
            {
                end_index = discards.size () - 1;
            }

            if ( end_index <= start_index )
            {
                start_index = end_index + 1;
                continue;
            }

            System.arraycopy ( this.elements, start_index,
                               post_removal_elements, offset, end_index - start_index + 1 );

            offset += end_index - start_index + 1;
            start_index = end_index + 1;
        }

        final Container<ELEMENT> post_removal_container =
            new ImmutableContainer<ELEMENT> ( this.version + 1,
                                              this.orderOrNull, // Still sorted, we've only removed, not reordered.
                                              this.elementType,
                                              post_removal_elements );
        return post_removal_container;
    }

    /**
     * @see musaico.foundation.container.Container#reverse()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Object - ELEMENT [],
    public final Container<ELEMENT> reverse ()
        throws Return.NeverNull.Violation
    {
        // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
        final ELEMENT [] reversed_elements = (ELEMENT [])
            Array.newInstance ( this.elementType,
                                this.elements.length );

        final int mid = this.elements.length / 2;
        for ( int e = 0; e < mid; e ++ )
        {
            final int r = this.elements.length - e;
            reversed_elements [ e ] = this.elements [ r ];
            reversed_elements [ r ] = this.elements [ e ];
        }

        final int m = mid + ( mid % 2 );
        if ( m != mid )
        {
            reversed_elements [ mid ] = this.elements [ mid ];
        }

        final Order<ELEMENT> reverse_order;
        if ( this.orderOrNull == null )
        {
            reverse_order = null;
        }
        else
        {
            reverse_order = this.orderOrNull.reverseOrder ();
        }

        final Container<ELEMENT> reversed_container =
            new ImmutableContainer<ELEMENT> ( this.version + 1,
                                              reverse_order,
                                              this.elementType,
                                              reversed_elements );
        return reversed_container;
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
        if ( elements == null )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "set ( null )",
                                                 Parameter1.MustNotBeNull.CONTRACT.violation (
                                                     this,       // plaintiff
                                                     elements ) ); // evidence_or_null
        }

        final Container<ELEMENT> new_container =
            new ImmutableContainer<ELEMENT> ( this.version + 1,
                                              null, // No more sort order.
                                              this.elementType,
                                              elements );

        return new_container;
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
        if ( order == null )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "sort ( null )",
                                                 Parameter1.MustNotBeNull.CONTRACT.violation (
                                                     this,       // plaintiff
                                                     elements ) ); // evidence_or_null
        }

        final ELEMENT [] defensive_copy =
            this.elements ();
        final ELEMENT [] sorted_elements =
            order.sort ( defensive_copy );

        final Container<ELEMENT> sorted_container =
            new ImmutableContainer<ELEMENT> ( this.version + 1,
                                              order, // Now sorted according to the specified order.
                                              this.elementType,
                                              sorted_elements );

        return sorted_container;
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
        if ( num_elements < 0
             || num_elements > this.elements.length )
        {
            return new ErrorContainer<ELEMENT> ( this,
                                                 "tail ( " + num_elements + " ) (this.elements.length = " + this.elements.length + ")",
                                                 new Parameter1.MustBeBetween<Integer> ( 1, this.elements.length - 1 ).violation (
                                                     this,             // plaintiff
                                                     num_elements ) ); // evidence_or_null
        }

        // SuppressWarnings("unchecked") Cast Object - ELEMENT [],
        final ELEMENT [] new_elements = (ELEMENT [])
            Array.newInstance ( this.elementType, num_elements );
        System.arraycopy ( this.elements, 0,
                           new_elements, 0, num_elements );

        final Container<ELEMENT> new_container =
            new ImmutableContainer<ELEMENT> ( this.version + 1,
                                              this.orderOrNull, // Still sorted, we've only lopped off the head, not reordered.
                                              this.elementType,
                                              new_elements );
        return new_container;
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
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "container [" );

        boolean is_first = true;
        for ( Object datum : this.elements )
        {
            if ( is_first )
            {
                is_first = false;
                sbuf.append ( " " );
            }
            else
            {
                sbuf.append ( ", " );
            }

            final String datum_string =
                StringRepresentation.of ( datum,
                                          this.toStringMaxTotalLength );
            sbuf.append ( datum_string );
        }

        if ( is_first )
        {
            sbuf.append ( "]" );
        }
        else
        {
            sbuf.append ( " ]" );
        }

        final String as_string = sbuf.toString ();
        return as_string;
    }

    /**
     * @see musaico.foundation.container.Container#version()
     */
    @Override
    public final int version ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return this.version;
    }
}
