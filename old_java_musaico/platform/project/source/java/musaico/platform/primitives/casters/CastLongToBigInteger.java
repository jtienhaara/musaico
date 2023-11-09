package musaico.types.primitive;


import java.io.Serializable;

import java.math.BigInteger;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from Long to BigInteger.
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
 * Copyright (c) 2009 Johann Tienhaara
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
public class CastLongToBigInteger
    implements TypeCaster<Long,BigInteger>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public BigInteger cast (
                            Long from
                            )
        throws TypeException
    {
        long long_value = from.longValue ();
        return BigInteger.valueOf ( long_value );
    }


    /**
     * @see musaico.types.TypeCaster#fromClass()
     */
    @Override
    public Class<Long> fromClass ()
    {
        return Long.class;
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
