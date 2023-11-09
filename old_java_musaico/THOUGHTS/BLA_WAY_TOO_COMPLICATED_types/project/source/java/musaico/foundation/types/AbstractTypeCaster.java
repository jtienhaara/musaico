package musaico.types;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Base type caster, provides some of the boilerplate query
 * methods of type casters.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public abstract class AbstractTypeCaster<FROM extends Object, TO extends Object>
    implements TypeCaster<FROM,TO>, Serializable
{
    /** Cannot cast FROM this class to the to class. */
    private final Class<FROM> fromClass;

    /** Cannot cast from the from class TO this class. */
    private final Class<TO> toClass;


    /**
     * <p>
     * Creates a new AbstractTypeCaster which always casts from
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
    protected AbstractTypeCaster (
                                  Class<FROM> from_class,
                                  Class<TO> to_class
                                  )
    {
        if ( from_class == null
             || to_class == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a [%type_caster_class%] to cast from class [%from_class%] to raw class [%to_class%]",
                                                     "type_caster_class", this.getClass (),
                                                     "from_class", from_class,
                                                     "to_class", to_class );
        }

        this.fromClass = from_class;
        this.toClass = to_class;
    }


    // Every AbstractTypeCaster must implement cast().


    /**
     * <p>
     * Returns the raw class FROM which instances are cast to the
     * to class.
     * </p>
     *
     * @return The FROM raw class of this type caster.  Never null.
     */
    @Override
    public Class<FROM> fromClass ()
    {
        return this.fromClass;
    }


    /**
     * <p>
     * Returns the raw class TO which instances of the from class
     * are cast.
     * </p>
     *
     * @return The TO raw class of this type caster.  Never null.
     */
    @Override
    public Class<TO> toClass ()
    {
        return this.toClass;
    }
}
