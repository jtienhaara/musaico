package musaico.kernel.driver;


import musaico.kernel.objectsystem.Cursor;

import musaico.region.Size;


/**
 * <p>
 * A driver which provides persistence rather than streaming,
 * such as a file system driver, or a database server driver,
 * or a memory driver, and so on.
 * </p>
 *
 * <p>
 * A BlockDriver is much like a block driver in the operating
 * system world: it can be used to read chunks of data into
 * a buffer cache, where it is available for quick access,
 * while updates to the buffer cache are periodically written
 * out to persistent storage.
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
public interface BlockDriver
    extends Driver
{
    /**
     * <p>
     * Flushes all pages from the buffer cache for this block driver,
     * writing them out to this driver.
     * </p>
     *
     * <p>
     * May only be called from the INITIALIZED state.
     * </p>
     *
     * <p>
     * Does not return until the flushing has completed.
     * </p>
     */
    public abstract void flush ()
        throws DriverException;


    /**
     * <p>
     * Returns the number of sectors which should be read ahead,
     * to optimize performance for this driver.
     * </p>
     *
     * <p>
     * Storage drivers (such as database
     * servers or file systems) should return sensible numbers
     * depending on the underlying technologies.  For example,
     * a file system with built-in readaheads will always be
     * faster than the driver, so there is no point in reading
     * ahead.  However a bulky database server on a remote machine
     * might be a good candidate for reading ahead several sectors.
     * </p>
     *
     * <p>
     * Many block drivers should return 0 for readahead.  However
     * large data transfers over wide-area networks would make
     * sense to read ahead.  Where possible, a block driver should
     * return a readahead value which takes into account the size
     * of the data being read and the latency of the read operation.
     * </p>
     *
     * <p>
     * Stackable protocol parsers / formatters should be optimized
     * according to how fast or slow the parsing / formatting is.
     * For example, a complex data format or a backtracking driver
     * or a "high level" driver (one which is useless without
     * several layers beneath it, such as an XML-SOAP
     * parser/formatter) should provide some readahead sectors.
     * On the other hand, a simple filter (such as a filter which
     * converts all text to lower case during reads and writes)
     * need not read ahead.
     * </p>
     *
     * <p>
     * Ultimately the kernel should choose the largest readahead
     * from a stack of drivers.  This will ensure each driver in
     * the stack keeps up with requests from user space.
     * </p>
     *
     * <p>
     * May only be called from the Driver.INITIALIZED state.
     * </p>
     *
     * @param cursor The cursor which is open on this driver.
     *               Depending on the the nature of the BlockDriver,
     *               the read-ahead Size may depend on the
     *               Cursor's position.  Must not be null.
     *
     * @return The number of sectors to read ahead.  Never null.
     *         Never an UnknownSize or a NoSize.
     */
    public abstract Size sectorReadAhead (
                                          Cursor cursor
                                          )
        throws DriverException;


    /**
     * <p>
     * Returns the size of each sector in this Driver (such as
     * fixed-length linear 16 Fields, or 512 Fields, or possibly even
     * variable-length depending on the sector).
     * </p>
     *
     * <p>
     * Each Driver should choose a sector size to optimize
     * efficiency according to its own internals.  If in doubt,
     * try an ArraySize with 512 Fields.
     * </p>
     *
     * <p>
     * May only be called from the Driver.INITIALIZED state.
     * </p>
     *
     * @param cursor The cursor which is open on this block driver.
     *               Depending on the nature of this driver, the
     *               sector size may vary depending on the cursor's
     *               position.  Must not be null.
     *
     * @return The layout and size (in terms of number of Fields) of
     *         one sector in this block driver.  Must not be null.
     *
     * @throws DriverException If this BlockDriver is not initialized
     *                         and so cannot inspect the sector size.
     */
    public abstract Size sectorSize (
                                     Cursor cursor
                                     )
        throws DriverException;
}
