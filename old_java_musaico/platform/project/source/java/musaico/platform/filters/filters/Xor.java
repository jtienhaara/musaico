package musaico.platform.filter.filters;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;
import musaico.foundation.contract.obligations.Parameter1;

import musaico.platform.filter.Filter;
import musaico.platform.filter.AbstractCompositeFilter;


/**
 * <p>
 * Logically XORs together the filter results of one or
 * more child Filters, so that a given object is KEPT only if
 * exactly one of the child filter results is KEPT.
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
 * <pre>
 * Copyright (c) 2010, 2011, 2013 Johann Tienhaara
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
public class Xor<GRAIN extends Object>
    extends AbstractCompositeFilter<GRAIN>
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130915L;
    private static final String serialVersionHash =
        "0x23B52BAB669FC409F98FDB00E968FFF6305BB2DB";


    /** Checks method parameters for us. */
    private final ObjectContracts objectContracts;


    /**
     * <p>
     * Creates a new Xor filter to combine all the specified
     * child filters.
     * </p>
     *
     * <p>
     * An object is DISCARDED (filtered out) unless exactly ONE
     * of the child filters KEPT it.
     * </p>
     *
     * @param filters The child Filter(s) to logically
     *                XOR together.  Must not be null.
     *                Must have at least one Filter.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public Xor (
                Filter<GRAIN> ... filters
                )
        throws Parameter1.MustContainNoNulls.Violation
    {
        super ( filters );


        this.objectContracts = new ObjectContracts ( this );
    }


    /**
     * @see musaico.foundation.io.Filter#filter(java.lang.Object)
     */
    @Override
    public Filter.State filter (
                                GRAIN grain
                                )
        throws ParametersMustNotBeNull.Violation
    {
        this.objectContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                     grain );

        int num_kept = 0;
        for ( Filter<GRAIN> filter : this.myFilters () )
        {
            Filter.State state = filter.filter ( grain );
            if ( state.equals ( Filter.State.KEPT ) )
            {
                num_kept ++;
                if ( num_kept > 1 )
                {
                    return Filter.State.DISCARDED;
                }
            }
        }

        if ( num_kept == 1 )
        {
            return Filter.State.KEPT;
        }
        else
        {
            return Filter.State.DISCARDED;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        StringBuilder sbuf = new StringBuilder ();
        boolean is_first_filter = true;
        for ( Filter<GRAIN> filter : this.myFilters () )
        {
            if ( is_first_filter )
            {
                is_first_filter = false;
            }
            else
            {
                sbuf.append ( " XOR " );
            }

            sbuf.append ( "( " + filter + " )" );
        }

        return sbuf.toString ();
    }
}
