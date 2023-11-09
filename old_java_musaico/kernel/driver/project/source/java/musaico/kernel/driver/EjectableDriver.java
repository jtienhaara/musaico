package musaico.kernel.driver;


import musaico.io.Path;
import musaico.io.Reference;

import musaico.io.references.SimpleSoftReference;


/**
 * <p>
 * A Driver with eject and insert capabilities.
 * </p>
 *
 * <p>
 * Insert and eject mirror the opeations of a CD ROM driver or
 * a magnetic tape driver or a floppy disk driver in the operating
 * system world.
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
 * Copyright (c) 2009, 2010, 2011 Johann Tienhaara
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
public interface EjectableDriver
    extends Driver
{
    /** The ejectable driver is currently in a "tray open" state. */
    public static final Reference EJECTED =
        new SimpleSoftReference<Path> ( new Path ( "/state/driver/ejected" ) );


    /**
     * <p>
     * Ejects the "media" from this driver (along the lines of
     * causing a CD ROM driver to eject its Compact Disc).
     * </p>
     *
     * <p>
     * May be called only from the Driver.INITIALIZED state.
     * </p>
     *
     * <p>
     * Leaves this driver in the EjectableDriver.EJECTED state.
     * </p>
     *
     * <p>
     * Does not return until the ejection has completed.
     * </p>
     */
    public abstract void eject ()
        throws DriverException;


    /**
     * <p>
     * Inserts "media" into the driver (aloong the lines of closing
     * the tray of a CD ROM driver).
     * </p>
     *
     * <p>
     * May be called only from the EjectableDriver.EJECTED state.
     * </p>
     *
     * <p>
     * Returns this driver to the Driver.INITIALIZED state.
     * </p>
     *
     * <p>
     * Does not return until the insertion has completed.
     * </p>
     */
    public abstract void insert ()
        throws DriverException;
}
