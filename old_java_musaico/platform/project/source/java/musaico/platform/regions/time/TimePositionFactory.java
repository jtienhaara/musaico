package musaico.region.time;

import java.io.Serializable;


import musaico.time.Time;


/**
 * <p>
 * Creates the appropriate TimePositions (absolute or relative) for
 * a TimeSpace.
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
public interface TimePositionFactory
    extends Serializable
{
    /**
     * <p>
     * Creates a TimePosition for the given Time.
     * </p>
     *
     * @param space The space in which to create the new time
     *              position, such as an absolute TimeSpace or
     *              a TimeSpace with relative times.  Must not be null.
     *
     * @param time The time to create a position for.  Must not be null.
     *
     * @return The new TimePosition for the specified time.  Never null.
     *         Can be the TimePosition.OUT_OF_BOUNDS position.
     */
    public TimePosition position (
                                  TimeSpace space,
                                  Time time
                                  );
   }
