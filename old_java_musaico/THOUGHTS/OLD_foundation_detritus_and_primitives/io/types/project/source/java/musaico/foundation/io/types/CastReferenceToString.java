package musaico.foundation.io.types;


import java.io.Serializable;

import musaico.foundation.io.SoftReference;
import musaico.foundation.io.Reference;

import musaico.foundation.types.TypeCastException;
import musaico.foundation.types.TypeCaster;
import musaico.foundation.types.TypeException;


/**
 * <p>
 * Casts SoftReferences&lt;String&gt;s to Strings.
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
public class CastReferenceToString
    implements TypeCaster<Reference,String>, Serializable
{
    /**
     * @see musaico.foundation.types.TypeCaster#cast(FROM,Class)
     */
    public String cast (
                        Reference from,
                        Class to_class
                        )
        throws TypeException
    {
        // Just throw a TypeException if anything goes wrong.
        try
        {
            SoftReference<String> string_ref = (SoftReference<String>) from;
            String string_value = string_ref.id ();
            return string_value;
        }
        catch ( Exception e )
        {
            throw new TypeException ( "Cannot cast from Reference [%reference%] to [%cast_to_class%]: must be a [%reference_class%]",
                                      "reference", from,
                                      "cast_to_class", String.class.getName (),
                                      "reference_class", "SoftReference<String>",
                                      "cause", e );
        }
    }
}
