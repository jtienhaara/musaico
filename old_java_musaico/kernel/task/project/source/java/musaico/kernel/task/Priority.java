package musaico.kernel.task;


import java.io.Serializable;


import musaico.io.NaturallyOrdered;
import musaico.io.Order;

import musaico.io.references.SimpleSoftReference;


/**
 * <p>
 * The priority level of a Task, as specified for scheduling purposes.
 * </p>
 *
 * <p>
 * Most Tasks are level <code> Priorities.NORMAL </code>.
 * However a Task might be better scheduled with level
 * <code> Priorities.REAL_TIME </code> or level
 * <code> Priorities.LOW </code> and so on, depending on the
 * nature of the Task and scheduling.
 * </p>
 *
 *
 * <p>
 * In Java every Priority must be Serializable, in order to
 * play nicely over RMI.
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
public class Priority
    extends SimpleSoftReference<Long>
    implements NaturallyOrdered<Priority>, Serializable
{
    /** Low priority, can be used for batch operations, long-running
     *  tasks, and so on. */
    public static final Priority BATCH = new Priority ( 200L );

    /** Normal priority for most tasks and user processes in
     *  most systems. */
    public static final Priority MEDIUM = new Priority ( 100L );

    /** High priority for realtime and critical tasks. */
    public static final Priority REALTIME = new Priority ( 50L );


    /** No priority at all.  Invalid! */
    public static final Priority NO_SUCH_PRIORITY = new Priority ( -1L );


    /**
     * <p>
     * Creates a new Priority with the specified level
     * (lower number = higher priority).
     * </p>
     *
     * @param level The level of this priority.  Must be greater
     *              than or equal to 0.  The lower the number,
     *              the higher the priority.
     */
    public Priority (
                     long level
                     )
    {
        super ( level );
    }

    // equals, hashCode, toString implemented by SimpleSoftReference.


    /**
     * @see musaico.io.NaturallyOrdered#order()
     */
    public Order<Priority> order ()
    {
        return PriorityOrder.STANDARD;
    }


    /**
     * @see musaico.io.NaturallyOrdered#orderIndex()
     */
    public Priority orderIndex ()
    {
        return this;
    }
}
