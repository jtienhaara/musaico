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
import musaico.foundation.domains.Generator;
import musaico.foundation.domains.StringRepresentation;

import musaico.foundation.domains.iterators.GeneratorIterator;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * The Elements of a finite sequence or infinite series of objects,
 * such as all the Integers from 1 to 10, or all the Strings of length
 * 1 or more containing only the letters "a", "b" and "c", and so on.
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
public class GeneratedElements<ELEMENT extends Object>
    extends AbstractElements<Generator<ELEMENT>, ELEMENT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new sequence of immutable, fixed-length GeneratedElements.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param generator The Generator which produces a specific number
     *                  (length) of elements, and returns the element
     *                  at each specific index (0L, 1L, 2L, ..., length - 1L).
     *                  Must not be null.
     *
     * @param orders 0 or more Comparators by which these Elements have
     *               already been sorted, in order from # 0 to # last.
     *               Can be empty.  Can be null, which is treated as empty.
     *               Must not contain any null elements.
     *
     * @throws NullPointerException If the specified element_class
     *                              or generator is/are null.
     */
    @SafeVarargs
    @SuppressWarnings("varargs") // generic varargs heap pollution (varargs).
    public GeneratedElements (
            Class<ELEMENT> element_class,
            Generator<ELEMENT> generator,
            Comparator<ELEMENT> ... orders
            )
        throws NullPointerException
    {
        // Throws NullPointerException:
        // Throws ElementsFlagsException (but shouldn't,
        // since we've been careful to use only IMMUTABLE flags):
        this ( element_class,            // element_class
               ElementFlags.IMMUTABLE,   // flags
               generator,                // container
               orders );                 // orders
    }


    /**
     * <p>
     * Creates a new sequence of GeneratedElements.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param flags The ElementFlags, specifying whether these
     *              Elements are mutable or immutable, fixed length
     *              or variable length, and so on.
     *              All GeneratedElements must be fixed length and
     *              not overwritable.  Must not be null.
     *
     * @param generator The Generator which produces a specific number
     *                  (length) of elements, and returns the element
     *                  at each specific index (0L, 1L, 2L, ..., length - 1L).
     *                  Must not be null.
     *
     * @param orders 0 or more Comparators by which these Elements have
     *               already been sorted, in order from # 0 to # last.
     *               Can be empty.  Can be null, which is treated as empty.
     *               Must not contain any null elements.
     *
     * @throws NullPointerException If the specified element_class
     *                              or generator is/are null.
     *
     * @throws ElementsFlagsException If the specified flags
     *                                are incompatible with
     *                                this type of Elements.
     *                                For example, an array container
     *                                cannot be
     *                                <code> ElementFlags.VARIABLE_LENGTH </code>.
     */
    @SafeVarargs
    @SuppressWarnings("varargs") // generic varargs heap pollution (varargs).
    public GeneratedElements (
            Class<ELEMENT> element_class,
            ElementFlags flags,
            Generator<ELEMENT> generator,
            Comparator<ELEMENT> ... orders
            )
        throws NullPointerException,
               ElementsFlagsException
    {
        // Throws NullPointerException:
        super ( element_class, // element_class
                flags,         // flags
                generator,     // container
                orders );      // orders

        if ( ! this.supportedFlags ( flags ).equals ( flags ) )
        {
            throw new ElementsFlagsException ( this, flags );
        }
    }


    /**
     * @see musaico.foundation.domains.elements.AbstractElements#extractContentsFrom(musaico.foundation.domains.elements.Elements)
     *
     * ALWAYS FAILS.  We can't create a Generator for just any arbitrary
     *                sequence of elements.
     */
    @Override
    protected Generator<ELEMENT> extractContentsFrom (
            Elements<ELEMENT> elements
            )
        throws ElementsLengthException
    {
        throw new ElementsLengthException ( elements );
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#array()
     */
    @Override
    @SuppressWarnings("unchecked") // If Array.newInstance() does not return
        // ELEMENT [], then I am a monkey's uncle.
        // Ook.
    public final ELEMENT [] array ()
        throws ElementsLengthException
    {
        final Class<ELEMENT> element_class = this.elementClass ();

        final Generator<ELEMENT> generator = this.container ();
        final long length = generator.length ();
        if ( length > (long) Integer.MAX_VALUE )
        {
            throw new ElementsLengthException ( this );
        }

        final ELEMENT [] new_array = (ELEMENT [])
            Array.newInstance ( element_class, (int) length );

        for ( int e = 0; e < (int) length; e ++ )
        {
            final ELEMENT element = generator.at ( e ) [ 0 ];
            new_array [ e ] = element;
        }

        return new_array;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#at(long)
     */
    @Override
    @SuppressWarnings("unchecked")
    public final ELEMENT [] at (
        long offset
        )
    {
        final Generator<ELEMENT> generator = this.container ();
        final long length = generator.length ();
        final long index =
            AbstractElements.clamp ( offset,
                                     length );
        if ( index < 0L
             || index >= length )
        {
            return this.none ();
        }

        final ELEMENT [] singleton = generator.at ( index );

        return singleton;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#collection()
     */
    @Override
    public final Collection<ELEMENT> collection ()
        throws ElementsLengthException
    {
        return this.list ();
    }


    /**
     * @see musaico.foundation.domains.elements.AbstractElements#contentsString()
     */
    protected final String contentsString ()
    {
        final Generator<ELEMENT> generator = this.container ();
        final long length = generator.length ();
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "{" );
        for ( long e = 0L; e < 3L && e < length; e ++ )
        {
            final ELEMENT element = generator.at ( e ) [ 0 ];
            final String element_string =
                StringRepresentation.of (
                    element,
                    StringRepresentation.DEFAULT_OBJECT_LENGTH );
            if ( e == 0L )
            {
                sbuf.append ( " " );
            }
            else
            {
                sbuf.append ( ", " );
            }

            sbuf.append ( element_string );
        }

        if ( length > 6L )
        {
            sbuf.append ( ", ..." );
        }

        for ( long e = length - 3L; e >= 3L && e < length; e ++ )
        {
            final ELEMENT element = generator.at ( e ) [ 0 ];
            final String element_string =
                StringRepresentation.of (
                    element,
                    StringRepresentation.DEFAULT_OBJECT_LENGTH );
            sbuf.append ( ", " );
            sbuf.append ( element_string );
        }

        if ( length > 0L )
        {
            sbuf.append ( " " );
        }
        sbuf.append ( "}" );

        final String contents_string = sbuf.toString ();

        return contents_string;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#duplicate(musaico.foundation.domains.elements.ElementFlags)
     */
    @Override
    public final GeneratedElements<ELEMENT> duplicate (
            ElementFlags flags
            )
        throws NullPointerException,
               ElementsFlagsException
    {
        if ( flags == null )
        {
            throw new NullPointerException ( "ERROR Cannot duplicate"
                                             + " Elements with null flags: "
                                             + this );
        }
        else if ( ! this.supportedFlags ( flags ).equals ( flags ) )
        {
            // Not allowed for Generators,
            // so not allowed for GeneratedElements.
            throw new ElementsFlagsException ( this, flags );
        }

        final Class<ELEMENT> element_class = this.elementClass ();

        final Generator<ELEMENT> shared_generator = this.container ();

        final GeneratedElements<ELEMENT> new_elements =
            new GeneratedElements<ELEMENT> ( element_class,
                                             flags,
                                             shared_generator );

        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.AbstractElements#equalsElements(java.lang.Object, musaico.foundation.domains.elements.AbstractElements, java.lang.Object)
     */
    @Override
    protected final boolean equalsElements (
            Generator<ELEMENT> this_generator,
            AbstractElements<Generator<ELEMENT>, ELEMENT> that_elements,
            Generator<ELEMENT> that_generator
            )
    {
        if ( this_generator == null
             || that_generator == null )
        {
            // Corrupt.
            return false;
        }

        if ( this_generator.length () != that_generator.length () )
        {
            return false;
        }

        for ( long e = 0L; e < this_generator.length (); e ++ )
        {
            final ELEMENT this_element = this_generator.at ( e ) [ 0 ];
            final ELEMENT that_element = that_generator.at ( e ) [ 0 ];

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
    @SuppressWarnings("unchecked") // If Array.newInstance() does not return
        // ELEMENT [], then I am a monkey's uncle.
        // Ook.
    public final Elements<ELEMENT> filter (
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

        final Generator<ELEMENT> generator = this.container ();
        final long length = generator.length ();
        final List<ELEMENT> filtered_list = new ArrayList<ELEMENT> ();
        boolean is_any_filtered_out = false;
        for ( long e = 0L; e < length; e ++ )
        {
            final ELEMENT element = generator.at ( e ) [ 0 ];
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

        final Class<ELEMENT> element_class = this.elementClass ();
        final ELEMENT [] template =
            (ELEMENT []) Array.newInstance ( element_class,
                                             filtered_list.size () );
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
        final Generator<ELEMENT> generator = this.container ();
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

        final long length = generator.length ();
        final List<Long> found = new ArrayList<Long> ();
        for ( long e = 0L; e < length; e ++ )
        {
            final ELEMENT compare_to_element = generator.at ( e ) [ 0 ];
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
                found.add ( Long.valueOf ( e ) );
            }
        }

        final Long [] template = new Long [ found.size () ];
        final Long [] found_offsets = found.toArray ( template );

        final Elements<Long> found_offsets_elements =
            new ArrayElements<Long> ( Long.class,
                                      ElementFlags.IMMUTABLE,
                                      found_offsets );

        return found_offsets_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#from(java.lang.Class, musaico.foundation.domains.elements.ElementFlags, Object[])
     */
    @Override
    @SafeVarargs
    @SuppressWarnings("varargs") // Pass T_E [] to toString, constructor.
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
        // Always return ArrayElements.  There's no point in creating
        // an array Generator, ArrayElements will do the job better.
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
        else if ( ! this.supportedFlags ( flags ).equals ( flags )
                  || flags.isVariableLength () )
        {
            // Even though ArrayElements have fewer restrictions
            // than GeneratedElements for flags, the request in
            // spirit is intended to create new GeneratedElements.
            // So we require our own class's flags restrictions, just
            // as any other Elements implementation would.
            // Thus the call above to supportedFlags ( ... ).
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
        if ( flags == null
             || that == null )
        {
            throw new NullPointerException ( "ERROR: Can't create "
                                             + ClassName.of ( this.getClass () )
                                             + " from flags "
                                             + flags
                                             + " Elements "
                                             + that );
        }
        else if ( ! this.supportedFlags ( flags ).equals ( flags ) )
        {
            // Even though ArrayElements have fewer restrictions
            // than GeneratedElements for flags, the request in
            // spirit is intended to create new GeneratedElements.
            // So we require our own class's flags restrictions, just
            // as any other Elements implementation would.
            // Thus the call above to supportedFlags ( ... ).
            throw new ElementsFlagsException ( this, flags );
        }

        // Throws NullPointerException, ElementsLengthException,
        // ElementsFlagsException, ConcurrentModificationException
        // (but since we've weeded out the NPEs and checked the flags,
        // most of those exceptions should allegedly never be thrown...
        // Allegedly...)
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
        final Generator<ELEMENT> generator = this.container ();
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

        final long length = generator.length ();
        for ( long e = 0L; e < length; e ++ )
        {
            final ELEMENT compare_to_element = generator.at ( e ) [ 0 ];
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
        final Generator<ELEMENT> generator = this.container ();
        return generator.hashCode ();
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#insert(long, java.lang.Object[])
     */
    @Override
    @SafeVarargs
    @SuppressWarnings({"varargs","unchecked"}) // Generic varargs
        // heap pollution, Array.newInstance() unchecked cast to ELEMENT [].
    public final Elements<ELEMENT> insert (
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
                final Generator<ELEMENT> generator = this.container ();
                final long length = generator.length ();
                final long index =
                    AbstractElements.clamp ( offset, length );
                if ( index < 0L
                     || index > length )
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

        final Generator<ELEMENT> generator = this.container ();
        final long length = generator.length ();
        if ( length > (long) Integer.MAX_VALUE
             || ( Integer.MAX_VALUE - (int) length ) < elements.length )
        {
            // Too big for a list or an array.
            final String elements_string =
                "" + this + " + "
                + StringRepresentation.of ( elements,
                                            StringRepresentation.DEFAULT_ARRAY_LENGTH );
            final long would_be_length;
            if ( ( (long) elements.length ) <= ( Long.MAX_VALUE - length ) )
            {
                would_be_length = length + (long) elements.length;
            }
            else
            {
                would_be_length = Long.MAX_VALUE;
            }

            throw new ElementsLengthException ( elements_string,
                                                would_be_length );
        }

        final long insert_at_index =
            AbstractElements.clamp ( offset,
                                     length );

        final List<ELEMENT> inserted_list = new ArrayList<ELEMENT> ();
        for ( long e = 0L; e <= length; e ++ )
        {
            if ( e == insert_at_index )
            {
                for ( ELEMENT inserted_element : elements )
                {
                    inserted_list.add ( inserted_element );
                }
            }

            if ( e == length )
            {
                break;
            }

            final ELEMENT element = generator.at ( e ) [ 0 ];
            inserted_list.add ( element );
        }

        final Class<ELEMENT> element_class = this.elementClass ();
        final ELEMENT [] template =
            (ELEMENT []) Array.newInstance ( element_class,
                                             inserted_list.size () );
        final ELEMENT [] inserted_array = inserted_list.toArray ( template );

        final ArrayElements<ELEMENT> inserted_elements =
            new ArrayElements<ELEMENT> ( element_class,
                                         this.flags (),
                                         inserted_array );

        return inserted_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#insertAll(long, musaico.foundation.domains.elements.Elements)
     */
    @Override
    @SuppressWarnings("unchecked") // Array.newInstance() cast to ELEMENT [].
    public final Elements<ELEMENT> insertAll (
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
            final Elements<ELEMENT> inserted_elements =
                this.insert ( offset, array );
            return inserted_elements;
        }

        final Generator<ELEMENT> generator = this.container ();
        final long elements_length = elements.length ();

        if ( elements_length == 0 )
        {
            // Error check the offset, then just return this as-is.
            if ( offset != Elements.AFTER_LAST )
            {
                final long length = generator.length ();
                final long index =
                    AbstractElements.clamp ( offset, length );
                if ( index < 0L
                     || index > length )
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

        final long length = generator.length ();
        if ( length > (long) Integer.MAX_VALUE
             || ( Integer.MAX_VALUE - (int) length ) < elements_length )
        {
            // Too big for a list or an array.
            final String elements_string =
                "" + this + " + "
                + StringRepresentation.of ( elements,
                                            StringRepresentation.DEFAULT_ARRAY_LENGTH );
            final long would_be_length;
            if ( elements_length <= ( Long.MAX_VALUE - length ) )
            {
                would_be_length = length + elements_length;
            }
            else
            {
                would_be_length = Long.MAX_VALUE;
            }

            throw new ElementsLengthException ( elements_string,
                                                would_be_length );
        }

        final long insert_at_index =
            AbstractElements.clamp ( offset,
                                     length );

        final List<ELEMENT> inserted_list = new ArrayList<ELEMENT> ();
        for ( long e = 0L; e <= length; e ++ )
        {
            if ( e == insert_at_index )
            {
                for ( ELEMENT inserted_element : elements )
                {
                    inserted_list.add ( inserted_element );
                }
            }

            if ( e == length )
            {
                break;
            }

            final ELEMENT element = generator.at ( e ) [ 0 ];
            inserted_list.add ( element );
        }

        final Class<ELEMENT> element_class = this.elementClass ();
        final ELEMENT [] template =
            (ELEMENT []) Array.newInstance ( element_class,
                                             inserted_list.size () );
        final ELEMENT [] inserted_array = inserted_list.toArray ( template );

        final ArrayElements<ELEMENT> inserted_elements =
            new ArrayElements<ELEMENT> ( element_class,
                                         this.flags (),
                                         inserted_array );

        return inserted_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#isArrayLength()
     */
    @Override
    public final boolean isArrayLength ()
    {
        final Generator<ELEMENT> generator = this.container ();
        final long length = generator.length ();
        if ( length <= (long) Integer.MAX_VALUE )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#isEmpty()
     */
    @Override
    public final boolean isEmpty ()
    {
        final Generator<ELEMENT> generator = this.container ();
        if ( generator.length () == 0L )
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
        final Generator<ELEMENT> generator = this.container ();
        if ( generator.length () == 1L )
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
        final Generator<ELEMENT> generator = this.container ();
        final GeneratorIterator<ELEMENT> iterator =
            new GeneratorIterator<ELEMENT> ( generator );
        return iterator;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#length()
     */
    @Override
    public final long length ()
    {
        final Generator<ELEMENT> generator = this.container ();
        return generator.length ();
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#list()
     */
    @Override
    public final List<ELEMENT> list ()
        throws ElementsLengthException
    {
        final Generator<ELEMENT> generator = this.container ();
        final long length = generator.length ();
        if ( length > (long) Integer.MAX_VALUE )
        {
            throw new ElementsLengthException ( this );
        }

        final List<ELEMENT> new_list =
            new ArrayList<ELEMENT> ( (int) length );
        for ( long e = 0L; e < length; e ++ )
        {
            final ELEMENT element = generator.at ( e ) [ 0 ];
            new_list.add ( element );
        }

        return new_list;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#overwrite(long, java.lang.Object[])
     */
    @Override
    @SafeVarargs
    @SuppressWarnings({"varargs","unchecked"}) // Generic varargs
        // heap pollution, Array.newInstance() unchecked cast to ELEMENT [].
    public final Elements<ELEMENT> overwrite (
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

        final Generator<ELEMENT> generator = this.container ();
        final long length = generator.length ();

        final long start =
            AbstractElements.clamp ( offset, length );
        final long end = start + (long) elements.length - 1L;
        if ( start < 0L
             || end < 0L
             || end >= length )
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
        else if ( length > (long) Integer.MAX_VALUE )
        {
            // Too big for a list or an array.
            final String elements_string =
                "" + this + " <- "
                + StringRepresentation.of ( elements,
                                            StringRepresentation.DEFAULT_ARRAY_LENGTH );

            throw new ElementsLengthException ( elements_string,
                                                length );
        }

        final List<ELEMENT> overwritten_list =
            new ArrayList<ELEMENT> ( (int) length );

        for ( long e = 0L; e < length; e ++ )
        {
            if ( e == start )
            {
                Collections.addAll ( overwritten_list, elements );
                e = end;
                continue;
            }

            final ELEMENT element = generator.at ( e ) [ 0 ];
            overwritten_list.add ( element );
        }

        final Class<ELEMENT> element_class = this.elementClass ();
        final ELEMENT [] template =
            (ELEMENT []) Array.newInstance ( element_class,
                                             overwritten_list.size () );
        final ELEMENT [] overwritten_array =
            overwritten_list.toArray ( template );

        final ArrayElements<ELEMENT> overwritten_elements =
            new ArrayElements<ELEMENT> ( element_class,
                                         this.flags (),
                                         overwritten_array );

        return overwritten_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#overwriteAll(long, musaico.foundation.domains.elements.Elements)
     */
    @Override
    @SuppressWarnings("unchecked") // Array.newInstance() cast to ELEMENT [].
    public final Elements<ELEMENT> overwriteAll (
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
            final Elements<ELEMENT> overwritten_elements =
                this.overwrite ( offset, array );
            return overwritten_elements;
        }

        final long elements_length = elements.length ();
        if ( elements_length == 0 )
        {
            return this;
        }

        final Generator<ELEMENT> generator = this.container ();
        final long length = generator.length ();
        if ( length > (long) Integer.MAX_VALUE )
        {
            // Too big for a list or an array.
            final String elements_string =
                "" + this + " <- "
                + StringRepresentation.of ( elements,
                                            StringRepresentation.DEFAULT_ARRAY_LENGTH );

            throw new ElementsLengthException ( elements_string,
                                                length );
        }

        final long start =
            AbstractElements.clamp ( offset, length );
        final long end = start + elements_length - 1L;
        if ( start < 0L
             || end < 0L
             || end >= length )
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

        final List<ELEMENT> overwritten_list =
            new ArrayList<ELEMENT> ( (int) length );

        for ( long e = 0L; e < start; e ++ )
        {
            final ELEMENT element = generator.at ( e ) [ 0 ];
            overwritten_list.add ( element );
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

            overwritten_list.set ( cti, element );

            cti ++;
        }

        if ( (long) cti != ( end + 1L ) )
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

        for ( long e = end + 1L; e < length; e ++ )
        {
            final ELEMENT element = generator.at ( e ) [ 0 ];
            overwritten_list.add ( element );
        }

        final Class<ELEMENT> element_class = this.elementClass ();
        final ELEMENT [] template =
            (ELEMENT []) Array.newInstance ( element_class,
                                             overwritten_list.size () );
        final ELEMENT [] overwritten_array =
            overwritten_list.toArray ( template );

        final ArrayElements<ELEMENT> overwritten_elements =
            new ArrayElements<ELEMENT> ( element_class,
                                         this.flags (),
                                         overwritten_array );

        return overwritten_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#remove(long[])
     */
    @Override
    @SuppressWarnings("unchecked") // Array.newInstance() --> ELEMENT []. Ook.
    public final Elements<ELEMENT> remove (
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

        final Generator<ELEMENT> generator = this.container ();
        final long length = generator.length ();
        if ( length > (long) Integer.MAX_VALUE )
        {
            // Too big for a list or an array.
            final String elements_string =
                "" + this + " / [ "
                + StringRepresentation.of (
                      offsets,
                      StringRepresentation.DEFAULT_ARRAY_LENGTH )
                + " ]";

            final long would_be_length = length - (long) offsets.length;
            throw new ElementsLengthException ( elements_string,
                                                would_be_length );
        }
        else if ( ( (long) offsets.length ) > length )
        {
            // More offsets to remove than we have elements!
            final String elements_string =
                "" + this + " / "
                + StringRepresentation.of ( offsets,
                                            StringRepresentation.DEFAULT_ARRAY_LENGTH );
            final long would_be_length = length - (long) offsets.length;
            throw new ElementsLengthException ( elements_string,
                                                would_be_length );
        }

        final List<ELEMENT> removed_list =
            new ArrayList<ELEMENT> ( ( (int) length ) - offsets.length );

        long last_index = -1L;
        long num_removed = 0L;
        for ( long offset : offsets )
        {
            long index =
                AbstractElements.clamp ( offset, length );

            if ( index < 0L
                 || index >= length )
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

            final long from = last_index + 1L;
            final long to = index - 1L;
            for ( long keep_index = from;
                  keep_index <= to;
                  keep_index ++ )
            {
                final ELEMENT element = generator.at ( keep_index ) [ 0 ];
                removed_list.add ( element );
            }

            num_removed ++;
            last_index = index;
        }

        final Class<ELEMENT> element_class = this.elementClass ();
        final ELEMENT [] template =
            (ELEMENT []) Array.newInstance ( element_class,
                                             removed_list.size () );
        final ELEMENT [] removed_array =
            removed_list.toArray ( template );

        final ArrayElements<ELEMENT> removed_elements =
            new ArrayElements<ELEMENT> ( element_class,
                                         this.flags (),
                                         removed_array );
        return removed_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#removeAll(musaico.foundation.domains.elements.Elements)
     */
    @Override
    @SuppressWarnings("unchecked") // Array.newInstance() --> ELEMENT []. Ook.
    public final Elements<ELEMENT> removeAll (
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

        final Generator<ELEMENT> generator = this.container ();
        final long length = generator.length ();
        if ( length > (long) Integer.MAX_VALUE )
        {
            // Too big for a list or an array.
            final String elements_string =
                "" + this + " / [ "
                + offsets
                + " ]";

            final long would_be_length = length - offsets.length ();
            throw new ElementsLengthException ( elements_string,
                                                would_be_length );
        }
        else if ( offsets_length > length )
        {
            // More offsets to remove than we have elements!
            final String elements_string =
                "" + this + " / "
                + StringRepresentation.of ( offsets,
                                            StringRepresentation.DEFAULT_ARRAY_LENGTH );
            final long would_be_length = length - offsets_length;
            throw new ElementsLengthException ( elements_string,
                                                would_be_length );
        }

        final List<ELEMENT> removed_list =
            new ArrayList<ELEMENT> ( ( (int) length ) - (int) offsets_length );

        long last_index = -1L;
        long num_removed = 0L;
        for ( Long offset_object : offsets )
        {
            final long offset = offset_object.longValue ();
            long index =
                AbstractElements.clamp ( offset, length );

            if ( index < 0L
                 || index >= length )
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

            final long from = last_index + 1L;
            final long to = index - 1L;
            for ( long keep_index = from;
                  keep_index <= to;
                  keep_index ++ )
            {
                final ELEMENT element = generator.at ( keep_index ) [ 0 ];
                removed_list.add ( element );
            }

            num_removed ++;
            last_index = index;
        }

        final Class<ELEMENT> element_class = this.elementClass ();
        final ELEMENT [] template =
            (ELEMENT []) Array.newInstance ( element_class,
                                             removed_list.size () );
        final ELEMENT [] removed_array =
            removed_list.toArray ( template );

        final ArrayElements<ELEMENT> removed_elements =
            new ArrayElements<ELEMENT> ( element_class,
                                         this.flags (),
                                         removed_array );
        return removed_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#set()
     */
    @Override
    public final LinkedHashSet<ELEMENT> set ()
        throws ElementsLengthException
    {
        final Generator<ELEMENT> generator = this.container ();
        final long length = generator.length ();
        if ( length > (long) Integer.MAX_VALUE )
        {
            throw new ElementsLengthException ( this );
        }

        final LinkedHashSet<ELEMENT> new_set =
            new LinkedHashSet<ELEMENT> ( (int) length );
        for ( long e = 0L; e < length; e ++ )
        {
            final ELEMENT element = generator.at ( e ) [ 0 ];
            new_set.add ( element );
        }

        return new_set;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#sort(java.util.Comparator)
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
        // raw type constructing Comparator[],
        // cast Comparator[] --> Comparator<ELEMENT>[].
    public final Elements<ELEMENT> sort (
            Comparator<ELEMENT> comparator
            )
        throws NullPointerException,
               ElementsLengthException,
               IllegalStateException
    {
        // GeneratedElements could conceivably be in some order.
        // For example, GeneratedElements covering the range of
        // an IndexGenerator in order of ascending long ints.
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

        final Generator<ELEMENT> generator = this.container ();

        final ELEMENT [] sorted_array = this.array ();

        Arrays.sort ( sorted_array, comparator );

        final Class<ELEMENT> element_class = this.elementClass ();

        // Throws NullPointerException, ElementsLengthException:
        final Elements<Comparator<ELEMENT>> new_orders =
            original_orders.insert ( Elements.AFTER_LAST,
                                     comparator );

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

        return flags
            .makeFixedLength ()
            .makeFixedValues ();
    }
}
