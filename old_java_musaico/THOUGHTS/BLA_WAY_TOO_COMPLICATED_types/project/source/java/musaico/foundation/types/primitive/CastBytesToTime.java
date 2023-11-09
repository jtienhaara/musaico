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
 * Casts values from byte arrays to Time.
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
public class CastBytesToTime
    implements TypeCaster<byte[],Time>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public Time cast (
                      byte [] from
                      )
        throws TypeException
    {
        if ( from == null
             || ( from.length != 16 && from.length != 8 ) )
        {
            throw new TypeException ( "Failed to cast from [%from_class%] [%from_object%] to [%to_class%]: Invalid format",
                                      "from_class", this.fromClass (),
                                      "from_object", from,
                                      "to_class", this.toClass () );
        }

        final long nanoseconds =
            from [ 7 ] << 56
            + from [ 6 ] << 48
            + from [ 5 ] << 40
            + from [ 4 ] << 32
            + from [ 3 ] << 24
            + from [ 2 ] << 16
            + from [ 1 ] << 8
            + from [ 0 ];

        final Time time;
        if ( from.length == 8 )
        {
            time = new RelativeTime ( 0L, nanoseconds );
        }
        else
        {
            final long seconds =
                from [ 15 ] << 56
                + from [ 14 ] << 48
                + from [ 13 ] << 40
                + from [ 12 ] << 32
                + from [ 11 ] << 24
                + from [ 10 ] << 16
                + from [ 9 ] << 8
                + from [ 8 ];

            time = new AbsoluteTime ( seconds, nanoseconds );
        }

        return time;
    }


    /**
     * @see musaico.types.TypeCaster#fromClass()
     */
    @Override
    public Class<byte[]> fromClass ()
    {
        return byte[].class;
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
