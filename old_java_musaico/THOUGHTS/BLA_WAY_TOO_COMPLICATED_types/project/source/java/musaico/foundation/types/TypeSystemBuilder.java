package musaico.types;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Represents a system of types, such as low-level, immutable,
 * idempotent primitives, or user-defined types, and so on.
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
public interface TypeSystemBuilder
    extends Serializable
{
    /**
     * <p>
     * Adds a Tag to the TypeSystem being built.
     * </p>
     *
     * @param tag The Tag to add to this type system.  Must not be null.
     *
     * @return This TypeSystemBuilder.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract TypeSystemBuilder add (
                                           Tag tag
                                           )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Adds a Type to the TypeSystem being built.
     * </p>
     *
     * @param type The Type to add to this type system.
     *             Must pass the validate () test,
     *             Must not be null.
     *
     * @return This TypeSystemBuilder.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract TypeSystemBuilder add (
                                           Type<Object> type
                                           )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Builds a new SimpleTypeSystem from the tags, types and so on
     * built up.
     * </p>
     *
     * @return The newly constructed SimpleTypeSystem.  Never null.
     */
    public abstract TypeSystem build ();


    /**
     * <p>
     * Returns the typing environment in which a type system
     * is being built.
     * </p>
     *
     * @return The TypingEnvironment of the type system being built.
     *         Never null.
     */
    public abstract TypingEnvironment environment ();


    /**
     * <p>
     * Creates a new type builder for the specified type name and
     * storage class.
     * </p>
     *
     * <p>
     * For example, the following creates a type builder that will
     * construct a type called "first_name" with String storage class.
     * </p>
     *
     * <pre>
     *     new SimpleTypeBuilder<String> ( "first_name", String.class,
     *                                     "NO_NAME" );
     * </pre>
     *
     * <p>
     * The following creates a type builder that will construct a
     * type called "age" with Integer storage class:
     * </p>
     *
     * <pre>
     *     new SimpleTypeBuilder<Integer> ( "age", Integer.class,
     *                                      Integer.MIN_VALUE );
     * </pre>
     *
     * @param name The name of the type to be built.  Must not be null.
     *
     * @param storage_class The storage class for the type to be
     *                      built.  Defines the class of value that
     *                      will be stored inside instances of the type.
     *                      Must not be null.
     *
     * @param none The "no value" value for the type to be built.
     *             An Instance whose raw value is equal to this
     *             none value is treated specially
     *             in certain cases, such as when querying the "next"
     *             or "previous" value from a data structure.
     *             Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract
        <STORAGE_VALUE extends Object>
            TypeBuilder<STORAGE_VALUE> prepare (
                                                String name,
                                                Class<STORAGE_VALUE> storage_class,
                                                STORAGE_VALUE none
                                                )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Removes the specified Tag from the TypeSystem being built.
     * </p>
     *
     * @param tag The Tag to remove from this type system.  Must not be null.
     *
     * @return This TypeSystemBuilder.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract TypeSystemBuilder remove (
                                              Tag tag
                                              )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Removes the specified Type from the TypeSystem being built.
     * </p>
     *
     * @param type The Type to remove from this type system.  Must not be null.
     *
     * @return This TypeSystemBuilder.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract TypeSystemBuilder remove (
                                              Type<Object> type
                                              )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Returns the tags for the system being built.
     * </p>
     *
     * @return The Tags for the type system being built, such
     *         as value constraints.
     *         Never null.
     */
    public abstract Tag [] tags ();


    /**
     * <p>
     * Returns the parent type system of the one being built.
     * </p>
     *
     * @return The parent of the TypeSystem under construction.
     *         Never null.
     */
    public abstract TypeSystem typeSystemParent ();


    /**
     * <p>
     * Returns all types for the system being built.
     * </p>
     *
     * @return The Types for the type system being built.
     *         Never null.
     */
    public abstract Type<Object> [] types ();


    /**
     * <p>
     * Validates the specified Type against the rules of
     * this type system builder.
     * </p>
     *
     * <p>
     * If any of this type system's rules for types are not
     * met, then a TypeException is thrown.
     * </p>
     *
     * <p>
     * This test must be passed by EVERY type before the TypeSystem
     * can be successfully built.
     * </p>
     *
     * @param type The type to validate.  Must not be null.
     *
     * @return This TypeSystemBuilder.  Never null.
     *
     * @throws TypeException If the specified type is invalid
     *                       for this type system, or if either
     *                       parameter (raw_class or type) is null.
     */
    public abstract
        <STORAGE_VALUE extends Object>
            TypeSystemBuilder validate (
                                        Type<STORAGE_VALUE> type
                                        )
        throws TypeException;
}
