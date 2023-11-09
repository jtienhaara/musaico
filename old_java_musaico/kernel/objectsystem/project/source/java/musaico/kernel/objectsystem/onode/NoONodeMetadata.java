package musaico.kernel.objectsystem.onode;

import java.io.Serializable;


import musaico.field.Attribute;
import musaico.field.AttributePositionOrder;

import musaico.hash.Hash;

import musaico.io.Identifier;
import musaico.io.Sequence;

import musaico.kernel.objectsystem.NoRecord;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordTypeIdentifier;

import musaico.region.Region;
import musaico.region.Size;
import musaico.region.Space;

import musaico.security.Credentials;

import musaico.time.Time;


/**
 * <p>
 * Metadata which cannot be read or written, for example for
 * ONode.NONE.
 * </p>
 *
 *
 * <p>
 * Because object systems can conceivably be distributed and using
 * RMI, every Record must be Serializable in order to play nicely
 * over RMI.
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
public class NoONodeMetadata
    extends NoRecord
    implements ONodeMetadata, Serializable
{
    /**
     * @see musaico.kernel.objectsystem.StructuredRecord#attributes()
     */
    @Override
    public Sequence<Attribute> attributes ()
    {
        return new Sequence<Attribute> (
                                        Attribute.NONE,
                                        this.metaHash (),
                                        this.metaObjectSystemType (),
                                        this.metaOwner (),
                                        this.metaRecordType (),
                                        this.metaRegion (),
                                        this.metaSizeFieldsFree (),
                                        this.metaSizeFieldsUsed (),
                                        this.metaSizeRecordsFree (),
                                        this.metaSizeRecordsUsed (),
                                        this.metaSpace (),
                                        this.metaTimeAccessed (),
                                        this.metaTimeCreated (),
                                        this.metaTimeDeleted (),
                                        this.metaTimeDirtied (),
                                        this.metaTimeModified ()
                                        )
            .sort ( new AttributePositionOrder ( this.space () ) );
    }

    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaHash()
     */
    @Override
    public Attribute<Hash> metaHash ()
    {
        return new Attribute<Hash> ( "hash",
                                     Hash.class,
                                     this.space ().position ( 0L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaObjectSystemType()
     */
    @Override
    public Attribute<Identifier> metaObjectSystemType ()
    {
        return new Attribute<Identifier> ( "object_system_type",
                                           Identifier.class,
                                           this.space ().position ( 1L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaOwner()
     */
    @Override
    public Attribute<Credentials> metaOwner ()
    {
        return new Attribute<Credentials> ( "owner",
                                            Credentials.class,
                                            this.space ().position ( 2L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaRecordType()
     */
    @Override
    public Attribute<RecordTypeIdentifier> metaRecordType ()
    {
        return new Attribute<RecordTypeIdentifier> ( "record_type",
                                                     RecordTypeIdentifier.class,
                                                     this.space ().position ( 3L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaRegion()
     */
    @Override
    public Attribute<Region> metaRegion ()
    {
        return new Attribute<Region> ( "region",
                                       Region.class,
                                       this.space ().position ( 4L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaSizeFieldsFree()
     */
    @Override
    public Attribute<Size> metaSizeFieldsFree ()
    {
        return new Attribute<Size> ( "fields_free",
                                     Size.class,
                                     this.space ().position ( 5L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaSizeFieldsUsed()
     */
    @Override
    public Attribute<Size> metaSizeFieldsUsed ()
    {
        return new Attribute<Size> ( "fields_used",
                                     Size.class,
                                     this.space ().position ( 6L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaSizeRecordsFree()
     */
    @Override
    public Attribute<Size> metaSizeRecordsFree ()
    {
        return new Attribute<Size> ( "records_free",
                                     Size.class,
                                     this.space ().position ( 7L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaSizeRecordsUsed()
     */
    @Override
    public Attribute<Size> metaSizeRecordsUsed ()
    {
        return new Attribute<Size> ( "records_used",
                                     Size.class,
                                     this.space ().position ( 8L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaSpace()
     */
    @Override
    public Attribute<Space> metaSpace ()
    {
        return new Attribute<Space> ( "space",
                                      Space.class,
                                      this.space ().position ( 9L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaTimeAccessed()
     */
    @Override
    public Attribute<Time> metaTimeAccessed ()
    {
        return new Attribute<Time> ( "time_accessed",
                                     Time.class,
                                     this.space ().position ( 10L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaTimeCreated()
     */
    @Override
    public Attribute<Time> metaTimeCreated ()
    {
        return new Attribute<Time> ( "time_created",
                                     Time.class,
                                     this.space ().position ( 11L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaTimeDeleted()
     */
    @Override
    public Attribute<Time> metaTimeDeleted ()
    {
        return new Attribute<Time> ( "time_deleted",
                                     Time.class,
                                     this.space ().position ( 12L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaTimeDirtied()
     */
    @Override
    public Attribute<Time> metaTimeDirtied ()
    {
        return new Attribute<Time> ( "time_dirtied",
                                     Time.class,
                                     this.space ().position ( 13L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaTimeModified()
     */
    @Override
    public Attribute<Time> metaTimeModified ()
    {
        return new Attribute<Time> ( "time_modified",
                                     Time.class,
                                     this.space ().position ( 14L ) );
    }




    /**
     * @see musaico.kernel.objectsystem.StructuredRecord#readValue(musaico.security.Credentials,musaico.kernel.objectsystem.Attribute)
     */
    @Override
    public <VALUE extends Serializable>
                          VALUE readValue (
                                           Credentials credentials,
                                           Attribute<VALUE> attribute
                                           )
        throws RecordOperationException,
               ONodeSecurityException
    {
        throw new RecordOperationException ( "NoONodeMetadata [%no_onode_metadata%] cannot read or write for [%credentials%] at [%position%]",
                                             "no_onode_metadata", this,
                                             "credentials", credentials,
                                             "position", attribute.position () );
    }


    /**
     * @see musaico.kernel.objectsystem.StructuredValue#writeValue(musaico.security.Credentials,musaico.kernel.objectsystem.Attribute,java.io.Serializable)
     */
    @Override
    public <VALUE extends Serializable>
                          void writeValue (
                                           Credentials credentials,
                                           Attribute<VALUE> attribute,
                                           VALUE value
                                           )
        throws RecordOperationException,
               ONodeSecurityException
    {
        throw new RecordOperationException ( "NoONodeMetadata [%no_onode_metadata%] cannot read or write for [%credentials%] at [%position%]",
                                             "no_onode_metadata", this,
                                             "credentials", credentials,
                                             "position", attribute.position () );
    }
}
