package musaico.kernel.task.strategies;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;

import musaico.kernel.task.Priority;
import musaico.kernel.task.Priorities;
import musaico.kernel.task.ScheduleException;

import musaico.kernel.task.queue.ScheduledTask;
import musaico.kernel.task.queue.ScheduledTasksQueue;
import musaico.kernel.task.queue.SchedulerStrategy;
import musaico.kernel.task.queue.TimedExecutor;


/**
 * <p>
 * Groups all Priorities.BATCH and lower-priority tasks onto
 * one queue (thread) per priority.  Every other task gets
 * its own queue (thread).
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
public class SimpleSchedulerStrategy
    implements SchedulerStrategy, Serializable
{
    /**
     * @see musaico.kernel.task.SchedulerStrategy#priorities()
     */
    public Priorities priorities ()
    {
        return Priorities.STANDARD_PRIORITIES;
    }


    /**
     * @see musaico.kernel.task.SchedulerStrategy#schedule(musaico.kernel.task.TimedExecutor,musaico.kernel.task.ScheduledTask)
     */
    public Reference schedule (
                               TimedExecutor executor,
                               ScheduledTask scheduled_task
                               )
        throws I18nIllegalArgumentException, ScheduleException
    {
        if ( executor == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot use executor [%timed_executor%] to schedule task [%scheduled_task%] (priority [%priority%])",
                                                     "timed_executor", executor,
                                                     "scheduled_task", scheduled_task,
                                                     "priority", "?" );
        }
        else if ( scheduled_task == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot use executor [%timed_executor%] to schedule task [%scheduled_task%] (priority [%priority%])",
                                                     "timed_executor", executor,
                                                     "scheduled_task", scheduled_task,
                                                     "priority", "?" );
        }

        Priority priority = scheduled_task.priority ();
        if ( priority == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot use executor [%timed_executor%] to schedule task [%scheduled_task%] (priority [%priority%])",
                                                     "timed_executor", executor,
                                                     "scheduled_task", scheduled_task,
                                                     "priority", "?" );
        }

        // Decide which queue to put the task in.
        final Reference queue_id;
        if ( priority.equals ( Priority.BATCH ) )
        {
            queue_id = Priority.BATCH;
        }
        else if ( priority.equals ( Priority.MEDIUM )
                  || priority.equals ( Priority.REALTIME ) )
        {
            queue_id = scheduled_task.task ().id ();
        }
        else
        {
            throw new ScheduleException ( "Cannot use executor [%timed_executor%] to schedule task [%scheduled_task%] (priority [%priority%])",
                                          "timed_executor", executor,
                                          "scheduled_task", scheduled_task,
                                          "priority", "?" );
        }

        // Now tell the executor to schedule/reschedule the
        // appropriate new or existing queue.
        ScheduledTasksQueue queue = executor.queue ( queue_id );
        if ( queue == null )
        {
            // Create a new queue, add the task to it, and
            // start executing it.
            queue = new ScheduledTasksQueue ( queue_id,
                                              scheduled_task.priority () );
            queue.add ( scheduled_task );
            executor.schedule ( queue );
        }
        else
        {
            // Add the task to the existing queue, and reschedule
            // so that the new task gets executed on time.
            queue.add ( scheduled_task );
            executor.reschedule ( queue.id () );
        }

        return queue_id;
    }
}
