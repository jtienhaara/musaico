package musaico.kernel.driver;


import musaico.io.Path;
import musaico.io.Reference;

import musaico.io.references.SimpleSoftReference;


/**
 * <p>
 * A Driver which can be suspended and resumed quickly, without performing
 * full shutdown and initialize procedures or losing state.
 * </p>
 *
 * <p>
 * Suspend and resume put a driver to sleep but allow it to wake
 * up quickly.  Typically suspend releases expensive resources,
 * such as unnecessary buffers, threads or network connections, but
 * maintains cheap ones, such as configuration state.
 * </p>
 *
 * <p>
 * Currently the interface is here for future trailblazers.  At the
 * time of writing, there are no intended uses.
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
 * Copyright (c) 2009, 2010,2011 Johann Tienhaara
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
public interface SuspendableDriver
    extends Driver
{
    /** The suspended driver is currently in a "sleeping" state. */
    public static final Reference SUSPENDED =
        new SimpleSoftReference<Path> ( new Path ( "/state/driver/suspended" ) );


    /**
     * <p>
     * Suspends this driver, putting it to sleep indefinitely (but
     * ready to be awoken at a moment's notice).
     * </p>
     *
     * <p>
     * May be called only from the Driver.INITIALIZED state.
     * </p>
     *
     * <p>
     * Leaves this driver in the SuspendableDriver.SUSPENDED state.
     * </p>
     *
     * <p>
     * Does not return until the suspension has completed.
     * </p>
     */
    public abstract void suspend ()
        throws DriverException;


    /**
     * <p>
     * Resumes normal operations for this suspended driver.
     * </p>
     *
     * <p>
     * May be called only from the SuspendableDriver.SUSPENDED state.
     * </p>
     *
     * <p>
     * Returns this driver to the Driver.INITIALIZED state.
     * </p>
     *
     * <p>
     * Does not return until the resumption of normal operations
     * has completed.
     * </p>
     */
    public abstract void resume ()
        throws DriverException;
}
