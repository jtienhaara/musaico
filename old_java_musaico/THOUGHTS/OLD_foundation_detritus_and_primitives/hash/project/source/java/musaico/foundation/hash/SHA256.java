package musaico.foundation.hash;


import java.io.InputStream;
import java.io.Serializable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * <p>
 * SHA-256 hash instance.
 * </p>
 *
 *
 * <p>
 * The Java implementation relies on
 * <code> MessageDigest.getInstance ( "SHA-256" ) </code>.
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
 * Copyright (c) 2010 Johann Tienhaara
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
public class SHA256
    extends AbstractHash
    implements Serializable
{
    /**
     * <p>
     * Creates a SHA-256 hash of the specified bytes array.
     * </p>
     *
     * @param bytes The input data.  Must not be null.
     *
     * @throws HashException If the hash cannot be computed for some reason.
     */
    public SHA256 (
                   byte [] bytes
                   )
        throws HashException
    {
        super ( "SHA-256", bytes );
    }


    /**
     * <p>
     * Creates a SHA-256 hash of the specified InputStream.
     * </p>
     *
     * @param input_stream The input stream.  Must not be null.
     *
     * @throws HashException If the hash cannot be computed for some reason.
     */
    public SHA256 (
                   InputStream input_stream
                   )
        throws HashException
    {
        super ( "SHA-256", input_stream );
    }
}
