package musaico.foundation.filter.container;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.structure.ClassName;

import musaico.foundation.filter.comparability.BoundedFilter;

import musaico.foundation.filter.number.EqualToNumber;
import musaico.foundation.filter.number.GreaterThanOne;
import musaico.foundation.filter.number.GreaterThanOrEqualToZero;
import musaico.foundation.filter.number.GreaterThanZero;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Filters containers whose lengths are matched by a number filter
 * (such as GreaterThanZero.FILTER).
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add new filters to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
 *
 *
 * <p>
 * In Java every Filter must be Serializable in order to play nicely
 * across RMI.
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
 * @see musaico.foundation.filter.container.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.container.MODULE#LICENSE
 */
public class LengthFilter<ELEMENT extends Object>
    extends AbstractContainerFilter<ELEMENT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The "longer than 1 element" filter singleton. */
    public static final LengthFilter<Object> GREATER_THAN_ONE =
        new LengthFilter<Object> ( GreaterThanOne.LONG_FILTER );

    /** The "longer than 0 elements" filter singleton. */
    public static final LengthFilter<Object> GREATER_THAN_ZERO =
        new LengthFilter<Object> ( GreaterThanZero.LONG_FILTER );

    /** The "less than or equal to Integer.MAX_SIZE" filter singleton. */
    public static final LengthFilter<Object> INT_32_SIZED =
        new LengthFilter<Object> ( 0, Integer.MAX_VALUE );


    // Matches the length of each container.
    private final Filter<Long> lengthFilter;


    /**
     * <p>
     * Creates a new container LengthFilter.
     * </p>
     *
     * @param exact_length The exact length of containers to keep.
     *                     Must be greater than or equal to 0.
     */
    public LengthFilter (
            int exact_length
            )
        throws IllegalArgumentException
    {
        this ( (long) exact_length );
    }


    /**
     * <p>
     * Creates a new container LengthFilter.
     * </p>
     *
     * @param minimum_length The closed minimum length of containers
     *                       to keep.  Every container must have
     *                       greater than or equal to this many elements.
     *                       Must be greater than or equal to 0.
     *
     * @param maximum_length The closed maximum length of containers
     *                       to keep.  Every container must have
     *                       less than or equal to this many elements.
     *                       Must be greater than or equal to minimum_length.
     */
    public LengthFilter (
            int minimum_length,
            int maximum_length
            )
        throws IllegalArgumentException
    {
        // [ minimum_length, maximum_length ]
        this ( BoundedFilter.EndPoint.CLOSED,
               (long) minimum_length,
               BoundedFilter.EndPoint.CLOSED,
               (long) maximum_length );
    }


    /**
     * <p>
     * Creates a new container LengthFilter.
     * </p>
     *
     * @param minimum_end_point Whether the minimum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param minimum_length The minimum length of containers to keep.
     *                       Must be greater than or equal to 0L.
     *
     * @param maximum_end_point Whether the maximum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param maximum_length The maximum length of containers to keep.
     *                       Must be greater than or equal to minimum_length.
     *
     * @throws NullPointerException If any of the parameters is null.
     *
     * @throws IllegalArgumentException If the specified minimum is
     *                                  negative, or if the specified minimum
     *                                  is greater than the specified
     *                                  maximum length.
     */
    public LengthFilter (
            BoundedFilter.EndPoint minimum_end_point,
            int minimum_length,
            BoundedFilter.EndPoint maximum_end_point,
            int maximum_length
            )
        throws NullPointerException,
               IllegalArgumentException
    {
        this ( minimum_end_point,
               (long) minimum_length,
               maximum_end_point,
               (long) maximum_length );
    }

    /**
     * <p>
     * Creates a new container LengthFilter.
     * </p>
     *
     * @param exact_length The exact length of containers to keep.
     *                     Must be greater than or equal to 0L.
     */
    public LengthFilter (
            long exact_length
            )
        throws IllegalArgumentException
    {
        if ( exact_length < 0L )
        {
            throw new IllegalArgumentException ( "LengthFilter constructor: exact_length must be greater than or equal to 0L" );
        }

        this.lengthFilter = new EqualToNumber<Long> ( exact_length );
    }


    /**
     * <p>
     * Creates a new container LengthFilter.
     * </p>
     *
     * @param minimum_length The closed minimum length of containers
     *                       to keep.  Every container must have
     *                       greater than or equal to this many elements.
     *                       Must be greater than or equal to 0.
     *
     * @param maximum_length The closed maximum length of containers
     *                       to keep.  Every container must have
     *                       less than or equal to this many elements.
     *                       Must be greater than or equal to minimum_length.
     */
    public LengthFilter (
            long minimum_length,
            long maximum_length
            )
        throws IllegalArgumentException
    {
        // [ minimum_length, maximum_length ]
        this ( BoundedFilter.EndPoint.CLOSED,
               minimum_length,
               BoundedFilter.EndPoint.CLOSED,
               maximum_length );
    }


    /**
     * <p>
     * Creates a new container LengthFilter.
     * </p>
     *
     * @param minimum_end_point Whether the minimum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param minimum_length The minimum length of containers to keep.
     *                       Must be greater than or equal to 0L.
     *
     * @param maximum_end_point Whether the maximum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param maximum_length The maximum length of containers to keep.
     *                       Must be greater than or equal to minimum_length.
     *
     * @throws NullPointerException If any of the parameters is null.
     *
     * @throws IllegalArgumentException If the specified minimum is
     *                                  negative, or if the specified minimum
     *                                  is greater than the specified
     *                                  maximum length.
     */
    public LengthFilter (
            BoundedFilter.EndPoint minimum_end_point,
            long minimum_length,
            BoundedFilter.EndPoint maximum_end_point,
            long maximum_length
            )
        throws NullPointerException,
               IllegalArgumentException
    {
        if ( minimum_end_point == null
             || maximum_end_point == null )
        {
            throw new NullPointerException ( "LengthFilter constructor:"
                                             +" cannot have minimum endpoint "
                                             + minimum_end_point
                                             + ", maximum endpoint "
                                             + maximum_end_point );
        }
        else if ( minimum_length < 0L )
        {
            throw new IllegalArgumentException ( "LengthFilter constructor: minimum_length must be greater than or equal to 0L" );
        }
        else if ( maximum_length < minimum_length )
        {
            throw new IllegalArgumentException ( "LengthFilter constructor: maximum_length must be greater than or equal to minimum_length" );
        }

        this.lengthFilter =
            BoundedFilter.overNumbers (
                minimum_end_point,
                minimum_length,
                maximum_end_point,
                maximum_length );
    }


    /**
     * <p>
     * Creates a new container LengthFilter with the specified filter to
     * match container lengths.
     * </p>
     *
     * @param length_filter Filters the lengths of containers, such as
     *                      GreaterThanZero.FILTER.  If null then
     *                      GreaterThanOrEqualToZero.FILTER is used
     *                      by default.
     */
    public LengthFilter (
            Filter<Long> length_filter
            )
    {
        if ( length_filter == null )
        {
            this.lengthFilter = GreaterThanOrEqualToZero.LONG_FILTER;
        }
        else
        {
            this.lengthFilter = length_filter;
        }
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
            Object obj
            )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null )
        {
            return false;
        }
        else if ( this.getClass () != obj.getClass () )
        {
            return false;
        }

        final LengthFilter<?> that = (LengthFilter<?>) obj;
        if ( this.lengthFilter == null )
        {
            if ( that.lengthFilter != null )
            {
                return false;
            }
        }
        else if ( that.lengthFilter == null )
        {
            return false;
        }
        else if ( ! this.lengthFilter.equals ( that.lengthFilter ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterArray(java.lang.Object[])
     */
    @Override
    public final FilterState filterArray (
            ELEMENT [] container
            )
    {
        if ( container == null )
        {
            return FilterState.DISCARDED;
        }

        final long length = (long) container.length;
        final FilterState length_filter_state =
            this.lengthFilter.filter ( length );

        return length_filter_state;
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterCollection(java.util.Collection)
     */
    @Override
    public final FilterState filterCollection(
            Collection<ELEMENT> container
            )
    {
        if ( container == null )
        {
            return FilterState.DISCARDED;
        }

        final long length = (long) container.size ();
        final FilterState length_filter_state =
            this.lengthFilter.filter ( length );

        return length_filter_state;
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterIterable(java.lang.Iterable)
     */
    @Override
    public final FilterState filterIterable(
            Iterable<ELEMENT> container
            )
    {
        if ( container == null )
        {
            return FilterState.DISCARDED;
        }

        long length = 0L;
        for ( ELEMENT element : container )
        {
            length ++;
            if ( length == Long.MAX_VALUE )
            {
                // Too long.  Infinite loop protector.
                return FilterState.DISCARDED;
            }
        }

        final FilterState length_filter_state =
            this.lengthFilter.filter ( length );

        return length_filter_state;
    }


    /**
     * @see musaico.foundation.filter.container.ContainerFilter#filterSingleton(java.lang.Object)
     */
    @Override
    public final FilterState filterSingleton(
            ELEMENT container
            )
    {
        if ( container == null )
        {
            return FilterState.DISCARDED;
        }

        final FilterState length_filter_state =
            this.lengthFilter.filter ( 1L );

        return length_filter_state;
    }



    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 17 * this.getClass ().getName ().hashCode ()
            + this.lengthFilter.hashCode ();
    }


    /**
     * @return The length of the specified array, Collection,
     *         Iterable or singleton object.  Always 0L or
     *         greater if the length count succeeded.
     *         Less than 0L if an internal error occurred,
     *         such as a class cast exception, or an iterator
     *         blew up.
     */
    public static final long lengthOf (
            Object container
            )
    {
        if ( container == null )
        {
            return 0L;
        }
        else if ( container.getClass ().isArray () )
        {
            try
            {
                final Object [] array = (Object []) container;
                return (long) array.length;
            }
            catch ( ClassCastException e )
            {
                return -1L;
            }
        }
        else if ( container instanceof Collection )
        {
            try
            {
                Collection<?> collection = (Collection<?>) container;
                return (long) collection.size ();
            }
            catch ( Exception e )
            {
                return -2L;
            }
        }
        else if ( container instanceof Iterable )
        {
            long length = 0L;
            try
            {
                final Iterable<?> iterable = (Iterable<?>) container;
                for ( Object element : iterable )
                {
                    length ++;
                    if ( length == Long.MAX_VALUE )
                    {
                        // Too long.  Infinite loop protector.
                        return length;
                    }
                }
            }
            catch ( Exception e )
            {
                length = 0L - length;
                if ( length > -3L )
                {
                    length = -3L;
                }
                return length;
            }
        }

        // Singleton.
        return 1L;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " "
            + this.lengthFilter;
    }
}
