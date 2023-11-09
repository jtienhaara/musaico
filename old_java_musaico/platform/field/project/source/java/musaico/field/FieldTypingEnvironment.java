package musaico.field;


import java.io.Serializable;


import musaico.types.RuntimeTypeException;
import musaico.types.Type;
import musaico.types.TypingEnvironment;


/**
 * <p>
 * All TypeSystem's and the TypeRegistry of all Types in
 * this system.  Also creates Fields.
 * </p>
 *
 *
 * <p>
 * In Java, every TypingEnvironment must be Serializable in order
 * to play nicely across RMI, even if the Instance values it covers
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
public interface FieldTypingEnvironment
    extends TypingEnvironment, Serializable
{
    /**
     * <p>
     * Creates a new Field from the specified id and value.
     * </p>
     *
     * @param id The name of the Field to create.  Need not be unique
     *           in any namespace.  Must not be null.
     * @param value The raw value from which to create a new Field.
     *              Must not be null.
     *
     * @throws RuntimeTypeException If the Field cannot be created,
     *                              and if and only if the
     *                              <code> exceptionHandler () </code>
     *                              throws RuntimeTypeExceptions.
     */
    public abstract 
        <RAW_VALUE extends Object,STORAGE_VALUE extends Object>
                           Field create (
                                         String id,
                                         RAW_VALUE value
                                         )
        throws RuntimeTypeException;



    /**
     * <p>
     * Creates a new Field of the specified id, raw class (such as
     * String.class, Integer.class, Foo.class, and so on) and value.
     * </p>
     *
     * <p>
     * For instance, to create a new Field of Foo:
     * </p>
     *
     * <pre>
     *     FieldTypingEnvironment environment = ...;
     *     Field foo_field = environment.create ( "foo", Foo.class, my_foo );
     * </pre>
     *
     * @param id The name of the new Field.  Need not be unique.
     *           Must not be null.
     * @param raw_class The class of value to use when looking up the
     *                  Type for the new Field.  Must not be null.
     * @param value The value of the new Field.  Must be an instance of
     *              the specified raw_class.  Must not be null.
     *
     * @throws RuntimeTypeException If the Field cannot be created,
     *                              and if and only if the
     *                              <code> exceptionHandler () </code>
     *                              throws RuntimeTypeExceptions.
     */
    public abstract 
        <RAW_VALUE extends Object,STORAGE_VALUE extends Object>
                           Field create (
                                         String id,
                                         Class<RAW_VALUE> raw_class,
                                         RAW_VALUE value
                                         )
        throws RuntimeTypeException;


    /**
     * <p>
     * Creates a new Field of the specified id, Type and value.
     * </p>
     *
     * <p>
     * For instance, to create a new Field of a FooType:
     * </p>
     *
     * <pre>
     *     FieldTypingEnvironment environment = ...;
     *     FooType foo_type = new FooType ();
     *     Field foo_field = environment.create ( "foo", foo_type, my_foo );
     * </pre>
     *
     * @param id The name of the new Field.  Need not be unique.
     *           Must not be null.
     * @param type The Type of the new Field.  Must be a type which
     *             is compatible with the raw class of the specified value.
     *             Must not be null.
     * @param value The raw value to use as the new Field's initial value.
     *              Must not be null.
     *
     * @return The new Field.  Never null.
     *
     * @throws RuntimeTypeException If the Field cannot be created,
     *                              and if and only if the
     *                              <code> exceptionHandler () </code>
     *                              throws RuntimeTypeExceptions.
     */
    public abstract 
        <RAW_VALUE extends Object,STORAGE_VALUE extends Object>
                           Field create (
                                         String id,
                                         Type<STORAGE_VALUE> type,
                                         RAW_VALUE value
                                         )
        throws RuntimeTypeException;


    /**
     * <p>
     * Duplicates the specified Field, returning a new
     * Field with the same id, Type, value, and so on.
     * </p>
     *
     * <p>
     * For example, to create a copy of a Field of a FooType:
     * </p>
     *
     * <pre>
     *     Field original_foo_field = ...;
     *     FieldTypingEnvironment environment = ...;
     *     Field copy_of_foo_field =
     *         environment.duplicateField ( original_foo_field );
     * </pre>
     *
     * @param field The Field to duplicate.  Must not be null.
     *
     * @return The new Field, identical in every way to the specified one.
     *         Never null.
     *
     * @throws RuntimeTypeException If the Field cannot be duplicated,
     *                              and if and only if the
     *                              <code> exceptionHandler () </code>
     *                              throws RuntimeTypeExceptions.
     */
    public abstract Field duplicateField (
                                          Field field
                                          )
        throws RuntimeTypeException;
}
