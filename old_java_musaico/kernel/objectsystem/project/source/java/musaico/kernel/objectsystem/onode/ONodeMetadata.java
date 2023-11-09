package musaico.kernel.objectsystem.onode;

import java.io.Serializable;


import musaico.field.Attribute;

import musaico.hash.Hash;

import musaico.io.Identifier;

import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordTypeIdentifier;
import musaico.kernel.objectsystem.RecordSecurityException;
import musaico.kernel.objectsystem.StructuredRecord;

import musaico.region.Region;
import musaico.region.Size;
import musaico.region.Space;

import musaico.security.Credentials;

import musaico.time.Time;


/**
 * <p>
 * An extension to the Record interface for keeping track of an ONode's metadata.
 * </p>
 *
 * <p>
 * Usage example:
 * </p>
 *
 * <pre>
 * ONodeMetadata metadata = ...;
 * Credentials credentials = ...;
 * Hash hash = metadata.readValue ( credentials,
 *                                  metadata.hash () );
 * </p>
 *
 *
 * <p>
 * In Java, because an object system can conceivably be distributed
 * over RMI, every ONodeMetadata must be Serializable in order to
 * play nicely over RMI.
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
public interface ONodeMetadata
    extends StructuredRecord, Serializable
{
    /** No ONode metadata at all.  Should really only be used
     *  by ONode.NONE, or as the tailpiece for Sequences of
     *  ONodeMetadata objects. */
    public static final ONodeMetadata NONE = new NoONodeMetadata ();




    /**
     * @return The meta-attribute pointing to the hash of the ONode's
     *         content, such as a SHA-1 hash.
     *         Note that the hash is meant to be fast and approximate,
     *         not <i> necessarily </i> a unique secure hash.  For example,
     *         if an ONode is thousands or millions of Fields in size,
     *         its hash might only take into consideration the size
     *         of the ONode and the first few and last few Fields.
     *         This approach is a compromise between getting a
     *         <i> good </i> hash to tell when any Field in the ONode
     *         has changed and getting a hash value <i> quickly </i>
     *         which is usually important.  To get a <i> good </i>
     *         hash you will need to hash the ONode yourself.
     *         Never null.
     */
    public abstract Attribute<Hash> metaHash ();


    /**
     * @return The meta-attribute pointing to the type of the object
     *         system (akin to the magic number of a file
     *         system in a UNIX-like operating system).
     *         An ONode should return its SuperBlock's
     *         object system factory reference.
     *         Always an ObjectSystemTypeIdentifier.
     *         Never null.
     */
    public abstract Attribute<Identifier> metaObjectSystemType ();


    /**
     * @return The meta-attribute pointing to the owner of
     *         the ONode.  For example, the owner might be
     *         a Credentials representing the user and the
     *         group which created the ONode; or it might be
     *         some more or less complicated scheme, such as
     *         just a user, or a particular process, and so on,
     *         depending on the security framework used by
     *         the kernel.  Never null.
     */
    public abstract Attribute<Credentials> metaOwner ();


    /**
     * @return The meta-attribute pointing to the type of record
     *         which the ONode describes.
     *         An ONode should return a hint as to which
     *         type of Record can be opened on it (such as
     *         Object.TYPE_ID, FlatRecord.TYPE_ID, and so on).
     *         A SuperBlock should return RecordTypeIdentifier.NONE
     *         since it is not open-able as a Record.
     *         Never null.
     */
    public abstract Attribute<RecordTypeIdentifier> metaRecordType ();


    /**
     * @return The meta-attribute pointing to the size and
     *         layout of Fields covered by the ONode.
     *         Never null.
     */
    public abstract Attribute<Region> metaRegion ();


    /**
     * @return The meta-attribute pointing to the number of
     *         Fields which are available to store
     *         data.  Never null.
     */
    public abstract Attribute<Size> metaSizeFieldsFree ();


    /**
     * @return The meta-attribute pointing to the total number
     *         of Fields used by an ONode.
     *         Never null.
     */
    public abstract Attribute<Size> metaSizeFieldsUsed ();


    /**
     * @return The meta-attribute pointing to the meta-attribute
     *         pointing to the number of
     *         Records which are free to be used
     *         by the ONode.
     *         Never null.
     */
    public abstract Attribute<Size> metaSizeRecordsFree ();


    /**
     * @return The meta-attribute pointing to the number of
     *         Records used by the ONode
     *         (one for a single ONode, many for an entire
     *         SuperBlock, and so on).  Never null.
     */
    public abstract Attribute<Size> metaSizeRecordsUsed ();


    /**
     * @return The meta-attribute pointing to the type of
     *         Positions, Regions and Sizes used
     *         to describe the layout of the contents of the
     *         ONode, such as a linear ArraySpace, or a time-indexed
     *         space, and so on.  Never null.
     */
    public abstract Attribute<Space> metaSpace ();


    /**
     * @return The meta-attribute pointing to the most
     *         recent time the ONode was accessed
     *         for either read or write.
     *         Defaults to the creation time.  Never null.
     */
    public abstract Attribute<Time> metaTimeAccessed ();


    /**
     * @return The meta-attribute pointing to the time
     *         the ONode was created.
     *         Defaults to Time.NEVER.  Never null.
     */
    public abstract Attribute<Time> metaTimeCreated ();


    /**
     * @return The meta-attribute pointing to the time
     *         the ONode was deleted.
     *         Defaults to Time.NEVER.  Never null.
     */
    public abstract Attribute<Time> metaTimeDeleted ();


    /**
     * @return The meta-attribute pointing to the oldest time
     *         the ONode was dirtied
     *         without since being cleaned (virtual memory).
     *         Defaults to Time.NEVER for read-only ONodes
     *         and ONodes which are clean.  Neve null.
     */
    public abstract Attribute<Time> metaTimeDirtied ();


    /**
     * @return The meta-attribute pointing to the most recent
     *         time the ONode was written to.
     *         Defaults to the creation time.  Never null.
     */
    public abstract Attribute<Time> metaTimeModified ();


    /* !!! ???
        long f_type;
        long f_bsize;
        u64 f_blocks;
        u64 f_bfree;
        u64 f_bavail; ??????
        u64 f_files;
        u64 f_ffree;
        __kernel_fsid_t f_fsid;
        long f_namelen;
        long f_frsize;
        long f_spare[5];
        !!! */
}
