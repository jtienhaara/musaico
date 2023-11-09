package musaico.platform.hash;

import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Abstract hash instance, can be extended to provide SHA-1, MD5, and so on.
 * </p>
 *
 *
 * <p>
 * The Java implementation relies on
 * <code> MessageDigest.getInstance ( algorithm_name ) </code>.
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
public class AbstractHash
    implements Hash, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130916L;
    private static final String serialVersionHash =
        "0x7A4A163C11C00D8CC104D930A3CFB7B9813FC3FF";


    /** Checks contracts on static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AbstractHash.class );


    /** The bytes computed for this hash. */
    private final byte [] hash;

    /** Hash code, in case we're put into a collection. */
    private final int hashCode;


    /**
     * <p>
     * Creates a (algorithm_name) hash of the specified bytes array.
     * </p>
     *
     * @param bytes The input data.  Must not be null.
     *
     * @throws HashException If the hash cannot be computed for some reason.
     */
    protected AbstractHash (
                            String algorithm_name,
                            byte [] bytes
                            )
        throws ParametersMustNotBeNull.Violation,
               HashAlgorithmMustBeBuiltIn.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               algorithm_name, bytes );
        classContracts.check ( HashAlgorithmMustBeBuiltIn.CONTRACT,
                               algorithm_name );

        try
        {
            MessageDigest hash_algorithm =
                MessageDigest.getInstance ( algorithm_name );

            this.hash = hash_algorithm.digest ( bytes );
        }
        catch ( NoSuchAlgorithmException e )
        {
            // Should be impossible, the previous check should have
            // already thrown a violation.
            throw new HashAlgorithmMustBeBuiltIn.Violation ( HashAlgorithmMustBeBuiltIn.CONTRACT,
                                                             AbstractHash.class,
                                                             algorithm_name );
        }

        int hash_code = 0;
        for ( int b = 0; b < this.hash.length; b ++ )
        {
            hash_code += this.hash [ b ];
        }
        this.hashCode = hash_code;
    }


    /**
     * <p>
     * Creates a (algorithm_name) hash of the specified InputStream.
     * </p>
     *
     * @param input_stream The input stream.  Must not be null.
     *
     * @throws HashException If the hash cannot be computed for some reason.
     */
    protected AbstractHash (
                            String algorithm_name,
                            InputStream input_stream
                            )
        throws ParametersMustNotBeNull.Violation,
               HashAlgorithmMustBeBuiltIn.Violation,
               IOException
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               algorithm_name, input_stream );
        classContracts.check ( HashAlgorithmMustBeBuiltIn.CONTRACT,
                               algorithm_name );

        try
        {
            MessageDigest hash_algorithm =
                MessageDigest.getInstance ( algorithm_name );

            byte [] bytes = new byte [ 65536 ];
            int num_bytes;
            long total_bytes = 0L;
            while ( ( num_bytes = input_stream.read ( bytes,
                                                      0,
                                                      bytes.length ) ) >= 0 )
            {
                hash_algorithm.update ( bytes, 0, num_bytes );

                total_bytes += (long) num_bytes;
            }

            this.hash = hash_algorithm.digest ();
        }
        catch ( NoSuchAlgorithmException e )
        {
            // Should be impossible, the previous check should have
            // already thrown a violation.
            throw new HashAlgorithmMustBeBuiltIn.Violation ( HashAlgorithmMustBeBuiltIn.CONTRACT,
                                                             AbstractHash.class,
                                                             algorithm_name );
        }
        catch ( IOException e )
        {
            throw e;
        }

        int hash_code = 0;
        for ( int b = 0; b < this.hash.length; b ++ )
        {
            hash_code += this.hash [ b ];
        }
        this.hashCode = hash_code;
    }


    /**
     * @see musaico.foundation.hash.Hash#bytes()
     */
    public final byte [] bytes ()
    {
        byte [] return_hash = new byte [ this.hash.length ];
        System.arraycopy ( this.hash, 0, return_hash, 0, return_hash.length );
        return return_hash;
    }


    /**
     * @see java.lang.Object#equals(Object)
     */
    public final boolean equals (
                                 Object obj
                                 )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( obj.getClass () != this.getClass () )
        {
            return false;
        }

        AbstractHash other = (AbstractHash) obj;
        byte [] my_bytes = this.hash;
        byte [] other_bytes = other.hash;

        if ( my_bytes == null
             || other_bytes == null )
        {
            return false;
        }

        if ( my_bytes.length != other_bytes.length )
        {
            return false;
        }

        for ( int b = 0; b < my_bytes.length; b ++ )
        {
            if ( my_bytes [ b ] != other_bytes [ b ] )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    public final int hashCode ()
    {
        return this.hashCode;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( this.getClass ().getSimpleName () + ": " );
        for ( int b = 0; b < this.hash.length; b ++ )
        {
            if ( b > 0 )
            {
                sbuf.append ( " " );
            }

            sbuf.append ( "0x" );

            String digits = Integer
                .toString ( this.hash [ b ] + 128, 16 )
                .toUpperCase ();
            if ( digits.length () < 2 )
            {
                sbuf.append ( "0" );
            }

            sbuf.append ( digits );
        }

        return sbuf.toString ();
    }
}
