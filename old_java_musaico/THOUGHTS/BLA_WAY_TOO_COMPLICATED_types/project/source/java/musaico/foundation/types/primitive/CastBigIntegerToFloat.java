package musaico.types.primitive;

import java.io.Serializable;

import java.math.BigInteger;


import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from BigInteger to Float.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public class CastBigIntegerToFloat
    implements TypeCaster<BigInteger,Float>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public Float cast (
                       BigInteger from
                       )
        throws TypeException
    {
        float float_value = from.floatValue ();
        BigInteger cast_value = new BigInteger ( "" + float_value );
        if ( cast_value.compareTo ( from ) != 0 )
        {
            // Can't cast if the number has too many digits / is too precise.
            BigInteger precision_loss = from.subtract ( cast_value );
            throw new TypeException ( "Failed to cast from [%from_class%] [%from_object%] to [%to_class%]: Loss of precision [%precision_loss%]",
                                      "from_class", this.fromClass (),
                                      "from_object", from,
                                      "to_class", this.toClass (),
                                      "precision_loss", precision_loss );
        }

        return new Float ( float_value );
    }


    /**
     * @see musaico.types.TypeCaster#fromClass()
     */
    @Override
    public Class<BigInteger> fromClass ()
    {
        return BigInteger.class;
    }


    /**
     * @see musaico.types.TypeCaster#toClass()
     */
    @Override
    public Class<Float> toClass ()
    {
        return Float.class;
    }
}
