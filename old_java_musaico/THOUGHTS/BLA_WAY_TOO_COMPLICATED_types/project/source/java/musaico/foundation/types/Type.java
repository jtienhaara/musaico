package musaico.types;


import java.io.Serializable;


import musaico.foundation.condition.Conditional;


/**
 * <p>
 * Cross-platform, dynamic type.
 * </p>
 *
 * <p>
 * In Java, all Type implementations must be Serializable
 * to play nicely across RMI, even if the Instances they
 * represent are not Serializable.
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
public interface Type<STORAGE_VALUE extends Object>
    extends Tag, Serializable
{
    /** The "no type" type, essentially void.  It should never be
     *  castable to or from any other type in a typing environment. */
    public static final Type NONE =
        new SimpleTypeBuilder<Object> ( "void", Object.class,
                                        new String ( "void" ) ).build ();


    // Every Type must implement the method:
    // @see musaico.types.Tag#check(Instance)


    /**
     * <p>
     * Returns true if this type is of the specified Type class.
     * </p>
     *
     * @param type_class The Class of Types.  Must not be null.
     *
     * @return True if this Type is of the specified Class,
     *         false if it is of some other Type class.
     */
    public abstract boolean isa (
                                 Class<Type<?>> type_class
                                 );


    /**
     * <p>
     * Returns the universal name for this Type.
     * </p>
     *
     * <p>
     * Even if the type name is only meaningful in one locale,
     * it remains constant across all other locales to ensure
     * consistency of program code and ease of writing technical
     * documentation.
     * </p>
     *
     * @return The universal name of this type, such as "car"
     *         or "truck" or "bank_account" and so on.  Never null.
     */
    public abstract String name ();


    /**
     * <p>
     * Returns the "no such value" value for this type.
     * <p>
     *
     * <p>
     * The "no value" value can be used when querying the
     * "next" or "previous" value of a data structure, for example.
     * </p>
     *
     * @return This type's no such value.  Never null.
     */
    public abstract STORAGE_VALUE none ();


    /**
     * <p>
     * Returns all raw classes supported by this Type.
     * </p>
     *
     * <p>
     * For example, a Type with storage class Number.class might
     * support raw classes Float.class, BigDecimal.class, Long.class
     * and so on.  The raw classes are used to lookup concrete instances
     * during instance creation and casting.
     * </p>
     *
     * @return This Type's supported raw classes.  Never null.
     *         Never contains any null elements.
     */
    public abstract Class<? extends STORAGE_VALUE> [] rawClasses ();


    /**
     * <p>
     * Returns the class of raw values stored in Instances of this
     * Type.
     * </p>
     *
     * <p>
     * For example, a <code> Type&lt;String&gt; </code> will
     * return <code> String.class </code> as its storage class.
     * </p>
     *
     * <p>
     * This is the most generalized class handled by this
     * Type.  For instance, a NumberType might return Number
     * as its storage class; but it would probably be registered
     * in the TypeSystem's under Number, Long, Integer, Float,
     * Double, and so on.  The NumberType itself could work
     * with any Number-derived objects, but in order to look
     * it up in the registry, every specific sub-class would
     * be registered individually.
     * </p>
     *
     * @return This Type's storage class.  Never null.
     */
    public abstract Class<STORAGE_VALUE> storageClass ();


    /**
     * <p>
     * Returns the tags which apply to all Instances of
     * this Type.
     * </p>
     *
     * <p>
     * Note that each Instance may also implement instance-specific
     * Tags (including Constraints), TypeSystems may implement
     * Tags for all or some Types in each system, and each TypingEnvironment
     * may also implement Tags to check Instances.
     * </p>
     *
     * @return A copy of the Tags which apply to all instances
     *         of this Type.  The caller can safely manipulate
     *         the returned array of Tags, since the internal
     *         storage is never exposed.
     *         Can be empty.  Never null.  Never
     *         contains any null elements.
     */
    public abstract Tag [] tags ();


    /**
     * <p>
     * Returns a Conditional TypeCaster which will cast the
     * specified raw value of this type to the specified target class
     * (possibly of another type).
     * </p>
     *
     * @param raw_value The raw value of this Type which will be cast
     *                  to some other class.  Must not be null.
     *                  If the specified raw value is not a raw value
     *                  of this type then the Conditional result will
     *                  be an error.
     *
     * @param to_class The target class for the TypeCaster to be
     *                 returned, such as another raw class in this
     *                 Type or a raw class from another Type.
     *                 Must not be null.
     *
     * @return A Conditional type caster, which will return
     *         the requested type caster on success.  If there is no such
     *         type caster the caller has the option of retrieving
     *         or throwing a checked or unchecked (runtime)
     *         TypeException, or retrieving a default value, and so on.
     *         Typically the "none" value of the Conditional is a
     *         NoTypeCaster.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract <FROM extends STORAGE_VALUE, TO extends Object>
        Conditional< TypeCaster<FROM, TO>, TypeException >
            typeCasterFromThisType (
                                    FROM raw_value,
                                    Class<TO> to_class
                                    );


    /**
     * <p>
     * Casters which can cast from instances of this type to other types.
     * </p>
     *
     * <p>
     * For example, if this is a number Type, there might be casters
     * from a text Type.
     * </p>
     *
     * @return The type casters from this Type.  Never null.
     *         Never contains any null elements.
     */
    public abstract TypeCaster<? extends STORAGE_VALUE, ?> []
        typeCastersFromThisType();


    /**
     * <p>
     * Returns a Conditional TypeCaster which will cast the
     * specified raw value from the specified target class
     * (possibly of another type) to this type.
     * </p>
     *
     * @param raw_value The object which will be cast to a raw
     *                  value of this Type.  Must not be null.
     *
     * @param to_class The target raw class for the TypeCaster to be
     *                 returned.  Must not be null.
     *                 If the specified raw class is not a raw class
     *                 of this type then the Conditional result will
     *                 be an error.
     *
     * @return A Conditional type caster, which will return
     *         the requested type caster on success.  If there is no such
     *         type caster the caller has the option of retrieving
     *         or throwing a checked or unchecked (runtime)
     *         TypeException, or retrieving a default value, and so on.
     *         Typically the "none" value of the Conditional is a
     *         NoTypeCaster.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract <FROM extends Object, TO extends STORAGE_VALUE>
        Conditional< TypeCaster<FROM, TO>, TypeException >
            typeCasterToThisType (
                                  FROM raw_value,
                                  Class<TO> to_class
                                  );


    /**
     * <p>
     * Casters which can cast to instances of this type.
     * </p>
     *
     * <p>
     * For example, if this is a number Type, there might be casters
     * to a text Type.
     * </p>
     *
     * @return The type casters to this Type.  Never null.
     *         Never contains any null elements.
     */
    public abstract TypeCaster<?, ? extends STORAGE_VALUE> []
        typeCastersToThisType();
}
