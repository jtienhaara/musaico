package musaico.kernel.task.queue;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;

import musaico.kernel.task.Priorities;
import musaico.kernel.task.ScheduleException;


/**
 * <p>
 * Decides the acceptable Priority levels for a scheduler,
 * how and when to group scheduled tasks into queues, and
 * so on.
 * </p>
 *
 *
 * <p>
 * In Java, every SchedulerStrategy must be Serializable, in order
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
public interface SchedulerStrategy
    extends Serializable
{
    /**
     * <p>
     * Returns the Priority levels recognized by this strategy.
     * </p>
     *
     * <p>
     * For example, one scheduler strategy might recognize
     * the standard priorities (Priorities.BATCH, Priorities.MEDIUM
     * and Priorities.REALTIME), while another strategy might
     * only recognize one priority level, and another might
     * have a wider range of priorities.
     * </p>
     *
     * @return The Priorities recognized by this SchedulerStrategy.
     *         Never null.
     */
    public abstract Priorities priorities ();


    /**
     * <p>
     * Adds the specified ScheduledTask to a queue in the specified
     * TimedExecutor, and schedules execution accordingly.
     * </p>
     *
     * <p>
     * For example, a strategy might instruct the executor to
     * create a new threader for every single task scheduled:
     * </p>
     *
     * <pre>
     *     ScheduledTasksQueue queue_for_one_task =
     *         new ScheduledTasksQueue ( task.id () );
     *     queue_for_one_task.add ( task );
     *     executor.schedule ( queue_for_one_task );
     * </pre>
     *
     * <p>
     * Or it might schedule more selectively:
     * </p>
     *
     * <pre>
     *     if ( task.priority ().compareTo ( Priorities.BATCH ) &lt;= 0 )
     *     {
     *         // Schedule all batch tasks on one queue / threader.
     *         ScheduledTasksQueue batch_queue =
     *             executor.queue ( Priorities.BATCH );
     *         if ( batch_queue == null )
     *         {
     *             batch_queue = new ScheduledTasksQueue ( Priorities.BATCH );
     *             batch_queue.add ( task );
     *             executor.schedule ( batch_queue );
     *         }
     *         else
     *         {
     *             batch_queue.add ( task );
     *             executor.schedule ( batch_queue );
     *         }
     *     }
     *     else
     *     {
     *         // Schedule each higher priority task on its own queue.
     *         ScheduledTasksQueue queue_for_one_task =
     *             new ScheduledTasksQueue ( task.id () );
     *         queue_for_one_task.add ( task );
     *         executor.schedule ( queue_for_one_task );
     *     }
     * </pre>
     *
     * <p>
     * And so on.
     * </p>
     *
     * @param executor The TimedExecutor which handles concurrency
     *                 and will actually execute the task.  Must not
     *                 be null.
     *
     * @param scheduled_task The task which is scheduled to be executed
     *                       at specific time(s).  Must not be null.
     *
     * @return The unique identifier of the queue on which the task
     *         was placed in the TimedExecutor.  Never null.
     *
     * @throws I18nIllegalArgumentException If the parameters are invalid.
     *
     * @throws ScheduleException If the specified executor has a problem
     *                           or if the scheduled task's priority level
     *                           is invalid for this strategy, and so on.
     */
    public abstract Reference schedule (
                                        TimedExecutor executor,
                                        ScheduledTask scheduled_task
                                        )
        throws I18nIllegalArgumentException, ScheduleException;
}
