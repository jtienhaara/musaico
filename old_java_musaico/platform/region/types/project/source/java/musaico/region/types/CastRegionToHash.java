package musaico.region.types;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;


import musaico.hash.Hash;

import musaico.region.Region;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;

import musaico.types.primitive.types.AbstractCastToHash;
import musaico.types.primitive.types.CastStringToHash;


/**
 * <p>
 * Hashes Regions into Hash objects.
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
public class CastRegionToHash
    implements TypeCaster<Region,Hash>, Serializable
{
    /** We cast to a String, then cast the string to a hash. */
    private final CastRegionToString regionToString =
        new CastRegionToString ();

    /** We cast to a String, then cast the string to a hash. */
    private final CastStringToHash stringToHash =
        new CastStringToHash ();


    /**
     * @see musaico.types.TypeCaster#cast(FROM,Class)
     */
    public Hash cast (
                      Region from,
                      Class to_class
                      )
        throws TypeException
    {
        String region_string = this.regionToString.cast ( from,
                                                          String.class );
        Hash region_hash = this.stringToHash.cast ( region_string,
                                                    to_class );

        return region_hash;
    }
}
