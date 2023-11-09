package musaico.region.array;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Order;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.RegionExpression;
import musaico.region.Size;
import musaico.region.Space;
import musaico.region.SparseRegionBuilder;
import musaico.region.StandardRegionExpression;


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
public class ArraySpace
    implements Space, Serializable
{
    /** Standard array space. */
    public static final ArraySpace STANDARD = new ArraySpace ();


    /**
     * @see musaico.region.Space#all()
     */
    @Override
    public ArrayRegion all ()
    {
        return this.region ( this.min (), this.max () );
    }


    /**
     * @see musaico.region.Space#empty()
     */
    @Override
    public ArrayRegion empty ()
    {
        return ArrayRegion.EMPTY;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( ! ( obj instanceof ArraySpace ) )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }

        ArraySpace that = (ArraySpace) obj;
        if ( ! this.min ().equals ( that.min () )
             || ! this.max ().equals ( that.max () ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.region.Space#expr(musaico.region.Position)
     */
    @Override
    public ArrayPositionExpression expr (
                                         Position position
                                         )
    {
        final ArrayPosition array_position;
        if ( position instanceof ArrayPosition )
        {
            array_position = (ArrayPosition) position;
        }
        else
        {
            array_position = ArrayPosition.OUT_OF_BOUNDS;
        }

        return new ArrayPositionExpression ( array_position );
    }


    /**
     * @see musaico.region.Space#expr(musaico.region.Region)
     */
    @Override
    public RegionExpression expr (
                                  Region region
                                  )
    {
        final ArrayRegion array_region;
        if ( region instanceof ArrayRegion )
        {
            array_region = (ArrayRegion) region;
        }
        else
        {
            array_region = this.empty ();
        }

        return new StandardRegionExpression ( array_region );
    }


    /**
     * @see musaico.region.Space#expr(musaico.region.Size)
     */
    @Override
    public ArraySizeExpression expr (
                                     Size size
                                     )
    {
        final ArraySize array_size;
        if ( size instanceof ArraySize )
        {
            array_size = (ArraySize) size;
        }
        else
        {
            array_size = this.none ();
        }

        return new ArraySizeExpression ( array_size );
    }


    /**
     * @see musaico.region.Space#from(musaico.region.Position)
     *
     * Final for speed.
     */
    @Override
    public final Position from (
                                Position that_position
                                )
    {
        if ( that_position == null )
        {
            return this.outOfBounds ();
        }

        Space that = that_position.space ();
        if ( that == this )
        {
            // Only if the references are identical can
            // we return that_position directly.
            return that_position;
        }
        else if ( this.equals ( that )
                  && ( that_position instanceof ArrayPosition ) )
        {
            // Exact same space, but different reference.
            // Recreate the position to point to our space.
            ArrayPosition that_array_position = (ArrayPosition)
                that_position;
            ArrayPosition this_position =
                this.position ( that_array_position.index () );
            return this_position;
        }

        Size that_offset_from_origin = that.expr ( that_position )
            .subtract ( that.origin () ).size ();

        Size this_offset_from_origin =
            this.from ( that_offset_from_origin );
        Position this_position = this.expr ( this.origin () )
            .add ( this_offset_from_origin ).position ();

        return this_position;
    }


    /**
     * @see musaico.region.Space#from(musaico.region.Region)
     *
     * Final for speed.
     */
    @Override
    public final Region from (
                              Region that_region
                              )
    {
        if ( that_region == null )
        {
            return this.empty ();
        }
        else if ( that_region.space () == this )
        {
            // Only if the references are identical can
            // we return that_region directly.
            return that_region;
        }

        // In case it is a SparseRegion, we don't try to match any
        // gaps.  We just make a region of equivalent size starting
        // at the equivalent start position.
        Position that_start = that_region.start ();
        Size that_size = that_region.size ();
        Size this_size = this.from ( that_size );
        Position this_start = this.from ( that_region.start () );
        Position this_end   =
            this.expr ( this_start ).add ( this_size ).previous ();
        Region this_region = this.region ( this_start, that_start );
        return this_region;
    }


    /**
     * @see musaico.region.Space#from(musaico.region.Size)
     *
     * Final for speed.
     */
    @Override
    public final Size from (
                            Size that_size
                            )
    {
        if ( that_size == null )
        {
            return this.none ();
        }

        Space that = that_size.space ();
        if ( that == this )
        {
            // Only if the references are identical can
            // we return that_size directly.
            return that_size;
        }
        else if ( this.equals ( that )
                  && ( that_size instanceof ArraySize ) )
        {
            // Exact same space, but different reference.
            // Recreate the size to point to our space.
            ArraySize that_array_size = (ArraySize)
                that_size;
            ArraySize this_size =
                this.size ( that_array_size.length () );
            return this_size;
        }

        double num_elements = that.expr ( that_size )
            .ratio ( that.one () );

        Size this_size = this.expr ( this.one () )
            .multiply ( num_elements ).size ();

        return this_size;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        long hash_code = this.min ().index ();
        hash_code += this.max ().index ();
        hash_code = hash_code >> 32;
        return (int) hash_code;
    }


    /**
     * @see musaico.region.Space#max()
     */
    @Override
    public ArrayPosition max ()
    {
        return this.position ( Long.MAX_VALUE );
    }


    /**
     * @see musaico.region.Space#min()
     */
    @Override
    public ArrayPosition min ()
    {
        return this.position ( 0L );
    }


    /**
     * @see musaico.region.Space#none()
     */
    @Override
    public ArraySize none ()
    {
        return ArraySize.NONE;
    }


    /**
     * @see musaico.region.Space#one()
     */
    @Override
    public ArraySize one ()
    {
        return ArraySize.ONE;
    }


    /**
     * @see musaico.region.Space#order()
     */
    @Override
    public Order<Position> order ()
    {
        return ArrayPositionOrder.DEFAULT;
    }


    /**
     * @see musaico.region.Space#origin()
     */
    @Override
    public ArrayPosition origin ()
    {
        return this.min ();
    }


    /**
     * @see musaico.region.Space#outOfBounds()
     */
    @Override
    public ArrayPosition outOfBounds ()
    {
        return ArrayPosition.OUT_OF_BOUNDS;
    }


    /**
     * <p>
     * Creates a new ArrayPosition with the specified index.
     * </p>
     *
     * @param index The index of the ArrayPosition to create.
     *              Must be 0 or greater.
     *
     * @return The newly created ArrayPosition.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see below).
     */
    public ArrayPosition position (
                                   long index
                                   )
        throws I18nIllegalArgumentException
    {
        // Let the ArrayPosition class throw the exception.
        return new ArrayPosition ( index );
    }


    /**
     * @see musaico.region.Space#region(musaico.region.Position,musaico.region.Position)
     */
    @Override
    public ArrayRegion region (
                               Position start,
                               Position end
                               )
    {
        if ( ! ( start instanceof ArrayPosition )
             || ! ( end instanceof ArrayPosition ) )
        {
            return this.empty ();
        }

        ArrayPosition array_start = (ArrayPosition) start;
        ArrayPosition array_end = (ArrayPosition) end;

        return new ArrayRegion ( array_start, array_end );
    }


    /**
     * <p>
     * Creates a new ArraySize from the specified array length.
     * </p>
     *
     * @param length The length of the array to create
     *               an ArraySize for.  Must be 0 or greater.
     *
     * @return The newly created ArraySize.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified are invalid
     *                                      (see above).
     */
    public ArraySize size (
                           long length
                           )
        throws I18nIllegalArgumentException
    {
        // Let the ArraySize class throw the exception if the
        // length parameter is invalid.
        return new ArraySize ( length );
    }


    /**
     * @see musaico.region.Space#sparseRegionBuilder()
     */
    @Override
    public SparseRegionBuilder sparseRegionBuilder ()
    {
        return new ArraySparseRegionBuilder ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "Space{" + this.min () + ".." + this.max () + "}";
    }
}
