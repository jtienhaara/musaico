package musaico.kernel.task.scheduler;


/**
 * <p>
 * Executes one or more tasks at a common priority level,
 * each with its own (possibly common or possibly unique)
 * Schedule.
 * </p>
 *
 * <p>
 * Each time <code> run () </code> is executed, any and all
 * tasks which are due (or overdue) for execution are run.
 * </p>
 *
 *
 * <p>
 * In Java, every TaskRunner must be Serializable in order
 * to play nicely over RMI.  (Note that all Tasks are
 * either Serializable or UnicastRemoteObjects, so that they
 * can play nicely over RMI too.)
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
public class TaskRunner
    extends Runnable
{
    /** Tasks, sorted by next scheduled execution time. */
}
