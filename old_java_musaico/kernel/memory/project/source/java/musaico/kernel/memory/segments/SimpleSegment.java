package musaico.kernel.memory.segments;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;
import musaico.io.ReferenceCount;
import musaico.io.SimpleReferenceCount;

import musaico.kernel.KernelObjectException;
import musaico.kernel.KernelObjectsRegistry;

import musaico.kernel.memory.MemoryException;
import musaico.kernel.memory.MemoryRequest;
import musaico.kernel.memory.MemoryRequestHandler;
import musaico.kernel.memory.MemoryRequestListener;
import musaico.kernel.memory.Segment;
import musaico.kernel.memory.SegmentFlag;
import musaico.kernel.memory.SegmentIdentifier;
import musaico.kernel.memory.SegmentOperationException;
import musaico.kernel.memory.SegmentPermissions;
import musaico.kernel.memory.SegmentSecurity;
import musaico.kernel.memory.SegmentSecurityException;

import musaico.kernel.memory.paging.Page;
import musaico.kernel.memory.paging.PagedArea;
import musaico.kernel.memory.paging.PagedAreaIdentifier;
import musaico.kernel.memory.paging.PageTable;
import musaico.kernel.memory.paging.PageTableException;
import musaico.kernel.memory.paging.SwapException;
import musaico.kernel.memory.paging.SwapOperation;
import musaico.kernel.memory.paging.SwapState;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Size;
import musaico.region.Space;

import musaico.security.Credentials;
import musaico.security.Permissions;
import musaico.security.SecurityException;


