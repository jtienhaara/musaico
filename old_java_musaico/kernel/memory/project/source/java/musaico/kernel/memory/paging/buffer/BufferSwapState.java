package musaico.kernel.memory.paging.buffer;

import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;

import musaico.kernel.memory.Memory;
import musaico.kernel.memory.MemoryException;

import musaico.kernel.memory.paging.KernelPaging;
import musaico.kernel.memory.paging.Page;
import musaico.kernel.memory.paging.SwapException;
import musaico.kernel.memory.paging.SwapState;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Size;
import musaico.region.Space;

import musaico.security.Credentials;


/**
 * <p>
 * The in-memory region of a virtual memory manager.
 * </p>
 *
 * <p>
 * The BufferSwapState is about as simple as it gets,
 * and typically forms the "swapped-in" memory for a
 * Musaico kernel.
 * </p>
 *
 *
 * <p>
 * In Java every SwapState must be Serializable in order to
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
public class BufferSwapState
    implements SwapState, Serializable
{
    /** The unique identifier for this swap state. */
    private final Reference id;

    /** The physical memory we use to allocate in-memory Buffers. */
    private final Memory physicalMemory;

    /** The size of each Page created by this swap state. */
    private final Size pageSize;

    /** The position/region/size Space used by the Pages created
     *  by this swap state. */
    private final Space space;

    /** The LRU and dirty/clean lists of all pages from all
     *  swap states for the kernel. */
    private final KernelPaging kernelPaging;


    /**
     * <p>
     * Creates a new BufferSwapState with the specified id
     * and page size.
     * </p>
     *
     * @param id The unique name for this swap state.
     *           Must not be null.
     *
     * @param physical_memory The memory to use for allocating
     *                        in-memory Buffers in this swap state.
     *                        Must not be null.
     *
     * @param kernel_paging The LRU and dirty/clean lists of
     *                      all Pages from all SwapStates for
     *                      the kernel.  Must not be null.
     *
     * @param page_size The size of each page that will be
     *                  created for paged areas using this
     *                  swap state.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public BufferSwapState (
                            Reference id,
                            Memory physical_memory,
                            KernelPaging kernel_paging,
                            Size page_size
                            )
    {
        if ( id == null
             || physical_memory == null
             || kernel_paging == null
             || page_size == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a BufferSwapState with id [%id%] physical memory [%physical_memory%] kernel paging [%kernel_paging%] page size [%page_size%]",
                                                     "id", id,
                                                     "physical_memory", physical_memory,
                                                     "kernel_paging", kernel_paging,
                                                     "page_size", page_size );
        }

        this.id = id;
        this.physicalMemory = physical_memory;
        this.pageSize = page_size;
        this.space = this.pageSize.space ();
        this.kernelPaging = kernel_paging;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapState#createPage(musaico.security.Credentials,musaico.region.Position,musaico.field.Field)
     */
    @Override
    public Page createPage (
                            Credentials credentials,
                            Position start_position,
			    Field swap_state_configuration
                            )
        throws I18nIllegalArgumentException,
               SwapException
    {
        if ( start_position == null
             || ! ( start_position.space ().equals ( this.space () ) ) )
        {
            throw new I18nIllegalArgumentException ( "SwapState [%swap_state%] cannot create a page at position [%start_position%]",
                                                     "swap_state", this,
                                                     "start_position", start_position );
        }

        Position end_position = this.space ().expr ( start_position )
            .add ( this.pageSize () )
            .subtract ( this.space ().one () ).position ();
        Region page_region =
            this.space ().region ( start_position, end_position );
        if ( end_position.equals ( this.space ().outOfBounds () )
             || ! page_region.size ().equals ( this.pageSize () ) )
        {
            throw new I18nIllegalArgumentException ( "SwapState [%swap_state%] cannot create a page at position [%start_position%]",
                                                     "swap_state", this,
                                                     "start_position", start_position );
        }

        final Page page;
        try
        {
            // Allocate a buffer of physical memory and wrap it in
            // a page.
            page = new SimpleBufferPage ( credentials,
                                          this.physicalMemory,
                                          this.kernelPaging (),
                                          this, // swap state
                                          page_region );
        }
        catch ( MemoryException e )
        {
            throw new SwapException ( e );
        }

        return page;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null
                  || ! ( obj instanceof BufferSwapState ) )
        {
            return false;
        }

        BufferSwapState that = (BufferSwapState) obj;
        if ( ! that.id ().equals ( this.id () )
             || ! that.kernelPaging ().equals ( this.kernelPaging () )
             || ! that.pageSize ().equals ( this.pageSize () )
             || ! that.space ().equals ( this.space () ) )
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode ()
    {
        return this.id ().hashCode () + this.pageSize ().hashCode ();
    }


    /**
     * @see musaico.kernel.memory.paging.SwapState#id()
     */
    @Override
    public Reference id ()
    {
        return this.id;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapState#kernelPaging()
     */
    @Override
    public KernelPaging kernelPaging ()
    {
        return this.kernelPaging;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapState#pageSize()
     */
    @Override
    public Size pageSize ()
    {
        return this.pageSize;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapState#space()
     */
    @Override
    public Space space ()
    {
        return this.space;
    }
}
