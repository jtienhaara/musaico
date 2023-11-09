package musaico.region.types;


import java.io.Serializable;

import musaico.region.Position;

import musaico.region.time.TimePosition;
import musaico.region.time.TimeSpace;

import musaico.time.AbsoluteTime;
import musaico.time.RelativeTime;
import musaico.time.Time;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from Time to TimePosition (0, 1, 2, ..., or
 * the standard out of bounds position if negative).
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
public class CastTimeToPosition
    implements TypeCaster<Time,Position>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object,java.lang.Class)
     */
    public Position cast (
                          Time from,
                          Class to_class
                          )
        throws TypeException
    {
        final TimePosition position;
        if ( from instanceof AbsoluteTime )
        {
            position = TimeSpace.ABSOLUTE.positionFactory ()
                .position ( TimeSpace.ABSOLUTE, from );
        }
        else if ( from instanceof RelativeTime )
        {
            position = TimeSpace.RELATIVE.positionFactory ()
                .position ( TimeSpace.RELATIVE, from );
        }
        else
        {
            position = TimeSpace.RELATIVE.outOfBounds ();
        }

        return position;
    }
}
