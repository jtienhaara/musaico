package musaico.region.time;

import java.io.Serializable;


import musaico.time.RelativeTime;
import musaico.time.Time;


/**
 * <p>
 * Creates RelativeTimePositions.
 * </p>
 *
 *
 * <p>
 * In Java every TimePositionFactory must be Serializable in order
 * to play nicely over RMI.
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
public class RelativeTimePositionFactory
    implements TimePositionFactory, Serializable
{
    /**
     * @see musaico.region.time.TimePositionFactory#position(musaico.time.TimeSpace,musaico.time.Time)
     */
    public RelativeTimePosition position (
                                          TimeSpace space,
                                          Time time
                                          )
    {
        if ( time == null
             || ! ( time instanceof RelativeTime ) )
        {
            return new RelativeTimePosition ( space,
                                              new RelativeTime ( Long.MIN_VALUE,
                                                                 0L ) );
        }

        RelativeTime relative_time = (RelativeTime) time;

        return new RelativeTimePosition ( space, relative_time );
    }
}
