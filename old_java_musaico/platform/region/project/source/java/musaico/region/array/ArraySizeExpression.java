package musaico.region.array;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.region.Size;
import musaico.region.SizeExpression;


/**
 * <p>
 * Manipulates array sizes.
 * </p>
 *
 *
 * <p>
 * In Java, every SizeExpression must implement Serializable.
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
public class ArraySizeExpression
    implements SizeExpression, Serializable
{
    /** The first ArraySize term of the expression. */
    private final ArraySize arraySize;


    /**
     * <p>
     * Creates a new ArraySizeExpression with the specified
     * first term.
     * </p>
     *
     * @param array_size The first term of the expression.
     *                   Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the specified
     *                                      parameter(s) is invalid
     *                                      (see above).
     */
    public ArraySizeExpression (
                                ArraySize array_size
                                )
        throws I18nIllegalArgumentException
    {
        if ( array_size == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create an ArraySizeExpression with array size [%array_size%]",
                                                     "array_size", array_size );
        }

        this.arraySize = array_size;
    }


    /**
     * @see musaico.region.SizeExpression#add(musaico.region.Size)
     */
    @Override
    public ArraySizeExpression add (
                                    Size that_size
                                    )
    {
        if ( ! ( that_size instanceof ArraySize ) )
        {
            return this.arraySize.space ()
                .expr ( this.arraySize.space ().none () );
        }

        ArraySize that = (ArraySize) that_size;

        long this_length = this.arraySize.length ();
        long that_length = that.length ();
        if ( that_length < 0L
             || this_length < 0L
             || ( Long.MAX_VALUE - this_length ) < that_length )
        {
            return this.arraySize.space ()
                .expr ( this.arraySize.space ().none () );
        }

        ArraySize added_size =
            new ArraySize ( this_length + that_length );
        return this.arraySize.space ().expr ( added_size );
    }


    /**
     * @see musaico.region.SizeExpression#divide(double)
     */
    @Override
    public ArraySizeExpression divide (
                                       double divisor
                                       )
    {
        if ( divisor <= 0D )
        {
            return this.arraySize.space ()
                .expr ( this.arraySize.space ().none () );
        }

        long length = this.arraySize.length ();
        double min_divisor = (double) length / (double) Long.MAX_VALUE;

        if ( divisor < min_divisor )
        {
            return this.arraySize.space ()
                .expr ( this.arraySize.space ().none () );
        }

        long divided_length = (long) ( length / divisor );
        ArraySize divided_size = new ArraySize ( divided_length );
        return this.arraySize.space ().expr ( divided_size );
    }


    /**
     * @see musaico.region.SizeExpression#modulo(musaico.region.Size)
     */
    @Override
    public ArraySizeExpression modulo (
                                       Size that_size
                                       )
    {
        if ( ! ( that_size instanceof ArraySize ) )
        {
            return this.arraySize.space ()
                .expr ( this.arraySize.space ().none () );
        }

        ArraySize that = (ArraySize) that_size;

        long this_length = this.arraySize.length ();
        long that_length = that.length ();

        long modulo = this_length % that_length;
        ArraySize modulo_size = new ArraySize ( modulo );
        return this.arraySize.space ().expr ( modulo_size );
    }


    /**
     * @see musaico.region.SizeExpression#multiply(double)
     */
    @Override
    public ArraySizeExpression multiply (
                                         double factor
                                         )
    {
        if ( factor < 0D )
        {
            return this.arraySize.space ()
                .expr ( this.arraySize.space ().none () );
        }

        long length = this.arraySize.length ();
        double max_factor = (double) Long.MAX_VALUE / (double) length;

        if ( factor > max_factor )
        {
            return this.arraySize.space ()
                .expr ( this.arraySize.space ().none () );
        }

        long multiplied_length = (long) ( factor * length );
        ArraySize multiplied_size = new ArraySize ( multiplied_length );
        return this.arraySize.space ().expr ( multiplied_size );
    }


    /**
     * @see musaico.region.SizeExpression#ratio(musaico.region.Size)
     */
    @Override
    public double ratio (
                         Size that_size
                         )
    {
        if ( ! ( that_size instanceof ArraySize ) )
        {
            return Double.NaN;
        }

        ArraySize that = (ArraySize) that_size;

        long this_length = this.arraySize.length ();
        long that_length = that.length ();

        double ratio = (double) this_length / (double) that_length;
        return ratio;
    }


    /**
     * @see musaico.region.SizeExpression#size()
     */
    @Override
    public ArraySize size ()
    {
        return this.arraySize;
    }


    /**
     * @see musaico.region.SizeExpression#subtract(musaico.region.Size)
     */
    @Override
    public ArraySizeExpression subtract (
                                         Size that_size
                                         )
    {
        if ( ! ( that_size instanceof ArraySize ) )
        {
            return this.arraySize.space ()
                .expr ( this.arraySize.space ().none () );
        }

        ArraySize that = (ArraySize) that_size;

        long this_length = this.arraySize.length ();
        long that_length = that.length ();
        if ( that_length < 0L
             || this_length < 0L
             || this_length < that_length )
        {
            return this.arraySize.space ()
                .expr ( this.arraySize.space ().none () );
        }

        ArraySize subtracted_size =
            new ArraySize ( this_length - that_length );
        return this.arraySize.space ().expr ( subtracted_size );
    }
}
