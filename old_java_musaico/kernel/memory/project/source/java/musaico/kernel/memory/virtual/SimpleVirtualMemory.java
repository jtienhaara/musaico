package musaico.kernel.memory.virtual;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import musaico.i18n.exceptions.I18nIllegalArgumentException;
import musaico.i18n.exceptions.TimeoutException;

import musaico.io.Reference;

import musaico.buffer.Buffer;
import musaico.buffer.BufferException;
import musaico.buffer.BufferTools;
import musaico.buffer.SimpleBuffer;

import musaico.field.Field;
import musaico.field.FieldTypingEnvironment;

import musaico.io.references.UUIDReference;

import musaico.kernel.memory.Memory;
import musaico.kernel.memory.MemoryException;
import musaico.kernel.memory.MemoryIdentifier;
import musaico.kernel.memory.MemoryPermission;
import musaico.kernel.memory.MemoryRequest;
import musaico.kernel.memory.Segment;
import musaico.kernel.memory.SegmentFactory;
import musaico.kernel.memory.SegmentIdentifier;
import musaico.kernel.memory.SegmentSecurity;

import musaico.kernel.memory.requests.ResizeMemoryRequest;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Size;

import musaico.security.Credentials;
import musaico.security.Security;

import musaico.time.RelativeTime;


/**
 * <p>
 * A simple virtual memory manager, with no constraints on memory usage
 * and no re-use of freed buffers.  The Segments created by the
 * SegmentFactory are assumed to take care of constraints.  Therefore
 * the SegmentFactory should be constructed with permissions in mind
 * (such as one SegmentFactory per type of user space segments, one
 * per type of kernel space segments, and so on).
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
 * Copyright (c) 2009, 2010, 2011, 2012 Johann Tienhaara
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
public class SimpleVirtualMemory
    implements VirtualMemory, Serializable
{
    /** Synchronize critical sections on this token: */
    private final Serializable lock = new String ();

    /** The unique identifier for this memory inside the kernel. */
    private final MemoryIdentifier id;

    /** The security manager for this memory, controls who
     *  can and can't allocate or free memory. */
    private final Security<MemoryPermission> security;

    /** The Buffers which are currently in use. */
    private final List<Buffer> allocatedBuffers;

    /** The Field typing environment for this virtual memory.
     *  Controls casting between field types and so on for the
     *  buffers allocated by this memory. */
    private final FieldTypingEnvironment environment;

    /** The factory which creates the Segments and PagedAreas
     *  underlying this virtual memory. */
    private final SegmentFactory segmentFactory;

    /** The security which will be given to each Segment we create. */
    private final SegmentSecurity segmentSecurity;


    /**
     * <p>
     * Creates a new SimpleVirtualMemory for the specified memory
     * SegmentFactory.
     * </p>
     *
     * @param id The unique identifier for this memory.
     *           Must not be null.
     *
     * @param security The security manager for this memory.
     *                 Controls who can and who can't allocate
     *                 or free memory.  Must not be null.
     *
     * @param environment The kernel's Field typing environment,
     *                    which controls casting between Field types.
     *                    Must not be null.  Can be specific to this
     *                    Memory, though usually it is shared between
     *                    trusted kernel modules.
     *
     * @param segment_factory The factory which creates Segments of memory
     *                        for us, each of which controls access
     *                        to the PagedArea and its underlying Pages.
     *
     * @param segment_security Security for each segment we create,
     *                         such as copy-on-write, read-only, and so on.
     *                         Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters are
     *                                      invalid (see above).
     */
    public SimpleVirtualMemory (
                                MemoryIdentifier id,
                                Security<MemoryPermission> security,
                                FieldTypingEnvironment environment,
                                SegmentFactory segment_factory,
                                SegmentSecurity segment_security
                                )
        throws I18nIllegalArgumentException
    {
        if ( id == null
             || security == null
             || environment == null
             || segment_factory == null
             || segment_security == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleVirtualMemory with id [%id%] security [%security%] environment [%environment%] segment factory [%segment_factory%] segment security [%segment_security%]",
                                                     "id", id,
                                                     "security", security,
                                                     "environment", environment,
                                                     "segment_factory", segment_factory,
                                                     "segment_security", segment_security );
        }

        this.id = id;
        this.security = security;
        this.allocatedBuffers = new ArrayList<Buffer> ();
        this.environment = environment;
        this.segmentFactory = segment_factory;
        this.segmentSecurity = segment_security;
    }


    /**
     * @see musaico.kernel.memory.Memory#allocate(musaico.security.Credentials,musaico.region.Region)
     */
    @Override
    public Buffer allocate (
                            Credentials credentials,
                            Region region
                            )
        throws MemoryException
    {
        if ( credentials == null
             || region == null )
        {
            throw new I18nIllegalArgumentException ( "Memory [%memory%] cannot allocate Buffer for region [%region%] to credentials [%credentials%]",
                                                     "memory", this,
                                                     "region", region,
                                                     "credentials", credentials );
        }

        SegmentIdentifier id = new SegmentIdentifier ();

        Buffer buffer;
        synchronized ( this.lock )
        {
            Segment segment =
                this.segmentFactory.createSegment ( id,
                                                    this.segmentSecurity,
                                                    credentials );

            MemoryRequest<Region> resize_segment =
                new ResizeMemoryRequest ( credentials,
                                          segment.id (),
                                          new RelativeTime ( 1L, 0L ), // 1s.!!!
                                          region );
            try
            {
                segment.request ( resize_segment ).waitFor ();
            }
            catch ( MemoryException e )
            {
                // Something went horribly wrong resizing the
                // Segment's PagedArea.
                throw e;
            }
            catch ( TimeoutException e )
            {
                // Took too long.
                throw new MemoryException ( e );
            }

            // Register the Segment in the kernel.
            // !!!!!!!!!!!!

            // Now that the segment has been sized and registered with
            // the kernel, create the virtual buffer.
            buffer = new VirtualBuffer ( this.environment,
                                         segment, // contains the pages.
                                         credentials );

            this.allocatedBuffers.add ( buffer );
        }

        return buffer;
    }


    /**
     * @see musaico.kernel.memory.Memory#environment()
     */
    @Override
    public FieldTypingEnvironment environment ()
    {
        return this.environment;
    }


    /**
     * @see musaico.kernel.memory.Memory#free(musaico.security.Credentials,musaico.buffer.Buffer)
     */
    @Override
    public void free (
                      Credentials credentials,
                      Buffer buffer
                      )
        throws MemoryException
    {
        if ( buffer == null )
        {
            throw new I18nIllegalArgumentException ( "Memory [%memory%] cannot free buffer [%buffer%]",
                                                     "memory", this,
                                                     "buffer", buffer );
        }

        synchronized ( this.lock )
        {
            int buffer_index = this.allocatedBuffers.indexOf ( buffer );
            if ( buffer_index < 0 )
            {
                throw new MemoryException ( "Memory [%memory%] cannot free buffer [%buffer%]: No such buffer in this memory",
                                            "memory", this,
                                            "buffer", buffer );
            }

            this.allocatedBuffers.remove ( buffer_index );

            VirtualBuffer virtual_buffer = (VirtualBuffer) buffer;
            virtual_buffer.freeSegment ();
        }
    }


    /**
     * @see musaico.kernel.memory.Memory#id()
     */
    @Override
    public MemoryIdentifier id ()
    {
        return this.id;
    }


    /**
     * @see musaico.kernel.memory.Memory#security()
     */
    @Override
    public Security<MemoryPermission> security ()
    {
        return this.security;
    }
}
