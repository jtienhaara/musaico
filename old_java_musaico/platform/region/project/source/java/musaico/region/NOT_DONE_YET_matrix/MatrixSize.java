package musaico.region.array;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Order;

import musaico.region.Size;


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
public class ArraySize
    implements Size, Serializable
{
    /** Size of 0 array indices. */
    public static final ArraySize NONE = new ArraySize ( 0L );

    /** Size of exactly 1 array index. */
    public static final ArraySize ONE = new ArraySize ( 1L );


    /** The length of the array. */
    private final long length;


    /**
     * <p>
     * Creates a new ArraySize with the specified length.
     * </p>
     *
     * @param length The length for this array size.
     *               Must be 0 or greater.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above for details).
     */
    public ArraySize (
                      long length
                      )
        throws I18nIllegalArgumentException
    {
        if ( length < 0L )
        {
            throw new I18nIllegalArgumentException ( "Cannot create an ArraySize of length [%length%]",
                                                     "length", length );
        }

        this.length = length;
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
        else if ( ! ( obj instanceof ArraySize ) )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }

        ArraySize that = (ArraySize) obj;
        if ( this.length () == that.length () )
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
        long hash_code = this.length () >> 32;
        return (int) hash_code;
    }


    /**
     * <p>
     * Returns the length of the array represented by this
     * ArraySize.
     * </p>
     *
     * @return The length of the array.  Always 0 or greater.
     */
    public long length ()
    {
        return this.length;
    }


    /**
     * @see musaico.io.NaturallyOrdered#orderIndex()
     */
    @Override
    public ArraySize orderIndex ()
    {
        return this;
    }


    /**
     * @see musaico.io.NaturallyOrdered#order()
     */
    @Override
    public Order<Size> order ()
    {
        return ArraySizeOrder.DEFAULT;
    }


    /**
     * @see musaico.region.Size#space()
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
        return "[Length: " + this.length () + "]";
    }
}
