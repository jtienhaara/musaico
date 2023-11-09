package musaico.region.types;


import java.io.Serializable;

import musaico.region.Size;

import musaico.region.time.TimeSize;

import musaico.time.Time;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts Sizes to offset long values.
 * </p>
 *
 * <p>
 * A TimeSize is cast to its duration.
 * </p>
 *
 * <p>
 * Other Sizes cannot be cast to meaningful Time values,
 * so an exception is thrown.
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
public class CastSizeToTime
    implements TypeCaster<Size,Time>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object,java.lang.Class)
     */
    public Time cast (
                      Size from,
                      Class to_class
                      )
        throws TypeException
    {
        if ( ! ( from instanceof TimeSize ) )
        {
            throw new TypeCastException ( "Cannot cast from [%size_class%] to Time: only TimeSizes can be cast to Time",
                                          "size_class", from.getClass () );
        }

        TimeSize time_size = (TimeSize) from;
        Time time = time_size.duration ();

        return time;
    }
}
