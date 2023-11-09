package musaico.types;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Creates Instances of Types, each with a specific value and
 * zero or more Tags to manage constraints and so on.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public interface InstanceBuilder
    extends Serializable
{
    /** Always fails to create an instance. */
    public static final InstanceBuilder NONE =
        new NoInstanceBuilder ();


    /**
     * <p>
     * Adds a Tag, such as a constraint on the values allowed for
     * the Instance being built.
     * </p>
     *
     * @param tag The tag to add to the instance being built.
     *            The instance must be valid according to the
     *            specified Tag.  Must not be null.
     *
     * @return This InstanceBuilder.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract InstanceBuilder add (
                                         Tag tag
                                         )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Adds all of the specified Tags to the instance being built.
     * </p>
     *
     * @param that The tags which will be added to the
     *             Instance currently being built.  Must not
     *             be null.  Must not contain any null elements.
     *
     * @return This InstanceBuilder.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract InstanceBuilder addTags (
                                             Tag... tags
                                             )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Creates a new Instance from the current builder settings.
     * </p>
     *
     * @return A newly created Instance.  Never null.
     *
     * @throws TypeException If the value violates the rules of
     *                       the Type or any tags (such as a privacy
     *                       tag and so on).
     */
    public abstract Instance build ()
        throws TypeException;


    /**
     * <p>
     * Copies metadata from the specified instance, including
     * the Tags.
     * </p>
     *
     * @param that The instance whose Tags and so on will be copied
     *             to the instance being built.  Must not be null.
     *
     * @return This InstanceBuilder.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract InstanceBuilder copy (
                                          Instance that
                                          )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Returns the TypingEnvironment in which this InstanceBuilder
     * was created.
     * </p>
     *
     * <p>
     * The TypingEnvironment provides all type and instance
     * services, from creating new type systems to validating
     * instance data.
     * </p>
     *
     * @return The TypingEnvironment in which this builder
     *         was created.  Never null.
     */
    public abstract TypingEnvironment environment ();


    /**
     * <p>
     * Removes a Tag, such as a constraint on the values allowed for
     * the Instance being built.
     * </p>
     *
     * @param tag The tag to remove from the instance being built.
     *            The instance must be valid according to the
     *            specified Tag.  Must not be null.
     *
     * @return This InstanceBuilder.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract InstanceBuilder remove (
                                            Tag tag
                                            )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Removes the Tags from the specified Instance.
     * </p>
     *
     * @param tags The tags which will be removed from the
     *             Instance currently being built.  Must not
     *             be null.  Must not contain any null elements.
     *
     * @return This InstanceBuilder.  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract InstanceBuilder removeTags (
                                                Tag... tags
                                                )
        throws I18nIllegalArgumentException;


    /**
     * <p>
     * Returns the Tags for the instance to be built.
     * </p>
     *
     * @return The Tags, such as value constraints.  Never null.
     *         Never contains any null elements.
     */
    public abstract Tag [] tags ();


    /**
     * <p>
     * Returns the Type being used to create an Instance.
     * </p>
     *
     * @return The Type being used to create an Instance,
     *         such as an <code> IntType </code> or a
     *         <code> StringType </code> and so on.
     *         Never null.
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
     * If any of the tags fails, then the exception handler
     * for the TypingEnvironment is invoked.
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
     * Returns the raw value of the Instance being built.
     * </p>
     *
     * @return The raw data value of the instance being built.
     *         Never null.
     */
    public abstract Object value ();
}
