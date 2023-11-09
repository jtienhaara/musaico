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
 * read a single Field from memory at a specific Position.
 * </p>
 *
 * <p>
 * This request is used, for example, to read
 * from a virtual memory Segment during a user call
 * to VirtualBuffer.
 * </p>
 *
 * <p>
 * The response of a ReadFieldRequest is the requested Field
 * content from memory.
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
public class ReadFieldRequest
    extends AbstractMemoryRequest<Field>
    implements Serializable
{
    /** The position of the memory area from which to read
     *  the Field. */
    private final Position memoryPosition;


    /**
     * <p>
     * Creates a new ReadFieldRequest to be fulfilled by the
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
     * @param memory_position The position at which to read the
     *                        Field from memory.
     *                        Must not be null.  Must be a valid
     *                        Position within the memory's Region
     *                        of Fields.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public ReadFieldRequest (
                             Credentials credentials,
                             Identifier target_id,
                             RelativeTime timeout,
                             Position memory_position
                             )
        throws I18nIllegalArgumentException
    {
        super ( credentials, target_id, timeout );

        if ( memory_position == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a ReadFieldRequest for credentials [%credentials%] target id [%target_id%] with timeout [%timeout%] memory position [%memory_position%]",
                                                     "target_id", target_id,
                                                     "timeout", timeout,
                                                     "memory_position", memory_position );
        }

        this.memoryPosition = memory_position;
    }


    /**
     * <p>
     * Creates a new ReadFieldRequest to be fulfilled by the
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
     * @param memory_position The position at which to read the
     *                        Field from memory.
     *                        Must not be null.  Must be a valid
     *                        Position within the memory's Region
     *                        of Fields.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public ReadFieldRequest (
                             Credentials credentials,
                             Identifier target_id,
                             AbsoluteTime timeout_time,
                             Position memory_position
                             )
        throws I18nIllegalArgumentException
    {
        super ( credentials, target_id, timeout_time );

        if ( memory_position == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a ReadFieldRequest for credentials [%credentials%] target id [%target_id%] with timeout_time [%timeout_time%] memory position [%memory_position%]",
                                                     "target_id", target_id,
                                                     "timeout_time", timeout_time,
                                                     "memory_position", memory_position );
        }

        this.memoryPosition = memory_position;
    }


    /**
     * <p>
     * Returns the Position at which to read the Field from memory.
     * </p>
     *
     * @return The Position at which to read the Field from memory.
     *         Never null.
     */
    public Position memoryPosition ()
    {
        return this.memoryPosition;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "read from " + this.targetRef ()
            + " position " + this.memoryPosition ();
    }
}
