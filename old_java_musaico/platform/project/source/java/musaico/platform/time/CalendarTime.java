package musaico.platform.time;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * An AbsoluteTime set according to some localized Calendar.
 * </p>
 *
 * <p>
 * Operations:
 * </p>
 *
 * <pre>
 *   CalendarTime + ImpossibleTime       = ImpossibleTime.
 *   CalendarTime + CalendarOffset       = new CalendarTime.
 *   CalendarTime + RelativeTime         = new CalendarTime.
 *   CalendarTime + CalendarTime         = ImpossibleTime.
 *   CalendarTime + AbsoluteTime         = ImpossibleTime.
 *   CalendarTime - ImpossibleTime       = ImpossibleTime.
 *   CalendarTime - CalendarOffset       = new CalendarTime.
 *   CalendarTime - RelativeTime         = new CalendarTime.
 *   CalendarTime - CalendarTime         = new CalendarOffset.
 *   CalendarTime - AbsoluteTime         = new RelativeTime.
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
 * Copyright (c) 2013 Johann Tienhaara
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
public class CalendarTime
    extends AbsoluteTime
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20131003L;
    private static final String serialVersionHash =
        "0xDB99F857427F0ACBE060FE5082C9E229A06E1D54";


    /** Checks constructor parameters, static method parameters, and so on. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( CalendarTime.class );


    /** Synchronize all critical sections on this token: */
    private final Serializable lock = new String ();

    /** The Calendar used to calculate all Time additions and
     *  subtractions.  Cloned for each operation. */
    private final Calendar calendar;

    /** The TimeZone used to construct this CalendarTime's Calendar.
        Can be null. */
    private final TimeZone timezone;

    /** The Locale used to construct this CalendarTime's Calendar.
     *  Can be null. */
    private final Locale locale;


    /**
     * <p>
     * Creates a new CalendarTime with the specified Calendar.
     * </p>
     *
     * @param calendar The calendar which will be cloned for each
     *                 operation.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Calendar.clone () cast to Calendar.
    public CalendarTime (
                         Calendar calendar
                         )
        throws ParametersMustNotBeNull.Violation
    {
        super ( checkNull ( calendar == null ? null : calendar.getTime () ) );

        this.calendar = (Calendar) calendar.clone ();

        this.timezone = calendar.getTimeZone ();
        this.locale = null;
    }


    /**
     * <p>
     * Creates a new CalendarTime for the specified timezone
     * (using Java's Calendar lookup).
     * </p>
     *
     * @param timezone The timezone by which to lookup the Calendar
     *                 implementation(such as Gregorian and so on,
     *                 with Daylight Savings Time settings according
     *                 to the timezone).  .  Must not be null.
     */
    public CalendarTime (
                         TimeZone timezone
                         )
        throws ParametersMustNotBeNull.Violation
    {
        this ( Calendar.getInstance ( checkNull ( timezone ) ) );

        this.timezone = timezone;
        this.locale = null;
    }


    /**
     * <p>
     * Creates a new CalendarTime for the specified locale
     * (using Java's Calendar lookup).
     * </p>
     *
     * @param locale The locale by which to lookup the Calendar
     *               implementation (such as Gregorian and so on,
     *               with Daylight Savings Time settings according
     *               to the locale).  Must not be null.
     */
    public CalendarTime (
                         Locale locale
                         )
        throws ParametersMustNotBeNull.Violation
    {
        this ( Calendar.getInstance ( checkNull ( locale ) ) );

        this.timezone = null;
        this.locale = locale;
    }


    /**
     * <p>
     * Creates a new CalendarTime for the specified
     * timezone and locale (using Java's Calendar lookup).
     * </p>
     *
     * @param timezone The timezone by which to lookup the Calendar
     *                 implementation(such as Gregorian and so on,
     *                 with Daylight Savings Time settings according
     *                 to the timezone).  .  Must not be null.
     *
     * @param locale The locale by which to lookup the Calendar
     *               implementation (such as Gregorian and so on,
     *               with Daylight Savings Time settings according
     *               to the locale).  Must not be null.
     */
    public CalendarTime (
                         TimeZone timezone,
                         Locale locale
                         )
        throws ParametersMustNotBeNull.Violation
    {
        this ( Calendar.getInstance ( checkNull ( timezone ),
                                      checkNull ( locale ) ) );

        this.timezone = timezone;
        this.locale = locale;
    }


    /**
     * <p>
     * Makes sure the specified oject is not null, and if it is,
     * throws a violation; if it isn't, returns the object as-is.
     * </p>
     *
     * <p>
     * Uses the class contracts to check, so if checking is
     * disabled, nulls will get through.
     * </p>
     */
    private static final <NOT_NULL extends Object>
        NOT_NULL checkNull (
                            NOT_NULL must_not_be_null
                            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               must_not_be_null );

        return must_not_be_null;
    }


    /**
     * @see musaico.platform.time.Time#add(Time)
     */
    @Override
    public Time add (
                     Time add_time
                     )
    {
        if ( add_time == null )
        {
            // CalendarTime + null = ImpossibleTime.
            return new ImpossibleTime ( this.seconds (),
                                        this.nanoseconds () );
        }

        // CalendarTime + ImpossibleTime       = ImpossibleTime.
        // CalendarTime + CalendarOffset       = new CalendarTime.
        // CalendarTime + RelativeTime         = new CalendarTime.
        // CalendarTime + CalendarTime         = ImpossibleTime.
        // CalendarTime + AbsoluteTime         = ImpossibleTime.
        final Time total_time;
        if ( add_time instanceof ImpossibleTime )
        {
            // Calendar + Impossible = Impossible.
            total_time = add_time;
        }
        else if ( add_time instanceof CalendarOffset )
        {
            // Calendar + CalendarOffset = new Calendar.
            // Clone the calendar.
            final CalendarOffset that = (CalendarOffset) add_time;
            final Calendar calendar = this.calendar ();
            that.addOffsetTo ( calendar );
            total_time = new CalendarTime ( calendar );
        }
        else if ( add_time instanceof RelativeTime )
        {
            // Calendar + Relative = new Calendar.
            // Clone the calendar.
            final RelativeTime that = (RelativeTime) add_time;
            final long add_seconds = add_time.seconds ();
            final long add_milliseconds = add_time.nanoseconds () / 1000000L;
            if ( add_seconds > Integer.MAX_VALUE
                 || add_milliseconds > Integer.MAX_VALUE )
            {
                // Calendar can't handle adding that many seconds,
                // milliseconds.
                total_time = new ImpossibleTime ( this.seconds ()
                                                  + add_seconds,
                                                  this.nanoseconds ()
                                                  + add_time.nanoseconds () );
            }
            else
            {
                final Calendar calendar = this.calendar ();
                calendar.add ( (int) add_seconds, Calendar.SECOND );
                calendar.add ( (int) add_milliseconds, Calendar.MILLISECOND );
                total_time = new CalendarTime ( calendar );
            }
        }
        else if ( add_time instanceof AbsoluteTime )
        {
            // Calendar + Absolute = Impossible.
            total_time =
                new ImpossibleTime ( this.seconds () + add_time.seconds (),
                                     this.nanoseconds ()
                                     + add_time.nanoseconds () );
        }

        return total_time;
    }


    /**
     * @return A clone of this calendar offset's Calendar.  Never null.
     */
    @SuppressWarnings("unchecked") // Calendar.clone () cast to Calendar.
    protected Calendar calendar ()
    {
        final Calendar calendar;
        synchronized ( this.lock )
        {
            calendar = (Calendar) this.calendar.clone ();
        }

        return calendar;
    }


    /**
     * @see musaico.platform.time.AbsoluteTime#create(long,long)
     */
    @Override
    protected CalendarTime create (
                                   long seconds,
                                   long nanoseconds
                                   )
    {
        final Calendar calendar = this.calendar ();
        final Date date =
            new Date ( seconds * 1000L + nanoseconds / 1000000L );
        calendar.setTime ( date );
        final CalendarTime calendar_time =
            new CalendarTime ( calendar );

        return calendar_time;
    }


    /**
     * @see musaico.platform.time.Time#subtract(Time)
     */
    @Override
    public Time subtract (
                          Time subtract_time
                          )
    {
        if ( subtract_time == null )
        {
            // CalendarTime + null = ImpossibleTime.
            return new ImpossibleTime ( this.seconds (),
                                        this.nanoseconds () );
        }

        // CalendarTime - ImpossibleTime       = ImpossibleTime.
        // CalendarTime - CalendarOffset       = new CalendarTime.
        // CalendarTime - RelativeTime         = new CalendarTime.
        // CalendarTime - CalendarTime         = CalendarOffset.
        // CalendarTime - AbsoluteTime         = new RelativeTime.
        final Time total_time;
        if ( subtract_time instanceof ImpossibleTime )
        {
            // Calendar + Impossible = Impossible.
            total_time = subtract_time;
        }
        else if ( subtract_time instanceof CalendarOffset )
        {
            // Calendar + CalendarOffset = new Calendar.
            // Clone the calendar.
            final CalendarOffset that = (CalendarOffset) subtract_time;
            final Calendar calendar = this.calendar ();
            that.subtractOffsetFrom ( calendar );
            total_time = new CalendarTime ( calendar );
        }
        else if ( subtract_time instanceof RelativeTime )
        {
            // Calendar + Relative = new Calendar.
            // Clone the calendar.
            final RelativeTime that = (RelativeTime) subtract_time;
            final long subtract_seconds = subtract_time.seconds ();
            final long subtract_milliseconds =
                subtract_time.nanoseconds () / 1000000L;
            if ( subtract_seconds > Integer.MAX_VALUE
                 || subtract_milliseconds > Integer.MAX_VALUE )
            {
                // Calendar can't handle subtracting that many seconds,
                // milliseconds.
                total_time = new ImpossibleTime ( this.seconds ()
                                                  + subtract_seconds,
                                                  this.nanoseconds ()
                                                  + subtract_time.nanoseconds () );
            }
            else
            {
                final Calendar calendar = this.calendar ();
                calendar.add ( 0 - (int) subtract_seconds,
                               Calendar.SECOND );
                calendar.add ( 0 - (int) subtract_milliseconds,
                               Calendar.MILLISECOND );
                total_time = new CalendarTime ( calendar );
            }
        }
        else if ( subtract_time instanceof CalendarTime )
        {
            // Calendar - Calendar = CalendarOffset
            CalendarTime that = (CalendarTime) subtract_time;
            final Calendar this_calendar = this.calendar ();
            boolean is_same_timezone_and_locale = true;
            if ( this.timezone == null
                 && that.timezone != null )
            {
                is_same_timezone_and_locale = false;
            }
            else if ( this.timezone != null
                      && that.timezone == null )
            {
                is_same_timezone_and_locale = false;
            }
            else if ( this.timezone != that.timezone
                      || ! this.timezone.equals ( that.timezone ) )
            {
                is_same_timezone_and_locale = false;
            }

            if ( this.locale == null
                 && that.locale != null )
            {
                is_same_timezone_and_locale = false;
            }
            else if ( this.locale != null
                      && that.locale == null )
            {
                is_same_timezone_and_locale = false;
            }
            else if ( this.locale != that.locale
                      || ! this.locale.equals ( that.locale ) )
            {
                is_same_timezone_and_locale = false;
            }

            final Calendar that_calendar;
            if ( is_same_timezone_and_locale )
            {
                // Use that Calendar as-is.
                that_calendar = that.calendar ();
            }
            else if ( this.timezone == null
                      && this.locale == null )
            {
                // Shouldn't ever happen but...  Use that Calendar as-is.
                that_calendar = that.calendar ();
            }
            else if ( this.timezone == null )
            {
                // Get that Calendar localized.
                that_calendar = that.calendar ( this.locale );
            }
            else if ( this.locale == null )
            {
                // Get that Calendar for our timezone.
                that_calendar = that.calendar ( this.timezone );
            }
            else
            {
                // Get that Calendar localized and timezone-ized.
                that_calendar = that.calendar ( this.timezone, this.locale );
            }

            final int this_year = this_calendar.get ( Calendar.YEAR );
            final int this_month = this_calendar.get ( Calendar.MONTH );
            final int this_day = this_calendar.get ( Calendar.DAY_OF_MONTH );
            final int this_hour = this_calendar.get ( Calendar.HOUR );
            final int this_minute = this_calendar.get ( Calendar.MINUTE );
            final int this_second = this_calendar.get ( Calendar.SECOND );

            final int that_year = that_calendar.get ( Calendar.YEAR );
            final int that_month = that_calendar.get ( Calendar.MONTH );
            final int that_day = that_calendar.get ( Calendar.DAY_OF_MONTH );
            final int that_hour = that_calendar.get ( Calendar.HOUR );
            final int that_minute = that_calendar.get ( Calendar.MINUTE );
            final int that_second = that_calendar.get ( Calendar.SECOND );

            int diff_year = this_year - that_year;
            int diff_month = this_month - that_month;
            int diff_day = this_day - that_day;
            int diff_hour = this_hour - that_hour;
            int diff_minute = this_minute - that_minute;
            int diff_second = this_second - that_second;

            that_calendar.add ( diff_year, Calendar.YEAR );
            that_calendar.add ( diff_month, Calendar.MONTH );
            that_calendar.add ( diff_day, Calendar.DAY_OF_MONTH );
            that_calendar.add ( diff_hour, Calendar.HOUR );
            that_calendar.add ( diff_minute, Calendar.MINUTE );
            that_calendar.add ( diff_second, Calendar.SECOND );

            final long this_milliseconds_utc =
                this_calendar.getTimeInMillis ();
            final long that_milliseconds_utc =
                that_calendar.getTimeInMillis ();
            final long diff_milliseconds =
                this_milliseconds_utc - that_milliseconds_utc;

            final List<CalendarOffset> offsets =
                new ArrayList<CalendarOffset> ();
            if ( diff_year != 0 )
            {
                offsets.add ( new Years ( diff_year, this.timezone, this.locale ) );
            }
            if ( diff_month != 0 )
            {
                offsets.add ( new Months ( diff_month, this.timezone, this.locale ) );
            }
            if ( diff_day != 0 )
            {
                offsets.add ( new Days ( diff_day, this.timezone, this.locale ) );
            }
            if ( diff_hour != 0 )
            {
                offsets.add ( new Hours ( diff_hour, this.timezone, this.locale ) );
            }
            if ( diff_minute != 0 )
            {
                offsets.add ( new Minutes ( diff_minute, this.timezone, this.locale ) );
            }
            if ( diff_second != 0 )
            {
                offsets.add ( new Seconds ( diff_second, this.timezone, this.locale ) );
            }
            if ( diff_millisecond != 0L
                 || offsets.size () == 0 )
            {
                offsets.add ( new Milliseconds ( diff_millisecond ) );
            }

            if ( offsets.size () == 1 )
            {
                total_time = offsets.get ( 0 );
            }
            else
            {
                final CalendarOffset [] template =
                    new CalendarOffset [ offsets.size () ];
                final CalendarOffset [] composite_offsets =
                    offsets.toArray ( template );
                total_time =
                    new CompositeCalendarOffsets ( composite_offsets );
            }
        }
        else if ( subtract_time instanceof RelativeTime )
        {
            // Calendar - Relative = new Calendar
            final Calendar calendar = this.calendar ();
            final long subtract_milliseconds = subtract_time.milliseconds ();
            final long milliseconds_chunk_size;
            if ( subtract_milliseconds >= 0L )
            {
                milliseconds_chunk_size = (long) Integer.MAX_VALUE;
            }
            else
            {
                milliseconds_chunk_size = (long) Integer.MIN_VALUE;
            }
            for ( long remaining_milliseconds = subtract_milliseconds;
                  remaining_milliseconds != 0L;
                  remaining_milliseconds -= milliseconds_chunk_size )
            {
                final int chunk = (int)
                    ( milliseconds_chunk_size % remaining_milliseconds );
                calendar.add ( 0 - chunk, Calendar.MILLISECOND );
            }

            total_time = new CalendarTime ( calendar );
        }
        else if ( subtract_time instanceof AbsoluteTime )
        {
            // Calendar - Absolute = new RelativeTime
            total_time = new RelativeTime ( this.seconds ()
                                            - subtract_seconds,
                                            this.nanoseconds ()
                                            - subtract_nanoseconds );
        }

        return total_time;
    }
}
