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
 * An AbsoluteTime set according to some localized Calendar.
 * </p>
 *
 * <p>
 * Operations:
 * </p>
 *
 * <pre>
 *   CalendarTime + CalendarTime         = ImpossibleTime.
 *   CalendarTime + AbsoluteTime         = ImpossibleTime.
 *   CalendarTime + CalendarOffset       = new CalendarTime.
 *   CalendarTime + RelativeTime         = new CalendarTime.
 *   CalendarTime + ImpossibleTime       = ImpossibleTime.
 *   CalendarTime - CalendarTime         = CalendarOffset.
 *   CalendarTime - AbsoluteTime         = new RelativeTime.
 *   CalendarTime - CalendarOffset       = new CalendarTime.
 *   CalendarTime - RelativeTime         = new CalendarTime.
 *   CalendarTime - ImpossibleTime       = ImpossibleTime.
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


    /**
     * <p>
     * Creates a new CalendarTime with the specified Calendar.
     * </p>
     *
     * @param calendar The calendar which will be cloned for each
     *                 operation.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Calendar.clone () cast to Calendar.
    public AbstractCalendarTime (
                                 Calendar calendar
                                 )
        throws ParametersMustNotBeNull.Violation
    {
        super ( checkNull ( calendar ) );

        this.calendar = (Calendar) calendar.clone ();
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
    public AbstractCalendarTime (
                                 TimeZone timezone
                                 )
        throws ParametersMustNotBeNull.Violation
    {
        this ( Calendar.getInstance ( checkNull ( timezone ) ) );
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
    public AbstractCalendarTime (
                                 Locale locale
                                 )
        throws ParametersMustNotBeNull.Violation
    {
        this ( Calendar.getInstance ( checkNull ( locale ) ) );
    }


    /**
     * <p>
     * Creates a new AbstractCalendarTime for the specified
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
    public AbstractCalendarTime (
                                 TimeZone timezone,
                                 Locale locale
                                 )
        throws ParametersMustNotBeNull.Violation
    {
        this ( Calendar.getInstance ( checkNull ( timezone ),
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
            final Calendar calendar = this.calendar ();
            long add_seconds = add_time.seconds ();
            long add_milliseconds = add_time.nanoseconds () / 1000000L;
            if ( seconds > (long) Integer.MAX_VALUE
                 || milliseconds > (long) Integer.MAX_VALUE )
            {
                return new ImpossibleTime ( this.seconds () + add_seconds,
                                            this.nanoseconds ()
                                            + add_time.nanoseconds () );
            }

            calendar.add ( (int) add_seconds, Calendar.SECOND );
            calendar.add ( (int) add_milliseconds, Calendar.MILLISECOND );
            !!!!!!!!!!!!!!!!!;
            this.addOffsetTo ( calendar );
            total_time = new AbsoluteTime ( calendar.getTimeInMillis () );
            RelativeTime that = (RelativeTime) add_time;
            total_time = new CompositeRelativeTime ( this.calendar,
                                                     this, that );
        }
        else if ( add_time instanceof AbsoluteTime )
        {
            // Calendar + Calendar = Impossible.
            return new ImpossibleTime ( this.seconds () + add_time.seconds (),
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
