package musaico.types.primitive;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;


import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from byte array to String.
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
public class CastBytesToString
    implements TypeCaster<byte[],String>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public String cast (
                        byte [] from
                        )
        throws TypeException
    {
        final String string_value;
        try
        {
            string_value =
                new String ( from, CastStringToBytes.STRING_ENCODING );
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new TypeCastException ( "Failed to cast from [%from_class%] [%from_object%] to [%to_class%]: Unsupported text encoding [%encoding%]",
                                          "from", from,
                                          "from_class", this.fromClass (),
                                          "to_class", this.toClass (),
                                          "encoding", CastStringToBytes.STRING_ENCODING,
                                          "cause", e );
        }

        return string_value;
    }


    /**
     * @see musaico.types.TypeCaster#fromClass()
     */
    @Override
    public Class<byte[]> fromClass ()
    {
        return byte[].class;
    }


    /**
     * @see musaico.types.TypeCaster#toClass()
     */
    @Override
    public Class<String> toClass ()
    {
        return String.class;
    }
}
