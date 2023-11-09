package musaico.region.array;

import java.io.Serializable;


import musaico.region.Position;
import musaico.region.PositionExpression;
import musaico.region.Region;
import musaico.region.Size;
import musaico.region.SparseRegion;


/**
 * <p>
 * Manipulates array positions and creates sizes and
 * regions from them.
 * </p>
 *
 * <p>
 * For example, the size of array index 17 - array index 3
 * is 15 (3, 4, 5, 6, ..., 15, 16, 17).
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
public class ArrayPositionExpression
    implements PositionExpression, Serializable
{
    /** The position from which the expression started. */
    private final ArrayPosition arrayPosition;


    /**
     * <p>
     * Creates a new ArrayPositionExpression, taking the
     * specified term as the start of the expression.
     * </p>
     *
     * @param array_position The position from which to start
     *                       the expression.  Must not be null.
     */
    public ArrayPositionExpression (
                                     ArrayPosition array_position
                                     )
    {
        this.arrayPosition = array_position;
    }


    /**
     * @see musaico.region.PositionExpression#add(musaico.region.Size)
     */
    @Override
    public ArrayPositionExpression add (
                                        Size size
                                        )
    {
        if ( ! ( size instanceof ArraySize ) )
        {
            return this.arrayPosition.space ()
                .expr ( this.arrayPosition.space ().outOfBounds () );
        }

        ArraySize array_size = (ArraySize) size;

        long index = this.arrayPosition.index ();
        long length = array_size.length ();

        if ( index < 0L
             || length < 0L )
        {
            return this.arrayPosition.space ()
                .expr ( this.arrayPosition.space ().outOfBounds () );
        }
        else if ( index > 0L
                  && length > 0L
                  && length > ( Long.MAX_VALUE - index ) )
        {
            return this.arrayPosition.space ()
                .expr ( this.arrayPosition.space ().outOfBounds () );
        }

        ArrayPosition added_position = new ArrayPosition ( index - length );
        return this.arrayPosition.space ().expr ( added_position );
    }


    /**
     * @see musaico.region.PositionExpression#modulo(musaico.region.Size)
     */
    @Override
    public ArraySizeExpression modulo (
                                       Size size
                                       )
    {
        if ( ! ( size instanceof ArraySize ) )
        {
            return this.arrayPosition.space ()
                .expr ( this.arrayPosition.space ().none () );
        }

        ArraySize array_size = (ArraySize) size;

        long index = this.arrayPosition.index ();
        long length = array_size.length ();

        if ( index < 0L
             || length < 0L )
        {
            return this.arrayPosition.space ()
                .expr ( this.arrayPosition.space ().none () );
        }

        long mod_length = index % length;

        ArraySize mod_size = new ArraySize ( mod_length );

        return this.arrayPosition.space ().expr ( mod_size );
    }


    /**
     * @see musaico.region.PositionExpression#next()
     */
    @Override
    public ArrayPosition next ()
    {
        if ( this.arrayPosition.index () >= Long.MAX_VALUE )
        {
            return this.arrayPosition.space ().outOfBounds ();
        }
        else
        {
            return new ArrayPosition ( this.arrayPosition.index () + 1L );
        }
    }


    /**
     * @see musaico.region.PositionExpression#position()
     */
    @Override
    public ArrayPosition position ()
    {
        return this.arrayPosition;
    }


    /**
     * @see musaico.region.PositionExpression#previous()
     */
    @Override
    public ArrayPosition previous ()
    {
        if ( this.arrayPosition.index () <= 0L )
        {
            return this.arrayPosition.space ().outOfBounds ();
        }
        else
        {
            return new ArrayPosition ( this.arrayPosition.index () - 1L );
        }
    }


    /**
     * @see musaico.region.PositionExpression#subtract(musaico.region.Position)
     */
    @Override
    public ArraySizeExpression subtract (
                                         Position that_position
                                         )
    {
        if ( ! ( that_position instanceof ArrayPosition ) )
        {
            return this.arrayPosition.space ()
                .expr ( this.arrayPosition.space ().none () );
        }

        ArrayPosition that = (ArrayPosition) that_position;

        long this_index = this.arrayPosition.index ();
        long that_index = that.index ();

        if ( this_index < that_index
             || this_index < 0L
             || that_index < 0L )
        {
            return this.arrayPosition.space ()
                .expr ( this.arrayPosition.space ().none () );
        }

        ArraySize subtracted_size =
            new ArraySize ( this_index - that_index );
        return this.arrayPosition.space ().expr ( subtracted_size );
    }


    /**
     * @see musaico.region.PositionExpression#subtract(musaico.region.Size)
     */
    @Override
    public ArrayPositionExpression subtract (
                                             Size size
                                             )
    {
        if ( ! ( size instanceof ArraySize ) )
        {
            return this.arrayPosition.space ()
                .expr ( this.arrayPosition.space ().outOfBounds () );
        }

        ArraySize array_size = (ArraySize) size;

        long index = this.arrayPosition.index ();
        long length = array_size.length ();

        if ( index < length
             || index < 0L
             || length < 0L )
        {
            return this.arrayPosition.space ()
                .expr ( this.arrayPosition.space ().outOfBounds () );
        }

        ArrayPosition subtracted_position =
            new ArrayPosition ( index - length );
        return this.arrayPosition.space ().expr ( subtracted_position );
    }


    /**
     * @see musaico.region.PositionExpression#to(musaico.region.Position)
     */
    @Override
    public RegionExpression to (
                                Position that_position
                                )
    {
        if ( ! ( that_position instanceof ArrayPosition ) )
        {
            return this.arrayPosition.space ()
                .expr ( this.arrayPosition.space ().empty () );
        }

        ArrayPosition that = (ArrayPosition) that_position;
        long this_index = this.arrayPosition.index ();
        long that_index = that.index ();
        if ( that_index < 0L
             || this_index < 0L
             || this_index > that_index )
        {
            return this.arrayPosition.space ()
                .expr ( this.arrayPosition.space ().empty () );
        }

        ArrayRegion region =
            new ArrayRegion ( this.arrayPosition, that );
        return this.arrayPosition.space ().expr ( region );
    }
}
