package musaico.kernel.task.schedulers;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.kernel.task.Priority;
import musaico.kernel.task.Priorities;
import musaico.kernel.task.Schedule;
import musaico.kernel.task.ScheduleException;
import musaico.kernel.task.Scheduler;
import musaico.kernel.task.Task;

import musaico.kernel.task.executors.TimerTaskExecutor;

import musaico.kernel.task.queue.ScheduledTask;
import musaico.kernel.task.queue.SchedulerStrategy;
import musaico.kernel.task.queue.TimedExecutor;

import musaico.kernel.task.strategies.SimpleSchedulerStrategy;


/**
 * <p>
 * A scheduler which creates one Timer (thread) and one TimerTask
 * for each queue of tasks to be executed.
 * </p>
 *
 * <p>
 * The SchedulerStrategy determines whether each task gets its own
 * queue (thread), or whether it is acceptable to put multiple tasks
 * on a queue (thread).
 * </p>
 *
 * <p>
 * One queue per task can overload a system if there are many
 * tasks to execute.  On the other hand, multiple tasks per
 * queue can cause delays in execution, since whenever a long-running
 * task is executed, the remaining tasks on the queue must wait.
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
public class TimerScheduler
    implements Scheduler
{
    /** Lock critical sections on this token, and allow the
     *  executor to lock on this token as well. */
    private final Serializable lock = new String ();

    /** The SchedulerStrategy decides how and when to start
     *  threads, how to group together ScheduledTasks into
     *  queues, which Priorities are supported, and so on. */
    private final SchedulerStrategy strategy;

    /** The TimedExecutor takes care of executing the scheduled
     *  task(s) on a given queue at the right time.  It takes care
     *  of Timers and TimerTasks, or thread pooling, or what have you. */
    private final TimedExecutor executor;


    /**
     * <p>
     * Creates a new TimerScheduler with a SimpleSchedulerStrategy
     * and a normal TimerTaskExecutor.
     * </p>
     */
    public TimerScheduler ()
    {
        this ( new SimpleSchedulerStrategy () );
    }


    /**
     * <p>
     * Creates a new TimerScheduler with the specified SchedulerStrategy
     * and a normal TimerTaskExecutor.
     * </p>
     *
     * @param scheduler_strategy The strategy to use for allocating
     *                           task queues in this scheduler.
     *                           Must not be null.
     */
    public TimerScheduler (
                           SchedulerStrategy scheduler_strategy
                           )
    {
        if ( scheduler_strategy == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a TimerScheduler with scheduler strategy [%scheduler_strategy%]",
                                                     "scheduler_strategy", scheduler_strategy );
        }

        this.strategy = scheduler_strategy;
        this.executor = new TimerTaskExecutor ( this.lock );
    }


    /**
     * @see musaico.kernel.task.Scheduler#priorities()
     */
    public Priorities priorities ()
    {
        return this.strategy.priorities ();
    }


    /**
     * @see musaico.kernel.task.Scheduler#schedule(musaico.kernel.task.Task,musaico.kernel.task.Schedule,musaico.kernel.task.Priority)
     */
    public Scheduler schedule (
                               Task task,
                               Schedule schedule,
                               Priority priority
                               )
        throws ScheduleException
    {
        ScheduledTask scheduled_task = new ScheduledTask ( task,
                                                           schedule,
                                                           priority );

        synchronized ( this.lock )
        {
            this.strategy.schedule ( this.executor,
                                     scheduled_task );
        }

        return this;
    }
}
