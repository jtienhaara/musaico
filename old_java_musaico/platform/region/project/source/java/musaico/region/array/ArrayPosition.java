package musaico.region.array;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.io.Order;

import musaico.region.Position;


/**
 * <p>
 * An indexed position within an array (0, 1, 2, ...).
 * </p>
 *
 *
 * <p>
 * In Java every Position must be Serializable in order to play
 * nicely over RMI.
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
public class ArrayPosition
    implements Position, Serializable
{
    /** The out-of-bounds array position. */
    public static final ArrayPosition OUT_OF_BOUNDS =
        new ArrayPosition ();


    /** This array position index (0, 1, 2, 3, ..., size - 1). */
    private final long index;


    /**
     * <p>
     * Creates a new ArrayPosition with the specified index into
     * an array.
     * </p>
     *
     * @param index The index into some array to which this
     *              array position points (0, 1, 2, 3, ..., size - 1 ).
     *              Must be 0 or greater.
     *
     * @throws I18nIllegalArgumentException If the parameter(s)
     *                                      specified are invalid
     *                                      (see above for details).
     */
    public ArrayPosition (
                          long index
                          )
        throws I18nIllegalArgumentException
    {
        if ( index < 0L )
        {
            throw new I18nIllegalArgumentException ( "Cannot create an ArrayPosition at index [%index%]",
                                                     "index", index );
        }

        this.index = index;
    }


    /**
     * <p>
     * Createa a new out-of-bounds array position (-1).
     * </p>
     */
    private ArrayPosition ()
    {
        this.index = -1L;
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
        else if ( ! ( obj instanceof ArrayPosition ) )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }

        ArrayPosition that = (ArrayPosition) obj;
        if ( this.index () == that.index () )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode ()
    {
        long hash_code = this.index () >> 32;
        return (int) hash_code;
    }


    /**
     * <p>
     * Returns the index of this position (into an array and so on).
     * </p>
     *
     * @return This array position index (0, 1, 2, 3, ..., size - 1).
     *         Always 0 or greater.
     */
    public long index ()
    {
        return this.index;
    }


    /**
     * @see musaico.region.Position#space()
     */
    @Override
    public ArraySpace space ()
    {
        return ArraySpace.STANDARD;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "ArrayIndex(" + this.index () + ")";
    }
}
