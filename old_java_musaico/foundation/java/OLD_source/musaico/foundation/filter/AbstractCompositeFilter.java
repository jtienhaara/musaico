package musaico.foundation.filter;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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
    extends AbstractDomain<GRAIN>
    implements CompositeFilter<GRAIN>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


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
     */
    public AbstractCompositeFilter (
                                    Iterable<? extends Filter<GRAIN>> filters
                                    )
    {
        this ( createArray ( filters ) );
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
            return null;
        }

        if ( filters instanceof Collection )
        {
            final Collection<Filter<ELEMENT>> collection =
                (Collection<Filter<ELEMENT>>) filters;
            final Filter<ELEMENT> [] template = (Filter<ELEMENT> [])
                new Filter [ collection.size () ];
            final Filter<ELEMENT> [] array =
                collection.toArray ( template );
            return array;
        }

        final List<Filter<ELEMENT>> list = new ArrayList<Filter<ELEMENT>> ();
        for ( Filter<ELEMENT> filter : filters )
        {
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
     * @param filters The child Filter(s).  If null, then no filtering
     *                will be done.  If any element is null, then that
     *                element will be removed from the filters.
     */
    @SuppressWarnings({"unchecked", // Possible heap pollution generic varargs.
                       "rawtypes"}) // Java forces generic array creation.
    public AbstractCompositeFilter (
                                    Filter<GRAIN> ... filters
                                    )
    {
        if ( filters == null )
        {
            this.filters = new Filter [ 0 ];
        }
        else
        {
            int num_null_filters = 0;
            for ( Filter<GRAIN> filter : filters )
            {
                if ( filter == null )
                {
                    num_null_filters ++;
                }
            }

            this.filters = new Filter [ filters.length - num_null_filters ];
            if ( num_null_filters == 0 )
            {
                System.arraycopy ( filters, 0,
                                   this.filters, 0, filters.length );
            }
            else
            {
                int f = 0;
                for ( Filter<GRAIN> filter : filters )
                {
                    if ( filter == null )
                    {
                        continue;
                    }

                    this.filters [ f ] = filter;
                    f ++;
                }
            }
        }
    }


    /**
     * @see musaico.foundation.filter.CompositeFilter#filters()
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
}
