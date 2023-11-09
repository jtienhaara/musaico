package musaico.kernel.objectsystem;

import java.io.Serializable;


import musaico.field.Attribute;

import musaico.io.Sequence;

import musaico.security.Credentials;


/**
 * <p>
 * A Record with a specific structure (order and types of fields).
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
public interface StructuredRecord
    extends Record, Serializable
{
    /**
     * <p>
     * Returns the sequence of Attributes (ordered by Position)
     * forming the structure which this Record implements.
     * </p>
     *
     * @return The structure of this record.  Never null.
     *         Never contains any null elements.
     */
    public abstract Sequence<Attribute> attributes ();

    /**
     * <p>
     * Reads one specific value from this StructuredRecord.
     * </p>
     *
     * <p>
     * Be aware that this call can block, because this
     * StructuredRecord is quite possibly built on top of a
     * virtual Segment with paging and swapping.
     * </p>
     *
     * @param credentials Who is requesting the read from this
     *                    StructuredRecord.  Typically either a user
     *                    or a module identifier.  For example,
     *                    during a Cursor operation,
     *                    <code> cursor.credentials () </code>.
     *                    Must not be null.
     *
     * @param attribute The attribute from the structure to read
     *                  from this record.  Must contain a Position
     *                  in this StructuredRecord's Space, such as
     *                  an ArrayPosition in an ArraySpace, or a
     *                  TimePosition in a TimeSpace, and so on.
     *                  Must be within this StructuredRecord's Region.
     *                  Must not be null.
     *
     * @return The value of the Field representing the structured
     *         attribute in this StructuredRecord, cast to the
     *         attribute's type.  Can be Field.NULL.  Never null.
     *
     * @throws RecordOperationException If the underlying Segment
     *                                  cannot be read properly,
     *                                  or if the Field is read but
     *                                  cannot be cast to the desired
     *                                  attribute type.
     *                                  For example if swapping is
     *                                  required but the swap driver
     *                                  encounters an I/O error, a
     *                                  RecordOperationException
     *                                  will be thrown.
     *
     * @throws RecordSecurityException If the specified credentials
     *                                 are not permitted to read the
     *                                 specified Field from this
     *                                 ONodeMetadata.
     */
    public abstract <VALUE extends Serializable>
                                   VALUE readValue (
                                                    Credentials credentials,
                                                    Attribute<VALUE> attribute
                                                    )
        throws RecordOperationException,
               RecordSecurityException;


    /**
     * <p>
     * Overwrites one specific Field in this StructuredRecord.
     * </p>
     *
     * <p>
     * Be aware that this call can block, because this StructuredRecord
     * is quite possibly built on top of a virtual Segment
     * with paging and swapping.
     * </p>
     *
     * @param credentials Who is requesting the write to this
     *                    StructuredRecord.  Typically either a user
     *                    or a module identifier.  For example,
     *                    during a Cursor operation,
     *                    <code> cursor.credentials () </code>.
     *                    Must not be null.
     *
     * @param attribute The attribute to overwrite in this StructuredRecord.
     *                  Must contain a Position within the Region
     *                  covered by this StructuredRecord.  Must contain a
     *                  valid Position for this StructuredRecord's
     *                  Space (such as an ArrayPosition in an
     *                  ArraySpace, or a TimePosition in a
     *                  TimeSpace, and so on).  Must not be null.
     *
     * @param value The value to write.  Must not be null.
     *
     * @throws RecordOperationException If the underlying Segment
     *                                  cannot be written properly.
     *                                  For example if swapping is
     *                                  required but the swap driver
     *                                  encounters an I/O error, a
     *                                  RecordOperationException
     *                                  will be thrown.
     *
     * @throws RecordSecurityException If the specified credentials
     *                                 are not permitted to read the
     *                                 specified Field from this
     *                                 StructuredRecord.
     */
    public abstract <VALUE extends Serializable>
                                   void writeValue (
                                                    Credentials credentials,
                                                    Attribute<VALUE> attribute,
                                                    VALUE value
                                                    )
        throws RecordOperationException,
               RecordSecurityException;
}
