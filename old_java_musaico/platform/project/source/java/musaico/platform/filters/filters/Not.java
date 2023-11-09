package musaico.platform.filter.filters;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;
import musaico.foundation.contract.obligations.Parameter1;

import musaico.platform.filter.Filter;
import musaico.platform.filter.AbstractCompositeFilter;


/**
 * <p>
 * Negates the effect of another Filter, so that every KEPT result
 * becomes DISCARDED and vice-versa.
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
public class Not<GRAIN extends Object>
    extends AbstractCompositeFilter<GRAIN>
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130915L;
    private static final String serialVersionHash =
        "0xE156F665E39C7E9AA29C65FF15C5FB1F4AE18376";


    /** Checks method parameters for us. */
    private final ObjectContracts objectContracts;


    /**
     * <p>
     * Creates a new Not filter to negate the specified filter.
     * </p>
     *
     * <p>
     * An object is DISCARDED (filtered out) whenever the child
     * filter KEPTs it; and an object is KEPTed whenever the child
     * filter decides it is DISCARDED.
     * </p>
     *
     * @param filter The child Filter to negate.  Must not be null.
     */
    @SuppressWarnings({"rawtypes", // Java forces generic array creation.
                       "unchecked"}) // Java forces generic array creation.
    public Not (
                Filter<GRAIN> filter
                )
    {
        super ( new Filter [] { filter } );


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

        Filter.State state = this.myFilters () [ 0 ].filter ( grain );
        if ( state.equals ( Filter.State.KEPT ) )
        {
            return Filter.State.DISCARDED;
        }
        else
        {
            return Filter.State.KEPT;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "! ( " + this.myFilters () [ 0 ] + " )";
    }
}
