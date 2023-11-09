package musaico.foundation.domains.elements;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;


import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.StringRepresentation;

import musaico.foundation.domains.iterators.ArrayIterator;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * The Extractor for 1-dimensional arrays (<code> ELEMENT [] </code>).
 * </p>
 *
 * <p>
 * In Java, every Extractor must be Serializable in order to play nicely
 * across RMI.
 * </p>
 *
 * <p>
 * In Java, every Extractor must implement equals (), hashCode ()
 * and toString (), in order to play nicely with Maps and so that
 * debugging / troubleshooting messages aren't cryptic.
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
public class ArrayExtractor<ELEMENT extends Object>
    implements Extractor<ELEMENT [], Integer, ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The Class of elements contained in arrays
    // handled by this ArrayExtractor.
    private final Class<ELEMENT> elementClass;


    /**
     * <p>
     * Creates a new ArrayExtractor.
     * </p>
     *
     * @param element_class The Class of elements contained in arrays
     *                      handled by this ArrayExtractor.
     *                      Must not be null.
     *
     * @throws NullPointerException If the specified element_class
     *                              is null.
     */
    public ArrayExtractor (
            Class<ELEMENT> element_class
            )
        throws NullPointerException
    {
        if ( element_class == null )
        {
            throw new NullPointerException ( "ERROR Cannot create "
                                             + ClassName.of ( this.getClass () )
                                             + " with"
                                             + " element_class = "
                                             + ClassName.of ( element_class ) );
        }

        this.elementClass = element_class;
    }


    /**
     * @see musaico.foundation.domains.elements.Extractor#at(java.lang.Object, java.lang.Object)
     */
    @Override
    public final ELEMENT at (
            ELEMENT [] container,
            Integer index
            )
        throws NullPointerException,
               IndexOutOfBoundsException
    {
        if ( container == null )
        {
            throw new NullPointerException ( "ERROR Cannot return"
                                             + " element for "
                                             + ClassName.of ( this.getClass () )
                                             + " from container = "
                                             + container
                                             + ", index = "
                                             + index );
        }
        else if ( index < 0L
                  || index >= container.length )
        {
            final String container_string =
                StringRepresentation.of ( container,
                                          StringRepresentation.DEFAULT_ARRAY_LENGTH );
            throw new IndexOutOfBoundsException ( "ERROR Cannot return"
                                                  + " element for "
                                                  + ClassName.of ( this.getClass () )
                                                  + " from container = "
                                                  + container_string
                                                  + ", index = "
                                                  + index );
        }

        return container [ index ];
    }


    /**
     * <p>
     * Compacts the specified (start, end) pairs of offsets
     * for the specified container into the shortest possible
     * sequence of pairs of (start, end) offsets.
     * </p>
     *
     * <p>
     * If an odd number of offsets is specified, then the final offset
     * will be treated as the start offset, ending with the offset
     * to the last element in the specified container
     * ( <code> { start, length ( container ) - 1L } </code>).
     * </p>
     *
     * <p>
     * For example, if the container is an array of length 42,
     * then the ranges <code> { 0L, 41L } </code>, <code> { 0L } </code>,
     * <code> { Elements.LAST + 41L, Elements.LAST } </code>
     * and <code> { 0L, 1L, 2L, 3L, ..., 40L, 41L } </code> will
     * all result in the array of offsets <code> { 0L, 41L } </code>.
     * </p>
     *
     * @param container The container whose offsets will be compacted.
     *                  Must not be null.
     *
     * @param length The number of elements in the container.
     *               Must be greater than or equal to 0L.
     *
     * @param start_end_offset_pairs The (start, end) offset pairs.
     *                               Each can be an offset from the start
     *                               (0L or greater) or an offset from the end
     *                               (Elements.LAST or greater).
     *                               Each can be positive or negative.
     *
     * @return The compacted offsets.  Always contains only pairs of offsets,
     *         never an odd number.  Each offset is always 0L or greater,
     *         and less than <code> length ( container ) </code>.
     *         Never null.
     *
     * @throws NullPointerException If the specified container
     *                              and/or start_end_offset_pairs is/are null.
     *
     * @throws IndexOutOfBoundsException If the container does not have
     *                                   one or more of the specified
     *                                   offset(s), or if Elements.AFTER_LAST
     *                                   or Elements.NONE is specified.
     */
    protected static final long [] compact (
            Object container,
            long length,
            long ... start_end_offset_pairs
            )
        throws NullPointerException,
               IndexOutOfBoundsException
    {
        final List<long []> compacted_pairs = new ArrayList<long []> ();
        boolean has_any_reverse_offsets = false;
        boolean has_any_compacted_pairs = false;
        long last_start = -1L;
        long last_end = -1L;
        for ( int sep = 0; sep < start_end_offset_pairs.length; sep += 2 )
        {
            long start = start_end_offset_pairs [ sep ];
            if ( start < -1L )
            {
                start = length + Elements.AFTER_LAST - start;
                has_any_reverse_offsets = true;
            }
            if ( start < 0L
                 || start >= length )
            {
                throw new IndexOutOfBoundsException ( "ERROR start offset "
                                                      + start_end_offset_pairs [ sep ]
                                                      + " [ " + sep + " ]"
                                                      + " is out of bounds"
                                                      + "for array of length "
                                                      + length );
            }

            final long original_end;
            final String original_index;
            if ( ( sep + 1 ) < start_end_offset_pairs.length )
            {
                original_end = start_end_offset_pairs [ sep + 1 ];
                original_index = " [ " + ( sep + 1 ) + " ]";
            }
            else
            {
                original_end = length - 1L;
                original_index = " (end)";
            }
            long end = original_end;
            if ( end < -1L )
            {
                end = length + Elements.AFTER_LAST - end;
                has_any_reverse_offsets = true;
            }
            if ( end < 0L
                 || end >= length )
            {
                throw new IndexOutOfBoundsException ( "ERROR end offset "
                                                      + original_end
                                                      + original_index
                                                      + " is out of bounds"
                                                      + "for array of length "
                                                      + length );
            }

            final boolean is_compactable;
            if ( sep == 0 )
            {
                // sep = 0, first start end pair.
                is_compactable = false;
            }
            else if ( last_start <= last_end )
            {
                if ( start <= end
                     && start == ( last_end + 1L ) )
                {
                    // e.g. { 0, 4, 5, 9 } start = 5, end = 9,
                    // last_start = 0, last_end = 4.
                    is_compactable = true;
                }
                else
                {
                    is_compactable = false;
                }
            }
            else
            {
                if ( start > end
                     && start == ( last_end - 1L ) )
                {
                    // e.g. { 9, 5, 4, 0 } start = 4, end = 0,
                    // last_start = 9, last_end = 5.
                    is_compactable = true;
                }
                else
                {
                    is_compactable = false;
                }
            }

            final long [] compacted_pair;
            if ( is_compactable )
            {
                compacted_pair =
                    compacted_pairs.get ( compacted_pairs.size () - 1 );
                has_any_compacted_pairs = true;
            }
            else
            {
                compacted_pair = new long [ 2 ];
                compacted_pair [ 0 ] = start;
            }

            compacted_pair [ 1 ] = end;

            compacted_pairs.add ( compacted_pair );
        }

        if ( ( start_end_offset_pairs.length % 2 ) == 0
             && ! has_any_compacted_pairs
             && ! has_any_reverse_offsets )
        {
            return start_end_offset_pairs;
        }

        final int num_pairs = compacted_pairs.size ();
        final long [] compacted_offsets = new long [ num_pairs * 2 ];
        int co = 0;
        for ( long [] compacted_pair : compacted_pairs )
        {
            compacted_offsets [ co ] = compacted_pair [ 0 ];
            compacted_offsets [ co + 1 ] = compacted_pair [ 1 ];
            co += 2;
        }

        return compacted_offsets;
    }


    /**
     * @see musaico.foundation.domains.elements.Extractor#duplicate(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // If Array.newInstance() does not return
        // ELEMENT [], then I am a monkey's uncle.
        // Ook.
    public final ELEMENT [] duplicate (
            ELEMENT [] container
            )
        throws NullPointerException
    {
        if ( container == null )
        {
            throw new NullPointerException ( "ERROR Cannot create"
                                             + " duplicate container for "
                                             + ClassName.of ( this.getClass () )
                                             + " from container = "
                                             + container );
        }

        final ELEMENT [] duplicate = (ELEMENT [])
            Array.newInstance ( this.elementClass,
                                container.length );

        System.arraycopy ( container, 0,
                           duplicate, 0, duplicate.length );

        return duplicate;
    }


    /**
     * @see musaico.foundation.domains.elements.Extractor#elementClass()
     */
    @Override
    public final Class<ELEMENT> elementClass ()
    {
        return this.elementClass;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
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
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.domains.elements.Extractor#extract(musaico.foundation.domains.elements.Elements, boolean, musaico.foundation.domains.elements.Extractor, long[])
     */
    @Override
    @SuppressWarnings("unchecked") // Checks via reflection before casting.
    public final ELEMENT [] extract (
            Elements<ELEMENT> elements,
            boolean is_new_instance,
            long ... start_end_offset_pairs
            )
        throws NullPointerException,
               IndexOutOfBoundsException,
               ElementsLengthException
    {
        if ( elements == null
             || start_end_offset_pairs == null )
        {
            final String start_end_offset_pairs_string =
                StringRepresentation.of ( start_end_offset_pairs,
                                          StringRepresentation.DEFAULT_ARRAY_LENGTH );
            throw new NullPointerException ( "ERROR Cannot create"
                                             + " container for "
                                             + ClassName.of ( this.getClass () )
                                             + " from elements = "
                                             + elements
                                             + ", start_end_offset_pairs = "
                                             + start_end_offset_pairs_string );
        }

        final Object from_container = elements.container ();
        final Extractor<Object> that =
            (Extractor<Object>) elements.extractor ();

        if ( that.isEmpty ( from_container ) )
        {
            if ( offsets.length == 0 )
            {
final ELEMENT [] empty =!!!!!!!!!!!!!!!!

        final long [] compacted_offsets;
        if ( offsets.length == 0 )
        {
            if ( that.length ( from_container ) == 0L )
            {
                !!!;
            compacted_offsets = new long [ 2 ];
            compacted_offsets [ 0 ] = 0L;
        }
        else

        if ( ! is_new_instance // Always create a new instance?
             && from_container.getClass ().isArray () // Is Object []?
             && from_container.getClass ().getComponentType ()
                    == this.elementClass ) // Is ELEMENT []?
        {
            if ( start_end_offset_pairs.length == 0 )
            {
                // ELEMENT [] array, always return raw,
                // even if it is the wrong length (e.g. RangeOfElements).
                return (ELEMENT []) from_container;
            }
            else
            {
                final ELEMENT [] array = (ELEMENT []) from_container;
                final long elements_length = elements.length ();
                final long array_length = (long) array.length;
                if ( elements_length == array_length )
                {
                    // ELEMENT [] array of the same length.
                    // Have we been asked to return all of the elements?
                    long last_offset = -1L;
                    for ( int sep = 0;
                          sep < start_end_offset_pairs.length;
                          sep += 2 )
                    {
                        long start = start_end_offset_pairs [ sep ];
                        if ( start < -1L )
                        {
                            !!!!!;
                        final long end;
                        if ( sep < start_end_offset_pairs.length )
                        {
                            end = start_end_offset_pairs [ sep + 1 ];
                        }
                        else
                        {
                            end = 
                    return array;
                }

                // ELEMENT [] array is not the same length as
                // elements, so we have to create an instance, even
                // if all elements have been requested
                // by the start_end_offset_pairs (e.g. { 0L } to return
                // all elements of the correct length from start to end).
            }
        }

        // For whatever reason, we now have to create a new container.
        // Let's figure out how big it needs to be.
        final long from_start = elements.start ();
        final long from_end = elements.end ();
        long to_length = 0L;
        for ( int sep = 0; sep < start_end_offset_pairs.length; sep += 2 )
        {
            final long start;
            final long end;
            if ( ( sep + 1 ) < start_end_offset_pairs.length ) // {start, end}
            {
                if ( start_end_offset_pairs [ sep ] <=
                     start_end_offset_pairs [ sep + 1 ] ) // Forward.
                {
                    start = start_end_offset_pairs [ sep ];
                    end = start_end_offset_pairs [ sep + 1 ];
                }
                else // Reversed.
                {
                    start = start_end_offset_pairs [ sep + 1 ];
                    end = start_end_offset_pairs [ sep ];
                }
            }
            else // { start }  (to end of elements)
            {
                start = start_end_offset_pairs [ sep ];
                end = from_end;
            }

            if ( start < from_start
                 || start > from_end
                 || end < from_start
                 || end > from_end )
            {
                final String start_end_offset_pairs_string =
                    StringRepresentation.of ( start_end_offset_pairs,
                                              StringRepresentation.DEFAULT_ARRAY_LENGTH );
                throw new IndexOutOfBoundsException ( "ERROR Cannot"
                                                      + " extract elements"
                                                      + " for "
                                                      + ClassName.of ( this.getClass () )
                                                      + " from elements = "
                                                      + elements
                                                      + ", is_new_instance = "
                                                      + is_new_instance
                                                      + ", start_end_offset_pairs = "
                                                      + start_end_offset_pairs_string
                                                      + ": start/end { "
                                                      + start
                                                      + ", "
                                                      + end
                                                      + " } out of bounds" );
            }

            to_length += end - start + 1L;
        }

        if ( start_end_offset_pairs.length == 0 )
        {
            to_length = elements.length ();
        }

        if ( to_length < 0L
             || to_length > (long) Integer.MAX_VALUE )
        {
            throw new ElementsLengthException ( elements,
                                                to_length );
        }

        final ELEMENT [] to_container =
            (ELEMENT []) Array.newInstance ( this.elementClass,
                                             (int) to_length );

        int to_index = 0;
        for ( int sep = 0; sep < start_end_offset_pairs.length; sep += 2 )
        {
            final long start;
            final long end;
            if ( ( sep + 1 ) < start_end_offset_pairs.length ) // {start, end}
            {
                if ( start_end_offset_pairs [ sep ] <=
                     start_end_offset_pairs [ sep + 1 ] ) // Forward.
                {
                    start = start_end_offset_pairs [ sep ];
                    end = start_end_offset_pairs [ sep + 1 ];
                }
                else // Reversed.
                {
                    start = start_end_offset_pairs [ sep + 1 ];
                    end = start_end_offset_pairs [ sep ];
                }
            }
            else // { start }  (to end of elements)
            {
                start = start_end_offset_pairs [ sep ];
                end = from_end;
            }

            for ( long from_index = start;
                  from_index <= end;
                  from_index ++ )
            {
                if ( ! elements.has ( from_index ) )
                {
                    continue;
                }

                to_container [ to_index ] = elements.at ( from_index ) [ 0 ];
                to_index ++;
            }
        }

        return to_container;
    }


    /**
     * @see musaico.foundation.domains.elements.Extractor#filter(java.lang.Object, musaico.foundation.filter.Filter)
     */
    @Override
    @SuppressWarnings("unchecked") // If Array.newInstance() does not return
        // ELEMENT [], then I am a monkey's uncle.
        // Oo oo oo oo ah.
    public final ELEMENT [] filter (
            ELEMENT [] container,
            Filter<ELEMENT> filter
            )
        throws NullPointerException
    {
        if ( container == null
             || filter == null )
        {
            throw new NullPointerException ( "ERROR Cannot filter for "
                                             + ClassName.of ( this.getClass () )
                                             + " with container = "
                                             + container
                                             + ", filter = "
                                             + filter );
        }

        final List<ELEMENT> filtered_list = new ArrayList<ELEMENT> ();
        boolean is_any_filtered_out = false;
        for ( int e = 0; e < container.length; e ++ )
        {
            final ELEMENT element = container [ e ];
            if ( ! filter.filter ( element ).isKept () )
            {
                // Filtered out.
                is_any_filtered_out = true;
                continue;
            }

            filtered_list.add ( element );
        }

        if ( ! is_any_filtered_out )
        {
            return container;
        }

        // We always create a new array, because arrays are
        // fixed length, so no way of filtering in-place.
        final ELEMENT [] filtered_array = (ELEMENT [])
            Array.newInstance ( this.elementClass, filtered_list.size () );
        for ( int e = 0; e < filtered_array.length; e ++ )
        {
            final ELEMENT element = filtered_list.get ( e );
            filtered_array [ e ] = element;
        }

        return filtered_array;
    }


    /**
     * @see musaico.foundation.domains.elements.Extractor#has(java.lang.Object, java.lang.Object)
     */
    @Override
    public final boolean has (
            ELEMENT [] container,
            Integer index
            )
        throws NullPointerException,
               IndexOutOfBoundsException
    {
        if ( container == null )
        {
            throw new NullPointerException ( "ERROR Cannot check if"
                                             + " container has element for "
                                             + ClassName.of ( this.getClass () )
                                             + " in container = "
                                             + container
                                             + ", index = "
                                             + index );
        }
        else if ( index < 0L )
        {
            final String container_string =
                StringRepresentation.of ( container,
                                          StringRepresentation.DEFAULT_ARRAY_LENGTH );
            throw new IndexOutOfBoundsException ( "ERROR Cannot check if"
                                                  + " container has element for "
                                                  + ClassName.of ( this.getClass () )
                                                  + " in container = "
                                                  + container_string
                                                  + ", index = "
                                                  + index );
        }

        if ( index < container.length )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return ClassName.of ( this.getClass () ).hashCode () * 31
            + ClassName.of ( this.elementClass ).hashCode () * 7;
    }


    /**
     * @see musaico.foundation.domains.elements.Extractor#isEmpty(java.lang.Object)
     */
    @Override
    public final boolean isEmpty (
            ELEMENT [] container
            )
        throws NullPointerException
    {
        if ( container == null )
        {
            throw new NullPointerException ( "ERROR Cannot check if"
                                             + " container is empty for "
                                             + ClassName.of ( this.getClass () )
                                             + " in container = "
                                             + container );
        }

        if ( container.length == 0 )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#isSingleton(java.lang.Object)
     */
    @Override
    public final boolean isSingleton (
            ELEMENT [] container
            )
        throws NullPointerException
    {
        if ( container == null )
        {
            throw new NullPointerException ( "ERROR Cannot check if"
                                             + " container is singleton for "
                                             + ClassName.of ( this.getClass () )
                                             + " in container = "
                                             + container );
        }

        if ( container.length == 1 )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.domains.elements.Extractor#iterator(java.lang.Object)
     */
    @Override
    public final Iterator<ELEMENT> iterator (
            ELEMENT [] container
            )
        throws NullPointerException
    {
        if ( container == null )
        {
            throw new NullPointerException ( "ERROR Cannot return"
                                             + " element for "
                                             + ClassName.of ( this.getClass () )
                                             + " from container = "
                                             + container );
        }

        final ArrayIterator<ELEMENT> iterator =
            new ArrayIterator<ELEMENT> ( container );
        return iterator;
    }


    /**
     * @see musaico.foundation.domains.elements.Extractor#length(java.lang.Object)
     */
    @Override
    public final long length (
            ELEMENT [] container
            )
        throws NullPointerException
    {
        if ( container == null )
        {
            throw new NullPointerException ( "ERROR Cannot return"
                                             + " length for "
                                             + ClassName.of ( this.getClass () )
                                             + " from container = "
                                             + container );
        }

        return (long) container.length;
    }


    /**
     * @see musaico.foundation.domains.elements.Extractor#set(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public final ArrayExtractor<ELEMENT> set (
            ELEMENT [] container,
            Integer index,
            ELEMENT element_or_null
            )
        throws NullPointerException,
               IndexOutOfBoundsException
    {
        if ( container == null )
        {
            throw new NullPointerException ( "ERROR Cannot set"
                                             + " element for "
                                             + ClassName.of ( this.getClass () )
                                             + " in container = "
                                             + container
                                             + ", index = "
                                             + index
                                             + ", element_or_null = "
                                             + element_or_null );
        }
        else if ( index < 0L
                  || index >= container.length )
        {
            final String container_string =
                StringRepresentation.of ( container,
                                          StringRepresentation.DEFAULT_ARRAY_LENGTH );
            throw new IndexOutOfBoundsException ( "ERROR Cannot set"
                                                  + " element for "
                                                  + ClassName.of ( this.getClass () )
                                                  + " in container = "
                                                  + container_string
                                                  + ", index = "
                                                  + index
                                                  + ", element_or_null = "
                                                  + element_or_null );
        }

        container [ index ] = element_or_null;

        return this;
    }


    /**
     * @see musaico.foundation.domains.elements.Extractor#sort(java.lang.Object, java.util.Comparator)
     */
    @Override
    public final ELEMENT [] sort (
            ELEMENT [] container,
            Comparator<ELEMENT> order
            )
        throws NullPointerException
    {
        if ( container == null
             || order == null )
        {
            throw new NullPointerException ( "ERROR Cannot sort"
                                             + " elements for "
                                             + ClassName.of ( this.getClass () )
                                             + " in container = "
                                             + container
                                             + ", order = "
                                             + order );
        }

        Arrays.sort ( container, order );
        return container;
    }
}
