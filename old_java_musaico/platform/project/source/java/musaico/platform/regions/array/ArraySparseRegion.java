package musaico.region.array;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.region.Position;
import musaico.region.PositionIterator;
import musaico.region.PositionExpression;
import musaico.region.Region;
import musaico.region.RegionalPositionExpression;
import musaico.region.RegionOrder;
import musaico.region.Search;
import musaico.region.SparseRegion;
import musaico.region.SpecificPosition;


/**
 * <p>
 * An array region with holes in it.
 * </p>
 *
 * <p>
 * For example, a sparse array region might cover
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
public class ArraySparseRegion
    extends ArrayRegion
    implements SparseRegion, Serializable
{
    /** The sorted ArrayRegions covered by this sparse array region.
     *  None of these is a sparse region! */
    private final ArrayRegion [] subRegions;

    /** The start of the first sub-region (if any). */
    private final ArrayPosition start;

    /** The end of the last sub-region (if any). */
    private final ArrayPosition end;

    /** The size of this sub-region, NOT including holes. */
    private final ArraySize size;


    /**
     * <p>
     * Creates a new ArraySparseRegion from the specified
     * ArrayRegion(s).
     * </p>
     *
     * @param sub_regions The sub-regions, possibly separated by holes,
     *                    which comprise this ArraySparseRegion.
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
    public ArraySparseRegion (
                              ArrayRegion [] sub_regions
                              )
        throws I18nIllegalArgumentException
    {
        // Don't rely on any of the super-class's methods, we override
        // all of them!
        super ( ArraySpace.STANDARD.outOfBounds (),
                ArraySpace.STANDARD.outOfBounds () );

        final List<ArrayRegion> flattened_sub_regions =
            new ArrayList<ArrayRegion> ();

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
                if ( sub_regions [ sr ] == null )
                {
                    is_ok = false;
                    break;
                }
                else if ( ! ( sub_regions [ sr ] instanceof ArraySparseRegion ) )
                {
                    // Flat ArrayRegion.
                    flattened_sub_regions.add ( sub_regions [ sr ] );
                }
                else
                {
                    ArraySparseRegion sparse_sub_region =
                        (ArraySparseRegion) sub_regions [ sr ];
                    for ( long ssr = 0L;
                          ssr < sparse_sub_region.numRegions ();
                          ssr ++ )
                    {
                        ArrayRegion sub_sub_region =
                            sparse_sub_region.region ( ssr );
                        flattened_sub_regions.add ( sub_sub_region );
                    }
                }
            }

            is_sub_regions_valid = is_ok;
        }

        if ( ! is_sub_regions_valid )
        {
            throw new I18nIllegalArgumentException ( "Cannot create an ArraySparseRegion with sub regions [%sub_regions%]",
                                                     "sub_regions", sub_regions );
        }

        // Sort the sub-regions and merge any that overlap.
        Collections.sort ( flattened_sub_regions, RegionOrder.DEFAULT );

        List<ArrayRegion> final_sub_regions = new ArrayList<ArrayRegion> ();
        long total_length = 0L;
        ArrayRegion last_sub_region = null;
        for ( ArrayRegion sub_region : flattened_sub_regions )
        {
            if ( last_sub_region != null )
            {
                long start_index = sub_region.start ().index ();
                if ( start_index <= last_sub_region.end ().index () )
                {
                    // Overlapping regions.  Don't bother
                    // adding the previous one just yet.
                    if ( last_sub_region.end ().index ()
                         >= sub_region.end ().index () )
                    {
                        // Just ignore this sub-region completely,
                        // the previous one engulfs it.
                    }
                    else
                    {
                        // Create a new merged region from the last
                        // sub-region and this sub-region.
                        ArrayPosition start = last_sub_region.start ();
                        ArrayPosition end = sub_region.end ();
                        last_sub_region = new ArrayRegion ( start, end );
                    }
                }
                else
                {
                    // This sub_region and last_sub_region do not overlap.
                    // Add the last_sub_region to the finalized flattened
                    // list.
                    final_sub_regions.add ( last_sub_region );
                    total_length += last_sub_region.size ().length ();
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
            total_length += last_sub_region.size ().length ();
        }

        // Now finalize the array.
        ArrayRegion [] template =
            new ArrayRegion [ final_sub_regions.size () ];
        this.subRegions = final_sub_regions.toArray ( template );

        if ( this.subRegions.length > 0 )
        {
            // At least 1 sub-region.
            this.start = this.subRegions [ 0 ].start ();
            this.end = this.subRegions [ this.subRegions.length - 1 ].end ();
            this.size = new ArraySize ( total_length );
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
     * @see musaico.region.ArrayRegion#contains(musaico.region.Position)
     */
    @Override
    public boolean contains (
                             Position position
                             )
    {
        if ( ! ( position instanceof ArrayPosition ) )
        {
            return false;
        }

        ArrayPosition array_position = (ArrayPosition) position;
        long index = array_position.index ();
        long start_index = this.start ().index ();
        long end_index = this.end ().index ();
        if ( index < start_index
             || index > end_index )
        {
            return false;
        }
        else if ( index == start_index
                  || index == end_index )
        {
            return true;
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
     * @see musaico.region.ArrayRegion#end()
     */
    @Override
    public ArrayPosition end ()
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
        else if ( ! ( obj instanceof ArrayRegion ) )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }

        long num_regions = this.numRegions ();
        if ( ! ( obj instanceof ArraySparseRegion ) )
        {
            if ( num_regions > 1L )
            {
                return false;
            }

            ArrayRegion that = (ArrayRegion) obj;
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

        ArraySparseRegion that = (ArraySparseRegion) obj;
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
        if ( ! ( position instanceof ArrayPosition ) )
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
        long hash_code = this.start ().index ();
        hash_code += this.end ().index ();
        hash_code += 72073L * this.numRegions ();
        hash_code = hash_code >> 32;
        return (int) hash_code;
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
    public ArrayRegion region (
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
     * @see musaico.region.ArrayRegion#size()
     */
    @Override
    public ArraySize size ()
    {
        return this.size;
    }


    /**
     * @see musaico.region.ArrayRegion#space()
     */
    @Override
    public ArraySpace space ()
    {
        return ArraySpace.STANDARD;
    }


    /**
     * @see musaico.region.ArrayRegion#start()
     */
    @Override
    public ArrayPosition start ()
    {
        return this.start;
    }
}
