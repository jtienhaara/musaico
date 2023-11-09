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
 * A RelativeTime that varies depending on the absolute calendar time it is
 * relative to.
 * </p>
 *
 * </p>
 * For example, 1 month from now might mean 28 days, 29 days, 30 days or
 * 31 days from now, depending on the CalendarTime "now".
 * </p>
 *
 * </p>
 * Or 1 day from now might mean 24 hours from now, 23 hours
 * from now, or 25 hours from now, depending on the CalendarTime "now"
 * and its Daylight Savings Time settings.
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
 *   CalendarOffset + ImpossibleTime       = ImpossibleTime.
 *   CalendarOffset + CalendarOffset       = new CalendarOffset.
 *   CalendarOffset + RelativeTime         = new CalendarOffset.
 *   CalendarOffset + CalendarTime         = new CalendarTime.
 *   CalendarOffset + AbsoluteTime         = ImpossibleTime.
 *   CalendarOffset - ImpossibleTime       = ImpossibleTime.
 *   CalendarOffset - CalendarOffset       = new CalendarOffset.
 *   CalendarOffset - RelativeTime         = new CalendarOffset.
 *   CalendarOffset - CalendarTime         = ImpossibleTime.
 *   CalendarOffset - AbsoluteTime         = ImpossibleTime.
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
public abstract class CalendarOffset
    extends RelativeTime
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20131114L;
    private static final String serialVersionHash =
        "0xDB99F857427F0ACBE060FE5082C9E229A06E1D54";


    /** Checks constructor parameters, static method parameters, and so on. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( CalendarOffset.class );


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
            // CalendarOffset + null = ImpossibleTime.
            return new ImpossibleTime ( this.seconds (),
                                        this.nanoseconds () );
        }

        // CalendarOffset + ImpossibleTime       = ImpossibleTime.
        // CalendarOffset + CalendarOffset       = new CalendarOffset.
        // CalendarOffset + RelativeTime         = new CalendarOffset.
        // CalendarOffset + CalendarTime         = new CalendarTime.
        // CalendarOffset + AbsoluteTime         = ImpossibleTime.
        final Time total_time;
        if ( add_time instanceof ImpossibleTime )
        {
            // CalendarOffset + ImpossibleTime       = ImpossibleTime.
            total_time = add_time;
        }
        else if ( add_time instanceof CalendarOffset )
        {
            // CalendarOffset + CalendarOffset       = new CalendarOffset.
            final CalendarOffset [] this_calendar_offsets;
            if ( this instanceof CompositeCalendarOffsets )
            {
                this_calendar_offsets = ( (CompositeCalendarOffsets) this )
                    .calendarOffsets ();
            }
            else
            {
                this_calendar_offsets = new CalendarOffset [] { this };
            }

            final CalendarOffset [] that_calendar_offsets;
            if ( that instanceof CompositeCalendarOffsets )
            {
                that_calendar_offsets = ( (CompositeCalendarOffsets) that )
                    .calendarOffsets ();
            }
            else
            {
                that_calendar_offsets = new CalendarOffset [] { that };
            }

            final CalendarOffset [] all_calendar_offsets =
                new CalendarOffset [ this_calendar_offsets.length,
                                     that_calendar_offsets.length ];
            System.arraycopy ( this_calendar_offsets, 0,
                               all_calendar_offsets, 0,
                               this_calendar_offsets.length );
            System.arraycopy ( that_calendar_offsets, 0,
                               all_calendar_offsets,
                               this_calendar_offsets.length,
                               that_calendar_offsets.length );
            total_time = new CompositeCalendarOffsets ( all_calendar_offsets );
        }
        else if ( add_time instanceof RelativeTime )
        {
            // CalendarOffset + RelativeTime         = new CalendarOffset.
            final List<CalendarOffset> calendar_offsets_list =
                new ArrayList<CalendarOffset> ();
            if ( this instanceof CompositeCalendarOffsets )
            {
                final CompositeCalendarOffsets composite =
                    (CompositeCalendarOffsets) this;
                for ( CalendarOffset offset : composite.calendarOffsets () )
                {
                    calendar_offsets_list.add ( offset );
                }
            }
            else
            {
                calendar_offsets_list.add ( this );
            }

            final long milliseconds = add_time.milliseconds ();
            final long chunk_size;
            if ( milliseconds >= 0 )
            {
                chunk_size = (long) Integer.MAX_VALUE;
            }
            else
            {
                chunk_size = (long) Integer.MIN_VALUE;
            }

            long remaining = milliseconds;
            while ( remaining != 0L )
            {
                final long chunk = chunk_size % remaining;
                final CalendarOffset offset = new Milliseconds ( chunk );
                calendar_offsets_list.add ( offset );
                remaining -= chunk;
            }

            final CalendarOffset [] template =
                new CalendarOffset [ calendar_offsets_list.size () ];
            final CalendarOffset [] calendar_offsets =
                calendar_offsets_list.toArray ( template );

            total_time = new CompositeCalendarOffsets ( calendar_offsets );
        }
        else if ( add_time instanceof CalendarTime )
        {
            // CalendarOffset + CalendarTime         = new CalendarTime.
            final CalendarTime that = (CalendarTime) add_time;
            final Calendar calendar = that.calendar ();
            this.addOffsetTo ( calendar );
            total_time = new CalendarTime ( calendar );
        }
        else if ( add_time instanceof AbsoluteTime )
        {
            // CalendarOffset + AbsoluteTime         = ImpossibleTime.
            total_time = new ImpossibleTime ( this.seconds ()
                                              + add_time.seconds (),
                                              this.nanoseconds ()
                                              + add_time.nanoseconds () );
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
     * @see musaico.platform.time.RelativeTime#negate()
     */
    @Override
    public abstract CalendarOffset negate ();


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
            // CalendarOffset - null = ImpossibleTime.
            return new ImpossibleTime ( this.seconds (),
                                        this.nanoseconds () );
        }

        // CalendarOffset - ImpossibleTime       = ImpossibleTime.
        // CalendarOffset - CalendarOffset       = new CalendarOffset.
        // CalendarOffset - RelativeTime         = new CalendarOffset.
        // CalendarOffset - CalendarTime         = ImpossibleTime.
        // CalendarOffset - AbsoluteTime         = ImpossibleTime.
        final Time total_time;
        if ( subtract_time instanceof ImpossibleTime )
        {
            // CalendarOffset - ImpossibleTime       = ImpossibleTime.
            total_time = subtract_time;
        }
        else if ( subtract_time instanceof CalendarOffset )
        {
            // CalendarOffset - CalendarOffset       = new CalendarOffset.
            final CalendarOffset [] this_calendar_offsets;
            if ( this instanceof CompositeCalendarOffsets )
            {
                this_calendar_offsets = ( (CompositeCalendarOffsets) this )
                    .calendarOffsets ();
            }
            else
            {
                this_calendar_offsets = new CalendarOffset [] { this };
            }

            final CalendarOffset [] that_calendar_offsets;
            if ( that instanceof CompositeCalendarOffsets )
            {
                that_calendar_offsets = ( (CompositeCalendarOffsets) that )
                    .negate ()
                    .calendarOffsets ();
            }
            else
            {
                that_calendar_offsets = new CalendarOffset []
                    {
                        that.negate ()
                    };
            }

            final CalendarOffset [] all_calendar_offsets =
                new CalendarOffset [ this_calendar_offsets.length,
                                     that_calendar_offsets.length ];
            System.arraycopy ( this_calendar_offsets, 0,
                               all_calendar_offsets, 0,
                               this_calendar_offsets.length );
            System.arraycopy ( that_calendar_offsets, 0,
                               all_calendar_offsets,
                               this_calendar_offsets.length,
                               that_calendar_offsets.length );
            total_time = new CompositeCalendarOffsets ( all_calendar_offsets );
        }
        else if ( subtract_time instanceof RelativeTime )
        {
            // CalendarOffset - RelativeTime         = new CalendarOffset.
            final List<CalendarOffset> calendar_offsets_list =
                new ArrayList<CalendarOffset> ();
            if ( this instanceof CompositeCalendarOffsets )
            {
                final CompositeCalendarOffsets composite =
                    (CompositeCalendarOffsets) this;
                for ( CalendarOffset offset : composite.calendarOffsets () )
                {
                    calendar_offsets_list.add ( offset );
                }
            }
            else
            {
                calendar_offsets_list.add ( this );
            }

            final long milliseconds =
                subtract_time.negate ().milliseconds ();
            final long chunk_size;
            if ( milliseconds >= 0 )
            {
                chunk_size = (long) Integer.MAX_VALUE;
            }
            else
            {
                chunk_size = (long) Integer.MIN_VALUE;
            }

            long remaining = milliseconds;
            while ( remaining != 0L )
            {
                final long chunk = chunk_size % remaining;
                final CalendarOffset offset = new Milliseconds ( chunk );
                calendar_offsets_list.add ( offset );
                remaining -= chunk;
            }

            final CalendarOffset [] template =
                new CalendarOffset [ calendar_offsets_list.size () ];
            final CalendarOffset [] calendar_offsets =
                calendar_offsets_list.toArray ( template );

            total_time = new CompositeCalendarOffsets ( calendar_offsets );
        }
        else if ( subtract_time instanceof CalendarTime )
        {
            // CalendarOffset - CalendarTime         = ImpossibleTime.
            total_time = new ImpossibleTime ( this.seconds ()
                                              - subtract_time.seconds (),
                                              this.nanoseconds ()
                                              - subtract_time.nanoseconds () );
        }
        else if ( subtract_time instanceof AbsoluteTime )
        {
            // CalendarOffset - AbsoluteTime         = ImpossibleTime.
            total_time = new ImpossibleTime ( this.seconds ()
                                              - subtract_time.seconds (),
                                              this.nanoseconds ()
                                              - subtract_time.nanoseconds () );
        }

        return total_time;
    }


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
