package musaico.kernel_modules.objectsystems.gnosys;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.buffer.SimpleBuffer;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Progress;
import musaico.io.Reference;

import musaico.io.progresses.SimpleProgress;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.memory.KernelPaging;
import musaico.kernel.memory.Page;
import musaico.kernel.memory.PagedArea;
import musaico.kernel.memory.PageTable;
import musaico.kernel.memory.PageTableException;
import musaico.kernel.memory.Segment;
import musaico.kernel.memory.SegmentSecurityException;
import musaico.kernel.memory.SwapException;
import musaico.kernel.memory.SwapState;
import musaico.kernel.memory.SwapSystem;

import musaico.kernel.module.Module;

import musaico.kernel.objectsystem.MetadataBuilder;
import musaico.kernel.objectsystem.ONodeIdentifier;
import musaico.kernel.objectsystem.ONodeOperations;
import musaico.kernel.objectsystem.ONodeOperationException;
import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordTypeIdentifier;
import musaico.kernel.objectsystem.SuperBlock;

import musaico.kernel.objectsystems.onodes.SimpleONode;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Size;

import musaico.security.Security;


/**
 * <p>
 * An ONode for the gnosys object system.  Follows a fairly simple
 * fixed structure.  All metadata is stored with the super block,
 * not with the individual ONode.
 * </p>
 *
 *
 * <p>
 * Because object systems can conceivably be distributed, every ONode
 * must be Serializable in order to play nicely over RMI.
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
 * Copyright (c) 2011 Johann Tienhaara
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
public class GnONode
    extends SimpleONode
{
    /** The Module which provides us with credentials and
     *  kernel access. */
    private final Module module;


    /**
     * <p>
     * Creates a new GnONode with the specified mode (readable,
     * executable, writable, and so on).
     * </p>
     *
     * <p>
     * This ONode does NOT fill in any metadata.  That is left
     * to the caller, and this ONode's operations methods.
     * </p>
     *
     * @param module The module which provides this ONode access
     *               to the kernel and other kernel modules.
     *               Must not be null.
     *
     * @param super_block The SuperBlock which created this ONode.
     *                    Must not be null.
     *
     * @param id The unique identifier of this ONode within the SuperBlock.
     *           Must not be null.
     *
     * @param ops The operations for this ONode.  Must not be null.
     *
     * @param security The security manager for this ONode.
     *                 For example, UNIX-like security.
     *                 Must not be null.
     *
     * @param data The segment and paged area backing this ONode.
     *             The segment's Security restricts this ONode's
     *             internal operations, whereas the ONode's own
     *             Security is used to restrict the outside world.
     *             Must not be null.
     *
     * @param metadata The metadata builder for this ONode.
     *                 Keeps the state of all times (created time,
     *                 modified time) and other metadata for
     *                 the ONode, and builds snapshots whenever asked.
     *                 Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid (see above).
     */
    public GnONode (
                    Module module,
                    SuperBlock super_block,
                    ONodeIdentifier id,
                    ONodeOperations ops,
                    Security<RecordFlag> security,
                    Segment data,
                    MetadataBuilder metadata
                    )
        throws I18nIllegalArgumentException
    {
        super ( module,
                super_block,
                id,
                ops,
                security,
                data,
                metadata );

        this.module = module;
    }


    /**
     * <p>
     * Marks all the in-memory pages of this GnONode as dirty.
     * </p>
     *
     * <p>
     * Package-private method.
     * </p>
     *
     * <p>
     * Be sure to own a mutex lock on this GnONode before calling.
     * </p>
     *
     * @throws ONodeOperationException If the underlying
     *                                 to mark all the in-memory
     *                                 paged area fails to mark pages
     *                                 dirty.
     */
    void dirty ()
        throws ONodeOperationException
    {
        final PagedArea paged_area;
        try
        {
            paged_area =
                this.data ().pagedArea ( this.module.credentials () );
        }
        catch ( SegmentSecurityException e )
        {
            throw new ONodeOperationException ( e );
        }

        PageTable page_table = paged_area.pageTable ();
        SwapSystem swap_system = paged_area.swapSystem ();
        SwapState [] swapped_in_states = swap_system.inSwapStates ();
        try
        {
            page_table.dirtyAll ( this.metadata ().region(),
                                  swapped_in_states );
        }
        catch ( PageTableException e )
        {
            throw new ONodeOperationException ( "ONode [%onode%] failed to mark all in-memory pages dirty",
                                                "cause", e );
        }
    }


    /**
     * <p>
     * Returns true if this GnONode has any dirty pages,
     * false if all pages are clean.
     * </p>
     *
     * <p>
     * Package-private method.
     * </p>
     *
     * <p>
     * Be sure to own a mutex lock on this GnONode before calling.
     * </p>
     *
     * @return True if any Page in this GnONode is dirty;
     *         false if all pages are clean.
     */
    boolean isDirty ()
    {
        final PagedArea paged_area;
        try
        {
            paged_area =
                this.data ().pagedArea ( this.module.credentials () );
        }
        catch ( SegmentSecurityException e )
        {
            // !!!
            return false;
        }

        PageTable page_table = paged_area.pageTable ();
        SwapSystem swap_system = paged_area.swapSystem ();
        SwapState [] swapped_in_states = swap_system.inSwapStates ();

        final Region dirty_region;
        try
        {
            dirty_region =
                page_table.dirtyRegion ( this.metadata ().region (),
                                         swapped_in_states );
        }
        catch ( PageTableException e )
        {
            // !!! WHAT TO DO...
            return false;
        }

        boolean is_dirty = false;
        for ( Position position : dirty_region )
        {
            // At least one of our pages is dirty.
            is_dirty = true;
            break;
        }

        return is_dirty;
    }


    /**
     * <p>
     * Writes out any and all dirty pages.
     * </p>
     *
     * <p>
     * This is a blocking call.
     * </p>
     *
     * <p>
     * Package-private method.
     * </p>
     *
     * <p>
     * Be sure to own a mutex lock on this GnONode before calling.
     * </p>
     */
    void sync ()
        throws ONodeOperationException
    {
        final PagedArea paged_area;
        try
        {
            paged_area =
                this.data ().pagedArea ( this.module.credentials () );
        }
        catch ( SegmentSecurityException e )
        {
            throw new ONodeOperationException ( e );
        }

        SwapSystem swap_system = paged_area.swapSystem ();
        Region region = this.metadata ().region ();
        // Keep swapping out until there's nothing left to swap out.
        int num_iterations = 0;
        while ( true )
        {
            num_iterations ++;

            final Page [] in_pages;
            try
            {
                in_pages =
                    paged_area.pageTable ().pages ( region );
            }
            catch ( PageTableException e )
            {
                throw new ONodeOperationException ( "ONode [%onode%] could not retrieve pages for region [%region%] from page table [%page_table%]",
                                                    "onode", this,
                                                    "region", region,
                                                    "page_table", paged_area.pageTable (),
                                                    "cause", e );
            }

            List<Page> swapped_out_pages = new ArrayList<Page> ();
            for ( int ip = 0; ip < in_pages.length; ip ++ )
            {
                Page in_page = in_pages [ ip ];
                Position in_start = in_page.region ().start ();
                Position in_end   = in_page.region ().end ();

                KernelPaging kernel_paging = in_page.kernelPaging ();
                if ( kernel_paging.isDirty ( in_page ) )
                {
                    SwapState swap_state_in = in_page.swapState ();
                    if ( swap_system.isSwapOutable ( swap_state_in ) )
                    {
                        // Don't just swap out one level, swap out all the way
                        // to the most-swapped-out state.
                        SwapState swap_state_out =
                            swap_system.outFrom ( swap_state_in );

                        final boolean is_dirty_out_page;
                        if ( swap_system.isSwapOutable ( swap_state_out ) )
                        {
                            // Mark every out page as dirty so that
                            // it will be swapped out to the next
                            // swap state.
                            is_dirty_out_page = true;
                        }
                        else
                        {
                            // This is the most swapped-out state.
                            is_dirty_out_page = false;
                        }

                        Position out_start = in_start;
                        while ( out_start.compareTo ( in_end ) <= 0 )
                        {
                            Page out_page = null;
                            try
                            {
                                out_page =
                                    paged_area.createPage ( swap_state_out,
                                                            out_start );

                                paged_area.synchronize ( in_page, out_page );
                            }
                            catch ( SwapException e )
                            {
                                throw new ONodeOperationException ( "ONode [%onode%] could not sync in page [%in_page%] to out_page [%out_page%]",
                                                                    "onode", this,
                                                                    "in_page", in_page,
                                                                    "out_page", out_page,
                                                                    "cause", e );
                            }

                            swapped_out_pages.add ( out_page );

                            if ( is_dirty_out_page )
                            {
                                // Mark this swapped-out page as dirty.
                                kernel_paging.dirty ( out_page );
                            }

                            Size out_size = out_page.region ().size ();
                            out_start = out_size.stepFrom ( out_start, 0L );
                        }
                    }
                }
            }

            // If we didn't swap anything out, we're done.
            if ( swapped_out_pages.size () == 0 )
            {
                break;
            }
            else if ( num_iterations >= 256 )
            {
                // Infinite loop prevention.
                System.out.println ( "!!! FAILED MISERABLY TO SWAP PROPERLY AFTER 256 ITERATIONS!!!" );
                break;
            }

            // Now replace all the swapped-in pages with the swapped-out
            // pages.
            Page [] template = new Page [ swapped_out_pages.size () ];
            Page [] out_pages = swapped_out_pages.toArray ( template );
            try
            {
                paged_area.pageTable ().put ( out_pages );
            }
            catch ( PageTableException e )
            {
                throw new ONodeOperationException ( "ONode [%onode%] could not put swapped-out pages [%out_pages%] into page table [%page_table%]",
                                                    "onode", this,
                                                    "out_pages", out_pages,
                                                    "page_table", paged_area.pageTable (),
                                                    "cause", e );
            }

            // Loop and swap out all the pages we can.
        }
    }


    /**
     * <p>
     * Truncates the pages in this GnONode.
     * </p>
     *
     * <p>
     * Package-private method.
     * </p>
     *
     * <p>
     * Be sure to own a mutex lock on this GnONode before calling.
     * </p>
     */
    void truncate ()
    {
        // !!! NOT YET IMPLEMENTED;
        System.out.println ( "!!! GnONode truncate not implemented yet" );
    }
}
