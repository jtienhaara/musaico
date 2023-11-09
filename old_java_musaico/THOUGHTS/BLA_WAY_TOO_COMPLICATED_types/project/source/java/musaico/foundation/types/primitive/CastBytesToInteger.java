package musaico.types.primitive;


import java.io.Serializable;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from byte arrays to Integer.
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
public class CastBytesToInteger
    implements TypeCaster<byte[],Integer>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public Integer cast (
                         byte [] from
                         )
        throws TypeException
    {
        if ( from == null
             || from.length == 0
             || from.length > 4 )
        {
            throw new TypeCastException ( "Failed to cast from [%from_class%] [%from_object%] to [%to_class%]: Invalid format",
                                          "from_class", this.fromClass (),
                                          "from_object", from,
                                          "to_class", this.toClass () );
        }

        final int int_value;
        if ( from.length == 4 )
        {
            int_value =
                from [ 3 ] << 24
                + from [ 2 ] << 16
                + from [ 1 ] << 8
                + from [ 0 ];
        }
        else if ( from.length == 3 )
        {
            int_value =
                from [ 2 ] << 16
                + from [ 1 ] << 8
                + from [ 0 ];
        }
        else if ( from.length == 2 )
        {
            int_value =
                from [ 1 ] << 8
                + from [ 0 ];
        }
        else // from.length == 1
        {
            int_value =
                from [ 0 ];
        }

        return new Integer ( int_value );
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
    public Class<Integer> toClass ()
    {
        return Integer.class;
    }
}
