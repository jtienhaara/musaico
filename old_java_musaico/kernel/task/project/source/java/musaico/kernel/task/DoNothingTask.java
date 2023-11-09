package musaico.kernel.task;


import java.io.Serializable;


import musaico.io.Progress;
import musaico.io.Reference;

import musaico.io.progresses.SimpleProgress;

import musaico.io.references.SimpleSoftReference;


/**
 * <p>
 * A task which does nothing.  No, really!
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
 * Copyright (c) 2012 Johann Tienhaara
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
public class DoNothingTask
    implements Task, Serializable
{
    /** The id of this do-nothing-task. */
    private final Reference id =
        new SimpleSoftReference<String> ( "do_nothing" );


    /**
     * @see musaico.kernel.task.Task#cleanUp(java.lang.Throwable)
     */
    @Override
    public void cleanUp (
                         Throwable cause
                         )
        throws TaskException
    {
    }


    /**
     * @see musaico.kernel.task.Task#execute()
     */
    @Override
    public void execute ()
        throws TaskException
    {
    }


    /**
     * @see musaico.kernel.task.Task#id()
     */
    @Override
    public Reference id ()
    {
        return this.id;
    }


    /**
     * @see musaico.kernel.task.Task#pre()
     */
    @Override
    public void pre ()
        throws TaskException
    {
    }


    /**
     * @see musaico.kernel.task.Task#post()
     */
    @Override
    public void post ()
        throws TaskException
    {
    }


    /**
     * @see musaico.kernel.task.Task#progress()
     */
    @Override
    public Progress progress ()
    {
        return new SimpleProgress ();
    }
}
