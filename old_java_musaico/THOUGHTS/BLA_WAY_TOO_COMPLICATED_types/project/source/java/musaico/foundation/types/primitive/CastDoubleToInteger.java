package musaico.types.primitive;


import java.io.Serializable;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from Double to Integer.
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
public class CastDoubleToInteger
    implements TypeCaster<Double,Integer>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public Integer cast (
                         Double from
                         )
        throws TypeException
    {
        int int_value = from.intValue ();
        if ( ( (double) int_value ) != from.doubleValue () )
        {
            // Can't cast if there are decimals.
            double precision_loss = from.doubleValue () - (double) int_value;
            throw new TypeException ( "Failed to cast from [%from_class%] [%from_object%] to [%to_class%]: Loss of precision [%precision_loss%]",
                                      "from_class", this.fromClass (),
                                      "from_object", from,
                                      "to_class", this.toClass (),
                                      "precision_loss", precision_loss );
        }

        return new Integer ( int_value );
    }


    /**
     * @see musaico.types.TypeCaster#fromClass()
     */
    @Override
    public Class<Double> fromClass ()
    {
        return Double.class;
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
