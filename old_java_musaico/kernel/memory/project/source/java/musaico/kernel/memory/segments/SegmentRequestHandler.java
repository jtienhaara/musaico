package musaico.kernel.memory.segments;

import java.io.Serializable;


import musaico.field.Field;

import musaico.i18n.Internationalized;

import musaico.i18n.exceptions.TimeoutException;

import musaico.i18n.message.Message;

import musaico.kernel.memory.MemoryException;
import musaico.kernel.memory.MemoryRequest;
import musaico.kernel.memory.MemoryRequestHandler;
import musaico.kernel.memory.Segment;
import musaico.kernel.memory.SegmentFlag;
import musaico.kernel.memory.SegmentPermission;
import musaico.kernel.memory.SegmentSecurityException;

import musaico.kernel.memory.paging.Page;
import musaico.kernel.memory.paging.PagedArea;
import musaico.kernel.memory.paging.PageTable;

import musaico.kernel.memory.requests.ReadFieldRequest;
import musaico.kernel.memory.requests.ReadMemoryRequest;
import musaico.kernel.memory.requests.ResizeMemoryRequest;
import musaico.kernel.memory.requests.WriteFieldRequest;
import musaico.kernel.memory.requests.WriteMemoryRequest;

import musaico.region.Region;

import musaico.security.Permissions;
import musaico.security.SimplePermissions;

import musaico.time.AbsoluteTime;


