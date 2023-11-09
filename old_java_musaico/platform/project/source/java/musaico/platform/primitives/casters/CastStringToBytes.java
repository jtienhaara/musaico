package musaico.types.primitive;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;


import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from String to byte array.
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
public class CastStringToBytes
    implements TypeCaster<String,byte[]>, Serializable
{
    /** The character encoding for all String casting which involves
     *  byte representations of strings. */
    public static final String STRING_ENCODING = "UTF-8";


    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public byte [] cast (
                         String from
                         )
        throws TypeException
    {
        final byte [] as_bytes;
        try
        {
            as_bytes = from.getBytes ( CastStringToBytes.STRING_ENCODING );
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

        return as_bytes;
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
    public Class<byte[]> toClass ()
    {
        return byte[].class;
    }
}
