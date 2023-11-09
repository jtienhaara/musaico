package musaico.kernel.memory.segments;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.kernel.KernelObjectsRegistry;

import musaico.kernel.memory.MemoryException;
import musaico.kernel.memory.MemoryRequestListener;
import musaico.kernel.memory.Segment;
import musaico.kernel.memory.SegmentFactory;
import musaico.kernel.memory.SegmentIdentifier;
import musaico.kernel.memory.SegmentSecurity;

import musaico.kernel.memory.paging.KernelPaging;
import musaico.kernel.memory.paging.PagedArea;
import musaico.kernel.memory.paging.PagedAreaIdentifier;
import musaico.kernel.memory.paging.PageTable;
import musaico.kernel.memory.paging.SwapConfiguration;
import musaico.kernel.memory.paging.Swapper;
import musaico.kernel.memory.paging.SwapSystem;

import musaico.kernel.memory.paging.pagedareas.SimplePagedArea;

import musaico.kernel.memory.paging.pagetables.SimplePageTable;

import musaico.security.Credentials;


/**
 * <p>
 * Creates SimpleSegments and SimplePagedAreas upon request.
 * </p>
 *
 *
 * <p>
 * In Java, every SegmentFactory must be Serializable in order to
 * play nicely over RMI, even if the segments it creates contain
 * non-serializable data.
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
 * Copyright (c) 2011, 2012 Johann Tienhaara
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
public class SimpleSegmentFactory
    implements SegmentFactory, Serializable
{
    /** The lookup of kernel objects, so that the segments can find
     *  PagedAreas during mmap operations. */
    private final KernelObjectsRegistry kernelObjectsRegistry;

    /** The Paging manager for the entire kernel. */
    private final KernelPaging kernelPaging;

    /** The swap system, with all swap states for use
     *  by this segment factory.  Can be shared between
     *  segment factories and segements. */
    private final SwapSystem swapSystem;

    /** The settings for each swap state, such as the specific
     *  block driver for a swapped-out-to-block-driver swap state. */
    private final SwapConfiguration swapConfiguration;

    /** The kernel Task which needs to be woken up every time
     *  Segments need their request queues purged. */
    private final MemoryRequestListener memoryRequestListener;


    /**
     * <p>
     * Creates a new SimpleSegmentFactory with the specified KernelPaging
     * manager (shared with everyone else in the kernel).
     * </p>
     *
     * @param kernel_objects_registry The lookup of kernel objects,
     *                                so that segments created by this
     *                                factory can find PagedAreas
     *                                when they are told to switch
     *                                (for example during mmap operations).
     *                                Must not be null.
     *
     * @param kernel_paging The KernelPaging manager, which implements the
     *                      least-recently-used list, and so on.
     *                      Must not be null.
     *
     * @param swap_system The system of swap states and swappers
     *                    which the PagedArea will use
     *                    to create pages when swapping in or out.
     *                    Must not be null.
     *
     * @param swap_configuration The settings for the swap states, such
     *                           as the specific block driver for a
     *                           swapped-out-to-block-driver swap state.
     *                           Must not be null.
     *
     * @param memory_request_listener The listener which must be woken
     *                                up every time a Segment is asked
     *                                to fulfill a request.  Typically
     *                                a kernel Task.  Must not be null.
     */
    public SimpleSegmentFactory (
                                 KernelObjectsRegistry kernel_objects_registry,
                                 KernelPaging kernel_paging,
                                 SwapSystem swap_system,
				 SwapConfiguration swap_configuration,
                                 MemoryRequestListener memory_request_listener
                                 )
    {
        if ( kernel_objects_registry == null
             || kernel_paging == null
             || swap_system == null
	     || swap_configuration == null
             || memory_request_listener == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleSegmentFactory with KernelObjectsRegistry [%kernel_objects_registry%] KernelPaging [%kernel_paging%] SwapSystem [%swap_system%] swap configuration [%swap_configuration%] memory request listener [%memory_request_listener%]",
                                                     "kernel_objects_registry", kernel_objects_registry,
                                                     "kernel_paging", kernel_paging,
                                                     "swap_system", swap_system,
						     "swap_configuration", swap_configuration,
                                                     "memory_request_listener", memory_request_listener );
        }

        this.kernelObjectsRegistry = kernel_objects_registry;
        this.kernelPaging = kernel_paging;
        this.swapSystem = swap_system;
	this.swapConfiguration = swap_configuration;
        this.memoryRequestListener = memory_request_listener;
    }


    /**
     * @see musaico.kernel.memory.SegmentFactory#createSegment(musaico.kernel.memory.SegmentIdentifier,musaico.kernel.memory.SegmentSecurity,musaico.security.Credentials)
     */
    public Segment createSegment (
                                  SegmentIdentifier id,
                                  SegmentSecurity security,
                                  Credentials owner
                                  )
        throws I18nIllegalArgumentException,
               MemoryException
    {
        if ( id == null
             || security == null
             || owner == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a segment with id [%id%] security [%security%] owner [%owner%]",
                                                     "id", id,
                                                     "security", security,
                                                     "owner", owner );
        }

        // Create a page table for the paged area to lookup its
        // own pages.
        PageTable page_table =
            new SimplePageTable ( this.swapSystem );

        // Create the paged area with a UUID.
        PagedAreaIdentifier paged_area_id =
            new PagedAreaIdentifier ();
        PagedArea paged_area = new SimplePagedArea ( paged_area_id,
                                                     this.swapSystem,
						     this.swapConfiguration,
                                                     this.kernelPaging,
                                                     page_table );

        // Create the requested segment.
        Segment segment = new SimpleSegment ( id,
                                              this.kernelObjectsRegistry,
                                              paged_area,
                                              security,
                                              owner,
                                              this.memoryRequestListener );

        return segment;
    }
}
