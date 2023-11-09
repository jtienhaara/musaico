package musaico.types.primitive;


import java.io.Serializable;

import java.math.BigInteger;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from Float to BigInteger.
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
public class CastFloatToBigInteger
    implements TypeCaster<Float,BigInteger>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public BigInteger cast (
                            Float from
                            )
        throws TypeException
    {
        String string_value = "" + from;
        final BigInteger big_integer;
        try
        {
            big_integer = new BigInteger ( string_value );
        }
        catch ( NumberFormatException e )
        {
            throw new TypeException ( "Failed to cast from [%from_class%] [%from_object%] to [%to_class%]",
                                      "from_class", this.fromClass (),
                                      "from_object", from,
                                      "to_class", this.toClass (),
                                      "cause", e );
        }

        return big_integer;
    }


    /**
     * @see musaico.types.TypeCaster#fromClass()
     */
    @Override
    public Class<Float> fromClass ()
    {
        return Float.class;
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
