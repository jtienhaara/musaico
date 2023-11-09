package musaico.kernel_modules.objectsystems.gnosys;

import java.io.Serializable;


import musaico.field.Attribute;

import musaico.hash.Hash;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;

import musaico.kernel.common.records.SegmentBackedRecord;

import musaico.kernel.memory.Segment;

import musaico.kernel.objectsystem.Record;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordTypeIdentifier;
import musaico.kernel.objectsystem.RecordSecurityException;

import musaico.kernel.objectsystem.objectsystemtype.ObjectSystemTypeIdentifier;

import musaico.kernel.objectsystem.onode.ONodeMetadata;

import musaico.region.Region;
import musaico.region.Size;
import musaico.region.Space;

import musaico.region.array.ArraySpace;

import musaico.security.Credentials;
import musaico.security.Security;

import musaico.time.Time;


/**
 * <p>
 * Metadata for ONodes in a Gnosys object system.
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
public class GnONodeMetadata
    extends SegmentBackedRecord
    implements ONodeMetadata, Serializable
{
    /**
     * <p>
     * Creates a new metadata area for a gnosys ONode
     * backed by paged segment data.
     * </p>
     *
     * @param segment The segment which will provide data for this metadata.
     *                Must not be null.
     *
     * @param security The security for this gnosys metadata,
     *                 which dictates who is (and who is not) allowed
     *                 to open, close, read or write this record.
     *                 Must not be null.
     */
    public GnONodeMetadata (
                            Segment segment,
                            Security<RecordFlag> security
                            )
    {
        super ( segment, security );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeMetadata#metaHash()
     */
    @Override
    public Attribute<Hash> metaHash ()
    {
	return HASH_ATTRIBUTE;
    }
    private static final Attribute<Hash> HASH_ATTRIBUTE =
	new Attribute<Hash> ( "hash", Hash.class,
			      ArraySpace.STANDARD.position ( 0L ) );


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#metaObjectSystemType()
     */
    @Override
    public Attribute<Identifier> metaObjectSystemType ()
    {
	return OBJECT_SYSTEM_TYPE_ATTRIBUTE;
    }
    private static final Attribute<Identifier>
	OBJECT_SYSTEM_TYPE_ATTRIBUTE =
	new Attribute<Identifier> ( "object_system_type",
                                    Identifier.class,
                                    ArraySpace.STANDARD.position ( 1L ) );


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#metaOwner()
     */
    @Override
    public Attribute<Credentials> metaOwner ()
    {
	return OWNER_ATTRIBUTE;
    }
    private static final Attribute<Credentials> OWNER_ATTRIBUTE =
	new Attribute<Credentials> ( "owner", Credentials.class,
				     ArraySpace.STANDARD.position ( 2L ) );


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#metaRecordType)
     */
    @Override
    public Attribute<RecordTypeIdentifier> metaRecordType ()
    {
	return RECORD_TYPE_ATTRIBUTE;
    }
    private static final Attribute<RecordTypeIdentifier>
        RECORD_TYPE_ATTRIBUTE =
	new Attribute<RecordTypeIdentifier> ( "type",
                                              RecordTypeIdentifier.class,
                                              ArraySpace.STANDARD.position ( 3L ) );


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#metaRegion()
     */
    @Override
    public Attribute<Region> metaRegion ()
    {
	return REGION_ATTRIBUTE;
    }
    private static final Attribute<Region> REGION_ATTRIBUTE =
	new Attribute<Region> ( "region", Region.class,
                                ArraySpace.STANDARD.position ( 4L ) );


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#metaSizeFieldsFree()
     */
    @Override
    public Attribute<Size> metaSizeFieldsFree ()
    {
	return SIZE_FIELDS_FREE_ATTRIBUTE;
    }
    private static final Attribute<Size> SIZE_FIELDS_FREE_ATTRIBUTE =
	new Attribute<Size> ( "size_fields_free", Size.class,
			      ArraySpace.STANDARD.position ( 5L ) );


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#metaSizeFieldsUsed()
     */
    @Override
    public Attribute<Size> metaSizeFieldsUsed ()
    {
	return SIZE_FIELDS_USED_ATTRIBUTE;
    }
    private static final Attribute<Size> SIZE_FIELDS_USED_ATTRIBUTE =
	new Attribute<Size> ( "size_fields_used", Size.class,
			      ArraySpace.STANDARD.position ( 6L ) );


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#metaSizeRecordsFree()
     */
    @Override
    public Attribute<Size> metaSizeRecordsFree ()
    {
	return SIZE_RECORDS_FREE_ATTRIBUTE;
    }
    private static final Attribute<Size> SIZE_RECORDS_FREE_ATTRIBUTE =
	new Attribute<Size> ( "size_records_free", Size.class,
			      ArraySpace.STANDARD.position ( 7L ) );


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#metaSizeRecordsUsed()
     */
    @Override
    public Attribute<Size> metaSizeRecordsUsed ()
    {
	return SIZE_RECORDS_USED_ATTRIBUTE;
    }
    private static final Attribute<Size> SIZE_RECORDS_USED_ATTRIBUTE =
	new Attribute<Size> ( "size_records_used", Size.class,
			      ArraySpace.STANDARD.position ( 8L ) );


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#metaSpace()
     */
    @Override
    public Attribute<Space> metaSpace ()
    {
	return SPACE_ATTRIBUTE;
    }
    private static final Attribute<Space> SPACE_ATTRIBUTE =
	new Attribute<Space> ( "space", Space.class,
                               ArraySpace.STANDARD.position ( 9L ) );


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#metaTimeAccessed()
     */
    @Override
    public Attribute<Time> metaTimeAccessed ()
    {
	return TIME_ACCESSED_ATTRIBUTE;
    }
    private static final Attribute<Time> TIME_ACCESSED_ATTRIBUTE =
	new Attribute<Time> ( "time_accessed", Time.class,
			      ArraySpace.STANDARD.position ( 10L ) );


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#metaTimeCreated()
     */
    @Override
    public Attribute<Time> metaTimeCreated ()
    {
	return TIME_CREATED_ATTRIBUTE;
    }
    private static final Attribute<Time> TIME_CREATED_ATTRIBUTE =
	new Attribute<Time> ( "time_created", Time.class,
			      ArraySpace.STANDARD.position ( 11L ) );


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#metaTimeDeleted()
     */
    @Override
    public Attribute<Time> metaTimeDeleted ()
    {
	return TIME_DELETED_ATTRIBUTE;
    }
    private static final Attribute<Time> TIME_DELETED_ATTRIBUTE =
	new Attribute<Time> ( "time_deleted", Time.class,
			      ArraySpace.STANDARD.position ( 12L ) );


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#metaTimeDirtied()
     */
    @Override
    public Attribute<Time> metaTimeDirtied ()
    {
	return TIME_DIRTIED_ATTRIBUTE;
    }
    private static final Attribute<Time> TIME_DIRTIED_ATTRIBUTE =
	new Attribute<Time> ( "time_dirtied", Time.class,
			      ArraySpace.STANDARD.position ( 13L ) );


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#metaTimeModified()
     */
    @Override
    public Attribute<Time> metaTimeModified ()
    {
	return TIME_MODIFIED_ATTRIBUTE;
    }
    private static final Attribute<Time> TIME_MODIFIED_ATTRIBUTE =
	new Attribute<Time> ( "time_modified", Time.class,
			      ArraySpace.STANDARD.position ( 14L ) );


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#readValue(musaico.security.Credentials,musaico.field.Attribute)
     */
    @Override
    public <VALUE extends Serializable>
                                   VALUE readValue (
                                                    Credentials credentials,
                                                    Attribute<VALUE> attribute
                                                    )
        throws RecordOperationException,
               RecordSecurityException
    {
	throw new I18nIllegalArgumentException ( "!!! NOT YET IMPLEMENTED" );
    }


    /**
     * @see musaico.kernel.objectsystem.onode.ONodeData#writeValue(musaico.security.Credentials,musaico.field.Attribute,java.lang.Object)
     */
    @Override
    public <VALUE extends Serializable>
                                   void writeValue (
                                                    Credentials credentials,
                                                    Attribute<VALUE> attribute,
                                                    VALUE value
                                                    )
        throws RecordOperationException,
               RecordSecurityException
    {
	throw new I18nIllegalArgumentException ( "!!! NOT YET IMPLEMENTED" );
    }
}
