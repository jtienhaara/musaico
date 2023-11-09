package musaico.types.primitive;


import java.io.Serializable;

import musaico.time.Time;
import musaico.time.AbsoluteTime;
import musaico.time.RelativeTime;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from Time to byte array.
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
public class CastTimeToBytes
    implements TypeCaster<Time,byte[]>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public byte [] cast (
                         Time from
                         )
        throws TypeException
    {
        final byte [] bytes;
        final long nanoseconds;
        if ( from instanceof RelativeTime )
        {
            bytes = new byte [ 8 ];
            long seconds = from.seconds ();
            nanoseconds = from.nanoseconds () + ( seconds * 1000000000L );
        }
        else if ( from instanceof AbsoluteTime )
        {
            bytes = new byte [ 16 ];
            long seconds = from.seconds ();
            bytes [ 15 ] = (byte) ( ( seconds & 0xFF00000000000000L ) >> 56 );
            bytes [ 14 ] = (byte) ( ( seconds & 0x00FF000000000000L ) >> 48 );
            bytes [ 13 ] = (byte) ( ( seconds & 0x0000FF0000000000L ) >> 40 );
            bytes [ 12 ] = (byte) ( ( seconds & 0x000000FF00000000L ) >> 32 );
            bytes [ 11 ] = (byte) ( ( seconds & 0x00000000FF000000L ) >> 24 );
            bytes [ 10 ] = (byte) ( ( seconds & 0x0000000000FF0000L ) >> 16 );
            bytes [ 9 ] = (byte) ( ( seconds & 0x000000000000FF00L ) >> 8 );
            bytes [ 8 ] = (byte) ( seconds & 0x00000000000000FFL );

            nanoseconds = from.nanoseconds ();
        }
        else
        {
            throw new TypeCastException ( "Failed to cast from [%from_class%] [%from_object%] to [%to_class%]: Invalid format",
                                          "from_class", this.fromClass (),
                                          "from_object", from,
                                          "to_class", this.toClass () );
        }

        bytes [ 7 ] = (byte) ( ( nanoseconds & 0xFF00000000000000L ) >> 56 );
        bytes [ 6 ] = (byte) ( ( nanoseconds & 0x00FF000000000000L ) >> 48 );
        bytes [ 5 ] = (byte) ( ( nanoseconds & 0x0000FF0000000000L ) >> 40 );
        bytes [ 4 ] = (byte) ( ( nanoseconds & 0x000000FF00000000L ) >> 32 );
        bytes [ 3 ] = (byte) ( ( nanoseconds & 0x00000000FF000000L ) >> 24 );
        bytes [ 2 ] = (byte) ( ( nanoseconds & 0x0000000000FF0000L ) >> 16 );
        bytes [ 1 ] = (byte) ( ( nanoseconds & 0x000000000000FF00L ) >> 8 );
        bytes [ 0 ] = (byte) ( nanoseconds & 0x00000000000000FFL );

        return bytes;
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
    public Class<byte[]> toClass ()
    {
        return byte[].class;
    }
}
