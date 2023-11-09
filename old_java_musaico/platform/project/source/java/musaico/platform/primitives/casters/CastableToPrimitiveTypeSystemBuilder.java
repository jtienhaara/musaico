package musaico.types.primitive;


import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.types.SimpleTypeSystemBuilder;
import musaico.types.Type;
import musaico.types.TypeBuilder;
import musaico.types.TypeCaster;
import musaico.types.TypeException;
import musaico.types.TypeSystem;
import musaico.types.TypeSystemBuilder;
import musaico.types.TypingEnvironment;


/**
 * <p>
 * TypeSystem of primitive types, such as text, number, time,
 * hash, and so on.  Primitives are all cross-platform,
 * so they can be shared between C, JavaScript, Tcl, Java,
 * and so on platforms.
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
public class CastableToPrimitiveTypeSystemBuilder
    extends SimpleTypeSystemBuilder
    implements Serializable
{
    /**
     * <p>
     * Creates a new CastableToPrimitiveTypeSystemBuilder in the specified
     * tying environment to build a new type system beneath the specified
     * one.
     * </p>
     *
     * @param environment The lookup of all Type's and TypeSystem's.
     *                    Must have a non-null root type system.
     *                    Must not be null.
     *
     * @param parent_type_system The parent type system.  This builder will
     *                           construct a child type system.
     *                           For example, the root type
     *                           system of the specified environment,
     *                           or perhaps another type system which
     *                           this builder will extend with a child
     *                           type system.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public CastableToPrimitiveTypeSystemBuilder (
                                                 TypingEnvironment environment,
                                                 TypeSystem parent_type_system
                                                 )
        throws I18nIllegalArgumentException
    {
        super ( environment, parent_type_system );
    }


    /**
     * @see musaico.types.TypeSystemBuilder#validate(Class<RAW_VALUE>,Type<STORAGE_VALUE>)
     */
    @Override
    public 
        <STORAGE_VALUE extends Object>
            TypeSystemBuilder validate (
                                        Type<STORAGE_VALUE> type
                                        )
        throws TypeException
    {
        super.validate ( type );

        for ( Class<? extends STORAGE_VALUE> raw_class : type.rawClasses () )
        {
            // Not serializable?  Can't be in this type system.  Sorry, bub.
            if ( ! Serializable.class.isAssignableFrom ( raw_class ) )
            {
                throw new TypeException ( "Only Serializable classes can be used as castable-to-primitives.  [%raw_class%] (represented by [%type%]) is not serializable so cannot be added to type system [%type_system%]",
                                          "raw_class", raw_class,
                                          "type", type,
                                          "type_system", this );
            }

            // Doesn't have TypeCasters to/from all other
            // primitive Types?  Can't be in this type system.  Sorry, bub.
            Type<?> [] types = this.environment ().types ();

            TypeCaster<? extends STORAGE_VALUE, ?> [] type_casters_from_type =
                type.typeCastersFromThisType ();
            Set<Class<?>> from_raw_class_to_x_set = new HashSet<Class<?>> ();
            for ( TypeCaster<? extends STORAGE_VALUE, ?> type_caster :
                      type_casters_from_type )
            {
                Class<?> from_class = type_caster.fromClass ();
                if ( ! raw_class.equals ( from_class ) )
                {
                    // Not a type caster FROM this raw class.
                    continue;
                }

                Class<?> to_class = type_caster.toClass ();
                from_raw_class_to_x_set.add ( to_class );
            }

            TypeCaster<?, ? extends STORAGE_VALUE> [] type_casters_to_type =
                type.typeCastersToThisType ();
            Set<Class<?>> from_x_to_raw_class_set = new HashSet<Class<?>> ();
            for ( TypeCaster<?, ? extends STORAGE_VALUE> type_caster :
                      type_casters_to_type )
            {
                Class<?> to_class = type_caster.toClass ();
                if ( ! raw_class.equals ( to_class ) )
                {
                    // Not a type caster TO this raw class.
                    continue;
                }

                Class<?> from_class = type_caster.fromClass ();
                from_x_to_raw_class_set.add ( from_class );
            }

            for ( int t = 0; t < types.length; t ++ )
            {
                if ( types [ t ].equals ( type ) )
                {
                    // No need to make sure there's a caster
                    // from itself to itself.
                    continue;
                }

                // Make sure this is a PrimitiveType.
                if ( ! ( types [ t ] instanceof PrimitiveType ) )
                {
                    // Not a PrimitiveType, doesn't need to be castable.
                    continue;
                }

                PrimitiveType<?> primitive = (PrimitiveType<?>) types [ t ];

                // The new Type must be cast-able to/from all
                // PrimitiveTypes in the type system being built.
                for ( Class<?> primitive_raw_class : primitive.rawClasses () )
                {
                    boolean has_caster_to_x =
                        from_raw_class_to_x_set.contains ( primitive_raw_class );
                    if ( ! has_caster_to_x )
                    {
                        // Maybe this type has a caster FROM the raw class.
                        for ( TypeCaster<?, ?> primitive_caster :
                                  primitive.typeCastersFromThisType () )
                        {
                            if ( raw_class.equals ( primitive_caster.toClass () ) )
                            {
                                has_caster_to_x = true;
                                break;
                            }
                        }
                    }

                    boolean has_caster_from_x =
                        from_x_to_raw_class_set.contains ( primitive_raw_class );
                    if ( ! has_caster_from_x )
                    {
                        // Maybe this type has a caster TO the raw class.
                        for ( TypeCaster<?, ?> primitive_caster :
                                  primitive.typeCastersFromThisType () )
                        {
                            if ( raw_class.equals ( primitive_caster.toClass () ) )
                            {
                                has_caster_from_x = true;
                                break;
                            }
                        }
                    }

                    if ( ! has_caster_from_x )
                    {
                        throw new TypeException ( "Invalid castable-to-primitive [%type%] in [%type_system%]: No caster from primitive [%from_primitive%] to [%to_type%]",
                                                  "type", type,
                                                  "type_system", this,
                                                  "from_primitive", primitive,
                                                  "to_type", type );
                    }
                    else if ( ! has_caster_to_x )
                    {
                        throw new TypeException ( "Invalid castable-to-primitive [%type%] in [%type_system%]: No caster from [%from_type%] to primitive [%to_primitive%]",
                                                  "type", type,
                                                  "type_system", this,
                                                  "from_type", type,
                                                  "to_primitive", primitive );
                    }
                }
            }
        }

        return this;
    }
}
