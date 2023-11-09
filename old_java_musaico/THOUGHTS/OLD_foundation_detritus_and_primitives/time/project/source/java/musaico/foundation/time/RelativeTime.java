package musaico.foundation.time;

import java.io.Serializable;

import java.math.BigInteger;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Represents the difference between two absolute times.
 * </p>
 *
 * <p>
 * Operations:
 * </p>
 *
 * <pre>
 *   RelativeTime + AbsoluteTime   = new AbsoluteTime.
 *   RelativeTime + RelativeTime   = new RelativeTime.
 *   RelativeTime + ImpossibleTime = ImpossibleTime.
 *   RelativeTime - AbsoluteTime   = ImpossibleTime.
 *   RelativeTime - RelativeTime   = new RelativeTime.
 *   RelativeTime - ImpossibleTime = ImpossibleTime.
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
 * Copyright (c) 2009 Johann Tienhaara
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
public class RelativeTime
    implements Time, Serializable
{
    /** The number of seconds difference. */
    private final long seconds;

    /** The number of nanoseconds to offset the seconds by. */
    private final long nanoseconds;


    /**
     * <p>
     * Creates a RelativeTime period from the specified frequency in Hz.
     * </p>
     *
     * @param hz The frequency per second to convert to a period in
     *           terms of number of seconds and nanoseconds.
     *           Must be greater than 0.
     *
     * @return The RelativeTime representing the period corresponding
     *         to the specified frequency.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public static final RelativeTime fromFrequency (
                                                    double hz
                                                    )
        throws I18nIllegalArgumentException
    {
        if ( hz <= 0D )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a RelativeTime period from frequency [%hz%]",
                                                     "hz", hz );
        }

        long period_in_seconds = (long) ( 1D / hz );
        long period_in_nanoseconds = (long) ( 1000000000D / hz );
        double modulo_nanoseconds =
            1000000000D - ( (double) period_in_nanoseconds * hz );
        period_in_nanoseconds =
            period_in_nanoseconds % Time.NANOSECONDS_PER_SECOND;
        if ( ( modulo_nanoseconds * 2D ) >= hz )
        {
            period_in_nanoseconds ++;
        }

        return new RelativeTime ( period_in_seconds, period_in_nanoseconds );
    }


    /**
     * <p>
     * Creates a new Time object from the specified number of milliseconds.
     * </p>
     *
     * <p>
     * A helper in Java only.
     * </p>
     *
     * @param milliseconds The number of milliseconds.
     *                     Can be positive or negative or 0.
     */
    public RelativeTime (
                         long milliseconds
                         )
    {
        long seconds = milliseconds / 1000L;
        long nanoseconds = ( milliseconds % 1000L ) * 1000000L;

        this.seconds = seconds;
        this.nanoseconds = nanoseconds;
    }


    /**
     * <p>
     * Creates a new RelativeTime object with the specified
     * number of seconds and nanoseconds.
     * </p>
     *
     * <p>
     * Java users: be wary, the first field is <i>seconds</i>
     * not milliseconds!
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
     * @throws I18nIllegalArgumentException If the parameters are
     *                                      invalid (such as
     *                                      an invalid nanoseconds
     *                                      parameter).
     */
    public RelativeTime (
                         long seconds,
                         long nanoseconds
                         )
    {
        if ( seconds >= 0L )
        {
            if ( nanoseconds < 0L
                 || nanoseconds > Time.MAX_NANOSECONDS )
            {
                throw new I18nIllegalArgumentException ( "Cannot create [%time_class%] with [%seconds%] seconds and [%nanoseconds%] nanoseconds",
                                                         "time_class", this.getClass (),
                                                         "seconds", seconds,
                                                         "nanoseconds", nanoseconds );
            }
        }
        else if ( nanoseconds > 0L
                  || nanoseconds < Time.MIN_NANOSECONDS )
        {
            throw new I18nIllegalArgumentException ( "Cannot create [%time_class%] with [%seconds%] seconds and [%nanoseconds%] nanoseconds",
                                                     "time_class", this.getClass (),
                                                     "seconds", seconds,
                                                     "nanoseconds", nanoseconds );
        }

        this.seconds = seconds;
        this.nanoseconds = nanoseconds;
    }


    /**
     * <p>
     * Creates a new RelativeTime object with the specified
     * number of seconds and nanoseconds.
     * </p>
     *
     * @param seconds_and_nanoseconds The number of nanoseconds offset.
     *                                Can be positive or negative.
     *                                Must not be null.
     *
     * @throws I18nIllegalArgumentException If the parameters are
     *                                      invalid (see above).
     */
    public RelativeTime (
                         BigInteger seconds_and_nanoseconds
                         )
    {
        this ( seconds_and_nanoseconds.divide ( Time.BIG_NANOSECONDS_PER_SECOND ).longValue (),
               seconds_and_nanoseconds.mod ( Time.BIG_NANOSECONDS_PER_SECOND ).longValue () );
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
        else if ( that instanceof ImpossibleTime )
        {
            // this < ImpossibleTime.
            return 1;
        }
        else if ( ! ( that instanceof RelativeTime ) )
        {
            // this > non-RelativeTime.
            return 1;
        }

        // RelativeTimes.
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
             || ! ( that instanceof RelativeTime ) )
        {
            return false;
        }

        int comparison = this.compareTo ( (RelativeTime) that );
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
     * @see musaico.foundation.time.Time#add(Time)
     */
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
            // Relative + Impossible = Impossible.
            total_time = add_time;
        }
        else if ( add_time instanceof AbsoluteTime )
        {
            // Relative + Absolute = new Absolute.
            total_time = new AbsoluteTime ( total_seconds,
                                            total_nanoseconds );
        }
        else
        {
            // Relative + Relative = new Relative.
            total_time = new RelativeTime ( total_seconds,
                                            total_nanoseconds );
        }

        return total_time;
    }


    /**
     * @see musaico.foundation.time.Time#subtract(Time)
     */
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
            // Relative - Impossible = no change.
            total_time = this;
        }
        else if ( subtract_time instanceof AbsoluteTime )
        {
            // Relative - Absolute = Impossible.
            total_time = new ImpossibleTime ( total_seconds,
                                              total_nanoseconds );
        }
        else
        {
            // Relative - Relative = new Relative.
            total_time = new RelativeTime ( total_seconds,
                                            total_nanoseconds );
        }

        return total_time;
    }


    /**
     * @see java.lang.Object#toString()
     */
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
