package musaico.types.primitive;


import java.io.Serializable;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from byte arrays to Long.
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
public class CastBytesToLong
    implements TypeCaster<byte[],Long>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public Long cast (
                      byte [] from
                      )
        throws TypeException
    {
        if ( from == null
             || from.length == 0
             || from.length > 8 )
        {
            throw new TypeCastException ( "Failed to cast from [%from_class%] [%from_object%] to [%to_class%]: Invalid format",
                                          "from_class", this.fromClass (),
                                          "from_object", from,
                                          "to_class", this.toClass () );
        }

        final long long_value;
        if ( from.length == 8 )
        {
            long_value =
                from [ 7 ] << 56
                + from [ 6 ] << 48
                + from [ 5 ] << 40
                + from [ 4 ] << 32
                + from [ 3 ] << 24
                + from [ 2 ] << 16
                + from [ 1 ] << 8
                + from [ 0 ];
        }
        else if ( from.length == 7 )
        {
            long_value =
                from [ 6 ] << 48
                + from [ 5 ] << 40
                + from [ 4 ] << 32
                + from [ 3 ] << 24
                + from [ 2 ] << 16
                + from [ 1 ] << 8
                + from [ 0 ];
        }
        else if ( from.length == 6 )
        {
            long_value =
                from [ 5 ] << 40
                + from [ 4 ] << 32
                + from [ 3 ] << 24
                + from [ 2 ] << 16
                + from [ 1 ] << 8
                + from [ 0 ];
        }
        else if ( from.length == 5 )
        {
            long_value =
                from [ 4 ] << 32
                + from [ 3 ] << 24
                + from [ 2 ] << 16
                + from [ 1 ] << 8
                + from [ 0 ];
        }
        else if ( from.length == 4 )
        {
            long_value =
                from [ 3 ] << 24
                + from [ 2 ] << 16
                + from [ 1 ] << 8
                + from [ 0 ];
        }
        else if ( from.length == 3 )
        {
            long_value =
                from [ 2 ] << 16
                + from [ 1 ] << 8
                + from [ 0 ];
        }
        else if ( from.length == 2 )
        {
            long_value =
                from [ 1 ] << 8
                + from [ 0 ];
        }
        else // from.length == 1
        {
            long_value =
                from [ 0 ];
        }

        return new Long ( long_value );
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
    public Class<Long> toClass ()
    {
        return Long.class;
    }
}
