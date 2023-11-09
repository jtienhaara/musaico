package musaico.kernel.task;


import java.io.Serializable;


import musaico.io.Order;
import musaico.io.Sequence;

import musaico.time.Time;


/**
 * <p>
 * A schedule of times.  May be periodic or a-periodic, and so on.
 * </p>
 *
 * <p>
 * A schedule is a Sequence of ordered Times.  To get the first
 * Time out of a schedule, call <code> schedule.first () </code>.
 * To get the final time out of a schedule, call
 * <code> schedule.last () </code>. Finally, to get the next
 * Time after Time "t", call <code> schedule.after ( t ) </code>.
 * </p>
 *
 *
 * <p>
 * In Java, every Schedule must be Serializable in order to play
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
 * Copyright (c) 2011, 2012 Johann Tienhaara
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
public class Schedule
    extends Sequence<Time>
    implements Serializable
{
    /** No scheduled times. */
    public static final Schedule NONE =
        new Schedule ();


    /**
     * <p>
     * Creates a new Schedule with the specified scheduled times.
     * </p>
     *
     * @param times The times in the schedule.  Must not be null.
     *              Must not contain any null elements.  Must not
     *              contain any ImpossibleTimes.
     */
    public Schedule (
                     Time ... times
                     )
    {
        super ( Time.NEVER, Order.TIME.sort ( times ) );
    }
}
