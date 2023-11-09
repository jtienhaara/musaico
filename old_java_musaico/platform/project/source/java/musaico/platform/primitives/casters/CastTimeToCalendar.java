package musaico.types.primitive;


import java.io.Serializable;

import java.util.Calendar;

import musaico.time.Time;
import musaico.time.AbsoluteTime;
import musaico.time.RelativeTime;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from Time to Calendar.
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
public class CastTimeToCalendar
    implements TypeCaster<Time,Calendar>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public Calendar cast (
                          Time from
                          )
        throws TypeException
    {
        if ( from instanceof RelativeTime )
        {
            throw new TypeCastException ( "Cannot cast relative Time difference [%time%] to absolute [%class%]",
                                          "time", from,
                                          "class", Calendar.class );
        }

        long seconds = from.seconds ();
        long nanoseconds = from.nanoseconds ();
        long milliseconds = ( seconds * 1000L ) + ( nanoseconds / 1000000L );

        Calendar calendar = Calendar.getInstance ();
        calendar.setTimeInMillis ( milliseconds );

        return calendar;
    }


    /**
     * @see musaico.types.TypeCaster#fromClass()
     */
    @Override
    public Class<Time> fromClass ()
    {
        return Time.class;
    }


    /**
     * @see musaico.types.TypeCaster#toClass()
     */
    @Override
    public Class<Calendar> toClass ()
    {
        return Calendar.class;
    }
}
