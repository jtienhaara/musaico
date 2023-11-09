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
 * An offset in terms of number of (some period, such as days or weeks
 * or months or hours or seconds and so on), according to some
 * specific calendar.
 * </p>
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
public class AbstractCalendarPeriod
    extends AbstractCalendarTime
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20131002L;
    private static final String serialVersionHash =
        "0x3501B20F7B42FA3651A88273335BE400BBD9A529";


    /** The Calendar field to add when performing calculations. */
    private final int calendarField;

    /** The number of periods offset.  Can be negative. */
    private final int periods;


    /**
     * <p>
     * Creates a new AbstractCalendarPeriod with the specified # of
     * periods.
     * </p>
     *
     * @param average_seconds The number of seconds for the total number
     *                        of periods, such as an average
     *                        non-daylight-savings-boundary day (24 hours),
     *                        or a non-leap year (365 days), or 12
     *                        average months (360 days), and so on.
     *                        Can be negative or positive.
     *
     * @param calendar_field The field to add to when performing
     *                       calculations.  For example, Calendar.YEAR
     *                       or Calendar.DAY_OF_YEAR and so on.
     *                       Must be a valid Calendar field.
     *
     * @param periods The number of periods (1, 2, 3, 7, -10, ... ).
     *                For example, the number of days, or months, or years.
     *
     * @param calendar The calendar to use for calculating period offsets.
     *                 Will be cloned for each operation.  Must not be null.
     */
    public AbsractCalendarPeriod (
                                  long average_seconds,
                                  int calendar_field,
                                  int periods,
                                  Calendar calendar
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        super ( average_seconds,
                0L, // average nanoseconds
                calendar );

        this.calendarField = calendar_field;
        this.periods = periods;
    }


    /**
     * <p>
     * Creates a new AbstractCalendarPeriod offset with the specified #
     * of periods for the specified timezone (using Java's Calendar lookup)
     * </p>
     *
     * @param average_seconds The number of seconds for the total number
     *                        of periods, such as an average
     *                        non-daylight-savings-boundary day (24 hours),
     *                        or a non-leap year (365 days), or 12
     *                        average months (360 days), and so on.
     *                        Can be negative or positive.
     *
     * @param calendar_field The field to add to when performing
     *                       calculations.  For example, Calendar.YEAR
     *                       or Calendar.DAY_OF_YEAR and so on.
     *                       Must be a valid Calendar field.
     *
     * @param periods The number of periods (1, 2, 3, 7, -10, ... ).
     *                For example, the number of days, or months, or years.
!!!     *
     * @param periods The number of periods (1, 2, 3, 7, -10, ... ).
     *
     * @param timezone The timezone by which to lookup the Calendar
     *                 implementation(such as Gregorian and so on,
     *                 with Daylight Savings Time settings according
     *                 to the timezone).  .  Must not be null.
     */
    public AbstractCalendarPeriod (
                                   long average_seconds,
                                   int calendar_field,
                                   int days,
                                   TimeZone timezone
                                   )
        throws ParametersMustNotBeNull.Violation
    {
        super ( SECONDS_PER_DAY * (long) days, // average seconds
                0L, // average nanoseconds
                timezone );

        this.days = days;
    }


    /**
     * <p>
     * Creates a new AbstractCalendarPeriod offset with the specified # of days
     * for the specified locale (using Java's Calendar lookup)
     * </p>
     *
     * @param average_seconds The number of seconds for the total number
     *                        of periods, such as an average
     *                        non-daylight-savings-boundary day (24 hours),
     *                        or a non-leap year (365 days), or 12
     *                        average months (360 days), and so on.
     *                        Can be negative or positive.
     *
     * @param calendar_field The field to add to when performing
     *                       calculations.  For example, Calendar.YEAR
     *                       or Calendar.DAY_OF_YEAR and so on.
     *                       Must be a valid Calendar field.
     *
     * @param periods The number of periods (1, 2, 3, 7, -10, ... ).
     *                For example, the number of days, or months, or years.
!!!     *
     * @param days The number of days (1, 2, 3, 7, -10, ... ).
     *
     * @param locale The locale by which to lookup the Calendar
     *               implementation (such as Gregorian and so on,
     *               with Daylight Savings Time settings according
     *               to the locale).  Must not be null.
     */
    public AbstractCalendarPeriod (
                                   long average_seconds,
                                   int calendar_field,
                                   int days,
                                   Locale locale
                                   )
        throws ParametersMustNotBeNull.Violation
    {
        super ( SECONDS_PER_DAY * (long) days, // average seconds
                0L, // average nanoseconds
                locale );

        this.days = days;
    }


    /**
     * <p>
     * Creates a new AbstractCalendarPeriod offset with the specified # of days
     * for the specified timezone and locale (using Java's
     * Calendar lookup)
     * </p>
     *
     * @param average_seconds The number of seconds for the total number
     *                        of periods, such as an average
     *                        non-daylight-savings-boundary day (24 hours),
     *                        or a non-leap year (365 days), or 12
     *                        average months (360 days), and so on.
     *                        Can be negative or positive.
     *
     * @param calendar_field The field to add to when performing
     *                       calculations.  For example, Calendar.YEAR
     *                       or Calendar.DAY_OF_YEAR and so on.
     *                       Must be a valid Calendar field.
     *
     * @param periods The number of periods (1, 2, 3, 7, -10, ... ).
     *                For example, the number of days, or months, or years.
!!!     *
     * @param days The number of days (1, 2, 3, 7, -10, ... ).
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
    public AbstractCalendarPeriod (
                                   long average_seconds,
                                   int calendar_field,
                                   int days,
                                   TimeZone timezone,
                                   Locale locale
                                   )
        throws ParametersMustNotBeNull.Violation
    {
        super ( SECONDS_PER_DAY * (long) days, // average seconds
                0L, // average nanoseconds
                timezone,
                locale );

        this.days = days;
    }


    /**
     * @see musaico.platform.time.AbstractCalendarTime#addOffsetTo(java.util.Calendar)
     */
    protected void addOffsetTo (
                                Calendar calendar
                                )
    {
        calendar.add ( this.calendarField, this.periods );
    }


    /**
     * @see musaico.platform.time.AbstractCalendarTime#subtractOffsetFrom(java.util.Calendar)
     */
    protected void subtractOffsetFrom (
                                       Calendar calendar
                                       )
    {
        calendar.add ( this.calendarField, 0 - this.periods );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "" + this.periods + " " + getClass ().getSimpleName ();
    }
}
