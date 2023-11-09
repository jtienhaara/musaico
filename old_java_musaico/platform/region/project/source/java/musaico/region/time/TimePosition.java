package musaico.region.time;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.io.Order;

import musaico.region.Position;

import musaico.time.ImpossibleTime;
import musaico.time.Time;


/**
 * <p>
 * A position in Time, either an AbsoluteTimePosition if the
 * TimeSpace.ABSOLUTE_TIMES space is being used, or a RelativeTimePosition
 * (time offsets) if the TimeSpace.RELATIVE_TIMES space is being used.
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
public class TimePosition
    implements Position, Serializable
{
    /** This positions's space (either absolute times or relative times). */
    private final TimeSpace space;

    /** This position's time. */
    private final Time time;


    /**
     * <p>
     * Creates a new TimePosition with the specified time.
     * </p>
     *
     * <p>
     * Package private so that AbsoluteTimePosition and
     * RelativeTimePosition can rely on the super constructor.
     * </p>
     *
     * @param space The space in which this time position was created,
     *              such as an absolute time space with daily
     *              time divisions.  Must not be null.
     *
     * @param time The time of this position, such as
     *             12:42pm Thursday December 29, 2011,
     *             or a relative time of 59 seconds,
     *             and so on.  Must not be null.
     *             Must not be an ImpossibleTime.
     *
     * @throws I18nIllegalArgumentException If the parameter(s)
     *                                      specified are invalid
     *                                      (see above for details).
     */
    TimePosition (
                  TimeSpace space,
                  Time time
                  )
        throws I18nIllegalArgumentException
    {
        if ( space == null
             || time == null
             || ( time instanceof ImpossibleTime ) )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a TimePosition in space [%space%] at time [%time%]",
                                                     "space", space,
                                                     "time", time );
        }

        this.space = space;
        this.time = time;
    }


    /**
     * <p>
     * Creates a new out-of-bounds TimePosition, at Time.NEVER,
     * in the specified space.
     * </p>
     *
     * <p>
     * Package private so that each TimeSpace can create its out of
     * bounds position.
     * </p>
     *
     * @param space The space in which to create the new out-of-bounds
     *              TimePosition.  Must not be null.
     */
    TimePosition (
                  TimeSpace space
                  )
    {
        this.space = space;
        this.time = Time.NEVER;
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
        else if ( ! ( obj instanceof TimePosition ) )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }

        TimePosition that = (TimePosition) obj;
        if ( this.time ().equals ( that.time () ) )
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
        return this.time ().hashCode ();
    }


    /**
     * @see musaico.region.Position#space()
     */
    @Override
    public TimeSpace space ()
    {
        return this.space;
    }


    /**
     * <p>
     * Returns the Time at this position.
     * </p>
     *
     * @return The Time at this position.  Never null.
     */
    public Time time ()
    {
        return this.time;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "Time(" + this.time () + ")";
    }
}
