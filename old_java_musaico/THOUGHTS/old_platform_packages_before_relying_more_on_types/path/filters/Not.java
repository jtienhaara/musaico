package musaico.foundation.io.filters;

import java.io.Serializable;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;

import musaico.foundation.io.Filter;
import musaico.foundation.io.FilterState;


/**
 * <p>
 * Negates the effect of another Filter.
 * </p>
 *
 * <p>
 * For example, to discard all ABCs matched by an XYZFilter:
 * </p>
 *
 * <pre>
 *     Filter<ABC> not_xyz_filter = new Not ( new XYZFilter () );
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
public class Not<INDEX extends Object>
    implements Filter<INDEX>, Serializable
{
    /** The child filter of this "Not" filter. */
    private Filter<INDEX> child;


    /**
     * <p>
     * Creates a new Not filter to negate the specified filter.
     * </p>
     *
     * <p>
     * An object is DISCARDed (filtered out) whenever the child
     * filter KEEPs it; and an object is KEEPed whenever the child
     * filter DISCARDs it.
     * </p>
     *
     * @param child_filter The child Filter to negate.  Must not be null.
     */
    public Not (
                Filter<INDEX> child_filter
                )
    {
        if ( child_filter == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create [%filter_class%] with child filter index [%child_filter_index%] = [%child_filter%]",
                                                     "filter_class", this.getClass (),
                                                     "child_filter_index", 0,
                                                     "child_filter", child_filter );
        }

        this.child = child_filter;
    }


    /**
     * @return The child (negated) Filter.  Never null.
     */
    public Filter<INDEX> child ()
    {
        return this.child;
    }


    /**
     * @see musaico.foundation.io.Filter#filter(java.lang.Object)
     */
    public FilterState filter (
                               INDEX index
                               )
    {
        FilterState state = this.child.filter ( index );
        if ( state.equals ( FilterState.KEEP ) )
        {
            return FilterState.DISCARD;
        }
        else
        {
            return FilterState.KEEP;
        }
    }
}
