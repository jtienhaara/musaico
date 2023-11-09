package musaico.foundation.domains.elements;

import java.io.Serializable;

import java.util.Array;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;


import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.iterators.IndexedIterator;


/**
 * <p>
 * Wrapper presenting a sub-range of other Elements.
 * </p>
 *
 *
 * <p>
 * In Java every Elements must be Serializable in order to
 * play nicely across RMI.  However users of the Elements
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
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
 * @see musaico.foundation.term.countable.MODULE#COPYRIGHT
 * @see musaico.foundation.term.countable.MODULE#LICENSE
 */
public class RangeOfElements<CONTAINER, ELEMENT>
    extends AbstractElements<CONTAINER, ELEMENT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * An Iterator which steps over RangeOfElements's, and which
     * does not allow elements to be removed.
     * </p>
     *
     * <p>
     * NOT thread-safe.  Do not access an ArrayIterator from multiple
     * threads without providing external synchronization.
     * </p>
     */
    public static class RangeIterator<RANGE_ELEMENT extends Object>
        implements IndexedIterator<RANGE_ELEMENT>, Serializable
    {
        private static final long serialVersionUID =
            RangeOfElements.serialVersionUID;

        // The RangeOfElements to iterate over.
        private final RangeOfElements<RANGE_ELEMENT> range;

        // MUTABLE:
        // The next index to step to, or Elements.NONE
        // if we're out of elements.
        private long nextIndex = 0L;

        // MUTABLE:
        // The previous index we stepped to, or Elements.NONE
        // if we're out of elements or have not yet started stepping.
        private long lastIndex = Elements.NONE;

        /**
         * <p>
         * Creates a new RangeOfElements.RangeIterator.
         * </p>
         *
         * @param range The RangeOfElements to iterate over.
         *              Must not be null.
         *
         * @throws NullPointerException If the specified RangeOfElements
         *                              is null.
         */
        public RangeIterator (
                RangeOfElements<RANGE_ELEMENT> range
                )
        {
            if ( range == null )
            {
                throw new NullPointerException ( "ERROR Cannot create"
                                                 ClassName.of ( this.getClass () )
                                                 + " with null"
                                                 +" RangeOfElements" );
            }

            this.range = range;
            if ( this.range.isEmpty () )
            {
                this.nextIndex = Elements.NONE;
            }
            else
            {
                this.nextIndex = 0L;
            }
            this.lastIndex = Elements.NONE;
        }


        /**
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public final boolean hasNext ()
        {
            if ( this.nextIndex == -1L )
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        /**
         * @see musaico.foundation.domains.iterators.IndexedIterator#index()
         */
        public final long index ()
        {
            return (long) this.lastIndex;
        }

        /**
         * @see java.util.Iterator#next()
         */
        @Override
        public final RANGE_ELEMENT next ()
            throws IndexOutOfBoundsException
        {
            if ( this.nextIndex == Elements.NONE )
            {
                throw new IndexOutOfBoundsException ( "ERROR no more elements"
                                                      + " to iterate over: "
                                                      + this.range );
            }

            this.lastIndex = this.nextIndex;

            final RANGE_ELEMENT element = this.range.at ( this.nextIndex );

            this.nextIndex ++;

            return element;
        }

        /**
         * @see java.util.Iterator#remove()
         */
        @Override
        public final void remove ()
            throws UnsupportedOperationException
        {
            throw new UnsupportedOperationException ( ClassName.of ( this.getClass () )
                                                      + ".remove () not supported" );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            final String next_index = " [ " + this.nextIndex + " ]";

            return ClassName.of ( this.getClass () )
                + " "
                + this.range
                + next_index;
        }
    }


    // The parent Elements, of which these are a sub-range.
    private final Elements<ELEMENT> parent;

    // The start index of the range.
    private final long start;

    // The end index of the range.
    private final long end;


    /**
     * <p>
     * Creates a new RangeOfElements with the same Elements.Flags
     * as its parent Elements.
     * </p>
     *
     * @param parent The parent Elements, of which these are a sub-range.
     *               Must not be null.
     *
     * @param start The start index, which will be clamped to the parent's
     *              first and last indices.  Can be any number.
     *
     * @param end The end index, which will be clamped to the parent's
     *              first and last indices.  Can be any number.
     */
    public RangeOfElements (
            Elements<ELEMENT> parent,
            long start,
            long end
            )
        throws NullPointerException
    {
        this ( parent,
               start,
               end,
               parent == null // flags
                   ? null
                   : parent.flags () );
    }


    /**
     * <p>
     * Creates a new RangeOfElements.
     * </p>
     *
     * @param parent The parent Elements, of which these are a sub-range.
     *               Must not be null.
     *
     * @param start The start index, which will be clamped to the parent's
     *              first and last indices.  Can be any number.
     *
     * @param end The end index, which will be clamped to the parent's
     *              first and last indices.  Can be any number.
     *
     * @param flags The Elements.Flags, specifying whether these
     *              Elements are mutable or immutable, fixed length
     *              or variable length, and so on.  Must not be null.
     *
     * @throws NullPointerException If the specified parent is null.
     */
    public RangeOfElements (
            Elements<ELEMENT> parent,
            long start,
            long end,
            Elements.Flags flags
            )
        throws NullPointerException
    {
        super ( parent == null // container
                    ? null
                    : parent.container (),
                parent == null // element_class
                    ? null
                    : parent.elementClass (),
                flags );

        if ( parent == null )
        {
            throw new NullPointerException ( "ERROR Cannot create "
                                             + ClassName.of ( this.getClass () )
                                             + " with parent = null"
                                             + ", start = " + start
                                             + ", end = " + end );
        }

        this.parent = parent;

        this.start = start;
        this.end = end;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#array()
     */
    @Override
    public final ELEMENT [] array ()
        // Never throws ElementsLengthException.
    {
        final long parent_start = this.parentStart ();
        if ( parent_start == Elements.NONE )
        {
            return this.none ();
        }

        final long parent_end = this.parentEnd ();
        if ( parent_end == Elements.NONE )
        {
            return this.none ();
        }

        final long maybe_length;
        final long direction;
        if ( parent_end >= parent_start )
        {
            maybe_length = parent_end - parent_start + 1L;
            direction = 1L;
        }
        else // parent_start > parent_end
        {
            maybe_length = parent_start - parent_end + 1L;
            direction = -1L;
        }

        if ( maybe_length < 0L
             || maybe_length > (long) Integer.MAX_VALUE )
        {
            throw new ElementsLengthException ( this, maybe_length );
        }

        final Class<ELEMENT> element_class = this.elementClass ();
        final int length = (int) maybe_length;
        final ELEMENT [] new_array = (ELEMENT [])
            Array.newInstance ( element_class, length );

        long parent_index = parent_start;
        for ( int e = 0; e < new_array.length; e ++ )
        {
            final ELEMENT element = this.parent.at ( parent_index );
            new_array [ e ] = element;
            parent_index += direction;
        }

        return new_array;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#at(long)
     */
    @Override
    public final ELEMENT [] at (
        long index
        )
    {
        final long parent_index = this.clampToParent ( index );
        if ( parent_index == Elements.NONE )
        {
            return this.none ();
        }

        final ELEMENT [] singleton = (ELEMENT [])
            Array.newInstance ( this.elementsClass (),
                                1 );

        singleton [ 0 ] = this.parent.at ( parent_index );

        return singleton;
    }


    /**
     * <p>
     * Clamps the specified index to this RangeOfElements's start and end
     * indices, each of which can be either absolute (non-negative)
     * or relative to the end of the parent indices (negative).
     * </p>
     */
    protected final long clampToParent (
            long index
            )
    {
        final long parent_length = this.parent.length ();
        final long parent_start = this.parentStart ();
        if ( parent_start == Elements.NONE )
        {
            return Elements.NONE;
        }

        final long parent_end =
            AbstractElements.clamp ( Element.LAST,
                                     this.start,
                                     this.end,
                                     parent_length );
        if ( parent_end == Elements.NONE )
        {
            return Elements.NONE;
        }

        final long clamped_index =
            AbstractElements.clamp ( index,
                                     parent_start,
                                     parent_end,
                                     parent_length );
        if ( clamped_index == Elements.NONE )
        {
            return Elements.NONE;
        }

        return clamped_index;
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
     * @see musaico.foundation.domains.elements.Elements#direction()
     */
    @Override
    public final long direction ()
    {
        final long parent_start = this.parentStart ();
        final long parent_end = this.parentEnd ();
        if ( parent_start <= parent_end )
        {
            return 1L;
        }
        else
        {
            return -1L;
        }
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#duplicate(musaico.foundation.domains.elements.Elements.Flags)
     */
    @Override
    public abstract Elements<CONTAINER, ELEMENT> duplicate (
            Elements.Flags flags
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

        final Elements<CONTAINER, ELEMENT> duplicate_parent =
            this.parent.duplicate ( flags );
        final RangeOfElements<CONTAINER, ELEMENT> duplicate =
            new RangeOfElements<CONTAINER, ELEMENT> ( duplicate_parent,
                                                      this.start,
                                                      this.end,
                                                      flags );
        return duplicate;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#end()
     */
    @Override
    public final long end ()
    {
        return this.end;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    protected final boolean equalsElements (
            CONTAINER this_container,
            AbstractElements<CONTAINER, ELEMENT> that,
            CONTAINER that_container
            )
    {
        if ( this_container == null
             || that_container == null )
        {
            // Corrupt.
            return false;
        }
        else if ( that.getClass () != this.getClass () )
        {
            // Corrupt.
            return false;
        }

        final long this_length = this.length ();
        final long that_length = that.length ();
        if ( that_length != this_length )
        {
            return false;
        }

        long this_index = this.parentStart ();
        long that_index = that.parentStart ();
        final long this_end = this.parentEnd ();
        final long that_end = that.parentEnd ();
        final long this_direction;
        if ( this_index <= this_end )
        {
            this_direction = 1L;
        }
        else
        {
            this_direction = -1L;
        }
        final long that_direction;
        if ( that_index <= that_end )
        {
            that_direction = 1L;
        }
        else
        {
            that_direction = -1L;
        }

        for ( long e = 0L; e < this_length && e < that_length; e ++ )
        {
            final ELEMENT [] this_element = this.parent.at ( this_index );
            final ELEMENT [] that_element = that.parent.at ( that_index );

            if ( this_element.length != that_element.length )
            {
                return false;
            }
            else if ( this_element [ 0 ] == that_element [ 0 ] )
            {
                continue;
            }
            else if ( this_element [ 0 ] == null )
            {
                if ( that_element [ 0 ] != null )
                {
                    return false;
                }
            }
            else if ( that_element [ 0 ] == null )
            {
                return false;
            }
            else if ( ! this_element [ 0 ].equals ( that_element [ 0 ] ) )
            {
                return false;
            }

            this_index += this_direction;
            that_index += that_direction;
        }

        // The length is equal, and all of the elements are equal.
        return true;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#filter(musaico.foundation.filter.Filter)
     */
    @Override
    public final Elements<CONTAINER, ELEMENT> filter (
            Filter<ELEMENT> filter
            )
        throws NullPointerException
    {
        if ( filter == null )
        {
            throw new NullPointerException ( "ERROR: "
                                             + this
                                             + " cannot filter elements with"
                                             + " filter = null" );
        }

        final List<ELEMENT> filtered_list = new ArrayList<ELEMENT> ();
        boolean is_any_filtered_out = false;
        for ( ELEMENT element : this )
        {
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
            return this;
        }

        // Create new ArrayElements.
        final Class<ELEMENT> element_class = this.elementClass ();
        final ELEMENT [] filtered_container = (ELEMENT [])
            Array.newInstance ( element_class, filtered_list.size () );
        for ( int e = 0; e < new_array.length; e ++ )
        {
            final ELEMENT element = filtered_list.get ( e );
            new_array [ e ] = element;
        }

        final ArrayElements<ELEMENT> filtered_elements =
            new ArrayElements<ELEMENT> ( filtered_array,
                                         element_class,
                                         this.flags () );

        return filtered_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#from(musaico.foundation.domains.elements.Elements, musaico.foundation.domains.elements.Elements.Flags)
     */
    @Override
    public final Elements<CONTAINER, ELEMENT> from (
            Elements<?, ELEMENT> that,
            Elements.Flags flags
            )
        throws NullPointerException,
               ElementsLengthException,
               ElementsFlagsException
    {
        if ( that == null
             || flags == null )
        {
            throw new NullPointerException ( "ERROR: Can't create "
                                             + ClassName.of ( this.getClass () )
                                             + " from  Elements "
                                             + that
                                             + " with Flags "
                                             + flags );
        }
        else if ( ! flags.equals ( that.flags () ) )
        {
            throw new ElementsFlagsException ( this, flags );
        }

        final Elements<CONTAINER, ELEMENT> new_elements =
            new RangeOfElements<ELEMENT> ( that,
                                           Elements.FIRST,
                                           Elements.LAST,
                                           flags );

        return new_elements;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        final int parent_hash_code = this.parent.hashCode ();

        final int hash_code =
            parent_hash_code
            + 37 * (int) ( this.start % (long) Integer.MAX_VALUE )
            + 29 * (int) ( this.end % (long) Integer.MAX_VALUE );

        return hash_code;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#isEmpty()
     */
    @Override
    public final boolean isEmpty ()
    {
        if ( this.parent.isEmpty () )
        {
            return true;
        }

        if ( this.parent.has ( this.start ) )
        {
            return true;
        }
        else
        {
            return false;
        }

        if ( this.parent.has ( this.end ) )
        {
            return true;
        }
        else
        {
            return false;
        }

        final long parent_start = this.parentStart ();
        final long parent_end = this.parentEnd ();
        final long direction;
        if ( parent_start <= parent_end )
        {
            direction = 1L;
        }
        else
        {
            direction = -1L;
        }
        for ( long parent_index = parent_start + direction;
              parent_index != parent_end;
              parent_index += direction )
        {
            if ( this.parent.has ( parent_index ) )
            {
                return true;
            }
        }

        return false;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#isSingleton()
     */
    @Override
    public final boolean isSingleton ()
    {
        final long parent_start = this.parentStart ();
        if ( parent_start == Elements.NONE )
        {
            return false;
        }

        final long parent_end = this.parentEnd ();
        if ( parent_start == parent_end )
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
        final RangeOfElements.RangeIterator<ELEMENT> iterator =
            new RangeOfElements.RangeIterator<ELEMENT> ( this );
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#length()
     */
    @Override
    public final long length ()
    {
        final long parent_start = this.parentStart ();
        final long parent_end = this.parentEnd ();

        if ( parent_start >= 0L
             && parent_end >= 0L )
        {
            final long length = parent_end - parent_start + 1L;
            return length;
        }
        else if ( parent.length () == 0L )
        {
            return 0L;
        }

        // Either or both parent_start and parent_end is/are out of bounds,
        // possibly with in-bounds indices in between.
        final long direction;
        final long !!!!!!!!!;
        if ( this.start >= 0L )
        {
            if ( this.end >= this.start )
            {
                direction = 1L;
            }
            else if ( this.end < 0L )
            {
                if ( parent_end == Elements.NONE )
                {
                    //
        if ( parent_start <= parent_end )
        {
            direction = 1L;
        }
        else
        {
            direction = -1L;
        }
        long length = 0L;
        long parent_index = parent_start;
        boolean is_counting = true;
        while ( is_counting )
        {
            if ( this.parent.has ( parent_index ) )
            {
                length ++;
            }

            if ( parent_index == parent_end )
            {
                is_counting = false;
            }
            else
            {
                parent_index += direction;
            }
        }

        return length;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#list()
     */
    @Override
    public final List<ELEMENT> list ()
        // Never throws ElementsLengthException.
    {
        final long parent_start = this.parentStart ();
        if ( parent_start == Elements.NONE )
        {
            return this.none ();
        }

        final long parent_end = this.parentEnd ();
        if ( parent_end == Elements.NONE )
        {
            return this.none ();
        }

        final long maybe_length;
        final long direction;
        if ( parent_end >= parent_start )
        {
            maybe_length = parent_end - parent_start + 1L;
            direction = 1L;
        }
        else // parent_start > parent_end
        {
            maybe_length = parent_start - parent_end + 1L;
            direction = -1L;
        }

        if ( maybe_length < 0L
             || maybe_length > (long) Integer.MAX_VALUE )
        {
            throw new ElementsLengthException ( this, maybe_length );
        }

        final int length = (int) maybe_length;
        final List<ELEMENT> new_list = new ArrayList<ELEMENT> ( length );

        long parent_index = parent_start;
        for ( int e = 0; e < length; e ++ )
        {
            final ELEMENT element = this.parent.at ( parent_index );
            new_list.add ( element );
            parent_index += direction;
        }

        return new_list;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#set()
     */
    @Override
    public final LinkedHashSet<ELEMENT> set ()
        // Never throws ElementsLengthException.
    {
        final long parent_start = this.parentStart ();
        if ( parent_start == Elements.NONE )
        {
            return this.none ();
        }

        final long parent_end = this.parentEnd ();
        if ( parent_end == Elements.NONE )
        {
            return this.none ();
        }

        final long maybe_length;
        final long direction;
        if ( parent_end >= parent_start )
        {
            maybe_length = parent_end - parent_start + 1L;
            direction = 1L;
        }
        else // parent_start > parent_end
        {
            maybe_length = parent_start - parent_end + 1L;
            direction = -1L;
        }

        if ( maybe_length < 0L
             || maybe_length > (long) Integer.MAX_VALUE )
        {
            throw new ElementsLengthException ( this, maybe_length );
        }

        final int length = (int) maybe_length;
        final LinkedHashSet<ELEMENT> new_set =
            new LinkedHashSet<ELEMENT> ( length );

        long parent_index = parent_start;
        for ( int e = 0; e < length; e ++ )
        {
            final ELEMENT element = this.parent.at ( parent_index );
            new_set.add ( element );
            parent_index += direction;
        }

        return new_set;
    }


    /**
     * @return The parent Elements, of which this RangeOfElements
     *         is a sub-sequence.  Never null.
     */
    public final Elements<ELEMENT> parent ()
    {
        return this.parent;
    }


    /**
     * @return The end index, in the parent's coordinate system.
     *         For example, if this range ends at 3L, then 3L
     *         will be returned.  However if this range ends
     *         at <code> Elements.LAST + 3L </code> and if the parent
     *         has 17 elements, then 14L will be returned (the 4th last
     *         element index in the parent).  Always 0L or greater
     *         unless this range's end is outside the bounds of
     *         the parent elements, in which case Elements.NONE is returned.
     */
    public long parentEnd ()
    {
        final long parent_length = this.parent.length ();
        final long parent_end =
            AbstractElements.clamp ( Elements.LAST,
                                     this.start,
                                     this.end,
                                     parent_length );
        return parent_end;
    }


    /**
     * @return The start index, in the parent's coordinate system.
     *         For example, if this range starts at 3L, then 3L
     *         will be returned.  However if this range starts
     *         at <code> Elements.LAST + 3L </code> and if the parent
     *         has 17 elements, then 14L will be returned (the 4th last
     *         element index in the parent).  Always 0L or greater
     *         unless this range's start is outside the bounds of
     *         the parent elements, in which case Elements.NONE is returned.
     */
    public long parentStart ()
    {
        final long parent_length = this.parent.length ();
        final long parent_start =
            AbstractElements.clamp ( 0L,
                                     this.start,
                                     this.end,
                                     parent_length );
        return parent_start;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#sort(java.util.Comparator)
     */
    @Override
    public final Elements<CONTAINER, ELEMENT> sort (
        throws NullPointerException
    {
        final long parent_start = this.parentStart ();
        final long parent_end = this.parentEnd ();
        final long direction;
        final long length;
        if ( parent_start <= parent_end )
        {
            direction = 1L;
            length = parent_end - parent_start + 1L;
        }
        else // parent_start > parent_end
        {
            direction = -1L;
            length = parent_start - parent_end + 1L;
        }

        if ( length > (long) Integer.MAX_VALUE )
        {
            !!! Not gonna work;
            Elements<ELEMENT> big_sorted_parent =
                this.parent.duplicate ( this.parent.flags ().makeMutable () );
            big_sorted_parent = big_sorted_parent.sort ( comparator );
            final Elements<ELEMENT> big_sorted_range =
                big_parent.range ( !!!parent_start, parent_end );
            return big_sorted_range;
            !!!;
        }

        final long [] sorted_indices = new long [ (int) length ];
        long parent_index = parent_start;
        for ( int e = 0L; e < (int) length; e ++ )
        {
            sorted_indices [ e ] = parent_index;
            parent_index += direction;
        }

        final Comparator<int> index_comparator =
            new ElementsIndexComparator ( this.parent, comparator );
    Arrays.sort ( sorted_indices, index_comparator );
    !!!!!;
        final Elements.Flags this_flags = this.flags ();
        final Elements.Flags parent_flags = this.parent.flags ();
        if ( this_flags.isImmutable () )
        {
            if ( parent_flags.isImmutable () )
            {
                // Both this and this.parent are immutable.
                // We can safely call t

                // Both this and this.parent are mutable,
                // but we
        final ELEMENT [] array = this.container ();
        if ( ! this.flags ().isImmutable () )
        {
            // Mutable.
            // Sort in place.
            Arrays.sort ( array, comparator );
            return this;
        }

        final ELEMENT [] sorted_array = new ELEMENT [ array.length ];
        System.arraycopy ( array, 0,
                           sorted_array, 0, array.length );

        Arrays.sort ( sorted_array, comparator );
        final Elements<ELEMENT [], ELEMENT> sorted_elements =
            new ArrayElements<ELEMENT> ( sorted_array,
                                         this.elementClass (),
                                         this.flags () );

        return sorted_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#start()
     */
    @Override
    public final long start ()
    {
        return 0L;
    }
}
