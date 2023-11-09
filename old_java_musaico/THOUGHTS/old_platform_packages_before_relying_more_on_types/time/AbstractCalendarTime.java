package musaico.platform.time;

import java.io.Serializable;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A RelativeTime that varies depending on the absolute time it is
 * relative to.
 * </p>
 *
 * </p>
 * For example, 1 month from now might mean 28 days, 29 days, 30 days or
 * 31 days from now, depending on the AbsoluteTime "now".
 * </p>
 *
 * </p>
 * Or 1 day from now might mean 24 hours from now, 23 hours
 * from now, or 25 hours from now, depending on the AbsoluteTime "now"
 * and the Daylight Savings Time settings of the locale.
 * </p>
 *
 * <p>
 * And so on.
 * </p>
 *
 * <p>
 * Operations:
 * </p>
 *
 * <pre>
 *   AbstractCalendarTime + AbsoluteTime         = new AbsoluteTime.
 *   AbstractCalendarTime + RelativeTime         = new AbstractCalendarTime.
 *   AbstractCalendarTime + AbstractCalendarTime = new AbstractCalendarTime.
 *   AbstractCalendarTime + ImpossibleTime       = ImpossibleTime.
 *   AbstractCalendarTime - AbsoluteTime         = ImpossibleTime.
 *   AbstractCalendarTime - RelativeTime         = new AbstractCalendarTime.
 *   AbstractCalendarTime - AbstractCalendarTime = new AbstractCalendarTime.
 *   AbstractCalendarTime - ImpossibleTime       = ImpossibleTime.
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
public abstract class AbstractCalendarTime
    extends RelativeTime
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130929L;
    private static final String serialVersionHash =
        "0xDB99F857427F0ACBE060FE5082C9E229A06E1D54";


    /** Checks constructor parameters, static method parameters, and so on. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AbstractCalendarTime.class );


    /** Synchronize all critical sections on this token: */
    private final Serializable lock = new String ();

    /** The Calendar used to calculate all Time additions and
     *  subtractions.  Cloned for each operation. */
    private final Calendar calendar;


    /**
     * <p>
     * Creates a new AbstractCalendarTime with the specified
     * Calendar and "average" number of seconds and nanoseconds.
     * </p>
     *
     * <p>
     * For example, a "day" offset might be the seconds equivalent of
     * 24 hours, even if not every day is 24 hours.  Or a "month"
     * offset might be the seconds equivalent of 30 days, even if not
     * every month is 30 days.  And so on.
     * </p>
     *
     * @param seconds The number of seconds offset.
     *                Can be positive or negative.
     *
     * @param nanoseconds The number of nanoseconds offset since the
     *                    seconds parameter.  If seconds is
     *                    positive or 0 then nanoseconds must be
     *                    between 0 and Time.MAX_NANOSECONDS (inclusive).
     *                    If seconds is negative then nanoseconds
     *                    must be between Time.MIN_NANOSECONDS
     *                    and 0 (inclusive).
     *
     * @param calendar The calendar which will be cloned for each
     *                 operation.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Calendar.clone () cast to Calendar.
    public AbstractCalendarTime (
                                 long seconds,
                                 long nanoseconds,
                                 Calendar calendar
                                 )
        throws Parameter2.DependsOn.Parameter1.Violation,
               Parameter2.MustBeInBounds.Violation,
               ParametersMustNotBeNull.Violation
    {
        super ( seconds, nanoseconds );

        this.calendar = (Calendar) calendar.clone ();
    }


    /**
     * <p>
     * Creates a new AbstractCalendarTime for the specified
     * timezone (using Java's Calendar lookup)
     * and the specified default time offset.
     * </p>
     *
     * <p>
     * For example, a "day" offset might be the seconds equivalent of
     * 24 hours, even if not every day is 24 hours.  Or a "month"
     * offset might be the seconds equivalent of 30 days, even if not
     * every month is 30 days.  And so on.
     * </p>
     *
     * @param seconds The number of seconds offset.
     *                Can be positive or negative.
     *
     * @param nanoseconds The number of nanoseconds offset since the
     *                    seconds parameter.  If seconds is
     *                    positive or 0 then nanoseconds must be
     *                    between 0 and Time.MAX_NANOSECONDS (inclusive).
     *                    If seconds is negative then nanoseconds
     *                    must be between Time.MIN_NANOSECONDS
     *                    and 0 (inclusive).
     *
     * @param timezone The timezone by which to lookup the Calendar
     *                 implementation(such as Gregorian and so on,
     *                 with Daylight Savings Time settings according
     *                 to the timezone).  .  Must not be null.
     */
    public AbstractCalendarTime (
                                 long seconds,
                                 long nanoseconds,
                                 TimeZone timezone
                                 )
        throws Parameter2.DependsOn.Parameter1.Violation,
               Parameter2.MustBeInBounds.Violation,
               ParametersMustNotBeNull.Violation
    {
        this ( seconds,
               nanoseconds,
               Calendar.getInstance ( checkNull ( timezone ) ) );
    }


    /**
     * <p>
     * Creates a new AbstractCalendarTime for the specified
     * locale (using Java's Calendar lookup)
     * and the specified default time offset.
     * </p>
     *
     * <p>
     * For example, a "day" offset might be the seconds equivalent of
     * 24 hours, even if not every day is 24 hours.  Or a "month"
     * offset might be the seconds equivalent of 30 days, even if not
     * every month is 30 days.  And so on.
     * </p>
     *
     * @param seconds The number of seconds offset.
     *                Can be positive or negative.
     *
     * @param nanoseconds The number of nanoseconds offset since the
     *                    seconds parameter.  If seconds is
     *                    positive or 0 then nanoseconds must be
     *                    between 0 and Time.MAX_NANOSECONDS (inclusive).
     *                    If seconds is negative then nanoseconds
     *                    must be between Time.MIN_NANOSECONDS
     *                    and 0 (inclusive).
     *
     * @param locale The locale by which to lookup the Calendar
     *               implementation (such as Gregorian and so on,
     *               with Daylight Savings Time settings according
     *               to the locale).  Must not be null.
     */
    public AbstractCalendarTime (
                                 long seconds,
                                 long nanoseconds,
                                 Locale locale
                                 )
        throws Parameter2.DependsOn.Parameter1.Violation,
               Parameter2.MustBeInBounds.Violation,
               ParametersMustNotBeNull.Violation
    {
        this ( seconds,
               nanoseconds,
               Calendar.getInstance ( checkNull ( locale ) ) );
    }


    /**
     * <p>
     * Creates a new AbstractCalendarTime for the specified
     * timezone and locale (using Java's Calendar lookup)
     * and the specified default time offset.
     * </p>
     *
     * <p>
     * For example, a "day" offset might be the seconds equivalent of
     * 24 hours, even if not every day is 24 hours.  Or a "month"
     * offset might be the seconds equivalent of 30 days, even if not
     * every month is 30 days.  And so on.
     * </p>
     *
     * @param seconds The number of seconds offset.
     *                Can be positive or negative.
     *
     * @param nanoseconds The number of nanoseconds offset since the
     *                    seconds parameter.  If seconds is
     *                    positive or 0 then nanoseconds must be
     *                    between 0 and Time.MAX_NANOSECONDS (inclusive).
     *                    If seconds is negative then nanoseconds
     *                    must be between Time.MIN_NANOSECONDS
     *                    and 0 (inclusive).
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
    public AbstractCalendarTime (
                                 long seconds,
                                 long nanoseconds,
                                 TimeZone timezone,
                                 Locale locale
                                 )
        throws Parameter2.DependsOn.Parameter1.Violation,
               Parameter2.MustBeInBounds.Violation,
               ParametersMustNotBeNull.Violation
    {
        this ( seconds,
               nanoseconds,
               Calendar.getInstance ( checkNull ( timezone ),
                                      checkNull ( locale ) ) );
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
     * @see musaico.foundation.time.Time#add(Time)
     */
    @Override
    public Time add (
                     Time add_time
                     )
    {
        if ( add_time == null )
        {
            // AbstractCalendarTime + null = ImpossibleTime.
            return new ImpossibleTime ( this.seconds (),
                                        this.nanoseconds () );
        }

        final Time total_time;
        if ( add_time instanceof ImpossibleTime )
        {
            // Calendar + Impossible = Impossible.
            total_time = add_time;
        }
        else if ( add_time instanceof AbstractCalendarTime )
        {
            // Calendar + Calendar = new Calendar.
            AbstractCalendarTime that = (AbstractCalendarTime)
                add_time;
            total_time = new CompositeRelativeTime ( this.calendar,
                                                     this, that );
        }
        else if ( add_time instanceof RelativeTime )
        {
            // Calendar + Relative = new Calendar.
            RelativeTime that = (RelativeTime) add_time;
            total_time = new CompositeRelativeTime ( this.calendar,
                                                     this, that );
        }
        else // AbsoluteTime.
        {
            // Calendar + Absolute = new Absolute.
            // Clone the calendar.
            final Calendar calendar = this.calendar ();
            calendar.setTimeInMillis ( add_time.milliseconds () );
            this.addOffsetTo ( calendar );
            total_time = new AbsoluteTime ( calendar.getTimeInMillis () );
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

        calendar.set ( 0, 0, 0, 0, 0, 0 );

        return calendar;
    }


    /**
     * @see musaico.platform.time.RelativeTime#negate()
     */
    @Override
    public abstract AbstractCalendarTime negate ();


    /**
     * @see musaico.foundation.time.Time#subtract(Time)
     */
    @Override
    public Time subtract (
                          Time subtract_time
                          )
    {
        if ( subtract_time == null )
        {
            // AbstractCalendarTime - null = ImpossibleTime.
            return new ImpossibleTime ( this.seconds (),
                                        this.nanoseconds () );
        }

        final Time total_time;
        if ( subtract_time instanceof ImpossibleTime )
        {
            // Calendar - Impossible = Impossible.
            total_time = subtract_time;
        }
        else if ( subtract_time instanceof AbstractCalendarTime )
        {
            // Calendar - Calendar = new Calendar.
            final AbstractCalendarTime calendar_time =
                (AbstractCalendarTime) subtract_time;
            total_time = new CompositeRelativeTime ( this.calendar,
                                                     this,
                                                     calendar_time.negate () );
        }
        else if ( subtract_time instanceof RelativeTime )
        {
            // Calendar - Relative = new Calendar.
            final RelativeTime relative_time = (RelativeTime) subtract_time;
            total_time = new CompositeRelativeTime ( this.calendar,
                                                     this,
                                                     relative_time.negate () );
        }
        else // AbsoluteTime.
        {
            // Calendar - Absolute = new Impossible.
            total_time = new ImpossibleTime ( this.seconds (),
                                              this.nanoseconds () );
        }

        return total_time;
    }


    /**
     * <p>
     * Operates on the specified Calendar date/time.
     * </p>
     *
     * @param calendar The Calendar time to which this offset will be
     *                 added.  The Calendar is considered private
     *                 to this call, and therefore is assumed to be
     *                 thread-safe, and so will not be cloned.
     *                 Must not be null.
     */
    protected abstract void addOffsetTo (
                                         Calendar calendar
                                         );


    /**
     * <p>
     * Operates on the specified Calendar date/time.
     * </p>
     *
     * @param calendar The Calendar time from which this offset will be
     *                 subtracted.  The Calendar is considered private
     *                 to this call, and therefore is assumed to be
     *                 thread-safe, and so will not be cloned.
     *                 Must not be null.
     */
    protected abstract void subtractOffsetFrom (
                                                Calendar calendar
                                                );


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public abstract String toString ();
}
