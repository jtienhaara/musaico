package musaico.kernel.task;


import java.io.Serializable;


import musaico.io.Progress;
import musaico.io.Reference;


/**
 * <p>
 * Represents a Task which can be scheduled and executed.
 * </p>
 *
 * <p>
 * The stages of execution of a Task are:
 * </p>
 *
 * <ol>
 *   <li> The <code> pre () </code> stage is executed before any
 *        scheduling is done, typically by the instantiator of the Task.
 *        On failure, handling of the failure (logging,
 *        alarms, and so on) is performed.
 *        This stage is intended to set up the (possibly repeated)
 *        execution environment of the task, so it should be
 *        done early in order to fail early if the task will
 *        not be executable. </li>
 *
 *   <li> If successful then the <code> execute () </code> stage is
 *        executed next.  If this fails, <code> cleanUp () </code>
 *        is called, and any additional handling of the failure
 *        (logging, alarms, and so on) is then performed.  A Task
 *        which fails to execute shall never be executed again
 *        by a scheduler, so it is important to only throw
 *        exceptions from the <code> execute () </code> method which
 *        will lead to clean-up and removal from the scheduler.</li>
 *
 *   <li> If successful then the <code> execute () </code> stage
 *        may be repeated, depending on the scheduling of the
 *        Task. </li>
 *
 *   <li> If all calls to <code> execute () </code> succeeded,
 *        then the <code> post () </code> stage is executed next.
 *        If this fails, any additional handling of the failure
 *        (logging, alarms, and so on) is performed, but the
 *        cleanUp method is NOT called.  The
 *        <code> post () </code> method is invoked by the
 *        scheduler after the task's schedule has been exhausted,
 *        if it has been scheduled for execution by a scheduler.
 *        In cases where a Task is executed directly, the
 *        <code> post () </code> method must be invoked by
 *        the user of the task after <code> execute () </code>
 *        has passed. </li>
 * </ol>
 *
 *
 * <p>
 * In Java, every Task must be Serializable in order to play nicely
 * across RMI.  Any Task which requires non-RMI-safe objects should
 * wrap them in a UnicastRemoteObject.  That way the Task itself can
 * be executed from anywhere, even if the objects it depends on
 * are always located on one node.  For example, an I/O library which
 * acts on a specific local node could be wrapped in a UnicastRemoteObject
 * API.  Then whenever the Task implementation executes, it would
 * access the API, thus invoking methods on the I/O library from
 * anywhere across an RMI network.
 * </p>
 *
 * <p>
 * In Java, generally speaking, it is much preferable to avoid
 * UnicastRemoteObjects, and make Tasks as Serializable as possible, all
 * the way down to the dependencies.  This makes the Task more
 * portable (both for code re-use and also runtime-portable across
 * RMI nodes).
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
public interface Task
    extends Serializable
{
    /** A task which does absosmurfly nothing. */
    public static final Task DO_NOTHING = new DoNothingTask ();


    /**
     * <p>
     * Cleans up the task after failure during <code> execute () </code>.
     * </p>
     *
     * @param cause The cause of cleaning up, such as a TaskException,
     *              or an out-of-memory error, and so on.  Must not be
     *              null.  If some reason outside the scope of the Task
     *              itself caused a failure (such as a scheduler falling
     *              over because of its own bug) then an
     *              ExternalScheduleException shall be passed to the
     *              cleanUp() method.
     *
     * @throws TaskException If something goes horribly wrong
     *                       during cleanup of the already-aborted task.
     *                       Some schedulers might provide
     *                       special exception handling (such
     *                       as logging, alarm notification, and
     *                       so on).
     */
    public abstract void cleanUp (
                                  Throwable cause
                                  )
        throws TaskException;


    /**
     * <p>
     * Executes one "step" of this task.
     * </p>
     *
     * <p>
     * If <code> execute () </code> ever throws an
     * exception, then <code> cleanUp () </code> will be
     * invoked and the task aborted.
     * </p>
     *
     * @throws TaskException If something goes horribly wrong
     *                       and the task must be aborted.
     *                       Some schedulers might provide
     *                       special exception handling (such
     *                       as logging, alarm notification, and
     *                       so on).
     */
    public abstract void execute ()
        throws TaskException;


    /**
     * <p>
     * Returns the unique identifier for this Task.
     * </p>
     *
     * @return The unique identifier for this Task.  Never null.
     */
    public abstract Reference id ();


    /**
     * <p>
     * Initializes the task before executing.
     * </p>
     *
     * @throws TaskException If something goes horribly wrong
     *                       and the task must be aborted.
     *                       Some schedulers might provide
     *                       special exception handling (such
     *                       as logging, alarm notification, and
     *                       so on).
     */
    public abstract void pre ()
        throws TaskException;


    /**
     * <p>
     * Cleans up the task after successfully executing.
     * </p>
     *
     * @throws TaskException If something goes horribly wrong
     *                       and the post-work must be aborted.
     *                       Some schedulers might provide
     *                       special exception handling (such
     *                       as logging, alarm notification, and
     *                       so on).
     */
    public abstract void post ()
        throws TaskException;


    /**
     * <p>
     * Returns the progress of this task.
     * </p>
     *
     * <p>
     * A Task which executes a certain number of times should
     * return a Progress which reflects how many times it has
     * executed vs. how many times it will eventually have
     * executed by the time it is finished.
     * </p>
     *
     * <p>
     * A Task which executes indefinitely should just return a
     * zero Progress.
     * </p>
     */
    public abstract Progress progress ();
}
