package musaico.types;


import java.io.Serializable;


/**
 * <p>
 * Always throws a TypeException when trying to cast from
 * type FROM to type TO.
 * </p>
 *
 * <p>
 * For example, the TypeCaster for transforming StringType
 * into IntType might rely on <code> Integer.parseInt () </code>
 * (re-rolling NumberFormatExceptions as TypeCastExceptions).
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
public class NoTypeCaster<FROM extends Object, TO extends Object>
    extends AbstractTypeCaster<FROM,TO>
    implements Serializable
{
    /**
     * <p>
     * Creates a new NoTypeCaster which always fails to cast from
     * and to instances of the specified raw classes.
     * </p>
     *
     * <p>
     * For example, to create type casters which always fails to
     * cast from a Date to various Numbers:
     * </p>
     *
     * <pre>
     *     new NoTypeCaster<Date,Integer> ( Date.class, Integer.class );
     *     new NoTypeCaster<Date,Float> ( Date.class, Float.class );
     *     ...etc...
     * </pre>
     *
     * <p>
     * Or to create a type caster which always fails to cast from
     * any Number to a Date:
     * </p>
     *
     * <pre>
     *     new NoTypeCaster<Number,Date> ( Number.class, Date.class );
     * </pre>
     *
     * @param from_class The caster will fail to cast FROM instances
     *                   of this raw class to the to class.
     *                   Must not be null.
     *
     * @param to_class The caster will fail to cast from instances of
     *                 the from class TO instances of this raw class.
     *                 Must not be null.
     */
    public NoTypeCaster (
                         Class<FROM> from_class,
                         Class<TO> to_class
                         )
    {
        super ( from_class, to_class );
    }


    /**
     * <p>
     * Always fails with a TypeCastException.
     * </p>
     *
     * @see musaico.types.TypeCaster.
     */
    public TO cast (
                    FROM from
                    )
        throws TypeException
    {
        throw new TypeCastException ( "No way of casting from [%from_class%] [%from_object%] to class [%to_class%]",
                                      "from_class", this.fromClass (),
                                      "from_object", from,
                                      "to_class", this.toClass () );
    }
}
