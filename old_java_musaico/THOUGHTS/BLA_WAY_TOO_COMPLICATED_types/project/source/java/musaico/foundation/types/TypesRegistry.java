package musaico.types;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Registry of Type representations, by raw data type Class.
 * </p>
 *
 * <p>
 * For example, <code> StringType </code> is always registered as the Type
 * for <code> String.class </code>.
 * </p>
 *
 *
 * <p>
 * In Java, every TypesRegistry must be Serializable in order
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
public interface TypesRegistry
    extends Serializable
{
    /** No types at all. */
    public static final TypesRegistry NONE= new NoTypesRegistry ();


    /**
     * <p>
     * Registers the specified TypeSystem.
     * </p>
     *
     * @param type_system The type system whose Types will be
     *                    registered and mapped from their raw classes
     *                    (for example Integer.class and Float.class
     *                    to a NumberType, and so on).
     *                    ust not be null.
     *
     * @return This registry.  Never null.
     *
     * @throws I18nIllegalArgumentException If a raw class has already
     *                                      been registered to another
     *                                      type (such as String.class
     *                                      registered to a type, and
     *                                      the specified type system
     *                                      has another Type with
     *                                      raw class String.class).
     */
    public abstract TypesRegistry put (
                                       TypeSystem type_system
                                       )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Returns the raw classes mapped by this types registry.
     * </p>
     *
     * <p>
     * For example, a types registry with a "number" type might map
     * Integer, Float, BigDecimal and so on to that type.  In
     * this case the raw classes would be Integer.class, Float.class,
     * BigDecimal.class and so on.
     * </p>
     *
     * @return The raw classes mapped to types in this registry.
     *         Never null.  Never contains any null elements.
     */
    public abstract Class<?> [] rawClasses ();


    /**
     * <p>
     * Looks up a Type by the raw class it represents (such as
     * String.class, Integer.class, Foo.class, and so on).
     * </p>
     *
     * <p>
     * For example:
     * </p>
     *
     * <pre>
     *     TypesRegistry my_registry = ...;
     *     StringType string_type = my_registry.type ( String.class );
     * </pre>
     *
     * @param raw_class The raw class represented by the Type to lookup.
     *                  Must not be null.
     *
     * @return The Type instance representing the specified raw class,
     * *       or null if no Type represents the specified raw class.
     */
    public abstract
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
                           Type<STORAGE_VALUE> type (
                                                     Class<RAW_VALUE> raw_class
                                                     );


    /**
     * <p>
     * Returns all registered Types.
     * </p>
     *
     * @return All registered Types.  Never null.  Never contains
     *         any null elements.
     */
    public abstract Type<Object> [] types ();


    /**
     * <p>
     * Returns the type system containing a mapping from the specified
     * raw class to a Type.
     * </p>
     *
     * @return The TypeSystem containing the specified raw class,
     *         or null if none maps the specified raw class.
     */
    public abstract TypeSystem typeSystem (
                                           Class<?> raw_class
                                           );


    /**
     * <p>
     * Returns all registered TypeSystems.
     * </p>
     *
     * @return All registered TypeSystems.  Never null.  Never contains
     *         any null elements.
     */
    public abstract TypeSystem [] typeSystems ();
}
