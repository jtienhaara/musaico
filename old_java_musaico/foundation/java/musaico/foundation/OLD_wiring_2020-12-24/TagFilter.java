package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.Arrays;
import java.util.Collection;


import musaico.foundation.filter.DiscardAll;
import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;
import musaico.foundation.filter.KeepAll;

import musaico.foundation.filter.equality.EqualTo;

import musaico.foundation.filter.membership.MemberOf;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Keeps only Tags matching a specific name/version and zero or more
 * value Filter(s).
 * </p>
 *
 *
 * <p>
 * In Java, every Filter must be Serializable in order to play
 * nicely over RMI.
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
public class TagFilter
    implements Filter<Tag>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Filters each Tag name.
    private final Filter<String> nameFilter;

    // Filters each Tag version.
    private final Filter<Integer> versionFilter;

    // Filters each Tag value.  The value must be KEPT by all filters.
    private final Filter<Object> [] valueFilters;

    private final int hashCode;


    public TagFilter (
            String name
            )
    {
        this ( name,
               new KeepAll<Integer> (),
               new KeepAll<Object> () );
    }

    public TagFilter (
            String name,
            Filter<Integer> version_filter
            )
    {
        this ( name,
               version_filter,
               new KeepAll<Object> () );
    }

    public TagFilter (
            String name,
            Object ... values
            )
    {
        this ( name,
               new KeepAll<Integer> (),
               new MemberOf<Object> ( values ) );
    }

    public TagFilter (
            String name,
            Filter<Integer> version_filter,
            Object ... values
            )
    {
        this ( name,
               version_filter,
               new MemberOf<Object> ( values ) );
    }

    public TagFilter (
            String name,
            Collection<Object> values
            )
    {
        this ( name,
               new KeepAll<Integer> (),
               new MemberOf<Object> ( values ) );
    }

    public TagFilter (
            String name,
            Filter<Integer> version_filter,
            Collection<Object> values
            )
    {
        this ( name,
               version_filter,
               new MemberOf<Object> ( values ) );
    }

    @SafeVarargs // Unchecked generic array creation Filter<Object> ...
    public TagFilter (
            String name,
            Filter<Object> ... value_filters
            )
    {
        this ( new EqualTo<String> ( name ),
               new KeepAll<Integer> (),
               value_filters );
    }

    @SafeVarargs // Unchecked generic array creation Filter<Object> ...
    public TagFilter (
            String name,
            Filter<Integer> version_filter,
            Filter<Object> ... value_filters
            )
    {
        this ( new EqualTo<String> ( name ),
               version_filter,
               value_filters );
    }

    @SafeVarargs // Unchecked generic array creation Filter<Object> ...
    public TagFilter (
            Filter<String> name_filter,
            Filter<Object> ... value_filters
            )
    {
        this ( name_filter,
               new KeepAll<Integer> (),
               value_filters );
    }

    @SafeVarargs // Unchecked generic array creation Filter<Object> ...
    @SuppressWarnings({"rawtypes","unchecked"}) // Generic array creation new Filter<Object> [ ... ]
    public TagFilter (
            Filter<String> name_filter,
            Filter<Integer> version_filter,
            Filter<Object> ... value_filters
            )
    {
        int hash_code = 0;

        if ( name_filter == null )
        {
            this.nameFilter = new DiscardAll<String> ();
        }
        else
        {
            this.nameFilter = name_filter;
        }

        hash_code += this.nameFilter.hashCode ();

        if ( version_filter == null )
        {
            this.versionFilter = new DiscardAll<Integer> ();
        }
        else
        {
            this.versionFilter = version_filter;
        }

        hash_code += this.versionFilter.hashCode () * 31;

        if ( value_filters == null )
        {
            // SuppressWarnings({rawtypes","unchecked"}) // Generic array creation new Filter<Object> [ ... ]
            this.valueFilters = (Filter<Object> [])
                new Filter [] { new DiscardAll<Object> () };

            hash_code += this.valueFilters [ 0 ].hashCode () * 17;
        }
        else
        {
            // SuppressWarnings({"rawtypes","unchecked"}) // Generic array creation new Filter<Object> [ ... ]
            this.valueFilters = (Filter<Object> [])
                new Filter [ value_filters.length ];
            for ( int vf = 0; vf < value_filters.length; vf ++ )
            {
                if ( value_filters [ vf ] == null )
                {
                    this.valueFilters [ vf ] = new DiscardAll<Object> ();
                }
                else
                {
                    this.valueFilters [ vf ] = value_filters [ vf ];
                }

                hash_code += this.valueFilters [ vf ].hashCode () * 17;
            }
        }

        this.hashCode = hash_code;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals (
            Object object
            )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( this == object )
        {
            return true;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final TagFilter that = (TagFilter) object;
        if ( this.nameFilter == null )
        {
            if ( that.nameFilter != null )
            {
                return false;
            }
        }
        else if ( that.nameFilter == null )
        {
            return false;
        }
        else if ( ! this.nameFilter.equals ( that.nameFilter ) )
        {
            return false;
        }

        if ( this.versionFilter == null )
        {
            if ( that.versionFilter != null )
            {
                return false;
            }
        }
        else if ( that.versionFilter == null )
        {
            return false;
        }
        else if ( ! this.versionFilter.equals ( that.versionFilter ) )
        {
            return false;
        }

        if ( this.valueFilters == null )
        {
            if ( that.valueFilters != null )
            {
                return false;
            }
        }
        else if ( that.valueFilters == null )
        {
            return false;
        }
        else if ( ! Arrays.equals ( this.valueFilters, that.valueFilters ) )
        {
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
            Tag tag
            )
    {
        if ( tag == null )
        {
            return FilterState.DISCARDED;
        }

        if ( ! this.nameFilter.filter ( tag.name () ).isKept () )
        {
            return FilterState.DISCARDED;
        }

        if ( ! this.versionFilter.filter ( tag.version () ).isKept () )
        {
            return FilterState.DISCARDED;
        }

        for ( Filter<Object> value_filter : this.valueFilters )
        {
            if ( ! value_filter.filter ( tag.value () ).isKept () )
            {
                return FilterState.DISCARDED;
            }
        }

        return FilterState.KEPT;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.hashCode;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + "["
            + this.nameFilter
            + ","
            + this.versionFilter
            + ","
            + Arrays.toString ( this.valueFilters )
            + "]";
    }
}
