package musaico.types;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * Represents no type system.  This singleton is used by types until
 * they are registered in a particular TypeSystem.
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
public class NoTypeSystem
    implements TypeSystem, Serializable
{
    /**
     * <p>
     * Creates a NoTypeSystem.
     * </p>
     */
    protected NoTypeSystem ()
    {
    }


    /**
     * @see musaico.types.TypeSystem#contains(musaico.types.Type)
     */
    @Override
    public boolean contains (
                             Type<?> type
                             )
    {
        return false;
    }


    /**
     * @see musaico.types.TypeSystem#environment()
     */
    public final TypingEnvironment environment ()
    {
        return TypingEnvironment.NONE;
    }


    /**
     * @see musaico.types.TypeSystem#tags()
     */
    public final Tag [] tags ()
    {
        return new Tag [ 0 ];
    }


    /**
     * @see musaico.types.TypeSystem#type(java.lang.Class)
     */
    public
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
            Type<STORAGE_VALUE> type (
                                      Class<RAW_VALUE> raw_class
                                      )
    {
        return null;
    }


    /**
     * @see musaico.types.TypeSystem#types()
     */
    public final Type<Object> [] types ()
    {
        return (Type<Object> []) new Type [ 0 ];
    }


    /**
     * @see musaico.types.TypeSystem#typesRegistry()
     */
    public final TypesRegistry typesRegistry ()
    {
        return TypesRegistry.NONE;
    }


    /**
     * @see musaico.types.TypeSystem#typeSystemParent()
     */
    public final TypeSystem typeSystemParent ()
    {
        return null;
    }


    /**
     * @see musaico.types.TypeSystem#validate(Class<RAW_VALUE>,Type<STORAGE_VALUE>)
     */
    public 
        <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
                           TypeSystem validate (
                                                Class<RAW_VALUE> raw_class,
                                                Type<STORAGE_VALUE> type
                                                )
        throws TypeException
    {
        // Do nothing.
        return this;
    }
}
