package musaico.region;

import java.io.Serializable;


import musaico.io.Filter;


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
public interface RegionExpression
    extends Serializable
{
    public abstract SparseRegionBuilder concatenate (
                                                     Region that
                                                     );

    public abstract RegionExpression difference (
                                                 Region that
                                                 );

    public abstract RegionExpression filter (
                                             Filter<Position> filter
                                             );

    public abstract RegionExpression intersection (
                                                   Region that
                                                   );

    public abstract RegionExpression invert ();

    public abstract Region region ();

    public abstract RegionExpression scale (
                                            Size size
                                            );

    public abstract SparseRegion split (
                                        long into
                                        );

    public abstract SparseRegion split (
                                        Size size
                                        );

    public abstract RegionExpression subtract (
                                               Region that
                                               );

    public abstract RegionExpression union (
                                            Region that
                                            );
}
