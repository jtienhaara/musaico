package musaico.region.time;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.region.Region;
import musaico.region.SparseRegionBuilder;


/**
 * <p>
 * Creates sparse time regions.
 * </p>
 *
 * <p>
 * The TimeSparseRegionBuilder is not thread-safe.
 * If you need to build an TimeSparseRegion from
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
public class TimeSparseRegionBuilder
    implements SparseRegionBuilder, Serializable
{
    /** The space for which this builder creates time sparse regions. */
    private final TimeSpace space;

    /** The sub-Regions added so far. */
    private final List<TimeRegion> subRegions =
        new ArrayList<TimeRegion> ();


    /**
     * <p>
     * Creates a new TimeSparseRegionBuilder for the specified space.
     * </p>
     *
     * @param space The space for which sparse regions will be built.
     *              Must not be null.
     */
    public TimeSparseRegionBuilder (
                                    TimeSpace space
                                    )
    {
        this.space = space;
    }


    /**
     * @see musaico.region.time#SparseRegionBuilder#build()
     */
    @Override
    public TimeSparseRegion build ()
    {
        TimeRegion [] template = new TimeRegion [ 0 ];
        TimeRegion [] sub_regions = this.subRegions.toArray ( template );

        TimeSparseRegion sparse_region =
            new TimeSparseRegion ( space, sub_regions );

        return sparse_region;
    }


    /**
     * @see musaico.region.time#SparseRegionBuilder#concatenate(musaico.region.Region)
     */
    @Override
    public TimeSparseRegionBuilder concatenate (
                                                 Region sub_region
                                                 )
    {
        if ( sub_region instanceof TimeRegion )
        {
            TimeRegion sub_time_region = (TimeRegion) sub_region;
            this.subRegions.add ( sub_time_region );
        }

        // Ignore it if it's not an TimeRegion!

        return this;
    }
}
