package musaico.platform.filter;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;
import musaico.foundation.contract.obligations.Parameter1;

import musaico.platform.filter.Filter;


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
public abstract class AbstractCompositeFilter<GRAIN extends Object>
    implements CompositeFilter<GRAIN>, Serializable
{
    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AbstractCompositeFilter.class );


    /** The child filters of this composite filter. */
    private Filter<GRAIN> [] filters;


    /**
     * <p>
     * Creates a new AbstractCompositeFilter to combine all the specified
     * child filters.
     * </p>
     *
     * @param filters The child Filter(s).  Must not be null.
     *                Must have at least one Filter.
     */
    @SuppressWarnings({"unchecked", // Possible heap pollution generic varargs.
                       "rawtypes"}) // Java forces generic array creation.
    public AbstractCompositeFilter (
                                    Filter<GRAIN> ... filters
                                    )
        throws Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               filters );

        this.filters = new Filter [ filters.length ];
        System.arraycopy ( filters, 0,
                           this.filters, 0, filters.length );
    }


    /**
     * @see musaico.platform.filter.CompositeFilter#filters()
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
