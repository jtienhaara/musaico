package musaico.kernel.task.queue;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.kernel.task.Priority;
import musaico.kernel.task.Schedule;
import musaico.kernel.task.Task;

import musaico.time.AbsoluteTime;
import musaico.time.Time;


/**
 * <p>
 * Bundles together a Task, its Schedule, and the most recently
 * executed Time in one shrinkwrapped, easy-to-use class.
 * </p>
 *
 *
 * <p>
 * In Java every ScheduledTask must be Serializable, in order to
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
public class ScheduledTask
    implements Serializable
{
    /** No scheduled task.  This task has never been executed
     *  and never will be. */
    public static final ScheduledTask NONE =
        new ScheduledTask ();


    /** The scheduled Task. */
    private final Task task;

    /** The Schedule on which it is to be executed. */
    private final Schedule schedule;

    /** The priority level at which it is to be executed. */
    private final Priority priority;

    /** The Time at which the task was most recently executed,
     *  or Time.NEVER to start. */
    private Time lastExecutionTime;


    /**
     * <p>
     * Creates a new ScheduledTask for the specified Task and
     * Schedule.
     * </p>
     *
     * @param task The scheduled task.  Must not be null.
     *
     * @param schedule The execution schedule for the task.  Must not be null.
     *
     * @param priority The priority level to execute the task at.
     *                 Must not be null.
     */
    public ScheduledTask (
                          Task task,
                          Schedule schedule,
                          Priority priority
                          )
    {
        this ( task, schedule, priority, Time.NEVER );
    }


    /**
     * <p>
     * Creates a new ScheduledTask for the specified Task and
     * Schedule which has already been executed at the specified Time.
     * </p>
     *
     * @param task The scheduled task.  Must not be null.
     *
     * @param schedule The execution schedule for the task.  Must not be null.
     *
     * @param priority The priority level to execute the task at.
     *                 Must not be null.
     *
     * @param last_execution_time The last time the task was executed.
     *                            Must not be null.  Must either be
     *                            Time.NEVER or an AbsoluteTime.
     */
    public ScheduledTask (
                          Task task,
                          Schedule schedule,
                          Priority priority,
                          Time last_execution_time
                          )
    {
        if ( task == null
             || schedule == null
             || priority == null
             || last_execution_time == null
             || last_execution_time != Time.NEVER
             && ! ( last_execution_time instanceof AbsoluteTime ) )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a ScheduledTask with task [%task%] schedule [%schedule%] priority [%priority%] last executed time [%last_execution_time%]",
                                                     "task", task,
                                                     "schedule", schedule,
                                                     "last_execution_time", last_execution_time );
        }

        this.task = task;
        this.schedule = schedule;
        this.priority = priority;
        this.lastExecutionTime = last_execution_time;
    }


    /**
     * <p>
     * Creates the NONE scheduled task.
     * </p>
     */
    private ScheduledTask ()
    {
        this.task = Task.DO_NOTHING;
        this.schedule = Schedule.NONE;
        this.priority = Priority.BATCH;
        this.lastExecutionTime = Time.NEVER;
    }


    /**
     * <p>
     * Returns the last time at which the task was executed.
     * </p>
     *
     * @return The last AbsoluteTime at which the task was executed,
     *         or Time.NEVER if it has never been executed before.
     */
    public Time lastExecutionTime ()
    {
        return this.lastExecutionTime;
    }


    /**
     * <p>
     * Sets the last time at which the task was executed.
     * </p>
     *
     * @param last_execution_time The last AbsoluteTime at which the
     *                            task was executed.  Must not be null.
     */
    public void lastExecutionTime (
                                   AbsoluteTime last_execution_time
                                   )
    {
        if ( last_execution_time == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot set the last executed time for task [%task%] with schedule [%schedule%] priority [%priority%] to [%last_execution_time%]",
                                                     "task", task,
                                                     "schedule", schedule,
                                                     "priority", priority,
                                                     "last_execution_time", last_execution_time );
        }

        this.lastExecutionTime = last_execution_time;
    }


    /**
     * <p>
     * Returns the next time at which the task shall be executed.
     * </p>
     *
     * @return The next AbsoluteTime at which the task shall be executed,
     *         or Time.NEVER if it should never again be executed.
     */
    public Time nextExecutionTime ()
    {
        final Time last_time = this.lastExecutionTime ();
        final Time next_time;
        if ( last_time == null
             || last_time.equals ( Time.NEVER ) )
        {
            // Never been executed before.
            next_time = this.schedule ().first ();
        }
        else
        {
            // Step one from the last executed time.
            next_time = this.schedule ().after ( last_time );
        }

        return next_time;
    }


    /**
     * <p>
     * Returns the priority level at which the task is to be executed.
     * </p>
     *
     * @return The Priority for the task.  Never null.
     */
    public Priority priority ()
    {
        return this.priority;
    }


    /**
     * <p>
     * Returns the schedule on which the task is to be executed.
     * </p>
     *
     * @return The Schedule for the task.  Never null.
     */
    public Schedule schedule ()
    {
        return this.schedule;
    }


    /**
     * <p>
     * Returns the task which has been scheduled.
     * </p>
     *
     * @return The scheduled Task.  Never null.
     */
    public Task task ()
    {
        return this.task;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "" + this.task ().id ()
            + " schedule: " + this.schedule () + " "
            + this.priority ();
    }
}
