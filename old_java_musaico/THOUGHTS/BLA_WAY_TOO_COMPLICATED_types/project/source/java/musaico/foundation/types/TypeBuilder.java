package musaico.types;


import java.io.Serializable;


import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Cross-platform, dynamic type builder.
 * </p>
 *
 * <p>
 * In Java, all TypeBuilder implementations must be Serializable
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
public interface TypeBuilder<STORAGE_VALUE extends Object>
    extends Serializable
{
    /**
     * <p>
     * Adds the specified raw class to the type being built.
     * </p>
     *
     * <p>
     * For example, a Type with Number storage class might support
     * raw classes Integer.class, Float.class and BigDecimal.class.
     * </p>
     *
     * @return This type builder.  Never null.
     */
    public abstract TypeBuilder addRawClass (
                                             Class<? extends STORAGE_VALUE> raw_class
                                             )
        throws ParametersMustNotBeNull.Violation,
               TypeBuilderMustNotIncludeRawClass.Violation;

    /**
     * <p>
     * Adds a Tag to the type being built.
     * </p>
     *
     * @param tag The tag to add.  Must not be null.
     *
     * @return This TypeBuilder.  Never null.
     */
    public abstract TypeBuilder<STORAGE_VALUE> addTag (
                                                       Tag tag
                                                       )
        throws ParametersMustNotBeNull.Violation,
               TypeBuilderMustNotIncludeTag.Violation;


    /**
     * <p>
     * Adds the specified type caster FROM the type being built
     * to some other raw class.
     * </p>
     *
     * @param type_caster The type caster FROM one of the
     *                    raw class(es) of the type being built
     *                    to some other class.  Must not be null.
     *
     * @return This TypeBuilder.  Never null.
     */
    public abstract TypeBuilder<STORAGE_VALUE> addTypeCasterFrom (
                                                                  TypeCaster<? extends STORAGE_VALUE, ?> type_caster
                                                                  )
        throws ParametersMustNotBeNull.Violation,
               TypeBuilderMustIncludeRawClass.Violation,
               TypeBuilderMustNotHaveTypeCasterToTargetClass.Violation;


    /**
     * <p>
     * Adds the specified type caster from some other raw class
     * TO the type being built.
     * </p>
     *
     * @param type_caster The type caster from some other class
     *                    TO one of the raw class(es) of the type
     *                    being built.  Must not be null.
     *
     * @return This TypeBuilder.  Never null.
     */
    public abstract TypeBuilder<STORAGE_VALUE> addTypeCasterTo (
                                                                TypeCaster<?, ? extends STORAGE_VALUE> type_caster
                                                                )
        throws ParametersMustNotBeNull.Violation,
               TypeBuilderMustNotIncludeRawClass.Violation,
               TypeBuilderMustNotHaveTypeCasterToTargetClass.Violation;


    /**
     * <p>
     * Builds a new Type from the configuration in this builder.
     * </p>
     *
     * @return A newly created Type with the name, storage class, tags,
     *         storage casters, and so on from this builder.
     *         Never null.
     */
    public abstract Type<STORAGE_VALUE> build ();


    /**
     * <p>
     * Builds a type caster from one of the raw classes for the type
     * being built, through an existing type caster to intermediate
     * values, through the specified type caster to target values,
     * and adds it to the type being built.
     * </p>
     *
     * <p>
     * For example, if an Address type is being built, then
     * the following code might add type casters to Strings
     * and bytes:
     * </p>
     *
     * <pre>
     *     TypeBuilder<Address> builder = ...;
     *     builder.addTypeCasterFrom ( new AddressToStringTypeCaster () );
     *     builder.chainFrom ( Address.class, new StringToBytesCaster () );
     * </pre>
     *
     * <p>
     * The above code first adds a typecaster from the Address class to
     * Strings, then creates a chain caster from
     * <code> Address -&gt; String -&gt; byte[] </code>, which relies
     * on the first type caster to cast each Address to a String, then
     * uses the StringToBytesCaster to cast that String to byte[].
     *
     * @param raw_class Which of the raw classes the chain caster
     *                  will start from.  For example, a number type
     *                  might have one chain caster from each of the
     *                  raw types ( Integer.class, Float.class,
     *                  BigDecimal.class ).  Must be one of the raw
     *                  classes for the type being built.
     *                  Must not be null.
     *
     * @param intermediate_to_target_caster The typecaster from an
     *                                      intermediate class to the
     *                                      target of the new chain caster.
     *                                      There must already be a
     *                                      typecaster from the specified
     *                                      raw class to the intermediate
     *                                      class.  There must not be
     *                                      any existing type caster
     *                                      (chained or otherwise) from
     *                                      the specified raw class to
     *                                      the target of the typecaster.
     *                                      Must not be null.
     *
     * @return This TypeBuilder.  Never null.
     */
    public abstract
        <RAW_VALUE extends STORAGE_VALUE,
        INTERMEDIARY extends Object,
        TARGET extends Object>
        TypeBuilder<STORAGE_VALUE> chainFrom (
                                              Class<RAW_VALUE> raw_class,
                                              TypeCaster<INTERMEDIARY, TARGET> intermediate_to_target_caster
                                              )
        throws ParametersMustNotBeNull.Violation,
               TypeBuilderMustIncludeRawClass.Violation,
               TypeBuilderMustHaveTypeCasterToIntermediaryClass.Violation,
               TypeBuilderMustNotHaveTypeCasterToTargetClass.Violation;


    /**
     * <p>
     * Builds a type caster from some other class to one of the raw
     * classes for the type being built, through the specified type
     * caster from source values to intermediate values, through an
     * existing type caster to the raw class,
     * and adds it to the type being built.
     * </p>
     *
     * <p>
     * For example, if an Address type is being built, then
     * the following code might add type casters from Strings
     * and bytes to Addresses:
     * </p>
     *
     * <pre>
     *     TypeBuilder<Address> builder = ...;
     *     builder.addTypeCasterTo ( new StringToAddressTypeCaster () );
     *     builder.chainTo ( new BytesToStringCaster (), Address.class );
     * </pre>
     *
     * <p>
     * The above code first adds a typecaster from Strings to the Address
     * class, then creates a chain caster from
     * <code> byte[] -&gt; String -&gt; Address </code>.
     *
     * @param intermediate_to_target_caster The typecaster from the
     *                                      source class of the new chain
     *                                      caster to an intermediate class.
     *                                      There must already be a
     *                                      typecaster from the intermediate
     *                                      class to the specified raw
     *                                      class.  There must not be
     *                                      any existing type caster
     *                                      (chained or otherwise) from
     *                                      the source of the typecaster to
     *                                      the specified raw class.
     *                                      Must not be null.
     *
     * @param raw_class Which of the raw classes the chain caster
     *                  will end with.  For example, a number type
     *                  might have one chain caster to each of the
     *                  raw types ( Integer.class, Float.class,
     *                  BigDecimal.class ).  Must be one of the raw
     *                  classes for the type being built.
     *                  Must not be null.
     *
     * @return This TypeBuilder.  Never null.
     */
    public abstract
        <SOURCE extends Object,
        INTERMEDIARY extends Object,
        RAW_VALUE extends STORAGE_VALUE>
        TypeBuilder<STORAGE_VALUE> chainTo (
                                            TypeCaster<SOURCE, INTERMEDIARY> source_to_intermediate_caster,
                                            Class<RAW_VALUE> raw_class
                                            )
        throws ParametersMustNotBeNull.Violation,
               TypeBuilderMustIncludeRawClass.Violation,
               TypeBuilderMustHaveTypeCasterFromIntermediaryClass.Violation,
               TypeBuilderMustHaveTypeCasterToTargetClass.Violation;


    /**
     * <p>
     * Returns the name of the type being built, such as "number"
     * or "age" or "distance" and so on.
     * </p>
     *
     * @return The name of the Type to be built.  Never null.
     */
    public abstract String name ();


    /**
     * <p>
     * Returns the "no such value" value of the type being built.
     * </p>
     *
     * @return The "no value" value for the type being built.
     *         Never null.
     */
    public abstract STORAGE_VALUE none ();


    /**
     * <p>
     * Creates a new "no type caster" from the specified raw class
     * of the type being built to the specfied target class.
     * </p>
     *
     * <p>
     * For example, if an Address type is being built and casts from
     * the Address class to Integer.class are to be explicitly forbidden
     * at runtime, then the following code will prevent
     * <code> Address -&gt; Integer </code> casts:
     * </p>
     *
     * <pre>
     *     TypeBuilder<Address> builder = ...;
     *     builder.noTypeCasterFrom ( Address.class, Integer.class );
     * </pre>
     *
     * @param from_raw_class The class from which type casts will be explicitly
     *                       forbidden at runtime.  Must be one of the raw
     *                       classes of the type being built.  Must not
     *                       be null.
     *
     * @param target_class The class to which type casts from the specified
     *                     raw class will be explicitly forbidden at
     *                     runtime.  There must not be an existing typecaster
     *                     from the specified raw class to the target
     *                     class.  Must not be null.
     *
     * @return This TypeBuilder.  Never null.
     */
    public abstract
        <RAW_VALUE extends STORAGE_VALUE,
        TARGET extends Object>
        TypeBuilder noTypeCasterFrom (
                                      Class<RAW_VALUE> from_raw_class,
                                      Class<TARGET> target_class
                                      )
        throws ParametersMustNotBeNull.Violation,
               TypeBuilderMustIncludeRawClass.Violation,
               TypeBuilderMustNotHaveTypeCasterToTargetClass.Violation;


    /**
     * <p>
     * Creates a new "no type caster" from the specified source class
     * to the specified raw class of the type being built.
     * </p>
     *
     * <p>
     * For example, if an Address type is being built and casts from
     * Integer.class to the Address class are to be explicitly forbidden
     * at runtime, then the following code will prevent
     * <code> Integer --&gt; Address </code> casts:
     * </p>
     *
     * <pre>
     *     TypeBuilder<Address> builder = ...;
     *     builder.noTypeCasterFrom ( Integer.class, Address.class );
     * </pre>
     *
     * @param source_class The class from which type casts to the specified
     *                     raw class will be explicitly forbidden at
     *                     runtime.  There must not be an existing typecaster
     *                     from the source class to the specified raw
     *                     class.  Must not be null.
     *
     * @param to_raw_class The class to which type casts will be explicitly
     *                     forbidden at runtime.  Must be one of the raw
     *                     classes of the type being built.  Must not
     *                     be null.
     *
     * @return This TypeBuilder.  Never null.
     */
    public abstract
        <SOURCE extends Object,
        RAW_VALUE extends STORAGE_VALUE>
        TypeBuilder noTypeCasterTo (
                                    Class<SOURCE> source_class,
                                    Class<RAW_VALUE> to_raw_class
                                    )
        throws ParametersMustNotBeNull.Violation,
               TypeBuilderMustIncludeRawClass.Violation,
               TypeBuilderMustNotHaveTypeCasterToTargetClass.Violation;


    /**
     * <p>
     * Returns all raw classes which will be supported by the Type
     * being built.
     * </p>
     *
     * <p>
     * For example, a Type with storage class Number.class might
     * support raw classes Float.class, BigDecimal.class, Long.class
     * and so on.  The raw classes are used to lookup concrete instances
     * during instance creation and casting.
     * </p>
     *
     * @return The raw classes which will be supported by the
     *         Type being built.  Never null.
     *         Never contains any null elements.
     */
    public abstract Class<? extends STORAGE_VALUE> [] rawClasses ();


    /**
     * <p>
     * Returns the storage class for the type being built,
     * such as String.class or Number.class.  This is the root
     * class of all raw class(es) mapped by the type.  For
     * example a numeric type might be mapped from Float.class
     * and Integer.class, but its storage class would be Number.class.
     * </p>
     *
     * @return The storage class of the type being built.  Never null.
     */
    public abstract Class<STORAGE_VALUE> storageClass ();


    /**
     * <p>
     * Returns all the Tags added to this type builder so far.
     * </p>
     *
     * @return All tags added to this type builder.  Never null.
     *         Never contains any null elements.
     */
    public Tag [] tags ();

    /**
     * <p>
     * Returns all the type casters FROM the raw classes of this type
     * to other types.
     * </p>
     *
     * <p>
     * For example, a NumberType might have type casters from
     * Integer.class, Float.class and so on to String.class.
     * </p>
     *
     * @return The type casters which take in raw values from this type
     *         and spit out raw values of other types.  Never null.
     *         Never contains any null elements.
     */
    public abstract TypeCaster<? extends STORAGE_VALUE, ?> [] typeCastersFrom ();

    /**
     * <p>
     * Returns all the type casters from other types TO the raw classes
     * of this type.
     * </p>
     *
     * <p>
     * For example, a NumberType might have type casters from
     * String.class to Integer.class, Float.class and so on.
     * </p>
     *
     * @return The type casters which take in raw values from other
     *         types and spit out raw values of this type.  Never null.
     *         Never contains any null elements.
     */
    public abstract TypeCaster<?, ? extends STORAGE_VALUE> [] typeCastersTo ();
}
