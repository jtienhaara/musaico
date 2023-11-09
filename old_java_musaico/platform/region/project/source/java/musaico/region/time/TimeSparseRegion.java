package musaico.region.time;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Comparison;
import musaico.io.Order;

import musaico.region.Position;
import musaico.region.PositionIterator;
import musaico.region.PositionExpression;
import musaico.region.Region;
import musaico.region.RegionalPositionExpression;
import musaico.region.RegionOrder;
import musaico.region.Search;
import musaico.region.SparseRegion;
import musaico.region.SpecificPosition;

import musaico.time.Time;


/**
 * <p>
 * A time region with holes in it.
 * </p>
 *
 * <p>
 * For example, a sparse time region might cover
 * the ranges of indices { 0 - 10 }, { 20 -32 }
 * and { 42 - 99 }, but not the gaps in between.
 * </p>
 *
 *
 * <p>
 * In Java, every Region must be Serializable in order to
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
public class TimeSparseRegion
    extends TimeRegion
    implements SparseRegion, Serializable
{
    /** The space in which this region resides, defining the
     *  interval between time positions and so on. */
    private final TimeSpace space;

    /** The sorted TimeRegions covered by this sparse time region.
     *  None of these is a sparse region! */
    private final TimeRegion [] subRegions;

    /** The start of the first sub-region (if any). */
    private final TimePosition start;

    /** The end of the last sub-region (if any). */
    private final TimePosition end;

    /** The size of this sub-region, NOT including holes. */
    private final TimeSize size;


    /**
     * <p>
     * Creates a new TimeSparseRegion from the specified
     * TimeRegion(s).
     * </p>
     *
     * @param space The space in which this time position was created,
     *              such as an absolute time space with daily
     *              time divisions.  Must not be null.
     *
     * @param sub_regions The sub-regions, possibly separated by holes,
     *                    which comprise this TimeSparseRegion.
     *                    May contain sparse regions.  May be unsorted.
     *                    This constructor takes care of flattening
     *                    the sub regions and sorting them.
     *                    Must not be null.  Must not contain any null
     *                    elements.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public TimeSparseRegion (
                             TimeSpace space,
                             TimeRegion [] sub_regions
                             )
        throws I18nIllegalArgumentException
    {
        // Don't rely on any of the super-class's methods, we override
        // all of them!
        super ( space.outOfBounds (),
                space.outOfBounds () );

        this.space = space;

        final List<TimeRegion> flattened_sub_regions =
            new ArrayList<TimeRegion> ();

        final boolean is_sub_regions_valid;
        if ( sub_regions == null )
        {
            is_sub_regions_valid = false;
        }
        else
        {
            boolean is_ok = true;
            for ( int sr = 0; sr < sub_regions.length; sr ++ )
            {
                if ( sub_regions [ sr ] == null
                     || ! this.space.equals ( sub_regions [ sr ].space () ) )
                {
                    // Null sub-region, or sub-region in a totally
                    // different TimeSpace.
                    is_ok = false;
                    break;
                }
                else if ( ! ( sub_regions [ sr ] instanceof TimeSparseRegion ) )
                {
                    // Flat TimeRegion.
                    flattened_sub_regions.add ( sub_regions [ sr ] );
                }
                else
                {
                    TimeSparseRegion sparse_sub_region =
                        (TimeSparseRegion) sub_regions [ sr ];
                    for ( long ssr = 0L;
                          ssr < sparse_sub_region.numRegions ();
                          ssr ++ )
                    {
                        TimeRegion sub_sub_region =
                            sparse_sub_region.region ( ssr );
                        flattened_sub_regions.add ( sub_sub_region );
                    }
                }
            }

            is_sub_regions_valid = is_ok;
        }

        if ( ! is_sub_regions_valid )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a TimeSparseRegion with sub regions [%sub_regions%]",
                                                     "sub_regions", sub_regions );
        }

        // Sort the sub-regions and merge any that overlap.
        Collections.sort ( flattened_sub_regions, RegionOrder.DEFAULT );

        List<TimeRegion> final_sub_regions = new ArrayList<TimeRegion> ();
        TimeSize total_duration = this.space.none ();
        TimeRegion last_sub_region = null;
        Order<Position> order = this.space.order ();
        for ( TimeRegion sub_region : flattened_sub_regions )
        {
            if ( last_sub_region != null )
            {
                if ( order.compareValues ( sub_region.start (),
                                           last_sub_region.end () )
                     .isIn ( Comparison.LEFT_LESS_THAN_RIGHT,
                             Comparison.LEFT_EQUALS_RIGHT ) )
                {
                    // Overlapping regions.  Don't bother
                    // adding the previous one just yet.
                    if ( order.compareValues ( last_sub_region.end (),
                                               sub_region.end () )
                         .isIn ( Comparison.LEFT_GREATER_THAN_RIGHT,
                                 Comparison.LEFT_EQUALS_RIGHT ) )
                    {
                        // Just ignore this sub-region completely,
                        // the previous one engulfs it.
                    }
                    else
                    {
                        // Create a new merged region from the last
                        // sub-region and this sub-region.
                        TimePosition start = last_sub_region.start ();
                        TimePosition end = sub_region.end ();
                        last_sub_region = new TimeRegion ( start, end );
                    }
                }
                else
                {
                    // This sub_region and last_sub_region do not overlap.
                    // Add the last_sub_region to the finalized flattened
                    // list.
                    final_sub_regions.add ( last_sub_region );
                    total_duration = this.space.expr ( total_duration )
                        .add ( last_sub_region.size () ).size ();
                    last_sub_region = sub_region;
                }
            }
            else
            {
                // last_sub_region was null, nothing to merge yet.
                last_sub_region = sub_region;
            }
        }

        if ( last_sub_region != null )
        {
            final_sub_regions.add ( last_sub_region );
            total_duration = this.space.expr ( total_duration )
                .add ( last_sub_region.size () ).size ();
        }

        // Now finalize the time.
        TimeRegion [] template =
            new TimeRegion [ final_sub_regions.size () ];
        this.subRegions = final_sub_regions.toArray ( template );

        if ( this.subRegions.length > 0 )
        {
            // At least 1 sub-region.
            this.start = this.subRegions [ 0 ].start ();
            this.end = this.subRegions [ this.subRegions.length - 1 ].end ();
            this.size = total_duration;
        }
        else
        {
            // Empty sparse region.
            this.start = this.space ().outOfBounds ();
            this.end = this.space ().outOfBounds ();
            this.size = this.space ().none ();
        }
    }


    /**
     * @see musaico.region.TimeRegion#contains(musaico.region.Position)
     */
    @Override
    public boolean contains (
                             Position position
                             )
    {
        if ( ! ( position instanceof TimePosition ) )
        {
            return false;
        }

        TimePosition time_position = (TimePosition) position;
        Time time = time_position.time ();
        Time start_time = this.start ().time ();
        Time end_time = this.end ().time ();
        Comparison start_comparison =
            Order.TIME.compareValues ( time, start_time );
        Comparison end_comparison =
            Order.TIME.compareValues ( time, end_time );
        if ( start_comparison.equals ( Comparison.LEFT_EQUALS_RIGHT )
             || end_comparison.equals ( Comparison.LEFT_EQUALS_RIGHT ) )
        {
            return true;
        }
        else if ( ! start_comparison.equals ( Comparison.LEFT_GREATER_THAN_RIGHT )
                  || ! end_comparison.equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            return false;
        }

        // Binary search.
        Search search =
            this.search ( new SpecificPosition ( position ) );
        long sub_region_index =
            search.findSubRegionIndex ();
        if ( sub_region_index >= 0L )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.region.SparseRegion#containsRegion(musaico.region.Region)
     */
    @Override
    public boolean containsRegion (
                                   Region region
                                   )
    {
        Search search =
            this.search ( new SpecificPosition ( region.start () ) );
        long sub_region_index = search.findSubRegionIndex ();
        if ( sub_region_index < 0L
             || sub_region_index >= this.subRegions.length )
        {
            return false;
        }

        Region sub_region = this.subRegions [ (int) sub_region_index ];
        if ( sub_region.equals ( region ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.region.TimeRegion#end()
     */
    @Override
    public TimePosition end ()
    {
        return this.end;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( ! ( obj instanceof TimeRegion ) )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }

        long num_regions = this.numRegions ();
        if ( ! ( obj instanceof TimeSparseRegion ) )
        {
            if ( num_regions > 1L )
            {
                return false;
            }

            TimeRegion that = (TimeRegion) obj;
            if ( this.start ().equals ( that.start () )
                 && this.end ().equals ( that.end () ) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        TimeSparseRegion that = (TimeSparseRegion) obj;
        if ( num_regions != that.numRegions () )
        {
            return false;
        }

        for ( long r = 0L; r < num_regions; r ++ )
        {
            Region this_sub_region = this.region ( r );
            Region that_sub_region = that.region ( r );
            if ( ! this_sub_region.equals ( that_sub_region ) )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * @see musaico.region.Region#expr(musaico.region.Position)
     */
    @Override
    public PositionExpression expr (
                                    Position position
                                    )
    {
        if ( ! ( position instanceof TimePosition ) )
        {
            return new RegionalPositionExpression ( this.space ().outOfBounds (),
                                                    this );
        }

        return new RegionalPositionExpression ( position, this );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode ()
    {
        int hash_code = this.start ().time ().hashCode ();
        hash_code += this.end ().time ().hashCode ();
        hash_code += (int) ( 72073L * this.numRegions () );
        return hash_code;
    }


    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Position> iterator ()
    {
        return new PositionIterator ( this );
    }


    /**
     * @see musaico.region.SparseRegion#numRegions()
     */
    @Override
    public long numRegions ()
    {
        return (long) this.subRegions.length;
    }


    /**
     * @see musaico.region.SparseRegion#region(long)
     */
    @Override
    public TimeRegion region (
                              long region_index
                              )
    {
        if ( region_index < 0L
             || region_index >= this.subRegions.length )
        {
            return this.space ().empty ();
        }

        return this.subRegions [ (int) region_index ];
    }

    /**
     * @see musaico.region.TimeRegion#size()
     */
    @Override
    public TimeSize size ()
    {
        return this.size;
    }


    /**
     * @see musaico.region.TimeRegion#space()
     */
    @Override
    public TimeSpace space ()
    {
        return this.space;
    }


    /**
     * @see musaico.region.TimeRegion#start()
     */
    @Override
    public TimePosition start ()
    {
        return this.start;
    }
}
