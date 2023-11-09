package musaico.kernel.driver;


import musaico.kernel.task.Scheduler;


/**
 * <p>
 * A driver which has tasks to perform in separate threads.
 * </p>
 *
 * <p>
 * For example, a transactional driver may need to use an internal event
 * queue, and schedule execution of the events on a single thread.
 * Such a driver might schedule the event execution at a fixed interval,
 * or request a scheduling every time some event is added to its queue.
 * </p>
 *
 * <p>
 * If the driver only needs one task on a single thread, then it
 * can implement the Task interface itself.  Or if it needs multiple
 * tasks / multiple threads, it can create individual Tasks as it
 * needs, and tell the Scheduler to schedule them.
 * </p>
 *
 * <p>
 * A SchedulableDriver is <i> not </i> guaranteed to get its own
 * Thread for execution.  Multiple kernel facilities might share
 * Threads, depending on the kernel implementation.
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
public interface SchedulableDriver
    extends Driver
{
    /**
     * <p>
     * Passes the DriverScheduler to this driver.  This driver
     * can maintain a reference to the scheduler as long as it likes
     * (until, at least, the kernel passes a new DriverScheduler to it --
     * at which time it must discard the first scheduler and re-schedule
     * with the new one).
     * </p>
     *
     * <p>
     * The driver may schedule as many tasks as it likes, whenever
     * it likes, using the most recent Scheduler passed in by the kernel.
     * </p>
     *
     * <p>
     * Note, however, that the kernel may deliberately restrict the
     * timing of scheduled tasks for a driver.  For example, the
     * kernel might pass in a Scheduler which guarantees fair time
     * for all schedulable drivers, forcing a particular driver's tasks
     * to be pushed back in the queue when it has just received scheduling
     * time and others are still waiting to be served.  The scheduling
     * "fairness rules" are up to the kernel.
     * </p>
     *
     * @param scheduler The scheduler which can schedule tasks
     *                  for this driver.  Never null.
     *
     * @throws DriverException If anything goes wrong while setting
     *                         up the initially scheduled tasks (if any).
     */
    public abstract void scheduler (
                                    Scheduler scheduler
                                    )
        throws DriverException;
}
