package musaico.platform.filter;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.types.AbstractOperation;
import musaico.foundation.types.NoValue;
import musaico.foundation.types.Value;
import musaico.foundation.types.ValueBuilder;


/**
 * <p>
 * Filters each input value, returning only the inputs which were kept
 * by the filter.
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
 * Copyright (c) 2013 Johann Tienhaara
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
public class FilterOp<GRAIN extends Object>
    extends AbstractOperation<GRAIN, GRAIN>
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130915L;
    private static final String serialVersionHash =
        "0xC3625F118220124FBB1FC1BCEA4A42412D1261B7";


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( FilterOp.class );


    /** The filter which decides which input values should be KEPT
     *  and which input values should be DISCARDED. */
    private final Filter<GRAIN> filter;


    /**
     * <p>
     * Creates a new FilterOp operation to filter out input values.
     * </p>
     *
     * @param id The unique identifier of this operation.  Often
     *           the id is unique within a typing environment, but
     *           some Types may refine an operation, reusing the
     *           id but with a different behaviour specific to the
     *           type.  Must not be null.
     *
     * @param output_class The class of objects which can be output
     *                     by this operation.  Must not be null.
     *
     * @param none The default no-value value, in case the operation
     *             fails and the caller wants a fallback value.
     *             Must not be null.
     *
     * @param filter The filter which decides which input values will
     *               be KEPT and which input values will be DISCARDED.
     *               Must not be null.
     */
    public FilterOp (
                     String id,
                     Class<GRAIN> output_class,
                     GRAIN none,
                     Filter<GRAIN> filter
                     )
    {
        super ( id, output_class, none );

        FilterOp.classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                        filter );

        this.filter = filter;
    }


    /**
     * @see musaico.foundation.types.Operation#evaluate(musaico.foundation.types.Value)
     */
    @Override
    public Value<GRAIN> evaluate (
                                  Value<GRAIN> input
                                  )
    {
        if ( input instanceof NoValue )
        {
            return input;
        }

        final ValueBuilder<GRAIN> builder =
            new ValueBuilder<GRAIN> ( this.outputClass (), this.none () );
        for ( GRAIN grain : input )
        {
            final Filter.State filter_state = this.filter.filter ( grain );
            if ( filter_state == Filter.State.KEPT )
            {
                builder.add ( grain );
            }
        }

        return builder.build ();
    }
}
