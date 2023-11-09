package musaico.types;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Empty read-only types registry, used instead of null.
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
public class NoTypesRegistry
    implements TypesRegistry, Serializable
{
    /**
     * <p>
     * Creates a new NoTypesRegistry.
     * </p>
     *
     * <p>
     * Package private.  Use <code> TypesRegistry.NONE </code>
     * instead.
     * </p>
     */
    NoTypesRegistry ()
    {
    }


    /**
     * @see musaico.types.TypesRegistry#put(musaico.types.TypeSystem)
     */
    @Override
    public TypesRegistry put (
                              TypeSystem type_system
                              )
        throws I18nIllegalArgumentException
    {
        throw new I18nIllegalArgumentException ( "NoTypesRegistry [%no_types_registry%] cannot register type system [%type_system%]",
                                                 "no_types_registry", this,
                                                 "type_system", type_system );
    }


    /**
     * @see musaico.types.TypesRegistry#rawClasses()
     */
    @Override
    public Class<?> [] rawClasses ()
    {
        return new Class [ 0 ];
    }


    /**
     * @see musaico.types.TypesRegistry#type(Class<RAW_VALUE>)
     */
    @Override
    public 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
                           Type<STORAGE_VALUE> type (
                                                     Class<RAW_VALUE> raw_class
                                                     )
    {
        return null;
    }


    /**
     * @see musaico.types.TypesRegistry#types()
     */
    @Override
    public Type<Object> [] types ()
    {
        return new Type [ 0 ];
    }


    /**
     * @see musaico.types.TypesRegistry#typeSystem(java.lang.Class)
     */
    public TypeSystem typeSystem (
                                  Class<?> raw_class
                                  )
    {
        return null;
    }


    /**
     * @see musaico.types.TypesRegistry#typeSystems()
     */
    public TypeSystem [] typeSystems ()
    {
        return new TypeSystem [ 0 ];
    }
}
