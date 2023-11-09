package musaico.foundation.domains.elements;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.Comparator;
import java.util.ConcurrentModificationException;


import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.Generator;
import musaico.foundation.domains.StringRepresentation;

import musaico.foundation.domains.generators.IndexGenerator;

import musaico.foundation.filter.FilterState;


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
public abstract class AbstractElements<CONTAINER extends Object, ELEMENT extends Object>
    implements Elements<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The Class of each individual element in these Elements.
    private final Class<ELEMENT> elementClass;

    // The Flags for these Elements, including immutability and fixed length.
    private final ElementFlags flags;

    // The container represented by these Elements.
    private final CONTAINER container;

    // The order(s) into which these elements have been sorted, if any.
    private final Elements<Comparator<ELEMENT>> orders;


    /**
     * <p>
     * Creates a new sequence of AbstractElements.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param flags The ElementFlags, specifying whether these
     *              Elements are mutable or immutable, fixed length
     *              or variable length, and so on.  Must not be null.
     *
     * @param container The container to be represented by these
     *                  AbstractElements.  Can contain null elements.
     *                  Contents can be modified by the caller only
     *                  if flags.isImmutable () is false, though in that case
     *                  the caller must ensure concurrency protection
     *                  is in place around these Elements.
     *                  Must not be null.
     *
     * @param orders 0 or more Comparators by which these Elements have
     *               already been sorted, in order from # 0 to # last.
     *               Can be empty if these elements are unordered.
     *               Can be null if these elements are unordered.
     *               Must not contain any null elements.
     *
     * @throws NullPointerException If the specified element_class
     *                              and/or flags and/or container
     *                              and/or any element of the specified
     *                              orders is/are null.
     */
    @SuppressWarnings("unchecked") // Generic varargs heap pollution.
    public AbstractElements (
            Class<ELEMENT> element_class,
            ElementFlags flags,
            CONTAINER container,
            Comparator<ELEMENT> ... orders
            )
        throws NullPointerException
    {
        this ( element_class,  // element_class
               flags,          // flags
               container,      // container
               // Can return null:
               AbstractElements.createOrderElementsFromArray ( // orders
                   flags,
                   orders ) );
    }


    /**
     * <p>
     * Creates a new sequence of AbstractElements.
     * </p>
     *
     * @param element_class The Class of each individual element
     *                      in these Elements.  Must not be null.
     *
     * @param flags The ElementFlags, specifying whether these
     *              Elements are mutable or immutable, fixed length
     *              or variable length, and so on.  Must not be null.
     *
     * @param container The container to be represented by these
     *                  AbstractElements.  Can contain null elements.
     *                  Contents can be modified by the caller only
     *                  if flags.isImmutable () is false, though in that case
     *                  the caller must ensure concurrency protection
     *                  is in place around these Elements.
     *                  Must not be null.
     *
     * @param orders 0 or more Comparators by which these Elements have
     *               already been sorted, in order from # 0 to # last.
     *               Can be empty if these elements are unordered.
     *               Can be null if these elements are unordered.
     *               Must not contain any null elements.
     *
     * @throws NullPointerException If the specified element_class
     *                              and/or flags and/or container
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
    public AbstractElements (
            Class<ELEMENT> element_class,
            ElementFlags flags,
            CONTAINER container,
            Elements<Comparator<ELEMENT>> orders
            )
        throws NullPointerException,
               ElementsFlagsException
    {
        if ( element_class == null
             || flags == null
             || container == null )
        {
            final String container_string =
                StringRepresentation.of ( container,
                                          StringRepresentation.DEFAULT_ARRAY_LENGTH );
            final String orders_string =
                StringRepresentation.of ( orders,
                                          StringRepresentation.DEFAULT_ARRAY_LENGTH );
            throw new NullPointerException ( "ERROR Cannot create "
                                             + ClassName.of ( this.getClass () )
                                             + " with container = "
                                             + container_string
                                             + ", element_class = "
                                             + ClassName.of ( element_class )
                                             + ", flags = "
                                             + flags
                                             + ", orders = "
                                             + orders_string );
        }
        else if ( ! this.supportedFlags ( flags ).equals ( flags ) )
        {
            throw new ElementsFlagsException ( this, flags );
        }

        this.elementClass = element_class;
        this.flags = flags;
        this.container = container;
        // If orders is null, we leave it that way, then create new
        // Elements for the orders when necessary.
        this.orders = orders;
    }


    /**
     * <p>
     * Creates a new sequence of AbstractElements.
     * </p>
     *
     * @param flags The ElementFlags, specifying whether these
     *              Elements are mutable or immutable, fixed length
     *              or variable length, and so on.  Must not be null.
     *
     * @param elements The Elements to include in these new AbstractElements.
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
    public AbstractElements (
            ElementFlags flags,
            Elements<ELEMENT> elements
            )
        throws NullPointerException,
               ElementsLengthException,
               ElementsFlagsException,
               ConcurrentModificationException
    {
        if ( flags == null
             || elements == null )
        {
            throw new NullPointerException ( "ERROR: Can't create "
                                             + ClassName.of ( this.getClass () )
                                             + " from flags "
                                             + flags
                                             + " Elements "
                                             + elements );
        }
        else if ( ! this.supportedFlags ( flags ).equals ( flags ) )
        {
            throw new ElementsFlagsException ( this, flags );
        }

        this.elementClass = elements.elementClass ();
        this.flags = flags;
        final Elements<Comparator<ELEMENT>> maybe_mutable_orders =
            elements.order ();
        final ElementFlags order_flags = maybe_mutable_orders.flags ();
        if ( ! order_flags.isVariableLength ()
             && ! order_flags.isOverwritable () )
        {
            this.orders = maybe_mutable_orders;
        }
        else
        {
            this.orders = maybe_mutable_orders.duplicate ( order_flags );
        }

        this.container = extractContentsFrom ( elements );
    }


    /**
     * <p>
     * Returns the container of these Elements extracted from or
     * constructed from the specified Elements.
     * </p>
     *
     * <p>
     * If the specified Elements have the same container type as these
     * Elements, but are NOT overwritable NOR variable length, then the
     * container of the specified Elements can be extracted and returned
     * as-is.
     * </p>
     *
     * <p>
     * If the specified Elements have a different container type
     * AND/OR if the specified Elements are overwritable
     * AND/OR if the specified Elements are variable-length,
     * then a new container must be constructed.
     * </p>
     *
     * @param elements The Elements whose contents will be extracted
     *                 as a container suitable for these Elements.
     *                 Must not be null.
     *
     * @return The (new or existing) container of elements extracted
     *         from the specified Elements.  Never null.
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
    protected abstract CONTAINER extractContentsFrom (
            Elements<ELEMENT> elements
            )
        throws NullPointerException,
               ElementsLengthException,
               ElementsFlagsException,
               ConcurrentModificationException;




    // Create Elements out of array of Comparators.
    // Returns null if null or empty array is passed in, or if there
    // is exactly 1 null element (which is what happens when you call
    // varargs with last argument null, unless you cast it).
    // Throws NullPointerException if there are otherwise any null orders.
    @SuppressWarnings("unchecked") // Generic varargs heap pollution,
                                   // Class<Comp> --> Class<Comp<ORDERABLE>>
    private static final <ORDERABLE extends Object>
        Elements<Comparator<ORDERABLE>> createOrderElementsFromArray (
                ElementFlags element_flags,
                Comparator<ORDERABLE> ... orders_array
        )
        throws NullPointerException
    {
        if ( orders_array == null
             || orders_array.length == 0
             || ( orders_array.length == 1
                  && orders_array [ 0 ] == null ) )
        {
            // Prevent infinite loop when creating an
            // AbstractElements<Comparator<X>> with no orders of its own.
            return null;
        }

        // Make sure there are no null orders.
        for ( Comparator<ORDERABLE> order : orders_array )
        {
            if ( order == null )
            {
                final String orders_array_string =
                    StringRepresentation.of (
                        orders_array,
                        StringRepresentation.DEFAULT_ARRAY_LENGTH );
                throw new NullPointerException ( "ERROR: Can't create"
                                                 + " Elements out of orders:"
                                                 + " element_flags = "
                                                 + element_flags
                                                 + " orders_array = "
                                                 + orders_array_string );
            }
        }

        final Elements<Comparator<ORDERABLE>> orders_elements;
        final ElementFlags order_flags;
        if ( element_flags.isOverwritable () )
        {
            // We can add or remove orderings after construction,
            // since the elements are overwritable and so therefore
            // can be sorted in different ways.
            order_flags = ElementFlags.VARIABLE_LENGTH;
        }
        else
        {
            // The elements cannot be overwritten, so there
            // is no way to sort them with fewer or more orders
            // than what we start with at construction time.
            order_flags = ElementFlags.IMMUTABLE;
        }

        // Throws ElementsFlagsException (but shouldn't,
        // since we've been careful to not use variable length flag):
        orders_elements =
            new ArrayElements<Comparator<ORDERABLE>> (
                (Class<Comparator<ORDERABLE>>) ( (Class<?>) Comparator.class ),
                order_flags,
                orders_array );

        return orders_elements;
    }


    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#array()

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#at(long)


    /**
     * <p>
     * Clamps the specified index to the range 0L to length - 1L,
     * inclusive, or returns Elements.NONE if the specified index
     * is out of bounds.
     * </p>
     *
     * <p>
     * For example, if Elements.FIRST is passed, then 0L
     * will be returned.  Or if Elements.LAST is passed, then length - 1L
     * will be returned.  And so on.
     * </p>
     *
     * @see musaico.foundation.domains.Elements#FIRST
     * @see musaico.foundation.domains.Elements#FORWARD
     * @see musaico.foundation.domains.Elements#FROM_START
     * @see musaico.foundation.domains.Elements#AFTER_LAST
     * @see musaico.foundation.domains.Elements#LAST
     * @see musaico.foundation.domains.Elements#BACKWARD
     * @see musaico.foundation.domains.Elements#FROM_END
     *
     * @param index The index to clamp.  Can be any number.
     *
     * @param length The length to clamp the index to.
     *               Must be greater than or equal to 0L.
     *
     * @return The specified index, converted if it is offset from
     *         LAST/BACKWARD (negative), clamped to the specified length.
     *         Always greater than or equal to 0L
     *         and less than or equal to length - 1L,
     *         UNLESS the index is out of clamping range,
     *         in which case Elements.NONE will be returned.
     *         If the specified index is Elements.AFTER_LAST
     *         or Elements.NONE, then Elements.NONE will always be returned.
     *
     * @throws ElementsLengthException If the specified length is negative.
     */
    protected static final long clamp (
            long index,
            long length
            )
        throws ElementsLengthException
    {
        if ( length < 0L )
        {
            throw new ElementsLengthException ( "clamp ( " + index + " )",
                                                length );
        }
        else if ( index == Elements.NONE
                  || index == Elements.AFTER_LAST )
        {
            return Elements.NONE;
        }

        if ( index >= Elements.FIRST
             && index < length )
        {
            return index;
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
            length
            + Elements.BACKWARD
            - index
            - 1L;

        return reversed_index;
    }


    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#collection()


    /**
     * @see musaico.foundation.domains.elements.Elements#container()
     */
    @Override
    public final CONTAINER container ()
    {
        return this.container;
    }


    /**
     * @return A String representation of the individual elements.
     *         Never null.
     */
    protected abstract String contentsString ();


    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#duplicate(musaico.foundation.domains.elements.ElementFlags)


    /**
     * @see musaico.foundation.domains.elements.Elements#elementClass()
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

        final AbstractElements<?, ?> unsure_that =
            (AbstractElements<?, ?>) object;

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

        final AbstractElements<CONTAINER, ELEMENT> that;
        try
        {
            that = (AbstractElements<CONTAINER, ELEMENT>) unsure_that;
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
     * @return True if this container is the same length as
     *         the specified container and every element
     *         of this container is equal to every corresponding
     *         element in the specified container;
     *         false if the length or any element
     *         is not equal.
     */
    protected abstract boolean equalsElements (
            CONTAINER this_container,
            AbstractElements<CONTAINER, ELEMENT> that_elements,
            CONTAINER that_container
            );


    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#filter(musaico.foundation.filter.Filter)


    /**
     * <p>
     * The result of filter ( ELEMENT grain ) is
     * KEPT if these Elements has ( grain ),
     * or DISCARDED if these Elements do not has ( grain ).
     * </p>
     *
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    public final FilterState filter (
            ELEMENT grain
            )
    {
        if ( this.has ( grain ) )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }


    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#find(java.lang.Object)


    /**
     * @see musaico.foundation.domains.elements.Elements#flags()
     */
    @Override
    public final ElementFlags flags ()
    {
        return this.flags;
    }


    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#from(musaico.foundation.domains.elements.Elements, musaico.foundation.domains.elements.ElementFlags)

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#has(java.lang.Object)


    /**
     * @see musaico.foundation.domains.elements.Elements#hasOffset(long)
     */
    @Override
    public final boolean hasOffset (
            long index
            )
    {
        final long length = this.length ();
        final long clamped_index;
        try
        {
            clamped_index =
                AbstractElements.clamp ( index, length );
        }
        catch ( ElementsLengthException e ) // Should never happen.
        {
            return false;
        }

        if ( clamped_index < 0L )
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    // Every AbstractElements must implement
    // java.lang.Object#hashCode()

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#insert(long, java.lang.Object[])

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#insertAll(long, musaico.foundation.domains.elements.Elements)

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#isArrayLength()

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#isEmpty()

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#isSingleton()

    // Every AbstractElements must implement
    // java.lang.Iterable#iterator()

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#keep(long[])

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#keepAll(musaico.foundation.domains.elements.Elements)

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#keepRange(long, long)

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#length()

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#list()


    /**
     * @see musaico.foundation.domains.elements.Elements#none()
     */
    @Override
    @SuppressWarnings("unchecked") // If Array.newInstance() does not return
        // ELEMENT [], then I am a monkey's uncle.
        // Ook.
    public final ELEMENT [] none ()
    {
        final Class<ELEMENT> element_class = this.elementClass ();
        final ELEMENT [] none = (ELEMENT [])
            Array.newInstance ( element_class, 0 );
        return none;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#offsets()
     */
    @Override
    public final Elements<Long> offsets ()
    {
        final long length = this.length ();
        final Generator<Long> generator = new IndexGenerator ( length );
        final GeneratedElements<Long> offsets =
            new GeneratedElements<Long> ( Long.class, // element_class
                                          generator,  // generator
                                          IndexGenerator.ORDER ); // orders
        return offsets;
    }


    /**
     * @see musaico.foundation.domains.elements.Elements#order()
     */
    @Override
    @SuppressWarnings("unchecked") // Class<Comp> --> Class<Comp<ELEMENT>>
    public final Elements<Comparator<ELEMENT>> order ()
    {
        if ( this.orders == null )
        {
            final ElementFlags order_flags;
            if ( this.flags.isOverwritable () )
            {
                // We can add orderings after construction,
                // since the elements are overwritable and so therefore
                // can be sorted in different ways.
                order_flags = ElementFlags.VARIABLE_LENGTH;
            }
            else
            {
                // The elements cannot be overwritten, so there
                // is no way to sort them with fewer or more orders
                // than what we started with at construction time.
                order_flags = ElementFlags.IMMUTABLE;
            }

            // Could throw ElementsFlagsException (but shouldn't,
            // since we've been careful to not use variable length flag):
            final Elements<Comparator<ELEMENT>> orders =
                new ListElements<Comparator<ELEMENT>> (
                    (Class<Comparator<ELEMENT>>) ( (Class<?>) Comparator.class ),
                    order_flags );
            return orders;
        }
        else
        {
            return this.orders;
        }
    }


    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#overwrite(long, java.lang.Object[])

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#overwriteAll(long, musaico.foundation.domains.elements.Elements)

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#remove(long[])

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#removeAll(musaico.foundation.domains.elements.Elements)

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#removeRange(long, long)

    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#set()


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

        final ELEMENT [] singleton = this.at ( Elements.FIRST );
        if ( singleton.length != 1 )
        {
            throw new ElementsLengthException ( this );
        }

        return singleton [ 0 ];
    }


    // Every AbstractElements must implement
    // musaico.foundation.domains.elements.Elements#sort(java.util.Comparator)


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " "
            + this.contentsString ();
    }
}
