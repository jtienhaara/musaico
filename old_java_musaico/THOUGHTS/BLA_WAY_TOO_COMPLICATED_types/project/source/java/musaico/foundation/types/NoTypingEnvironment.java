package musaico.types;


import java.io.Serializable;


import musaico.condition.Conditional;
import musaico.condition.Failed;

import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * TypeSystems representing all types which have yet to be
 * placed into type systems.  These types should not be
 * used until they have been registered in a proper TypingEnvironment.
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
public class NoTypingEnvironment
    implements TypingEnvironment, Serializable
{
    /**
     * <p>
     * Creates a new NoTypingEnvironment.
     * </p>
     */
    protected NoTypingEnvironment ()
    {
    }


    /**
     * @see musaico.types.TypingEnvironment#cast(Instance,Type<STORAGE_VALUE>,Class)
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
    {
        TypeException type_exception =
            new TypeException ( "Invalid operation on NoTypingEnvironment: [%operation%]",
                                "operation", "cast instance = " + instance
                                                 + ", type = " + type );

        Conditional< RAW_VALUE, TypeException > conditional =
            new Failed< RAW_VALUE, TypeException > ( to_class,
                                                     type_exception,
                                                     type.none () );

        return conditional;
    }


    /**
     * @see musaico.types.TypingEnvironment#duplicate(Instance)
     */
    @Override
    public final
        Conditional< InstanceBuilder, TypeException >
            duplicate (
                       Instance instance
                       )
    {
        TypeException type_exception =
            new TypeException ( "Invalid operation on NoTypingEnvironment: [%operation%]",
                                "operation", "duplicate instance = " + instance );
        Conditional< InstanceBuilder, TypeException> conditional =
            new Failed< InstanceBuilder, TypeException > ( InstanceBuilder.class,
                                                           type_exception,
                                                           InstanceBuilder.NONE );

        return conditional;
    }


    /**
     * @see musaico.types.TypingEnvironment#prepare(RAW_VALUE)
     */
    @Override
    public final 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Conditional< InstanceBuilder, TypeException >
                prepare (
                         RAW_VALUE value
                         )
    {
        TypeException type_exception =
            new TypeException ( "Invalid operation on NoTypingEnvironment: [%operation%]",
                                "operation", "create value = " + value );
        Conditional< InstanceBuilder, TypeException > conditional =
            new Failed< InstanceBuilder, TypeException > ( InstanceBuilder.class,
                                                           type_exception,
                                                           InstanceBuilder.NONE );

        return conditional;
    }



    /**
     * @see musaico.types.TypingEnvironment#prepare(Class<RAW_VALUE>,RAW_VALUE)
     */
    @Override
    public final 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Conditional< InstanceBuilder, TypeException >
                prepare (
                         Class<RAW_VALUE> raw_class,
                         RAW_VALUE value
                         )
    {
        TypeException type_exception =
            new TypeException ( "Invalid operation on NoTypingEnvironment: [%operation%]",
                                "operation", "create value = " + value );
        Conditional< InstanceBuilder, TypeException > conditional =
            new Failed< InstanceBuilder, TypeException > ( InstanceBuilder.class,
                                                           type_exception,
                                                           InstanceBuilder.NONE );

        return conditional;
    }


    /**
     * @see musaico.types.TypingEnvironment#prepare(Type<STORAGE_VALUE>,RAW_VALUE)
     */
    @Override
    public final 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Conditional< InstanceBuilder, TypeException >
                prepare (
                         Type<STORAGE_VALUE> type,
                         RAW_VALUE value
                         )
    {
        TypeException type_exception =
            new TypeException ( "Invalid operation on NoTypingEnvironment: [%operation%]",
                                "operation", "create value = " + value );
        Conditional< InstanceBuilder, TypeException > conditional =
            new Failed< InstanceBuilder, TypeException > ( InstanceBuilder.class,
                                                           type_exception,
                                                           InstanceBuilder.NONE );

        return conditional;
    }


    /**
     * @see musaico.types.TypingEnvironment#register(musaico.types.TypeSystem)
     */
    @Override
    public final TypingEnvironment register (
                                             TypeSystem type_system
                                             )
        throws I18nIllegalArgumentException
    {
        throw new I18nIllegalArgumentException ( "NoTypingEnvironment cannot register type system [%type_system%]",
                                                 "type_system", type_system );
    }


    /**
     * @see musaico.types.TypingEnvironment#root()
     */
    @Override
    public final TypeSystem root ()
    {
        return TypeSystem.NONE;
    }


    /**
     * @see musaico.types.TypingEnvironment#tags()
     */
    @Override
    public Tag [] tags ()
    {
        return new Tag [ 0 ];
    }


    /**
     * @see musaico.types.TypingEnvironment#tags(Instance)
     */
    @Override
    public Tag [] tags (
                        Instance instance
                        )
    {
        return new Tag [ 0 ];
    }


    /**
     * @see musaico.types.TypingEnvironment#type(java.lang.Class)
     */
    @Override
    public final 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Type<STORAGE_VALUE> type (
                                      Class<RAW_VALUE> raw_class
                                      )
    {
        return null;
    }


    /**
     * @see musaico.types.TypingEnvironment#typeSystem(Class)
     */
    @Override
    public
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            TypeSystem typeSystem (
                                   Class<RAW_VALUE> raw_class
                                   )
    {
        return null;
    }


    /**
     * @see musaico.types.TypingEnvironment#type(java.lang.Class)
     */
    @Override
    public final Type<Object> [] types ()
    {
        return new Type [ 0 ];
    }
}
