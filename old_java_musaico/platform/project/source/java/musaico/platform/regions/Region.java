package musaico.region;

import java.io.Serializable;


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
 * Copyright (c) 2011, 2012 Johann Tienhaara
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
public interface Region
    extends Iterable<Position>, Serializable
{
    public abstract boolean contains (
                                      Position position
                                      );

    public abstract Position end ();

    // Every Region must implement
    // java.lang.Object#equals(java.lang.Object).

    public abstract PositionExpression expr (
                                             Position position
                                             );

    // Every Region must implement java.lang.Object#hashCode().

    // Every Region must implement java.lang.Iterable#iterator().

    public abstract Size size ();

    public abstract Search search (
                                   Criterion criterion
                                   );

    public abstract Space space ();

    public abstract Position start ();
}
