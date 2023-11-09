package musaico.platform.time;

import java.io.Serializable;

import java.util.Calendar;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A number of RelativeTimes added together, usually kept separate
 * because there are AbstractCalendarTimes involved which cannot be
 * calculated until an AbsoluteTime is in the mix.
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
public class CompositeRelativeTime
    extends AbstractCalendarTime
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130929L;
    private static final String serialVersionHash =
        "0x472A1CC0187036BC935127718C49CBBA4986CFD8";


    /** Checks constructor parameters, static method parameters, and so on. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( CompositeRelativeTime.class );


    /** The relative times to add together. */
    private final RelativeTime [] relativeTimes;


    /**
     * <p>
     * Creates a new CompositeRelativeTime from the specified
     * RelativeTimes, adding them together whenever requested.
     * </p>
     *
     * @param calendar The calendar to use for calendar operations.
     *                 Must not be null.
     *
     * @param relative_times The times which will be added together.
     *                       Must not be null.  Must not contain any
     *                       null elements.
     */
    public CompositeRelativeTime (
                                  Calendar calendar,
                                  RelativeTime ... relative_times
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        super ( 0L, // seconds
                0L, // nanoseconds
                calendar );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               calendar, relative_times );

        this.relativeTimes = new RelativeTime [ relative_times.length ];
        System.arraycopy ( relative_times, 0,
                           this.relativeTimes, 0, relative_times.length );
    }


    /**
     * @return The negative of this relative time.  For example, if this
     *         relative time is +1.1 seconds, then -1.1 seconds will
     *         be returned.  Never null.
     */
    @Override
    public CompositeRelativeTime negate ()
    {
        RelativeTime [] negated_times =
            new RelativeTime [ this.relativeTimes.length ];
        for ( int rt = 0; rt < this.relativeTimes.length; rt ++ )
        {
            negated_times [ rt ] = this.relativeTimes [ rt ].negate ();
        }

        return new CompositeRelativeTime ( this.calendar (),
                                           negated_times );
    }


    /**
     * @see musaico.platform.time.CompositeRelativeTime#addOffsetTo(java.util.Calendar)
     */
    @Override
    protected void addOffsetTo (
                                Calendar calendar
                                )
    {
        for ( RelativeTime relative_time : this.relativeTimes )
        {
            if ( relative_time instanceof AbstractCalendarTime )
            {
                final AbstractCalendarTime calendar_time =
                    (AbstractCalendarTime) relative_time;
                calendar_time.addOffsetTo ( calendar );
            }
            else
            {
                // Calendar doesn't have a NANOSECONDS field.
                final long milliseconds = relative_time.milliseconds ();
                calendar.add ( (int) milliseconds,
                               Calendar.MILLISECOND );
            }
        }
    }


    /**
     * @see musaico.platform.time.CompositeRelativeTime#subtractOffsetFrom(java.util.Calendar)
     */
    @Override
    protected void subtractOffsetFrom (
                                       Calendar calendar
                                       )
    {
        for ( RelativeTime relative_time : this.relativeTimes )
        {
            if ( relative_time instanceof AbstractCalendarTime )
            {
                final AbstractCalendarTime calendar_time =
                    (AbstractCalendarTime) relative_time;
                calendar_time.subtractOffsetFrom ( calendar );
            }
            else
            {
                // Calendar doesn't have a NANOSECONDS field.
                final long milliseconds = relative_time.milliseconds ();
                calendar.add ( 0 - (int) milliseconds,
                               Calendar.MILLISECOND);
            }
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "(" );
        boolean is_first = true;
        for ( RelativeTime relative_time : this.relativeTimes )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( " +" );
            }

            sbuf.append ( " " + relative_time );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( ")" );

        return sbuf.toString ();
    }
}
