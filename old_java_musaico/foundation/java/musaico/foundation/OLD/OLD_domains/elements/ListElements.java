package musaico.foundation.domains.elements;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;


import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.StringRepresentation;

import musaico.foundation.domains.iterators.CollectionIterator;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * The Elements of a 1-dimensional Collection, such as a List or a Set.
 * </p>
 *
 *
 * <p>
 * In Java every Elements must implement <code> equals ( ... ) </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Elements must be Serializable in order to
 * play nicely across RMI.  However users of the Elements
 * must be careful, since the elements and any other data or metadata
 * stored inside might not be Serializable.
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
 * @see musaico.foundation.domains.elements.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.elements.MODULE#LICENSE
 */
public class ListElements<ELEMENT extends Object>
    extends AbstractElements<List<ELEMENT>, ELEMENT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new sequence of immutable, fixed-length empty ListElements.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @throws NullPointerException If the specified element_class is null.
     */
    public ListElements (
            Class<ELEMENT> element_class
            )
        throws NullPointerException
    {
        // Throws ElementsFlagsException (but shouldn't,
        // since we've been careful to not use variable length flag):
        this ( element_class,                  // element_class
               ElementFlags.MUTABLE,           // flags
               new ArrayList<ELEMENT> ( 0 ) ); // container
    }


    /**
     * <p>
     * Creates a new sequence of empty ListElements.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param flags The ElementFlags, specifying whether these
     *              Elements are mutable or immutable, fixed length
     *              or variable length, and so on.  Must not be null.
     *
     * @throws NullPointerException If the specified element_class
     *                              or flags is/are null.
     */
    public ListElements (
            Class<ELEMENT> element_class,
            ElementFlags flags
            )
        throws NullPointerException
    {
        // Throws ElementsFlagsException (but shouldn't,
        // since we've been careful to not use variable length flag):
        this ( element_class, // element_class
               flags,         // flags
               new ArrayList<ELEMENT> ( 0 ) ); // container
    }


    /**
     * <p>
     * Creates a new sequence of mutable, fixed-length,
     * unordered ListElements.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param container The List container to be represented by these
     *                  ListElements, or some other type of Collection
     *                  (such as a Set) to be converted into ListElements.
     *                  Can contain null elements.
     *                  Element values can be modified by the caller,
     *                  though the caller must ensure concurrency protection
     *                  is in place around these Elements.  Must not be null.
     *
     * @throws NullPointerException If the specified element_class
     *                              and/or container is/are null.
     */
    public ListElements (
            Class<ELEMENT> element_class,
            Collection<ELEMENT> container
            )
        throws NullPointerException
    {
        // Throws ElementsFlagsException (but shouldn't,
        // since we've been careful to not use variable length flag):
        this ( element_class,             // element_class
               ElementFlags.MUTABLE,      // flags
               container );               // container
    }


    /**
     * <p>
     * Creates a new sequence of unordered ListElements.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param container The List container to be represented by these
     *                  ListElements, or some other type of Collection
     *                  (such as a Set) to be converted into ListElements.
     *                  Can contain null elements.
     *                  Element values can be modified by the caller,
     *                  though the caller must ensure concurrency protection
     *                  is in place around these Elements.  Must not be null.
     *
     * @param flags The ElementFlags, specifying whether these
     *              Elements are mutable or immutable, fixed length
     *              or variable length, and so on.  Must not be null.
     *
     * @param orders 0 or more Comparators by which these Elements have
     *               already been sorted, in order from # 0 to # last.
     *               Can be empty.  Can be null, which is treated as empty.
     *               Must not contain any null elements.
     *
     * @throws NullPointerException If the specified element_class
     *                              and/or flags and/or container
     *                              is/are null.
     */
    @SuppressWarnings("varargs") // generic varargs heap pollution (varargs).
    public ListElements (
            Class<ELEMENT> element_class,
            ElementFlags flags,
            Collection<ELEMENT> container
            )
        throws NullPointerException,
               ElementsFlagsException
    {
        this ( element_class,                   // element_class
               flags,                           // flags
               container,                       // container
               null );                          // orders
    }


    /**
     * <p>
     * Creates a new sequence of ListElements.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param flags The ElementFlags, specifying whether these
     *              Elements are mutable or immutable, fixed length
     *              or variable length, and so on.  Must not be null.
     *
     * @param container The List container to be represented by these
     *                  ListElements, or some other type of Collection
     *                  (such as a Set) to be converted into ListElements.
     *                  Can contain null elements.
     *                  Element values can be modified by the caller,
     *                  though the caller must ensure concurrency protection
     *                  is in place around these Elements.  Must not be null.
     *
     * @param orders 0 or more Comparators by which these Elements have
     *               already been sorted, in order from # 0 to # last.
     *               Can be empty.  Can be null, which is treated as empty.
     *               Must not contain any null elements.
     *
     * @throws NullPointerException If the specified element_class
     *                              and/or flags and/or container
     *                              and/or any element of the specified
     *                              orders is/are null.
     */
    @SafeVarargs
    @SuppressWarnings("varargs") // Generic varargs heap pollution.
    public ListElements (
            Class<ELEMENT> element_class,
            ElementFlags flags,
            Collection<ELEMENT> container,
            Comparator<ELEMENT> ... orders
            )
        throws NullPointerException,
               ElementsFlagsException
    {
        super ( element_class,                // element_class
                flags,                        // flags
                ( container instanceof List ) // container
                    ? (List<ELEMENT>) container
                    : new ArrayList<ELEMENT> ( container ),
                orders );                     // orders
    }


    /**
     * <p>
     * Creates a new sequence of ListElements.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param flags The ElementFlags, specifying whether these
     *              Elements are mutable or immutable, fixed length
     *              or variable length, and so on.  Must not be null.
     *
     * @param container The List container to be represented by these
     *                  ListElements, or some other type of Collection
     *                  (such as a Set) to be converted into ListElements.
     *                  Can contain null elements.
     *                  Element values can be modified by the caller,
     *                  though the caller must ensure concurrency protection
     *                  is in place around these Elements.  Must not be null.
     *
     * @param orders 0 or more Comparators by which these Elements have
     *               already been sorted, in order from # 0 to # last.
     *               Can be empty.  Can be null, which is treated as empty.
     *               Must not contain any null elements.
     *
     * @throws NullPointerException If the specified element_class
     *                              and/or flags and/or container
     *                              and/or any element of the specified
     *                              orders is/are null.
     */
    public ListElements (
            Class<ELEMENT> element_class,
            ElementFlags flags,
            Collection<ELEMENT> container,
            Elements<Comparator<ELEMENT>> orders
            )
        throws NullPointerException,
               ElementsFlagsException
    {
        super ( element_class,                // element_class
                flags,                        // flags
                ( container instanceof List ) // container
                    ? (List<ELEMENT>) container
                    : new ArrayList<ELEMENT> ( container ),
                orders );                     // orders
    }


    /**
     * <p>
     * Creates a new sequence of ListElements.
     * </p>
     *
     * @param flags The ElementFlags, specifying whether these
     *              Elements are mutable or immutable, fixed length
     *              or variable length, and so on.  Must not be null.
     *
     * @param elements The Elements to include in these new ListElements.
     *                 Must not be null.
     *
     * @throws NullPointerException If the specified flags or elements
     *                              is/are null.
     *
     * @throws ElementsLengthException If elements contains too many elements
     *                                 to store in Elements of this
     *                                 container type.  For example,
     *                                 list containers cannot have
     *                                 more than
     *                                 <copde> Integer.MAX_VALUE </code>
     *                                 elements.
     *
     * @throws ElementsFlagsException If the specified flags
     *                                are incompatible with
     *                                this type of Elements.
     *                                For example, an array container
     *                                cannot be
     *                                <code> ElementFlags.VARIABLE_LENGTH </code>.
     *
     * @throws ConcurrentModificationException If a change in length
     *                                         or content is detected
     *                                         in the specified Elements
     *                                         during processing.  If the
     *                                         Elements are shared across
     *                                         Threads then they must be
     *                                         locked before calling
     *                                         this method.
     */
    public ListElements (
            ElementFlags flags,
            Elements<ELEMENT> elements
            )
        throws NullPointerException,
               ElementsLengthException,
               ElementsFlagsException,
               ConcurrentModificationException
    {
        // Throws all the exceptions, calls extractContentsFrom ( elements ):
        super ( flags,      // flags
                elements ); // elements
    }




    /**
     * @see musaico.foundation.domains.elements.AbstractElements#extractContentsFrom(musaico.foundation.domains.elements.Elements)
     */
    @Override
    protected List<ELEMENT> extractContentsFrom (
            Elements<ELEMENT> elements
            )
        throws NullPointerException,
               ElementsLengthException,
               ElementsFlagsException,
               ConcurrentModificationException
    {
        final long length = elements.length ();
        if ( length < 0L
             || length > (long) Integer.MAX_VALUE )
        {
            throw new ElementsLengthException ( this,
                                                length );
        }

        final Class<ELEMENT> element_class = elements.elementClass ();

        final List<ELEMENT> old_list;
        if ( elements instanceof ListElements )
        {
            final ListElements<ELEMENT> list_elements =
                (ListElements<ELEMENT>) elements;
            old_list = list_elements.container ();

            final ElementFlags flags = elements.flags ();
            if ( ! flags.isOverwritable ()
                 && ! flags.isVariableLength () )
            {
                return old_list;
            }
        }
        else
        {
            old_list = null;
        }

        final List<ELEMENT> new_list;
        try
        {
            new_list = new ArrayList<ELEMENT> ( (int) length );
        }
        catch ( Throwable t )
        {
            throw new ElementsLengthException ( this,
                                                length,
                                                t );
        }

        if ( old_list != null )
        {
            new_list.addAll ( old_list );

            if ( new_list.size () != (int) length )
            {
                throw new ConcurrentModificationException (
                    "ERROR the length of "
                    + elements
                    + " changed from "
                    + length
                    + " to "
                    + new_list.size ()
                    + " while creating a new "
                    + ClassName.of ( this.getClass () )
                    + " from them using "
                    + this );
            }
        }
        else if ( elements instanceof ArrayElements )
        {
            // Iterating through an array is faster than iterating
            // through Elements by an order of magnitude.
            final ArrayElements<ELEMENT>that_array_elements =
                (ArrayElements<ELEMENT>) elements;
            final ELEMENT [] that_array =
                that_array_elements.container ();

            if ( that_array.length != (int) length )
            {
                throw new ConcurrentModificationException (
                    "ERROR the length of "
                    + elements
                    + " changed from "
                    + length
                    + " to "
                    + that_array.length
                    + " while creating a new "
                    + ClassName.of ( this.getClass () )
                    + " from them using "
                    + this );
            }

            Collections.addAll ( new_list, that_array );
        }
        else
        {
            int e = 0;
            for ( ELEMENT element : elements )
            {
                if ( e >= (int) length )
                {
                    throw new ConcurrentModificationException (
                        "ERROR the length of "
                        + elements
                        + " changed from "
                        + length
                        + " to at least "
                        + ( e + 1 )
                        + " ("
                        + elements.length ()
                        + ") while creating a new "
                        + ClassName.of ( this.getClass () )
                        + " from them using "
                        + this );
                }

                new_list.add ( element );
                e ++;
            }

            if ( e < ( new_list.size () - 1 ) )
            {
                throw new ConcurrentModificationException (
                    "ERROR the length of "
                    + elements
                    + " changed from "
                    + length
                    + " to at least "
                    + ( e + 1 )
                    + " ("
                    + elements.length ()
                    + ") while creating a new "
                    + ClassName.of ( this.getClass () )
                    + " from them using "
                    + this );
            }
        }

        return new_list;
    }




    /**
     * <p>
     * Searches a List (haystack) for the specified Object (needle),
     * starting at the specified index, up to the specified length
     * to search, using the specified comparator to determine which
     * direction to search in.
     * </p>
     *
     * @param haystack The List to search.  Must not be null.
     *
     * @param start_index The index at which to start searching.
     *                    Must be 0 or greater, but less than the specified
     *                    maybe_length.
     *
     * @param maybe_length The number of elements of the List to search,
     *                     starting from the specified start_index.
     *                     Must be greater than 0.
     *
     * @param needle The object to search for.  Can be null...???!!!
     *
     * @param comparator The order of the List, used to determine which
     *                   direction to search in.  Must not be null.
     */
    public static final <NEEDLE extends Object>
        int binarySearch (
            List<NEEDLE> haystack,
            int start_index,
            int maybe_length,
            NEEDLE needle,
            Comparator<NEEDLE> comparator
            )
    {
        if ( start_index == 0
             && maybe_length == haystack.size () )
        {
            return Collections.binarySearch ( haystack,
                                              needle,
                                              comparator );
        }

        final List<NEEDLE> sublist =
            haystack.subList ( start_index, start_index + maybe_length );
        final int found_offset =
            Collections.binarySearch ( sublist, needle, comparator );
        if ( found_offset < 0 )
        {
            return found_offset;
        }
        else if ( found_offset == 0 )
        {
            return start_index;
        }

        // Found the needle somewhere after the start of the collection.
        // Maybe there's another matching needle immediately before
        // the one we found?
        int first_found_index = start_index + found_offset;
        while ( first_found_index >= start_index )
        {
            final NEEDLE maybe_first = haystack.get ( first_found_index - 1 );
            if ( comparator.compare ( needle, maybe_first ) != 0 )
            {
                return first_found_index;
            }

            // Found another matching needle.  Keep going backward,
            // toward the beginning of the collection.  See if we
            // can find any more.
            first_found_index --;
        }

        return first_found_index;
    }




    /**
     * @see musaico.foundation.domains.elements.Elements#array()
     */
    @Override
    @SuppressWarnings("unchecked") // If Array.newInstance() does not return
        // ELEMENT [], then I am a monkey's uncle.
        // Ook.
    public final ELEMENT [] array ()
        // Never throws ElementsLengthException.
    {
        final Class<ELEMENT> element_class = this.elementClass ();

        final List<ELEMENT> collection = this.container ();
        final ELEMENT [] new_array = (ELEMENT [])
            Array.newInstance ( element_class, collection.size () );

        int e = 0;
        for ( ELEMENT element : collection )
        {
            new_array [ e ] = element;
            e ++;
        }

        return new_array;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#at(long)
     */
    @Override
    @SuppressWarnings("unchecked") // If Array.newInstance() does not return
        // ELEMENT [], then I am a monkey's uncle.
        // Ook.
    public final ELEMENT [] at (
        long offset
        )
    {
        final long length = this.length ();
        final long index =
            AbstractElements.clamp ( offset,
                                     length );
        if ( index < 0L
             || index >= length )
        {
            return this.none ();
        }

        final ELEMENT [] singleton = (ELEMENT [])
            Array.newInstance ( this.elementClass (),
                                1 );

        final List<ELEMENT> collection = this.container ();
        singleton [ 0 ] = collection.get ( (int) index );

        return singleton;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#collection()
     */
    @Override
    public final Collection<ELEMENT> collection ()
        // Never throws ElementsLengthException.
    {
        return this.list ();
    }


    /**
     * @see musaico.foundation.domains.elements.AbstractElements#contentsString()
     */
    protected final String contentsString ()
    {
        final List<ELEMENT> list = this.container ();
        final String contents_string =
            StringRepresentation.of (
                list,
                StringRepresentation.DEFAULT_ARRAY_LENGTH );
        return contents_string;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#duplicate(musaico.foundation.domains.elements.ElementFlags)
     */
    @Override
    public final ListElements<ELEMENT> duplicate (
            ElementFlags flags
            )
        throws NullPointerException,
               ElementsFlagsException
    {
        if ( flags == null )
        {
            throw new NullPointerException ( "ERROR "
                                             + this
                                             + " cannot be duplicated"
                                             + " with null flags" );
        }

        final Class<ELEMENT> element_class = this.elementClass ();

        final List<ELEMENT> collection = this.container ();
        final List<ELEMENT> new_collection =
            new ArrayList<ELEMENT> ( collection );

        final ListElements<ELEMENT> new_elements =
            new ListElements<ELEMENT> ( element_class,
                                        flags,
                                        new_collection );

        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.AbstractElements#equalsElements(java.lang.Object, musaico.foundation.domains.elements.AbstractElements, java.lang.Object)
     */
    @Override
    protected final boolean equalsElements (
            List<ELEMENT> this_container,
            AbstractElements<List<ELEMENT>, ELEMENT> that_elements,
            List<ELEMENT> that_container
            )
    {
        if ( this_container == null
             || that_container == null )
        {
            // Corrupt.
            return false;
        }

        if ( this_container.size () != that_container.size () )
        {
            return false;
        }

        for ( int e = 0; e < this_container.size (); e ++ )
        {
            final ELEMENT this_element = this_container.get ( e );
            final ELEMENT that_element = that_container.get ( e );

            if ( this_element == null )
            {
                if ( that_element != null )
                {
                    return false;
                }
            }
            else if ( that_element == null )
            {
                return false;
            }
            else if ( this_element != that_element
                      && ! this_element.equals ( that_element ) )
            {
                return false;
            }
        }

        // The length is equal, and all of the elements are equal.
        return true;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#filter(musaico.foundation.filter.Filter[])
     */
    @Override
    @SafeVarargs
    public final ListElements<ELEMENT> filter (
            Filter<ELEMENT> ... filters
            )
        throws NullPointerException
    {
        if ( filters == null )
        {
            throw new NullPointerException ( "ERROR: "
                                             + this
                                             + " cannot filter elements with"
                                             + " filters = " + filters );
        }

        final List<ELEMENT> collection = this.container ();
        final List<ELEMENT> filtered_list = new ArrayList<ELEMENT> ();
        boolean is_any_filtered_out = false;
        for ( ELEMENT element : collection )
        {
            boolean is_element_filtered_out = false;
            for ( Filter<ELEMENT> filter : filters )
            {
                if ( filter == null )
                {
                    throw new NullPointerException ( "ERROR: "
                                                     + this
                                                     + " cannot filter"
                                                     + " elements with"
                                                     + " filter = " + filter );
                }

                if ( ! filter.filter ( element ).isKept () )
                {
                    // Filtered out.
                    is_element_filtered_out = true;
                    break;
                }
            }

            if ( is_element_filtered_out )
            {
                is_any_filtered_out = true;
            }
            else
            {
                filtered_list.add ( element );
            }
        }

        if ( ! is_any_filtered_out )
        {
            return this;
        }

        if ( this.flags ().isVariableLength () )
        {
            collection.clear ();
            collection.addAll ( filtered_list );

            return this;
        }
        else
        {
            final Class<ELEMENT> element_class = this.elementClass ();

            final ListElements<ELEMENT> filtered_elements =
                new ListElements<ELEMENT> ( element_class,
                                            this.flags (),
                                            filtered_list );

            return filtered_elements;
        }
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#find(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // ELEMENT --> Comp<ELEMENT> in try/catch.
    public final Elements<Long> find (
            ELEMENT element
            )
        throws ElementsLengthException
    {
        final List<ELEMENT> collection = this.container ();
        final Elements<Comparator<ELEMENT>> orders = this.order ();

        final Comparable<ELEMENT> comparable;
        if ( element instanceof Comparable )
        {
            Comparable<ELEMENT> maybe_comparable;
            try
            {
                maybe_comparable = (Comparable<ELEMENT>) element;
            }
            catch ( ClassCastException e )
            {
                maybe_comparable = null;
            }

            comparable = maybe_comparable;
        }
        else
        {
            comparable = null;
        }

        final List<Long> found = new ArrayList<Long> ();
        if ( orders.isEmpty () )
        {
            int a = 0;
            for ( ELEMENT compare_to_element : collection )
            {
                final boolean is_found;
                if ( compare_to_element == null )
                {
                    if ( element == null )
                    {
                        is_found = true;
                    }
                    else
                    {
                        is_found = false;
                    }
                }
                else if ( comparable != null )
                {
                    if ( comparable.compareTo ( compare_to_element ) == 0 )
                    {
                        is_found = true;
                    }
                    else
                    {
                        is_found = false;
                    }
                }
                else if ( compare_to_element.equals ( element ) )
                {
                    is_found = true;
                }
                else
                {
                    is_found = false;
                }

                if ( is_found )
                {
                    found.add ( Long.valueOf ( (long) a ) );
                }

                a ++;
            }
        }
        else
        {
            final Comparator<ELEMENT> order =
                orders.at ( Elements.LAST ) [ 0 ];

            final int collection_length = collection.size ();
            for ( int a = Collections.binarySearch ( collection,
                                                     element,
                                                     order );
                  a >= 0 && a < collection_length;
                  a = ListElements.binarySearch ( collection,
                                                  a + 1, collection_length,
                                                  element, order ) )
            {
                found.add ( Long.valueOf ( (long) a ) );
            }
        }

        final Elements<Long> found_offsets =
            new ListElements<Long> ( Long.class,
                                     ElementFlags.IMMUTABLE,
                                     found );

        return found_offsets;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#from(java.lang.Class, musaico.foundation.domains.elements.ElementFlags, Object[])
     */
    @Override
    @SafeVarargs
    @SuppressWarnings("varargs") // Pass T_E [] to toString, constructor.
    public final <THAT_ELEMENT extends Object>
        ListElements<THAT_ELEMENT> from (
            Class<THAT_ELEMENT> element_class,
            ElementFlags flags,
            THAT_ELEMENT ... array
            )
        throws NullPointerException,
               ElementsLengthException,
               ElementsFlagsException,
               ConcurrentModificationException
    {
        if ( element_class == null
             || flags == null
             || array == null )
        {
            final String array_string =
                StringRepresentation.of ( array,
                                          StringRepresentation.DEFAULT_ARRAY_LENGTH );
            throw new NullPointerException ( "ERROR: Can't create "
                                             + ClassName.of ( this.getClass () )
                                             + " from element class "
                                             + element_class
                                             + " Flags "
                                             + flags
                                             + " Elements "
                                             // Varargs heap pollution:
                                             + array_string );
        }

        final List<THAT_ELEMENT> list =
            new ArrayList<THAT_ELEMENT> ( array.length );
        Collections.addAll ( list, array );

        final ListElements<THAT_ELEMENT> new_elements =
            new ListElements<THAT_ELEMENT> ( element_class,
                                             flags,
                                             list );

        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#from(musaico.foundation.domains.elements.ElementFlags, musaico.foundation.domains.elements.Elements)
     */
    @Override
    public final <THAT_ELEMENT extends Object>
        ListElements<THAT_ELEMENT> from (
            ElementFlags flags,
            Elements<THAT_ELEMENT> that
            )
        throws NullPointerException,
               ElementsLengthException,
               ElementsFlagsException,
               ConcurrentModificationException
    {
        // Throws all the exceptions:
        final ListElements<THAT_ELEMENT> new_elements =
            new ListElements<THAT_ELEMENT> ( flags,
                                             that );

        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#has(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // ELEMENT --> Comp<ELEMENT> in try/catch.
    public final boolean has (
            ELEMENT element
            )
    {
        final List<ELEMENT> list = this.container ();
        final Elements<Comparator<ELEMENT>> orders = this.order ();

        final Comparable<ELEMENT> comparable;
        if ( element instanceof Comparable )
        {
            Comparable<ELEMENT> maybe_comparable;
            try
            {
                maybe_comparable = (Comparable<ELEMENT>) element;
            }
            catch ( ClassCastException e )
            {
                maybe_comparable = null;
            }

            comparable = maybe_comparable;
        }
        else
        {
            comparable = null;
        }

        if ( orders.isEmpty () )
        {
            for ( int a = 0; a < list.size (); a ++ )
            {
                final ELEMENT compare_to_element = list.get ( a );
                if ( compare_to_element == null )
                {
                    if ( element == null )
                    {
                        return true;
                    }
                }
                else if ( comparable != null )
                {
                    if ( comparable.compareTo ( compare_to_element ) == 0 )
                    {
                        return true;
                    }

                }
                else if ( compare_to_element.equals ( element ) )
                {
                    return true;
                } // else not found.
            }
        }
        else
        {
            final Comparator<ELEMENT> order =
                orders.at ( Elements.LAST ) [ 0 ];

            final int a = Collections.binarySearch ( list, element, order );
            if ( a >= 0
                 && a < list.size () )
            {
                return true;
            }
        }

        return false;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        final List<ELEMENT> list = this.container ();
        final int size = list.size ();
        int hash_code = 0;
        for ( int e = 0; e < 4 && e < size; e ++ )
        {
            final ELEMENT element = list.get ( e );;
            if ( element != null )
            {
                hash_code += element.hashCode ();
            }
        }

        int start = size - 4;
        if ( start < 4 )
        {
            start = 4;
        }

        for ( int e = start; e < size; e ++ )
        {
            final ELEMENT element = list.get ( e );
            if ( element != null )
            {
                hash_code += element.hashCode ();
            }
        }

        return hash_code;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#insert(long, java.lang.Object[])
     */
    @Override
    @SafeVarargs
    @SuppressWarnings("varargs") // Generic varargs heap pollution,
    public final ListElements<ELEMENT> insert (
            long offset,
            ELEMENT ... elements
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ElementsLengthException
    {
        if ( elements == null )
        {
            throw new NullPointerException ( "ERROR Cannot insert elements "
                                             + " at offset = " + offset
                                             + " elements = " + elements
                                             + " into "
                                             + this );
        }
        else if ( elements.length == 0 )
        {
            // Error check the offset, then just return this as-is.
            if ( offset != Elements.AFTER_LAST )
            {
                final List<ELEMENT> original_list = this.container ();
                final long index =
                    AbstractElements.clamp ( offset,
                                             (long) original_list.size () );
                if ( index < 0L
                     || index > (long) original_list.size () )
                {
                    final String elements_string =
                        StringRepresentation.of ( elements,
                                                  StringRepresentation.DEFAULT_ARRAY_LENGTH );
                    throw new IndexOutOfBoundsException ( "ERROR Cannot insert"
                                                          + "elements at"
                                                          + " offset "
                                                          + offset
                                                          + " with elements "
                                                          + elements_string
                                                          + " into "
                                                          + this );
                }
            }

            return this;
        }

        final List<ELEMENT> original_list = this.container ();
        if ( Integer.MAX_VALUE - original_list.size () < elements.length )
        {
            // Too big for a list.
            final String elements_string =
                "" + this + " + "
                + StringRepresentation.of ( elements,
                                            StringRepresentation.DEFAULT_ARRAY_LENGTH );
            final long would_be_length =
                (long) original_list.size () + (long) elements.length;
            throw new ElementsLengthException ( elements_string,
                                                would_be_length );
        }

        final List<ELEMENT> new_list;
        if ( this.flags ().isVariableLength () )
        {
            new_list = original_list;
        }
        else
        {
            new_list =
                new ArrayList<ELEMENT> ( original_list.size ()
                                         + elements.length );
        }

        if ( offset == Elements.AFTER_LAST
             || offset == (long) original_list.size () )
        {
            if ( new_list != original_list )
            {
                new_list.addAll ( original_list );
            }

            Collections.addAll ( new_list, elements );
        }
        else
        {
            final long index =
                AbstractElements.clamp ( offset,
                                         (long) original_list.size () );
            if ( index < 0L
                 || index >= (long) original_list.size () )
            {
                final String elements_string =
                    StringRepresentation.of ( elements,
                                              StringRepresentation.DEFAULT_ARRAY_LENGTH );
                throw new IndexOutOfBoundsException ( "ERROR Cannot insert"
                                                      + "elements at offset "
                                                      + offset
                                                      + " with elements "
                                                      + elements_string
                                                      + " into "
                                                      + this );
            }

            if ( index == 0L )
            {
                if ( new_list == original_list )
                {
                    for ( int e = 0; e < elements.length; e ++ )
                    {
                        new_list.add ( e, elements [ e ] );
                    }
                }
                else
                {
                    Collections.addAll ( new_list, elements );
                    new_list.addAll ( original_list );
                }
            }
            else
            {
                if ( new_list == original_list )
                {
                    for ( int e = 0; e < elements.length; e ++ )
                    {
                        new_list.add ( e + (int) index, elements [ e ] );
                    }
                }
                else
                {
                    final List<ELEMENT> before =
                        original_list.subList ( 0, (int) index );
                    new_list.addAll ( before );

                    Collections.addAll ( new_list, elements );

                    final List<ELEMENT> after =
                        original_list.subList ( (int) index,
                                                original_list.size () );
                    new_list.addAll ( after );
                }
            }
        }

        final ListElements<ELEMENT> new_elements;
        if ( this.flags ().isVariableLength () )
        {
            new_elements = this;
        }
        else
        {
            new_elements =
                new ListElements<ELEMENT> ( this.elementClass (),
                                            this.flags (),
                                            new_list );
        }

        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#insertAll(long, musaico.foundation.domains.elements.Elements)
     */
    @Override
    public final ListElements<ELEMENT> insertAll (
            long offset,
            Elements<ELEMENT> elements
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ElementsLengthException,
               ConcurrentModificationException
    {
        if ( elements == null )
        {
            throw new NullPointerException ( "ERROR Cannot insert elements "
                                             + " at offset = " + offset
                                             + " elements = " + elements
                                             + " into "
                                             + this );
        }
        else if ( elements instanceof ArrayElements )
        {
            final ArrayElements<ELEMENT> that =
                (ArrayElements<ELEMENT>) elements;
            final ELEMENT [] array = that.container ();
            // Throws exceptions:
            final ListElements<ELEMENT> inserted_elements =
                this.insert ( offset, array );
            return inserted_elements;
        }

        final List<ELEMENT> original_list = this.container ();
        final long elements_length = elements.length ();

        if ( elements_length == 0L )
        {
            // Error check the offset, then just return this as-is.
            if ( offset != Elements.AFTER_LAST )
            {
                final long index =
                    AbstractElements.clamp ( offset,
                                             (long) original_list.size () );
                if ( index < 0L
                     || index > (long) original_list.size () )
                {
                    throw new IndexOutOfBoundsException ( "ERROR Cannot insert"
                                                          + "elements at"
                                                          + " offset "
                                                          + offset
                                                          + " with "
                                                          + elements
                                                          + " into "
                                                          + this );
                }
            }

            return this;
        }

        if ( (long) Integer.MAX_VALUE - (long) original_list.size ()
             < elements_length )
        {
            // Too big for a List.
            final String this_plus_elements_string =
                "" + this + " + " + elements;
            final long would_be_length =
                (long) original_list.size () + elements_length;
            throw new ElementsLengthException ( this_plus_elements_string,
                                                would_be_length );
        }

        final List<ELEMENT> new_list;
        if ( this.flags ().isVariableLength () )
        {
            new_list = original_list;
        }
        else if ( original_list.size () == 0
                  && elements.flags () == this.flags () )
        {
            new_list = elements.list ();
        }
        else
        {
            new_list =
                new ArrayList<ELEMENT> ( original_list.size ()
                                         + (int) elements_length );
        }

        final List<ELEMENT> add_list = elements.list ();
        if ( offset == Elements.AFTER_LAST
             || offset == (long) original_list.size () )
        {
            if ( new_list != original_list )
            {
                new_list.addAll ( original_list );
            }

            new_list.addAll ( add_list );
        }
        else
        {
            final long index =
                AbstractElements.clamp ( offset,
                                         (long) original_list.size () );
            if ( index < 0L
                 || index >= (long) original_list.size () )
            {
                throw new IndexOutOfBoundsException ( "ERROR Cannot insert"
                                                      + "elements at offset "
                                                      + offset
                                                      + " with "
                                                      + elements
                                                      + " into "
                                                      + this );
            }

            if ( index == 0L )
            {
                new_list.addAll ( 0, add_list );
                if ( new_list != original_list )
                {
                    new_list.addAll ( original_list );
                }
            }
            else
            {
                if ( new_list != original_list )
                {
                    new_list.addAll ( original_list );
                }

                new_list.addAll ( (int) index, add_list );
            }
        }

        final ListElements<ELEMENT> new_elements;
        if ( this.flags ().isVariableLength () )
        {
            new_elements = this;
        }
        else
        {
            new_elements =
                new ListElements<ELEMENT> ( this.elementClass (),
                                            this.flags (),
                                            new_list );
        }

        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#isArrayLength()
     */
    @Override
    public final boolean isArrayLength ()
    {
        // a List's max size is Integer.MAX_VALUE, just like an array.
        // (Even if we'd never actually be able to allocate Intrger.MAX_VALUE
        // elements, at least the caller will know that there are no
        // offsets above (long) Integer.MAX_VALUE.)
        // So yup.
        return true;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#isEmpty()
     */
    @Override
    public final boolean isEmpty ()
    {
        final List<ELEMENT> list = this.container ();
        if ( list.size () == 0 )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#isSingleton()
     */
    @Override
    public final boolean isSingleton ()
    {
        final List<ELEMENT> list = this.container ();
        if ( list.size () == 1 )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public final Iterator<ELEMENT> iterator ()
    {
        final List<ELEMENT> list = this.container ();
        final CollectionIterator<ELEMENT> iterator =
            new CollectionIterator<ELEMENT> ( list );
        return iterator;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#keep(long[])
     */
    @Override
    public final ListElements<ELEMENT> keep (
            long ... offsets
            )
        throws NullPointerException,
               IndexOutOfBoundsException
    {
        if ( offsets == null )
        {
            throw new NullPointerException ( "ERROR Cannot keep elements "
                                             + " at offsets = "
                                             + offsets
                                             + " from "
                                             + this );
        }
        else if ( offsets.length == 0L )
        {
            // Dont keep anything.
            final List<ELEMENT> empty_list =
                new ArrayList<ELEMENT> ( 0 );
            final ArrayElements<ELEMENT> empty_elements =
                new ArrayElements<ELEMENT> ( this.elementClass (),
                                             this.flags (),
                                             empty_list );
            return empty_elements;
        }

        final List<ELEMENT> original_list = this.container ();
        // Don't bother to check whether we're keeping everything
        // and just return this.
        // We would have to convert all the offsets to indices, then check
        // to make sure they're in order.
        if ( offsets.length > original_list.length )
        {
            // More offsets to keep than we have elements!
            final String elements_string =
                "" + this + " * "
                + StringRepresentation.of ( offsets,
                                            StringRepresentation.DEFAULT_ARRAY_LENGTH );
            final long would_be_length = (long) offsets.length;
            throw new ElementsLengthException ( elements_string,
                                                would_be_length );
        }

        !!!;
        // Arrays are never variable length, so we always have to create
        // a new array when removing elements.
        final List<ELEMENT> kept_list =
            new ArrayList<ELEMENT> ( offsets.length );

        long last_index = -1L;
        long copy_to_index = 0L;
        for ( long offset : offsets )
        {
            long index =
                AbstractElements.clamp ( offset,
                                         (long) original_list.size () );

            if ( index < 0L
                 || index >= (long) original_list.size () )
            {
                final String offsets_string =
                    StringRepresentation.of ( offsets,
                                              StringRepresentation.DEFAULT_ARRAY_LENGTH );
                throw new IndexOutOfBoundsException ( "ERROR Cannot keep"
                                                      + " element at offset "
                                                      + offset
                                                      + " when keeping"
                                                      + " elements at"
                                                      + offsets_string
                                                      + " in "
                                                      + this );
            }
            else if ( index <= last_index )
            {
                final String offsets_string =
                    StringRepresentation.of ( offsets,
                                              StringRepresentation.DEFAULT_ARRAY_LENGTH );
                throw new IndexOutOfBoundsException ( "ERROR Cannot keep"
                                                      + " element at offset "
                                                      + offset
                                                      + ": out of order"
                                                      + " ( index "
                                                      + index
                                                      + " <= last index "
                                                      + last_index
                                                      + ") when keeping"
                                                      + " elements at"
                                                      + offsets_string
                                                      + " in "
                                                      + this );
            }

            kept_list.add ( original_list.get ( (int) index ) );

            copy_to_index ++;
            last_index = index;
        }

        final ListElements<ELEMENT> kept_elements =
            new ListElements<ELEMENT> ( this.elementClass (),
                                        this.flags (),
                                        kept_list );
        return kept_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#keepAll(musaico.foundation.domains.elements.Elements)
     */
    @Override
    public final ListElements<ELEMENT> keepAll (
            Elements<Long> offsets
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ConcurrentModificationException
    {
        !!!!!!!!!!!!!!!!!!!;
        if ( offsets == null )
        {
            throw new NullPointerException ( "ERROR Cannot keep elements "
                                             + " at offsets = "
                                             + offsets
                                             + " from "
                                             + this );
        }

        final long offsets_length = offsets.length ();
        if ( offsets_length == 0L )
        {
            // Dont keep anything.
            final List<ELEMENT> empty_list =
                new ArrayList<ELEMENT> ( 0 );
            final ListElements<ELEMENT> empty_elements =
                new ListElements<ELEMENT> ( this.elementClass (),
                                            this.flags (),
                                            empty_list );
            return empty_elements;
        }

        final List<ELEMENT> original_list = this.container ();
        // Don't bother to check whether we're keeping everything
        // and just return this.
        // We would have to convert all the offsets to indices, then check
        // to make sure they're in order.
        if ( offsets_length > (long) original_list.size () )
        {
            // More offsets to keep than we have elements!
            final String elements_string = "" + this + " * " + offsets;
            final long would_be_length = offsets_length;
            throw new ElementsLengthException ( elements_string,
                                                would_be_length );
        }

        final List<ELEMENT> kept_list =
            new ArrayList<ELEMENT> ( (int) offsets_length );
        long last_index = -1L;
        long copy_to_index = 0L;
        for ( long offset : offsets )
        {
            long index =
                AbstractElements.clamp ( offset,
                                         (long) original_list.size () );

            if ( index < 0L
                 || index >= (long) original_list.size () )
            {
                throw new IndexOutOfBoundsException ( "ERROR Cannot keep"
                                                      + " element at offset "
                                                      + offset
                                                      + " when removing"
                                                      + " elements at"
                                                      + offsets
                                                      + " from "
                                                      + this );
            }
            else if ( index <= last_index )
            {
                throw new IndexOutOfBoundsException ( "ERROR Cannot keep"
                                                      + " element at offset "
                                                      + offset
                                                      + ": out of order"
                                                      + " ( index "
                                                      + index
                                                      + " <= last index "
                                                      + last_index
                                                      + ") when removing"
                                                      + " elements at"
                                                      + offsets
                                                      + " from "
                                                      + this );
            }

            kept_list.add ( original_list.get ( (int) index );

            copy_to_index ++;
            last_index = index;
        }

        final ListElements<ELEMENT> kept_elements =
            new ListElements<ELEMENT> ( this.elementClass (),
                                        this.flags (),
                                        kept_list );
        return kept_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#keepRange(long, long)
     */
    @Override
    public final Elements<ELEMENT> keepRange (
            long start,
            long end
            )
        throws IndexOutOfBoundsException
    {
        !!!!!!!!!!!!!!!!!!!;
        final Lis<ELEMENT> original_list = this.container ();
        final long original_length = (long) original_list.size ();

        final long clamped_start =
            AbstractElements.clamp ( start, original_length );
        final long clamped_end =
            AbstractElements.clamp ( end, original_length );
        if ( clamped_start < 0L
             || clamped_start >= original_length
             || clamped_end < 0L
             || clamped_end >= original_length )
        {
            throw new IndexOutOfBoundsException ( "ERROR Cannot keep range "
                                                  + start
                                                  + " .. "
                                                  + end
                                                  + ": out of range ("
                                                  + clamped_start
                                                  + ", "
                                                  + clamped_end
                                                  + ") in "
                                                  + this );
        }

        final long first;
        final long last;
        if ( clamped_start <= clamped_end )
        {
            // Forward order.
            first = clamped_start;
            last = clamped_end;
        }
        else
        {
            // Reverse order.
            first = clamped_end;
            last = clamped_start;
        }

        final Class<ELEMENT> element_class = this.elementClass ();
        final long kept_length = last - first + 1;
        final List<ELEMENT> kept_list =
            new ArrayList<ELEMENT> ( (int) kept_length );

        System.arraycopy ( array, (int) first,
                           kept_array, 0, (int) kept_length );

        if ( clamped_start > clamped_end )
        {
            // Reverse order.
            final int end_index = kept_list.size () - 1;
            final int half_length = kept_list.size () / 2;
            for ( int e = 0; e < half_length; e ++ )
            {
                final ELEMENT swap = kept_list.get ( end_index - e );
                !!! kept_list.set ( end_index - e, kept_array [ e ];
                                    !!! kept_list.set ( e, swap );
            }
        }

        final ArrayElements<ELEMENT> kept_elements =
            new ArrayElements<ELEMENT> ( this.elementClass (),
                                         this.flags (),
                                         kept_list );
        return kept_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#length()
     */
    @Override
    public final long length ()
    {
        final List<ELEMENT> list = this.container ();
        return (long) list.size ();
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#list()
     */
    @Override
    public final List<ELEMENT> list ()
        // Never throws ElementsLengthException.
    {
        if ( this.flags ().isOverwritable () )
        {
            return this.container ();
        }

        final Class<ELEMENT> element_class = this.elementClass ();

        final List<ELEMENT> list = this.container ();
        final List<ELEMENT> new_list = new ArrayList<ELEMENT> ( list );

        return new_list;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#overwrite(long, java.lang.Object[])
     */
    @Override
    @SafeVarargs
    @SuppressWarnings("varargs")
        // Generic varargs heap pollution,
    public final ListElements<ELEMENT> overwrite (
            long offset,
            ELEMENT ... elements
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ElementsLengthException
    {
        if ( elements == null )
        {
            throw new NullPointerException ( "ERROR Cannot overwrite elements "
                                             + " at offset = " + offset
                                             + " elements = " + elements
                                             + " in "
                                             + this );
        }

        final List<ELEMENT> original_list = this.container ();

        final long start =
            AbstractElements.clamp ( offset,
                                     (long) original_list.size () );
        final long end = start + (long) elements.length - 1L;
        if ( start < 0L
             || end < 0L
             || end >= (long) original_list.size () )
        {
            final String elements_string =
                // Varargs heap pollution:
                StringRepresentation.of ( elements,
                                          StringRepresentation.DEFAULT_ARRAY_LENGTH );
            throw new IndexOutOfBoundsException ( "ERROR Cannot overwrite"
                                                  + " elements at offset "
                                                  + offset
                                                  + " with elements "
                                                  + elements_string
                                                  + " in "
                                                  + this );
        }

        if ( elements.length == 0 )
        {
            return this;
        }

        final List<ELEMENT> new_list;
        if ( this.flags ().isOverwritable () )
        {
            new_list = original_list;

            for ( int e = 0; e < elements.length; e ++ )
            {
                new_list.set ( (int) start + e, elements [ e ] );
            }
        }
        else
        {
            new_list = new ArrayList<ELEMENT> ( original_list.size () );

            if ( start > 0L )
            {
                final List<ELEMENT> pre_list =
                    original_list.subList ( 0, (int) start );
                new_list.addAll ( pre_list );
            }

            Collections.addAll ( new_list, elements );

            if ( end < ( (long) original_list.size () - 1L ) )
            {
                final List<ELEMENT> post_list =
                    original_list.subList ( (int) end + 1,
                                            original_list.size () );
                new_list.addAll ( post_list );
            }
        }

        if ( new_list == original_list )
        {
            return this;
        }

        final ListElements<ELEMENT> new_elements =
            new ListElements<ELEMENT> ( this.elementClass (),
                                        this.flags (),
                                        new_list );
        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#overwriteAll(long, musaico.foundation.domains.elements.Elements)
     */
    @Override
    public final ListElements<ELEMENT> overwriteAll (
            long offset,
            Elements<ELEMENT> elements
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ElementsLengthException,
               ConcurrentModificationException
    {
        if ( elements == null )
        {
            throw new NullPointerException ( "ERROR Cannot overwrite elements "
                                             + " at offset = " + offset
                                             + " elements = " + elements
                                             + " in "
                                             + this );
        }
        else if ( elements instanceof ArrayElements )
        {
            final ArrayElements<ELEMENT> that =
                (ArrayElements<ELEMENT>) elements;
            final ELEMENT [] array = that.container ();
            // Throws exceptions:
            final ListElements<ELEMENT> overwritten_elements =
                this.overwrite ( offset, array );
            return overwritten_elements;
        }

        final List<ELEMENT> original_list = this.container ();

        final long elements_length = elements.length ();
        final long start =
            AbstractElements.clamp ( offset,
                                     (long) original_list.size () );
        final long end = start + elements_length - 1L;
        if ( start < 0L
             || end < 0L
             || end >= (long) original_list.size () )
        {
            throw new IndexOutOfBoundsException ( "ERROR Cannot overwrite"
                                                  + " elements at offset "
                                                  + offset
                                                  + " with elements "
                                                  + elements
                                                  + " in "
                                                  + this );
        }

        if ( elements_length == 0L )
        {
            return this;
        }

        final List<ELEMENT> new_list;
        if ( this.flags ().isOverwritable () )
        {
            new_list = original_list;
        }
        else
        {
            new_list = new ArrayList<ELEMENT> ( original_list.size () );

            if ( start > 0L )
            {
                final List<ELEMENT> pre_list =
                    original_list.subList ( 0, (int) start );
                new_list.addAll ( pre_list );
            }

            if ( end < ( (long) original_list.size () - 1L ) )
            {
                final List<ELEMENT> post_list =
                    original_list.subList ( (int) end + 1,
                                            original_list.size () );
                new_list.addAll ( post_list );
            }
        }

        int cti = (int) start;
        for ( ELEMENT element : elements )
        {
            if ( (long) cti > end )
            {
                throw new ConcurrentModificationException ( "ERROR the length"
                                                            +" of "
                                                            + elements
                                                            + " changed while"
                                                            + " overwriting"
                                                            + " with them"
                                                            + " at offset "
                                                            + offset
                                                            + " into "
                                                            + this );
            }

            new_list.set ( cti, element );

            cti ++;
        }

        if ( (long) cti <= end )
        {
            throw new ConcurrentModificationException ( "ERROR the length "
                                                        +" of "
                                                        + elements
                                                        + " changed while"
                                                        + " overwriting them"
                                                        + " at offset "
                                                        + offset
                                                        + " into "
                                                        + this );
        }

        if ( new_list == original_list )
        {
            return this;
        }

        final ListElements<ELEMENT> new_elements =
            new ListElements<ELEMENT> ( this.elementClass (),
                                        this.flags (),
                                        new_list );
        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#remove(long[])
     */
    @Override
    public final ListElements<ELEMENT> remove (
            long ... offsets
            )
        throws NullPointerException,
               IndexOutOfBoundsException
    {
        if ( offsets == null )
        {
            throw new NullPointerException ( "ERROR Cannot remove elements "
                                             + " at offsets = "
                                             + offsets
                                             + " from "
                                             + this );
        }
        else if ( offsets.length == 0 )
        {
            // Nothing to remove.
            return this;
        }

        final List<ELEMENT> original_list = this.container ();
        final long original_list_size = original_list.size ();
        if ( offsets.length > (int) original_list_size )
        {
            // More offsets to remove than we have elements!
            final String elements_string =
                "" + this + " / "
                + StringRepresentation.of ( offsets,
                                            StringRepresentation.DEFAULT_ARRAY_LENGTH );
            final long would_be_length =
                original_list_size - (long) offsets.length;
            throw new ElementsLengthException ( elements_string,
                                                would_be_length );
        }

        final List<ELEMENT> new_list;
        final ElementFlags flags = this.flags ();
        if ( flags.isVariableLength () )
        {
            new_list = original_list;
        }
        else
        {
            new_list = new ArrayList<ELEMENT> ( (int) original_list_size
                                                - offsets.length );
        }

        long last_index = -1L;
        long num_removed = 0L;
        for ( long offset : offsets )
        {
            long index =
                AbstractElements.clamp ( offset, original_list_size );

            if ( index < 0L
                 || index >= original_list_size )
            {
                final String offsets_string =
                    StringRepresentation.of ( offsets,
                                              StringRepresentation.DEFAULT_ARRAY_LENGTH );
                throw new IndexOutOfBoundsException ( "ERROR Cannot remove"
                                                      + " element at offset "
                                                      + offset
                                                      + " when removing"
                                                      + " elements at"
                                                      + offsets_string
                                                      + " from "
                                                      + this );
            }
            else if ( index <= last_index )
            {
                final String offsets_string =
                    StringRepresentation.of ( offsets,
                                              StringRepresentation.DEFAULT_ARRAY_LENGTH );
                throw new IndexOutOfBoundsException ( "ERROR Cannot remove"
                                                      + " element at offset "
                                                      + offset
                                                      + ": out of order"
                                                      + " ( index "
                                                      + index
                                                      + " <= last index "
                                                      + last_index
                                                      + ") when removing"
                                                      + " elements at"
                                                      + offsets_string
                                                      + " from "
                                                      + this );
            }

            if ( new_list == original_list )
            {
                new_list.remove ( (int) index - (int) num_removed );
            }
            else
            {
                if ( index > ( last_index + 1L ) )
                {
                    final long from = last_index + 1L;
                    final long to = index - 1L;
                    final List<ELEMENT> part_list =
                        original_list.subList ( (int) from,
                                                (int) to + 1 );
                    new_list.addAll ( part_list );
                }
            }

            num_removed ++;
            last_index = index;
        }

        if ( new_list == original_list )
        {
            return this;
        }

        final ListElements<ELEMENT> new_elements =
            new ListElements<ELEMENT> ( this.elementClass (),
                                        this.flags (),
                                        new_list );
        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#removeAll(musaico.foundation.domains.elements.Elements)
     */
    @Override
    public final ListElements<ELEMENT> removeAll (
            Elements<Long> offsets
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ConcurrentModificationException
    {
        if ( offsets == null )
        {
            throw new NullPointerException ( "ERROR Cannot remove elements "
                                             + " at offsets = "
                                             + offsets
                                             + " from "
                                             + this );
        }

        final long offsets_length = offsets.length ();
        if ( offsets_length == 0L )
        {
            // Nothing to remove.
            return this;
        }

        final List<ELEMENT> original_list = this.container ();
        final long original_list_size = original_list.size ();
        if ( offsets_length > original_list_size )
        {
            // More offsets to remove than we have elements!
            final String elements_string = "" + this + " / " + offsets;
            final long would_be_length = original_list_size - offsets_length;
            throw new ElementsLengthException ( elements_string,
                                                would_be_length );
        }

        final List<ELEMENT> new_list;
        final ElementFlags flags = this.flags ();
        if ( flags.isOverwritable ()
             && flags.isVariableLength () )
        {
            new_list = original_list;
        }
        else
        {
            new_list = new ArrayList<ELEMENT> ( (int) original_list_size
                                                - (int) offsets_length );
        }

        long last_index = -1L;
        long num_removed = 0L;
        for ( long offset : offsets )
        {
            long index =
                AbstractElements.clamp ( offset, original_list_size );

            if ( index < 0L
                 || index >= original_list_size )
            {
                final String offsets_string =
                    StringRepresentation.of ( offsets,
                                              StringRepresentation.DEFAULT_ARRAY_LENGTH );
                throw new IndexOutOfBoundsException ( "ERROR Cannot remove"
                                                      + " element at offset "
                                                      + offset
                                                      + " when removing"
                                                      + " elements at"
                                                      + offsets_string
                                                      + " from "
                                                      + this );
            }
            else if ( index <= last_index )
            {
                final String offsets_string =
                    StringRepresentation.of ( offsets,
                                              StringRepresentation.DEFAULT_ARRAY_LENGTH );
                throw new IndexOutOfBoundsException ( "ERROR Cannot remove"
                                                      + " element at offset "
                                                      + offset
                                                      + ": out of order"
                                                      + " ( index "
                                                      + index
                                                      + " <= last index "
                                                      + last_index
                                                      + ") when removing"
                                                      + " elements at"
                                                      + offsets_string
                                                      + " from "
                                                      + this );
            }

            if ( new_list == original_list )
            {
                original_list.remove ( (int) index - (int) num_removed );
            }
            else
            {
                if ( index > ( last_index + 1L ) )
                {
                    final long from = last_index + 1L;
                    final long to = index - 1L;
                    final List<ELEMENT> part_list =
                        original_list.subList ( (int) from,
                                                (int) to + 1 );
                    new_list.addAll ( part_list );
                }
            }

            num_removed ++;
            last_index = index;
        }

        if ( new_list == original_list )
        {
            return this;
        }

        final ListElements<ELEMENT> new_elements =
            new ListElements<ELEMENT> ( this.elementClass (),
                                        this.flags (),
                                        new_list );
        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#removeRange(long, long)
     */
    @Override
    @SuppressWarnings("unchecked") // Array.newInstance () -> ELEMENT [].  Ook.
    public final Elements<ELEMENT> removeRange (
            long start,
            long end
            )
        throws IndexOutOfBoundsException
    {
        !!!!!!!!!!!!!!!!!!!;
        final ELEMENT [] array = this.container ();
        final long length = (long) array.length;

        final long clamped_start = AbstractElements.clamp ( start, length );
        final long clamped_end = AbstractElements.clamp ( end, length );
        if ( clamped_start < 0L
             || clamped_start >= length
             || clamped_end < 0L
             || clamped_end >= length )
        {
            throw new IndexOutOfBoundsException ( "ERROR Cannot remove range "
                                                  + start
                                                  + " .. "
                                                  + end
                                                  + ": out of range ("
                                                  + clamped_start
                                                  + ", "
                                                  + clamped_end
                                                  + ") in "
                                                  + this );
        }

        final long first;
        final long last;
        if ( clamped_start <= clamped_end )
        {
            // Forward order.
            first = clamped_start;
            last = clamped_end;
        }
        else
        {
            // Reverse order.
            first = clamped_end;
            last = clamped_start;
        }

        final Class<ELEMENT> element_class = this.elementClass ();
        final long length_to_remove = last - first + 1;
        final long removed_length = length - length_to_remove;
        final ELEMENT [] removed_array = (ELEMENT [])
            Array.newInstance ( element_class, (int) removed_length );

        if ( first > 0L )
        {
            System.arraycopy ( array, 0,
                               removed_array, 0, (int) first );
        }

        if ( last < ( length - 1L ) )
        {
            int post_length = (int) ( length - last );
            System.arraycopy ( array, (int) last + 1,
                               removed_array, (int) first, post_length );
        }

        if ( clamped_start > clamped_end )
        {
            // Reverse order.
            final int end_index = removed_array.length - 1;
            final int half_length = removed_array.length / 2;
            for ( int e = 0; e < half_length; e ++ )
            {
                final ELEMENT swap = removed_array [ end_index - e ];
                removed_array [ end_index - e ] = removed_array [ e ];
                removed_array [ e ] = swap;
            }
        }

        final ArrayElements<ELEMENT> removed_elements =
            new ArrayElements<ELEMENT> ( this.elementClass (),
                                         this.flags (),
                                         removed_array );
        return removed_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#set()
     */
    @Override
    public final LinkedHashSet<ELEMENT> set ()
        // Never throws ElementsLengthException.
    {
        final List<ELEMENT> list = this.container ();
        final LinkedHashSet<ELEMENT> set =
            new LinkedHashSet<ELEMENT> ( list );

        return set;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#sort(java.util.Comparator)
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
        // raw type constructing Comparator[],
        // cast Comparator[] --> Comparator<ELEMENT>[].
    public final ListElements<ELEMENT> sort (
            Comparator<ELEMENT> comparator
            )
        throws NullPointerException,
               ElementsLengthException,
               IllegalStateException
    {
        final Elements<Comparator<ELEMENT>> original_orders = this.order ();

        // Don't bother sorting again if we're already in the right order.
        final Comparator<ELEMENT> [] last_order =
            original_orders.at ( Elements.LAST );
        if ( last_order.length == 1
             && last_order [ 0 ].equals ( comparator ) )
        {
            // Already sorted in that order.
            return this;
        }

        final ElementFlags flags = this.flags ();
        if ( flags.isOverwritable ()
             && ! original_orders.flags ().isVariableLength () )
        {
            throw new IllegalStateException ( "ERROR Corruption during sort ()"
                                              + ": overwritable Elements"
                                              + " must have variable-length"
                                              + " orders, but orders have"
                                              + " flags "
                                              + original_orders.flags ()
                                              + " for "
                                              + this );
        }
        else if ( ! flags.isOverwritable ()
                  && original_orders.flags ().isVariableLength () )
        {
            throw new IllegalStateException ( "ERROR Corruption during sort ()"
                                              + ": non-overwritable Elements"
                                              + " must have fixed-length"
                                              + " orders, but orders have"
                                              + " flags "
                                              + original_orders.flags ()
                                              + " for "
                                              + this );
        }

        // Throws NullPointerException, ElementsLengthException:
        final Elements<Comparator<ELEMENT>> new_orders =
            original_orders.insert ( Elements.AFTER_LAST,
                                     comparator );

        final List<ELEMENT> list = this.container ();

        if ( flags.isOverwritable () )
        {
            // Mutable.
            // Sort in place.
            Collections.sort ( list, comparator );
            return this;
        }

        final Class<ELEMENT> element_class = this.elementClass ();
        final List<ELEMENT> sorted_list =
            new ArrayList<ELEMENT> ( list );

        Collections.sort ( sorted_list, comparator );

        final ListElements<ELEMENT> sorted_elements =
            new ListElements<ELEMENT> ( element_class,
                                        this.flags (),
                                        sorted_list,
                                        new_orders );

        return sorted_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#supportedFlags(musaico.foundation.domains.elements.ElementFlags)
     */
    @Override
    public ElementFlags supportedFlags (
            ElementFlags flags
            )
        throws NullPointerException
    {
        if ( flags == null )
        {
            throw new NullPointerException ( "ERROR Cannot determine the"
                                             + " supported flags from input"
                                             + " flags " + flags
                                             + " for "
                                             + this );
        }

        return flags;
    }
}
