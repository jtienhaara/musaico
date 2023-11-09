package musaico.types;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Simple registry of TypeCasters, indexed by both "from" and "to"
 * types.
 * </p>
 *
 * <p>
 * For instance, the FooToString type caster might be retrieved
 * either by calling
 * <code> TypeCaster<?,?> caster = registry.from ( foo_type ).to ( string_type ) </code>
 * or by calling
 * <code> TypeCaster<?,?> caster = registry.to ( string_type ).from ( foo_type ) </code>.
 * </p>
 *
 *
 * <p>
 * In Java, every TypeCastersRegistry must be Serializable in order
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
public class SimpleTypeCastersRegistry
    implements TypeCastersRegistry, Serializable
{
    /** Synchronize critical sections on this lock: */
    private final Serializable lock = new String ();

    /** The registry of TypeCaster<from,to> by from Types. */
    private final Map<Type<Object>,SimpleTypeCastersFrom<Object>> from =
        new HashMap<Type<Object>,SimpleTypeCastersFrom<Object>> ();

    /** The registry of TypeCaster<from,to> by to Types. */
    private final Map<Type<Object>,SimpleTypeCastersTo<Object>> to =
        new HashMap<Type<Object>,SimpleTypeCastersTo<Object>> ();

    /** All registered TypeCasters in this registry. */
    private final List<TypeCaster<Object,Object>> typeCasters =
        new ArrayList<TypeCaster<Object,Object>> ();


    /**
     * <p>
     * Creates a new SimpleTypeCastersRegistry.
     * </p>
     */
    public SimpleTypeCastersRegistry ()
    {
    }


    /**
     * @see musaico.types.TypeCastersRegistry#from(Type<FROM>)
     */
    public 
        <FROM extends Object>
                      TypeCastersFrom<FROM> from (
                                                  Type<FROM> from_type
                                                  )
    {
        synchronized ( this.lock )
        {
            TypeCastersFrom<FROM> type_casters_from =
                (TypeCastersFrom<FROM>)
                this.from.get ( from_type );

            if ( type_casters_from == null )
            {
                type_casters_from =
                    new SimpleTypeCastersFrom<FROM> ( from_type );

                this.from.put ( (Type<Object>) from_type,
                                (SimpleTypeCastersFrom<Object>)
                                type_casters_from );
            }

            return type_casters_from;
        }
    }


    /**
     * @see musaico.types.TypeCastersRegistry#get(Type<FROM>,Type<TO>)
     */
    public 
        <FROM extends Object,TO extends Object>
            TypeCaster<FROM,TO> get (
                                     Type<FROM> from_type,
                                     Type<TO> to_type
                                     )
    {
        synchronized ( this.lock )
        {
            TypeCaster<FROM,TO> caster = this.from ( from_type ).to ( to_type );
            if ( caster == null )
            {
                caster = this.to ( to_type ).from ( from_type );
            }

            return caster;
        }
    }


    /**
     * @see musaico.types.TypeCastersRegistry#put(Type<FROM>,Type<TO>,TypeCaster<FROM,TO>)
     */
    public 
        <FROM extends Object,TO extends Object>
            TypeCastersRegistry put (
                                     Type<FROM> from_type,
                                     Type<TO> to_type,
                                     TypeCaster<FROM,TO> type_caster
                                     )
        throws TypeException
    {
        synchronized ( this.lock )
        {
            SimpleTypeCastersFrom<FROM> type_casters_from =
                (SimpleTypeCastersFrom<FROM>)
                this.from ( from_type );
            SimpleTypeCastersTo<TO> type_casters_to =
                (SimpleTypeCastersTo<TO>)
                this.to ( to_type );

            TypeCaster<FROM,TO> existing_from_to =
                type_casters_from.to ( to_type );
            if ( existing_from_to != null )
            {
                throw new TypeException ( "TypeCaster from [%from_type%] to [%to_type%] has already been registered: previously registered type [%old_type_caster%] attempted to register [%new_type_caster%]",
                                          "from_type", from_type,
                                          "to_type", to_type,
                                          "old_type_caster", existing_from_to,
                                          "new_type_caster", type_caster );
            }

            TypeCaster<FROM,TO> existing_to_from =
                type_casters_to.from ( from_type );
            if ( existing_to_from != null )
            {
                throw new TypeException ( "TypeCaster from [%from_type%] to [%to_type%] has already been registered: previously registered type [%old_type_caster%] attempted to register [%new_type_caster%]",
                                          "from_type", from_type,
                                          "to_type", to_type,
                                          "old_type_caster", existing_from_to,
                                          "new_type_caster", type_caster );
            }

            type_casters_from.register ( to_type, type_caster );
            type_casters_to.register ( from_type, type_caster );

            this.typeCasters.add ( (TypeCaster<Object,Object>)
                                   type_caster );
        }

        return this;
    }


    /**
     * @see musaico.types.TypeCastersRegistry#to(Type<TO>)
     */
    public 
        <TO extends Object>
                      TypeCastersTo<TO> to (
                                            Type<TO> to_type
                                            )
    {
        synchronized ( this.lock )
        {
            TypeCastersTo<TO> type_casters_to =
                (TypeCastersTo<TO>)
                this.to.get ( to_type );

            if ( type_casters_to == null )
            {
                type_casters_to =
                    new SimpleTypeCastersTo<TO> ( to_type );

                this.to.put ( (Type<Object>) to_type,
                              (SimpleTypeCastersTo<Object>)
                              type_casters_to );
            }

            return type_casters_to;
        }
    }


    /**
     * @see musaico.types.TypeCastersRegistry#typeCasters()
     */
    public TypeCaster<Object,Object> [] typeCasters ()
    {
        TypeCaster<Object,Object> [] type_casters;
        synchronized ( this.lock )
        {
            type_casters =
                new TypeCaster [ this.typeCasters.size () ];

            type_casters = this.typeCasters.toArray ( type_casters );
        }

        return type_casters;
    }
}
