package musaico.types;


import java.io.Serializable;


import musaico.condition.Conditional;

import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * All TypeSystem's and the TypeRegistry of all Types in
 * this system.
 * </p>
 *
 *
 * <p>
 * In Java, the TypingEnvironment must be Serializable in order
 * to play nicely across RMI, even if the Instances it covers
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
public interface TypingEnvironment
    extends Serializable
{
    /** No typing environment at all. */
    public static final TypingEnvironment NONE = new NoTypingEnvironment ();


    /**
     * <p>
     * Casts the specified Instance to the specified Type,
     * and returns a Conditional result as the requested class.
     * </p>
     *
     * <p>
     * If the cast succeeds, then the result can be retrieved from
     * the Conditional result.  If it fails then the caller has
     * the option of throwing the failure exception directly, or
     * an unchecked (runtime) exception, or returning a default
     * value, and so on.
     * </p.
     *
     * <p>.
     * Typically the type system uses this method internally,
     * there is no need to call it externally.  Use the various
     * Instance.value ( ... ) methods instead.
     * However you can call this method if you want.
     * </p>
     *
     * <p>
     * For example, to cast an Instance of FooType to StringType using
     * this method, call:
     * </p>
     *
     * <pre>
     *     TypingEnvironment typing = ...;
     *     Instance instance = ...;
     *     Type<String> string_type = typing.typeOf ( String.class );
     *     String as_string =
     *         typing.cast ( instance, string_type, String.class )
     *               .valueOrChecked (); // String value or TypeException.
     * </pre>
     *
     * @param instance The instance whose internal value will be cast.
     *                 Need not be an instance created in this environment,
     *                 but its internal value must have a Type equivalent
     *                 in this environment.  Must not be null.
     *
     * @param type The type to cast to.  Must be a type in this environment.
     *             Must not be null.
     *
     * @param to_class The raw class to cast to.  For example, when casting
     *                 to a "number" type you might choose between casting
     *                 to Integer.class or Double.class.
     *                 Must not be null.
     *
     * @return A Conditional result.  On success, the requested cast value
     *         will be returned by the Conditional.  However if the cast
     *         failed for any reason (because, for example, there is
     *         no type caster from the specified instance's raw value to
     *         the requested output raw value), then the caller can decide
     *         how to deal with the failure (by throwing a checked exception,
     *         by throwing a runtime exception, by defaulting to a certain
     *         value and so on).  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      specified is null.  Other
     *                                      types of errors will be contained
     *                                      in the Conditional result.
     */
    public abstract 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Conditional< RAW_VALUE, TypeException >
                cast (
                      Instance instance,
                      Type<STORAGE_VALUE> type,
                      Class<RAW_VALUE> to_class
                      )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Creates and prepares a new InstanceBuilder which will build
     * an Instance of some Type with the specified value.
     * </p>
     *
     * @param value The raw value from which to create a new Instance.
     *              Must not be null.
     *
     * @return A Conditional InstanceBuider: either an InstanceBuilder
     *         which will build a new Instance, or a TypeException
     *         if no instance can be built (for example because the
     *         raw class of the specified value does not belong to any type).
     *         Never null.
     */
    public abstract 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Conditional< InstanceBuilder, TypeException >
                prepare (
                         RAW_VALUE value
                         );


    /**
     * <p>
     * Creates and prepares a new InstanceBuilder which will create
     * an instance of the specified raw class (such as
     * String.class, Integer.class, Foo.class, and so on) and raw value.
     * </p>
     *
     * <p>
     * For instance, to create a new numeric Instance:
     * </p>
     *
     * <pre>
     *     TypingEnvironment types = ...;
     *     Instance a_42 = types.prepare ( Number.class, new Integer(42) )
     *                          .build ();
     * </pre>
     *
     * @param raw_class The class of value to use when looking up the
     *                  Type for the new InstanceBuilder.  Must not be null.
     *
     * @param value The value for the new Instance to be built.
     *              Must be an instance of the specified raw_class.
     *              Must not be null.
     *
     * @return A Conditional InstanceBuider: either an InstanceBuilder
     *         which will build a new Instance, or a TypeException
     *         if no instance can be built (for example because the
     *         raw class of the specified value does not belong to any type).
     *         The caller can decide how to deal with failure (by
     *         throwing a checked or unchecked/runtime exception,
     *         by returning InstanceBuilder.NONE, and so on).
     *         Never null.
     */
    public abstract 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Conditional< InstanceBuilder, TypeException >
                prepare (
                         Class<RAW_VALUE> raw_class,
                         RAW_VALUE value
                         );


    /**
     * <p>
     * Creates and prepares a new InstanceBuilder which will build an
     * Instance of the specified Type and value.
     * </p>
     *
     * <p>
     * For instance, to create a new numeric Instance:
     * </p>
     *
     * <pre>
     *     TypingEnvironment types = ...;
     *     Instance a_42 = types.prepare ( Number.class, new Integer ( 42 ) )
     *                          .build ();
     * </pre>
     *
     * @param type The Type of the Instance being prepared.
     *             Must not be null.
     *
     * @param value The raw value to prepare as the eventual Instance's
     *              value.  Must be an instance of a class compatible
     *              with the specified type (either an instance of
     *              the type's <code> storageClass () </code> or
     *              an instance of one of its internally castable
     *              storage classes; for example if a URI type then
     *              the value must either be a URI or an instance of
     *              a class which the type knows how to cast to a URI,
     *              say a URL).
     *              Must not be null.
     *
     * @return A Conditional InstanceBuider: either an InstanceBuilder
     *         which will build a new Instance, or a TypeException
     *         if no instance can be built (for example because the
     *         raw class of the specified value does not belong to
     *         the specified type, or the specified type is not from
     *         this typing environment, and so on).
     *         The caller can decide how to deal with failure (by
     *         throwing a checked or unchecked/runtime exception,
     *         by returning InstanceBuilder.NONE, and so on).
     *         Never null.
     */
    public abstract 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Conditional< InstanceBuilder, TypeException >
                prepare (
                         Type<STORAGE_VALUE> type,
                         RAW_VALUE value
                         );


    /**
     * <p>
     * Duplicates the specified Instance, returning a new
     * InstanceBuilder which can build an Instance with the same Type,
     * value, tags and so on.
     * </p>
     *
     * <p>
     * For example, to create a copy of an Instance of a FooType:
     * </p>
     *
     * <pre>
     *     Instance original_foo = ...;
     *     TypeSystem type_system = ...;
     *     Instance copy_of_foo = type_system.duplicate ( original_foo )
     *                                       .build ();
     * </pre>
     *
     * @param instance The Instance to duplicate.  Must not be null.
     *
     * @return A Conditional InstanceBuider: either an InstanceBuilder
     *         which will build a new duplicate of the specified
     *         Instance, or a TypeException if no instance can
     *         possibly be built (for example because the type of the
     *         specified instance is not present in this environment).
     *         Never null.
     */
    public abstract
            Conditional< InstanceBuilder, TypeException >
                duplicate (
                           Instance instance
                           );
 

    /**
     * <p>
     * Registers the specified type system, so that all its
     * types are available in this environment.
     * </p>
     *
     * @param type_system The type system to register.
     *                    Must not map any raw classes that have
     *                    already been mapped to types in this
     *                    typing environment (such as
     *                    Integer to a numeric Type).
     *                    Must not be null.
     *
     * @return This typing environment.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract TypingEnvironment register (
                                                TypeSystem type_system
                                                )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Returns the root TypeSystem of the hierarchy of
     * this TypingEnvironment.
     * </p>
     *
     * <p>
     * Typically the root TypeSystem does not contain
     * any types or constraints.  Its sole role is
     * to bear children (such as a "primitives" type
     * system, a "user defined types" system, and so on).
     * </p>
     *
     * @return The root TypeSystem.  Never null.
     */
    public abstract TypeSystem root ();


    /**
     * <p>
     * Returns all Tags which apply to Instances in this TypingEnvironment.
     * </p>
     *
     * @return The tags applicable to all Instances in this typing
     *         environment.  Never null.  Never contains
     *         any null elements.
     */
    public abstract Tag [] tags ();


    /**
     * <p>
     * Returns all Tags which apply to the specified Instance
     * in this TypingEnvironment, including the Instance's
     * own Tags, its Type, the TypeSystem to which the Type
     * belongs, and so on.
     * </p>
     *
     * <p>
     * Note that this method can be invoked even on an Instance
     * which does not belong to this TypingEnvironment.
     * </p>
     *
     * @param instance The Instance whose Tags will be returned.
     *                 Must not be null.
     *
     * @return The tags applicable to the specified Instance in
     *         this environment.  Never null.  Never contains
     *         any null elements.
     */
    public abstract Tag [] tags (
                                 Instance instance
                                 );


    /**
     * <p>
     * Looks up a Type in the registry by its raw class.
     * </p>
     *
     * <p>
     * Shorthand for:
     * </p>
     *
     * <pre>
     *     TypingEnvironment typing = ...;
     *     Type<String> string_type =
     *         typing.registry ().typeOf ( String.class );
     * </pre>
     *
     * @param raw_class The raw class represented by the Type to lookup.
     *                  Must not be null.
     *
     * @return The Type instance for the specified raw class, or null
     *         if no such Type has been registered for the specified
     *         raw class.
     */
    public abstract
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Type<STORAGE_VALUE> type (
                                      Class<RAW_VALUE> raw_class
                                      );


    /**
     * <p>
     * Returns the TypeSystem housing the Type mapped to by
     * the specified raw class in this typing environment.
     * </p>
     *
     * <p>
     * For example, to find out which type system String.class
     * belongs to in this environment (if any):
     * </p>
     *
     * <pre>
     *     TypeSystem system = environment.typeSystem ( String.class );
     * </pre>
     *
     * @param raw_class The raw class whose type system will
     *                   be returned.  Must not be null.
     *
     * @return The TypeSystem housing the specified class of Type
     *         in this environment, or null if no such raw class
     *         has been registered.
     */
    public abstract
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            TypeSystem typeSystem (
                                   Class<RAW_VALUE> raw_class
                                   );


    /**
     * <p>
     * Returns all the Types currently registered in this typing environment.
     * </p>
     *
     * @return All registered Types.  Never null.  Never contains any
     *         null elements.
     */
    public abstract Type<Object> [] types ();
}
