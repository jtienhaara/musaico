package musaico.foundation.io.filters;

import java.io.Serializable;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;

import musaico.foundation.io.Filter;
import musaico.foundation.io.FilterState;


/**
 * <p>
 * Logically XORs together the filter results of one or
 * more child Filters, KEEPing a given object only if
 * the child filter results XORed together result in KEEP.
 * </p>
 *
 * <p>
 * For example:
 * </p>
 *
 * <pre>
 *     Filter<ABC> filter = new Xor ( new XFilter (),
 *                                    new YFilter (),
 *                                    new ZFilter () );
 *     FilterState state = filter.filter ( new ABC () );
 * </pre>
 *
 * <p>
 * In this example, the resulting state will be KEEP only
 * if the filter results from XFilter, YFilter and ZFilter on
 * the specified object are XORed together to produce a KEEP result.
 * In other words, the result is identical to:
 * </p>
 *
 * <pre>
 *     ABC abc = new ABC ();
 *     boolean filter_boolean =
 *         ( new XFilter ().filter ( abc ).equals ( FilterState.KEEP )
 *     XOR ( new YFilter ().filter ( abc ).equals ( FilterState.KEEP )
 *     XOR ( new ZFilter ().filter ( abc ).equals ( FilterState.KEEP );
 *     FilterState state;
 *     if ( filter_boolean )
 *     {
 *         state = FilterState.KEEP;
 *     }
 *     else
 *     {
 *         state = FilterState.DISCARD;
 *     }
 * </pre>
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
 * <pre>
 * Copyright (c) 2010, 2011 Johann Tienhaara
 * All rights reserved.
 *
 * This file is part of Musaico.
 *
 * Musaico is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Musaico is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Musaico.  If not, see &lt;http://www.gnu.org/licenses/&gt;.
 * </pre>
 */
public class Xor<INDEX extends Object>
    implements Filter<INDEX>, Serializable
{
    /** The child filters of this "Xor" filter. */
    private Filter<INDEX> [] children;


    /**
     * <p>
     * Creates a new Xor filter to combine all the specified
     * child filters.
     * </p>
     *
     * <p>
     * An object is DISCARDed (filtered out) unless exactly ONE
     * of the child filters KEEP it.
     * </p>
     *
     * @param child_filters The child Filter(s) to logically
     *                      XOR together.  Must not be null.
     *                      Must have at least one Filter.
     */
    public Xor (
                Filter<INDEX> ... child_filters
                )
    {
        if ( child_filters == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create [%filter_class%] with child_filters = [%child_filters%]",
                                                     "filter_class", this.getClass (),
                                                     "child_filters", child_filters );
        }
        else if ( child_filters.length == 0 )
        {
            throw new I18nIllegalArgumentException ( "Cannot create [%filter_class%] with child_filters = [%child_filters%]",
                                                     "filter_class", this.getClass (),
                                                     "child_filters", child_filters );
        }

        this.children = new Filter [ child_filters.length ];
        for ( int c = 0; c < this.children.length; c ++ )
        {
            if ( child_filters [ c ] == null )
            {
                throw new I18nIllegalArgumentException ( "Cannot create [%filter_class%] with child filter index [%child_filter_index%] = [%child_filter%]",
                                                         "filter_class", this.getClass (),
                                                         "child_filter_index", c,
                                                         "child_filter", child_filters [ c ] );
            }

            this.children [ c ] = child_filters [ c ];
        }
    }


    /**
     * @return The child Filter at the specified index.
     *         Only returns null if index is less than 0 or greater
     *         than or equal to size (); otherwise never null.
     */
    public Filter<INDEX> child (
                                int child_index
                                )
    {
        if ( child_index < 0
             || child_index >= this.children.length )
        {
            return null;
        }

        return this.children [ child_index ];
    }


    /**
     * @see musaico.foundation.io.Filter#filter(java.lang.Object)
     */
    public FilterState filter (
                               INDEX index
                               )
    {
        int num_kept = 0;
        for ( int c = 0; c < this.children.length; c ++ )
        {
            FilterState state = this.children [ c ].filter ( index );
            if ( state.equals ( FilterState.KEEP ) )
            {
                num_kept ++;
                if ( num_kept > 1 )
                {
                    return FilterState.DISCARD;
                }
            }
        }

        if ( num_kept == 1 )
        {
            return FilterState.KEEP;
        }
        else
        {
            return FilterState.DISCARD;
        }
    }


    /**
     * @return The size of this logical XOR clause, in terms
     *         of number of child filters.  Always 1 or greater.
     */
    public int size ()
    {
        return this.children.length;
    }
}
