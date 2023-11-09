package musaico.kernel.task.queue;


import java.io.Serializable;


import musaico.io.Reference;

import musaico.kernel.task.Priority;
import musaico.kernel.task.ScheduleException;


/**
 * <p>
 * Takes care of threading (including using TimerTasks) in order
 * to execute a scheduling strategy.
 * </p>
 *
 * <p>
 * For all intents and purposes, the TimedExecutor is a particular
 * Scheduler's braun, while the SchedulerStrategy is its brains.
 * Typically a SchedulerStrategy decides what the TimedExecutor
 * should do, and the TimedExecutor builds TimerTasks and
 * ScheduledTasksQueues accordingly.
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
public interface TimedExecutor
    extends Serializable
{
    /**
     * <p>
     * Removes the specified ScheduledTasksQueue from scheduled execution.
     * </p>
     *
     * @param queue_id The unique identifier of the ScheduledTasksQueue
     *                 which will be returned.  Must not be null.  Must
     *                 be a queue in this TimedExecutor.
     *
     * @return The specified ScheduledTasksQueue.  Never null.
     *         The queue is not changed in any way by the TimedExecutor.
     *         It is simply removed from internal structures and
     *         returned.  Any cleanup of the queue is the responsibility
     *         of the caller.  For example, Tasks are not removed
     *         from the queue.
     *
     * @throws ScheduleException If something goes horribly wrong.
     */
    public abstract ScheduledTasksQueue destroyQueue (
                                                      Reference queue_id
                                                      )
        throws ScheduleException;


    /**
     * <p>
     * Returns the priority level of the threader executing the
     * queue of tasks identified by the specified id.
     * </p>
     *
     * @param queue_id The unique identifier of the queue whose
     *                 threader Priority will be returned.
     *                 Must not be null.
     *
     * @return The threader's priority.  Note that this need
     *         not correspond with the priorit(y/ies) of the
     *         task(s) on the queue.  It can be
     *         used to set thread priorities and so on in
     *         the threader.  Beyond that, the SchedulerStrategy
     *         or whoever scheduled the threader in the first
     *         place determines what (if anything) the threader's
     *         priority level means.  Never null.
     */
    public abstract Priority priority (
                                       Reference queue_id
                                       );


    /**
     * <p>
     * Returns the specified ScheduledTasksQueue.
     * </p>
     *
     * @param queue_id The unique identifier of the queue to return.
     *                 Must not be null.
     *
     * @return The requested ScheduledTasksQueue, or null if
     *         no such queue exists.
     */
    public ScheduledTasksQueue queue (
                                      Reference queue_id
                                      );


    /**
     * <p>
     * Reschedules the specified ScheduledTasksQueue.
     * </p>
     *
     * <p>
     * This method must be invoked after changing the contents
     * of a queue externally to the executor.  For example, if
     * a task is canceled or added to the queue by the external
     * system, <code> reschedule () </code> must be invoked to
     * change the timing triggers accordingly.
     * </p>
     *
     * @param queue_id The unique identifier of the queue to reschedule.
     *                 Must not be null.
     *
     * @return This TimedExecutor.  Never null.
     */
    public TimedExecutor reschedule (
                                     Reference queue_id
                                     );


    /**
     * <p>
     * Schedules a queue of tasks for execution.
     * </p>
     *
     * <p>
     * In Java this typically means creating a new Timer or a
     * Thread from a thread pool, though other concurrency
     * mechanisms are equally valid.  For example, spawning
     * a new process, sending the queue of tasks to another networked
     * machine, or using operating system poll / select type
     * mechanisms, are all valid approaches to scheduling the
     * queue for execution.
     * </p>
     *
     * <p>
     * One guarantee must hold true: that tasks which are
     * executed on different threaders will not block / interfere
     * with each other.  For example, a long running task
     * executed by <code> X </code> will not prevent a
     * task executed by <code> Y </code> from running on time.
     * </p>
     *
     * <p>
     * (How strongly the implementor can make this guarantee
     * depends on the implementation...  For example, context
     * switching in a multi-threaded environment can fail to
     * meet the guarantee when system load is high or memory
     * is maxed out.  But the guarantee must hold under
     * normal conditions...)
     * </p>
     *
     * @param queue The ScheduledTasksQueue which will be executed
     *              concurrently with all other queues in this
     *              TimedExecutor.  Must not be null.
     *
     * @return This TimedExecutor.  Never null.
     *
     * @throws I18nIllegalArgumentException If the parameters
     *                                      are invalid or if
     *                                      the specified queue's id
     *                                      is not unique.
     *
     * @throws ScheduleException If something goes horribly wrong.
     */
    public abstract TimedExecutor schedule (
                                            ScheduledTasksQueue queue
                                            )
        throws ScheduleException;
}
