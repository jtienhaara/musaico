package musaico.platform.time;

import java.io.Serializable;


/**
 * <p>
 * Time representation.
 * </p>
 *
 * <p>
 * A Time object is immutable.
 * </p>
 *
 * <p>
 * Absolute time is epoch time, that is, time that has passed since
 * (or before, if negative) midnight January 1st, 1970 UTC.
 * </p>
 *
 * <p>
 * A Time object may also represent a difference between two times,
 * or may be extended to cover human calendar time.
 * </p>
 *
 *
 * <p>
 * In Java, an absolute (epoch) Time may be manually converted to
 * a Java Date by calling:
 * </p>
 *
 * <pre>
 *     Time time = ...;
 *     long seconds = time.seconds ();
 *     long nanoseconds = time.nanoseconds ();
 *     long milliseconds = ( seconds * 1000L ) + ( nanoseconds / 1000000L );
 *     Date date = new Date ( milliseconds );
 * </pre>
 *
 * <p>
 * Note that precision is lost when converting to a Date.
 * </p>
 *
 * <p>
 * To convert from a Date to an absolute (epoch) Time:
 * </p>
 *
 * <pre>
 *     Date date = ...;
 *     long milliseconds = date.getTime ();
 *     long seconds = milliseconds / 1000L;
 *     long nanoseconds = ( milliseonds % 1000L ) * 1000000L;
 *     Time time = ... ( seconds, nanoseconds );
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
public interface Time
    extends Comparable<Time>, Serializable
{
    /** The number of nanoseconds per second. */
    public static final long NANOSECONDS_PER_SECOND = 1000000000L;

    /** The number of nanoseconds per second - 1.
     *  Used by the Time implementations to check parameter values. */
    public static final long MAX_NANOSECONDS = NANOSECONDS_PER_SECOND - 1L;

    /** The number of ( nanoseconds per second - 1 ) times -1.
     *  Used by the Time implementations to check parameter values. */
    public static final long MIN_NANOSECONDS = 1L - NANOSECONDS_PER_SECOND;


    /** Useful constant: a time in the infinite past.  Not an
     *  AbsoluteTime nor a RelativeTime. */
    public static final Time NEVER =
        new ImpossibleTime ( Long.MIN_VALUE, Long.MIN_VALUE );

    /** Useful constant: a time in the infinite future.  Not
     *  an AbsoluteTime nor a RelativeTime. */
    public static final Time FOREVER =
        new ImpossibleTime ( Long.MAX_VALUE, Long.MAX_VALUE );


    /**
     * <p>
     * Returns the number of seconds.
     * </p>
     *
     * <p>
     * If this Time is a relative time, then the seconds
     * represents the amount of time elapsed between two
     * absolute times.
     * </p>
     *
     * <p>
     * If this Time is an absolute time, then the seconds
     * represent the number of seconds elapsed from midnight
     * January 1st, 1970 GMT, until the specific instant
     * in time (UTC time).
     * </p>
     *
     * @return The number of seconds.  Can be negative.
     */
    public abstract long seconds ();


    /**
     * <p>
     * Returns the number of nanoseconds.
     * </p>
     *
     * @return The number of nanoseconds.  Can be negative.
     */
    public abstract long nanoseconds ();


    /**
     * <p>
     * Converts this time to milliseconds.  A convenience method
     * for Java only.
     * </p>
     *
     * @return The number of milliseconds.  May lose precision.
     *         Can be negative.
     */
    public abstract long milliseconds ();


    /**
     * <p>
     * Returns the sum of this + add_time.
     * </p>
     *
     * @param add_time The Time which will be added to this
     *                 Time.  Must not be an ImpossibleTime.
     *                 Might also have other domain restrictions
     *                 by implementation.  Must not be null.
     *
     * @return The result of this + add_time.  Possibly an
     *         ImpossibleTime, if the parameter is invalid for
     *         this implementation.  Never null.
     */
    public abstract Time add (
                              Time add_time
                              );


    /**
     * <p>
     * Returns the difference of this - subtract_time.
     * </p>
     *
     * @param subtract_time The Time which will be subtracted from this
     *                      Time.  Must not be an ImpossibleTime.
     *                      Might also have other domain restrictions
     *                      by implementation.  Must not be null.
     *
     * @return The result of this - subtract_time.  Possibly an
     *         ImpossibleTime, if the parameter is invalid for
     *         this implementation.  Never null.
     */
    public abstract Time subtract (
                                   Time subtract_time
                                   );


    // Every Time must implement compareTo (), hashCode () and equals ().
}
