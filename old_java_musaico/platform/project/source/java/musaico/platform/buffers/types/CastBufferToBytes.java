package musaico.buffer.types;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.buffer.Buffer;

import musaico.field.Field;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts Buffers byte arrays by casting each constituent Field to
 * a byte array then merging them all.
 * </p>
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
 * Copyright (c) 2009, 2011 Johann Tienhaara
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
public class CastBufferToBytes
    extends AbstractBufferCaster<byte[],List<byte[]>>
    implements Serializable
{
    /**
     * @see musaico.buffer.types.AbstractBufferCaster#add(java.lang.Object,java.lang.Object)
     */
    @Override
    protected List<byte[]> add (
                                byte [] element,
                                List<byte[]> cast_under_construction
                                )
    {
        if ( cast_under_construction == null )
        {
            cast_under_construction = new ArrayList<byte[]> ();
        }

        cast_under_construction.add ( element );

        return cast_under_construction;
    }


    /**
     * @see musaico.buffer.types.AbstractBufferCaster#build(java.lang.Object)
     */
    @Override
    protected byte [] build (
                             List<byte[]> cast_under_construction
                             )
    {
        int total_length = 0;
        for ( byte [] chunk : cast_under_construction )
        {
            total_length += chunk.length;
        }

        byte [] result = new byte [ total_length ];
        int b = 0;
        for ( byte [] chunk : cast_under_construction )
        {
            System.arraycopy ( chunk, 0, result, b, chunk.length );
        }

        return result;
    }
}
