package musaico.region.array;

import java.io.Serializable;

import java.util.Iterator;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.region.Criterion;
import musaico.region.Position;
import musaico.region.PositionExpression;
import musaico.region.PositionIterator;
import musaico.region.Region;
import musaico.region.RegionalPositionExpression;
import musaico.region.Search;
import musaico.region.Size;

import musaico.region.search.BinarySearch;


/**
 * <p>
 * The region describing the indices of an array.
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
public class ArrayRegion
    implements Region, Serializable
{
    /** The empty array region. */
    public static final ArrayRegion EMPTY =
        new ArrayRegion ();


    /** The start index of this array region. */
    private final ArrayPosition start;

    /** The last index of this array region. */
    private final ArrayPosition end;

    /** The size of this array region. */
    private final ArraySize size;


    /**
     * <p>
     * Creates a new ArrayRegion with the specified start and end
     * Positions.
     * </p>
     *
     * @param start The starting position of this array region,
     *              such as ArrayPosition ( 0L ).  Must not be null.
     *
     * @param end The ending position of this array region,
     *            such as ArrayPosition ( 17L ).  Must not be null.
     *            Must be greater than the start position.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      specified are invalid
     *                                      (see above).
     */
    public ArrayRegion (
                        ArrayPosition start,
                        ArrayPosition end
                        )
        throws I18nIllegalArgumentException
    {
        if ( start == null
             || end == null
             || end.index () < start.index () )
        {
            throw new I18nIllegalArgumentException ( "Invalid array region [%start%] .. [%end%]",
                                                     "start", start,
                                                     "end", end );
        }

        this.start = start;
        this.end = end;
        this.size =
            new ArraySize ( this.end.index () - this.start.index () + 1L );
    }


    /**
     * <p>
     * Creates a new empty ArrayRegion.
     * </p>
     */
    private ArrayRegion ()
    {
        this.start = ArraySpace.STANDARD.outOfBounds ();
        this.end = ArraySpace.STANDARD.outOfBounds ();
        this.size =
            new ArraySize ( 0L );
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
        if ( index >= this.start ().index ()
             && index <= this.end ().index () )
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
        else if ( obj instanceof ArraySparseRegion
                  && ( (ArraySparseRegion) obj ).numRegions () > 1L )
        {
            return false;
        }

        ArrayRegion that = (ArrayRegion) obj;
        if ( ! this.start ().equals ( that.start () )
             || ! this.end ().equals ( that.end () ) )
        {
            return true;
        }
        else
        {
            return false;
        }
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
     * @see musaico.region.Space#search(musaico.region.Criterion)
     */
    public Search search (
                          Criterion criterion
                          )
    {
        return new BinarySearch ( this, criterion );
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


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "{" + this.start () + ".." + this.end () + "}";
    }
}
