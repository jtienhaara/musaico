package musaico.types;


import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Simple set of type casters to a specific type from other types.
 * </p>
 *
 *
 * <p>
 * In Java, every TypeCastersTo must be Serializable in order
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
public class SimpleTypeCastersTo<STORAGE_VALUE extends Object>
    implements TypeCastersTo<STORAGE_VALUE>, Serializable
{
    /** Synchronize on this lock for critical sections. */
    private final Serializable lock = new String ();

    /** The type to cast to from various other types. */
    private final Type<STORAGE_VALUE> toType;

    /** The lookup of type casters by "from" type.
     *  Each caster will cast from the specified type to our "to" type. */
    private final Map<Type<Object>,TypeCaster<Object,STORAGE_VALUE>>
        typeCasters =
        new HashMap<Type<Object>,TypeCaster<Object,STORAGE_VALUE>> ();


    /**
     * <p>
     * Creates a new set of TypeCasters to the specified type.
     * </p>
     *
     * @param to_type The type to cast to.  Must not be null.
     */
    public SimpleTypeCastersTo (
                                Type<STORAGE_VALUE> to_type
                                )
    {
        if ( to_type == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create [%class%] with null type",
                                                     "class", this.getClass ().getName () );
        }

        this.toType = to_type;
    }


    /**
     * @see musaico.types.TypeCastersTo#from(Type)
     */
    public 
        <FROM extends Object>
        TypeCaster<FROM,STORAGE_VALUE> from (
                                             Type<FROM> from_type
                                             )
    {
        TypeCaster<FROM,STORAGE_VALUE> type_caster =
            (TypeCaster<FROM,STORAGE_VALUE>) this.typeCasters.get ( from_type );
        return type_caster;
    }


    /**
     * <p>
     * Registers the specified TypeCaster to cast to storage values
     * of this set's "to" type from storage values of the specified
     * from_type.
     * </p>
     *
     * <p>
     * Only the SimpleTypeCastersRegistry should call this method.
     * </p>
     *
     * <p>
     * For example, to register a TypeCaster which casts to
     * StringTypes from FooTypes:
     * </p>
     *
     * <pre>
     *     TypeCastersRegistry registry = ...;
     *     Type<Foo> foo_type = ...;
     *     Type<String> string_type = ...;
     *     TypeCaster<Foo,String> cast_foo_to_string = ...;
     *     TypeCastersTo<String> to_string = registry.to ( string_type );
     *     to_string.register ( foo_type, cast_foo_to_string );
     * </pre>
     *
     * <p>
     * Any exceptions (such as trying to register two TypeCasters for
     * a given to_type and from_type pair) will be thrown as TypeExceptions.
     * </p>
     *
     * @param from_type The Type from which to cast.
     *
     * @param type_caster The TypeCaster which will perform the
     *                    specified casts.
     *
     * @return This TypeCastersTo.  Never null.
     *
     * @throws TypeException If a TypeCaster has already been registered
     *                       for the from type and to type pair.
     */
    protected 
        <FROM extends Object>
        TypeCastersTo register (
                                Type<FROM> from_type,
                                TypeCaster<FROM,STORAGE_VALUE> type_caster
                                )
        throws TypeException
    {
        if ( from_type == null )
        {
            throw new TypeException ( "Cannot register type caster [%type_caster%] from [%from_type%] to [%to_type%]",
                                      "type_caster", type_caster,
                                      "from_type", null,
                                      "to_type", this.type () );
        }
        else if ( type_caster == null )
        {
            throw new TypeException ( "Cannot register type caster [%type_caster%] from [%from_type%] to [%to_type%]",
                                      "type_caster", null,
                                      "from_type",from_type,
                                      "to_type", this.type () );
        }

        synchronized ( this.lock )
        {
            // Make sure there is no existing TypeCaster from the
            // specified type.
            TypeCaster<FROM,STORAGE_VALUE> existing_type_caster =
                this.from ( from_type );
            if ( existing_type_caster != null )
            {
                throw new TypeException ( "Cannot register type caster [%new_type_caster%] from [%from_type%] to [%to_type%]: Existing type caster [%old_type_caster%]",
                                          "old_type_caster", existing_type_caster,
                                          "new_type_caster", type_caster,
                                          "from_type",from_type,
                                          "to_type", this.type () );
            }

            // Now register the type caster.
            this.typeCasters.put ( (Type<Object>) from_type,
                                   (TypeCaster<Object,STORAGE_VALUE>)
                                   type_caster );
        }

        return this;
    }


    /**
     * @see musaico.types.TypeCastersTo#type()
     */
    public Type<STORAGE_VALUE> type ()
    {
        return this.toType;
    }


    /**
     * @see musaico.types.TypeCastersTo#typeCasters()
     */
    public TypeCaster<Object,STORAGE_VALUE> [] typeCasters ()
    {
        final TypeCaster [] type_casters;

        synchronized ( this.lock )
        {
            type_casters = new TypeCaster [ this.typeCasters.size () ];
            Iterator<TypeCaster<Object,STORAGE_VALUE>> it =
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
