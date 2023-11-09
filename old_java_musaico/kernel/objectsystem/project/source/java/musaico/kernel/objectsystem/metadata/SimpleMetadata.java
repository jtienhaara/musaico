package musaico.kernel.objectsystem.metadata;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import musaico.field.Field;

import musaico.hash.Hash;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Order;

import musaico.kernel.objectsystem.Metadata;
import musaico.kernel.objectsystem.ObjectSystemTypeIdentifier;
import musaico.kernel.objectsystem.RecordTypeIdentifier;

import musaico.region.Region;
import musaico.region.Size;
import musaico.region.Space;

import musaico.time.Time;


/**
 * <p>
 * A straightforward no-nonsense Metadata implementation.
 * </p>
 *
 * <p>
 * A SimpleMetadata is a snapshot of the metadata for an ONode
 * or SuperBlock at a specific point in time.  It will not
 * change over time.  To get the latest snapshot, request
 * the metadata again.
 * </p>
 *
 *
 * <p>
 * In Java, each Metadata must be Serializable in order to
 * play nicely across RMI.
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
public class SimpleMetadata
    implements Metadata, Serializable
{
    /** The lookup of metadata fields by attribute name. */
    private final Map<String,Field> attributes;


    /**
     * <p>
     * Creates a SimpleMetadata with the specified Fields.
     * </p>
     *
     * <p>
     * The SimpleMetadata class does not check for potential
     * typecasting exceptions, so make sure your input
     * data is correct!  For example, see the standard
     * metadata attribute types returned by the methods.
     * </p>
     *
     * @param attribute_fields The metadata to store in this
     *                         snapshot.  Must not be null.
     *                         Must not contain any null elements.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public SimpleMetadata (
                           Field [] attribute_fields
                           )
    {
        if ( attribute_fields == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleMetadata with attribute fields [%attribute_fields%]",
                                                     "attribute_fields", attribute_fields );
        }

        this.attributes = new HashMap<String,Field> ();
        for ( Field attribute_field : attribute_fields )
        {
            if ( attribute_field == null )
            {
                throw new I18nIllegalArgumentException ( "Cannot create a SimpleMetadata with attribute fields [%attribute_fields%]",
                                                         "attribute_fields", attribute_fields );
            }

            this.attributes.put ( attribute_field.id (), attribute_field );
        }
    }


    /**
     * @see musaico.kernel.objectsystem.Metadata#all()
     */
    @Override
    public Field [] all ()
    {
        return SimpleMetadata.getSortedMetadata ( this.attributes );
    }


    /**
     * @see musaico.kernel.objectsystem.Metadata#attribute()
     */
    @Override
    public Field attribute (
                            String attribute_id
                            )
    {
        Field attribute_field = this.attributes.get ( attribute_id );
        if ( attribute_field == null )
        {
            return Field.NULL;
        }

        return attribute_field;
    }


    /**
     * <p>
     * Given the specified Map of Fields by attribute ID,
     * creates a new array and populates it in dictionary
     * sorted order by attribute ID of the contents of the map.
     * </p>
     *
     * <p>
     * The map MUST be synchronized before calling this method.
     * </p>
     */
    protected static Field [] getSortedMetadata (
                                                 Map<String,Field> metadata_by_id
                                                 )
    {
        // First sort the metadata ids by dictionary order.
        List<String> sorted_ids = new ArrayList<String> ();
        sorted_ids.addAll ( metadata_by_id.keySet () );
        Collections.sort ( sorted_ids, Order.DICTIONARY );

        // Now retrieve the metadata fields in dictionary order.
        Field [] metadata_fields = new Field [ metadata_by_id.size () ];
        int f = 0;
        for ( String metadata_id : sorted_ids )
        {
            metadata_fields [ f ] = metadata_by_id.get ( metadata_id );
            f ++;
        }

        return metadata_fields;
    }


    /**
     * @see musaico.kernel.objectsystem.Metadata#hash()
     */
    @Override
    public Hash hash ()
    {
        return this.attribute ( Metadata.HASH ).value ( Hash.class );
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata#objectSystemType()
     */
    @Override
    public ObjectSystemTypeIdentifier objectSystemType ()
    {
        return
            this.attribute ( Metadata.OBJECT_SYSTEM_TYPE )
            .value ( ObjectSystemTypeIdentifier.class );
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata#recordType()
     */
    public RecordTypeIdentifier recordType ()
    {
        return
            this.attribute ( Metadata.RECORD_TYPE )
            .value ( RecordTypeIdentifier.class );
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata#region()
     */
    @Override
    public Region region ()
    {
        return this.attribute ( Metadata.REGION ).value ( Region.class );
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata#region()
     */
    public Size sizeFieldsFree ()
    {
        return this.attribute ( Metadata.SIZE_FIELDS_FREE )
            .value ( Size.class );
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata#sizeFieldsUsed()
     */
    @Override
    public Size sizeFieldsUsed ()
    {
        return this.attribute ( Metadata.SIZE_FIELDS_USED )
            .value ( Size.class );
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata#sizeRecordsFree()
     */
    @Override
    public Size sizeRecordsFree ()
    {
        return this.attribute ( Metadata.SIZE_RECORDS_FREE )
            .value ( Size.class );
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata#sizeRecordsUsed()
     */
    @Override
    public Size sizeRecordsUsed ()
    {
        return this.attribute ( Metadata.SIZE_RECORDS_USED )
            .value ( Size.class );
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata#space()
     */
    @Override
    public Space space ()
    {
        return this.attribute ( Metadata.SPACE ).value ( Space.class );
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata#timeAccessed()
     */
    @Override
    public Time timeAccessed ()
    {
        return this.attribute ( Metadata.TIME_ACCESSED )
            .value ( Time.class );
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata#timeCreated()
     */
    @Override
    public Time timeCreated ()
    {
        return this.attribute ( Metadata.TIME_CREATED )
            .value ( Time.class );
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata#timeDeleted()
     */
    @Override
    public Time timeDeleted ()
    {
        return this.attribute ( Metadata.TIME_DELETED )
            .value ( Time.class );
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata#timeDirtied()
     */
    @Override
    public Time timeDirtied ()
    {
        return this.attribute ( Metadata.TIME_DIRTIED )
            .value ( Time.class );
    }

    /**
     * @see musaico.kernel.objectsystem.Metadata#timeModified()
     */
    @Override
    public Time timeModified ()
    {
        return this.attribute ( Metadata.TIME_MODIFIED )
            .value ( Time.class );
    }
}
