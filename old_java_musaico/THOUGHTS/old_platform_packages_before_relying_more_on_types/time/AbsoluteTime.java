package musaico.platform.time;

import java.io.Serializable;

import java.math.BigInteger;

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
 * Represents an absolute point in time (UTC).
 * </p>
 *
 * <p>
 * Operations:
 * </p>
 *
 * <pre>
 *   AbsoluteTime + AbsoluteTime         = ImpossibleTime.
 *   AbsoluteTime + RelativeTime         = new AbsoluteTime.
 *   AbsoluteTime + AbstractCalendarTime = new AbsoluteTime.
 *   AbsoluteTime + ImpossibleTime       = ImpossibleTime.
 *   AbsoluteTime - AbsoluteTime         = RelativeTime.
 *   AbsoluteTime - RelativeTime         = new AbsoluteTime.
 *   AbsoluteTime - AbstractCalendarTime = new AbsoluteTime.
 *   AbsoluteTime - ImpossibleTime       = ImpossibleTime.
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
public class AbsoluteTime
    implements Time, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130929L;
    private static final String serialVersionHash =
        "0x736EEA3F40BADD09852ACDD6E87541BE094A237B";


    /** Checks constructor parameters, static method parameters, and so on. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AbsoluteTime.class );


    /** The number of seconds since midnight Jan 1 1970 GMT. */
    private final long seconds;

    /** The number of nanoseconds to offset the seconds by. */
    private final long nanoseconds;




    /**
     * <p>
     * Returns the current time as an AbsoluteTime, with
     * nanosecond precision (but not necessarily high
     * accuracy...).
     * </p>
     *
     * @return A newly created AbsoluteTime representing
     *         the time right now.  Never null.
     */
    public static final AbsoluteTime now ()
    {
        return new AbsoluteTime ( System.currentTimeMillis () );
    }


    /**
     * <p>
     * Converts an array of Times to AbsoluteTimes.
     * </p>
     *
     * <p>
     * If the first Time in the array is a RelativeTime, then it
     * is converted to an offset from "right now".
     * </p>
     *
     * <p>
     * All RelativeTimes after the first Time are treated as
     * offsets from the previous AbsoluteTime.
     * </p>
     *
     * <p>
     * For example the following array of Times:
     * </p>
     *
     * <pre>
     *     AbsoluteTime ( X ),
     *     RelativeTime ( Y ),
     *     RelativeTime ( Z )
     * </pre>
     *
     * <p>
     * Is converted to the following array of AbsoluteTimes:
     * </p>
     *
     * <pre>
     *     AbsoluteTime ( X ),
     *     AbsoluteTime ( X + Y ),
     *     AbsoluteTime ( X + Y + Z )
     * </pre>
     *
     * <p>
     * And the following array of Times:
     * </p>
     *
     * <pre>
     *     RelativeTime ( W ),
     *     RelativeTime ( X ),
     *     AbsoluteTime ( Y ),
     *     RelativeTime ( Z )
     * </pre>
     *
     * <p>
     * Is converted to the following array of AbsoluteTimes:
     * </p>
     *
     * <pre>
     *     AbsoluteTime ( now + W ),
     *     AbsoluteTime ( now + W + X ),
     *     AbsoluteTime ( Y ),
     *     AbsoluteTime ( Y + Z )
     * </pre>
     *
     * <p>
     * If any ImpossibleTimes are encountered in the array then
     * a zero-length array is returned.
     * </p>
     *
     * @param times The array of Times to convert into an array
     *              of AbsoluteTimes.  Must not be null.
     *
     * @return A newly created array of AbsoluteTimes.  Zero-length
     *         if any impossible times arise.  Never null.
     */
    public static AbsoluteTime [] convert (
                                           Time [] times
                                           )
    {
        AbsoluteTime [] absolute_times = new AbsoluteTime [ times.length ];
        for ( int t = 0; t < times.length; t ++ )
        {
            if ( times [ t ] instanceof AbsoluteTime )
            {
                // Already an AbsoluteTime.
                absolute_times [ t ] = (AbsoluteTime) times [ t ];
            }
            else if ( times [ t ] instanceof RelativeTime )
            {
                if ( t == 0 )
                {
                    // Now + offset.
                    Time first_time =
                        AbsoluteTime.now ().add ( times [ t ] );
                    if ( ! ( first_time instanceof AbsoluteTime ) )
                    {
                        return new AbsoluteTime [ 0 ];
                    }

                    absolute_times [ t ] = (AbsoluteTime) first_time;
                }
                else
                {
                    // Previous AbsoluteTime + offset.
                    Time next_time =
                        absolute_times [ t - 1 ].add ( times [ t ] );
                    if ( ! ( next_time instanceof AbsoluteTime ) )
                    {
                        return new AbsoluteTime [ 0 ];
                    }

                    absolute_times [ t ] = (AbsoluteTime) next_time;
                }
            }
            else
            {
                return new AbsoluteTime [ 0 ];
            }
        }

        return absolute_times;
    }




    /**
     * <p>
     * Creates a new Time object from the specified Calendar object.
     * </p>
     */
    public AbsoluteTime (
                         Calendar calendar
                         )
    {
        this ( calendar.getTime () );
    }


    /**
     * <p>
     * Creates a new Time object from the specified Date object.
     * </p>
     */
    public AbsoluteTime (
                         Date date
                         )
    {
        this ( date.getTime () );
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
    public AbsoluteTime (
                         long milliseconds_since_utc_zero
                         )
    {
        long seconds = milliseconds_since_utc_zero / 1000L;
        long nanoseconds =
            ( milliseconds_since_utc_zero % 1000L ) * 1000000L;

        this.seconds = seconds;
        this.nanoseconds = nanoseconds;
    }


    /**
     * <p>
     * Creates a new AbsoluteTime object, offset from midnight
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
    public AbsoluteTime (
                         long seconds,
                         long nanoseconds
                         )
        throws Parameter2.DependsOn.Parameter1.Violation,
               Parameter2.MustBeInBounds.Violation
    {
        final Parameter2.DependsOn.Parameter1 both_positive_or_both_negative =
            new Parameter2.DependsOn.Parameter1 ( GreaterThanOrEqualToZero.DOMAIN,
                                                  LessThanOrEqualToZero.DOMAIN );
        classContracts.check ( both_positive_or_both_negative,
                               new long [] { seconds, nanoseconds } );
        classContracts.check ( Parameter2.MustBeInBounds.over ( Time.MIN_NANOSECONDS,
                                                                Time.MAX_NANOSECONDS ),
                               nanoseconds );

        this.seconds = seconds;
        this.nanoseconds = nanoseconds;
    }


    /**
     * <p>
     * Creates a new AbsoluteTime object, offset from midnight
     * Jan 1 1970 GMT by the specified number of seconds and
     * nanoseconds.
     * </p>
     *
     * @param seconds_and_nanoseconds The number of nanoseconds
     *                                since 0 UTC.
     *                                Can be positive or negative.
     *                                Must not be null.
     */
    public AbsoluteTime (
                         BigInteger seconds_and_nanoseconds
                         )
    {
        this ( seconds_and_nanoseconds.divide ( Time.BIG_NANOSECONDS_PER_SECOND ).longValue (),
               seconds_and_nanoseconds.mod ( Time.BIG_NANOSECONDS_PER_SECOND ).longValue () );
    }


    /**
     * <p>
     * Returns this AbsoluteTime as a newly created Java Calendar.
     * </p>
     *
     * @param locale The locale to format the calendar in.
     *               Must not be null.
     *
     * @return This time as a Calendar object.  Never null.
     */
    public Calendar calendar (
                              Locale locale
                              )
    {
        Calendar calendar = Calendar.getInstance ( locale );
        Date date = this.date ();
        calendar.setTime ( date );

        return calendar;
    }


    /**
     * <p>
     * Returns this AbsoluteTime as a newly created Java Calendar.
     * </p>
     *
     * @param time_zone The time zone to format the calendar in.
     *                  Must not be null.
     *
     * @return This time as a Calendar object.  Never null.
     */
    public Calendar calendar (
                              TimeZone time_zone
                              )
    {
        Calendar calendar = Calendar.getInstance ( time_zone );
        Date date = this.date ();
        calendar.setTime ( date );

        return calendar;
    }


    /**
     * <p>
     * Returns this AbsoluteTime as a newly created Java Calendar.
     * </p>
     *
     * @param time_zone The time zone to format the calendar in.
     *                  Must not be null.
     *
     * @param locale The locale to format the calendar in.
     *               Must not be null.
     *
     * @return This time as a Calendar object.  Never null.
     */
    public Calendar calendar (
                              TimeZone time_zone,
                              Locale locale
                              )
    {
        Calendar calendar = Calendar.getInstance ( time_zone, locale );
        Date date = this.date ();
        calendar.setTime ( date );

        return calendar;
    }


    /**
     * <p>
     * Returns this AbsoluteTime as a newly created CalendarTime.
     * </p>
     *
     * @param locale The locale to format the calendar in.
     *               Must not be null.
     *
     * @return The new CalendarTime.  Never null.
     */
    public CalendarTime calendarTime (
                                      Locale locale
                                      )
    {
        final Calendar calendar = this.calendar ( locale );
        return new CalendarTime ( calendar );
    }


    /**
     * <p>
     * Returns this AbsoluteTime as a newly created CalendarTime.
     * </p>
     *
     * @param time_zone The time zone to format the calendar time in.
     *                  Must not be null.
     *
     * @return The new CalendarTime.  Never null.
     */
    public CalendarTime calendarTime (
                                      TimeZone time_zone
                                      )
    {
        final Calendar calendar = this.calendar ( timezone );
        return new CalendarTime ( calendar );
    }


    /**
     * <p>
     * Returns this AbsoluteTime as a newly created CalendarTime.
     * </p>
     *
     * @param time_zone The time zone to format the calendar time in.
     *                  Must not be null.
     *
     * @param locale The locale to format the calendar time in.
     *               Must not be null.
     *
     * @return The new CalendarTime.  Never null.
     */
    public CalendarTime calendarTime (
                                      TimeZone time_zone,
                                      Locale locale
                                      )
    {
        final Calendar calendar = this.calendar ( time_zone, locale );
        return new CalendarTime ( calendar ); 
    }


    /**
     * @see java.util.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo (
                          Time that
                          )
    {
        if ( that == null )
        {
            // Any AbsoluteTime < null.
            return Integer.MAX_VALUE;
        }
        else if ( ! ( that instanceof AbsoluteTime ) )
        {
            // Any AbsoluteTime < any non-AbsoluteTime.
            return Integer.MAX_VALUE;
        }

        long this_seconds = this.seconds ();
        long that_seconds = that.seconds ();

        if ( this_seconds < that_seconds )
        {
            // X < (X + n)
            long diff = this_seconds - that_seconds;
            if ( diff < (long) ( Integer.MIN_VALUE + 1 ) )
            {
                diff = (long) ( Integer.MIN_VALUE + 1 );
            }

            return (int) diff;
        }
        else if ( this_seconds > that_seconds )
        {
            // (X + n) > X
            long diff = this_seconds - that_seconds;
            if ( diff > (long) Integer.MAX_VALUE )
            {
                diff = (long) Integer.MAX_VALUE;
            }

            return (int) diff;
        }

        long this_nanoseconds = this.nanoseconds ();
        long that_nanoseconds = that.nanoseconds ();

        if ( this_nanoseconds < that_nanoseconds )
        {
            // X < (X + n) within 1 second.
            return -1;
        }
        else if ( this_nanoseconds > that_nanoseconds )
        {
            // (X + n) > X within 1 second.
            return 1;
        }
        else
        {
            // X == X
            return 0;
        }
    }


    /**
     * <p>
     * Returns this AbsoluteTime as a newly created Java Date.
     * </p>
     *
     * @return This time as a Date object.  Never null.
     */
    public Date date ()
    {
        long milliseconds =
            ( this.seconds * 1000L ) + ( this.nanoseconds / 1000000L );

        Date date = new Date ( milliseconds );

        return date;
    }


    /**
     * @see java.lang.equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object that
                           )
    {
        if ( that == null
             || ! ( that instanceof AbsoluteTime ) )
        {
            return false;
        }

        int comparison = this.compareTo ( (AbsoluteTime) that );
        if ( comparison == 0 )
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
    @Override
    public int hashCode ()
    {
        int hash_code = (int)
            ( this.seconds () * Time.NANOSECONDS_PER_SECOND
              + this.nanoseconds () );
        return hash_code;
    }


    /**
     * @see musaico.foundation.time.Time#seconds()
     */
    @Override
    public long seconds ()
    {
        return this.seconds;
    }


    /**
     * @see musaico.foundation.time.Time#nanoseconds()
     */
    @Override
    public long nanoseconds ()
    {
        return this.nanoseconds;
    }


    /**
     * @see musaico.foundation.time.Time#secondsAndNanoseconds()
     */
    @Override
    public BigInteger secondsAndNanoseconds ()
    {
        BigInteger seconds =
            BigInteger.valueOf ( this.seconds () )
            .multiply ( Time.BIG_NANOSECONDS_PER_SECOND );
        BigInteger nanoseconds =
            BigInteger.valueOf ( this.nanoseconds () );

        BigInteger seconds_and_nanoseconds =
            seconds.add ( nanoseconds );

        return seconds_and_nanoseconds;
    }


    /**
     * @see musaico.foundation.time.Time#milliseconds()
     */
    @Override
    public long milliseconds ()
    {
        // Helper for Java only.
        long milliseconds = this.seconds () * 1000L;
        milliseconds += ( this.nanoseconds / 1000000L );
        return milliseconds;
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
            // Absolute + Impossible = Impossible.
            total_time = add_time;
        }
        else if ( add_time instanceof AbstractCalendarTime )
        {
            // Absolute + Calendar = new Absolute.
            AbstractCalendarTime calendar_offset =
                (AbstractCalendarTime) add_time;
            final Calendar calendar = calendar_offset.calendar ();
            calendar.setTimeInMillis ( this.milliseconds () );
            calendar_offset.addOffsetTo ( calendar );
            total_time = new AbsoluteTime ( calendar );
        }
        else if ( add_time instanceof RelativeTime )
        {
            // Absolute + Relative = new Absolute.
            total_time = new AbsoluteTime ( total_seconds,
                                            total_nanoseconds );
        }
        else
        {
            // Absolute + Absolute = Impossible.
            total_time = new AbsoluteTime ( total_seconds,
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
            // Absolute - Impossible = Impossible.
            total_time = subtract_time;
        }
        else if ( subtract_time instanceof AbstractCalendarTime )
        {
            // Absolute - Calendar = new Absolute.
            AbstractCalendarTime calendar_offset =
                (AbstractCalendarTime) subtract_time;
            final Calendar calendar = calendar_offset.calendar ();
            calendar.setTimeInMillis ( this.milliseconds () );
            calendar_offset.subtractOffsetFrom ( calendar );
            total_time = new AbsoluteTime ( calendar );
        }
        else if ( subtract_time instanceof RelativeTime )
        {
            // Absolute - Relative = new Absolute.
            total_time = new AbsoluteTime ( total_seconds,
                                            total_nanoseconds );
        }
        else
        {
            // Absolute - Absolute = new Relative.
            total_time = new RelativeTime ( total_seconds,
                                            total_nanoseconds );
        }

        return total_time;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        long seconds = this.seconds ();
        long nanoseconds = this.nanoseconds ();

        StringBuilder sbuf = new StringBuilder ();
        boolean has_fields = false;

        long hours = ( seconds / 3600L );
        seconds = ( seconds % 3600L );
        if ( hours > 0L )
        {
            sbuf.append ( "+" + hours );
            sbuf.append ( ":" );
            has_fields = true;
        }
        else if ( hours < 0L )
        {
            hours = 0L - hours;
            sbuf.append ( "-" + hours );
            sbuf.append ( ":" );
            has_fields = true;
        }

        long minutes = ( seconds / 60L );
        seconds = ( seconds % 60L );
        if ( minutes >= 10L )
        {
            sbuf.append ( "" + minutes );
            has_fields = true;
        }
        else if ( minutes > 0L
                  || has_fields )
        {
            sbuf.append ( "0" + minutes );
            has_fields = true;
        }

        if ( seconds >= 10L )
        {
            sbuf.append ( "" + seconds );
        }
        else if ( seconds > 0L
                  || has_fields )
        {
            sbuf.append ( "0" + seconds );
        }

        if ( nanoseconds > 0L )
        {
            sbuf.append ( "." + nanoseconds );
        }
        else
        {
            nanoseconds = 0L - nanoseconds;
            sbuf.append ( "-." + nanoseconds );
        }

        return sbuf.toString ();
    }
}