/**
 * <p>
 * A basic implementation of the Segment region of memory.
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
 * Copyright (c) 2010, 2012 Johann Tienhaara
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
public class SimpleSegment
    implements Segment, Serializable
{
    /** Lock critical sections on this token: */
    private final Serializable lock = new String ();

    /** This Segment's unique identifier, distinguishing it
     *  from all other Segments in this kernel. */
    private final SegmentIdentifier id;

    /** The lookup of kernel objects, so that we can find PagedAreas
     *  whenever we are told to switch (for example during mmap). */
    private final KernelObjectsRegistry kernelObjectsRegistry;

    /** A count of objects referring to this segment. */
    private final ReferenceCount referenceCount = new SimpleReferenceCount ();

    /** The paged area (including swapper) to which
     *  this Segment belongs.  This can change (for example
     *  when a segment of swap-backed memory is mmap'ed). */
    private PagedArea pagedArea;

    /** Security for this segment (such as copy-on-write,
     *  read-only, and so on). */
    private final SegmentSecurity security;

    /** This segment's owner. */
    private final Credentials owner;

    /** The listener (typically a kernel Task) which we wake up whenever
     *  someone queues up a request to read, write, resize, and so on. */
    private final MemoryRequestListener requestsQueueListener;

    /** The queue of MemoryRequests awaiting execution. */
    private final List<MemoryRequest<?>> requestsQueue =
        new ArrayList<MemoryRequest<?>> ();

    /** The handler which actually reads or writes or resizes us
     *  whenever we ask it to. */
    private final SegmentRequestHandler requestHandler;


    /**
     * <p>
     * Creates a new SimpleSegment with the specified identifier
     * and PagedArea.
     * </p>
     *
     * @param id The unique identifier for this Segment.
     *           Must not be null.  Must be unique throughout
     *           the kernel in which this Segment resides.
     *
     * @param kernel_objects_registry The lookup of kernel objects, so
     *                                that this Segment can find
     *                                PagedAreas whenever it is told
     *                                to switch (for example during
     *                                an mmap operation).  Must not be null.
     *
     * @param paged_area The paged area which backs this
     *                   Segment, providing swapping-in and
     *                   swapping-out of this segment's Pages.
     *                   Must not be null.
     *
     * @param security The security settings and permissions framework
     *                 for this Segment.  Must not be null.
     *
     * @param owner The user or module which owns this Segment.
     *              These Credentials are used for all kernel operations
     *              performed by the segment.  Must not be null.
     *
     * @param requests_task The listener which this Segment notifies any
     *                      time a request is made.  Typically the listener
     *                      is a kernel Task which wakes up when we
     *                      notify it of the request.  Must not be null.
     */
    public SimpleSegment (
                          SegmentIdentifier id,
                          KernelObjectsRegistry kernel_objects_registry,
                          PagedArea paged_area,
                          SegmentSecurity security,
                          Credentials owner,
                          MemoryRequestListener requests_task
                          )
    {
        if ( id == null
             || kernel_objects_registry == null
             || paged_area == null
             || security == null
             || owner == null
             || requests_task == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleSegment with id [%id%] KernelObjectsRegistry [%kernel_objects_registry%] PagedArea [%paged_area%] security [%security%] owner [%owner%] requests task [%requests_task%]",
                                                     "id", id,
                                                     "kernel_objects_registry", kernel_objects_registry,
                                                     "paged_area", paged_area,
                                                     "security", security,
                                                     "owner", owner,
                                                     "requests_task", requests_task );
        }

        this.id = id;
        this.kernelObjectsRegistry = kernel_objects_registry;
        this.pagedArea = paged_area;
        this.security = security;
        this.owner = owner;
        this.requestsQueueListener = requests_task;

        this.requestHandler =
            new SegmentRequestHandler ( this, this.pagedArea );

        this.requestsQueueListener.registerSegment ( this );
    }


    /**
     * @see musaico.kernel.memory.Segment#close(musaico.security.Credentials)
     */
    @Override
    public void close (
                       Credentials credentials
                       )
        throws I18nIllegalArgumentException,
               SegmentOperationException
    {
        if ( credentials == null )
        {
            throw new I18nIllegalArgumentException ( "Segment [%segment%] cannot be closed by credentials [%credentials%]",
                                                     "segment", this.id (),
                                                     "credentials", credentials );
        }

        // Check permissions first.
        Permissions<SegmentFlag> requested_permissions =
            new SegmentPermissions ( credentials,
                                     this.id (),
                                     SegmentFlag.ALLOW_CLOSE );
        Permissions<SegmentFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SegmentSecurityException ( "Access denied: segment [%segment%] cannot be closed by credentials [%credentials%]",
                                                 "segment", this,
                                                 "credentials", credentials );
        }

        if ( this.referenceCount.decrement () < 0L )
        {
            throw new SegmentOperationException ( "Cannot close segment [%segment%]: Already closed",
                                                  "segment", this );
        }
    }


    /**
     * @see musaico.kernel.memory.Segment#free()
     */
    @Override
    public void free ()
    {
        this.requestsQueueListener.unregisterSegment ( this );
    }


    /**
     * @see musaico.kernel.memory.Segment#handleOneRequest()
     */
    @Override
    public void handleOneRequest ()
    {
        final MemoryRequest<?> request;
        synchronized ( this.lock )
        {
            if ( this.requestsQueue.isEmpty () )
            {
                // Nothing to do.
                return;
            }

            request = this.requestsQueue.remove ( 0 );
        }

        this.requestHandler.handleRequest ( request );
    }


    /**
     * @see musaico.kernel.memory.Segment#id()
     */
    @Override
    public final SegmentIdentifier id ()
    {
        return this.id;
    }


    /**
     * @see musaico.kernel.memory.Segment#isRequestWaiting()
     */
    @Override
    public boolean isRequestWaiting ()
    {
        synchronized ( this.lock )
        {
            if ( ! this.requestsQueue.isEmpty () )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }


    /**
     * @see musaico.kernel.memory.Segment#open(musaico.security.Credentials)
     */
    @Override
    public void open (
                      Credentials credentials
                      )
        throws I18nIllegalArgumentException,
               SegmentOperationException
    {
        if ( credentials == null )
        {
            throw new I18nIllegalArgumentException ( "Segment [%segment%] cannot be opened by credentials [%credentials%]",
                                                     "segment", this.id (),
                                                     "credentials", credentials );
        }

        // Check permissions first.
        Permissions<SegmentFlag> requested_permissions =
            new SegmentPermissions ( credentials,
                                     this.id (),
                                     SegmentFlag.ALLOW_OPEN );
        Permissions<SegmentFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SegmentSecurityException ( "Access denied: segment [%segment%] cannot be opened by credentials [%credentials%]",
                                                 "segment", this,
                                                 "credentials", credentials );
        }

        this.referenceCount.increment ();
    }


    /**
     * @see musaico.kernel.memory.Segment#pagedArea(musaico.security.Credentials,musaico.io.Identifier)
     */
    @Override
    public PagedAreaIdentifier pagedArea (
                                          Credentials credentials,
                                          Identifier paged_area_uncast_id
                                          )
        throws I18nIllegalArgumentException,
               SegmentOperationException
    {
        if ( credentials == null
             || paged_area_uncast_id == null
             || ! ( paged_area_uncast_id instanceof PagedAreaIdentifier ) )
        {
            throw new I18nIllegalArgumentException ( "Segment [%segment%] cannot change paged area to [%paged_area_id%] for credentials [%credentials%]",
                                                     "segment", this,
                                                     "paged_area_id", paged_area_uncast_id );
        }

        PagedAreaIdentifier paged_area_id = (PagedAreaIdentifier)
            paged_area_uncast_id;

        // Check permissions first.
        Permissions<SegmentFlag> requested_permissions =
            new SegmentPermissions ( credentials,
                                     this.id (),
                                     SegmentFlag.ALLOW_CHANGE_PAGED_AREA );
        Permissions<SegmentFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SegmentSecurityException ( "Access denied: segment [%segment%] cannot change paged areas from [%old_paged_area_id%] to [%new_paged_area_id%] for credentials [%credentials%]",
                                                 "segment", this,
                                                 "old_paged_area_id", this.pagedArea.id (),
                                                 "new_paged_area_id", paged_area_id,
                                                 "credentials", credentials );
        }

        final PagedArea new_paged_area;
        try
        {
            // We do the lookup by our owner, not by the credentials
            // asking us to change.  We have already verified that
            // the caller is allowed to ask us to do our work.  Now
            // we must do the work with our own credentials.
            new_paged_area =
                this.kernelObjectsRegistry.get ( this.owner,
                                                 paged_area_id );
        }
        catch ( SecurityException e )
        {
            throw new SegmentSecurityException ( "Access denied: segment [%segment%] cannot change paged areas from [%old_paged_area_id%] to [%new_paged_area_id%] for credentials [%credentials%]",
                                                 "segment", this,
                                                 "old_paged_area_id", this.pagedArea.id (),
                                                 "new_paged_area_id", paged_area_id,
                                                 "credentials", credentials,
                                                 "cause", e );
        }
        catch ( KernelObjectException e )
        {
            throw new SegmentSecurityException ( "Access denied: segment [%segment%] cannot change paged areas from [%old_paged_area_id%] to [%new_paged_area_id%] for credentials [%credentials%]",
                                                 "segment", this,
                                                 "old_paged_area_id", this.pagedArea.id (),
                                                 "new_paged_area_id", paged_area_id,
                                                 "credentials", credentials,
                                                 "cause", e );
        }

        // Now change the paged area.
        final PagedAreaIdentifier old_paged_area_id;
        synchronized ( this.lock )
        {
            old_paged_area_id = this.pagedArea.id ();
            this.pagedArea = new_paged_area;

            // Update the MemoryRequestHandler, too, so that
            // it works on the correct paged area.
            this.requestHandler.pagedArea ( this.pagedArea );
        }

        return old_paged_area_id;
    }


    /**
     * @see musaico.kernel.memory.Segment#pagedAreaRef()
     */
    @Override
    public PagedAreaIdentifier pagedAreaRef ()
    {
        return this.pagedArea.id ();
    }


    /**
     * @see musaico.kernel.memory.Segment#request()
     */
    @Override
    public <RESPONSE extends Serializable>
        MemoryRequest<RESPONSE> request (
                                         MemoryRequest<RESPONSE> request
                                         )
    {
        synchronized ( this.lock )
        {
            this.requestsQueue.add ( request );
        }

        this.requestsQueueListener.requestNotification ( request );

        return request;
    }


    /**
     * @see musaico.kernel.memory.Segment#region()
     */
    @Override
    public Region region ()
    {
        return this.pagedArea.pageTable ().region ();
    }


    /**
     * @see musaico.kernel.memory.Segment#security()
     */
    @Override
    public SegmentSecurity security ()
    {
        return this.security;
    }


    /**
     * @see musaico.kernel.memory.Segment#space()
     */
    @Override
    public Space space ()
    {
        return this.pagedArea.pageTable ().space ();
    }
}
