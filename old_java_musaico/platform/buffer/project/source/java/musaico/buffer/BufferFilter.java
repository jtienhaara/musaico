package musaico.buffer;


import java.io.Serializable;


import musaico.io.Filter;


/**
 * <p>
 * Filters the Positions of a specific Buffer, typically
 * by inspecting the Field at each Position.
 * </p>
 *
 *
 * <p>
 * In Java, every BufferFilter must be Serializable in
 * order to play nicely over RMI.
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
 * Copyright (c) 2010 Johann Tienhaara
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
public interface BufferFilter
    extends Filter, Serializable
{
    /**
     * <p>
     * Returns the Buffer over which Positions will
     * be filtered by some criteria.
     * </p>
     *
     * @return The Buffer used for filtering out Positions.
     *         Never null.  Note that Positions which are
     *         not present in this Buffer will always be
     *         filtered out.
     */
    public abstract Buffer buffer ();
}
