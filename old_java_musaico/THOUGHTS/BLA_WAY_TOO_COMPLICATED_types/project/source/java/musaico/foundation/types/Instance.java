package musaico.types;


import musaico.foundation.condition.Conditional;


/**
 * <p>
 * Represents an instance of a Type.
 * </p>
 *
 * <p>
 * For example, text Strings, integer numbers, dates and times
 * are all primitive Instances; user defined types can be
 * Instances; and so on.
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
public interface Instance
{
    /**
     * <p>
     * Returns true if the default instance type is of the
     * specified Type class.
     * </p>
     *
     * @param type_class The Class of Types.  Must not be null.
     *
     * @return True if the instance's default Type is of the
     *         specified Class, false if it is of some other Type class.
     */
    public abstract boolean isa (
                                 Class<Type<?>> type_class
                                 );


    /**
     * <p>
     * Returns the Tags which apply to this particular Instance,
     * including Instance-specific Constraints.
     * </p>
     *
     * @return This Instance's Tags.  Never null.  Never contains
     *         any null elements.
     */
    public abstract Tag [] tags ();


    /**
     * <p>
     * Returns the Type used to create this Instance.
     * </p>
     *
     * @return The Type originally used to create this Instance,
     *         such as an <code> IntType </code> or a
     *         <code> StringType </code> and so on.
     */
    public abstract 
        <STORAGE_VALUE extends Object>
                               Type<STORAGE_VALUE> type ();


    /**
     * <p>
     * Validates this Instance against all of its Tags
     * and the Tags of its Type, TypeSystem and TypingEnvironment
     * (including all constraints).
     * </p>
     *
     * <p>
     * If any of the tags fails, then a TypeException is thrown.
     * </p>
     *
     * @throws TypeException If the instance is invalid against any
     *                       of the applicable tags (for the Instance
     *                       itself, its Type, its TypeSystem, its
     *                       TypingEnvironment).
     */
    public abstract void validate ()
        throws TypeException;


    /**
     * <p>
     * Returns the raw value of this Instance, without any casting.
     * </p>
     *
     * @return The raw data value of this instance.
     *         Never null.
     */
    public abstract Object value ();


    /**
     * <p>
     * Returns a Conditional value of this Instance using the specified
     * raw class (such as String.class, Integer.class, Foo.class,
     * and so on).
     * </p>
     *
     * <p>
     * If a dynamic cast is required, it is performed.
     * </p>
     *
     * <p>
     * Casting errors are stored in the resulting Conditional.
     * The caller can choose how to handle errors (by throwing
     * checked or unchecked/runtime exceptions, by returning
     * default values, and so on).
     * </p>
     *
     * @param raw_class The raw class to cast this instance as.
     *                  For example, <code> Integer.class </code>
     *                  or <code> String.class </code> and so on.
     *
     * @return The Conditional raw data value of this instance, cast
     *         to the specified raw class, or containing the TypeException
     *         which prevented the cast from succeeding (for example
     *         because there is no type for the specified raw
     *         class, and so on).  The caller can choose how to
     *         handle error conditions (by throwing checked or
     *         unchecked/runtime exceptions, by returning default
     *         values, and so on).  Never null.
     */
    public abstract
        <RAW_VALUE extends Object>
            Conditional<RAW_VALUE, TypeException>
                value (
                       Class<RAW_VALUE> raw_class
                       );


    /**
     * <p>
     * Returns a Conditional value of this Instance using the specified
     * Type and its storage raw class.
     * </p>
     *
     * <p>
     * If a dynamic cast is required, it is performed.
     * </p>
     *
     * <p>
     * Casting errors are stored in the resulting Conditional.
     * The caller can choose how to handle errors (by throwing
     * checked or unchecked/runtime exceptions, by returning
     * default values, and so on).
     * </p>
     *
     * @param type The type to cast this instance as.
     *             For example, an <code> IntType </code>
     *             or a <code> StringType </code> and so on.
     *
     * @return The Conditional raw data value of this instance, cast
     *         to the specified raw class, or containing the TypeException
     *         which prevented the cast from succeeding (for example
     *         because there is no typecaster from the internal raw
     *         value to the specified type, and so on).  The caller
     *         can choose how to handle error conditions (by
     *         throwing checked or unchecked/runtime exceptions,
     *         by returning default values, and so on).  Never null.
     */
    public abstract
        <STORAGE_VALUE extends Object>
            Conditional<STORAGE_VALUE, TypeException>
                value (
                       Type<STORAGE_VALUE> type
                       );


    /**
     * <p>
     * Returns a Conditional value of this Instance using the specified
     * Type and raw class.
     * </p>
     *
     * <p>
     * If a dynamic cast is required, it is performed.
     * </p>
     *
     * <p>
     * Casting errors are stored in the resulting Conditional.
     * The caller can choose how to handle errors (by throwing
     * checked or unchecked/runtime exceptions, by returning
     * default values, and so on).
     * </p>
     *
     * @param raw_class The specific raw class to cast the value as.
     *                  For example, <code> String.class </code> or
     *                  <code> Integer.class </code> or
     *                  <code> Long.class </code> and so on.
     * @param type The type to cast this instance as.
     *             For example, an <code> IntType </code>
     *             or a <code> StringType </code> and so on.
     *
     * @return The Conditional raw data value of this instance, cast
     *         to the specified raw class, or containing the TypeException
     *         which prevented the cast from succeeding (for example
     *         because there is no type for the specified raw
     *         class, and so on).  The caller can choose how to
     *         handle error conditions (by throwing checked or
     *         unchecked/runtime exceptions, by returning default
     *         values, and so on).  Never null.
     */
    public abstract
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Conditional<RAW_VALUE, TypeException>
                value (
                       Class<RAW_VALUE> raw_class,
                       Type<STORAGE_VALUE> type
                       );
}
