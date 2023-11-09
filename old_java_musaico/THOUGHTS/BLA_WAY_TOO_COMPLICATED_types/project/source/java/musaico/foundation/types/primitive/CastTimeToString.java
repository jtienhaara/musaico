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
 * Casts values from Time to String.
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
 * Copyright (c) 2009 Johann Tienhaara
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
public class CastTimeToString
    implements TypeCaster<Time,String>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public String cast (
                        Time from
                        )
        throws TypeException
    {
        final String units;
        if ( from instanceof AbsoluteTime )
        {
            units = "seconds UTC";
        }
        else
        {
            units = "seconds";
        }

        return "" + from.seconds () + "." + from.nanoseconds ()
            + " " + units;
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
    public Class<String> toClass ()
    {
        return String.class;
    }
}
