package musaico.types;


import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Simple set of type casters from a specific type to other types.
 * </p>
 *
 *
 * <p>
 * In Java, every TypeCastersFrom must be Serializable in order
 * to play nicely across RMI, even if the raw values it casts between
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
public class SimpleTypeCastersFrom<STORAGE_VALUE extends Object>
    implements TypeCastersFrom<STORAGE_VALUE>, Serializable
{
    /** Synchronize on this lock for critical sections. */
    private final Serializable lock = new String ();

    /** The type to cast from to various other types. */
    private final Type<STORAGE_VALUE> fromType;

    /** The lookup of type casters by "to" type.
     *  Each caster will cast to the specified type from our "from" type. */
    private final Map<Type<Object>,TypeCaster<STORAGE_VALUE,Object>>
        typeCasters =
        new HashMap<Type<Object>,TypeCaster<STORAGE_VALUE,Object>> ();


    /**
     * <p>
     * Creates a new set of TypeCasters from the specified type.
     * </p>
     *
     * @param from_type The type to cast from.  Must not be null.
     */
    public SimpleTypeCastersFrom (
                                  Type<STORAGE_VALUE> from_type
                                  )
    {
        if ( from_type == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create [%class%] with null type",
                                                     "class", this.getClass ().getName () );
        }

        this.fromType = from_type;
    }


    /**
     * @see musaico.types.TypeCastersFrom#to(Type)
     */
    public 
        <TO extends Object>
        TypeCaster<STORAGE_VALUE,TO> to (
                                         Type<TO> to_type
                                         )
    {
        TypeCaster<STORAGE_VALUE,TO> type_caster =
            (TypeCaster<STORAGE_VALUE,TO>) this.typeCasters.get ( to_type );
        return type_caster;
    }


    /**
     * <p>
     * Registers the specified TypeCaster to cast from storage values
     * of this set's "from" type to storage values of the specified
     * to_type.
     * </p>
     *
     * <p>
     * Only the SimpleTypeCastersRegistry should call this method.
     * </p>
     *
     * <p>
     * For example, to register a TypeCaster which casts from
     * FooTypes to StringTypes:
     * </p>
     *
     * <pre>
     *     TypeCastersRegistry registry = ...;
     *     Type<Foo> foo_type = ...;
     *     Type<String> string_type = ...;
     *     TypeCaster<Foo,String> cast_foo_to_string = ...;
     *     TypeCastersFrom<Foo> from_foo = registry.from ( foo_type );
     *     from_foo.register ( string_tye, cast_foo_to_string );
     * </pre>
     *
     * <p>
     * Any exceptions (such as trying to register two TypeCasters for
     * a given from_type and to_type pair) will be thrown as TypeExceptions.
     * </p>
     *
     * @param to_type The Type to which to cast.
     *
     * @param type_caster The TypeCaster which will perform the
     *                    specified casts.
     *
     * @return This registry.  Never null.
     *
     * @throws TypeException If a TypeCaster has already been registered
     *                       for the from type and to type pair.
     */
    protected 
        <TO extends Object>
        TypeCastersFrom register (
                                  Type<TO> to_type,
                                  TypeCaster<STORAGE_VALUE,TO> type_caster
                                  )
        throws TypeException
    {
        if ( to_type == null )
        {
                throw new TypeException ( "Cannot register type caster [%new_type_caster%] from [%from_type%] to [%to_type%]",
                                          "type_caster", type_caster,
                                          "from_type", this.type (),
                                          "to_type", null );
        }
        else if ( type_caster == null )
        {
                throw new TypeException ( "Cannot register type caster [%new_type_caster%] from [%from_type%] to [%to_type%]",
                                          "type_caster", null,
                                          "from_type", this.type (),
                                          "to_type", to_type );
        }

        synchronized ( this.lock )
        {
            // Make sure there is no existing TypeCaster to the
            // specified type.
            TypeCaster<STORAGE_VALUE,TO> existing_type_caster =
                this.to ( to_type );
            if ( existing_type_caster != null )
            {
                throw new TypeException ( "Cannot register type caster [%new_type_caster%] from [%from_type%] to [%to_type%]: Existing type caster [%old_type_caster%]",
                                          "old_type_caster", existing_type_caster,
                                          "new_type_caster", type_caster,
                                          "from_type", this.type (),
                                          "to_type", to_type );
            }

            // Now register the type caster.
            this.typeCasters.put ( (Type<Object>) to_type,
                                   (TypeCaster<STORAGE_VALUE,Object>)
                                   type_caster );
        }

        return this;
    }


    /**
     * @see musaico.types.TypeCastersFrom#type()
     */
    public Type<STORAGE_VALUE> type ()
    {
        return this.fromType;
    }


    /**
     * @see musaico.types.TypeCastersFrom#typeCasters()
     */
    public TypeCaster<STORAGE_VALUE,Object> [] typeCasters ()
    {
        final TypeCaster [] type_casters;

        synchronized ( this.lock )
        {
            type_casters = new TypeCaster [ this.typeCasters.size () ];
            Iterator<TypeCaster<STORAGE_VALUE,Object>> it =
                this.typeCasters.values ().iterator ();
            int tc = 0;
            while ( it.hasNext () )
            {
                type_casters [ tc ] = it.next ();
                tc ++;
            }
        }

        return type_casters;
    }
}
