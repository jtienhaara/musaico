package musaico.types;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Empty read-only registry of TypeCasters, used instead of null.
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
public class NoTypeCastersRegistry
    implements TypeCastersRegistry, Serializable
{
    /**
     * <p>
     * Creates a new NoTypeCastersRegistry.
     * </p>
     *
     * <p>
     * Package private.  Use <code> TypeCastersRegistry.NONE </code>
     * instead.
     * </p>
     */
    NoTypeCastersRegistry ()
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
        return new SimpleTypeCastersFrom<FROM> ( from_type );
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
        return null;
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
        throw new TypeException ( "Cannot register type caster [%type_caster%] from [%from_type%] to [%to_type%] in [%type_casters_registry%]",
                                  "type_caster", type_caster,
                                  "from_type", from_type,
                                  "to_type", to_type,
                                  "type_casters_registry", this );
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
        return new SimpleTypeCastersTo<TO> ( to_type );
    }


    /**
     * @see musaico.types.TypeCastersRegistry#typeCasters()
     */
    public TypeCaster<Object,Object> [] typeCasters ()
    {
        return new TypeCaster [ 0 ];
    }
}
