package musaico.platform.time;

import java.io.Serializable;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.domains.GreaterThanOrEqualToZero;
import musaico.foundation.contract.domains.LessThanOrEqualToZero;

import musaico.foundation.contract.obligations.Parameter2;


/**
 * <p>
 * Represents an absolute point in time (UTC) according to the local
 * system.  Warning: local system time is inherently inaccurate
 * and imprecise, since it is not necessarily synchronized with
 * any stable clock source, and the user can even change it at will
 * on most systems.
 * </p>
 *
 * <p>
 * Operations:
 * </p>
 *
 * <pre>
 *   LocalTime + AbsoluteTime         = ImpossibleTime.
 *   LocalTime + RelativeTime         = new LocalTime.
 *   LocalTime + CalendarRelativeTime = ImpossibeTime.
 *   LocalTime + ImpossibleTime       = ImpossibleTime.
 *   LocalTime - AbsoluteTime         = RelativeTime.
 *   LocalTime - CalendarRelativeTime = ImpossibleTime.
 *   LocalTime - RelativeTime         = new LocalTime.
 *   LocalTime - ImpossibleTime       = ImpossibleTime.
 * </pre>
 *
 *
 * <p>
 * In Java every Time must be Serializable in order to play
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
 * Copyright (c) 2009, 2011, 2013 Johann Tienhaara
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
public class LocalTime
    extends AbsoluteTime
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20131022L;
    private static final String serialVersionHash =
        "0x736EEA3F40BADD09852ACDD6E87541BE094A237B";


    /** Checks constructor parameters, static method parameters, and so on. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( LocalTime.class );




    /**
     * <p>
     * Returns the current LocalTime, with
     * nanosecond precision (but not necessarily high
     * accuracy...).
     * </p>
     *
     * @return A newly created LocalTime representing
     *         the time right now.  Never null.
     */
    public static final LocalTime now ()
    {
        return new LocalTime ( System.currentTimeMillis () );
    }




    /**
     * <p>
     * Creates a new Time object from the specified Calendar object.
     * </p>
     */
    public LocalTime (
                      Calendar calendar
                      )
    {
        super ( calendar );
    }


    /**
     * <p>
     * Creates a new Time object from the specified Date object.
     * </p>
     */
    public LocalTime (
                      Date date
                      )
    {
        super ( date );
    }


    /**
     * <p>
     * Creates a new Time object from the specified milliseconds-since-UTC-0.
     * </p>
     *
     * <p>
     * A helper in Java only.
     * </p>
     *
     * @param milliseconds_since_utc_zero The absolute number of milliseconds
     *                                    that have elapsed from UTC 0.
     *                                    can be positive or negative or 0.
     */
    public LocalTime (
                      long milliseconds_since_utc_zero
                      )
    {
        super ( milliseconds_since_utc_zero );
    }


    /**
     * <p>
     * Creates a new LocalTime object, offset from midnight
     * Jan 1 1970 GMT by the specified number of seconds and
     * nanoseconds.
     * </p>
     *
     * <p>
     * Java users: be wary, the first field is <i>seconds</i>
     * not milliseconds!
     * </p>
     *
     * @param seconds The number of seconds since 0 UTC.
     *                Can be positive or negative.
     *
     * @param nanoseconds The number of nanoseconds since the
     *                    seconds parameter UTC.  If seconds is
     *                    positive or 0 then nanoseconds must be
     *                    between 0 and Time.MAX_NANOSECONDS (inclusive).
     *                    If seconds is negative then nanoseconds
     *                    must be between Time.MIN_NANOSECONDS
     *                    and 0 (inclusive).
     */
    public LocalTime (
                      long seconds,
                      long nanoseconds
                         )
        throws Parameter2.DependsOn.Parameter1.Violation,
               Parameter2.MustBeInBounds.Violation
    {
        super ( seconds, nanoseconds );
    }


    /**
     * @see musaico.foundation.time.Time#add(musaico.foundation.time.Time)
     */
    @Override
    public Time add (
                     Time add_time
                     )
    {
        long total_seconds = this.seconds () + add_time.seconds ();
        long total_nanoseconds = this.nanoseconds () + add_time.nanoseconds ();

        if ( total_nanoseconds > Time.NANOSECONDS_PER_SECOND )
        {
            total_seconds +=
                ( total_nanoseconds / Time.NANOSECONDS_PER_SECOND );
            total_nanoseconds =
                ( total_nanoseconds % Time.NANOSECONDS_PER_SECOND );
        }

        final Time total_time;
        if ( add_time instanceof ImpossibleTime )
        {
            // Local + Impossible = Impossible.
            total_time = add_time;
        }
        else if ( add_time instanceof CalendarRelativeTime )
        {
            // Local + Calendar = Impossible.
            total_time =
                new ImpossibleTime ( total_seconds, total_nanoseconds );
        }
        else if ( add_time instanceof RelativeTime )
        {
            // Local + Relative = new Local.
            total_time = new LocalTime ( total_seconds,
                                         total_nanoseconds );
        }
        else
        {
            // Local + Absolute = Impossible.
            total_time = new ImpossibleTime ( total_seconds,
                                              total_nanoseconds );
        }

        return total_time;
    }


    /**
     * @see musaico.foundation.time.Time#subtract(musaico.foundation.time.Time)
     */
    @Override
    public Time subtract (
                          Time subtract_time
                          )
    {
        long total_seconds = this.seconds () - subtract_time.seconds ();
        long total_nanoseconds =
            this.nanoseconds () - subtract_time.nanoseconds ();

        if ( total_nanoseconds < 0L )
        {
            total_seconds +=
                ( total_nanoseconds / Time.NANOSECONDS_PER_SECOND ) - 1;
            total_nanoseconds =
                ( total_nanoseconds % Time.NANOSECONDS_PER_SECOND );
            if ( total_nanoseconds < 0L )
            {
                total_nanoseconds += Time.NANOSECONDS_PER_SECOND;
            }
        }

        final Time total_time;
        if ( subtract_time instanceof ImpossibleTime )
        {
            // Local - Impossible = Impossible.
            total_time = subtract_time;
        }
        else if ( subtract_time instanceof CalendarRelativeTime )
        {
            // Local - Calendar = Impossible.
            total_time =
                new ImpossibleTime ( total_seconds, total_nanoseconds );
        }
        else if ( subtract_time instanceof RelativeTime )
        {
            // Local - Relative = new Local.
            total_time = new LocalTime ( total_seconds,
                                         total_nanoseconds );
        }
        else
        {
            // Local - Absolute = new Relative.
            total_time = new RelativeTime ( total_seconds,
                                            total_nanoseconds );
        }

        return total_time;
    }
}
