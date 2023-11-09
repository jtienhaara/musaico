package musaico.types.primitive;


import java.io.Serializable;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from Long to byte array.
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
public class CastLongToBytes
    implements TypeCaster<Long,byte[]>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public byte [] cast (
                         Long from
                         )
        throws TypeException
    {
        byte [] bytes = new byte [ 8 ];
        bytes [ 7 ] = (byte) ( ( from & 0xFF00000000000000L ) >> 56 );
        bytes [ 6 ] = (byte) ( ( from & 0x00FF000000000000L ) >> 48 );
        bytes [ 5 ] = (byte) ( ( from & 0x0000FF0000000000L ) >> 40 );
        bytes [ 4 ] = (byte) ( ( from & 0x000000FF00000000L ) >> 32 );
        bytes [ 3 ] = (byte) ( ( from & 0x00000000FF000000L ) >> 24 );
        bytes [ 2 ] = (byte) ( ( from & 0x0000000000FF0000L ) >> 16 );
        bytes [ 1 ] = (byte) ( ( from & 0x000000000000FF00L ) >> 8 );
        bytes [ 0 ] = (byte) ( from & 0x00000000000000FFL );

        return bytes;
    }


    /**
     * @see musaico.types.TypeCaster#fromClass()
     */
    @Override
    public Class<Long> fromClass ()
    {
        return Long.class;
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
