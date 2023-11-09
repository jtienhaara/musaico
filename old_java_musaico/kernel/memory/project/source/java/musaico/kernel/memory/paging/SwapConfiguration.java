package musaico.kernel.memory.paging;

import java.io.Serializable;


import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Stores the configuration settings for a particular paged area
 * to be used by general-purpose SwapSystems (such as the BlockDriver
 * backing a swapped-out state for the paged area, and so on).
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
public interface SwapConfiguration
    extends Serializable
{
    /**
     * <p>
     * Returns the swap configuration setting for the specified
     * swap state.
     * </p>
     *
     * @param swap_state The swap state whose configuration data
     *                   shall be returned as a Field.
     *                   Must not be null.
     *
     * @return The swap configuration field for the specified swap state,
     *         or Field.NULL if this swap configuration does not store
     *         any configuration data for the specified swap state.
     *         Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract Field forSwapState (
					SwapState swap_state
					)
	throws I18nIllegalArgumentException;
}
