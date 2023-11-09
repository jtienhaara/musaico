package musaico.foundation.io.types;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;


import musaico.foundation.hash.Hash;

import musaico.foundation.io.Reference;

import musaico.foundation.types.TypeCastException;
import musaico.foundation.types.TypeCaster;
import musaico.foundation.types.TypeException;

import musaico.foundation.types.primitive.types.AbstractCastToHash;
import musaico.foundation.types.primitive.types.CastStringToHash;


/**
 * <p>
 * Casts values from Reference to Hash.
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
public class CastReferenceToHash
    implements TypeCaster<Reference,Hash>, Serializable
{
    /** We cast to a String, then cast the string to a hash. */
    private final CastReferenceToString referenceToString =
        new CastReferenceToString ();

    /** We cast to a String, then cast the string to a hash. */
    private final CastStringToHash stringToHash =
        new CastStringToHash ();


    /**
     * @see musaico.foundation.types.TypeCaster#cast(FROM,Class)
     */
    public Hash cast (
                      Reference from,
                      Class to_class
                      )
        throws TypeException
    {
        String reference_string = this.referenceToString.cast ( from,
                                                                String.class );
        Hash reference_hash = this.stringToHash.cast ( reference_string,
                                                       Hash.class );

        return reference_hash;
    }
}
