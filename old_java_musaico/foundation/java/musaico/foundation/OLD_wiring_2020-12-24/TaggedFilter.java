package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.Arrays;
import java.util.Collection;


import musaico.foundation.filter.DiscardAll;
import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Keeps only Tagged wiring objects whose Tags match
 * a specific Tags filter.
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
public class TaggedFilter<TAGGED extends Tagged<TAGGED>>
    implements Filter<TAGGED>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Filters the Tags of a Tagged wiring object (such
    // as a Conductor).
    private final Filter<Tag> [] tagFilters;

    private final int hashCode;


    @SafeVarargs // Filter<Tag> ...
    @SuppressWarnings({"rawtypes","unchecked"}) // Generic array creation new Filter<Tag> [ ... ]
    public TaggedFilter (
            Filter<Tag> ... tag_filters
            )
    {
        int hash_code = 0;
        if ( tag_filters == null )
        {
            // SuppressWarnings({"rawtypes","unchecked"}) // Generic array creation new Filter<Tag> [ ... ]
            this.tagFilters = (Filter<Tag> [])
                new Filter [] { new DiscardAll<Tag> () };

            hash_code = this.tagFilters [ 0 ].hashCode ();
        }
        else
        {
            // SuppressWarnings({"rawtypes","unchecked"}) // Generic array creation new Filter<Tag> [ ... ]
            this.tagFilters = (Filter<Tag> [])
                new Filter [ tag_filters.length ];
            for ( int tf = 0; tf < tag_filters.length; tf ++ )
            {
                if ( tag_filters [ tf ] == null )
                {
                    this.tagFilters [ tf ] = new DiscardAll<Tag> ();
                }
                else
                {
                    this.tagFilters [ tf ] = tag_filters [ tf ];
                }

                hash_code += this.tagFilters [ tf ].hashCode ();
            }
        }

        this.hashCode = hash_code;
    }

    public TaggedFilter (
            Collection<Filter<Tag>> tag_filters
            )
    {
        this ( collectionToArray ( tag_filters ) );
    }

    @SuppressWarnings({"rawtypes","unchecked"}) // Generic array creation new Filter<Tag> [ ... ]
    private static final Filter<Tag> [] collectionToArray (
            Collection<Filter<Tag>> tag_filters
            )
    {
        if ( tag_filters == null )
        {
            return null;
        }

        // SuppressWarnings({"rawtypes","unchecked"}) // Generic array creation new Filter<Tag> [ ... ]
        final Filter<Tag> [] template = (Filter<Tag> [])
            new Filter [ tag_filters.size () ];
        final Filter<Tag> [] tag_filters_array =
            tag_filters.toArray ( template );

        return tag_filters_array;
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

        final TaggedFilter<?> that = (TaggedFilter<?>) object;
        if ( this.tagFilters == null )
        {
            if ( that.tagFilters != null )
            {
                return false;
            }
        }
        else if ( that.tagFilters == null )
        {
            return false;
        }
        else if ( ! Arrays.equals ( this.tagFilters, that.tagFilters ) )
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
            TAGGED tagged
            )
    {
        if ( tagged == null )
        {
            return FilterState.DISCARDED;
        }

        for ( Filter<Tag> tag_filter : this.tagFilters )
        {
            final Tag [] matching_tags =
                tagged.tags ( tag_filter );
            if ( matching_tags == null
                 || matching_tags.length == 0 )
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
            + "[" + Arrays.toString ( this.tagFilters ) + "]";
    }
}
