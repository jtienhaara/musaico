package musaico.region.time;

import java.io.Serializable;

import java.math.BigDecimal;
import java.math.BigInteger;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Comparison;
import musaico.io.Order;

import musaico.region.Size;
import musaico.region.SizeExpression;

import musaico.time.RelativeTime;
import musaico.time.Time;


/**
 * <p>
 * Manipulates time sizes.
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
public class TimeSizeExpression
    implements SizeExpression, Serializable
{
    /** The first TimeSize term of the expression. */
    private final TimeSize timeSize;


    /**
     * <p>
     * Creates a new TimeSizeExpression with the specified
     * first term.
     * </p>
     *
     * @param time_size The first term of the expression.
     *                   Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the specified
     *                                      parameter(s) is invalid
     *                                      (see above).
     */
    public TimeSizeExpression (
                                TimeSize time_size
                                )
        throws I18nIllegalArgumentException
    {
        if ( time_size == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a TimeSizeExpression with time size [%time_size%]",
                                                     "time_size", time_size );
        }

        this.timeSize = time_size;
    }


    /**
     * @see musaico.region.SizeExpression#add(musaico.region.Size)
     */
    @Override
    public TimeSizeExpression add (
                                   Size that_size
                                   )
    {
        TimeSpace space = this.timeSize.space ();
        TimeSize none = space.none ();
        if ( ! ( that_size instanceof TimeSize ) )
        {
            return space.expr ( none );
        }

        TimeSize that = (TimeSize) that_size;

        RelativeTime this_duration = this.timeSize.duration ();
        RelativeTime that_duration = that.duration ();
        if ( Order.TIME.compareValues ( this_duration, none.duration () )
             .equals ( Comparison.LEFT_LESS_THAN_RIGHT )
             || Order.TIME.compareValues ( that_duration, none.duration () )
             .equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            return space.expr ( none );
        }

        Time added_duration = this_duration.add ( that_duration );
        if ( ! ( added_duration instanceof RelativeTime ) )
        {
            TimeSize added_size =
                new TimeSize ( space,
                               (RelativeTime) added_duration );
            return space.expr ( added_size );
        }
        else
        {
            // Overflow.
            return space.expr ( none );
        }
    }


    /**
     * @see musaico.region.SizeExpression#divide(double)
     */
    @Override
    public TimeSizeExpression divide (
                                      double divisor
                                      )
    {
        TimeSpace space = this.timeSize.space ();
        if ( divisor <= 0D )
        {
            return space.expr ( space.none () );
        }

        RelativeTime duration = this.timeSize.duration ();
        BigDecimal seconds_and_nanoseconds =
            new BigDecimal ( duration.secondsAndNanoseconds () );
        BigDecimal big_divisor = new BigDecimal ( divisor );
        BigInteger divided_seconds_and_nanoseconds =
            seconds_and_nanoseconds.divide ( big_divisor ).toBigInteger ();
        RelativeTime divided_time =
            new RelativeTime ( divided_seconds_and_nanoseconds );
        TimeSize divided_size = new TimeSize ( space, divided_time );

        return space.expr ( divided_size );
    }


    /**
     * @see musaico.region.SizeExpression#modulo(musaico.region.Size)
     */
    @Override
    public TimeSizeExpression modulo (
                                      Size that_size
                                      )
    {
        TimeSpace space = this.timeSize.space ();
        if ( ! ( that_size instanceof TimeSize ) )
        {
            return space.expr ( space.none () );
        }

        TimeSize that = (TimeSize) that_size;

        RelativeTime this_duration = this.timeSize.duration ();
        RelativeTime that_duration = that.duration ();

        BigInteger this_mod_that =
            this_duration.secondsAndNanoseconds ()
            .mod ( that_duration.secondsAndNanoseconds () );
        RelativeTime mod_time = new RelativeTime ( this_mod_that );
        TimeSize mod_size = new TimeSize ( space, mod_time );

        return space.expr ( mod_size );
    }


    /**
     * @see musaico.region.SizeExpression#multiply(double)
     */
    @Override
    public TimeSizeExpression multiply (
                                        double factor
                                        )
    {
        TimeSpace space = this.timeSize.space ();
        if ( factor < 0D )
        {
            return space.expr ( space.none () );
        }

        RelativeTime duration = this.timeSize.duration ();
        BigDecimal seconds_and_nanoseconds =
            new BigDecimal ( duration.secondsAndNanoseconds () );
        BigDecimal big_factor = new BigDecimal ( factor );
        BigInteger multiplied_seconds_and_nanoseconds =
            seconds_and_nanoseconds.multiply ( big_factor ).toBigInteger ();
        RelativeTime multiplied_time =
            new RelativeTime ( multiplied_seconds_and_nanoseconds );
        TimeSize multiplied_size = new TimeSize ( space, multiplied_time );

        return space.expr ( multiplied_size );
    }


    /**
     * @see musaico.region.SizeExpression#ratio(musaico.region.Size)
     */
    @Override
    public double ratio (
                         Size that_size
                         )
    {
        if ( ! ( that_size instanceof TimeSize ) )
        {
            return Double.NaN;
        }

        TimeSize that = (TimeSize) that_size;

        RelativeTime this_duration = this.timeSize.duration ();
        RelativeTime that_duration = that.duration ();

        BigDecimal this_seconds_and_nanoseconds =
            new BigDecimal ( this_duration.secondsAndNanoseconds () );
        BigDecimal that_seconds_and_nanoseconds =
            new BigDecimal ( that_duration.secondsAndNanoseconds () );
        BigDecimal big_ratio =
            this_seconds_and_nanoseconds.divide ( that_seconds_and_nanoseconds );
        double ratio = big_ratio.doubleValue ();

        return ratio;
    }


    /**
     * @see musaico.region.SizeExpression#size()
     */
    @Override
    public TimeSize size ()
    {
        return this.timeSize;
    }


    /**
     * @see musaico.region.SizeExpression#subtract(musaico.region.Size)
     */
    @Override
    public TimeSizeExpression subtract (
                                        Size that_size
                                        )
    {
        TimeSpace space = this.timeSize.space ();
        TimeSize none = space.none ();
        if ( ! ( that_size instanceof TimeSize ) )
        {
            return space.expr ( none );
        }

        TimeSize that = (TimeSize) that_size;

        RelativeTime this_duration = this.timeSize.duration ();
        RelativeTime that_duration = that.duration ();
        if ( Order.TIME.compareValues ( this_duration, none.duration () )
             .equals ( Comparison.LEFT_LESS_THAN_RIGHT )
             || Order.TIME.compareValues ( that_duration, none.duration () )
             .equals ( Comparison.LEFT_LESS_THAN_RIGHT ) )
        {
            return space.expr ( none );
        }

        Time subtracted_duration = this_duration.subtract ( that_duration );
        if ( ! ( subtracted_duration instanceof RelativeTime ) )
        {
            TimeSize subtracted_size =
                new TimeSize ( space,
                               (RelativeTime) subtracted_duration );
            return space.expr ( subtracted_size );
        }
        else
        {
            // Overflow.
            return space.expr ( none );
        }
    }
}
