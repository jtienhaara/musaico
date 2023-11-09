package musaico.buffer.types;


import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.field.Field;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts Buffers Strings by casting each constituent Field to
 * a String then appending them all.
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
public class CastBufferToString
    extends AbstractBufferCaster<String,StringBuilder>
    implements Serializable
{
    /**
     * @see musaico.buffer.types.AbstractBufferCaster#add(java.lang.Object,java.lang.Object)
     */
    @Override
    protected StringBuilder add (
                                 String element,
                                 StringBuilder cast_under_construction
                                 )
    {
        if ( cast_under_construction == null )
        {
            cast_under_construction = new StringBuilder ();
        }

        cast_under_construction.append ( element );

        return cast_under_construction;
    }


    /**
     * @see musaico.buffer.types.AbstractBufferCaster#build(java.lang.Object)
     */
    @Override
    protected String build (
                            StringBuilder cast_under_construction
                            )
    {
        return cast_under_construction.toString ();
    }
}
