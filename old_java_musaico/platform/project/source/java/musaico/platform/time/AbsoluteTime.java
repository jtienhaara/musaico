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
 * Represents an absolute point in time (UTC).
 * </p>
 *
 * <p>
 * Default operations (note that sub-classes may override these!):
 * </p>
 *
 * <pre>
 *   AbsoluteTime + ImpossibleTime       = ImpossibleTime.
 *   AbsoluteTime + CalendarOffset       = ImpossibleTime.
 *   AbsoluteTime + RelativeTime         = new AbsoluteTime.
 *   AbsoluteTime + CalendarTime         = ImpossibleTime.
 *   AbsoluteTime + AbsoluteTime         = ImpossibleTime.
 *   AbsoluteTime - ImpossibleTime       = ImpossibleTime.
 *   AbsoluteTime - CalendarOffset       = ImpossibleTime.
 *   AbsoluteTime - RelativeTime         = new AbsoluteTime.
 *   AbsoluteTime - CalendarTime         = new CalendarOffset.
 *   AbsoluteTime - AbsoluteTime         = new RelativeTime.
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
public abstract class AbsoluteTime
    implements Time, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20131022L;
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
                        LocalTime.now ().add ( times [ t ] );
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
     * @see musaico.platform.time.Time#add(musaico.platform.time.Time)
     */
    @Override
    public Time add (
                     Time add_time
                     )
    {
        if ( add_time == null )
        {
            // AbsoluteTime + null = ImpossibleTime.
            return new ImpossibleTime ( this.seconds (),
                                        this.nanoseconds () );
        }

        // AbsoluteTime + ImpossibleTime       = ImpossibleTime.
        // AbsoluteTime + CalendarOffset       = ImpossibleTime.
        // AbsoluteTime + RelativeTime         = new AbsoluteTime.
        // AbsoluteTime + CalendarTime         = ImpossibleTime.
        // AbsoluteTime + AbsoluteTime         = ImpossibleTime.
        final long add_seconds = add_time.seconds ();
        final long add_nanoseconds = add_time.nanoseconds ();
        final Time total_time;
        if ( add_time instanceof ImpossibleTime )
        {
            // Absolute + Impossible = Impossible.
            total_time = add_time;
        }
        else if ( add_time instanceof CalendarOffset )
        {
            // Most AbsoluteTimes do not have a Calendar
            // associated with them.  Create a CalendarTime,
            // instead, if you want to add a CalendarOffset.
            total_time = new ImpossibleTime ( this.seconds () + add_seconds,
                                              this.nanoseconds ()
                                              + add_time.nanoseconds () );
        }
        else if ( add_time instanceof RelativeTime )
        {
            // Absolute + Relative = new Absolute.
            total_time = this.create ( this.seconds () + add_seconds,
                                       this.nanoseconds ()
                                       + add_nanoseconds );
        }
        else if ( add_time instanceof AbsoluteTime )
        {
            // Absolute + Absolute = Impossible.
            return new ImpossibleTime ( this.seconds () + add_seconds,
                                        this.nanoseconds ()
                                        + add_nanoseconds );
        }

        return total_time;
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
     * @param timezone The time zone to format the calendar in.
     *                 Must not be null.
     *
     * @return This time as a Calendar object.  Never null.
     */
    public Calendar calendar (
                              TimeZone timezone
                              )
    {
        Calendar calendar = Calendar.getInstance ( timezone );
        Date date = this.date ();
        calendar.setTime ( date );

        return calendar;
    }


    /**
     * <p>
     * Returns this AbsoluteTime as a newly created Java Calendar.
     * </p>
     *
     * @param timezone The time zone to format the calendar in.
     *                 Must not be null.
     *
     * @param locale The locale to format the calendar in.
     *               Must not be null.
     *
     * @return This time as a Calendar object.  Never null.
     */
    public Calendar calendar (
                              TimeZone timezone,
                              Locale locale
                              )
    {
        Calendar calendar = Calendar.getInstance ( timezone, locale );
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
     * @param timezone The time zone to format the calendar time in.
     *                 Must not be null.
     *
     * @return The new CalendarTime.  Never null.
     */
    public CalendarTime calendarTime (
                                      TimeZone timezone
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
     * @param timezone The time zone to format the calendar time in.
     *                 Must not be null.
     *
     * @param locale The locale to format the calendar time in.
     *               Must not be null.
     *
     * @return The new CalendarTime.  Never null.
     */
    public CalendarTime calendarTime (
                                      TimeZone timezone,
                                      Locale locale
                                      )
    {
        final Calendar calendar = this.calendar ( timezone, locale );
        return new CalendarTime ( calendar ); 
    }


    /**
     * <p>
     * Returns this AbsoluteTime as a newly created CalendarTime.
     * </p>
     *
     * @param calendar The calendar whose time zone will be
     *                 used to create the Calendar.  Must not be null.
     *
     * @return The new CalendarTime.  Never null.
     */
    public CalendarTime calendarTime (
                                      Calendar calendar
                                      )
    {
        final Calendar this_calendar_in_that_timezone =
            this.calendar ( calendar.getTimeZone () );
        return new CalendarTime ( this_calendar_in_that_timezone );
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
     * Creates a new AbsoluteTime of the same class, but with the specified
     * time.
     * </p>
     *
     * <p>
     * Used from <code> AbsoluteTime.add () </code> and
     * <code> AbsoluteTime.subtract () </code>, so that not every
     * AbsoluteTime has to implement those methods.
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
     *
     * @return A newly created AbsoluteTime at the specified UTC time.
     *         Never null.
     */
    protected abstract AbsoluteTime create (
                                            long seconds,
                                            long nanoseconds
                                            );


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
     * @see musaico.platform.time.Time#seconds()
     */
    @Override
    public long seconds ()
    {
        return this.seconds;
    }


    /**
     * @see musaico.platform.time.Time#nanoseconds()
     */
    @Override
    public long nanoseconds ()
    {
        return this.nanoseconds;
    }


    /**
     * @see musaico.platform.time.Time#milliseconds()
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
     * @see musaico.platform.time.Time#subtract(musaico.platform.time.Time)
     */
    @Override
    public Time subtract (
                          Time subtract_time
                          )
    {
        if ( subtract_time == null )
        {
            // AbsoluteTime + null = ImpossibleTime.
            return new ImpossibleTime ( this.seconds (),
                                        this.nanoseconds () );
        }

        // AbsoluteTime - ImpossibleTime       = ImpossibleTime.
        // AbsoluteTime - CalendarOffset       = ImpossibleTime.
        // AbsoluteTime - RelativeTime         = new AbsoluteTime.
        // AbsoluteTime - CalendarTime         = new CalendarOffset.
        // AbsoluteTime - AbsoluteTime         = new RelativeTime.
        final long subtract_seconds = subtract_time.seconds ();
        final long subtract_nanoseconds = subtract_time.nanoseconds ();
        final Time total_time;
        if ( subtract_time instanceof ImpossibleTime )
        {
            // AbsoluteTime - ImpossibleTime       = ImpossibleTime.
            total_time = subtract_time;
        }
        else if ( subtract_time instanceof CalendarOffset )
        {
            // AbsoluteTime - CalendarOffset       = ImpossibleTime.
            // Most AbsoluteTimes do not have a Calendar
            // associated with them.  Create a CalendarTime
            // to subtract a CalendarOffset.
            total_time = new ImpossibleTime ( this.seconds ()
                                              - subtract_seconds,
                                              this.nanoseconds ()
                                              - subtract_time.nanoseconds () );
        }
        else if ( subtract_time instanceof RelativeTime )
        {
            // AbsoluteTime - RelativeTime         = new AbsoluteTime.
            total_time = this.create ( this.seconds ()
                                       - subtract_seconds,
                                       this.nanoseconds ()
                                       - subtract_nanoseconds );
        }
        else if ( subtract_time instanceof CalendarTime )
        {
            // AbsoluteTime - CalendarTime         = new CalendarOffset.
            final CalendarTime that = (CalendarTime) subtract_time;
            final TimeZone timezone = that.timezone (); // Might be null.
            final Locale locale = that.locale (); // Might be null.
            final CalendarTime this_calendar_time =
                this.calendarTime ( timezone, locale );
            // Let the CalendarTime object do all the hard work, figuring
            // out how many days, hours, minutes, seconds and so on
            // are between this and that, and returning a CalendarOffset
            // accordingly.
            total_time = this_calendar_time.subtract ( that );
        }
        else if ( subtract_time instanceof AbsoluteTime )
        {
            // AbsoluteTime - AbsoluteTime         = new RelativeTime.
            total_time = new RelativeTime ( this.seconds ()
                                            - subtract_seconds,
                                            this.nanoseconds ()
                                            - subtract_nanoseconds );
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
        sbuf.append ( getClass ().getSimpleName () );
        sbuf.append ( " " );

        boolean has_fields = false;

        // !!!! what is this?!?
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
