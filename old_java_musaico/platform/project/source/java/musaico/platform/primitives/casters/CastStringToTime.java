package musaico.types.primitive;


import java.io.Serializable;

import musaico.time.AbsoluteTime;
import musaico.time.RelativeTime;
import musaico.time.Time;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from String to Time.
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
public class CastStringToTime
    implements TypeCaster<String,Time>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public Time cast (
                      String from
                      )
        throws TypeException
    {
        int units_index = from.indexOf ( " seconds" );

        if ( units_index <= 0 )
        {
            throw new TypeException ( "Failed to cast from [%from_class%] [%from_object%] to [%to_class%]: Invalid format",
                                      "from_class", this.fromClass (),
                                      "from_object", from,
                                      "to_class", this.toClass () );
        }

        String [] seconds_nanoseconds =
            from.substring ( 0, units_index ).split ( "\\." );

        Time time;

        try
        {
            final long seconds = Long.parseLong ( seconds_nanoseconds [ 0 ] );
            final long nanoseconds;
            if ( seconds_nanoseconds.length == 1 )
            {
                nanoseconds = 0L;
            }
            else if ( seconds_nanoseconds.length == 2 )
            {
                nanoseconds = Long.parseLong ( seconds_nanoseconds [ 1 ] );
            }
            else
            {
                throw new TypeException ( "Failed to cast from [%from_class%] [%from_object%] to [%to_class%]: Invalid format",
                                          "from_class", this.fromClass (),
                                          "from_object", from,
                                          "to_class", this.toClass () );
            }

            String units = from.substring ( units_index + 1 );
            if ( "seconds UTC".equals ( units ) )
            {
                time = new AbsoluteTime ( seconds, nanoseconds );
            }
            else if ( "seconds".equals ( units ) )
            {
                time = new RelativeTime ( seconds, nanoseconds );
            }
            else
            {
                throw new TypeException ( "Failed to cast from [%from_class%] [%from_object%] to [%to_class%]: Unrecognized units [%units%]",
                                          "from_class", this.fromClass (),
                                          "from_object", from,
                                          "to_class", this.toClass (),
                                          "units", units );
            }

            return time;
        }
        catch ( Throwable t )
        {
            throw new TypeException ( "Failed to cast from [%from_class%] [%from_object%] to [%to_class%]",
                                      "from_class", this.fromClass (),
                                      "from_object", from,
                                      "to_class", this.toClass (),
                                      "cause", t );
        }
    }


    /**
     * @see musaico.types.TypeCaster#fromClass()
     */
    @Override
    public Class<String> fromClass ()
    {
        return String.class;
    }


    /**
     * @see musaico.types.TypeCaster#toClass()
     */
    @Override
    public Class<Time> toClass ()
    {
        return Time.class;
    }
}
