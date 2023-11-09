package musaico.types;


import java.io.Serializable;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts values from a sub-class to a superclass or vice-versa
 * (doing nothing along the way).
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
 * Copyright (c) 2009, 2012 Johann Tienhaara
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
public class PassThroughTypeCaster<FROM extends Object,TO extends Object>
    implements TypeCaster<FROM, TO>, Serializable
{
    /** The class of objects to be cast. */
    private final Class<FROM> fromClass;

    /** The target class to cast objects to. */
    private final Class<TO> toClass;


    /**
     * <p>
     * Creates a new PassThroughTypeCaster which always casts from
     * and to instances of the specified raw classes.
     * </p>
     *
     * @param from_class The caster will cast FROM instances
     *                   of this raw class to the to class.
     *                   Must not be null.
     *
     * @param to_class The caster will cast from instances of
     *                 the from class TO instances of this raw class.
     *                 Must not be null.
     */
    public PassThroughTypeCaster (
                                  Class<FROM> from_class,
                                  Class<TO> to_class
                                  )
    {
        this.fromClass = from_class;
        this.toClass = to_class;
    }


   /**
     * @see musaico.types.TypeCaster#cast(FROM)
     */
    public TO cast (
                    FROM from
                    )
        throws TypeException
    {
        return (TO) from;
    }


    /**
     * @see musaico.types.TypeCaster#fromClass()
     */
    @Override
    public Class<FROM> fromClass ()
    {
        return this.fromClass;
    }


    /**
     * @see musaico.types.TypeCaster#toClass()
     */
    @Override
    public Class<TO> toClass ()
    {
        return this.toClass;
    }
}
