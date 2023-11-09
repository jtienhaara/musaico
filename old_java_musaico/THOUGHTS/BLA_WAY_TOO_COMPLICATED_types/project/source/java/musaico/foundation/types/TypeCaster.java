package musaico.types;


import java.io.Serializable;


/**
 * <p>
 * Casts values from one Type to another.
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
public interface TypeCaster<FROM extends Object, TO extends Object>
    extends Serializable
{
    /**
     * <p>
     * Casts the specified "from" value to an instance of the "to" raw class.
     * </p>
     *
     * @param from The input to cast.  Must not be null.
     *
     * @return The freshly cast output.
     *
     * @throws TypeException If the input is incorrectly formatted
     *                       or for some reason cannot be typecast.
     *                       Typically a TypeCastException is thrown.
     */
    public abstract TO cast (
                             FROM from
                             )
        throws TypeException;


    /**
     * <p>
     * Returns the class from which this TypeCaster casts.
     * </p>
     *
     * @return The "from" class.  Never null.
     */
    public abstract Class<FROM> fromClass ();


    /**
     * <p>
     * Returns the class to which this TypeCaster casts.
     * </p>
     *
     * @return The "to" class.  Never null.
     */
    public abstract Class<TO> toClass ();
}
