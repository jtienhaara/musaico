package musaico.foundation.time;

import java.io.Serializable;

import java.math.BigInteger;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * <p>
 * Represents an absolute but completely impossible point in time.
 * </p>
 *
 * <p>
 * For example, <code> Time.NEVER </code> represents a time in
 * the infinite past, and <code> Time.FOREVER </code> represents
 * a time in the infinite future.
 * </p>
 *
 * <p>
 * Operations:
 * </p>
 *
 * <pre>
 *     ImpossibleTime + AbsoluteTime   = ImpossibleTime
 *     ImpossibleTime + RelativeTime   = ImpossibleTime
 *     ImpossibleTime + ImpossibleTime = ImpossibleTime
 *     ImpossibleTime - AbsoluteTime   = ImpossibleTime
 *     ImpossibleTime - RelativeTime   = ImpossibleTime
 *     ImpossibleTime - ImpossibleTime = ImpossibleTime
 * </pre>
 *
 * <p>
 * The behaviour of SomeImplementationTime X +/- ImpossibleTime I
 * must always be implemented to return an ImpossibleTime.
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
public class ImpossibleTime
    implements Time, Serializable
{
    /** The number of seconds since midnight Jan 1 1970 GMT. */
    private final long seconds;

    /** The number of nanoseconds to offset the seconds by. */
    private final long nanoseconds;


    /**
     * <p>
     * Creates a new ImpossibleTime object, offset from midnight
     * Jan 1 1970 GMT by the specified number of seconds and
     * nanoseconds.  For ImpossibleTimes, these parameters are
     * essentially arbitrary -- they do not have to make sense.
     * </p>
     *
     * <p>
     * Java users: be wary, the first field is <i>seconds</i>
     * not milliseconds!
     * </p>
     *
     * @param seconds The impossible time in seconds since 0 UTC.
     *
     * @param nanoseconds The impossible time in nanoseconds since
     *                    the seconds parameter UTC.
     */
    public ImpossibleTime (
                           long seconds,
                           long nanoseconds
                           )
    {
        this.seconds = seconds;
        this.nanoseconds = nanoseconds;
    }


    /**
     * @see java.util.Comparable#compareTo(T)
     */
    public int compareTo (
                          Time that
                          )
    {
        if ( that == null )
        {
            // this < null.
            return -1;
        }
        else if ( ! ( that instanceof ImpossibleTime ) )
        {
            // this > non-ImpossibleTime.
            return 1;
        }

        // ImpossibleTimes.
        long this_seconds = this.seconds ();
        long that_seconds = that.seconds ();
        if ( this_seconds < that_seconds )
        {
            // this < that (e.g. NEVER < FOREVER).
            return -1;
        }
        else if ( this_seconds > that_seconds )
        {
            // this > that (e.g. FOREVER > NEVER).
            return 1;
        }

        long this_nanoseconds = this.nanoseconds ();
        long that_nanoseconds = that.nanoseconds ();
        if ( this_nanoseconds < that_nanoseconds )
        {
            // this < that (e.g. NEVER < FOREVER).
            return -1;
        }
        else if ( this_nanoseconds > that_nanoseconds )
        {
            // this > that (e.g. FOREVER > NEVER).
            return 1;
        }

        // this = that.
        return 0;
    }


    /**
     * @see java.lang.equals(java.lang.Object)
     */
    public boolean equals (
                           Object that
                           )
    {
        if ( that == null
             || ! ( that instanceof ImpossibleTime ) )
        {
            return false;
        }

        int comparison = this.compareTo ( (ImpossibleTime) that );
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
    public long seconds ()
    {
        return this.seconds;
    }


    /**
     * @see musaico.foundation.time.Time#nanoseconds()
     */
    public long nanoseconds ()
    {
        return this.nanoseconds;
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
     * @see musaico.foundation.time.Time#add(musaico.foundation.time.Time)
     */
    public Time add (
                     Time add_time
                     )
    {
        // No matter how much time is added or removed,
        // this impossible time + X always sums to
        // this impossible time.
        return this;
    }


    /**
     * @see musaico.foundation.time.Time#subtract(Time)
     */
    public Time subtract (
                          Time subtract_time
                          )
    {
        // No matter how much time is added or removed,
        // this impossible time - X always sums to
        // this impossible time.
        return this;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        if ( this.equals ( Time.NEVER ) )
        {
            return "never";
        }
        else if ( this.equals ( Time.FOREVER ) )
        {
            return "forever";
        }

        return "ImpossibleTime(" + this.seconds ()
            + "," + this.nanoseconds () + ")";
    }
}
