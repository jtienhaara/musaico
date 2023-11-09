package musaico.types;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Represents a simple system of types, such as low-level, immutable,
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
public class SimpleTypeSystem
    implements TypeSystem, Serializable
{
    /** The parent of this and all other TypeSystem's.
     *  Contains the registry of all types. */
    private TypingEnvironment environment;

    /** The parent TypeSystem of this TypeSystem. */
    private final TypeSystem typeSystemParent;

    /** Tags for Instances.  These tags apply to all
     *  Instances of all Types, whereas each Type and
     *  each Instance may add further Tags of their own. */
    private final Tag [] tags;

    /** All types in this type system. */
    private final Type<Object> [] types;


    /**
     * <p>
     * Creates a simple root TypeSystem.
     * </p>
     *
     * <p>
     * The root type system has no types.  Child type systems
     * can be created under the root type system.  However the
     * root type system can have constraints which apply to
     * all child type systems, to customize the behaviour
     * of the tree of types.
     * </p>
     *
     * @param environment The lookup of all Type's and TypeSystem's.
     *                    Must not be null.  Must also not have another
     *                    root type system already.
     *
     * @param tags Constraints and other tags to customize the
     *             rules for instances in this type system.
     *             Can be empty.  Must not be null.  Must not
     *             contain any null elements.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public SimpleTypeSystem (
                             TypingEnvironment environment,
                             Tag [] tags
                             )
        throws I18nIllegalArgumentException
    {
        boolean is_parameters_ok = true;
        if ( environment == null
             || tags == null )
        {
            is_parameters_ok = false;
        }
        else
        {
            for ( Tag tag : tags )
            {
                if ( tag == null )
                {
                    is_parameters_ok = false;
                    break;
                }
            }
        }

        if ( ! is_parameters_ok )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleTypeSystem in typing environment [%typing_environment%] parent [%parent_type_system%] with tags [%tags%] types [%types%]",
                                                     "typing_environment", environment,
                                                     "parent_type_system", "(ROOT)",
                                                     "tags", tags,
                                                     "types", "{}" );
        }

        if ( environment.root () != null
             && environment.root () != this )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a second root type system for typing environment [%typing_environment%]",
                                      "typing_environment", environment );
        }

        // We're the root type system.
        this.environment = environment;
        this.typeSystemParent = null;

        this.tags = new Tag [ tags.length ];
        System.arraycopy ( tags, 0, this.tags, 0, tags.length );

        this.types = new Type [ 0 ];
    }


    /**
     * <p>
     * Creates a new SimpleTypeSystem (such as a primitives TypeSystem,
     * or a user-defined types TypeSystem, and so on).
     * </p>
     *
     * @param environment The lookup of all Type's and TypeSystem's.
     *                    Must have a non-null root type system.
     *                    Must not be null.
     *
     * @param parent_type_system The parent type system, of which this
     *                           is a child.  Must not be null.
     *
     * @param tags Constraints and other tags to customize the
     *             rules for instances in this type system.
     *             Can be empty.  Must not be null.  Must not
     *             contain any null elements.
     *
     * @param types The types defined in this type system.
     *              For example, a "primitives" type system
     *              might define number and text types,
     *              mapped from Integer.class, Float.class,
     *              String.class and so on; whereas
     *              a "distances" type system might define
     *              metres, feet and leagues, all mapped
     *              to numeric Java classes.  And so on.
     *              Must not be null.  Must not contain
     *              any null elements.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public SimpleTypeSystem (
                             TypingEnvironment environment,
                             TypeSystem parent_type_system,
                             Tag [] tags,
                             Type [] types
                             )
        throws I18nIllegalArgumentException
    {
        boolean is_parameters_ok = true;
        if ( parent_type_system == null
             || tags == null
             || types == null )
        {
            is_parameters_ok = false;
        }
        else
        {
            // Make sure the arrays don't have any null elements.
            for ( Type<Object> type : types )
            {
                if ( type == null )
                {
                    is_parameters_ok = false;
                    break;
                }
            }

            for ( Tag tag : tags )
            {
                if ( tag == null )
                {
                    is_parameters_ok = false;
                    break;
                }
            }
        }

        if ( ! is_parameters_ok )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleTypeSystem in typing environment [%typing_environment%] parent [%parent_type_system%] with tags [%tags%] types [%types%]",
                                                     "typing_environment", environment,
                                                     "parent_type_system", parent_type_system,
                                                     "tags", tags,
                                                     "types", types );
        }

        // Non-root type system.
        this.typeSystemParent = parent_type_system;
        this.environment = this.typeSystemParent.environment ();

        // Copy the specified tags AND the parent tags into this
        // type system's tags.
        Tag [] parent_type_system_tags = this.typeSystemParent.tags ();
        this.tags = new Tag [ parent_type_system_tags.length + tags.length ];
        int offset = 0;
        System.arraycopy ( parent_type_system_tags, 0,
                           this.tags, offset, parent_type_system_tags.length );
        offset += parent_type_system_tags.length;
        System.arraycopy ( tags, 0,
                           this.tags, offset, tags.length );

        this.types = new Type [ types.length ];
        System.arraycopy ( types, 0,
                           this.types, 0, this.types.length );
    }


    /**
     * @see musaico.types.TypeSystem#contains(musaico.types.Type)
     */
    @Override
    public boolean contains (
                             Type<?> type
                             )
    {
        for ( Type<Object> system_type : this.types )
        {
            if ( system_type.equals ( type ) )
            {
                return true;
            }
        }

        return false;
    }


    /**
     * @see musaico.types.TypeSystem#environment()
     */
    @Override
    public TypingEnvironment environment ()
    {
        return this.environment;
    }


    /**
     * @see musaico.types.TypeSystem#tags()
     */
    @Override
    public Tag [] tags ()
    {
        final Tag [] tags = new Tag [ this.tags.length ];
        System.arraycopy ( this.tags, 0, tags, 0, this.tags.length );

        return tags;
    }


    /**
     * @see musaico.types.TypeSystem#types()
     */
    @Override
    public Type<Object> [] types ()
    {
        final Type<Object> [] types = new Type [ this.types.length ];
        System.arraycopy ( this.types, 0,
                           types, 0, types.length );

        return types;
    }


    /**
     * @see musaico.types.TypeSystem#typeSystemParent()
     */
    @Override
    public TypeSystem typeSystemParent ()
    {
        return this.typeSystemParent;
    }
}
