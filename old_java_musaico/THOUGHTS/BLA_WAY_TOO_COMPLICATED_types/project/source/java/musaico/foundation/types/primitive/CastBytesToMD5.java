package musaico.types.primitive;


import java.io.Serializable;


import musaico.hash.HashException;
import musaico.hash.MD5;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts byte[] arrays to MD5 hashes.
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
 * Copyright (c) 2009, 2011, 2012 Johann Tienhaara
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
public class CastBytesToMD5
    implements TypeCaster<byte[], MD5>, Serializable
{
    /**
     * @see musaico.types.TypeCaster#cast(java.lang.Object)
     */
    public MD5 cast (
                     byte [] from
                     )
        throws TypeException
    {
        try
        {
            return new MD5 ( from );
        }
        catch ( HashException hex )
        {
            throw new TypeCastException ( "Failed to cast from [%from_class%] [%from_object%] to [%to_class%]",
                                          "from", from,
                                          "from_class", this.fromClass (),
                                          "to_class", this.toClass (),
                                          "cause", hex );
        }
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
    public Class<MD5> toClass ()
    {
        return MD5.class;
    }
}
