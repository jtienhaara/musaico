package musaico.region.types;


import java.io.Serializable;

import musaico.region.Size;

import musaico.region.array.ArraySize;

import musaico.region.time.TimeSize;

import musaico.time.Time;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;

import musaico.types.casters.ChainCaster;

import musaico.types.primitive.types.CastLongToString;
import musaico.types.primitive.types.CastTimeToString;


/**
 * <p>
 * Casts Sizes to Strings.
 * </p>
 *
 * <p>
 * An ArraySize is cast to its length then to a String.
 * A TimeSize is cast to its Time then to a String.
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
public class CastSizeToString
    implements TypeCaster<Size,String>, Serializable
{
    /** Text prefix for ArraySize-to/from-string casts. */
    public static final String ARRAY_SIZE_PREFIX = "ArraySize:";

    /** Text prefix for ArraySize-to/from-string casts. */
    public static final String TIME_SIZE_PREFIX = "TimeSize:";


    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object,java.lang.Class)
     */
    public String cast (
                        Size from,
                        Class to_class
                        )
        throws TypeException
    {
        final String as_string;
        if ( from instanceof ArraySize )
        {
            ChainCaster<Size,Long,String> chain =
                new ChainCaster<Size,Long,String> ( new CastSizeToLong (),
                                                        Long.class,
                                                        new CastLongToString () );
            as_string =
                CastSizeToString.ARRAY_SIZE_PREFIX
                + chain.cast ( from, to_class );
        }
        else if ( from instanceof TimeSize )
        {
            ChainCaster<Size,Time,String> chain =
                new ChainCaster<Size,Time,String> ( new CastSizeToTime (),
                                                    Time.class,
                                                    new CastTimeToString () );
            as_string = "TimeSize:" + chain.cast ( from, to_class );
        }
        else
        {
            throw new TypeCastException ( "Unknown Size class [%size_class%]",
                                          "size_class", from.getClass () );
        }

        return as_string;
    }
}
