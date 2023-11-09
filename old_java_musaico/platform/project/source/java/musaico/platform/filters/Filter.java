package musaico.platform.filter;

import java.io.Serializable;


/**
 * <p>
 * A Filter is used to keep or discard (filter out) objects.
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
public interface Filter<GRAIN extends Object>
    extends Serializable
{
    /**
     * The result of a filter operation.
     */
    public static enum State
    {
        /** Filter state: kept the object (not filtered out). */
        KEPT,

        /** Filter state: filtered out the object. */
        DISCARDED;
    }


    /**
     * <p>
     * Returns Filter.State.KEPT if the specified grain has
     * been kept by this Filter, or Filter.State.DISCARDED
     * if the specified grain has been filtered out.
     * </p>
     *
     * @param grain The object to filter.  Must not be null.
     *
     * @return Filter.State.KEPT if the object is kept;
     *         Filter.State.DISCARDED if the object is filtered out.
     *
     * @throws I18nIllegalArgumentException If the grain is null.
     */
    public abstract Filter.State filter (
                                         GRAIN grain
                                         );
}
