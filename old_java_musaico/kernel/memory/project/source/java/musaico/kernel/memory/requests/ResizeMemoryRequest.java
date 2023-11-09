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
 * change its size, either expanding or shrinking the Region
 * of Fields it houses.
 * </p>
 *
 * <p>
 * This request is used, for example, to allocate the size
 * of Segment backing a VirtualBuffer.
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
public class ResizeMemoryRequest
    extends AbstractMemoryRequest<Region>
    implements Serializable
{
    /** The Region of Fields to be covered by the memory after
     *  resizing. */
    private final Region resizeRegion;


    /**
     * <p>
     * Creates a new ResizeMemoryRequest to be fulfilled by the
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
     * @param resize_region The Region of Fields to resize the memory to.
     *                      Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public ResizeMemoryRequest (
                                Credentials credentials,
                                Identifier target_id,
                                RelativeTime timeout,
                                Region resize_region
                                )
        throws I18nIllegalArgumentException
    {
        super ( credentials, target_id, timeout );

        if ( resize_region == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a ResizeMemoryRequest for credentials [%credentials%] target id [%target_id%] with timeout [%timeout%] resize region [%resize_region%]",
                                                     "target_id", target_id,
                                                     "timeout", timeout,
                                                     "resize_region", resize_region );
        }

        this.resizeRegion = resize_region;
    }




    /**
     * <p>
     * Creates a new ResizeMemoryRequest to be fulfilled by the
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
     * @param resize_region The Region of Fields to resize the memory to.
     *                      Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public ResizeMemoryRequest (
                                Credentials credentials,
                                Identifier target_id,
                                AbsoluteTime timeout_time,
                                Region resize_region
                                )
        throws I18nIllegalArgumentException
    {
        super ( credentials, target_id, timeout_time );

        if ( resize_region == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a ResizeMemoryRequest for credentials [%credentials%] target id [%target_id%] with timeout time [%timeout_time%] resize region [%resize_region%]",
                                                     "target_id", target_id,
                                                     "timeout_time", timeout_time,
                                                     "resize_region", resize_region );
        }

        this.resizeRegion = resize_region;
    }


    /**
     * <p>
     * Returns the Region of Fields which the handler will resize its
     * memory to fit.
     * </p>
     *
     * @return The Region to resize to.  Must not be null.
     */
    public Region resizeRegion ()
    {
        return this.resizeRegion;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "resize " + this.targetRef ()
            + " to " + this.resizeRegion ();
    }
}
