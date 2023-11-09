package musaico.types;


import java.io.Serializable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Builds SimpleTypes one step at a time.
 * </p>
 *
 * <p>
 * In Java, all TypeBuilder implementations must be Serializable
 * to play nicely across RMI, even if the Instances they
 * represent are not Serializable.
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
public class SimpleTypeBuilder<STORAGE_VALUE extends Object>
    implements TypeBuilder<STORAGE_VALUE>, Serializable
{
    /** Any synchronization necessary must be done on this token: */
    private final Serializable lock = new String ();

    /** The universal name of the type being built.
     *  Used across all locales. */
    private final String name;

    /** The storage class for the type being built up. */
    private final Class<STORAGE_VALUE> storageClass;

    /** Other raw classes under which this type is registered.
     *  For example if the Type's storage class is Number.class,
     *  it might be registered with raw classes Float.class,
     *  Integer.class, BigInteger.class and so on. */
    private final List<Class<? extends STORAGE_VALUE>> rawClasses =
        new ArrayList<Class<? extends STORAGE_VALUE>> ();

    /** The "no value" value for this type. */
    private final STORAGE_VALUE none;

    /** The tags being built up for a type, including constraints.
     *  The tags are used to check each Instance during calls
     *  to <code> Instance.validate () </code>. */
    private final List<Tag> tags = new ArrayList<Tag> ();

    /** Type casters FROM the raw class(es) of the type being
     *  built to other classes. */
    private final List<TypeCaster<? extends STORAGE_VALUE, ?>> typeCastersFrom =
        new ArrayList<TypeCaster<? extends STORAGE_VALUE, ?>> ();

    /** Type casters from the raw class(es) of the type being
     *  built TO other classes. */
    private final List<TypeCaster<?, ? extends STORAGE_VALUE>> typeCastersTo =
        new ArrayList<TypeCaster<?, ? extends STORAGE_VALUE>> ();


    /**
     * <p>
     * Creates a new type builder for the specified type name and
     * storage class.
     * </p>
     *
     * <p>
     * For example, the following creates a type builder that will
     * construct a type called "first_name" with String storage class.
     * </p>
     *
     * <pre>
     *     new SimpleTypeBuilder<String> ( "first_name", String.class,
     *                                     "NO_NAME" );
     * </pre>
     *
     * <p>
     * The following creates a type builder that will construct a
     * type called "age" with Integer storage class:
     * </p>
     *
     * <pre>
     *     new SimpleTypeBuilder<Integer> ( "age", Integer.class,
     *                                      Integer.MIN_VALUE );
     * </pre>
     *
     * @param name The name of the type to be built.  Must not be null.
     *
     * @param storage_class The storage class for the type to be
     *                      built.  Defines the class of value that
     *                      will be stored inside instances of the type.
     *                      Must not be null.
     *
     * @param none The "no value" value for the type to be built.
     *             An Instance whose raw value is equal to this
     *             none value is treated specially
     *             in certain cases, such as when querying the "next"
     *             or "previous" value from a data structure.
     *             Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public SimpleTypeBuilder (
                              String name,
                              Class<STORAGE_VALUE> storage_class,
                              STORAGE_VALUE none
                              )
    {
        if ( name == null
             || storage_class == null
             || none == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleTypeBuilder for name [%name%] storage class [%storage_class%] none value [%none%]",
                                                     "name", name,
                                                     "storage_class", storage_class,
                                                     "none", none );
        }

        this.name = name;
        this.storageClass = storage_class;
        this.none = none;

        this.rawClasses.add ( this.storageClass );
    }


    /**
     * @see musaico.types.TypeBuilder#addRawClass(java.lang.Class)
     */
    @Override
    public SimpleTypeBuilder addRawClass (
                                          Class<? extends STORAGE_VALUE> raw_class
                                          )
    {
        synchronized ( this.lock )
        {
            this.rawClasses.add ( raw_class );
        }

        return this;
    }


    /**
     * @see musaico.types.TypeBuilder#addTag(musaico.types.Tag)
     */
    @Override
    public TypeBuilder<STORAGE_VALUE> addTag (
                                              Tag tag
                                              )
        throws I18nIllegalArgumentException
    {
        if ( tag == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot add Tag [%tag%] to TypeBuilder [%type_builder%]",
                                                     "tag", tag,
                                                     "type_builder", this );
        }

        synchronized ( this.lock )
        {
            this.tags.add ( tag );
        }

        return this;
    }


    /**
     * @see musaico.types.TypeBuilder#addTypeCasterFrom(java.lang.Class)
     */
    @Override
    public TypeBuilder<STORAGE_VALUE> addTypeCasterFrom (
                                                         TypeCaster<? extends STORAGE_VALUE, ?> type_caster
                                                         )
        throws I18nIllegalArgumentException
    {
        synchronized ( this.lock )
        {
            this.typeCastersFrom.add ( type_caster );
        }

        return this;
    }


    /**
     * @see musaico.types.TypeBuilder#addTypeCasterTo(java.lang.Class)
     */
    @Override
    public TypeBuilder<STORAGE_VALUE> addTypeCasterTo (
                                                       TypeCaster<?, ? extends STORAGE_VALUE> type_caster
                                                       )
        throws I18nIllegalArgumentException
    {
        synchronized ( this.lock )
        {
            this.typeCastersTo.add ( type_caster );
        }

        return this;
    }


    /**
     * @see musaico.types.TypeBuilder#build()
     */
    @Override
    public Type<STORAGE_VALUE> build ()
    {
        return new SimpleType<STORAGE_VALUE> ( this.name,
                                               this.storageClass,
                                               this.rawClasses (),
                                               this.none,
                                               this.tags (),
                                               this.typeCastersFrom (),
                                               this.typeCastersTo () );
    }


    /**
     * @see musaico.types.TypeBuilder#chainFrom(java.lang.Class, musaico.types.TypeCaster)
     */
    @Override
    public
        <RAW_VALUE extends STORAGE_VALUE,
        INTERMEDIARY extends Object,
        TARGET extends Object>
        TypeBuilder<STORAGE_VALUE> chainFrom (
                                              Class<RAW_VALUE> raw_class,
                                              TypeCaster<INTERMEDIARY, TARGET> intermediate_to_target_caster
                                              )
        throws I18nIllegalArgumentException
    {
        Class<INTERMEDIARY> intermediate_class =
            intermediate_to_target_caster.fromClass ();
        Class<TARGET> target_class = intermediate_to_target_caster.toClass ();
        synchronized ( this.lock )
        {
            for ( TypeCaster<? extends STORAGE_VALUE, ?> type_caster :
                      this.typeCastersFrom )
            {
                if ( type_caster.fromClass ().equals ( raw_class )
                     && type_caster.toClass ().equals ( intermediate_class ) )
                {
                    TypeCaster<RAW_VALUE, INTERMEDIARY>
                        raw_to_intermediate_caster =
                        (TypeCaster<RAW_VALUE, INTERMEDIARY>) type_caster;
                    TypeCaster<RAW_VALUE, TARGET>
                        chain_caster =
                        new ChainCaster<RAW_VALUE, INTERMEDIARY, TARGET> ( raw_class,
                                                                           raw_to_intermediate_caster,
                                                                           intermediate_class,
                                                                           intermediate_to_target_caster,
                                                                           target_class );
                    this.typeCastersFrom.add ( chain_caster );
                    return this;
                }
            }
        }

        throw new I18nIllegalArgumentException ( "Cannot add a chain caster from [%source_class%] to [%target_class%]: No type caster from [%intermediate_source_class%] to [%intermediate_target_class%]",
                                                 "source_class", raw_class,
                                                 "target_class", target_class,
                                                 "intermediate_source_class", raw_class,
                                                 "intermeiate_target_class", intermediate_class );
    }


    /**
     * @see musaico.types.TypeBuilder#chainTo(musaico.types.TypeCaster, java.lang.Class)
     */
    @Override
    public
        <SOURCE extends Object,
        INTERMEDIARY extends Object,
        RAW_VALUE extends STORAGE_VALUE>
        TypeBuilder<STORAGE_VALUE> chainTo (
                                            TypeCaster<SOURCE, INTERMEDIARY> source_to_intermediate_caster,
                                            Class<RAW_VALUE> raw_class
                                            )
        throws I18nIllegalArgumentException
    {
        Class<SOURCE> source_class = source_to_intermediate_caster.fromClass ();
        Class<INTERMEDIARY> intermediate_class =
            source_to_intermediate_caster.toClass ();
        synchronized ( this.lock )
        {
            for ( TypeCaster<?, ? extends STORAGE_VALUE> type_caster :
                      this.typeCastersTo )
            {
                if ( type_caster.fromClass ().equals ( intermediate_class )
                     && type_caster.toClass ().equals ( raw_class ) )
                {
                    TypeCaster<INTERMEDIARY, RAW_VALUE>
                        intermediate_to_raw_caster =
                        (TypeCaster<INTERMEDIARY, RAW_VALUE>) type_caster;
                    TypeCaster<SOURCE, RAW_VALUE>
                        chain_caster =
                        new ChainCaster<SOURCE, INTERMEDIARY, RAW_VALUE> ( source_class,
                                                                           source_to_intermediate_caster,
                                                                           intermediate_class,
                                                                           intermediate_to_raw_caster,
                                                                           raw_class );
                    this.typeCastersTo.add ( chain_caster );
                    return this;
                }
            }
        }

        throw new I18nIllegalArgumentException ( "Cannot add a chain caster from [%source_class%] to [%target_class%]: No type caster from [%intermediate_source_class%] to [%intermediate_target_class%]",
                                                 "source_class", source_class,
                                                 "target_class", raw_class,
                                                 "intermediate_source_class", intermediate_class,
                                                 "intermeiate_target_class", raw_class );
    }


    /**
     * @see musaico.types.TypeBuilder#name()
     */
    @Override
    public String name ()
    {
        return this.name;
    }


    /**
     * @see musaico.types.TypeBuilder#none()
     */
    @Override
    public STORAGE_VALUE none ()
    {
        return this.none;
    }


    /**
     * @see musaico.types.TypeBuilder#noTypeCasterFrom(java.lang.Class,java.lang.Class)
     */
    @Override
    public
        <RAW_VALUE extends STORAGE_VALUE,
        TARGET extends Object>
        TypeBuilder noTypeCasterFrom (
                                      Class<RAW_VALUE> from_raw_class,
                                      Class<TARGET> target_class
                                      )
        throws I18nIllegalArgumentException
    {
        TypeCaster<RAW_VALUE, TARGET> no_type_caster =
            new NoTypeCaster<RAW_VALUE, TARGET> ( from_raw_class,
                                                  target_class );
        this.addTypeCasterFrom ( no_type_caster );

        return this;
    }


    /**
     * @see musaico.types.TypeBuilder#noTypeCasterTo(java.lang.Class,java.lang.Class)
     */
    @Override
    public
        <SOURCE extends Object,
        RAW_VALUE extends STORAGE_VALUE>
        TypeBuilder noTypeCasterTo (
                                    Class<SOURCE> source_class,
                                    Class<RAW_VALUE> to_raw_class
                                    )
        throws I18nIllegalArgumentException
    {
        TypeCaster<SOURCE, RAW_VALUE> no_type_caster =
            new NoTypeCaster<SOURCE, RAW_VALUE> ( source_class,
                                                  to_raw_class );
        this.addTypeCasterTo ( no_type_caster );

        return this;
    }


    /**
     * @see musaico.types.TypeBuilder#rawClasses()
     */
    @Override
    public Class<? extends STORAGE_VALUE> [] rawClasses ()
    {
        final Class<? extends STORAGE_VALUE> [] raw_classes;
        synchronized ( this.lock )
        {
            Class<? extends STORAGE_VALUE> [] template =
                new Class [ this.rawClasses.size () ];
            raw_classes = this.rawClasses.toArray ( template );
        }

        return raw_classes;
    }


    /**
     * @see musaico.types.TypeBuilder#storageClass()
     */
    @Override
    public Class<STORAGE_VALUE> storageClass ()
    {
        return this.storageClass;
    }


    /**
     * @see musaico.types.TypeBuilder#tags()
     */
    @Override
    public Tag [] tags ()
    {
        final Tag [] tags;
        synchronized ( this.lock )
        {
            Tag [] template = new Tag [ this.tags.size () ];
            tags = this.tags.toArray ( template );
        }

        return tags;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "SimpleTypeBuilder " + this.name;
    }


    /**
     * @see musaico.types.TypeBuilder#typeCastersFrom()
     */
    @Override
    public TypeCaster<? extends STORAGE_VALUE, ?> [] typeCastersFrom ()
    {
        final TypeCaster<? extends STORAGE_VALUE, ?> []
            type_casters_from;
        synchronized ( this.lock )
        {
            TypeCaster<? extends STORAGE_VALUE, ?> [] template =
                new TypeCaster [ this.typeCastersFrom.size () ];
            type_casters_from = this.typeCastersFrom.toArray ( template );
        }

        return type_casters_from;
    }


    /**
     * @see musaico.types.TypeBuilder#typeCastersTo()
     */
    @Override
    public TypeCaster<?, ? extends STORAGE_VALUE> [] typeCastersTo ()
    {
        final TypeCaster<?, ? extends STORAGE_VALUE> []
            type_casters_to;
        synchronized ( this.lock )
        {
            TypeCaster<?, ? extends STORAGE_VALUE> [] template =
                new TypeCaster [ this.typeCastersTo.size () ];
            type_casters_to = this.typeCastersTo.toArray ( template );
        }

        return type_casters_to;
    }
}
