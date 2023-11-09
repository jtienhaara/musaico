package musaico.region.time;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.io.Order;

import musaico.region.Position;

import musaico.time.AbsoluteTime;
import musaico.time.Time;


/**
 * <p>
 * An absolute position in time, such as
 * 12:42pm Thursday December 29, 2011.
 * </p>
 *
 *
 * <p>
 * In Java every Position must be Serializable in order to play
 * nicely over RMI.
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
public class AbsoluteTimePosition
    extends TimePosition
    implements Serializable
{
    /**
     * <p>
     * Creates a new AbsoluteTimePosition with the specified absolute
     * time.
     * </p>
     *
     * @param space The space to which this TimePosition belongs.
     *              Must not be null.
     *              Must have an AbsoluteTimePositionFactory.
     *
     * @param absolute_time The time of this position, such as
     *                      12:42pm Thursday December 29, 2011.
     *                      Must not be null.
     *
     * @throws I18nIllegalArgumentException If the parameter(s)
     *                                      specified are invalid
     *                                      (see above for details).
     */
    public AbsoluteTimePosition (
                                 TimeSpace space,
                                 AbsoluteTime absolute_time
                                 )
        throws I18nIllegalArgumentException
    {
        super ( space, absolute_time );

        TimePositionFactory position_factory = space.positionFactory ();
        if ( ! ( position_factory instanceof AbsoluteTimePositionFactory ) )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a TimePosition in space [%space%] at time [%time%]",
                                                     "space", space,
                                                     "time", absolute_time );
        }
    }
}
