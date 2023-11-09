package musaico.kernel.objectsystem;

import java.io.Serializable;


import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;

import musaico.kernel.memory.Segment;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Space;

import musaico.security.Security;


/**
 * <p>
 * Provides access to an ONode or Driver for flat record type operations.
 * </p>
 *
 * <p>
 * These operations are useful for more complex structures as well,
 * so apart from flat records, this class is extended to create
 * hierarchical objects, symbolic links, drivers, and so on.
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
 * Copyright (c) 2009, 2010, 2011 Johann Tienhaara
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
public interface Record
    extends Serializable
{
    /** No record.  All the open, close, read, write and so on
     *  operations fail. */
    public static final Record NONE = new NoRecord ();


    /**
     * <p>
     * Decrements the reference count for this record, cleans up
     * anything needed, and, if the last reference has been closed,
     * then also releases this record for good.
     * </p>
     *
     * @param cursor The Cursor connection to this record.
     *               The cursor might have opened this record
     *               to stream in fields from a driver, or to
     *               randomly access a persistent object, and so on.
     *               Must not be null.
     *
     * @throws RecordSecurityException If the specified Cursor
     *                                 does not have permission
     *                                 to perform this operation.
     *
     * @throws RecordOperationException if anything goes horribly wrong.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (null and so on).
     */
    public abstract void close (
                                Cursor cursor
                                )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException;


    /**
     * <p>
     * Maps the record into the specified segment of memory.
     * </p>
     *
     * <p>
     * Handy for large quantities of data, since a paged memory
     * segment will only swap in the data from the driver as
     * it deems necessary.
     * </p>
     *
     * <p>
     * Warning: distributed object system designers must be
     * very careful when implementing mmap!  If a Record which
     * is mmap'ed to a local-only Segment moves to another node,
     * then any references to the Segment will be serialized,
     * rendering them useless unless the other node does some
     * extra work to acquire the Segment locally as well.
     * In general distributed object systems should not allow
     * an mmap'ed Record to be distributed away from its source
     * node.
     * </p>
     *
     * @param cursor The Cursor connection to this record.
     *               The cursor might have opened this record
     *               to stream in fields from a driver, or to
     *               randomly access a persistent object, and so on.
     *               Must not be null.
     *
     * @param segment The segment of memory into which this record's
     *                data will be mapped.  The mapping will page
     *                in and synchronize data as it is deemed
     *                necessary (that is, either on demand or when
     *                readahead is considered advantageous by the
     *                record and the underlying driver).
     *                Must not be null.
     *
     * @throws RecordSecurityException If the specified Cursor
     *                                 does not have permission
     *                                 to perform this operation.
     *
     * @throws RecordOperationException if anything goes horribly wrong.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (null and so on).
     */
    public abstract void mmap (
                               Cursor cursor,
                               Segment segment
                               )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException;


    /**
     * <p>
     * Opens the specified record.
     * </p>
     *
     * <p>
     * Be sure to call close () when done with this Record.
     * </p>
     *
     * @param cursor The Cursor connection to this record.
     *               The cursor might have opened this record
     *               to stream in fields from a driver, or to
     *               randomly access a persistent object, and so on.
     *               Must not be null.
     *
     * @throws RecordSecurityException If the specified Cursor
     *                                 does not have permission
     *                                 to perform this operation.
     *
     * @throws RecordOperationException if anything goes horribly wrong.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (null and so on).
     */
    public abstract void open (
                               Cursor cursor
                               )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException;


    /**
     * <p>
     * Polls this record.
     * </p>
     *
     * @param cursor The Cursor connection to this record.
     *               The cursor might have opened this record
     *               to stream in fields from a driver, or to
     *               randomly access a persistent object, and so on.
     *               Must not be null.
     *
     * @param request The parameters to poll for.  Must not be null.
     *
     * @param response The response handler for when the Cursor
     *                 becomes readable / writable and so on.
     *                 Must not be null.
     *
     * @throws RecordSecurityException If the specified Cursor
     *                                 does not have permission
     *                                 to perform this operation.
     *
     * @throws RecordOperationException if anything goes horribly wrong.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (null and so on).
     */
    public abstract void poll (
                               Cursor cursor,
                               PollRequest request,
                               PollResponse response
                               )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException;


    /**
     * <p>
     * Reads fields from the record, starting at the current Position
     * of the Cursor, and stopping when the Region of the buffer
     * has been filled.
     * </p>
     *
     * <p>
     * The number of fields actually read in is returned.
     * </p>
     *
     * @param cursor The Cursor connection to this record.
     *               The cursor might have opened this record
     *               to stream in fields from a driver, or to
     *               randomly access a persistent object, and so on.
     *               Must not be null.
     *
     * @param read_fields The buffer of Fields into which the
     *                    contents of this record shall be read.
     *                    Must not be null.
     *
     * @param read_into_region The region of the read_fields buffer
     *                         into which fields shall be read.
     *                         Typically just
     *                         <code> read_fields.region () </code>.
     *                         Must not be null.
     *
     * @return The actual region of Fields in the specified buffer
     *         into which this Record's data was written.
     *         Can be an empty region.  Never null.
     *
     * @throws RecordSecurityException If the specified Cursor
     *                                 does not have permission
     *                                 to perform this operation.
     *
     * @throws RecordOperationException if anything goes horribly wrong.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (null and so on).
     */
    public abstract Region read (
                                 Cursor cursor,
                                 Buffer read_fields,
                                 Region read_into_region
                                 )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException;


    /**
     * <p>
     * Returns the size and layout of the Fields in this Record,
     * as long as the Record has been opened by at least one
     * Cursor.
     * </p>
     *
     * <p>
     * The size might not be known in advance, in which case
     * the maximum possible size of Region shall be returned.
     * </p>
     *
     * @param cursor The Cursor connection to this record.
     *               The cursor might have opened this record
     *               to stream in fields from a driver, or to
     *               randomly access a persistent object, and so on.
     *               Must not be null.  The cursor is important
     *               especially in determining the region of
     *               a record which might have changed during an
     *               as-yet-uncommitted transaction.
     *
     * @return This Record's region.  Never null.  May be
     *         a region of maximum size, if the size is not known
     *         in advance (for example during a streaming read).
     *
     * @throws RecordSecurityException If the specified Cursor
     *                                 does not have permission
     *                                 to perform this operation.
     *
     * @throws RecordException If the record cannot determine its
     *                         own region (for example because an
     *                         underlying I/O operation failed or
     *                         a network connection has gone down).
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (null and so on).
     */
    public abstract Region region (
                                   Cursor cursor
                                   )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException;


    /**
     * <p>
     * Returns the security for this Record, which
     * determines who is (and who isn't) allowed to read
     * and write and so on.
     * </p>
     *
     * @return The Security for this Record.  Never null.
     */
    public abstract Security<RecordFlag> security ();


    /**
     * <p>
     * Seeks to the specified position within the record.
     * </p>
     *
     * <p>
     * To seek from the start of the record, pass
     * <code>record.region ( cursor ).start ()</code>.  From the end, pass
     * <code>record.region ( cursor ).end ()</code>.  Or from the
     * existing position of the Cursor pointing to this record, pass
     * <code>record.region ( cursor ).step ( cursor.position (), offset )</code>.
     * </p>
     *
     * @param cursor The Cursor connection to this record.
     *               The cursor might have opened this record
     *               to stream in fields from a driver, or to
     *               randomly access a persistent object, and so on.
     *               Must not be null.
     *
     * @param position The position to seek to within the Record.
     *                 Must not be null.
     *
     * @return The actual Field Position reached.
     *
     * @throws RecordSecurityException If the specified Cursor
     *                                 does not have permission
     *                                 to perform this operation.
     *
     * @throws RecordOperationException If the specified Position is
     *                                  invalid or not reachable (for
     *                                  example a streaming Record
     *                                  may not be able to seek backwards
     *                                  to the beginnig), or if anything
     *                                  goes horribly wrong.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (null and so on).
     */
    public abstract Position seek (
                                   Cursor cursor,
                                   Position position
                                   )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException;


    /**
     * <p>
     * Returns the space of positions, regions and sizes used to
     * navigate through data in this Record.
     * </p>
     *
     * <p>
     * For example, some Records might index data by array positions,
     * while others might use time indices.
     * </p>
     *
     * @return This Record's space of positions, regions and sizes.
     *         Never null.
     */
    public abstract Space space ();


    /**
     * <p>
     * Flushes this record's data and metadata.
     * </p>
     *
     * <p>
     * Note that the parent object of the record might still need
     * flushing (for example, if the specified record is a new
     * child record inside an existing hierarchical object).
     * </p>
     *
     * @param cursor The Cursor connection to this record.
     *               The cursor might have opened this record
     *               to stream in fields from a driver, or to
     *               randomly access a persistent object, and so on.
     *               Must not be null.
     *
     * @param is_metadata_only True if only this record's metadata
     *                         (modified time and so on) should be
     *                         synchronized; false if the entire
     *                         record's data should be synchronized.
     *
     * @throws RecordSecurityException If the specified Cursor
     *                                 does not have permission
     *                                 to perform this operation.
     *
     * @throws RecordOperationException if anything goes horribly wrong.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (null and so on).
     */
    public abstract void sync (
                               Cursor cursor,
                               boolean is_metadata_only
                               )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException;


    /**
     * <p>
     * Writes fields to the record, starting from the current Position
     * of the Cursor, and writing until the Region of fields in the
     * Buffer has been written out.
     * </p>
     *
     * <p>
     * The number of fields actually written is returned.
     * </p>
     *
     * @param cursor The Cursor connection to this record.
     *               The cursor might have opened this record
     *               to stream in fields from a driver, or to
     *               randomly access a persistent object, and so on.
     *               Must not be null.
     *
     * @param write_fields The Buffer of data to write out to this
     *                     Record.  Must not be null.
     *
     * @param write_from_region The region of the write_fields Buffer
     *                          to write out to this record, such as
     *                          <code> write_fields.region ( cursor ) </code>.
     *                          Must not be null.
     *
     * @return The actual region of Fields from the specified Buffer
     *         which were written out to this Record.  Never null.
     *         Never NoSuchRegion or UnknownRegion.
     *
     * @throws RecordSecurityException If the specified Cursor
     *                                 does not have permission
     *                                 to perform this operation.
     *
     * @throws RecordOperationException if anything goes horribly wrong.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (null and so on).
     */
    public abstract Region write (
                                  Cursor cursor,
                                  Buffer write_fields,
                                  Region write_from_region
                                  )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException;
}
