package musaico.foundation.filter.composite;

import java.io.Serializable;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import musaico.foundation.filter.Filter;


/**
 * <p>
 * Provides the boilerplate code for most composite filters, such as
 * logical ands, ors, xors and so on.
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
 * @see musaico.foundation.filter.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.MODULE#LICENSE
 */
public abstract class AbstractCompositeFilter<GRAIN extends Object>
    implements CompositeFilter<GRAIN>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The child filters of this composite filter. */
    private Filter<GRAIN> [] filters;


    /**
     * <p>
     * Creates a new AbstractCompositeFilter to combine all the specified
     * child filters.
     * </p>
     *
     * @param filters The child Filter(s).  If null, then no filtering
     *                will be done.  If any element is null, then that
     *                element will be removed from the filters.
     *
     * @throws NullPointerException If either the specified filters parameter
     *                              is null, or any of the elements
     *                              is/are null.
     */
    public AbstractCompositeFilter (
            Iterable<? extends Filter<GRAIN>> filters
            )
        throws NullPointerException
    {
        this ( false,                      // is_new_array_required
               createArray ( filters ) ); // filters
    }


    // Turns the specified Iterable into an array.
    @SuppressWarnings({"rawtypes", // Create generic array Filter<E> [].
                "unchecked"}) // Cast Filter[] to Filter<E>[].
    private static final <ELEMENT extends Object>
        Filter<ELEMENT> [] createArray (
            Iterable<? extends Filter<ELEMENT>> filters
            )
    {
        if ( filters == null )
        {
            return null; // Let the filter constructor throw the NPE.
        }

        if ( filters instanceof Collection )
        {
            final Collection<Filter<ELEMENT>> collection =
                (Collection<Filter<ELEMENT>>) filters;
            final Filter<ELEMENT> [] template = (Filter<ELEMENT> [])
                new Filter [ collection.size () ];
            final Filter<ELEMENT> [] array =
                collection.toArray ( template );
            // The filter constructor will throw NPE if any elements null.
            return array;
        }

        final List<Filter<ELEMENT>> list = new ArrayList<Filter<ELEMENT>> ();
        for ( Filter<ELEMENT> filter : filters )
        {
            // The filter constructor will throw NPE if any elements null.
            list.add ( filter );
        }

        final Filter<ELEMENT> [] template = (Filter<ELEMENT> [])
            new Filter [ list.size () ];
        final Filter<ELEMENT> [] array =
            list.toArray ( template );
        return array;
    }


    /**
     * <p>
     * Creates a new AbstractCompositeFilter to combine all the specified
     * child filters.
     * </p>
     *
     * <p>
     * A new copy of the specified array of Filters is created,
     * to ensure nobody messes with it after construction of this
     * composite filter.
     * </p>
     *
     * @param filters The child Filter(s).  If null, then no filtering
     *                will be done.  Must not be null.  Must not contain
     *                any null elements.
     *
     * @throws NullPointerException If either the specified filters parameter
     *                              is null, or any of the elements
     *                              is/are null.
     */
    @SuppressWarnings("unchecked") // Varargs possible heap pollution.
    public AbstractCompositeFilter (
            Filter<GRAIN> ... filters
            )
        throws NullPointerException
    {
        this ( true,      // is_new_array_required
               filters ); // filters
    }


    /**
     * <p>
     * Creates a new AbstractCompositeFilter to combine all the specified
     * child filters.
     * </p>
     *
     * @param filters The child Filter(s).  If null, then no filtering
     *                will be done.  Must not be null.  Must not contain
     *                any null elements.
     *
     * @param is_new_array_required True to create a copy of the specified
     *                              array to protect against concurrent
     *                              modification; false to use the specified
     *                              array as-is.  If you're not sure,
     *                              pass true to ensure thread safety.
     *
     * @throws NullPointerException If either the specified filters parameter
     *                              is null, or any of the elements
     *                              is/are null.
     */
    @SuppressWarnings({"unchecked", // Possible heap pollution generic varargs.
                       "rawtypes"}) // Java forces generic array creation.
    public AbstractCompositeFilter (
            boolean is_new_array_required,
            Filter<GRAIN> ... filters
            )
        throws NullPointerException
    {
        if ( filters == null )
        {
            throw new NullPointerException ( "AbstractCompositeFilter:"
                                             + " illegal filters = "
                                             + filters
                                             + " (is_new_array_required = "
                                             + is_new_array_required
                                             + ")" );
        }

        // Check each individual Filter:
        List<Integer> null_filter_indices = null;
        int filter_index = 0;
        for ( Filter<GRAIN> filter : filters )
        {
            if ( filter == null )
            {
                if ( null_filter_indices == null )
                {
                    null_filter_indices =
                        new ArrayList<Integer> ();
                }

                null_filter_indices.add ( filter_index );
            }

            filter_index ++;
        }

        if ( null_filter_indices != null
             && null_filter_indices.size () > 0 )
        {
            throw new NullPointerException ( "AbstractCompositeFilter:"
                                             + " illegal filters = "
                                             + Arrays.toString ( filters )
                                             + " null filters at "
                                             + null_filter_indices
                                             + " (is_new_array_required = "
                                             + is_new_array_required
                                             + ")" );
        }

        if ( is_new_array_required )
        {
            this.filters = new Filter [ filters.length ];
            System.arraycopy ( filters, 0,
                               this.filters, 0, filters.length );
        }
        else
        {
            this.filters = filters;
        }
    }


    /**
     * @see musaico.foundation.filter.composite.CompositeFilter#filters()
     */
    @Override
    @SuppressWarnings({"rawtypes", // Java forces generic array creation.
                       "unchecked"}) // Cast array to genericized array.
    public Filter<GRAIN> [] filters ()
    {
        final Filter<GRAIN> [] filters = new Filter [ this.filters.length ];
        System.arraycopy ( this.filters, 0,
                           filters, 0, this.filters.length );

        return filters;
    }


    /**
     * @return The actual filters stored in this composite filter.
     *         For use by the implementing class only.  External
     *         callers should retrieve a copy of the filters, using
     *         the <code> filters () </code> method.
     *         Never null.  Never contains any null elements.
     */
    protected final Filter<GRAIN> [] myFilters ()
    {
        return this.filters;
    }


    /**
     * <p>
     * Returns a String representing the child compponent Filters
     * of this composite, separated by he specified separator
     * (such as "," or "and" and so on).
     * </p>
     *
     * <p>
     * Each component Filter's toString method will be used to convert
     * it to a String.
     * </p>
     *
     * @param separator The text to separate each pair of component filters.
     *                  Must not be null.
     *
     * @return The String representationn of the component Filters.
     *         Never null.
     */
    protected final String toStringComponents (
            String separator
            )
    {
        StringBuilder sbuf = new StringBuilder ();
        final Filter<GRAIN> [] filters =
            this.myFilters ();
        if ( filters == null
             || filters.length == 0 )
        {
            return "NO FILTERS";
        }

        boolean is_first_filter = true;
        for ( Filter<GRAIN> filter : filters )
        {
            if ( is_first_filter )
            {
                is_first_filter = false;
            }
            else
            {
                sbuf.append ( " " + separator + " " );
            }

            sbuf.append ( "( " + filter + " )" );
        }

        return sbuf.toString ();
    }
}
