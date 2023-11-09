package musaico.foundation.domains.elements;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;


import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.StringRepresentation;

import musaico.foundation.domains.iterators.ArrayIterator;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * The Elements of a 1-dimensional array.
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
public class ArrayElements<ELEMENT extends Object>
    extends AbstractElements<ELEMENT [], ELEMENT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new sequence of immutable, fixed-length empty ArrayElements.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @throws NullPointerException If the specified element_class is null.
     */
    @SuppressWarnings("unchecked") // If Array.newInstance() does not return
        // ELEMENT [], then I am a monkey's uncle.
        // Ook.
    public ArrayElements (
            Class<ELEMENT> element_class
            )
        throws NullPointerException
    {
        // Throws ElementsFlagsException (but shouldn't,
        // since we've been careful to not use variable length flag):
        this ( element_class,             // element_class
               ElementFlags.OVERWRITABLE, // flags
               (ELEMENT []) Array.newInstance ( element_class, 0 ) ); // array
    }


    /**
     * <p>
     * Creates a new sequence of empty ArrayElements.
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
     *                              and/or flags is/are null.
     */
    @SuppressWarnings("unchecked") // If Array.newInstance() does not return
        // ELEMENT [], then I am a monkey's uncle.
        // Ook.
    public ArrayElements (
            Class<ELEMENT> element_class,
            ElementFlags flags
            )
        throws NullPointerException
    {
        // Throws ElementsFlagsException (but shouldn't,
        // since we've been careful to not use variable length flag):
        this ( element_class, // element_class
               flags,         // flags
               (ELEMENT []) Array.newInstance ( element_class, 0 ) ); // array
    }


    /**
     * <p>
     * Creates a new sequence of mutable, fixed-length,
     * unordered ArrayElements.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param array The array container to be represented by these
     *              ArrayElements.  Can contain null elements.
     *              Element values can be modified by the caller,
     *              though the caller must ensure concurrency protection
     *              is in place around these Elements.  Must not be null.
     *
     * @throws NullPointerException If the specified element_class
     *                              and/or array container is/are null.
     */
    @SafeVarargs
    public ArrayElements (
            Class<ELEMENT> element_class,
            ELEMENT ... array
            )
        throws NullPointerException
    {
        // Throws ElementsFlagsException (but shouldn't,
        // since we've been careful to not use variable length flag):
        this ( element_class, // element_class
               ElementFlags.OVERWRITABLE, // flags
               array );       // array
    }


    /**
     * <p>
     * Creates a new sequence of unordered ArrayElements.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param array The array container to be represented by these
     *              ArrayElements.  Can contain null elements.
     *              Element values can be modified by the caller
     *              only if flags.isImmutable () is false, though in that case
     *              the caller must ensure concurrency protection
     *              is in place around these Elements.  Must not be null.
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
     *                              and/or flags and/or array container
     *                              is/are null.
     *
     * @throws ElementsFlagsException If the specified flags
     *                                are incompatible with
     *                                this type of Elements.
     *                                For example, an array container
     *                                cannot be
     *                                <code> ElementFlags.VARIABLE_LENGTH </code>.
     */
    @SafeVarargs
    @SuppressWarnings({"varargs", "unchecked", "rawtypes"}) // Generic varargs
        // heap pollution (varargs),
        // unchecked cast Comparator[] --> Comparator<ELEMENT>[],
        // raw type constructing Comparator[].
    public ArrayElements (
            Class<ELEMENT> element_class,
            ElementFlags flags,
            ELEMENT ... array
            )
        throws NullPointerException,
               ElementsFlagsException
    {
        this ( element_class,                   // element_class
               flags,                           // flags
               array,                           // array
               (Comparator<ELEMENT> []) new Comparator [ 0 ] ); // orders
    }


    /**
     * <p>
     * Creates a new sequence of ArrayElements.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param flags The ElementFlags, specifying whether these
     *              Elements are mutable or immutable, fixed length
     *              or variable length, and so on.  Must not be null.
     *
     * @param array The array container to be represented by these
     *              ArrayElements.  Can contain null elements.
     *              Element values can be modified by the caller
     *              only if flags.isImmutable () is false, though in that case
     *              the caller must ensure concurrency protection
     *              is in place around these Elements.  Must not be null.
     *
     * @param orders 0 or more Comparators by which these Elements have
     *               already been sorted, in order from # 0 to # last.
     *               Can be empty.  Can be null, which is treated as empty.
     *               Must not contain any null elements.
     *
     * @throws NullPointerException If the specified element_class
     *                              and/or flags and/or array container
     *                              and/or any element of the specified
     *                              orders is/are null.
     *
     * @throws ElementsFlagsException If the specified flags
     *                                are incompatible with
     *                                this type of Elements.
     *                                For example, an array container
     *                                cannot be
     *                                <code> ElementFlags.VARIABLE_LENGTH </code>.
     */
    @SafeVarargs
    @SuppressWarnings("varargs") // Generic varargs heap pollution.
    public ArrayElements (
            Class<ELEMENT> element_class,
            ElementFlags flags,
            ELEMENT [] array,
            Comparator<ELEMENT> ... orders
            )
        throws NullPointerException,
               ElementsFlagsException
    {
        super ( element_class,     // element_class
                flags,             // flags
                array,             // container
                orders );          // orders

        if ( ! this.supportedFlags ( flags ).equals ( flags ) )
        {
            // ArrayElements can't support variable length Elements.
            throw new ElementsFlagsException ( this, flags );
        }
    }


    /**
     * <p>
     * Creates a new sequence of ArrayElements.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param flags The ElementFlags, specifying whether these
     *              Elements are mutable or immutable, fixed length
     *              or variable length, and so on.  Must not be null.
     *
     * @param array The array container to be represented by these
     *              ArrayElements.  Can contain null elements.
     *              Element values can be modified by the caller
     *              only if flags.isImmutable () is false, though in that case
     *              the caller must ensure concurrency protection
     *              is in place around these Elements.  Must not be null.
     *
     * @param orders 0 or more Comparators by which these Elements have
     *               already been sorted, in order from # 0 to # last.
     *               Can be empty.  Can be null, which is treated as empty.
     *               Must not contain any null elements.
     *
     * @throws NullPointerException If the specified element_class
     *                              and/or flags and/or array container
     *                              and/or any element of the specified
     *                              orders is/are null.
     *
     * @throws ElementsFlagsException If the specified flags
     *                                are incompatible with
     *                                this type of Elements.
     *                                For example, an array container
     *                                cannot be
     *                                <code> ElementFlags.VARIABLE_LENGTH </code>.
     */
    public ArrayElements (
            Class<ELEMENT> element_class,
            ElementFlags flags,
            ELEMENT [] array,
            Elements<Comparator<ELEMENT>> orders
            )
        throws NullPointerException,
               ElementsFlagsException
    {
        super ( element_class,     // element_class
                flags,             // flags
                array,             // container
                orders );          // orders

        if ( ! this.supportedFlags ( flags ).equals ( flags ) )
        {
            // ArrayElements can't support variable length Elements.
            throw new ElementsFlagsException ( this, flags );
        }
    }


    /**
     * <p>
     * Creates a new sequence of ArrayElements.
     * </p>
     *
     * @param flags The ElementFlags, specifying whether these
     *              Elements are mutable or immutable, fixed length
     *              or variable length, and so on.  Must not be null.
     *
     * @param elements The Elements to include in these new ArrayElements.
     *                 Must not be null.
     *
     * @throws NullPointerException If the specified flags or elements
     *                              is/are null.
     *
     * @throws ElementsLengthException If elements contains too many elements
     *                                 to store in Elements of this
     *                                 container type.  For example,
     *                                 array containers cannot have
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
    public ArrayElements (
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
    @SuppressWarnings("unchecked") // If Array.newInstance() does not return
        // ELEMENT [], then I am a monkey's uncle.
        // Ook.
    protected ELEMENT [] extractContentsFrom (
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

        final ELEMENT [] old_array;
        if ( elements instanceof ArrayElements )
        {
            final ArrayElements<ELEMENT> array_elements =
                (ArrayElements<ELEMENT>) elements;
            old_array = array_elements.container ();

            final ElementFlags flags = elements.flags ();
            if ( ! flags.isOverwritable ()
                 && ! flags.isVariableLength () )
            {
                return old_array;
            }
        }
        else
        {
            old_array = null;
        }

        final ELEMENT [] new_array;
        try
        {
            new_array = (ELEMENT []) Array.newInstance ( element_class,
                                                         (int) length );
        }
        catch ( Throwable t )
        {
            throw new ElementsLengthException ( this,
                                                length,
                                                t );
        }

        if ( old_array != null )
        {
            System.arraycopy ( old_array, 0,
                               new_array, 0, old_array.length );
        }
        else
        {
            int e = 0;
            for ( ELEMENT element : old_array )
            {
                if ( e >= new_array.length )
                {
                    throw new ConcurrentModificationException (
                        "ERROR the length of "
                        + StringRepresentation.of (
                              old_array,
                              StringRepresentation.DEFAULT_ARRAY_LENGTH )
                        + " changed while creating a new "
                        + ClassName.of ( this.getClass () )
                        + " from them using "
                        + this );
                }

                new_array [ e ] = element;
                e ++;
            }

            if ( e < ( new_array.length - 1 ) )
            {
                throw new ConcurrentModificationException (
                    "ERROR the length of "
                    + StringRepresentation.of (
                          old_array,
                          StringRepresentation.DEFAULT_ARRAY_LENGTH )
                    + " changed while creating a new "
                    + ClassName.of ( this.getClass () )
                    + " from them using "
                    + this );
            }
        }

        return new_array;
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
        if ( this.flags ().isOverwritable () )
        {
            return this.container ();
        }

        final Class<ELEMENT> element_class = this.elementClass ();

        final ELEMENT [] array = this.container ();
        final ELEMENT [] new_array = (ELEMENT [])
            Array.newInstance ( element_class, array.length );

        System.arraycopy ( array, 0,
                           new_array, 0, array.length );

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

        final ELEMENT [] array = this.container ();
        singleton [ 0 ] = array [ (int) index ];

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
        final ELEMENT [] array = this.container ();
        final String contents_string =
            StringRepresentation.of (
                array,
                StringRepresentation.DEFAULT_ARRAY_LENGTH );
        return contents_string;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#duplicate(musaico.foundation.domains.elements.ElementFlags)
     */
    @Override
    @SuppressWarnings("unchecked") // If Array.newInstance() does not return
        // ELEMENT [], then I am a monkey's uncle.
        // Ook.
    public final ArrayElements<ELEMENT> duplicate (
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
        else if ( ! this.supportedFlags ( flags ).equals ( flags ) )
        {
            // ArrayElements can't support variable length Elements.
            throw new ElementsFlagsException ( this, flags );
        }

        final Class<ELEMENT> element_class = this.elementClass ();

        final ELEMENT [] array = this.container ();
        final ELEMENT [] new_array = (ELEMENT [])
            Array.newInstance ( element_class, array.length );

        System.arraycopy ( array, 0,
                           new_array, 0, array.length );

        final ArrayElements<ELEMENT> new_elements =
            new ArrayElements<ELEMENT> ( element_class,
                                         flags,
                                         new_array );

        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.AbstractElements#equalsElements(java.lang.Object, musaico.foundation.domains.elements.AbstractElements, java.lang.Object)
     */
    @Override
    protected final boolean equalsElements (
            ELEMENT [] this_container,
            AbstractElements<ELEMENT [], ELEMENT> that_elements,
            ELEMENT [] that_container
            )
    {
        if ( this_container == null
             || that_container == null )
        {
            // Corrupt.
            return false;
        }

        if ( this_container.length != that_container.length )
        {
            return false;
        }

        for ( int e = 0; e < this_container.length; e ++ )
        {
            final ELEMENT this_element = this_container [ e ];
            final ELEMENT that_element = that_container [ e ];

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
    @SuppressWarnings("unchecked") // Array.newInstance ()
    public final ArrayElements<ELEMENT> filter (
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

        final ELEMENT [] array = this.container ();
        final List<ELEMENT> filtered_list = new ArrayList<ELEMENT> ();
        boolean is_any_filtered_out = false;
        for ( int e = 0; e < array.length; e ++ )
        {
            final ELEMENT element = array [ e ];
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

        // We always create a new array, because arrays are
        // fixed length, so no way of filtering in-place.
        final Class<ELEMENT> element_class = this.elementClass ();
        final ELEMENT [] template = (ELEMENT [])
            Array.newInstance ( element_class, filtered_list.size () );
        final ELEMENT [] filtered_array = filtered_list.toArray ( template );

        final ArrayElements<ELEMENT> filtered_elements =
            new ArrayElements<ELEMENT> ( element_class,
                                         this.flags (),
                                         filtered_array );

        return filtered_elements;
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
        final ELEMENT [] array = this.container ();
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
            for ( int a = 0; a < array.length; a ++ )
            {
                final ELEMENT compare_to_element = array [ a ];
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
            }
        }
        else
        {
            final Comparator<ELEMENT> order =
                orders.at ( Elements.LAST ) [ 0 ];

            for ( int a = Arrays.binarySearch ( array, element, order );
                  a >= 0 && a < array.length;
                  a = Arrays.binarySearch ( array,
                                            a + 1, array.length,
                                            element, order ) )
            {
                found.add ( Long.valueOf ( (long) a ) );
            }
        }

        final Long [] template = new Long [ found.size () ];
        final Long [] found_array = found.toArray ( template );
        final Elements<Long> found_offsets =
            new ArrayElements<Long> ( Long.class,
                                      ElementFlags.IMMUTABLE,
                                      found_array );

        return found_offsets;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#from(java.lang.Class, musaico.foundation.domains.elements.ElementFlags, Object[])
     */
    @Override
    @SafeVarargs
    @SuppressWarnings("varargs") // Pass THAT_ELEMENT [] to constructor.
    public final <THAT_ELEMENT extends Object>
        ArrayElements<THAT_ELEMENT> from (
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
                                             + array_string );
        }
        else if ( ! this.supportedFlags ( flags ).equals ( flags ) )
        {
            throw new ElementsFlagsException ( this, flags );
        }

        final ArrayElements<THAT_ELEMENT> new_elements =
            new ArrayElements<THAT_ELEMENT> ( element_class,
                                              flags,
                                              // Varargs heap pollution:
                                              array );

        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#from(musaico.foundation.domains.elements.ElementFlags, musaico.foundation.domains.elements.Elements)
     */
    @Override
    @SuppressWarnings("unchecked") // If Array.newInstance() does not return
        // ELEMENT [], then I am a monkey's uncle.
        // Ook.
    public final <THAT_ELEMENT extends Object>
        ArrayElements<THAT_ELEMENT> from (
            ElementFlags flags,
            Elements<THAT_ELEMENT> that
            )
        throws NullPointerException,
               ElementsLengthException,
               ElementsFlagsException,
               ConcurrentModificationException
    {
        // Throws all the exceptions:
        final ArrayElements<THAT_ELEMENT> new_elements =
            new ArrayElements<THAT_ELEMENT> ( flags,
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
        final ELEMENT [] array = this.container ();
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
            for ( int a = 0; a < array.length; a ++ )
            {
                final ELEMENT compare_to_element = array [ a ];
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

            final int a = Arrays.binarySearch ( array, element, order );
            if ( a >= 0
                 && a < array.length )
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
        final ELEMENT [] array = this.container ();
        int hash_code = 0;
        for ( int e = 0; e < 4 && e < array.length; e ++ )
        {
            final ELEMENT element = array [ e ];
            if ( element != null )
            {
                hash_code += element.hashCode ();
            }
        }

        int start = array.length - 4;
        if ( start < 4 )
        {
            start = 4;
        }

        for ( int e = start; e < array.length; e ++ )
        {
            final ELEMENT element = array [ e ];
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
    @SuppressWarnings({"unchecked", "varargs"})
        // Array.newInstance () cast to ELEMENT [].  Ook.
        // Generic varargs heap pollution,
    public final ArrayElements<ELEMENT> insert (
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
                final ELEMENT [] original_array = this.container ();
                final long index =
                    AbstractElements.clamp ( offset,
                                             (long) original_array.length );
                if ( index < 0L
                     || index > (long) original_array.length )
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

        final ELEMENT [] original_array = this.container ();
        if ( Integer.MAX_VALUE - original_array.length < elements.length )
        {
            // Too big for an array.
            final String elements_string =
                "" + this + " + "
                + StringRepresentation.of ( elements,
                                            StringRepresentation.DEFAULT_ARRAY_LENGTH );
            final long would_be_length =
                (long) original_array.length + (long) elements.length;
            throw new ElementsLengthException ( elements_string,
                                                would_be_length );
        }

        final ELEMENT [] new_array = (ELEMENT [])
            Array.newInstance ( this.elementClass (),
                                original_array.length + elements.length );
        if ( offset == Elements.AFTER_LAST
             || offset == (long) original_array.length )
        {
            System.arraycopy ( original_array, 0,
                               new_array, 0,
                               original_array.length );
            System.arraycopy ( elements, 0,
                               new_array, original_array.length,
                               elements.length );
        }
        else
        {
            final long index =
                AbstractElements.clamp ( offset,
                                         (long) original_array.length );
            if ( index < 0L
                 || index >= (long) original_array.length )
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
                System.arraycopy ( elements, 0,
                                   new_array, 0,
                                   elements.length );
                System.arraycopy ( original_array, 0,
                                   new_array, elements.length,
                                   original_array.length );
            }
            else
            {
                System.arraycopy ( original_array, 0,
                                   new_array, 0,
                                   (int) index );
                System.arraycopy ( elements, 0,
                                   new_array, (int) index,
                                   elements.length );
                System.arraycopy ( original_array,
                                   (int) index,
                                   new_array,
                                   (int) index + elements.length,
                                   original_array.length - (int) index );
            }
        }

        final ArrayElements<ELEMENT> new_elements =
            new ArrayElements<ELEMENT> ( this.elementClass (),
                                         this.flags (),
                                         new_array );
        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#insertAll(long, musaico.foundation.domains.elements.Elements)
     */
    @Override
    @SuppressWarnings("unchecked") // Array.newInstance () -> ELEMENT [].  Ook.
    public final ArrayElements<ELEMENT> insertAll (
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
            final ArrayElements<ELEMENT> inserted_elements =
                this.insert ( offset, array );
            return inserted_elements;
        }

        final ELEMENT [] original_array = this.container ();
        final long elements_length = elements.length ();

        if ( elements_length == 0L )
        {
            // Error check the offset, then just return this as-is.
            if ( offset != Elements.AFTER_LAST )
            {
                final long index =
                    AbstractElements.clamp ( offset,
                                             (long) original_array.length );
                if ( index < 0L
                     || index > (long) original_array.length )
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

        if ( (long) Integer.MAX_VALUE - (long) original_array.length
             < elements_length )
        {
            // Too big for an array.
            final String this_plus_elements_string =
                "" + this + " + " + elements;
            final long would_be_length =
                (long) original_array.length + elements_length;
            throw new ElementsLengthException ( this_plus_elements_string,
                                                would_be_length );
        }

        final ELEMENT [] new_array = (ELEMENT [])
            Array.newInstance ( this.elementClass (),
                                original_array.length
                                + (int) elements_length );
        final int copy_to_index;
        if ( offset == Elements.AFTER_LAST
             || offset == (long) original_array.length )
        {
            System.arraycopy ( original_array, 0,
                               new_array, 0,
                               original_array.length );
            copy_to_index = original_array.length;
        }
        else
        {
            final long index =
                AbstractElements.clamp ( offset,
                                         (long) original_array.length );
            if ( index < 0L
                 || index >= (long) original_array.length )
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
                copy_to_index = 0;
                System.arraycopy ( original_array, 0,
                                   new_array, (int) elements_length,
                                   original_array.length );
            }
            else
            {
                System.arraycopy ( original_array, 0,
                                   new_array, 0,
                                   (int) index );
                copy_to_index = (int) index;
                System.arraycopy ( original_array, (int) index,
                                   new_array,
                                   (int) index + (int) elements_length,
                                   original_array.length - (int) index );

            }
        }

        final int copy_to_end = copy_to_index + (int) elements_length - 1;
        int cti = copy_to_index;
        for ( ELEMENT element : elements )
        {
            if ( cti > copy_to_end )
            {
                throw new ConcurrentModificationException ( "ERROR the length"
                                                            +" of "
                                                            + elements
                                                            + " changed while"
                                                            + " inserting them"
                                                            + " at offset "
                                                            + offset
                                                            + " into "
                                                            + this );
            }

            new_array [ cti ] = element;

            cti ++;
        }

        if ( cti <= copy_to_end )
        {
            throw new ConcurrentModificationException ( "ERROR the length "
                                                        +" of "
                                                        + elements
                                                        + " changed while"
                                                        + " inserting them"
                                                        + " at offset "
                                                        + offset
                                                        + " into "
                                                        + this );
        }

        final ArrayElements<ELEMENT> new_elements =
            new ArrayElements<ELEMENT> ( this.elementClass (),
                                         this.flags (),
                                         new_array );
        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#isArrayLength()
     */
    @Override
    public final boolean isArrayLength ()
    {
        // Well duh.
        return true;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#isEmpty()
     */
    @Override
    public final boolean isEmpty ()
    {
        final ELEMENT [] array = this.container ();
        if ( array.length == 0 )
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
        final ELEMENT [] array = this.container ();
        if ( array.length == 1 )
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
        final ELEMENT [] array = this.container ();
        final ArrayIterator<ELEMENT> iterator =
            new ArrayIterator<ELEMENT> ( array );
        return iterator;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#keep(long[])
     */
    @Override
    @SuppressWarnings("unchecked") // Array.newInstance () -> ELEMENT [].  Ook.
    public final ArrayElements<ELEMENT> keep (
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
            final ELEMENT [] empty_array = (ELEMENT [])
                Array.newInstance ( this.elementClass (),
                                    0 );
            final ArrayElements<ELEMENT> empty_elements =
                new ArrayElements<ELEMENT> ( this.elementClass (),
                                             this.flags (),
                                             empty_array );
            return empty_elements;
        }

        final ELEMENT [] original_array = this.container ();
        // Don't bother to check whether we're keeping everything
        // and just return this.
        // We would have to convert all the offsets to indices, then check
        // to make sure they're in order.
        if ( offsets.length > original_array.length )
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

        // Arrays are never variable length, so we always have to create
        // a new array when removing elements.
        final ELEMENT [] kept_array = (ELEMENT [])
            Array.newInstance ( this.elementClass (),
                                offsets.length );

        long last_index = -1L;
        long copy_to_index = 0L;
        for ( long offset : offsets )
        {
            long index =
                AbstractElements.clamp ( offset,
                                         (long) original_array.length );

            if ( index < 0L
                 || index >= (long) original_array.length )
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

            kept_array [ (int) copy_to_index ] =
                original_array [ (int) index ];

            copy_to_index ++;
            last_index = index;
        }

        final ArrayElements<ELEMENT> kept_elements =
            new ArrayElements<ELEMENT> ( this.elementClass (),
                                         this.flags (),
                                         kept_array );
        return kept_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#keepAll(musaico.foundation.domains.elements.Elements)
     */
    @Override
    @SuppressWarnings("unchecked") // Array.newInstance () -> ELEMENT [].  Ook.
    public final ArrayElements<ELEMENT> keepAll (
            Elements<Long> offsets
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ConcurrentModificationException
    {
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
            final ELEMENT [] empty_array = (ELEMENT [])
                Array.newInstance ( this.elementClass (),
                                    0 );
            final ArrayElements<ELEMENT> empty_elements =
                new ArrayElements<ELEMENT> ( this.elementClass (),
                                             this.flags (),
                                             empty_array );
            return empty_elements;
        }

        final ELEMENT [] original_array = this.container ();
        // Don't bother to check whether we're keeping everything
        // and just return this.
        // We would have to convert all the offsets to indices, then check
        // to make sure they're in order.
        if ( offsets_length > (long) original_array.length )
        {
            // More offsets to keep than we have elements!
            final String elements_string = "" + this + " * " + offsets;
            final long would_be_length = offsets_length;
            throw new ElementsLengthException ( elements_string,
                                                would_be_length );
        }

        final ELEMENT [] kept_array = (ELEMENT [])
            Array.newInstance ( this.elementClass (),
                                (int) offsets_length );
        long last_index = -1L;
        long copy_to_index = 0L;
        for ( long offset : offsets )
        {
            long index =
                AbstractElements.clamp ( offset,
                                         (long) original_array.length );

            if ( index < 0L
                 || index >= (long) original_array.length )
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

            kept_array [ (int) copy_to_index ] =
                original_array [ (int) index ];

            copy_to_index ++;
            last_index = index;
        }

        final ArrayElements<ELEMENT> kept_elements =
            new ArrayElements<ELEMENT> ( this.elementClass (),
                                         this.flags (),
                                         kept_array );
        return kept_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#keepRange(long, long)
     */
    @Override
    @SuppressWarnings("unchecked") // Array.newInstance () -> ELEMENT [].  Ook.
    public final Elements<ELEMENT> keepRange (
            long start,
            long end
            )
        throws IndexOutOfBoundsException
    {
        final ELEMENT [] original_array = this.container ();
        final long original_length = (long) original_array.length;

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
        final ELEMENT [] kept_array = (ELEMENT [])
            Array.newInstance ( element_class, (int) kept_length );

        System.arraycopy ( original_array, (int) first,
                           kept_array, 0, (int) kept_length );

        if ( clamped_start > clamped_end )
        {
            // Reverse order.
            final int end_index = kept_array.length - 1;
            final int half_length = kept_array.length / 2;
            for ( int e = 0; e < half_length; e ++ )
            {
                final ELEMENT swap = kept_array [ end_index - e ];
                kept_array [ end_index - e ] = kept_array [ e ];
                kept_array [ e ] = swap;
            }
        }

        final ArrayElements<ELEMENT> kept_elements =
            new ArrayElements<ELEMENT> ( this.elementClass (),
                                         this.flags (),
                                         kept_array );
        return kept_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#length()
     */
    @Override
    public final long length ()
    {
        final ELEMENT [] array = this.container ();
        return (long) array.length;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#list()
     */
    @Override
    public final List<ELEMENT> list ()
        // Never throws ElementsLengthException.
    {
        final ELEMENT [] array = this.container ();
        final List<ELEMENT> list = new ArrayList<ELEMENT> ( array.length );
        Collections.addAll ( list, array );

        return list;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#overwrite(long, java.lang.Object[])
     */
    @Override
    @SafeVarargs
    @SuppressWarnings({"unchecked", "varargs"})
        // Array.newInstance () cast to ELEMENT [].  Ook.
        // Generic varargs heap pollution,
    public final ArrayElements<ELEMENT> overwrite (
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

        final ELEMENT [] original_array = this.container ();

        final long start =
            AbstractElements.clamp ( offset,
                                     (long) original_array.length );
        final long end = start + (long) elements.length - 1L;
        if ( start < 0L
             || end < 0L
             || end >= (long) original_array.length )
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

        final ELEMENT [] new_array;
        if ( this.flags ().isOverwritable () )
        {
            new_array = original_array;
        }
        else
        {
            new_array = (ELEMENT [])
                Array.newInstance ( this.elementClass (),
                                    original_array.length );

            if ( start > 0L )
            {
                System.arraycopy ( original_array, 0,
                                   new_array, 0,
                                   (int) start );
            }

            if ( end < ( (long) original_array.length - 1L ) )
            {
                System.arraycopy ( original_array, (int) end + 1,
                                   new_array, (int) end + 1,
                                   original_array.length - (int) end - 1 );
            }
        }

        System.arraycopy ( elements, 0,
                           new_array, (int) start,
                           elements.length );

        if ( new_array == original_array )
        {
            return this;
        }

        final ArrayElements<ELEMENT> new_elements =
            new ArrayElements<ELEMENT> ( this.elementClass (),
                                         this.flags (),
                                         new_array );
        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#overwriteAll(long, musaico.foundation.domains.elements.Elements)
     */
    @Override
    @SuppressWarnings("unchecked") // Array.newInstance () -> ELEMENT [].  Ook.
    public final ArrayElements<ELEMENT> overwriteAll (
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
            final ArrayElements<ELEMENT> overwritten_elements =
                this.overwrite ( offset, array );
            return overwritten_elements;
        }

        final ELEMENT [] original_array = this.container ();

        final long elements_length = elements.length ();
        final long start =
            AbstractElements.clamp ( offset,
                                     (long) original_array.length );
        final long end = start + elements_length - 1L;
        if ( start < 0L
             || end < 0L
             || end >= (long) original_array.length )
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

        final ELEMENT [] new_array;
        if ( this.flags ().isOverwritable () )
        {
            new_array = original_array;
        }
        else
        {
            new_array = (ELEMENT [])
                Array.newInstance ( this.elementClass (),
                                    original_array.length );

            if ( start > 0L )
            {
                System.arraycopy ( original_array, 0,
                                   new_array, 0,
                                   (int) start );
            }

            if ( end < ( (long) original_array.length - 1L ) )
            {
                System.arraycopy ( original_array, (int) end + 1,
                                   new_array, (int) end + 1,
                                   original_array.length - (int) end - 1 );
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

            new_array [ cti ] = element;

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

        if ( new_array == original_array )
        {
            return this;
        }

        final ArrayElements<ELEMENT> new_elements =
            new ArrayElements<ELEMENT> ( this.elementClass (),
                                         this.flags (),
                                         new_array );
        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#remove(long[])
     */
    @Override
    @SuppressWarnings("unchecked") // Array.newInstance () -> ELEMENT [].  Ook.
    public final ArrayElements<ELEMENT> remove (
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

        final ELEMENT [] original_array = this.container ();
        if ( offsets.length > original_array.length )
        {
            // More offsets to remove than we have elements!
            final String elements_string =
                "" + this + " / "
                + StringRepresentation.of ( offsets,
                                            StringRepresentation.DEFAULT_ARRAY_LENGTH );
            final long would_be_length =
                (long) original_array.length - (long) offsets.length;
            throw new ElementsLengthException ( elements_string,
                                                would_be_length );
        }

        // Arrays are never variable length, so we always have to create
        // a new array when removing elements.
        final ELEMENT [] new_array = (ELEMENT [])
            Array.newInstance ( this.elementClass (),
                                original_array.length - offsets.length );

        long last_index = -1L;
        long copy_to_index = 0L;
        for ( long offset : offsets )
        {
            long index =
                AbstractElements.clamp ( offset,
                                         (long) original_array.length );

            if ( index < 0L
                 || index >= (long) original_array.length )
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

            if ( index > ( last_index + 1L ) )
            {
                final long from = last_index + 1L;
                final long to = index - 1L;
                final long copy_length = to - from + 1L;
                System.arraycopy ( original_array, (int) last_index + 1,
                                   new_array, (int) copy_to_index,
                                   (int) copy_length );
                copy_to_index += copy_length;
            }

            last_index = index;
        }

        final ArrayElements<ELEMENT> new_elements =
            new ArrayElements<ELEMENT> ( this.elementClass (),
                                         this.flags (),
                                         new_array );
        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#removeAll(musaico.foundation.domains.elements.Elements)
     */
    @Override
    @SuppressWarnings("unchecked") // Array.newInstance () -> ELEMENT [].  Ook.
    public final ArrayElements<ELEMENT> removeAll (
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

        final ELEMENT [] original_array = this.container ();
        if ( offsets_length > (long) original_array.length )
        {
            // More offsets to remove than we have elements!
            final String elements_string = "" + this + " / " + offsets;
            final long would_be_length =
                (long) original_array.length - offsets_length;
            throw new ElementsLengthException ( elements_string,
                                                would_be_length );
        }

        final ELEMENT [] new_array = (ELEMENT [])
            Array.newInstance ( this.elementClass (),
                                original_array.length - (int) offsets_length );
        long last_index = -1L;
        long copy_to_index = 0L;
        for ( long offset : offsets )
        {
            long index =
                AbstractElements.clamp ( offset,
                                         (long) original_array.length );

            if ( index < 0L
                 || index >= (long) original_array.length )
            {
                throw new IndexOutOfBoundsException ( "ERROR Cannot remove"
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
                                                      + offsets
                                                      + " from "
                                                      + this );
            }

            if ( index > ( last_index + 1L ) )
            {
                final long from = last_index + 1L;
                final long to = index - 1L;
                final long copy_length = to - from + 1L;
                System.arraycopy ( original_array, (int) last_index + 1,
                                   new_array, (int) copy_to_index,
                                   (int) copy_length );
                copy_to_index += copy_length;
            }

            last_index = index;
        }

        final ArrayElements<ELEMENT> new_elements =
            new ArrayElements<ELEMENT> ( this.elementClass (),
                                         this.flags (),
                                         new_array );
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
        final ELEMENT [] array = this.container ();
        final LinkedHashSet<ELEMENT> set =
            new LinkedHashSet<ELEMENT> ( array.length );
        for ( int e = 0; e < array.length; e ++ )
        {
            set.add ( array [ e ] );
        }

        return set;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#sort(java.util.Comparator)
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
        // Array.newInstance () -> ELEMENT [].  Ook.
        // raw type constructing Comparator[],
        // cast Comparator[] --> Comparator<ELEMENT>[].
    public final ArrayElements<ELEMENT> sort (
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

        final ELEMENT [] array = this.container ();

        if ( flags.isOverwritable () )
        {
            // Mutable.
            // Sort in place.
            Arrays.sort ( array, comparator );
            return this;
        }

        final Class<ELEMENT> element_class = this.elementClass ();
        final ELEMENT [] sorted_array = (ELEMENT [])
            Array.newInstance ( element_class,
                                array.length );
        System.arraycopy ( array, 0,
                           sorted_array, 0, array.length );

        Arrays.sort ( sorted_array, comparator );

        final ArrayElements<ELEMENT> sorted_elements =
            new ArrayElements<ELEMENT> ( element_class,
                                         this.flags (),
                                         sorted_array,
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

        return flags.makeFixedLength ();
    }
}
