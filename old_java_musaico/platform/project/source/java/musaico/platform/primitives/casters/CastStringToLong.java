package musaico.types.primitive;


import java.io.Serializable;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from String to Long.
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
public class CastStringToLong
    implements TypeCaster<String,Long>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public Long cast (
                      String from
                      )
        throws TypeException
    {
        int radix = 10;
        String string_value = from;
        if ( from.length () > 2 )
        {
            // Maybe a prefix.
            String prefix = string_value.substring ( 0, 2 ).toLowerCase ();
            if ( prefix.equals ( "0x" )
                 || prefix.equals ( "&h" ) )
            {
                radix = 16;
                string_value = string_value.substring ( 2 );
            }
            else if ( prefix.equals ( "0o" )
                      || prefix.equals ( "&o" ) )
            {
                radix = 8;
                string_value = string_value.substring ( 2 );
            }
        }

        long long_value;
        try
        {
            long_value = Long.parseLong ( string_value, radix );
        }
        catch ( NumberFormatException nfe )
        {
            throw new TypeCastException ( "Failed to cast from [%from_class%] [%from_object%] to [%to_class%]: Invalid format",
                                          "from_class", this.fromClass (),
                                          "from_object", from,
                                          "to_class", this.toClass (),
                                          "cause", nfe );
        }

        return new Long ( long_value );
    }


    /**
     * @see musaico.types.TypeCaster#fromClass()
     */
    @Override
    public Class<String> fromClass ()
    {
        return String.class;
    }


    /**
     * @see musaico.types.TypeCaster#toClass()
     */
    @Override
    public Class<Long> toClass ()
    {
        return Long.class;
    }
}
