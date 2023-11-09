package musaico.kernel_modules.drivers.byte_stream;


import java.io.InputStream;
import java.io.OutputStream;


import musaico.io.Reference;

import musaico.kernel.driver.Driver;


/**
 * <p>
 * Keeps track of I/O streams for the ByteStreamDriver.
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
public class Streams
{
    /** The input stream (if any). */
    private final InputStream in;

    /** The output stream (if any). */
    private final OutputStream out;

    /** The current state of the streams, such as in the middle of a
     *  transaction. */
    private Reference state;


    /**
     * <p>
     * Creates a new Streams pair with the specified InputStream and
     * OutputStream, in state Driver.INITIALIZED.
     * </p>
     *
     * @param in The input stream (if any).  Can be null.
     *
     * @param out The output stream (if any).  Can be null.
     */
    public Streams (
                    InputStream in,
                    OutputStream out
                    )
    {
        this.in = in;
        this.out = out;
        this.state = Driver.INITIALIZED;
    }


    /**
     * <p>
     * Returns the input stream.
     * </p>
     *
     * @return The InputStream.  Can be null.
     */
    public InputStream in ()
    {
        return this.in;
    }


    /**
     * <p>
     * Returns the output stream.
     * </p>
     *
     * @return The OutputStream.  Can be null.
     */
    public OutputStream out ()
    {
        return this.out;
    }


    /**
     * <p>
     * Returns the current state of this Streams pair, such as
     * Driver.INITIALIZED, or in the middle of a transaction, and
     * so on.
     * </p>
     *
     * @return The current state.  Never null.
     */
    public Reference state ()
    {
        return this.state;
    }
}
