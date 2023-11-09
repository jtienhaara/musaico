package musaico.region.types;


import java.io.Serializable;

import musaico.region.Position;

import musaico.region.array.ArrayPosition;

import musaico.region.time.TimePosition;
import musaico.region.time.TimeSpace;

import musaico.time.AbsoluteTime;
import musaico.time.Time;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;

import musaico.types.primitive.types.CastStringToLong;
import musaico.types.primitive.types.CastStringToTime;


/**
 * <p>
 * Casts Strings to Positions.
 * </p>
 *
 * <p>
 * A String with the prefix
 * <code> CastPositionToString.ARRAY_POSITION_PREFIX </code>
 * is cast to an ArrayPosition, whereas a String with the prefix
 * <code> CastPosittionToString.TIME_POSITION_PREFIX </code>
 * is cast to a TimePosition, and so on.
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
public class CastStringToPosition
    implements TypeCaster<String,Position>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object,java.lang.Class)
     */
    public Position cast (
                          String from,
                          Class to_class
                          )
        throws TypeException
    {
        final Position position;
        if ( from.startsWith ( CastPositionToString.ARRAY_POSITION_PREFIX ) )
        {
            int start_index =
                CastPositionToString.ARRAY_POSITION_PREFIX.length ();
            String index_as_string = from.substring ( start_index );
            long index =
                new CastStringToLong ().cast ( index_as_string, Long.class )
                .longValue ();
            position = new ArrayPosition ( index );
        }
        else if ( from.startsWith ( CastPositionToString.TIME_POSITION_PREFIX ) )
        {
            int start_time =
                CastPositionToString.TIME_POSITION_PREFIX.length ();
            String time_as_string = from.substring ( start_time );
            Time time =
                new CastStringToTime ().cast ( time_as_string, Time.class );
            if ( time instanceof AbsoluteTime )
            {
                position =
                    TimeSpace.ABSOLUTE.positionFactory ()
                    .position ( TimeSpace.ABSOLUTE, time );
            }
            else
            {
                position =
                    TimeSpace.RELATIVE.positionFactory ()
                    .position ( TimeSpace.RELATIVE, time );
            }
        }
        else
        {
            throw new TypeCastException ( "Unknown Position string [%position_string%]",
                                          "position_string", from );
        }

        return position;
    }
}
