package musaico.types;


import java.io.Serializable;


/**
 * <p>
 * Registry of type casters, to cast from one type to another.
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
 * In Java, every TypeCastersRegistry must be Serializable in order
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
public interface TypeCastersRegistry
    extends Serializable
{
    /** No type casters at all. */
    public static final TypeCastersRegistry NONE =
        new NoTypeCastersRegistry ();


    /**
     * <p>
     * Returns the set of type casters from the specified type.
     * </p>
     *
     * @return the TypeCastersFrom the specified type.
     *         Never null.  Returns an empty TypeCastersFrom if
     *         no such type casters yet exist.  The resulting
     *         TypeCastersFrom may be modified.
     */
    public abstract
        <FROM extends Object>
                      TypeCastersFrom<FROM> from (
                                                  Type<FROM> from_type
                                                  );


    /**
     * <p>
     * Returns the TypeCaster which casts from storage values of
     * the specified "from" type to storage values of the
     * specified "to" type.
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
     *     TypeCaster<?,String> caster =
     *         my_registry.get ( foo_type, string_type );
     *     String foo_as_string =
     *         caster.cast ( my_foo_instance.getValue (), String.class );
     * </pre>
     *
     * @param from_type The type from which the TypeCaster will cast.
     *
     * @param to_type The type to which the TypeCaster will cast.
     *
     * @return The TypeCaster which casts from storage values of
     *         type from_type to storage values of type to_type.
     *         Can be null if no such type caster is registered.
     */
    public abstract
        <FROM extends Object,TO extends Object>
            TypeCaster<FROM,TO> get (
                                     Type<FROM> from_type,
                                     Type<TO> to_type
                                     );


    /**
     * <p>
     * Registers the specified TypeCaster to cast from storage values
     * of the specified from_type to storage values of the specified
     * to_type.
     * </p>
     *
     * <p>
     * For example, to register a TypeCaster which casts from
     * FooTypes to StringTypes:
     * </p>
     *
     * <pre>
     *     TypeCastersRegistry registry = ...;
     *     Type<Foo> foo_type = ...;
     *     Type<String> string_type = ...;
     *     TypeCaster<Foo,String> cast_foo_to_string = ...;
     *     registry.put ( foo_type, string_tye, cast_foo_to_string );
     * </pre>
     *
     * <p>
     * Any exceptions (such as trying to register two TypeCasters for
     * a given from_type and to_type pair) will be thrown as TypeExceptions.
     * </p>
     *
     * @pram from_type The Type from which to cast.
     *
     * @param to_type The Type to which to cast.
     *
     * @param type_caster The TypeCaster which will perform the
     *                    specified casts.
     *
     * @return This registry.  Never null.
     *
     * @throws TypeException If a TypeCaster has already been registered
     *                       for the specified from_type and to_type pair.
     */
    public abstract
        <FROM extends Object,TO extends Object>
            TypeCastersRegistry put (
                                     Type<FROM> from_type,
                                     Type<TO> to_type,
                                     TypeCaster<FROM,TO> type_caster
                                     )
        throws TypeException;


    /**
     * <p>
     * Returns the set of type casters to the specified type.
     * </p>
     *
     * @return the TypeCastersTo the specified type.
     *         Never null.  Returns an empty TypeCastersTo if
     *         no such type casters yet exist.  The resulting
     *         TypeCastersTo may be modified.
     */
    public abstract
        <TO extends Object>
                      TypeCastersTo<TO> to (
                                            Type<TO> to_type
                                            );


    /**
     * <p>
     * Returns the set of all registered TypeCasters.
     * </p>
     *
     * @return The set of all registered TypeCasters.  Never null.
     */
    public abstract TypeCaster<Object,Object> [] typeCasters ();
}
