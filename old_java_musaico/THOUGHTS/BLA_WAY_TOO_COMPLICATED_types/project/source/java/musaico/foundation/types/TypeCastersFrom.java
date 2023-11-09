package musaico.types;


import java.io.Serializable;


/**
 * <p>
 * Set of type casters from a specific type to other types.
 * </p>
 *
 * <p>
 * For example, a <code> FooType </code> might have a TypeCaster
 * which casts it to a <code> StringType </code>.  The reverse
 * might also be true, so that a <code> StringType </code> can be
 * dynamically cast as (parsed into) a <code> FooType </code>.
 * </p>
 *
 *
 * <p>
 * In Java, every TypeCastersFrom must be Serializable in order
 * to play nicely across RMI, even if the raw values it casts between
 * are not themselves Serializable.
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
public interface TypeCastersFrom<STORAGE_VALUE extends Object>
    extends Serializable
{
    /**
     * <p>
     * Returns the TypeCaster which casts from storage values of
     * this set's type to storage values of the specified "to" type.
     * </p>
     *
     * <p>
     * For example, the following code might be used to cast an
     * Instance of a FooType to a String:
     * </p>
     *
     * <pre>
     *     TypeCastersRegistry my_registry = ...;
     *     FooType foo_type = ...;
     *     StringType string_type = ...;
     *     Instance my_foo_instance = ...;
     *     TypeCastersFrom<?> from_foo = my_registry.from ( foo_type );
     *     TypeCaster<?,String> caster =
     *         from_foo.to ( string_type );
     *     String foo_as_string =
     *         caster.cast ( my_foo_instance.getValue (), String.class );
     * </pre>
     *
     * @param to_type The type to which the TypeCaster will cast.
     *
     * @return The TypeCaster which casts from storage values of
     *         this set's type to storage values of the specified to_type.
     *         Can be null if no such type caster is registered.
     */
    public abstract
        <TO extends Object>
        TypeCaster<STORAGE_VALUE,TO> to (
                                         Type<TO> to_type
                                         );


    /**
     * <p>
     * Returns the "from" type for this set of type casters.
     * </p>
     *
     * @return This type casters set's "from" type.
     *         Never null.
     */
    public abstract Type<STORAGE_VALUE> type ();


    /**
     * <p>
     * Returns the set of all registered TypeCasters
     * from this set's type.
     * </p>
     *
     * @return The set of all registered TypeCasters from
     *         this set's type.  Never null.
     */
    public abstract TypeCaster<STORAGE_VALUE,Object> [] typeCasters ();
}
