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
 * An offset in terms of number of weeks according to some specific calendar.
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
public class Weeks
    extends AbstractCalendarTime
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130929L;
    private static final String serialVersionHash =
        "0x3501B20F7B42FA3651A88273335BE400BBD9A529";


    /** # of seconds in one "average" day (no daylight savings changeover
     *  etc). */
    private static final long SECONDS_PER_WEEK = 7L * 24L * 60L * 60L;


    /** The number of weeks offset.  Can be negative. */
    private final int weeks;


    /**
     * <p>
     * Creates a new Weeks offset with the specified # of weeks.
     * </p>
     *
     * @param weeks The number of weeks (1, 2, 3, 7, -10, ... ).
     *
     * @param calendar The calendar to use for calculating day offsets.
     *                 Will be cloned for each operation.  Must not be null.
     */
    public Weeks (
                  int weeks,
                  Calendar calendar
                  )
        throws ParametersMustNotBeNull.Violation
    {
        super ( SECONDS_PER_WEEK * (long) weeks, // average seconds
                0L, // average nanoseconds
                calendar );

        this.weeks = weeks;
    }


    /**
     * <p>
     * Creates a new Weeks offset with the specified # of weeks
     * for the specified timezone (using Java's Calendar lookup)
     * </p>
     *
     * @param weeks The number of weeks (1, 2, 3, 7, -10, ... ).
     *
     * @param timezone The timezone by which to lookup the Calendar
     *                 implementation(such as Gregorian and so on,
     *                 with Daylight Savings Time settings according
     *                 to the timezone).  .  Must not be null.
     */
    public Weeks (
                  int weeks,
                  TimeZone timezone
                  )
        throws ParametersMustNotBeNull.Violation
    {
        super ( SECONDS_PER_WEEK * (long) weeks, // average seconds
                0L, // average nanoseconds
                timezone );

        this.weeks = weeks;
    }


    /**
     * <p>
     * Creates a new Weeks offset with the specified # of weeks
     * for the specified locale (using Java's Calendar lookup)
     * </p>
     *
     * @param weeks The number of weeks (1, 2, 3, 7, -10, ... ).
     *
     * @param locale The locale by which to lookup the Calendar
     *               implementation (such as Gregorian and so on,
     *               with Daylight Savings Time settings according
     *               to the locale).  Must not be null.
     */
    public Weeks (
                  int weeks,
                  Locale locale
                  )
        throws ParametersMustNotBeNull.Violation
    {
        super ( SECONDS_PER_WEEK * (long) weeks, // average seconds
                0L, // average nanoseconds
                locale );

        this.weeks = weeks;
    }


    /**
     * <p>
     * Creates a new Weeks offset with the specified # of weeks
     * for the specified timezone and locale (using Java's
     * Calendar lookup)
     * </p>
     *
     * @param weeks The number of weeks (1, 2, 3, 7, -10, ... ).
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
    public Weeks (
                  int weeks,
                  TimeZone timezone,
                  Locale locale
                  )
        throws ParametersMustNotBeNull.Violation
    {
        super ( SECONDS_PER_WEEK * (long) weeks, // average seconds
                0L, // average nanoseconds
                timezone,
                locale );

        this.weeks = weeks;
    }


    /**
     * @see musaico.platform.time.RelativeTime#negate()
     */
    @Override
    public AbstractCalendarTime negate ()
    {
        return new weeks ( 0 - this.weeks,
                          this.calendar () );
    }


    /**
     * @see musaico.platform.time.AbstractCalendarTime#addOffsetTo(java.util.Calendar)
     */
    protected void addOffsetTo (
                                Calendar calendar
                                )
    {
        calendar.add ( Calendar.weeks, this.weeks );
    }


    /**
     * @see musaico.platform.time.AbstractCalendarTime#subtractOffsetFrom(java.util.Calendar)
     */
    protected void subtractOffsetFrom (
                                       Calendar calendar
                                       )
    {
        calendar.add ( Calendar.weeks, 0 - this.weeks );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "" + this.weeks + " weeks";
    }
}
