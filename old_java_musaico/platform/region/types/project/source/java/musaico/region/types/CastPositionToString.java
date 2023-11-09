package musaico.region.types;


import java.io.Serializable;

import musaico.region.Position;

import musaico.region.array.ArrayPosition;

import musaico.region.time.TimePosition;

import musaico.time.Time;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;

import musaico.types.casters.ChainCaster;

import musaico.types.primitive.types.CastLongToString;
import musaico.types.primitive.types.CastTimeToString;


/**
 * <p>
 * Casts Positions to Strings.
 * </p>
 *
 * <p>
 * An ArrayPosition is cast to its index then to a String.
 * A TimePosition is cast to its Time then to a String.
 * And so on.
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
public class CastPositionToString
    implements TypeCaster<Position,String>, Serializable
{
    /** Text prefix for ArrayPosition-to/from-string casts. */
    public static final String ARRAY_POSITION_PREFIX = "ArrayPosition:";

    /** Text prefix for ArrayPosition-to/from-string casts. */
    public static final String TIME_POSITION_PREFIX = "TimePosition:";


    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object,java.lang.Class)
     */
    public String cast (
                        Position from,
                        Class to_class
                        )
        throws TypeException
    {
        final String as_string;
        if ( from instanceof ArrayPosition )
        {
            ChainCaster<Position,Long,String> chain =
                new ChainCaster<Position,Long,String> ( new CastPositionToLong (),
                                                        Long.class,
                                                        new CastLongToString () );
            as_string =
                CastPositionToString.ARRAY_POSITION_PREFIX
                + chain.cast ( from, to_class );
        }
        else if ( from instanceof TimePosition )
        {
            ChainCaster<Position,Time,String> chain =
                new ChainCaster<Position,Time,String> ( new CastPositionToTime (),
                                                        Time.class,
                                                        new CastTimeToString () );
            as_string = "TimePosition:" + chain.cast ( from, to_class );
        }
        else
        {
            throw new TypeCastException ( "Unknown Position class [%position_class%]",
                                          "position_class", from.getClass () );
        }

        return as_string;
    }
}