/**
 * <p>
 * Handles MemoryRequests to a Segment, such as read/write/resize.
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
public class SegmentRequestHandler
    implements MemoryRequestHandler, Serializable
{
    /** Synchronize critical sections on this lock: */
    private final Serializable lock = new String ();

    /** The Segment on which this request handler operates. */
    private final Segment segment;

    /** The private PagedArea of the Segment on which this request
     *  handler operates.  Can change over time, for example if
     *  the Segment gets mmap()ed. */
    private PagedArea pagedArea;


    /**
     * <p>
     * Creates a new SegmentRequestHandler for the specified
     * Segment and its private PagedArea.
     * </p>
     *
     * <p>
     * Whenever a read, write, resize or other request is received
     * by the Segment, it should queue it up, and then ask this
     * handler to execute one request at a time, in a sensible order
     * (for example reading in a large chunk of memory first, then
     * reading sub-chunks of it).
     * </p>
     *
     * @param segment The Segment whose requests this handler executes.
     *                Must not be null.
     *
     * @param paged_area The current private PagedArea for the Segment.
     *                   Must not be null.  Whenever the PagedArea
     *                   changes (for example during mmap), the
     *                   Segment must update this handler with the
     *                   new private PagedArea.
     */
    public SegmentRequestHandler (
                                  Segment segment,
                                  PagedArea paged_area
                                  )
    {
        this.segment = segment;
        this.pagedArea = paged_area;
    }


    /**
     * @see musaico.kernel.memory.MemoryRequestHandler#handleRequest(musaico.kernel.memory.MemoryRequest)
     */
    public <RESPONSE extends Serializable>
        void handleRequest (
                            MemoryRequest<RESPONSE> request
                            )
    {
        final PagedArea paged_area;
        synchronized ( this.lock )
        {
            paged_area = this.pagedArea;
        }

        AbsoluteTime now = AbsoluteTime.now ();

        try
        {
            if ( now.compareTo ( request.timeoutTime () ) >= 0 )
            {
                // The request has already timed out.
                TimeoutException timeout_exception =
                    new TimeoutException ( "Failed to complete [%memory_request_class%] [%memory_request%] before [%timeout_time%]",
                                           "memory_request_class", request.getClass (),
                                           "memory_request", request,
                                           "timeout_time", request.timeoutTime () );
                request.failure ( timeout_exception );
                return;
            }

            if ( request instanceof ReadFieldRequest )
            {
                ReadFieldRequest read = (ReadFieldRequest) request;
                SegmentSecurityException security_exception =
                    this.securityException ( read,
                                             SegmentPermission.READ );
                if ( security_exception != null )
                {
                    read.failure ( security_exception );
                    return;
                }

                Field field = paged_area.readField ( read.credentials (),
                                                     read.memoryPosition () );
                read.success ( field );
            }
            else if ( request instanceof ReadMemoryRequest )
            {
                ReadMemoryRequest read = (ReadMemoryRequest) request;
                SegmentSecurityException security_exception =
                    this.securityException ( read,
                                             SegmentPermission.READ );
                if ( security_exception != null )
                {
                    read.failure ( security_exception );
                    return;
                }

                Region actual_region_read_into =
                    paged_area.read ( read.credentials (),
                                      read.memoryPosition (),
                                      read.readIntoBuffer (),
                                      read.readIntoRegion () );
                read.success ( actual_region_read_into );
            }
            else if ( request instanceof WriteFieldRequest )
            {
                WriteFieldRequest write = (WriteFieldRequest) request;
                SegmentSecurityException security_exception =
                    this.securityException ( write,
                                             SegmentPermission.WRITE );
                if ( security_exception != null )
                {
                    write.failure ( security_exception );
                    return;
                }

                paged_area.writeField ( write.credentials (),
                                        write.memoryPosition (),
                                        write.writeField () );
                write.success ( write.writeField () );
            }
            else if ( request instanceof WriteMemoryRequest )
            {
                WriteMemoryRequest write = (WriteMemoryRequest) request;
                SegmentSecurityException security_exception =
                    this.securityException ( write,
                                             SegmentPermission.WRITE );
                if ( security_exception != null )
                {
                    write.failure ( security_exception );
                    return;
                }

                Region actual_region_written_from =
                    paged_area.write ( write.credentials (),
                                       write.memoryPosition (),
                                       write.writeFromBuffer (),
                                       write.writeFromRegion () );
                write.success ( actual_region_written_from );
            }
            else if ( request instanceof ResizeMemoryRequest )
            {
                ResizeMemoryRequest resize = (ResizeMemoryRequest) request;
                SegmentSecurityException security_exception =
                    this.securityException ( resize,
                                             SegmentPermission.RESIZE );
                if ( security_exception != null )
                {
                    resize.failure ( security_exception );
                    return;
                }

                Region actual_resized_region =
                    paged_area.resize ( resize.credentials (),
                                        resize.resizeRegion () );
                resize.success ( actual_resized_region );
            }
            else
            {
                MemoryException memory_exception =
                    new MemoryException ( "SegmentRequestHandler for segment [%segment_id%] does not know how to handle [%memory_request_class%] [%memory_request%]",
                                          "segment_id", this.segment.id (),
                                          "memory_request_class", request.getClass (),
                                          "memory_request", request );
            }
        }
        catch ( Throwable t )
        {
            // Maybe it's an I18n exception, maybe not.
            try
            {
                Internationalized<Message,String> i18n_exception =
                    (Internationalized<Message,String>) t;
                request.failure ( i18n_exception );
            }
            catch ( ClassCastException cce )
            {
                // Need to create an internationalized exception.
                MemoryException me =
                    new MemoryException ( "Segment request [%memory_request_class%] [%memory_request%] failed",
                                          "memory_request_class", request.getClass (),
                                          "memory_request", request,
                                          "cause", t );

                request.failure ( me );
            }
        }
    }


    /**
     * <p>
     * Changes the PagedArea for the Segment.
     * </p>
     *
     * <p>
     * Called, for example, during mmap.
     * </p>
     *
     * @param new_paged_area The new private PagedArea for the
     *                       Segment.  Must not be null.
     */
    public void pagedArea (
                           PagedArea new_paged_area
                           )
    {
        synchronized ( this.lock )
        {
            this.pagedArea = new_paged_area;
        }
    }


    /**
     * <p>
     * Creates a SegmentSecurityException IF the Credentials behind
     * the specified MemoryRequest are not allowed the specified
     * SegmentPermission(s).
     * </p>
     */
    private <RESPONSE extends Serializable>
        SegmentSecurityException securityException (
                                                    MemoryRequest<RESPONSE> request,
                                                    SegmentFlag... permissions
                                                    )
    {
        Permissions<SegmentFlag> required_permissions =
            new SimplePermissions<SegmentFlag> ( request.credentials (),
                                                 this.segment.id (),
                                                 permissions );
        Permissions<SegmentFlag> granted_permissions =
            this.segment.security ().request ( required_permissions );

        if ( granted_permissions.isAllowed ( required_permissions ) )
        {
            // No exception, the Credentials are OK.
            return null;
        }

        // Credentials not given all permissions, so create
        // an exception for the caller.
        SegmentSecurityException security_exception =
            new SegmentSecurityException ( "Access denied: request [%memory_request%] from credentials [%credentials%] does not have [%permissions%]",
                                           "memory_request", request,
                                           "credentials", request.credentials (),
                                           "permissions", permissions );

        return security_exception;
    }
}
