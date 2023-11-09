package musaico.kernel.memory.paging.buffer;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.ReferenceCount;
import musaico.io.SimpleReferenceCount;

import musaico.buffer.Buffer;

import musaico.kernel.memory.Memory;
import musaico.kernel.memory.MemoryException;
import musaico.kernel.memory.MemorySecurityException;

import musaico.kernel.memory.paging.KernelPaging;
import musaico.kernel.memory.paging.SwapState;

import musaico.region.Region;

import musaico.security.Credentials;


/**
 * <p>
 * A nothing-fancy implementation of BufferPage,
 * a Page of in-memory Fields.
 * </p>
 *
 * <p>
 * The Buffer of Fields (raw memory) can be retrieved and
 * manipulated through this Page.  However this Buffer
 * should never be accessed directly by Tasks.  Instead,
 * a VirtualBuffer layer should straddle one or more
 * Pages, so that this Page can be swapped out without
 * anyone ever maintaining a reference directly to the
 * Buffer.
 * </p>
 *
 * @see musaico.kernel.memory.paging.pagedareas
 *
 * <p>
 * In Java, all Pages must be Serializable.  Note, however,
 * that a Page may contain non-Serializable data.
 * (For example, a buffer Page with non-Serializable Fields.)
 * Therefore great care must be taken when passing Pages
 * back and forth across RMI.
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
 * Copyright (c) 2009, 2010, 2011 Johann Tienhaara
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
public class SimpleBufferPage
    implements BufferPage, Serializable
{
    /** Security credentials for allocating and freeing memory. */
    private final Credentials credentials;

    /** The Memory manager for this Page, for allocating, freeing,
     *  and so on. */
    private final Memory memory;

    /** The KernelPaging manager for this Page, including LRU, clean
     *  and dirty lists, and so on. */
    private final KernelPaging kernelPaging;

    /** The count of references to this Page. */
    private final ReferenceCount referenceCount =
        new SimpleReferenceCount ();

    /** The state of this Page, which depends on the paged
     *  area in which this page resides.  Often (but not
     *  necessarily) SwapStates.SWAPPED_IN_TO_FIELDS. */
    private final SwapState swapState;

    /** The raw in-memory Buffer of Fields is wrapped by
     *  a page access wrapper which updates the kernel's LRU
     *  every time a field is read, and also dirties the page
     *  every time a field is written.  The underlying buffer passed
     *  can be retrieved with <code> this.buffer.raw () </code>. */
    private final PageAccessBufferWrapper buffer;


    /**
     * <p>
     * Creates a new SimpleBufferPage with the specified Buffer.
     * </p>
     *
     * @param credentials The credentials to use for security
     *                    clearance when allocating or freeing
     *                    memory, such as module or user credentials.
     *                    Must not be null.
     *
     * @param memory The Memory manager for this Page, which
     *               controls allocation and freeing.
     *               Must not be null.
     *
     * @param kernel_paging The KernelPaging manager for this Page, providing
     *                      the least-recently-used (LRU) list, clean
     *                      and dirty page lists, and so on.
     *                      Must not be null.
     *
     * @param swap_state The swap state of this buffer page, such
     *                   as SWAPPED_IN_TO_FIELDS.  Must not be null.
     *
     * @param buffer The in-memory Buffer of Fields represented
     *               by this SimpleBufferPage.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     *
     * @throws MemorySecurityException If the specified credentials
     *                                 are not allowed to allocate
     *                                 physical memory.
     *
     * @throws MemoryException If the underlying memory cannot
     *                         allocate the requested memory
     *                         (for example because the system is
     *                         out of physical memory).
     */
    public SimpleBufferPage (
                             Credentials credentials,
                             Memory memory,
                             KernelPaging kernel_paging,
                             SwapState swap_state,
                             Region region
                             )
        throws I18nIllegalArgumentException,
               MemorySecurityException,
               MemoryException
    {
        if ( credentials == null
             || memory == null
             || kernel_paging == null
             || swap_state == null
             || region == null
             || ! region.space ().equals ( swap_state.space () ) )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleBufferPage with Credentials [%credentials%] Memory [%memory%] KernelPaging [%kernel_paging%] SwapState [%swap_state%] Region [%region%]",
                                                     "memory", memory,
                                                     "kernel_paging", kernel_paging,
                                                     "swap_state", swap_state,
                                                     "region", region );
        }

        this.credentials = credentials;
        this.memory = memory;
        this.kernelPaging = kernel_paging;
        this.swapState = swap_state;

        Buffer raw_buffer = this.memory.allocate ( this.credentials,
                                                   region );
        this.buffer =
            new PageAccessBufferWrapper ( raw_buffer,
                                          this.kernelPaging,
                                          this );
    }


    /**
     * @see musaico.kernel.memory.paging.buffer.BufferPage#buffer()
     */
    public final Buffer buffer ()
    {
        // Move this page to the end of the LRU.
        this.kernelPaging ().recent ( this );

        return this.buffer;
    }


    /**
     * @see musaico.kernel.memory.paging.Page#free()
     */
    public final void free ()
        throws MemoryException
    {
        this.kernelPaging ().remove ( this );
        this.memory ().free ( this.credentials, this.buffer.raw () );
    }


    /**
     * <p>
     * Returns the Memory manager which controls the allocation,
     * freeing and LRU of this SimpleBufferPage.
     * </p>
     *
     * @return This Page's parent Memory.  Never null.
     */
    public final Memory memory ()
    {
        return this.memory;
    }


    /**
     * @see musaico.kernel.memory.paging.Page#kernelPaging()
     */
    public final KernelPaging kernelPaging ()
    {
        return this.kernelPaging;
    }


    /**
     * @see musaico.kernel.memory.paging.Page#referenceCount()
     */
    public final ReferenceCount referenceCount ()
    {
        return this.referenceCount;
    }


    /**
     * @see musaico.kernel.memory.paging.Page#region()
     */
    public final Region region ()
    {
        // Just return the region describing the underlying Buffer.
        return this.buffer.region ();
    }


    /**
     * @see musaico.kernel.memory.paging.Page#swapState()
     */
    public final SwapState swapState ()
    {
        return this.swapState;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "BufferPage(" + this.buffer + ")"
            + " region " + this.region ();
    }
}
