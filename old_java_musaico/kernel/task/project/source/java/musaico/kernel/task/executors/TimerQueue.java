package musaico.kernel.task.executors;


import java.io.Serializable;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.kernel.task.Priority;
import musaico.kernel.task.Task;

import musaico.kernel.task.queue.ScheduledTask;
import musaico.kernel.task.queue.ScheduledTasksQueue;

import musaico.time.AbsoluteTime;
import musaico.time.Time;


/**
 * <p>
 * A queue of tasks to be executed by a single Timer (thread).
 * </p>
 *
 * <p>
 * There is one TimerTask which gets executed every time a task
 * needs to be executed.
 * </p>
 *
 * <p>
 * In Java, every TimerQueue must be Serializable, in order to play
 * nicely over RMI.  (Note, though, that Tasks might be either
 * Serializable or UnicastRemoteObjects.)
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
public class TimerQueue
    implements Serializable
{
    /** Lock all critical sections on this token: */
    private final Serializable lock = new String ();

    /** The Timer (thread) in which we execute tasks.
     *  Created at start () time, deleted at stop () time. */
    private Timer timer;
    private TimerTask timerTask;

    /** The queue of tasks. */
    private final ScheduledTasksQueue queue;

    /** The next task to execute at a specific time. */
    private ScheduledTask nextScheduledTask = ScheduledTask.NONE;

    /** The time at which we've scheduled execution of the
     *  next scheduled task. */
    private Time nextScheduledTime = Time.NEVER;


    /**
     * <p>
     * Creates a new TimerQueue to execute the specified queue of
     * task(s) on a single Timer (thread).
     * </p>
     *
     * @param queue The queue of scheduled tasks.  Must not be null.
     */
    public TimerQueue (
                       ScheduledTasksQueue queue
                       )
    {
        if ( queue == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a TimerQueue with queue [%queue%]",
                                                     "queue", queue );
        }

        this.queue = queue;
    }


    /**
     * <p>
     * Returns the queue of scheduled tasks.
     * </p>
     *
     * @return This TimerQueue's scheduled tasks.  Never null.
     */
    public ScheduledTasksQueue queue ()
    {
        return this.queue;
    }


    /**
     * <p>
     * Re-reads the queue and changes the TimerTask accordingly.
     * </p>
     */
    public void reschedule ()
    {
        // Figure out the first scheduled task.
        synchronized ( this.lock )
        {
            ScheduledTask previously_scheduled_task =
                this.nextScheduledTask;
            Time previously_scheduled_time =
                this.nextScheduledTime;

            // Re-sort the queue.
            this.queue ().sort ();

            this.nextScheduledTask = this.queue ().scheduledTasks ().first ();
            if ( this.nextScheduledTask == ScheduledTask.NONE )
            {
                // Nothing scheduled.
                this.nextScheduledTime = Time.NEVER;
                if ( this.timer != null )
                {
                    this.timer.cancel ();
                }
                this.timer = null;
                return;
            }

            this.nextScheduledTime =
                this.nextScheduledTask.nextExecutionTime ();
            if ( ! ( this.nextScheduledTime instanceof AbsoluteTime ) )
            {
                if ( this.timer != null )
                {
                    this.timer.cancel ();
                }
                this.timer = null;
                return;
            }

            // Don't bother changing anything if we've already
            // got the right next task and time.
            if ( this.nextScheduledTask.equals ( previously_scheduled_task )
                 && this.nextScheduledTime.equals ( previously_scheduled_time ) )
            {
                return;
            }

            if ( this.timer == null )
            {
                // Create the first timer task.
                this.timer = new Timer ();
                this.timerTask = this.createTimerTask ();
            }
            else
            {
                // Replace the old timer task.
                this.timerTask.cancel ();
                this.timerTask = this.createTimerTask ();
            }

            AbsoluteTime absolute_next_time = (AbsoluteTime)
                this.nextScheduledTime;
            Date next_date = absolute_next_time.date ();

            // !!! this.timer.schedule ( this, next_date );
            this.timer.schedule ( this.timerTask, next_date );
        }
    }


    /**
     * <p>
     * Executes the next scheduled task in the queue, updates
     * its last executed time, re-sorts the queue, and reschedules
     * the timer task.
     * </p>
     */
    private TimerTask createTimerTask ()
    {
        return new TimerTask ()
        {
            public void run ()
            {
                runNextTask ();
            }
        };
    }

    public void runNextTask ()
    {
        // First set our thread priority.
        Priority queue_priority = this.queue ().priority ();
        final long numeric_priority;
        if ( queue_priority == Priority.NO_SUCH_PRIORITY )
        {
            numeric_priority = Long.MAX_VALUE;
        }
        else
        {
            numeric_priority = queue_priority.id ();
        }

        final int thread_priority;
        if ( numeric_priority <= Integer.MAX_VALUE )
        {
            thread_priority = (int) numeric_priority;
        }
        else
        {
            thread_priority = Integer.MAX_VALUE;
        }
        // !!!         System.out.println ( "!!! PRIORITY " + thread_priority );
        // !!! Doesn't work???: Thread.currentThread ().setPriority ( thread_priority );

        // Now run the next scheduled task.
        final ScheduledTask scheduled_task;
        final Time scheduled_time;
        final Task task;

        synchronized ( this.lock )
        {
            scheduled_task = this.nextScheduledTask;
            scheduled_time = this.nextScheduledTime;

            task = scheduled_task.task ();

            // Step to the next scheduled time for the task.
            if ( scheduled_time instanceof AbsoluteTime )
            {
                AbsoluteTime absolute_scheduled_time = (AbsoluteTime)
                    scheduled_time;
                scheduled_task.lastExecutionTime ( absolute_scheduled_time );
            }
        }

        // Don't execute the task inside a critical section.
        // That could cause long blocks on reschedule () calls
        // and so on from the parent scheduler.
        try
        {
            task.execute ();
        }
        catch ( Throwable task_failure )
        {
            // !!! LOG IT!

            // Task failed miserably.  First remove it from
            // the scheduler.
            this.queue ().remove ( scheduled_task );

            // Now clean up the task.
            try
            {
                task.cleanUp ( task_failure );
            }
            catch ( Throwable cleanup_failure )
            {
                // !!!! LOG IT!
            }

            // Finally, reschedule and carry on about our business.
            this.reschedule ();
            return;
        }

        boolean is_task_finished = false;
        synchronized ( this.lock )
        {
            Time next_scheduled_time = scheduled_task.nextExecutionTime ();

            // Have we finished all executions of the task?
            // If so, finish it off and remove it from the queue.
            if ( ! ( next_scheduled_time instanceof AbsoluteTime ) )
            {
                is_task_finished = true;
                this.queue ().remove ( scheduled_task );
            }
        }

        // Is the task completed?
        if ( is_task_finished )
        {
            try
            {
                task.post ();
            }
            catch ( Throwable task_failure )
            {
                // !!! LOG IT!
            }
        }

        // Reschedule to figure out our next task and time.
        this.reschedule ();
    }


    /**
     * <p>
     * Starts the Timer and schedules the first task.
     * </p>
     */
    public void start ()
    {
        if ( this.timer != null )
        {
            throw new I18nIllegalArgumentException ( "TimerQueue [%timer_queue%] has already been started",
                                                     "timer_queue", this );
        }

        this.reschedule ();
    }


    /**
     * <p>
     * Stops the Timer and any future executions.
     * </p>
     */
    public void stop ()
    {
        if ( this.timer == null )
        {
            throw new I18nIllegalArgumentException ( "TimerQueue [%timer_queue%] has already been stopped",
                                                     "timer_queue", this );
        }

        synchronized ( this.lock )
        {
            this.timer.purge (); // !!!
            this.timer.cancel (); // !!!
            this.timer = null;
            this.timerTask.cancel ();
        }

        this.reschedule ();
    }
}
