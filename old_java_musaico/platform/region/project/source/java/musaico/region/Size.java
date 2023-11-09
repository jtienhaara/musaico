package musaico.region;

import java.io.Serializable;


import musaico.io.NaturallyOrdered;


/**
 * <p>
 * !!!
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
 * Copyright (c) 2011 Johann Tienhaara
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
public interface Size
    extends NaturallyOrdered<Size>, Serializable
{
    // Every Size must implement
    // java.lang.Object#equals(java.lang.Object).

    // Every Size must implement java.lang.Object#hashCode().

    // Every Size must return itself from
    // musaico.io.NaturallyOrdered#order().

    // Every Size must implement musaico.io.NaturallyOrdered#order().

    public abstract Space space ();
}
