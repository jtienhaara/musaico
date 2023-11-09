package musaico.kernel.task;


/**
 * <p>
 * A scheduler, which implements specific policies for threading
 * an execution, fairness, error handling, and so on.
 * </p>
 *
 * <p>
 * In Java, although Schedules and Priority's need to be Serializable,
 * a Scheduler does not.  In cases where Schedulers are shared
 * over RMI, the individual Scheduler implementations should be made either
 * Serializable or UnicastRemoteObject.  However generally speaking
 * a Scheduler should be tied to one node / VM.
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
public interface Scheduler
{
    /**
     * <p>
     * Returns this Scheduler's list of priorities
     * (such as low/batch priority, medium priority and realtime
     * priority).
     * </p>
     *
     * @return This Scheduler's list of Priorities.  Never null.
     */
    public abstract Priorities priorities ();


    /**
     * <p>
     * Schedules the specified task.
     * </p>
     *
     * <p>
     * The priority reflects the scheduling precision requested by
     * the caller.  Priorities are handled in different ways by different
     * schedulers, but <code> scheduler.priorities ().start () </code>
     * will always be lower priority than the next (if any), and
     * <code> scheduler.priorities.end () </code> will always be
     * the highest priority available to the caller.
     * </p>
     *
     * @param task The task to schedule.  Must not be null.
     *             Must be unique within this Scheduler
     *             (it is illegal to schedule the same Task with
     *             multiple schedules and/or priorities).
     *
     * @param schedule The time(s) at which to execute the task.
     *                 Must not be null.
     *
     * @param priority The priority level at which to execute the
     *                 task, such as
     *                 <code> scheduler.priorities ().normal () </code>.
     *                 Must not be null.  Must be a priority
     *                 in this Scheduler's list of <code> priorities () </code>.
     *
     * @return This scheduler.  Never null.
     *
     * @throws ScheduleException If the task cannot be scheduled as
     *                           requested.  For example, if the
     *                           priority is not supported, or if the
     *                           task queues are full, and so on.
     *                           Some types of ScheduleException may
     *                           indicate that trying again with
     *                           different parameters is warranted.
     *                           For example, a TaskQueueFullException
     *                           might be thrown to indicate that
     *                           a particular priority queue is full;
     *                           but lower or higher priorities may
     *                           be OK.
     */
    public abstract Scheduler schedule (
                                        Task task,
                                        Schedule schedule,
                                        Priority priority
                                        )
        throws ScheduleException;
}
