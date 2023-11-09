package musaico.kernel.driver;


import java.io.InputStream;
import java.io.OutputStream;


import musaico.kernel.objectsystem.Cursor;


/**
 * <p>
 * A Driver which provides direct access to input and output
 * streams to the next higher up Driver on the stack.
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
public interface StreamDriver
    extends Driver
{
    /**
     * <p>
     * Returns the InputStream for the given Cursor, providing
     * direct access to read bytes from the underlying data source
     * to a Driver up the stack.
     * </p>
     *
     * <p>
     * May only be called when the <code> read() </code> method
     * is callable, when this driver is in the Driver.INITIALIZED
     * state and the Cursor is open with read access on this driver
     * or on an object system backed by this driver.
     * </p>
     *
     * @param cursor The Cursor connection to this driver.  Must not be null.
     *
     * @throws DriverException If this Driver is in an invalid state
     *                         (not initialized, or suspended),
     *                         or if the Cursor is not open on this
     *                         Driver.
     */
    public abstract InputStream in (
                                    Cursor cursor
                                    )
        throws DriverException;


    /**
     * <p>
     * Returns the OutputStream for the given Cursor, providing
     * direct access to write bytes to the underlying data target
     * to a Driver up the stack.
     * </p>
     *
     * <p>
     * May only be called when the <code> write() </code> method
     * is callable, when this driver is in the Driver.INITIALIZED
     * state and the Cursor is open with write access on this driver
     * or on an object system backed by this driver.
     * </p>
     *
     * @param cursor The Cursor connection to this driver.  Must not be null.
     *
     * @throws DriverException If this Driver is in an invalid state
     *                         (not initialized, or suspended),
     *                         or if the Cursor is not open on this
     *                         Driver.
     */
    public abstract OutputStream out (
                                      Cursor cursor
                                      )
        throws DriverException;
}
