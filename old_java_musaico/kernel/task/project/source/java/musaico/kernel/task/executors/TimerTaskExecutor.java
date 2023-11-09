package musaico.kernel.task.executors;


import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;

import musaico.kernel.task.Priorities;
import musaico.kernel.task.Priority;
import musaico.kernel.task.ScheduleException;

import musaico.kernel.task.queue.ScheduledTasksQueue;
import musaico.kernel.task.queue.TimedExecutor;


/**
 * <p>
 * A task executor which relies on one Timer for each
 * queue of tasks, with one TimerTask for each task in a queue.
 * </p>
 *
 *
 * <p>
 * In Java, every TimedExecutor must be Serializable in order to
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
public class TimerTaskExecutor
    implements TimedExecutor, Serializable
{
    /** Synchronize all critical sections on this lock. */
    private final Serializable lock;

    /** Lookup of TimerQueue by identifier. */
    private final Map<Reference,TimerQueue> queues =
        new HashMap<Reference,TimerQueue> ();


    /**
     * <p>
     * Creates a new TimerTaskExecutor with the specified lock
     * for synchronizing on critical sections.
     * </p>
     *
     * @param lock The lock to ensure this executor modifies its own data
     *             as part of the caller's atomic operations.
     *             Must not be null.  Typically this lock is used by
     *             a Scheduler, its SchedulerStrategy and its TimedExecutor.
     */
    public TimerTaskExecutor (
                              Serializable lock
                              )
    {
        if ( lock == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a TimerTaskExecutor with lock [%lock%]",
                                                     "lock", lock );
        }

        this.lock = lock;
    }


    /**
     * @see musaico.kernel.task.TimedExecutor#destroyQueue(musaico.io.Reference)
     */
    @Override
    public ScheduledTasksQueue destroyQueue (
                                             Reference id
                                             )
        throws ScheduleException
    {
        final TimerQueue timer_queue;
        synchronized ( this.lock )
        {
            timer_queue = this.queues.remove ( id );
        }

        if ( timer_queue == null )
        {
            // No such queue.
            return null;
        }

        // Stop the timer and all scheduled tasks.
        timer_queue.stop ();

        ScheduledTasksQueue queue = timer_queue.queue ();
        return queue;
    }


    /**
     * @see musaico.kernel.task.TimedExecutor#priority(musaico.io.Reference)
     */
    @Override
    public Priority priority (
                              Reference queue_id
                              )
    {
        final ScheduledTasksQueue queue;
        synchronized ( this.lock )
        {
            queue = this.queue ( queue_id );
            if ( queue == null )
            {
                // No such queue.
                return Priority.NO_SUCH_PRIORITY;
            }
        }

        return queue.priority ();
    }


    /**
     * @see musaico.kernel.task.TimedExecutor#queue(musaico.io.Reference)
     */
    @Override
    public ScheduledTasksQueue queue (
                                      Reference queue_id
                                      )
    {
        final TimerQueue timer_queue;
        synchronized ( this.lock )
        {
            timer_queue = this.queues.get ( queue_id );
            if ( timer_queue == null )
            {
                // No such queue.
                return null;
            }
        }

        return timer_queue.queue ();
    }


    /**
     * @see musaico.kernel.task.TimedExecutor#reschedule(musaico.io.Reference)
     */
    @Override
    public TimedExecutor reschedule (
                                     Reference queue_id
                                     )
    {
        final TimerQueue timer_queue;
        synchronized ( this.lock )
        {
            timer_queue = this.queues.get ( queue_id );
            if ( timer_queue == null )
            {
                // No such queue.
                return this;
            }
        }

        timer_queue.reschedule ();

        return this;
    }


    /**
     * @see musaico.kernel.task.TimedExecutor#schedule(musaico.kernel.task.ScheduledTasksQueue)
     */
    @Override
    public TimedExecutor schedule (
                                   ScheduledTasksQueue queue
                                   )
        throws ScheduleException
    {
        // Create a new TimerQueue for the scheduled tasks.
        TimerQueue timer_queue = new TimerQueue ( queue );

        synchronized ( this.lock )
        {
            if ( this.queues.containsKey ( queue.id () ) )
            {
                throw new ScheduleException ( "Executor [%timed_executor%] already contains scheduled tasks queue [%queue_id%]",
                                              "timed_executor", this,
                                              "queue_id", queue.id () );
            }

            this.queues.put ( queue.id (), timer_queue );
        }

        // Start the timer queue executing tasks.
        timer_queue.start ();

        return this;
    }
}
