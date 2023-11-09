package musaico.kernel.objectsystem.metadata;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;


import musaico.field.Field;
import musaico.field.FieldTypingEnvironment;

import musaico.hash.Hash;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;

import musaico.kernel.objectsystem.Metadata;
import musaico.kernel.objectsystem.MetadataBuilder;
import musaico.kernel.objectsystem.ObjectSystemTypeIdentifier;
import musaico.kernel.objectsystem.RecordTypeIdentifier;

import musaico.region.Region;
import musaico.region.Size;
import musaico.region.Space;

import musaico.region.array.ArraySpace;

import musaico.time.Time;

import musaico.types.RuntimeTypeException;


/**
 * <p>
 * Builds SimpleMetadata metadata containers for ONodes and SuperBlocks and so on.
 * </p>
 *
 *
 * <p>
 * In Java every MetadataBuilder must be Serializable in order to play
 * nicely over RMI, since ONodes and SuperBlocks will often need
 * to carry their MetadataBuilders with them.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public class SimpleMetadataBuilder
    implements MetadataBuilder, Serializable
{
    /** Synchronize all critical sections on this token: */
    private final Serializable lock = new String ();

    /** Creates fields for the standard metadata attributes. */
    private final FieldTypingEnvironment environment;

    /** Lookup of default values metadata by field name.
     *  Never changes after constructor. */
    private final Map<String,Field> defaults;

    /** Lookup of metadata by field name.
     *  Contents change over time. */
    private final Map<String,Field> attributes;


    /**
     * <p>
     * Creates a new SimpleMetadataBuilder with the specified
     * field typing environment.
     * </p>
     *
     * @param environment The FieldTypingEnvironment which will
     *                    be used to create Fields for the
     *                    "standard" metadata attributes.
     *                    Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public SimpleMetadataBuilder (
                                  FieldTypingEnvironment environment
                                  )
        throws I18nIllegalArgumentException
    {
        if ( environment == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleMetadataBuilder with field typing environment [%environment%]",
                                                     "environment", environment );
        }

        this.environment = environment;

        // Set the default standard metadata attributes.
        final FieldTypingEnvironment env = this.environment;
        this.defaults = new HashMap<String,Field> ();
        this.defaults.put ( Metadata.HASH,
                            env.create ( Metadata.HASH,
                                         Hash.class,
                                         Hash.NONE ) );
        this.defaults.put ( Metadata.OBJECT_SYSTEM_TYPE,
                            env.create ( Metadata.OBJECT_SYSTEM_TYPE,
                                         ObjectSystemTypeIdentifier.class,
                                         ObjectSystemTypeIdentifier.NONE ) );
        this.defaults.put ( Metadata.RECORD_TYPE,
                            env.create ( Metadata.RECORD_TYPE,
                                         RecordTypeIdentifier.class,
                                         RecordTypeIdentifier.NONE ) );
        this.defaults.put ( Metadata.REGION,
                            env.create ( Metadata.REGION,
                                         Region.class,
                                         ArraySpace.STANDARD.empty () ) );
        this.defaults.put ( Metadata.SIZE_FIELDS_FREE,
                            env.create ( Metadata.SIZE_FIELDS_FREE,
                                         Size.class,
                                         ArraySpace.STANDARD.none () ) );
        this.defaults.put ( Metadata.SIZE_FIELDS_USED,
                            env.create ( Metadata.SIZE_FIELDS_USED,
                                         Size.class,
                                         ArraySpace.STANDARD.none () ) );
        this.defaults.put ( Metadata.SIZE_RECORDS_FREE,
                            env.create ( Metadata.SIZE_RECORDS_FREE,
                                         Size.class,
                                         ArraySpace.STANDARD.none () ) );
        this.defaults.put ( Metadata.SIZE_RECORDS_USED,
                            env.create ( Metadata.SIZE_RECORDS_USED,
                                         Size.class,
                                         ArraySpace.STANDARD.none () ) );
        this.defaults.put ( Metadata.SPACE,
                            env.create ( Metadata.SPACE,
                                         Space.class,
                                         ArraySpace.STANDARD ) );
        this.defaults.put ( Metadata.TIME_ACCESSED,
                            env.create ( Metadata.TIME_ACCESSED,
                                         Time.class,
                                         Time.NEVER ) );
        this.defaults.put ( Metadata.TIME_CREATED,
                            env.create ( Metadata.TIME_CREATED,
                                         Time.class,
                                         Time.NEVER ) );
        this.defaults.put ( Metadata.TIME_DELETED,
                            env.create ( Metadata.TIME_DELETED,
                                         Time.class,
                                         Time.NEVER ) );
        this.defaults.put ( Metadata.TIME_DIRTIED,
                            env.create ( Metadata.TIME_DIRTIED,
                                         Time.class,
                                         Time.NEVER ) );
        this.defaults.put ( Metadata.TIME_MODIFIED,
                            env.create ( Metadata.TIME_MODIFIED,
                                         Time.class,
                                         Time.NEVER ) );

        this.attributes = new HashMap<String,Field> ();
        for ( String attribute_id : this.defaults.keySet () )
        {
            Field metadata_field = this.defaults.get ( attribute_id );
            this.attributes.put ( attribute_id, metadata_field );
        }
    }


    /**
     * @see musaico.kernel.objectsystem.Metadata#all()
     */
    @Override
    public Field [] all ()
    {
        synchronized ( this.lock )
        {
            return SimpleMetadata.getSortedMetadata ( this.attributes );
        }
    }


    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder#attribute(musaico.field.Field)
     */
    @Override
    public Field attribute (
                            Field attribute_field
                            )
        throws I18nIllegalArgumentException
    {
        final Field previous_field;
        synchronized ( this.lock )
        {
            // Check that the existing attribute class and
            // the new attribute class are the same.
            // If not, throw an exception -- to prevent,
            // for example, cast exceptions when returning
            // the standard attributes.
            Field cast_to_type_field = attribute_field;
            previous_field = this.attributes.get ( attribute_field.id () );
            if ( previous_field != null )
            {
                if ( ! previous_field.type ()
                     .equals ( attribute_field.type () ) )
                {
                    try
                    {
                        Object cast_value =
                            attribute_field.value ( previous_field.type () );
                        cast_to_type_field =
                            this.environment.create ( attribute_field.id (),
                                                      previous_field.type (),
                                                      cast_value );
                    }
                    catch ( RuntimeTypeException e )
                    {
                        throw new I18nIllegalArgumentException ( "Cannot set metadata attribute [%attribute_id%] from [%previous_field%] to [%new_field%]: expected [%expected_type%] but found [%actual_type%]",
                                                                 "attribute_id", attribute_field.id (),
                                                                 "prrevious_field", previous_field,
                                                                 "new_field", attribute_field,
                                                                 "expected_type", previous_field.type (),
                                                                 "actual_type", attribute_field.type () );
                    }
                }
            }

            this.attributes.put ( cast_to_type_field.id (),
                                  cast_to_type_field );
        }

        return previous_field;
    }


    /**
     * @see musaico.kernel.objectsystem.Metadata#attribute(java.lang.String)
     */
    @Override
    public Field attribute (
                            String attribute_id
                            )
    {
        if ( attribute_id == null )
        {
            return Field.NULL;
        }

        final Field attribute_field;
        synchronized ( this.lock )
        {
            attribute_field = this.attributes.get ( attribute_id );
        }

        return attribute_field;
    }


    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder#attribute(java.lang.String,java.lang.Class,java.lang.Object)
     */
    public <ATTRIBUTE extends Object>
                              Field attribute (
                                               String attribute_id,
                                               Class<ATTRIBUTE> attribute_class,
                                               ATTRIBUTE attribute_value
                                               )
        throws I18nIllegalArgumentException
    {
        final Field attribute_field;
        try
        {
            attribute_field =
                this.environment.create ( attribute_id,
                                          attribute_class,
                                          attribute_value );
        }
        catch ( RuntimeTypeException e )
        {
            throw new I18nIllegalArgumentException ( "SimpleMetadataBuilder [%metadata_builder%] could not create attribute field with id [%attriute_id%] class [%attribute_class%] value [%attribute_value%]",
                                                     "attribute_id", attribute_id,
                                                     "attribute_class", attribute_class,
                                                     "attribute_value", attribute_value );
        }

        // Can still thrown an I18nIllegalArgumentException:
        return this.attribute ( attribute_field );
    }


    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder#remove(java.lang.String)
     */
    @Override
    public Field remove (
                         String attribute_id
                         )
        throws I18nIllegalArgumentException
    {
        if ( attribute_id == null )
        {
            return Field.NULL;
        }

        final Field attribute_field;
        synchronized ( this.lock )
        {
            final Field found_field = this.attributes.get ( attribute_id );
            if ( found_field != null )
            {
                attribute_field = found_field;
            }
            else
            {
                attribute_field = Field.NULL;
            }

            final Field default_field = this.defaults.get ( attribute_id );
            if ( default_field != null )
            {
                // Restore the attribute to its default, never remove
                // default metadata values like hash or modified time.
                this.attributes.put ( attribute_id, default_field );
            }
            else
            {
                this.attributes.remove ( attribute_id );
            }
        }

        return attribute_field;
    }

    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder.build()
     */
    @Override
    public Metadata build ()
    {
        final Field [] snapshot;
        synchronized ( this.lock )
        {
            snapshot = new Field [ this.attributes.size () ];
            int s = 0;
            for ( String attribute_id : this.attributes.keySet () )
            {
                Field attribute_field = this.attributes.get ( attribute_id );
                snapshot [ s ] = attribute_field;
                s ++;
            }
        }

        Metadata metadata = new SimpleMetadata ( snapshot );
        return metadata;
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata.hash()
     */
    @Override
    public Hash hash ()
    {
        Field field = this.attribute ( Metadata.HASH );
        if ( field.equals ( Field.NULL ) )
        {
            return Hash.NONE;
        }

        return field.value ( Hash.class );
    }

    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder.hash(musaico.hash.Hash)
     */
    @Override
    public SimpleMetadataBuilder hash (
                                       Hash hash
                                       )
        throws I18nIllegalArgumentException
    {
        Field field =
            this.environment.create ( Metadata.HASH, Hash.class, hash );
        this.attribute ( field );

        return this;
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata.objectSystemType()
     */
    @Override
    public ObjectSystemTypeIdentifier objectSystemType ()
    {
        Field field = this.attribute ( Metadata.OBJECT_SYSTEM_TYPE );
        if ( field.equals ( Field.NULL ) )
        {
            return ObjectSystemTypeIdentifier.NONE;
        }

        return field.value ( ObjectSystemTypeIdentifier.class );
    }

    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder.objectSystemType(musaico.kernel.objectsystem.ObjectSystemTypeIdentifier)
     */
    @Override
    public SimpleMetadataBuilder objectSystemType (
                                                   ObjectSystemTypeIdentifier object_system_type
                                                   )
        throws I18nIllegalArgumentException
    {
        Field field =
            this.environment.create ( Metadata.OBJECT_SYSTEM_TYPE,
                                      Identifier.class, object_system_type );
        this.attribute ( field );

        return this;
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata.recordType()
     */
    @Override
    public RecordTypeIdentifier recordType ()
    {
        Field field = this.attribute ( Metadata.RECORD_TYPE );
        if ( field.equals ( Field.NULL ) )
        {
            return RecordTypeIdentifier.NONE;
        }

        return field.value ( RecordTypeIdentifier.class );
    }

    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder.recordType(musaico.kernel.objectsystem.RecordTypeIdentifier)
     */
    @Override
    public SimpleMetadataBuilder recordType (
                                             RecordTypeIdentifier record_type
                                             )
        throws I18nIllegalArgumentException
    {
        Field field =
            this.environment.create ( Metadata.RECORD_TYPE,
                                      Identifier.class, record_type );
        this.attribute ( field );

        return this;
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata.region()
     */
    @Override
    public Region region ()
    {
        Field field = this.attribute ( Metadata.REGION );
        if ( field.equals ( Field.NULL ) )
        {
            return this.space ().empty ();
        }

        return field.value ( Region.class );
    }

    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder.region(musaico.region.Region)
     */
    @Override
    public SimpleMetadataBuilder region (
                                         Region region
                                         )
        throws I18nIllegalArgumentException
    {
        Field field =
            this.environment.create ( Metadata.REGION, Region.class, region );
        this.attribute ( field );

        return this;
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata.sizeFieldsFree()
     */
    @Override
    public Size sizeFieldsFree ()
    {
        Field field = this.attribute ( Metadata.SIZE_FIELDS_FREE );
        if ( field.equals ( Field.NULL ) )
        {
            return this.space ().none ();
        }

        return field.value ( Size.class );
    }

    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder.sizeFieldsFree(musaico.region.Size)
     */
    @Override
    public SimpleMetadataBuilder sizeFieldsFree (
                                                 Size size_fields_free
                                                 )
        throws I18nIllegalArgumentException
    {
        Field field =
            this.environment.create ( Metadata.SIZE_FIELDS_FREE,
                                      Size.class, size_fields_free );
        this.attribute ( field );

        return this;
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata.sizeFieldsUsed()
     */
    @Override
    public Size sizeFieldsUsed ()
    {
        Field field = this.attribute ( Metadata.SIZE_FIELDS_USED );
        if ( field.equals ( Field.NULL ) )
        {
            return this.space ().none ();
        }

        return field.value ( Size.class );
    }

    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder.sizeFieldsUsed(musaico.region.Size)
     */
    @Override
    public SimpleMetadataBuilder sizeFieldsUsed (
                                                 Size size_fields_used
                                                 )
        throws I18nIllegalArgumentException
    {
        Field field =
            this.environment.create ( Metadata.SIZE_FIELDS_FREE,
                                      Size.class, size_fields_used );
        this.attribute ( field );

        return this;
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata.sizeRecordsFree()
     */
    @Override
    public Size sizeRecordsFree ()
    {
        Field field = this.attribute ( Metadata.SIZE_RECORDS_FREE );
        if ( field.equals ( Field.NULL ) )
        {
            return ArraySpace.STANDARD.none ();
        }

        return field.value ( Size.class );
    }

    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder.sizeRecordsFree(musaico.region.Size)
     */
    @Override
    public SimpleMetadataBuilder sizeRecordsFree (
                                                  Size size_records_free
                                                  )
        throws I18nIllegalArgumentException
    {
        Field field =
            this.environment.create ( Metadata.SIZE_FIELDS_FREE,
                                      Size.class, size_records_free );
        this.attribute ( field );

        return this;
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata.sizeRecordsUsed()
     */
    @Override
    public Size sizeRecordsUsed ()
    {
        Field field = this.attribute ( Metadata.SIZE_RECORDS_USED );
        if ( field.equals ( Field.NULL ) )
        {
            return ArraySpace.STANDARD.none ();
        }

        return field.value ( Size.class );
    }

    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder.sizeRecordsUsed(musaico.region.Size)
     */
    @Override
    public SimpleMetadataBuilder sizeRecordsUsed (
                                                  Size size_records_used
                                                  )
        throws I18nIllegalArgumentException
    {
        Field field =
            this.environment.create ( Metadata.SIZE_FIELDS_FREE,
                                      Size.class, size_records_used );
        this.attribute ( field );

        return this;
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata.space()
     */
    @Override
    public Space space ()
    {
        Field field = this.attribute ( Metadata.SPACE );
        if ( field.equals ( Field.NULL ) )
        {
            return ArraySpace.STANDARD;
        }

        return field.value ( Space.class );
    }

    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder.space(musaico.region.Space)
     */
    @Override
    public SimpleMetadataBuilder space (
                                        Space space
                                        )
        throws I18nIllegalArgumentException
    {
        Field field =
            this.environment.create ( Metadata.SPACE, Space.class, space );
        this.attribute ( field );

        return this;
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata.timeAccessed()
     */
    @Override
    public Time timeAccessed ()
    {
        Field field = this.attribute ( Metadata.TIME_ACCESSED );
        if ( field.equals ( Field.NULL ) )
        {
            return Time.NEVER;
        }

        return field.value ( Time.class );
    }

    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder.timeAccessed(musaico.time.Time)
     */
    @Override
    public SimpleMetadataBuilder timeAccessed (
                                               Time time_accessed
                                               )
        throws I18nIllegalArgumentException
    {
        Field field =
            this.environment.create ( Metadata.TIME_ACCESSED,
                                      Time.class, time_accessed );
        this.attribute ( field );

        return this;
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata.timeCreated()
     */
    @Override
    public Time timeCreated ()
    {
        Field field = this.attribute ( Metadata.TIME_CREATED );
        if ( field.equals ( Field.NULL ) )
        {
            return Time.NEVER;
        }

        return field.value ( Time.class );
    }

    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder.timeCreated(musaico.time.Time)
     */
    @Override
    public SimpleMetadataBuilder timeCreated (
                                              Time time_created
                                              )
        throws I18nIllegalArgumentException
    {
        Field field =
            this.environment.create ( Metadata.TIME_CREATED,
                                      Time.class, time_created );
        this.attribute ( field );

        return this;
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata.timeDeleted()
     */
    @Override
    public Time timeDeleted ()
    {
        Field field = this.attribute ( Metadata.TIME_DELETED );
        if ( field.equals ( Field.NULL ) )
        {
            return Time.NEVER;
        }

        return field.value ( Time.class );
    }

    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder.timeDeleted(musaico.time.Time)
     */
    @Override
    public SimpleMetadataBuilder timeDeleted (
                                              Time time_deleted
                                              )
        throws I18nIllegalArgumentException
    {
        Field field =
            this.environment.create ( Metadata.TIME_DELETED,
                                      Time.class, time_deleted );
        this.attribute ( field );

        return this;
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata.timeDirtied()
     */
    @Override
    public Time timeDirtied ()
    {
        Field field = this.attribute ( Metadata.TIME_DIRTIED );
        if ( field.equals ( Field.NULL ) )
        {
            return Time.NEVER;
        }

        return field.value ( Time.class );
    }

    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder.timeDirtied(musaico.time.Time)
     */
    @Override
    public SimpleMetadataBuilder timeDirtied (
                                              Time time_dirtied
                                              )
        throws I18nIllegalArgumentException
    {
        Field field =
            this.environment.create ( Metadata.TIME_DIRTIED,
                                      Time.class, time_dirtied );
        this.attribute ( field );

        return this;
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata.timeModified()
     */
    @Override
    public Time timeModified ()
    {
        Field field = this.attribute ( Metadata.TIME_MODIFIED );
        if ( field.equals ( Field.NULL ) )
        {
            return Time.NEVER;
        }

        return field.value ( Time.class );
    }

    /**
     * @see musaico.kernel.objectsystem.MetadataBuilder.timeModified(musaico.time.Time)
     */
    @Override
    public SimpleMetadataBuilder timeModified (
                                               Time time_modified
                                               )
        throws I18nIllegalArgumentException
    {
        Field field =
            this.environment.create ( Metadata.TIME_MODIFIED,
                                      Time.class, time_modified );
        this.attribute ( field );

        return this;
    }
}
