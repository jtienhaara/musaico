package musaico.region.time;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Comparison;
import musaico.io.Order;

import musaico.region.Size;

import musaico.time.RelativeTime;


/**
 * <p>
 * The duration of a TimeRegion, from start to end.
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
public class TimeSize
    implements Size, Serializable
{
    /** The space in which this size was created. */
    private final TimeSpace space;

    /** The duration of time represented by this size. */
    private final RelativeTime duration;


    /**
     * <p>
     * Creates a new TimeSize with the specified duration.
     * </p>
     *
     * @param space The space in which this time size was created,
     *              such as an absolute time space with daily
     *              time divisions.  Must not be null.
     *
     * @param duration The duration for this time size.
     *                 Must be 0 nanoseconds or greater.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above for details).
     */
    public TimeSize (
                     TimeSpace space,
                     RelativeTime duration
                     )
        throws I18nIllegalArgumentException
    {
        if ( space == null
             || duration == null
             || ! Order.TIME.compareValues ( duration,
                                             new RelativeTime ( 0L, 0L ) )
             .isIn ( Comparison.LEFT_GREATER_THAN_RIGHT,
                     Comparison.LEFT_EQUALS_RIGHT ) )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a TimeSize in space [%space%] of duration [%duration%]",
                                                     "space", space,
                                                     "duration", duration );
        }

        this.space = space;
        this.duration = duration;
    }


    /**
     * <p>
     * Creates a new, zero-length duration.
     * </p>
     *
     * <p>
     * Package private so that each TimeSpace can have its own "none"
     * TimeSize.
     * </p>
     *
     * @param space The space in which this time size was created,
     *              such as an absolute time space with daily
     *              time divisions.  Must not be null.
     */
    TimeSize (
              TimeSpace space
              )
    {
        this.space = space;
        this.duration = new RelativeTime ( 0L, 0L );
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
        else if ( ! ( obj instanceof TimeSize ) )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }

        TimeSize that = (TimeSize) obj;
        if ( this.duration ().equals ( that.duration () ) )
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
        return this.duration ().hashCode ();
    }


    /**
     * <p>
     * Returns the duration of the time represented by this
     * TimeSize.
     * </p>
     *
     * @return The duration of the time.  Always 0 or greater.
     */
    public RelativeTime duration ()
    {
        return this.duration;
    }


    /**
     * @see musaico.io.NaturallyOrdered#orderIndex()
     */
    @Override
    public TimeSize orderIndex ()
    {
        return this;
    }


    /**
     * @see musaico.io.NaturallyOrdered#order()
     */
    @Override
    public Order<Size> order ()
    {
        return TimeSizeOrder.DEFAULT;
    }


    /**
     * @see musaico.region.Size#space()
     */
    @Override
    public TimeSpace space ()
    {
        return this.space;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "[Duration: " + this.duration () + "]";
    }
}
