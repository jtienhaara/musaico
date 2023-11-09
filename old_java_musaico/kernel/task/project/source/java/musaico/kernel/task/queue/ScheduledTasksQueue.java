package musaico.kernel.task.queue;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Order;
import musaico.io.Reference;
import musaico.io.Sequence;

import musaico.kernel.task.Priority;

import musaico.region.Size;


/**
 * <p>
 * A queue of tasks, each one with a Priority and a Schedule,
 * sorted (default by "next scheduled times").
 * </p>
 *
 *
 * <p>
 * In Java every ScheduledTasksQueue must be Serializable, in order to
 * play nicely over RMI.  (Note that Tasks may be Serializable
 * or UnicastRemoteObjects.)
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
public class ScheduledTasksQueue
    implements Serializable
{
    /** Lock all critical sections on this token: */
    private final Serializable lock = new String ();

    /** Unique identifier for this queue. */
    private final Reference id;

    /** Overall priority for this queue of tasks.
     *  Can be used to set thread priority and that sort of thing. */
    private final Priority priority;

    /** List of ScheduledTasks, sorted by next scheduled time. */
    private final List<ScheduledTask> sortedScheduledTasks =
        new ArrayList<ScheduledTask> ();

    /** Sorts ScheduledTasks by next scheduled time. */
    private final Order<ScheduledTask> order;


    /**
     * <p>
     * Creates a new ScheduledTasksQueue, sorting by next
     * scheduled time.
     * </p>
     *
     * @param id The unique identifier for this scheduled tasks
     *           queue.  Must not be null.
     *
     * @param priority The priority of this whole queue.  Can be
     *                 used to set threading priorities and so on.
     *                 Must not be null.
     *
     * @throws I18nIllegalArgumentException If the id argument is
     *                                      invalid.
     */
    public ScheduledTasksQueue (
                                Reference id,
                                Priority priority
                                )
    {
        this ( id, priority, new ScheduledTaskOrder () );
    }


    /**
     * <p>
     * Creates a new ScheduledTasksQueue sorted by the
     * specified order.
     * </p>
     *
     * @param id The unique identifier for this scheduled tasks
     *           queue.  Must not be null.
     *
     * @param priority The priority of this whole queue.  Can be
     *                 used to set threading priorities and so on.
     *                 Must not be null.
     *
     * @param order The order to use for sorting
     *              the queue of ScheduledTasks, such as
     *              a ScheduledTaskOrder.
     *              Must not be null.
     *
     * @throws I18nIllegalArgumentException If any parameter
     *                                      is invalid.
     */
    public ScheduledTasksQueue (
                                Reference id,
                                Priority priority,
                                Order<ScheduledTask> order
                                )
    {
        if ( id == null
             || priority == null
             || order == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a ScheduledTasksQueue with id [%id%] priority [%priority%] order [%order%]",
                                                     "id", id,
                                                     "priority", priority,
                                                     "order", order );
        }

        this.id = id;
        this.priority = priority;
        this.order = order;
    }


    /**
     * <p>
     * Adds the specified task to the sorted queue.
     * </p>
     *
     * @param scheduled_task The ScheduledTask to add to the
     *                       queue.  Must not be null.
     *
     * @return This ScheduledTasksQueue.  Never null.
     *
     * @throws I18nIllegalArgumentException If the scheduled_task is
     *                                      invalid.
     */
    public ScheduledTasksQueue add (
                                    ScheduledTask scheduled_task
                                    )
    {
        if ( scheduled_task == null )
        {
            throw new I18nIllegalArgumentException ( "Schedule tasks queue [%scheduled_task_queue%] cannot add scheduled task [%scheduled_task%]",
                                                     "scheduled_tasks_queue", this,
                                                     "scheduled_task", scheduled_task );
        }

        synchronized ( this.lock )
        {
            int index = Collections.binarySearch ( this.sortedScheduledTasks,
                                                   scheduled_task,
                                                   this.order );

            if ( index < 0 )
            {
                index = ( 0 - index );
            }

            // Insert in front of the other ScheduledTasks with
            // the same sort order(s).
            if ( index <= this.sortedScheduledTasks.size () )
            {
                // Insert the task.
                this.sortedScheduledTasks.add ( index, scheduled_task );
            }
            else
            {
                // Append the task.
                this.sortedScheduledTasks.add ( scheduled_task );
            }
            System.out.println ( "!!! TASKS = " + this.sortedScheduledTasks );
        }

        return this;
    }


    /**
     * <p>
     * Returns this ScheduledTasksQueue's unique identifier.
     * </p>
     *
     * @return This queue's unique id.  Never null.
     */
    public Reference id ()
    {
        return this.id;
    }


    /**
     * <p>
     * Returns the overall priority of this queue (such as
     * Priorities.MEDIUM and so on).
     * </p>
     *
     * @return The overall priority of this queue of tasks.
     *         Never null.
     */
    public Priority priority ()
    {
        return this.priority;
    }


    /**
     * <p>
     * Removes the specified task from the sorted queue.
     * </p>
     *
     * @param scheduled_task The ScheduledTask to remove from the
     *                       queue.  Must not be null.
     *
     * @return The specified ScheduledTask, if it was removed
     *         from the queue, or null if this queue did not contain
     *         the specified ScheduledTask.
     *
     * @throws I18nIllegalArgumentException If the scheduled_task is
     *                                      invalid.
     */
    public ScheduledTask remove (
                                 ScheduledTask scheduled_task
                                 )
    {
        if ( scheduled_task == null )
        {
            throw new I18nIllegalArgumentException ( "Scheduled tasks queue [%scheduled_task_queue%] cannot remove scheduled task [%scheduled_task%]",
                                                     "scheduled_tasks_queue", this,
                                                     "scheduled_task", scheduled_task );
        }

        final boolean was_removed;
        synchronized ( this.lock )
        {
            was_removed =
                this.sortedScheduledTasks.remove ( scheduled_task );
        }

        if ( was_removed )
        {
            return scheduled_task;
        }
        else
        {
            return null;
        }
    }


    /**
     * <p>
     * Returns a snapshot of the current scheduled tasks in this
     * queue.
     * </p>
     *
     * @return This queue's scheduled tasks, in sorted order.
     *         Never null.
     */
    public Sequence<ScheduledTask> scheduledTasks ()
    {
        final ScheduledTask [] current_scheduled_tasks;
        synchronized ( this.lock )
        {
            ScheduledTask [] template =
                new ScheduledTask [ this.sortedScheduledTasks.size () ];
            current_scheduled_tasks =
                this.sortedScheduledTasks.toArray ( template );
        }

        Sequence<ScheduledTask> scheduled_tasks =
            new Sequence<ScheduledTask> ( ScheduledTask.NONE,
                                          current_scheduled_tasks );

        return scheduled_tasks;
    }


    /**
     * <p>
     * Re-sorts the queue.  Call this after <i>changing</i>
     * the next scheduled time of any of the scheduled tasks.
     * </p>
     */
    public void sort ()
    {
        synchronized ( this.lock )
        {
            Collections.sort ( this.sortedScheduledTasks,
                               this.order );
        }
    }
}
