package musaico.kernel.memory.paging;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;


import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * A simple hash map lookup of configuration Field - by - SwapState.
 * </p>
 *
 *
 * <p>
 * In Java every SwapConfiguration must be Serializable in order to
 * play nicely over RMI.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public class SimpleSwapConfiguration
    implements SwapConfiguration, Serializable
{
    /** The lookup of configuration data by swap state. */
    private final Map<SwapState,Field> configurations;


    /**
     * <p>
     * Creates a new SimpleSwapConfiguration with the specified
     * lookup of configuration Fields by swap states.
     * </p>
     *
     * <p>
     * For example, to create a swap configuration containing
     * a block driver for a swapped-out-to-block-driver swap state:
     * </p>
     *
     * <pre>
     *     SwapState block_swap_state = ...;
     *     BlockDriver block_driver = ...;
     *     FieldTypingEnvironment env = ...;
     *     Field block_driver_field = env.create ( "block_driver",
     *                                             Object.class,
     *                                             block_driver );
     *     Map<SwapState,Field> configurations =
     *         new HashMap<SwapState,Field> ();
     *     configurations.put ( block_swap_state, block_driver_field );
     *
     *     new SwapConfiguration ( configurations );
     * </pre>
     *
     * @param configurations The lookup of configuration fields by
     *                       swap states.  One field per swap state.
     *                       Must not be null.  Must not contain any
     *                       null keys.  Must not contain any null
     *                       values.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public SimpleSwapConfiguration (
				    Map<SwapState,Field> configurations
				    )
    {
	if ( configurations == null )
	{
	    throw new I18nIllegalArgumentException ( "Cannot create a SimpleSwapConfiguration with configurations [%configurations%]",
						     "configurations", configurations );
	}

	this.configurations = new HashMap<SwapState, Field> ();
	for ( SwapState swap_state : configurations.keySet () )
	{
	    Field configuration = configurations.get ( swap_state );

            if ( configuration == null )
            {
                throw new I18nIllegalArgumentException ( "Cannot create a SimpleSwapConfiguration with configurations [%configurations%]",
						         "configurations", configurations );
	    }

	    this.configurations.put ( swap_state, configuration );
	}
    }


    /**
     * @see musaico.kernel.memory.paging.SwapConfiguration#forSwapState(musaico.kernel.paging.SwapState)
     */
    public Field forSwapState (
			       SwapState swap_state
			       )
	throws I18nIllegalArgumentException
    {
	if ( swap_state == null )
	{
	    throw new I18nIllegalArgumentException ( "Cannot return configuration data for swap state [%swap_state%]",
						     "swap_state", swap_state );
	}

	Field configuration = this.configurations.get ( swap_state );

	if ( configuration == null )
	{
	    return Field.NULL;
	}

	return configuration;
    }
}
