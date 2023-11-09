package musaico.foundation.io.types;


import java.io.Serializable;

import musaico.foundation.io.SoftReference;
import musaico.foundation.io.Reference;

import musaico.foundation.types.TypeCastException;
import musaico.foundation.types.TypeCaster;
import musaico.foundation.types.TypeException;


/**
 * <p>
 * Casts SoftReference<Long>s to long values.
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
public class CastReferenceToLong
    implements TypeCaster<Reference,Long>, Serializable
{
    /**
     * @see musaico.foundation.types.TypeCaster#cast(FROM,Class)
     */
    public Long cast (
                      Reference from,
                      Class to_class
                      )
        throws TypeException
    {
        // If anything goes wrong, just throw a TypeException.
        try
        {
            SoftReference<Long> long_ref = (SoftReference<Long>) from;
            Long long_value = long_ref.id ();
            return long_value;
        }
        catch ( Exception e )
        {
            throw new TypeException ( "Cannot cast from Reference [%reference%] to [%cast_to_class%]: must be a [%reference_class%]",
                                      "reference", from,
                                      "cast_to_class", Long.class.getName (),
                                      "SoftReference<Long>",
                                      "cause", e );
        }
    }
}
