package musaico.types.primitive;


import java.io.Serializable;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from Long to Integer.
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
public class CastLongToInteger
    implements TypeCaster<Long,Integer>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public Integer cast (
                         Long from
                         )
        throws TypeException
    {
        long long_value = from.longValue ();
        if ( long_value > ( (long) Integer.MAX_VALUE )
             || long_value < ( (long) Integer.MIN_VALUE ) )
        {
            throw new TypeException ( "Failed to cast from [%from_class%] [%from_object%] to [%to_class%]: Value out of bounds [%min%] - [%max%]",
                                      "from_class", this.fromClass (),
                                      "from_object", from,
                                      "to_class", this.toClass (),
                                      "min", Integer.MIN_VALUE,
                                      "max", Integer.MAX_VALUE );
        }

        return new Integer ( from.intValue () );
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
    public Class<Integer> toClass ()
    {
        return Integer.class;
    }
}
