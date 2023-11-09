package musaico.region.time;

import java.io.Serializable;

import java.math.BigInteger;


import musaico.io.Comparison;
import musaico.io.Order;

import musaico.region.Position;
import musaico.region.PositionExpression;
import musaico.region.RegionExpression;
import musaico.region.Size;
import musaico.region.SparseRegion;

import musaico.time.ImpossibleTime;
import musaico.time.RelativeTime;
import musaico.time.Time;


/**
 * <p>
 * Manipulates time positions and creates sizes and
 * regions from them.
 * </p>
 *
 * <p>
 * For example, the size of time position 12:42pm Thursday December 29 2011
 * - time position 12:41:01pm Thursday December 29 2011 is 59 seconds.
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
public class TimePositionExpression
    implements PositionExpression, Serializable
{
    /** The position from which the expression started. */
    private final TimePosition timePosition;


    /**
     * <p>
     * Creates a new TimePositionExpression, taking the
     * specified term as the start of the expression.
     * </p>
     *
     * @param time_position The position from which to start
     *                      the expression.  Must not be null.
     */
    public TimePositionExpression (
                                   TimePosition time_position
                                   )
    {
        this.timePosition = time_position;
    }


    /**
     * @see musaico.region.PositionExpression#add(musaico.region.Size)
     */
    @Override
    public TimePositionExpression add (
                                       Size size
                                       )
    {
        if ( ! ( size instanceof TimeSize ) )
        {
            return this.timePosition.space ()
                .expr ( this.timePosition.space ().outOfBounds () );
        }

        TimeSize time_size = (TimeSize) size;

        Time time = this.timePosition.time ();
        RelativeTime duration = time_size.duration ();

        Time added_time = time.add ( duration );

        if ( added_time instanceof ImpossibleTime )
        {
            return this.timePosition.space ()
                .expr ( this.timePosition.space ().outOfBounds () );
        }
        else
        {
            TimePosition added_position =
                this.timePosition.space ().positionFactory ()
                .position ( this.timePosition.space (), added_time );
            return this.timePosition.space ().expr ( added_position );
        }
    }


    /**
     * @see musaico.region.PositionExpression#modulo(musaico.region.Size)
     */
    @Override
    public TimeSizeExpression modulo (
                                      Size size
                                      )
    {
        if ( ! ( size instanceof TimeSize ) )
        {
            return this.timePosition.space ()
                .expr ( this.timePosition.space ().none () );
        }

        TimeSize time_size = (TimeSize) size;

        Time time = this.timePosition.time ();
        RelativeTime duration = time_size.duration ();

        BigInteger position_mod_size =
            time.secondsAndNanoseconds ()
            .mod ( duration.secondsAndNanoseconds () );
        RelativeTime mod_time = new RelativeTime ( position_mod_size );
        TimeSize mod_size =
            new TimeSize ( this.timePosition.space (), mod_time );

        return this.timePosition.space ().expr ( mod_size );
    }


    /**
     * @see musaico.region.PositionExpression#next()
     */
    @Override
    public TimePosition next ()
    {
        Time time = this.timePosition.time ();
        RelativeTime one_interval =
            this.timePosition.space ().one ().duration ();

        Time next_time = time.add ( one_interval );
        if ( next_time instanceof ImpossibleTime )
        {
            return this.timePosition.space ().outOfBounds ();
        }
        else
        {
            return this.timePosition.space ().positionFactory ()
                .position ( this.timePosition.space (), next_time );
        }
    }


    /**
     * @see musaico.region.PositionExpression#position()
     */
    @Override
    public TimePosition position ()
    {
        return this.timePosition;
    }


    /**
     * @see musaico.region.PositionExpression#previous()
     */
    @Override
    public TimePosition previous ()
    {
        Time time = this.timePosition.time ();
        RelativeTime one_interval =
            this.timePosition.space ().one ().duration ();

        Time previous_time = time.subtract ( one_interval );
        if ( previous_time instanceof ImpossibleTime )
        {
            return this.timePosition.space ().outOfBounds ();
        }
        else
        {
            return this.timePosition.space ().positionFactory ()
                .position ( this.timePosition.space (), previous_time );
        }
    }


    /**
     * @see musaico.region.PositionExpression#subtract(musaico.region.Position)
     */
    @Override
    public TimeSizeExpression subtract (
                                        Position that_position
                                        )
    {
        if ( ! ( that_position instanceof TimePosition ) )
        {
            return this.timePosition.space ()
                .expr ( this.timePosition.space ().none () );
        }

        TimePosition that = (TimePosition) that_position;

        Time this_time = this.timePosition.time ();
        Time that_time = that.time ();

        Time subtracted_time = this_time.subtract ( that_time );

        if ( ! ( subtracted_time instanceof RelativeTime ) )
        {
            return this.timePosition.space ()
                .expr ( this.timePosition.space ().none () );
        }
        else
        {
            RelativeTime duration = (RelativeTime) subtracted_time;
            TimeSize subtracted_size =
                new TimeSize ( this.timePosition.space (), duration );
            return this.timePosition.space ().expr ( subtracted_size );
        }
    }


    /**
     * @see musaico.region.PositionExpression#subtract(musaico.region.Size)
     */
    @Override
    public TimePositionExpression subtract (
                                            Size size
                                            )
    {
        if ( ! ( size instanceof TimeSize ) )
        {
            return this.timePosition.space ()
                .expr ( this.timePosition.space ().outOfBounds () );
        }

        TimeSize time_size = (TimeSize) size;

        Time time = this.timePosition.time ();
        RelativeTime duration = time_size.duration ();

        Time subtracted_time = time.subtract ( duration );

        if ( subtracted_time instanceof ImpossibleTime )
        {
            return this.timePosition.space ()
                .expr ( this.timePosition.space ().outOfBounds () );
        }
        else
        {
            TimePosition subtracted_position =
                this.timePosition.space ().positionFactory ()
                .position ( this.timePosition.space (), subtracted_time );
            return this.timePosition.space ().expr ( subtracted_position );
        }
    }


    /**
     * @see musaico.region.PositionExpression#to(musaico.region.Position)
     */
    @Override
    public RegionExpression to (
                                Position that_position
                                )
    {
        if ( ! ( that_position instanceof TimePosition ) )
        {
            return this.timePosition.space ()
                .expr ( this.timePosition.space ().empty () );
        }

        TimePosition that = (TimePosition) that_position;
        Time this_time = this.timePosition.time ();
        Time that_time = that.time ();
        if ( this_time.getClass () != that_time.getClass () )
        {
            // One is an absolute time and the other relative time,
            // or something along those lines.
            return this.timePosition.space ()
                .expr ( this.timePosition.space ().empty () );
        }

        Comparison comparison =
            Order.TIME.compareValues ( this_time, that_time );
        if ( ! comparison.isIn ( Comparison.LEFT_LESS_THAN_RIGHT,
                                 Comparison.LEFT_EQUALS_RIGHT ) )
        {
            return this.timePosition.space ()
                .expr ( this.timePosition.space ().empty () );
        }

        TimeRegion region = new TimeRegion ( this.timePosition, that );
        return this.timePosition.space ().expr ( region );
    }
}
