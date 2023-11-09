package musaico.region.array;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.region.Region;
import musaico.region.SparseRegionBuilder;


/**
 * <p>
 * Creates sparse array regions.
 * </p>
 *
 * <p>
 * The ArraySparseRegionBuilder is not thread-safe.
 * If you need to build an ArraySparseRegion from
 * multiple threads concurrently, implement the necessary
 * locking yourself, or you'll get hit by concurrent
 * modification exceptions.
 * </p>
 *
 *
 * <p>
 * In Java every SparseRegionBuilder must be Serializable
 * in order to play nicely over RMI.
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
public class ArraySparseRegionBuilder
    implements SparseRegionBuilder, Serializable
{
    /** The sub-Regions added so far. */
    private final List<ArrayRegion> subRegions =
        new ArrayList<ArrayRegion> ();


    /**
     * @see musaico.region.array#SparseRegionBuilder#build()
     */
    @Override
    public ArraySparseRegion build ()
    {
        ArrayRegion [] template = new ArrayRegion [ 0 ];
        ArrayRegion [] sub_regions = this.subRegions.toArray ( template );

        ArraySparseRegion sparse_region =
            new ArraySparseRegion ( sub_regions );

        return sparse_region;
    }


    /**
     * @see musaico.region.array#SparseRegionBuilder#concatenate(musaico.region.Region)
     */
    @Override
    public ArraySparseRegionBuilder concatenate (
                                                 Region sub_region
                                                 )
    {
        if ( sub_region instanceof ArrayRegion )
        {
            ArrayRegion sub_array_region = (ArrayRegion) sub_region;
            this.subRegions.add ( sub_array_region );
        }

        // Ignore it if it's not an ArrayRegion!

        return this;
    }
}
