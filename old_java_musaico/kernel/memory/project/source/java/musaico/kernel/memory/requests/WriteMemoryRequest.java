package musaico.kernel.memory.requests;

import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.i18n.Internationalized;

import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.TimeoutException;

import musaico.i18n.message.Message;

import musaico.io.Identifier;

import musaico.kernel.memory.MemoryException;
import musaico.kernel.memory.MemoryRequest;

import musaico.region.Position;
import musaico.region.Region;

import musaico.security.Credentials;

import musaico.time.AbsoluteTime;
import musaico.time.RelativeTime;


/**
 * <p>
 * A request to a Segment or other memory request handler to
 * write to the memory starting at a specific Position from
 * a specific Buffer covering a specific Region of the Buffer.
 * </p>
 *
 * <p>
 * This request is used, for example, to asynchronously write
 * to a virtual memory Segment from a user space Buffer.
 * </p>
 *
 * <p>
 * The response to a WriteMemoryRequest is the actual Region
 * of the source buffer that was written out to memory.
 * This Region might be smaller than the region requested,
 * if the memory target ran out of space to write Fields.
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
public class WriteMemoryRequest
    extends AbstractMemoryRequest<Region>
    implements Serializable
{
    /** The starting position of the memory area to which data
     *  will be copied. */
    private final Position memoryPosition;

    /** The buffer from which the request handler will copy data. */
    private final Buffer writeFromBuffer;

    /** The region of the writeFromBuffer from which data will be copied. */
    private final Region writeFromRegion;


    /**
     * <p>
     * Creates a new WriteMemoryRequest to be fulfilled by the
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
     * @param memory_position The position at which to start
     *                        writing Fields into memory.
     *                        Must not be null.  Must be a valid
     *                        Position within the memory's Region
     *                        of Fields.
     *
     * @param write_from_buffer The Buffer from which data will be
     *                          copied into memory.  Must not be null.
     *
     * @param write_from_region The Region of the Buffer from which
     *                          data will be copied into memory.
     *                          Must not be null.  Must be a valid
     *                          Region of Fields for the specified
     *                          Buffer.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public WriteMemoryRequest (
                               Credentials credentials,
                               Identifier target_id,
                               RelativeTime timeout,
                               Position memory_position,
                               Buffer write_from_buffer,
                               Region write_from_region
                               )
        throws I18nIllegalArgumentException
    {
        super ( credentials, target_id, timeout );

        if ( memory_position == null
             || write_from_buffer == null
             || write_from_region == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a WriteMemoryRequest for credentials [%credentials%] target id [%target_id%] with timeout [%timeout%] memory position [%memory_position%] write from buffer [%write_from_buffer%] write from region [%write_from_region%]",
                                                     "target_id", target_id,
                                                     "timeout", timeout,
                                                     "memory_position", memory_position,
                                                     "write_from_buffer", write_from_buffer,
                                                     "write_from_region", write_from_region );
        }

        this.memoryPosition = memory_position;
        this.writeFromBuffer = write_from_buffer;
        this.writeFromRegion = write_from_region;
    }


    /**
     * <p>
     * Creates a new WriteMemoryRequest to be fulfilled by the
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
     * @param memory_position The position at which to start
     *                        writing Fields into memory.
     *                        Must not be null.  Must be a valid
     *                        Position within the memory's Region
     *                        of Fields.
     *
     * @param write_from_buffer The Buffer from which data will be
     *                          copied into memory.  Must not be null.
     *
     * @param write_from_region The Region of the Buffer from which
     *                          data will be copied into memory.
     *                          Must not be null.  Must be a valid
     *                          Region of Fields for the specified
     *                          Buffer.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public WriteMemoryRequest (
                               Credentials credentials,
                               Identifier target_id,
                               AbsoluteTime timeout_time,
                               Position memory_position,
                               Buffer write_from_buffer,
                               Region write_from_region
                               )
        throws I18nIllegalArgumentException
    {
        super ( credentials, target_id, timeout_time );

        if ( memory_position == null
             || write_from_buffer == null
             || write_from_region == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a WriteMemoryRequest for credentials [%credentials%] target id [%target_id%] with timeout_time [%timeout_time%] memory position [%memory_position%] write from buffer [%write_from_buffer%] write from region [%write_from_region%]",
                                                     "target_id", target_id,
                                                     "timeout_time", timeout_time,
                                                     "memory_position", memory_position,
                                                     "write_from_buffer", write_from_buffer,
                                                     "write_from_region", write_from_region );
        }

        this.memoryPosition = memory_position;
        this.writeFromBuffer = write_from_buffer;
        this.writeFromRegion = write_from_region;
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
     * Returns the Region of the Buffer from which the handler
     * will write data.
     * </p>
     *
     * @return The Region of the Buffer which will be read from
     *         by the memory request handler.  Never null.
     */
    public Region writeFromRegion ()
    {
        return this.writeFromRegion;
    }


    /**
     * <p>
     * Returns the Buffer from which the handler will write data.
     * </p>
     *
     * @return The Buffer which will be read from by
     *         the memory request handler.  Never null.
     */
    public Buffer writeFromBuffer ()
    {
        return this.writeFromBuffer;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "write to " + this.targetRef ()
            + " position " + this.memoryPosition ()
            + " from buffer " + this.writeFromBuffer ()
            + " region " + this.writeFromRegion ();
    }
}
