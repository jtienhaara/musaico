package musaico.types.primitive;


import java.io.Serializable;

import java.math.BigInteger;


import musaico.hash.Hash;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from Hash to BigInteger.
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
public class CastHashToBigInteger<FROM extends Hash>
    implements TypeCaster<FROM,BigInteger>, Serializable
{
    /** The Hash class, such as MD5.class or SHA1.class. */
    private final Class<FROM> fromClass;


    /**
     * <p>
     * Creates a caster to cast from some specific class of Hashes
     * (MD5, SHA1 and so on) to BigIntegers.
     * </p>
     *
     * @param from_class The class of Hashes from which this caster
     *                   will cast to BigIntegers, such as MD5.class
     *                   or SHA1.class and so on.  Must not be null.
     */
    public CastHashToBigInteger (
                                 Class<FROM> from_class
                                 )
    {
        if ( from_class == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a typecaster to cast from Hash class [%from_class%] to [%to_class%]",
                                                     "from_class", from_class,
                                                     "to_class", this.toClass () );
        }

        this.fromClass = from_class;
    }


    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    @Override
    public BigInteger cast (
                            FROM from
                            )
        throws TypeException
    {
        byte [] hash_bytes = from.bytes ();
        BigInteger big_integer = BigInteger.ZERO;
        for ( byte b : hash_bytes )
        {
            // !!!!! CHECK MATH:
            long unsigned_byte = ( (long) b ) & 0xFF;
            big_integer = big_integer.multiply ( BigInteger.valueOf ( 256L ) )
                .add ( BigInteger.valueOf ( unsigned_byte ) );
        }

        return big_integer;
    }


    /**
     * @see musaico.types.TypeCaster#fromClass()
     */
    @Override
    public Class<FROM> fromClass ()
    {
        return this.fromClass;
    }


    /**
     * @see musaico.types.TypeCaster#toClass()
     */
    @Override
    public Class<BigInteger> toClass ()
    {
        return BigInteger.class;
    }
}
