package musaico.region.types;


import java.io.Serializable;

import musaico.region.Size;

import musaico.region.array.ArraySize;

import musaico.region.time.TimeSize;
import musaico.region.time.TimeSpace;

import musaico.time.Time;
import musaico.time.RelativeTime;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;

import musaico.types.primitive.types.CastStringToLong;
import musaico.types.primitive.types.CastStringToTime;


/**
 * <p>
 * Casts Strings to Sizes.
 * </p>
 *
 * <p>
 * A String with the prefix
 * <code> CastSizeToString.ARRAY_SIZE_PREFIX </code>
 * is cast to an ArraySize, whereas a String with the prefix
 * <code> CastPosittionToString.TIME_SIZE_PREFIX </code>
 * is cast to a TimeSize, and so on.
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
 * Copyright (c) 2009, 2011 Johann Tienhaara
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
public class CastStringToSize
    implements TypeCaster<String,Size>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object,java.lang.Class)
     */
    public Size cast (
                          String from,
                          Class to_class
                          )
        throws TypeException
    {
        final Size size;
        if ( from.startsWith ( CastSizeToString.ARRAY_SIZE_PREFIX ) )
        {
            int start_length =
                CastSizeToString.ARRAY_SIZE_PREFIX.length ();
            String length_as_string = from.substring ( start_length );
            long length =
                new CastStringToLong ().cast ( length_as_string, Long.class )
                .longValue ();
            size = new ArraySize ( length );
        }
        else if ( from.startsWith ( CastSizeToString.TIME_SIZE_PREFIX ) )
        {
            int start_duration =
                CastSizeToString.TIME_SIZE_PREFIX.length ();
            String duration_as_string = from.substring ( start_duration );
            Time duration =
                new CastStringToTime ().cast ( duration_as_string, Time.class );
            if ( ! ( duration instanceof RelativeTime ) )
            {
                throw new TypeCastException ( "Cannot cast time string [%time_string%] to a relative time duration TimeSize",
                                              "time_string", from );
            }

            // !!! arbitrarily chose absolute time space...
            size = new TimeSize ( TimeSpace.ABSOLUTE, (RelativeTime) duration );
        }
        else
        {
            throw new TypeCastException ( "Unknown Size string [%size_string%]",
                                          "size_string", from );
        }

        return size;
    }
}
