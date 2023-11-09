package musaico.region;

import java.io.Serializable;


import musaico.io.Order;


/**
 * <p>
 * The space in which a Region, Position, Size, and so on resides.
 * </p>
 *
 * <p>
 * For example, Regions, Positions and Sizes describing arrays
 * might be described with non-negative integers, whereas the Regions,
 * Positions and Sizes describing a 3-dimensional volume might
 * be described with 3-tuples doube[] positions in space.
 * </p>
 *
 *
 * <p>
 * In Java, every Space must be Serializable in order to play nicely
 * over RMI.
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
public interface Space
    extends Serializable
{
    public abstract Region all ();

    public abstract Region empty ();

    // Every Space must implement
    // java.lang.Object#equals(java.lang.Object).

    public abstract PositionExpression expr (
                                             Position position
                                             );

    public abstract RegionExpression expr (
                                           Region region
                                           );

    public abstract SizeExpression expr (
                                         Size size
                                         );

    public abstract Position from (
                                   Position that_position
                                   );

    public abstract Region from (
                                 Region that_region
                                 );

    public abstract Size from (
                               Size that_size
                               );

    // Every Space must implement java.lang.Object#hashCode().

    public abstract Position max ();

    public abstract Position min ();

    public abstract Size none ();

    public abstract Size one ();

    public abstract Order<Position> order ();

    public abstract Position origin ();

    public abstract Position outOfBounds ();

    public abstract Region region (
                                   Position start,
                                   Position end
                                   );

    public abstract SparseRegionBuilder sparseRegionBuilder ();
}
