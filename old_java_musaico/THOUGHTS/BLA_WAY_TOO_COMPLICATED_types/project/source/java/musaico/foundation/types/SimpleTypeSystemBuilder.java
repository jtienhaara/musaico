package musaico.types;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Builds up a SimpleTypeSystem.
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
public class SimpleTypeSystemBuilder
    implements TypeSystemBuilder, Serializable
{
    /** Synchronize all critical sections on this token: */
    private final Serializable lock = new String ();

    /** The parent of this and all other TypeSystem's.
     *  Contains the registry of all types. */
    private final TypingEnvironment environment;

    /** The parent TypeSystem of this TypeSystem. */
    private final TypeSystem typeSystemParent;

    /** Tags for Instances.  These tags apply to all
     *  Instances of all Types, whereas each Type and
     *  each Instance may add further Tags of their own. */
    private final List<Tag> tags = new ArrayList<Tag> ();

    /** All Types belonging to the TypeSystem being built. */
    private final List<Type<Object>> types = new ArrayList<Type<Object>> ();


    /**
     * <p>
     * Creates a SimpleTypeSystem builder.
     * </p>
     *
     * @param environment The lookup of all Type's and TypeSystem's.
     *                    Must have a non-null root type system.
     *                    Must not be null.
     *
     * @param type_system_parent The parent of the type system to build,
     *                           such as the root type system of the
     *                           typing environment, or perhaps a specific
     *                           type system which the new type system
     *                           will extend.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public SimpleTypeSystemBuilder (
                                    TypingEnvironment environment,
                                    TypeSystem type_system_parent
                                    )
        throws I18nIllegalArgumentException
    {
        if ( environment == null
             || environment.root () == null
             || type_system_parent == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleTypeSystemBuilder in typing environment [%typing_environment%] under parent type system [%type_system_parent%]",
                                      "typing_environment", environment,
                                      "type_system_parent", type_system_parent );
        }

        this.environment = environment;
        this.typeSystemParent = type_system_parent;
    }


    /**
     * @see musaico.types.TypeSystemBuilder#add(musaico.types.Tag)
     */
    @Override
    public TypeSystemBuilder add (
                                  Tag tag
                                  )
        throws I18nIllegalArgumentException
    {
        if ( tag == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot add tag [%tag%] to type system builder [%type_system_builder%]",
                                      "tag", tag,
                                      "type_system_builder", this );
        }

        synchronized ( this.lock )
        {
            this.tags.add ( tag );
        }

        return this;
    }


    /**
     * @see musaico.types.TypeSystemBuilder#add(musaico.types.Type)
     */
    @Override
    public TypeSystemBuilder add (
                                  Type<Object> type
                                  )
        throws I18nIllegalArgumentException
    {
        if ( type == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot add type [%type%] to type system builder [%type_system_builder%]",
                                      "tyoe", type,
                                      "type_system_builder", this );
        }

        // Validate the type first.
        try
        {
            this.validate ( type );
        }
        catch ( TypeException e )
        {
            throw new I18nIllegalArgumentException ( e );
        }

        synchronized ( this.lock )
        {
            this.types.add ( type );
        }

        return this;
    }


    /**
     * @see musaico.types.TypeSystemBuilder#build()
     */
    @Override
    public SimpleTypeSystem build ()
    {
        final Tag [] tags;
        final Type<Object> [] types;
        synchronized ( this.lock )
        {
            tags = this.tags ();
            types = this.types ();
        }

        return new SimpleTypeSystem ( this.environment, this.typeSystemParent,
                                      tags, types );
    }


    /**
     * @see musaico.types.TypeSystemBuilder#environment()
     */
    @Override
    public TypingEnvironment environment ()
    {
        return this.environment;
    }


    /**
     * @see musaico.types.SimpleTypeSystemBuilder#prepare(java.lang.String,java.lang.Class,java.lang.Object)
     */
    public
        <STORAGE_VALUE extends Object>
            TypeBuilder<STORAGE_VALUE> prepare (
                                                String name,
                                                Class<STORAGE_VALUE> storage_class,
                                                STORAGE_VALUE none
                                                )
    {
        return new SimpleTypeBuilder<STORAGE_VALUE> ( name,
                                                      storage_class,
                                                      none );
    }


    /**
     * @see musaico.types.TypeSystemBuilder#remove(musaico.types.Tag)
     */
    @Override
    public TypeSystemBuilder remove (
                                     Tag tag
                                     )
        throws I18nIllegalArgumentException
    {
        if ( tag == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot remove tag [%tag%] from type system builder [%type_system_builder%]",
                                      "tag", tag,
                                      "type_system_builder", this );
        }

        synchronized ( this.lock )
        {
            this.tags.remove ( tag );
        }

        return this;
    }


    /**
     * @see musaico.types.TypeSystemBuilder#remove(musaico.types.Type)
     */
    @Override
    public TypeSystemBuilder remove (
                                     Type<Object> type
                                     )
        throws I18nIllegalArgumentException
    {
        if ( type == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot remove type [%type%] from type system builder [%type_system_builder%]",
                                      "type", type,
                                      "type_system_builder", this );
        }

        synchronized ( this.lock )
        {
            this.types.remove ( type );
        }

        return this;
    }


    /**
     * @see musaico.types.TypeSystemBuilder#tags()
     */
    @Override
    public Tag [] tags ()
    {
        final Tag [] tags;
        synchronized ( this.lock )
        {
            Tag [] template = new Tag [ this.tags.size () ];
            tags = this.tags.toArray ( template );
        }

        return tags;
    }


    /**
     * @see musaico.types.TypeSystemBuilder#typeSystemParent()
     */
    @Override
    public TypeSystem typeSystemParent ()
    {
        return this.typeSystemParent;
    }


    /**
     * @see musaico.types.TypeSystemBuilder#types()
     */
    @Override
    public Type<Object> [] types ()
    {
        final Type<Object> [] types;
        synchronized ( this.lock )
        {
            Type<Object> [] template = new Type [ this.types.size () ];
            types = this.types.toArray ( template );
        }

        return types;
    }


    /**
     * @see musaico.types.TypeSystemBuilder#validate(musaico.types.Type)
     */
    @Override
    public
        <STORAGE_VALUE extends Object>
            TypeSystemBuilder validate (
                                        Type<STORAGE_VALUE> type
                                        )
        throws TypeException
    {
        // No rules for the simple type system builder.
        return this;
    }
}
