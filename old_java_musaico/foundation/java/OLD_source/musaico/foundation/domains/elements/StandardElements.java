package musaico.foundation.domains.elements;

import java.io.Serializable;

import java.lang.reflect.Array;


import musaico.foundation.domains.ClassName;


/**
 * <p>
 * Boilerplate code for most Elements implementations.
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
public class StandardElements<CONTAINER extends Object, INDEX extends Object, ELEMENT extends Object>
    implements Elements<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The container represented by these Elements.
    private final CONTAINER container;

    // The Extractor that knows how to deal with the container.
    // It can extract individual elements, iterate over them all,
    // filter them, and so on.  We rely on it to do the detail work.
    private final Extractor<CONTAINER, INDEX, ELEMENT> extractor;

    // The Class of each individual element in these Elements.
    private final Class<ELEMENT> elementClass;

    // The Flags for these Elements, including immutability and fixed length.
    private final Elements.Flags flags;

    // The index of the first element from the container.
    // Can be offset from the beginning of the elements (0 or greater)
    // or offset from the end of the elements (Elements.LAST or greater).
    private final long start;

    // The index of the last element from the container.
    // Can be offset from the beginning of the elements (0 or greater)
    // or offset from the end of the elements (Elements.LAST or greater).
    private final long end;


    /**
     * <p>
     * Creates a new sequence of StandardElements covering all elements
     * of the specified container with immutable, fixed length flags set.
     * </p>
     *
     * <p>
     * Equivalent to constructing a <code> new StandardElements (
     * container, extractor, element_class,
     * Elements.Flags.IMMUTABLE_FIXED_LENGTH,
     * Elements.FIRST, Elements.LAST ) </code>.
     * </p>
     *
     * @param container The container to be represented by these
     *                  StandardElements.  Can contain null elements.
     *                  Contents can be modified by the caller only
     *                  if flags.isImmutable () is false, though in that case
     *                  the caller must ensure concurrency protection
     *                  is in place around these Elements.
     *                  Must not be null.
     *
     * @param extractor The Extractor that knows how to deal with
     *                  the specified container.  It can extract
     *                  individual elements, iterate over them all,
     *                  filter them, and so on.  Must not be null.
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @throws NullPointerException If the specified container or
     *                              extractor is/are null.
     */
    public StandardElements (
            CONTAINER container,
            Extractor<CONTAINER, INDEX, ELEMENT> extractor,
            Class<ELEMENT> element_class,
            Elements.Flags flags
            )
        throws NullPointerException
    {
        this ( container,
               extractor,
               Elements.Flags.IMMUTABLE_FIXED_LENGTH,
               Elements.FIRST,
               Elements.LAST );
    }


    /**
     * <p>
     * Creates a new sequence of StandardElements covering all elements
     * of the specified container.
     * </p>
     *
     * <p>
     * Equivalent to constructing a <code> new StandardElements (
     * container, extractor, element_class, flags,
     * Elements.FIRST, Elements.LAST ) </code>.
     * </p>
     *
     * @param container The container to be represented by these
     *                  StandardElements.  Can contain null elements.
     *                  Contents can be modified by the caller only
     *                  if flags.isImmutable () is false, though in that case
     *                  the caller must ensure concurrency protection
     *                  is in place around these Elements.
     *                  Must not be null.
     *
     * @param extractor The Extractor that knows how to deal with
     *                  the specified container.  It can extract
     *                  individual elements, iterate over them all,
     *                  filter them, and so on.  Must not be null.
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param flags The Elements.Flags, specifying whether these
     *              Elements are mutable or immutable, fixed length
     *              or variable length, and so on.  Must not be null.
     *
     * @throws NullPointerException If the specified container or
     *                              extractor or flags is/are null.
     */
    public StandardElements (
            CONTAINER container,
            Extractor<CONTAINER, INDEX, ELEMENT> extractor,
            Class<ELEMENT> element_class,
            Elements.Flags flags
            )
        throws NullPointerException
    {
        this ( container,
               extractor,
               flags,
               Elements.FIRST,
               Elements.LAST );
    }


    /**
     * <p>
     * Creates a new sequence of StandardElements.
     * </p>
     *
     * @param container The container to be represented by these
     *                  StandardElements.  Can contain null elements.
     *                  Contents can be modified by the caller only
     *                  if flags.isImmutable () is false, though in that case
     *                  the caller must ensure concurrency protection
     *                  is in place around these Elements.
     *                  Must not be null.
     *
     * @param extractor The Extractor that knows how to deal with
     *                  the specified container.  It can extract
     *                  individual elements, iterate over them all,
     *                  filter them, and so on.  Must not be null.
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param flags The Elements.Flags, specifying whether these
     *              Elements are mutable or immutable, fixed length
     *              or variable length, and so on.  Must not be null.
     *
     * @param start The index of the first element from the container.
     *              Can be offset from the beginning of the elements
     *              (0 or greater)
     *              or offset from the end of the elements
     *              (Elements.LAST or greater).
     *
     * @param end The index of the last element from the container.
     *            Can be offset from the beginning of the elements
     *            (0 or greater),
     *            or offset from the end of the elements
     *           (Elements.LAST or greater).
     *
     * @throws NullPointerException If the specified container or
     *                              extractor or flags is/are null.
     */
    public StandardElements (
            CONTAINER container,
            Extractor<CONTAINER, INDEX, ELEMENT> extractor,
            Class<ELEMENT> element_class,
            Elements.Flags flags,
            long start,
            long end
            )
        throws NullPointerException
    {
        if ( container == null
             || extractor == null
             || flags == null )
        {
            throw new NullPointerException ( "ERROR Cannot create "
                                             + ClassName.of ( this.getClass () )
                                             + " with"
                                             + " container = "
                                             + container
                                             + ", extractor = "
                                             + extractor
                                             + ", flags = "
                                             + flags );
        }

        this.container = container;
        this.extractor = extractor;
        this.flags = flags;
        this.start = start;
        this.end = end;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#array()
     */
    @Override
    @SuppressWarnings("unchecked") // Checked with reflection.
    public final ELEMENT [] array ()
        throws ElementsLengthException
    {
        final ArrayExtractor<ELEMENT> array_extractor =
            new ArrayExtractor<ELEMENT> ( this.elementClass );

        // Return the container as-is, if it's already an array.
        // Otherwise a new array instance will be created from
        // our container.
        // Can throw ElementsLengthException.
        final ELEMENT [] array =
            array_extractor.extract ( this, false );
        !!! this.start, this.end !!! ;

        return array;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#at(long)
     */
    @Override
    public final ELEMENT [] at (
        long index
        )
    {
        final long clamped_index = this.clamp ( index );
        if ( clamped_index < 0L )
        {
            return this.none ();
        }

        final ELEMENT [] singleton = (ELEMENT [])
            Array.newInstance ( this.elementsClass (),
                                1 );

        singleton [ 0 ] = this.extractor.at ( this.container,
                                              clamped_index );

        return singleton;
    }


    /**
     * <p>
     * Clamps the specified index to the range 0L - length (),
     * or returns Elements.NONE if the specified index is out of bounds.
     * </p>
     *
     * <p>
     * For example, if Elements.FIRST is passed, then start ()
     * will be returned.  Or if Elements.LAST is passed, then end ()
     * will be returned.  And so on.
     * </p>
     *
     * @param index The index to clamp.  Can be any number.
     *
     * @return The clamped index, either between start () and end (),
     *         inclusive, or Elements.NONE if the specified index
     *         is out of bounds.
     */
    protected final long clamp (
            long index
            )
    {
        return this.clamp ( index,         // index
                            this.start (), // start
                            this.end () ); // end
    }


    /**
     * @param index The index to clamp.  Can be any number except
     *              Elements.AFTER_LAST or Elements.NONE,
     *              in either of which case Elements.NONE
     *              will always be returned.
     * @see musaico.foundation.domains.Elements#FIRST
     * @see musaico.foundation.domains.Elements#FORWARD
     * @see musaico.foundation.domains.Elements#FROM_START
     * @see musaico.foundation.domains.Elements#AFTER_LAST
     * @see musaico.foundation.domains.Elements#LAST
     * @see musaico.foundation.domains.Elements#BACKWARD
     * @see musaico.foundation.domains.Elements#FROM_END
     *
     * @param start The index to start clamping from.  This is an
     *              offset from the <code> start () </code>
     *              of these Elements.  It can be greater than
     *              the end, in which case the range in which a valid
     *              clamped index will fall is backward from the direction
     *              of these Elements (though it makes no difference
     *              in the end to the one clamped index).  Must be
     *              greater than or equal to 0L, and less than the length
     *              of these Elements, otherwise Elements.NONE
     *              will be returned.
     *
     * @param end The index to end clamping at.  This is an
     *             offset from the <code> start () </code>
     *             of these Elements.  It can be less than
     *             the start, in which case the range in which a valid
     *             clamped index will fall is backward from the direction
     *             of these Elements (though it makes no difference
     *             in the end to the one clamped index).  Must be
     *             greater than or equal to 0L, and less than the length
     *             of these Elements, otherwise Elements.NONE
     *             will be returned.
     *
     * @return The specified index, converted if it is offset from
     *         LAST/BACKWARD, clamped to the specified range
     *         (which may be backward), and clamped to these Elements'
     *         range (which may also be backward).  Always greater than
     *         or equal to <code> start () </code> and less than
     *         or equal to <code> end () </code>, and always greater than
     *         or equal to 0L, UNLESS the index is out of clamping range,
     *         in which case Elements.NONE will be returned.
     */
    protected final long clamp (
            long index,
            long start,
            long end
            )
    {
        if ( start < 0L
             || end < 0L
             || index == Elements.AFTER_LAST )
        {
            return Elements.NONE;
        }

        final long length = this.length ();
        final long offset =
            StandardElements.clamp ( index,
                                     start,
                                     end,
                                     length );
        final long clamped;
        if ( offset <= Elements.NONE )
        {
            clamped = offset;
        }
        else
        {
            final long absolute_start = this.start ();
            if ( this.direction () > 0L )
            {
                clamped = absolute_start + offset;
            }
            else
            {
                clamped = absolute_start - offset;
            }
        }

        return clamped;
    }


    /**
     * @return The specified index, converted if offset from LAST/BACKWARD,
     *         and clamped to the specified range.
     */
    protected static final long clamp (
            long index,
            long start,
            long end,
            long out_of_bounds
            )
    {
        if ( end >= out_of_bounds )
        {
            end = out_of_bounds - 1L;
        }

        if ( index == Elements.AFTER_LAST
             || start < 0L
             || end < 0L
             || start >= out_of_bounds )
        {
            return Elements.NONE;
        }

        final long length;
        if ( end >= start )
        {
            length = end - start + 1L;
            if ( index >= Elements.FIRST
                 && index < length )
            {
                return start + index;
            }
            else if ( index >= length )
            {
                return Elements.NONE;
            }
            // Index < -1L, a backward-counting index.
            else if ( ( index - Elements.BACKWARD ) >= length )
            {
                return Elements.NONE;
            }

            final long reversed_index =
                Elements.BACKWARD
                - index
                + end;

            return reversed_index;
        }
        else // start > end )
        {
            // Descending: start, start-1, start-2, ..., end+2, end+1, end.
            length = start - end + 1L;
            if ( index >= Elements.FIRST
                 && index < length )
            {
                return start - index;
            }
            else if ( index >= length )
            {
                return Elements.NONE;
            }
            // Index < -1L, a backward-counting index.
            else if ( ( index - Elements.BACKWARD ) >= length )
            {
                return Elements.NONE;
            }

            final long reversed_index =
                index
                - Elements.BACKWARD
                + end;

            return reversed_index;
        }
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#collection()
     */
    @Override
    public final Collection<ELEMENT> collection ()
        // Never throws ElementsLengthException.
        throws ElementsLengthException;
    {
        final CollectionExtractor<ELEMENT> collection_extractor =
            new CollectionExtractor<ELEMENT> ( this.elementClass );

        // Return the container as-is, if it's already a Collection.
        // Otherwise a new Collection (such as a List) will be created from
        // our container.
        // Can throw ElementsLengthException.
        final Collection<ELEMENT> collection =
            collection_extractor.extract ( this, false );

        return collection;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#container()
     */
    @Override
    public final CONTAINER container ()
    {
        return this.container;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#direction()
     */
    @Override
    public final long direction ()
    {
        !!!;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#duplicate(musaico.foundation.domains.elements.Elements.Flags)
     */
    @Override
    public abstract Elements<ELEMENT [], ELEMENT> duplicate (
            Elements.Flags flags
            )
        throws NullPointerException,
               ElementsFlagsException
    {
        !!!;
        if ( flags == null )
        {
            throw new NullPointerException ( "ERROR "
                                             + this
                                             + " cannot be duplicated"
                                             + " with null flags" );
        }
        else if ( ! flags.isFixedLength () )
        {
            // ArrayElements can't support variable length Elements.
            throw new ElementsFlagsException ( this, flags );
        }

        final Class<ELEMENT> element_class = this.elementClass ();

        final ELEMENT [] array = this.container ();
        final ELEMENT [] new_array = (ELEMENT [])
            Array.newInstance ( element_class, array.length );

        final ArrayElements<ELEMENT> new_elements =
            new ArrayElements<ELEMENT> ( new_array,
                                         element_class,
                                         flags );

        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#elementClass()
     */
    @Override
    public final Class<ELEMENT> elementClass ()
    {
        return this.elementClass;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#end()
     */
    @Override
    public final long end ()
    {
        !!!;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast inside try...catch.
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

        final StandardElements<?, ?> unsure_that =
            (StandardElements<?, ?>) object;

        if ( this.elementClass != unsure_that.elementClass )
        {
            return false;
        }
        else if ( this.container == unsure_that.container )
        {
            return true;
        }
        else if ( this.isEmpty () != unsure_that.isEmpty () )
        {
            return false;
        }
        else if ( this.isSingleton () != unsure_that.isSingleton () )
        {
            return false;
        }

        final StandardElements<CONTAINER, ELEMENT> that;
        try
        {
            that = (StandardElements<CONTAINER, ELEMENT>) unsure_that;
        }
        catch (ClassCastException e)
        {
            return false;
        }

        final CONTAINER that_container = that.container;
        final boolean is_equal = this.equalsElements ( this.container,
                                                       that,
                                                       that_container );

        return is_equal;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#filter(musaico.foundation.filter.Filter)
     */
    @Override
    public final Elements<ELEMENT [], ELEMENT> filter (
            Filter<ELEMENT> filter
            )
        throws NullPointerException
    {
        !!!;
        if ( filter == null )
        {
            throw new NullPointerException ( "ERROR: "
                                             + this
                                             + " cannot filter elements with"
                                             + " filter = null" );
        }
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#flags()
     */
    @Override
    public final Elements.Flags flags ()
    {
        return this.flags;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#from(musaico.foundation.domains.elements.Elements, musaico.foundation.domains.elements.Elements.Flags)
     */
    @Override
    public final Elements<ELEMENT [], ELEMENT> from (
            Elements<?, ELEMENT> that,
            Elements.Flags flags
            )
        throws NullPointerException,
               ElementsLengthException,
               ElementsFlagsException
    {
        !!!;
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
        else if ( ! flags.isFixedLength () )
        {
            throw new ElementsFlagsException ( this, flags );
        }

        final long length = that.length ();
        if ( length < 0L
             || length > (long) Integer.MAX_VALUE )
        {
            throw new ElementsLengthException ( this,
                                                length );
        }

        final Class<ELEMENT> element_class = this.elementClass ();

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

        int e = 0;
        for ( ELEMENT element : that )
        {
            new_array [ e ] = element;
            e ++;
        }

        final Elements<ELEMENT [], ELEMENT> new_elements =
            new ArrayElements<ELEMENT> ( new_array,
                                         element_class,
                                         flags );

        return new_elements;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#has(long)
     */
    @Override
    public final boolean has (
            long index
            )
    {
        final long clamped_index = this.clamp ( index );
        if ( clamped_index < 0L )
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        !!!;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#isEmpty()
     */
    @Override
    public final boolean isEmpty ()
    {
        !!!;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#isSingleton()
     */
    @Override
    public final boolean isSingleton ()
    {
        !!!;
    }


    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public final Iterator<ELEMENT> iterator ()
    {
        !!!;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#length()
     */
    @Override
    public final long length ()
    {
        !!!;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#list()
     */
    @Override
    public final List<ELEMENT> list ()
        throws ElementsLengthException
    {
        final ListExtractor<ELEMENT> list_extractor =
            new ListExtractor<ELEMENT> ( this.elementClass );

        // Return the container as-is, if it's already a List.
        // Otherwise a new List will be created from our container.
        // Can throw ElementsLengthException.
        final List<ELEMENT> list =
            list_extractor.extract ( this, false );

        return list;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#none()
     */
    @Override
    public final ELEMENT [] none ()
    {
        final Class<ELEMENT> element_class = this.elementClass ();
        final ELEMENT [] none = (ELEMENT [])
            Array.newInstance ( element_class, 0 );
        return none;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#range(long, long)
     */
    @Override
    public final Elements<ELEMENT> range (
            long start,
            long end
            )
    {
        return null;
        // !!! return new RangeOfElements<ELEMENT> ( this, start, end );
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#set()
     */
    @Override
    public final LinkedHashSet<ELEMENT> set ()
        throws ElementsLengthException
    {
        final SetExtractor<ELEMENT> set_extractor =
            new SetExtractor<ELEMENT> ( this.elementClass );

        // Return the container as-is, if it's already a LinkedHashSet.
        // Otherwise a new LinkedHashSet will be created from our container.
        // Can throw ElementsLengthException.
        final Set<ELEMENT> set =
            set_extractor.extract ( this, false );

        return set;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#singleton()
     */
    @Override
    public final ELEMENT singleton ()
        throws ElementsLengthException
    {
        if ( ! this.isSingleton () )
        {
            throw new ElementsLengthException ( this );
        }

        final ELEMENT singleton = this.at ( Elements.FIRST );

        return singleton;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#sort(java.util.Comparator)
     */
    @Override
    public final Elements<ELEMENT [], ELEMENT> sort (
            Comparator<ELEMENT> comparator
            )
        throws NullPointerException
    {
        !!!;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#start()
     */
    @Override
    public final long start ()
    {
        !!!;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return StringRepresentation.of ( this,
                                         StringRepresentation.DEFAULT_ARRAY_LENGTH );
    }
}
