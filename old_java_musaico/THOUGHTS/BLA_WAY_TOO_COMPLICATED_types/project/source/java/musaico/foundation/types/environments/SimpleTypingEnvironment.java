package musaico.types.environments;


import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;


import musaico.condition.Conditional;
import musaico.condition.Failed;
import musaico.condition.Successful;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.types.Instance;
import musaico.types.InstanceBuilder;
import musaico.types.NoTypeCaster;
import musaico.types.SimpleTypeCastersRegistry;
import musaico.types.SimpleTypesRegistry;
import musaico.types.SimpleTypeSystem;
import musaico.types.Tag;
import musaico.types.Type;
import musaico.types.TypeCaster;
import musaico.types.TypeCastersRegistry;
import musaico.types.TypeException;
import musaico.types.TypesRegistry;
import musaico.types.TypeSystem;
import musaico.types.TypingEnvironment;


/**
 * <p>
 * Simple registry of Type representations, by raw data type Class.
 * </p>
 *
 * <p>
 * For example, <code> StringType </code> is always registered as the Type
 * for <code> String.class </code>.
 * </p>
 *
 * <p>
 * The root of the hierarchy of TypeSystem's is also stored in
 * the TypesRegistry and can be retrieved by calling
 * <code>root ()<code>.
 * </p>
 *
 *
 * <p>
 * In Java, every TypesRegistry must be Serializable in order
 * to play nicely across RMI, even if the instances it covers
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
public class SimpleTypingEnvironment
    implements TypingEnvironment, Serializable
{
    /** Any synchronization necessary must be done on this token: */
    private final Serializable lock = new String ();

    /** Root of all the TypeSystem objects in the type systems
     *  hierarchy. */
    private final TypeSystem root;

    /** Registry of all raw class to Type mappings from all type systems. */
    private final TypesRegistry typesRegistry = new SimpleTypesRegistry ();

    /** Registry of all type casters inside and between all TypeSystem's. */
    private final TypeCastersRegistry typeCastersRegistry =
        new SimpleTypeCastersRegistry ();


    /**
     * <p>
     * Creates a new SimpleTypingEnvironment with the specified
     * Tags to customize its constraints and behaviour.
     * </p>
     *
     * @param tags The tags to customize this typing environment.
     *             Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public SimpleTypingEnvironment (
                                    Tag... tags
                                    )
        throws I18nIllegalArgumentException
    {
        boolean is_parameters_ok = true;
        if ( tags == null )
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
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleTypingEnvironment with tags [%tags%]",
                                                     "tags", tags );
        }

        this.root = new SimpleTypeSystem ( this, tags );
    }


    /**
     * @see musaico.types.TypingEnvironment#cast(musaico.types.Instance,musaico.types.Type,java.lang.Class)
     */
    @Override
    public 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Conditional< RAW_VALUE, TypeException >
                cast (
                      Instance instance,
                      Type<STORAGE_VALUE> type,
                      Class<RAW_VALUE> to_class
                      )
        throws I18nIllegalArgumentException
    {
        if ( instance == null
             || instance.type () == null
             || type == null
             || to_class == null )
        {
            throw new I18nIllegalArgumentException ( "Invalid parameters to typing environment [%typing_environment%] cast [%instance%] to type [%type%] raw class [%to_class%]",
                                                   "typing_environment", this,
                                                   "instance", instance,
                                                   "raw_class", to_class,
                                                   "type", type );
        }

        // Always cast to the specified type.
        Object value = instance.value ();
        TypeCaster< Object, RAW_VALUE > caster =
            instance.type ().typeCasterFromThisType ( value,
                                                      to_class )
            .orNull ();
        if ( caster == null )
        {
            caster = type.typeCasterToThisType ( value,
                                                 to_class )
                .orNull ();
        }

        if ( caster == null )
        {
            if ( to_class.isAssignableFrom ( instance.type ().storageClass () ) )
            {
                // No TypeCaster necessary!
                RAW_VALUE raw_value = (RAW_VALUE) instance.value ();
                Conditional< RAW_VALUE, TypeException >
                    conditional =
                    new Successful< RAW_VALUE, TypeException > (
                        to_class,
                        raw_value );
                return conditional;
            }

            TypeException ex = new TypeException ( "Typing environment [%typing_environment%] failed to cast from instance [%instance%] to raw class [%raw_class%] type [%type%]",
                                                   "typing_environment", this,
                                                   "instance", instance,
                                                   "raw_class", to_class,
                                                   "type", type );

            Conditional< RAW_VALUE, TypeException > conditional =
                new Failed< RAW_VALUE, TypeException > ( to_class,
                                                         ex,
                                                         type.none () );
            return conditional;
        }

        // Now cast from initial type to the specified type.
        RAW_VALUE raw_value;
        try
        {
            raw_value = caster.cast ( instance.value () );
        }
        catch ( TypeException ex )
        {
            Conditional< RAW_VALUE, TypeException > conditional =
                new Failed< RAW_VALUE, TypeException > ( to_class,
                                                         ex,
                                                         type.none () );
            return conditional;
        }
        catch ( Throwable throwable )
        {
            TypeException ex = new TypeException ( "Typing environment [%typing_environment%] failed to cast from instance [%instance%] to raw class [%raw_class%] type [%type%]",
                                                   "typing_environment", this,
                                                   "instance", instance,
                                                   "raw_class", to_class,
                                                   "type", type,
                                                   "cause", throwable );

            Conditional< RAW_VALUE, TypeException > conditional =
                new Failed< RAW_VALUE, TypeException > ( to_class,
                                                         ex,
                                                         type.none () );

            return conditional;
        }

        Conditional< RAW_VALUE, TypeException >
            conditional =
            new Successful< RAW_VALUE, TypeException > (
                to_class,
                raw_value );
        return conditional;
    }


    /**
     * @see musaico.types.TypingEnvironment#prepare(java.lang.Object)
     */
    @Override
    public 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Conditional< InstanceBuilder, TypeException >
                prepare (
                         RAW_VALUE value
                         )
    {
        // Can't create an Instance from null.
        if ( value == null )
        {
            TypeException ex =
                new TypeException ( "Cannot create Instance from type [%type%] raw [%raw_class%] value [%raw_value%] in typing environment [%typing_environment%]",
                                    "type", null,
                                    "raw_class", null,
                                    "raw_value", value,
                                    "typing_environment", this );

            Conditional< InstanceBuilder, TypeException > conditional =
                new Failed< InstanceBuilder, TypeException > (
                    InstanceBuilder.class,
                    ex,
                    InstanceBuilder.NONE );

            return conditional;
        }

        // Lookup the Type.
        // Stupid getClass signature needs a cast
        Class<RAW_VALUE> raw_class = (Class<RAW_VALUE>) value.getClass ();
        Type<STORAGE_VALUE> type = this.type ( raw_class );
        if ( type == null )
        {
            TypeException ex =
                new TypeException ( "Cannot create Instance from type [%type%] raw [%raw_class%] value [%raw_value%] in typing environment [%typing_environment%]",
                                    "type", type,
                                    "raw_class", value.getClass ().getName (),
                                    "raw_value", value,
                                    "typing_environment", this );

            Conditional< InstanceBuilder, TypeException > conditional =
                new Failed< InstanceBuilder, TypeException > (
                    InstanceBuilder.class,
                    ex,
                    InstanceBuilder.NONE );

            return conditional;
        }

        // Create an InstanceBuilder given the type.
        return this.prepare ( type, value );
    }



    /**
     * @see musaico.types.TypingEnvironment#prepare(java.lang.Class,java.lang.Object)
     */
    @Override
    public 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Conditional< InstanceBuilder, TypeException >
                prepare (
                         Class<RAW_VALUE> raw_class,
                         RAW_VALUE value
                         )
    {
        // Can't create an Instance from null raw class.
        if ( raw_class == null
             || value == null )
        {
            TypeException ex =
                new TypeException ( "Cannot create Instance from type [%type%] raw [%raw_class%] value [%raw_value%] in typing environment [%typing_environment%]",
                                    "type", null,
                                    "raw_class", raw_class,
                                    "raw_value", value,
                                    "typing_environment", this );

            Conditional< InstanceBuilder, TypeException > conditional =
                new Failed< InstanceBuilder, TypeException > (
                    InstanceBuilder.class,
                    ex,
                    InstanceBuilder.NONE );

            return conditional;
        }

        // Lookup the Type in the TypesRegistry.
        Type<STORAGE_VALUE> type = (Type<STORAGE_VALUE>)
            this.type ( raw_class );
        if ( type == null )
        {
            TypeException ex =
                new TypeException ( "Cannot create Instance from type [%type%] raw [%raw_class%] value [%raw_value%] in typing environment [%typing_environment%]",
                                    "type", type,
                                    "raw_class", value.getClass (),
                                    "raw_value", value,
                                    "typing_environment", this );

            Conditional< InstanceBuilder, TypeException > conditional =
                new Failed< InstanceBuilder, TypeException > (
                    InstanceBuilder.class,
                    ex,
                    InstanceBuilder.NONE );

            return conditional;
        }

        // Create an InstanceBuilder given the type.
        return this.prepare ( type, value );
    }


    /**
     * @see musaico.types.TypingEnvironment#prepare(musaico.types.Type,java.lang.Class,java.lang.Object)
     */
    @Override
    public 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Conditional< InstanceBuilder, TypeException >
                prepare (
                         Type<STORAGE_VALUE> type,
                         RAW_VALUE value
                         )
    {
        // Can't create an Instance from null.
        if ( type == null
             || value == null )
        {
            TypeException ex =
                new TypeException ( "Cannot create Instance from type [%type%] raw [%raw_class%] value [%raw_value%] in typing environment [%typing_environment%]",
                                    "type", type,
                                    "raw_class", ( value == null ? null : value.getClass () ),
                                    "raw_value", value,
                                    "typing_environment", this );

            Conditional< InstanceBuilder, TypeException > conditional =
                new Failed< InstanceBuilder, TypeException > (
                    InstanceBuilder.class,
                    ex,
                    InstanceBuilder.NONE );

            return conditional;
        }

        // Create an InstanceBuilder.
        InstanceBuilder builder;
        try
        {
            builder =
                new SimpleInstanceBuilder<STORAGE_VALUE> ( this, type, value );

            // Apply all Tags from Instance, Type, TypeSystem(s).
            // Any failure will result in a TypeException.
            builder.validate ();
        }
        catch ( TypeException ex )
        {
            // Any casting errors and so on get dealt with by the caller
            // via Conditional.
            Conditional< InstanceBuilder, TypeException > conditional =
                new Failed< InstanceBuilder, TypeException > (
                    InstanceBuilder.class,
                    ex,
                    InstanceBuilder.NONE );

            return conditional;
        }
        catch ( Throwable throwable )
        {
            // Any casting errors and so on get dealt with by the caller
            // via Conditional.
            Conditional< InstanceBuilder, TypeException > conditional =
                new Failed< InstanceBuilder, TypeException > (
                    InstanceBuilder.class,
                    new TypeException ( "Typing environment [%typing_environment%] failed to prepare instance of type [%type%] with raw value [%raw_value%]",
                                        "typing_environment", this,
                                        "type", type,
                                        "raw_value", value,
                                        "cause", throwable ),
                    InstanceBuilder.NONE );

            return conditional;
        }

        Conditional< InstanceBuilder, TypeException >
            conditional =
            new Successful< InstanceBuilder, TypeException > (
                InstanceBuilder.class,
                builder );
        return conditional;
    }


    /**
     * @see musaico.types.TypingEnvironment#duplicate(musaico.types.Instance)
     */
    @Override
    public Conditional< InstanceBuilder, TypeException >
        duplicate (
                   Instance instance
                   )
    {
        if ( instance == null )
        {
            TypeException ex =
                new TypeException ( "Cannot duplicate instance [%instance%] in typing environment [%typing_environment%]",
                                    "instance", instance,
                                    "typing_environment", this );

            Conditional< InstanceBuilder, TypeException >
                conditional =
                    new Failed< InstanceBuilder,
                                TypeException> ( InstanceBuilder.class,
                                                 ex,
                                                 InstanceBuilder.NONE );

            return conditional;
        }

        Type<Object> type = (Type<Object>) instance.type ();
        Object value = instance.value ();

        // Now create the duplicate InstanceBuilder.
        InstanceBuilder duplicator =
            new SimpleInstanceBuilder<Object> ( this, type, value );


        Conditional< InstanceBuilder, TypeException >
            conditional =
            new Successful< InstanceBuilder, TypeException > (
                InstanceBuilder.class,
                duplicator );
        return conditional;
    }


    /**
     * @see musaico.types.TypingEnvironment#register(musaico.types.TypeSystem)
     */
    public TypingEnvironment register (
                                       TypeSystem type_system
                                       )
        throws I18nIllegalArgumentException
    {
        synchronized ( this.lock )
        {
            try
            {
                this.typesRegistry.put ( type_system );
            }
            catch ( I18nIllegalArgumentException e )
            {
                // We've already registered one of the raw classes
                // in the specified type system.  Throw a slightly
                // more useful error message.
                throw new I18nIllegalArgumentException ( "Typing environment [%typing_environment%] cannot register type system [%type_system%]",
                                                         "typing_environment", this,
                                                         "type_system", type_system,
                                                         "cause", e );
            }
        }

        return this;
    }


    /**
     * @see musaico.types.TypingEnvironment#root()
     */
    @Override
    public TypeSystem root ()
    {
        return this.root;
    }


    /**
     * @see musaico.types.TypingEnvironment#tags()
     */
    @Override
    public Tag [] tags ()
    {
        return this.root.tags ();
    }


    /**
     * @see musaico.types.TypingEnvironment#tags(musaico.types.Instance)
     */
    @Override
    public Tag [] tags (
                        Instance instance
                        )
    {
        if ( instance == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot determine the tags for instance [%instance%] in environment [%environment%] type [%type%] type system [%type_system%]",
                                                     "instance", instance,
                                                     "environment", this,
                                                     "type", "?",
                                                     "type_system", "?" );
        }

        Type type = instance.type ();
        if ( type == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot determine the tags for instance [%instance%] in environment [%environment%] type [%type%] type system [%type_system%]",
                                                     "instance", instance,
                                                     "environment", this,
                                                     "type", type,
                                                     "type_system", "?" );
        }

        // Figure out the type system whence the type came.
        TypeSystem type_system =
            this.typeSystem ( (Class<Type>) type.getClass () );
        if ( type_system == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot determine the tags for instance [%instance%] in environment [%environment%] type [%type%] type system [%type_system%]",
                                                     "instance", instance,
                                                     "environment", this,
                                                     "type", type,
                                                     "type_system", type_system );
        }

        // Accumulate instance's tags, the type itself, and all
        // tags for the type system into one.
        // Note that the root type system (and therefore all child
        // type systems) already have the tags we were constructed with.
        Tag [] instance_tags = instance.tags ();
        Tag [] type_tags = type.tags ();
        Tag [] type_system_tags = type_system.tags ();

        Tag [] all_tags =
            new Tag [ instance_tags.length
                      + type_tags.length
                      + type_system_tags.length ];
        int offset = 0;
        System.arraycopy ( instance_tags, 0,
                           all_tags, offset,
                           instance_tags.length );
        offset += instance_tags.length;
        System.arraycopy ( type_tags, 0,
                           all_tags, offset,
                           type_tags.length );
        offset += type_tags.length;
        System.arraycopy ( type_system_tags, 0,
                           all_tags, offset,
                           type_system_tags.length );

        return all_tags;
    }


    /**
     * @see musaico.types.TypingEnvironment#type(java.lang.Class)
     */
    @Override
    public 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Type<STORAGE_VALUE> type (
                                      Class<RAW_VALUE> raw_class
                                      )
    {
        return this.typesRegistry.type ( raw_class );
    }


    /**
     * @see musaico.types.TypingEnvironment#typeSystem(java.lang.Class)
     */
    @Override
    public
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
        TypeSystem typeSystem (
                               Class<RAW_VALUE> raw_class
                               )
    {
        synchronized ( this.lock )
        {
            return this.typesRegistry.typeSystem ( raw_class );
        }
    }


    /**
     * @see musaico.types.TypingEnvironment#type(java.lang.Class)
     */
    @Override
    public final Type<Object> [] types ()
    {
        synchronized ( this.lock )
        {
            return this.typesRegistry.types ();
        }
    }
}
