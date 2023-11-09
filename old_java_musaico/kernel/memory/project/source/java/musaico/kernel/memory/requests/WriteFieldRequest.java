package musaico.kernel.memory.requests;

import java.io.Serializable;


import musaico.field.Field;

import musaico.i18n.Internationalized;

import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.TimeoutException;

import musaico.i18n.message.Message;

import musaico.io.Identifier;

import musaico.kernel.memory.MemoryException;
import musaico.kernel.memory.MemoryRequest;

import musaico.region.Position;

import musaico.security.Credentials;

import musaico.time.AbsoluteTime;
import musaico.time.RelativeTime;


/**
 * <p>
 * A request to a Segment or other memory request handler to
 * write a single Field to the memory at a specific Position.
 * </p>
 *
 * <p>
 * This request is used, for example, to write one Field
 * to a virtual memory Segment during user access to a VirtualBuffer.
 * </p>
 *
 * <p>
 * The response of a WriteFieldRequest is simply the Field that
 * was written to memory.
 * </p>
 *
 *
 * <p>
 * In Java every MemoryRequest must be Serializable in order to
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
public class WriteFieldRequest
    extends AbstractMemoryRequest<Field>
    implements Serializable
{
    /** The position of the memory area to which data
     *  will be copied. */
    private final Position memoryPosition;

    /** The field to write into the memory area. */
    private final Field writeField;


    /**
     * <p>
     * Creates a new WriteFieldRequest to be fulfilled by the
     * specified target within the specified relative time.
     * </p>
     *
     * @param credentials The Credentials of the requester.
     *                    Typically the memory request handler
     *                    will do some initial security checks,
     *                    and these Credentials will also be
     *                    used for sub-operations such as swapping.
     *                    Must not be null.
     *
     * @param target_id The id of the memory request handler
     *                  which will fulfill this request.
     *                  For example, the id of a Segment.
     *                  Must not be null.
     *
     * @param timeout Amount of time which the handler has to complete
     *                this request, relative to right now.  Must not
     *                be null.
     *
     * @param memory_position The position at which to write the Field.
     *                        Must not be null.  Must be a valid
     *                        Position within the memory's Region
     *                        of Fields.
     *
     * @param write_field The Field to write to memory.
     *                    Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public WriteFieldRequest (
                              Credentials credentials,
                              Identifier target_id,
                              RelativeTime timeout,
                              Position memory_position,
                              Field write_field
                              )
        throws I18nIllegalArgumentException
    {
        super ( credentials, target_id, timeout );

        if ( memory_position == null
             || write_field == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a WriteFieldRequest for credentials [%credentials%] target id [%target_id%] with timeout [%timeout%] memory position [%memory_position%] write field [%write_field%]",
                                                     "target_id", target_id,
                                                     "timeout", timeout,
                                                     "memory_position", memory_position,
                                                     "write_field", write_field );
        }

        this.memoryPosition = memory_position;
        this.writeField = write_field;
    }


    /**
     * <p>
     * Creates a new WriteFieldRequest to be fulfilled by the
     * specified target within the specified relative time.
     * </p>
     *
     * @param credentials The Credentials of the requester.
     *                    Typically the memory request handler
     *                    will do some initial security checks,
     *                    and these Credentials will also be
     *                    used for sub-operations such as swapping.
     *                    Must not be null.
     *
     * @param target_id The id of the memory request handler
     *                  which will fulfill this request.
     *                  For example, the id of a Segment.
     *                  Must not be null.
     *
     * @param timeout_time Time at which this request will timeout.
     *                     Must not be null.
     *
     * @param memory_position The position at which to write
     *                        the field to memory.
     *                        Must not be null.  Must be a valid
     *                        Position within the memory's Region
     *                        of Fields.
     *
     * @param write_field The Field which will be written to memory.
     *                    Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public WriteFieldRequest (
                              Credentials credentials,
                              Identifier target_id,
                              AbsoluteTime timeout_time,
                              Position memory_position,
                              Field write_field
                              )
        throws I18nIllegalArgumentException
    {
        super ( credentials, target_id, timeout_time );

        if ( memory_position == null
             || write_field == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a WriteFieldRequest for credentials [%credentials%] target id [%target_id%] with timeout_time [%timeout_time%] memory position [%memory_position%] write field [%write_field%]",
                                                     "target_id", target_id,
                                                     "timeout_time", timeout_time,
                                                     "memory_position", memory_position,
                                                     "write_field", write_field );
        }

        this.memoryPosition = memory_position;
        this.writeField = write_field;
    }


    /**
     * <p>
     * Returns the Position at which to start writing
     * data into memory.
     * </p>
     *
     * @return The Position from which to start writing Fields
     *         into memory.  Never null.
     */
    public Position memoryPosition ()
    {
        return this.memoryPosition;
    }


    /**
     * <p>
     * Returns the Field which will be written to memory.
     * </p>
     *
     * @return The Field to write to the memory area.
     *         Never null.
     */
    public Field writeField ()
    {
        return this.writeField;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "write to " + this.targetRef ()
            + " position " + this.memoryPosition ();
    }
}
