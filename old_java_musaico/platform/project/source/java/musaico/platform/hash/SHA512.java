package musaico.platform.hash;

import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;


import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * SHA-512 hash instance.
 * </p>
 *
 *
 * <p>
 * The Java implementation relies on
 * <code> MessageDigest.getInstance ( "SHA-512" ) </code>.
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
 * Copyright (c) 2010, 2013 Johann Tienhaara
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
public class SHA512
    extends AbstractHash
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130916L;
    private static final String serialVersionHash =
        "0x56621A4804D6D597E29F46CD58DDAABC9054F360";


    /**
     * <p>
     * Creates a SHA-512 hash of the specified bytes array.
     * </p>
     *
     * @param bytes The input data.  Must not be null.
     *
     * @throws HashException If the hash cannot be computed for some reason.
     */
    public SHA512 (
                   byte [] bytes
                   )
        throws ParametersMustNotBeNull.Violation,
               HashAlgorithmMustBeBuiltIn.Violation
    {
        super ( "SHA-512", bytes );
    }


    /**
     * <p>
     * Creates a SHA-512 hash of the specified InputStream.
     * </p>
     *
     * @param input_stream The input stream.  Must not be null.
     *
     * @throws HashException If the hash cannot be computed for some reason.
     */
    public SHA512 (
                 InputStream input_stream
                 )
        throws ParametersMustNotBeNull.Violation,
               HashAlgorithmMustBeBuiltIn.Violation,
               IOException
    {
        super ( "SHA-512", input_stream );
    }
}
