package musaico.types;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import musaico.i18n.exceptions.I18nIllegalArgumentException;


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
public class SimpleTypesRegistry
    implements TypesRegistry, Serializable
{
    /** Synchronize critical sections on this lock: */
    private final Serializable lock = new String ();

    /** The registry of Type<x> by Class<x>.  For example:
     *  <code>
     *    this.registry.get ( String.class ) = StringType instance
     *  </code>
     *  and so on. */
    private final Map<Class<?>,Type<?>> registry =
        new HashMap<Class<?>,Type<?>> ();

    /** All registered TypeSystems in this registry. */
    private final Set<TypeSystem> typeSystems =
        new LinkedHashSet<TypeSystem> ();


    /**
     * <p>
     * Creates a new SimpleTypesRegistry.
     * </p>
     */
    public SimpleTypesRegistry ()
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
        synchronized ( this.lock )
        {
            if ( this.typeSystems.contains ( type_system ) )
            {
                throw new I18nIllegalArgumentException ( "Types registry [%types_registry%] cannot register type system [%type_system%] again",
                                                         "types_registry", this,
                                                         "type_system", type_system );
            }

            Type<Object> [] types = type_system.types ();

            // First make sure none of the raw classes registered
            // in the specified type system have already been
            // registered in this environment.  We don't want
            // two ways of mapping String to a type.
            for ( Type<Object> type : types )
            {
                Class<?> [] raw_classes = type.rawClasses ();
                for ( Class<?> raw_class : raw_classes )
                {
                    final Type old_type = this.registry.get ( raw_class );
                    if ( old_type != null )
                    {
                        throw new I18nIllegalArgumentException ( "Types registry [%types_registry%] cannot register type system [%type_system%] with type [%new_type%] : raw class [%raw_class%] is already mapped to type [%old_type%]",
                                                                 "types_registry", this,
                                                                 "type_system", type_system,
                                                                 "new_type", type,
                                                                 "raw_class", raw_class,
                                                                 "old_type", old_type );
                    }
                }
            }

            // Now we can safely map all the raw classes
            // from the new type system.
            for ( Type<Object> type : types )
            {
                Class<?> [] raw_classes = type.rawClasses ();
                for ( Class<?> raw_class : raw_classes )
                {
                    this.registry.put ( raw_class, type );
                }
            }

            this.typeSystems.add ( type_system );
        }

        return this;
    }


    /**
     * @see musaico.types.TypesRegistry#rawClasses()
     */
    @Override
    public Class<?> [] rawClasses ()
    {
        final Class<?> [] raw_classes;
        synchronized ( this.lock )
        {
            Class<?> [] template = new Class<?> [ this.registry.size () ];
            raw_classes = this.registry.keySet ().toArray ( template );
        }

        return raw_classes;
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
        synchronized ( this.lock )
        {
            return (Type<STORAGE_VALUE>) this.registry.get ( raw_class );
        }
    }


    /**
     * @see musaico.types.TypesRegistry#types()
     */
    @Override
    public Type<Object> [] types ()
    {
        List<Type<Object>> types_list = new ArrayList<Type<Object>> ();
        synchronized ( this.lock )
        {
            for ( TypeSystem type_system : this.typeSystems )
            {
                for ( Type<Object> type : type_system.types () )
                {
                    types_list.add ( type );
                }
            }
        }

        Type<Object> [] template = new Type [ 0 ];
        Type<Object> [] types = types_list.toArray ( template );

        return types;
    }


    /**
     * @see musaico.types.TypesRegistry#typeSystem(java.lang.Class)
     */
    public TypeSystem typeSystem (
                                  Class<?> raw_class
                                  )
    {
        synchronized ( this.lock )
        {
            Type<?> type = this.type ( raw_class );
            if ( type == null )
            {
                return null;
            }

            for ( TypeSystem type_system : this.typeSystems )
            {
                if ( type_system.contains ( type ) )
                {
                    return type_system;
                }
            }
        }

        // Should be impossible...  We have the type registered
        // yet we don't have its type system?!?
        return null;
    }


    /**
     * @see musaico.types.TypesRegistry#typeSystems()
     */
    public TypeSystem [] typeSystems ()
    {
        final TypeSystem [] type_systems;
        synchronized ( this.lock )
        {
            TypeSystem [] template =
                new TypeSystem [ this.typeSystems.size () ];
            type_systems = this.typeSystems.toArray ( template );
        }

        return type_systems;
    }
}
